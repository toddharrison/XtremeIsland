package com.eharrison.canary.xis;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.factory.ItemFactory;
import net.canarymod.api.factory.ObjectFactory;
import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.inventory.Item;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.player.SlotClickHook;
import net.canarymod.plugin.PluginListener;

public class XGuiManager implements PluginListener {
	private static final String MENU_NAME = "XtremeIsland Challenge Menu";
	
	private final ObjectFactory oFactory;
	private final ItemFactory iFactory;
	
	public XGuiManager() {
		oFactory = Canary.factory().getObjectFactory();
		iFactory = Canary.factory().getItemFactory();
	}
	
	public void openGui(final Player player) {
		final Inventory inventory = oFactory.newCustomStorageInventory(MENU_NAME, 6);
		
		final Item item = iFactory.newItem(ItemType.Stone);
		item.setDisplayName("Foo");
		item.setAmount(1);
		inventory.setSlot(4, item);
		
		player.openInventory(inventory);
	}
	
	@HookHandler
	public void onSlotClick(final SlotClickHook hook) {
		if (hook.getInventory().getInventoryName().equals(MENU_NAME)) {
			final Player player = hook.getPlayer();
			final Item item = hook.getItem();
			if (item != null) {
				XPlugin.logger.info(player.getDisplayName() + " selected: " + item.getDisplayName());
			}
			
			hook.setCanceled();
		}
	}
}
