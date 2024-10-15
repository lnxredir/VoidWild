**WildVOID** is a custom Minecraft plugin designed to teleport players to random coordinates in your world. It provides an exciting and adventurous experience for players by teleporting them to a random location with a platform generated beneath their feet, ensuring safe landings. The plugin is highly configurable, allowing server administrators to customize the teleportation radius, platform material, cooldown duration, and more.

## Features
- **Random Teleportation**: Players are teleported to random coordinates, ensuring a minimum distance away from the spawn point.
- **2x2 Platform Generation**: A configurable 2x2 platform is generated beneath the player before teleportation to prevent falling into voids or hazardous locations.
- **Customizable Cooldown**: Set a cooldown period between uses of the teleport command to prevent abuse.
- **First-Time Message**: When players use the `/rtp` or `/wild` command for the first time, a customizable welcome message is displayed on their screen using the `/title` command.
- **Configurable Platform Material**: Customize the block type used to generate the 2x2 platform underneath the player.
- **Configurable Teleportation Radius**: Control the maximum radius and Y-coordinate range for teleportation, ensuring players are sent to appropriate locations.
- **Spawn Safe Zone**: Players are teleported at least a specified distance away from the world spawn to avoid interference with central areas.
- **Customizable Messages**: All in-game messages, including teleport notifications and cooldown warnings, can be customized in the `config.yml`.
- **Permissions**: Full permission control over who can use the `/rtp` or `/wild` commands.

## Commands
- `/rtp`: Teleports the player to a random location within the configured radius.
- `/wild`: Alias for `/rtp`.

## Permissions
- `wildvoid.use`: Allows the player to use the `/rtp` and `/wild` commands.
  
## Configuration

All plugin settings are fully configurable via the `config.yml` file:

```yaml
# Cooldown time in seconds
cooldown: 30

# Material used for the platform
platform-material: OBSIDIAN

# Radius for random X and Z coordinates
radius: 5000

# Minimum and maximum Y values for teleportation
min-y: 30
max-y: 150

# Minimum distance from spawn for teleportation
min-distance-from-spawn: 1000

# Spawn coordinates for the world
spawn-coordinates:
  x: 0.0
  y: 64.0
  z: 0.0

# Messages with color codes (& for colors)
teleport-message: '&6You have been teleported to random coordinates!'
cooldown-message: '&cYou must wait before teleporting again.'
first-time-message: '&aWelcome to the wild!'
command-usage: '&aYou will be teleported in 3 seconds...'
only-players-message: '&cOnly players can use this command!'
```

### Example Configurable Options:

- **Platform Material**: Change the platform beneath the player to any valid block type (`STONE`, `GLASS`, `DIRT`, etc.).
- **Cooldown**: Control how long players must wait before using the command again.
- **Teleportation Range**: Set minimum and maximum coordinates to ensure players are teleported to safe and interesting locations.
- **Custom Messages**: Tailor all plugin messages to your server's theme and style.
- **Title Appearance**: Customize how long the first-time title message appears on the player's screen.

## Installation

1. **Download the plugin JAR**: Place the `WildVOID.jar` file in your server's `plugins` directory.
2. **Start your server**: Allow the server to start up and load the plugin.
3. **Edit the config.yml**: Customize the plugin's settings to fit your server.
4. **Reload/Restart the server**: Apply the changes by restarting your server or using a plugin reload command.
5. **Assign Permissions**: Ensure players have the necessary permissions (`wildvoid.use`) to use the random teleport commands.
