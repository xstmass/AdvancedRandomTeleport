package me.mrmaga.art;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.mrmaga.art.managers.CooldownManager;
import me.mrmaga.art.managers.TeleportManager;
import me.mrmaga.art.yml.LanguageConfig;
import me.mrmaga.art.yml.MainConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private MainConfig config;
    private LanguageConfig lang;
    private TeleportManager tm;
    private CooldownManager cm;

    @Override
    public void onEnable() {
        this.config = new MainConfig(this);
        this.lang = new LanguageConfig(this);
        this.tm = new TeleportManager(this);
        this.cm = new CooldownManager(this);
        Commands commands = new Commands(this);
        getCommand("randomteleport").setExecutor(commands);
    }

    public LanguageConfig getLanguageConfig() {
        return lang;
    }

    public MainConfig getMainConfig() {
        return config;
    }

    public TeleportManager getTeleportManager() {
        return tm;
    }

    public CooldownManager getCooldownManager() {
        return cm;
    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");

        if (!(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }
}