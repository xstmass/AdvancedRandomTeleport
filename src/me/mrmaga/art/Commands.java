package me.mrmaga.art;

import me.mrmaga.art.managers.CooldownManager;
import me.mrmaga.art.managers.TeleportManager;
import me.mrmaga.art.yml.LanguageConfig;
import me.mrmaga.art.yml.MainConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private MainConfig config;
    private LanguageConfig lang;
    private TeleportManager tm;
    private CooldownManager cm;

    Commands(Main main) {
        this.config = main.getMainConfig();
        this.lang = main.getLanguageConfig();
        this.tm = main.getTeleportManager();
        this.cm = main.getCooldownManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        if (!(sender instanceof Player) && args.length == 0) {
            sender.sendMessage(lang.getMsg("only-players"));
            return true;
        }
        if (args.length >= 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("art.reload")) {
            config.reload();
            lang.reload();
            sender.sendMessage(lang.getPrefixedMsg("reload"));
            return true;
        }
        Player player = null;
        boolean self = true;
        if (args.length == 0) {
            player = (Player) sender;
        } else if (sender.hasPermission("art.others")) {
            player = Bukkit.getPlayer(args[0]);
            self = false;
        }
        if (player == null) {
            if (self) {
                sender.sendMessage(lang.getMsg("no-permissions"));
            } else {
                sender.sendMessage(lang.getPrefixedMsg("player-not-online").replace("%player%", args[0]));
            }
            return true;
        }
        if (!player.hasPermission("art.self") && self) {
            player.sendMessage(lang.getMsg("no-permissions"));
            return true;
        }
        if (config.get().contains("disabled-worlds") && Utils.containsIgnoreCase(config.getDisabledWorld(), player.getWorld().getName())) {
            sender.sendMessage(lang.getPrefixedMsg("world-disabled"));
            return true;
        }
        boolean cooldownEnabled = config.isCooldownEnabled() && !player.hasPermission("art.bypasscd");
        if (cooldownEnabled) {
            long time = cm.getWaitTime(player.getName());
            if (time > 0) {
                sender.sendMessage(lang.getPrefixedMsg("wait-cooldown").replace("%time%", Utils.formatTime(time)));
                return true;
            }
        }
        String coords = tm.randomTeleport(player);
        player.sendMessage(lang.getPrefixedMsg(self ? "teleport-self" : "teleported-by-other").replace("%coords%", coords));
        if (!self) player.sendMessage(lang.getPrefixedMsg("you-teleported-other")
                .replace("%player%", player.getDisplayName())
                .replace("%coords%", coords));
        if (cooldownEnabled) {
            cm.addCooldown(player);
        }
        return true;
    }
}
