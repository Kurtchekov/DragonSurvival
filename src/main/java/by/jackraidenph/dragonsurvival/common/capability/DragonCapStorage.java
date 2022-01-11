package by.jackraidenph.dragonsurvival.common.capability;

import by.jackraidenph.dragonsurvival.misc.DragonLevel;
import by.jackraidenph.dragonsurvival.misc.DragonType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class DragonCapStorage implements Capability.IStorage<DragonStateHandler> {
    
    
    @Override
    public INBT writeNBT(Capability<DragonStateHandler> capability, DragonStateHandler instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("type", instance.getType().toString());
    
        if (instance.isDragon()) {
            DragonStateHandler.DragonMovementData movementData = instance.getMovementData();
            tag.putDouble("bodyYaw", movementData.bodyYaw);
            tag.putDouble("headYaw", movementData.headYaw);
            tag.putDouble("headPitch", movementData.headPitch);
            
            tag.putInt("spinCooldown", movementData.spinCooldown);
            tag.putInt("spinAttack", movementData.spinAttack);
            tag.putBoolean("spinLearned", movementData.spinLearned);
    
            tag.putBoolean("bite", movementData.bite);
            tag.putBoolean("dig", movementData.dig);
            
            DragonStateHandler.DragonDebuffData debuffData = instance.getDebuffData();
            tag.putDouble("timeWithoutWater", debuffData.timeWithoutWater);
            tag.putInt("timeInDarkness", debuffData.timeInDarkness);
            tag.putInt("timeInRain", debuffData.timeInRain);
            tag.putBoolean("isHiding", instance.isHiding());
            
            tag.putDouble("size", instance.getSize());
            tag.putBoolean("growing", instance.growing);
            
            tag.putBoolean("hasWings", instance.hasWings());
            tag.putBoolean("isFlying", instance.isWingsSpread());
            
            tag.putInt("lavaAirSupply", instance.getLavaAirSupply());
    
            tag.putBoolean("resting", instance.treasureResting);
            tag.putInt("restingTimer", instance.treasureRestTimer);
    
            tag.putDouble("caveSize", instance.caveSize);
            tag.putDouble("seaSize", instance.seaSize);
            tag.putDouble("forestSize", instance.forestSize);
    
            tag.putBoolean("caveWings", instance.caveWings);
            tag.putBoolean("seaWings", instance.seaWings);
            tag.putBoolean("forestWings", instance.forestWings);
            
            tag.put("clawInv", instance.getClawInventory().writeNBT(capability, side));
            tag.put("emotes", instance.getEmotes().writeNBT(capability, side));
            tag.put("magic", instance.getMagic().writeNBT(capability, side));
        }
        return tag;
    }

    @Override
    public void readNBT(Capability<DragonStateHandler> capability, DragonStateHandler instance, Direction side, INBT base) {
        CompoundNBT tag = (CompoundNBT) base;
        if (tag.getString("type").equals(""))
        	instance.setType(DragonType.NONE);
        else
        	instance.setType(DragonType.valueOf(tag.getString("type")));
        
        if (instance.isDragon()) {
            instance.setMovementData(tag.getDouble("bodyYaw"), tag.getDouble("headYaw"), tag.getDouble("headPitch"), tag.getBoolean("bite"));
    
            instance.setHasWings(tag.getBoolean("hasWings"));
            instance.setWingsSpread(tag.getBoolean("isFlying"));
            
            instance.getMovementData().dig = tag.getBoolean("dig");
            instance.getMovementData().spinCooldown = tag.getInt("spinCooldown");
            instance.getMovementData().spinAttack = tag.getInt("spinAttack");
            instance.getMovementData().spinLearned = tag.getBoolean("spinLearned");
    
            instance.setDebuffData(tag.getInt("timeWithoutWater"), tag.getInt("timeInDarkness"), tag.getInt("timeInRain"));
            instance.setIsHiding(tag.getBoolean("isHiding"));
            
            instance.setSize(tag.getDouble("size"));
            instance.growing =!tag.contains("growing") || tag.getBoolean("growing");
    
            instance.treasureResting = tag.getBoolean("resting");
            instance.treasureRestTimer = tag.getInt("restingTimer");
    
            instance.caveSize = tag.getDouble("caveSize");
            instance.seaSize = tag.getDouble("seaSize");
            instance.forestSize = tag.getDouble("forestSize");
    
            instance.caveWings = tag.getBoolean("caveWings");
            instance.seaWings = tag.getBoolean("seaWings");
            instance.forestWings = tag.getBoolean("forestWings");
            
            if(tag.contains("clawInv")) instance.getClawInventory().readNBT(capability, side, tag.get("clawInv"));
            if(tag.contains("emotes"))  instance.getEmotes().readNBT(capability, side, tag.get("emotes"));
            if(tag.contains("magic"))  instance.getMagic().readNBT(capability, side, tag.get("magic"));
    
            if (instance.getSize() == 0)
                instance.setSize(DragonLevel.BABY.size);
            
            instance.setLavaAirSupply(tag.getInt("lavaAirSupply"));
        }
    }
}
