package com.eharrison.canary.xis;

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

import com.eharrison.canary.xis.hook.XEnterHook;
import com.eharrison.canary.xis.hook.XExitHook;

public class XScoreboard implements PluginListener {
	private static final String NAME = "xis_scoreboard";
	
	private final XWorldManager worldManager;
	private final XPlayerManager playerManager;
	private final Scoreboard scoreboard;
	private ScoreObjective scoreObjective;
	private ScoreObjective highScoreObjective;
	
	public XScoreboard(final XWorldManager worldManager, final XPlayerManager playerManager) {
		this.worldManager = worldManager;
		this.playerManager = playerManager;
		
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
		
		// TODO on plugin enable/disable do the same with the score
		scoreboard.setScoreboardPosition(ScorePosition.PLAYER_LIST, scoreObjective);
		scoreboard.setScoreboardPosition(ScorePosition.SIDEBAR, highScoreObjective);
	}
	
	@HookHandler
	public void onJoin(final XEnterHook hook) {
		final Player player = hook.getPlayer();
		final Score score = scoreboard.getScore(player, scoreObjective);
		// score.setScore(0);
		// score.setScore(playerManager.getXPlayer(player).score);
		score.update();
	}
	
	@HookHandler
	public void onPlaceBlock(final BlockPlaceHook hook) {
		final Block block = hook.getBlockPlaced();
		if (block.getWorld() == worldManager.getWorld()) {
			final Player player = hook.getPlayer();
			final Score score = scoreboard.getScore(player, scoreObjective);
			score.addToScore(1);
			score.update();
		}
	}
	
	@HookHandler
	public void onRemoveBlock(final BlockDestroyHook hook) {
		final Block block = hook.getBlock();
		if (block.getWorld() == worldManager.getWorld()) {
			final BlockType type = hook.getBlock().getType();
			if (type != BlockType.AcaciaLeaves && type != BlockType.AcaciaLog
					&& type != BlockType.BirchLeaves && type != BlockType.BirchLog
					&& type != BlockType.DarkOakLeaves && type != BlockType.DarkOakLog
					&& type != BlockType.JungleLeaves && type != BlockType.JungleLog
					&& type != BlockType.OakLeaves && type != BlockType.OakLog
					&& type != BlockType.PineLeaves && type != BlockType.PineLog && type != BlockType.Cobble) {
				final Player player = hook.getPlayer();
				final Score score = scoreboard.getScore(player, scoreObjective);
				score.removeFromScore(1);
				score.update();
			}
		}
	}
	
	@HookHandler
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
	
	@HookHandler
	public void onLeave(final XExitHook hook) {
		// TODO remove this player from the scoreboard?
	}
}
