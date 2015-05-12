package com.goodformentertainment.canary.xis;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.blocks.Chest;
import net.visualillusionsent.utils.TaskManager;

public class XIslandManager {
	private final XConfig config;
	
	private final Set<UUID> islandsDeleting;
	
	public XIslandManager(final XConfig config) {
		this.config = config;
		islandsDeleting = Collections.synchronizedSet(new HashSet<UUID>());
	}
	
	public void generateIsland(final World world, final int x, int y, final int z) {
		XPlugin.LOG.info("Generating island at " + x + ":" + y + ":" + z);
		
		// Layer 1
		world.setBlockAt(x, y, z, BlockType.Stone);
		
		// Layer 2
		y++;
		world.setBlockAt(x, y, z, BlockType.Sand);
		world.setBlockAt(x + 1, y, z, BlockType.Dirt);
		world.setBlockAt(x, y, z + 1, BlockType.Dirt);
		world.setBlockAt(x - 1, y, z, BlockType.Dirt);
		world.setBlockAt(x, y, z - 1, BlockType.Dirt);
		
		// Layer 3
		y++;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				world.setBlockAt(x + i, y, z + j, BlockType.Dirt);
			}
		}
		world.setBlockAt(x, y, z, BlockType.Sand);
		world.setBlockAt(x + 2, y, z, BlockType.Dirt);
		world.setBlockAt(x - 2, y, z, BlockType.Dirt);
		world.setBlockAt(x, y, z + 2, BlockType.Dirt);
		world.setBlockAt(x, y, z - 2, BlockType.Dirt);
		
		// Layer 4
		y++;
		for (int i = -2; i < 3; i++) {
			for (int j = -2; j < 3; j++) {
				world.setBlockAt(x + i, y, z + j, BlockType.Dirt);
			}
		}
		world.setBlockAt(x, y, z, BlockType.Sand);
		world.setBlockAt(x + 3, y, z, BlockType.Dirt);
		world.setBlockAt(x - 3, y, z, BlockType.Dirt);
		world.setBlockAt(x, y, z + 3, BlockType.Dirt);
		world.setBlockAt(x, y, z - 3, BlockType.Dirt);
		
		// Layer 5
		y++;
		for (int i = -3; i < 4; i++) {
			for (int j = -3; j < 4; j++) {
				world.setBlockAt(x + i, y, z + j, BlockType.Grass);
			}
		}
		world.setBlockAt(x + 3, y, z + 3, BlockType.Air);
		world.setBlockAt(x + 3, y, z - 3, BlockType.Air);
		world.setBlockAt(x - 3, y, z + 3, BlockType.Air);
		world.setBlockAt(x - 3, y, z - 3, BlockType.Air);
		
		// Layer 6
		y++;
		
		// Create starting chest
		final Block block = world.getBlockAt(x - 1, y, z);
		block.setType(BlockType.Chest);
		world.setBlock(block);
		final Chest chest = (Chest) block.getTileEntity();
		chest.clearContents();
		chest.addItem(ItemType.MelonSlice);
		chest.addItem(ItemType.Ice, 2);
		
		// Create starting tree
		world.setBlockAt(x, y, z, BlockType.OakLog);
		
		// Layer 7
		y++;
		world.setBlockAt(x, y, z, BlockType.OakLog);
		
		// Layer 8
		y++;
		world.setBlockAt(x, y, z, BlockType.OakLog);
		
		// Layer 9
		y++;
		for (int i = -2; i < 3; i++) {
			for (int j = -2; j < 3; j++) {
				world.setBlockAt(x + i, y, z + j, BlockType.OakLeaves);
			}
		}
		world.setBlockAt(x, y, z, BlockType.OakLog);
		world.setBlockAt(x + 2, y, z + 2, BlockType.Air);
		world.setBlockAt(x + 2, y, z - 2, BlockType.Air);
		world.setBlockAt(x - 2, y, z + 2, BlockType.Air);
		world.setBlockAt(x - 2, y, z - 2, BlockType.Air);
		
		// Layer 10
		y++;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				world.setBlockAt(x + i, y, z + j, BlockType.OakLeaves);
			}
		}
		world.setBlockAt(x, y, z, BlockType.OakLog);
		world.setBlockAt(x + 2, y, z, BlockType.OakLeaves);
		world.setBlockAt(x - 2, y, z, BlockType.OakLeaves);
		world.setBlockAt(x, y, z + 2, BlockType.OakLeaves);
		world.setBlockAt(x, y, z - 2, BlockType.OakLeaves);
		
		// Layer 11
		y++;
		world.setBlockAt(x, y, z, BlockType.OakLog);
		world.setBlockAt(x + 1, y, z, BlockType.OakLeaves);
		world.setBlockAt(x - 1, y, z, BlockType.OakLeaves);
		world.setBlockAt(x, y, z + 1, BlockType.OakLeaves);
		world.setBlockAt(x, y, z - 1, BlockType.OakLeaves);
		
		// Layer 12
		y++;
		world.setBlockAt(x, y, z, BlockType.OakLeaves);
	}
	
	public boolean isClearingIsland(final Player player) {
		return islandsDeleting.contains(player.getUUID());
	}
	
	public void clearIsland(final World world, final Player player, final int islandId) {
		XPlugin.LOG.info("Deleting island " + islandId);
		islandsDeleting.add(player.getUUID());
		
		TaskManager.submitTask(new Runnable() {
			@Override
			public void run() {
				final int maxSize = config.getMaxSize();
				final int centerX = islandId * maxSize;
				final int centerZ = 0;
				
				for (int x = maxSize / -2; x <= maxSize / 2; x++) {
					for (int y = 0; y <= 255; y++) {
						for (int z = maxSize / -2; z <= maxSize / 2; z++) {
							final Block block = world.getBlockAt(centerX + x, y, centerZ + z);
							if (block.getTileEntity() instanceof Inventory) {
								final Inventory inv = (Inventory) block.getTileEntity();
								inv.clearContents();
							}
							world.setBlockAt(centerX + x, y, centerZ + z, BlockType.Air);
						}
					}
				}
				
				// Deletion completion
				XPlugin.LOG.info("Island " + islandId + " deleted");
				islandsDeleting.remove(player.getUUID());
			}
		});
	}
}
