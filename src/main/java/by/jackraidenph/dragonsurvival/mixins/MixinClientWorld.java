package by.jackraidenph.dragonsurvival.mixins;

import by.jackraidenph.dragonsurvival.common.blocks.SourceOfMagicBlock;
import by.jackraidenph.dragonsurvival.server.tileentity.SourceOfMagicPlaceholder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.profiler.IProfiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISpawnWorldInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin( ClientWorld.class )
public abstract class MixinClientWorld extends World
{
	@Shadow
	@Final
	private WorldRenderer levelRenderer;
	
	protected MixinClientWorld(ISpawnWorldInfo p_i241925_1_, RegistryKey<World> p_i241925_2_, DimensionType p_i241925_3_, Supplier<IProfiler> p_i241925_4_, boolean p_i241925_5_, boolean p_i241925_6_, long p_i241925_7_)
	{
		super(p_i241925_1_, p_i241925_2_, p_i241925_3_, p_i241925_4_, p_i241925_5_, p_i241925_6_, p_i241925_7_);
	}
	
	@Inject( at = @At("HEAD"), method = "destroyBlockProgress", cancellable = true)
	public void destroyBlockProgress(int playerId, BlockPos pos, int progress, CallbackInfo ci) {
		BlockState state = getBlockState(pos);
		
		if(state.getBlock() instanceof SourceOfMagicBlock){
			if(!state.getValue(SourceOfMagicBlock.PRIMARY_BLOCK)){
				TileEntity blockEntity = getBlockEntity(pos);
				BlockPos pos1 = pos;
				
				if(blockEntity instanceof SourceOfMagicPlaceholder){
					pos1 = ((SourceOfMagicPlaceholder)blockEntity).rootPos;
				}
				
				this.levelRenderer.destroyBlockProgress(playerId, pos1, progress);
				ci.cancel();
			}
		}
	}
}
