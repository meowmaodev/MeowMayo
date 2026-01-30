package com.mmdev.meowmayo.gui;

import com.mmdev.meowmayo.config.ModConfig;
import com.mmdev.meowmayo.config.settings.HudElementSetting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HudLocations extends GuiScreen {
    private TextField searchField;

    private int scrollOffset = 0;

    private final int BUTTON_WIDTH = 200;
    private final int BUTTON_HEIGHT = 20;
    private final int SPACING = 5;

    List<HudElementSetting> currentLocations = new ArrayList<>();

    @Override
    public void initGui() {
        searchField = new TextField(5, 5, BUTTON_WIDTH, BUTTON_HEIGHT);
        searchField.setFocused(true);

        updateButtons();
    }

    public void updateButtons() {
        this.buttonList.clear();
        String query = searchField.getText().toLowerCase();

        this.buttonList.add(new GuiButton(-1, width - 25, 5, 20, 20, "X"));

        List<HudElementSetting> locations = ModConfig.getLocations();

        currentLocations.clear();

        for (HudElementSetting setting : locations) {
            if (setting.getTitle().toLowerCase().contains(query)) {
                currentLocations.add(setting);
            }
        }

        for (int i = 0; i < currentLocations.size(); i++) {
            HudElementSetting setting = currentLocations.get(i);

            int yPos = 50 + (i * (BUTTON_HEIGHT + SPACING)) - scrollOffset;

            if (yPos > 30 && yPos < height - 50) {
                this.buttonList.add(new GuiButton(i, width / 2 - 100, yPos, BUTTON_WIDTH, BUTTON_HEIGHT, setting.getTitle()));
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == -1) {
            GuiHandler.setGuiToOpen(new MainGui());
        }

        List<HudElementSetting> locations = ModConfig.getLocations();
        if (button.id >= 0 && button.id < locations.size()) {
            GuiHandler.setGuiToOpen(new GuiHudEditor(currentLocations.get(button.id)));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if (keyCode == 1) {
            GuiHandler.setGuiToOpen(new MainGui());
            return;
        }

        String oldText = searchField.getText();
        searchField.keyTyped(typedChar, keyCode);

        if (!searchField.getText().equals(oldText)) {
            scrollOffset = 0;
            updateButtons();
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Mouse.getEventDWheel();
        if (wheel != 0) {
            List<HudElementSetting> locations = ModConfig.getLocations();

            int totalContentHeight = locations.size() * (BUTTON_HEIGHT + SPACING);

            int viewableHeight = height - 50;

            int maxScroll = Math.max(0, totalContentHeight - viewableHeight + 20);

            if (wheel > 0) scrollOffset -= 25;
            else scrollOffset += 25;

            if (scrollOffset < 0) scrollOffset = 0;
            if (scrollOffset > maxScroll) scrollOffset = maxScroll;

            updateButtons();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        String textBefore = searchField.getText();
        searchField.mouseClicked(mouseX, mouseY, mouseButton);

        if (!searchField.getText().equals(textBefore)) {
            updateButtons();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        String title = "Select HUD Element to edit";
        drawCenteredString(this.fontRendererObj, title, this.width / 2, 12, 0xFFFFFF);

        searchField.draw(mouseX, mouseY);

        drawHorizontalLine(10, width - 10, 30, 0xFFAAAAAA);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}