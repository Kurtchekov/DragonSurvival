package by.jackraidenph.dragonsurvival.client.handlers;

import by.jackraidenph.dragonsurvival.common.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.network.NetworkHandler;
import by.jackraidenph.dragonsurvival.network.container.OpenDragonInventory;
import by.jackraidenph.dragonsurvival.network.magic.SyncDragonAbilitySlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber( Dist.CLIENT)
public class KeyInputHandler
{
	public static KeyBinding TOGGLE_WINGS;
	public static KeyBinding DRAGON_INVENTORY;
	
	public static KeyBinding NEXT_ABILITY;
	public static KeyBinding PREV_ABILITY;
	
	public static KeyBinding USE_ABILITY;
	public static KeyBinding TOGGLE_ABILITIES;
	
	public static KeyBinding ABILITY1;
	public static KeyBinding ABILITY2;
	public static KeyBinding ABILITY3;
	public static KeyBinding ABILITY4;
	
	public static KeyBinding SPIN_ABILITY;
	public static KeyBinding FREE_LOOK;
	
	
	public static void setupKeybinds() {
		TOGGLE_WINGS = new KeyBinding("ds.keybind.wings", GLFW.GLFW_KEY_G, "Dragon Survival");
		TOGGLE_WINGS.setKeyConflictContext(KeyConflictContext.IN_GAME);
		ClientRegistry.registerKeyBinding(TOGGLE_WINGS);
		
		DRAGON_INVENTORY = new KeyBinding("ds.keybind.dragon_inv", GLFW.GLFW_KEY_UNKNOWN, "Dragon Survival");
		DRAGON_INVENTORY.setKeyConflictContext(KeyConflictContext.IN_GAME);
		ClientRegistry.registerKeyBinding(DRAGON_INVENTORY);
		
		USE_ABILITY = new KeyBinding("ds.keybind.use_ability", GLFW.GLFW_KEY_C, "Dragon Survival");
		USE_ABILITY.setKeyConflictContext(KeyConflictContext.IN_GAME);
		ClientRegistry.registerKeyBinding(USE_ABILITY);
		
		TOGGLE_ABILITIES = new KeyBinding("ds.keybind.toggle_abilities", GLFW.GLFW_KEY_X, "Dragon Survival");
		TOGGLE_ABILITIES.setKeyConflictContext(KeyConflictContext.IN_GAME);
		ClientRegistry.registerKeyBinding(TOGGLE_ABILITIES);
		
		NEXT_ABILITY = new KeyBinding("ds.keybind.next_ability", GLFW.GLFW_KEY_R, "Dragon Survival");
		NEXT_ABILITY.setKeyConflictContext(KeyConflictContext.IN_GAME);
		ClientRegistry.registerKeyBinding(NEXT_ABILITY);
		
		PREV_ABILITY = new KeyBinding("ds.keybind.prev_ability", GLFW.GLFW_KEY_F, "Dragon Survival");
		PREV_ABILITY.setKeyConflictContext(KeyConflictContext.IN_GAME);
		ClientRegistry.registerKeyBinding(PREV_ABILITY);
		
		ABILITY1 = new KeyBinding("ds.keybind.ability1", GLFW.GLFW_KEY_UNKNOWN, "Dragon Survival");
		ABILITY1.setKeyConflictContext(KeyConflictContext.IN_GAME);
		ClientRegistry.registerKeyBinding(ABILITY1);
		
		ABILITY2 = new KeyBinding("ds.keybind.ability2", GLFW.GLFW_KEY_UNKNOWN, "Dragon Survival");
		ABILITY2.setKeyConflictContext(KeyConflictContext.IN_GAME);
		ClientRegistry.registerKeyBinding(ABILITY2);
		
		ABILITY3 = new KeyBinding("ds.keybind.ability3", GLFW.GLFW_KEY_UNKNOWN, "Dragon Survival");
		ABILITY3.setKeyConflictContext(KeyConflictContext.IN_GAME);
		ClientRegistry.registerKeyBinding(ABILITY3);
		
		ABILITY4 = new KeyBinding("ds.keybind.ability4", GLFW.GLFW_KEY_UNKNOWN, "Dragon Survival");
		ABILITY4.setKeyConflictContext(KeyConflictContext.IN_GAME);
		ClientRegistry.registerKeyBinding(ABILITY4);
		
		SPIN_ABILITY = new KeyBinding("ds.keybind.spin", Type.MOUSE, GLFW.GLFW_MOUSE_BUTTON_RIGHT, "Dragon Survival");
		SPIN_ABILITY.setKeyConflictContext(KeyConflictContext.GUI);
		ClientRegistry.registerKeyBinding(SPIN_ABILITY);
		
		FREE_LOOK = new KeyBinding("ds.keybind.free_look", GLFW.GLFW_KEY_LEFT_ALT, "Dragon Survival");
		FREE_LOOK.setKeyConflictContext(KeyConflictContext.IN_GAME);
		ClientRegistry.registerKeyBinding(FREE_LOOK);
	}
	
	@SubscribeEvent
	public static void onKey(InputEvent.KeyInputEvent keyInputEvent) {
	    Minecraft minecraft = Minecraft.getInstance();
	    ClientPlayerEntity player = Minecraft.getInstance().player;
	    
		if(player == null) return;
		
	    if (DRAGON_INVENTORY.consumeClick()) {
	        if(DragonStateProvider.isDragon(minecraft.player)){
				if(minecraft.screen == null) {
					NetworkHandler.CHANNEL.sendToServer(new OpenDragonInventory());
				}else{
					player.closeContainer();
				}
	        }
	    }else  if (TOGGLE_ABILITIES.consumeClick()) {
	        if(DragonStateProvider.isDragon(minecraft.player)){
	            DragonStateProvider.getCap(minecraft.player).ifPresent(dragonStateHandler -> {
	                dragonStateHandler.getMagic().setRenderAbilities(!dragonStateHandler.getMagic().renderAbilityHotbar());
	                NetworkHandler.CHANNEL.sendToServer(new SyncDragonAbilitySlot(player.getId(), dragonStateHandler.getMagic().getSelectedAbilitySlot(), dragonStateHandler.getMagic().renderAbilityHotbar()));
	            });
	        }
	    }else  if (NEXT_ABILITY.consumeClick()) {
	        if(DragonStateProvider.isDragon(minecraft.player)){
	            DragonStateProvider.getCap(minecraft.player).ifPresent(dragonStateHandler -> {
	                int nextSlot = dragonStateHandler.getMagic().getSelectedAbilitySlot() == 3 ? 0 : dragonStateHandler.getMagic().getSelectedAbilitySlot() + 1;
	                dragonStateHandler.getMagic().setSelectedAbilitySlot(nextSlot);
	                NetworkHandler.CHANNEL.sendToServer(new SyncDragonAbilitySlot(player.getId(), dragonStateHandler.getMagic().getSelectedAbilitySlot(), dragonStateHandler.getMagic().renderAbilityHotbar()));
	            });
	        }
	    }else  if (PREV_ABILITY.consumeClick()) {
	        if(DragonStateProvider.isDragon(minecraft.player)){
	            DragonStateProvider.getCap(minecraft.player).ifPresent(dragonStateHandler -> {
	                int nextSlot = dragonStateHandler.getMagic().getSelectedAbilitySlot() == 0 ? 3 : dragonStateHandler.getMagic().getSelectedAbilitySlot() - 1;
	                dragonStateHandler.getMagic().setSelectedAbilitySlot(nextSlot);
	                NetworkHandler.CHANNEL.sendToServer(new SyncDragonAbilitySlot(player.getId(), dragonStateHandler.getMagic().getSelectedAbilitySlot(), dragonStateHandler.getMagic().renderAbilityHotbar()));
	            });
	        }
	    }else  if (ABILITY1.consumeClick()) {
	        if(DragonStateProvider.isDragon(minecraft.player)){
	            DragonStateProvider.getCap(minecraft.player).ifPresent(dragonStateHandler -> {
	                dragonStateHandler.getMagic().setSelectedAbilitySlot(0);
	                NetworkHandler.CHANNEL.sendToServer(new SyncDragonAbilitySlot(player.getId(), dragonStateHandler.getMagic().getSelectedAbilitySlot(), dragonStateHandler.getMagic().renderAbilityHotbar()));
	            });
	        }
	    }else  if (ABILITY2.consumeClick()) {
	        if(DragonStateProvider.isDragon(minecraft.player)){
	            DragonStateProvider.getCap(minecraft.player).ifPresent(dragonStateHandler -> {
	                dragonStateHandler.getMagic().setSelectedAbilitySlot(1);
	                NetworkHandler.CHANNEL.sendToServer(new SyncDragonAbilitySlot(player.getId(), dragonStateHandler.getMagic().getSelectedAbilitySlot(), dragonStateHandler.getMagic().renderAbilityHotbar()));
	            });
	        }
	    }else  if (ABILITY3.consumeClick()) {
	        if(DragonStateProvider.isDragon(minecraft.player)){
	            DragonStateProvider.getCap(minecraft.player).ifPresent(dragonStateHandler -> {
	                dragonStateHandler.getMagic().setSelectedAbilitySlot(2);
	                NetworkHandler.CHANNEL.sendToServer(new SyncDragonAbilitySlot(player.getId(), dragonStateHandler.getMagic().getSelectedAbilitySlot(), dragonStateHandler.getMagic().renderAbilityHotbar()));
	            });
	        }
	    }else  if (ABILITY4.consumeClick()) {
	        if(DragonStateProvider.isDragon(minecraft.player)){
	            DragonStateProvider.getCap(minecraft.player).ifPresent(dragonStateHandler -> {
	                dragonStateHandler.getMagic().setSelectedAbilitySlot(3);
	                NetworkHandler.CHANNEL.sendToServer(new SyncDragonAbilitySlot(player.getId(), dragonStateHandler.getMagic().getSelectedAbilitySlot(), dragonStateHandler.getMagic().renderAbilityHotbar()));
	            });
	        }
	    }
	}
}
