package com.eharrison.canary.util.menu;

import java.util.HashMap;
import java.util.Map;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.plugin.Plugin;

public class MenuFactory {
	private final Plugin plugin;
	private final String title;
	private final int rows;
	private final MenuConfiguration menuConfig;
	private final MenuItem[] menuItems;
	
	private final Map<String, Menu> menus;
	
	public MenuFactory(final Plugin plugin, final String title, final int rows,
			final MenuItem... menuItems) {
		this(plugin, title, rows, null, menuItems);
	}
	
	public MenuFactory(final Plugin plugin, final String title, final int rows,
			final MenuConfiguration menuConfig, final MenuItem... menuItems) {
		this.plugin = plugin;
		this.title = title;
		this.rows = rows;
		this.menuConfig = menuConfig;
		this.menuItems = menuItems;
		menus = new HashMap<String, Menu>();
	}
	
	public Menu getMenu(final Player player) {
		final String playerId = player.getUUIDString();
		Menu menu = menus.get(playerId);
		if (menu == null) {
			final MenuItem[] newMenuItems = new MenuItem[menuItems.length];
			for (int i = 0; i < menuItems.length; i++) {
				newMenuItems[i] = new MenuItem(menuItems[i]);
			}
			if (menuConfig != null) {
				menuConfig.configure(newMenuItems);
			}
			menu = new Menu(plugin, this, title, rows, menuConfig, newMenuItems);
			menus.put(playerId, menu);
		}
		return menu;
	}
	
	public void removeMenu(final Player player) {
		menus.remove(player.getUUIDString());
	}
}
