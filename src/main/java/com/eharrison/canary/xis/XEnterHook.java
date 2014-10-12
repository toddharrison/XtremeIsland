package com.eharrison.canary.xis;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.position.Location;
import net.canarymod.hook.Hook;

public class XEnterHook extends Hook {
	private final Player player;
	private final Location returnLocation;
	
	public XEnterHook(final Player player) {
		this.player = player;
		returnLocation = null;
	}
	
	public XEnterHook(final Player player, final Location returnLocation) {
		this.player = player;
		this.returnLocation = returnLocation;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Location getReturnLocation() {
		return returnLocation;
	}
}
