package by.jackraidenph.dragonsurvival.common.entity.creatures;

import by.jackraidenph.dragonsurvival.client.render.util.AnimationTimer;
import by.jackraidenph.dragonsurvival.client.render.util.CommonTraits;
import by.jackraidenph.dragonsurvival.common.DragonEffects;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.common.entity.DSEntities;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.misc.PrincessTrades;
import by.jackraidenph.dragonsurvival.util.Functions;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.item.DyeColor;
import net.minecraft.item.MerchantOffers;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PrincesHorseEntity extends VillagerEntity implements IAnimatable, CommonTraits
{
    private static final List<DyeColor> colors = Arrays.asList(DyeColor.RED, DyeColor.YELLOW, DyeColor.PURPLE, DyeColor.BLUE, DyeColor.BLACK, DyeColor.WHITE);
    public static DataParameter<Integer> color = EntityDataManager.defineId(PrincesHorseEntity.class, DataSerializers.INT);

    public PrincesHorseEntity(EntityType<? extends VillagerEntity> entityType, World world) {
        super(entityType, world);
    }
    
    public PrincesHorseEntity(EntityType<? extends VillagerEntity> entityType, World world, VillagerType villagerType) {
        super(entityType, world, villagerType);
    }
    
    protected int getExperienceReward(PlayerEntity p_70693_1_) {
        return 1 + this.level.random.nextInt(2);
    }
    
    AnimationFactory animationFactory = new AnimationFactory(this);
    AnimationTimer animationTimer = new AnimationTimer();

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(color, 0);
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld serverWorld, DifficultyInstance difficultyInstance, SpawnReason reason, @Nullable ILivingEntityData livingEntityData, @Nullable CompoundNBT compoundNBT) {
        setColor(colors.get(this.random.nextInt(6)).getId());
        setVillagerData(getVillagerData().setProfession(DSEntities.PRINCE_PROFESSION));
        return super.finalizeSpawn(serverWorld, difficultyInstance, reason, livingEntityData, compoundNBT);
    }

    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        setColor(compoundNBT.getInt("Color"));
    }

    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putInt("Color", getColor());
    }

    public int getColor() {
        return this.entityData.get(color);
    }

    public void setColor(int i) {
        this.entityData.set(color, i);
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    protected SoundEvent getTradeUpdatedSound(boolean p_213721_1_) {
        return null;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return null;
    }

    public void playCelebrateSound() {
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PLAYER_DEATH;
    }

    public void playWorkSound() {
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> p_213364_1_) {
        return brainProvider().makeBrain(p_213364_1_);
    }

    public void refreshBrain(ServerWorld p_213770_1_) {
    }

    public boolean canBreed() {
        return false;
    }

    protected ITextComponent getTypeName() {
        return new TranslationTextComponent(this.getType().getDescriptionId());
    }

    public void thunderHit(ServerWorld p_241841_1_, LightningBoltEntity p_241841_2_) {
    }

    protected void pickUpItem(ItemEntity p_175445_1_) {
    }

    protected void updateTrades() {
        VillagerData villagerdata = getVillagerData();
        Int2ObjectMap<VillagerTrades.ITrade[]> int2objectmap = PrincessTrades.colorToTrades.get(getColor());
        if (int2objectmap != null && !int2objectmap.isEmpty()) {
            VillagerTrades.ITrade[] trades = int2objectmap.get(villagerdata.getLevel());
            if (trades != null) {
                MerchantOffers merchantoffers = getOffers();
                addOffersFromItemListings(merchantoffers, trades, 2);
            }
        }
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1) {
            public boolean canUse() {
                return (!PrincesHorseEntity.this.isTrading() && super.canUse());
            }
        });
        this.goalSelector.addGoal(6, new LookAtGoal(this, LivingEntity.class, 8.0F));
        goalSelector.addGoal(6, new AvoidEntityGoal<>(this, PlayerEntity.class, 16, 1, 1, livingEntity -> {
            return DragonStateProvider.isDragon(livingEntity) && livingEntity.hasEffect(DragonEffects.EVIL_DRAGON);
        }));
        goalSelector.addGoal(7, new PanicGoal(this, 1.5));
    }

    public void gossip(ServerWorld p_242368_1_, VillagerEntity p_242368_2_, long p_242368_3_) {
    }

    public void startSleeping(BlockPos p_213342_1_) {
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "everything", 4, event -> {
            AnimationBuilder builder = new AnimationBuilder();
            double speed = getMovementSpeed(this);
            AnimationController controller = event.getController();
            if (speed > 0.6)
                builder.addAnimation("run_princess");
            else if (speed > 0.1) {
                builder.addAnimation("walk_princess");
            } else {
                Animation animation = controller.getCurrentAnimation();
                if (animation == null) {
                    animationTimer.putAnimation("idle_princess", 88d, builder);
                } else {
                    String name = animation.animationName;
                    switch (name) {
                        case "idle_princess":
                            if (animationTimer.getDuration("idle_princess") <= 0) {
                                if (random.nextInt(2000) == 1) {
                                    animationTimer.putAnimation("idle_princess_2", 145d, builder);
                                }
                            }
                            break;
                        case "walk_princess":
                        case "run_princess":
                            animationTimer.putAnimation("idle_princess", 88d, builder);
                            break;
                        case "idle_princess_2":
                            if (animationTimer.getDuration("idle_princess_2") <= 0) {
                                animationTimer.putAnimation("idle_princess", 88d, builder);
                            }
                            break;
                    }
                }
            }
            controller.setAnimation(builder);
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimationFactory getFactory() {
        return animationFactory;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return !this.hasCustomName() && tickCount >= Functions.minutesToTicks(ConfigHandler.COMMON.princessDespawnDelay.get()) && !hasCustomName();
    }

    @Override
    public void die(DamageSource damageSource) {
        if (level instanceof ServerWorld && !(this instanceof PrinceHorseEntity)) {
            PrincessEntity princess = DSEntities.PRINCESS.create(level);
            princess.setPos(getX(), getY(), getZ());
            princess.finalizeSpawn((IServerWorld) level, level.getCurrentDifficultyAt(blockPosition()), SpawnReason.NATURAL, null, null);
            princess.setColor(getColor());
            princess.setUUID(UUID.randomUUID());
            level.addFreshEntity(princess);
        }
        super.die(damageSource);
//        if (damageSource.getEntity() instanceof PlayerEntity) {
//            VillagerRelationsHandler.applyEvilMarker((PlayerEntity) damageSource.getEntity());
//        }
    }
}
