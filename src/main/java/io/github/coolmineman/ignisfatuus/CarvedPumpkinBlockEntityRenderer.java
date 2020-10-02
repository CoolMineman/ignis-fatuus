package io.github.coolmineman.ignisfatuus;

import java.util.Random;

import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;

public class CarvedPumpkinBlockEntityRenderer extends BlockEntityRenderer<CarvedPumpkinBlockEntity> {
    static Random dumb_Random = new Random();

    public CarvedPumpkinBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(CarvedPumpkinBlockEntity entity, float tickDelta, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        float rot = 0f;
        switch (entity.getCachedState().get(HorizontalFacingBlock.FACING)) {
            case EAST:
                rot = -90;
                break;
            case NORTH:
                rot = 0;
                break;
            case SOUTH:
                rot = 180;
                break;
            case WEST:
                rot = 90;
                break;
            case UP:
            case DOWN:
            default:
                break;
        }
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rot));
        matrices.translate(-0.5, -0.5, -0.5);
        for (int i = 0; i <= 11; i++) {
            for (int j = 0; j <= 11; j++) {
                if (!entity.getCarved_area()[i][j]) {
                    drawBlockModelSmooth(entity, matrices, vertexConsumers, IgnisfatuusClient.PUMPKIN_MODEL_PARTS2[i][j]);
                }
            }
        }
        matrices.pop();
    }

    public static void drawBlockModelSmooth(BlockEntity entity, MatrixStack matrices, VertexConsumerProvider vcp,
            ModelIdentifier id) {
        MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().renderSmooth(entity.getWorld(),
                MinecraftClient.getInstance().getBakedModelManager().getModel(id), entity.getCachedState(),
                entity.getPos(), matrices, vcp.getBuffer(RenderLayer.getTranslucent()), true, dumb_Random, 1L,
                OverlayTexture.DEFAULT_UV);
    }

}
