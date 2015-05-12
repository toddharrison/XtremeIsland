package com.goodformentertainment.canary.xis;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.World;
import net.canarymod.api.world.position.Location;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandListener;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;

import com.goodformentertainment.canary.xis.dao.XPlayer;

public class XCommand implements CommandListener {
	private final XWorldManager worldManager;
	private final XPlayerManager playerManager;
	private final XChallengeManager challengeManager;
	private final XIslandManager islandManager;
	
	public XCommand(final XWorldManager worldManager, final XPlayerManager playerManager,
			final XChallengeManager challengeManager, final XIslandManager islandManager) {
		this.worldManager = worldManager;
		this.playerManager = playerManager;
		this.challengeManager = challengeManager;
		this.islandManager = islandManager;
	}
	
	@Command(aliases = {
			"xis", "xisland"
	}, description = "XtremeIsland commands", permissions = {
		"xis.command"
	}, toolTip = "/xis")
	public void islandCommand(final MessageReceiver caller, final String[] parameters)
			throws DatabaseReadException, DatabaseWriteException {
		if (caller instanceof Player) {
			final Player player = (Player) caller;
			final World world = worldManager.getWorld();
			
			if (player.getWorld() != world) {
				if (islandManager.isClearingIsland(player)) {
					player.message("Your island is still deleting, please wait a moment...");
				} else {
					player.teleportTo(playerManager.getIslandLocation(player));
				}
			} else {
				challengeManager.openMenu(player);
			}
		} else {
			XPlugin.LOG.info(playerManager.getActivePlayerIds());
		}
	}
	
	@Command(aliases = {
			"exit", "quit"
	}, parent = "xis", helpLookup = "xis exit", description = "Leave XtremeIsland", permissions = {
		"xis.command.exit"
	}, toolTip = "/xis <exit>")
	public void exitIsland(final MessageReceiver caller, final String[] parameters)
			throws DatabaseReadException, DatabaseWriteException {
		if (caller instanceof Player) {
			final Player player = (Player) caller;
			if (player.getWorld() == worldManager.getWorld()) {
				final XPlayer xPlayer = playerManager.getXPlayer(player);
				Location returnLocation = xPlayer.getReturnLocation();
				if (returnLocation == null) {
					returnLocation = worldManager.getDefaultSpawn();
				}
				player.teleportTo(returnLocation);
			}
		}
	}
}
