package com.eharrison.canary.util.menu;

import java.util.HashMap;
import java.util.Map;

import net.canarymod.Canary;
import net.canarymod.api.factory.ItemFactory;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.ItemType;

public class DynaMenuItem implements IMenuItem {
	private static final ItemFactory FACTORY = Canary.factory().getItemFactory();
	
	private Menu menu;
	private final int slot;
	private final Map<String, Mode> modes;
	private String mode;
	
	public DynaMenuItem(final String name, final ItemType icon, final int slot) {
		this(name, null, icon, slot, false);
	}
	
	public DynaMenuItem(final String name, final String description, final ItemType icon,
			final int slot) {
		this(name, description, icon, slot, false);
	}
	
	public DynaMenuItem(final String name, final String description, final ItemType icon,
			final int slot, final boolean disabled) {
		this.slot = slot;
		
		modes = new HashMap<String, Mode>();
		modes.put(Mode.DEFAULT, new Mode(name, description, icon, disabled));
		mode = Mode.DEFAULT;
	}
	
	public void addMode(final String mode, final String name, final String description,
			final ItemType icon, final boolean disabled) {
		modes.put(mode, new Mode(name, description, icon, disabled));
	}
	
	public boolean setMode(final String mode) {
		boolean set = false;
		if (modes.containsKey(mode)) {
			this.mode = mode;
			menu.updateMenuItem(this);
			set = true;
		}
		return set;
	}
	
	@Override
	public void setMenu(final Menu menu) {
		this.menu = menu;
	}
	
	@Override
	public String getName() {
		String name = null;
		if (modes.containsKey(mode)) {
			name = modes.get(mode).getName();
		}
		return name;
	}
	
	@Override
	public boolean isDisabled() {
		boolean disabled = true;
		if (modes.containsKey(mode)) {
			disabled = modes.get(mode).isDisabled();
		}
		return disabled;
	}
	
	@Override
	public Item getItem() {
		Item item = null;
		if (modes.containsKey(mode)) {
			item = modes.get(mode).getItem();
		}
		return item;
	}
	
	private class Mode {
		public static final String DEFAULT = "default";
		
		private final String name;
		private final boolean disabled;
		private final Item item;
		
		public Mode(final String name, final String description, final ItemType icon,
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
		
		public Item getItem() {
			return item;
		}
		
		public boolean isDisabled() {
			return disabled;
		}
	}
}
