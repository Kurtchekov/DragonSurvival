package by.jackraidenph.dragonsurvival.common.magic.abilities.Actives.BuffAbilities;

import by.jackraidenph.dragonsurvival.util.Functions;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.client.handlers.KeyInputHandler;
import by.jackraidenph.dragonsurvival.common.magic.common.AbilityAnimation;
import by.jackraidenph.dragonsurvival.common.magic.common.ActiveDragonAbility;
import by.jackraidenph.dragonsurvival.common.DragonEffects;
import by.jackraidenph.dragonsurvival.misc.DragonType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AoeBuffAbility extends ActiveDragonAbility
{
	protected EffectInstance effect;
	protected int range;
	protected IParticleData particle;
	
	public AoeBuffAbility(DragonType type, EffectInstance effect, int range, IParticleData particle, String id, String icon, int minLevel, int maxLevel, int manaCost, int castTime, int cooldown, Integer[] requiredLevels)
	{
		super(type, id, icon, minLevel, maxLevel, manaCost, castTime, cooldown, requiredLevels);
		this.effect = effect;
		this.range = range;
		this.particle = particle;
	}
	
	
	
	@Override
	public ArrayList<ITextComponent> getInfo()
	{
		ArrayList<ITextComponent> components = super.getInfo();
		components.add(new TranslationTextComponent("ds.skill.duration.seconds", getDuration()));
		components.add(new TranslationTextComponent("ds.skill.aoe", getRange() + "x" + getRange()));
		
		if(effect.getEffect() == DragonEffects.REVEALING_THE_SOUL){
			components.add(new TranslationTextComponent("ds.skill.bonus_exp.multiplier", ConfigHandler.SERVER.revealingTheSoulMultiplier.get() + "x"));
			components.add(new TranslationTextComponent("ds.skill.bonus_exp.max_gain", Integer.toString(ConfigHandler.SERVER.revealingTheSoulMaxEXP.get())));
		}
		
		if(!KeyInputHandler.ABILITY3.isUnbound()) {
			String key = KeyInputHandler.ABILITY3.getKey().getDisplayName().getContents().toUpperCase(Locale.ROOT);
			
			if(key.isEmpty()){
				key = KeyInputHandler.ABILITY3.getKey().getDisplayName().getString();
			}
			components.add(new TranslationTextComponent("ds.skill.keybind", key));
		}
		
		return components;
	}
	
	@Override
	public boolean isDisabled()
	{
		if(effect.getEffect() == DragonEffects.REVEALING_THE_SOUL && !ConfigHandler.SERVER.revealingTheSoul.get()) return true;
		if(effect.getEffect() == DragonEffects.STRONG_LEATHER && !ConfigHandler.SERVER.toughSkin.get()) return true;
		if(effect.getEffect() == Effects.DIG_SPEED && !ConfigHandler.SERVER.inspiration.get()) return true;
		return super.isDisabled();
	}
	
	@Override
	public AoeBuffAbility createInstance()
	{
		return new AoeBuffAbility(type, effect, range, particle, id, icon, minLevel, maxLevel, manaCost, castTime, abilityCooldown, requiredLevels);
	}
	
	public EffectInstance getEffect(){
		return new EffectInstance(effect.getEffect(), Functions.secondsToTicks(getDuration()) * 4, effect.getAmplifier());
	}
	
	@Override
	public void onActivation(PlayerEntity player)
	{
		super.onActivation(player);
		float f5 = (float)Math.PI * getRange() * getRange();
		
		for(int i = 0; i < 20; i++) {
			for (int k1 = 0; (float)k1 < f5; ++k1) {
				float f6 = player.level.random.nextFloat() * ((float)Math.PI * 2F);
				float f7 = MathHelper.sqrt(player.level.random.nextFloat()) * getRange();
				float f8 = MathHelper.cos(f6) * f7;
				float f9 = MathHelper.sin(f6) * f7;
				player.level.addAlwaysVisibleParticle(particle, player.getX() + (double)f8, player.getY(), player.getZ() + (double)f9, (0.5D - player.level.random.nextDouble()) * 0.15D, (double)0.01F, (0.5D - player.level.random.nextDouble()) * 0.15D);
			}
		}
		
		List<LivingEntity> list1 = player.level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(getRange()));
		if (!list1.isEmpty()) {
			for(LivingEntity livingentity : list1) {
				if (livingentity.isAffectedByPotions()) {
					double d0 = livingentity.getX() - player.getX();
					double d1 = livingentity.getZ() - player.getZ();
					double d2 = d0 * d0 + d1 * d1;
					if (d2 <= (double)(getRange() * getRange())) {
						livingentity.addEffect(new EffectInstance(new EffectInstance(effect.getEffect(), Functions.secondsToTicks(getDuration()), effect.getAmplifier())));
					}
				}
			}
		}
		player.level.playLocalSound(player.position().x, player.position().y + 0.5, player.position().z, SoundEvents.UI_TOAST_OUT, SoundCategory.PLAYERS, 5F, 0.1F, false);
	}
	
	public boolean canMoveWhileCasting(){ return false; }
	
	public int getDuration(){
		if(effect.getEffect() == Effects.DIG_SPEED){
			return ConfigHandler.SERVER.inspirationDuration.get();
		}else if(effect.getEffect() == DragonEffects.REVEALING_THE_SOUL){
			return ConfigHandler.SERVER.revealingTheSoulDuration.get();
		}else if(effect.getEffect() == DragonEffects.STRONG_LEATHER){
			return ConfigHandler.SERVER.toughSkinDuration.get();
		}
		return getLevel() * (effect.getDuration() > 0 ? effect.getDuration() : 30);
	}
	
	public int getRange()
	{
		return range;
	}
	
	@OnlyIn( Dist.CLIENT )
	public ArrayList<ITextComponent> getLevelUpInfo(){
		ArrayList<ITextComponent> list = super.getLevelUpInfo();
		list.add(new TranslationTextComponent("ds.skill.duration.seconds", "+" + (effect.getDuration() > 0 ? effect.getDuration() : 30)));
		return list;
	}
	
	@Override
	public AbilityAnimation getLoopingAnimation()
	{
		return new AbilityAnimation("cast_mass_buff", true, true);
	}
	
	@Override
	public AbilityAnimation getStoppingAnimation()
	{
		return new AbilityAnimation("mass_buff", 0.56 * 20, true, true);
	}
	
	@Override
	public IFormattableTextComponent getDescription()
	{
		return new TranslationTextComponent("ds.skill.description." + getId(), getDuration());
	}
}
