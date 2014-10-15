package com.eharrison.canary.xis;

import java.util.ArrayList;
import java.util.Map;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.inventory.Inventory;
import net.canarymod.api.inventory.ItemType;
import net.canarymod.database.exceptions.DatabaseReadException;

import com.eharrison.canary.xis.dao.XChallenge;

public class XChallengeManager {
	public XChallengeManager() {
		try {
			final XChallenge challenge = new XChallenge();
			challenge.name = "cobblegenerator";
			challenge.level = "Survivalist";
			challenge.description = "Create a cobblestone generator and mine 32 cobblestone.";
			challenge.type = "onPlayer";
			challenge.itemsRequired = new ArrayList<String>();
			challenge.itemsRequired.add("cobblestone:32");
			challenge.consumeItems = true;
			challenge.itemsReward = new ArrayList<String>();
			challenge.itemsReward.add("cactus:1");
			challenge.itemsReward.add("grass:5");
			challenge.rewardDescription = "1 cactus and 5 grass blocks";
			challenge.repeatable = false;
			challenge.itemsRepeatReward = new ArrayList<String>();
			challenge.itemsRepeatReward.add("grass:1");
			challenge.repeatRewardDescription = "1 grass block";
			challenge.update();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean completeChallenge(final Player player, final String name)
			throws DatabaseReadException {
		boolean success = false;
		
		final XChallenge xChallenge = XChallenge.getXChallenge(name);
		if (xChallenge != null) {
			switch (xChallenge.type) {
				case "onPlayer":
					// Verify the player has the needed items
					final Inventory inv = player.getInventory();
					final Map<ItemType, Integer> requiredItems = xChallenge.getItemsRequired();
					boolean allItemsPresent = true;
					for (final ItemType itemType : requiredItems.keySet()) {
						if (!inv.hasItemStack(itemType, requiredItems.get(itemType))) {
							allItemsPresent = false;
							break;
						}
					}
					
					if (allItemsPresent) {
						// Remove items from player's inventory if appropriate to the challenge
						if (xChallenge.consumeItems) {
							for (final ItemType itemType : requiredItems.keySet()) {
								inv.getItem(itemType, requiredItems.get(itemType));
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
		
		return success;
	}
}
