package by.jackraidenph.dragonsurvival.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerStateProvider implements ICapabilitySerializable {

    @CapabilityInject(IPlayerStateHandler.class)
    public static Capability<IPlayerStateHandler> PLAYER_STATE_HANDLER_CAPABILITY = null;
    private LazyOptional<IPlayerStateHandler> instance = LazyOptional.of(PLAYER_STATE_HANDLER_CAPABILITY::getDefaultInstance);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (side == Direction.DOWN)
            return cap == PLAYER_STATE_HANDLER_CAPABILITY ? instance.cast() : LazyOptional.empty();
        else return LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return PLAYER_STATE_HANDLER_CAPABILITY.getStorage().writeNBT(PLAYER_STATE_HANDLER_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), Direction.DOWN);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        PLAYER_STATE_HANDLER_CAPABILITY.getStorage().readNBT(PLAYER_STATE_HANDLER_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), Direction.DOWN, nbt);
    }
}
