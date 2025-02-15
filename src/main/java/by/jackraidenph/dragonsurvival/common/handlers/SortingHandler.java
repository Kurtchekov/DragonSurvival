package by.jackraidenph.dragonsurvival.common.handlers;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.*;
import java.util.function.Predicate;


/**
 *  Copied from Quark
 *  Source: https://github.com/VazkiiMods/Quark/blob/fae8f48eae8157424dcf4f97f9be186d567a344d/src/main/java/vazkii/quark/base/handler/SortingHandler.java
 *  License: https://github.com/VazkiiMods/Quark/blob/master/LICENSE.md
 */
public final class SortingHandler {
	
	private static final Comparator<ItemStack> FALLBACK_COMPARATOR = jointComparator(
			Comparator.comparingInt((ItemStack s) -> Item.getId(s.getItem())),
			SortingHandler::damageCompare,
			(ItemStack s1, ItemStack s2) -> s2.getCount() - s1.getCount(),
			(ItemStack s1, ItemStack s2) -> s2.hashCode() - s1.hashCode());
	
	private static final Comparator<ItemStack> FOOD_COMPARATOR = jointComparator(
			SortingHandler::foodHealCompare,
			SortingHandler::foodSaturationCompare);
	
	private static final Comparator<ItemStack> TOOL_COMPARATOR = jointComparator(
			SortingHandler::toolPowerCompare,
			SortingHandler::enchantmentCompare,
			SortingHandler::damageCompare);
	
	private static final Comparator<ItemStack> SWORD_COMPARATOR = jointComparator(
			SortingHandler::swordPowerCompare,
			SortingHandler::enchantmentCompare,
			SortingHandler::damageCompare);
	
	private static final Comparator<ItemStack> ARMOR_COMPARATOR = jointComparator(
			SortingHandler::armorSlotAndToughnessCompare,
			SortingHandler::enchantmentCompare,
			SortingHandler::damageCompare);
	
	private static final Comparator<ItemStack> BOW_COMPARATOR = jointComparator(
			SortingHandler::enchantmentCompare,
			SortingHandler::damageCompare);
	
	public static void sortInventory(PlayerEntity player) {
		Container c = player.containerMenu;
		
		for (Slot s : c.slots) {
			IInventory inv = s.container;
			if (inv == player.inventory) {
				InvWrapper wrapper = new InvWrapper(inv);
				sortInventory(wrapper, 9, 36);
				break;
			}
		}
	}
	
	public static void sortInventory(IItemHandler handler, int iStart, int iEnd) {
		List<ItemStack> stacks = new ArrayList<>();
		List<ItemStack> restore = new ArrayList<>();
		
		for (int i = iStart; i < iEnd; i++) {
			ItemStack stackAt = handler.getStackInSlot(i);
			restore.add(stackAt.copy());
			if (!stackAt.isEmpty())
				stacks.add(stackAt.copy());
		}
		
		mergeStacks(stacks);
		sortStackList(stacks);
		
		if (setInventory(handler, stacks, iStart, iEnd) == ActionResultType.FAIL)
			setInventory(handler, restore, iStart, iEnd);
	}
	
	private static ActionResultType setInventory(IItemHandler inventory, List<ItemStack> stacks, int iStart, int iEnd) {
		for (int i = iStart; i < iEnd; i++) {
			int j = i - iStart;
			ItemStack stack = j >= stacks.size() ? ItemStack.EMPTY : stacks.get(j);
			
			if (!stack.isEmpty() && !inventory.isItemValid(i, stack))
				return ActionResultType.PASS;
		}
		
		for (int i = iStart; i < iEnd; i++) {
			int j = i - iStart;
			ItemStack stack = j >= stacks.size() ? ItemStack.EMPTY : stacks.get(j);
			
			inventory.extractItem(i, inventory.getSlotLimit(i), false);
			if (!stack.isEmpty())
				if (!inventory.insertItem(i, stack, false).isEmpty())
					return ActionResultType.FAIL;
		}
		
		return ActionResultType.SUCCESS;
	}
	
	public static void mergeStacks(List<ItemStack> list) {
		for (int i = 0; i < list.size(); i++) {
			ItemStack set = mergeStackWithOthers(list, i);
			list.set(i, set);
		}
		
		list.removeIf((ItemStack stack) -> stack.isEmpty() || stack.getCount() == 0);
	}
	
	private static ItemStack mergeStackWithOthers(List<ItemStack> list, int index) {
		ItemStack stack = list.get(index);
		if (stack.isEmpty())
			return stack;
		
		for (int i = 0; i < list.size(); i++) {
			if (i == index)
				continue;
			
			ItemStack stackAt = list.get(i);
			if (stackAt.isEmpty())
				continue;
			
			if (stackAt.getCount() < stackAt.getMaxStackSize() && ItemStack.isSame(stack, stackAt) && ItemStack.tagMatches(stack, stackAt)) {
				int setSize = stackAt.getCount() + stack.getCount();
				int carryover = Math.max(0, setSize - stackAt.getMaxStackSize());
				stackAt.setCount(carryover);
				stack.setCount(setSize - carryover);
				
				if (stack.getCount() == stack.getMaxStackSize())
					return stack;
			}
		}
		
		return stack;
	}
	
	public static void sortStackList(List<ItemStack> list) {
		list.sort(SortingHandler::stackCompare);
	}
	
	private static int stackCompare(ItemStack stack1, ItemStack stack2) {
		if (stack1 == stack2)
			return 0;
		if (stack1.isEmpty())
			return -1;
		if (stack2.isEmpty())
			return 1;
		
		ItemType type1 = getType(stack1);
		ItemType type2 = getType(stack2);
		
		if (type1 == type2)
			return type1.comparator.compare(stack1, stack2);
		
		return type1.ordinal() - type2.ordinal();
	}
	
	private static ItemType getType(ItemStack stack) {
		for (ItemType type : ItemType.values())
			if (type.fitsInType(stack))
				return type;
		
		throw new RuntimeException("Having an ItemStack that doesn't fit in any type is impossible.");
	}
	
	private static Predicate<ItemStack> classPredicate(Class<? extends Item> clazz) {
		return (ItemStack s) -> !s.isEmpty() && clazz.isInstance(s.getItem());
	}
	
	private static Predicate<ItemStack> inverseClassPredicate(Class<? extends Item> clazz) {
		return classPredicate(clazz).negate();
	}
	
	private static Predicate<ItemStack> itemPredicate(List<Item> list) {
		return (ItemStack s) -> !s.isEmpty() && list.contains(s.getItem());
	}
	
	public static Comparator<ItemStack> jointComparator(Comparator<ItemStack> finalComparator, Comparator<ItemStack>[] otherComparators) {
		if (otherComparators == null)
			return jointComparator(finalComparator);
		
		Comparator<ItemStack>[] resizedArray = Arrays.copyOf(otherComparators, otherComparators.length + 1);
		resizedArray[otherComparators.length] = finalComparator;
		return jointComparator(resizedArray);
	}
	
	@SafeVarargs
	public static Comparator<ItemStack> jointComparator(Comparator<ItemStack>... comparators) {
		return jointComparatorFallback((ItemStack s1, ItemStack s2) -> {
			for (Comparator<ItemStack> comparator : comparators) {
				if (comparator == null)
					continue;
				
				int compare = comparator.compare(s1, s2);
				if (compare == 0)
					continue;
				
				return compare;
			}
			
			return 0;
		}, FALLBACK_COMPARATOR);
	}
	
	private static Comparator<ItemStack> jointComparatorFallback(Comparator<ItemStack> comparator, Comparator<ItemStack> fallback) {
		return (ItemStack s1, ItemStack s2) -> {
			int compare = comparator.compare(s1, s2);
			if (compare == 0)
				return fallback == null ? 0 : fallback.compare(s1, s2);
			
			return compare;
		};
	}
	
	private static Comparator<ItemStack> listOrderComparator(List<Item> list) {
		return (ItemStack stack1, ItemStack stack2) -> {
			Item i1 = stack1.getItem();
			Item i2 = stack2.getItem();
			if (list.contains(i1)) {
				if (list.contains(i2))
					return list.indexOf(i1) - list.indexOf(i2);
				return 1;
			}
			
			if (list.contains(i2))
				return -1;
			
			return 0;
		};
	}
	
	private static List<Item> list(Object... items) {
		List<Item> itemList = new ArrayList<>();
		for (Object o : items)
			if (o != null) {
				if (o instanceof Item)
					itemList.add((Item) o);
				else if (o instanceof Block)
					itemList.add(((Block) o).asItem());
				else if (o instanceof ItemStack)
					itemList.add(((ItemStack) o).getItem());
				else if (o instanceof String) {
					Registry.ITEM.getOptional(new ResourceLocation((String) o)).ifPresent(itemList::add);
				}
			}
		
		return itemList;
	}
	
	private static int foodHealCompare(ItemStack stack1, ItemStack stack2) {
		return stack2.getItem().getFoodProperties().getNutrition() - stack1.getItem().getFoodProperties().getNutrition();
	}
	
	private static int foodSaturationCompare(ItemStack stack1, ItemStack stack2) {
		return (int) (stack2.getItem().getFoodProperties().getSaturationModifier() * 100 - stack1.getItem().getFoodProperties().getSaturationModifier() * 100);
	}
	
	private static int enchantmentCompare(ItemStack stack1, ItemStack stack2) {
		return enchantmentPower(stack2) - enchantmentPower(stack1);
	}
	
	private static int enchantmentPower(ItemStack stack) {
		if (!stack.isEnchanted())
			return 0;
		
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
		int total = 0;
		
		for (Integer i : enchantments.values())
			total += i;
		
		return total;
	}
	
	private static int toolPowerCompare(ItemStack stack1, ItemStack stack2) {
		IItemTier mat1 = ((ToolItem) stack1.getItem()).getTier();
		IItemTier mat2 = ((ToolItem) stack2.getItem()).getTier();
		return (int) (mat2.getSpeed() * 100 - mat1.getSpeed() * 100);
	}
	
	private static int swordPowerCompare(ItemStack stack1, ItemStack stack2) {
		IItemTier mat1 = ((SwordItem) stack1.getItem()).getTier();
		IItemTier mat2 = ((SwordItem) stack2.getItem()).getTier();
		return (int) (mat2.getAttackDamageBonus() * 100 - mat1.getAttackDamageBonus() * 100);
	}
	
	private static int armorSlotAndToughnessCompare(ItemStack stack1, ItemStack stack2) {
		ArmorItem armor1 = (ArmorItem) stack1.getItem();
		ArmorItem armor2 = (ArmorItem) stack2.getItem();
		
		EquipmentSlotType slot1 = armor1.getSlot();
		EquipmentSlotType slot2 = armor2.getSlot();
		
		if (slot1 == slot2)
			return armor2.getMaterial().getDefenseForSlot(slot2) - armor2.getMaterial().getDefenseForSlot(slot1);
		
		return slot2.getIndex() - slot1.getIndex();
	}
	
	public static int damageCompare(ItemStack stack1, ItemStack stack2) {
		return stack1.getDamageValue() - stack2.getDamageValue();
	}
	
	private enum ItemType {
		
		FOOD(ItemStack::isEdible, FOOD_COMPARATOR),
		TORCH(list(Blocks.TORCH)),
		TOOL_PICKAXE(classPredicate(PickaxeItem.class), TOOL_COMPARATOR),
		TOOL_SHOVEL(classPredicate(ShovelItem.class), TOOL_COMPARATOR),
		TOOL_AXE(classPredicate(AxeItem.class), TOOL_COMPARATOR),
		TOOL_SWORD(classPredicate(SwordItem.class), SWORD_COMPARATOR),
		TOOL_GENERIC(classPredicate(ToolItem.class), TOOL_COMPARATOR),
		ARMOR(classPredicate(ArmorItem.class), ARMOR_COMPARATOR),
		BOW(classPredicate(BowItem.class), BOW_COMPARATOR),
		CROSSBOW(classPredicate(CrossbowItem.class), BOW_COMPARATOR),
		TRIDENT(classPredicate(TridentItem.class), BOW_COMPARATOR),
		ARROWS(classPredicate(ArrowItem.class)),
		POTION(classPredicate(PotionItem.class)),
		MINECART(classPredicate(MinecartItem.class)),
		RAIL(list(Blocks.RAIL, Blocks.POWERED_RAIL, Blocks.DETECTOR_RAIL, Blocks.ACTIVATOR_RAIL)),
		DYE(classPredicate(DyeItem.class)),
		ANY(inverseClassPredicate(BlockItem.class)),
		BLOCK(classPredicate(BlockItem.class));
		
		private final Predicate<ItemStack> predicate;
		private final Comparator<ItemStack> comparator;
		
		@SafeVarargs
		@SuppressWarnings("varargs")
		ItemType(List<Item> list, Comparator<ItemStack>... comparators) {
			this(itemPredicate(list), jointComparator(listOrderComparator(list), comparators));
		}
		
		ItemType(Predicate<ItemStack> predicate) {
			this(predicate, FALLBACK_COMPARATOR);
		}
		
		ItemType(Predicate<ItemStack> predicate, Comparator<ItemStack> comparator) {
			this.predicate = predicate;
			this.comparator = comparator;
		}
		
		public boolean fitsInType(ItemStack stack) {
			return predicate.test(stack);
		}
		
	}
}