package by.jackraidenph.dragonsurvival.capability;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityStorage implements Capability.IStorage<DragonStateHandler> {
    private static CompoundNBT writeVec3d(Vec3d vec) {
        CompoundNBT comp = new CompoundNBT();
        comp.putDouble("X", vec.x);
        comp.putDouble("Y", vec.y);
        comp.putDouble("Z", vec.z);
        return comp;
    }

    private static Vec3d getVec3d(INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        Vec3d vec = Vec3d.ZERO;
        if (tag != null && tag.contains("X"))
            vec = new Vec3d(tag.getDouble("X"), tag.getDouble("Y"), tag.getDouble("Z"));
        return vec;
    }

    @Override
    public INBT writeNBT(Capability<DragonStateHandler> capability, DragonStateHandler instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("isDragon", instance.isDragon());
        if (instance.isDragon()) {
            DragonStateHandler.DragonMovementData movementData = instance.getMovementData();

            tag.putDouble("bodyYaw", movementData.bodyYaw);
            tag.putDouble("headYaw", movementData.headYaw);
            tag.putDouble("headPitch", movementData.headPitch);

            tag.putBoolean("isHiding", instance.isHiding());
            tag.putString("type", instance.getType().toString());
            tag.putString("level", instance.getLevel().toString());
            tag.putInt("mana", instance.getCurrentMana());
            tag.putInt("maxMana", instance.getMaxMana());

            tag.putInt("selectedAbilitySlot", instance.getSelectedAbilitySlot());

            tag.putString("abilitySlot1", instance.getAbilityFromSlot(0).getId());
            tag.putString("abilitySlot2", instance.getAbilityFromSlot(1).getId());
            tag.putString("abilitySlot3", instance.getAbilityFromSlot(2).getId());
            tag.putString("abilitySlot4", instance.getAbilityFromSlot(3).getId());
            tag.putString("abilitySlot5", instance.getAbilityFromSlot(4).getId());
            tag.put("unlockedAbilityContainer", instance.getUnlockedAbilities().serializeNBT());
            tag.putFloat("Health", instance.getHealth());
            tag.putBoolean("Has wings", instance.hasWings());
        }
        return tag;
    }

    @Override
    public void readNBT(Capability<DragonStateHandler> capability, DragonStateHandler instance, Direction side, INBT base) {
        CompoundNBT tag = (CompoundNBT) base;
        instance.setIsDragon(tag.getBoolean("isDragon"));
        if (tag.getBoolean("isDragon")) {
            instance.setMovementData(tag.getDouble("bodyYaw"), tag.getDouble("headYaw"),
                    tag.getDouble("headPitch"));
            instance.setIsHiding(tag.getBoolean("isHiding"));
            instance.setType(DragonType.valueOf(tag.getString("type")));
            String level = tag.getString("level");
            if (level.isEmpty())
                instance.setLevel(DragonLevel.BABY);
            else
                instance.setLevel(DragonLevel.valueOf(level));
            instance.setSelectedAbilitySlot(tag.getInt("selectedAbilitySlot"));
            instance.setMaxMana(tag.getInt("maxMana"));
            instance.setCurrentMana(tag.getInt("mana"));

            instance.setAbilityInSlot(DragonSurvivalMod.ABILITY_TYPES.get(tag.getString("abilitySlot1")).create(null), 0);
            instance.setAbilityInSlot(DragonSurvivalMod.ABILITY_TYPES.get(tag.getString("abilitySlot2")).create(null), 1);
            instance.setAbilityInSlot(DragonSurvivalMod.ABILITY_TYPES.get(tag.getString("abilitySlot3")).create(null), 2);
            instance.setAbilityInSlot(DragonSurvivalMod.ABILITY_TYPES.get(tag.getString("abilitySlot4")).create(null), 3);
            instance.setAbilityInSlot(DragonSurvivalMod.ABILITY_TYPES.get(tag.getString("abilitySlot5")).create(null), 4);

            instance.getUnlockedAbilities().deserializeNBT(tag.get("unlockedAbilityContainer"));
            instance.setHealth(tag.getFloat("Health"));
            instance.setHasWings(tag.getBoolean("Has wings"));
        }
    }
}
