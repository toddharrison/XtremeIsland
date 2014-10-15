package com.eharrison.canary.xis;

import net.canarymod.Canary;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.logger.Logman;
import net.canarymod.plugin.Plugin;

import com.eharrison.canary.util.menu.Menu;
import com.eharrison.canary.util.menu.MenuItem;

public class XPlugin extends Plugin {
	protected static Logman logger;
	
	private final XConfig config;
	private final XWorldManager worldManager;
	private final XIslandManager islandManager;
	private final XPlayerManager playerManager;
	private final XChallengeManager challengeManager;
	private final XCommand command;
	private final XScoreboard scoreboard;
	private final Menu menu;
	
	public XPlugin() {
		XPlugin.logger = getLogman();
		config = new XConfig(this);
		menu = new Menu("XtremeIsland Menu", 7,
				new MenuItem("Help", "Get some help", ItemType.Anvil, 4));
		worldManager = new XWorldManager(config);
		islandManager = new XIslandManager(config);
		playerManager = new XPlayerManager(worldManager, islandManager);
		challengeManager = new XChallengeManager();
		command = new XCommand(config, worldManager, islandManager, playerManager, menu);
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
			Canary.hooks().registerListener(worldManager, this);
			Canary.hooks().registerListener(playerManager, this);
			Canary.hooks().registerListener(scoreboard, this);
			Canary.hooks().registerListener(command, this);
			Canary.hooks().registerListener(menu, this);
			
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
