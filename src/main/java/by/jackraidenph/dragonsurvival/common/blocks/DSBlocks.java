package by.jackraidenph.dragonsurvival.common.blocks;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.client.render.item.HelmetStackTileEntityRenderer;
import by.jackraidenph.dragonsurvival.client.sounds.SoundRegistry;
import by.jackraidenph.dragonsurvival.common.items.DragonDoorItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.awt.*;

@ObjectHolder(DragonSurvivalMod.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DSBlocks
{
    public static Block PREDATOR_STAR_BLOCK;
    
    public static DragonDoor spruceDoor, acaciaDoor, birchDoor, jungleDoor, oakDoor, darkOakDoor, crimsonDoor, warpedDoor;
    public static DragonDoor legacyDoor, ironDoor, murdererDoor, sleeperDoor, stoneDoor;
    public static DragonDoor caveDoor, forestDoor, seaDoor;
    public static SourceOfMagicBlock forestSourceOfMagic, caveSourceOfMagic, seaSourceOfMagic;

    public static Block dragon_altar_stone;
    public static Block dragon_altar_sandstone;
    public static Block dragon_altar_red_sandstone;
    public static Block dragon_altar_purpur_block;
    public static Block dragon_altar_oak_log;
    public static Block dragon_altar_nether_bricks;
    public static Block dragon_altar_mossy_cobblestone;
    public static Block dragon_altar_blackstone;
    public static Block dragon_altar_birch_log;

    public static HelmetBlock helmet1, helmet2, helmet3;
    public static DragonBeacon dragonBeacon, peaceDragonBeacon, magicDragonBeacon, fireDragonBeacon;
    public static Block dragonMemoryBlock;

    public static TreasureBlock treasureDebris, treasureDiamond, treasureEmerald, treasureCopper, treasureGold, treasureIron;
    
    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> forgeRegistry = event.getRegistry();

        dragon_altar_stone = new DragonAltarBlock(Block.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(0).strength(1.5f).sound(SoundType.STONE).requiresCorrectToolForDrops());
        dragon_altar_sandstone = new DragonAltarBlock(Block.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(0).strength(0.8f).sound(SoundType.STONE).requiresCorrectToolForDrops());
        dragon_altar_red_sandstone = new DragonAltarBlock(Block.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(0).strength(0.8f).sound(SoundType.STONE).requiresCorrectToolForDrops());
        dragon_altar_purpur_block = new DragonAltarBlock(Block.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(0).strength(1.5f).sound(SoundType.STONE).requiresCorrectToolForDrops());
        dragon_altar_oak_log = new DragonAltarBlock(Block.Properties.of(Material.WOOD).harvestTool(ToolType.AXE).harvestLevel(0).strength(2f).sound(SoundType.WOOD));
        dragon_altar_birch_log = new DragonAltarBlock(Block.Properties.of(Material.WOOD).harvestTool(ToolType.AXE).harvestLevel(0).strength(2f).sound(SoundType.WOOD));
        dragon_altar_nether_bricks = new DragonAltarBlock(Block.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(0).strength(0.4f).sound(SoundType.NETHER_BRICKS).requiresCorrectToolForDrops());
        dragon_altar_mossy_cobblestone = new DragonAltarBlock(Block.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(0).strength(2f).sound(SoundType.STONE).requiresCorrectToolForDrops());
        dragon_altar_blackstone = new DragonAltarBlock(Block.Properties.of(Material.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(0).strength(1.5f).sound(SoundType.STONE).requiresCorrectToolForDrops());
        
        forgeRegistry.register(dragon_altar_stone.setRegistryName(DragonSurvivalMod.MODID, "dragon_altar_stone"));
        forgeRegistry.register(dragon_altar_sandstone.setRegistryName(DragonSurvivalMod.MODID, "dragon_altar_sandstone"));
        forgeRegistry.register(dragon_altar_red_sandstone.setRegistryName(DragonSurvivalMod.MODID, "dragon_altar_red_sandstone"));
        forgeRegistry.register(dragon_altar_purpur_block.setRegistryName(DragonSurvivalMod.MODID, "dragon_altar_purpur_block"));
        forgeRegistry.register(dragon_altar_oak_log.setRegistryName(DragonSurvivalMod.MODID, "dragon_altar_oak_log"));
        forgeRegistry.register(dragon_altar_birch_log.setRegistryName(DragonSurvivalMod.MODID, "dragon_altar_birch_log"));
        forgeRegistry.register(dragon_altar_nether_bricks.setRegistryName(DragonSurvivalMod.MODID, "dragon_altar_nether_bricks"));
        forgeRegistry.register(dragon_altar_mossy_cobblestone.setRegistryName(DragonSurvivalMod.MODID, "dragon_altar_mossy_cobblestone"));
        forgeRegistry.register(dragon_altar_blackstone.setRegistryName(DragonSurvivalMod.MODID, "dragon_altar_blackstone"));
        
        PREDATOR_STAR_BLOCK = new PredatorStarBlock(Block.Properties.of(Material.EGG).noCollission()
                .strength(10F, 9999F).randomTicks().harvestTool(ToolType.HOE).requiresCorrectToolForDrops()
                .sound(SoundType.NETHER_WART));
        forgeRegistry.register(PREDATOR_STAR_BLOCK.setRegistryName(DragonSurvivalMod.MODID, "predator_star"));
        
        oakDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, Blocks.OAK_PLANKS.defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.NONE), "oak_dragon_door", forgeRegistry);
        spruceDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, Blocks.SPRUCE_PLANKS.defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.NONE), "spruce_dragon_door", forgeRegistry);
        acaciaDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, Blocks.ACACIA_PLANKS.defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.NONE), "acacia_dragon_door", forgeRegistry);
        birchDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, Blocks.BIRCH_PLANKS.defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.NONE), "birch_dragon_door", forgeRegistry);
        jungleDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, Blocks.JUNGLE_PLANKS.defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.NONE), "jungle_dragon_door", forgeRegistry);
        darkOakDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, Blocks.DARK_OAK_PLANKS.defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.NONE), "dark_oak_dragon_door", forgeRegistry);
        warpedDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, Blocks.WARPED_PLANKS.defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.NONE), "warped_dragon_door", forgeRegistry);
        crimsonDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, Blocks.CRIMSON_PLANKS.defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.NONE), "crimson_dragon_door", forgeRegistry);
        
        caveDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.STONE, Blocks.BLACKSTONE.defaultMaterialColor()).requiresCorrectToolForDrops().strength(7.0F).sound(SoundType.GILDED_BLACKSTONE).noOcclusion(), DragonDoor.OpenRequirement.CAVE), "cave_dragon_door", forgeRegistry);
        forestDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.STONE, Blocks.DARK_PRISMARINE.defaultMaterialColor()).requiresCorrectToolForDrops().strength(7.0F).sound(SoundType.STONE).noOcclusion(), DragonDoor.OpenRequirement.FOREST), "forest_dragon_door", forgeRegistry);
        seaDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(7.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.SEA), "sea_dragon_door", forgeRegistry);

        ironDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion(), DragonDoor.OpenRequirement.POWER), "iron_dragon_door", forgeRegistry);
        
        // TODO set properties for these doors
        murdererDoor = registerBlock(new DragonDoor(AbstractBlock.Properties.copy(oakDoor), DragonDoor.OpenRequirement.NONE), "murderer_dragon_door", forgeRegistry);
        sleeperDoor = registerBlock(new DragonDoor(AbstractBlock.Properties.copy(oakDoor), DragonDoor.OpenRequirement.NONE), "sleeper_dragon_door", forgeRegistry);
        stoneDoor = registerBlock(new DragonDoor(AbstractBlock.Properties.copy(oakDoor), DragonDoor.OpenRequirement.NONE), "stone_dragon_door", forgeRegistry);
        legacyDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, Blocks.SPRUCE_PLANKS.defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.NONE), "legacy_dragon_door", forgeRegistry);
        
        caveSourceOfMagic = registerBlock(new SourceOfMagicBlock(Block.Properties.of(Material.STONE).randomTicks().strength(3, 100).noOcclusion().lightLevel((c1) -> 10)), "cave_source_of_magic", forgeRegistry);
        forestSourceOfMagic = registerBlock(new SourceOfMagicBlock(Block.Properties.of(Material.STONE).strength(3, 100).noOcclusion().lightLevel((c1) -> 10)), "forest_source_of_magic", forgeRegistry);
        seaSourceOfMagic = registerBlock(new SourceOfMagicBlock(Block.Properties.of(Material.STONE).strength(3, 100).noOcclusion().lightLevel((c1) -> 10)), "sea_source_of_magic", forgeRegistry);
       
        treasureDebris = registerBlock(new TreasureBlock(new Color(148, 120, 114), AbstractBlock.Properties.of(Material.METAL, MaterialColor.COLOR_BROWN).noOcclusion().sound(SoundRegistry.treasureMetal).strength(0.5F)), "treasure_debris", forgeRegistry);
        treasureDiamond = registerBlock(new TreasureBlock(new Color(212, 255, 255), AbstractBlock.Properties.of(Material.METAL, MaterialColor.DIAMOND).noOcclusion().sound(SoundRegistry.treasureGem).strength(0.5F)), "treasure_diamond", forgeRegistry);
        treasureEmerald = registerBlock(new TreasureBlock(new Color(57, 240, 94), AbstractBlock.Properties.of(Material.METAL, MaterialColor.COLOR_GREEN).noOcclusion().sound(SoundRegistry.treasureGem).strength(0.5F)), "treasure_emerald", forgeRegistry);
        treasureCopper = registerBlock(new TreasureBlock(new Color(255, 255, 208), AbstractBlock.Properties.of(Material.GLASS, MaterialColor.COLOR_ORANGE).noOcclusion().sound(SoundRegistry.treasureMetal).strength(0.5F)), "treasure_copper", forgeRegistry);
        treasureGold = registerBlock(new TreasureBlock(new Color(255, 255, 243), AbstractBlock.Properties.of(Material.METAL, MaterialColor.GOLD).noOcclusion().sound(SoundRegistry.treasureMetal).strength(0.5F)), "treasure_gold", forgeRegistry);
        treasureIron = registerBlock(new TreasureBlock(new Color(211, 211, 211), AbstractBlock.Properties.of(Material.METAL, MaterialColor.METAL).noOcclusion().sound(SoundRegistry.treasureMetal).strength(0.5F)), "treasure_iron", forgeRegistry);
        
        helmet1 = registerBlock(new HelmetBlock(AbstractBlock.Properties.of(Material.METAL)), "broken_knight_helmet_1", forgeRegistry);
        helmet2 = registerBlock(new HelmetBlock(AbstractBlock.Properties.of(Material.METAL)), "broken_knight_helmet_2", forgeRegistry);
        helmet3 = registerBlock(new HelmetBlock(AbstractBlock.Properties.of(Material.METAL)), "broken_knight_helmet_3", forgeRegistry);

        dragonBeacon = registerBlock(new DragonBeacon(AbstractBlock.Properties.of(Material.HEAVY_METAL).strength(15, 50).requiresCorrectToolForDrops().noOcclusion().noCollission()), "empty_dragon_beacon", forgeRegistry);
        dragonMemoryBlock = registerBlock(new Block(AbstractBlock.Properties.of(Material.HEAVY_METAL).strength(3, 30).requiresCorrectToolForDrops()), "dragon_memory_block", forgeRegistry);
        peaceDragonBeacon = registerBlock(new DragonBeacon(AbstractBlock.Properties.copy(dragonBeacon).lightLevel(value -> value.getValue(DragonBeacon.LIT) ? 15 : 0)), "dragon_beacon_peace", forgeRegistry);
        magicDragonBeacon = registerBlock(new DragonBeacon(AbstractBlock.Properties.copy(dragonBeacon).lightLevel(value -> value.getValue(DragonBeacon.LIT) ? 15 : 0)), "dragon_beacon_magic", forgeRegistry);
        fireDragonBeacon = registerBlock(new DragonBeacon(AbstractBlock.Properties.copy(dragonBeacon).lightLevel(value -> value.getValue(DragonBeacon.LIT) ? 15 : 0)), "dragon_beacon_fire", forgeRegistry);
    }

    private static <B extends Block> B registerBlock(B block, String identifier, IForgeRegistry<Block> forgeRegistry) {
        block.setRegistryName(DragonSurvivalMod.MODID, identifier);
        forgeRegistry.register(block);
        return block;
    }

    @SubscribeEvent
    public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> forgeRegistry = event.getRegistry();
        registerDoor(oakDoor, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerDoor(spruceDoor, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerDoor(acaciaDoor, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerDoor(jungleDoor, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerDoor(darkOakDoor, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerDoor(birchDoor, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerDoor(caveDoor, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerDoor(seaDoor, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerDoor(forestDoor, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerDoor(ironDoor, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerDoor(murdererDoor, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerDoor(warpedDoor, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerDoor(crimsonDoor, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerDoor(sleeperDoor, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerDoor(stoneDoor, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerDoor(legacyDoor, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        

        forgeRegistry.register(new BlockItem(PREDATOR_STAR_BLOCK, new Item.Properties().tab(DragonSurvivalMod.items)).setRegistryName("predator_star"));
    
        registerItem(seaSourceOfMagic, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(forestSourceOfMagic, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(caveSourceOfMagic, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
    
        registerItem(treasureDebris, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(treasureDiamond, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(treasureEmerald, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(treasureCopper, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(treasureGold, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(treasureIron, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);

        registerItem(dragon_altar_stone, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(dragon_altar_sandstone, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(dragon_altar_red_sandstone, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(dragon_altar_purpur_block, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(dragon_altar_oak_log, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(dragon_altar_nether_bricks, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(dragon_altar_mossy_cobblestone, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(dragon_altar_blackstone, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(dragon_altar_birch_log, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);

        registerItem(helmet1, new Item.Properties().setISTER(() -> HelmetStackTileEntityRenderer::new).tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(helmet2, new Item.Properties().setISTER(() -> HelmetStackTileEntityRenderer::new).tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(helmet3, new Item.Properties().setISTER(() -> HelmetStackTileEntityRenderer::new).tab(DragonSurvivalMod.items), forgeRegistry);

        registerItem(dragonBeacon, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(peaceDragonBeacon, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(magicDragonBeacon, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(fireDragonBeacon, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
        registerItem(dragonMemoryBlock, new Item.Properties().tab(DragonSurvivalMod.items), forgeRegistry);
    }
    
    @SuppressWarnings("ConstantConditions")
    private static void registerItem(Block block, Item.Properties itemProperties, IForgeRegistry<Item> forgeRegistry) {
        forgeRegistry.register(new BlockItem(block, itemProperties.tab(DragonSurvivalMod.items)).setRegistryName(block.getRegistryName()));
    }

    private static void registerSingleItem(Block block, Item.Properties properties, IForgeRegistry<Item> forgeRegistry) {
        registerItem(block, properties.stacksTo(1), forgeRegistry);
    }

    @SuppressWarnings("ConstantConditions")
    private static void registerDoor(Block block, Item.Properties itemProps, IForgeRegistry<Item> forgeRegistry) {
        forgeRegistry.register(new DragonDoorItem(block, itemProps.tab(DragonSurvivalMod.items)).setRegistryName(block.getRegistryName()));
    }

}