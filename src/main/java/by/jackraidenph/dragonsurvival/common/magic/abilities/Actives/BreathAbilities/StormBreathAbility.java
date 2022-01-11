package by.jackraidenph.dragonsurvival.common.magic.abilities.Actives.BreathAbilities;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.client.particles.SeaDragon.LargeLightningParticleData;
import by.jackraidenph.dragonsurvival.client.particles.SeaDragon.SmallLightningParticleData;
import by.jackraidenph.dragonsurvival.client.sounds.SoundRegistry;
import by.jackraidenph.dragonsurvival.client.sounds.StormBreathSound;
import by.jackraidenph.dragonsurvival.common.DragonEffects;
import by.jackraidenph.dragonsurvival.common.capability.Capabilities;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.common.capability.GenericCapability;
import by.jackraidenph.dragonsurvival.common.entity.DSEntities;
import by.jackraidenph.dragonsurvival.common.entity.projectiles.StormBreathEntity;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.misc.DragonType;
import by.jackraidenph.dragonsurvival.util.Functions;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.DistExecutor;

import java.util.ArrayList;
import java.util.List;

public class StormBreathAbility extends BreathAbility
{
	public StormBreathAbility(DragonType type, String id, String icon, int minLevel, int maxLevel, int manaCost, int castTime, int cooldown, Integer[] requiredLevels)
	{
		super(type, id, icon, minLevel, maxLevel, manaCost, castTime, cooldown, requiredLevels);
	}

	@Override
	public StormBreathAbility createInstance()
	{
		return new StormBreathAbility(type, id, icon, minLevel, maxLevel, manaCost, castTime, abilityCooldown, requiredLevels);
	}
	
	@Override
	public int getManaCost()
	{
		return player != null && player.hasEffect(DragonEffects.SOURCE_OF_MAGIC) ? 0 :(firstUse ? ConfigHandler.SERVER.stormBreathInitialMana.get() : ConfigHandler.SERVER.stormBreathOvertimeMana.get());
	}
	
	public void tickCost(){
		if(firstUse || castingTicks % ConfigHandler.SERVER.stormBreathManaTicks.get() == 0){
			consumeMana(player);
			firstUse = false;
		}
	}
	
	public static StormBreathEntity EFFECT_ENTITY;
	
	@OnlyIn(Dist.CLIENT)
	private ISound startingSound;

	@OnlyIn(Dist.CLIENT)
	private TickableSound loopingSound;

	@OnlyIn(Dist.CLIENT)
	private ISound endSound;
	
	@OnlyIn(Dist.CLIENT)
	public void sound(){
		if (castingTicks == 2) {
			if(startingSound == null){
				startingSound = SimpleSound.forAmbientAddition(SoundRegistry.stormBreathStart);
			}
			Minecraft.getInstance().getSoundManager().play(startingSound);
			loopingSound = new StormBreathSound(this);
			
			Minecraft.getInstance().getSoundManager().stop(new ResourceLocation(DragonSurvivalMod.MODID, "storm_breath_loop"), SoundCategory.PLAYERS);
			Minecraft.getInstance().getSoundManager().play(loopingSound);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void stopSound(){
		castingTicks = 0;
		
		if(SoundRegistry.stormBreathEnd != null) {
			if (endSound == null) {
				endSound = SimpleSound.forAmbientAddition(SoundRegistry.stormBreathEnd);
			}

			Minecraft.getInstance().getSoundManager().play(endSound);
		}
		
		Minecraft.getInstance().getSoundManager().stop(new ResourceLocation(DragonSurvivalMod.MODID, "storm_breath_loop"), SoundCategory.PLAYERS);
	}

	@Override
	public void stopCasting()
	{
		if(castingTicks > 1) {
			if (player.level.isClientSide) {
				DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> (DistExecutor.SafeRunnable)() -> stopSound());
			}
		}

		super.stopCasting();
	}
	@Override
	public boolean isDisabled()
	{
		return super.isDisabled() || !ConfigHandler.SERVER.stormBreath.get();
	}
	
	
	@Override
	public Entity getEffectEntity()
	{
		return EFFECT_ENTITY;
	}
	
	@Override
	public void onActivation(PlayerEntity player)
	{
		if(EFFECT_ENTITY == null){
			EFFECT_ENTITY = DSEntities.STORM_BREATH_EFFECT.create(player.level);
		}
		
		tickCost();
		super.onActivation(player);
		
		if(player.level.isClientSide) {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> (DistExecutor.SafeRunnable)() -> sound());
		}
		
		if(player.level.isClientSide) {
			for (int i = 0; i < 6; i++) {
				double xSpeed = speed * 1f * xComp;
				double ySpeed = speed * 1f * yComp;
				double zSpeed = speed * 1f * zComp;
				player.level.addParticle(new SmallLightningParticleData(37, true), dx, dy, dz, xSpeed, ySpeed, zSpeed);
			}
			
			for (int i = 0; i < 2; i++) {
				double xSpeed = speed * xComp + (spread * 0.7 * (player.level.random.nextFloat() * 2 - 1) * (Math.sqrt(1 - xComp * xComp)));
				double ySpeed = speed * yComp + (spread * 0.7 * (player.level.random.nextFloat() * 2 - 1) * (Math.sqrt(1 - yComp * yComp)));
				double zSpeed = speed * zComp + (spread * 0.7 * (player.level.random.nextFloat() * 2 - 1) * (Math.sqrt(1 - zComp * zComp)));
				player.level.addParticle(new LargeLightningParticleData(37, false), dx, dy, dz, xSpeed, ySpeed, zSpeed);
			}
		}
		
		hitEntities();
		
		if (player.tickCount % 10 == 0) {
			hitBlocks();
		}
	}
	
	@Override
	public boolean canHitEntity(LivingEntity entity)
	{
		return !(entity instanceof PlayerEntity) || player.canHarmPlayer(((PlayerEntity)entity));
	}
	
	@Override
	public void onDamage(LivingEntity entity) {
		onDamageChecks(entity);
	}
	
	public void onEntityHit(LivingEntity entityHit){
		hurtTarget(entityHit);
		StormBreathAbility.chargedEffectSparkle(player, entityHit, ConfigHandler.SERVER.chargedChainRange.get(), ConfigHandler.SERVER.stormBreathChainCount.get(), ConfigHandler.SERVER.chargedEffectDamage.get());
	}
	
	public static void onDamageChecks(LivingEntity entity){
		if(entity instanceof CreeperEntity){
			CreeperEntity creeper = (CreeperEntity)entity;
			
			if(!creeper.isPowered()){
				creeper.getEntityData().set(CreeperEntity.DATA_IS_POWERED, true);
			}
		}
	}
	
	public void hurtTarget(LivingEntity entity){
		entity.hurt(new BreathDamage(player), getDamage());
		onDamage(entity);
		
		if(player.level.random.nextInt(100) < 50){
			if(!player.level.isClientSide) {
				player.addEffect(new EffectInstance(DragonEffects.CHARGED, Functions.secondsToTicks(30)));
			}
		}
		
		if(!entity.level.isClientSide) {
			if (entity.level.random.nextInt(100) < 40) {
				GenericCapability cap = Capabilities.getGenericCapability(entity).orElse(null);
				
				if(cap != null){
					cap.lastAfflicted = player.getId();
					cap.chainCount = 1;
				}
				
				entity.addEffect(new EffectInstance(DragonEffects.CHARGED, Functions.secondsToTicks(10), 0, false, true));
			}
		}
	}
	
	public static void spark(LivingEntity source, LivingEntity target){
		if(source.level.isClientSide) {
			float eyeHeight = source instanceof PlayerEntity ? 0f : source.getEyeHeight();
			Vector3d start = source.getPosition(eyeHeight);
			Vector3d end = target.getPosition(target.getEyeHeight());
			
			int parts = 20;
			
			double xDif = (end.x - start.x) / parts;
			double yDif = (end.y - start.y) / parts;
			double zDif = (end.z - start.z) / parts;
			
			for (int i = 0; i < parts; i++) {
				double x = start.x + (xDif * i);
				double y = start.y + (yDif * i) + eyeHeight;
				double z = start.z + (zDif * i);
				source.level.addParticle(new RedstoneParticleData(0f, 1F, 1F, 1f), x, y, z, 0, 0, 0);
			}
		}
	}
	
	public static void chargedEffectSparkle(PlayerEntity player, LivingEntity source, int chainRange, int maxChainTargets, int damage){
		List<LivingEntity> secondaryTargets = getEntityLivingBaseNearby(source, source.getX(), source.getY() + source.getBbHeight() / 2, source.getZ(), chainRange);
		secondaryTargets.removeIf(e -> !isValidTarget(source, e));
		
		if(secondaryTargets.size() > maxChainTargets){
			secondaryTargets.sort((c1, c2) -> Boolean.compare(c1.hasEffect(DragonEffects.CHARGED), c2.hasEffect(DragonEffects.CHARGED)));
			secondaryTargets = secondaryTargets.subList(0, maxChainTargets);
		}
		
		secondaryTargets.add(source);
		
		for(LivingEntity target : secondaryTargets){
			boolean damaged = false;
			if(target != null && target.getType() != null && target.getType().getRegistryType() != null) {
				if (ConfigHandler.SERVER.chargedBlacklist.get().contains(target.getType().getRegistryName().toString())) {
					if(player != null) {
						target.hurt(DamageSource.playerAttack(player), damage);
					}else{
						target.hurt(DamageSource.GENERIC, damage);
					}
					
					damaged = true;
				}
			}
			if(!damaged) {
				if(player != null){
					target.hurt(DamageSource.indirectMobAttack(source, player), damage);
				}else{
					target.hurt(DamageSource.mobAttack(source), damage);
					
				}
			}
			onDamageChecks(target);
			
			if(!ConfigHandler.SERVER.chargedSpreadBlacklist.get().contains(source.getType().getRegistryName().toString())) {
				if (target != source) {
					GenericCapability capSource = Capabilities.getGenericCapability(source).orElse(null);
					GenericCapability cap = Capabilities.getGenericCapability(target).orElse(null);
					
					if(cap != null && capSource != null){
						cap.chainCount = capSource.chainCount + 1;
					}
					
					if (!target.level.isClientSide) {
						if (target.level.random.nextInt(100) < 40) {
							if (cap != null && (cap.chainCount < ConfigHandler.SERVER.chargedEffectMaxChain.get() || ConfigHandler.SERVER.chargedEffectMaxChain.get() == -1)) {
								cap.lastAfflicted = player != null ? player.getId() : -1;
								target.addEffect(new EffectInstance(DragonEffects.CHARGED, Functions.secondsToTicks(10), 0, false, true));
							}
						}
					}
					
					if (player != null) {
						if (player.level.random.nextInt(100) < 50) {
							if (!player.level.isClientSide) {
								player.addEffect(new EffectInstance(DragonEffects.CHARGED, Functions.secondsToTicks(30)));
							}
						}
					}
					spark(source, target);
				}
			}
		}
	}
	
	@Override
	public void onBlock(BlockPos pos, BlockState blockState, Direction direction)
	{
		if(!player.level.isClientSide){
			if(player.tickCount % 40 == 0) {
				if (player.level.isThundering()) {
					if (player.level.random.nextInt(100) < 30) {
						if (player.level.canSeeSky(pos)) {
							LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(player.level);
							lightningboltentity.moveTo(new Vector3d(pos.getX(), pos.getY(), pos.getZ()));
							lightningboltentity.setCause((ServerPlayerEntity)player);
							player.level.addFreshEntity(lightningboltentity);
							player.level.playSound(player, pos, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 5F, 1.0F);
						}
					}
				}
			}
		}
	}

	public static float getDamage(int level){
		return (float)(ConfigHandler.SERVER.stormBreathDamage.get() * level);
	}
	
	public float getDamage(){
		return getDamage(getLevel());
	}
	
	@OnlyIn( Dist.CLIENT )
	public ArrayList<ITextComponent> getLevelUpInfo(){
		ArrayList<ITextComponent> list = super.getLevelUpInfo();
		list.add(new TranslationTextComponent("ds.skill.damage", "+" + ConfigHandler.SERVER.stormBreathDamage.get()));
		return list;
	}
	
	public static boolean isValidTarget(LivingEntity attacker, LivingEntity target){
		if(target == null || attacker == null) return false;
		if(target == attacker) return false;
		if(target instanceof FakePlayer) return false;
		if(target instanceof TameableEntity && ((TameableEntity)target).getOwner() == attacker) return false;
		if(attacker instanceof TameableEntity && !isValidTarget(((TameableEntity)attacker).getOwner(), target)) return false;
		if(target.getLastHurtByMob() == attacker && target.getLastHurtByMobTimestamp() + Functions.secondsToTicks(1) < target.tickCount) return false;
		if(DragonStateProvider.getCap(target).map(cap -> cap.getType()).orElse(null) == DragonType.SEA) return false;
		
		return true;
	}
}
