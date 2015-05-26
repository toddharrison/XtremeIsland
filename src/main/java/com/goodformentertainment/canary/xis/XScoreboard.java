package com.goodformentertainment.canary.xis;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.scoreboard.Score;
import net.canarymod.api.scoreboard.ScoreObjective;
import net.canarymod.api.scoreboard.ScorePosition;
import net.canarymod.api.scoreboard.Scoreboard;
import net.canarymod.api.world.blocks.Block;
import net.canarymod.api.world.blocks.BlockType;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.BlockDestroyHook;
import net.canarymod.hook.player.BlockPlaceHook;
import net.canarymod.hook.player.PlayerDeathHook;
import net.canarymod.plugin.PluginListener;
import net.canarymod.plugin.Priority;

import com.goodformentertainment.canary.playerstate.hook.WorldEnterHook;
import com.goodformentertainment.canary.playerstate.hook.WorldExitHook;

public class XScoreboard implements PluginListener {
	private static final String NAME = "xis_scoreboard";
	
	private final XWorldManager worldManager;
	private final BlockScoreValue blockScoreValues;
	private final Scoreboard scoreboard;
	private ScoreObjective scoreObjective;
	private ScoreObjective highScoreObjective;
	
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
		highScoreObjective = scoreboard.getScoreObjective("xHighScore");
		if (highScoreObjective == null) {
			highScoreObjective = scoreboard.addScoreObjective("xHighScore");
			highScoreObjective.setDisplayName("High Score");
		}
	}
	
	public void addToScore(final Player player, final int value) {
		final Score score = scoreboard.getScore(player, scoreObjective);
		score.addToScore(value);
		score.update();
	}
	
	@HookHandler(priority = Priority.PASSIVE)
	public void onWorldEnter(final WorldEnterHook hook) {
		if (hook.getWorld() == worldManager.getWorld()) {
			final Player player = hook.getPlayer();
			scoreboard.setScoreboardPosition(ScorePosition.PLAYER_LIST, scoreObjective, player);
			scoreboard.setScoreboardPosition(ScorePosition.SIDEBAR, highScoreObjective, player);
			
			scoreboard.getScore(player, scoreObjective).update();
			scoreboard.getScore(player, highScoreObjective).update();
		}
	}
	
	@HookHandler(priority = Priority.PASSIVE)
	public void onLeave(final WorldExitHook hook) {
		if (hook.getWorld() == worldManager.getWorld()) {
			final Player player = hook.getPlayer();
			scoreboard.clearScoreboardPosition(ScorePosition.PLAYER_LIST, player);
			scoreboard.clearScoreboardPosition(ScorePosition.SIDEBAR, player);
			// TODO need to update scoreboards?
		}
	}
	
	@HookHandler(priority = Priority.PASSIVE)
	public void onPlaceBlock(final BlockPlaceHook hook) {
		final Block block = hook.getBlockPlaced();
		if (block.getWorld() == worldManager.getWorld()) {
			final BlockType type = block.getType();
			final int value = blockScoreValues.getPlaceValue(type);
			if (value > 0) {
				final Player player = hook.getPlayer();
				final Score score = scoreboard.getScore(player, scoreObjective);
				score.addToScore(value);
				score.update();
			}
		}
	}
	
	@HookHandler(priority = Priority.PASSIVE)
	public void onRemoveBlock(final BlockDestroyHook hook) {
		final Block block = hook.getBlock();
		if (block.getWorld() == worldManager.getWorld()) {
			final BlockType type = block.getType();
			final int value = blockScoreValues.getRemoveValue(type);
			if (value > 0) {
				final Player player = hook.getPlayer();
				final Score score = scoreboard.getScore(player, scoreObjective);
				score.removeFromScore(value);
				score.update();
			}
		}
	}
	
	@HookHandler(priority = Priority.PASSIVE)
	public void onDeath(final PlayerDeathHook hook) {
		final Player player = hook.getPlayer();
		if (player.getWorld() == worldManager.getWorld()) {
			final Score score = scoreboard.getScore(player, scoreObjective);
			final Score highScore = scoreboard.getScore(player, highScoreObjective);
			if (score.getScore() > highScore.getScore()) {
				highScore.setScore(score.getScore());
				highScore.update();
			}
			score.setScore(0);
			score.update();
		}
	}
}
