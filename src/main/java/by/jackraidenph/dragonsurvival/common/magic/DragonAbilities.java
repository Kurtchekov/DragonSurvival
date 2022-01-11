package by.jackraidenph.dragonsurvival.common.magic;

import by.jackraidenph.dragonsurvival.util.Functions;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.common.magic.abilities.Actives.*;
import by.jackraidenph.dragonsurvival.common.magic.abilities.Actives.BreathAbilities.NetherBreathAbility;
import by.jackraidenph.dragonsurvival.common.magic.abilities.Actives.BreathAbilities.StormBreathAbility;
import by.jackraidenph.dragonsurvival.common.magic.abilities.Actives.BreathAbilities.ForestBreathAbility;
import by.jackraidenph.dragonsurvival.common.magic.abilities.Actives.BuffAbilities.AoeBuffAbility;
import by.jackraidenph.dragonsurvival.common.magic.abilities.Actives.BuffAbilities.HunterAbility;
import by.jackraidenph.dragonsurvival.common.magic.abilities.Actives.BuffAbilities.ToughSkinAbility;
import by.jackraidenph.dragonsurvival.common.magic.abilities.Actives.BuffAbilities.EyesBuffAbility;
import by.jackraidenph.dragonsurvival.common.magic.abilities.Innate.*;
import by.jackraidenph.dragonsurvival.common.magic.abilities.Passives.*;
import by.jackraidenph.dragonsurvival.common.magic.common.ActiveDragonAbility;
import by.jackraidenph.dragonsurvival.common.magic.common.DragonAbility;
import by.jackraidenph.dragonsurvival.common.magic.common.InnateDragonAbility;
import by.jackraidenph.dragonsurvival.common.magic.common.PassiveDragonAbility;
import by.jackraidenph.dragonsurvival.common.DragonEffects;
import by.jackraidenph.dragonsurvival.client.particles.DSParticles;
import by.jackraidenph.dragonsurvival.misc.DragonType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import java.util.ArrayList;
import java.util.HashMap;

public class DragonAbilities
{
	//Forest dragon
	public static ActiveDragonAbility FOREST_BREATH;
	public static ActiveDragonAbility SPIKE;
	public static ActiveDragonAbility INSPIRATION;
	public static ActiveDragonAbility HUNTER;
	
	public static PassiveDragonAbility FOREST_MAGIC;
	public static PassiveDragonAbility FOREST_ATHLETICS;
	public static PassiveDragonAbility LIGHT_IN_DARKNESS;
	public static PassiveDragonAbility CLIFFHANGER;
	
	public static InnateDragonAbility FOREST_CLAWS_AND_TEETH;
	public static InnateDragonAbility FOREST_WINGS;
	public static InnateDragonAbility FOREST_DRAGON;
	public static InnateDragonAbility FEAR_OF_DARK;
	
	//Sea dragon
	public static ActiveDragonAbility STORM_BREATH;
	public static ActiveDragonAbility BALL_LIGHTNING;
	public static ActiveDragonAbility REVEALING_THE_SOUL;
	public static ActiveDragonAbility SEA_EYES;
	
	public static PassiveDragonAbility SEA_MAGIC;
	public static PassiveDragonAbility SEA_ATHLETICS;
	public static PassiveDragonAbility WATER;
	public static PassiveDragonAbility SPECTRAL_IMPACT;
	
	public static InnateDragonAbility SEA_CLAWS_AND_TEETH;
	public static InnateDragonAbility SEA_WINGS;
	public static InnateDragonAbility SEA_DRAGON;
	public static InnateDragonAbility AMPHIBIAN;
	
	//Cave dragon
	public static ActiveDragonAbility NETHER_BREATH;
	public static ActiveDragonAbility FIREBALL;
	public static ActiveDragonAbility TOUGH_SKIN;
	public static ActiveDragonAbility LAVA_VISION;
	
	public static PassiveDragonAbility CAVE_MAGIC;
	public static PassiveDragonAbility CAVE_ATHLETICS;
	public static PassiveDragonAbility CONTRAST_SHOWER;
	public static PassiveDragonAbility BURN;
	
	public static InnateDragonAbility CAVE_CLAWS_AND_TEETH;
	public static InnateDragonAbility CAVE_WINGS;
	public static InnateDragonAbility CAVE_DRAGON;
	public static InnateDragonAbility HOT_BLOOD;
	
	public static void initAbilities(){
		//Forest dragon
		FOREST_BREATH = register(DragonType.FOREST, new ForestBreathAbility(DragonType.FOREST, "poisonous_breath", "forest/poisonous_breath", 1, 4, 2, 10, ConfigHandler.SERVER.forestBreathCooldown.get(), new Integer[]{0, 10, 30, 50}));
		SPIKE = register(DragonType.FOREST, new SpikeAbility(DragonType.FOREST, "spike", "forest/spike", 0, 4, ConfigHandler.SERVER.spikeManaCost.get(), 1, ConfigHandler.SERVER.spikeCooldown.get(), new Integer[]{0, 20, 30, 40}));
		INSPIRATION = register(DragonType.FOREST, new AoeBuffAbility(DragonType.FOREST, new EffectInstance(Effects.DIG_SPEED, ConfigHandler.SERVER.inspirationDuration.get(), 2), 5, DSParticles.fireBeaconParticle, "inspiration", "forest/inspiration", 0, 3, ConfigHandler.SERVER.inspirationManaCost.get(), Functions.secondsToTicks(5), ConfigHandler.SERVER.inspirationCooldown.get(), new Integer[]{0, 15, 35}));
		HUNTER = register(DragonType.FOREST, new HunterAbility(DragonType.FOREST, "hunter", "forest/hunter", 0, 2, ConfigHandler.SERVER.hunterManaCost.get(), Functions.secondsToTicks(3), ConfigHandler.SERVER.hunterCooldown.get(), new Integer[]{0, 25}));
		
		FOREST_MAGIC = register(DragonType.FOREST, new MagicAbility(DragonType.FOREST, "forest_magic", "forest/forest_magic", 0, 10));
		FOREST_ATHLETICS = register(DragonType.FOREST, new AthleticsAbility(DragonType.FOREST, "forest_athletics", "forest/forest_athletics", 0, 5));
		LIGHT_IN_DARKNESS = register(DragonType.FOREST, new LightInDarknessAbility(DragonType.FOREST, "light_in_darkness", "forest/light_in_darkness", 0, 6));
		CLIFFHANGER = register(DragonType.FOREST, new CliffhangerAbility(DragonType.FOREST, "cliffhanger", "forest/cliffhanger", 0, 5));
		
		FOREST_CLAWS_AND_TEETH = register(DragonType.FOREST, new DragonClawsAbility(DragonType.FOREST, "forest_claws_and_teeth", "forest/forest_claws_and_teeth", 1, 1));
		FOREST_WINGS = register(DragonType.FOREST, new DragonWingAbility(DragonType.FOREST, "forest_wings", "forest/forest_wings", 1, 1));
		FOREST_DRAGON = register(DragonType.FOREST, new InnateDragonAbility(DragonType.FOREST, "forest_dragon", "forest/forest_dragon", 1, 1));
		FEAR_OF_DARK = register(DragonType.FOREST, new FearOfDarkAbility(DragonType.FOREST, "fear_of_dark", "forest/fear_of_dark", 1, 1));
		
		//Sea dragon
		STORM_BREATH = register(DragonType.SEA, new StormBreathAbility(DragonType.SEA, "storm_breath", "sea/storm_breath", 1, 4, 2, 10, ConfigHandler.SERVER.stormBreathCooldown.get(), new Integer[]{0, 10, 30, 50}));
		BALL_LIGHTNING = register(DragonType.SEA, new BallLightningAbility(DragonType.SEA, 4, "ball_lightning", "sea/ball_lightning", 0, 4, ConfigHandler.SERVER.ballLightningManaCost.get(), Functions.secondsToTicks(2), ConfigHandler.SERVER.ballLightningCooldown.get(), new Integer[]{0, 20, 45, 50}));
		REVEALING_THE_SOUL = register(DragonType.SEA, new AoeBuffAbility(DragonType.SEA, new EffectInstance(DragonEffects.REVEALING_THE_SOUL, ConfigHandler.SERVER.revealingTheSoulDuration.get()), 5, DSParticles.magicBeaconParticle, "revealing_the_soul", "sea/revealing_the_soul", 0, 3, ConfigHandler.SERVER.revealingTheSoulManaCost.get(), Functions.secondsToTicks(5), ConfigHandler.SERVER.revealingTheSoulCooldown.get(), new Integer[]{0, 25, 40}));
		SEA_EYES = register(DragonType.SEA, new EyesBuffAbility(DragonType.SEA, DragonEffects.WATER_VISION, "sea_eyes", "sea/sea_eyes", 0, 2, ConfigHandler.SERVER.seaEyesManaCost.get(), Functions.secondsToTicks(2), ConfigHandler.SERVER.seaEyesCooldown.get(), new Integer[]{0, 15}));
		
		SEA_MAGIC = register(DragonType.SEA, new MagicAbility(DragonType.SEA, "sea_magic", "sea/sea_magic", 0, 10));
		SEA_ATHLETICS = register(DragonType.SEA, new AthleticsAbility(DragonType.SEA, "sea_athletics", "sea/sea_athletics", 0, 5));
		WATER = register(DragonType.SEA, new WaterAbility(DragonType.SEA, "water", "sea/water", 0, 6));
		SPECTRAL_IMPACT = register(DragonType.SEA, new SpectralImpactAbility(DragonType.SEA, "spectral_impact", "sea/spectral_impact", 0, 3));
		
		SEA_CLAWS_AND_TEETH = register(DragonType.SEA, new DragonClawsAbility(DragonType.SEA, "sea_claws_and_teeth", "sea/sea_claws_and_teeth", 1, 1));
		SEA_WINGS = register(DragonType.SEA, new DragonWingAbility(DragonType.SEA, "sea_wings", "sea/sea_wings", 1, 1));
		SEA_DRAGON = register(DragonType.SEA, new InnateDragonAbility(DragonType.SEA, "sea_dragon", "sea/sea_dragon", 1, 1));
		AMPHIBIAN = register(DragonType.SEA, new AmphibianAbility(DragonType.SEA, "amphibian", "sea/amphibian", 1, 1));
		
		//Cave dragon
		NETHER_BREATH = register(DragonType.CAVE, new NetherBreathAbility(DragonType.CAVE, "nether_breath", "cave/nether_breath", 1, 4, 2, 10, ConfigHandler.SERVER.fireBreathCooldown.get(), new Integer[]{0, 10, 30, 50}));
		FIREBALL = register(DragonType.CAVE, new FireBallAbility(DragonType.CAVE, "fireball", "cave/fireball", 0, 4,ConfigHandler.SERVER.fireballManaCost.get(), Functions.secondsToTicks(4), ConfigHandler.SERVER.fireballCooldown.get(),  new Integer[]{0, 20, 40, 45}));
		TOUGH_SKIN = register(DragonType.CAVE, new ToughSkinAbility(DragonType.CAVE, new EffectInstance(DragonEffects.STRONG_LEATHER, ConfigHandler.SERVER.toughSkinDuration.get()), 5, DSParticles.peaceBeaconParticle, "strong_leather", "cave/strong_leather", 0, 3, ConfigHandler.SERVER.toughSkinManaCost.get(), Functions.secondsToTicks(5), ConfigHandler.SERVER.toughSkinCooldown.get(), new Integer[]{0, 15, 35}));
		LAVA_VISION = register(DragonType.CAVE, new EyesBuffAbility(DragonType.CAVE, DragonEffects.LAVA_VISION, "lava_vision", "cave/lava_vision", 0, 2, ConfigHandler.SERVER.lavaVisionManaCost.get(), Functions.secondsToTicks(2), ConfigHandler.SERVER.lavaVisionCooldown.get(), new Integer[]{0, 25}));
		
		CAVE_MAGIC = register(DragonType.CAVE, new MagicAbility(DragonType.CAVE, "cave_magic", "cave/cave_magic", 0, 10));
		CAVE_ATHLETICS = register(DragonType.CAVE, new AthleticsAbility(DragonType.CAVE, "cave_athletics", "cave/cave_athletics", 0, 5));
		CONTRAST_SHOWER = register(DragonType.CAVE, new ContrastShowerAbility(DragonType.CAVE, "contrast_shower", "cave/contrast_shower", 0, 5));
		BURN = register(DragonType.CAVE, new BurnAbility(DragonType.CAVE, "burn", "cave/burn", 0, 3));
		
		CAVE_CLAWS_AND_TEETH = register(DragonType.CAVE, new DragonClawsAbility(DragonType.CAVE, "cave_claws_and_teeth", "cave/cave_claws_and_teeth", 1, 1));
		CAVE_WINGS = register(DragonType.CAVE, new DragonWingAbility(DragonType.CAVE, "cave_wings", "cave/cave_wings", 1, 1));
		CAVE_DRAGON = register(DragonType.CAVE, new InnateDragonAbility(DragonType.CAVE, "cave_dragon", "cave/cave_dragon", 1, 1));
		HOT_BLOOD = register(DragonType.CAVE, new HotBloodAbility(DragonType.CAVE, "hot_blood", "cave/hot_blood", 1, 1));
	}
	
	
	public static HashMap<DragonType, ArrayList<DragonAbility>> ABILITIES = new HashMap<>();
	public static HashMap<DragonType, ArrayList<ActiveDragonAbility>> ACTIVE_ABILITIES = new HashMap<>();
	public static HashMap<DragonType, ArrayList<PassiveDragonAbility>> PASSIVE_ABILITIES = new HashMap<>();
	public static HashMap<DragonType, ArrayList<InnateDragonAbility>> INNATE_ABILITIES = new HashMap<>();
	
	public static HashMap<String, DragonAbility> ABILITY_LOOKUP = new HashMap<>();
	
	public static ActiveDragonAbility register(DragonType type, ActiveDragonAbility activeDragonAbility){
		if(!ABILITIES.containsKey(type)){
			ABILITIES.put(type, new ArrayList<>());
		}
		
		if(!ACTIVE_ABILITIES.containsKey(type)){
			ACTIVE_ABILITIES.put(type, new ArrayList<>());
		}
		
		ABILITIES.get(type).add(activeDragonAbility);
		ACTIVE_ABILITIES.get(type).add(activeDragonAbility);
		
		ABILITY_LOOKUP.put(activeDragonAbility.getId(), activeDragonAbility);
		
		return activeDragonAbility;
	}
	
	public static PassiveDragonAbility register(DragonType type, PassiveDragonAbility passiveDragonAbility){
		if(!ABILITIES.containsKey(type)){
			ABILITIES.put(type, new ArrayList<>());
		}
		
		if(!PASSIVE_ABILITIES.containsKey(type)){
			PASSIVE_ABILITIES.put(type, new ArrayList<>());
		}
		
		ABILITIES.get(type).add(passiveDragonAbility);
		PASSIVE_ABILITIES.get(type).add(passiveDragonAbility);
		ABILITY_LOOKUP.put(passiveDragonAbility.getId(), passiveDragonAbility);
		
		return passiveDragonAbility;
	}
	
	public static InnateDragonAbility register(DragonType type, InnateDragonAbility informationDragonAbility){
		if(!ABILITIES.containsKey(type)){
			ABILITIES.put(type, new ArrayList<>());
		}
		
		if(!INNATE_ABILITIES.containsKey(type)){
			INNATE_ABILITIES.put(type, new ArrayList<>());
		}
		
		ABILITIES.get(type).add(informationDragonAbility);
		INNATE_ABILITIES.get(type).add(informationDragonAbility);
		ABILITY_LOOKUP.put(informationDragonAbility.getId(), informationDragonAbility);
		
		return informationDragonAbility;
	}
	
	public static int getAbilitySlot(ActiveDragonAbility ability)
	{
	    int abilityId = -1;
	    top:
	    for(DragonType type : DragonType.values()){
	        int index = 0;
	        
	        for(ActiveDragonAbility ab : ACTIVE_ABILITIES.get(type)){
	            if(ab.getId() == ability.getId()){
	                abilityId = index;
	                break top;
	            }
	            index++;
	        }
	    }
	    return abilityId;
	}
}
