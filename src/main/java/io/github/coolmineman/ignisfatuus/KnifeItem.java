package io.github.coolmineman.ignisfatuus;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class KnifeItem extends Item {

    public KnifeItem(Settings settings) {
        super(settings);
    }
    

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        if (state.isOf(Blocks.PUMPKIN)) {
            world.setBlockState(pos, Ignisfatuus.knifed_pumpkin_block.getDefaultState());
        } else if (state.isOf(Ignisfatuus.knifed_pumpkin_block)) {
            int progress = state.get(KnifedPumpkinBlock.knifed);
            if (progress < 3) {
                world.setBlockState(pos, state.with(KnifedPumpkinBlock.knifed, progress + 1));
            } else {
                context.getPlayer().giveItemStack(new ItemStack(Items.PUMPKIN_SEEDS));
                world.setBlockState(pos, Ignisfatuus.carved_pumpkin_block.getPlacementState(new ItemPlacementContext(context)));
            }
        } else if (state.isOf(Ignisfatuus.carved_pumpkin_block)) {
            if (world.isClient && world.getBlockEntity(pos) instanceof CarvedPumpkinBlockEntity) {
                MinecraftClient.getInstance().openScreen(new CarvingScreen(new CarvingGui((CarvedPumpkinBlockEntity) world.getBlockEntity(pos))));
            }
        } else if (state.isOf(Blocks.CARVED_PUMPKIN)) {
            world.setBlockState(pos, Ignisfatuus.carved_pumpkin_block.getDefaultState().with(HorizontalFacingBlock.FACING, state.get(HorizontalFacingBlock.FACING)));
        } else {
            return ActionResult.PASS;
        }
        return ActionResult.SUCCESS;
    }
}
