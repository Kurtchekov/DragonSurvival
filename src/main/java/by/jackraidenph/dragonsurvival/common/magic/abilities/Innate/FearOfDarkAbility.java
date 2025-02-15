package by.jackraidenph.dragonsurvival.common.magic.abilities.Innate;

import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.common.magic.common.InnateDragonAbility;
import by.jackraidenph.dragonsurvival.misc.DragonType;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FearOfDarkAbility extends InnateDragonAbility
{
	public FearOfDarkAbility(DragonType type, String abilityId, String icon, int minLevel, int maxLevel)
	{
		super(type, abilityId, icon, minLevel, maxLevel);
	}
	
	@Override
	public FearOfDarkAbility createInstance()
	{
		return new FearOfDarkAbility(type, id, icon, minLevel, maxLevel);
	}
	
	@Override
	public int getLevel()
	{
		return ConfigHandler.SERVER.penalties.get() && ConfigHandler.SERVER.forestStressTicks.get() != 0.0 ? 1 : 0;
	}
	
	@OnlyIn( Dist.CLIENT )
	public boolean isDisabled() {
		return super.isDisabled() || !ConfigHandler.SERVER.penalties.get() || ConfigHandler.SERVER.forestStressTicks.get() == 0.0;
	}
	
	@Override
	public IFormattableTextComponent getDescription()
	{
		return new TranslationTextComponent("ds.skill.description." + getId(), 3, ConfigHandler.SERVER.caveWaterDamage.get(), 0.5);
	}
}