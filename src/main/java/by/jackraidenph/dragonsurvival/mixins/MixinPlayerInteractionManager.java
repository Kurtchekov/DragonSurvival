package by.jackraidenph.dragonsurvival.mixins;

import by.jackraidenph.dragonsurvival.common.handlers.magic.ClawToolHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin( PlayerInteractionManager.class)
public class MixinPlayerInteractionManager
{
	@Redirect( method = "destroyBlock",
	           at = @At( value="INVOKE", target="Lnet/minecraft/entity/player/ServerPlayerEntity;getMainHandItem()Lnet/minecraft/item/ItemStack;"))
	public ItemStack getTools(ServerPlayerEntity player)
	{
		return ClawToolHandler.getDragonTools(player);
	}
}
