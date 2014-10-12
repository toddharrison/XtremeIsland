package com.eharrison.canary.xis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.api.world.DimensionType;
import net.canarymod.api.world.World;
import net.canarymod.api.world.WorldManager;
import net.canarymod.api.world.WorldType;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.api.world.blocks.Chest;
import net.canarymod.api.world.position.Location;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandListener;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.ConnectionHook;
import net.canarymod.hook.player.DisconnectionHook;
import net.canarymod.hook.player.PlayerDeathHook;
import net.canarymod.hook.player.PlayerRespawningHook;
import net.canarymod.hook.world.ChunkCreationHook;
import net.canarymod.plugin.PluginListener;

import com.eharrison.canary.xis.XExitHook.Cause;

public class XEventManager implements CommandListener, PluginListener {
	private final XConfig config;
	private final World world;
	private final Map<String, XPlayer> players;
	private final Location hubLocation;
	
	public XEventManager(final XConfig config) {
		this.config = config;
		final WorldManager worldManager = Canary.getServer().getWorldManager();
		final boolean success = worldManager.createWorld(config.getWorldName(), 0,
				DimensionType.NORMAL, WorldType.SUPERFLAT);
		world = worldManager.getWorld("xis", true);
		hubLocation = Canary.getServer().getWorldManager().getWorld("default", true).getSpawnLocation();
		players = new HashMap<String, XPlayer>();
		XPlugin.logger.info("Created XtremeIsland world " + config.getWorldName());
	}
	
	@Command(aliases = {
			"xis", "xisland"
	}, description = "XtremeIsland commands", permissions = {
		"xis.join"
	}, toolTip = "/xis")
	public void islandCommand(final MessageReceiver caller, final String[] parameters)
			throws DatabaseReadException, DatabaseWriteException {
		if (caller instanceof Player) {
			final Player player = (Player) caller;
			
			if (player.getWorld() != world) {
				Canary.hooks().callHook(new XEnterHook(player, player.getLocation()));
				final XPlayer xPlayer = players.get(player.getUUIDString());
				Location location = xPlayer.getLocation();
				if (location == null) {
					// TODO: Make tile algorithm instead of row
					final int x = xPlayer.islandId * config.getMaxSize();
					final int y = config.getHeight();
					final int z = 0;
					generateIsland(world, x, y, z);
					location = new Location(world, x, y + 5, z - 1, 0, 0);
				}
				player.teleportTo(location);
			}
		} else {
			XPlugin.logger.info(players);
		}
	}
	
	@Command(aliases = {
			"exit", "quit"
	}, parent = "xis", helpLookup = "xis exit", description = "Leave XtremeIsland", permissions = {
		"xis.exit"
	}, toolTip = "/xis <exit>")
	public void groupAdd(final MessageReceiver caller, final String[] parameters)
			throws DatabaseReadException, DatabaseWriteException {
		if (caller instanceof Player) {
			final Player player = (Player) caller;
			if (player.getWorld() == world) {
				final XPlayer xPlayer = players.get(player.getUUIDString());
				Location returnLocation = xPlayer.getReturnLocation();
				if (returnLocation == null) {
					returnLocation = hubLocation;
				}
				player.teleportTo(returnLocation);
			}
		}
	}
	
	@HookHandler
	public void onConnect(final ConnectionHook hook) throws DatabaseReadException,
			DatabaseWriteException {
		final Player player = hook.getPlayer();
		if (player.getWorld() == world) {
			Canary.hooks().callHook(new XEnterHook(player));
		}
	}
	
	@HookHandler
	public void onDisconnect(final DisconnectionHook hook) {
		final Player player = hook.getPlayer();
		if (player.getWorld() == world) {
			Canary.hooks().callHook(new XExitHook(player, Cause.EXIT));
		}
	}
	
	@HookHandler
	public void onRespawning(final PlayerRespawningHook hook) throws DatabaseReadException,
			DatabaseWriteException {
		final Player player = hook.getPlayer();
		// XPlugin.logger.info("Calling " + player.getDisplayName() + " respawn");
		
		if (player.getWorld() == world) {
			if (hook.getRespawnLocation() == null) {
				final XPlayer xPlayer = players.get(player.getUUIDString());
				// XPlugin.logger.info("Calling " + player.getDisplayName() + " DEATH");
				hook.setRespawnLocation(xPlayer.getReturnLocation());
				Canary.hooks().callHook(new XExitHook(player, Cause.DEATH));
			} else if (hook.getRespawnLocation().getWorld() != world) {
				// XPlugin.logger.info("Calling " + player.getDisplayName() + " EXIT");
				Canary.hooks().callHook(new XExitHook(player, Cause.EXIT));
			}
		}
	}
	
	@HookHandler
	public void onDeath(final PlayerDeathHook hook) throws DatabaseReadException,
			DatabaseWriteException {
		final Player player = hook.getPlayer();
		if (player.getWorld() == world) {
			final XPlayer xPlayer = players.get(player.getUUIDString());
			xPlayer.setLocation(null);
			xPlayer.died = true;
			XData.updateXPlayer(xPlayer);
			clearIsland(xPlayer.islandId);
		}
	}
	
	@HookHandler
	public void onXEnter(final XEnterHook hook) throws DatabaseReadException, DatabaseWriteException {
		final Player player = hook.getPlayer();
		final XPlayer xPlayer = XData.getXPlayer(player);
		players.put(player.getUUIDString(), xPlayer);
		if (hook.getReturnLocation() != null) {
			xPlayer.setReturnLocation(hook.getReturnLocation());
			XData.updateXPlayer(xPlayer);
		}
		
		XPlugin.logger.info(player.getDisplayName() + " entered XIS");
	}
	
	@HookHandler
	public void onXExit(final XExitHook hook) throws DatabaseReadException, DatabaseWriteException {
		final Player player = hook.getPlayer();
		if (hook.getCause() == Cause.EXIT) {
			final XPlayer xPlayer = players.remove(player.getUUIDString());
			xPlayer.setLocation(player.getLocation());
			XData.updateXPlayer(xPlayer);
		}
		
		XPlugin.logger.info(player.getDisplayName() + " left XIS");
	}
	
	@HookHandler
	public void onChunkCreation(final ChunkCreationHook hook) {
		if (hook.getWorld().getName().equals(config.getWorldName())) {
			final int[] blockdata = new int[32768];
			Arrays.fill(blockdata, BlockType.Air.getId());
			hook.setBlockData(blockdata);
		}
	}
	
	private void generateIsland(final World world, final int x, int y, final int z) {
		XPlugin.logger.info("Generating island at " + x + ":" + y + ":" + z);
		
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
	
	private void clearIsland(final int islandId) {
		XPlugin.logger.info("Deleting island " + islandId);
		
		new Thread() {
			@Override
			public void run() {
				final int maxSize = config.getMaxSize();
				final int centerX = islandId * maxSize;
				final int centerZ = 0;
				
				for (int x = maxSize / -2; x <= maxSize / 2; x++) {
					for (int y = 0; y <= 255; y++) {
						for (int z = maxSize / -2; z <= maxSize / 2; z++) {
							world.setBlockAt(centerX + x, y, centerZ + z, BlockType.Air);
						}
					}
				}
			}
		}.start();
	}
}
