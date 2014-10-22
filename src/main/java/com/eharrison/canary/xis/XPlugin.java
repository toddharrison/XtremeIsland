package com.eharrison.canary.xis;

import net.canarymod.Canary;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.logger.Logman;
import net.canarymod.plugin.Plugin;

public class XPlugin extends Plugin {
	public static Logman logger;
	
	private final XConfig config;
	private final XWorldManager worldManager;
	private final XIslandManager islandManager;
	private final XPlayerManager playerManager;
	private final XChallengeManager challengeManager;
	private final XCommand command;
	private final XScoreboard scoreboard;
	
	public XPlugin() throws DatabaseReadException {
		XPlugin.logger = getLogman();
		
		config = new XConfig(this);
		worldManager = new XWorldManager(config);
		islandManager = new XIslandManager(config);
		playerManager = new XPlayerManager(config, worldManager, islandManager);
		challengeManager = new XChallengeManager(this, playerManager);
		command = new XCommand(worldManager, playerManager, challengeManager);
		scoreboard = new XScoreboard(worldManager);
		
		if (worldManager.createWorld()) {
			XPlugin.logger.info("Created XtremeIsland world");
		}
	}
	
	@Override
	public boolean enable() {
		boolean success = false;
		
		logger.info("Enabling " + getName() + " Version " + getVersion());
		logger.info("Authored by " + getAuthor());
		
		if (success = worldManager.load()) {
			Canary.hooks().registerListener(playerManager, this);
			Canary.hooks().registerListener(challengeManager, this);
			Canary.hooks().registerListener(scoreboard, this);
			
			try {
				Canary.commands().registerCommands(command, this, false);
			} catch (final CommandDependencyException e) {
				logger.error("Error registering commands: ", e);
				success = false;
			}
		}
		
		return success;
	}
	
	@Override
	public void disable() {
		logger.info("Disabling " + getName());
		Canary.commands().unregisterCommands(this);
		Canary.hooks().unregisterPluginListeners(this);
		
		worldManager.unload();
	}
}
