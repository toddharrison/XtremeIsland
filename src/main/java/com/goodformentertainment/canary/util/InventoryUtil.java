package com.goodformentertainment.canary.util;

import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.ItemType;

import java.util.Map;

public final class InventoryUtil {
    public static boolean hasItems(final Inventory inv, final Map<ItemType, Integer> requiredItems) {
        boolean allItemsPresent = false;
        for (final ItemType itemType : requiredItems.keySet()) {
            final int count = requiredItems.get(itemType);
            int curCount = 0;
            for (final Item item : inv.getContents()) {
                if (item != null && item.getType() == itemType) {
                    curCount += item.getAmount();
                }
                if (curCount >= count) {
                    allItemsPresent = true;
                    break;
                }
            }
        }
        return allItemsPresent;
    }

    public static boolean removeItems(final Inventory inv, final Map<ItemType, Integer> requiredItems) {
        boolean removedAll = false;
        for (final ItemType itemType : requiredItems.keySet()) {
            final int count = requiredItems.get(itemType);
            int curCount = count;
            for (final Item item : inv.getContents()) {
                if (item != null && item.getType() == itemType) {
                    if (item.getAmount() <= curCount) {
                        inv.removeItem(item);
                        curCount -= item.getAmount();
                    } else {
                        item.setAmount(item.getAmount() - curCount);
                        curCount = 0;
                    }
                }
                if (curCount == 0) {
                    removedAll = true;
                    break;
                }
            }
        }
        return removedAll;
    }
}
