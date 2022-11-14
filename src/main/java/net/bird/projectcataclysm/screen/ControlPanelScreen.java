package net.bird.projectcataclysm.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ControlPanelScreen extends HandledScreen<ControlPanelScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(ProjectCataclysmMod.MOD_ID,"textures/gui/control_panel.png");
    private static final Text LAUNCH_TEXT = Text.translatable("block.projectcataclysm.control_panel.launch");
    private static final Text DISMANTLE_TEXT = Text.translatable("block.projectcataclysm.control_panel.dismantle");
    private final List<ControlPanelScreen.ControlPanelButtonWidget> buttons = Lists.newArrayList();

    private int[] target = new int[2];

    public ControlPanelScreen(ControlPanelScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 221;
        this.backgroundWidth = 230;
        this.titleX = 8;
        this.titleY = 6;
        this.playerInventoryTitleX = 36;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
        this.target[0] = handler.propertyDelegate.get(0);
        this.target[1] = handler.propertyDelegate.get(1);
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
        if (mouseX - this.x > 120 && mouseY - this.y > 22 && mouseX - this.x <= 217 && mouseY - this.y <= 119) {
            this.renderTooltip(matrices, Text.literal("X: " + getTargetPos(mouseX - this.x, mouseY - this.y).getX() + " Z: " + getTargetPos(mouseX - this.x, mouseY - this.y).getZ()),mouseX - this.x, mouseY - this.y);
        }
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
        if (this.handler.hasHead()) {
            this.drawTexture(matrices, 19 + this.x, 30 + this.y, 234, 0, 16, 24);
        }
        if (this.handler.hasTail()) {
            this.drawTexture(matrices, 15 + this.x, 70 + this.y, 230, 40, 24, 44);
        }
        if (this.handler.hasPayload()) {
            BlockState state = Block.getBlockFromItem(this.handler.getPayload().getItem()).getDefaultState();
            ModelIdentifier modelIdentifier = BlockModels.getModelId(state);
            Sprite sprite = MinecraftClient.getInstance().getBakedModelManager().getModel(modelIdentifier).getQuads(state, Direction.EAST, Random.create()).get(0).getSprite();
            RenderSystem.setShaderTexture(0, new Identifier(sprite.getId().getNamespace(), "textures/" + sprite.getId().getPath() + ".png"));
            drawTexture(matrices, 19 + this.x, 54 + this.y, 0, 0, 16, 16, 16, 16);
            RenderSystem.setShaderTexture(0, TEXTURE);
            this.drawTexture(matrices, 19 + this.x, 54 + this.y, 234, 24, 16, 16);
        }
        if (target != null) {
            this.drawTexture(matrices, target[0] + this.x - 3, target[1] + this.y - 3, 104, 221, 5, 5);
            this.renderTooltip(matrices, Text.literal("X: " + getTargetPos(target[0], target[1]).getX() + " Z: " + getTargetPos(target[0], target[1]).getZ()), target[0] + this.x, target[1] + this.y);
        }
        if (mouseX - this.x > 120 && mouseY - this.y > 22 && mouseX - this.x <= 217 && mouseY - this.y <= 119) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, TEXTURE);
            this.drawTexture(matrices, mouseX - 3, mouseY - 3, 104, 221, 5, 5);
        }
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX - this.x > 120 && mouseY - this.y > 22 && mouseX - this.x <= 217 && mouseY - this.y <= 119) {
            if (target == null) {
                target = new int[2];
            }
            target[0] = (int)(mouseX - this.x);
            target[1] = (int)(mouseY - this.y);
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(target[0]);
            buf.writeInt(target[1]);
            ClientPlayNetworking.send(ProjectCataclysmMod.TARGET_PACKET_ID, buf);
            return true;
        }
        else {
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    public BlockPos getTargetPos(int x, int y) {
        return new BlockPos(4*(x-169) + this.handler.getPos().getX(), 0, 4*(y-71) + this.handler.getPos().getZ());
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
            World world = ControlPanelScreen.this.client.world;
            assert world != null;
            BlockPos topPos = getTargetPos(target[0], target[1]).offset(Direction.UP, world.getTopY());;
            for (int i = world.getTopY(); i > world.getBottomY(); i--) {
                if (!(world.getBlockState(topPos).getBlock() instanceof AirBlock)) {
                    break;
                }
                topPos = topPos.down();
            }
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBlockPos(ControlPanelScreen.this.handler.getPos());
            buf.writeBlockPos(topPos);
            buf.writeItemStack(ControlPanelScreen.this.handler.getPayload());
            ClientPlayNetworking.send(ProjectCataclysmMod.LAUNCH_PACKET_ID, buf);
        }

        public void tick() {
            this.active = ControlPanelScreen.this.handler.canLaunch() && target != null;
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
            MinecraftClient client = ControlPanelScreen.this.client;
            assert client != null;
            assert client.player != null;
            assert client.world != null;
            client.world.playSound(client.player, ControlPanelScreen.this.handler.getPos(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBlockPos(ControlPanelScreen.this.handler.getPos());
            ClientPlayNetworking.send(ProjectCataclysmMod.DISMANTLE_PACKET_ID, buf);
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
