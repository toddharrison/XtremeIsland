XtremeIsland
============
v.0.1.0

XtremeIsland is a single player game where you are transported to an empty world where all you have is a tiny island and a meager set of supplies to survive. Challenges may be completed for additional resources and your overall score is tracked realtime against other players. You get one chance to expand your island score as high as possible before falling prey to clumsiness or the dangers of a ruthless world. The difficulty is hard and natural health recovery is disabled. If you die, it's all over. Your island is removed and your score reset.

XtremeIsland is built against the latest 1.2.0-SNAPSHOT of [canarymod](http://www.canarymod.net/) and supports Minecraft 1.8.

**WARNING:
This is still a beta plugin and there have been observed bugs. Please backup your server regularly and report any bugs that you find. Wait 30 seconds or so after you die before executing `/xis` again until I add the forced wait.**

## Installation
XtremeIsland requires PlayerState v.0.1.2 to be deployed.

Deploy the XtremeIsland plugin in the canary plugins directory. The first time it starts it will automatically create the default data files required. These files can be modified to change the challenges available and documentation for configuration will be provided later.

### Permissions
The XtremeIsland commands are controlled by the `xis.command` permission.

## Commands
To enter XtremeIsland, use the command `/xis` or `/xisland`. This will transport you to your island. When you are ready to take a break, use `/xis exit` and it will return you to exactly where you were before.

While in XtremeIsland you can execute `/xis` again and this will pull up a graphical menu of challenges for you to complete. Some of these challenges are based on things on your island (within 10 blocks of you) and some will consume items in your inventory. When you mouse over each it will display what it requires and what the rewards are.

The challenges are color coded:

* Green challenges are ones that are currently available.
* Blue challenges are repeatable and usually offer reduced rewards.
* Gray challenges are not available.

The challenges are organized into levels, which will be relevant in the future.

## Features
* Graphical challenge menu (a generalized component which will be eventually turned into an external utility and API for other plugins)
* Inventory management between worlds using PlayerState
* Destroys islands on a separate thread to lessen impact on server
* Scoreboard for players based upon the work they do on their island

## Known Issues / TODO
* Implement level restrictions and requirements for challenges
* Allow more configuration options for XtremeIsland settings
* Force player to wait after they respawn from death before running `/xis` so that island can completely delete first
* Abstract score to a set of levels
* Vary scores based upon block types
* Add to score by completing challenges
* Remember player top scores
* Disable "dangerous" commands in XIS like `/spawn` and `/home`
