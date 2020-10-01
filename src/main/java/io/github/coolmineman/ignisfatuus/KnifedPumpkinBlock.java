package io.github.coolmineman.ignisfatuus;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PumpkinBlock;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;

public class KnifedPumpkinBlock extends PumpkinBlock {
    public static final IntProperty knifed = IntProperty.of("knifed", 1, 3);

    protected KnifedPumpkinBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(knifed, 1));
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(knifed);
    }

}
