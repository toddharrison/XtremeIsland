package com.eharrison.canary.xis;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.World;
import net.canarymod.api.world.position.Location;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandListener;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;

import com.eharrison.canary.xis.dao.XPlayer;
import com.eharrison.canary.xis.hook.XEnterHook;

public class XCommand implements CommandListener {
	private final XConfig config;
	private final XWorldManager worldManager;
	private final XIslandManager islandManager;
	private final XPlayerManager playerManager;
	
	public XCommand(final XConfig config, final XWorldManager worldManager,
			final XIslandManager islandManager, final XPlayerManager playerManager) {
		this.config = config;
		this.worldManager = worldManager;
		this.islandManager = islandManager;
		this.playerManager = playerManager;
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
			final World world = worldManager.getWorld();
			
			if (player.getWorld() != world) {
				Canary.hooks().callHook(new XEnterHook(player, player.getLocation()));
				final XPlayer xPlayer = playerManager.getXPlayer(player);
				Location location = xPlayer.getLocation();
				if (location == null) {
					// TODO: Make tile algorithm instead of row
					final int x = xPlayer.islandId * config.getMaxSize();
					final int y = config.getHeight();
					final int z = 0;
					islandManager.generateIsland(world, x, y, z);
					location = new Location(world, x, y + 5, z - 1, 0, 0);
				}
				player.teleportTo(location);
			}
		} else {
			XPlugin.logger.info(playerManager.getActivePlayerIds());
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
			if (player.getWorld() == worldManager.getWorld()) {
				final XPlayer xPlayer = playerManager.getXPlayer(player);
				Location returnLocation = xPlayer.getReturnLocation();
				if (returnLocation == null) {
					returnLocation = worldManager.getHubLocation();
				}
				player.teleportTo(returnLocation);
			}
		}
	}
}
