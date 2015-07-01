package com.goodformentertainment.canary.util.menu.hook;

import com.goodformentertainment.canary.util.menu.Menu;
import com.goodformentertainment.canary.util.menu.MenuItem;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.hook.CancelableHook;

public class MenuItemSelectHook extends CancelableHook {
    private final Player player;
    private final Menu menu;
    private final MenuItem menuItem;

    public MenuItemSelectHook(final Player player, final Menu menu, final MenuItem menuItem) {
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
