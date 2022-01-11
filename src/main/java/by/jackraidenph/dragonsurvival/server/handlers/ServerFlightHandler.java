package by.jackraidenph.dragonsurvival.server.handlers;

import by.jackraidenph.dragonsurvival.client.handlers.ClientFlightHandler;
import by.jackraidenph.dragonsurvival.common.DragonEffects;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.misc.DragonType;
import by.jackraidenph.dragonsurvival.network.NetworkHandler;
import by.jackraidenph.dragonsurvival.network.flight.SyncFlyingStatus;
import by.jackraidenph.dragonsurvival.network.flight.SyncSpinStatus;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;

/**
 * Used in pair with {@link ClientFlightHandler}
 */
@Mod.EventBusSubscriber()
@SuppressWarnings("unused")
public class ServerFlightHandler {

    /**
     * Sets the fall damage based on flight speed and dragon's size
     */
    @SubscribeEvent
    public static void changeFallDistance(LivingFallEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
		double flightSpeed = event.getDistance();
        
        DragonStateProvider.getCap(livingEntity).ifPresent(dragonStateHandler -> {
            if(dragonStateHandler.isDragon() && dragonStateHandler.hasWings()) {
	            if (!ConfigHandler.SERVER.enableFlightFallDamage.get()) {
		            event.setCanceled(true);
	            }
				
	            if (flightSpeed <= 2 || dragonStateHandler.isWingsSpread() && !livingEntity.isSprinting() && flightSpeed <= 4) {
		            event.setCanceled(true);
		            return;
	            }
	
	            if (livingEntity.isPassenger() && DragonStateProvider.isDragon(livingEntity.getVehicle())) {
		            event.setCanceled(true);
					return;
	            }
				
	
	            EffectInstance effectinstance = livingEntity.getEffect(Effects.JUMP);
	            float f = effectinstance == null ? 0.0F : (float)(effectinstance.getAmplifier() + 1);
				
	            double damage = livingEntity.getDeltaMovement().lengthSqr() * (dragonStateHandler.getSize() / 20);
	            damage = MathHelper.clamp(damage, 0, livingEntity.getHealth() - (ConfigHandler.SERVER.lethalFlight.get() ? 0 : 1));
				
				if(!livingEntity.level.isClientSide && dragonStateHandler.isWingsSpread()) {
					event.setDistance((float)Math.floor(((damage + 3.0F + f) / event.getDamageMultiplier())));
				}
				
				if(!livingEntity.level.isClientSide) {
					if (ConfigHandler.SERVER.foldWingsOnLand.get()) {
						if (dragonStateHandler.isWingsSpread()) {
							dragonStateHandler.setWingsSpread(false);
							NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> livingEntity), new SyncFlyingStatus(livingEntity.getId(), false));
						}
					}
				}
            }
        });
    }
    
	@SubscribeEvent
	public static void foldWings(PlayerTickEvent tickEvent){
		PlayerEntity player = tickEvent.player;
		if(tickEvent.phase ==  Phase.START || !DragonStateProvider.isDragon(player) || player.level.isClientSide) return;
		if(!ConfigHandler.SERVER.foldWingsOnLand.get()) return;
		
		DragonStateHandler dragonStateHandler = DragonStateProvider.getCap(player).orElse(null);
		if(dragonStateHandler != null){
			if(dragonStateHandler.hasFlown && player.isOnGround()){
				if(dragonStateHandler.isWingsSpread() && player.isCreative()) {
					dragonStateHandler.hasFlown = false;
					dragonStateHandler.setWingsSpread(false);
                    NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SyncFlyingStatus(player.getId(), false));
                }
			}else{
				if(!dragonStateHandler.hasFlown && isFlying(player)){
					dragonStateHandler.hasFlown = true;
				}
			}
		}
	}
	@SubscribeEvent
	public static void playerFlightIcon(TickEvent.PlayerTickEvent playerTickEvent) {
		if(playerTickEvent.phase == Phase.START) return;
		PlayerEntity player = playerTickEvent.player;
		DragonStateProvider.getCap(player).ifPresent(handler -> {
			if (handler.isDragon()) {
				if(player.tickCount % 10 == 0) {
					if (handler.isWingsSpread()) {
						switch (handler.getType()) {
							case SEA:
								player.addEffect(new EffectInstance(DragonEffects.sea_wings, 500));
								break;
							
							case CAVE:
								player.addEffect(new EffectInstance(DragonEffects.cave_wings, 500));
								break;
							
							case FOREST:
								player.addEffect(new EffectInstance(DragonEffects.forest_wings, 500));
								break;
						}
					} else {
						if (player.hasEffect(DragonEffects.sea_wings)) player.removeEffect(DragonEffects.sea_wings);
						if (player.hasEffect(DragonEffects.cave_wings)) player.removeEffect(DragonEffects.cave_wings);
						if (player.hasEffect(DragonEffects.forest_wings)) player.removeEffect(DragonEffects.forest_wings);
					}
				}
			}
		});
	}
	
	
	
	@SubscribeEvent
	public static void playerFlightAttacks(TickEvent.PlayerTickEvent playerTickEvent) {
		if(playerTickEvent.phase == Phase.START) return;
		PlayerEntity player = playerTickEvent.player;
		DragonStateProvider.getCap(player).ifPresent(handler -> {
			if(handler.isDragon()) {
				if(handler.getMovementData().spinAttack > 0){
					if(!isFlying(player) && !canSwimSpin(player)){
						if(!player.level.isClientSide){
							handler.getMovementData().spinAttack = 0;
							NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SyncSpinStatus(player.getId(), handler.getMovementData().spinAttack, handler.getMovementData().spinCooldown, handler.getMovementData().spinLearned));
						}
					}
				}
				
				if(isSpin(player)){
					int range = 5;
					List<Entity> entities = player.level.getEntities(null, new AxisAlignedBB(player.position().x - range, player.position().y - range, player.position().z - range, player.position().x + range, player.position().y + range, player.position().z + range));
					entities.removeIf((e) -> e.distanceTo(player) > range);
					entities.remove(player);
					entities.removeIf((e) -> e instanceof PlayerEntity && !player.canHarmPlayer((PlayerEntity)e));
					
					for(Entity ent : entities){
						if(player.hasPassenger(ent)) continue;
						if(ent instanceof LivingEntity){
							LivingEntity entity = (LivingEntity)ent;
							
							//Dont hit the same mob multiple times
							if(entity.getLastHurtByMob() == player && entity.getLastHurtByMobTimestamp() <= entity.tickCount + 5 * 20){
								continue;
							}
						}
						player.attack(ent);
					}
					
					handler.getMovementData().spinAttack--;
					
					if(!player.level.isClientSide){
						NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SyncSpinStatus(player.getId(), handler.getMovementData().spinAttack, handler.getMovementData().spinCooldown, handler.getMovementData().spinLearned));
					}
					
				}else if(handler.getMovementData().spinCooldown > 0){
					if(!player.level.isClientSide){
						handler.getMovementData().spinCooldown--;
						NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SyncSpinStatus(player.getId(), handler.getMovementData().spinAttack, handler.getMovementData().spinCooldown, handler.getMovementData().spinLearned));
					}
					
				}
			}
		});
	}
	
    @SubscribeEvent
    public static void playerFoodExhaustion(TickEvent.PlayerTickEvent playerTickEvent) {
        if(playerTickEvent.phase == Phase.START) return;
        PlayerEntity player = playerTickEvent.player;
		
        DragonStateProvider.getCap(player).ifPresent(dragonStateHandler -> {
            if(dragonStateHandler.isDragon()) {
                boolean wingsSpread = dragonStateHandler.isWingsSpread();
                if(ConfigHandler.SERVER.creativeFlight.get() && !player.level.isClientSide){
                    if(player.abilities.flying != wingsSpread && (!player.isCreative() && !player.isSpectator())){
                        player.abilities.flying = wingsSpread;
                        player.onUpdateAbilities();
                    }
                }
                
                if (wingsSpread) {
                    if (ConfigHandler.SERVER.flyingUsesHunger.get()) {
                        if (isFlying(player)) {
							if(!player.level.isClientSide) {
								if (player.getFoodData().getFoodLevel() <= ConfigHandler.SERVER.foldWingsThreshold.get() && !ConfigHandler.SERVER.allowFlyingWithoutHunger.get() && !player.isCreative()) {
									player.sendMessage(new TranslationTextComponent("ds.wings.nohunger"), player.getUUID());
									dragonStateHandler.setWingsSpread(false);
									NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SyncFlyingStatus(player.getId(), false));
									return;
								}
							}
							
                            Vector3d delta = player.getDeltaMovement();
                            double l = delta.length();
                            if(delta.x == 0 && delta.z == 0){
                                l = 15;
                            }
                            float exhaustion = ConfigHandler.SERVER.creativeFlight.get() ? (player.abilities.flying ?  player.flyingSpeed : 0F) : (float)(0.005F * l);
                            player.causeFoodExhaustion(exhaustion);
                        }
                    }
                }
            }
        });
    }
	
	public static final int spinDuration = (int)Math.round(0.76 * 20);
	public static boolean isSpin(PlayerEntity entity){
		DragonStateHandler handler = DragonStateProvider.getCap(entity).orElse(null);
		
		if(handler != null){
			if(isFlying(entity) || canSwimSpin(entity)){
				if(handler.getMovementData().spinAttack > 0){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static boolean canSwimSpin(LivingEntity player){
		DragonStateHandler dragonStateHandler = DragonStateProvider.getCap(player).orElse(null);
		boolean validSwim = ((dragonStateHandler.getType() == DragonType.SEA || dragonStateHandler.getType() == DragonType.FOREST) && player.isInWater()) || (player.isInLava() && dragonStateHandler.getType() == DragonType.CAVE);
		return dragonStateHandler != null && validSwim && dragonStateHandler.hasWings() && !player.isOnGround();
	}
	
	public static boolean isFlying(LivingEntity player){
		DragonStateHandler dragonStateHandler = DragonStateProvider.getCap(player).orElse(null);
		return dragonStateHandler != null && dragonStateHandler.hasWings() && dragonStateHandler.isWingsSpread() && !player.isOnGround() && !player.isInWater() && !player.isInLava();
	}
	
	public static boolean isGliding(PlayerEntity player){
		DragonStateHandler dragonStateHandler = DragonStateProvider.getCap(player).orElse(null);
		boolean hasFood = player.getFoodData().getFoodLevel() > ConfigHandler.SERVER.flightHungerThreshold.get() || player.isCreative() || ConfigHandler.SERVER.allowFlyingWithoutHunger.get();
		return hasFood && player.isSprinting() && isFlying(player);
	}
	
	public static double getLandTime(PlayerEntity playerEntity, double goalTime)
	{
        if (isFlying(playerEntity)) {
	        Vector3d motion = playerEntity.getDeltaMovement();
	        BlockPos blockHeight = playerEntity.level.getHeightmapPos(Type.MOTION_BLOCKING, playerEntity.blockPosition());
	        int height = blockHeight.getY();
	        double aboveGround = Math.max(0, playerEntity.position().y - height);
	        double timeToGround = (aboveGround / Math.abs(motion.y));
	        if (playerEntity.fallDistance > 5 && motion.y < 0) {
		        if (aboveGround < 20 && timeToGround <= goalTime) {
			        return timeToGround;
		        }
	        }
        }
		
	    return -1;
	}
	
}
