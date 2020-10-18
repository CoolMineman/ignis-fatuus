package io.github.coolmineman.ignisfatuus;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AHHHHHH {
    public static void aHHHHHHHH(BlockPos pos, World world) {
        MinecraftClient.getInstance().openScreen(new CarvingScreen(new CarvingGui((CarvedPumpkinBlockEntity) world.getBlockEntity(pos))));
    }
}
