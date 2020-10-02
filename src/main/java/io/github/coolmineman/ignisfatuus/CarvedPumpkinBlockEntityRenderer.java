package io.github.coolmineman.ignisfatuus;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;

public class CarvedPumpkinBlockEntityRenderer extends BlockEntityRenderer<CarvedPumpkinBlockEntity> {

    public CarvedPumpkinBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(CarvedPumpkinBlockEntity entity, float tickDelta, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        drawBlockModel(matrices, vertexConsumers, WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos()), IgnisfatuusClient.PUMPKIN_MODEL_PARTS2[0][0]);
        matrices.pop();
    }

    public static void drawBlockModel(MatrixStack matrices, VertexConsumerProvider vcp, int light, ModelIdentifier id) {
        MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(
                matrices.peek(),
                vcp.getBuffer(RenderLayer.getSolid()),
                null,
                MinecraftClient.getInstance().getBakedModelManager().getModel(id),
                1f, 1f, 1f,
                Math.max(light - 3, 0),
                OverlayTexture.DEFAULT_UV
        );
    }
    
}
