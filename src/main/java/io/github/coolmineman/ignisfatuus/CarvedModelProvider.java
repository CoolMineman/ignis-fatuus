package io.github.coolmineman.ignisfatuus;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelResourceProvider;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.util.Identifier;

public class CarvedModelProvider implements ModelResourceProvider {
    public static final Identifier PUMPKIN_DYNAMIC = new Identifier("ignis-fatuus:block/pumpkin_dynamic");

    @Override
    public @Nullable UnbakedModel loadModelResource(Identifier resourceId, ModelProviderContext context)
            throws ModelProviderException {
        if (resourceId.equals(PUMPKIN_DYNAMIC)) {
            return new CarvedPumpkinModel();
        }
        return null;
    }
    
}
