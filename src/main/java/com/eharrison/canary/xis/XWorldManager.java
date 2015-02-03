package com.eharrison.canary.xis;

import net.canarymod.Canary;
import net.canarymod.api.world.DimensionType;
import net.canarymod.api.world.World;
import net.canarymod.api.world.WorldManager;
import net.canarymod.api.world.WorldType;
import net.canarymod.api.world.position.Location;
import net.canarymod.config.Configuration;
import net.canarymod.config.WorldConfiguration;
import net.visualillusionsent.utils.PropertiesFile;

import com.eharrison.canary.playerstate.PlayerState;
import com.eharrison.canary.playerstate.PlayerState.Save;

public class XWorldManager {
	private static final DimensionType X_DIMENSION = DimensionType.NORMAL;
	private static final WorldType X_TYPE = WorldType.SUPERFLAT;
	
	private final XConfig config;
	private final WorldManager worldManager;
	private World world;
	
	public XWorldManager(final XConfig config) {
		this.config = config;
		worldManager = Canary.getServer().getWorldManager();
	}
	
	public Location getDefaultSpawn() {
		return Canary.getServer().getDefaultWorld().getSpawnLocation();
	}
	
	public boolean createWorld() {
		boolean success = false;
		if (!worldManager.worldExists(config.getWorldName())) {
			XPlugin.LOG.info("Creating XtremeIsland world " + config.getWorldName());
			
			final WorldConfiguration worldConfig = Configuration.getWorldConfig(config.getWorldName()
					+ "_" + X_DIMENSION.getName());
			final PropertiesFile file = worldConfig.getFile();
			file.setInt("difficulty", World.Difficulty.HARD.getId());
			file.setBoolean("generate-structures", false);
			file.setBoolean("allow-nether", false);
			file.setBoolean("allow-end", false);
			file.setBoolean("spawn-villagers", true);
			file.setBoolean("spawn-golems", true);
			file.setBoolean("spawn-animals", true);
			file.setBoolean("spawn-monsters", true);
			file.setString("world-type", "FLAT");
			file.setString("generator-settings", "2;0;0;");
			// file.setString("generator-settings", "3;minecraft:air;0;");
			file.save();
			
			success = worldManager.createWorld(config.getWorldName(), 0, X_DIMENSION, X_TYPE);
		} else {
			success = true;
		}
		return success;
	}
	
	public boolean load() {
		world = worldManager.getWorld(config.getWorldName(), true);
		PlayerState.registerWorld(world, new Save[] {
				Save.CONDITIONS, Save.INVENTORY, Save.LOCATIONS
		});
		return world != null;
	}
	
	public void unload() {
		worldManager.unloadWorld(config.getWorldName(), X_DIMENSION, true);
		PlayerState.unregisterWorld(world);
	}
	
	public World getWorld() {
		return world;
	}
}
