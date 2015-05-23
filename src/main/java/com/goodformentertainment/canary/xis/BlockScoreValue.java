package com.goodformentertainment.canary.xis;

import net.canarymod.api.world.blocks.BlockType;

public final class BlockScoreValue {
	private static final int VALUE_NONE = 0;
	private static final int VALUE_DEFAULT = 1;
	private static final int VALUE_EASY = 2;
	private static final int VALUE_MODERATE = 4;
	private static final int VALUE_HARD = 8;
	private static final int VALUE_VERY_HARD = 16;
	private static final int VALUE_RARE = 32;
	private static final int VALUE_VERY_RARE = 64;
	
	public static int getPlaceValue(final BlockType type) {
		int value;
		if (type == BlockType.AcaciaSapling || type == BlockType.BirchSapling
				|| type == BlockType.DarkOakSapling || type == BlockType.JungleSapling
				|| type == BlockType.OakSapling || type == BlockType.SpruceSapling
				|| isFromBonemealGrowth(type) || isFromCropGrowth(type)) {
			// These are worth nothing to place
			value = VALUE_NONE;
		} else if (isEasy(type)) {
			return VALUE_EASY;
		} else if (isModerate(type)) {
			return VALUE_MODERATE;
		} else if (isHard(type)) {
			return VALUE_HARD;
		} else if (isVeryHard(type)) {
			return VALUE_VERY_HARD;
		} else if (isRare(type)) {
			return VALUE_RARE;
		} else if (isVeryRare(type)) {
			return VALUE_VERY_RARE;
		} else {
			value = VALUE_DEFAULT;
		}
		return value;
	}
	
	public static int getRemoveValue(final BlockType type) {
		int value;
		if (type == BlockType.Cobble || isFromTreeGrowth(type) || isFromBonemealGrowth(type)
				|| isFromCropGrowth(type)) {
			// These are worth nothing when removing
			value = VALUE_NONE;
		} else if (isEasy(type)) {
			return VALUE_EASY;
		} else if (isModerate(type)) {
			return VALUE_MODERATE;
		} else if (isHard(type)) {
			return VALUE_HARD;
		} else if (isVeryHard(type)) {
			return VALUE_VERY_HARD;
		} else if (isRare(type)) {
			return VALUE_RARE;
		} else if (isVeryRare(type)) {
			return VALUE_VERY_RARE;
		} else {
			value = VALUE_DEFAULT;
		}
		return value;
	}
	
	private static boolean isFromTreeGrowth(final BlockType type) {
		return type == BlockType.Cobble
				// Tree growth
				|| type == BlockType.AcaciaLeaves || type == BlockType.AcaciaLog
				|| type == BlockType.AcaciaLeaves || type == BlockType.AcaciaSapling
				|| type == BlockType.BirchLeaves || type == BlockType.BirchLog
				|| type == BlockType.BirchSapling || type == BlockType.DarkOakLeaves
				|| type == BlockType.DarkOakLog || type == BlockType.DarkOakSapling
				|| type == BlockType.JungleLeaves || type == BlockType.JungleLog
				|| type == BlockType.JungleSapling || type == BlockType.OakLeaves
				|| type == BlockType.OakLog || type == BlockType.OakSapling
				|| type == BlockType.SpruceLeaves || type == BlockType.SpruceLog
				|| type == BlockType.SpruceSapling;
	}
	
	private static boolean isFromBonemealGrowth(final BlockType type) {
		return type == BlockType.TallGrass || type == BlockType.DoubleGrass
				|| type == BlockType.Dandelion || type == BlockType.Poppy || type == BlockType.OxeyeDaisy
				|| type == BlockType.BrownMushroom || type == BlockType.RedMushroom
				|| type == BlockType.HugeBrownMushroom || type == BlockType.HugeRedMushroom;
	}
	
	private static boolean isFromCropGrowth(final BlockType type) {
		return type == BlockType.Reed || type == BlockType.Cactus || type == BlockType.CocoaPlant
				|| type == BlockType.Wheat || type == BlockType.Carrots || type == BlockType.Potatoes
				|| type == BlockType.Melon || type == BlockType.Pumpkin || type == BlockType.NetherWart;
	}
	
	private static boolean isEasy(final BlockType type) {
		return type == BlockType.AcaciaStairs || type == BlockType.DarkOakStairs
				|| type == BlockType.OakStairs || type == BlockType.DoubleWoodSlab
				|| type == BlockType.WoodSlab || type == BlockType.SpruceStairs
				|| type == BlockType.BirchStairs || type == BlockType.JungleStairs
				|| type == BlockType.Fence || type == BlockType.SpruceFence || type == BlockType.BirchFence
				|| type == BlockType.DarkOakFence || type == BlockType.JungleFence
				|| type == BlockType.AcaciaFence || type == BlockType.FenceGate
				|| type == BlockType.WoodenDoor || type == BlockType.SpruceDoor
				|| type == BlockType.BirchDoor || type == BlockType.JungleDoor
				|| type == BlockType.AcaciaDoor || type == BlockType.DarkOakDoor
				|| type == BlockType.Trapdoor || type == BlockType.WoodenButton
				|| type == BlockType.WoodenPressurePlate || type == BlockType.Chest
				|| type == BlockType.Torch || type == BlockType.Workbench || type == BlockType.Furnace
				|| type == BlockType.StandingSign || type == BlockType.WallSign || type == BlockType.Ladder
				|| type == BlockType.Lever || type == BlockType.DoubleStoneSlab
				|| type == BlockType.StoneSlab || type == BlockType.CobblestoneWall
				|| type == BlockType.StoneStairs;
	}
	
	private static boolean isModerate(final BlockType type) {
		return type == BlockType.Stone || type == BlockType.StoneBrick
				|| type == BlockType.StoneBrickStairs || type == BlockType.Dirt || type == BlockType.Grass
				|| type == BlockType.Farmland || type == BlockType.StonePressurePlate
				|| type == BlockType.StoneButton || type == BlockType.Tripwire;
	}
	
	private static boolean isHard(final BlockType type) {
		return type == BlockType.Sand || type == BlockType.Glass || type == BlockType.GlassPane
				|| type == BlockType.IronOre || type == BlockType.Dispenser || type == BlockType.Bed
				|| type == BlockType.StickyPiston || type == BlockType.Piston
				|| type == BlockType.RedstoneWire || type == BlockType.Rail || type == BlockType.IronDoor
				|| type == BlockType.IronBars || type == BlockType.RedstoneOre
				|| type == BlockType.RedstoneTorchOff || type == BlockType.RedstoneTorchOn
				|| type == BlockType.Cake || type == BlockType.RedstoneRepeaterOff
				|| type == BlockType.RedstoneRepeaterOn || type == BlockType.RedstoneLampOff
				|| type == BlockType.RedstoneLampOn || type == BlockType.TripwireHook
				|| type == BlockType.TrappedChest || type == BlockType.LightWeightedPressurePlate
				|| type == BlockType.RedstoneBlock || type == BlockType.IronTrapDoor
				|| type == BlockType.SlimeBlock || type == BlockType.HayBale
				|| type == BlockType.StandingBanner || type == BlockType.WallBanner
				|| type == BlockType.WhiteWool || type == BlockType.OrangeWool
				|| type == BlockType.YellowWool || type == BlockType.LimeWool || type == BlockType.PinkWool
				|| type == BlockType.BrownWool || type == BlockType.GreenWool || type == BlockType.RedWool
				|| type == BlockType.WhiteCarpet || type == BlockType.OrangeCarpet
				|| type == BlockType.YellowCarpet || type == BlockType.LimeCarpet
				|| type == BlockType.PinkCarpet || type == BlockType.BrownCarpet
				|| type == BlockType.GreenCarpet || type == BlockType.RedCarpet
				|| type == BlockType.WhiteGlass || type == BlockType.OrangeGlass
				|| type == BlockType.YellowGlass || type == BlockType.LimeGlass
				|| type == BlockType.PinkGlass || type == BlockType.BrownGlass
				|| type == BlockType.BrownGlass || type == BlockType.GreenGlass
				|| type == BlockType.RedGlass || type == BlockType.WhiteGlassPane
				|| type == BlockType.OrangeGlassPane || type == BlockType.YellowGlassPane
				|| type == BlockType.LimeGlassPane || type == BlockType.PinkGlassPane
				|| type == BlockType.BrownGlassPane || type == BlockType.BrownGlassPane
				|| type == BlockType.GreenGlassPane || type == BlockType.RedGlassPane;
	}
	
	private static boolean isVeryHard(final BlockType type) {
		return type == BlockType.Sandstone || type == BlockType.SandstoneChiseled
				|| type == BlockType.SandStoneSlab || type == BlockType.SandstoneSmooth
				|| type == BlockType.SandstoneStairs || type == BlockType.IronBlock
				|| type == BlockType.GoldOre || type == BlockType.EmeraldOre
				|| type == BlockType.DiamondOre || type == BlockType.NoteBlock
				|| type == BlockType.PoweredRail || type == BlockType.DetectorRail || type == BlockType.TNT
				|| type == BlockType.Bookshelf || type == BlockType.Jukebox || type == BlockType.Cauldron
				|| type == BlockType.Hopper || type == BlockType.ActivatorRail || type == BlockType.Dropper
				|| type == BlockType.HeavyWeightedPressurePlate || type == BlockType.MagentaWool
				|| type == BlockType.MagentaCarpet || type == BlockType.MagentaGlass
				|| type == BlockType.MagentaGlassPane || type == BlockType.LightBlueCarpet
				|| type == BlockType.LightBlueGlass || type == BlockType.LightBlueGlassPane
				|| type == BlockType.LightBlueWool || type == BlockType.LightGrayCarpet
				|| type == BlockType.LightGrayGlass || type == BlockType.LightGrayGlassPane
				|| type == BlockType.LightGrayWool || type == BlockType.GrayCarpet
				|| type == BlockType.GrayGlass || type == BlockType.GrayGlassPane
				|| type == BlockType.GrayWool || type == BlockType.CyanCarpet
				|| type == BlockType.CyanGlass || type == BlockType.CyanGlassPane
				|| type == BlockType.CyanWool || type == BlockType.PurpleCarpet
				|| type == BlockType.PurpleGlass || type == BlockType.PurpleGlassPane
				|| type == BlockType.PurpleWool || type == BlockType.BlueCarpet
				|| type == BlockType.BlueGlass || type == BlockType.BlueGlassPane
				|| type == BlockType.BlueWool || type == BlockType.BlackCarpet
				|| type == BlockType.BlackGlass || type == BlockType.BlackGlassPane
				|| type == BlockType.BlackWool;
	}
	
	private static boolean isRare(final BlockType type) {
		return type == BlockType.EnchantmentTable || type == BlockType.GoldBlock
				|| type == BlockType.EmeraldBlock || type == BlockType.DiamondBlock
				|| type == BlockType.Obsidian || type == BlockType.PackedIce || type == BlockType.Ice
				|| type == BlockType.Skull || type == BlockType.Anvil;
	}
	
	private static boolean isVeryRare(final BlockType type) {
		return type == BlockType.LapisBlock || type == BlockType.BrewingStand
				|| type == BlockType.Beacon;
	}
}
