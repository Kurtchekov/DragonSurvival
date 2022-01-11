package by.jackraidenph.dragonsurvival.common.items;

import by.jackraidenph.dragonsurvival.common.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.network.NetworkHandler;
import by.jackraidenph.dragonsurvival.network.entity.player.SynchronizeDragonCap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;

public class WingGrantItem extends Item
{
	public WingGrantItem(Properties p_i48487_1_)
	{
		super(p_i48487_1_);
	}
	
	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand p_77659_3_)
	{
		DragonStateHandler handler = DragonStateProvider.getCap(player).orElse(null);
		
		if (handler != null && handler.isDragon()) {
			if(!world.isClientSide) {
				handler.setHasWings(!handler.hasWings());
				NetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new SynchronizeDragonCap(player.getId(), handler.isHiding(), handler.getType(), handler.getSize(), handler.hasWings(), handler.getLavaAirSupply(), handler.getPassengerId()));
				
				if (!player.isCreative()) {
					player.getItemInHand(p_77659_3_).shrink(1);
				}
			}
			
			player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1F, 0F);
			return ActionResult.success(player.getItemInHand(p_77659_3_));
		}
		
		return super.use(world, player, p_77659_3_);
	}
	
	@Override
	public void appendHoverText(ItemStack p_77624_1_,
			@Nullable
					World p_77624_2_, List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_)
	{
		super.appendHoverText(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
		p_77624_3_.add(new TranslationTextComponent("ds.description.wing_grant"));
	}
}
