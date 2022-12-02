package net.bird.projectcataclysm.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.bird.projectcataclysm.ProjectCataclysmMod;
import net.bird.projectcataclysm.block.custom.LaunchPlatformBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

import java.util.List;

@Environment(EnvType.CLIENT)
public class RemoteControlScreen extends HandledScreen<RemoteControlScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(ProjectCataclysmMod.MOD_ID,"textures/gui/remote_control.png");

    private static final Text LAUNCH_TEXT = Text.translatable("block.projectcataclysm.control_panel.launch");

    private int[] target;

    private final List<RemoteControlScreen.RemoteControlButtonWidget> buttons = Lists.newArrayList();

    public List<BlockPos> posList;

    public RemoteControlScreen(RemoteControlScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 228;
        this.backgroundWidth = 123;
    }

    private <T extends ClickableWidget & RemoteControlScreen.RemoteControlButtonWidget> void addButton(T button) {
        this.addDrawableChild(button);
        this.buttons.add(button);
    }

    protected void init() {
        super.init();
        this.buttons.clear();
        this.addButton(new RemoteControlScreen.DoneButtonWidget(this.x + 72, this.y + 155));
    }

    public void handledScreenTick() {
        super.handledScreenTick();
        this.tickButtons();
    }

    void tickButtons() {
        this.buttons.forEach(RemoteControlScreen.RemoteControlButtonWidget::tick);
    }

    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        if (mouseX - this.x > 13 && mouseY - this.y > 13 && mouseX - this.x <= 110 && mouseY - this.y <= 110) {
            this.renderTooltip(matrices, Text.literal("X: " + getTargetPos(mouseX - this.x, mouseY - this.y).getX() + " Z: " + getTargetPos(mouseX - this.x, mouseY - this.y).getZ()),mouseX - this.x, mouseY - this.y);
        }
        for (RemoteControlScreen.RemoteControlButtonWidget remoteControlButtonWidget : this.buttons) {
            if (remoteControlButtonWidget.shouldRenderTooltip()) {
                remoteControlButtonWidget.renderTooltip(matrices, mouseX - this.x, mouseY - this.y);
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
        LaunchPlatformBlockEntity lpbe = this.handler.getControlPanel();
        if (this.handler.getControlPanel().hasHead()) {
            this.drawTexture(matrices, 31 + this.x, 126 + this.y, 127, 0, 16, 24);
        }
        if (this.handler.getControlPanel().hasTail()) {
            this.drawTexture(matrices, 27 + this.x, 166 + this.y, 123, 40, 24, 44);
        }
        if (this.handler.getControlPanel().hasPayload()) {
            BlockState state = Block.getBlockFromItem(this.handler.getControlPanel().getPayload().getItem()).getDefaultState();
            ModelIdentifier modelIdentifier = BlockModels.getModelId(state);
            Sprite sprite = MinecraftClient.getInstance().getBakedModelManager().getModel(modelIdentifier).getQuads(state, Direction.EAST, Random.create()).get(0).getSprite();
            RenderSystem.setShaderTexture(0, new Identifier(sprite.getId().getNamespace(), "textures/" + sprite.getId().getPath() + ".png"));
            drawTexture(matrices, 31 + this.x, 150 + this.y, 0, 0, 16, 16, 16, 16);
            RenderSystem.setShaderTexture(0, TEXTURE);
            this.drawTexture(matrices, 31 + this.x, 150 + this.y, 127, 24, 16, 16);
        }
        if (target != null) {
            this.drawTexture(matrices, target[0] + this.x - 3, target[1] + this.y - 3, 104, 228, 5, 5);
            this.renderTooltip(matrices, Text.literal("X: " + getTargetPos(target[0], target[1]).getX() + " Z: " + getTargetPos(target[0], target[1]).getZ()), target[0] + this.x, target[1] + this.y);
        }
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        if (mouseX - this.x > 13 && mouseY - this.y > 13 && mouseX - this.x <= 110 && mouseY - this.y <= 110) {
            this.drawTexture(matrices, mouseX - 3, mouseY - 3, 104, 228, 5, 5);
        }
        PacketByteBuf buf = PacketByteBufs.create();
        ClientPlayNetworking.send(ProjectCataclysmMod.GET_PLAYERS_PACKET_ID, buf);
        if (this.posList != null) {
            for (BlockPos blockPos : this.posList) {
                this.drawTexture(matrices, (blockPos.getX() - this.handler.getPos().getX()) / 4 + 62 + this.x - 1, (blockPos.getZ() - this.handler.getPos().getZ()) / 4 + 62 + this.y - 1, 109, 228, 3, 3);
            }
        }
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX - this.x > 13 && mouseY - this.y > 13 && mouseX - this.x <= 110 && mouseY - this.y <= 110) {
            if (target == null) {
                target = new int[2];
            }
            target[0] = (int)(mouseX - this.x);
            target[1] = (int)(mouseY - this.y);
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBlockPos(this.handler.getSource());
            buf.writeInt(target[0]);
            buf.writeInt(target[1]);
            ClientPlayNetworking.send(ProjectCataclysmMod.TRANSMIT_PACKET_ID, buf);
            return true;
        }
        else {
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    public BlockPos getTargetPos(int x, int y) {
        return new BlockPos(4*(x-62) + this.handler.getPos().getX(), 0, 4*(y-62) + this.handler.getPos().getZ());
    }

    @Environment(EnvType.CLIENT)
    interface RemoteControlButtonWidget {
        boolean shouldRenderTooltip();

        void renderTooltip(MatrixStack matrices, int mouseX, int mouseY);

        void tick();
    }

    class DoneButtonWidget extends PressableWidget implements RemoteControlScreen.RemoteControlButtonWidget {
        protected DoneButtonWidget(int x, int y) {
            super(x, y, 22, 22, ScreenTexts.DONE);
        }

        public void onPress() {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBlockPos(RemoteControlScreen.this.handler.getPos());
            ClientPlayNetworking.send(ProjectCataclysmMod.LAUNCH_PACKET_ID, buf);
            RemoteControlScreen.this.close();
        }

        public void tick() {
            this.active = RemoteControlScreen.this.handler.getControlPanel().canLaunch() && target != null;
        }

        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, RemoteControlScreen.TEXTURE);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int i = 0;
            if (!this.active) {
                i += this.width;
            } else if (this.isHovered()) {
                i += this.width * 2;
            }

            this.drawTexture(matrices, this.x, this.y, i, 228, this.width, this.height);
            this.renderExtra(matrices);
        }

        protected void renderExtra(MatrixStack matrices) {
            this.drawTexture(matrices, this.x + 2, this.y + 2, 68, 229, 18, 18);
        }

        public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
            RemoteControlScreen.this.renderTooltip(matrices, LAUNCH_TEXT, mouseX, mouseY);
        }

        public boolean shouldRenderTooltip() {
            return this.hovered;
        }

        public void appendNarrations(NarrationMessageBuilder builder) {
            this.appendDefaultNarrations(builder);
        }
    }
}
