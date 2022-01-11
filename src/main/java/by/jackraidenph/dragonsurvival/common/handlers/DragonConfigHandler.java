package by.jackraidenph.dragonsurvival.common.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.config.ConfigUtils;
import by.jackraidenph.dragonsurvival.misc.DragonType;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DragonConfigHandler
{
	public static List<Block> SEA_DRAGON_HYDRATION_BLOCKS;
	public static List<Item> SEA_DRAGON_HYDRATION_USE_ALTERNATIVES;
	
	public static List<Block> FOREST_DRAGON_BREATH_GROW_BLACKLIST;
	
	public static Map<DragonType, List<Block>> DRAGON_SPEEDUP_BLOCKS;
	public static Map<DragonType, List<Block>> DRAGON_BREATH_BLOCKS;
	public static Map<DragonType, List<Block>> DRAGON_MANA_BLOCKS;
	
	@SubscribeEvent
	public static void onConfigLoad(ModConfig.Loading event) {
		if (event.getConfig().getType() == Type.SERVER) {
			rebuildSpeedupBlocksMap();
			rebuildSeaDragonConfigs();
			rebuildBreathBlocks();
			rebuildManaBlocks();
			rebuildForestDragonConfigs();
		}
	}
	
	private static void rebuildSpeedupBlocksMap() {
		HashMap<DragonType, List<Block>> speedupMap = new HashMap<>();
		speedupMap.put(DragonType.CAVE, ConfigUtils.parseConfigBlockList(ConfigHandler.SERVER.caveSpeedupBlocks.get()));
		speedupMap.put(DragonType.FOREST, ConfigUtils.parseConfigBlockList(ConfigHandler.SERVER.forestSpeedupBlocks.get()));
		speedupMap.put(DragonType.SEA, ConfigUtils.parseConfigBlockList(ConfigHandler.SERVER.seaSpeedupBlocks.get()));
		DRAGON_SPEEDUP_BLOCKS = speedupMap;
	}
	
	public static void rebuildBreathBlocks() {
		HashMap<DragonType, List<Block>> breathMap = new HashMap<>();
		breathMap.put(DragonType.CAVE, ConfigUtils.parseConfigBlockList(ConfigHandler.SERVER.fireBreathBlockBreaks.get()));
		breathMap.put(DragonType.FOREST, ConfigUtils.parseConfigBlockList(ConfigHandler.SERVER.forestBreathBlockBreaks.get()));
		breathMap.put(DragonType.SEA, ConfigUtils.parseConfigBlockList(ConfigHandler.SERVER.stormBreathBlockBreaks.get()));
		DRAGON_BREATH_BLOCKS = breathMap;
	}
	
	public static void rebuildManaBlocks() {
		HashMap<DragonType, List<Block>> map = new HashMap<>();
		map.put(DragonType.CAVE, ConfigUtils.parseConfigBlockList(ConfigHandler.SERVER.caveDragonManaBlocks.get()));
		map.put(DragonType.FOREST, ConfigUtils.parseConfigBlockList(ConfigHandler.SERVER.forestDragonManaBlocks.get()));
		map.put(DragonType.SEA, ConfigUtils.parseConfigBlockList(ConfigHandler.SERVER.seaDragonManaBlocks.get()));
		DRAGON_MANA_BLOCKS = map;
	}
	
	private static void rebuildSeaDragonConfigs() {
		SEA_DRAGON_HYDRATION_BLOCKS = ConfigUtils.parseConfigBlockList(ConfigHandler.SERVER.seaHydrationBlocks.get());
		SEA_DRAGON_HYDRATION_USE_ALTERNATIVES = ConfigUtils.parseConfigItemList(ConfigHandler.SERVER.seaAdditionalWaterUseables.get());
	}
	
	private static void rebuildForestDragonConfigs() {
		FOREST_DRAGON_BREATH_GROW_BLACKLIST = ConfigUtils.parseConfigBlockList(ConfigHandler.SERVER.forestBreathGrowBlacklist.get());
	}
}
