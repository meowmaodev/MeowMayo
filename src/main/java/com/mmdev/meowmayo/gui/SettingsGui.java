package com.mmdev.meowmayo.gui;

import com.mmdev.meowmayo.config.ModConfig;
import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.lang.Math;

public class SettingsGui extends GuiScreen {

    private int selectedCategory = 0;
    private float scrollAmount = 0;
    private int maxScroll = 0;

    private Minecraft mc;

    private Setting activeSlider = null;

    private final Map<Setting, TextField> textFields = new HashMap<>();

    @Override
    public void initGui() {
        super.initGui();
        mc = Minecraft.getMinecraft();

        this.buttonList.clear();
        this.buttonList.add(new GuiButton(-1, width - 25, 5, 20, 20, "X"));

        int y = 35;
        for (int i = 0; i < ModConfig.getSettings().size(); i++) {
            GuiButton catButton = new GuiButton(i, 5, y, 90, 20, ModConfig.getSettings().get(i).getName());
            this.buttonList.add(catButton);
            y += 30;
        }

        this.buttonList.get(selectedCategory+1).enabled = false;

        // Initialize TextFields for all TextSettings
        for (SettingCategory cat : ModConfig.getSettings()) {
            for (Setting s : cat.getMiscSettings()) {
                if (s instanceof TextSetting) {
                    TextField tf = new TextField(0, 0, 120, 20);
                    tf.setText((String) s.getValue());
                    textFields.put(s, tf);
                }
            }
            for (SettingSubcategory sub : cat.getSubcategories()) {
                for (Setting s : sub.getSettings()) {
                    if (s instanceof TextSetting) {
                        TextField tf = new TextField(0, 0, 120, 20);
                        tf.setText((String) s.getValue());
                        textFields.put(s, tf);
                    }
                }
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == -1) {
            mc.displayGuiScreen(null);
        }

        if (button.id >= 0) {
            selectedCategory = button.id;

            for (GuiButton b : this.buttonList) {
                if (b.id >= 0) {
                    b.enabled = true;
                }
            }
            button.enabled = false;
            scrollAmount = 0;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String title = "MeowMayo Settings";
        this.drawDefaultBackground();

        drawCenteredString(this.fontRendererObj, title, this.width / 2, 12, 0xFFFFFF);
        drawHorizontalLine(10, width - 10, 30, 0xFFAAAAAA);
        drawVerticalLine(100, 30, height-10, 0xFFAAAAAA);

        ScaledResolution scaledRes = new ScaledResolution(mc);

        int scissorX = 110;
        int scissorY = 35;
        int scissorWidth = width - scissorX - 10;
        int scissorHeight = height - scissorY - 10;

        double scaleX = (double) mc.displayWidth / scaledRes.getScaledWidth();
        double scaleY = (double) mc.displayHeight / scaledRes.getScaledHeight();

        int fbX = (int) (scissorX * scaleX);
        int fbY = (int) ((scaledRes.getScaledHeight() - scissorY - scissorHeight) * scaleY);
        int fbWidth = (int) (scissorWidth * scaleX);
        int fbHeight = (int) (scissorHeight * scaleY);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(fbX, fbY, fbWidth, fbHeight);

        int yOffset = 40 - (int) scrollAmount;
        int boxHeight = 40;

        // Draw misc settings
        for (Setting s : ModConfig.getSettings().get(selectedCategory).getMiscSettings()) {
            drawSettingBox(110, yOffset, width-10, s);
            yOffset += 85;
            boxHeight += 85;
        }

        // Draw subcategory settings
        for (SettingSubcategory sub : ModConfig.getSettings().get(selectedCategory).getSubcategories()) {
            drawString(fontRendererObj, sub.getName(), 110, yOffset, 0xFFFFFF);
            yOffset += 15;
            boxHeight += 15;

            for (Setting s : sub.getSettings()) {
                drawSettingBox(110, yOffset, width-10, s);
                yOffset += 85;
                boxHeight += 85;
            }
        }

        maxScroll = Math.max(0, boxHeight-this.height);
        scrollAmount = Math.max(0, Math.min(scrollAmount, maxScroll));

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (activeSlider != null && Mouse.isButtonDown(0)) {
            float x1 = (width-10) - ((int)(((width-10)-110)*.25f)+15);
            float percent = (mouseX - x1) / ((width-25)-x1);
            if (activeSlider instanceof FloatSliderSetting) {
                FloatSliderSetting sl = (FloatSliderSetting) activeSlider;
                sl.setValue(Math.round((((sl.getMax() - sl.getMin())*percent) + sl.getMin())*10)/10f);
            } else if (activeSlider instanceof IntSliderSetting) {
                IntSliderSetting sl = (IntSliderSetting) activeSlider;
                sl.setValue(Math.round((sl.getMax() - sl.getMin())*percent) + sl.getMin());
            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {

        super.handleMouseInput();
        if (maxScroll <= 0) return;

        int wheel = Mouse.getDWheel();
        if (wheel != 0) {
            scrollAmount -= wheel / 8f;
            scrollAmount = Math.max(0, Math.min(scrollAmount, maxScroll));
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton != 0) return;

        int yOffset = 40 - (int) scrollAmount;

        // Handle toggles for misc settings
        for (Setting s : ModConfig.getSettings().get(selectedCategory).getMiscSettings()) {
            if (s instanceof ToggleSetting) {
                int toggleX1 = width - 55;
                int toggleX2 = width - 25;
                int toggleY1 = yOffset + 22;
                int toggleY2 = yOffset + 52;

                if (mouseX >= toggleX1 && mouseX <= toggleX2 && mouseY >= toggleY1 && mouseY <= toggleY2) {
                    ConfigSettings.edit(s.getTitle(), !((Boolean) s.getValue()));
                    return;
                }
            } else if (s instanceof IntSliderSetting || s instanceof FloatSliderSetting) {
                int sliderX1 = width - ((int)(((width-10)-110)*.25f)+10); //110  (int)((eX-x)*.25f)
                int sliderX2 = width - 25;
                int sliderY1 = yOffset + 27;
                int sliderY2 = yOffset + 38;
                if (mouseX >= sliderX1 && mouseX <= sliderX2 && mouseY >= sliderY1 && mouseY <= sliderY2) {
                    activeSlider = s;
                    return;
                }
            }
            yOffset += 85;
        }

        // Handle toggles for subcategory settings
        for (SettingSubcategory sub : ModConfig.getSettings().get(selectedCategory).getSubcategories()) {
            yOffset += 15;
            for (Setting s : sub.getSettings()) {
                if (s instanceof ToggleSetting) {
                    int toggleX1 = width - 55;
                    int toggleX2 = width - 25;
                    int toggleY1 = yOffset + 22;
                    int toggleY2 = yOffset + 52;

                    if (mouseX >= toggleX1 && mouseX <= toggleX2 && mouseY >= toggleY1 && mouseY <= toggleY2) {
                        ConfigSettings.edit(s.getTitle(), !((Boolean) s.getValue()));
                        return;
                    }
                } else if (s instanceof IntSliderSetting || s instanceof FloatSliderSetting) {
                    int sliderX1 = width - ((int)(((width-10)-110)*.25f)+10); //110  (int)((eX-x)*.25f)
                    int sliderX2 = width - 25;
                    int sliderY1 = yOffset + 27;
                    int sliderY2 = yOffset + 38;

                    if (mouseX >= sliderX1 && mouseX <= sliderX2 && mouseY >= sliderY1 && mouseY <= sliderY2) {
                        activeSlider = s;
                        return;
                    }
                }
                yOffset += 85;
            }
        }

        // Handle TextFields
        for (TextField field : textFields.values()) {
            field.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0 && activeSlider != null) {
            ConfigSettings.edit(activeSlider.getTitle(), activeSlider.getValue());
            activeSlider = null;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        for (Map.Entry<Setting, TextField> entry : textFields.entrySet()) {
            TextField field = entry.getValue();
            if (field.isFocused()) {
                field.keyTyped(typedChar, keyCode);
                ConfigSettings.edit(entry.getKey().getTitle(), field.getText());
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private void drawSettingBox(int x, int y, int eX, Setting s) {
        drawRect(x, y, eX, y+75, 0xFF333333);

        GlStateManager.pushMatrix();
        GlStateManager.scale(1.5f, 1.5f, 1.0f);
        drawString(fontRendererObj, s.getTitle(), (int)((x + 4)/1.5f), (int)((y + 4)/1.5f), 0xFFFFFF);
        GlStateManager.popMatrix();

        String desc = s.getDescription();
        if (desc != null && !desc.isEmpty()) {
            int maxWidth = (int)((eX-x) * 0.6f);
            List<String> lines = fontRendererObj.listFormattedStringToWidth(desc, maxWidth);
            int lineY = y + 19;
            for (String line : lines) {
                GlStateManager.pushMatrix();
                GlStateManager.scale(1.2f, 1.2f, 1.0f);
                drawString(fontRendererObj, line, (int)((x+8)/1.2f), (int)(lineY/1.2f), 0xAAAAAA);
                GlStateManager.popMatrix();
                lineY += 15;
            }
        }

        if (s instanceof ToggleSetting) {
            boolean value = (Boolean) s.getValue();
            drawRect(eX - 45, y + 22, eX - 15, y + 52, value ? 0xdd328046 : 0xddd44848);
        } else if (s instanceof IntSliderSetting || s instanceof FloatSliderSetting) {
            int barX = eX - (int)((eX-x)*.25f);
            int barY = y + 30;
            int barWidth = (int)((eX-x)*.25f)-15;
            int barHeight = 5;

            // Draw slider background
            drawRect(barX, barY, barX + barWidth, barY + barHeight, 0xFF555555);

            // Calculate handle position
            float percent;
            if (s instanceof IntSliderSetting) {
                IntSliderSetting intS = (IntSliderSetting) s;
                percent = (intS.getValue() - intS.getMin()) / (float) (intS.getMax() - intS.getMin());
            } else {
                FloatSliderSetting floatS = (FloatSliderSetting) s;
                percent = (floatS.getValue() - floatS.getMin()) / (float) (floatS.getMax() - floatS.getMin());
            }

            int handleX = barX + Math.round(percent * barWidth);
            int handleY = barY - 3;
            int handleSize = 10;

            // Draw slider handle
            drawRect(handleX - handleSize/2, handleY, handleX + handleSize/2, handleY + barHeight + 6, 0xFFAAAAAA);

            // Draw value above handle
            String valueStr;
            if (s instanceof IntSliderSetting) {
                valueStr = String.valueOf(((IntSliderSetting)s).getValue());
            } else {
                float val = ((FloatSliderSetting)s).getValue();
                val = ((int)(val * 10)) / 10f; // truncate to 1 decimal place
                valueStr = String.valueOf(val);
            }
            int textWidth = fontRendererObj.getStringWidth(valueStr);
            drawString(fontRendererObj, valueStr, handleX - textWidth/2, handleY - 12, 0xAAAAAA);
        } else if (s instanceof TextSetting) {
            TextField field = textFields.get(s);
            if (field != null) {
                field.setPosition(eX - (int)((eX-x)*.25f), y + 25);
                field.setSize((int)((eX-x)*.25f)-15, 25);
                field.draw(0, 0); // mouse coords not needed for now
            }
        }
    }
}
