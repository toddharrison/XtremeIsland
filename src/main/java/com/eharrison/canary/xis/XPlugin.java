package com.eharrison.canary.xis;

import net.canarymod.Canary;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.logger.Logman;
import net.canarymod.plugin.Plugin;

public class XPlugin extends Plugin {
	protected static Logman logger;
	
	private final XConfig config;
	private final XEventManager eventManager;
	
	public XPlugin() {
		XPlugin.logger = getLogman();
		config = new XConfig(this);
		eventManager = new XEventManager(config);
	}
	
	@Override
	public boolean enable() {
		boolean success = true;
		
		logger.info("Enabling " + getName() + " Version " + getVersion());
		logger.info("Authored by " + getAuthor());
		
		Canary.hooks().registerListener(eventManager, this);
		
		try {
			Canary.commands().registerCommands(eventManager, this, false);
		} catch (final CommandDependencyException e) {
			logger.error("Error registering commands: ", e);
			success = false;
		}
		
		return success;
	}
	
	@Override
	public void disable() {
		logger.info("Disabling " + getName());
		Canary.commands().unregisterCommands(this);
		Canary.hooks().unregisterPluginListeners(this);
	}
}
