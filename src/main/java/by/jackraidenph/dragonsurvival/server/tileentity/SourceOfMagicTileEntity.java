package by.jackraidenph.dragonsurvival.server.tileentity;

import by.jackraidenph.dragonsurvival.common.blocks.DSBlocks;
import by.jackraidenph.dragonsurvival.common.blocks.SourceOfMagicBlock;
import by.jackraidenph.dragonsurvival.common.items.DSItems;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.misc.DragonType;
import by.jackraidenph.dragonsurvival.server.containers.SourceOfMagicContainer;
import by.jackraidenph.dragonsurvival.util.Functions;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.HashMap;

public class SourceOfMagicTileEntity extends BaseBlockTileEntity implements ITickableTileEntity, INamedContainerProvider, IAnimatable, IInventory
{
    public DragonType type = DragonType.NONE;
    public NonNullList<ItemStack> stacks = NonNullList.withSize(1, ItemStack.EMPTY);
    
    private final AnimationFactory manager = new AnimationFactory(this);
    
    public SourceOfMagicTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }
    
    public static HashMap<Item, Integer> consumables = new HashMap<>();
    
    static {
        consumables.put(DSItems.elderDragonDust, Functions.secondsToTicks(ConfigHandler.SERVER.elderDragonDustTime.get()));
        consumables.put(DSItems.elderDragonBone, Functions.secondsToTicks(ConfigHandler.SERVER.elderDragonBoneTime.get()));
        consumables.put(DSItems.dragonHeartShard, Functions.secondsToTicks(ConfigHandler.SERVER.weakHeartShardTime.get()));
        consumables.put(DSItems.weakDragonHeart, Functions.secondsToTicks(ConfigHandler.SERVER.weakDragonHeartTime.get()));
        consumables.put(DSItems.elderDragonHeart, Functions.secondsToTicks(ConfigHandler.SERVER.elderDragonHeartTime.get()));
    }
    
    @Override
    public void tick() {
        if(getBlockState().getBlock() == DSBlocks.seaSourceOfMagic){
            type = DragonType.SEA;
        }else if(getBlockState().getBlock() == DSBlocks.forestSourceOfMagic){
            type = DragonType.FOREST;
        }else if(getBlockState().getBlock() == DSBlocks.caveSourceOfMagic){
            type = DragonType.CAVE;
        }
        
        BlockState state = getBlockState();
        
        if(!state.getValue(SourceOfMagicBlock.FILLED) && !isEmpty()){
            level.setBlockAndUpdate(getBlockPos(), state.setValue(SourceOfMagicBlock.FILLED, true));
        }else if(state.getValue(SourceOfMagicBlock.FILLED) && isEmpty()){
            level.setBlockAndUpdate(getBlockPos(), state.setValue(SourceOfMagicBlock.FILLED, false));
        }
    }
    
    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putString("Type", type.name());
        ItemStackHelper.saveAllItems(compound, stacks);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        type = DragonType.valueOf(compound.getString("Type"));
        this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.stacks);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Source Of Magic");
    }


    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
        buffer.writeBlockPos(worldPosition);
        return new SourceOfMagicContainer(p_createMenu_1_, p_createMenu_2_, buffer);
    }
    
    @Override
    public void registerControllers(AnimationData data)
    {
    }
    
    @Override
    public AnimationFactory getFactory()
    {
        return manager;
    }
    
    @Override
    public int getContainerSize()
    {
        return 1;
    }
    
    @Override
    public boolean isEmpty()
    {
        return stacks.isEmpty() || getItem(0).isEmpty();
    }
    
    @Override
    public ItemStack getItem(int i)
    {
        return stacks.get(i);
    }
    
    @Override
    public ItemStack removeItem(int i, int i1)
    {
        return ItemStackHelper.removeItem(stacks, i, i1);
    }
    
    @Override
    public ItemStack removeItemNoUpdate(int i)
    {
        return ItemStackHelper.takeItem(this.stacks, 0);
    }
    
    @Override
    public void setItem(int i, ItemStack itemStack)
    {
        if (i >= 0 && i < this.stacks.size()) {
            this.stacks.set(i, itemStack);
        }
    }
    
    @Override
    public boolean stillValid(PlayerEntity playerEntity)
    {
        return true;
    }
    
    @Override
    public void clearContent()
    {
        stacks.clear();
    }
}
