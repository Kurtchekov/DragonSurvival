package by.jackraidenph.dragonsurvival.common.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ChargedCoalItem extends Item
{
	public ChargedCoalItem(Properties p_i48487_1_)
	{
		super(p_i48487_1_);
	}
	
	@Override
	public int getBurnTime(ItemStack itemStack) {
		return 4000;
	}
	
	@Override
	public void appendHoverText(ItemStack p_77624_1_,
			@Nullable
					World p_77624_2_, List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_)
	{
		super.appendHoverText(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
		p_77624_3_.add(new TranslationTextComponent("ds.description.chargedCoal"));
	}
}
