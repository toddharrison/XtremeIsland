package com.eharrison.canary.xis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;
import net.canarymod.hook.HookHandler;
import net.canarymod.plugin.PluginListener;

import com.eharrison.canary.util.InventoryUtil;
import com.eharrison.canary.util.menu.Menu;
import com.eharrison.canary.util.menu.MenuConfiguration;
import com.eharrison.canary.util.menu.MenuFactory;
import com.eharrison.canary.util.menu.MenuItem;
import com.eharrison.canary.util.menu.hook.MenuItemSelectHook;
import com.eharrison.canary.xis.dao.XChallenge;
import com.eharrison.canary.xis.dao.XChallengeLevel;
import com.eharrison.canary.xis.dao.XPlayer;

public class XChallengeManager implements PluginListener {
	private final XPlayerManager playerManager;
	private final MenuFactory menuFactory;
	
	public XChallengeManager(final XPlugin plugin, final XPlayerManager playerManager)
			throws DatabaseReadException {
		this.playerManager = playerManager;
		
		final List<MenuItem> menuItems = new ArrayList<MenuItem>();
		
		final List<XChallengeLevel> levels = XChallengeLevel.getAllXChallengeLevels();
		for (int row = 0; row < levels.size(); row++) {
			final List<XChallenge> challenges = XChallenge.getXChallengesForLevel(levels.get(row).name);
			for (int column = 0; column < challenges.size(); column++) {
				final XChallenge challenge = challenges.get(column);
				menuItems.add(new MenuItem(challenge.name, challenge.description, ItemType.WoolLightGreen,
						row * 9 + column, false));
			}
		}
		
		final MenuConfiguration menuConfig = null;
		menuFactory = new MenuFactory(plugin, "XtremeIsland Challenges", 6, menuConfig,
				menuItems.toArray(new MenuItem[menuItems.size()]));
	}
	
	public void openMenu(final Player player) {
		menuFactory.getMenu(player).display(player);
	}
	
	public boolean completeChallenge(final Player player, final String name)
			throws DatabaseReadException, DatabaseWriteException {
		boolean success = false;
		
		final XChallenge xChallenge = XChallenge.getXChallenge(name);
		if (xChallenge != null) {
			switch (xChallenge.type) {
				case "onPlayer":
					// Verify the player has the needed items
					final Inventory inv = player.getInventory();
					final Map<ItemType, Integer> requiredItems = xChallenge.getItemsRequired();
					final boolean allItemsPresent = InventoryUtil.hasItems(inv, requiredItems);
					
					// XPlugin.logger.info("All items present? " + allItemsPresent);
					
					if (allItemsPresent) {
						if (xChallenge.consumeItems) {
							InventoryUtil.removeItems(inv, requiredItems);
						}
						
						// Give player reward
						for (final ItemType itemType : xChallenge.getItemsReward().keySet()) {
							final int count = xChallenge.getItemsReward().get(itemType);
							for (int i = 0; i < count; i++) {
								inv.addItem(itemType);
							}
						}
						
						success = true;
					}
					break;
				case "onIsland":
					break;
				default:
					throw new IllegalStateException("Unrecognized xChallenge type: " + xChallenge.type);
			}
		}
		
		if (success) {
			// Record that the player completed this challenge
			final XPlayer xPlayer = playerManager.getXPlayer(player);
			if (!xPlayer.challengesCompleted.contains(name)) {
				xPlayer.challengesCompleted.add(name);
				xPlayer.update();
				XPlugin.logger.info(player.getDisplayName() + " has completed " + name);
			}
		}
		
		return success;
	}
	
	@HookHandler
	public void onMenuSelect(final MenuItemSelectHook hook) throws DatabaseReadException,
			DatabaseWriteException {
		final Menu menu = hook.getMenu();
		if (menu.getMenuFactory() == menuFactory) {
			final Player player = hook.getPlayer();
			
			final MenuItem menuItem = hook.getMenuItem();
			XPlugin.logger.info(player.getDisplayName() + " selected " + menuItem.getName() + " from "
					+ menu.getTitle());
			
			if (completeChallenge(player, menuItem.getName())) {
				menuItem.setIcon(ItemType.WoolLightGray);
				menuItem.setDisabled(true);
			}
		}
	}
}
