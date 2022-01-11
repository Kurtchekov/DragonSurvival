package by.jackraidenph.dragonsurvival.config;

import by.jackraidenph.dragonsurvival.util.Functions;
import by.jackraidenph.dragonsurvival.misc.DragonLevel;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ServerConfig {

	public final ForgeConfigSpec.DoubleValue maxFlightSpeed;
	public final ForgeConfigSpec.DoubleValue gravityFactor;
	public final ForgeConfigSpec.BooleanValue mineStarBlock;
    public final ForgeConfigSpec.BooleanValue sizeChangesHitbox;
    public final ForgeConfigSpec.BooleanValue hitboxGrowsPastHuman;
	
	public final ForgeConfigSpec.BooleanValue enderDragonGrantsSpin;
	public final ForgeConfigSpec.BooleanValue startWithWings;
	public final ForgeConfigSpec.BooleanValue enableFlightFallDamage;
	public final ForgeConfigSpec.IntValue flightHungerThreshold;
	public final ForgeConfigSpec.BooleanValue allowFlyingWithoutHunger;
	public final ForgeConfigSpec.BooleanValue flyingUsesHunger;
	public final ForgeConfigSpec.BooleanValue creativeFlight;
	public final ForgeConfigSpec.BooleanValue foldWingsOnLand;
	public final ForgeConfigSpec.BooleanValue lethalFlight;
	public final ForgeConfigSpec.IntValue flightSpinCooldown;
	
	public final ForgeConfigSpec.BooleanValue treasureHealthRegen;
	public final ForgeConfigSpec.IntValue treasureRegenTicks;
	public final ForgeConfigSpec.IntValue treasureRegenTicksReduce;
	public final ForgeConfigSpec.IntValue maxTreasures;
	
	public final ForgeConfigSpec.BooleanValue sourceOfMagicInfiniteMagic;
	public final ForgeConfigSpec.BooleanValue damageWrongSourceOfMagic;
	public final ForgeConfigSpec.BooleanValue canUseAllSourcesOfMagic;
	public final ForgeConfigSpec.IntValue elderDragonDustTime;
	public final ForgeConfigSpec.IntValue elderDragonBoneTime;
	public final ForgeConfigSpec.IntValue weakHeartShardTime;
	public final ForgeConfigSpec.IntValue weakDragonHeartTime;
	public final ForgeConfigSpec.IntValue elderDragonHeartTime;
	
	public final ForgeConfigSpec.IntValue altarUsageCooldown;
	public final ForgeConfigSpec.DoubleValue newbornJump;
	public final ForgeConfigSpec.DoubleValue youngJump;
	public final ForgeConfigSpec.DoubleValue adultJump;
	
	public final ForgeConfigSpec.BooleanValue keepClawItems;
	public final ForgeConfigSpec.BooleanValue syncClawRender;
	
	public final ForgeConfigSpec.BooleanValue ridingBlacklist;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> allowedVehicles;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklistedItems;
	public final ForgeConfigSpec.ConfigValue<List<? extends Integer>> blacklistedSlots;
	
	public final ForgeConfigSpec.BooleanValue alternateGrowing;
	public final ForgeConfigSpec.DoubleValue maxGrowthSize;
	public final ForgeConfigSpec.DoubleValue newbornGrowthModifier;
	public final ForgeConfigSpec.DoubleValue youngGrowthModifier;
	public final ForgeConfigSpec.DoubleValue adultGrowthModifier;
	public final ForgeConfigSpec.DoubleValue maxGrowthModifier;
	
	public final ForgeConfigSpec.DoubleValue reachBonus;
	public final ForgeConfigSpec.BooleanValue saveGrowthStage;
	public final ForgeConfigSpec.BooleanValue fireBreathSpreadsFire;

    public final ForgeConfigSpec.BooleanValue customDragonFoods;
    public final ForgeConfigSpec.BooleanValue healthAdjustments;
    public final ForgeConfigSpec.IntValue minHealth;
    public final ForgeConfigSpec.IntValue maxHealth;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> growNewborn;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> growYoung;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> growAdult;

    public final ForgeConfigSpec.BooleanValue bonuses;
    public final ForgeConfigSpec.BooleanValue attackDamage;
    public final ForgeConfigSpec.DoubleValue babyBonusDamage;
    public final ForgeConfigSpec.DoubleValue youngBonusDamage;
    public final ForgeConfigSpec.DoubleValue adultBonusDamage;
    public final ForgeConfigSpec.BooleanValue clawsAreTools;
    public final ForgeConfigSpec.IntValue baseHarvestLevel;
    public final ForgeConfigSpec.IntValue bonusHarvestLevel;
    public final ForgeConfigSpec.ConfigValue<DragonLevel> bonusUnlockedAt;
    public final ForgeConfigSpec.IntValue speedupEffectLevel; // 0 = Disabled

    public final ForgeConfigSpec.BooleanValue caveFireImmunity;
    public final ForgeConfigSpec.BooleanValue caveLavaSwimming;
    public final ForgeConfigSpec.IntValue caveLavaSwimmingTicks; // 0 = Disabled
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> caveSpeedupBlocks;

    public final ForgeConfigSpec.DoubleValue forestFallReduction; // 0.0 = Disabled
    public final ForgeConfigSpec.BooleanValue forestBushImmunity;
	public final ForgeConfigSpec.BooleanValue forestCactiImmunity;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> forestSpeedupBlocks;
    public final ForgeConfigSpec.BooleanValue seaSwimmingBonuses;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> seaSpeedupBlocks;

    public final ForgeConfigSpec.BooleanValue penalties;

    public final ForgeConfigSpec.DoubleValue caveWaterDamage; // 0.0 = Disabled
    public final ForgeConfigSpec.DoubleValue caveRainDamage; // 0.0 = Disabled
	public final ForgeConfigSpec.DoubleValue caveSplashDamage; // 0.0 = Disabled
	public final ForgeConfigSpec.IntValue chargedSoupBuffDuration; // 0 = Disabled

    public final ForgeConfigSpec.IntValue forestStressTicks; // 0 = Disabled
    public final ForgeConfigSpec.IntValue forestStressEffectDuration;
    public final ForgeConfigSpec.DoubleValue stressExhaustion;

    public final ForgeConfigSpec.IntValue seaTicksWithoutWater; // 0 = Disabled
	public final ForgeConfigSpec.BooleanValue seaTicksBasedOnTemperature;
	public final ForgeConfigSpec.DoubleValue seaDehydrationDamage;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> seaHydrationBlocks;
    public final ForgeConfigSpec.BooleanValue seaAllowWaterBottles;
    public final ForgeConfigSpec.IntValue seaTicksWithoutWaterRestored; // 0 = Disabled
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> seaAdditionalWaterUseables;

    public final ForgeConfigSpec.DoubleValue humanOreDustChance;
    public final ForgeConfigSpec.DoubleValue dragonOreDustChance;
    public final ForgeConfigSpec.DoubleValue humanOreBoneChance;
    public final ForgeConfigSpec.DoubleValue dragonOreBoneChance;
    public final ForgeConfigSpec.ConfigValue<String> oresTag;
	
	public final ForgeConfigSpec.DoubleValue dragonHeartShardChance;
	public final ForgeConfigSpec.DoubleValue weakDragonHeartChance;
	public final ForgeConfigSpec.DoubleValue elderDragonHeartChance;

	public final ForgeConfigSpec.ConfigValue<List<? extends String>> seaDragonHurtfulItems;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> caveDragonHurtfulItems;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> forestDragonHurtfulItems;

    public final ForgeConfigSpec.ConfigValue<List<? extends String>> caveDragonFoods;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> forestDragonFoods;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> seaDragonFoods;

	public final ForgeConfigSpec.BooleanValue noEXPRequirements;
	public final ForgeConfigSpec.BooleanValue consumeEXPAsMana;
	public final ForgeConfigSpec.IntValue favorableManaTicks;
	public final ForgeConfigSpec.IntValue normalManaTicks;
	
	public final ForgeConfigSpec.BooleanValue dragonAbilities;
	public final ForgeConfigSpec.BooleanValue caveDragonAbilities;
	public final ForgeConfigSpec.BooleanValue forestDragonAbilities;
	public final ForgeConfigSpec.BooleanValue seaDragonAbilities;
	
	public final ForgeConfigSpec.BooleanValue fireBreath;
	public final ForgeConfigSpec.DoubleValue fireBreathDamage;
	public final ForgeConfigSpec.IntValue fireBreathInitialMana;
	public final ForgeConfigSpec.IntValue fireBreathOvertimeMana;
	public final ForgeConfigSpec.IntValue fireBreathManaTicks;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> fireBreathBlockBreaks;
	
	public final ForgeConfigSpec.BooleanValue stormBreath;
	public final ForgeConfigSpec.DoubleValue stormBreathDamage;
	public final ForgeConfigSpec.IntValue stormBreathInitialMana;
	public final ForgeConfigSpec.IntValue stormBreathOvertimeMana;
	public final ForgeConfigSpec.IntValue stormBreathManaTicks;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> stormBreathBlockBreaks;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> chargedBlacklist;
	
	
	public final ForgeConfigSpec.BooleanValue forestBreath;
	public final ForgeConfigSpec.DoubleValue forestBreathDamage;
	public final ForgeConfigSpec.IntValue forestBreathInitialMana;
	public final ForgeConfigSpec.IntValue forestBreathOvertimeMana;
	public final ForgeConfigSpec.IntValue forestBreathManaTicks;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> forestBreathBlockBreaks;
	
	public final ForgeConfigSpec.BooleanValue spike;
	public final ForgeConfigSpec.DoubleValue spikeDamage;
	public final ForgeConfigSpec.IntValue spikeManaCost;
	
	public final ForgeConfigSpec.BooleanValue inspiration;
	public final ForgeConfigSpec.IntValue inspirationDuration;
	public final ForgeConfigSpec.IntValue inspirationManaCost;
	
	public final ForgeConfigSpec.BooleanValue hunter;
	public final ForgeConfigSpec.IntValue hunterDuration;
	public final ForgeConfigSpec.DoubleValue hunterDamageBonus;
	public final ForgeConfigSpec.IntValue hunterManaCost;
	
	public final ForgeConfigSpec.BooleanValue forestMagic;
	public final ForgeConfigSpec.BooleanValue forestAthletics;
	public final ForgeConfigSpec.BooleanValue lightInDarkness;
	public final ForgeConfigSpec.BooleanValue cliffHanger;
	
	public final ForgeConfigSpec.BooleanValue ballLightning;
	public final ForgeConfigSpec.DoubleValue ballLightningDamage;
	public final ForgeConfigSpec.IntValue ballLightningManaCost;
	
	public final ForgeConfigSpec.BooleanValue revealingTheSoul;
	public final ForgeConfigSpec.IntValue revealingTheSoulDuration;
	public final ForgeConfigSpec.IntValue revealingTheSoulManaCost;
	public final ForgeConfigSpec.IntValue revealingTheSoulMaxEXP;
	public final ForgeConfigSpec.DoubleValue revealingTheSoulMultiplier;
	
	public final ForgeConfigSpec.BooleanValue seaEyes;
	public final ForgeConfigSpec.IntValue seaEyesDuration;
	public final ForgeConfigSpec.IntValue seaEyesManaCost;
	
	public final ForgeConfigSpec.BooleanValue seaMagic;
	public final ForgeConfigSpec.BooleanValue seaAthletics;
	public final ForgeConfigSpec.BooleanValue water;
	public final ForgeConfigSpec.BooleanValue spectralImpact;
	
	public final ForgeConfigSpec.BooleanValue fireball;
	public final ForgeConfigSpec.DoubleValue fireballDamage;
	public final ForgeConfigSpec.IntValue fireballManaCost;
	
	public final ForgeConfigSpec.BooleanValue toughSkin;
	public final ForgeConfigSpec.IntValue toughSkinDuration;
	public final ForgeConfigSpec.IntValue toughSkinManaCost;
	public final ForgeConfigSpec.DoubleValue toughSkinArmorValue;
	
	public final ForgeConfigSpec.BooleanValue lavaVision;
	public final ForgeConfigSpec.IntValue lavaVisionDuration;
	public final ForgeConfigSpec.IntValue lavaVisionManaCost;
	
	public final ForgeConfigSpec.BooleanValue caveMagic;
	public final ForgeConfigSpec.BooleanValue caveAthletics;
	public final ForgeConfigSpec.BooleanValue contrastShower;
	public final ForgeConfigSpec.BooleanValue burn;
	
	public final ForgeConfigSpec.BooleanValue saveAllAbilities;

	ServerConfig(ForgeConfigSpec.Builder builder){
		builder.push("server");
		// General
		builder.push("general");
		mineStarBlock = builder
				.comment("Whether silk touch hoes can be used to harvest Predator Stars.")
				.define("harvestableStarBlock", false);
		
		altarUsageCooldown = builder
				.comment("How long of a cooldown in seconds the altar has after each use.")
				.defineInRange("altarUsageCooldown", 0, 0, 1000);
		
		keepClawItems = builder
				.comment("Whether to keep items in the claw slots on death otherwise they will drop on death.")
				.define("keepClawItems", false);
		
		syncClawRender = builder
				.comment("If players are allowed to hide their claws and teeth from other players. If it is important to you to see your opponent's weapon during pvp, set false.")
				.define("syncClawRender", true);
		
		// Growth
		builder.pop().push("growth");
		sizeChangesHitbox = builder
				.comment("Whether the dragon size determines its hitbox size. The bigger the dragon, the bigger the hitbox. If false standard player's hitbox be used.")
				.define("sizeChangesHitbox", true);
		hitboxGrowsPastHuman = builder
				.comment("Whether the dragon hitbox grows past a human hitbox.")
				.define("largerDragonHitbox", true);
		growNewborn = builder
				.comment("List of items to grow newborn dragon. Format: item/tag:modid:id")
				.defineList("growNewborn", Arrays.asList(
						"item:dragonsurvival:heart_element",
						"item:dragonsurvival:weak_dragon_heart",
						"item:dragonsurvival:elder_dragon_heart"
				), this::isValidItemConfig);
		growYoung = builder
				.comment("List of items to grow young dragon. Format: item/tag:modid:id")
				.defineList("growYoung", Arrays.asList(
						"item:dragonsurvival:weak_dragon_heart",
						"item:dragonsurvival:elder_dragon_heart"
				), this::isValidItemConfig);
		growAdult = builder
				.comment("List of items to grow adult dragon. Format: item/tag:modid:id")
				.defineList("growAdult", Collections.singletonList(
						"item:dragonsurvival:elder_dragon_heart"
				), this::isValidItemConfig);
		alternateGrowing = builder
				.comment("Defines if dragon should grow without requirement of catalyst items. Your dragon will just grow over time.")
				.define("alternateGrowing", true);
		maxGrowthSize = builder
				.comment("Defines the max size your dragon can grow to. Values that are too high can break your game. It is not advisable to set a number higher than 60.")
				.defineInRange("maxGrowthSize", 60.0, 14.0, 1000000.0);
		reachBonus = builder
				.comment("The bonus that is given to dragons at ever 60 size. Human players have 1.0x reach and a size 60 dragon will have 1.5x distance with default value. Only applies to block mining.")
				.defineInRange("reachBonus", 0.5, 0, 1000000.0);
		saveGrowthStage = builder
				.comment("Should the growth stage of a dragon be saved even when you change. Does not affect the saving progress of magic (use saveAllAbilities). The author does not approve of weredragons, but if you insist...")
				.define("saveGrowthStage", false);
		minHealth = builder
				.comment("Dragon starting health. Minumum health dragons will start off with.")
				.defineInRange("minHealth", 14, 1, 100);
		maxHealth = builder
				.comment("Maximum health dragons can grow to.")
				.defineInRange("maxHealth", 40, 1, 100);
		
		newbornGrowthModifier = builder
				.comment("A multiplier to change the growth rate from newborn to young. At 1.0 it takes about 3 hours to turn a newborn dragon into a young dragon.")
				.defineInRange("newbornGrowthModifier", 1.0, 0, 1000);
		
		youngGrowthModifier = builder
				.comment("A multiplier to change the growth rate from young to adult. At 1.0 it takes about 1 day to turn a young dragon into a adult dragon.")
				.defineInRange("youngGrowthModifier", 1.0, 0, 1000);
		
		adultGrowthModifier = builder
				.comment("A multiplier to change the growth rate from adult to a full sized adult. At 1.0 it takes about 3 days to become a dragon of maximum adult size.")
				.defineInRange("adultGrowthModifier", 1.0, 0, 1000);
		
		maxGrowthModifier = builder
				.comment("A multiplier to change the growth rate from full sized adult to max size. The change in growth after the maximum adult size is measured in months and years.")
				.defineInRange("maxGrowthModifier", 1.0, 0, 1000);
		
		builder.pop().push("drops");
		dragonHeartShardChance = builder
				.comment("The chance for dragon heart shards to drop from any mobs with max health between 14-20")
				.defineInRange("dragonHeartShardChance", 0.02, 0.0, 1.0);
		weakDragonHeartChance = builder
				.comment("The chance for dragon heart shards to drop from any mobs with max health between 20-50")
				.defineInRange("weakDragonHeartChance", 0.02, 0.0, 1.0);
		elderDragonHeartChance = builder
				.comment("The chance for dragon heart shards to drop from any mobs with max health above 50")
				.defineInRange("elderDragonHeartChance", 0.2, 0.0, 1.0);
		
		// Treasure
		builder.pop().push("treasure");
		treasureHealthRegen = builder
				.comment("Whether sleeping on treasure will recover health or not. ")
				.define("treasureHealthRegen", true);
		treasureRegenTicks = builder
				.comment("The time in ticks it takes to recover 1hp while sleeping on treasure. Keep in mind that there is 1 treasure in mind. A large number of treasures in one place reduces time.")
				.defineInRange("treasureRegenTicks", 24000, 1, 10000000);
		treasureRegenTicksReduce = builder
				.comment("The amount of ticks each additional treasure reduces the regen time by")
				.defineInRange("treasureRegenTicksReduce", 97, 1, 10000000);
		maxTreasures = builder
				.comment("The max amount of additional treasure that can be used to reduce the regen time")
				.defineInRange("maxTreasures", 240, 1, 10000000);
		
		// Source of Magic
		builder.pop().push("source_of_magic");
		sourceOfMagicInfiniteMagic = builder
				.comment("Whether using the source of magic block will grant the infinite magic buff.")
				.define("sourceOfMagicInfiniteMagic", true);
		
		damageWrongSourceOfMagic = builder
				.comment("Whether using the the source of magic intended for another dragon type will hurt you.")
				.define("damageWrongSourceOfMagic", true);
		canUseAllSourcesOfMagic = builder
				.comment("Whether you are able to use all types of source of magic no matter your dragon type.")
				.define("canUseAllSourcesOfMagic", false);
		
		elderDragonDustTime = builder
				.comment("How long duration of the infinite magic effect using elder dragon dust gives in seconds. Note that you also spend 10 seconds while waiting.")
				.defineInRange("elderDragonDustTime", 20, 1, 10000);
		elderDragonBoneTime = builder
				.comment("How long duration of the infinite magic effect using elder dragon bone gives in seconds. Note that you also spend 10 seconds while waiting.")
				.defineInRange("elderDragonBoneTime", 60, 1, 10000);
		weakHeartShardTime = builder
				.comment("How long duration of the infinite magic effect using weak heart shard gives in seconds. Note that you also spend 10 seconds while waiting.")
				.defineInRange("weakHeartShardTime", 110, 1, 10000);
		weakDragonHeartTime = builder
				.comment("How long duration of the infinite magic effect using weak dragon heart gives in seconds. Note that you also spend 10 seconds while waiting.")
				.defineInRange("weakDragonHeartTime", 310, 1, 10000);
		elderDragonHeartTime = builder
				.comment("How long duration of the infinite magic effect using elder dragon heart gives in seconds. Note that you also spend 10 seconds while waiting.")
				.defineInRange("elderDragonHeartTime", 1010, 1, 10000);
		
		// Wings
		builder.pop().push("wings");
		maxFlightSpeed = builder
				.comment("Maximum acceleration fly speed up and down. Take into account the chunk load speed. A speed of 0.3 is optimal.")
				.defineInRange("maxFlightSpeed", 0.3, 0.0001, 1);
		gravityFactor = builder
				.comment("Gravity gets multiplied by this value when flying.")
				.defineInRange("gravityFactor", 0.3, 0.0001, 1);
		startWithWings = builder
				.comment("Whether dragons born with wings.")
				.define("startWithWings", true);
		enderDragonGrantsSpin = builder
				.comment("Whether you should be able to obtain the spin ability from the ender dragon.")
				.define("enderDragonGrantsSpin", true);
		allowFlyingWithoutHunger = builder
				.comment("Whether dragons can fly when totally hungry. You can't open your wings if you're hungry.")
				.define("allowFlyingWhenTotallyHungry", false);
		flightHungerThreshold = builder
				.comment("If the player's hunger is below this parameter, he can't open his wings.")
				.defineInRange("flightHungerThreshold", 6, 0, 20);
		flyingUsesHunger = builder
				.comment("Whether you use up hunger while flying.")
				.define("flyingUsesHunger", true);
		enableFlightFallDamage = builder
				.comment("Whether fatal fall damage in flight is included. If true dragon will take fatal damage from the fall.")
				.define("enableFlightFallDamage", true);
		lethalFlight = builder
				.comment("Whether fall damage from flight is lethal, otherwise it will leave you at half a heart")
				.define("lethalFallDamage", false);
		foldWingsOnLand = builder
				.comment("Whether your wings will fold automatically when landing. Has protection against accidental triggering, so the wings do not always close. If you always need to close the wings manually.")
				.define("foldWingsOnLand", false);
		creativeFlight = builder
				.comment("Whether to use flight similar to creative rather then gliding.")
				.define("alternateFlight", false);
		flightSpinCooldown = builder
				.comment("The cooldown in seconds in between uses of the spin attack in flight")
				.defineInRange("flightSpinCooldown", 5, 0, 100000);
		
		// Innate dragon bonuses
		builder.pop().push("bonuses");
		healthAdjustments = builder
				.comment("Apply a health modifier for dragons. The older the dragon, the more health it has.")
				.define("healthMod", true);
		bonuses = builder
				.comment("Set to false to toggle off all dragon bonuses and play as human.")
				.define("bonuses", true);
		attackDamage = builder
				.comment("Apply an attack damage modifier for dragons.")
				.define("attackMod", true);
		babyBonusDamage = builder
				.comment("Attack modifier for baby dragons.")
				.defineInRange("babyAttackMod", 1.0, 0.0, 100.0);
		youngBonusDamage = builder
				.comment("Attack modifier for young dragons.")
				.defineInRange("youngAttackMod", 2.0, 0.0, 100.0);
		adultBonusDamage = builder
				.comment("Attack modifier for adult dragons.")
				.defineInRange("adultAttackMod", 3.0, 0.0, 100.0);
		newbornJump = builder
				.comment("Jumping height for a newborn dragon. Default is 1 block.")
				.defineInRange("newbornJump", 0.025, 0, 0.9);
		youngJump = builder
				.comment("Jumping height for a young dragon. Default is 1.5 block.")
				.defineInRange("youngJump", 0.1, 0, 0.9);
		adultJump = builder
				.comment("Jumping height for a adult dragon. Default is 2 block.")
				.defineInRange("adultJump", 0.15, 0, 0.9);
		clawsAreTools = builder
				.comment("Whether dragon claws function as tools.")
				.define("clawsAreTools", true);
		baseHarvestLevel = builder
				.comment("The harvest level to apply when dragons breaks a block, regardless of dragon/tool type.")
				.defineInRange("baseHarvestLevel", 0, -1, 100);
		bonusHarvestLevel = builder
				.comment("The harvest level to apply to a dragons specific tool type once unlocked.")
				.defineInRange("bonusHarvestLevel", 1, -1, 100);
		bonusUnlockedAt = builder
				.comment("The stage that dragons unlock the bonus harvest level.")
				.defineEnum("bonusUnlockedAt", DragonLevel.YOUNG, DragonLevel.values());
		speedupEffectLevel = builder
				.comment("The speed effect level for dragon block-specific speedups. Set to 0 to disable.")
				.defineInRange("speedupEffectLevel", 2, 0, 100);
		// Cave Dragon Bonuses
		builder.push("cave");
		caveFireImmunity = builder
				.comment("Whether cave dragons are immune to fire damage types.")
				.define("fireImmunity", true);
		caveLavaSwimming = builder
				.comment("Set to false to disable cave dragon fast lava swimming.")
				.define("lavaSwimming", true);
		caveLavaSwimmingTicks = builder
				.comment("The maximum number of ticks a cave dragon can swim in lava. Set to 0 to allow unlimited air while under lava.")
				.defineInRange("lavaSwimTicks", 3600, 0, 100000);
		caveSpeedupBlocks = builder
				.comment("Blocks cave dragons gain speed when standing above. Formatting: block/tag:modid:id")
				.worldRestart()
				.defineList("caveSpeedupBlocks", Arrays.asList(
						"tag:minecraft:base_stone_nether",
						"tag:minecraft:base_stone_overworld",
						"tag:minecraft:stone_bricks",
						"tag:minecraft:beacon_base_blocks",
						"tag:forge:cobblestone",
						"tag:forge:sandstone",
						"tag:forge:stone",
						"tag:forge:ores",
						"block:quark:deepslate",
						"block:quark:deepslate_bricks",
						"block:quark:cobbled_deepslate"
				), this::isValidBlockConfig);

		// Forest Dragon Bonuses
		builder.pop().push("forest");
		forestFallReduction = builder
				.comment("How many blocks of fall damage is mitigated for forest dragons. Set to 0.0 to disable.")
				.defineInRange("fallReduction", 5.0, 0.0, 100.0);
		forestBushImmunity = builder
				.comment("Whether forest dragons are immune to Sweet Berry Bush damage.")
				.define("bushImmunity", true);
		forestCactiImmunity = builder
				.comment("Whether forest dragons are immune to Cactus damage.")
				.define("cactiImmunity", true);
		forestSpeedupBlocks = builder
				.comment("Blocks forest dragons gain speed when standing above. Formatting: block/tag:modid:id")
				.worldRestart()
				.defineList("forestSpeedupBlocks", Arrays.asList(
						"tag:minecraft:logs",
						"tag:minecraft:leaves",
						"tag:minecraft:planks",
						"tag:forge:dirt"
				), this::isValidBlockConfig);

		// Sea Dragon Bonuses
		builder.pop().push("sea");
		seaSwimmingBonuses = builder
				.comment("Whether sea dragons gain bonus swim speed and unlimited air.")
				.define("waterBonuses", true);
		seaSpeedupBlocks = builder
				.comment("Blocks sea dragons gain speed when standing above. Formatting: block/tag:modid:id")
				.worldRestart()
				.defineList("seaSpeedupBlocks", Arrays.asList(
						"tag:minecraft:ice",
						"tag:minecraft:impermeable",
						"tag:minecraft:sand",
						"tag:minecraft:coral_blocks",
						"tag:forge:sand",
						"block:minecraft:dirt_path",
						"block:minecraft:sandstone",
						"block:minecraft:cut_sandstone",
						"block:minecraft:chiseled_sandstone",
						"block:minecraft:smooth_sandstone",
						"block:minecraft:red_sandstone",
						"block:minecraft:cut_red_sandstone",
						"block:minecraft:chiseled_red_sandstone",
						"block:minecraft:smooth_red_sandstone",
						"block:minecraft:water"
				), this::isValidBlockConfig);

		//Dragon Penalties
		builder.pop().pop().push("penalties");
		penalties = builder
				.comment("Set to false to toggle off all dragon penalties.")
				.define("penalties", true);
		allowedVehicles = builder
				.comment("List of rideable entities. Format: modid:id")
				.defineList("allowedVehicles", Lists.newArrayList(
						"minecraft:boat"
				), value -> value instanceof String);
		
		ridingBlacklist= builder
				.comment("Should dragons be limited by which entities they can ride")
				.define("limitedRiding", true);
		
		blacklistedItems = builder
				.comment("List of items that disallowed to be used by dragons. Format: item/tag:modid:id")
				.defineList("blacklistedItems", Arrays.asList(
						"item:minecraft:bow",
						"item:spartanshields:shield_basic_nickel",
						"item:spartanshields:shield_basic_invar",
						"item:spartanshields:shield_basic_constantan",
						"item:spartanshields:shield_basic_platinum",
						"item:spartanshields:shield_mekanism_refined_glowstone",
						"item:spartanshields:shield_tower_wood",
						"item:spartanshields:shield_tower_stone",
						"item:spartanshields:shield_tower_iron",
						"item:spartanshields:shield_tower_gold",
						"item:spartanshields:shield_tower_diamond",
						"item:spartanshields:shield_tower_netherite",
						"item:spartanshields:shield_tower_obsidian",
						"item:spartanshields:shield_tower_copper",
						"item:spartanshields:shield_tower_tin",
						"item:spartanshields:shield_tower_bronze",
						"item:spartanshields:shield_tower_steel",
						"item:spartanshields:shield_tower_silver",
						"item:spartanshields:shield_tower_lead",
						"item:spartanshields:shield_tower_nickel",
						"item:spartanshields:shield_tower_constantan",
						"item:spartanshields:shield_tower_invar",
						"item:spartanshields:shield_tower_platinum",
						"item:spartanshields:shield_tower_electrum",
						"item:spartanshields:shield_mekanism_powered_ultimate",
						"item:quark:flamerang", "item:quark:pickarang",
						"item:spartanshields:shield_botania_manasteel",
						"item:spartanshields:shield_botania_elementium",
						"item:spartanshields:shield_mekanism_osmium",
						"item:spartanshields:shield_mekanism_lapis_lazuli",
						"item:spartanshields:shield_basic_electrum",
						"item:spartanshields:shield_mekanism_refined_obsidian",
						"item:spartanshields:shield_mekanism_powered_basic",
						"item:spartanshields:shield_mekanism_powered_advanced",
						"item:spartanshields:shield_mekanism_powered_elite",
						"item:spartanweaponry:boomerang_steel",
						"item:spartanweaponry:boomerang_invar",
						"item:spartanweaponry:boomerang_platinum",
						"item:spartanweaponry:boomerang_electrum",
						"item:spartanshields:shield_basic_bronze",
						"item:spartanshields:shield_basic_tin",
						"item:spartanshields:shield_basic_copper",
						"item:spartanshields:shield_basic_obsidian",
						"item:spartanshields:shield_basic_netherite",
						"item:spartanshields:shield_basic_diamond",
						"item:spartanshields:shield_basic_gold",
						"item:spartanshields:shield_basic_iron",
						"item:spartanshields:shield_basic_stone",
						"item:spartanshields:shield_basic_wood",
						"item:spartanweaponry:boomerang_lead",
						"item:spartanweaponry:boomerang_nickel",
						"item:spartanshields:shield_basic_steel",
						"item:spartanshields:shield_basic_silver",
						"item:spartanshields:shield_basic_lead",
						"item:spartanweaponry:boomerang_bronze",
						"item:spartanweaponry:boomerang_tin",
						"item:spartanweaponry:boomerang_copper",
						"item:spartanweaponry:boomerang_netherite",
						"item:spartanweaponry:boomerang_gold",
						"item:spartanweaponry:boomerang_iron",
						"item:spartanweaponry:boomerang_stone",
						"item:spartanweaponry:heavy_crossbow_bronze",
						"item:spartanshields:shield_botania_terrasteel",
						"item:spartanweaponry:heavy_crossbow_leather",
						"item:spartanweaponry:heavy_crossbow_iron",
						"item:spartanweaponry:heavy_crossbow_gold",
						"item:spartanweaponry:heavy_crossbow_diamond",
						"item:spartanweaponry:heavy_crossbow_netherite",
						"item:spartanweaponry:heavy_crossbow_copper",
						"item:spartanweaponry:heavy_crossbow_tin",
						"item:spartanweaponry:boomerang_wood",
						"item:nethers_exoticism:rambutan_shield",
						"item:minecraft:shield",
						"item:minecraft:trident",
						"item:spartanweaponry:heavy_crossbow_lead",
						"item:spartanweaponry:heavy_crossbow_nickel",
						"item:spartanweaponry:heavy_crossbow_electrum",
						"item:spartanweaponry:heavy_crossbow_platinum",
						"item:spartanweaponry:heavy_crossbow_invar",
						"item:spartanweaponry:heavy_crossbow_silver",
						"item:spartanweaponry:heavy_crossbow_steel",
						"item:spartanweaponry:boomerang_diamond",
						"item:spartanweaponry:heavy_crossbow_wood",
						"item:minecraft:crossbow",
						"item:aquaculture:neptunium_bow",
						"item:spartanweaponry:longbow_electrum",
						"item:spartanweaponry:longbow_invar",
						"item:infernalexp:glowsilk_bow",
						"item:spartanweaponry:longbow_wood",
						"item:spartanweaponry:longbow_leather",
						"item:spartanweaponry:longbow_silver",
						"item:spartanweaponry:longbow_steel",
						"item:spartanweaponry:longbow_bronze",
						"item:spartanweaponry:longbow_tin",
						"item:spartanweaponry:longbow_copper",
						"item:spartanweaponry:longbow_netherite",
						"item:spartanweaponry:longbow_diamond",
						"item:spartanweaponry:longbow_gold",
						"item:spartanweaponry:longbow_iron",
						"item:spartanweaponry:boomerang_diamond",
						"item:spartanweaponry:boomerang_iron",
						"item:spartanweaponry:boomerang_wood",
						"item:spartanweaponry:boomerang_gold",
						"item:spartanweaponry:boomerang_netherite",
						"item:spartanweaponry:boomerang_copper",
						"item:spartanweaponry:boomerang_tin",
						"item:spartanweaponry:boomerang_bronze",
						"item:spartanweaponry:boomerang_stone",
						"item:spartanweaponry:boomerang_platinum",
						"item:spartanweaponry:boomerang_electrum",
						"item:spartanweaponry:boomerang_steel",
						"item:spartanweaponry:boomerang_lead",
						"item:spartanweaponry:boomerang_invar",
						"item:spartanweaponry:boomerang_nickel"
				), this::isValidItemConfig);
		blacklistedSlots = builder
				.comment("List of slots to handle blacklistedItems option")
				.defineList("blacklistedSlots", Arrays.asList(
						0, 1, 2, 3, 4, 5, 6, 7, 8, 45
				), value -> value instanceof Integer);

		// Cave Dragon Penalties
		builder.push("cave");
		caveWaterDamage = builder
				.comment("The amount of damage taken per water damage tick (once every 10 ticks). Set to 0.0 to disable water damage.")
				.defineInRange("waterDamage", 1.0, 0.0, 100.0);
		caveRainDamage = builder
				.comment("The amount of damage taken per rain damage tick (once every 40 ticks). Set to 0.0 to disable rain damage.")
				.defineInRange("rainDamage", 1.0, 0.0, 100.0);
		caveSplashDamage = builder
				.comment("The amount of damage taken when hit with a snowball or a water bottle. Set to 0.0 to disable splash damage.")
				.defineInRange("splashDamage", 2.0, 0.0, 100.0);

		// Forest Dragon Penalties
		builder.pop().push("forest");
		forestStressTicks = builder
				.comment("The number of ticks in darkness before the forest dragon gets Stress effect. Set to 0 to disable to stress effect.")
				.defineInRange("ticksBeforeStressed", 70, 0, 10000);
		forestStressEffectDuration = builder
				.comment("The number of seconds the stress effect lasts for.")
				.defineInRange("stressEffectDuration", 50, 2, 100000);
		stressExhaustion = builder
				.comment("The amount of exhaustion applied per 10 ticks during the stress effect.")
				.defineInRange("stressExhaustion", 1.0, 0.1, 4.0);

		// Sea Dragon Penalties
		builder.pop().push("sea");
		seaTicksWithoutWater = builder
				.comment("The number of ticks out of water before the sea dragon will start taking dehydration damage. Set to 0 to disable. Note: This value can stack up to double while dehydrated.")
				.defineInRange("ticksWithoutWater", 1200, 0, 100000);
		seaTicksBasedOnTemperature = builder
				.comment("Whether the sea dragon should lose more water in warmer biomes and less during the night.")
				.define("waterConsumptionDependsOnTemperature", true);
		seaDehydrationDamage = builder
				.comment("The amount of damage taken per tick while dehydrated (once every 40 ticks unless fully dehydrated, then once every 20 ticks).")
				.defineInRange("dehydrationDamage", 1.0, 0.5, 100.0);
		seaHydrationBlocks = builder
				.comment("When sea dragons stand on these blocks, hydration is restored. Format: block/tag:modid:id")
				.worldRestart()
				.defineList("seaHydrationBlocks", Arrays.asList(
						"tag:minecraft:ice",
						"block:minecraft:snow",
						"block:minecraft:snow_block",
						"block:dragonsurvival:sea_source_of_magic"
				), this::isValidBlockConfig);
		seaAllowWaterBottles = builder
				.comment("Set to false to disable sea dragons using vanilla water bottles to avoid dehydration.")
				.define("allowWaterBottles", true);
		seaTicksWithoutWaterRestored = builder
				.comment("How many ticks do water restoration items restore when used. Set to 0 to disable.")
				.defineInRange("waterItemRestorationTicks", 5000, 0, 100000);
		seaAdditionalWaterUseables = builder
				.comment("Additional modded USEABLE items that restore water when used (called from LivingEntityUseItemEvent.Finish). Format: item/tag:modid:id")
				.worldRestart()
				.defineList("seaHydrationItems", Collections.singletonList(
						"item:minecraft:enchanted_golden_apple"
				), this::isValidItemConfig);
		// Ore Loot
		builder.pop().pop().push("ore");
		humanOreDustChance = builder
				.comment("The odds of dust dropping when a human harvests an ore.")
				.defineInRange("humanOreDustChance", 0.1, 0.0, 1.0);
        dragonOreDustChance = builder
        		.comment("The odds of dust dropping when a dragon harvests an ore.")
        		.defineInRange("dragonOreDustChance", 0.2, 0.0, 1.0);
        humanOreBoneChance = builder
        		.comment("The odds of a bone dropping when a human harvests an ore.")
        		.defineInRange("humanOreBoneChance", 0.0, 0.0, 1.0);
        dragonOreBoneChance = builder
        		.comment("The odds of a bone dropping when a dragon harvests an ore.")
        		.defineInRange("dragonOreBoneChance", 0.01, 0.0, 1.0);
        oresTag = builder
        		.comment("The tag that contains all ores that can drop dust/bones when harvested. Will not drop if the ore drops another of the items in this tag. Format: modid:id")
        		.define("oresTag", "forge:ores");
		// Food general
		builder.pop().push("food");
		customDragonFoods = builder
				.comment("Force dragons to eat a unique diet for their type.")
				.define("dragonFoods", true);
		caveDragonHurtfulItems = builder
				.comment("Items which will cause damage to cave dragons when consumed. Formatting: item/tag:modid:itemid:damage")
				.defineList("hurtfulToCaveDragon", Arrays.asList(
						"item:minecraft:potion:2",
						"item:minecraft:water_bottle:2",
						"item:minecraft:milk_bucket:2"
				), this::isValidHurtfulItem);
		seaDragonHurtfulItems = builder
				.comment("Items which will cause damage to sea dragons when consumed. Formatting: item/tag:modid:itemid:damage")
				.defineList("hurtfulToSeaDragon", Arrays.asList(), this::isValidHurtfulItem);
		forestDragonHurtfulItems = builder
				.comment("Items which will cause damage to forest dragons when consumed. Formatting: item/tag:modid:itemid:damage")
				.defineList("hurtfulToForestDragon", Arrays.asList(),  this::isValidHurtfulItem);
		chargedSoupBuffDuration = builder
				.comment("How long in seconds should the cave fire effect from charged soup last. (Default to 5min) Set to 0 to disable.")
				.defineInRange("chargedSoupBuffDuration", 300, 0, 10000);

		// Dragon Food List
		builder.push("list");
		builder.comment("Dragon food formatting: item/tag:modid:id:food:saturation. Food/saturation values are optional as the human values will be used if missing.");

		caveDragonFoods = builder
				.worldRestart()
				.defineList("caveDragon", Arrays.asList(
						"tag:minecraft:coals:1:1",
						"item:minecraft:charcoal:1:2",
						"item:minecraft:golden_apple",
						"item:minecraft:enchanted_golden_apple",
						"item:dragonsurvival:charged_coal:6:1",
						"item:dragonsurvival:charred_meat:10:12",
						"item:dragonsurvival:cave_dragon_treat:10:12",
						"item:dragonsurvival:charred_seafood:8:10",
						"item:dragonsurvival:charred_vegetable:8:9",
						"item:dragonsurvival:charred_mushroom:8:5",
						"item:dragonsurvival:charged_soup:20:15",
						"item:desolation:cinder_fruit:6:7",
						"item:desolation:powered_cinder_fruit:8:12",
						"item:desolation:activatedcharcoal:2:2",
						"item:desolation:infused_powder:10:10",
						"item:desolation:primed_ash:7:8",
						"item:pickletweaks:diamond_apple",
						"item:pickletweaks:emerald_apple",
						"item:undergarden:ditchbulb:5,6",
						"item:xreliquary:molten_core:1:1",
						"item:silents_mechanisms:coal_generator_fuels:1:1",
						"item:mekanism:dust_charcoal:1:1",
						"item:mekanism:dust_coal:1:1",
						"item:rats:nether_cheese",
						"item:potionsmaster:charcoal_powder:1:1",
						"item:potionsmaster:coal_powder:1:1",
						"item:potionsmaster:activated_charcoal:2:2",
						"item:thermal:coal_coke:1:1",
						"item:infernalexp:glowcoal:2:3",
						"item:resourcefulbees:coal_honeycomb:5:5",
						"item:resourcefulbees:netherite_honeycomb:5:5",
						"item:lazierae2:coal_dust:1:1",
						"item:wyrmroost:jewelled_apple",
						"item:silents_mechanisms:coal_dust:1:1",
						"item:potionsmaster:calcinatedcoal_powder:1:1",
						"item:thermal:basalz_rod:2:4",
						"item:thermal:basalz_powder:1:2",
						"item:druidcraft:fiery_glass:2:2"
				), this::isValidFoodConfig);

		forestDragonFoods = builder
				.worldRestart()
				.defineList("forestDragon", Arrays.asList(
						"tag:forge:raw_meats:5:7",
						"item:minecraft:sweet_berries:2:2",
						"item:minecraft:rotten_flesh:2:3",
						"item:minecraft:spider_eye:7:8",
						"item:minecraft:rabbit:7:13",
						"item:minecraft:poisonous_potato:7:10",
						"item:minecraft:chorus_fruit:9:12",
						"item:minecraft:golden_apple",
						"item:minecraft:enchanted_golden_apple",
						"item:minecraft:honey_bottle",
						"item:dragonsurvival:forest_dragon_treat:10:12",
						"item:aoa3:fiery_chops:6:7",
						"item:aoa3:raw_chimera_chop:6:7",
						"item:aoa3:raw_furlion_chop:6:7",
						"item:aoa3:raw_halycon_beef:7:8",
						"item:aoa3:raw_charger_shank:6:7",
						"item:aoa3:trilliad_leaves:8:11",
						"item:aoa3:heart_fruit:9:10",
						"item:pamhc2foodextended:rawtofabbititem",
						"item:pamhc2foodextended:rawtofickenitem",
						"item:pamhc2foodextended:rawtofuttonitem",
						"item:alexsmobs:kangaroo_meat:5:6",
						"item:alexsmobs:moose_ribs:6:8",
						"item:simplefarming:raw_horse_meat:5:6",
						"item:simplefarming:raw_bacon:3:3",
						"item:simplefarming:raw_chicken_wings:2:3",
						"item:simplefarming:raw_sausage:3:4",
						"item:xenoclustwo:raw_tortice:7:8",
						"item:unnamedanimalmod:musk_ox_shank:7:8",
						"item:unnamedanimalmod:frog_legs:5:6",
						"item:unnamedanimalmod:mangrove_fruit:4:7",
						"item:betteranimalsplus:venisonraw:5:6",
						"item:betteranimalsplus:pheasantraw:7:5",
						"item:betteranimalsplus:turkey_leg_raw:4:5",
						"item:infernalexp:raw_hogchop:6:7",
						"item:infernalexp:cured_jerky:10:7",
						"item:druidcraft:elderberries:3:4",
						"item:rats:raw_rat:4:5",
						"item:aquaculture:frog:4:5",
						"item:aquaculture:frog_legs_raw:4:4",
						"item:aquaculture:box_turtle:4:5",
						"item:aquaculture:arrau_turtle:4:5",
						"item:aquaculture:starshell_turtle:4:5",
						"item:nethers_exoticism:kiwano:3:4",
						"item:undergarden:raw_gloomper_leg:4:5",
						"item:undergarden:raw_dweller_meat:6:7",
						"item:farmersdelight:chicken_cuts:3:3",
						"item:farmersdelight:bacon:3:3",
						"item:farmersdelight:ham:9:10",
						"item:farmersdelight:minced_beef:5:3",
						"item:farmersdelight:mutton_chops:5:3",
						"item:abnormals_delight:duck_fillet:2:3",
						"item:abnormals_delight:venison_shanks:7:3",
						"item:pickletweaks:diamond_apple",
						"item:pickletweaks:emerald_apple",
						"item:autumnity:foul_berries:2:4",
						"item:autumnity:turkey:7:8",
						"item:autumnity:turkey_piece:2:4",
						"item:autumnity:foul_soup:12:8",
						"item:endergetic:bolloom_fruit:3:4",
						"item:quark:frog_leg:4:5",
						"item:nethers_delight:hoglin_loin:8:6",
						"item:nethers_delight:raw_stuffed_hoglin:18:10",
						"item:xreliquary:zombie_heart:4:7",
						"item:xreliquary:bat_wing:2:2",
						"item:eidolon:zombie_heart:7:7",
						"item:forbidden_arcanus:bat_wing:5:2",
						"item:twilightforest:raw_venison:5:5",
						"item:twilightforest:raw_meef:9:5",
						"item:twilightforest:hydra_chop",
						"item:cyclic:chorus_flight",
						"item:cyclic:chorus_spectral",
						"item:cyclic:apple_ender",
						"item:cyclic:apple_honey",
						"item:cyclic:apple_chorus",
						"item:cyclic:apple_bone",
						"item:cyclic:apple_prismarine",
						"item:cyclic:apple_lapis",
						"item:cyclic:apple_iron",
						"item:cyclic:apple_diamond",
						"item:cyclic:apple_emerald",
						"item:cyclic:apple_chocolate",
						"item:cyclic:toxic_carrot:15:15",
						"item:artifacts:everlasting_beef",
						"item:resourcefulbees:rainbow_honey_bottle",
						"item:resourcefulbees:diamond_honeycomb:5:5",
						"item:byg:soul_shroom:9:5",
						"item:byg:death_cap:9:8",
						"item:byg:holly_berries:2:2",
						"item:minecolonies:chorus_bread",
						"item:wyrmroost:jewelled_apple",
						"item:wyrmroost:raw_lowtier_meat:3:2",
						"item:wyrmroost:raw_common_meat:5:3",
						"item:wyrmroost:raw_apex_meat:8:6",
						"item:wyrmroost:raw_behemoth_meat:11:12",
						"item:wyrmroost:desert_wyrm:4:3",
						"item:eanimod:rawchicken_darkbig:9:5",
						"item:eanimod:rawchicken_dark:5:4",
						"item:eanimod:rawchicken_darksmall:3:2",
						"item:eanimod:rawchicken_pale:5:3",
						"item:eanimod:rawchicken_palesmall:4:3",
						"item:eanimod:rawrabbit_small:4:4",
						"item:environmental:duck:4:3",
						"item:environmental:venison:7:7",
						"item:cnb:lizard_item_0:4:4",
						"item:cnb:lizard_item_1:4:4",
						"item:cnb:lizard_item_2:4:4",
						"item:cnb:lizard_item_3:4:4",
						"item:snowpig:frozen_porkchop:7:3",
						"item:snowpig:frozen_ham:5:7",
						"item:untamedwilds:snake_grass_snake:4:4",
						"item:untamedwilds:snake_green_mamba:4:4",
						"item:untamedwilds:snake_rattlesnake:4:4",
						"item:untamedwilds:snake_emerald:4:4",
						"item:untamedwilds:snake_carpet_python:4:4",
						"item:untamedwilds:snake_corn:4:4",
						"item:untamedwilds:snake_gray_kingsnake:4:4",
						"item:untamedwilds:snake_coral:4:4",
						"item:untamedwilds:snake_ball_python:4:4",
						"item:untamedwilds:snake_black_mamba:4:4",
						"item:untamedwilds:snake_western_rattlesnake:4:4",
						"item:untamedwilds:snake_taipan:4:4",
						"item:untamedwilds:snake_adder:4:4",
						"item:untamedwilds:snake_rice_paddy:4:4",
						"item:untamedwilds:snake_coral_blue:4:4",
						"item:untamedwilds:snake_cave_racer:4:4",
						"item:untamedwilds:snake_swamp_moccasin:4:4",
						"item:untamedwilds:softshell_turtle_pig_nose:4:4",
						"item:untamedwilds:softshell_turtle_flapshell:4:4",
						"item:untamedwilds:softshell_turtle_chinese:4:4",
						"item:untamedwilds:tortoise_asian_box:4:4",
						"item:untamedwilds:tortoise_gopher:4:4",
						"item:untamedwilds:tortoise_leopard:4:4",
						"item:untamedwilds:softshell_turtle_peacock:4:4",
						"item:untamedwilds:softshell_turtle_nile:4:4",
						"item:untamedwilds:softshell_turtle_spiny:4:4",
						"item:untamedwilds:tortoise_sulcata:4:4",
						"item:untamedwilds:tortoise_star:4:4",
						"item:untamedwilds:tortoise_marginated:4:4",
						"item:leescreatures:raw_boarlin:6:6",
						"item:mysticalworld:venison:5:5",
						"item:toadterror:toad_chops:8:7"
				), this::isValidFoodConfig);

		seaDragonFoods = builder
				.worldRestart()
				.defineList("seaDragon", Arrays.asList(
						"tag:forge:raw_fishes:6:7",
						"item:minecraft:dried_kelp:1:1",
						"item:minecraft:kelp:2:3",
						"item:minecraft:pufferfish:10:15",
						"item:minecraft:golden_apple",
						"item:minecraft:enchanted_golden_apple",
						"item:minecraft:honey_bottle",
						"item:dragonsurvival:sea_dragon_treat:10:12",
						"item:aoa3:raw_candlefish:9:9",
						"item:aoa3:raw_crimson_skipper:8:8",
						"item:aoa3:raw_fingerfish:4:4",
						"item:aoa3:raw_pearl_stripefish:5:4",
						"item:aoa3:raw_limefish:5:5",
						"item:aoa3:raw_sailback:6:5",
						"item:aoa3:raw_golden_gullfish:10:2",
						"item:aoa3:raw_turquoise_stripefish:7:6",
						"item:aoa3:raw_violet_skipper:7:7",
						"item:aoa3:raw_rocketfish:4:10",
						"item:aoa3:raw_crimson_stripefish:8:7",
						"item:aoa3:raw_sapphire_strider:9:8",
						"item:aoa3:raw_dark_hatchetfish:9:9",
						"item:aoa3:raw_ironback:10:9",
						"item:aoa3:raw_rainbowfish:11:11",
						"item:aoa3:raw_razorfish:12:14",
						"item:quark:golden_frog_leg",
						"item:alexsmobs:lobster_tail:4:5",
						"item:alexsmobs:blobfish:8:9",
						"item:oddwatermobs:raw_ghost_shark:8:8",
						"item:oddwatermobs:raw_isopod:4:2",
						"item:oddwatermobs:raw_mudskipper:6:7",
						"item:oddwatermobs:raw_coelacanth:9:10",
						"item:oddwatermobs:raw_anglerfish:6:6",
						"item:oddwatermobs:deep_sea_fish:4:2",
						"item:oddwatermobs:crab_leg:5:6",
						"item:simplefarming:raw_calamari:5:6",
						"item:unnamedanimalmod:elephantnose_fish:5:6",
						"item:unnamedanimalmod:flashlight_fish:5:6",
						"item:unnamedanimalmod:rocket_killifish:5:6",
						"item:unnamedanimalmod:leafy_seadragon:5:6",
						"item:unnamedanimalmod:elephantnose_fish:5:6",
						"item:betteranimalsplus:eel_meat_raw:5:6",
						"item:betteranimalsplus:calamari_raw:4:5",
						"item:betteranimalsplus:crab_meat_raw:4:4",
						"item:aquaculture:fish_fillet_raw:2:2",
						"item:aquaculture:goldfish:8:4",
						"item:aquaculture:box_turtle:4:5",
						"item:aquaculture:arrau_turtle:4:5",
						"item:aquaculture:starshell_turtle:4:5",
						"item:aquaculture:algae:3:2",
						"item:betterendforge:end_fish_raw:6:7",
						"item:betterendforge:hydralux_petal:3:3",
						"item:betterendforge:charnia_green:2:2",
						"item:shroomed:raw_shroomfin:5:6",
						"item:undergarden:raw_gwibling:5:6",
						"item:pickletweaks:diamond_apple",
						"item:pickletweaks:emerald_apple",
						"item:bettas:betta_fish:4:5",
						"item:quark:crab_leg:4:4",
						"item:pamhc2foodextended:rawtofishitem",
						"item:fins:banded_redback_shrimp:6:1",
						"item:fins:night_light_squid:6:2",
						"item:fins:night_light_squid_tentacle:6:2",
						"item:fins:emerald_spindly_gem_crab:7:2",
						"item:fins:amber_spindly_gem_crab:7:2",
						"item:fins:rubby_spindly_gem_crab:7:2",
						"item:fins:sapphire_spindly_gem_crab:7:2",
						"item:fins:pearl_spindly_gem_crab:7:2",
						"item:fins:papa_wee:6:2",
						"item:fins:bugmeat:4:2",
						"item:fins:raw_golden_river_ray_wing:6:2",
						"item:fins:red_bull_crab_claw:4:4",
						"item:fins:white_bull_crab_claw:4:4",
						"item:fins:wherble_fin:1:1",
						"item:forbidden_arcanus:tentacle:5:2",
						"item:pneumaticcraft:raw_salmon_tempura:6:10",
						"item:rats:ratfish:4:2",
						"item:cyclic:chorus_flight",
						"item:cyclic:chorus_spectral",
						"item:cyclic:apple_ender",
						"item:cyclic:apple_honey",
						"item:cyclic:apple_chorus",
						"item:cyclic:apple_bone",
						"item:cyclic:apple_prismarine",
						"item:cyclic:apple_lapis",
						"item:cyclic:apple_iron",
						"item:cyclic:apple_diamond",
						"item:cyclic:apple_emerald",
						"item:cyclic:apple_chocolate",
						"item:upgrade_aquatic:purple_pickerelweed:2:2",
						"item:upgrade_aquatic:blue_pickerelweed:2:2",
						"item:upgrade_aquatic:polar_kelp:2:2",
						"item:upgrade_aquatic:tongue_kelp:2:2",
						"item:upgrade_aquatic:thorny_kelp:2:2",
						"item:upgrade_aquatic:ochre_kelp:2:2",
						"item:upgrade_aquatic:lionfish:8:9",
						"item:resourcefulbees:gold_honeycomb:5:5",
						"item:resourcefulbees:rainbow_honey_bottle",
						"item:wyrmroost:jewelled_apple",
						"item:aquaculture:sushi:6:5",
						"item:freshwarriors:fresh_soup:15:10",
						"item:freshwarriors:beluga_caviar:10:3",
						"item:freshwarriors:piranha:4:1",
						"item:freshwarriors:tilapia:4:1",
						"item:freshwarriors:stuffed_piranha:4:1",
						"item:freshwarriors:tigerfish:5:5",
						"item:freshwarriors:toe_biter_leg:3:3",
						"item:untamedwilds:egg_arowana_black:4:4",
						"item:untamedwilds:egg_trevally_jack:4:4",
						"item:untamedwilds:egg_trevally_golden:4:4",
						"item:untamedwilds:egg_giant_salamander_chinese:6:4",
						"item:untamedwilds:egg_giant_salamander_hellbender:6:4",
						"item:untamedwilds:egg_giant_salamander_japanese:6:4",
						"item:untamedwilds:giant_clam_gigas:4:4",
						"item:untamedwilds:giant_clam_derasa:4:4",
						"item:untamedwilds:giant_clam_maxima:4:4",
						"item:untamedwilds:giant_clam_squamosa:4:4",
						"item:untamedwilds:egg_trevally_giant:6:4",
						"item:untamedwilds:egg_trevally_bluespotted:6:4",
						"item:untamedwilds:egg_trevally_bigeye:6:4",
						"item:untamedwilds:egg_sunfish_southern:6:4",
						"item:untamedwilds:egg_sunfish_sunfish:6:4",
						"item:untamedwilds:egg_giant_clam_squamosa:6:4",
						"item:untamedwilds:egg_giant_clam_gigas:6:4",
						"item:untamedwilds:egg_giant_clam_derasa:6:4",
						"item:untamedwilds:egg_giant_clam_maxima:6:4",
						"item:untamedwilds:egg_football_fish_atlantic:6:4",
						"item:untamedwilds:egg_arowana_silver:6:4",
						"item:untamedwilds:egg_arowana_jardini:6:4",
						"item:untamedwilds:egg_arowana_green:6:4",
						"item:mysticalworld:raw_squid:6:5",
						"item:aquafina:fresh_soup:15:10",
						"item:aquafina:beluga_caviar:10:3",
						"item:aquafina:raw_piranha:4:1",
						"item:aquafina:raw_tilapia:4:1",
						"item:aquafina:stuffed_piranha:4:1",
						"item:aquafina:tigerfish:5:5",
						"item:aquafina:toe_biter_leg:3:3",
						"item:aquafina:raw_angelfish:4:1",
						"item:aquafina:raw_football_fish:4:1",
						"item:aquafina:raw_foxface_fish:4:1",
						"item:aquafina:raw_royal_gramma:4:1",
						"item:aquafina:raw_starfish:4:1",
						"item:aquafina:spider_crab_leg:4:1",
						"item:aquafina:raw_stingray_slice:4:1"
				), this::isValidFoodConfig);
		//Magic
		builder.pop().pop().push("magic");
		builder.comment("Config values for the magic system");
		
		dragonAbilities = builder
				.comment("Whether dragon abilities should be enabled")
				.define("dragonAbilities", true);
		
		caveDragonAbilities = builder
				.comment("Whether cave dragon abilities should be enabled")
				.define("caveDragonAbilities", true);
		
		forestDragonAbilities = builder
				.comment("Whether forest dragon abilities should be enabled")
				.define("forestDragonAbilities", true);
		
		seaDragonAbilities = builder
				.comment("Whether sea dragon abilities should be enabled")
				.define("seaDragonAbilities", true);
		
		
		noEXPRequirements = builder
				.comment("Disable the exp requirements for leveling up active skills")
				.define("noEXPRequirements", false);
		
		consumeEXPAsMana = builder
				.comment("Whether to use exp instead of mana if mana is empty")
				.define("consumeEXPAsMana", true);
		
		favorableManaTicks = builder
				.comment("How fast in seconds should mana be recovered in favorable conditions")
				.defineInRange("favorableManaRegen", 5, 1, 1000);
		
		normalManaTicks = builder
				.comment("How fast in seconds should mana be recovered in normal conditions")
				.defineInRange("normalManaRegen", 15, 1, 1000);
		
		builder.push("abilities");
		
		saveAllAbilities = builder
				.comment("Whether to save passives skills when changing dragon type")
				.define("saveAllAbilities", false);
		
		builder.push("forest_dragon");
		builder.push("actives");
		
		{
			forestBreath = builder
					.comment("Whether the forest breath ability should be enabled")
					.define("forestBreath", true);
			
			forestBreathDamage = builder
					.comment("The amount of damage the forest breath ability deals. This value is multiplied by the skill level.")
					.defineInRange("forestBreathDamage", 2.0, 0, 100.0);
			
			forestBreathInitialMana = builder
					.comment("The mana cost for starting the forest breath ability")
					.defineInRange("forestBreathInitialMana", 2, 0, 100);
			
			forestBreathOvertimeMana = builder
					.comment("The mana cost of sustaining the forest breath ability")
					.defineInRange("forestBreathOvertimeMana", 1, 0, 100);
			
			forestBreathManaTicks = builder
					.comment("How often in ticks, mana is consumed while using forest breath")
					.defineInRange("forestBreathManaTicks", Functions.secondsToTicks(2), 0, 100);
			
			forestBreathBlockBreaks = builder
					.comment("Blocks that have a chance to be broken by forest breath. Formatting: block/tag:modid:id")
					.worldRestart()
					.defineList("stormBreathBlockBreaks", Arrays.asList(
							"tag:minecraft:banners"
					), this::isValidBlockConfig);
		}
		
		{
			spike = builder
					.comment("Whether the spike ability should be enabled")
					.define("spike", true);
			
			spikeDamage = builder
					.comment("The amount of damage the spike ability deals. This value is multiplied by the skill level.")
					.defineInRange("spikeDamage", 2.0, 0, 100.0);
			
			spikeManaCost = builder
					.comment("The mana cost for using the spike ability")
					.defineInRange("spikeManaCost", 2, 0, 100);
			
		}
		
		{
			inspiration = builder
					.comment("Whether the inspiration ability should be enabled")
					.define("inspiration", true);
			
			inspirationDuration = builder
					.comment("The duration in seconds of the inspiration effect given when the ability is used")
					.defineInRange("inspirationDuration", 60, 0, 10000);
			
			inspirationManaCost = builder
					.comment("The mana cost for using the inspiration ability")
					.defineInRange("inspirationManaCost", 3, 0, 100);
			
		}
		
		{
			hunter = builder
					.comment("Whether the hunter ability should be enabled")
					.define("hunter", true);
			
			hunterDuration = builder
					.comment("The duration in seconds of the inspiration effect given when the ability is used")
					.defineInRange("hunterDuration", 60, 0, 10000);
			
			hunterDamageBonus = builder
					.comment("The damage bonus the hunter effect gives when invisible. This value is multiplied by the skill level.")
					.defineInRange("hunterDamageBonus", 1.5, 0, 100.0);
			
			hunterManaCost = builder
					.comment("The mana cost for using the inspiration ability")
					.defineInRange("hunterManaCost", 3, 0, 100);
			
		}
		
		builder.pop().push("passives");
		
		{
			forestMagic = builder
					.comment("Whether the forest magic ability should be enabled")
					.define("forestMagic", true);
			
			forestAthletics = builder
					.comment("Whether the forest athletics ability should be enabled")
					.define("forestAthletics", true);
			
			lightInDarkness = builder
					.comment("Whether the light in darkness ability should be enabled")
					.define("lightInDarkness", true);
			
			cliffHanger = builder
					.comment("Whether the cliffhanger ability should be enabled")
					.define("cliffHanger", true);
			
		}
		
		
		
		builder.pop().pop().push("sea_dragon");
		builder.push("actives");
		{
			stormBreath = builder
					.comment("Whether the storm breath ability should be enabled")
					.define("stormBreath", true);
			
			stormBreathDamage = builder
					.comment("The amount of damage the storm breath ability deals. This value is multiplied by the skill level.")
					.defineInRange("stormBreathDamage", 1.0, 0, 100.0);
			
			stormBreathInitialMana = builder
					.comment("The mana cost for starting the storm breath ability")
					.defineInRange("stormBreathInitialMana", 2, 0, 100);
			
			stormBreathOvertimeMana = builder
					.comment("The mana cost of sustaining the storm breath ability")
					.defineInRange("stormBreathOvertimeMana", 1, 0, 100);
			
			stormBreathManaTicks = builder
					.comment("How often in ticks, mana is consumed while using storm breath")
					.defineInRange("stormBreathManaTicks", Functions.secondsToTicks(2), 0, 100);
			
			stormBreathBlockBreaks = builder
					.comment("Blocks that have a chance to be broken by storm breath. Formatting: block/tag:modid:id")
					.worldRestart()
					.defineList("stormBreathBlockBreaks", Arrays.asList(
							"tag:minecraft:impermeable",
							"block:minecraft:snow",
							"tag:minecraft:crops",
							"tag:minecraft:flowers",
							"tag:minecraft:banners",
							"tag:minecraft:lush_plants_replaceable",
							"tag:minecraft:azalea_log_replaceable",
							"tag:minecraft:replaceable_plants",
							"tag:minecraft:leaves"
							), this::isValidBlockConfig);
			
			chargedBlacklist = builder
					.comment("List of entities that do not work with the charged effect. Format: modid:id")
					.worldRestart()
					.defineList("chargedBlacklist", Arrays.asList(
							"upgrade_aquatic:thrasher",
							"upgrade_aquatic:great_thrasher"
					), value -> value instanceof String);
		}
		
		{
			ballLightning = builder
					.comment("Whether the lightning ball ability should be enabled")
					.define("ballLightning", true);
			
			ballLightningDamage = builder
					.comment("The amount of damage the lightning ball ability deals. This value is multiplied by the skill level.")
					.defineInRange("ballLightningDamage", 4.0, 0, 100.0);
			
			ballLightningManaCost = builder
					.comment("The mana cost for using the lightning ball ability")
					.defineInRange("ballLightningManaCost", 3, 0, 100);
			
		}
		
		{
			revealingTheSoul = builder
					.comment("Whether the revealing The Soul ability should be enabled")
					.define("revealingTheSoul", true);
			
			revealingTheSoulDuration = builder
					.comment("The duration in seconds of the revealing The Soul effect given when the ability is used")
					.defineInRange("revealingTheSoulDuration", 60, 0, 10000);
			
			revealingTheSoulManaCost = builder
					.comment("The mana cost for using the revealing The Soul ability")
					.defineInRange("revealingTheSoulManaCost", 3, 0, 100);
			
			revealingTheSoulMaxEXP = builder
					.comment("The max amount of increased exp that can be gained from a single mob with reavling the soul")
					.defineInRange("revealingTheSoulMaxEXP", 20, 0, 10000);
			
			revealingTheSoulMultiplier = builder
					.comment("The multiplier that is applied to exp with revealing the soul, the extra exp is in addition to the normal drops. so 1.0 = 100% increase")
					.defineInRange("revealingTheSoulMultiplier", 1.0, 0, 10000);
		}
		
		{
			seaEyes = builder
					.comment("Whether the sea eyes ability should be enabled")
					.define("seaEyes", true);
			
			seaEyesDuration = builder
					.comment("The duration in seconds of the sea eyes effect given when the ability is used")
					.defineInRange("seaEyesDuration", 90, 0, 10000);
			
			seaEyesManaCost = builder
					.comment("The mana cost for using the sea eyes ability")
					.defineInRange("seaEyesManaCost", 2, 0, 100);
			
		}
		
		builder.pop().push("passives");
		
		{
			seaMagic = builder
					.comment("Whether the sea magic ability should be enabled")
					.define("seaMagic", true);
			
			seaAthletics = builder
					.comment("Whether the sea athletics ability should be enabled")
					.define("seaAthletics", true);
			
			water = builder
					.comment("Whether the water ability should be enabled")
					.define("water", true);
			
			spectralImpact = builder
					.comment("Whether the spectralImpact ability should be enabled")
					.define("spectralImpact", true);
			
		}
		
		builder.pop().pop().push("cave_dragon");
		builder.push("actives");
		
		{
			fireBreath = builder
					.comment("Whether the firebreath ability should be enabled")
					.define("fireBreath", true);
			
			fireBreathDamage = builder
					.comment("The amount of damage the firebreath ability deals. This value is multiplied by the skill level.")
					.defineInRange("fireBreathDamage", 3.0, 0, 100.0);
			
			fireBreathInitialMana = builder
					.comment("The mana cost for starting the firebreath ability")
					.defineInRange("fireBreathInitialMana", 2, 0, 100);
			
			fireBreathOvertimeMana = builder
					.comment("The mana cost of sustaining the firebreath ability")
					.defineInRange("fireBreathOvertimeMana", 1, 0, 100);
			
			fireBreathManaTicks = builder
					.comment("How often in ticks, mana is consumed while using fire breath")
					.defineInRange("fireBreathManaTicks", Functions.secondsToTicks(2), 0, 100);
			
			fireBreathSpreadsFire = builder
					.comment("Whether the fire breath actually spreads fire when used")
					.define("fireBreathSpreadsFire", true);
			
			fireBreathBlockBreaks = builder
					.comment("Blocks that have a chance to be broken by fire breath. Formatting: block/tag:modid:id")
					.worldRestart()
					.defineList("fireBreathBlockBreaks", Arrays.asList(
							"tag:minecraft:ice",
							"block:minecraft:snow",
							"tag:minecraft:crops",
							"tag:minecraft:leaves",
							"tag:minecraft:flowers",
							"tag:minecraft:banners",
							"tag:minecraft:lush_plants_replaceable",
							"tag:minecraft:azalea_log_replaceable",
							"tag:minecraft:replaceable_plants",
							"tag:minecraft:wooden_fences",
							"tag:minecraft:logs_that_burn",
							"tag:minecraft:mycelium",
							"tag:minecraft:wooden_stairs",
							"tag:minecraft:wooden_doors",
							"tag:minecraft:wool",
							"tag:minecraft:saplings",
							"tag:minecraft:impermeable",
							"block:minecraft:cobweb",
							"block:minecraft:large_fern",
							"block:minecraft:sugar_cane",
							"block:minecraft:snow_block"
					), this::isValidBlockConfig);
		}
		
		{
			fireball = builder
					.comment("Whether the fireball ability should be enabled")
					.define("fireball", true);
			
			fireballDamage = builder
					.comment("The amount of damage the fireball ability deals. This value is multiplied by the skill level.")
					.defineInRange("fireballDamage", 5.0, 0, 100.0);
			
			fireballManaCost = builder
					.comment("The mana cost for using the fireball ball ability")
					.defineInRange("fireballManaCost", 3, 0, 100);
			
		}
		
		{
			toughSkin = builder
					.comment("Whether the tough skin ability should be enabled")
					.define("toughSkin", true);
			
			toughSkinDuration = builder
					.comment("The duration in seconds of the tough skin effect given when the ability is used")
					.defineInRange("toughSkinDuration", 180, 0, 10000);
			
			toughSkinManaCost = builder
					.comment("The mana cost for using the tough skin ability")
					.defineInRange("toughSkinManaCost", 3, 0, 100);
			
			toughSkinArmorValue = builder
					.comment("The amount of extra armor given per level of tough skin effect")
					.defineInRange("toughSkinArmorValue", 3.0, 0, 10000);
			
		}
		
		{
			lavaVision = builder
					.comment("Whether the lava vision ability should be enabled")
					.define("lavaVision", true);
			
			lavaVisionDuration = builder
					.comment("The duration in seconds of the lava vision effect given when the ability is used")
					.defineInRange("lavaVisionDuration", 60, 0, 10000);
			
			lavaVisionManaCost = builder
					.comment("The mana cost for using the lava vision ability")
					.defineInRange("lavaVisionManaCost", 2, 0, 100);
			
		}
		
		builder.pop().push("passives");
		{
			caveMagic = builder
					.comment("Whether the cave magic ability should be enabled")
					.define("caveMagic", true);
			
			caveAthletics = builder
					.comment("Whether the cave athletics ability should be enabled")
					.define("caveAthletics", true);
			
			contrastShower = builder
					.comment("Whether the contrast shower ability should be enabled")
					.define("contrastShower", true);
			
			burn = builder
					.comment("Whether the burn ability should be enabled")
					.define("burn", true);
			
		}

		builder.pop().pop().pop();
	}

	private boolean isValidHurtfulItem(Object food){
		final String[] foodSplit = String.valueOf(food).split(":");
		if (foodSplit.length != 4 || !(foodSplit[0].equalsIgnoreCase("item") || foodSplit[0].equalsIgnoreCase("tag")))
			return false;
		try {
			final float damage = Float.parseFloat(foodSplit[3]);
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}

	private boolean isValidFoodConfig(Object food) {
		final String[] foodSplit = String.valueOf(food).split(":");
		if (foodSplit.length < 3 || foodSplit.length > 5 || foodSplit.length == 4 ||!(foodSplit[0].equalsIgnoreCase("item") || foodSplit[0].equalsIgnoreCase("tag")))
			return false;
		try {
			if (foodSplit.length == 5) {
				final int value = Integer.parseInt(foodSplit[3]);
				final int saturation = Integer.parseInt(foodSplit[4]);
				if (value > 20 || value < 1 || saturation < 1 || saturation > 20)
					return false;
			}
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}

	private boolean isValidBlockConfig(Object block) {
		final String[] blockSplit = String.valueOf(block).split(":");
		return blockSplit.length == 3 && (blockSplit[0].equalsIgnoreCase("block") || blockSplit[0].equalsIgnoreCase("tag"));
	}

	private boolean isValidItemConfig(Object item) {
		final String[] itemSplit = String.valueOf(item).split(":");
		return itemSplit.length == 3 && (itemSplit[0].equalsIgnoreCase("item") || itemSplit[0].equalsIgnoreCase("tag"));
	}

}
