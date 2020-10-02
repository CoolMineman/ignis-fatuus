package io.github.coolmineman.ignisfatuus;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.blockstate.JVariant;
import net.devtech.arrp.json.models.JElement;
import net.devtech.arrp.json.models.JFace;
import net.devtech.arrp.json.models.JFaces;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JRotation;
import net.devtech.arrp.json.models.JTextures;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction.Axis;

public class IgnisfatuusClient implements ClientModInitializer {
    public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create("ignis-fatuus:runtime_resources");
    private static final Identifier[][] PUMPKIN_MODEL_PARTS = new Identifier[12][12];
    public static final ModelIdentifier[][] PUMPKIN_MODEL_PARTS2 = new ModelIdentifier[12][12];

    static {
        for (int i = 0; i <= 11; i++) {
            for (int j = 0; j <= 11; j++) {
                PUMPKIN_MODEL_PARTS[i][j] = new Identifier("ignis-fatuus:block/pumpkin_model_part_" + i + "_" + j);
            }
        }
        for (int i = 0; i <= 11; i++) {
            for (int j = 0; j <= 11; j++) {
                PUMPKIN_MODEL_PARTS2[i][j] = new ModelIdentifier(new Identifier("ignis-fatuus", "pumpkin_model_part_" + i + "_" + j), "");
            }
        }
    }

    @Override
    public void onInitializeClient() {
        for (int i = 0; i <= 11; i++) {
            for (int j = 0; j <= 11; j++) {
                JState state = new JState().add(new JVariant().put("", new JBlockModel(PUMPKIN_MODEL_PARTS[i][j].toString())));
                JModel model1 = new JModel()
                    .textures(
                        new JTextures()
                            .var("0", "ignis-fatuus:block/pumpkin_insides")
                            .var("1", "block/pumpkin_side")
                            .particle("ignis-fatuus:block/pumpkin_insides")
                    )
                    .element(
                        new JElement().from(13f - i, 13f - j, 0).to(14f - i, 14f - j, 2).rotation(
                            new JRotation(Axis.Y).angle(0f).origin(21, 21, 8)
                        )
                        .faces(
                            new JFaces()
                                .north(new JFace("1").uv(2f + i, 2f + j, 3f + i, 3f + j))
                                .south(new JFace("0").uv(0, 0, 16, 16))
                                .west(new JFace("0").uv(0, 0, 16, 16))
                                .east(new JFace("0").uv(0, 0, 16, 16))
                                .up(new JFace("0").uv(0, 0, 16, 16))
                                .down(new JFace("0").uv(0, 0, 16, 16))
                        )
                );
                RESOURCE_PACK.addBlockState(state, PUMPKIN_MODEL_PARTS2[i][j]);
                RESOURCE_PACK.addModel(model1, PUMPKIN_MODEL_PARTS[i][j]);
            }
        }
        RRPCallback.EVENT.register(a -> a.add(RESOURCE_PACK));

        ModelLoadingRegistry.INSTANCE.registerAppender((a, b) -> {
            for (int i = 0; i <= 11; i++) {
                for (int j = 0; j <= 11; j++) {
                    b.accept(PUMPKIN_MODEL_PARTS2[i][j]);
                }
            }
        });

        BlockEntityRendererRegistry.INSTANCE.register(Ignisfatuus.knifed_pumpkin_block_entity, CarvedPumpkinBlockEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(Ignisfatuus.carved_pumpkin_block, RenderLayer.getCutout());
    }
    
}
