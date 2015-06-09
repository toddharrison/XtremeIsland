package com.goodformentertainment.canary.xis;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.scoreboard.Score;
import net.canarymod.api.scoreboard.ScoreObjective;
import net.canarymod.api.scoreboard.ScorePosition;
import net.canarymod.api.scoreboard.Scoreboard;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.BlockDestroyHook;
import net.canarymod.hook.player.BlockPlaceHook;
import net.canarymod.hook.player.PlayerDeathHook;
import net.canarymod.plugin.PluginListener;
import net.canarymod.plugin.Priority;

import com.goodformentertainment.canary.playerstate.hook.WorldEnterHook;
import com.goodformentertainment.canary.playerstate.hook.WorldExitHook;
import com.goodformentertainment.canary.xis.dao.XHighScore;
import com.goodformentertainment.canary.xis.dao.XPlayer;

public class XScoreboard implements PluginListener {
	private static final String NAME = "xis_scoreboard";
	
	private final XWorldManager worldManager;
	private final BlockScoreValue blockScoreValues;
	private final Scoreboard scoreboard;
	private ScoreObjective scoreObjective;
	// private ScoreObjective highScoreObjective;
	private XPlayerManager playerManager;
	
	private final Map<String, XHighScore> highScores;
	
	public XScoreboard(final XWorldManager worldManager, final BlockScoreValue blockScoreValues) {
		this.worldManager = worldManager;
		this.blockScoreValues = blockScoreValues;
		
		// TODO set the world instead of the name
		scoreboard = Canary.scoreboards().getScoreboard(NAME);
		scoreObjective = scoreboard.getScoreObjective("xScore");
		if (scoreObjective == null) {
			scoreObjective = scoreboard.addScoreObjective("xScore");
			scoreObjective.setDisplayName("Score");
		}
		// highScoreObjective = scoreboard.getScoreObjective("xHighScore");
		// if (highScoreObjective == null) {
		// highScoreObjective = scoreboard.addScoreObjective("xHighScore");
		// highScoreObjective.setDisplayName("High Score");
		// }
		highScores = new HashMap<String, XHighScore>();
		try {
			for (final XHighScore xHighScore : XHighScore.getAllXHighScores()) {
				highScores.put(xHighScore.playerUuid, xHighScore);
			}
		} catch (final DatabaseReadException e) {
			XPlugin.LOG.error("Error reading highscores from the database", e);
		}
	}
	
	protected void setPlayerManager(final XPlayerManager playerManager) {
		this.playerManager = playerManager;
	}
	
	public void addToScore(final Player player, final int value) {
		final Score score = scoreboard.getScore(player, scoreObjective);
		score.addToScore(value);
		score.update();
	}
	
	public int getScore(final Player player) {
		final Score score = scoreboard.getScore(player, scoreObjective);
		return score.getScore();
	}
	
	public SortedSet<XHighScore> getHighScores() {
		final SortedSet<XHighScore> sortedHighScores = new TreeSet<XHighScore>(
				new Comparator<XHighScore>() {
					@Override
					public int compare(final XHighScore hs1, final XHighScore hs2) {
						int scoreDiff = hs2.highScore - hs1.highScore;
						if (scoreDiff == 0) {
							scoreDiff = hs1.playerName.compareTo(hs2.playerName);
						}
						return scoreDiff;
					}
				});
		sortedHighScores.addAll(highScores.values());
		return sortedHighScores;
	}
	
	@HookHandler(priority = Priority.PASSIVE)
	public void onWorldEnter(final WorldEnterHook hook) {
		if (hook.getWorld() == worldManager.getWorld()) {
			final Player player = hook.getPlayer();
			// scoreboard.setScoreboardPosition(ScorePosition.PLAYER_LIST, highScoreObjective, player);
			scoreboard.setScoreboardPosition(ScorePosition.SIDEBAR, scoreObjective, player);
			
			final Score score = scoreboard.getScore(player, scoreObjective);
			// score.addToScore(1);
			score.update();
			// score.removeFromScore(1);
			// score.update();
			
			// TODO
			// scoreboard.getScore(player, scoreObjective).update();
			// scoreboard.getScore(player, highScoreObjective).update();
		}
	}
	
	@HookHandler(priority = Priority.PASSIVE)
	public void onLeave(final WorldExitHook hook) {
		if (hook.getWorld() == worldManager.getWorld()) {
			final Player player = hook.getPlayer();
			// scoreboard.clearScoreboardPosition(ScorePosition.PLAYER_LIST, player);
			scoreboard.clearScoreboardPosition(ScorePosition.SIDEBAR, player);
			// TODO need to update scoreboards?
		}
	}
	
	@HookHandler(priority = Priority.PASSIVE)
	public void onPlaceBlock(final BlockPlaceHook hook) {
		final Block block = hook.getBlockPlaced();
		if (block.getWorld() == worldManager.getWorld()) {
			final Player player = hook.getPlayer();
			final XPlayer xPlayer = playerManager.getXPlayer(player);
			if (!xPlayer.practice) {
				final BlockType type = block.getType();
				final int value = blockScoreValues.getPlaceValue(type);
				if (value > 0) {
					final Score score = scoreboard.getScore(player, scoreObjective);
					score.addToScore(value);
					score.update();
				}
			}
		}
	}
	
	@HookHandler(priority = Priority.PASSIVE)
	public void onRemoveBlock(final BlockDestroyHook hook) {
		final Block block = hook.getBlock();
		if (block.getWorld() == worldManager.getWorld()) {
			final Player player = hook.getPlayer();
			final XPlayer xPlayer = playerManager.getXPlayer(player);
			if (!xPlayer.practice) {
				final BlockType type = block.getType();
				final int value = blockScoreValues.getRemoveValue(type);
				if (value > 0) {
					final Score score = scoreboard.getScore(player, scoreObjective);
					score.removeFromScore(value);
					score.update();
				}
			}
		}
	}
	
	@HookHandler(priority = Priority.PASSIVE)
	public void onDeath(final PlayerDeathHook hook) {
		final Player player = hook.getPlayer();
		if (player.getWorld() == worldManager.getWorld()) {
			final XPlayer xPlayer = playerManager.getXPlayer(player);
			if (!xPlayer.practice) {
				final Score score = scoreboard.getScore(player, scoreObjective);
				// final Score highScore = scoreboard.getScore(player, highScoreObjective);
				// if (score.getScore() > highScore.getScore()) {
				// highScore.setScore(score.getScore());
				// highScore.update();
				// }
				
				try {
					final String uuid = player.getUUIDString();
					XHighScore xHighScore = highScores.get(uuid);
					if (xHighScore == null) {
						xHighScore = new XHighScore();
						xHighScore.playerUuid = uuid;
						xHighScore.playerName = player.getName();
						xHighScore.highScore = score.getScore();
						highScores.put(uuid, xHighScore);
						xHighScore.update();
					} else if (score.getScore() > xHighScore.highScore) {
						xHighScore.highScore = score.getScore();
						xHighScore.update();
					}
				} catch (final DatabaseWriteException e) {
					XPlugin.LOG.error("Error writing highscore to the database", e);
				}
				
				score.setScore(0);
				score.update();
			}
		}
	}
}
