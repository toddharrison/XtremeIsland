package com.goodformentertainment.canary.xis;

import java.util.Set;
import java.util.SortedSet;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.position.Location;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandListener;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;

import com.goodformentertainment.canary.xis.dao.XHighScore;
import com.goodformentertainment.canary.xis.dao.XPlayer;
import com.goodformentertainment.canary.zown.api.IZown;
import com.goodformentertainment.canary.zown.api.IZownManager;

public class XCommand implements CommandListener {
	private final XWorldManager worldManager;
	private final XPlayerManager playerManager;
	private final XChallengeManager challengeManager;
	private final XIslandManager islandManager;
	private final XScoreboard scoreboard;
	private final IZownManager zownManager;
	
	public XCommand(final XWorldManager worldManager, final XPlayerManager playerManager,
			final XChallengeManager challengeManager, final XIslandManager islandManager,
			final XScoreboard scoreboard, final IZownManager zownManager) {
		this.worldManager = worldManager;
		this.playerManager = playerManager;
		this.challengeManager = challengeManager;
		this.islandManager = islandManager;
		this.scoreboard = scoreboard;
		this.zownManager = zownManager;
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
			player
					.message("Usage: /xis <(g)o | (c)hallenge | (e)xit | (l)istplayers | (t)opscores | (p)ractice | (r)estart>");
		} else {
			XPlugin.LOG
					.info("Usage: /xis <(g)o | (c)hallenge | (e)xit | (l)istplayers | (t)opscores | (p)ractice | (r)estart>");
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
	
	@Command(aliases = {
			"topscores", "t"
	}, parent = "xis", description = "List the top scores in XtremeIsland", permissions = {
		"xis.command.top"
	}, toolTip = "/xis (t)opscores")
	public void topScores(final MessageReceiver caller, final String[] parameters) {
		final StringBuilder sb = new StringBuilder();
		sb.append("XtremeIsland Top Scores:");
		
		final SortedSet<XHighScore> highScores = scoreboard.getHighScores();
		if (highScores == null || highScores.isEmpty()) {
			sb.append("\n  none");
		} else {
			for (final XHighScore highScore : highScores) {
				sb.append("\n  ");
				sb.append(highScore.highScore);
				sb.append(" : ");
				sb.append(highScore.playerName);
			}
		}
		
		if (caller instanceof Player) {
			final Player player = caller.asPlayer();
			player.message(sb.toString());
		} else {
			XPlugin.LOG.info(sb.toString());
		}
	}
	
	@Command(aliases = {
			"practice", "p"
	}, parent = "xis", description = "Change an island to practice", permissions = {
		"xis.command.practice"
	}, toolTip = "/xis (p)ractice")
	public void practice(final MessageReceiver caller, final String[] parameters)
			throws DatabaseWriteException {
		if (caller instanceof Player) {
			final Player player = caller.asPlayer();
			if (player.getWorld() == worldManager.getWorld()) {
				final Location playerLocation = player.getLocation();
				
				final XPlayer xPlayer = playerManager.getXPlayer(player);
				xPlayer.practice = true;
				xPlayer.setLocation(playerLocation);
				playerManager.persist(xPlayer);
				
				player.setHome(playerLocation);
				final IZown zown = zownManager.getZown(playerLocation).getData();
				zown.getConfiguration().removeCommandRestriction("/home");
				zown.getConfiguration().removeCommandRestriction("/sethome");
				zownManager.saveZownConfiguration(playerLocation.getWorld(), zown.getName());
			}
		}
	}
	
	@Command(aliases = {
			"restart", "r"
	}, parent = "xis", description = "Restart your island", permissions = {
		"xis.command.restart"
	}, toolTip = "/xis (r)estart")
	public void restart(final MessageReceiver caller, final String[] parameters)
			throws DatabaseWriteException {
		if (caller instanceof Player) {
			final Player player = caller.asPlayer();
			if (player.getWorld() == worldManager.getWorld()) {
				final XPlayer xPlayer = playerManager.getXPlayer(player);
				if (xPlayer.practice) {
					xPlayer.practice = false;
					xPlayer.setLocation(null);
					
					final Location playerLocation = player.getLocation();
					final IZown zown = zownManager.getZown(playerLocation).getData();
					zown.getConfiguration().addCommandRestriction("/home");
					zown.getConfiguration().addCommandRestriction("/sethome");
					zownManager.saveZownConfiguration(playerLocation.getWorld(), zown.getName());
				}
				playerManager.persist(xPlayer);
				
				player.kill();
			}
		}
	}
}
