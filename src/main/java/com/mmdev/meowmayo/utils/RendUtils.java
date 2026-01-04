package com.mmdev.meowmayo.utils;

public class RendUtils {
    public static String getDamageColor(double i) {
        if (i >= 2100 && i <= 4200) {
            return "§c§l";
        }
        if (i >= 4200 && i <= 7200) {
            return "§e§l";
        }
        if (i >= 7200) {
            return "§a§l";
        }
        return "§f§l";
    }

    public static String formatHealth(double hp) {
        if (hp > 1e9) {
            return String.format("%.2fb", hp/1e9);
        }
        if (hp > 1e6) {
            return String.format("%.2fm", hp/1e6);
        }
        if (hp > 1e3) {
            return String.format("%.2fk", hp/1e3);
        }
        return String.format("%.2f", hp);
    }

    public static String pullConfidence(int pulls) {
        switch (pulls) {
            case 0:
                return "";
            case 1:
                return "§r| §31h §r- §aSolo Pull";
            case 2:
                return "§r| §32h §r- §eDuo Pull";
            default:
                return "§r| §33+h §r- §cMulti Pull";
        }
    }
}
