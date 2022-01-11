package by.jackraidenph.dragonsurvival.common.magic.abilities.Actives.BuffAbilities;

import by.jackraidenph.dragonsurvival.util.Functions;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.client.handlers.KeyInputHandler;
import by.jackraidenph.dragonsurvival.misc.DragonType;
import net.minecraft.particles.IParticleData;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Locale;

public class ToughSkinAbility extends AoeBuffAbility
{
	public ToughSkinAbility(DragonType type, EffectInstance effect, int range, IParticleData particle, String id, String icon, int minLevel, int maxLevel, int manaCost, int castTime, int cooldown, Integer[] requiredLevels)
	{
		super(type, effect, range, particle, id, icon, minLevel, maxLevel, manaCost, castTime, cooldown, requiredLevels);
	}
	
	public static double getDefence(int level){
		return level * ConfigHandler.SERVER.toughSkinArmorValue.get();
	}
	
	@Override
	public EffectInstance getEffect()
	{
		return new EffectInstance(effect.getEffect(), Functions.secondsToTicks(getDuration()) * 4, getLevel() - 1, false, false);
	}
	
	@Override
	public ToughSkinAbility createInstance()
	{
		return new ToughSkinAbility(type, effect, range, particle, id, icon, minLevel, maxLevel, manaCost, castTime, abilityCooldown, requiredLevels);
	}
	
	@Override
	public IFormattableTextComponent getDescription()
	{
		return new TranslationTextComponent("ds.skill.description." + getId(), getDuration(), getDefence(getLevel()));
	}
	
	@Override
	public ArrayList<ITextComponent> getInfo()
	{
		ArrayList<ITextComponent> components = super.getInfo();
		
		if(!KeyInputHandler.ABILITY3.isUnbound()) {
			components = new ArrayList<>(components.subList(0, components.size() - 1));
		}
		
		components.add(new TranslationTextComponent("ds.skill.duration.seconds", getDuration()));
		
		if(!KeyInputHandler.ABILITY3.isUnbound()) {
			String key = KeyInputHandler.ABILITY3.getKey().getDisplayName().getContents().toUpperCase(Locale.ROOT);
			
			if(key.isEmpty()){
				key = KeyInputHandler.ABILITY3.getKey().getDisplayName().getString();
			}
			components.add(new TranslationTextComponent("ds.skill.keybind", key));
		}
		
		return components;
	}
	
	public boolean canMoveWhileCasting(){ return false; }
	@OnlyIn( Dist.CLIENT )
	public ArrayList<ITextComponent> getLevelUpInfo(){
		ArrayList<ITextComponent> list = super.getLevelUpInfo();
		list.add(new TranslationTextComponent("ds.skill.defence", "+" + ConfigHandler.SERVER.toughSkinArmorValue.get()));
		return list;
	}
	
	@Override
	public boolean isDisabled()
	{
		return super.isDisabled() || !ConfigHandler.SERVER.toughSkin.get();
	}
}
