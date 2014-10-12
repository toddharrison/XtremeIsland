package com.eharrison.canary.xis;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.hook.Hook;

public class XExitHook extends Hook {
	private final Player player;
	private final Cause cause;
	
	public XExitHook(final Player player, final Cause cause) {
		this.player = player;
		this.cause = cause;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Cause getCause() {
		return cause;
	}
	
	public enum Cause {
		EXIT, DEATH
	}
}
