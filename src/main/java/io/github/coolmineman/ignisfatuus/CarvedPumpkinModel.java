package io.github.coolmineman.ignisfatuus;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;

import grondag.frex.api.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext.QuadTransform;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.BlockRenderView;

public class CarvedPumpkinModel implements UnbakedModel, BakedModel, FabricBakedModel {
    private static final boolean canvas = RendererAccess.INSTANCE.getRenderer().getClass().getName().equals("grondag.canvas.apiimpl.Canvas");

    RenderMaterial canvasmaterial;

    @Override
    public Collection<Identifier> getModelDependencies() {
        return Collections.emptyList();
    }

    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter,
            Set<Pair<String, String>> unresolvedTextureReferences) {
        return Collections.emptyList();
    }

    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter,
            ModelBakeSettings rotationContainer, Identifier modelId) {
        
        if (canvas) {
            Renderer canvasInstance = (Renderer) RendererAccess.INSTANCE.getRenderer();
            canvasmaterial = canvasInstance.materialFinder().shader(new Identifier("canvas:shaders/material/default.vert"), new Identifier("ignis-fatuus:shaders/material/ultra_warm_glow.frag")).find();
        }
        return this;
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction face, Random random) {
        return null;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean hasDepth() {
        return false;
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getSprite() {
        return null;
    }

    @Override
    public ModelTransformation getTransformation() {
        return null;
    }

    @Override
    public ModelOverrideList getOverrides() {
        return null;
    }

    //Important Stuff Here

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        float rot = 0f;
        switch (state.get(HorizontalFacingBlock.FACING)) {
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
        Quaternion rotate = Vec3f.POSITIVE_Y.getDegreesQuaternion(rot);
        QuadTransform transform = mv -> {
            Vec3f tmp = new Vec3f();

            for (int i = 0; i < 4; i++) {
                // Transform the position (center of rotation is 0.5, 0.5, 0.5)
                mv.copyPos(i, tmp);
                tmp.add(-0.5f, -0.5f, -0.5f);
                tmp.rotate(rotate);
                tmp.add(0.5f, 0.5f, 0.5f);
                mv.pos(i, tmp);

                // Transform the normal
                if (mv.hasNormal(i)) {
                    mv.copyNormal(i, tmp);
                    tmp.rotate(rotate);
                    mv.normal(i, tmp);
                }
            }

            mv.nominalFace(state.get(HorizontalFacingBlock.FACING));
            return true;
        };
        context.pushTransform(transform);
        Object carvedAreaObject = ((RenderAttachedBlockView) blockView).getBlockEntityRenderAttachment(pos);
        if (carvedAreaObject instanceof boolean[][]) {
            boolean[][] carvedArea = (boolean[][]) carvedAreaObject;
            for (int i = 0; i <= 11; i++) {
                for (int j = 0; j <= 11; j++) {
                    if (!carvedArea[i][j]) {
                        context.fallbackConsumer().accept(MinecraftClient.getInstance().getBakedModelManager().getModel(IgnisfatuusClient.PUMPKIN_MODEL_PARTS2[i][j]));
                    }
                }
            }
        }
        context.popTransform();

        if (canvas) {
            context.pushTransform(mv -> {
                mv.material(canvasmaterial);
                return true;
            });
        }
        context.pushTransform(mv -> {
            Vec3f tmp = new Vec3f();
            for (int i = 0; i < 4; i++) {
                mv.copyPos(i, tmp);
                tmp.add(-0.5f, -0.5f, -0.5f);
                tmp.transform(Matrix3f.scale(0.5f, 0.5f, 0.5f));
                tmp.add(0.5f, 0.5f, 0.5f);
                tmp.add(0, (1f / 16f) - 0.25f, 0);
                mv.pos(i, tmp);
            }
            return true;
        });
        if (Boolean.TRUE.equals(state.get(CarvedPumpkinBlock.torch))) {
            context.fallbackConsumer().accept(MinecraftClient.getInstance().getBakedModelManager().getModel(new ModelIdentifier(new Identifier("minecraft", "torch"), "")));
        }
        context.popTransform();
        context.pushTransform(transform);
        if (canvas && Boolean.TRUE.equals(state.get(CarvedPumpkinBlock.lid)) && Boolean.TRUE.equals(state.get(CarvedPumpkinBlock.torch))) {
            context.fallbackConsumer().accept(MinecraftClient.getInstance().getBakedModelManager().getModel(IgnisfatuusClient.PUMPKIN_GLOW));
        }
        context.popTransform();
        if (canvas) context.popTransform();

    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        //No Item
    }
    
}
