package com.goodformentertainment.canary.xis;

import java.io.File;
import java.io.IOException;

import net.canarymod.Canary;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.logger.Logman;
import net.canarymod.plugin.Plugin;

import com.goodformentertainment.canary.util.JarUtil;

public class XPlugin extends Plugin {
	public static Logman LOG;
	
	private final XConfig config;
	private final XWorldManager worldManager;
	private final XIslandManager islandManager;
	private final XChallengeManager challengeManager;
	private final XPlayerManager playerManager;
	private final XCommand command;
	private final XScoreboard scoreboard;
	
	public XPlugin() throws DatabaseReadException {
		XPlugin.LOG = getLogman();
		
		try {
			JarUtil.exportResource(this, "xis_xchallenge.xml", new File("db"));
			JarUtil.exportResource(this, "xis_xchallengelevel.xml", new File("db"));
		} catch (final IOException e) {
			LOG.warn("Failed to create the default challenge data files.", e);
		}
		
		config = new XConfig(this);
		worldManager = new XWorldManager(config);
		scoreboard = new XScoreboard(worldManager);
		islandManager = new XIslandManager(config);
		challengeManager = new XChallengeManager(this, scoreboard);
		playerManager = new XPlayerManager(config, worldManager, islandManager, challengeManager);
		command = new XCommand(worldManager, playerManager, challengeManager, islandManager);
		
		if (worldManager.createWorld()) {
			XPlugin.LOG.info("Created XtremeIsland world");
		}
	}
	
	@Override
	public boolean enable() {
		boolean success = false;
		
		LOG.info("Enabling " + getName() + " Version " + getVersion());
		LOG.info("Authored by " + getAuthor());
		
		if (success = worldManager.load()) {
			Canary.hooks().registerListener(playerManager, this);
			Canary.hooks().registerListener(challengeManager, this);
			Canary.hooks().registerListener(scoreboard, this);
			
			try {
				Canary.commands().registerCommands(command, this, false);
			} catch (final CommandDependencyException e) {
				LOG.error("Error registering commands: ", e);
				success = false;
			}
		}
		
		return success;
	}
	
	@Override
	public void disable() {
		LOG.info("Disabling " + getName());
		Canary.commands().unregisterCommands(this);
		Canary.hooks().unregisterPluginListeners(this);
		
		worldManager.unload();
	}
}
