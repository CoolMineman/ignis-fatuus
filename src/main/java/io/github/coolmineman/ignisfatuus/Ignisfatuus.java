package io.github.coolmineman.ignisfatuus;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Ignisfatuus implements ModInitializer {
	public static KnifedPumpkinBlock knifed_pumpkin_block;
	public static KnifeItem knife_item;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		knifed_pumpkin_block = Registry.register(Registry.BLOCK, new Identifier("ignis-fatuus", "knifed_pumpkin_block"), new KnifedPumpkinBlock(FabricBlockSettings.copyOf(Blocks.PUMPKIN)));
		knife_item = Registry.register(Registry.ITEM, new Identifier("ignis-fatuus", "knife"), new KnifeItem(new FabricItemSettings()));
		System.out.println("Hello Fabric world!");
	}
}
