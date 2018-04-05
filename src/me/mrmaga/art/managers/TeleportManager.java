package me.mrmaga.art.managers;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.mrmaga.art.Main;
import me.mrmaga.art.Utils;
import me.mrmaga.art.yml.MainConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TeleportManager {

    private Main main;
    private MainConfig config;

    public TeleportManager(Main main) {
        this.main = main;
        this.config = main.getMainConfig();
    }

    public String randomTeleport(Player player) {
        Location spot = findRandomLocation(player.getWorld());
        player.teleport(spot);
        return spot.getBlockX() + " " + spot.getBlockY() + " " + spot.getBlockZ();
    }

    private Location findRandomLocation(World world) {
        FileConfiguration cfg = config.get();
        int xmax = cfg.getInt("x-max");
        int xmin = cfg.getInt("x-min");
        int zmax = cfg.getInt("z-max");
        int zmin = cfg.getInt("z-min");
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
        ApplicableRegionSet set = manager.getApplicableRegions(new Location(world, (double)x, (double)y, (double)z));
        for (ProtectedRegion region : set) {
            if (Utils.containsIgnoreCase(config.getDisallowedRegions(), region.getId())) {
                return true;
            }
        }
        return false;
    }

}
