package com.eharrison.canary.xis;

import java.util.List;
import java.util.Map;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;
import net.canarymod.hook.HookHandler;
import net.canarymod.plugin.PluginListener;

import com.eharrison.canary.util.InventoryUtil;
import com.eharrison.canary.util.menu.DynaMenuItem;
import com.eharrison.canary.util.menu.IMenuItem;
import com.eharrison.canary.util.menu.Menu;
import com.eharrison.canary.util.menu.hook.MenuSelectHook;
import com.eharrison.canary.xis.dao.XChallenge;
import com.eharrison.canary.xis.dao.XChallengeLevel;
import com.eharrison.canary.xis.dao.XPlayer;

public class XChallengeManager implements PluginListener {
	private final XPlayerManager playerManager;
	private final Menu menu;
	
	public XChallengeManager(final XPlugin plugin, final XPlayerManager playerManager)
			throws DatabaseReadException {
		this.playerManager = playerManager;
		
		menu = new Menu("XtremeIsland Challenges", 6);
		final List<XChallengeLevel> levels = XChallengeLevel.getAllXChallengeLevels();
		for (int row = 0; row < levels.size(); row++) {
			final XChallengeLevel level = levels.get(row);
			final List<XChallenge> challenges = XChallenge.getXChallengesForLevel(level.name);
			for (int column = 0; column < challenges.size(); column++) {
				final XChallenge challenge = challenges.get(column);
				final int slot = row * 9 + column;
				final DynaMenuItem menuItem = new DynaMenuItem(challenge.name, challenge.description,
						ItemType.WoolLightGreen, slot);
				menuItem.addMode("completed", challenge.name, challenge.description, ItemType.WoolYellow);
				menuItem.addMode("noRepeat", challenge.name, challenge.description, ItemType.WoolRed, true);
				menuItem
						.addMode("disabled", challenge.name, challenge.description, ItemType.WoolGray, true);
				menu.setMenuItem(menuItem);
			}
		}
		Canary.hooks().registerListener(menu, plugin);
	}
	
	public void openMenu(final Player player) {
		menu.open(player);
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
	public void onMenuSelect(final MenuSelectHook hook) throws DatabaseReadException,
			DatabaseWriteException {
		final Menu menu = hook.getMenu();
		if (this.menu == menu) {
			final Player player = hook.getPlayer();
			final IMenuItem menuItem = hook.getMenuItem();
			XPlugin.logger.info(player.getDisplayName() + " selected " + menuItem.getName() + " from "
					+ menu.getName());
			
			if (completeChallenge(player, menuItem.getName())) {
				if (menuItem instanceof DynaMenuItem) {
					final DynaMenuItem dMenuItem = (DynaMenuItem) menuItem;
					// TODO change the state of the menu options
					// dMenuItem.setMode("disabled");
				}
			}
		}
	}
}
