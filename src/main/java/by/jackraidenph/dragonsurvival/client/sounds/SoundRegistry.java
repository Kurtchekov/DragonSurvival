package by.jackraidenph.dragonsurvival.client.sounds;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoundRegistry
{
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DragonSurvivalMod.MODID);
    
    public static SoundEvent activateBeacon, deactivateBeacon, upgradeBeacon, applyEffect;
    public static SoundEvent fireBreathStart, fireBreathLoop, fireBreathEnd;
    public static SoundEvent forestBreathStart, forestBreathLoop, forestBreathEnd;
    public static SoundEvent stormBreathStart, stormBreathLoop, stormBreathEnd;
    
    public static RegistryObject<SoundEvent> treasureGemBreak = SOUNDS.register("treasure_gem_break", () -> new SoundEvent(new ResourceLocation(DragonSurvivalMod.MODID, "treasure_gem_break")));
    public static RegistryObject<SoundEvent> treasureGemHit = SOUNDS.register("treasure_gem_hit", () -> new SoundEvent(new ResourceLocation(DragonSurvivalMod.MODID, "treasure_gem_hit")));
    public static RegistryObject<SoundEvent> treasureMetalBreak = SOUNDS.register("treasure_metal_break", () -> new SoundEvent(new ResourceLocation(DragonSurvivalMod.MODID, "treasure_metal_break")));
    public static RegistryObject<SoundEvent> treasureMetalHit = SOUNDS.register("treasure_metal_hit", () -> new SoundEvent(new ResourceLocation(DragonSurvivalMod.MODID, "treasure_metal_hit")));
    
    public static ForgeSoundType treasureGem = new ForgeSoundType(1f, 1f, treasureGemBreak, treasureGemHit, treasureGemHit, treasureGemHit, treasureGemHit);
    public static ForgeSoundType treasureMetal = new ForgeSoundType(1f, 1f, treasureMetalBreak, treasureMetalHit, treasureMetalHit, treasureMetalHit, treasureMetalHit);

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> registryEvent) {
        IForgeRegistry<SoundEvent> forgeRegistry = registryEvent.getRegistry();
        activateBeacon = register("activate_beacon", forgeRegistry);
        deactivateBeacon = register("deactivate_beacon", forgeRegistry);
        upgradeBeacon = register("upgrade_beacon", forgeRegistry);
        applyEffect = register("apply_effect", forgeRegistry);
    
        fireBreathStart = register("fire_breath_start", forgeRegistry);
        fireBreathLoop = register("fire_breath_loop", forgeRegistry);
        fireBreathEnd = register("fire_breath_end", forgeRegistry);
    
        forestBreathStart = register("forest_breath_start", forgeRegistry);
        forestBreathLoop = register("forest_breath_loop", forgeRegistry);
        forestBreathEnd = register("forest_breath_end", forgeRegistry);

        stormBreathStart = register("storm_breath_start", forgeRegistry);
        stormBreathLoop = register("storm_breath_loop", forgeRegistry);
        stormBreathEnd = register("storm_breath_end", forgeRegistry);
    }

    private static SoundEvent register(String name, IForgeRegistry<SoundEvent> forgeRegistry) {
        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(DragonSurvivalMod.MODID, name));
        soundEvent.setRegistryName(DragonSurvivalMod.MODID, name);
        forgeRegistry.register(soundEvent);
        return soundEvent;
    }
}
