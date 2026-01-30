package com.mmdev.meowmayo.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMagmaCube;

public class KuudraUtils {
    public static EntityMagmaCube getKuudra() {
        for (Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (entity instanceof EntityMagmaCube) {
                EntityMagmaCube cube = (EntityMagmaCube) entity;

                if (cube.getSlimeSize() >= 15 ) return cube;
            }
        }
        return null;
    }
}
