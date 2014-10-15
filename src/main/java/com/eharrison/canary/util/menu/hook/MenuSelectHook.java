package com.eharrison.canary.util.menu.hook;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.hook.Hook;

import com.eharrison.canary.util.menu.Menu;
import com.eharrison.canary.util.menu.MenuItem;

public class MenuSelectHook extends Hook {
	private final Player player;
	private final Menu menu;
	private final MenuItem menuItem;
	
	public MenuSelectHook(final Player player, final Menu menu, final MenuItem menuItem) {
		this.player = player;
		this.menu = menu;
		this.menuItem = menuItem;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	public MenuItem getMenuItem() {
		return menuItem;
	}
}
