package by.jackraidenph.dragonsurvival.common.entity.projectiles;

import by.jackraidenph.dragonsurvival.util.Functions;
import by.jackraidenph.dragonsurvival.common.magic.DragonAbilities;
import by.jackraidenph.dragonsurvival.common.magic.abilities.Actives.BallLightningAbility;
import by.jackraidenph.dragonsurvival.client.particles.SeaDragon.LargeLightningParticleData;
import by.jackraidenph.dragonsurvival.common.DragonEffects;
import by.jackraidenph.dragonsurvival.common.entity.DSEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;

public class BallLightningEntity extends DragonBallEntity
{
	public BallLightningEntity(World p_i50168_9_, LivingEntity p_i50168_2_, double p_i50168_3_, double p_i50168_5_, double p_i50168_7_)
	{
		super(DSEntities.BALL_LIGHTNING, p_i50168_2_, p_i50168_3_, p_i50168_5_, p_i50168_7_, p_i50168_9_);
	}
	
	public BallLightningEntity(EntityType<? extends AbstractFireballEntity> p_i50166_1_, World p_i50166_2_) {
		super(p_i50166_1_, p_i50166_2_);
	}
	
	@Override
	protected IParticleData getTrailParticle()
	{
		return ParticleTypes.WHITE_ASH;
	}
	
	protected boolean shouldBurn() {
		return false;
	}
	
	public void attackMobs()
	{
		int range = ((BallLightningAbility)DragonAbilities.BALL_LIGHTNING).getRange();
		List<Entity> entities = this.level.getEntities(null, new AxisAlignedBB(position().x - range, position().y - range, position().z - range, position().x + range, position().y + range, position().z + range));
		entities.removeIf((e) -> e == getOwner() || e instanceof BallLightningEntity);
		entities.removeIf((e) -> e.distanceTo(this) > range);
		entities.removeIf((e) -> !(e instanceof LivingEntity));
		
		for(Entity ent : entities){
			if (!this.level.isClientSide) {
				ent.hurt(DamageSource.LIGHTNING_BOLT, BallLightningAbility.getDamage(getLevel()));
				
				if(ent instanceof LivingEntity) {
					LivingEntity livingEntity = (LivingEntity)ent;
					if (livingEntity.level.random.nextInt(100) < 40) {
						if(!livingEntity.level.isClientSide)
							livingEntity.addEffect(new EffectInstance(DragonEffects.CHARGED, Functions.secondsToTicks(10), 0, false, true));
					}
				}
				
				if (getOwner() instanceof LivingEntity) {
					this.doEnchantDamageEffects((LivingEntity)getOwner(), ent);
				}
			}
			
			if(this.level.isClientSide) {
				for (int i = 0; i < 10; i++) {
					double d1 = level.random.nextFloat();
					double d2 = level.random.nextFloat();
					double d3 = level.random.nextFloat();
					level.addParticle(new LargeLightningParticleData(37F, false), ent.getX() + d1, ent.getY() + 0.5 + d2, ent.getZ() + d3, 0.0D, 0.0D, 0.0D);
				}
			}
		}
		
		if(!level.isClientSide){
			if (level.isThundering()) {
				if (level.random.nextInt(100) < 30) {
					if (level.canSeeSky(blockPosition())) {
						LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(level);
						lightningboltentity.moveTo(new Vector3d(position().x, position().y, position().z));
						level.addFreshEntity(lightningboltentity);
					}
				}
			}
		}
		
		if(this.level.isClientSide) {
			float f = range;
			float f5 = (float)Math.PI * f * f;
			
			for(int k1 = 0; (float)k1 < f5; ++k1) {
				float f6 = this.random.nextFloat() * ((float)Math.PI * 2F);
				float f7 = MathHelper.sqrt(this.random.nextFloat()) * f;
				float f8 = MathHelper.cos(f6) * f7;
				float f9 = MathHelper.sin(f6) * f7;
				level.addParticle(new LargeLightningParticleData(37F, false), this.getX() + (double)f8, this.getY(), this.getZ() + (double)f9, 0, 0, 0);
			}
		}
		
		level.playLocalSound(getX(), getY(), getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundCategory.HOSTILE, 3.0F, 0.5f, false);
	}
}
