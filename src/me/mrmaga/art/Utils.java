package me.mrmaga.art;

import org.bukkit.ChatColor;

import java.util.Calendar;
import java.util.Collection;

public class Utils {
    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String unColor(String msg) {
        return ChatColor.stripColor(msg);
    }

    public static boolean containsIgnoreCase(Collection<String> collection, String str) {
        for (String s : collection) {
            if (s.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    public static String formatTime(long time) {
        long s = time / 1000;
        long m;
        long h;
        long d;
        if (s < 60) {
            return s + " сек.";
        }
        if (s < 3600) {
            m = s / 60;
            s %= 60;
            return m + " мин. " + s + " сек.";
        }
        if (s < 86400) {
            h = s / 3600;
            s %= 3600;
            m = s / 60;
            s %= 60;
            return h + " ч. " + m + " мин. " + s + " сек.";
        }
        d = s / 86400;
        s %= 86400;
        h = s / 3600;
        s %= 3600;
        m = s / 60;
        s %= 60;
        return d + " д. " + h + " ч. " + m + " мин. " + s + " сек.";
    }

    public static long convertToMill(String time) {
        Calendar cal = Calendar.getInstance();
        for (String i : time.split(" ")) {
            if (i.contains("D") || i.contains("d")) {
                cal.add(Calendar.DATE, Integer.parseInt(i.replace("D", "").replace("d", "")));
            }
            if (i.contains("H") || i.contains("h")) {
                cal.add(Calendar.HOUR, Integer.parseInt(i.replace("H", "").replace("h", "")));
            }
            if (i.contains("M") || i.contains("m")) {
                cal.add(Calendar.MINUTE, Integer.parseInt(i.replace("M", "").replace("m", "")));
            }
            if (i.contains("S") || i.contains("s")) {
                cal.add(Calendar.SECOND, Integer.parseInt(i.replace("S", "").replace("s", "")));
            }
        }
        return cal.getTimeInMillis() - System.currentTimeMillis();
    }
}
