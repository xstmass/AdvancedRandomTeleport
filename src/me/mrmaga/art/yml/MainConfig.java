package me.mrmaga.art.yml;

import me.mrmaga.art.Main;
import me.mrmaga.art.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MainConfig extends CustomConfig {

    public MainConfig(Main main) {
        super(main, "config", true);
    }

    public String getPrefix() {
        return Utils.color(yml.getString("prefix"));
    }

    public boolean isCooldownEnabled() {
        try {
            return yml.getBoolean("cooldown-enabled");
        } catch (NullPointerException npe) {
            return false;
        }
    }

    public boolean isWGEnabled() {
        try {
            return yml.getBoolean("enable-worldguard-cheks");
        } catch (NullPointerException npe) {
            return false;
        }
    }

    public List<String> getDisallowedRegions() {
        return yml.getStringList("disallowed-regions");
    }

    public List<String> getDisabledWorld() {
        return yml.getStringList("disabled-worlds");
    }

    public List<Material> getAvoidBlocks() {
        List<Material> list = new ArrayList<>();
        for (String type : yml.getStringList("avoid-blocks")) {
            list.add(Material.matchMaterial(type));
        }
        return list;
    }

    public long getCooldownTime(Player player) {
        long min = Long.MAX_VALUE;
        for (String group : yml.getConfigurationSection("groups-cooldown").getKeys(false)) {
            if (!player.hasPermission("art.cooldown." + group)) {
                continue;
            }
            long time = Utils.convertToMill(yml.getString("groups-cooldown." + group));
            if (time >= min) {
                continue;
            }
            min = time;
        }
        if (min == Long.MAX_VALUE) {
            min = Utils.convertToMill(yml.getString("standart-cooldown"));
        }
        return min;
    }
}
