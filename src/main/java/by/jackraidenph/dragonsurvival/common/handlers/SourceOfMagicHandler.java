package by.jackraidenph.dragonsurvival.common.handlers;

import by.jackraidenph.dragonsurvival.client.particles.DSParticles;
import by.jackraidenph.dragonsurvival.common.DragonEffects;
import by.jackraidenph.dragonsurvival.common.blocks.SourceOfMagicBlock;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.network.NetworkHandler;
import by.jackraidenph.dragonsurvival.network.status.SyncMagicSourceStatus;
import by.jackraidenph.dragonsurvival.server.tileentity.SourceOfMagicPlaceholder;
import by.jackraidenph.dragonsurvival.server.tileentity.SourceOfMagicTileEntity;
import by.jackraidenph.dragonsurvival.util.Functions;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Random;

@Mod.EventBusSubscriber
public class SourceOfMagicHandler
{
	@SubscribeEvent
	public static void playerTick(PlayerTickEvent event){
		if(event.phase == Phase.START || event.side == LogicalSide.CLIENT) return;
		PlayerEntity player = event.player;
		
		if(DragonStateProvider.isDragon(player)){
			DragonStateHandler handler = DragonStateProvider.getCap(player).orElse(null);
			
			if(handler != null){
				if(handler.getMagic().onMagicSource){
					if(!(player.getFeetBlockState().getBlock() instanceof SourceOfMagicBlock) || handler.getMovementData().bite || player.isCrouching() && handler.getMagic().magicSourceTimer > 40){
						handler.getMagic().onMagicSource = false;
						handler.getMagic().magicSourceTimer = 0;
						NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SyncMagicSourceStatus(player.getId(), false, 0));
						return;
					}
					
					BlockPos pos1 = player.blockPosition();
					TileEntity blockEntity = player.level.getBlockEntity(pos1);
					
					if(blockEntity instanceof SourceOfMagicPlaceholder){
						pos1 = ((SourceOfMagicPlaceholder)blockEntity).rootPos;
					}
					
					TileEntity sourceOfMagic = player.level.getBlockEntity(pos1);
					
					if(sourceOfMagic instanceof SourceOfMagicTileEntity){
						SourceOfMagicTileEntity tile = (SourceOfMagicTileEntity)sourceOfMagic;
						
						if(!tile.isEmpty()){
							if(handler.getType() == tile.type || player.isCreative() || ConfigHandler.SERVER.canUseAllSourcesOfMagic.get()) {
								if(ConfigHandler.SERVER.sourceOfMagicInfiniteMagic.get()) {
									if (handler.getMagic().magicSourceTimer >= Functions.secondsToTicks(10)) {
										handler.getMagic().magicSourceTimer = 0;
										Effect effect = DragonEffects.SOURCE_OF_MAGIC;
										EffectInstance effectInstance = player.getEffect(effect);
										int duration = SourceOfMagicTileEntity.consumables.get(tile.getItem(0).getItem());
										
										if (effectInstance == null) {
											player.addEffect(new EffectInstance(effect, duration));
										} else {
											player.addEffect(new EffectInstance(effect, effectInstance.getDuration() + duration));
										}
										
										tile.removeItem(0, 1);
									}else{
										handler.getMagic().magicSourceTimer++;
									}
								}
								
							}else{
								if(ConfigHandler.SERVER.damageWrongSourceOfMagic.get()) {
									if (player.tickCount % Functions.secondsToTicks(5) == 0) {
										player.hurt(DamageSource.MAGIC, 1F);
									}
								}
							}
						}else{
							handler.getMagic().magicSourceTimer = 0;
							handler.getMagic().onMagicSource = false;
							NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SyncMagicSourceStatus(player.getId(), false, 0));
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void playerParticles(PlayerTickEvent event){
		if(event.phase == Phase.START || event.side == LogicalSide.SERVER) return;
		PlayerEntity player = event.player;
		
		if(DragonStateProvider.isDragon(player)){
			DragonStateHandler handler = DragonStateProvider.getCap(player).orElse(null);
			
			if(handler != null){
				if(handler.getMagic().onMagicSource){
					BlockPos pos1 = player.blockPosition();
					TileEntity blockEntity = player.level.getBlockEntity(pos1);
					
					if(blockEntity instanceof SourceOfMagicPlaceholder){
						pos1 = ((SourceOfMagicPlaceholder)blockEntity).rootPos;
					}
					
					TileEntity sourceOfMagic = player.level.getBlockEntity(pos1);
					
					if(sourceOfMagic instanceof SourceOfMagicTileEntity){
						SourceOfMagicTileEntity tile = (SourceOfMagicTileEntity)sourceOfMagic;
						
						if(!tile.isEmpty()){
							if(handler.getType() == tile.type || player.isCreative() || ConfigHandler.SERVER.canUseAllSourcesOfMagic.get()) {
								if(ConfigHandler.SERVER.sourceOfMagicInfiniteMagic.get()) {
									if(player.level.isClientSide) {
										Minecraft minecraft = Minecraft.getInstance();
										Random random = player.level.random;
										double x = -1 + random.nextDouble() * 2;
										double z = -1 + random.nextDouble() * 2;
										
										switch (tile.type) {
											case SEA:
											case FOREST:
												if (!minecraft.isPaused()){
													player.level.addParticle(DSParticles.magicBeaconParticle, player.getX() + x, player.getY() + 0.5, player.getZ() + z, 0, 0, 0);
												}
												break;
											case CAVE:
												if (!minecraft.isPaused()){
													player.level.addParticle(DSParticles.fireBeaconParticle, player.getX() + x, player.getY() + 0.5, player.getZ() + z, 0, 0, 0);
												}
												break;
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@OnlyIn( Dist.CLIENT)
	@SubscribeEvent
	public static void playerTick(ClientTickEvent event){
		if(event.phase == Phase.START) return;
		PlayerEntity player = Minecraft.getInstance().player;
		
		if(DragonStateProvider.isDragon(player)){
			DragonStateHandler handler = DragonStateProvider.getCap(player).orElse(null);
			
			if(handler != null){
				if(handler.getMagic().onMagicSource){
					Vector3d velocity = player.getDeltaMovement();
					float groundSpeed = MathHelper.sqrt((velocity.x * velocity.x) + (velocity.z * velocity.z));
					if(Math.abs(groundSpeed) > 0.05){
						NetworkHandler.CHANNEL.sendToServer(new SyncMagicSourceStatus(player.getId(), false, 0));
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void playerAttacked(LivingHurtEvent event){
		LivingEntity entity = event.getEntityLiving();
		
		if(entity instanceof PlayerEntity){
			PlayerEntity player = (PlayerEntity)entity;
			
			if(!player.level.isClientSide) {
				DragonStateProvider.getCap(player).ifPresent(cap -> {
					if (cap.getMagic().onMagicSource) {
						cap.getMagic().onMagicSource = false;
						cap.getMagic().magicSourceTimer = 0;
						NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new SyncMagicSourceStatus(player.getId(), false, 0));
					}
				});
			}
		}
	}
}
