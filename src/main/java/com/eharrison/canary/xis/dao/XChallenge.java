package com.eharrison.canary.xis.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import net.canarymod.api.inventory.ItemType;
import net.canarymod.database.Column;
import net.canarymod.database.Column.ColumnType;
import net.canarymod.database.Column.DataType;
import net.canarymod.database.DataAccess;
import net.canarymod.database.Database;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;

public class XChallenge extends DataAccess {
	public static final String NAME = "name";
	public static final String LEVEL = "level";
	public static final String DESCRIPTION = "description";
	public static final String TYPE = "type";
	public static final String ITEMS_REQUIRED = "items_required";
	public static final String CONSUME_ITEMS = "consume_items";
	public static final String ITEMS_REWARD = "items_reward";
	public static final String REWARD_DESCRIPTION = "reward_description";
	public static final String REPEATABLE = "repeatable";
	public static final String ITEMS_REPEAT_REWARD = "items_repeat_reward";
	public static final String REPEAT_REWARD_DESCRIPTION = "repeat_reward_description";
	
	public static XChallenge getXChallenge(final String name) throws DatabaseReadException {
		final XChallenge xChallenge = new XChallenge();
		final Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(XChallenge.NAME, name);
		Database.get().load(xChallenge, filters);
		
		if (xChallenge.hasData()) {
			return xChallenge;
		} else {
			return null;
		}
	}
	
	private Map<ItemType, Integer> itemsRequiredMap;
	private Map<ItemType, Integer> itemsRewardMap;
	private Map<ItemType, Integer> itemsRepeatRewardMap;
	
	public XChallenge() {
		super("xsb_xchallenge");
	}
	
	@Override
	public XChallenge getInstance() {
		return new XChallenge();
	}
	
	@Column(columnName = NAME, dataType = DataType.STRING, columnType = ColumnType.UNIQUE)
	public String name;
	
	@Column(columnName = LEVEL, dataType = DataType.STRING)
	public String level;
	
	@Column(columnName = DESCRIPTION, dataType = DataType.STRING)
	public String description;
	
	@Column(columnName = TYPE, dataType = DataType.STRING)
	public String type;
	
	@Column(columnName = ITEMS_REQUIRED, isList = true, dataType = DataType.STRING)
	public List<String> itemsRequired;
	
	@Column(columnName = CONSUME_ITEMS, dataType = DataType.BOOLEAN)
	public boolean consumeItems;
	
	@Column(columnName = ITEMS_REWARD, isList = true, dataType = DataType.STRING)
	public List<String> itemsReward;
	
	@Column(columnName = REWARD_DESCRIPTION, dataType = DataType.STRING)
	public String rewardDescription;
	
	@Column(columnName = REPEATABLE, dataType = DataType.BOOLEAN)
	public boolean repeatable;
	
	@Column(columnName = ITEMS_REPEAT_REWARD, isList = true, dataType = DataType.STRING)
	public List<String> itemsRepeatReward;
	
	@Column(columnName = REPEAT_REWARD_DESCRIPTION, dataType = DataType.STRING)
	public String repeatRewardDescription;
	
	public Map<ItemType, Integer> getItemsRequired() {
		if (itemsRequiredMap == null && itemsRequired != null) {
			itemsRequiredMap = getItemQuantities(itemsRequired);
		}
		return itemsRequiredMap;
	}
	
	public Map<ItemType, Integer> getItemsReward() {
		if (itemsRewardMap == null && itemsReward != null) {
			itemsRewardMap = getItemQuantities(itemsReward);
		}
		return itemsRewardMap;
	}
	
	public Map<ItemType, Integer> getItemsRepeatReward() {
		if (itemsRepeatRewardMap == null && itemsRepeatReward != null) {
			itemsRepeatRewardMap = getItemQuantities(itemsRepeatReward);
		}
		return itemsRepeatRewardMap;
	}
	
	public void update() throws DatabaseWriteException {
		final Map<String, Object> filters = new HashMap<String, Object>();
		filters.put(XChallenge.NAME, name);
		Database.get().update(this, filters);
	}
	
	private Map<ItemType, Integer> getItemQuantities(final List<String> itemStringList) {
		final Map<ItemType, Integer> itemQuantities = new HashMap<ItemType, Integer>();
		for (final String itemString : itemStringList) {
			final StringTokenizer tokenizer = new StringTokenizer(itemString.trim(), ":");
			final String machineName = tokenizer.nextToken() + ":" + tokenizer.nextToken();
			final int quantity = Integer.parseInt(tokenizer.nextToken());
			int data = 0;
			if (tokenizer.hasMoreTokens()) {
				data = Integer.parseInt(tokenizer.nextToken());
			}
			final ItemType itemType = ItemType.fromStringAndData(machineName, data);
			if (itemType != null) {
				itemQuantities.put(itemType, quantity);
			} else {
				throw new IllegalArgumentException("The item string '" + itemString + "' is invalid");
			}
		}
		return itemQuantities;
	}
}
