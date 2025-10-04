package com.mmdev.meowmayo.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class RenderShapeUtils {

    /**
     * Draws a wireframe box in the world, centered on the given coordinates.
     *
     * @param x World X coordinate
     * @param y World Y coordinate
     * @param z World Z coordinate
     * @param w Width of box (X-axis)
     * @param h Height of box (Y-axis)
     * @param d Depth of box (Z-axis)
     * @param r Red (0–1)
     * @param g Green (0–1)
     * @param b Blue (0–1)
     * @param a Alpha (0–1)
     * @param phase If true, box is visible through walls
     * @param partialTicks Interpolation from RenderWorldLastEvent
     */
    public static void drawBox(double x, double y, double z,
                               double w, double h, double d,
                               float r, float g, float b, float a,
                               boolean phase, float partialTicks) {

        Minecraft mc = Minecraft.getMinecraft();
        Entity viewer = mc.getRenderViewEntity();

        if (viewer == null) return;

        // Interpolated camera position
        double interpX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        double interpY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        double interpZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        // Offset relative to camera
        double cx = x - interpX;
        double cy = y - interpY;
        double cz = z - interpZ;

        // Half-sizes to center the box
        double hw = w / 2.0;
        double hh = h / 2.0;
        double hd = d / 2.0;

        AxisAlignedBB aabb = new AxisAlignedBB(
                cx - hw, cy - hh, cz - hd,
                cx + hw, cy + hh, cz + hd
        );

        // Rendering
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(r, g, b, a);

        if (phase) GlStateManager.disableDepth(); else GlStateManager.enableDepth();

        RenderGlobal.drawSelectionBoundingBox(aabb);

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        if (phase) GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
}
