package com.goodformentertainment.canary.util.menu;

import net.canarymod.api.entity.living.humanoid.Player;

public interface MenuConfiguration {
    void configure(MenuItem[] menuItems);

    void configure(MenuItem[] menuItems, Player player);
}
