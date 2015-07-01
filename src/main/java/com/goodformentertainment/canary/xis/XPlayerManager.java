package com.goodformentertainment.canary.xis;

import com.goodformentertainment.canary.playerstate.hook.WorldDeathHook;
import com.goodformentertainment.canary.playerstate.hook.WorldEnterHook;
import com.goodformentertainment.canary.playerstate.hook.WorldExitHook;
import com.goodformentertainment.canary.xis.dao.XPlayer;
import com.goodformentertainment.canary.zown.Flag;
import com.goodformentertainment.canary.zown.api.IConfiguration;
import com.goodformentertainment.canary.zown.api.IZown;
import com.goodformentertainment.canary.zown.api.IZownManager;
import com.goodformentertainment.canary.zown.api.Point;
import com.goodformentertainment.canary.zown.api.impl.Tree;
import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.api.world.World;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.position.Location;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.HealthChangeHook;
import net.canarymod.hook.player.ItemUseHook;
import net.canarymod.hook.player.PlayerDeathHook;
import net.canarymod.plugin.PluginListener;
import net.canarymod.plugin.Priority;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class XPlayerManager implements PluginListener {
    private final XConfig config;
    private final XWorldManager worldManager;
    private final XIslandManager islandManager;
    private final XChallengeManager challengeManager;
    private final Map<String, XPlayer> players;
    private final Collection<String> deadPlayers;

    private final IZownManager zownManager;

    public XPlayerManager(final XConfig config, final XWorldManager worldManager,
                          final XIslandManager islandManager, final XChallengeManager challengeManager,
                          final IZownManager zownManager) {
        this.config = config;
        this.worldManager = worldManager;
        this.islandManager = islandManager;
        this.challengeManager = challengeManager;
        this.challengeManager.setPlayerManager(this);
        this.zownManager = zownManager;
        deadPlayers = Collections.synchronizedCollection(new HashSet<String>());
        players = new HashMap<String, XPlayer>();
    }

    public Set<String> getActivePlayerIds() {
        return players.keySet();
    }

    public XPlayer getXPlayer(final Player player) {
        XPlayer xPlayer = null;
        if (player != null) {
            xPlayer = players.get(player.getUUIDString());
        }
        return xPlayer;
    }

    public Location getIslandLocation(final Player player) throws DatabaseReadException,
            DatabaseWriteException {
        XPlugin.LOG.debug("GETTING ISLAND LOCATION");

        final World world = worldManager.getWorld();
        final XPlayer xPlayer = addPlayer(player);
        Location location = xPlayer.getLocation();
        if (location == null) {
            XPlugin.LOG.debug("CREATING ISLAND");

            // Spiral algorithm for island placement
            final Point islandRelativePoint = islandManager.getIslandSpiralLocation(xPlayer.islandId);
            final int x = islandRelativePoint.x * config.getMaxSize();
            final int y = config.getHeight();
            final int z = islandRelativePoint.z * config.getMaxSize();
            islandManager.generateIsland(world, x, y, z);
            location = new Location(world, x, y + 5, z - 1, 0, 0);

            // Create island zown if it doesn't already exist
            Tree<? extends IZown> playerZown = zownManager.getZown(location);
            if (playerZown == zownManager.getZown(world)) {
                // Location is in the world zown, create player zown
                int zownRadius = (config.getMaxSize() - 10) / 2;
                final Point minPoint = new Point(x - zownRadius, -100, z - zownRadius);
                final Point maxPoint = new Point(x + zownRadius, 255, z + zownRadius);

                final String name = "XIS_Player_" + player.getUUIDString();
                playerZown = zownManager.createZown(world, name, null, minPoint, maxPoint);
                final IConfiguration playerZownConfig = playerZown.getData().getConfiguration();
                playerZownConfig.addCommandRestriction("/spawn");
                playerZownConfig.addCommandRestriction("/sethome");
                playerZownConfig.addCommandRestriction("/home");
                playerZownConfig.setFlag(Flag.playerexit.name(), false);
                XPlugin.LOG.debug("Created XIS player zown");
                if (!zownManager.saveZownConfiguration(world, name)) {
                    XPlugin.LOG.error("Error saving XIS player zown");
                }
            }
        }
        return location;
    }

    public XPlayer addPlayer(final Player player) throws DatabaseReadException,
            DatabaseWriteException {
        final XPlayer xPlayer = XPlayer.getXPlayer(player);
        players.put(player.getUUIDString(), xPlayer);
        return xPlayer;
    }

    public XPlayer removePlayer(final Player player) {
        return players.remove(player.getUUIDString());
    }

    public void persist(final XPlayer xPlayer) throws DatabaseWriteException {
        xPlayer.update();
    }

    @HookHandler(priority = Priority.PASSIVE)
    public void onDeath(final PlayerDeathHook hook) throws DatabaseReadException,
            DatabaseWriteException {
        final Player player = hook.getPlayer();
        final World world = player.getWorld();
        if (world == worldManager.getWorld()) {
            final XPlayer xPlayer = XPlayer.getXPlayer(player);
            if (!xPlayer.practice) {
                deadPlayers.add(player.getUUIDString());

                xPlayer.setLocation(null);
                challengeManager.resetMenu(player);
                xPlayer.challengesCompleted.clear();
                persist(xPlayer);
                islandManager.clearIsland(world, player, xPlayer.islandId);

                XPlugin.LOG.debug("DIED, CLEAR ISLAND");
            }
        }
    }

    @HookHandler(priority = Priority.PASSIVE)
    public void onXisDeath(final WorldDeathHook hook) throws DatabaseReadException,
            DatabaseWriteException {
        if (hook.getDeathLocation().getWorld() == worldManager.getWorld()) {
            final Player player = hook.getPlayer();
            final XPlayer xPlayer = XPlayer.getXPlayer(player);
            hook.setSpawnLocation(xPlayer.getReturnLocation());
            XPlugin.LOG.debug("RESET DEATH RESPAWN LOCATION " + xPlayer.getReturnLocation());
        }
    }

    @HookHandler(priority = Priority.PASSIVE)
    public void onWorldEnter(final WorldEnterHook hook) throws DatabaseReadException,
            DatabaseWriteException {
        if (hook.getWorld() == worldManager.getWorld()) {
            final Player player = hook.getPlayer();
            final XPlayer xPlayer = addPlayer(player);

            if (!xPlayer.practice) {
                final Location fromLocation = hook.getFromLocation();
                if (fromLocation != null) {
                    xPlayer.setReturnLocation(fromLocation);
                    persist(xPlayer);
                }
            }

            XPlugin.LOG.debug(player.getName() + " entered XIS");
        }
    }

    @HookHandler(priority = Priority.PASSIVE)
    public void onWorldExit(final WorldExitHook hook) throws DatabaseReadException,
            DatabaseWriteException {
        if (hook.getWorld() == worldManager.getWorld()) {
            final Player player = hook.getPlayer();
            final XPlayer xPlayer = removePlayer(player);

            if (!deadPlayers.remove(player.getUUIDString())) {
                final Location fromLocation;
                if (xPlayer.practice) {
                    fromLocation = player.getHome();
                } else {
                    fromLocation = hook.getFromLocation();
                }
                if (fromLocation != null) {
                    xPlayer.setLocation(fromLocation);
                    persist(xPlayer);
                }
            }

            XPlugin.LOG.debug(player.getName() + " left XIS");
        }
    }

    @HookHandler(priority = Priority.NORMAL)
    public void onHealthChange(final HealthChangeHook hook) {
        final Player player = hook.getPlayer();
        if (player.getWorld() == worldManager.getWorld()) {
            final XPlayer xPlayer = getXPlayer(player);
            if (xPlayer != null && !xPlayer.practice) {
                final float oldHealth = hook.getOldValue();
                final float newHealth = hook.getNewValue();
                if (oldHealth > 0 && oldHealth < newHealth) {
                    hook.setCanceled();
                }
            }
        }
    }

    /**
     * Convert obsidian into a lava bucket when clicked on by an empty bucket.
     */
    @HookHandler(priority = Priority.NORMAL)
    public void onItemUse(final ItemUseHook hook) {
        final Player player = hook.getPlayer();
        if (player.getWorld() == worldManager.getWorld()) {
            final Block block = hook.getBlockClicked();
            if (block != null && block.getType() == BlockType.Obsidian) {
                final Item item = hook.getItem();
                if (item.getType() == ItemType.Bucket) {
                    block.setType(BlockType.Air);
                    block.update();
                    player.getInventory().removeItem(item);
                    player.giveItem(Canary.factory().getItemFactory().newItem(ItemType.LavaBucket));
                    hook.setCanceled();
                }
            }
        }
    }
}
