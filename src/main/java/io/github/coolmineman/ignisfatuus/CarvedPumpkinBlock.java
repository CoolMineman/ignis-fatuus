package io.github.coolmineman.ignisfatuus;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CarvedPumpkinBlock extends HorizontalFacingBlock implements BlockEntityProvider {
    public static final BooleanProperty lid = BooleanProperty.of("lid");
    public static final BooleanProperty torch = BooleanProperty.of("torch");

    protected CarvedPumpkinBlock(Settings settings) {
        super(settings);
        setDefaultState(
                getStateManager().getDefaultState().with(lid, false).with(torch, false).with(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return ctx.getPlayerLookDirection().getAxis() == Axis.Y ? this.getDefaultState() : this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        if (player.getStackInHand(hand).getItem().equals(Items.TORCH) && state.get(torch).equals(false)) {
            world.setBlockState(pos, state.with(torch, true));
            player.getStackInHand(hand).decrement(1);
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
        builder.add(lid);
        builder.add(torch);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new CarvedPumpkinBlockEntity();
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

}
