package by.jackraidenph.dragonsurvival.client.gui.settings;

import by.jackraidenph.dragonsurvival.client.gui.widgets.lists.OptionsList;
import by.jackraidenph.dragonsurvival.client.gui.widgets.lists.OptionsList.CategoryEntry;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.network.NetworkHandler;
import by.jackraidenph.dragonsurvival.network.config.SyncBooleanConfig;
import by.jackraidenph.dragonsurvival.network.config.SyncEnumConfig;
import by.jackraidenph.dragonsurvival.network.config.SyncNumberConfig;
import com.electronwill.nightconfig.core.AbstractConfig;
import com.electronwill.nightconfig.core.EnumGetMethod;
import com.electronwill.nightconfig.core.UnmodifiableConfig.Entry;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.IBidiTooltip;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.client.settings.IteratableOption;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.*;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientSettingsScreen extends SettingsScreen
{
	private ArrayList<AbstractOption> OPTIONS = new ArrayList<>();
	private TreeMap<String, ArrayList<AbstractOption>> optionMap = new TreeMap<>();
	private ArrayList<String> options = new ArrayList<>();
	public OptionsList list;
	
	protected void init() {
		OPTIONS.clear();
		optionMap.clear();
		options.clear();
		
		OptionsList.config.clear();
		OptionsList.configMap.clear();
		
		this.list = new OptionsList(this.minecraft, this.width, this.height, 32, this.height - 32, 25);
		
		for (Entry entry : getSpec().entrySet()) {
			Object value = getSpec().get(entry.getKey());
			
			if(value instanceof AbstractConfig){
				AbstractConfig config = (AbstractConfig)value;
				
				CopyOnWriteArrayList<Pair<String, Set<Map.Entry<String, Object>>>> list = new CopyOnWriteArrayList<>();
				list.add(Pair.of("", config.valueMap().entrySet()));
				
				while(list.size() > 0){
					Pair<String, Set<Map.Entry<String, Object>>> pair = list.get(0);
					
					for(Map.Entry<String, Object> ent : pair.getSecond()){
						if (ent.getValue() instanceof AbstractConfig) {
							AbstractConfig config1 = (AbstractConfig)ent.getValue();
							list.add(Pair.of((!pair.getFirst().isEmpty() ? pair.getFirst() + "." : "") + ent.getKey(), config1.valueMap().entrySet()));
						}else{
							addValue((!pair.getFirst().isEmpty() ? pair.getFirst() + "." : "") + ent.getKey(), pair.getFirst(), ent.getKey(), ent.getValue());
						}
					}
					
					list.remove(0);
				}

			}else{
				addValue("", null, entry.getKey(), value);
			}
		}
		
		this.list.addSmall(OPTIONS.toArray(new AbstractOption[0]), null);
		
		for(Map.Entry<String, ArrayList<AbstractOption>> ent : optionMap.entrySet()){
			CategoryEntry entry = null;
			if(!ent.getKey().isEmpty()) {
				String key = ent.getKey();
				String lastKey = key;
				for (String s : key.split("\\.")) {
					if(this.list.findCategory(s, lastKey) == null
					   || (this.list.findCategory(s, lastKey).parent != null && !this.list.findCategory(s, lastKey).parent.origName.equals(lastKey))) {
						entry = this.list.addCategory(s, entry);

					}else{
						entry = this.list.findCategory(s, lastKey);
					}
					lastKey = s;
				}
			}
			this.list.addSmall(ent.getValue().toArray(new AbstractOption[0]), entry);
		}
		
		this.children.add(this.list);
		this.addButton(new Button(this.width / 2 - 100, this.height - 27, 200, 20, DialogTexts.GUI_BACK, (p_213106_1_) -> {
			this.minecraft.setScreen(this.lastScreen);
		}));
	}
	
	public ForgeConfigSpec getSpec(){
		return ConfigHandler.clientSpec;
	}
	
	public String getConfigName(){
		return "client";
	}
	
	private static final Float sliderPerc = 0.1F;
	
	public void addValue(String key, String category, String path, Object value){
		if(options.contains(path)) return;
		
		int origLength = path.length();
		String origPath = path;
		path = path.substring(0, Math.min(path.length(), 15));
		
		if(path.length() < origLength){
			path += "...";
		}
		
		String finalPath = path;
		
		if(value instanceof ValueSpec) {
			ValueSpec spec = (ValueSpec)value;
			Object ob = getSpec().getValues().get(getConfigName() + "." + key);
			
			IFormattableTextComponent tooltip = spec.getTranslationKey() == null ? new StringTextComponent("§l" + origPath + "§r\n\n" + (spec.getComment() != null ? spec.getComment() : ""))
					: new StringTextComponent("§l" + origPath + "§r\n\n").append(new TranslationTextComponent(spec.getTranslationKey()));
			if(spec.needsWorldRestart()){
				tooltip.append("\n§4This setting requires a server restart!§r");
			}
			
			List<IReorderingProcessor> tooltip1 = Minecraft.getInstance().font.split(tooltip, 200);
			if (ob instanceof BooleanValue) {
				BooleanValue booleanValue = (BooleanValue)ob;
				
				BooleanOption option = new BooleanOption(path, tooltip, (settings) -> booleanValue.get(), (settings, settingValue) -> {
					try {
						booleanValue.set(settingValue);
					}catch (Exception ignored){}
					
					if(getConfigName() != "client") {
						NetworkHandler.CHANNEL.sendToServer(new SyncBooleanConfig(key, settingValue, getConfigName() == "server" ? 0 : 1));
					}
				});
				if(option != null) {
					OptionsList.configMap.put(option, key);
					OptionsList.config.put(option, Pair.of(spec, booleanValue));
					addOption(category, path, option);
				}
			} else if (ob instanceof IntValue) {
				IntValue value1 = (IntValue)ob;
				Integer min = (Integer)spec.correct(Integer.MIN_VALUE);
				Integer max = (Integer)spec.correct(Integer.MAX_VALUE);
				
				SliderPercentageOption option = new SliderPercentageOption(path, min, max, sliderPerc, (settings) -> Double.valueOf(value1.get()), (settings, settingValue) -> {
					try {
						value1.set(settingValue.intValue());
					}catch (Exception ignored){}
					
					if(getConfigName() != "client") {
						NetworkHandler.CHANNEL.sendToServer(new SyncNumberConfig(key, settingValue, getConfigName() == "server" ? 0 : 1));
					}
				}, (settings, slider) -> {
					return new TranslationTextComponent("options.generic_value", new StringTextComponent(finalPath), (int)slider.get(settings));
				});
				
				option.setTooltip(tooltip1);
				
				if(option != null) {
					OptionsList.configMap.put(option, key);
					OptionsList.config.put(option, Pair.of(spec, value1));
					addOption(category, path, option);
				}
			} else if (ob instanceof DoubleValue) {
				DoubleValue value1 = (DoubleValue)ob;
				double min = (double)spec.correct(Double.MIN_VALUE);
				double max = (double)spec.correct(Double.MAX_VALUE);
				
				SliderPercentageOption option = new SliderPercentageOption(path, min, max, sliderPerc, (settings) -> value1.get(), (settings, settingValue) -> {
					try {
						value1.set(Math.round(settingValue * 100.0) / 100.0);
					}catch (Exception ignored){}
					
					if(getConfigName() != "client") {
						NetworkHandler.CHANNEL.sendToServer(new SyncNumberConfig(key, settingValue, getConfigName() == "server" ? 0 : 1));
					}
				}, (settings, slider) -> {
					return new TranslationTextComponent("options.generic_value", new StringTextComponent(finalPath), Math.round(slider.get(settings) * 100.0) / 100.0);
				});
				
				option.setTooltip(tooltip1);
				
				if(option != null) {
					OptionsList.configMap.put(option, key);
					OptionsList.config.put(option, Pair.of(spec, value1));
					addOption(category, path, option);
				}
			} else if (ob instanceof LongValue) {
				LongValue value1 = (LongValue)ob;
				Long min = (Long)spec.correct(Long.MIN_VALUE);
				Long max = (Long)spec.correct(Long.MAX_VALUE);
				
				SliderPercentageOption option = new SliderPercentageOption(path, min, max, sliderPerc, (settings) -> Double.valueOf(value1.get()), (settings, settingValue) -> {
					try {
						value1.set(settingValue.longValue());
					}catch (Exception ignored){}
					
					if(getConfigName() != "client") {
						NetworkHandler.CHANNEL.sendToServer(new SyncNumberConfig(key, settingValue, getConfigName() == "server" ? 0 : 1));
					}
				}, (settings, slider) -> {
					return new TranslationTextComponent("options.generic_value", new StringTextComponent(finalPath), (long)slider.get(settings));
				});
				option.setTooltip(tooltip1);
				
				if(option != null) {
					OptionsList.configMap.put(option, key);
					OptionsList.config.put(option, Pair.of(spec, value1));
					addOption(category, path, option);
				}
			} else if (ob instanceof EnumValue) {
				EnumValue value1 = (EnumValue)ob;
				IteratableOption option = new IteratableOption(path, (settings, val) -> {
					Class<? extends Enum> cs = (Class<? extends Enum>)value1.get().getClass();
					int max = cs.getEnumConstants().length;
					int curVal = ((Enum)value1.get()).ordinal();
					
					if(curVal == max - 1){
						curVal = 0;
					}else{
						curVal += 1;
					}
					Enum en = EnumGetMethod.ORDINAL_OR_NAME.get(curVal, cs);
					
					try {
						value1.set(en);
					}catch (Exception ignored){}
					
					if(getConfigName() != "client") {
						NetworkHandler.CHANNEL.sendToServer(new SyncEnumConfig(key, en, getConfigName() == "server" ? 0 : 1));
					}
				}, (settings, set) -> {
					return new TranslationTextComponent("options.generic_value", new StringTextComponent(finalPath), ((Enum)value1.get()).name());
				});
				
				option.setTooltip(tooltip1);
				
				if(option != null) {
					OptionsList.configMap.put(option, key);
					OptionsList.config.put(option, Pair.of(spec, value1));
					addOption(category, path, option);
				}
			}else if (ob instanceof ConfigValue) {
				ConfigValue value1 = (ConfigValue)ob;
				if(value1.get() instanceof List && (((List)value1.get()).isEmpty() || ((List)value1.get()).get(0) instanceof String)) {
					String finalPath1 = path;
					IteratableOption option = new IteratableOption(path, (settings, val) -> {
						this.minecraft.setScreen(new ListConfigSettingsScreen(this, minecraft.options, new StringTextComponent(finalPath1), value1, getSpec(), getConfigName() + "." + key));
					}, (settings, set) -> {
						return new StringTextComponent(finalPath);
					});
					
					option.setTooltip(tooltip1);
					
					if(option != null) {
						OptionsList.configMap.put(option, key);
						OptionsList.config.put(option, Pair.of(spec, value1));
						addOption(category, path, option);
					}
				}
			}
		}
	}
	
	private void addOption(String category, String path, AbstractOption option)
	{
		if(category != null){
			if(!optionMap.containsKey(category)){
				optionMap.put(category, new ArrayList<>());
			}
			optionMap.get(category).add(option);
		}else{
			OPTIONS.add(option);
		}
		
		options.add(path);
	}
	
	public ClientSettingsScreen(Screen p_i225930_1_, GameSettings p_i225930_2_, ITextComponent p_i225930_3_)
	{
		super(p_i225930_1_, p_i225930_2_, p_i225930_3_);
	}
	
	public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.renderBackground(p_230430_1_);
		this.list.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
		drawCenteredString(p_230430_1_, this.font, this.title, this.width / 2, 5, 16777215);
		super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
		List<IReorderingProcessor> list = tooltipAt(this.list, p_230430_2_, p_230430_3_);
		if (list != null) {
			this.renderTooltip(p_230430_1_, list, p_230430_2_, p_230430_3_);
		}
	}
	
	@Nullable
	public static List<IReorderingProcessor> tooltipAt(OptionsList p_243293_0_, int p_243293_1_, int p_243293_2_) {
		Optional<Widget> optional = p_243293_0_.getMouseOver((double)p_243293_1_, (double)p_243293_2_);
		if (optional.isPresent() && optional.get() instanceof IBidiTooltip) {
			Optional<List<IReorderingProcessor>> optional1 = ((IBidiTooltip)optional.get()).getTooltip();
			return optional1.orElse(null);
		} else {
			return null;
		}
	}
}
