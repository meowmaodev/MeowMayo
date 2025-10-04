package com.mmdev.meowmayo.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.regex.*;

public class ChatUtils {
    public static void allChat(String message) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/ac " + message);
    }

    public static void guildChat(String message) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/gc " + message);
    }

    public static void partyChat(String message) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/pc " + message);
    }

    public static void command(String command) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/" + command);
        System.out.println("Executing command: " + command);
    }

    public static void system(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§e§lMeow§f§lMayo §a> §r" + message));
    }

    public static boolean isSystemMessage(String message) {
        return (!(message.startsWith("Party >") || message.startsWith("Guild >") || message.startsWith("From ") || message.startsWith("To ")));
    }


    private static final Pattern rank = Pattern.compile("\\[.{3,8}]");
    public static String stripRank(String name) {
        Matcher removed = rank.matcher(name);
        return removed.replaceAll("").replace(" ", "");
    }

    public static String formatTime(double seconds) {
        if (seconds < 60.0) {
            return String.format("%.2fs", seconds);
        }

        double secs = seconds % 60;
        int minutes = (int) Math.floor(seconds / 60.0);

        if (minutes > 60) {
            int hours = minutes / 60;
            minutes = minutes % 60;

            if (hours > 24) {
                int days = hours / 24;
                hours = hours % 24;
                return String.format("%dd %dh %dm %.2fs", days, hours, minutes, secs);
            }

            return String.format("%dh %dm %.2fs", hours, minutes, secs);
        }

        return String.format("%dm %.2fs", minutes, secs);
    }
}