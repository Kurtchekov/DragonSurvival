package by.jackraidenph.dragonsurvival.client.render.entity.dragon;

import by.jackraidenph.dragonsurvival.client.render.ClientDragonRender;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.common.entity.DragonEntity;
import by.jackraidenph.dragonsurvival.common.magic.abilities.Actives.BreathAbilities.BreathAbility;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.util.RenderUtils;

import javax.annotation.Nullable;
import java.awt.*;

public class DragonRenderer extends GeoEntityRenderer<DragonEntity> {
	public ResourceLocation glowTexture = null;
	public boolean renderLayers = true;
	public boolean isLayer = false;
	
	public DragonRenderer(EntityRendererManager renderManager, AnimatedGeoModel<DragonEntity> modelProvider) {
        super(renderManager, modelProvider);
		this.addLayer(new DragonGlowLayerRenderer(this));
		this.addLayer(new ClawsAndTeethRenderLayer(this));
		this.addLayer(new DragonArmorRenderLayer(this));
		
	}
	
	public Color renderColor = new Color(255, 255, 255);
	
	@Override
	public Color getRenderColor(DragonEntity animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn)
	{
		return renderColor;
	}
	
	@Override
	public void render(DragonEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn)
	{
		PlayerEntity player = entity.getPlayer();
		DragonStateHandler handler = DragonStateProvider.getCap(player).orElse(null);
		
		boolean hasWings = true;
		
		if(handler != null){
			hasWings = handler.hasWings();
		}
		
		final IBone leftwing = ClientDragonRender.dragonModel.getAnimationProcessor().getBone("WingLeft");
		final IBone rightWing = ClientDragonRender.dragonModel.getAnimationProcessor().getBone("WingRight");
		
		if (leftwing != null) {
			leftwing.setHidden(!hasWings);
		}
		
		if (rightWing != null) {
			rightWing.setHidden(!hasWings);
		}
		
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	}
	
	@Override
	public void renderLate(DragonEntity animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
	{
		super.renderLate(animatable, stackIn, ticks, renderTypeBuffer, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
		currentEntity = animatable;
		this.partialTicks = partialTicks;
	}
	
	private float partialTicks;
	private DragonEntity currentEntity;
	
	@Override
	public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		PlayerEntity player = currentEntity != null ? currentEntity.getPlayer() : null;
		
		if(!isLayer && player != null) {
			/*if (bone.getName().equals("RightItem") && !mainHand.isEmpty()) {
				if(player != Minecraft.getInstance().player || ConfigHandler.CLIENT.alternateHeldItem.get() || !Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
					stack.pushPose();
					RenderUtils.translate(bone, stack);
					RenderUtils.moveToPivot(bone, stack);
					RenderUtils.rotate(bone, stack);
					RenderUtils.scale(bone, stack);
					stack.mulPose(Vector3f.ZP.rotationDegrees(0));
					stack.translate(0.0, 0, 0.0);
					Minecraft.getInstance().getItemRenderer().renderStatic(mainHand, TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, rtb);
					stack.popPose();
					bufferIn = rtb.getBuffer(RenderType.entityCutout(whTexture));
				}
			}else if (bone.getName().equals("LeftItem") && !offHand.isEmpty()) {
				if(player != Minecraft.getInstance().player || ConfigHandler.CLIENT.alternateHeldItem.get() || !Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
					stack.pushPose();
					RenderUtils.translate(bone, stack);
					RenderUtils.moveToPivot(bone, stack);
					RenderUtils.rotate(bone, stack);
					RenderUtils.scale(bone, stack);
					stack.mulPose(Vector3f.ZP.rotationDegrees(0));
					stack.translate(0.0, 0, 0.0);
					Minecraft.getInstance().getItemRenderer().renderStatic(offHand, TransformType.THIRD_PERSON_LEFT_HAND, packedLightIn, packedOverlayIn, stack, rtb);
					stack.popPose();
					bufferIn = rtb.getBuffer(RenderType.entityCutout(whTexture));
				}
			}else */if(bone.getName().equals("BreathSource")){
				DragonStateHandler handler = DragonStateProvider.getCap(player).orElse(null);
				
				if(handler != null){
					if(handler.getMagic().getCurrentlyCasting() instanceof BreathAbility){
						if(((BreathAbility)handler.getMagic().getCurrentlyCasting()).getEffectEntity() != null) {
							stack.pushPose();
							RenderUtils.translate(bone, stack);
							RenderUtils.moveToPivot(bone, stack);
							RenderUtils.rotate(bone, stack);
							RenderUtils.scale(bone, stack);
							stack.mulPose(Vector3f.YN.rotationDegrees(-90));
							//stack.mulPose(Vector3f.ZN.rotationDegrees(player.xRot));//For head pitch
							EntityRenderer<? super Entity> effectRender = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(((BreathAbility)handler.getMagic().getCurrentlyCasting()).getEffectEntity());
							effectRender.render(((BreathAbility)handler.getMagic().getCurrentlyCasting()).getEffectEntity(), player.getViewYRot(partialTicks), partialTicks, stack, rtb, 200);
							bufferIn = rtb.getBuffer(RenderType.entityCutout(whTexture));
							
							stack.popPose();
						}
					}
				}
			}
		}
		
		stack.pushPose();
		RenderUtils.translate(bone, stack);
		RenderUtils.moveToPivot(bone, stack);
		RenderUtils.rotate(bone, stack);
		RenderUtils.scale(bone, stack);
		RenderUtils.moveBackFromPivot(bone, stack);
		
		GeoBone currentCheckBone = bone;
		boolean isHidden = currentCheckBone.isHidden;
		
		//Check if any of the parents is hidden
		if(!isHidden){
			while(currentCheckBone.parent != null && !isHidden){
				isHidden = currentCheckBone.isHidden;
				currentCheckBone = currentCheckBone.parent;
			}
		}
		
		//Disable rendering if current bone or any bones above is hidden.
		if (!isHidden) {
			for (GeoCube cube : bone.childCubes) {
				stack.pushPose();
				renderCube(cube, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
				stack.popPose();
			}
		}
		
		//Still list through all bones even when hidden to allow rendering effects based on head in first person
		for (GeoBone childBone : bone.childBones) {
			renderRecursively(childBone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		}
		
		stack.popPose();
	}
	
}
