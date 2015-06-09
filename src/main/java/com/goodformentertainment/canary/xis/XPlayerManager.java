package com.goodformentertainment.canary.xis;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.World;
import net.canarymod.api.world.position.Location;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.HealthChangeHook;
import net.canarymod.hook.player.PlayerDeathHook;
import net.canarymod.plugin.PluginListener;

import com.goodformentertainment.canary.playerstate.hook.WorldDeathHook;
import com.goodformentertainment.canary.playerstate.hook.WorldEnterHook;
import com.goodformentertainment.canary.playerstate.hook.WorldExitHook;
import com.goodformentertainment.canary.xis.dao.XPlayer;
import com.goodformentertainment.canary.zown.Flag;
import com.goodformentertainment.canary.zown.api.IConfiguration;
import com.goodformentertainment.canary.zown.api.IZown;
import com.goodformentertainment.canary.zown.api.IZownManager;
import com.goodformentertainment.canary.zown.api.Point;
import com.goodformentertainment.canary.zown.api.impl.Tree;

public class XPlayerManager implements PluginListener {
	private final XConfig config;
	private final XWorldManager worldManager;
	private final XIslandManager islandManager;
	private final XChallengeManager challengeManager;
	private final Map<String, XPlayer> players;
	private final Collection<String> deadPlayers;
	
	private final IZownManager zownManager;
	
	public XPlayerManager(final XConfig config, final XWorldManager worldManager,
			final XIslandManager islandManager, final XChallengeManager challengeManager,
			final IZownManager zownManager) {
		this.config = config;
		this.worldManager = worldManager;
		this.islandManager = islandManager;
		this.challengeManager = challengeManager;
		this.challengeManager.setPlayerManager(this);
		this.zownManager = zownManager;
		deadPlayers = Collections.synchronizedCollection(new HashSet<String>());
		players = new HashMap<String, XPlayer>();
	}
	
	public Set<String> getActivePlayerIds() {
		return players.keySet();
	}
	
	public XPlayer getXPlayer(final Player player) {
		XPlayer xPlayer = null;
		if (player != null) {
			xPlayer = players.get(player.getUUIDString());
		}
		return xPlayer;
	}
	
	public Location getIslandLocation(final Player player) throws DatabaseReadException,
			DatabaseWriteException {
		XPlugin.LOG.debug("GETTING ISLAND LOCATION");
		
		final World world = worldManager.getWorld();
		final XPlayer xPlayer = addPlayer(player);
		Location location = xPlayer.getLocation();
		if (location == null) {
			XPlugin.LOG.debug("CREATING ISLAND");
			
			// TODO: Make tile algorithm instead of row
			final int x = xPlayer.islandId * config.getMaxSize();
			final int y = config.getHeight();
			final int z = 0;
			islandManager.generateIsland(world, x, y, z);
			location = new Location(world, x, y + 5, z - 1, 0, 0);
			
			// Create island zown if it doesn't already exist
			Tree<? extends IZown> playerZown = zownManager.getZown(location);
			if (playerZown == zownManager.getZown(world)) {
				// Location is in the world zown, create player zown
				final Point minPoint = new Point(x - 45, -100, z - 45);
				final Point maxPoint = new Point(x + 45, 255, z + 45);
				
				final String name = "XIS_Player_" + player.getUUIDString();
				playerZown = zownManager.createZown(world, name, null, minPoint, maxPoint);
				final IConfiguration playerZownConfig = playerZown.getData().getConfiguration();
				playerZownConfig.addCommandRestriction("/spawn");
				playerZownConfig.addCommandRestriction("/sethome");
				playerZownConfig.addCommandRestriction("/home");
				playerZownConfig.setFlag(Flag.playerexit.name(), false);
				XPlugin.LOG.debug("Created XIS player zown");
				if (!zownManager.saveZownConfiguration(world, name)) {
					XPlugin.LOG.error("Error saving XIS player zown");
				}
			}
		}
		return location;
	}
	
	public XPlayer addPlayer(final Player player) throws DatabaseReadException,
			DatabaseWriteException {
		final XPlayer xPlayer = XPlayer.getXPlayer(player);
		players.put(player.getUUIDString(), xPlayer);
		return xPlayer;
	}
	
	public XPlayer removePlayer(final Player player) {
		return players.remove(player.getUUIDString());
	}
	
	public void persist(final XPlayer xPlayer) throws DatabaseWriteException {
		xPlayer.update();
	}
	
	@HookHandler
	public void onDeath(final PlayerDeathHook hook) throws DatabaseReadException,
			DatabaseWriteException {
		final Player player = hook.getPlayer();
		final World world = player.getWorld();
		if (world == worldManager.getWorld()) {
			deadPlayers.add(player.getUUIDString());
			final XPlayer xPlayer = XPlayer.getXPlayer(player);
			
			xPlayer.setLocation(null);
			challengeManager.resetMenu(player);
			xPlayer.challengesCompleted.clear();
			persist(xPlayer);
			islandManager.clearIsland(world, player, xPlayer.islandId);
			
			XPlugin.LOG.debug("DIED, CLEAR ISLAND");
		}
	}
	
	@HookHandler
	public void onXisDeath(final WorldDeathHook hook) throws DatabaseReadException,
			DatabaseWriteException {
		if (hook.getDeathLocation().getWorld() == worldManager.getWorld()) {
			final Player player = hook.getPlayer();
			final XPlayer xPlayer = XPlayer.getXPlayer(player);
			hook.setSpawnLocation(xPlayer.getReturnLocation());
			
			XPlugin.LOG.debug("RESET DEATH RESPAWN LOCATION " + xPlayer.getReturnLocation());
		}
	}
	
	@HookHandler
	public void onWorldEnter(final WorldEnterHook hook) throws DatabaseReadException,
			DatabaseWriteException {
		if (hook.getWorld() == worldManager.getWorld()) {
			final Player player = hook.getPlayer();
			final XPlayer xPlayer = addPlayer(player);
			
			final Location fromLocation = hook.getFromLocation();
			if (fromLocation != null) {
				xPlayer.setReturnLocation(fromLocation);
				persist(xPlayer);
			}
			
			// TODO fix
			// Canary.getServer().consoleCommand("gamerule naturalRegeneration false", player);
			
			XPlugin.LOG.debug(player.getName() + " entered XIS");
		}
	}
	
	@HookHandler
	public void onWorldExit(final WorldExitHook hook) throws DatabaseReadException,
			DatabaseWriteException {
		if (hook.getWorld() == worldManager.getWorld()) {
			final Player player = hook.getPlayer();
			final XPlayer xPlayer = removePlayer(player);
			
			if (!deadPlayers.remove(player.getUUIDString())) {
				final Location fromLocation = hook.getFromLocation();
				if (fromLocation != null) {
					xPlayer.setLocation(fromLocation);
					persist(xPlayer);
				}
			}
			
			// TODO fix
			// Canary.getServer().consoleCommand("gamerule naturalRegeneration true", player);
			
			XPlugin.LOG.debug(player.getName() + " left XIS");
		}
	}
	
	@HookHandler
	public void onHealthChange(final HealthChangeHook hook) {
		final Player player = hook.getPlayer();
		if (player.getWorld() == worldManager.getWorld()) {
			final float oldHealth = hook.getOldValue();
			final float newHealth = hook.getNewValue();
			// XPlugin.LOG.info("OLD HEALTH: " + oldHealth);
			// XPlugin.LOG.info("NEW HEALTH: " + newHealth);
			// if (oldHealth == -1) {
			// // Spawn-in
			// } else
			if (oldHealth > 0 && oldHealth < newHealth) {
				hook.setCanceled();
			}
		}
	}
}
