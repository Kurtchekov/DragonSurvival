package by.jackraidenph.dragonsurvival.client.emotes;

import by.jackraidenph.dragonsurvival.common.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

import static net.minecraft.client.settings.PointOfView.THIRD_PERSON_BACK;


@Mod.EventBusSubscriber( Dist.CLIENT)
public class EmoteHandler
{
	private static final UUID EMOTE_NO_MOVE = UUID.fromString("09c2716e-0da9-430b-aaaf-8653c643dc09");
	
	@SubscribeEvent
	public static void playerTick(PlayerTickEvent event){
		if(event.phase == Phase.START) return;
		
		PlayerEntity player = event.player;
		
		if (player != null) {
			DragonStateProvider.getCap(player).ifPresent(cap -> {
				if (cap.getEmotes().getCurrentEmote() != null) {
					Emote emote = cap.getEmotes().getCurrentEmote();
					cap.getEmotes().emoteTick++;
					
					//Cancel emote if its duration is expired, this should happen even if it isnt local
					if (emote.duration != -1 && cap.getEmotes().emoteTick > emote.duration) {
						cap.getEmotes().setCurrentEmote(null);
						return;
					}
					
					if(player.getId() == Minecraft.getInstance().player.getId()) {
					/*
					//Enable this code to fix emotes being usable while flying and jumping
					if(!player.isOnGround()){
						EmoteMenuHandler.setEmote(null);
						return;
					}
					*/
						
						if (player.isCrouching() || player.swinging) {
							EmoteMenuHandler.setEmote(null);
							return;
						}
						
						if(emote.thirdPerson) {
							Minecraft.getInstance().levelRenderer.needsUpdate();
							PointOfView pointofview = Minecraft.getInstance().options.getCameraType();
							
							if (pointofview.isFirstPerson()) {
								Minecraft.getInstance().options.setCameraType(THIRD_PERSON_BACK);
								
								if (pointofview.isFirstPerson() != Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
									Minecraft.getInstance().gameRenderer.checkEntityPostEffect(Minecraft.getInstance().options.getCameraType().isFirstPerson() ? Minecraft.getInstance().getCameraEntity() : null);
								}
							}
						}
					}
					
					if (emote.sound != null && emote.sound.interval > 0) {
						if (cap.getEmotes().emoteTick % emote.sound.interval == 0) {
							player.level.playLocalSound(player.position().x, player.position().y, player.position().z, new SoundEvent(new ResourceLocation(emote.sound.key)), SoundCategory.PLAYERS, emote.sound.volume, emote.sound.pitch, false);
						}
					}
					
					if(ConfigHandler.SERVER.canMoveInEmote.get()) {
						if (emote.animation != null && !emote.animation.isEmpty()) {
							ModifiableAttributeInstance attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
							AttributeModifier noMove = new AttributeModifier(EMOTE_NO_MOVE, "EMOTE", -attributeInstance.getValue(), AttributeModifier.Operation.ADDITION);
							
							if (!attributeInstance.hasModifier(noMove)) {
								attributeInstance.addTransientModifier(noMove);
							}
						}
					}
				}else {
					if (ConfigHandler.SERVER.canMoveInEmote.get()) {
						ModifiableAttributeInstance attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
						AttributeModifier noMove = new AttributeModifier(EMOTE_NO_MOVE, "EMOTE", -attributeInstance.getValue(), AttributeModifier.Operation.ADDITION);
						
						if (attributeInstance.hasModifier(noMove)) {
							attributeInstance.removeModifier(EMOTE_NO_MOVE);
						}
					}
				}
			});
		}
	}
	
	@SubscribeEvent
	public static void playerAttacked(LivingHurtEvent event){
		LivingEntity entity = event.getEntityLiving();
		
		if(entity instanceof PlayerEntity){
			PlayerEntity player = (PlayerEntity)entity;
			
			DragonStateProvider.getCap(player).ifPresent(cap -> {
				if (cap.getEmotes().getCurrentEmote() != null) {
					EmoteMenuHandler.setEmote(null);
				}
			});
		}
	}
	
	@SubscribeEvent
	public static void playerTick(ClientTickEvent event){
		if(event.phase == Phase.START) return;
		
		DragonStateProvider.getCap(Minecraft.getInstance().player).ifPresent((cap) -> {
			if(cap.getEmotes().getCurrentEmote() != null && !cap.getEmotes().getCurrentEmote().loops){
				if(cap.getEmotes().emoteTick >= cap.getEmotes().getCurrentEmote().duration){
					EmoteMenuHandler.setEmote(null);
				}
			}
		});
	}
}
