package com.goodformentertainment.canary.xis;

import com.goodformentertainment.canary.zown.api.Point;
import net.canarymod.api.entity.living.animal.EntityAnimal;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.entity.living.monster.EntityMob;
import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.blocks.Chest;
import net.visualillusionsent.utils.TaskManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class XIslandManager {
    // TODO make configurable
    public static final int xOffset = 5000;
    public static final int zOffset = 5000;

    private final XConfig config;

    private final Set<UUID> islandsDeleting;

    public XIslandManager(final XConfig config) {
        this.config = config;
        islandsDeleting = Collections.synchronizedSet(new HashSet<UUID>());
    }

    public Point getIslandSpiralLocation(final int n) {
        final Point pos = new Point(0, 0, 0);

        double r = Math.floor((Math.sqrt(n + 1) - 1) / 2) + 1;
        double p = (8 * r * (r - 1)) / 2;
        double en = r * 2;
        double a = (1 + n - p) % (r * 8);
        switch ((int)Math.floor(a / (r * 2))) {
            case 0:
                pos.x = (int) (a - r);
                pos.z = (int) (-r);
                break;
            case 1:
                pos.x = (int) (r);
                pos.z = (int) ((a % en) - r);
                break;
            case 2:
                pos.x = (int) (r - (a % en));
                pos.z = (int) (r);
                break;
            case 3:
                pos.x = (int) (-r);
                pos.z = (int) (r - (a % en));
                break;
        }

        return pos;
    }

    public void generateIsland(final World world, final int x, int y, final int z) {
        XPlugin.LOG.debug("Generating island at " + x + ":" + y + ":" + z);

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
        XPlugin.LOG.debug("Deleting island " + islandId);
        islandsDeleting.add(player.getUUID());

        TaskManager.submitTask(new Runnable() {
            @Override
            public void run() {
                final int maxSize = config.getMaxSize();

                // TODO combine into common method in IslandManager
                final Point islandRelativePoint = getIslandSpiralLocation(islandId - 2);

                // Get island dimensions
                final int minX = (islandRelativePoint.x * maxSize + xOffset) - (maxSize / 2);
                final int maxX = minX + maxSize;
                final int minZ = (islandRelativePoint.z * maxSize + zOffset) - (maxSize / 2);
                final int maxZ = minZ + maxSize;

                // Remove all animals
                for (final EntityAnimal animal : world.getAnimalList()) {
                    final int blockX = animal.getLocation().getBlockX();
                    final int blockZ = animal.getLocation().getBlockZ();
                    if (blockX >= minX && blockX <= maxX && blockZ >= minZ && blockZ <= maxZ) {
                        animal.destroy();
                    }
                }

                // Remove all mobs
                for (final EntityMob mob : world.getMobList()) {
                    final int blockX = mob.getLocation().getBlockX();
                    final int blockZ = mob.getLocation().getBlockZ();
                    if (blockX >= minX && blockX <= maxX && blockZ >= minZ && blockZ <= maxZ) {
                        mob.destroy();
                    }
                }

                // Set all blocks to air
                for (int x = minX; x <= maxX; x++) {
                    for (int y = 0; y <= 255; y++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            final Block block = world.getBlockAt(x, y, z);
                            if (block.getTileEntity() instanceof Inventory) {
                                final Inventory inv = (Inventory) block.getTileEntity();
                                inv.clearContents();
                            }
                            world.setBlockAt(x, y, z, BlockType.Air);
                        }
                    }
                }

                // Deletion completion
                XPlugin.LOG.debug("Island " + islandId + " deleted");
                islandsDeleting.remove(player.getUUID());
            }
        });
    }
}
