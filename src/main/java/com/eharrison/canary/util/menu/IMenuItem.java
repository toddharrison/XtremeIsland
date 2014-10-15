package com.eharrison.canary.util.menu;

import net.canarymod.api.inventory.Item;

public interface IMenuItem {
	void setMenu(Menu menu);
	
	String getName();
	
	Item getItem();
	
	boolean isDisabled();
}
