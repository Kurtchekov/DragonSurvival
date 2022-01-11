package by.jackraidenph.dragonsurvival.common.blocks;

import by.jackraidenph.dragonsurvival.client.particles.TreasureParticleData;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.network.NetworkHandler;
import by.jackraidenph.dragonsurvival.network.status.SyncTreasureRestStatus;
import net.minecraft.block.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class TreasureBlock extends FallingBlock implements IWaterLoggable
{
	public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	protected static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[]{VoxelShapes.empty(), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};
	
	private Color effectColor;
	
	public TreasureBlock(Color c, Properties p_i48328_1_)
	{
		super(p_i48328_1_);
		this.registerDefaultState(this.stateDefinition.any().setValue(LAYERS, Integer.valueOf(1)).setValue(WATERLOGGED, false));
		this.effectColor = c;
	}
	@Override
	public void appendHoverText(ItemStack p_190948_1_,
			@Nullable
					IBlockReader p_190948_2_, List<ITextComponent> p_190948_3_, ITooltipFlag p_190948_4_)
	{
		super.appendHoverText(p_190948_1_, p_190948_2_, p_190948_3_, p_190948_4_);
		p_190948_3_.add(new TranslationTextComponent("ds.description.treasures"));
	}
	
	@Override
	public boolean isBed(BlockState state, IBlockReader world, BlockPos pos, @Nullable Entity player)
	{
		return true;
	}
	
	public Optional<Vector3d> getBedSpawnPosition(EntityType<?> entityType, BlockState state, IWorldReader world, BlockPos pos, float orientation, @Nullable
			LivingEntity sleeper)
	{
		if (world instanceof World)
		{
			return RespawnAnchorBlock.findStandUpPosition(entityType, world, pos);
		}
		
		return Optional.empty();
	}
	
	public boolean isPossibleToRespawnInThis() {
		return true;
	}
	
	public void tick(BlockState p_225534_1_, ServerWorld p_225534_2_, BlockPos p_225534_3_, Random p_225534_4_) {
		boolean belowEmpty = p_225534_2_.isEmptyBlock(p_225534_3_.below()) || (isFree(p_225534_2_.getBlockState(p_225534_3_.below()))) && p_225534_3_.getY() >= 0;
		boolean lowerLayer = p_225534_2_.getBlockState(p_225534_3_.below()).getBlock() == p_225534_1_.getBlock() && p_225534_2_.getBlockState(p_225534_3_.below()).getValue(LAYERS) < 8;
		if (belowEmpty || lowerLayer) {
			FallingBlockEntity fallingblockentity = new FallingBlockEntity(p_225534_2_, (double)p_225534_3_.getX() + 0.5D, (double)p_225534_3_.getY(), (double)p_225534_3_.getZ() + 0.5D, p_225534_2_.getBlockState(p_225534_3_)){
				@Override
				public void tick()
				{
					BlockState state = level.getBlockState(blockPosition().below());
					
					if(state.getBlock() == getBlockState().getBlock()){
						int i = state.getValue(LAYERS);
						
						if(i > 0 && i < 8){
							int missingLayers = 8 - i;
							int newLayers = getBlockState().getValue(LAYERS);
							int leftOver = 0;
							
							if(newLayers > missingLayers){
								leftOver = newLayers - missingLayers;
								newLayers = missingLayers;
							}
							
							level.setBlockAndUpdate(blockPosition().below(), state.setValue(LAYERS, Integer.valueOf(Math.min(8, i + newLayers))));
							
							if(leftOver > 0){
								p_225534_2_.setBlock(blockPosition(), getBlock().defaultBlockState().setValue(LAYERS, Integer.valueOf(Math.min(8, leftOver))), 3);
								
							}else {
								p_225534_2_.setBlock(p_225534_3_, Blocks.AIR.defaultBlockState(), 3);
							}
							
							this.remove();
							return;
						}
					}
					
					super.tick();
				}
			};
			this.falling(fallingblockentity);
			p_225534_2_.addFreshEntity(fallingblockentity);
			
		}
	}
	
	public boolean isPathfindable(BlockState p_196266_1_, IBlockReader p_196266_2_, BlockPos p_196266_3_, PathType p_196266_4_) {
		switch(p_196266_4_) {
			case LAND:
				return p_196266_1_.getValue(LAYERS) < 5;
			case WATER:
				return false;
			case AIR:
				return false;
			default:
				return false;
		}
	}
	
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return SHAPE_BY_LAYER[p_220053_1_.getValue(LAYERS)];
	}
	
	public VoxelShape getCollisionShape(BlockState p_220071_1_, IBlockReader p_220071_2_, BlockPos p_220071_3_, ISelectionContext p_220071_4_) {
		if(p_220071_4_.getEntity() instanceof FallingBlockEntity){
			return SHAPE_BY_LAYER[p_220071_1_.getValue(LAYERS)];
		}
		
		return SHAPE_BY_LAYER[Math.max(p_220071_1_.getValue(LAYERS)-1, 0)];
	}
	
	public VoxelShape getBlockSupportShape(BlockState p_230335_1_, IBlockReader p_230335_2_, BlockPos p_230335_3_) {
		return SHAPE_BY_LAYER[p_230335_1_.getValue(LAYERS)];
	}
	
	public VoxelShape getVisualShape(BlockState p_230322_1_, IBlockReader p_230322_2_, BlockPos p_230322_3_, ISelectionContext p_230322_4_) {
		return VoxelShapes.empty();
	}
	
	@OnlyIn(Dist.CLIENT)
	public float getShadeBrightness(BlockState p_220080_1_, IBlockReader p_220080_2_, BlockPos p_220080_3_) {
		return 1.0F;
	}
	
	public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
		return true;
	}
	
	public boolean useShapeForLightOcclusion(BlockState p_220074_1_) {
		return true;
	}
	
	@Override
	public ActionResultType use(BlockState p_225533_1_, World world, BlockPos p_225533_3_, PlayerEntity player, Hand hand, BlockRayTraceResult p_225533_6_)
	{
		if(DragonStateProvider.isDragon(player) && player.getItemInHand(hand).isEmpty()){
			if(player.getFeetBlockState().getBlock() == p_225533_1_.getBlock()) {
				DragonStateHandler handler = DragonStateProvider.getCap(player).orElse(null);
				
				if(handler != null) {
					if (!handler.treasureResting) {
						if (world.isClientSide) {
							NetworkHandler.CHANNEL.sendToServer(new SyncTreasureRestStatus(player.getId(), true));
						}
						
						return ActionResultType.SUCCESS;
					}
					
					if(!world.isClientSide) {
						player.resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
						ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)player;
						if (serverplayerentity.getRespawnPosition() == null
						    || serverplayerentity.getRespawnDimension() != world.dimension()
						    || serverplayerentity.getRespawnPosition() != null && !serverplayerentity.getRespawnPosition().equals(p_225533_3_) && serverplayerentity.getRespawnPosition().distSqr(p_225533_3_) > 40) {
							serverplayerentity.setRespawnPosition(world.dimension(), p_225533_3_, 0.0F, false, true);
							return ActionResultType.SUCCESS;
						}
					}
				}
			}
		}
		
		return super.use(p_225533_1_, world, p_225533_3_, player, hand, p_225533_6_);
	}
	
	public boolean canBeReplaced(BlockState p_196253_1_, BlockItemUseContext p_196253_2_) {
		int i = p_196253_1_.getValue(LAYERS);
		
		if (p_196253_2_.getItemInHand().getItem() == this.asItem() && i < 8) {
			if (p_196253_2_.replacingClickedOnBlock()) {
				return p_196253_2_.getClickedFace() == Direction.UP;
			}
		}
		
		return false;
	}
	
	@Override
	public void onBroken(World world, BlockPos pos, FallingBlockEntity entity)
	{
		BlockState state = world.getBlockState(pos);
		if(state.getBlock() instanceof TreasureBlock){
			if(state.getBlock() == entity.getBlockState().getBlock()){
				int i = state.getValue(LAYERS);
				world.setBlockAndUpdate(pos, state.setValue(LAYERS, Integer.valueOf(Math.min(8, i + entity.getBlockState().getValue(LAYERS)))));
			}
		}
	}
	
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
		BlockState blockstate = p_196258_1_.getLevel().getBlockState(p_196258_1_.getClickedPos());
		if (blockstate.is(this)) {
			int i = blockstate.getValue(LAYERS);
			return blockstate.setValue(LAYERS, Integer.valueOf(Math.min(8, i + 1))).setValue(WATERLOGGED, p_196258_1_.getLevel().getFluidState(p_196258_1_.getClickedPos()).getType() == Fluids.WATER);
		} else {
			return super.getStateForPlacement(p_196258_1_).setValue(WATERLOGGED, p_196258_1_.getLevel().getFluidState(p_196258_1_.getClickedPos()).getType() == Fluids.WATER);
		}
	}
	
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
		p_206840_1_.add(LAYERS);
		p_206840_1_.add(WATERLOGGED);
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction dir, BlockState state2, IWorld level, BlockPos pos, BlockPos pos2) {
		if (state.getValue(WATERLOGGED)) {
			level.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		return super.updateShape(state, dir, state2, level, pos, pos2);
	}
	
	@OnlyIn( Dist.CLIENT)
	public void animateTick(BlockState block, World world, BlockPos pos, Random random)
	{
		double d1 = random.nextDouble();
		double d2 = (block.getValue(LAYERS)) * (1.0 / 8) + .1;
		double d3 = random.nextDouble();
		
		if (world.isEmptyBlock(pos.above())) {
			if (random.nextInt(100) < 35) {
				world.addParticle(new TreasureParticleData(effectColor.getRed() / 255F, effectColor.getGreen() / 255F, effectColor.getBlue() / 255F, 1F), (double)pos.getX() + d1, (double)pos.getY() + d2, (double)pos.getZ() + d3, 0.0D, 0.0D, 0.0D);
			}
		}
	}
}
