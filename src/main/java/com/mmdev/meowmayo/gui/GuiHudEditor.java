package com.mmdev.meowmayo.gui;

import com.mmdev.meowmayo.config.HudManager;
import com.mmdev.meowmayo.config.settings.HudElementSetting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import java.io.IOException;

public class GuiHudEditor extends GuiScreen {
    private final HudElementSetting element;

    // Dragging state
    private boolean dragging = false;
    private int lastMouseX, lastMouseY;
    private int dragOffsetX, dragOffsetY;

    public GuiHudEditor(HudElementSetting element) {
        this.element = element;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, this.width, 25, 0x88000000);
        this.drawCenteredString(fontRendererObj,
                "Editing: " + element.getTitle() + " | Drag to move, Scroll to scale, ESC to go back",
                this.width / 2, 8, 0xFFFFFF);

        String previewText = element.getPlaceholder();
        String[] lines = previewText.split("\n");

        int maxWidth = 0;
        for (String line : lines) {
            int lineWidth = fontRendererObj.getStringWidth(line);
            if (lineWidth > maxWidth) maxWidth = lineWidth;
        }
        int totalHeight = lines.length * (fontRendererObj.FONT_HEIGHT + 2);

        GlStateManager.pushMatrix();
        GlStateManager.translate(element.getX(), element.getY(), 0);
        GlStateManager.scale(element.getScale(), element.getScale(), 1.0F);

        drawRect(-2, -2, maxWidth + 2, totalHeight + 2, 0x55FFFFFF);
        fontRendererObj.drawSplitString(previewText, 0, 0, maxWidth, 0xFFFF00);

        GlStateManager.popMatrix();

        // 4. Handle Dragging Logic
        if (this.dragging) {
            element.setX(element.getX() + (mouseX - lastMouseX));
            element.setY(element.getY() + (mouseY - lastMouseY));
        }

        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) { // Left Click
            if (isHovering(mouseX, mouseY)) {
                this.dragging = true;

                this.dragOffsetX = mouseX - element.getX();
                this.dragOffsetY = mouseY - element.getY();
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (!this.dragging) return;
        this.dragging = false;
        HudManager.editLocation(element.name, mouseX - this.dragOffsetX, mouseY - this.dragOffsetY);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        int dw = Mouse.getEventDWheel();
        if (dw != 0) {
            float currentScale = element.getScale();
            if (dw > 0) {
                HudManager.editScale(element.getTitle(), currentScale + 0.05F);
            }
            else {
                HudManager.editScale(element.getTitle(), Math.max(0.1F, currentScale - 0.05F));
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) {
            GuiHandler.setGuiToOpen(new HudLocations());
        }
    }

    private boolean isHovering(int mouseX, int mouseY) {
        String[] lines = element.getPlaceholder().split("\n");
        int maxWidth = 0;
        for (String line : lines) {
            int w = fontRendererObj.getStringWidth(line);
            if (w > maxWidth) maxWidth = w;
        }
        int totalHeight = lines.length * fontRendererObj.FONT_HEIGHT;

        float scaledW = (maxWidth + 4) * element.getScale();
        float scaledH = (totalHeight + 4) * element.getScale();

        return mouseX >= element.getX() && mouseX <= element.getX() + scaledW &&
                mouseY >= element.getY() && mouseY <= element.getY() + scaledH;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}