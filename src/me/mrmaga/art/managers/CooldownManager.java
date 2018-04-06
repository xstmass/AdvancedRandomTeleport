package me.mrmaga.art.managers;

import me.mrmaga.art.Main;
import me.mrmaga.art.yml.MainConfig;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CooldownManager {

    private MainConfig config;
    private HashMap<String, Long> cd;

    public CooldownManager(Main main) {
        this.config = main.getMainConfig();
        this.cd = new HashMap<>();
    }

    public void addCooldown(Player player) {
        cd.put(player.getName().toLowerCase(), config.getCooldownTime(player) + System.currentTimeMillis());
    }

    public long getWaitTime(String name) {
        name = name.toLowerCase();
        if (!cd.containsKey(name)) {
            return -1;
        }
        return cd.get(name) - System.currentTimeMillis();
    }
}
