package com.goodformentertainment.canary.xis;

import net.canarymod.Canary;
import net.canarymod.api.world.DimensionType;
import net.canarymod.api.world.World;
import net.canarymod.api.world.WorldManager;
import net.canarymod.api.world.WorldType;
import net.canarymod.api.world.position.Location;
import net.canarymod.config.Configuration;
import net.canarymod.config.WorldConfiguration;
import net.visualillusionsent.utils.PropertiesFile;

import com.goodformentertainment.canary.playerstate.PlayerStatePlugin;
import com.goodformentertainment.canary.playerstate.api.IWorldStateManager;
import com.goodformentertainment.canary.playerstate.api.SaveState;
import com.goodformentertainment.canary.zown.Flag;
import com.goodformentertainment.canary.zown.api.IConfiguration;
import com.goodformentertainment.canary.zown.api.IZown;
import com.goodformentertainment.canary.zown.api.IZownManager;

public class XWorldManager {
	private static final DimensionType X_DIMENSION = DimensionType.NORMAL;
	private static final WorldType X_TYPE = WorldType.SUPERFLAT;
	
	private final XConfig config;
	private final WorldManager worldManager;
	private World world;
	
	private final IZownManager zownManager;
	
	public XWorldManager(final XConfig config, final IZownManager zownManager) {
		this.config = config;
		this.zownManager = zownManager;
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
		final IWorldStateManager worldStateManager = PlayerStatePlugin.getWorldManager();
		
		worldStateManager.registerWorld(world, new SaveState[] {
				SaveState.CONDITIONS, SaveState.INVENTORY, SaveState.LOCATIONS
		});
		
		// Configure xis world zown
		final IZown zown = zownManager.getZown(world).getData();
		final IConfiguration zownConfig = zown.getConfiguration();
		if (!zownConfig.hasCommandRestriction("/spawn")
				|| !zownConfig.hasCommandRestriction("/sethome")
				|| !zownConfig.hasCommandRestriction("/home")) {
			zownConfig.addCommandRestriction("/spawn");
			zownConfig.addCommandRestriction("/sethome");
			zownConfig.addCommandRestriction("/home");
			zownConfig.setFlag(Flag.build.name(), false);
			if (!zownManager.saveZownConfiguration(world, zown.getName())) {
				XPlugin.LOG.error("Error saving XIS world zown");
			}
		}
		
		return world != null;
	}
	
	public void unload() {
		worldManager.unloadWorld(config.getWorldName(), X_DIMENSION, true);
		final IWorldStateManager worldStateManager = PlayerStatePlugin.getWorldManager();
		worldStateManager.unregisterWorld(world);
	}
	
	public World getWorld() {
		return world;
	}
}
