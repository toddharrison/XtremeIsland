package com.goodformentertainment.canary.xis.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.canarymod.database.Column;
import net.canarymod.database.Column.ColumnType;
import net.canarymod.database.Column.DataType;
import net.canarymod.database.DataAccess;
import net.canarymod.database.Database;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;

public class XChallengeLevel extends DataAccess {
	public static final String NAME = "name";
	public static final String ORDER = "order";
	
	public static List<XChallengeLevel> getAllXChallengeLevels() throws DatabaseReadException {
		final List<XChallengeLevel> levels = new ArrayList<XChallengeLevel>();
		
		final List<DataAccess> daos = new ArrayList<DataAccess>();
		final XChallengeLevel xChallengeLevel = new XChallengeLevel();
		Database.get().loadAll(xChallengeLevel, daos, new HashMap<String, Object>());
		for (final DataAccess dao : daos) {
			levels.add((XChallengeLevel) dao);
		}
		
		Collections.sort(levels, new Comparator<XChallengeLevel>() {
			@Override
			public int compare(final XChallengeLevel x1, final XChallengeLevel x2) {
				return new Integer(x1.order).compareTo(x2.order);
			}
		});
		
		return levels;
	}
	
	public XChallengeLevel() {
		super("xis_xchallengelevel");
	}
	
	@Override
	public XChallengeLevel getInstance() {
		return new XChallengeLevel();
	}
	
	@Column(columnName = NAME, dataType = DataType.STRING, columnType = ColumnType.UNIQUE)
	public String name;
	
	@Column(columnName = ORDER, dataType = DataType.INTEGER, columnType = ColumnType.UNIQUE, autoIncrement = true)
	public int order;
	
	public void update() throws DatabaseWriteException {
		final Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(XChallengeLevel.NAME, name);
		Database.get().update(this, filters);
	}
}
