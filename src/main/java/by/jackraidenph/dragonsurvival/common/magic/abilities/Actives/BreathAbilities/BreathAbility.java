package by.jackraidenph.dragonsurvival.common.magic.abilities.Actives.BreathAbilities;

import by.jackraidenph.dragonsurvival.client.handlers.KeyInputHandler;
import by.jackraidenph.dragonsurvival.common.DragonEffects;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.common.handlers.DragonConfigHandler;
import by.jackraidenph.dragonsurvival.common.magic.common.AbilityAnimation;
import by.jackraidenph.dragonsurvival.common.magic.common.ActiveDragonAbility;
import by.jackraidenph.dragonsurvival.common.magic.common.ISecondAnimation;
import by.jackraidenph.dragonsurvival.misc.DragonLevel;
import by.jackraidenph.dragonsurvival.misc.DragonType;
import by.jackraidenph.dragonsurvival.server.handlers.ServerFlightHandler;
import by.jackraidenph.dragonsurvival.util.Functions;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public abstract class BreathAbility extends ActiveDragonAbility implements ISecondAnimation
{
	public BreathAbility(DragonType type, String id, String icon, int minLevel, int maxLevel, int manaCost, int castTime, int cooldown, Integer[] requiredLevels)
	{
		super(type, id, icon, minLevel, maxLevel, manaCost, castTime, cooldown, requiredLevels);
	}
	
	private int RANGE = 5;
	private static final int ARC = 45;

	public int channelCost = 1;
	protected boolean firstUse = true;
	public int castingTicks = 0;
	
	public Entity getEffectEntity(){
		return null;
	}
	
	@Override
	public int getManaCost()
	{
		return player != null && player.hasEffect(DragonEffects.SOURCE_OF_MAGIC) ? 0 :(firstUse ? super.getManaCost() : channelCost);
	}
	public void stopCasting() {
		super.stopCasting();

		if(getCooldown() == 0 && !firstUse){
			startCooldown();
			firstUse = true;
		}
		castingTicks = 0;
	}
	
	public void tickCost(){
		if(firstUse || castingTicks % Functions.secondsToTicks(2) == 0){
			DragonStateProvider.consumeMana(player, this.getManaCost());
			firstUse = false;
		}
	}
	
	float yaw;
	float pitch;
	float speed;
	float spread;
	float xComp;
	float yComp;
	float zComp;
	double dx;
	double dy;
	double dz;
	
	@Override
	public void onActivation(PlayerEntity player)
	{
		if(ServerFlightHandler.isGliding(player)){
			stopCasting();
			return;
		}
		
		castingTicks++;
		
		DragonStateHandler playerStateHandler = DragonStateProvider.getCap(player).orElseGet(null);
		
		if(playerStateHandler == null){
			return;
		}
		
		DragonLevel growthLevel = DragonStateProvider.getCap(player).map(cap -> cap.getLevel()).get();
//		RANGE = growthLevel == DragonLevel.BABY ? 4 : growthLevel == DragonLevel.YOUNG ? 7 : 10;
		RANGE = (int)Math.round(4 + ((playerStateHandler.getSize() - DragonLevel.BABY.size) / (DragonLevel.ADULT.size - DragonLevel.BABY.size)) * 6);
		yaw = (float) Math.toRadians(-player.yRot);
		pitch = (float) Math.toRadians(-player.xRot);
		speed = growthLevel == DragonLevel.BABY ? 0.1F : growthLevel == DragonLevel.YOUNG ? 0.2F : 0.3F; //Changes distance
		spread = 0.1f;
		xComp = (float) (Math.sin(yaw) * Math.cos(pitch));
		yComp = (float) (Math.sin(pitch));
		zComp = (float) (Math.cos(yaw) * Math.cos(pitch));
		
		double headRot = playerStateHandler.getMovementData().headYaw;
		double pitch = playerStateHandler.getMovementData().headPitch;
		Vector3f bodyRot = DragonStateProvider.getCameraOffset(player);
		
		Point2D result = new Point2D.Double();
		Point2D result2 = new Point2D.Double();
		
		{
			Point2D point = new Double(player.position().x() + bodyRot.x(), player.position().y() + player.getEyeHeight() - 0.2);
			AffineTransform transform = new AffineTransform();
			double angleInRadians = ((MathHelper.clamp(pitch, -90, 90) * -1) * Math.PI / 180);
			transform.rotate(angleInRadians, player.position().x(), player.position().y() + player.getEyeHeight()- 0.2);
			transform.transform(point, result);
		}
		
		{
			Point2D point2 = new Double(player.position().x() + bodyRot.x(), player.position().z() + bodyRot.z());
			AffineTransform transform2 = new AffineTransform();
			double angleInRadians2 = ((MathHelper.clamp(headRot, -180, 180) * -1) * Math.PI / 180);
			transform2.rotate(angleInRadians2, player.position().x(), player.position().z());
			transform2.transform(point2, result2);
		}
		
		dx = result2.getX();
		dy = result.getY() - (Math.abs(headRot) / 180 * .5);
		dz = result2.getY();
		
		Vector3d delta = player.getDeltaMovement();
		
		if(player.isFallFlying() || player.abilities.flying) {
			yComp += delta.y * 6;
		}
		
		xComp += delta.x * 6;
		zComp += delta.z * 6;
	}

	public void hitEntities() {
		boolean found = false;
		List<LivingEntity> entitiesHit = getEntityLivingBaseNearby(RANGE, RANGE, RANGE, RANGE);
		for (LivingEntity entityHit : entitiesHit) {
			if (entityHit == player) continue;

			float entityHitYaw = (float) ((Math.atan2(entityHit.getZ() - player.getZ(), entityHit.getX() - player.getX()) * (180 / Math.PI) - 90) % 360);
			float entityAttackingYaw = player.yRot % 360;
			if (entityHitYaw < 0) {
				entityHitYaw += 360;
			}
			if (entityAttackingYaw < 0) {
				entityAttackingYaw += 360;
			}
			float entityRelativeYaw = entityHitYaw - entityAttackingYaw;

			float xzDistance = (float) Math.sqrt((entityHit.getZ() - player.getZ()) * (entityHit.getZ() - player.getZ()) + (entityHit.getX() - player.getX()) * (entityHit.getX() - player.getX()));
			double hitY = entityHit.getY() + entityHit.getBbHeight() / 2.0;
			float entityHitPitch = (float) ((Math.atan2((hitY - player.getY()), xzDistance) * (180 / Math.PI)) % 360);
			float entityAttackingPitch = -player.xRot % 360;
			if (entityHitPitch < 0) {
				entityHitPitch += 360;
			}
			if (entityAttackingPitch < 0) {
				entityAttackingPitch += 360;
			}
			float entityRelativePitch = entityHitPitch - entityAttackingPitch;

			float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - player.getZ()) * (entityHit.getZ() - player.getZ()) + (entityHit.getX() - player.getX()) * (entityHit.getX() - player.getX()) + (hitY - player.getY()) * (hitY - player.getY()));

			boolean inRange = entityHitDistance <= RANGE;
			boolean yawCheck = (entityRelativeYaw <= ARC / 2f && entityRelativeYaw >= -ARC / 2f) || (entityRelativeYaw >= 360 - ARC / 2f || entityRelativeYaw <= -360 + ARC / 2f);
			boolean pitchCheck = (entityRelativePitch <= ARC / 2f && entityRelativePitch >= -ARC / 2f) || (entityRelativePitch >= 360 - ARC / 2f || entityRelativePitch <= -360 + ARC / 2f);
			
			if (inRange && yawCheck && pitchCheck) {
				// Raytrace to mob center to avoid damaging through walls
				Vector3d from = player.getEyePosition(1.0F);
				Vector3d to = entityHit.position().add(0, entityHit.getEyeHeight() / 2.0f, 0);
				BlockRayTraceResult result = player.level.clip(new RayTraceContext(from, to, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, player));

				if (result.getType() == RayTraceResult.Type.BLOCK) {
					continue;
				}
				
				if(!canHitEntity(entityHit)) return;
				
				if(entityHit.getLastHurtByMob() == player && entityHit.getLastHurtByMobTimestamp() + Functions.secondsToTicks(1) < entityHit.tickCount){
					continue;
				}
				
				onEntityHit(entityHit);
				found = true;
			}
		}
		
		if(!found){
			Vector3d vector3d = player.getEyePosition(1.0F);
			Predicate<Entity> predicate = (entity) -> entity instanceof LivingEntity && !entity.isSpectator() && entity.isPickable();
			RayTraceResult result = ProjectileHelper.getHitResult(player, predicate);
			
			if(result.getType() == Type.ENTITY) {
				LivingEntity entity = (LivingEntity)((EntityRayTraceResult)result).getEntity();
				if (vector3d.distanceToSqr(result.getLocation()) <= RANGE) {
					onEntityHit(entity);
				}
			}
		}
	}
	
	public abstract boolean canHitEntity(LivingEntity entity);
	public abstract void onDamage(LivingEntity entity);
	public abstract float getDamage();
	public abstract void onBlock(BlockPos pos, BlockState blockState, Direction direction);
	
	public static class BreathDamage extends EntityDamageSource{
		
		public BreathDamage(@Nullable Entity p_i1567_2_)
		{
			super("player", p_i1567_2_);
		}
	}
	
	public void onEntityHit(LivingEntity entityHit){
		if (entityHit.hurt(new BreathDamage(player), getDamage())) {
			entityHit.setDeltaMovement(entityHit.getDeltaMovement().multiply(0.25, 1, 0.25));
			onDamage(entityHit);
		}
	}
	
	public int blockBreakChance(){
		return 90;
	}
	
	public void hitBlocks() {
		{
			Vector3d vector3d = player.getEyePosition(1.0F);
			Vector3d vector3d1 = player.getViewVector(1.0F).scale(RANGE);
			Vector3d vector3d2 = vector3d.add(vector3d1);
			BlockRayTraceResult result = player.level.clip(new RayTraceContext(vector3d, vector3d2, BlockMode.OUTLINE, this instanceof StormBreathAbility ? FluidMode.NONE : FluidMode.ANY, null));
			
			BlockPos pos = null;
			
			if (result.getType() == Type.MISS) {
				pos = new BlockPos(vector3d2.x, vector3d2.y, vector3d2.z);
			}else if(result.getType() == Type.BLOCK){
				pos = result.getBlockPos();
			}
			if(pos == null) return;
			
			for(int x = -(RANGE / 2); x < (RANGE / 2); x++){
				for(int y = -(RANGE / 2); y < (RANGE / 2); y++){
					for(int z =  -(RANGE / 2); z < (RANGE / 2); z++){
						BlockPos newPos = new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
						if(newPos.distSqr(pos) <= RANGE){
							BlockState state = player.level.getBlockState(newPos);
							if(state.getBlock() != Blocks.AIR){
								if(DragonConfigHandler.DRAGON_BREATH_BLOCKS != null && DragonConfigHandler.DRAGON_BREATH_BLOCKS.containsKey(type) && DragonConfigHandler.DRAGON_BREATH_BLOCKS.get(type).contains(state.getBlock())){
									if(!player.level.isClientSide) {
										if (player.level.random.nextFloat() * 100 <= blockBreakChance()) {
											player.level.destroyBlock(newPos, false, player);
											continue;
										}
									}
								}
								
								if(newPos != null && state != null) {
									onBlock(newPos, state, result.getDirection());
								}
							}
						}
					}
				}
			}
		}
	}
	
	public static List<LivingEntity> getEntityLivingBaseNearby(LivingEntity source, double distanceX, double distanceY, double distanceZ, double radius) {
		return getEntitiesNearby(source, LivingEntity.class, distanceX, distanceY, distanceZ, radius);
	}
	
	public static <T extends Entity> List<T> getEntitiesNearby(LivingEntity source, Class<T> entityClass, double dX, double dY, double dZ, double r) {
		return source.level.getEntitiesOfClass(entityClass, source.getBoundingBox().inflate(dX, dY, dZ), e -> e != source && source.distanceTo(e) <= r + e.getBbWidth() / 2f && e.getY() <= source.getY() + dY);
	}

	public List<LivingEntity> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
		return getEntitiesNearby(LivingEntity.class, distanceX, distanceY, distanceZ, radius);
	}

	public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
		return player.level.getEntitiesOfClass(entityClass, player.getBoundingBox().inflate(dX, dY, dZ), e -> e != player && player.distanceTo(e) <= r + e.getBbWidth() / 2f && e.getY() <= player.getY() + dY);
	}
	
	@Override
	public IFormattableTextComponent getDescription()
	{
		return new TranslationTextComponent("ds.skill.description." + getId(), getDamage());
	}

	@Override
	public ArrayList<ITextComponent> getInfo()
	{
		ArrayList<ITextComponent> components = new ArrayList<ITextComponent>();
		
		DragonLevel growthLevel = DragonStateProvider.getCap(player).map(cap -> cap.getLevel()).get();
		int RANGE = growthLevel == DragonLevel.BABY ? 4 : growthLevel == DragonLevel.YOUNG ? 7 : 10;
		
		components.add(new TranslationTextComponent("ds.skill.mana_cost", getManaCost()));
		components.add(new TranslationTextComponent("ds.skill.channel_cost", channelCost, 2));
		
		components.add(new TranslationTextComponent("ds.skill.cast_time", nf.format((double)getCastingTime() / 20)));
		components.add(new TranslationTextComponent("ds.skill.cooldown", Functions.ticksToSeconds(getMaxCooldown())));
		
		components.add(new TranslationTextComponent("ds.skill.damage", getDamage()));
		components.add(new TranslationTextComponent("ds.skill.range.blocks", RANGE));
		
		if(!KeyInputHandler.ABILITY1.isUnbound()) {
			String key = KeyInputHandler.ABILITY1.getKey().getDisplayName().getContents().toUpperCase(Locale.ROOT);
			
			if(key.isEmpty()){
				key = KeyInputHandler.ABILITY1.getKey().getDisplayName().getString();
			}
			components.add(new TranslationTextComponent("ds.skill.keybind", key));
		}
		
		return components;
	}
	
	@Override
	public AbilityAnimation getLoopingAnimation()
	{
		return new AbilityAnimation("breath", false, false);
	}
}
