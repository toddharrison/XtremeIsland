package com.goodformentertainment.canary.util.menu;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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

    public Player getPlayer(final Menu menu) {
        Player player = null;
        for (final Entry<String, Menu> entry : menus.entrySet()) {
            if (entry.getValue() == menu) {
                player = Canary.getServer().getPlayerFromUUID(entry.getKey());
            }
        }
        return player;
    }

    public Menu getMenu(final Player player) {
        final String playerId = player.getUUIDString();
        Menu menu = menus.get(playerId);
        if (menu == null) {
            final MenuItem[] newMenuItems = new MenuItem[menuItems.length];
            for (int i = 0; i < menuItems.length; i++) {
                newMenuItems[i] = new MenuItem(menuItems[i]);
            }
            menu = new Menu(plugin, this, title, rows, menuConfig);
            menus.put(playerId, menu);
            menu.setMenuItems(newMenuItems);
        }
        return menu;
    }

    public void removeMenu(final Player player) {
        menus.remove(player.getUUIDString());
    }
}
