package com.eharrison.canary.xis;

import java.util.Arrays;

import net.canarymod.Canary;
import net.canarymod.api.world.DimensionType;
import net.canarymod.api.world.World;
import net.canarymod.api.world.WorldManager;
import net.canarymod.api.world.WorldType;
import net.canarymod.api.world.position.Location;
import net.canarymod.config.Configuration;
import net.canarymod.config.WorldConfiguration;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.world.ChunkCreationHook;
import net.canarymod.plugin.PluginListener;
import net.visualillusionsent.utils.PropertiesFile;

public class XWorldManager implements PluginListener {
	private static final DimensionType X_DIMENSION = DimensionType.NORMAL;
	private static final WorldType X_TYPE = WorldType.SUPERFLAT;
	
	private final XConfig config;
	private final WorldManager worldManager;
	private final Location hubLocation;
	private World world;
	
	public XWorldManager(final XConfig config) {
		this.config = config;
		hubLocation = Canary.getServer().getWorldManager().getWorld(config.getHubWorld(), true)
				.getSpawnLocation();
		worldManager = Canary.getServer().getWorldManager();
	}
	
	public boolean createWorld() {
		boolean success = false;
		if (!worldManager.worldExists(config.getWorldName())) {
			XPlugin.logger.info("Creating XtremeIsland world " + config.getWorldName());
			
			final WorldConfiguration worldConfig = Configuration.getWorldConfig(config.getWorldName()
					+ "_" + X_DIMENSION.getName());
			final PropertiesFile file = worldConfig.getFile();
			file.setInt("difficulty", World.Difficulty.HARD.getId());
			file.setBoolean("generate-structures", false);
			file.setBoolean("allow-nether", false);
			file.setBoolean("allow-end", false);
			file.setBoolean("spawn-villagers", false);
			file.setBoolean("spawn-golems", false);
			file.setBoolean("spawn-animals", false);
			file.setBoolean("spawn-monsters", true);
			file.setString("world-type", "FLAT");
			file.save();
			
			success = worldManager.createWorld(config.getWorldName(), 0, X_DIMENSION, X_TYPE);
			
			// TODO Use 1.8 customized when available instead
			// file.setBoolean("force-gamemode", false);
			// file.setString("level-type", "CUSTOMIZED");
			// file.setString("generator-settings", "2;0;0;");
			// file.setString("generator-settings", "2;7,3x1,52x24;2;");
			// file.setString("generator-settings", "3;minecraft:air;0;");
			//
			// final boolean success = worldManager.createWorld(worldConfig);
		} else {
			success = true;
		}
		return success;
	}
	
	public boolean load() {
		world = worldManager.getWorld(config.getWorldName(), true);
		return world != null;
	}
	
	public void unload() {
		worldManager.unloadWorld(config.getWorldName(), X_DIMENSION, true);
	}
	
	public World getWorld() {
		return world;
	}
	
	public Location getHubLocation() {
		return hubLocation;
	}
	
	@HookHandler
	public void onChunkCreation(final ChunkCreationHook hook) {
		// TODO Doesn't work right now... use a custom world of air instead when that's working
		if (hook.getWorld() == world) {
			final int[] blockData = new int[32768];
			Arrays.fill(blockData, 0);
			hook.setBlockData(blockData);
		}
	}
}
