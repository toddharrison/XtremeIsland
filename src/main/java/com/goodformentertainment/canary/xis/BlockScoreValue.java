package com.goodformentertainment.canary.xis;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.canarymod.api.world.blocks.BlockType;

public class BlockScoreValue {
	private final Map<BlockType, Integer> placeValues;
	private final Map<BlockType, Integer> removeValues;
	
	public BlockScoreValue(final XConfig config) {
		placeValues = configPlaceValues(config);
		removeValues = configRemoveValues(config);
	}
	
	public int getPlaceValue(final BlockType type) {
		return placeValues.get(type);
	}
	
	public int getRemoveValue(final BlockType type) {
		return removeValues.get(type);
	}
	
	protected Map<BlockType, Integer> getPlaceValues() {
		return Collections.unmodifiableMap(placeValues);
	}
	
	protected Map<BlockType, Integer> getRemoveValues() {
		return Collections.unmodifiableMap(removeValues);
	}
	
	private Map<BlockType, Integer> configPlaceValues(final XConfig config) {
		final Map<BlockType, Integer> values = new HashMap<BlockType, Integer>();
		
		XPlugin.LOG.debug("XIS block place values:");
		final StringBuffer sb = new StringBuffer();
		for (final BlockType type : BlockType.values()) {
			final String valueType = getValueType(type);
			if (valueType != null) {
				final String valueVariant = getValueVariant(type);
				final String valueColor = getValueColor(type);
				
				int value = config.getBlockTypeValue(valueType);
				value *= config.getBlockVariantValueMultiplier(valueVariant);
				value *= config.getBlockColorValueMultiplier(valueColor);
				
				values.put(type, value);
				
				sb.append(type.getMachineName() + " = " + value + "\n");
			}
		}
		XPlugin.LOG.debug(sb.toString());
		
		return values;
	}
	
	private Map<BlockType, Integer> configRemoveValues(final XConfig config) {
		final Map<BlockType, Integer> values = new HashMap<BlockType, Integer>();
		
		XPlugin.LOG.debug("XIS block remove values:");
		final StringBuffer sb = new StringBuffer();
		final Collection<String> ignoredRemoves = config.getIgnoredBlockRemoves();
		for (final BlockType type : BlockType.values()) {
			final String valueType = getValueType(type);
			if (valueType != null && !ignoredRemoves.contains(valueType)) {
				final String valueVariant = getValueVariant(type);
				final String valueColor = getValueColor(type);
				
				int value = config.getBlockTypeValue(valueType);
				value *= config.getBlockVariantValueMultiplier(valueVariant);
				value *= config.getBlockColorValueMultiplier(valueColor);
				
				values.put(type, value);
				
				sb.append(type.getMachineName() + " = " + value + "\n");
			}
		}
		XPlugin.LOG.debug(sb.toString());
		
		return values;
	}
	
	public static final String TYPE_COBBLESTONE = "cobblestone";
	public static final String TYPE_TREE = "tree";
	public static final String TYPE_DIRT = "dirt";
	public static final String TYPE_GLASS = "glass";
	public static final String TYPE_WOOL = "wool";
	public static final String TYPE_CLAY = "clay";
	public static final String TYPE_STONE = "stone";
	public static final String TYPE_WOOD = "wood";
	public static final String TYPE_BRICK = "brick";
	public static final String TYPE_QUARTZ = "quartz";
	public static final String TYPE_SAND = "sand";
	public static final String TYPE_SANDSTONE = "sandstone";
	public static final String TYPE_COAL = "coal";
	public static final String TYPE_IRON = "iron";
	public static final String TYPE_GOLD = "gold";
	public static final String TYPE_REDSTONE = "redstone";
	public static final String TYPE_LAPIS = "lapis";
	public static final String TYPE_EMERALD = "emerald";
	public static final String TYPE_DIAMOND = "diamond";
	public static final String TYPE_OBSIDIAN = "obsidian";
	public static final String TYPE_SLIME = "slime";
	public static final String TYPE_HAY = "hay";
	public static final String TYPE_ICE = "ice";
	public static final String TYPE_BEACON = "beacon";
	public static final String TYPE_FANCY = "fancy";
	public static final String TYPE_ELECTRONICS = "electronics";
	public static final String TYPE_RAIL = "rail";
	public static final String TYPE_DECORATION = "decoration";
	public static final String TYPE_SPECIALTY = "specialty";
	
	private String getValueType(final BlockType type) {
		String valueType = null;
		
		if (type == BlockType.Cobble) {
			valueType = TYPE_COBBLESTONE;
		} else if (type == BlockType.AcaciaLeaves || type == BlockType.AcaciaLog
				|| type == BlockType.BirchLeaves || type == BlockType.BirchLog
				|| type == BlockType.DarkOakLeaves || type == BlockType.DarkOakLog
				|| type == BlockType.JungleLeaves || type == BlockType.JungleLog
				|| type == BlockType.OakLeaves || type == BlockType.OakLog
				|| type == BlockType.SpruceLeaves || type == BlockType.SpruceLog
				|| type == BlockType.HugeBrownMushroom || type == BlockType.HugeRedMushroom) {
			valueType = TYPE_TREE;
		} else if (type == BlockType.Dirt || type == BlockType.CoarseDirt || type == BlockType.Podzol
				|| type == BlockType.Grass || type == BlockType.Mycelium || type == BlockType.Farmland) {
			valueType = TYPE_DIRT;
		} else if (type == BlockType.Glass || type == BlockType.GlassPane
				|| type == BlockType.BlackGlass || type == BlockType.BlueGlass
				|| type == BlockType.BrownGlass || type == BlockType.CyanGlass
				|| type == BlockType.GrayGlass || type == BlockType.GreenGlass
				|| type == BlockType.LightBlueGlass || type == BlockType.LightGrayGlass
				|| type == BlockType.LimeGlass || type == BlockType.MagentaGlass
				|| type == BlockType.OrangeGlass || type == BlockType.PinkGlass
				|| type == BlockType.PurpleGlass || type == BlockType.RedGlass
				|| type == BlockType.WhiteGlass || type == BlockType.YellowGlass
				|| type == BlockType.BlackGlassPane || type == BlockType.BlueGlassPane
				|| type == BlockType.BrownGlassPane || type == BlockType.CyanGlassPane
				|| type == BlockType.GrayGlassPane || type == BlockType.GreenGlassPane
				|| type == BlockType.LightBlueGlassPane || type == BlockType.LightGrayGlassPane
				|| type == BlockType.LimeGlassPane || type == BlockType.MagentaGlassPane
				|| type == BlockType.OrangeGlassPane || type == BlockType.PinkGlassPane
				|| type == BlockType.PurpleGlassPane || type == BlockType.RedGlassPane
				|| type == BlockType.WhiteGlassPane || type == BlockType.YellowGlassPane) {
			valueType = TYPE_GLASS;
		} else if (type == BlockType.BlackWool || type == BlockType.BlueWool
				|| type == BlockType.BrownWool || type == BlockType.CyanWool || type == BlockType.GrayWool
				|| type == BlockType.GreenWool || type == BlockType.LightBlueWool
				|| type == BlockType.LightGrayWool || type == BlockType.LimeWool
				|| type == BlockType.MagentaWool || type == BlockType.OrangeWool
				|| type == BlockType.PinkWool || type == BlockType.PurpleWool || type == BlockType.RedWool
				|| type == BlockType.WhiteWool || type == BlockType.YellowWool
				|| type == BlockType.BlackCarpet || type == BlockType.BlueCarpet
				|| type == BlockType.BrownCarpet || type == BlockType.CyanCarpet
				|| type == BlockType.GrayCarpet || type == BlockType.GreenCarpet
				|| type == BlockType.LightBlueCarpet || type == BlockType.LightGrayCarpet
				|| type == BlockType.LimeCarpet || type == BlockType.MagentaCarpet
				|| type == BlockType.OrangeCarpet || type == BlockType.PinkCarpet
				|| type == BlockType.PurpleCarpet || type == BlockType.RedCarpet
				|| type == BlockType.WhiteCarpet || type == BlockType.YellowCarpet) {
			valueType = TYPE_WOOL;
		} else if (type == BlockType.Clay || type == BlockType.HardenedClay
				|| type == BlockType.BlackStainedClay || type == BlockType.BlueStainedClay
				|| type == BlockType.BrownStainedClay || type == BlockType.CyanStainedClay
				|| type == BlockType.GrayStainedClay || type == BlockType.GreenStainedClay
				|| type == BlockType.LightBlueStainedClay || type == BlockType.LightGrayStainedClay
				|| type == BlockType.LimeStainedClay || type == BlockType.MagentaStainedClay
				|| type == BlockType.OrangeStainedClay || type == BlockType.PinkStainedClay
				|| type == BlockType.PurpleStainedClay || type == BlockType.RedStainedClay
				|| type == BlockType.WhiteStainedClay || type == BlockType.YellowStainedClay) {
			valueType = TYPE_CLAY;
		} else if (type == BlockType.Stone || type == BlockType.Stone || type == BlockType.StoneBrick
				|| type == BlockType.StoneBricksSlab || type == BlockType.StoneBrickStairs
				|| type == BlockType.StoneButton || type == BlockType.StonePressurePlate
				|| type == BlockType.StoneSlab || type == BlockType.Andesite || type == BlockType.Diorite
				|| type == BlockType.Granite || type == BlockType.PolishedAndesite
				|| type == BlockType.PolishedDiorite || type == BlockType.PolishedGranite
				|| type == BlockType.DoubleStoneBricksSlab || type == BlockType.DoubleStoneSlab
				|| type == BlockType.MossyCobble || type == BlockType.MossyStoneBrick
				|| type == BlockType.CobblestoneWall || type == BlockType.MossyCobbleWall
				|| type == BlockType.CrackedStoneBrick || type == BlockType.Furnace
				|| type == BlockType.BurningFurnace) {
			valueType = TYPE_STONE;
		} else if (type == BlockType.AcaciaDoor || type == BlockType.AcaciaFence
				|| type == BlockType.AcaciaFenceGate || type == BlockType.AcaciaPlanks
				|| type == BlockType.AcaciaStairs || type == BlockType.AcaciaWoodSlab
				|| type == BlockType.DoubleAcaciaWoodSlab || type == BlockType.BirchDoor
				|| type == BlockType.BirchFence || type == BlockType.BirchFenceGate
				|| type == BlockType.BirchPlanks || type == BlockType.BirchStairs
				|| type == BlockType.BirchWoodSlab || type == BlockType.DoubleBirchWoodSlab
				|| type == BlockType.DarkOakDoor || type == BlockType.DarkOakFence
				|| type == BlockType.DarkOakFenceGate || type == BlockType.DarkOakPlanks
				|| type == BlockType.DarkOakStairs || type == BlockType.DarkOakWoodSlab
				|| type == BlockType.DoubleDarkOakWoodSlab || type == BlockType.JungleDoor
				|| type == BlockType.JungleFence || type == BlockType.JungleFenceGate
				|| type == BlockType.JunglePlanks || type == BlockType.JungleStairs
				|| type == BlockType.JungleWoodSlab || type == BlockType.DoubleJungleWoodSlab
				|| type == BlockType.OakDoor || type == BlockType.Fence || type == BlockType.FenceGate
				|| type == BlockType.OakPlanks || type == BlockType.OakStairs
				|| type == BlockType.OakWoodSlab || type == BlockType.DoubleOakWoodSlab
				|| type == BlockType.SpruceDoor || type == BlockType.SpruceFence
				|| type == BlockType.SpruceFenceGate || type == BlockType.SprucePlanks
				|| type == BlockType.SpruceStairs || type == BlockType.SpruceWoodSlab
				|| type == BlockType.DoubleSpruceWoodSlab || type == BlockType.WoodenButton
				|| type == BlockType.WoodenPressurePlate || type == BlockType.Lever
				|| type == BlockType.Trapdoor || type == BlockType.Workbench || type == BlockType.Chest
				|| type == BlockType.TrappedChest || type == BlockType.Torch || type == BlockType.Ladder
				|| type == BlockType.StandingSign || type == BlockType.WallSign
		
		) {
			valueType = TYPE_WOOD;
		} else if (type == BlockType.BrickBlock || type == BlockType.BrickSlab
				|| type == BlockType.BrickStairs || type == BlockType.NetherBrick
				|| type == BlockType.NetherBricksSlab || type == BlockType.NetherBrickStairs
				|| type == BlockType.NetherBrickFence) {
			valueType = TYPE_BRICK;
		} else if (type == BlockType.QuartzBlock || type == BlockType.QuartzPillarCap
				|| type == BlockType.QuartzPillarHorizontal || type == BlockType.QuartzPillarVertical
				|| type == BlockType.QuartzSlab || type == BlockType.QuartzStairs
				|| type == BlockType.ChiseledQuartzBlock) {
			valueType = TYPE_QUARTZ;
		} else if (type == BlockType.Sand || type == BlockType.RedSand) {
			valueType = TYPE_SAND;
		} else if (type == BlockType.Sandstone || type == BlockType.SandstoneChiseled
				|| type == BlockType.SandStoneSlab || type == BlockType.SandstoneSmooth
				|| type == BlockType.SandstoneStairs || type == BlockType.RedSandstone
				|| type == BlockType.RedSandstoneChiseled || type == BlockType.RedSandstoneSlab
				|| type == BlockType.RedSandstoneSmooth || type == BlockType.RedSandstoneStairs) {
			valueType = TYPE_SANDSTONE;
		} else if (type == BlockType.CoalBlock) {
			valueType = TYPE_COAL;
		} else if (type == BlockType.IronBlock || type == BlockType.IronBars
				|| type == BlockType.IronDoor || type == BlockType.IronTrapDoor
				|| type == BlockType.HeavyWeightedPressurePlate) {
			valueType = TYPE_IRON;
		} else if (type == BlockType.GoldBlock || type == BlockType.LightWeightedPressurePlate) {
			valueType = TYPE_GOLD;
		} else if (type == BlockType.RedstoneBlock) {
			valueType = TYPE_REDSTONE;
		} else if (type == BlockType.LapisBlock) {
			valueType = TYPE_LAPIS;
		} else if (type == BlockType.EmeraldBlock) {
			valueType = TYPE_EMERALD;
		} else if (type == BlockType.DiamondBlock) {
			valueType = TYPE_DIAMOND;
		} else if (type == BlockType.Obsidian) {
			valueType = TYPE_OBSIDIAN;
		} else if (type == BlockType.SlimeBlock) {
			valueType = TYPE_SLIME;
		} else if (type == BlockType.HayBale) {
			valueType = TYPE_HAY;
		} else if (type == BlockType.Ice || type == BlockType.PackedIce || type == BlockType.SnowBlock) {
			valueType = TYPE_ICE;
		} else if (type == BlockType.Beacon) {
			valueType = TYPE_BEACON;
		} else if (type == BlockType.DarkPrismarine || type == BlockType.EndStone
				|| type == BlockType.GlowStone || type == BlockType.Gravel || type == BlockType.Prismarine
				|| type == BlockType.PrismarineBricks || type == BlockType.SeaLantern
				|| type == BlockType.SoulSand || type == BlockType.Sponge) {
			valueType = TYPE_FANCY;
		} else if (type == BlockType.RedstoneTorchOff || type == BlockType.RedstoneTorchOn
				|| type == BlockType.RedstoneLampOff || type == BlockType.RedstoneLampOn
				|| type == BlockType.RedstoneRepeaterOff || type == BlockType.RedstoneRepeaterOn
				|| type == BlockType.DaylightSensor || type == BlockType.DaylightSensorInverted
				|| type == BlockType.RedstoneComparator || type == BlockType.RedstoneComparatorPowered) {
			valueType = TYPE_ELECTRONICS;
		} else if (type == BlockType.ActivatorRail || type == BlockType.DetectorRail
				|| type == BlockType.PoweredRail || type == BlockType.Rail) {
			valueType = TYPE_RAIL;
		} else if (type == BlockType.Bed || type == BlockType.JackOLantern || type == BlockType.Lilypad
				|| type == BlockType.NoteBlock || type == BlockType.StandingBanner
				|| type == BlockType.TripwireHook || type == BlockType.WallBanner
				|| type == BlockType.Bookshelf || type == BlockType.Flowerpot || type == BlockType.Skull) {
			valueType = TYPE_DECORATION;
		} else if (type == BlockType.Dispenser || type == BlockType.Dropper || type == BlockType.Piston
				|| type == BlockType.PistonExtended || type == BlockType.PistonHead
				|| type == BlockType.Cauldron || type == BlockType.Hopper || type == BlockType.StickyPiston
				|| type == BlockType.TNT || type == BlockType.Anvil || type == BlockType.BrewingStand
				|| type == BlockType.EnchantmentTable || type == BlockType.EnderChest
				|| type == BlockType.Jukebox
		
		) {
			valueType = TYPE_SPECIALTY;
		}
		
		return valueType;
	}
	
	public static final String VARIANT_ORE = "ore";
	public static final String VARIANT_SLAB = "slab";
	public static final String VARIANT_BLOCK = "block";
	public static final String VARIANT_STAIR = "stair";
	public static final String VARIANT_FENCE = "fence";
	public static final String VARIANT_CARPET = "carpet";
	public static final String VARIANT_PANE = "pane";
	public static final String VARIANT_MACHINE = "machine";
	public static final String VARIANT_BASIC = "basic";
	public static final String VARIANT_MODERATE = "moderate";
	public static final String VARIANT_ADVANCED = "advanced";
	
	private String getValueVariant(final BlockType type) {
		String valueVariant = VARIANT_BLOCK;
		
		if (type == BlockType.AcaciaWoodSlab || type == BlockType.BirchWoodSlab
				|| type == BlockType.BrickSlab || type == BlockType.CobbleSlab
				|| type == BlockType.DarkOakWoodSlab || type == BlockType.JungleWoodSlab
				|| type == BlockType.NetherBricksSlab || type == BlockType.OakWoodSlab
				|| type == BlockType.QuartzSlab || type == BlockType.RedSandstoneSlab
				|| type == BlockType.SandStoneSlab || type == BlockType.SpruceWoodSlab
				|| type == BlockType.StoneBricksSlab || type == BlockType.StoneSlab) {
			valueVariant = VARIANT_SLAB;
		} else if (type == BlockType.AcaciaStairs || type == BlockType.BirchStairs
				|| type == BlockType.BrickStairs || type == BlockType.StoneStairs
				|| type == BlockType.DarkOakStairs || type == BlockType.JungleStairs
				|| type == BlockType.NetherBrickStairs || type == BlockType.OakStairs
				|| type == BlockType.QuartzStairs || type == BlockType.RedSandstoneStairs
				|| type == BlockType.SandstoneStairs || type == BlockType.SpruceStairs
				|| type == BlockType.StoneBrickStairs) {
			valueVariant = VARIANT_STAIR;
		} else if (type == BlockType.AcaciaFence || type == BlockType.AcaciaFenceGate
				|| type == BlockType.BirchFence || type == BlockType.BirchFenceGate
				|| type == BlockType.DarkOakFence || type == BlockType.DarkOakFenceGate
				|| type == BlockType.JungleFence || type == BlockType.JungleFenceGate
				|| type == BlockType.Fence || type == BlockType.FenceGate || type == BlockType.SpruceFence
				|| type == BlockType.SpruceFenceGate || type == BlockType.CobblestoneWall
				|| type == BlockType.MossyCobbleWall || type == BlockType.NetherBrickFence) {
			valueVariant = VARIANT_FENCE;
		} else if (type == BlockType.BlackCarpet || type == BlockType.BlueCarpet
				|| type == BlockType.BrownCarpet || type == BlockType.CyanCarpet
				|| type == BlockType.GrayCarpet || type == BlockType.GreenCarpet
				|| type == BlockType.LightBlueCarpet || type == BlockType.LightGrayCarpet
				|| type == BlockType.LimeCarpet || type == BlockType.MagentaCarpet
				|| type == BlockType.OrangeCarpet || type == BlockType.PinkCarpet
				|| type == BlockType.PurpleCarpet || type == BlockType.RedCarpet
				|| type == BlockType.WhiteCarpet || type == BlockType.YellowCarpet) {
			valueVariant = VARIANT_CARPET;
		} else if (type == BlockType.GlassPane || type == BlockType.BlackGlassPane
				|| type == BlockType.BlueGlassPane || type == BlockType.BrownGlassPane
				|| type == BlockType.CyanGlassPane || type == BlockType.GrayGlassPane
				|| type == BlockType.GreenGlassPane || type == BlockType.LightBlueGlassPane
				|| type == BlockType.LightGrayGlassPane || type == BlockType.LimeGlassPane
				|| type == BlockType.MagentaGlassPane || type == BlockType.OrangeGlassPane
				|| type == BlockType.PinkGlassPane || type == BlockType.PurpleGlassPane
				|| type == BlockType.RedGlassPane || type == BlockType.WhiteGlassPane
				|| type == BlockType.YellowGlassPane) {
			valueVariant = VARIANT_PANE;
		} else if (type == BlockType.CoalOre || type == BlockType.IronOre || type == BlockType.GoldOre
				|| type == BlockType.RedstoneOre || type == BlockType.LapisOre
				|| type == BlockType.EmeraldOre || type == BlockType.DiamondOre
				|| type == BlockType.GlowingRedstoneOre) {
			valueVariant = VARIANT_ORE;
		} else if (type == BlockType.WoodenButton || type == BlockType.AcaciaDoor
				|| type == BlockType.BirchDoor || type == BlockType.DarkOakDoor
				|| type == BlockType.JungleDoor || type == BlockType.OakDoor
				|| type == BlockType.SpruceDoor || type == BlockType.WoodenPressurePlate
				|| type == BlockType.Lever || type == BlockType.StoneButton
				|| type == BlockType.StonePressurePlate || type == BlockType.IronDoor
				|| type == BlockType.IronTrapDoor || type == BlockType.Workbench
				|| type == BlockType.Furnace || type == BlockType.BurningFurnace || type == BlockType.Chest
				|| type == BlockType.TrappedChest || type == BlockType.Ladder
				|| type == BlockType.StandingSign || type == BlockType.WallSign) {
			valueVariant = VARIANT_MACHINE;
		} else if (type == BlockType.RedstoneTorchOff || type == BlockType.RedstoneTorchOn
				|| type == BlockType.Bed || type == BlockType.JackOLantern || type == BlockType.Lilypad
				|| type == BlockType.NoteBlock || type == BlockType.StandingBanner
				|| type == BlockType.TripwireHook || type == BlockType.WallBanner
				|| type == BlockType.Dispenser || type == BlockType.Dropper || type == BlockType.Piston
				|| type == BlockType.PistonExtended || type == BlockType.PistonHead) {
			valueVariant = VARIANT_BASIC;
		} else if (type == BlockType.RedstoneLampOff || type == BlockType.RedstoneLampOn
				|| type == BlockType.RedstoneRepeaterOff || type == BlockType.RedstoneRepeaterOn
				|| type == BlockType.Bookshelf || type == BlockType.Flowerpot || type == BlockType.Cauldron
				|| type == BlockType.Hopper || type == BlockType.StickyPiston || type == BlockType.TNT) {
			valueVariant = VARIANT_MODERATE;
		} else if (type == BlockType.DaylightSensor || type == BlockType.DaylightSensorInverted
				|| type == BlockType.RedstoneComparator || type == BlockType.RedstoneComparatorPowered
				|| type == BlockType.Skull || type == BlockType.Anvil || type == BlockType.BrewingStand
				|| type == BlockType.EnchantmentTable || type == BlockType.EnderChest
				|| type == BlockType.Jukebox) {
			valueVariant = VARIANT_ADVANCED;
		}
		
		return valueVariant;
	}
	
	public static final String COLOR_NONE = "none";
	public static final String COLOR_BLACK = "black";
	public static final String COLOR_BLUE = "blue";
	public static final String COLOR_BROWN = "brown";
	public static final String COLOR_CYAN = "cyan";
	public static final String COLOR_GRAY = "gray";
	public static final String COLOR_GREEN = "green";
	public static final String COLOR_LIGHT_BLUE = "light_blue";
	public static final String COLOR_LIGHT_GRAY = "light_gray";
	public static final String COLOR_LIME = "lime";
	public static final String COLOR_MAGENTA = "magenta";
	public static final String COLOR_ORANGE = "orange";
	public static final String COLOR_PINK = "pink";
	public static final String COLOR_PURPLE = "purple";
	public static final String COLOR_RED = "red";
	public static final String COLOR_WHITE = "white";
	public static final String COLOR_YELLOW = "yellow";
	
	private String getValueColor(final BlockType type) {
		String valueColor = COLOR_NONE;
		
		if (type == BlockType.BlackCarpet || type == BlockType.BlackGlass
				|| type == BlockType.BlackGlassPane || type == BlockType.BlackStainedClay
				|| type == BlockType.BlackWool) {
			valueColor = COLOR_BLACK;
		} else if (type == BlockType.BlueCarpet || type == BlockType.BlueGlass
				|| type == BlockType.BlueGlassPane || type == BlockType.BlueStainedClay
				|| type == BlockType.BlueWool) {
			valueColor = COLOR_BLUE;
		} else if (type == BlockType.BrownCarpet || type == BlockType.BrownGlass
				|| type == BlockType.BrownGlassPane || type == BlockType.BrownStainedClay
				|| type == BlockType.BrownWool) {
			valueColor = COLOR_BROWN;
		} else if (type == BlockType.CyanCarpet || type == BlockType.CyanGlass
				|| type == BlockType.CyanGlassPane || type == BlockType.CyanStainedClay
				|| type == BlockType.CyanWool) {
			valueColor = COLOR_CYAN;
		} else if (type == BlockType.GrayCarpet || type == BlockType.GrayGlass
				|| type == BlockType.GrayGlassPane || type == BlockType.GrayStainedClay
				|| type == BlockType.GrayWool) {
			valueColor = COLOR_GRAY;
		} else if (type == BlockType.GreenCarpet || type == BlockType.GreenGlass
				|| type == BlockType.GreenGlassPane || type == BlockType.GreenStainedClay
				|| type == BlockType.GreenWool) {
			valueColor = COLOR_GREEN;
		} else if (type == BlockType.LightBlueCarpet || type == BlockType.LightBlueGlass
				|| type == BlockType.LightBlueGlassPane || type == BlockType.LightBlueStainedClay
				|| type == BlockType.LightBlueWool) {
			valueColor = COLOR_LIGHT_BLUE;
		} else if (type == BlockType.LightGrayCarpet || type == BlockType.LightGrayGlass
				|| type == BlockType.LightGrayGlassPane || type == BlockType.LightGrayStainedClay
				|| type == BlockType.LightGrayWool) {
			valueColor = COLOR_LIGHT_GRAY;
		} else if (type == BlockType.LimeCarpet || type == BlockType.LimeGlass
				|| type == BlockType.LimeGlassPane || type == BlockType.LimeStainedClay
				|| type == BlockType.LimeWool) {
			valueColor = COLOR_LIME;
		} else if (type == BlockType.MagentaCarpet || type == BlockType.MagentaGlass
				|| type == BlockType.MagentaGlassPane || type == BlockType.MagentaStainedClay
				|| type == BlockType.MagentaWool) {
			valueColor = COLOR_MAGENTA;
		} else if (type == BlockType.OrangeCarpet || type == BlockType.OrangeGlass
				|| type == BlockType.OrangeGlassPane || type == BlockType.OrangeStainedClay
				|| type == BlockType.OrangeWool) {
			valueColor = COLOR_ORANGE;
		} else if (type == BlockType.PinkCarpet || type == BlockType.PinkGlass
				|| type == BlockType.PinkGlassPane || type == BlockType.PinkStainedClay
				|| type == BlockType.PinkWool) {
			valueColor = COLOR_PINK;
		} else if (type == BlockType.PurpleCarpet || type == BlockType.PurpleGlass
				|| type == BlockType.PurpleGlassPane || type == BlockType.PurpleStainedClay
				|| type == BlockType.PurpleWool) {
			valueColor = COLOR_PURPLE;
		} else if (type == BlockType.RedCarpet || type == BlockType.RedGlass
				|| type == BlockType.RedGlassPane || type == BlockType.RedStainedClay
				|| type == BlockType.RedWool) {
			valueColor = COLOR_RED;
		} else if (type == BlockType.WhiteCarpet || type == BlockType.WhiteGlass
				|| type == BlockType.WhiteGlassPane || type == BlockType.WhiteStainedClay
				|| type == BlockType.WhiteWool) {
			valueColor = COLOR_WHITE;
		} else if (type == BlockType.YellowCarpet || type == BlockType.YellowGlass
				|| type == BlockType.YellowGlassPane || type == BlockType.YellowStainedClay
				|| type == BlockType.YellowWool) {
			valueColor = COLOR_YELLOW;
		}
		
		return valueColor;
	}
}
