package com.eharrison.canary.xis;

import net.canarymod.Canary;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.logger.Logman;
import net.canarymod.plugin.Plugin;

public class XPlugin extends Plugin {
	protected static Logman logger;
	
	private final XConfig config;
	private final XWorldManager worldManager;
	private final XIslandManager islandManager;
	private final XPlayerManager playerManager;
	private final XCommand command;
	
	public XPlugin() {
		XPlugin.logger = getLogman();
		config = new XConfig(this);
		worldManager = new XWorldManager(config);
		islandManager = new XIslandManager(config);
		playerManager = new XPlayerManager(worldManager, islandManager);
		command = new XCommand(config, worldManager, islandManager, playerManager);
		
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
			Canary.hooks().registerListener(worldManager, this);
			Canary.hooks().registerListener(playerManager, this);
			
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
