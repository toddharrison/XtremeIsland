package com.goodformentertainment.canary.util.menu;

import net.canarymod.Canary;
import net.canarymod.api.factory.ItemFactory;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.ItemType;

public class MenuItem {
    private static final ItemFactory FACTORY = Canary.factory().getItemFactory();

    private final String name;
    private final int slot;

    private String[] description;
    private ItemType icon;
    private boolean disabled;
    private Menu menu;

    public MenuItem(final String name, final ItemType icon, final int slot) {
        this(name, null, icon, slot, false);
    }

    public MenuItem(final String name, final String[] description, final ItemType icon, final int slot) {
        this(name, description, icon, slot, false);
    }

    public MenuItem(final String name, final String[] description, final ItemType icon,
                    final int slot, final boolean disabled) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.slot = slot;
        this.disabled = disabled;
    }

    public MenuItem(final MenuItem menuItem) {
        this(menuItem.name, menuItem.description, menuItem.icon, menuItem.slot, menuItem.disabled);
    }

    public String getName() {
        return name;
    }

    public String[] getDescription() {
        return description;
    }

    public void setDescription(final String[] description) {
        this.description = description;
    }

    public ItemType getIcon() {
        return icon;
    }

    public void setIcon(final ItemType icon) {
        if (!this.icon.equals(icon)) {
            this.icon = icon;
        }
    }

    public Item getItem() {
        final Item item = FACTORY.newItem(icon);
        item.setDisplayName(name);
        if (description != null) {
            item.setLore(description);
        }
        item.setAmount(1);
        item.setSlot(slot);
        return item;
    }

    public int getSlot() {
        return slot;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(final boolean disabled) {
        if (disabled != this.disabled) {
            this.disabled = disabled;
        }
    }

    public void setMenu(final Menu menu) {
        this.menu = menu;
    }

    public void update() {
        if (menu != null) {
            menu.setMenuItems(this);
        }
    }
}
