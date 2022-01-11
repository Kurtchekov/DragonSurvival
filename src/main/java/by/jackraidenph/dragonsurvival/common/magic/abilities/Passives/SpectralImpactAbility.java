package by.jackraidenph.dragonsurvival.common.magic.abilities.Passives;

import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.common.magic.common.PassiveDragonAbility;
import by.jackraidenph.dragonsurvival.misc.DragonType;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;

public class SpectralImpactAbility extends PassiveDragonAbility
{
	public SpectralImpactAbility(DragonType type, String abilityId, String icon, int minLevel, int maxLevel)
	{
		super(type, abilityId, icon, minLevel, maxLevel);
	}
	
	public int getChance(){
		return ConfigHandler.SERVER.spectralImpactProcChance.get() * getLevel();
	}
	
	@Override
	public SpectralImpactAbility createInstance()
	{
		return new SpectralImpactAbility(type, id, icon, minLevel, maxLevel);
	}
	
	@Override
	public IFormattableTextComponent getDescription()
	{
		return new TranslationTextComponent("ds.skill.description." + getId(), getChance());
	}
	
	@OnlyIn( Dist.CLIENT )
	public ArrayList<ITextComponent> getLevelUpInfo(){
		ArrayList<ITextComponent> list = super.getLevelUpInfo();
		list.add(new TranslationTextComponent("ds.skill.chance", "+" + ConfigHandler.SERVER.spectralImpactProcChance.get()));
		return list;
	}
	
	@Override
	public boolean isDisabled()
	{
		return super.isDisabled() || !ConfigHandler.SERVER.spectralImpact.get();
	}
}
