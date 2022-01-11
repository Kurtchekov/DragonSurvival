package by.jackraidenph.dragonsurvival.api.appleskin;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.common.handlers.DragonFoodHandler;
import by.jackraidenph.dragonsurvival.misc.DragonType;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent.PostText;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import squeek.appleskin.api.event.HUDOverlayEvent.HungerRestored;
import squeek.appleskin.api.event.HUDOverlayEvent.Saturation;
import squeek.appleskin.api.event.TooltipOverlayEvent;
import squeek.appleskin.api.food.FoodValues;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/*
	Some code is reused from the official AppleSkin repo at https://github.com/squeek502/AppleSkin
 */

@OnlyIn( Dist.CLIENT)
public class AppleSkinSupport
{
	public static ResourceLocation appleSkinTexture = new ResourceLocation(DragonSurvivalMod.MODID, "textures/gui/appleskin.png");
	public static ResourceLocation foodTextures = new ResourceLocation(DragonSurvivalMod.MODID + ":textures/gui/dragon_hud.png");
	
	private static float unclampedFlashAlpha = 0.0F;
	private static float flashAlpha = 0.0F;
	private static byte alphaDir = 1;
	public static final Vector<Point> foodBarOffsets = new Vector();
	
	private static final TextureOffsets normalBarTextureOffsets;
	private static final TextureOffsets rottenBarTextureOffsets;
	
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if (event.phase == Phase.END) {
			unclampedFlashAlpha += (float)alphaDir * 0.125F;
			if (unclampedFlashAlpha >= 1.5F) {
				alphaDir = -1;
			} else if (unclampedFlashAlpha <= -0.5F) {
				alphaDir = 1;
			}
			
			flashAlpha = Math.max(0.0F, Math.min(1.0F, unclampedFlashAlpha)) * Math.max(0.0F, Math.min(1.0F, 0.65f));
		}
	}
	
	@SubscribeEvent
	public void hudSaturation(Saturation event){
		if(DragonStateProvider.isDragon(Minecraft.getInstance().player)){
			event.setCanceled(true);
			Minecraft mc = Minecraft.getInstance();
			PlayerEntity player = mc.player;
			FoodStats stats = player.getFoodData();
			MatrixStack matrixStack = event.matrixStack;
			
			DragonStateHandler handler = DragonStateProvider.getCap(player).orElse(null);
			
			ItemStack heldItem = ItemStack.EMPTY;
			Food food = null;
			
			if(!player.getMainHandItem().isEmpty() && DragonFoodHandler.isDragonEdible(player.getMainHandItem().getItem(), handler.getType())){
				heldItem = player.getMainHandItem();
			}else if(!player.getOffhandItem().isEmpty() && DragonFoodHandler.isDragonEdible(player.getOffhandItem().getItem(), handler.getType())){
				heldItem = player.getOffhandItem();
			}
			
			if(!heldItem.isEmpty()){
				food = DragonFoodHandler.getDragonFoodProperties(heldItem.getItem(), handler.getType());
			}
			
			int top = mc.getWindow().getGuiScaledHeight() - DragonFoodHandler.rightHeight + 10;
			int left = mc.getWindow().getGuiScaledWidth() / 2 - 91;
			int right = mc.getWindow().getGuiScaledWidth() / 2 + 92;
			
			generateHungerBarOffsets(top, left, right, mc.gui.getGuiTicks(), player);
			
			FoodValues values = food != null ? new FoodValues(food.getNutrition(), food.getSaturationModifier()) : null;
			
			int foodHunger = food != null ? food.getNutrition() : 0;
			float foodSaturationIncrement = values != null ? values.getSaturationIncrement() : 0;
			
			int newFoodValue = stats.getFoodLevel() + foodHunger;
			float newSaturationValue = stats.getSaturationLevel() + foodSaturationIncrement;
			float saturationGained = newSaturationValue > (float)newFoodValue ? (float)newFoodValue - stats.getSaturationLevel() : foodSaturationIncrement;
			drawSaturationOverlay(0f, event.saturationLevel, mc, matrixStack, event.x, event.y, 1f);
			drawSaturationOverlay(saturationGained, event.saturationLevel, mc, matrixStack, event.x, event.y, flashAlpha);
		}
	}
	
	/*
		Reimplementing the code for rendertooltip from AppleSkin due to bug of not rendering food values if tooltip was wrapped
	 */
	@SubscribeEvent
	public void onRenderTooltip(PostText event)
	{
		if(!ModList.get().isLoaded("appleskin")) return;
		
		if (!event.isCanceled()) {
			ItemStack hoveredStack = event.getStack();
			if(DragonStateProvider.isDragon(Minecraft.getInstance().player)){
				if(DragonFoodHandler.isDragonEdible(hoveredStack.getItem(), DragonStateProvider.getDragonType(Minecraft.getInstance().player))){
					Minecraft mc = Minecraft.getInstance();
					Screen gui = mc.screen;
					
					int toolTipX = event.getX();
					int toolTipY = event.getY();
					int toolTipZ = 100;
					
					//Get tooltip length with 2 empty lines in a row to attempt to find the FoodOverlayTextComponent
					List<? extends ITextProperties> lines = event.getLines();
					boolean lastEmpty = false;
					for(int i = 0; i < lines.size(); ++i) {
						ITextProperties line = lines.get(i);
						if (line.getString().replace(" ", "").isEmpty() && lastEmpty) {
							toolTipY += (i-1) * 10;
							break;
						} else if (line.getString().replace(" ", "").isEmpty() && !lastEmpty) {
							lastEmpty = true;
						}
					}
					
					ItemStack itemStack = hoveredStack;
					
					Food food = DragonFoodHandler.getDragonFoodProperties(hoveredStack.getItem(), DragonStateProvider.getDragonType(Minecraft.getInstance().player));
					FoodValues values = new FoodValues(food.getNutrition(), food.getSaturationModifier());
					FoodValues defaultFood = values;
					FoodValues modifiedFood = values;
					
					MatrixStack matrixStack = event.getMatrixStack();
					RenderSystem.disableLighting();
					RenderSystem.disableDepthTest();
					RenderSystem.enableBlend();
					RenderSystem.defaultBlendFunc();
					matrixStack.pushPose();
					matrixStack.translate(0.0D, 0.0D, (double)toolTipZ);
					int y = toolTipY + 2;
					int defaultHunger = defaultFood.hunger;
					int modifiedHunger = modifiedFood.hunger;
					int biggestHunger = Math.max(defaultFood.hunger, modifiedFood.hunger);
					int hungerBars = (int)Math.ceil((double)((float)Math.abs(biggestHunger) / 2.0F));
					
					float biggestSaturationIncrement = Math.max(defaultFood.getSaturationIncrement(), modifiedFood.getSaturationIncrement());
					String saturationBarsText = null;
					
					int saturationBars = (int)Math.ceil((double)(Math.abs(biggestSaturationIncrement) / 2.0F));
					if (saturationBars > 10 || saturationBars == 0) {
						saturationBarsText = "x" + (biggestSaturationIncrement < 0.0F ? -1 : 1) * saturationBars;
						saturationBars = 1;
					}
					
					String hungerBarsText = null;
					if (hungerBars > 10) {
						hungerBarsText = "x" + (biggestHunger < 0 ? -1 : 1) * hungerBars;
						hungerBars = 1;
					}
					
					int x = toolTipX + (hungerBars - 1) * 9;
					mc.getTextureManager().bind(foodTextures);
					TextureOffsets offsets = isRotten(itemStack) ? rottenBarTextureOffsets : normalBarTextureOffsets;
					
					DragonStateHandler handler = DragonStateProvider.getCap(mc.player).orElse(null);
					
					DragonType type = handler.getType();
					int u = type == DragonType.FOREST ? 0 : type == DragonType.CAVE ? 9 : 18;
					
					for (int i = 0; i < hungerBars * 2; i += 2) {
						if (modifiedHunger < 0) {
							gui.blit(matrixStack, x, y, offsets.containerNegativeHunger - 16, u, 9, 9);
						} else if (modifiedHunger > defaultHunger && defaultHunger <= i) {
							gui.blit(matrixStack, x, y, offsets.containerExtraHunger - 16, u, 9, 9);
						} else if (modifiedHunger <= i + 1 && defaultHunger != modifiedHunger) {
							if (modifiedHunger == i + 1) {
								gui.blit(matrixStack, x, y, offsets.containerPartialHunger - 16, u, 9, 9);
							} else {
								RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.5F);
								gui.blit(matrixStack, x, y, offsets.containerMissingHunger - 16, u, 9, 9);
								RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
							}
						} else {
							gui.blit(matrixStack, x, y, offsets.containerNormalHunger - 16, u, 9, 9);
						}
						
						RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.25F);
						gui.blit(matrixStack, x, y, defaultHunger - 1 == i ? offsets.shankMissingPartial - 16 : offsets.shankMissingFull - 16, u, 9, 9);
						RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
						if (modifiedHunger > i) {
							gui.blit(matrixStack, x, y, modifiedHunger - 1 == i ? offsets.shankPartial - 16 : offsets.shankFull - 16, u, 9, 9);
						}
						
						x -= 9;
					}
					
					if (hungerBarsText != null) {
						x += 18;
						matrixStack.pushPose();
						matrixStack.translate((double)x, (double)y, 0.0D);
						matrixStack.scale(0.75F, 0.75F, 0.75F);
						mc.font.drawShadow(matrixStack, hungerBarsText, 2.0F, 2.0F, -5592406, false);
						matrixStack.popPose();
					}
					
					y += 10;
					float modifiedSaturationIncrement = modifiedFood.getSaturationIncrement();
					float absModifiedSaturationIncrement = Math.abs(modifiedSaturationIncrement);
					x = toolTipX + (saturationBars - 1) * 7;
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					mc.getTextureManager().bind(appleSkinTexture);
					
					for (int i = 0; i < saturationBars * 2; i += 2) {
						float effectiveSaturationOfBar = (absModifiedSaturationIncrement - (float)i) / 2.0F;
						boolean shouldBeFaded = absModifiedSaturationIncrement <= (float)i;
						if (shouldBeFaded) {
							RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.5F);
						}
						int offset = (handler.getType().ordinal() + 1) * 35;
						gui.blit(matrixStack, x, y, effectiveSaturationOfBar >= 1.0F ? 21 + offset : ((double)effectiveSaturationOfBar > 0.5D ? 14 + offset : ((double)effectiveSaturationOfBar > 0.25D ? 7 + offset : (effectiveSaturationOfBar > 0.0F ? 0 + offset : 28 + offset))), modifiedSaturationIncrement >= 0.0F ? 27 : 34, 7, 7);
						if (shouldBeFaded) {
							RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
						}
						
						x -= 7;
					}
					
					if (saturationBarsText != null) {
						x += 14;
						matrixStack.pushPose();
						matrixStack.translate((double)x, (double)y, 0.0D);
						matrixStack.scale(0.75F, 0.75F, 0.75F);
						mc.font.drawShadow(matrixStack, saturationBarsText, 2.0F, 1.0F, -5592406, false);
						matrixStack.popPose();
					}
					
					matrixStack.popPose();
					RenderSystem.disableBlend();
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					RenderSystem.disableRescaleNormal();
					RenderHelper.turnOff();
					RenderSystem.disableLighting();
					RenderSystem.disableDepthTest();
				}
			}
		}
	}
	
	
	@SubscribeEvent
	public void tooltip(TooltipOverlayEvent.Render renderEvent){
		if(DragonStateProvider.isDragon(Minecraft.getInstance().player)) {
			renderEvent.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void hudHunger(HungerRestored event){
		if(DragonStateProvider.isDragon(Minecraft.getInstance().player)){
			event.setCanceled(true);
			
			Minecraft mc = Minecraft.getInstance();
			PlayerEntity player = mc.player;
			MatrixStack matrixStack = event.matrixStack;
			
			int top = mc.getWindow().getGuiScaledHeight() - DragonFoodHandler.rightHeight + 10;
			int left = mc.getWindow().getGuiScaledWidth() / 2 - 91;
			int right = mc.getWindow().getGuiScaledWidth() / 2 + 91;
			
			generateHungerBarOffsets(top, left, right, mc.gui.getGuiTicks(), player);

			drawHungerOverlay(event.foodValues.hunger, event.currentFoodLevel, mc, matrixStack, right, top, flashAlpha, isRotten(event.itemStack));
		}
	}
	
	public static void drawHungerOverlay(int hungerRestored, int foodLevel, Minecraft mc, MatrixStack matrixStack, int right, int top, float alpha, boolean useRottenTextures) {
		if (hungerRestored != 0) {
			enableAlpha(alpha);
			mc.getTextureManager().bind(foodTextures);
			int modifiedFood = Math.min(20, foodLevel + hungerRestored);
			int startFoodBars = foodLevel / 2;
			int endFoodBars = (int)Math.ceil((double)((float)modifiedFood / 2.0F));
			int iconStartOffset = 0;
			int iconSize = 9;
			DragonType type = DragonStateProvider.getDragonType(Minecraft.getInstance().player);
			for(int i = startFoodBars; i < endFoodBars; ++i) {
				Point offset = (Point)foodBarOffsets.get(i);
				if (offset != null) {
					int x = right + offset.x;
					int y = top + offset.y;
					int v = type == DragonType.FOREST ? 0 : type == DragonType.CAVE ? 9 : 18;
					int u = iconStartOffset + 4 * iconSize;
					int ub = iconStartOffset + 1 * iconSize;
					if (useRottenTextures) {
						u += 4 * iconSize;
						ub += 12 * iconSize;
					}
					
					if (i * 2 + 1 == modifiedFood) {
						u += 1 * iconSize;
					}
					
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha * 0.25F);
					mc.gui.blit(matrixStack, x, y, ub, v, iconSize, iconSize);
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
					mc.gui.blit(matrixStack, x, y, u, v, iconSize, iconSize);
				}
			}
			mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
			disableAlpha(alpha);
		}
	}
	
	public static void drawSaturationOverlay(float saturationGained, float saturationLevel, Minecraft mc, MatrixStack matrixStack, int right, int top, float alpha) {
		if (!(saturationLevel + saturationGained < 0.0F)) {
			DragonStateHandler handler = DragonStateProvider.getCap(Minecraft.getInstance().player).orElse(null);
			
			enableAlpha(alpha);
			mc.getTextureManager().bind(appleSkinTexture);
			float modifiedSaturation = Math.min(saturationLevel + saturationGained, 20.0F);
			int startSaturationBar = 0;
			int endSaturationBar = (int)Math.ceil((double)(modifiedSaturation / 2.0F));
			if (saturationGained != 0.0F) {
				startSaturationBar = (int)Math.max(saturationLevel / 2.0F, 0.0F);
			}
			
			int iconSize = 9;
			
			for(int i = startSaturationBar; i < endSaturationBar; ++i) {
				Point offset = (Point)foodBarOffsets.get(i);
				if (offset != null) {
					int x = right + offset.x;
					int y = top + offset.y;
					int v = 0;
					int u = (handler.getType().ordinal() + 1) * 36;
					float effectiveSaturationOfBar = modifiedSaturation / 2.0F - (float)i;
					if (effectiveSaturationOfBar >= 1.0F) {
						u += (3 * iconSize);
					} else if ((double)effectiveSaturationOfBar > 0.5D) {
						u += (2 * iconSize);
					} else if ((double)effectiveSaturationOfBar > 0.25D) {
						u += (1 * iconSize);
					}
					
					mc.gui.blit(matrixStack, x, y, u, v, iconSize, iconSize);
				}
			}
			
			mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
			disableAlpha(alpha);
		}
	}
	
	private static void generateHungerBarOffsets(int top, int left, int right, int ticks, PlayerEntity player) {
		boolean preferFoodBars = true;
		boolean shouldAnimatedFood = false;
		int y;
		FoodStats stats = player.getFoodData();
		float saturationLevel = stats.getSaturationLevel();
		y = stats.getFoodLevel();
		shouldAnimatedFood = saturationLevel <= 0.0F && ticks % (y * 3 + 1) == 0;
		
		if (foodBarOffsets.size() != 10) {
			foodBarOffsets.setSize(10);
		}
		
		for(int i = 0; i < 10; ++i) {
			int x = right - i * 8 - 9;
			y = top;
			if (shouldAnimatedFood) {
				y = top + (player.level.random.nextInt(3) - 1);
			}
			
			Point point = (Point)foodBarOffsets.get(i);
			if (point == null) {
				point = new Point();
				foodBarOffsets.set(i, point);
			}
			
			point.x = x - right;
			point.y = y - top;
		}
		
	}
	
	public static void enableAlpha(float alpha) {
		RenderSystem.enableBlend();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
		RenderSystem.blendFunc(770, 771);
	}
	
	public static void disableAlpha(float alpha) {
		RenderSystem.disableBlend();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	public static boolean isRotten(ItemStack itemStack) {
		if (!DragonFoodHandler.isDragonEdible(itemStack.getItem(), DragonStateProvider.getDragonType(Minecraft.getInstance().player))) {
			return false;
		} else {
			Iterator var1 = itemStack.getItem().getFoodProperties().getEffects().iterator();
			
			Pair effect;
			do {
				if (!var1.hasNext()) {
					return false;
				}
				
				effect = (Pair)var1.next();
			} while(effect.getFirst() == null || ((EffectInstance)effect.getFirst()).getEffect() == null || ((EffectInstance)effect.getFirst()).getEffect().getCategory() != EffectType.HARMFUL);
			
			return true;
		}
	}
	
	static {
		normalBarTextureOffsets = new TextureOffsets();
		normalBarTextureOffsets.containerNegativeHunger = 43;
		normalBarTextureOffsets.containerExtraHunger = 133;
		normalBarTextureOffsets.containerNormalHunger = 16;
		normalBarTextureOffsets.containerPartialHunger = 124;
		normalBarTextureOffsets.containerMissingHunger = 34;
		normalBarTextureOffsets.shankMissingFull = 70;
		normalBarTextureOffsets.shankMissingPartial = normalBarTextureOffsets.shankMissingFull + 9;
		normalBarTextureOffsets.shankFull = 52;
		normalBarTextureOffsets.shankPartial = normalBarTextureOffsets.shankFull + 9;
		rottenBarTextureOffsets = new TextureOffsets();
		rottenBarTextureOffsets.containerNegativeHunger = normalBarTextureOffsets.containerNegativeHunger;
		rottenBarTextureOffsets.containerExtraHunger = normalBarTextureOffsets.containerExtraHunger;
		rottenBarTextureOffsets.containerNormalHunger = normalBarTextureOffsets.containerNormalHunger;
		rottenBarTextureOffsets.containerPartialHunger = normalBarTextureOffsets.containerPartialHunger;
		rottenBarTextureOffsets.containerMissingHunger = normalBarTextureOffsets.containerMissingHunger;
		rottenBarTextureOffsets.shankMissingFull = 106;
		rottenBarTextureOffsets.shankMissingPartial = rottenBarTextureOffsets.shankMissingFull + 9;
		rottenBarTextureOffsets.shankFull = 88;
		rottenBarTextureOffsets.shankPartial = rottenBarTextureOffsets.shankFull + 9;
	}
	
	
	static class TextureOffsets {
		int containerNegativeHunger;
		int containerExtraHunger;
		int containerNormalHunger;
		int containerPartialHunger;
		int containerMissingHunger;
		int shankMissingFull;
		int shankMissingPartial;
		int shankFull;
		int shankPartial;
		
		TextureOffsets() {
		}
	}
}
