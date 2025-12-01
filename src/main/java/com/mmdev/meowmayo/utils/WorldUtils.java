package com.mmdev.meowmayo.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;

public class WorldUtils {
    public static EntityPlayer getPlayerByName(String ign) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld == null || ign == null || ign.isEmpty()) return null;

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (player.getName().equalsIgnoreCase(ign)) {
                    return player;
                }
            }
        }

        return null;
    }

    public static EntityArmorStand getStand(double x, double y, double z) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld == null) return null;

        EntityArmorStand closest = null;
        double bestDist = Double.MAX_VALUE;

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityArmorStand) {
                EntityArmorStand stand = (EntityArmorStand) entity;

                double dx = stand.posX - x;
                double dy = stand.posY - y;
                double dz = stand.posZ - z;
                double distSq = dx * dx + dy * dy + dz * dz;

                if (distSq < bestDist) {
                    bestDist = distSq;
                    closest = stand;
                }
            }
        }

        return closest;
    }
}
