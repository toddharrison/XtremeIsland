package com.eharrison.canary.util.menu;

import java.util.HashMap;
import java.util.Map;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.factory.ObjectFactory;
import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.inventory.Item;
import net.canarymod.hook.CancelableHook;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.InventoryHook;
import net.canarymod.hook.player.SlotClickHook;
import net.canarymod.logger.Logman;
import net.canarymod.plugin.Plugin;
import net.canarymod.plugin.PluginListener;

import com.eharrison.canary.util.menu.hook.MenuCloseHook;
import com.eharrison.canary.util.menu.hook.MenuOpenHook;
import com.eharrison.canary.util.menu.hook.MenuSelectHook;

public class Menu implements PluginListener {
	private static final Logman LOG = Logman.getLogman("Menu");
	private static final ObjectFactory FACTORY = Canary.factory().getObjectFactory();
	
	private final String name;
	private final Map<String, MenuItem> itemMap;
	private final Inventory inventory;
	
	public Menu(final String name, final int rows, final MenuItem... menuItems) {
		LOG.info("Creating menu: " + name);
		this.name = name;
		itemMap = new HashMap<String, MenuItem>();
		inventory = FACTORY.newCustomStorageInventory(name, rows);
		for (final MenuItem menuItem : menuItems) {
			final Item item = menuItem.getItem();
			itemMap.put(item.getDisplayName(), menuItem);
			inventory.setSlot(item);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public void open(final Player player) {
		final CancelableHook hook = new MenuOpenHook(player, this);
		Canary.hooks().callHook(hook);
		if (!hook.isCanceled()) {
			player.openInventory(inventory);
		}
	}
	
	@HookHandler
	public void onSlotClick(final SlotClickHook hook) {
		if (hook.getInventory() == inventory) {
			final Player player = hook.getPlayer();
			final MenuItem menuItem = itemMap.get(hook.getItem().getDisplayName());
			if (menuItem != null && !menuItem.isDisabled()) {
				Canary.hooks().callHook(new MenuSelectHook(player, this, menuItem));
			}
			hook.setCanceled();
		}
	}
	
	@HookHandler
	public void onInventoryClose(final InventoryHook hook) {
		if (hook.isClosing() && hook.getInventory() == inventory) {
			final Player player = hook.getPlayer();
			Canary.hooks().callHook(new MenuCloseHook(player, this));
		}
	}
	
	protected void register(final Plugin plugin) {
		Canary.hooks().registerListener(this, plugin);
	}
	
	protected void unregister() {
		Canary.hooks().unregisterPluginListener(this);
	}
}
