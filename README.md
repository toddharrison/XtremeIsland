XtremeIsland
============

XtremeIsland is a single player game where you are transported to an empty world where all you have
is a tiny island and a meager set of supplies to survive. Challenges may be completed for additional
resources and your overall score is tracked realtime against other players. You get one chance to
expand your island score as high as possible before falling prey to clumsiness or the dangers of a
ruthless world. The difficulty is hard and natural health recovery is disabled. If you die, it's all
over. Your island is removed and your score reset.

XtremeIsland is built against the latest 1.2.1-SNAPSHOT of [canarymod](http://www.canarymod.net/)
and supports Minecraft 1.8.

## Installation
XtremeIsland requires **PlayerState v.1.x** and **Zown v.1.x** to be deployed.

Deploy the XtremeIsland plugin in the canary plugins directory. The first time it starts it will
automatically create the default data files required. These files can be modified to change the
challenges available and documentation for configuration will be provided later.

### Permissions
The XtremeIsland commands are controlled by the `xis.command` permission.

## Commands
To see help on the commands for XtremeIsland, enter the command `/xis` or `/xisland`.

### XIS Go
Entering `/xis go` or `/xis g` will take you to your island.

### XIS Exit
When you are ready to take a break, use `/xis exit` or `/xis e` and it will return you to exactly
where you were before.

### XIS Challenges
While in XtremeIsland you can execute `/xis challenges` or `/xis c` and this will pull up a
graphical menu of challenges for you to complete. Some of these challenges are based on things on
your island (within 10 blocks of you) and some will consume items in your inventory. When you mouse
over each it will display what it requires and what the rewards are.

The challenges are color coded:

* Green challenges are ones that are currently available.
* Blue challenges are repeatable and usually offer reduced rewards.
* Gray challenges are not available.

The challenges are organized into levels, which will be relevant in the future.

### XIS List
You can use the command `/xis listplayers` or `/xis l` to show who's playing right now.

### XIS Top Scores
You can use the command `/xis topscores` or `/xis t` to show the all-time top scores for all players.

## Features
* Graphical challenge menu (a generalized component which will be eventually turned into an external utility and API for other plugins)
* Inventory management between worlds using PlayerState
* Destroys islands on a separate thread to lessen impact on server
* Scoreboard for players based upon the work they do on their island
* Forces the player to wait until the island is deleted before allowing them to return
* Level restrictions and requirements for challenges
* Vary scores based upon block types
* Add to score by completing challenges
* Remember player top scores

## Known Issues / TODO
* Allow more configuration options for XtremeIsland settings
* Abstract score to a set of levels
