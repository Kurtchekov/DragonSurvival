package by.jackraidenph.dragonsurvival.client.gui;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.client.gui.widgets.buttons.HelpButton;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.common.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.common.entity.DragonEntity;
import by.jackraidenph.dragonsurvival.client.render.entity.dragon.DragonRenderer;
import by.jackraidenph.dragonsurvival.client.gui.widgets.buttons.TabButton;
import by.jackraidenph.dragonsurvival.client.render.ClientDragonRender;
import by.jackraidenph.dragonsurvival.client.handlers.DragonSkins;
import by.jackraidenph.dragonsurvival.client.handlers.magic.ClientMagicHUDHandler;
import by.jackraidenph.dragonsurvival.network.NetworkHandler;
import by.jackraidenph.dragonsurvival.network.entity.player.SyncDragonSkinSettings;
import by.jackraidenph.dragonsurvival.common.entity.DSEntities;
import by.jackraidenph.dragonsurvival.misc.DragonLevel;
import com.ibm.icu.impl.Pair;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.ConfirmOpenLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.processor.IBone;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SkinsScreen extends Screen
{
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(DragonSurvivalMod.MODID, "textures/gui/skin_interface.png");
	
	private static final ResourceLocation UNCHECKED = new ResourceLocation(DragonSurvivalMod.MODID, "textures/gui/unchecked.png");
	private static final ResourceLocation CHECKED = new ResourceLocation(DragonSurvivalMod.MODID, "textures/gui/dragon_claws_tetris.png");
	
	private static final ResourceLocation DISCORD = new ResourceLocation(DragonSurvivalMod.MODID, "textures/gui/discord_button.png");
	private static final ResourceLocation WIKI = new ResourceLocation(DragonSurvivalMod.MODID, "textures/gui/wiki_button.png");
	private static final ResourceLocation HELP = new ResourceLocation(DragonSurvivalMod.MODID, "textures/gui/dragon_claws_button.png");
	
	
	private static final String DISCORD_URL = "http://discord.gg/8SsB8ar";
	private static final String WIKI_URL = "https://dragons-survival.fandom.com/wiki/Skins";
	
	public Screen sourceScreen;
	
	private int guiLeft;
	private int guiTop;
	
	protected int imageWidth = 164;
	protected int imageHeight = 128;
	
	private float yRot = -3;
	private float xRot = -5;
	private float zoom = 0;
	
	private static String playerName = null;
	private static String lastPlayerName = null;
	private static DragonLevel level = DragonLevel.ADULT;
	
	private DragonEntity dragon;
	private static RemoteClientPlayerEntity clientPlayer;
	
	
	private static ArrayList<String> seenSkins = new ArrayList<>();
	
	public static ResourceLocation skinTexture = null;
	public static ResourceLocation glowTexture = null;
	private static boolean noSkin = false;
	private static boolean loading = false;
	
	
	private static ExecutorService executor = Executors.newSingleThreadExecutor();
	
	public SkinsScreen(Screen sourceScreen)
	{
		super(new StringTextComponent(""));
		this.sourceScreen = sourceScreen;
	}
	
	public void setTextures(){
		loading = true;
		
		DragonStateHandler handler = DragonStateProvider.getCap(Minecraft.getInstance().player).orElse(null);
		ResourceLocation skinTexture = DragonSkins.getPlayerSkin(playerName + "_" + level.name);
		ResourceLocation glowTexture = null;
		boolean defaultSkin = false;
		
		if((!DragonSkins.renderStage(minecraft.player, level) && playerName == minecraft.player.getGameProfile().getName()) || skinTexture == null){
			skinTexture = DragonSkins.getDefaultSkin(handler.getType(), level);
			defaultSkin = true;
		}
		
		if(skinTexture != null) {
			if(!defaultSkin) {
				glowTexture = DragonSkins.getPlayerGlow(playerName + "_" + level.name);
			}
		}
		
		SkinsScreen.glowTexture = glowTexture;
		SkinsScreen.skinTexture = skinTexture;
		
		if(Objects.equals(lastPlayerName, playerName) || lastPlayerName == null) {
			zoom = level.size;
		}
		
		noSkin = defaultSkin;
		loading = false;
		lastPlayerName = playerName;
	}
	
	@Override
	protected void init() {
		super.init();
		
		this.guiLeft = (this.width - 256) / 2;
		this.guiTop = (this.height - 128) / 2;
		
		if(playerName == null) {
			playerName = minecraft.player.getGameProfile().getName();
		}
		
		if(clientPlayer == null) {
			clientPlayer = new RemoteClientPlayerEntity(minecraft.level, new GameProfile(UUID.randomUUID(), "DRAGON_RENDER"));
			DragonStateHandler handler = DragonStateProvider.getCap(Minecraft.getInstance().player).orElse(null);
			
			DragonStateProvider.getCap(clientPlayer).ifPresent((cap) -> {
				cap.setHasWings(true);
				
				if(handler != null){
					cap.setType(handler.getType());
				}
			});
		}
		
		dragon = new DragonEntity(DSEntities.DRAGON, minecraft.level){
			@Override
			public void registerControllers(AnimationData animationData) {
				animationData.addAnimationController(new AnimationController<DragonEntity>(this, "controller", 2, (event) -> {
					AnimationBuilder builder = new AnimationBuilder();
					builder.addAnimation("fly", true);
					event.getController().setAnimation(builder);
					return PlayState.CONTINUE;
				}));
			}
			
			@Override
			public PlayerEntity getPlayer()
			{
				return clientPlayer;
			}
		};
		
		setTextures();
	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
	
	@Override
	public void init(Minecraft p_231158_1_, int width, int height)
	{
		super.init(p_231158_1_, width, height);
		
		int startX = this.guiLeft;
		int startY = this.guiTop;
		
		addButton(new TabButton(startX + 128 + 5, startY - 26, 0, this));
		addButton(new TabButton(startX + 128 + 33, startY - 26, 1, this));
		addButton(new TabButton(startX + 128 + 62, startY - 26, 2, this));
		addButton(new TabButton(startX + 128 + 91, startY - 28, 3, this));
		
		addButton(new Button(startX + 128, startY + 45, imageWidth, 20, new TranslationTextComponent("ds.level.newborn"), (button) -> {
			DragonStateHandler handler = DragonStateProvider.getCap(getMinecraft().player).orElse(null);
			
			if(handler != null){
				handler.getSkin().renderNewborn = !handler.getSkin().renderNewborn;
				NetworkHandler.CHANNEL.sendToServer(new SyncDragonSkinSettings(getMinecraft().player.getId(), handler.getSkin().renderNewborn, handler.getSkin().renderYoung, handler.getSkin().renderAdult));
				executor.execute(() -> setTextures());
			}
		}){
			@Override
			public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_)
			{
				super.renderButton(p_230431_1_, p_230431_2_, p_230431_3_, p_230431_4_);
				
				DragonStateHandler handler = DragonStateProvider.getCap(getMinecraft().player).orElse(null);
				minecraft.getTextureManager().bind(handler == null || !handler.getSkin().renderNewborn ? UNCHECKED : CHECKED);
				blit(p_230431_1_, x + 3, y + 3, 0, 0, 13, 13, 13,13);
			}
		});
		
		addButton(new Button(startX + 128, startY + 45 + 23, imageWidth, 20, new TranslationTextComponent("ds.level.young"), (button) -> {
			DragonStateHandler handler = DragonStateProvider.getCap(getMinecraft().player).orElse(null);
			
			if(handler != null){
				handler.getSkin().renderYoung = !handler.getSkin().renderYoung;
				NetworkHandler.CHANNEL.sendToServer(new SyncDragonSkinSettings(getMinecraft().player.getId(), handler.getSkin().renderNewborn, handler.getSkin().renderYoung, handler.getSkin().renderAdult));
				executor.execute(() -> setTextures());
			}
		}){
			@Override
			public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_)
			{
				super.renderButton(p_230431_1_, p_230431_2_, p_230431_3_, p_230431_4_);
				
				DragonStateHandler handler = DragonStateProvider.getCap(getMinecraft().player).orElse(null);
				minecraft.getTextureManager().bind(handler == null || !handler.getSkin().renderYoung ? UNCHECKED : CHECKED);
				blit(p_230431_1_, x + 3, y + 3, 0, 0, 13, 13, 13,13);
			}
		});
		
		addButton(new Button(startX + 128, startY + 45 + 46, imageWidth, 20, new TranslationTextComponent("ds.level.adult"), (button) -> {
			DragonStateHandler handler = DragonStateProvider.getCap(getMinecraft().player).orElse(null);
			
			if(handler != null){
				handler.getSkin().renderAdult = !handler.getSkin().renderAdult;
				NetworkHandler.CHANNEL.sendToServer(new SyncDragonSkinSettings(getMinecraft().player.getId(), handler.getSkin().renderNewborn, handler.getSkin().renderYoung, handler.getSkin().renderAdult));
				executor.execute(() -> setTextures());
			}
		}){
			@Override
			public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_)
			{
				super.renderButton(p_230431_1_, p_230431_2_, p_230431_3_, p_230431_4_);
				
				DragonStateHandler handler = DragonStateProvider.getCap(getMinecraft().player).orElse(null);
				minecraft.getTextureManager().bind(handler == null || !handler.getSkin().renderAdult ? UNCHECKED : CHECKED);
				blit(p_230431_1_, x + 3, y + 3, 0, 0, 13, 13, 13,13);
			}
		});
		
		addButton(new Button(startX + 128, startY + 128, imageWidth, 20, new TranslationTextComponent("ds.gui.skins.other_skins"), (button) -> {
			ConfigHandler.CLIENT.renderOtherPlayerSkins.set(!ConfigHandler.CLIENT.renderOtherPlayerSkins.get());
			executor.execute(() -> setTextures());
		}){
			@Override
			public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_)
			{
				super.renderButton(p_230431_1_, p_230431_2_, p_230431_3_, p_230431_4_);
				
				minecraft.getTextureManager().bind(ConfigHandler.CLIENT.renderOtherPlayerSkins.get() ? CHECKED : UNCHECKED);
				blit(p_230431_1_, x + 3, y + 3, 0, 0, 13, 13, 13,13);
			}
		});
		
		addButton(new Button(startX + 128 + (imageWidth / 2) - 8, startY + 128 + 30, 16, 16, new StringTextComponent(""), (button) -> {
			try {
				URI uri = new URI(DISCORD_URL);
				this.clickedLink = uri;
				this.minecraft.setScreen(new ConfirmOpenLinkScreen(this::confirmLink, DISCORD_URL, false));
			} catch (URISyntaxException urisyntaxexception) {
				urisyntaxexception.printStackTrace();
			}
		}){
			@Override
			public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_)
			{
				minecraft.getTextureManager().bind(DISCORD);
				blit(p_230431_1_, x, y, 0, 0, 16, 16, 16,16);
			}
			
			@Override
			public void renderToolTip(MatrixStack p_230443_1_, int p_230443_2_, int p_230443_3_)
			{
				GuiUtils.drawHoveringText(p_230443_1_, Arrays.asList(new TranslationTextComponent("ds.gui.skins.tooltip.discord")), p_230443_2_, p_230443_3_, Minecraft.getInstance().screen.width, Minecraft.getInstance().screen.height, 200, Minecraft.getInstance().font);
			}
		});
		
		addButton(new Button(startX + 128 + (imageWidth / 2) - 8 + 25, startY + 128 + 30, 16, 16, new StringTextComponent(""), (button) -> {
			try {
				URI uri = new URI(WIKI_URL);
				this.clickedLink = uri;
				this.minecraft.setScreen(new ConfirmOpenLinkScreen(this::confirmLink, WIKI_URL, false));
			} catch (URISyntaxException urisyntaxexception) {
				urisyntaxexception.printStackTrace();
			}
		}){
			@Override
			public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_)
			{
				minecraft.getTextureManager().bind(WIKI);
				blit(p_230431_1_, x, y, 0, 0, 16, 16, 16,16);
			}
			
			@Override
			public void renderToolTip(MatrixStack p_230443_1_, int p_230443_2_, int p_230443_3_)
			{
				GuiUtils.drawHoveringText(p_230443_1_, Arrays.asList(new TranslationTextComponent("ds.gui.skins.tooltip.wiki")), p_230443_2_, p_230443_3_, Minecraft.getInstance().screen.width, Minecraft.getInstance().screen.height, 200, Minecraft.getInstance().font);
			}
		});
		
		addButton(new HelpButton(startX + 128 + (imageWidth / 2) - 8 - 25, startY + 128 + 30, 16, 16, "ds.gui.skins.tooltip.help"));
		
		addButton(new Button(startX - 60, startY + 128, 90, 20, new TranslationTextComponent("ds.gui.skins.yours"), (button) -> {
			playerName = minecraft.player.getGameProfile().getName();
			setTextures();
		}){
			@Override
			public void renderToolTip(MatrixStack p_230443_1_, int p_230443_2_, int p_230443_3_)
			{
				GuiUtils.drawHoveringText(p_230443_1_, Arrays.asList(new TranslationTextComponent("ds.gui.skins.tooltip.yours")), p_230443_2_, p_230443_3_, Minecraft.getInstance().screen.width, Minecraft.getInstance().screen.height, 200, Minecraft.getInstance().font);
			}
		});
		
		addButton(new Button(startX + 35, startY + 128, 60, 20, new TranslationTextComponent("ds.gui.skins.random"), (button) -> {
			ArrayList<Pair<DragonLevel, String>> skins = new ArrayList<>();
			ArrayList<String> users = new ArrayList<>();
			Random random = new Random();
			
			for(Map.Entry<DragonLevel, ArrayList<String>> ent : DragonSkins.SKIN_USERS.entrySet()){
				for(String user : ent.getValue()){
					skins.add(Pair.of(ent.getKey(), user));
					
					if(!users.contains(user)){
						users.add(user);
					}
				}
			}
			
			skins.removeIf((c) -> seenSkins.contains(c.second));
			if(skins.size() > 0) {
				Pair<DragonLevel, String> skin = skins.get(random.nextInt(skins.size()));
				
				if (skin != null) {
					level = skin.first;
					playerName = skin.second;
					
					seenSkins.add(skin.second);
					
					if (seenSkins.size() >= users.size() / 2) {
						seenSkins.remove(0);
					}
					
					executor.execute(() -> setTextures());
				}
			}
		}){
			@Override
			public void renderToolTip(MatrixStack p_230443_1_, int p_230443_2_, int p_230443_3_)
			{
				GuiUtils.drawHoveringText(p_230443_1_, Arrays.asList(new TranslationTextComponent("ds.gui.skins.tooltip.random")), p_230443_2_, p_230443_3_, Minecraft.getInstance().screen.width, Minecraft.getInstance().screen.height, 200, Minecraft.getInstance().font);
			}
		});
		
		addButton(new Button(startX + 90, startY + 10, 11, 17, new StringTextComponent(""), (button) -> {
			int pos = MathHelper.clamp(level.ordinal() + 1, 0, DragonLevel.values().length - 1);
			level = DragonLevel.values()[pos];
			
			executor.execute(() -> setTextures());
		}){
			@Override
			public void renderButton(MatrixStack stack, int mouseX, int mouseY, float p_230431_4_)
			{
				Minecraft.getInstance().getTextureManager().bind(ClientMagicHUDHandler.widgetTextures);
				
				if(isHovered()){
					blit(stack, x, y, 66 / 2, 222 / 2, 11, 17,128, 128);
				}else{
					blit(stack, x, y, 44 / 2, 222 / 2, 11, 17, 128, 128);
				}
			}
		});
		
		addButton(new Button(startX - 70, startY + 10, 11, 17, new StringTextComponent(""), (button) -> {
			int pos = MathHelper.clamp(level.ordinal() - 1, 0, DragonLevel.values().length - 1);
			level = DragonLevel.values()[pos];
			
			executor.execute(() -> setTextures());
		}){
			@Override
			public void renderButton(MatrixStack stack, int mouseX, int mouseY, float p_230431_4_)
			{
				Minecraft.getInstance().getTextureManager().bind(ClientMagicHUDHandler.widgetTextures);
				
				if(isHovered()){
					blit(stack, x, y, 22 / 2, 222 / 2, 11, 17,128, 128);
				}else{
					blit(stack, x, y, 0, 222 / 2, 11, 17, 128, 128);
				}
			}
		});
	}
	
	@Override
	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		if (this.minecraft == null)
			return;
		
		GL11.glTranslatef(0F, 0F, -100);
		this.renderBackground(stack);
		GL11.glTranslatef(0F, 0F, 100);
		
		int startX = this.guiLeft;
		int startY = this.guiTop;
		
		DragonStateHandler handler = DragonStateProvider.getCap(getMinecraft().player).orElse(null);
		
		if(handler != null) {
			stack.pushPose();
			final IBone neckandHead = ClientDragonRender.dragonModel.getAnimationProcessor().getBone("Neck");
			
			if (neckandHead != null) {
				neckandHead.setHidden(false);
			}
			
			EntityRenderer<? super DragonEntity> dragonRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(dragon);

			ClientDragonRender.dragonModel.setCurrentTexture(skinTexture);
			((DragonRenderer)dragonRenderer).glowTexture = glowTexture;
			float scale = zoom;
			stack.scale(scale, scale, scale);
			
			if(!loading) {
				renderEntityInInventory(startX + 10, startY + 90, scale, xRot, yRot, dragon);
			}
			
			((DragonRenderer)dragonRenderer).glowTexture = null;
			
			stack.popPose();
			
			GL11.glTranslatef(0F, 0F, 400);
			
			this.minecraft.getTextureManager().bind(BACKGROUND_TEXTURE);
			blit(stack, startX + 128, startY, 0, 0, 164, 256);
			
			drawNonShadowString(stack, minecraft.font, new TranslationTextComponent("ds.gui.skins").withStyle(TextFormatting.DARK_GRAY), startX + 128 + ((imageWidth) / 2), startY + 7, -1);
			drawCenteredString(stack, minecraft.font, new TranslationTextComponent("ds.gui.skins.toggle"), startX + 128 + ((imageWidth) / 2), startY + 30, -1);
			
			drawNonShadowString(stack, minecraft.font, new StringTextComponent(playerName + " - " + level.getName()).withStyle(TextFormatting.GRAY), startX + 15, startY - 15, -1);
			
			if(!loading) {
				if (noSkin) {
					if (playerName == minecraft.player.getGameProfile().getName()) {
						drawNonShadowLineBreak(stack, minecraft.font, new TranslationTextComponent("ds.gui.skins.noskin.yours").withStyle(TextFormatting.DARK_GRAY), startX + 40, startY + this.imageHeight - 20, -1);
						
					} else {
						drawNonShadowLineBreak(stack, minecraft.font, new TranslationTextComponent("ds.gui.skins.noskin").withStyle(TextFormatting.DARK_GRAY), startX + 65, startY + this.imageHeight - 20, -1);
					}
				}
			}
			
			super.render(stack, mouseX, mouseY, partialTicks);
			
			for(Widget btn : buttons){
				if(btn.isHovered()){
					btn.renderToolTip(stack, mouseX, mouseY);
				}
			}
			
			GL11.glTranslatef(0F, 0F, -400f);
			
		}
	}
	
	public void renderEntityInInventory(int p_228187_0_, int p_228187_1_, float p_228187_2_, float p_228187_3_, float p_228187_4_, LivingEntity p_228187_5_) {
		float f = p_228187_3_;
		float f1 = p_228187_4_;
		RenderSystem.pushMatrix();
		RenderSystem.translatef((float)p_228187_0_, (float)p_228187_1_, 1050.0F);
		RenderSystem.scalef(1.0F, 1.0F, -1.0F);
		MatrixStack matrixstack = new MatrixStack();
		matrixstack.translate(0, (Math.abs(yRot) / 17) * -70, 0);
		matrixstack.translate(0.0D, 0.0D, 1000.0D);
		matrixstack.scale((float)p_228187_2_, (float)p_228187_2_, (float)p_228187_2_);
		Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
		Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 10.0F);
		quaternion.mul(quaternion1);
		matrixstack.mulPose(quaternion);
		float f2 = p_228187_5_.yBodyRot;
		float f3 = p_228187_5_.yRot;
		float f4 = p_228187_5_.xRot;
		float f5 = p_228187_5_.yHeadRotO;
		float f6 = p_228187_5_.yHeadRot;
		p_228187_5_.yBodyRot = 180.0F + f * 10.0F;
		p_228187_5_.yRot = 180.0F + f * 10.0F;
		p_228187_5_.xRot = -f1 * 10.0F;
		p_228187_5_.yHeadRot = p_228187_5_.yRot;
		p_228187_5_.yHeadRotO = p_228187_5_.yRot;
		EntityRendererManager entityrenderermanager = Minecraft.getInstance().getEntityRenderDispatcher();
		boolean renderHitbox = entityrenderermanager.shouldRenderHitBoxes();
		quaternion1.conj();
		entityrenderermanager.overrideCameraOrientation(quaternion1);
		entityrenderermanager.setRenderShadow(false);
		IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
		RenderSystem.runAsFancy(() -> {
			entityrenderermanager.setRenderHitBoxes(false);
			entityrenderermanager.render(p_228187_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
			entityrenderermanager.setRenderHitBoxes(renderHitbox);
		});
		irendertypebuffer$impl.endBatch();
		entityrenderermanager.setRenderShadow(true);
		
		p_228187_5_.yBodyRot = f2;
		p_228187_5_.yRot = f3;
		p_228187_5_.xRot = f4;
		p_228187_5_.yHeadRotO = f5;
		p_228187_5_.yHeadRot = f6;
		RenderSystem.popMatrix();
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount)
	{
		zoom += amount;
		zoom = MathHelper.clamp(zoom, 10, 80);
		
		return true;
	}
	
	public static void drawNonShadowString(MatrixStack p_238472_0_, FontRenderer p_238472_1_, ITextComponent p_238472_2_, int p_238472_3_, int p_238472_4_, int p_238472_5_) {
		p_238472_1_.draw(p_238472_0_, LanguageMap.getInstance().getVisualOrder(p_238472_2_), (int)(p_238472_3_ - p_238472_1_.width(p_238472_2_) / 2), (int)p_238472_4_, p_238472_5_);
	}
	
	public static void drawNonShadowLineBreak(MatrixStack p_238472_0_, FontRenderer p_238472_1_, ITextComponent p_238472_2_, int p_238472_3_, int p_238472_4_, int p_238472_5_) {
		IReorderingProcessor ireorderingprocessor = p_238472_2_.getVisualOrderText();
		
		List<ITextProperties> wrappedLine = p_238472_1_.getSplitter().splitLines(p_238472_2_, 150, Style.EMPTY);
		int i = 0;
		for(ITextProperties properties : wrappedLine){
			p_238472_1_.draw(p_238472_0_, LanguageMap.getInstance().getVisualOrder(properties), (int)(p_238472_3_ - p_238472_1_.width(ireorderingprocessor) / 2), (int)p_238472_4_ + i * 9, p_238472_5_);
			i++;
		}
	}
	
	@Override
	public boolean mouseDragged(double x1, double y1, int p_231045_5_, double x2, double y2)
	{
		xRot -= x2 / 6;
		yRot -= y2 / 6;
		
		xRot = MathHelper.clamp(xRot, -17, 17);
		yRot = MathHelper.clamp(yRot, -17, 17);
		
		return super.mouseDragged(x1, y1, p_231045_5_, x2, y2);
	}
	
	private URI clickedLink;
	
	private void confirmLink(boolean p_231162_1_) {
		if (p_231162_1_) {
			this.openLink(this.clickedLink);
		}
		
		this.clickedLink = null;
		this.minecraft.setScreen(this);
	}
	
	private void openLink(URI p_231156_1_) {
		Util.getPlatform().openUri(p_231156_1_);
	}
}
