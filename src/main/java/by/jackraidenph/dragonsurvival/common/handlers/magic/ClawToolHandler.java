package by.jackraidenph.dragonsurvival.common.handlers.magic;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.misc.DragonLevel;
import by.jackraidenph.dragonsurvival.misc.DragonType;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent.PickupXp;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.ArrayList;

@EventBusSubscriber
public class ClawToolHandler
{
	@SubscribeEvent
	public static void experiencePickup(PickupXp event){
		PlayerEntity player = event.getPlayer();
		
		DragonStateProvider.getCap(player).ifPresent(cap -> {
			ArrayList<ItemStack> stacks = new ArrayList<>();
			
			for(int i = 0; i < 4; i++){
				ItemStack clawStack = cap.getClawInventory().getClawsInventory().getItem(i);
				int mending = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MENDING, clawStack);
				
				if(mending > 0 && clawStack.isDamaged()){
					stacks.add(clawStack);
				}
			}
			
			if(stacks.size() > 0) {
				ItemStack repairTime = stacks.get(player.level.random.nextInt(stacks.size()));
				if (!repairTime.isEmpty() && repairTime.isDamaged()) {
					
					int i = Math.min((int)(event.getOrb().value * repairTime.getXpRepairRatio()), repairTime.getDamageValue());
					event.getOrb().value -= i * 2;
					repairTime.setDamageValue(repairTime.getDamageValue() - i);
				}
			}
			
			event.getOrb().value = Math.max(0, event.getOrb().value);
		});
	}
	
	@SubscribeEvent
	public static void playerDieEvent(LivingDropsEvent event){
		Entity ent = event.getEntity();
		
		if(ent instanceof PlayerEntity){
			PlayerEntity player = (PlayerEntity)ent;
			
			if(!player.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) && !ConfigHandler.SERVER.keepClawItems.get()){
				DragonStateHandler handler = DragonStateProvider.getCap(player).orElse(null);
				
				if(handler != null){
					for(int i = 0; i < handler.getClawInventory().getClawsInventory().getContainerSize(); i++){
						ItemStack stack = handler.getClawInventory().getClawsInventory().getItem(i);
						
						if(!stack.isEmpty()) {
							event.getDrops().add(new ItemEntity(player.level, player.getX(), player.getY(), player.getZ(), stack));
							handler.getClawInventory().getClawsInventory().setItem(i, ItemStack.EMPTY);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void dropBlocksMinedByPaw(PlayerEvent.HarvestCheck harvestCheck) {
		if (!ConfigHandler.SERVER.bonuses.get() || !ConfigHandler.SERVER.clawsAreTools.get())
			return;
	    PlayerEntity playerEntity = harvestCheck.getPlayer();
	    DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
	        if (dragonStateHandler.isDragon()) {
	            ItemStack stack = playerEntity.getMainHandItem();
	            Item item = stack.getItem();
	            BlockState blockState = harvestCheck.getTargetBlock();
	            if (!(item instanceof ToolItem || item instanceof SwordItem || item instanceof ShearsItem) && !harvestCheck.canHarvest()) {
		            harvestCheck.setCanHarvest(dragonStateHandler.canHarvestWithPaw(playerEntity, blockState));
		        }
	        }
	    });
	}
	
	public static ItemStack getDragonTools(PlayerEntity player)
	{
		ItemStack mainStack = player.inventory.getSelected();
		DragonStateHandler cap = DragonStateProvider.getCap(player).orElse(null);
		
		if(!(mainStack.getItem() instanceof TieredItem) && cap != null) {
			float newSpeed = 0F;
			ItemStack harvestTool = null;
			
			World world = player.level;
			Vector3d vector3d = player.getEyePosition(1f);
			Vector3d vector3d1 = player.getViewVector(1f);
			Vector3d vector3d2 = vector3d.add(vector3d1.x, vector3d1.y, vector3d1.z);
			BlockRayTraceResult raytraceresult = world.clip(new RayTraceContext(vector3d1, vector3d2, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, player));
			
			if (raytraceresult != null && raytraceresult.getType() != RayTraceResult.Type.MISS) {
				BlockState state = world.getBlockState(raytraceresult.getBlockPos());
				
				if(state != null) {
					for (int i = 1; i < 4; i++) {
						if (state.getHarvestTool() == null || state.getHarvestTool() == DragonStateHandler.CLAW_TOOL_TYPES[i]) {
							ItemStack breakingItem = cap.getClawInventory().getClawsInventory().getItem(i);
							if (!breakingItem.isEmpty()) {
								float tempSpeed = breakingItem.getDestroySpeed(state);
								
								if (tempSpeed > newSpeed) {
									newSpeed = tempSpeed;
									harvestTool = breakingItem;
								}
							}
						}
					}
				}
			}
			
			if(harvestTool != null && !harvestTool.isEmpty()){
				return harvestTool;
			}
		}
		
		return mainStack;
	}
	
	
	@Mod.EventBusSubscriber( modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Event_busHandler{
		@SubscribeEvent
		public void modifyBreakSpeed(PlayerEvent.BreakSpeed breakSpeedEvent) {
			if (!ConfigHandler.SERVER.bonuses.get() || !ConfigHandler.SERVER.clawsAreTools.get())
				return;
			PlayerEntity playerEntity = breakSpeedEvent.getPlayer();
			
			ItemStack mainStack = playerEntity.getMainHandItem();
			DragonStateHandler dragonStateHandler = DragonStateProvider.getCap(playerEntity).orElse(null);
			if(mainStack.getItem() instanceof TieredItem || dragonStateHandler == null || !dragonStateHandler.isDragon()) return;
			
			BlockState blockState = breakSpeedEvent.getState();
			Item item = mainStack.getItem();
			
			float originalSpeed = breakSpeedEvent.getOriginalSpeed();
			
			if(!(item instanceof ToolItem)){
				for (int i = 1; i < 4; i++) {
					if (blockState.getHarvestTool() == null || blockState.getHarvestTool() == DragonStateHandler.CLAW_TOOL_TYPES[i]) {
						ItemStack breakingItem = dragonStateHandler.getClawInventory().getClawsInventory().getItem(i);
						if (breakingItem != null && !breakingItem.isEmpty()) {
							return;
						}
					}
				}
			}
			
			if (!(item instanceof ToolItem || item instanceof SwordItem || item instanceof ShearsItem)) {
				float bonus = dragonStateHandler.getLevel() == DragonLevel.ADULT ? (
						blockState.getHarvestTool() == ToolType.AXE && dragonStateHandler.getType() == DragonType.FOREST ? 4 :
						blockState.getHarvestTool() == ToolType.PICKAXE && dragonStateHandler.getType() == DragonType.CAVE ? 4 :
						blockState.getHarvestTool() == ToolType.SHOVEL && dragonStateHandler.getType() == DragonType.SEA ? 4 : 2F
						) : dragonStateHandler.getLevel() == DragonLevel.BABY ? ConfigHandler.SERVER.bonusUnlockedAt.get() != DragonLevel.BABY ? 2F : 1F
						: dragonStateHandler.getLevel() == DragonLevel.YOUNG ? ConfigHandler.SERVER.bonusUnlockedAt.get() == DragonLevel.ADULT && dragonStateHandler.getLevel() != DragonLevel.BABY ? 2F : 1F
						: 2F;
				
				breakSpeedEvent.setNewSpeed((originalSpeed * bonus));
			}
		}
	}
}
