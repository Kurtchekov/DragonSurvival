package by.jackraidenph.dragonsurvival.client.models;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.common.entity.DragonEntity;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.server.handlers.ServerFlightHandler;
import by.jackraidenph.dragonsurvival.util.Functions;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;
import software.bernie.shadowed.eliotlash.molang.MolangParser;

public class DragonModel extends AnimatedGeoModel<DragonEntity> {

    private ResourceLocation currentTexture = new ResourceLocation(DragonSurvivalMod.MODID, "textures/dragon/cave_newborn.png");

    @Override
    public ResourceLocation getModelLocation(DragonEntity dragonEntity) {
        return new ResourceLocation(DragonSurvivalMod.MODID, "geo/dragon_model.geo.json");
    }

    public void setCurrentTexture(ResourceLocation currentTexture) {
        this.currentTexture = currentTexture;
    }

    @Override
    public ResourceLocation getTextureLocation(DragonEntity dragonEntity) {
        return currentTexture;
    }

	@Override
	public ResourceLocation getAnimationFileLocation(DragonEntity animatable) {
		return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.animations.json");
	}
	
	private final double lookSpeed = 0.05;
	private final double lookDistance = 10;
	
	@Override
	public void setMolangQueries(IAnimatable animatable, double currentTick)
	{
		super.setMolangQueries(animatable, currentTick);
		if(!(animatable instanceof DragonEntity))return;
		
		DragonEntity dragonEntity = (DragonEntity)animatable;
		MolangParser parser = GeckoLibCache.getInstance().parser;
		PlayerEntity player = dragonEntity.getPlayer();
		
		if(player == null) return;
		
		DragonStateHandler handler = DragonStateProvider.getCap(player).orElse(null);
		
		boolean renderRot = ConfigHandler.CLIENT.renderOtherPlayerRotation.get() || Minecraft.getInstance().player == player;
		renderRot = false;
		
		{
			double curFlightY = MathHelper.lerp(0.1, dragonEntity.flightY, player.getDeltaMovement().y);
			if(Float.isNaN((float)curFlightY)){
				curFlightY = 0;
			}
			
			dragonEntity.flightY = curFlightY;
			
			parser.setValue("query.flight_y", renderRot ? curFlightY : 0);
		}
		
		{
			Vector3d vector3d1 = player.getDeltaMovement();
			Vector3d vector3d = player.getViewVector(1f);
			double d0 = Entity.getHorizontalDistanceSqr(vector3d1);
			double d1 = Entity.getHorizontalDistanceSqr(vector3d);
			double d2 = (vector3d1.x * vector3d.x + vector3d1.z * vector3d.z) / Math.sqrt(d0 * d1);
			double d3 = vector3d1.x * vector3d.z - vector3d1.z * vector3d.x;
			
			float rot = ((float)(Math.signum(d3) * Math.acos(d2))) * 2;
			
			double curFlightX = MathHelper.lerp(0.1, Float.isNaN((float)dragonEntity.flightX) ? 0 : dragonEntity.flightX, rot);
			if(Float.isNaN((float)curFlightX)){
				curFlightX = 0;
			}
			
			dragonEntity.flightX = curFlightX;
			
			parser.setValue("query.flight_x", renderRot ? curFlightX : 0);
		}
		
		parser.setValue("query.delta_y", player.getDeltaMovement().y);
		parser.setValue("query.head_yaw", handler.getMovementData().headYaw);
		parser.setValue("query.head_pitch", handler.getMovementData().headPitch);
		
		double bodyYawChange = Functions.angleDifference((float)handler.getMovementData().bodyYawLastTick, (float)handler.getMovementData().bodyYaw);
		double headYawChange = Functions.angleDifference((float)handler.getMovementData().headYawLastTick, (float)handler.getMovementData().headYaw);
		double headPitchChange = Functions.angleDifference((float)handler.getMovementData().headPitchLastTick, (float)handler.getMovementData().headPitch);
		
		double distance = Math.min( player.position().subtract(player.xo, player.yo, player.zo).length(), 10);
		
		ModifiableAttributeInstance gravity = player.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
		double g = gravity.getValue();
		
		dragonEntity.tailMotionUp = MathHelper.lerp(0.25, dragonEntity.tailMotionUp, ServerFlightHandler.isFlying(player) ? 0 : (player.getDeltaMovement().y + g) * 2);
		dragonEntity.tailMotionSide = MathHelper.lerp(0.1, dragonEntity.tailMotionSide, ServerFlightHandler.isFlying(player) ? 0 : bodyYawChange * (distance / 0.2));
		
		dragonEntity.bodyYawAverage.add(bodyYawChange);
		while(dragonEntity.bodyYawAverage.size() > 10) dragonEntity.bodyYawAverage.remove(0);
		
		dragonEntity.headYawAverage.add(headYawChange);
		while(dragonEntity.headYawAverage.size() > 10) dragonEntity.headYawAverage.remove(0);
		
		dragonEntity.headPitchAverage.add(headPitchChange);
		while(dragonEntity.headPitchAverage.size() > 10) dragonEntity.headPitchAverage.remove(0);
		
		dragonEntity.tailSideAverage.add(dragonEntity.tailMotionSide);
		while(dragonEntity.tailSideAverage.size() > 10) dragonEntity.tailSideAverage.remove(0);
		
		dragonEntity.tailUpAverage.add(dragonEntity.tailMotionUp * -1);
		while(dragonEntity.tailUpAverage.size() > 10) dragonEntity.tailUpAverage.remove(0);
		
		double bodyYawAvg = dragonEntity.bodyYawAverage.stream().mapToDouble(a -> a).sum() / dragonEntity.bodyYawAverage.size();
		double headYawAvg = dragonEntity.headYawAverage.stream().mapToDouble(a -> a).sum() / dragonEntity.headYawAverage.size();
		double headPitchAvg = dragonEntity.headPitchAverage.stream().mapToDouble(a -> a).sum() / dragonEntity.headPitchAverage.size();
		double tailSideAvg = MathHelper.clamp(dragonEntity.tailSideAverage.stream().mapToDouble(a -> a).sum() / dragonEntity.tailSideAverage.size(), -3, 3);
		double tailUpAvg = MathHelper.clamp(dragonEntity.tailUpAverage.stream().mapToDouble(a -> a).sum() / dragonEntity.tailUpAverage.size(), -3, 2);
		
		double query_body_yaw_change = Math.abs(MathHelper.degreesDifference((float)bodyYawAvg, (float)dragonEntity.body_yaw_change)) > 0.3 ? MathHelper.lerp(0.1, dragonEntity.body_yaw_change, bodyYawAvg) : bodyYawAvg;
		double query_head_yaw_change = Math.abs(MathHelper.degreesDifference((float)headYawAvg, (float)dragonEntity.head_yaw_change)) > 0.3 ? MathHelper.lerp(0.1, dragonEntity.head_yaw_change, headYawAvg) : headYawAvg;
		double query_head_pitch_change = Math.abs(MathHelper.degreesDifference((float)headPitchAvg, (float)dragonEntity.head_pitch_change)) > 0.3 ? MathHelper.lerp(0.1, dragonEntity.head_pitch_change, headPitchAvg) : headPitchAvg;
		double query_tail_motion_up = Math.abs(MathHelper.degreesDifference((float)tailUpAvg, (float)dragonEntity.tail_motion_up)) > 0.3 ? MathHelper.lerp(0.1, dragonEntity.tail_motion_up, tailUpAvg) : tailUpAvg;
		double query_tail_motion_side = Math.abs(MathHelper.degreesDifference((float)tailSideAvg, (float)dragonEntity.tail_motion_side)) > 0.3 ? MathHelper.lerp(0.1, dragonEntity.tail_motion_side, tailSideAvg) : tailSideAvg;
		
		if(((DragonEntity)animatable).tailLocked || !ConfigHandler.CLIENT.enableTailPhysics.get()){
			dragonEntity.tailMotionUp = 0;
			dragonEntity.tailMotionSide = 0;
			
			dragonEntity.tail_motion_up = 0;
			dragonEntity.tail_motion_side = 0;
			
			query_tail_motion_up = 0;
			query_tail_motion_side = 0;
		}
		
		parser.setValue("query.body_yaw_change", query_body_yaw_change);
		parser.setValue("query.head_yaw_change", query_head_yaw_change);
		parser.setValue("query.head_pitch_change", query_head_pitch_change);
		parser.setValue("query.tail_motion_up", query_tail_motion_up);
		parser.setValue("query.tail_motion_side", query_tail_motion_side);
		
		dragonEntity.body_yaw_change = query_body_yaw_change;
		dragonEntity.head_yaw_change = query_head_yaw_change;
		dragonEntity.head_pitch_change = query_head_pitch_change;
		dragonEntity.tail_motion_up = query_tail_motion_up;
		dragonEntity.tail_motion_side = query_tail_motion_side;
		
		if(handler.getEmotes().getCurrentEmote() != null) {
			EntityPredicate predicate = new EntityPredicate().range(lookDistance).allowSameTeam().allowInvulnerable().allowNonAttackable().selector(player::canSee);
			Entity lookAt = player.level.getNearestLoadedEntity(LivingEntity.class, predicate, player, player.getX(), player.getEyeY(), player.getZ(), player.getBoundingBox().inflate(lookDistance, 3.0D, lookDistance));
			
			if (lookAt != null && lookAt.isAlive()) {
				if (player.distanceToSqr(lookAt) <= (lookDistance * lookDistance)) {
					float xRotD = 0;
					float yRotD = 0;
					
					{
						double d0 = lookAt.getX() - player.getX();
						double d1 = lookAt.getEyeY() - player.getEyeY();
						double d2 = lookAt.getZ() - player.getZ();
						double d3 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
						xRotD = (float)(-(MathHelper.atan2(d1, d3) * (double)(180F / (float)Math.PI)));
					}
					
					{
						double d0 = lookAt.getX() - player.getX();
						double d1 = lookAt.getZ() - player.getZ();
						yRotD = (float)(MathHelper.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
					}
					
					double bodyRot = handler.getMovementData().bodyYaw - (((int)handler.getMovementData().bodyYaw) / 360) * 360;
					double yawDif = Math.abs(MathHelper.degreesDifference((float)yRotD, (float)bodyRot));
					
					if (yawDif <= 90 && Math.abs(xRotD) <= 30) {
						dragonEntity.lookYaw = MathHelper.lerp(lookSpeed, dragonEntity.lookYaw, MathHelper.wrapDegrees(-bodyRot + yRotD));
						dragonEntity.lookPitch = MathHelper.lerp(lookSpeed, dragonEntity.lookPitch, xRotD);
					}  else {
						dragonEntity.lookYaw = Math.round(MathHelper.lerp(lookSpeed, dragonEntity.lookYaw, 0)*100.0)/100.0;
						dragonEntity.lookPitch = Math.round(MathHelper.lerp(lookSpeed, dragonEntity.lookPitch, 0)*100.0)/100.0;
						
						if(Math.abs(dragonEntity.lookYaw) < 0.1) dragonEntity.lookYaw = 0;
						if(Math.abs(dragonEntity.lookPitch) < 0.1) dragonEntity.lookPitch = 0;
					}
				}
			}
		} else {
			dragonEntity.lookYaw = Math.round(MathHelper.lerp(lookSpeed, dragonEntity.lookYaw, 0)*100.0)/100.0;
			dragonEntity.lookPitch = Math.round(MathHelper.lerp(lookSpeed, dragonEntity.lookPitch, 0)*100.0)/100.0;
			
			if(Math.abs(dragonEntity.lookYaw) < 0.1) dragonEntity.lookYaw = 0;
			if(Math.abs(dragonEntity.lookPitch) < 0.1) dragonEntity.lookPitch = 0;
		}
		
		double query_look_at_yaw = Math.abs(MathHelper.degreesDifference((float)dragonEntity.lookYaw, (float)dragonEntity.look_at_yaw)) > 0.3 ? MathHelper.lerp(0.1, dragonEntity.look_at_yaw, dragonEntity.lookYaw) : dragonEntity.lookYaw;
		double query_look_at_pitch = Math.abs(MathHelper.degreesDifference((float)dragonEntity.lookPitch, (float)dragonEntity.look_at_pitch)) > 0.3 ? MathHelper.lerp(0.1, dragonEntity.look_at_pitch, dragonEntity.lookPitch) : dragonEntity.lookPitch;
		
		parser.setValue("query.look_at_yaw", query_look_at_yaw);
		parser.setValue("query.look_at_pitch", query_look_at_pitch);
		
		dragonEntity.look_at_yaw = query_look_at_yaw;
		dragonEntity.look_at_pitch = query_look_at_pitch;
	}
}
