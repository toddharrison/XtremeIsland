package com.goodformentertainment.canary.xis;

import com.goodformentertainment.canary.util.JarUtil;
import com.goodformentertainment.canary.zown.ZownPlugin;
import com.goodformentertainment.canary.zown.api.IZownManager;
import net.canarymod.Canary;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.logger.Logman;
import net.canarymod.plugin.Plugin;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.io.File;
import java.io.IOException;

public class XPlugin extends Plugin {
    public static Logman LOG;

    private XConfig config;
    private XWorldManager worldManager;
    private XIslandManager islandManager;
    private XChallengeManager challengeManager;
    private XPlayerManager playerManager;
    private XCommand command;
    private XScoreboard scoreboard;

    private IZownManager zownManager;

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

        zownManager = ZownPlugin.getZownManager();

        try {
            worldManager = new XWorldManager(config, zownManager);
            scoreboard = new XScoreboard(worldManager, new BlockScoreValue(config));
            islandManager = new XIslandManager(config);
            challengeManager = new XChallengeManager(this, scoreboard);
            playerManager = new XPlayerManager(config, worldManager, islandManager, challengeManager,
                    zownManager);
            command = new XCommand(worldManager, playerManager, challengeManager, islandManager,
                    scoreboard, zownManager);

            scoreboard.setPlayerManager(playerManager);

            if (worldManager.createWorld()) {
                XPlugin.LOG.debug("Created XtremeIsland world");
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

        zownManager = null;
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
