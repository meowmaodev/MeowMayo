package com.mmdev.meowmayo.utils;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.opengl.GL11;

public class TextOverlayUtils {
    private static String currentText = null;
    private static long endTime = 0;

    public static void showOverlayText(String text, long duration) {
        currentText = text;
        endTime = System.currentTimeMillis() + duration;
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event) {
        if (currentText == null) return;
        if (System.currentTimeMillis() > endTime) {
            currentText = null; // expire
            return;
        }

        // Use scaled resolution to respect GUI scale
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int screenWidth = sr.getScaledWidth();
        int screenHeight = sr.getScaledHeight();

        float scale = 2.0f; // 2x bigger text
        int textWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(currentText);

        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, 1.0f);

        // Because scaling changes coordinates, divide by scale to keep position correct
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(
                currentText,
                (int)((screenWidth / 2 - (textWidth * scale) / 2) / scale),
                (int)(screenHeight / 3 / scale),
                0xFFFFFF
        );

        GL11.glPopMatrix();
    }
}
