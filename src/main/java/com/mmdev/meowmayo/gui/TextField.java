package com.mmdev.meowmayo.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class TextField {
    private int x, y, width, height;
    private String text = "";
    private int cursorPosition = 0;
    private int renderOffset = 0;
    private boolean focused = false;

    private final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

    public TextField(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setText(String text) {
        this.text = text;
        this.cursorPosition = text.length();
        renderOffset = 0;
    }

    public String getText() {
        return text;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focus) {
        this.focused = focus;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void draw(int mouseX, int mouseY) {
        // Draw background
        int borderColor = focused ? 0xFFAAAAAA : 0xFF555555;
        int bgColor = 0xFF222222;
        GuiScreen.drawRect(x - 1, y - 1, x + width + 1, y + height + 1, borderColor);
        GuiScreen.drawRect(x, y, x + width, y + height, bgColor);

        String visible = fontRenderer.trimStringToWidth(text.substring(renderOffset), width - 6);
        fontRenderer.drawString(visible, x + 3, y + (height - 8) / 2, 0xFFFFFF);

        // Draw cursor
        if (focused && Minecraft.getSystemTime() / 500 % 2 == 0) {
            int cursorX = x + 3 + fontRenderer.getStringWidth(text.substring(renderOffset, cursorPosition));
            if (cursorX <= x + width - 2) {
                GuiScreen.drawRect(cursorX, y + 3, cursorX + 1, y + height - 3, 0xFFFFFFFF);
            }
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (!focused) return;

        switch (keyCode) {
            case Keyboard.KEY_BACK:
                if (cursorPosition > 0 && text.length() > 0) {
                    text = text.substring(0, cursorPosition - 1) + text.substring(cursorPosition);
                    cursorPosition--;
                }
                break;
            case Keyboard.KEY_LEFT:
                if (cursorPosition > 0) cursorPosition--;
                break;
            case Keyboard.KEY_RIGHT:
                if (cursorPosition < text.length()) cursorPosition++;
                break;
            case Keyboard.KEY_HOME:
                cursorPosition = 0;
                break;
            case Keyboard.KEY_END:
                cursorPosition = text.length();
                break;
            default:
                if (typedChar >= 32) {
                    text = text.substring(0, cursorPosition) + typedChar + text.substring(cursorPosition);
                    cursorPosition++;
                }
        }

        adjustRenderOffset();
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean hovered = mouseX >= x && mouseX < x + width &&
                mouseY >= y && mouseY < y + height;

        focused = hovered;

        if (hovered && button == 1) {
            this.text = "";
            this.cursorPosition = 0;
            this.renderOffset = 0;
        }
    }

    private void adjustRenderOffset() {
        if (cursorPosition < 0) cursorPosition = 0;
        if (cursorPosition > text.length()) cursorPosition = text.length();

        // Ensure renderOffset is within valid bounds
        if (renderOffset < 0) renderOffset = 0;
        if (renderOffset > cursorPosition) renderOffset = cursorPosition;

        // Move renderOffset forward if cursor is past visible width
        while (fontRenderer.getStringWidth(text.substring(renderOffset, cursorPosition)) > width - 6) {
            renderOffset++;
            if (renderOffset > cursorPosition) renderOffset = cursorPosition;
        }

        // Move renderOffset backward if cursor moved before visible area
        while (cursorPosition - renderOffset < 0) {
            renderOffset--;
            if (renderOffset < 0) renderOffset = 0;
        }
    }
}
