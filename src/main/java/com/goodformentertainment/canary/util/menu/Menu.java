package com.goodformentertainment.canary.util.menu;

import com.goodformentertainment.canary.util.menu.hook.MenuCloseHook;
import com.goodformentertainment.canary.util.menu.hook.MenuItemSelectHook;
import com.goodformentertainment.canary.util.menu.hook.MenuOpenHook;
import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.factory.ObjectFactory;
import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.inventory.Item;
import net.canarymod.hook.CancelableHook;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.InventoryHook;
import net.canarymod.hook.player.SlotClickHook;
import net.canarymod.plugin.Plugin;
import net.canarymod.plugin.PluginListener;

import java.util.HashMap;
import java.util.Map;

public class Menu implements PluginListener {
    private static final ObjectFactory FACTORY = Canary.factory().getObjectFactory();

    private final MenuFactory menuFactory;

    private final String title;
    private final int rows;
    private final MenuConfiguration menuConfig;
    private final Map<String, MenuItem> menuItems;

    private final Inventory inventory;

    public Menu(final Plugin plugin, final String title, final int rows) {
        this(plugin, null, title, rows, (MenuConfiguration) null);
    }

    public Menu(final Plugin plugin, final MenuFactory menuFactory, final String title, final int rows) {
        this(plugin, menuFactory, title, rows, (MenuConfiguration) null);
    }

    public Menu(final Plugin plugin, final MenuFactory menuFactory, final String title,
                final int rows, final MenuConfiguration menuConfig) {
        this.menuFactory = menuFactory;
        this.title = title;
        this.rows = rows;
        this.menuConfig = menuConfig;
        menuItems = new HashMap<String, MenuItem>();
        inventory = FACTORY.newCustomStorageInventory(title, rows);

        Canary.hooks().registerListener(this, plugin);
    }

    public Menu(final Plugin plugin, final String title, final int rows, final MenuItem... menuItems) {
        this(plugin, title, rows);
        setMenuItems(menuItems);
    }

    public Menu(final Plugin plugin, final MenuFactory menuFactory, final String title,
                final int rows, final MenuItem... menuItems) {
        this(plugin, menuFactory, title, rows);
        setMenuItems(menuItems);
    }

    public Menu(final Plugin plugin, final MenuFactory menuFactory, final String title,
                final int rows, final MenuConfiguration menuConfig, final MenuItem... menuItems) {
        this(plugin, menuFactory, title, rows, menuConfig);
        setMenuItems(menuItems);
    }

    public MenuFactory getMenuFactory() {
        return menuFactory;
    }

    public String getTitle() {
        return title;
    }

    public int getRows() {
        return rows;
    }

    public void setMenuItems(final MenuItem... menuItems) {
        if (menuItems.length > 0) {
            if (menuConfig != null) {
                if (menuFactory != null) {
                    menuConfig.configure(menuItems, menuFactory.getPlayer(this));
                } else {
                    menuConfig.configure(menuItems);
                }
            }
            for (final MenuItem menuItem : menuItems) {
                menuItem.setMenu(this);
                this.menuItems.put(menuItem.getName(), menuItem);
                inventory.setSlot(menuItem.getItem());
            }
        }
    }

    public void display(final Player player) {
        final CancelableHook hook = new MenuOpenHook(player, this);
        Canary.hooks().callHook(hook);
        if (!hook.isCanceled()) {
            player.openInventory(inventory);
        }
    }

    public void clear() {
        inventory.clearContents();
        menuItems.clear();
    }

    public void destroy() {
        menuItems.clear();
        inventory.clearContents();
        Canary.hooks().unregisterPluginListener(this);
    }

    @HookHandler
    public void onSlotClick(final SlotClickHook hook) {
        if (hook.getInventory() == inventory) {
            final Item item = hook.getItem();
            if (item != null) {
                final MenuItem menuItem = menuItems.get(item.getDisplayName());
                if (menuItem != null && !menuItem.isDisabled()) {
                    Canary.hooks().callHook(new MenuItemSelectHook(hook.getPlayer(), this, menuItem));
                }
            }
            hook.setCanceled();
        }
    }

    @HookHandler
    public void onInventoryClose(final InventoryHook hook) {
        if (hook.isClosing() && hook.getInventory() == inventory) {
            Canary.hooks().callHook(new MenuCloseHook(hook.getPlayer(), this));
        }
    }
}
