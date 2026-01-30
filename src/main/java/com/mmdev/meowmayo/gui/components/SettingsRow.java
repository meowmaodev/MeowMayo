package com.mmdev.meowmayo.gui.components;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.*;
import com.mmdev.meowmayo.gui.TextField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.util.List;

public class SettingsRow {
    public interface ISettingComponent {
        void draw(int x, int y, int width, int mouseX, int mouseY);
        boolean mouseClicked(int mouseX, int mouseY, int button);
        void keyTyped(char typedChar, int keyCode);
        void mouseReleased(int mouseX, int mouseY, int state);
        int getHeight();
    }

    public static String currentQuery = "";

    private static void drawTitle(FontRenderer fr, String title, int x, int y) {
        if (currentQuery.isEmpty() || !title.toLowerCase().contains(currentQuery.toLowerCase())) {
            fr.drawStringWithShadow(title, x, y, 0xFFFFFF);
            return;
        }

        String lowerTitle = title.toLowerCase();
        String lowerQuery = currentQuery.toLowerCase();

        int start = lowerTitle.indexOf(lowerQuery);
        int end = start + currentQuery.length();

        String pre = title.substring(0, start);
        String match = title.substring(start, end);
        String post = title.substring(end);

        fr.drawStringWithShadow(pre, x, y, 0xFFFFFF);
        int xOffset = fr.getStringWidth(pre);

        int matchWidth = fr.getStringWidth(match);
        GuiScreen.drawRect(x + xOffset, y - 1, x + xOffset + matchWidth, y + fr.FONT_HEIGHT, 0x99FFAA00);

        fr.drawStringWithShadow(match, x + xOffset, y, 0xFFFFFF);
        xOffset += matchWidth;

        fr.drawStringWithShadow(post, x + xOffset, y, 0xFFFFFF);
    }

    private static int drawDescription(int x, int y, int width, String desc) {
        if (desc == null || desc.isEmpty()) return 0;

        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        int maxWidth = (int) (width * 0.6f);
        List<String> lines = fr.listFormattedStringToWidth(desc, maxWidth);

        int lineY = y + 22;
        for (String line : lines) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.2f, 1.2f, 1.0f);
            fr.drawString(line, (int) ((x + 8) / 1.2f), (int) (lineY / 1.2f), 0xAAAAAA);
            GlStateManager.popMatrix();
            lineY += 15;
        }
        return lines.size() * 15;
    }

    public static class HeaderRow implements ISettingComponent {
        private final String title;
        public HeaderRow(String title) { this.title = title; }

        @Override
        public void draw(int x, int y, int width, int mouseX, int mouseY) {
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(title, x, y + 5, 0xFFFFFF);
        }
        @Override public boolean mouseClicked(int mx, int my, int b) { return false; }
        @Override public void keyTyped(char c, int k) {}
        @Override public void mouseReleased(int mx, int my, int s) {}
        @Override public int getHeight() { return 20; }
    }

    public static class ToggleRow implements ISettingComponent {
        private final ToggleSetting setting;
        public ToggleRow(ToggleSetting setting) { this.setting = setting; }

        @Override
        public void draw(int x, int y, int width, int mouseX, int mouseY) {
            GuiScreen.drawRect(x, y, x + width, y + getHeight() - 5, 0xFF333333);

            GlStateManager.pushMatrix();
            GlStateManager.scale(1.5f, 1.5f, 1.0f);
            drawTitle(Minecraft.getMinecraft().fontRendererObj, setting.getTitle(), (int)((x + 4)/1.5f), (int)((y + 4)/1.5f));
            GlStateManager.popMatrix();

            drawDescription(x, y, width, setting.getDescription());

            boolean val = (Boolean) setting.getValue();
            // Match your original toggle box positioning
            GuiScreen.drawRect(x + width - 45, y + 22, x + width - 15, y + 52, val ? 0xdd328046 : 0xddd44848);
        }

        @Override
        public boolean mouseClicked(int mouseX, int mouseY, int button) {
            if (button == 0) {
                // Bounds check matching the drawRect above
                if (mouseX >= (Minecraft.getMinecraft().currentScreen.width - 55) && mouseX <= (Minecraft.getMinecraft().currentScreen.width - 25)) {
                    ConfigSettings.edit(setting.getTitle(), !((Boolean) setting.getValue()));
                    return true;
                }
            }
            return false;
        }

        @Override public void keyTyped(char c, int k) {}
        @Override public void mouseReleased(int mx, int my, int s) {}
        @Override public int getHeight() { return 85; }
    }

    public static class SliderRow implements ISettingComponent {
        private final Setting setting;
        private boolean dragging = false;

        public SliderRow(Setting setting) { this.setting = setting; }

        @Override
        public void draw(int x, int y, int width, int mouseX, int mouseY) {
            GuiScreen.drawRect(x, y, x + width, y + getHeight() - 5, 0xFF333333);
            FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;

            GlStateManager.pushMatrix();
            GlStateManager.scale(1.5f, 1.5f, 1.0f);
            drawTitle(Minecraft.getMinecraft().fontRendererObj, setting.getTitle(), (int)((x + 4)/1.5f), (int)((y + 4)/1.5f));
            GlStateManager.popMatrix();

            drawDescription(x, y, width, setting.getDescription());

            int barWidth = (int)(width * 0.25f) - 15;
            int barX = x + width - barWidth - 15;
            int barY = y + 30;

            float min = (setting instanceof IntSliderSetting) ? ((IntSliderSetting)setting).getMin() : ((FloatSliderSetting)setting).getMin();
            float max = (setting instanceof IntSliderSetting) ? ((IntSliderSetting)setting).getMax() : ((FloatSliderSetting)setting).getMax();
            float val = Float.parseFloat(setting.getValue().toString());

            if (this.dragging) {
                float percent = Math.min(1, Math.max(0, (float)(mouseX - barX) / barWidth));
                if (setting instanceof IntSliderSetting) {
                    ConfigSettings.edit(setting.getTitle(), Math.round((max - min) * percent) + (int)min);
                } else {
                    float newVal = ((max - min) * percent) + min;
                    ConfigSettings.edit(setting.getTitle(), Math.round(newVal * 10) / 10f);
                }
            }

            float renderPercent = (val - min) / (max - min);
            GuiScreen.drawRect(barX, barY, barX + barWidth, barY + 5, 0xFF555555);
            int handleX = barX + (int)(renderPercent * barWidth);
            GuiScreen.drawRect(handleX - 5, barY - 3, handleX + 5, barY + 11, 0xFFAAAAAA);

            String valStr = (setting instanceof IntSliderSetting) ? String.valueOf((int)val) : String.valueOf(Math.round(val * 10) / 10f);
            fr.drawString(valStr, handleX - fr.getStringWidth(valStr)/2, barY - 12, 0xAAAAAA);
        }

        @Override
        public boolean mouseClicked(int mouseX, int mouseY, int button) {
            if (button == 0) {
                int barWidth = (int)((Minecraft.getMinecraft().currentScreen.width - 120) * 0.25f) - 15;

                int barX = Minecraft.getMinecraft().currentScreen.width - barWidth - 25;

                if (mouseX >= barX && mouseX <= barX + barWidth) {
                    this.dragging = true;
                    return true;
                }
            }
            return false;
        }

        @Override public void mouseReleased(int mx, int my, int s) { this.dragging = false; }
        @Override public void keyTyped(char c, int k) {}
        @Override public int getHeight() { return 85; }
    }

    public static class TextRow implements ISettingComponent {
        private final TextSetting setting;
        private final TextField textField;

        public TextRow(TextSetting setting) {
            this.setting = setting;
            this.textField = new TextField(0, 0, 120, 25);
            this.textField.setText((String) setting.getValue());
        }

        @Override
        public void draw(int x, int y, int width, int mouseX, int mouseY) {
            GuiScreen.drawRect(x, y, x + width, y + getHeight() - 5, 0xFF333333);

            GlStateManager.pushMatrix();
            GlStateManager.scale(1.5f, 1.5f, 1.0f);
            drawTitle(Minecraft.getMinecraft().fontRendererObj, setting.getTitle(), (int)((x + 4)/1.5f), (int)((y + 4)/1.5f));
            GlStateManager.popMatrix();

            drawDescription(x, y, width, setting.getDescription());

            textField.setPosition(x + width - (int)(width * 0.25f), y + 25);
            textField.setSize((int)(width * 0.25f) - 15, 25);
            textField.draw(mouseX, mouseY);
        }

        @Override
        public boolean mouseClicked(int mouseX, int mouseY, int button) {
            textField.mouseClicked(mouseX, mouseY, button);

            String old = textField.getText();
            if (!textField.getText().equals(old)) {
                ConfigSettings.edit(setting.getTitle(), textField.getText());
            }
            return textField.isFocused();
        }

        @Override
        public void keyTyped(char typedChar, int keyCode) {
            if (textField.isFocused()) {
                textField.keyTyped(typedChar, keyCode);
                ConfigSettings.edit(setting.getTitle(), textField.getText());
            }
        }
        @Override public void mouseReleased(int mx, int my, int s) {}
        @Override public int getHeight() { return 85; }
    }
}