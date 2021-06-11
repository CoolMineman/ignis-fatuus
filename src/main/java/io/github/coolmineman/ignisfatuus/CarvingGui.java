package io.github.coolmineman.ignisfatuus;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class CarvingGui extends LightweightGuiDescription {
    final boolean[][] carved_area;
    final CarvedPumpkinBlockEntity entity;
    CarvingScreen parent;

    public CarvingGui(CarvedPumpkinBlockEntity entity) {
        this.entity = entity;
        carved_area = entity.getCarved_area();
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        WSprite background = new WSprite(new Identifier("minecraft:textures/block/pumpkin_side.png"));
        root.add(background, 0, 0, 16, 16);
        for (int i = 0; i <= 11; i++) {
            for (int j = 0; j <= 11; j++) {
                WCarveButton bCarveButton = new WCarveButton(this, i, j);
                root.add(bCarveButton, i + 2, j + 2);
            }
        }
        root.validate(this);
    }

    private static class WCarveButton extends WSprite {
        private static final Identifier imageid = new Identifier("ignis-fatuus:textures/gui/carve_overlay.png");
        private CarvingGui yes;
        private int knapx;
        private int knapy;

        WCarveButton(CarvingGui yes, int knapx, int knapy) {
            super(imageid);
            this.yes = yes;
            this.knapx = knapx;
            this.knapy = knapy;
        }

        @Override
        public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
            if (yes.carved_area[knapx][knapy]) super.paint(matrices, x, y, mouseX, mouseY);
        }
    
        @Environment(EnvType.CLIENT)
        @Override
        public InputResult onMouseMove(int x, int y) {
            if (this.yes.parent.mouseDown && !yes.carved_area[knapx][knapy]) {
                MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                this.yes.carved_area[knapx][knapy] = true;
                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                passedData.writeBlockPos(yes.entity.getPos());
                passedData.writeInt(knapx);
                passedData.writeInt(knapy);
                ClientSidePacketRegistry.INSTANCE.sendToServer(Ignisfatuus.CARVE_PACKET_ID, passedData);
                return InputResult.PROCESSED;
            }
            return InputResult.IGNORED;
        }
    
        @Override
        public InputResult onClick(int x, int y, int button) {
            if (!yes.carved_area[knapx][knapy]) {
                MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                this.yes.carved_area[knapx][knapy] = true;
                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                passedData.writeBlockPos(yes.entity.getPos());
                passedData.writeInt(knapx);
                passedData.writeInt(knapy);
                ClientSidePacketRegistry.INSTANCE.sendToServer(Ignisfatuus.CARVE_PACKET_ID, passedData);
                return InputResult.PROCESSED;
            }
            return InputResult.IGNORED;
        }
    }
}
