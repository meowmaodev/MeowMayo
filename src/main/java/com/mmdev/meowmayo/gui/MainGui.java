package com.mmdev.meowmayo.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

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

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    // I used ai to create this because it was funny
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
}

