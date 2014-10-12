package com.eharrison.canary.xis;

import net.canarymod.config.Configuration;
import net.visualillusionsent.utils.PropertiesFile;

public class XConfig {
	private final PropertiesFile cfg;
	
	public XConfig(final XPlugin plugin) {
		cfg = Configuration.getPluginConfig(plugin);
	}
	
	// public String getHubWorld() {
	// return cfg.getString("worldName", "default");
	// }
	
	public String getWorldName() {
		return cfg.getString("worldName", "xis");
	}
	
	public int getMaxSize() {
		return cfg.getInt("maxSize", 100);
	}
	
	public int getHeight() {
		return cfg.getInt("islandHeight", 64);
	}
}
