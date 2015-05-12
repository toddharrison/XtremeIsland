package com.goodformentertainment.canary.xis;

import java.util.HashMap;
import java.util.Map;

import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.config.Configuration;
import net.visualillusionsent.utils.PropertiesFile;

public class XConfig {
	private final PropertiesFile cfg;
	
	public XConfig(final XPlugin plugin) {
		cfg = Configuration.getPluginConfig(plugin);
		// cfg.save();
	}
	
	public String getWorldName() {
		return cfg.getString("worldName", "xis");
	}
	
	public int getMaxSize() {
		return cfg.getInt("maxSize", 100);
	}
	
	public int getHeight() {
		return cfg.getInt("islandHeight", 64);
	}
	
	private static final int BLOCK_TRIVIAL = 1;
	private static final int BLOCK_VERY_EASY = 2;
	private static final int BLOCK_EASY = 3;
	private static final int BLOCK_MODERATE = 4;
	
	// private static final int BLOCK_VERY_EASY = 1;
	
	// TODO different block values
	public void foo() {
		final Map<BlockType, Integer> bar = new HashMap<BlockType, Integer>();
		
		bar.put(BlockType.Cobble, BLOCK_TRIVIAL);
		bar.put(BlockType.AcaciaLeaves, BLOCK_TRIVIAL);
		bar.put(BlockType.AcaciaLog, BLOCK_TRIVIAL);
		bar.put(BlockType.AcaciaWood, BLOCK_TRIVIAL);
		bar.put(BlockType.BirchLeaves, BLOCK_TRIVIAL);
		bar.put(BlockType.BirchLog, BLOCK_TRIVIAL);
		bar.put(BlockType.BirchWood, BLOCK_TRIVIAL);
		bar.put(BlockType.DarkOakLeaves, BLOCK_TRIVIAL);
		bar.put(BlockType.DarkOakLog, BLOCK_TRIVIAL);
		bar.put(BlockType.DarkOakWood, BLOCK_TRIVIAL);
		bar.put(BlockType.JungleLeaves, BLOCK_TRIVIAL);
		bar.put(BlockType.JungleLog, BLOCK_TRIVIAL);
		bar.put(BlockType.JungleWood, BLOCK_TRIVIAL);
		bar.put(BlockType.OakLeaves, BLOCK_TRIVIAL);
		bar.put(BlockType.OakLog, BLOCK_TRIVIAL);
		bar.put(BlockType.OakWood, BLOCK_TRIVIAL);
		bar.put(BlockType.PineLeaves, BLOCK_TRIVIAL);
		bar.put(BlockType.PineLog, BLOCK_TRIVIAL);
		bar.put(BlockType.SpruceWood, BLOCK_TRIVIAL);
		
		bar.put(BlockType.AcaciaStairs, BLOCK_VERY_EASY);
		bar.put(BlockType.DoubleAcaciaWoodSlab, BLOCK_VERY_EASY);
		bar.put(BlockType.AcaciaWoodSlab, BLOCK_VERY_EASY);
		bar.put(BlockType.BirchWoodStair, BLOCK_VERY_EASY);
		bar.put(BlockType.DoubleBirchWoodSlab, BLOCK_VERY_EASY);
		bar.put(BlockType.BirchWoodSlab, BLOCK_VERY_EASY);
		bar.put(BlockType.DarkOakStairs, BLOCK_VERY_EASY);
		bar.put(BlockType.DoubleDarkOakWoodSlab, BLOCK_VERY_EASY);
		bar.put(BlockType.DarkOakWoodSlab, BLOCK_VERY_EASY);
		bar.put(BlockType.JungleWoodStair, BLOCK_VERY_EASY);
		bar.put(BlockType.DoubleJungleWoodSlab, BLOCK_VERY_EASY);
		bar.put(BlockType.JungleWoodSlab, BLOCK_VERY_EASY);
		bar.put(BlockType.WoodenStair, BLOCK_VERY_EASY);
		bar.put(BlockType.DoubleOakWoodSlab, BLOCK_VERY_EASY);
		bar.put(BlockType.OakWoodSlab, BLOCK_VERY_EASY);
		bar.put(BlockType.PineWoodStair, BLOCK_VERY_EASY);
		bar.put(BlockType.DoubleSpruceWoodSlab, BLOCK_VERY_EASY);
		bar.put(BlockType.SpruceWoodSlab, BLOCK_VERY_EASY);
		bar.put(BlockType.Fence, BLOCK_VERY_EASY);
		bar.put(BlockType.FenceGate, BLOCK_VERY_EASY);
		bar.put(BlockType.WoodenDoor, BLOCK_VERY_EASY);
		bar.put(BlockType.Trapdoor, BLOCK_VERY_EASY);
		bar.put(BlockType.WoodenButton, BLOCK_VERY_EASY);
		bar.put(BlockType.WoodPlate, BLOCK_VERY_EASY);
		bar.put(BlockType.Chest, BLOCK_VERY_EASY);
		bar.put(BlockType.Torch, BLOCK_VERY_EASY);
		bar.put(BlockType.Workbench, BLOCK_VERY_EASY);
		bar.put(BlockType.Furnace, BLOCK_VERY_EASY);
		bar.put(BlockType.BurningFurnace, BLOCK_VERY_EASY);
		bar.put(BlockType.SignPost, BLOCK_VERY_EASY);
		bar.put(BlockType.WallSign, BLOCK_VERY_EASY);
		bar.put(BlockType.Ladder, BLOCK_VERY_EASY);
		bar.put(BlockType.Lever, BLOCK_VERY_EASY);
		bar.put(BlockType.CobblestoneWall, BLOCK_VERY_EASY);
		bar.put(BlockType.CobbleStair, BLOCK_VERY_EASY);
		bar.put(BlockType.DoubleCobbleSlab, BLOCK_VERY_EASY);
		bar.put(BlockType.CobbleSlab, BLOCK_VERY_EASY);
		
		bar.put(BlockType.Stone, BLOCK_EASY);
		bar.put(BlockType.StoneSlab, BLOCK_EASY);
		bar.put(BlockType.DoubleStoneSlab, BLOCK_EASY);
		bar.put(BlockType.OrnateStoneSlab, BLOCK_EASY);
		bar.put(BlockType.DoubleOrnateStoneSlab, BLOCK_EASY);
		bar.put(BlockType.StoneBrick, BLOCK_EASY);
		bar.put(BlockType.StoneBrickStair, BLOCK_EASY);
		bar.put(BlockType.StoneBricksSlab, BLOCK_EASY);
		bar.put(BlockType.DoubleStoneBricksSlab, BLOCK_EASY);
		bar.put(BlockType.Dirt, BLOCK_EASY);
		bar.put(BlockType.Soil, BLOCK_EASY);
		bar.put(BlockType.Grass, BLOCK_EASY);
		bar.put(BlockType.StonePlate, BLOCK_EASY);
		bar.put(BlockType.StoneButton, BLOCK_EASY);
		bar.put(BlockType.Tripwire, BLOCK_EASY);
		
		bar.put(BlockType.Sand, BLOCK_MODERATE);
		bar.put(BlockType.Glass, BLOCK_MODERATE);
		bar.put(BlockType.GlassPane, BLOCK_MODERATE);
		bar.put(BlockType.IronOre, BLOCK_MODERATE);
		bar.put(BlockType.Dispenser, BLOCK_MODERATE);
		bar.put(BlockType.BedBlock, BLOCK_MODERATE);
		bar.put(BlockType.StickyPiston, BLOCK_MODERATE);
		bar.put(BlockType.Piston, BLOCK_MODERATE);
		// bar.put(BlockType., BLOCK_MODERATE);
		// bar.put(BlockType., BLOCK_MODERATE);
		// bar.put(BlockType., BLOCK_MODERATE);
		// bar.put(BlockType., BLOCK_MODERATE);
		// bar.put(BlockType., BLOCK_MODERATE);
		// bar.put(BlockType., BLOCK_MODERATE);
		// bar.put(BlockType., BLOCK_MODERATE);
		// bar.put(BlockType., BLOCK_MODERATE);
		// bar.put(BlockType., BLOCK_MODERATE);
		// bar.put(BlockType., BLOCK_MODERATE);
		
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		// bar.put(BlockType., );
		
	}
}
