package by.jackraidenph.dragonsurvival.client.gui.widgets.lists;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.client.gui.widgets.buttons.ResetSettingsButton;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.OptionSlider;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.OptionButton;
import net.minecraft.client.gui.widget.list.AbstractOptionList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.ValueSpec;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OptionsList extends AbstractOptionList<OptionsList.Entry>
{
	public static ConcurrentHashMap<AbstractOption, Pair<ValueSpec, ConfigValue>> config = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<AbstractOption, String> configMap = new ConcurrentHashMap<>();
	
	public OptionsList(Minecraft p_i51130_1_, int p_i51130_2_, int p_i51130_3_, int p_i51130_4_, int p_i51130_5_, int p_i51130_6_) {
		super(p_i51130_1_, p_i51130_2_, p_i51130_3_, p_i51130_4_, p_i51130_5_, p_i51130_6_);
		this.centerListVertically = false;
		this.setRenderBackground(false);
	}
	
	public CategoryEntry addCategory(String p_214333_1_, CategoryEntry ent) {
		String name = p_214333_1_.substring(0, 1).toUpperCase(Locale.ROOT) + p_214333_1_.substring(1).replace("_", " ");
		CategoryEntry entry = new CategoryEntry(new StringTextComponent(name), ent);
		entry.origName = p_214333_1_;
		this.addEntry(entry);
		return entry;
	}
	
	@Override
	public int addEntry(Entry p_230513_1_)
	{
		return super.addEntry(p_230513_1_);
	}
	
	public int addBig(AbstractOption p_214333_1_, CategoryEntry entry) {
		return this.addEntry(OptionsList.OptionEntry.big(this.minecraft.options, this.width, p_214333_1_, entry));
	}
	
	public void addSmall(AbstractOption p_214334_1_, @Nullable AbstractOption p_214334_2_, CategoryEntry entry) {
		this.addEntry(OptionsList.OptionEntry.small(this.minecraft.options, this.width, p_214334_1_, p_214334_2_, entry));
	}
	
	public void textBox(AbstractOption p_214334_1_) {
		this.addEntry(OptionsList.OptionEntry.textBox(this, this.minecraft.options, this.width, p_214334_1_));
	}
	
	public void addSmall(AbstractOption[] p_214335_1_, CategoryEntry entry) {
		for(int i = 0; i < p_214335_1_.length; i += 2) {
			this.addSmall(p_214335_1_[i], i < p_214335_1_.length - 1 ? p_214335_1_[i + 1] : null, entry);
		}
	}
	
	
	
	protected int getRowTop(int p_230962_1_) {
		int height = 0;
		for(int i = 0; i < p_230962_1_; i++){
			Entry e = this.getEntry(i);
			height += e.getHeight();
		}
		return this.y0 + 4 - (int)this.getScrollAmount() + height + this.headerHeight;
	}
	
	private int getRowBottom(int p_230948_1_) {
		Entry e = this.getEntry(p_230948_1_);
		return this.getRowTop(p_230948_1_) + e.getHeight();
	}
	private boolean renderSelection = true;
	
	protected void renderList(MatrixStack p_238478_1_, int p_238478_2_, int p_238478_3_, int p_238478_4_, int p_238478_5_, float p_238478_6_) {
		int i = this.getItemCount();
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();
		int curPos = 0;
		for(int j = 0; j < i; ++j) {
			int k = this.getRowTop(j);
			int l = this.getRowBottom(j);
			if (l >= this.y0 && k <= this.y1) {
				Entry e = this.getEntry(j);
				int j1 = e.getHeight() - 4;
				curPos += e.getHeight();
				int i1 = p_238478_3_ + curPos + this.headerHeight;
				int k1 = this.getRowWidth();
				if (this.renderSelection && this.isSelectedItem(j)) {
					int l1 = this.x0 + this.width / 2 - k1 / 2;
					int i2 = this.x0 + this.width / 2 + k1 / 2;
					RenderSystem.disableTexture();
					float f = this.isFocused() ? 1.0F : 0.5F;
					RenderSystem.color4f(f, f, f, 1.0F);
					bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
					bufferbuilder.vertex((double)l1, (double)(i1 + j1 + 2), 0.0D).endVertex();
					bufferbuilder.vertex((double)i2, (double)(i1 + j1 + 2), 0.0D).endVertex();
					bufferbuilder.vertex((double)i2, (double)(i1 - 2), 0.0D).endVertex();
					bufferbuilder.vertex((double)l1, (double)(i1 - 2), 0.0D).endVertex();
					tessellator.end();
					RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
					bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
					bufferbuilder.vertex((double)(l1 + 1), (double)(i1 + j1 + 1), 0.0D).endVertex();
					bufferbuilder.vertex((double)(i2 - 1), (double)(i1 + j1 + 1), 0.0D).endVertex();
					bufferbuilder.vertex((double)(i2 - 1), (double)(i1 - 1), 0.0D).endVertex();
					bufferbuilder.vertex((double)(l1 + 1), (double)(i1 - 1), 0.0D).endVertex();
					tessellator.end();
					RenderSystem.enableTexture();
				}
				
				int j2 = this.getRowLeft();
				e.render(p_238478_1_, j, k, j2, k1, j1, p_238478_4_, p_238478_5_, this.isMouseOver((double)p_238478_4_, (double)p_238478_5_) && Objects.equals(this.getEntryAtPos((double)p_238478_4_, (double)p_238478_5_), e), p_238478_6_);
			}
		}
	}
	
	public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
		this.updateScrollingState(p_231044_1_, p_231044_3_, p_231044_5_);
		if (!this.isMouseOver(p_231044_1_, p_231044_3_)) {
			return false;
		} else {
			Entry e = this.getEntryAtPos(p_231044_1_, p_231044_3_);
			if (e != null) {
				if (e.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_)) {
					this.setFocused(e);
					this.setDragging(true);
					return true;
				}
			} else if (p_231044_5_ == 0) {
				this.clickedHeader((int)(p_231044_1_ - (double)(this.x0 + this.width / 2 - this.getRowWidth() / 2)), (int)(p_231044_3_ - (double)this.y0) + (int)this.getScrollAmount() - 4);
				return true;
			}
			
			return this.scrolling;
		}
	}
	
	@Override
	protected void ensureVisible(Entry p_230954_1_)
	{
		int i = this.getRowTop(this.children().indexOf(p_230954_1_));
		int j = i - this.y0 - 4 - p_230954_1_.getHeight();
		if (j < 0) {
			this.scroll(j);
		}
		
		int k = this.y1 - i - p_230954_1_.getHeight() - p_230954_1_.getHeight();
		if (k < 0) {
			this.scroll(-k);
		}
	}
	
	public void scroll(int p_230937_1_) {
		this.setScrollAmount(this.getScrollAmount() + (double)p_230937_1_);
	}
	
	protected int getMaxPosition() {
		int size = this.headerHeight;
		for(Entry ent : children()){
			size += ent.getHeight();
		}
		return size;
	}
	
	public int getRowWidth() {
		return 400;
	}
	
	protected int getScrollbarPosition() {
		return super.getScrollbarPosition() + 32;
	}
	
	@Override
	public boolean removeEntry(Entry p_230956_1_)
	{
		return super.removeEntry(p_230956_1_);
	}
	
	@Nullable
	public CategoryEntry findCategory(String text, String lastKey) {
		for(OptionsList.Entry optionsrowlist$row : this.children()) {
			if(optionsrowlist$row instanceof CategoryEntry) {
				CategoryEntry cat = (CategoryEntry)optionsrowlist$row;
				
				if(cat.parent == null || cat.parent.origName.equals(lastKey)) {
					if (cat.origName.equals(text)) {
						return cat;
					}
				}
			}
		}
		
		return null;
	}
	
	@Nullable
	public Widget findWidget(String text) {
		for(OptionsList.Entry optionsrowlist$row : this.children()) {
			for(IGuiEventListener widget : optionsrowlist$row.children()) {
				if(widget instanceof Widget) {
					if (widget instanceof OptionButton && ((OptionButton)widget).getMessage().getString().equals(text)) {
						return (Widget)widget;
					}else if (widget instanceof OptionSlider && ((OptionSlider)widget).getMessage().getString().equals(text)) {
						return (Widget)widget;
					}
				}
			}
		}
		
		return null;
	}
	
	@Nullable
	public Entry findEntry(String text) {
		for(OptionsList.Entry optionsrowlist$row : this.children()) {
			for(IGuiEventListener widget : optionsrowlist$row.children()) {
				if(widget instanceof Widget) {
					if (widget instanceof OptionButton && ((OptionButton)widget).getMessage().getString().equals(text)) {
						return optionsrowlist$row;
					}else if (widget instanceof OptionSlider && ((OptionSlider)widget).getMessage().getString().equals(text)) {
						return optionsrowlist$row;
					}
				}
			}
		}
		
		return null;
	}
	
	@Nullable
	public Widget findOption(AbstractOption p_243271_1_) {
		for(OptionsList.Entry optionsrowlist$row : this.children()) {
			for(IGuiEventListener widget : optionsrowlist$row.children()) {
				if(widget instanceof Widget) {
					if (widget instanceof OptionButton && ((OptionButton)widget).getOption() == p_243271_1_) {
						return (Widget)widget;
					}if (widget instanceof OptionSlider && ((OptionSlider)widget).option == p_243271_1_) {
						return (Widget)widget;
					}
				}
			}
		}
		
		return null;
	}
	
	protected void centerScrollOn(Entry p_230951_1_) {
		int num = this.children().indexOf(p_230951_1_);
		int size = 0;
		for(int i = 0; i < num; i++){
			size += getEntry(i).getHeight();
		}
		
		this.setScrollAmount((double)(size + p_230951_1_.getHeight() / 2 - (this.y1 - this.y0) / 2));
	}
	
	public Optional<Widget> getMouseOver(double p_238518_1_, double p_238518_3_) {
		for(OptionsList.Entry optionsrowlist$row : this.children()) {
			for(IGuiEventListener widget : optionsrowlist$row.children()) {
				if(widget instanceof Widget) {
					if (((Widget)widget).isMouseOver(p_238518_1_, p_238518_3_)) {
						return Optional.of((Widget)widget);
					}
				}
			}
		}
		
		return Optional.empty();
	}
	
	public Entry getEntryAtPos(double p_230933_1_, double p_230933_3_) {
		int i = this.getRowWidth() / 2;
		int j = this.x0 + this.width / 2;
		int k = j - i;
		int l = j + i;
		int i1 = MathHelper.floor(p_230933_3_ - (double)this.y0) - this.headerHeight + (int)this.getScrollAmount() - 4;
		int curSize = 0;
		int j1 = 0;
		for(int g = 0; g < children().size(); g++){
			curSize += getEntry(g).getHeight();
			if(curSize >= i1){
				j1 = g;
				break;
			}
		}
		
		return (Entry)(p_230933_1_ < (double)this.getScrollbarPosition() && p_230933_1_ >= (double)k && p_230933_1_ <= (double)l && j1 >= 0 && i1 >= 0 && j1 < this.getItemCount() ? this.children().get(j1) : null);
	}
	
	@OnlyIn( Dist.CLIENT)
	public abstract static class Entry extends AbstractOptionList.Entry<OptionsList.Entry> {
		public abstract int getHeight();
		
		public boolean isMouseOver(double p_231047_1_, double p_231047_3_) {
			return Objects.equals(((OptionsList)this.list).getEntryAtPos(p_231047_1_, p_231047_3_), this);
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class OptionEntry extends OptionsList.Entry {
		private final List<Widget> children;
		private CategoryEntry category;
		
		private OptionEntry(List<Widget> p_i50481_1_, CategoryEntry categoryEntry) {
			this.children = p_i50481_1_;
			this.category = categoryEntry;
		}
		
		public static OptionsList.OptionEntry big(GameSettings p_214384_0_, int p_214384_1_, AbstractOption p_214384_2_, CategoryEntry category) {
			return new OptionsList.OptionEntry(ImmutableList.of(p_214384_2_.createButton(p_214384_0_, p_214384_1_ / 2 - 155, 0, 310)), category);
		}
		
		public static OptionsList.OptionEntry textBox(OptionsList list, GameSettings p_214384_0_, int p_214384_1_, AbstractOption p_214384_2_) {
			Widget widget1 = p_214384_2_.createButton(p_214384_0_, p_214384_1_ / 2 - 155, 0, 250);
			Widget widget2 = new Button(p_214384_1_ / 2 - 155 + 260, 0, 50, 18, new StringTextComponent("Remove"), (btn) -> {
				for (Entry child : list.children()) {
					if(child.children().contains(widget1)){
						list.removeEntry(child);
						list.scroll(-child.getHeight());
						return;
					}
				}
			});
			
			return new OptionsList.OptionEntry(ImmutableList.of(widget1, widget2), null);
		}
		
		public static OptionsList.OptionEntry small(GameSettings p_214382_0_, int p_214382_1_, AbstractOption p_214382_2_, @Nullable
				AbstractOption p_214382_3_, CategoryEntry category) {
			Widget widget = p_214382_2_.createButton(p_214382_0_, p_214382_1_ / 2 - 155 + (category != null ? category.indent : 0), 0, 140 - (category != null ? category.indent / 2 : 0));
			Widget widget2 = p_214382_3_ != null ? p_214382_3_.createButton(p_214382_0_, p_214382_1_ / 2 - 153 + (160 - (category != null ? category.indent / 2 : 0)) + (category != null ? category.indent : 0), 0, 125 - (category != null ? category.indent / 2 : 0)) : null;
			
			Widget resetWidget = new ResetSettingsButton(widget.x + widget.getWidth() + (category != null && category.parent != null ? 0 : 1), 0, p_214382_2_);
			Widget resetWidget2 = p_214382_3_ != null ? new ResetSettingsButton(widget2.x + widget2.getWidth() + (category != null && category.parent != null ? 1 : 0), 0, p_214382_3_) : null;
			
			return p_214382_3_ == null ? new OptionsList.OptionEntry(ImmutableList.of(widget, resetWidget), category)
					: new OptionsList.OptionEntry(ImmutableList.of(widget, resetWidget, widget2, resetWidget2), category);
		}
		
		public void render(MatrixStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
			this.children.forEach((p_238519_5_) -> {
				p_238519_5_.visible = getHeight() != 0;
				p_238519_5_.y = p_230432_3_;
				p_238519_5_.render(p_230432_1_, p_230432_7_, p_230432_8_, p_230432_10_);
			});
		}
		
		public List<? extends IGuiEventListener> children() {
			return this.children;
		}
		
		@Override
		public int getHeight()
		{
			if(category != null) {
				CategoryEntry entry = category.parent;
				while (entry != null) {
					if (!entry.enabled) {
						return 0;
					} else {
						entry = entry.parent;
					}
				}
			}
			
			return category == null || category.enabled ? 22 : 0;
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public class CategoryEntry extends OptionsList.Entry {
		private final ResourceLocation BUTTON_UP = new ResourceLocation(DragonSurvivalMod.MODID, "textures/gui/emote/button_up.png");
		private final ResourceLocation BUTTON_DOWN = new ResourceLocation(DragonSurvivalMod.MODID, "textures/gui/emote/button_down.png");
		
		public final ITextComponent name;
		public String origName;
		private int width;
		public boolean enabled = false;
		public int indent = 0;
		public CategoryEntry parent;
		public CategoryEntry(ITextComponent p_i232280_2_, CategoryEntry entry) {
			this.name = p_i232280_2_;
			this.width = OptionsList.this.minecraft.font.width(this.name);
			this.parent = entry;
			if(entry != null){
				this.indent = entry.indent + 10;
			}
		}
		
		@Override
		public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_)
		{
			Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
			this.enabled = !this.enabled;
			return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
		}
		
		public boolean isMouseOver(double p_231047_1_, double p_231047_3_) {
			return Objects.equals(((OptionsList)this.list).getEntryAtPos(p_231047_1_, p_231047_3_), this);
		}
		
		public void render(MatrixStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
			if(parent != null && !parent.enabled) return;
		
			int color = new Color(0.05F, 0.05F, 0.05F, 0.85F).getRGB();
			AbstractGui.fill(p_230432_1_, (OptionsList.this.minecraft.screen.width / 2) - 155 + indent, (p_230432_3_ + p_230432_6_ - 16), (OptionsList.this.minecraft.screen.width / 2) + 155, (p_230432_3_ + p_230432_6_), color);
			
			OptionsList.this.minecraft.font.draw(p_230432_1_, this.name, (float)(OptionsList.this.minecraft.screen.width / 2 - this.width / 2), (float)(p_230432_3_ + p_230432_6_ - 12), 16777215);
			
			if(!enabled) {
				Minecraft.getInstance().getTextureManager().bind(BUTTON_UP);
				blit(p_230432_1_, (OptionsList.this.minecraft.screen.width / 2) + 128, (p_230432_3_ + p_230432_6_ - 16), 0, 0, 16, 16, 16, 16);
			}else{
				Minecraft.getInstance().getTextureManager().bind(BUTTON_DOWN);
				blit(p_230432_1_, (OptionsList.this.minecraft.screen.width / 2) + 128, (p_230432_3_ + p_230432_6_ - 16), 0, 0, 16, 16, 16, 16);
			}
		}
		
		public boolean changeFocus(boolean p_231049_1_) {
			return false;
		}
		
		public List<? extends IGuiEventListener> children() {
			return Collections.emptyList();
		}
		
		@Override
		public int getHeight()
		{
			return parent == null || parent.enabled ? 20 : 0;
		}
	}
}
