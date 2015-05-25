package com.goodformentertainment.canary.xis;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.canarymod.config.Configuration;
import net.visualillusionsent.utils.PropertiesFile;

public class XConfig {
	private final PropertiesFile cfg;
	
	public XConfig(final XPlugin plugin) {
		cfg = Configuration.getPluginConfig(plugin);
	}
	
	public String getWorldName() {
		return cfg.getString("worldName", "xis");
	}
	
	public int getMaxSize() {
		return cfg.getInt("maxSize", 100);
	}
	
	public int getHeight() {
		return cfg.getInt("islandHeight", 64);
	}
	
	public int getBlockTypeValue(final String type) {
		return cfg.getInt("block.type." + type, 0);
	}
	
	public int getBlockVariantValueMultiplier(final String variant) {
		return cfg.getInt("block.variant.multiplier." + variant, 1);
	}
	
	public int getBlockColorValueMultiplier(final String color) {
		return cfg.getInt("block.color.multiplier." + color, 1);
	}
	
	public Collection<String> getIgnoredBlockRemoves() {
		final Set<String> ignoredRemoves = new HashSet<String>();
		for (final String ignore : cfg.getStringArray("block.remove.ignore", new String[0])) {
			ignoredRemoves.add(ignore);
		}
		return ignoredRemoves;
	}
	
	public String getLoggingLevel() {
		String level = null;
		final String key = "log.level";
		if (cfg.containsKey(key)) {
			level = cfg.getString(key);
		}
		return level;
	}
}
