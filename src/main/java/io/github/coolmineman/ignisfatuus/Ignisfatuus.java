package io.github.coolmineman.ignisfatuus;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class Ignisfatuus implements ModInitializer {
	public static KnifedPumpkinBlock knifed_pumpkin_block;
	public static BlockEntityType<CarvedPumpkinBlockEntity> knifed_pumpkin_block_entity;
	public static CarvedPumpkinBlock carved_pumpkin_block;
	public static KnifeItem knife_item;
	public static final Identifier CARVE_PACKET_ID = new Identifier("ignis-fatuus", "carve");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		knifed_pumpkin_block = Registry.register(Registry.BLOCK, new Identifier("ignis-fatuus", "knifed_pumpkin_block"), new KnifedPumpkinBlock(FabricBlockSettings.copyOf(Blocks.PUMPKIN)));
		carved_pumpkin_block = Registry.register(Registry.BLOCK, new Identifier("ignis-fatuus", "carved_pumpkin_block"), new CarvedPumpkinBlock(FabricBlockSettings.copyOf(Blocks.PUMPKIN).nonOpaque().luminance(bs -> {
			return bs.get(CarvedPumpkinBlock.torch) ? 5 : 0;
		})));
		knifed_pumpkin_block_entity = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ignis-fatuus:carved_pumpkin_block_entity", FabricBlockEntityTypeBuilder.create(CarvedPumpkinBlockEntity::new, carved_pumpkin_block).build(null));
		knife_item = Registry.register(Registry.ITEM, new Identifier("ignis-fatuus", "knife"), new KnifeItem(new FabricItemSettings().group(ItemGroup.TOOLS)));

		ServerSidePacketRegistry.INSTANCE.register(CARVE_PACKET_ID, (packetContext, attachedData) -> {
            // Get the BlockPos we put earlier in the IO thread
			BlockPos pos = attachedData.readBlockPos();
			int x = attachedData.readInt();
			int y = attachedData.readInt();
            packetContext.getTaskQueue().execute(() -> {
                // Execute on the main thread
 
                // ALWAYS validate that the information received is valid in a C2S packet!
                if(packetContext.getPlayer().getBlockPos().getSquaredDistance(pos) < 25){
                    // Turn to diamond
					BlockEntity e = packetContext.getPlayer().world.getBlockEntity(pos);
					if (e instanceof CarvedPumpkinBlockEntity && x <= 11 && y <= 11) {
						((CarvedPumpkinBlockEntity)e).setCarved(x, y);
					}
                }
 
            });
        });

		System.out.println("Hello Fabric world!");
	}
}
