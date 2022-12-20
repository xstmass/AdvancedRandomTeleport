package me.mrmaga.art.managers;

import com.earth2me.essentials.utils.LocationUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.mrmaga.art.Main;
import me.mrmaga.art.Utils;
import me.mrmaga.art.yml.MainConfig;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TeleportManager {

    private final Main main;
    private final MainConfig config;

    public TeleportManager(Main main) {
        this.main = main;
        this.config = main.getMainConfig();
    }

    public static boolean isSafeLocation(Location location) {
        Block feet = location.getBlock();
        if (!feet.getType().isTransparent() && !feet.getLocation().add(0, 1, 0).getBlock().getType().isTransparent()) {
            return false; // not transparent (will suffocate)
        }
        Block head = feet.getRelative(BlockFace.UP);
        if (!head.getType().isTransparent()) {
            return false; // not transparent (will suffocate)
        }
        Block ground = feet.getRelative(BlockFace.DOWN);
        return ground.getType().isSolid(); // not solid
    }

    public void tryRandom(String world) {
        for (World worlds : Bukkit.getWorlds()) {
            System.out.println(worlds.getName());
        }
        if (Bukkit.getWorld(world) != null) {
            System.out.println(findRandomLocation(Bukkit.getWorld(world)));
        }
    }

    public String randomTeleport(Player player) {
        World toWorld;
        boolean toSpecificWorld = config.get().getBoolean("rtp-to-another-world.enabled");
        if (toSpecificWorld) {
            toWorld = Bukkit.getWorld(config.get().getString("rtp-to-another-world.world"));
            if (toWorld == null) toWorld = Bukkit.getWorld("world");
            main.getLogger().warning(ChatColor.RED + "Указан несуществующий мир для данного вида телепортации! Используем основной мир.");
        } else toWorld = player.getWorld();
        Location targetSpot;
        try {
            targetSpot = LocationUtil.getSafeDestination(findRandomLocation(toWorld));
        } catch (Exception ex) {
            targetSpot = findRandomLocation(toWorld);
        }
        String pos;
        if (toSpecificWorld && player.getWorld() != toWorld) {
            pos = "§7w=§c" + targetSpot.getWorld().getName() + " " + "§7x=§c" + targetSpot.getBlockX() + " §7y=§c" + targetSpot.getBlockY() + " §7z=§c" + targetSpot.getBlockZ();;
        } else {
            pos = "§7x=§c" + targetSpot.getBlockX() + " §7y=§c" + targetSpot.getBlockY() + " §7z=§c" + targetSpot.getBlockZ();
        }
        player.teleport(targetSpot);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 5, 1));
        return pos;
    }

    public Location findRandomLocation(World world) {
        FileConfiguration cfg = config.get();
        int xmax, xmin, zmax, zmin;
        if (cfg.isSet("worlds." + world.getName())) {
            xmax = cfg.getInt("worlds." + world.getName() + ".x-max");
            xmin = cfg.getInt("worlds." + world.getName() + ".x-min");
            zmax = cfg.getInt("worlds." + world.getName() + ".z-max");
            zmin = cfg.getInt("worlds." + world.getName() + ".z-min");
        } else {
            xmax = cfg.getInt("worlds.default.x-max");
            xmin = cfg.getInt("worlds.default.x-min");
            zmax = cfg.getInt("worlds.default.z-max");
            zmin = cfg.getInt("worlds.default.z-min");
        }
        Material material;
        int randomX;
        int randomY;
        int randomZ;
        do {
            randomX = getRandom(xmin, xmax);
            randomZ = getRandom(zmin, zmax);
            randomY = world.getHighestBlockYAt(randomX, randomZ) - 1;
            material = world.getBlockAt(randomX, randomY, randomZ).getType();
        } while ((config.get().contains("avoid-blocks") && config.getAvoidBlocks().contains(material)) || (config.isWGEnabled() && checkRegions(world, randomX, randomY + 1, randomZ)));
        return new Location(world, (double) randomX + 0.5, (double) randomY + 1, (double) randomZ + 0.5);
    }

    private int getRandom(int min, int max) {
        return (int) (Math.random() * (double) (max - min + 1) + min);
    }

    private boolean checkRegions(World world, int x, int y, int z) {
        WorldGuardPlugin wg = main.getWorldGuard();
        if (wg == null) return false;
        RegionManager manager = wg.getRegionManager(world);
        ApplicableRegionSet set = manager.getApplicableRegions(new Location(world, x, y, z));
        for (ProtectedRegion region : set) {
            if (Utils.containsIgnoreCase(config.getDisallowedRegions(), region.getId())) {
                return true;
            }
        }
        return false;
    }

}
