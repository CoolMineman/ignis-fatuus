package io.github.coolmineman.ignisfatuus;

import java.util.Random;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;

public class CarvedPumpkinBlockEntityRenderer extends BlockEntityRenderer<CarvedPumpkinBlockEntity> {
    static Random dumb_Random = new Random();

    public CarvedPumpkinBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(CarvedPumpkinBlockEntity entity, float tickDelta, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        drawBlockModelSmooth(entity, matrices, vertexConsumers, IgnisfatuusClient.PUMPKIN_MODEL_PARTS2[11][11]);
        matrices.pop();
    }
    
    public static void drawBlockModelSmooth(BlockEntity entity, MatrixStack matrices, VertexConsumerProvider vcp, ModelIdentifier id) {
        MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().renderSmooth(entity.getWorld(), MinecraftClient.getInstance().getBakedModelManager().getModel(id), entity.getCachedState(), entity.getPos(), matrices, vcp.getBuffer(RenderLayer.getTranslucent()), true, dumb_Random, 1L, OverlayTexture.DEFAULT_UV);
    }
    
}
