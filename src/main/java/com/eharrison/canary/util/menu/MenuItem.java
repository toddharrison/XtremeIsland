package com.eharrison.canary.util.menu;

import net.canarymod.Canary;
import net.canarymod.api.factory.ItemFactory;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.ItemType;

public class MenuItem {
	private static final ItemFactory FACTORY = Canary.factory().getItemFactory();
	
	private final String name;
	private final boolean disabled;
	private final Item item;
	
	public MenuItem(final String name, final ItemType icon, final int slot) {
		this(name, null, icon, slot, false);
	}
	
	public MenuItem(final String name, final String description, final ItemType icon, final int slot) {
		this(name, description, icon, slot, false);
	}
	
	public MenuItem(final String name, final String description, final ItemType icon, final int slot,
			final boolean disabled) {
		this.name = name;
		this.disabled = disabled;
		
		item = FACTORY.newItem(icon);
		item.setDisplayName(name);
		if (description != null) {
			item.setLore(description);
		}
		item.setAmount(1);
		item.setSlot(slot);
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isDisabled() {
		return disabled;
	}
	
	public Item getItem() {
		return item;
	}
}
