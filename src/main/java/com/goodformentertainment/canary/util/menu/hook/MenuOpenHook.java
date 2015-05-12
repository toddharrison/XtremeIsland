package com.goodformentertainment.canary.util.menu.hook;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.hook.CancelableHook;

import com.goodformentertainment.canary.util.menu.Menu;

public class MenuOpenHook extends CancelableHook {
	private final Player player;
	private final Menu menu;
	
	public MenuOpenHook(final Player player, final Menu menu) {
		this.player = player;
		this.menu = menu;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Menu getMenu() {
		return menu;
	}
}
