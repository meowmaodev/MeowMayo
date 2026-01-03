package com.mmdev.meowmayo.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class RenderShapeUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void drawBox(double x, double y, double z,
                               double w, double h, double d,
                               float r, float g, float b, float a,
                               boolean phase, float partialTicks)
    {

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
        GlStateManager.disableCull();

        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA,1 , 0);

        GL11.glLineWidth(2F);

        GlStateManager.resetColor();
        GlStateManager.color(r, g, b, a);

        if (phase) GlStateManager.disableDepth(); else GlStateManager.enableDepth();
        GlStateManager.depthMask(!phase);

        RenderGlobal.drawSelectionBoundingBox(aabb);

        GL11.glLineWidth(1F);

        if (phase) {
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
        }

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.resetColor();

        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    // cat gpt
    public static void drawFilledBox(double x, double y, double z,
                                     double w, double h, double d,
                                     float r, float g, float b, float a,
                                     boolean phase, float partialTicks)
    {

        Entity viewer = mc.getRenderViewEntity();
        if (viewer == null) return;

        double cx = x - (viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks);
        double cy = y - (viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks);
        double cz = z - (viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks);

        double hx = w / 2.0;
        double hy = h / 2.0;
        double hz = d / 2.0;

        AxisAlignedBB aabb = new AxisAlignedBB(
                cx - hx, cy - hy, cz - hz,
                cx + hx, cy + hy, cz + hz
        );

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();

        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA,1 , 0);

        GlStateManager.resetColor();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (phase) GlStateManager.disableDepth(); else GlStateManager.enableDepth();
        GlStateManager.depthMask(!phase);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer renderer = tessellator.getWorldRenderer();
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        // Bottom face
        renderer.pos(aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        renderer.pos(aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        renderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        renderer.pos(aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();

        // Top face
        renderer.pos(aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        renderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        renderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        renderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();

        // Front face
        renderer.pos(aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        renderer.pos(aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        renderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        renderer.pos(aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();

        // Back face
        renderer.pos(aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        renderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        renderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        renderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();

        // Left face
        renderer.pos(aabb.minX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        renderer.pos(aabb.minX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();
        renderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        renderer.pos(aabb.minX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();

        // Right face
        renderer.pos(aabb.maxX, aabb.minY, aabb.minZ).color(r, g, b, a).endVertex();
        renderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).color(r, g, b, a).endVertex();
        renderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).color(r, g, b, a).endVertex();
        renderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).color(r, g, b, a).endVertex();

        tessellator.draw();

        GL11.glLineWidth(2F);

        RenderGlobal.drawSelectionBoundingBox(aabb);

        GL11.glLineWidth(1F);

        if (phase) {
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
        }

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.resetColor();

        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    public static void drawFloatingText(String text, double x, double y, double z,
                                        float scale, float partialTicks)
    {
        Entity viewer = mc.getRenderViewEntity();
        FontRenderer fr = mc.fontRendererObj;
        if (viewer == null || fr == null) return;

        // Interpolate camera position
        double camX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks;
        double camY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks;
        double camZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks;

        // Translate to world coordinates relative to camera
        double relX = x - camX;
        double relY = y - camY;
        double relZ = z - camZ;

        GlStateManager.pushMatrix();
        GlStateManager.disableCull();

        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA,1 , 0);

        GlStateManager.resetColor();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);

        GlStateManager.translate(relX, relY, relZ);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0, 1, 0);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, 1, 0, 0);
        GlStateManager.scale(-scale, -scale, scale);

        // Center the text
        int textWidth = fr.getStringWidth(text);
        fr.drawString(text, -textWidth / 2, -fr.FONT_HEIGHT / 2, 0xFFFFFF);

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.resetColor();

        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }
}
