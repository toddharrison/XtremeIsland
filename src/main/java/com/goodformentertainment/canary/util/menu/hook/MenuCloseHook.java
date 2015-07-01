package com.goodformentertainment.canary.util.menu.hook;

import com.goodformentertainment.canary.util.menu.Menu;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.hook.Hook;

public class MenuCloseHook extends Hook {
    private final Player player;
    private final Menu menu;

    public MenuCloseHook(final Player player, final Menu menu) {
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
