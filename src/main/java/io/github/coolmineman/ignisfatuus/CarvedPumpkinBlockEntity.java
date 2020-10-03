package io.github.coolmineman.ignisfatuus;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class CarvedPumpkinBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    private boolean[][] carved_area = new boolean[12][12];

    public CarvedPumpkinBlockEntity() {
        super(Ignisfatuus.knifed_pumpkin_block_entity);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        ListTag list = new ListTag();
        for (int i = 0; i <= 11; i++) {
            ListTag innerlist = new ListTag();
            for (int j = 0; j <= 11; j++) {
                innerlist.add(ByteTag.of(carved_area[i][j]));
            }
            list.add(innerlist);
        }
        tag.put("carved_area", list);
        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        if (tag.contains("carved_area")) {
            ListTag carvedListTag = tag.getList("carved_area", 9);
            for (int i = 0; i <= 11; i++) {
                ListTag carvedListTagInner = carvedListTag.getList(i);
                for (int j = 0; j <= 11; j++) {
                    carved_area[i][j] = ((ByteTag)carvedListTagInner.get(j)).getByte() == 1;
                }
            }
        }
    }

    public boolean[][] getCarved_area() {
        return carved_area;
    }

    public void setCarved(int x, int y) {
        carved_area[x][y] = true;
        sync();
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        fromTag(null, tag);
        MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return toTag(tag);
    }
}
