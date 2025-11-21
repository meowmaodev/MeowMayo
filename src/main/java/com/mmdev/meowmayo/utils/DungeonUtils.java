package com.mmdev.meowmayo.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.Collection;

public class DungeonUtils {
    public static Character getPlayerClass() {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc == null || mc.thePlayer.sendQueue == null) return null;

        // tab list shenanigans
        String ign = mc.thePlayer.getName();
        Collection<NetworkPlayerInfo> tabList = mc.thePlayer.sendQueue.getPlayerInfoMap();

        for (NetworkPlayerInfo networkPlayerInfo : tabList) {
            if (networkPlayerInfo.getDisplayName() != null) {
                String line = networkPlayerInfo.getDisplayName().getUnformattedText();
                if (line.contains(ign)) {
                    return line.charAt(line.indexOf('(')+1);
                }
            }
        }
        return null;
    }

    public static String getDragonStatue(double x, double y, double z) {
        if (y < 14 || y > 19) return null;

        if (x >= 27 && x <= 32) {
            if (z > 55 && z < 64) {
                return "Red";
            }
            if (z > 90 && z < 99) {
                return "Green";
            }
        }

        if (x >= 76 && x <= 90) {
            if (z > 90 && z < 99) {
                return "Blue";
            }
            if (z > 48 && z < 63) {
                return "Orange";
            }
        }

        if (x >= 52 && x <= 60 && z >= 121 && z <= 128) {
            return "Purple";
        }

        return null;
    }

    public static String getDragonPrefix(String color) {
        switch (color) {
            case "Red":
                return "§c";
            case "Green":
                return "§a";
            case "Blue":
                return "§b";
            case "Orange":
                return "§6";
            case "Purple":
                return "§d";
            default:
                return "";
        }
    }
}
