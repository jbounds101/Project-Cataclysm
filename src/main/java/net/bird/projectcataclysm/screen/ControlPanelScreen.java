package net.bird.projectcataclysm.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ControlPanelScreen extends HandledScreen<ControlPanelScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(ProjectCataclysmMod.MOD_ID,"textures/gui/control_panel.png");
    private static final Text LAUNCH_TEXT = Text.translatable("block.projectcataclysm.control_panel.launch");
    private static final Text DISMANTLE_TEXT = Text.translatable("block.projectcataclysm.control_panel.dismantle");
    private final List<ControlPanelScreen.ControlPanelButtonWidget> buttons = Lists.newArrayList();

    public ControlPanelScreen(ControlPanelScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 221;
        this.backgroundWidth = 230;
        this.titleX = 8;
        this.titleY = 6;
        this.playerInventoryTitleX = 36;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    private <T extends ClickableWidget & ControlPanelScreen.ControlPanelButtonWidget> void addButton(T button) {
        this.addDrawableChild(button);
        this.buttons.add(button);
    }

    protected void init() {
        super.init();
        this.buttons.clear();
        this.addButton(new DoneButtonWidget(this.x + 79, this.y + 61));
        this.addButton(new DismantleButtonWidget(this.x + 206, this.y + 4));
    }

    public void handledScreenTick() {
        super.handledScreenTick();
        this.tickButtons();
    }

    void tickButtons() {
        this.buttons.forEach(ControlPanelButtonWidget::tick);
    }

    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        super.drawForeground(matrices, mouseX, mouseY);
        for (ControlPanelButtonWidget controlPanelButtonWidget : this.buttons) {
            if (controlPanelButtonWidget.shouldRenderTooltip()) {
                controlPanelButtonWidget.renderTooltip(matrices, mouseX - this.x, mouseY - this.y);
                break;
            }
        }
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int i = this.x;
        int j = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Environment(EnvType.CLIENT)
    interface ControlPanelButtonWidget {
        boolean shouldRenderTooltip();

        void renderTooltip(MatrixStack matrices, int mouseX, int mouseY);

        void tick();
    }

    @Environment(EnvType.CLIENT)
    class DoneButtonWidget extends PressableWidget implements ControlPanelButtonWidget {
        protected DoneButtonWidget(int x, int y) {
            super(x, y, 22, 22, ScreenTexts.DONE);
        }

        public void onPress() {
            assert ControlPanelScreen.this.client != null;
            assert ControlPanelScreen.this.client.player != null;
            ControlPanelScreen.this.client.player.closeHandledScreen();
        }

        public void tick() {
            this.active = ControlPanelScreen.this.handler.canLaunch();
        }

        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, ControlPanelScreen.TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int i = 0;
            if (!this.active) {
                i += this.width;
            } else if (this.isHovered()) {
                i += this.width * 2;
            }

            this.drawTexture(matrices, this.x, this.y, i, 221, this.width, this.height);
            this.renderExtra(matrices);
        }

        protected void renderExtra(MatrixStack matrices) {
            this.drawTexture(matrices, this.x + 2, this.y + 2, 68, 222, 18, 18);
        }

        public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
            ControlPanelScreen.this.renderTooltip(matrices, LAUNCH_TEXT, mouseX, mouseY);
        }

        public boolean shouldRenderTooltip() {
            return this.hovered;
        }

        public void appendNarrations(NarrationMessageBuilder builder) {
            this.appendDefaultNarrations(builder);
        }
    }

    @Environment(EnvType.CLIENT)
    class DismantleButtonWidget extends PressableWidget implements ControlPanelButtonWidget {
        protected DismantleButtonWidget(int x, int y) {
            super(x, y, 16, 11, ScreenTexts.CANCEL);
        }

        public void onPress() {
            assert ControlPanelScreen.this.client != null;
            assert ControlPanelScreen.this.client.player != null;
            assert ControlPanelScreen.this.client.world != null;
            ControlPanelScreen.this.handler.dismantle(ControlPanelScreen.this.client.player);
            ControlPanelScreen.this.client.player.closeHandledScreen();
        }

        public void tick() {
        }

        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, ControlPanelScreen.TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int j = 221;
            if (this.isHovered()) {
                j += this.height;
            }

            this.drawTexture(matrices, this.x, this.y, 88, j, this.width, this.height);

        }

        public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
            ControlPanelScreen.this.renderTooltip(matrices, DISMANTLE_TEXT, mouseX, mouseY);
        }

        public boolean shouldRenderTooltip() {
            return this.hovered;
        }

        public void appendNarrations(NarrationMessageBuilder builder) {
            this.appendDefaultNarrations(builder);
        }
    }
}
