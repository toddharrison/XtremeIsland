package com.eharrison.canary.util.menu.hook;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.hook.Hook;

import com.eharrison.canary.util.menu.IMenuItem;
import com.eharrison.canary.util.menu.Menu;

public class MenuSelectHook extends Hook {
	private final Player player;
	private final Menu menu;
	private final IMenuItem menuItem;
	
	public MenuSelectHook(final Player player, final Menu menu, final IMenuItem menuItem) {
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
	
	public IMenuItem getMenuItem() {
		return menuItem;
	}
}
