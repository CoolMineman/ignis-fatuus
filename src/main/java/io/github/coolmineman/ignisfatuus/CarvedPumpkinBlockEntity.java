package io.github.coolmineman.ignisfatuus;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;

public class CarvedPumpkinBlockEntity extends BlockEntity implements BlockEntityClientSerializable, RenderAttachmentBlockEntity {
    private boolean[][] carved_area = new boolean[12][12];

    public CarvedPumpkinBlockEntity(BlockPos pos, BlockState state) {
        super(Ignisfatuus.knifed_pumpkin_block_entity, pos, state);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        NbtList list = new NbtList();
        for (int i = 0; i <= 11; i++) {
            NbtList innerlist = new NbtList();
            for (int j = 0; j <= 11; j++) {
                innerlist.add(NbtByte.of(carved_area[i][j]));
            }
            list.add(innerlist);
        }
        tag.put("carved_area", list);
        return tag;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        if (tag.contains("carved_area")) {
            boolean[][] new_carved_area = new boolean[12][12];
            NbtList carvedListTag = tag.getList("carved_area", 9);
            for (int i = 0; i <= 11; i++) {
                NbtList carvedListTagInner = carvedListTag.getList(i);
                for (int j = 0; j <= 11; j++) {
                    new_carved_area[i][j] = ((NbtByte)carvedListTagInner.get(j)).byteValue() == 1;
                }
            }
            carved_area = new_carved_area;
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
    public void fromClientTag(NbtCompound tag) {
        readNbt(tag);
        MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public NbtCompound toClientTag(NbtCompound tag) {
        return writeNbt(tag);
    }

	@Override
	public @Nullable Object getRenderAttachmentData() {
		return getCarved_area();
	}
}
