package by.jackraidenph.dragonsurvival.common.capability.DragonCapabilities;

import by.jackraidenph.dragonsurvival.common.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.common.magic.DragonAbilities;
import by.jackraidenph.dragonsurvival.common.magic.common.ActiveDragonAbility;
import by.jackraidenph.dragonsurvival.common.magic.common.DragonAbility;
import by.jackraidenph.dragonsurvival.common.magic.common.PassiveDragonAbility;
import by.jackraidenph.dragonsurvival.misc.DragonType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import java.util.ArrayList;
import java.util.Objects;

public class MagicCap implements DragonCapability
{
	
	private DragonStateHandler instance;
	
	public MagicCap(DragonStateHandler instance)
	{
		this.instance = instance;
	}
	
	private ArrayList<DragonAbility> abilities = new ArrayList<>();
	
	private ActiveDragonAbility currentlyCasting = null;
	private int selectedAbilitySlot = 0;
	private int currentMana = 0;
	
	private boolean renderAbilities = true;
	
	public boolean onMagicSource = false;
	public int magicSourceTimer = 0;
	
	public void initAbilities(DragonType type){
		if(DragonAbilities.ACTIVE_ABILITIES.containsKey(type)) {
			if(instance.getType() != null && instance.getType() != DragonType.NONE) {
				if (!ConfigHandler.SERVER.saveAllAbilities.get()) {
					abilities.clear();
				}
			}
			top:
			for (DragonAbility ability : DragonAbilities.ABILITIES.get(type)) {
				if(ability.getMinLevel() <= 0 && ability instanceof PassiveDragonAbility) continue;
				
				for(DragonAbility ab : abilities){
					if(Objects.equals(ab.getId(), ability.getId())){
						continue top;
					}
				}
				
				
				DragonAbility newAbility = ability.createInstance();
				newAbility.setLevel(ability.getMinLevel());
				abilities.add(newAbility);
			}
		}
	}
	public int getCurrentMana() {
		return currentMana;
	}
	
	public void setCurrentMana(int currentMana) {
		this.currentMana = currentMana;
	}
	
	public ActiveDragonAbility getCurrentlyCasting()
	{
		return currentlyCasting;
	}
	
	public void setCurrentlyCasting(ActiveDragonAbility currentlyCasting)
	{
		this.currentlyCasting = currentlyCasting;
	}
	
	public DragonAbility getAbilityOrDefault(DragonAbility ability){
		DragonAbility ab = getAbility(ability);
		return ab != null ? ab : ability;
	}
	
	public DragonAbility getAbility(DragonAbility ability){
		if(ability != null && ability.getId() != null) {
			for (DragonAbility ab : getAbilities()) {
				if (ab != null && ab.getId() != null) {
					if (Objects.equals(ab.getId(), ability.getId())) {
						return ab;
					}
				}
			}
		}
		
		return null;
	}
	
	public int getAbilityLevel(DragonAbility ability){
		DragonAbility ab = getAbility(ability);
		return ab == null ? ability.getMinLevel() : ab.getLevel();
	}
	
	public ArrayList<DragonAbility> getAbilities()
	{
		if(abilities.size() <= 0 && this.instance.getType() != null && this.instance.getType() != DragonType.NONE){
			initAbilities(this.instance.getType());
		}
		
		return abilities;
	}
	
	public void addAbility(DragonAbility ability){
		abilities.removeIf((c) -> c.getId() == ability.getId());
		abilities.add(ability);
	}
	
	public ActiveDragonAbility getAbilityFromSlot(int slot) {
		ActiveDragonAbility dragonAbility = instance.getType() != null && DragonAbilities.ACTIVE_ABILITIES.get(instance.getType()) != null && DragonAbilities.ACTIVE_ABILITIES.get(instance.getType()).size() >= slot ? DragonAbilities.ACTIVE_ABILITIES.get(instance.getType()).get(slot) : null;
		ActiveDragonAbility actual = (ActiveDragonAbility)getAbility(dragonAbility);
		
		return actual == null ? dragonAbility : actual;
	}
	
	
	public void resetCasting(){
		for(int i = 0; i < 4; i++){
			if(getAbilityFromSlot(i) != null) {
				getAbilityFromSlot(i).setCastTime(0);
			}
		}
		if(getCurrentlyCasting() != null) {
			getCurrentlyCasting().setCastTime(0);
		}
	}
	
	public int getSelectedAbilitySlot() {
		return this.selectedAbilitySlot;
	}
	
	public void setSelectedAbilitySlot(int newSlot) {
		this.selectedAbilitySlot = newSlot;
		resetCasting();
	}
	
	public boolean renderAbilityHotbar()
	{
		return renderAbilities;
	}
	
	public void setRenderAbilities(boolean renderAbilities)
	{
		this.renderAbilities = renderAbilities;
	}
	
	public CompoundNBT saveAbilities(){
		CompoundNBT tag = new CompoundNBT();
		
		for(DragonAbility ability : abilities){
			tag.put(ability.getId(), ability.saveNBT());
		}
		
		return tag;
	}
	
	public void loadAbilities(CompoundNBT nbt){
		CompoundNBT tag = nbt.contains("abilitySlots") ? nbt.getCompound("abilitySlots") : null;
		
		if(tag != null){
			for(DragonAbility staticAbility : DragonAbilities.ABILITY_LOOKUP.values()){
				if(tag.contains(staticAbility.getId())){
					DragonAbility ability = staticAbility.createInstance();
					ability.loadNBT(tag.getCompound(staticAbility.getId()));
					addAbility(ability);
				}
			}
		}
	}
	
	@Override
	public INBT writeNBT(Capability<DragonStateHandler> capability,  Direction side)
	{
		CompoundNBT tag = new CompoundNBT();
		
		tag.putBoolean("renderSkills",renderAbilityHotbar());
		
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("mana", getCurrentMana());
		nbt.putInt("selectedAbilitySlot", getSelectedAbilitySlot());
		nbt.put("abilitySlots", saveAbilities());
		tag.put("abilityData", nbt);
		tag.putBoolean("onMagicSource", onMagicSource);
		tag.putInt("magicSourceTimer", magicSourceTimer);
		
		return tag;
	}
	
	@Override
	public void readNBT(Capability<DragonStateHandler> capability, Direction side, INBT base)
	{
		CompoundNBT tag = (CompoundNBT) base;
		onMagicSource = tag.getBoolean("onMagicSource");
		magicSourceTimer = tag.getInt("magicSourceTimer");
		
		setRenderAbilities(tag.getBoolean("renderSkills"));
		
		if(tag.contains("abilityData")) {
			CompoundNBT ability = tag.getCompound("abilityData");
			
			if (ability != null) {
				setSelectedAbilitySlot(ability.getInt("selectedAbilitySlot"));
				setCurrentMana(ability.getInt("mana"));
				loadAbilities(ability);
			}
		}
	}
	
	@Override
	public void clone(DragonStateHandler oldCap)
	{
		getAbilities().clear();
		getAbilities().addAll(oldCap.getMagic().getAbilities());
		setCurrentMana(oldCap.getMagic().getCurrentMana());
		setSelectedAbilitySlot(oldCap.getMagic().getSelectedAbilitySlot());
		setRenderAbilities(oldCap.getMagic().renderAbilityHotbar());
	}
}
