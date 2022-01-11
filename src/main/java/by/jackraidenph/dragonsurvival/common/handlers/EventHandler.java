package by.jackraidenph.dragonsurvival.common.handlers;

import by.jackraidenph.dragonsurvival.common.blocks.DSBlocks;
import by.jackraidenph.dragonsurvival.common.DragonEffects;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.common.entity.DSEntities;
import by.jackraidenph.dragonsurvival.common.handlers.magic.ClawToolHandler;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.common.entity.monsters.MagicalPredatorEntity;
import by.jackraidenph.dragonsurvival.common.items.DSItems;
import by.jackraidenph.dragonsurvival.network.NetworkHandler;
import by.jackraidenph.dragonsurvival.network.status.PlayerJumpSync;
import by.jackraidenph.dragonsurvival.server.tileentity.SourceOfMagicTileEntity;
import by.jackraidenph.dragonsurvival.misc.DragonLevel;
import by.jackraidenph.dragonsurvival.misc.DragonType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class EventHandler {

    static int cycle = 0;

    /**
     * Check every 2 seconds
     */
    @SubscribeEvent
    public static void removeElytraFromDragon(TickEvent.PlayerTickEvent playerTickEvent) {
        if (!ConfigHandler.COMMON.dragonsAllowedToUseElytra.get() && playerTickEvent.phase == TickEvent.Phase.START) {
            PlayerEntity playerEntity = playerTickEvent.player;
            DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
                if (dragonStateHandler.isDragon() && playerEntity instanceof ServerPlayerEntity && cycle >= 40) {
                    //chestplate slot is #38
                    ItemStack stack = playerEntity.inventory.getItem(38);
                    Item item = stack.getItem();
                    if (item instanceof ElytraItem) {
                        playerEntity.drop(playerEntity.inventory.removeItemNoUpdate(38), true, false);
                    }
                    cycle = 0;
                } else cycle++;
            });
        }
    }
    
    @SubscribeEvent
    public static void mobDeath(LivingDropsEvent event){
        LivingEntity entity = event.getEntityLiving();
        float health = entity.getMaxHealth();
        
        //if(entity instanceof AnimalEntity) return;
        if(event.getSource() == null || !(event.getSource().getEntity() instanceof PlayerEntity)) return;
        if(!DragonStateProvider.isDragon(event.getSource().getEntity())) return;

        if(health >= 14 && health < 20){
            if (entity.level.random.nextInt(100) <= (ConfigHandler.SERVER.dragonHeartShardChance.get() * 100) + (event.getLootingLevel() * ((ConfigHandler.SERVER.dragonHeartShardChance.get() * 100) / 4))) {
                event.getDrops().add(new ItemEntity(entity.level, entity.position().x, entity.position().y, entity.position().z, new ItemStack(DSItems.dragonHeartShard)));
            }
        }else if(health >= 20 && health < 50){
            if (entity.level.random.nextInt(100) <= (ConfigHandler.SERVER.weakDragonHeartChance.get() * 100) + (event.getLootingLevel() * ((ConfigHandler.SERVER.weakDragonHeartChance.get() * 100) / 4))) {
                event.getDrops().add(new ItemEntity(entity.level, entity.position().x, entity.position().y, entity.position().z, new ItemStack(DSItems.weakDragonHeart)));
            }
        }else if(health >= 50){
            if (entity.level.random.nextInt(100) <= (ConfigHandler.SERVER.elderDragonHeartChance.get() * 100) + (event.getLootingLevel() * ((ConfigHandler.SERVER.elderDragonHeartChance.get() * 100) / 4))) {
                event.getDrops().add(new ItemEntity(entity.level, entity.position().x, entity.position().y, entity.position().z, new ItemStack(DSItems.elderDragonHeart)));
            }
        }
    }

    /**
     * Adds dragon avoidance goal
     */
    @SubscribeEvent
    public static void onJoin(EntityJoinWorldEvent joinWorldEvent) {
        Entity entity = joinWorldEvent.getEntity();
        if (entity instanceof AnimalEntity && !(entity instanceof WolfEntity || entity instanceof HoglinEntity)) {

            ((AnimalEntity) entity).goalSelector.addGoal(5, new AvoidEntityGoal(
                    (AnimalEntity) entity, PlayerEntity.class,
                    livingEntity -> DragonStateProvider.isDragon((PlayerEntity) livingEntity) && !((PlayerEntity) livingEntity).hasEffect(DragonEffects.ANIMAL_PEACE),
                    20.0F, 1.3F, 1.5F, EntityPredicates.ATTACK_ALLOWED));
        }
        if (entity instanceof HorseEntity) {
            HorseEntity horseEntity = (HorseEntity) entity;
            horseEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(horseEntity, PlayerEntity.class, 0, true, false, livingEntity -> livingEntity.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElseGet(null).getLevel() != DragonLevel.ADULT));
            horseEntity.targetSelector.addGoal(4, new AvoidEntityGoal<>(horseEntity, PlayerEntity.class, livingEntity -> livingEntity.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null).getLevel() == DragonLevel.ADULT && !livingEntity.hasEffect(DragonEffects.ANIMAL_PEACE), 20, 1.3, 1.5, EntityPredicates.ATTACK_ALLOWED::test));
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent e) {
        LivingEntity livingEntity = e.getEntityLiving();
        World world = livingEntity.level;
        if (livingEntity instanceof AnimalEntity && livingEntity.level.getRandom().nextDouble() < ConfigHandler.COMMON.predatorAnimalSpawnChance.get()) {
            if (world.getEntitiesOfClass(PlayerEntity.class, new AxisAlignedBB(livingEntity.blockPosition()).inflate(50), playerEntity -> playerEntity.hasEffect(DragonEffects.PREDATOR_ANTI_SPAWN)).isEmpty()) {
                MagicalPredatorEntity beast = DSEntities.MAGICAL_BEAST.create(livingEntity.level);
                livingEntity.level.addFreshEntity(beast);
                beast.teleportToWithTicket(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
            }
        }
    }
    
    @SubscribeEvent
    public static void sleepCheck(SleepingLocationCheckEvent sleepingLocationCheckEvent) {
        BlockPos sleepingLocation = sleepingLocationCheckEvent.getSleepingLocation();
        World world = sleepingLocationCheckEvent.getEntity().level;
        if (world.isNight() && world.getBlockEntity(sleepingLocation) instanceof SourceOfMagicTileEntity)
            sleepingLocationCheckEvent.setResult(Event.Result.ALLOW);
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void expDrops(BlockEvent.BreakEvent breakEvent) {
       if(DragonStateProvider.isDragon(breakEvent.getPlayer())){
           if(breakEvent.getExpToDrop() > 0){
               int bonusLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, breakEvent.getPlayer());
               int silklevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, breakEvent.getPlayer());
               breakEvent.setExpToDrop(breakEvent.getState().getExpDrop(breakEvent.getWorld(), breakEvent.getPos(), bonusLevel, silklevel));
           }
       }
    }
    
    @SubscribeEvent
    public static void blockBroken(BlockEvent.BreakEvent breakEvent) {
        if(breakEvent.isCanceled()) return;
        
        PlayerEntity playerEntity = breakEvent.getPlayer();
        if(playerEntity.isCreative()) return;
    
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, playerEntity);
    
        if(i <= 0) {
            IWorld world = breakEvent.getWorld();
            if (world instanceof ServerWorld) {
                BlockState blockState = breakEvent.getState();
                BlockPos blockPos = breakEvent.getPos();
                Block block = blockState.getBlock();
                ItemStack mainHandItem = ClawToolHandler.getDragonTools(playerEntity);
                double random;
                // Modded Ore Support
                String[] tagStringSplit = ConfigHandler.SERVER.oresTag.get().split(":");
                ResourceLocation ores = new ResourceLocation(tagStringSplit[0], tagStringSplit[1]);
                // Checks to make sure the ore does not drop itself or another ore from the tag (no going infinite with ores)
                ITag<Item> oresTag = ItemTags.getAllTags().getTag(ores);
                
                if (!oresTag.contains(block.asItem())){
                    return;
                }
                
                List<ItemStack> drops = block.getDrops(blockState, new LootContext.Builder((ServerWorld) world)
                        .withParameter(LootParameters.ORIGIN, new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                        .withParameter(LootParameters.TOOL, mainHandItem));
                DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
                    final boolean suitableOre = (mainHandItem.isCorrectToolForDrops(blockState) ||
                                                 (dragonStateHandler.isDragon() && dragonStateHandler.canHarvestWithPaw(playerEntity, blockState)))
                                                && drops.stream().noneMatch(item -> oresTag.contains(item.getItem()));
                    if (suitableOre && !playerEntity.isCreative()) {
                        boolean isCave = dragonStateHandler.getType() == DragonType.CAVE;
                        
                        if (dragonStateHandler.isDragon()) {
                            if (playerEntity.getRandom().nextDouble() < ConfigHandler.SERVER.dragonOreDustChance.get()) {
                                world.addFreshEntity(new ItemEntity((World) world, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, new ItemStack(DSItems.elderDragonDust)){
                                    @Override
                                    public boolean fireImmune()
                                    {
                                        return isCave || super.fireImmune();
                                    }
                                });
                            }
                            if (playerEntity.getRandom().nextDouble() < ConfigHandler.SERVER.dragonOreBoneChance.get()) {
                                world.addFreshEntity(new ItemEntity((World) world, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, new ItemStack(DSItems.elderDragonBone)){
                                    @Override
                                    public boolean fireImmune()
                                    {
                                        return isCave || super.fireImmune();
                                    }
                                });
                            }
                        } else {
                            if (playerEntity.getRandom().nextDouble() < ConfigHandler.SERVER.humanOreDustChance.get()) {
                                world.addFreshEntity(new ItemEntity((World) world, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, new ItemStack(DSItems.elderDragonDust)){
                                    @Override
                                    public boolean fireImmune()
                                    {
                                        return isCave || super.fireImmune();
                                    }
                                });
                            }
                            if (playerEntity.getRandom().nextDouble() < ConfigHandler.SERVER.humanOreBoneChance.get()) {
                                world.addFreshEntity(new ItemEntity((World) world, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, new ItemStack(DSItems.elderDragonBone)){
                                    @Override
                                    public boolean fireImmune()
                                    {
                                        return isCave || super.fireImmune();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }
    }
    
    
    @SubscribeEvent
    public static void createAltar(PlayerInteractEvent.RightClickBlock rightClickBlock) {
        ItemStack itemStack = rightClickBlock.getItemStack();
        if (itemStack.getItem() == DSItems.elderDragonBone) {
            if(!rightClickBlock.getPlayer().isSpectator()) {

                final World world = rightClickBlock.getWorld();
                final BlockPos blockPos = rightClickBlock.getPos();
                BlockState blockState = world.getBlockState(blockPos);
                final Block block = blockState.getBlock();

                boolean replace = false;
                rightClickBlock.getPlayer().isSpectator();
                rightClickBlock.getPlayer().isCreative();
                BlockItemUseContext deirection = new BlockItemUseContext(
                        rightClickBlock.getPlayer(),
                        rightClickBlock.getHand(),
                        rightClickBlock.getItemStack(),
                        new BlockRayTraceResult(
                                new Vector3d(0, 0, 0),
                                rightClickBlock.getPlayer().getDirection(),
                                blockPos,
                                false));
                if (block == Blocks.STONE) {
                    world.setBlockAndUpdate(blockPos, DSBlocks.dragon_altar_stone.getStateForPlacement(deirection));
                    replace = true;
                } else if (block == Blocks.MOSSY_COBBLESTONE) {
                    world.setBlockAndUpdate(blockPos, DSBlocks.dragon_altar_mossy_cobblestone.getStateForPlacement(deirection));
                    replace = true;
                } else if (block == Blocks.SANDSTONE) {
                    world.setBlockAndUpdate(blockPos, DSBlocks.dragon_altar_sandstone.getStateForPlacement(deirection));
                    replace = true;
                } else if (block == Blocks.RED_SANDSTONE) {
                    world.setBlockAndUpdate(blockPos, DSBlocks.dragon_altar_red_sandstone.getStateForPlacement(deirection));
                    replace = true;
                } else if (block == Blocks.OAK_LOG) {
                    world.setBlockAndUpdate(blockPos, DSBlocks.dragon_altar_oak_log.getStateForPlacement(deirection));
                    replace = true;
                } else if (block == Blocks.BIRCH_LOG) {
                    world.setBlockAndUpdate(blockPos, DSBlocks.dragon_altar_birch_log.getStateForPlacement(deirection));
                    replace = true;
                } else if (block == Blocks.PURPUR_BLOCK) {
                    world.setBlockAndUpdate(blockPos, DSBlocks.dragon_altar_purpur_block.getStateForPlacement(deirection));
                    replace = true;
                } else if (block == Blocks.NETHER_BRICKS) {
                    world.setBlockAndUpdate(blockPos, DSBlocks.dragon_altar_nether_bricks.getStateForPlacement(deirection));
                    replace = true;
                } else if (block == Blocks.BLACKSTONE) {
                    rightClickBlock.getPlayer().getDirection();
                    world.setBlockAndUpdate(blockPos, DSBlocks.dragon_altar_blackstone.getStateForPlacement(deirection));
                    replace = true;
                }

                if (replace) {
                    if (!rightClickBlock.getPlayer().isCreative()) {
                        itemStack.shrink(1);
                    }
                    rightClickBlock.setCanceled(true);
                    world.playSound(rightClickBlock.getPlayer(), blockPos, SoundEvents.WITHER_SPAWN, SoundCategory.PLAYERS, 1, 1);
                    rightClickBlock.setCancellationResult(ActionResultType.SUCCESS);
                }
            }
        }
    }

    @SubscribeEvent
    public static void returnBeacon(PlayerEvent.ItemCraftedEvent craftedEvent) {
        IInventory inventory = craftedEvent.getInventory();
        ItemStack result = craftedEvent.getCrafting();
        if (result.getItem() == DSBlocks.dragonBeacon.asItem()) {
            craftedEvent.getPlayer().addItem(new ItemStack(Items.BEACON));
        }
    }
	
	@SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent jumpEvent) {
        final LivingEntity livingEntity = jumpEvent.getEntityLiving();
        DragonStateProvider.getCap(livingEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                switch (dragonStateHandler.getLevel()) {
                    case BABY:
                        livingEntity.push(0, ConfigHandler.SERVER.newbornJump.get(), 0); //1+ block
                        break;
                    case YOUNG:
                        livingEntity.push(0, ConfigHandler.SERVER.youngJump.get(), 0); //1.5+ block
                        break;
                    case ADULT:
                        livingEntity.push(0, ConfigHandler.SERVER.adultJump.get(), 0); //2+ blocks
                        break;
                }
                if (livingEntity instanceof ServerPlayerEntity) {
                    if (livingEntity.getServer().isSingleplayer())
                        NetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new PlayerJumpSync(livingEntity.getId(), 20)); // 42
                    else
                        NetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new PlayerJumpSync(livingEntity.getId(), 10)); // 21
                }
            }
        });
    }
}
