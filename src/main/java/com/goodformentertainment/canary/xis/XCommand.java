package com.goodformentertainment.canary.xis;

import java.util.Set;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
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
	}, description = "Get help for XtremeIsland", permissions = {
		"xis.command"
	}, toolTip = "/xis")
	public void help(final MessageReceiver caller, final String[] parameters) {
		if (caller instanceof Player) {
			final Player player = caller.asPlayer();
			player.message("XtremeIsland Challenge!");
			player.message("Usage: /xis <(g)o | (c)hallenge | (e)xit | (l)istplayers>");
		} else {
			XPlugin.LOG.info("Usage: /xis <(g)o | (c)hallenge | (e)xit | (l)istplayers>");
		}
	}
	
	@Command(aliases = {
			"go", "g"
	}, parent = "xis", description = "Go to your XtremeIsland", permissions = {
		"xis.command.go"
	}, toolTip = "/xis (g)o")
	public void go(final MessageReceiver caller, final String[] parameters)
			throws DatabaseReadException, DatabaseWriteException {
		if (caller instanceof Player) {
			final Player player = caller.asPlayer();
			if (player.getWorld() != worldManager.getWorld()) {
				if (islandManager.isClearingIsland(player)) {
					player.message("Your island is still deleting, please wait a moment...");
				} else {
					player.teleportTo(playerManager.getIslandLocation(player));
				}
			} else {
				player.message("You are already on your XtremeIsland!");
			}
		}
	}
	
	@Command(aliases = {
			"challenge", "c"
	}, parent = "xis", description = "Compete a XtremeIsland Challenge", permissions = {
		"xis.command.challenge"
	}, toolTip = "/xis (c)hallenge")
	public void challenge(final MessageReceiver caller, final String[] parameters) {
		if (caller instanceof Player) {
			final Player player = caller.asPlayer();
			if (player.getWorld() == worldManager.getWorld()) {
				challengeManager.openMenu(player);
			} else {
				player.message("You are not on your XtremeIsland!");
			}
		}
	}
	
	@Command(aliases = {
			"exit", "e"
	}, parent = "xis", description = "Exit your XtremeIsland", permissions = {
		"xis.command.exit"
	}, toolTip = "/xis (e)xit")
	public void exit(final MessageReceiver caller, final String[] parameters)
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
			} else {
				player.message("You are not on your XtremeIsland!");
			}
		}
	}
	
	@Command(aliases = {
			"listplayers", "l"
	}, parent = "xis", description = "List the current Players in XtremeIsland", permissions = {
		"xis.command.list"
	}, toolTip = "/xis (l)istplayers")
	public void listPlayers(final MessageReceiver caller, final String[] parameters) {
		final StringBuilder sb = new StringBuilder();
		sb.append("Current XtremeIsland Players: ");
		
		final Set<String> uuids = playerManager.getActivePlayerIds();
		if (uuids == null || uuids.isEmpty()) {
			sb.append("none");
		} else {
			boolean added = false;
			for (final String uuid : uuids) {
				if (added) {
					sb.append(", ");
				}
				final Player player = Canary.getServer().getPlayerFromUUID(uuid);
				sb.append(player.getName());
				added = true;
			}
		}
		
		if (caller instanceof Player) {
			final Player player = caller.asPlayer();
			player.message(sb.toString());
		} else {
			XPlugin.LOG.info(sb.toString());
		}
	}
}
