package by.jackraidenph.dragonsurvival.common.magic.abilities.Actives;

import by.jackraidenph.dragonsurvival.client.handlers.KeyInputHandler;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.common.entity.DSEntities;
import by.jackraidenph.dragonsurvival.common.entity.projectiles.DragonSpikeEntity;
import by.jackraidenph.dragonsurvival.common.magic.common.ActiveDragonAbility;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.misc.DragonType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Locale;

public class SpikeAbility extends ActiveDragonAbility
{
	public SpikeAbility(DragonType type, String id, String icon, int minLevel, int maxLevel, int manaCost, int castTime, int cooldown, Integer[] requiredLevels)
	{
		super(type, id, icon, minLevel, maxLevel, manaCost, castTime, cooldown, requiredLevels);
	}
	
	public float getDamage(){
		return (float)(ConfigHandler.SERVER.spikeDamage.get() * getLevel());
	}
	
	@Override
	public IFormattableTextComponent getDescription()
	{
		return new TranslationTextComponent("ds.skill.description." + getId(), getDamage());
	}
	
	@Override
	public SpikeAbility createInstance()
	{
		return new SpikeAbility(type, id, icon, minLevel, maxLevel, manaCost, castTime, abilityCooldown, requiredLevels);
	}
	
	@Override
	public ArrayList<ITextComponent> getInfo()
	{
		ArrayList<ITextComponent> components = super.getInfo();
		components.add(new TranslationTextComponent("ds.skill.damage", getDamage()));
		
		if(!KeyInputHandler.ABILITY2.isUnbound()) {
			String key = KeyInputHandler.ABILITY2.getKey().getDisplayName().getContents().toUpperCase(Locale.ROOT);
			
			if(key.isEmpty()){
				key = KeyInputHandler.ABILITY2.getKey().getDisplayName().getString();
			}
			components.add(new TranslationTextComponent("ds.skill.keybind", key));
		}
		
		return components;
	}
	
	@OnlyIn( Dist.CLIENT )
	public ArrayList<ITextComponent> getLevelUpInfo(){
		ArrayList<ITextComponent> list = super.getLevelUpInfo();
		list.add(new TranslationTextComponent("ds.skill.damage", "+" + ConfigHandler.SERVER.spikeDamage.get() ));
		return list;
	}
	
	@Override
	public void onActivation(PlayerEntity player)
	{
		super.onActivation(player);
		Vector3d vector3d = player.getViewVector(1.0F);
		double speed = 1d;
		double d2 = vector3d.x * speed;
		double d3 = vector3d.y * speed;
		double d4 = vector3d.z * speed;
		
		DragonStateHandler handler = DragonStateProvider.getCap(player).orElse(null);
		if(handler == null) return;
		handler.getMovementData().bite = true;

		DragonSpikeEntity entity = new DragonSpikeEntity(DSEntities.DRAGON_SPIKE, player.level, player);
		entity.setPos(entity.getX() + d2, entity.getY() + d3, entity.getZ() + d4);
		entity.setArrow_level(getLevel());
		entity.setBaseDamage(getDamage());
		entity.pickup = AbstractArrowEntity.PickupStatus.DISALLOWED;
		entity.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 4F, 1.0F);
		player.level.addFreshEntity(entity);
	}
	
	@Override
	public boolean isDisabled()
	{
		return super.isDisabled() || !ConfigHandler.SERVER.spike.get();
	}
}
