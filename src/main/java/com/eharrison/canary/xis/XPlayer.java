package com.eharrison.canary.xis;

import net.canarymod.api.world.position.Location;
import net.canarymod.database.Column;
import net.canarymod.database.Column.ColumnType;
import net.canarymod.database.Column.DataType;
import net.canarymod.database.DataAccess;

public class XPlayer extends DataAccess {
	public static final String PLAYER_UUID = "player_uuid";
	public static final String ISLAND_ID = "island_id";
	public static final String RETURN_LOCATION = "return_location";
	public static final String LOCATION = "location";
	
	private Location returnLocationObj;
	private Location locationObj;
	
	public XPlayer() {
		super("xsb_xplayer");
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
