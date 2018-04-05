package me.mrmaga.art.yml;

import me.mrmaga.art.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class CustomConfig {
    YamlConfiguration yml;
    Main plugin;
    private File file;

    public CustomConfig(Main plugin, String name, boolean isResource) {
        this.file = new File(plugin.getDataFolder(), name + ".yml");
        this.plugin = plugin;
        this.file.getParentFile().mkdirs();
        if (!this.file.exists() && isResource) {
            plugin.saveResource(name + ".yml", false);
        }
        yml = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get() {
        return yml;
    }

    public void save() {
        try {
            yml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        try {
            yml.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}