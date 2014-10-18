package com.eharrison.canary.xis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.World;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.ConnectionHook;
import net.canarymod.hook.player.DisconnectionHook;
import net.canarymod.hook.player.PlayerDeathHook;
import net.canarymod.hook.player.PlayerRespawningHook;
import net.canarymod.plugin.PluginListener;

import com.eharrison.canary.xis.dao.XPlayer;
import com.eharrison.canary.xis.hook.XEnterHook;
import com.eharrison.canary.xis.hook.XExitHook;
import com.eharrison.canary.xis.hook.XExitHook.Cause;

public class XPlayerManager implements PluginListener {
	private final XWorldManager worldManager;
	private final XIslandManager islandManager;
	private final Map<String, XPlayer> players;
	
	public XPlayerManager(final XWorldManager worldManager, final XIslandManager islandManager) {
		this.worldManager = worldManager;
		this.islandManager = islandManager;
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
	public void onConnect(final ConnectionHook hook) throws DatabaseReadException,
			DatabaseWriteException {
		final Player player = hook.getPlayer();
		if (player.getWorld() == worldManager.getWorld()) {
			Canary.hooks().callHook(new XEnterHook(player));
		}
	}
	
	@HookHandler
	public void onDisconnect(final DisconnectionHook hook) {
		final Player player = hook.getPlayer();
		if (player.getWorld() == worldManager.getWorld()) {
			Canary.hooks().callHook(new XExitHook(player, Cause.EXIT));
		}
	}
	
	@HookHandler
	public void onRespawning(final PlayerRespawningHook hook) throws DatabaseReadException,
			DatabaseWriteException {
		final Player player = hook.getPlayer();
		final World world = worldManager.getWorld();
		
		if (player.getWorld() == world) {
			if (hook.getRespawnLocation() == null) {
				final XPlayer xPlayer = getXPlayer(player);
				hook.setRespawnLocation(xPlayer.getReturnLocation());
				Canary.hooks().callHook(new XExitHook(player, Cause.DEATH));
			} else if (hook.getRespawnLocation().getWorld() != world) {
				Canary.hooks().callHook(new XExitHook(player, Cause.EXIT));
			}
		}
	}
	
	@HookHandler
	public void onDeath(final PlayerDeathHook hook) throws DatabaseReadException,
			DatabaseWriteException {
		final Player player = hook.getPlayer();
		final World world = worldManager.getWorld();
		
		if (player.getWorld() == world) {
			final XPlayer xPlayer = getXPlayer(player);
			xPlayer.setLocation(null);
			xPlayer.died = true;
			persist(xPlayer);
			islandManager.clearIsland(world, xPlayer.islandId);
		}
	}
	
	@HookHandler
	public void onXEnter(final XEnterHook hook) throws DatabaseReadException, DatabaseWriteException {
		final Player player = hook.getPlayer();
		final XPlayer xPlayer = addPlayer(player);
		if (hook.getReturnLocation() != null) {
			xPlayer.setReturnLocation(hook.getReturnLocation());
			persist(xPlayer);
		}
		
		XPlugin.logger.info(player.getDisplayName() + " entered XIS");
	}
	
	@HookHandler
	public void onXExit(final XExitHook hook) throws DatabaseReadException, DatabaseWriteException {
		final Player player = hook.getPlayer();
		if (hook.getCause() == Cause.EXIT) {
			final XPlayer xPlayer = removePlayer(player);
			xPlayer.setLocation(player.getLocation());
			persist(xPlayer);
		}
		
		XPlugin.logger.info(player.getDisplayName() + " left XIS");
	}
}
