package com.lneux.wildvoid;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class WildVOID extends JavaPlugin implements Listener, CommandExecutor {
    private HashMap<UUID, Long> cooldowns = new HashMap<>();
    private HashMap<UUID, Boolean> firstUse = new HashMap<>();
    private long cooldownTime;
    private Material platformMaterial;
    private int radius;
    private int minY;
    private int maxY;
    private int minDistanceFromSpawn;
    private String teleportMessage;
    private String cooldownMessage;
    private String firstTimeMessage;
    private String commandUsage;
    private String onlyPlayersMessage;
    private Location spawnLocation;

    @Override
    public void onEnable() {
        // Save default config.yml
        saveDefaultConfig();

        // Load config options
        cooldownTime = getConfig().getLong("cooldown") * 1000; // Convert to milliseconds
        platformMaterial = Material.valueOf(getConfig().getString("platform-material").toUpperCase());
        radius = getConfig().getInt("radius");
        minY = getConfig().getInt("min-y");
        maxY = getConfig().getInt("max-y");
        minDistanceFromSpawn = getConfig().getInt("min-distance-from-spawn");
        teleportMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("teleport-message"));
        cooldownMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("cooldown-message"));
        firstTimeMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("first-time-message"));
        commandUsage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("command-usage"));
        onlyPlayersMessage = ChatColor.translateAlternateColorCodes('&', getConfig().getString("only-players-message"));

        // Set spawn location from config
        double spawnX = getConfig().getDouble("spawn-coordinates.x");
        double spawnY = getConfig().getDouble("spawn-coordinates.y");
        double spawnZ = getConfig().getDouble("spawn-coordinates.z");
        spawnLocation = new Location(getServer().getWorld("world"), spawnX, spawnY, spawnZ);

        // Register /rtp and /wild commands
        getCommand("rtp").setExecutor(this);
        getCommand("wild").setExecutor(this);

        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("WildVOID has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("WildVOID has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerId = player.getUniqueId();
            long currentTime = System.currentTimeMillis();

            // Handle cooldown
            if (cooldowns.containsKey(playerId) && (currentTime - cooldowns.get(playerId)) < cooldownTime) {
                player.sendMessage(cooldownMessage);
                return true;
            }

            // Check if the player is moving
            Location initialLocation = player.getLocation();
            player.sendMessage(commandUsage);
            cooldowns.put(playerId, currentTime);

            // Show custom message on first use
            if (!firstUse.containsKey(playerId)) {
                firstUse.put(playerId, true);
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (initialLocation.distanceSquared(player.getLocation()) > 0) {
                        player.sendMessage(ChatColor.RED + "You cannot teleport while moving.");
                        return;
                    }
                    teleportPlayer(player);
                }
            }.runTaskLater(this, 60L); // 60L = 3 seconds (20 ticks per second)

            return true;
        } else {
            sender.sendMessage(onlyPlayersMessage);
            return true;
        }
    }

    private void teleportPlayer(Player player) {
        Random random = new Random();
        Location randomLocation;

        do {
            // Generate random X and Z within the given radius
            int x = random.nextInt(radius * 2) - radius;
            int z = random.nextInt(radius * 2) - radius;
            int y = random.nextInt(maxY - minY + 1) + minY; // Random Y between minY and maxY
            randomLocation = new Location(player.getWorld(), x, y, z);
        } while (randomLocation.distance(spawnLocation) < minDistanceFromSpawn); // Ensure distance from spawn

        // Generate platform underneath the location
        generatePlatform(randomLocation.clone().subtract(0, 1, 0));

        // Teleport player after platform has been generated
        player.teleport(randomLocation);
        player.sendMessage(teleportMessage);

        // Show title message after 3 seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                if (firstUse.containsKey(player.getUniqueId())) {
                    // Send title message using Paper API
                    player.sendTitle(
                            ChatColor.translateAlternateColorCodes('&', firstTimeMessage),
                            "",
                            10, // Fade in
                            70, // Stay
                            20  // Fade out
                    );
                }
            }
        }.runTaskLater(this, 60L); // 60L = 3 seconds (20 ticks per second)
    }

    private void generatePlatform(Location loc) {
        // Set the platform blocks (2x2 around the center)
        loc.getBlock().setType(platformMaterial); // Central block
        loc.clone().add(1, 0, 0).getBlock().setType(platformMaterial);
        loc.clone().add(0, 0, 1).getBlock().setType(platformMaterial);
        loc.clone().add(1, 0, 1).getBlock().setType(platformMaterial);
    }
}
