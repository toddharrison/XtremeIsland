package com.goodformentertainment.canary.xis;

import java.io.File;
import java.io.IOException;

import net.canarymod.Canary;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.logger.Logman;
import net.canarymod.plugin.Plugin;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.goodformentertainment.canary.util.JarUtil;

public class XPlugin extends Plugin {
	public static Logman LOG;
	
	private XConfig config;
	private XWorldManager worldManager;
	private XIslandManager islandManager;
	private XChallengeManager challengeManager;
	private XPlayerManager playerManager;
	private XCommand command;
	private XScoreboard scoreboard;
	
	public XPlugin() {
		XPlugin.LOG = getLogman();
	}
	
	@Override
	public boolean enable() {
		boolean success = false;
		
		try {
			JarUtil.exportResource(this, "XtremeIsland.cfg", new File("config/XtremeIsland"));
		} catch (final IOException e) {
			LOG.warn("Failed to create the default configuration file.", e);
		}
		
		try {
			JarUtil.exportResource(this, "xis_xchallenge.xml", new File("db"));
			JarUtil.exportResource(this, "xis_xchallengelevel.xml", new File("db"));
		} catch (final IOException e) {
			LOG.warn("Failed to create the default challenge data files.", e);
		}
		
		config = new XConfig(this);
		setLoggingLevel(config.getLoggingLevel());
		
		LOG.info("Enabling " + getName() + " Version " + getVersion());
		LOG.info("Authored by " + getAuthor());
		
		try {
			worldManager = new XWorldManager(config);
			scoreboard = new XScoreboard(worldManager, new BlockScoreValue(config));
			islandManager = new XIslandManager(config);
			challengeManager = new XChallengeManager(this, scoreboard);
			playerManager = new XPlayerManager(config, worldManager, islandManager, challengeManager);
			command = new XCommand(worldManager, playerManager, challengeManager, islandManager);
			
			if (worldManager.createWorld()) {
				XPlugin.LOG.info("Created XtremeIsland world");
			}
			
			if (worldManager.load()) {
				Canary.hooks().registerListener(playerManager, this);
				Canary.hooks().registerListener(challengeManager, this);
				Canary.hooks().registerListener(scoreboard, this);
				
				try {
					Canary.commands().registerCommands(command, this, false);
					success = true;
				} catch (final CommandDependencyException e) {
					LOG.error("Error registering commands: ", e);
				}
			}
		} catch (final Exception e) {
			LOG.error("Error starting up XIS", e);
		}
		
		return success;
	}
	
	@Override
	public void disable() {
		LOG.info("Disabling " + getName());
		Canary.commands().unregisterCommands(this);
		Canary.hooks().unregisterPluginListeners(this);
		
		worldManager.unload();
		
		command = null;
		playerManager = null;
		challengeManager = null;
		islandManager = null;
		scoreboard = null;
		worldManager = null;
		config = null;
	}
	
	private void setLoggingLevel(final String level) {
		if (level != null) {
			final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
			final Configuration config = ctx.getConfiguration();
			final LoggerConfig loggerConfig = config.getLoggerConfig(LOG.getName());
			loggerConfig.setLevel(Level.toLevel(level));
			ctx.updateLoggers();
		}
	}
}
