package by.jackraidenph.dragonsurvival.common.handlers;

import by.jackraidenph.dragonsurvival.common.DragonEffects;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.common.magic.DragonAbilities;
import by.jackraidenph.dragonsurvival.common.magic.abilities.Passives.CliffhangerAbility;
import by.jackraidenph.dragonsurvival.common.magic.common.DragonAbility;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.misc.DragonType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DragonBonusHandler
{
	@SubscribeEvent
    public static void dragonDamageImmunities(LivingAttackEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        DamageSource damageSource = event.getSource();
        DragonStateProvider.getCap(livingEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
				if(ConfigHandler.SERVER.bonuses.get()){
					if (dragonStateHandler.getType() == DragonType.CAVE && ConfigHandler.SERVER.caveFireImmunity.get()){
						if(damageSource.isFire() && ConfigHandler.SERVER.caveFireImmunity.get()){
							event.setCanceled(true);
						}
						
					}else if(dragonStateHandler.getType() == DragonType.FOREST){
						if(damageSource == DamageSource.SWEET_BERRY_BUSH && ConfigHandler.SERVER.forestBushImmunity.get()){
							event.setCanceled(true);
						}else if(damageSource == DamageSource.CACTUS && ConfigHandler.SERVER.forestCactiImmunity.get()){
							event.setCanceled(true);
						}
					}
				}

				
				if(ConfigHandler.SERVER.caveSplashDamage.get() != 0.0) {
					if (dragonStateHandler.getType() == DragonType.CAVE && !livingEntity.hasEffect(DragonEffects.FIRE)) {
						if (damageSource instanceof IndirectEntityDamageSource) {
							if (damageSource.getDirectEntity() instanceof SnowballEntity) {
								livingEntity.hurt(DamageSource.GENERIC, ConfigHandler.SERVER.caveSplashDamage.get().floatValue());
							}
						}
					}
				}
			}
        });
    }
	
	@SubscribeEvent
    public static void removeLavaFootsteps(PlaySoundAtEntityEvent event) {
    	if (!(event.getEntity() instanceof PlayerEntity))
    		return;
    	PlayerEntity player = (PlayerEntity)event.getEntity();
    	DragonStateProvider.getCap(player).ifPresent(dragonStateHandler -> {
    		if (dragonStateHandler.getType() == DragonType.CAVE && ConfigHandler.SERVER.bonuses.get() && ConfigHandler.SERVER.caveLavaSwimming.get() && DragonSizeHandler.getOverridePose(player) == Pose.SWIMMING && event.getSound().getRegistryName().getPath().contains(".step"))
    			event.setCanceled(true);
    	});
    }
	
	@SubscribeEvent
    public static void reduceFallDistance(LivingFallEvent livingFallEvent) {
        LivingEntity livingEntity = livingFallEvent.getEntityLiving();
        DragonStateProvider.getCap(livingEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
	            float distance = livingFallEvent.getDistance();
	
	            if (dragonStateHandler.getType() == DragonType.FOREST) {
					
					if(ConfigHandler.SERVER.bonuses.get()){
						distance -= ConfigHandler.SERVER.forestFallReduction.get().floatValue();
					}
	
	                DragonAbility ability = dragonStateHandler.getMagic().getAbility(DragonAbilities.CLIFFHANGER);

					if(ability != null){
						distance -= ((CliffhangerAbility)ability).getHeight();
					}
                }
	            distance -= dragonStateHandler.getLevel().jumpHeight;
	            livingFallEvent.setDistance(distance);
            }
        });
    }
}
