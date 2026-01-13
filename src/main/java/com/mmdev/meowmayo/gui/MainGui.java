package com.mmdev.meowmayo.gui;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainGui extends GuiScreen {
    private static final ResourceLocation LOGO_TEXTURE = new ResourceLocation("meowmayo", "textures/meowmayo.png");

    @Override
    public void initGui() {
        super.initGui();

        this.buttonList.clear();
        this.buttonList.add(new GuiButton(-1, width - 25, 5, 20, 20, "X"));


        this.buttonList.add(new GuiButton(1, 5, 40, 150, 20, "Edit Config"));
        this.buttonList.add(new GuiButton(2, 5, 70, 150, 20, "Edit Gui"));

        for (GuiButton b : this.buttonList) {
            b.enabled = true;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == -1) {
            mc.displayGuiScreen(null);
        }

        if (button.id == 1) {
            GuiHandler.setGuiToOpen(new SettingsGui());
        }

        if (button.id == 2) {
            GuiHandler.setGuiToOpen(new HudLocations());
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String title = "MeowMayo";
        this.drawDefaultBackground();

        drawCenteredString(this.fontRendererObj, title, this.width / 2, 12, 0xFFFFFF);

        drawHorizontalLine(10, width - 10, 30, 0xFFAAAAAA);

        drawWarholGrid(mouseX, mouseY);

        renderRipples();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    // I used ai to create this because it was funny
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        // Grid coordinates (ensure these match your drawing math exactly)
        int gridSize = 6;
        int totalGridSize = Math.min(width / 2 - 40, height - 80);
        int tileSize = (totalGridSize / gridSize) - 2; // -2 for padding
        int startX = width - totalGridSize - 20;
        int startY = 45;

        if (mouseButton == 0) { // Left click only
            for (int row = 0; row < gridSize; row++) {
                for (int col = 0; col < gridSize; col++) {
                    int x = startX + (col * (tileSize + 2));
                    int y = startY + (row * (tileSize + 2));

                    if (mouseX >= x && mouseX < x + tileSize && mouseY >= y && mouseY < y + tileSize) {
                        playCustomSound();

                        int[] randomPalette = {0xFF5555, 0x55FF55, 0x5555FF, 0xFFFF55, 0xFF55FF, 0x55FFFF};
                        int randomColor = randomPalette[(int)(Math.random() * randomPalette.length)];

                        activeRipples.add(new Ripple(mouseX, mouseY, x, y, tileSize, randomColor));
                    }
                }
            }
        }
    }

    private final List<Ripple> activeRipples = new ArrayList<>();

    private void renderRipples() {
        long now = System.currentTimeMillis();
        Iterator<Ripple> it = activeRipples.iterator();

        while (it.hasNext()) {
            Ripple r = it.next();
            float progress = (now - r.startTime) / 800f; // Slower, smoother 800ms

            if (progress >= 1.0f) {
                it.remove();
                continue;
            }

            // Calculate fading alpha
            int alpha = (int) ((1.0f - progress) * 180);
            // Apply the random color stored in the ripple object
            int colorWithAlpha = (alpha << 24) | (r.color & 0x00FFFFFF);

            float currentRadius = r.maxRadius * progress;

            GlStateManager.pushMatrix();
            // Draw the expanding circle
            drawCircle(r.x, r.y, currentRadius, colorWithAlpha);
            GlStateManager.popMatrix();
        }
    }

    private void drawWarholGrid(int mouseX, int mouseY) {
        int gridSize = 6;
        int padding = 2;

        // 1. Calculate sizing to fit the right side of the screen
        // We'll make the grid occupy roughly 40% of the screen width, or a max size
        int totalGridSize = Math.min(width / 2 - 40, height - 80);
        int tileSize = (totalGridSize / gridSize) - padding;

        // 2. Anchor to the RIGHT
        int startX = width - totalGridSize - 20; // 20px margin from the right edge
        int startY = 45; // Below the header line

        mc.getTextureManager().bindTexture(LOGO_TEXTURE);
        GlStateManager.enableBlend();

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                int x = startX + (col * (tileSize + padding));
                int y = startY + (row * (tileSize + padding));

                boolean isHovered = mouseX >= x && mouseX < x + tileSize && mouseY >= y && mouseY < y + tileSize;

                GlStateManager.pushMatrix();

                if (isHovered) {
                    // POP EFFECT: Scale and Random Warhol Color
                    GlStateManager.translate(x + tileSize/2f, y + tileSize/2f, 0);
                    GlStateManager.scale(1.1f, 1.1f, 1.1f);
                    GlStateManager.translate(-(x + tileSize/2f), -(y + tileSize/2f), 0);

                    // Use the grid position to pick a "random" consistent color
                    int index = (row * gridSize + col);
                    applyWarholColor(index);
                } else {
                    // NORMAL STATE: No tint
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                }

                Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, tileSize, tileSize, tileSize, tileSize);
                GlStateManager.popMatrix();
            }
        }

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Generates a vibrant color based on an index so it's "random" but stays the same for that tile.
     */
    private void applyWarholColor(int index) {
        // A list of classic vibrant Warhol-style colors
        int[] warholPalette = {
                0xFF5555, 0xFFFF55, 0x55FF55, 0x55FFFF, 0x5555FF, 0xFF55FF,
                0xFF8800, 0xFF0088, 0x00FF88, 0x8800FF, 0x0088FF, 0x88FF00
        };

        int color = warholPalette[index % warholPalette.length];
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;

        // Boost brightness for the "hover" look
        GlStateManager.color(r, g, b, 1.0f);
    }

    private void playCustomSound() {
        float randomPitch = 0.5F + (float)Math.random() * (2.0F - 0.5F);

        // Create the sound record
        ResourceLocation soundPath = new ResourceLocation("meowmayo:boom_sound");
        PositionedSoundRecord sound = PositionedSoundRecord.create(soundPath, randomPitch);

        // Play it through the Minecraft SoundHandler
        mc.getSoundHandler().playSound(sound);
    }

    private void drawCircle(float cx, float cy, float r, int color) {
        float f = (float)(color >> 24 & 255) / 255.0F;
        float f1 = (float)(color >> 16 & 255) / 255.0F;
        float f2 = (float)(color >> 8 & 255) / 255.0F;
        float f3 = (float)(color & 255) / 255.0F;

        GlStateManager.color(f1, f2, f3, f);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();

        GL11.glBegin(GL11.GL_POLYGON);
        for (int i = 0; i <= 360; i++) {
            double angle = Math.toRadians(i);
            GL11.glVertex2d(cx + Math.sin(angle) * r, cy + Math.cos(angle) * r);
        }
        GL11.glEnd();

        GlStateManager.enableTexture2D();
    }

    public static class Ripple {
        public int x, y, tileX, tileY, tileSize, color;
        public long startTime;
        public float maxRadius;

        public Ripple(int x, int y, int tileX, int tileY, int tileSize, int color) {
            this.x = x;
            this.y = y;
            this.tileX = tileX;
            this.tileY = tileY;
            this.tileSize = tileSize;
            this.color = color;
            this.startTime = System.currentTimeMillis();
            this.maxRadius = tileSize * 0.6f; // Smaller radius to stay circular
        }
    }
}

