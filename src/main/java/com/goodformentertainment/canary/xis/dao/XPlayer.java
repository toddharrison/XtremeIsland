package com.goodformentertainment.canary.xis.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.position.Location;
import net.canarymod.database.Column;
import net.canarymod.database.Column.ColumnType;
import net.canarymod.database.Column.DataType;
import net.canarymod.database.DataAccess;
import net.canarymod.database.Database;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;

public class XPlayer extends DataAccess {
	public static final String PLAYER_UUID = "player_uuid";
	public static final String ISLAND_ID = "island_id";
	public static final String RETURN_LOCATION = "return_location";
	public static final String LOCATION = "location";
	public static final String CHALLENGES_COMPLETED = "challenges_completed";
	public static final String PRACTICE = "practice";
	
	public static XPlayer getXPlayer(final Player player) throws DatabaseReadException,
			DatabaseWriteException {
		XPlayer xPlayer = new XPlayer();
		final Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(XPlayer.PLAYER_UUID, player.getUUIDString());
		Database.get().load(xPlayer, filters);
		
		if (xPlayer.hasData()) {
			return xPlayer;
		} else {
			xPlayer = new XPlayer();
			xPlayer.playerUuid = player.getUUIDString();
			xPlayer.update();
			return getXPlayer(player);
		}
	}
	
	private Location returnLocationObj;
	private Location locationObj;
	
	public XPlayer() {
		super("xis_xplayer");
	}
	
	@Override
	public XPlayer getInstance() {
		return new XPlayer();
	}
	
	@Column(columnName = PLAYER_UUID, dataType = DataType.STRING, columnType = ColumnType.PRIMARY)
	public String playerUuid;
	
	@Column(columnName = ISLAND_ID, dataType = DataType.INTEGER, columnType = ColumnType.UNIQUE, autoIncrement = true)
	public int islandId;
	
	@Column(columnName = RETURN_LOCATION, dataType = DataType.STRING)
	public String returnLocation;
	
	@Column(columnName = LOCATION, dataType = DataType.STRING)
	public String location;
	
	@Column(columnName = CHALLENGES_COMPLETED, dataType = DataType.STRING, isList = true)
	public List<String> challengesCompleted;
	
	@Column(columnName = PRACTICE, dataType = DataType.BOOLEAN)
	public boolean practice;
	
	public Location getReturnLocation() {
		if (returnLocationObj == null && returnLocation != null && !returnLocation.equals("null")) {
			returnLocationObj = Location.fromString(returnLocation);
		}
		return returnLocationObj;
	}
	
	public void setReturnLocation(final Location returnLocation) {
		returnLocationObj = returnLocation;
		String loc = null;
		if (returnLocation != null) {
			loc = returnLocation.toString();
		}
		this.returnLocation = loc;
	}
	
	public Location getLocation() {
		if (locationObj == null && location != null && !location.equals("null")) {
			locationObj = Location.fromString(location);
		}
		return locationObj;
	}
	
	public void setLocation(final Location location) {
		locationObj = location;
		String loc = null;
		if (location != null) {
			loc = location.toString();
		}
		this.location = loc;
	}
	
	public void update() throws DatabaseWriteException {
		final Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(XPlayer.PLAYER_UUID, playerUuid);
		Database.get().update(this, filters);
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(playerUuid);
		sb.append(", ");
		sb.append(islandId);
		sb.append(", ");
		sb.append(returnLocation);
		sb.append(", ");
		sb.append(location);
		return sb.toString();
	}
}
