package com.eharrison.canary.xis;

import java.util.HashMap;
import java.util.Map;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.database.Database;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;

public final class XData {
	public static XPlayer getXPlayer(final Player player) throws DatabaseReadException,
			DatabaseWriteException {
		final XPlayer xPlayer = new XPlayer();
		final Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(XPlayer.PLAYER_UUID, player.getUUIDString());
		Database.get().load(xPlayer, filters);
		
		if (xPlayer.hasData()) {
			return xPlayer;
		} else {
			return createXPlayer(player);
		}
	}
	
	public static XPlayer createXPlayer(final Player player) throws DatabaseWriteException,
			DatabaseReadException {
		final XPlayer xPlayer = new XPlayer();
		xPlayer.playerUuid = player.getUUIDString();
		XData.updateXPlayer(xPlayer);
		return XData.getXPlayer(player);
	}
	
	public static void updateXPlayer(final XPlayer xPlayer) throws DatabaseWriteException {
		final Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(XPlayer.PLAYER_UUID, xPlayer.playerUuid);
		Database.get().update(xPlayer, filters);
	}
}
