package com.mmdev.meowmayo.gui;

import com.mmdev.meowmayo.config.ModConfig;
import com.mmdev.meowmayo.config.settings.*;
import com.mmdev.meowmayo.gui.components.SettingsRow;
import com.mmdev.meowmayo.gui.components.SettingsRow.*;
import com.mmdev.meowmayo.gui.components.SettingsRow.ISettingComponent;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.lang.Math;

public class SettingsGui extends GuiScreen {
    private int selectedCategory = 0;
    private float scrollAmount = 0;
    private int maxScroll = 0;

    private TextField searchField;

    private final List<ISettingComponent> activeRows = new ArrayList<>();

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(-1, width - 25, 5, 20, 20, "X"));

        int y = 35;
        for (int i = 0; i < ModConfig.getSettings().size(); i++) {
            GuiButton catButton = new GuiButton(i, 5, y, 90, 20, ModConfig.getSettings().get(i).getName());
            this.buttonList.add(catButton);
            y += 30;
        }

        if (this.buttonList.size() > selectedCategory + 1) {
            this.buttonList.get(selectedCategory + 1).enabled = false;
        }

        this.searchField = new TextField(5, 5, 200, 20);
        this.searchField.setFocused(true);

        rebuildSettingsList();
    }

    private void rebuildSettingsList() {
        activeRows.clear();
        this.maxScroll = 0;

        SettingsRow.currentQuery = searchField.getText();
        String query = searchField.getText().toLowerCase();

        int totalHeight = 40;

        List<SettingCategory> categoriesToSearch = new ArrayList<>();

        if (!query.isEmpty()) {
            categoriesToSearch.addAll(ModConfig.getSettings());
        } else {
            categoriesToSearch.add(ModConfig.getSettings().get(selectedCategory));
        }
        for (SettingCategory cat : categoriesToSearch) {
            // If global searching, add a Header for the Category Name itself
            if (!query.isEmpty()) {
                activeRows.add(new HeaderRow("Category: " + cat.getName()));
                totalHeight += 20;
            }

            // Filter Misc Settings
            for (Setting s : cat.getMiscSettings()) {
                if (s.getTitle().toLowerCase().contains(query)) {
                    activeRows.add(createRow(s));
                    totalHeight += 85;
                }
            }

            // Filter Subcategories
            for (SettingSubcategory sub : cat.getSubcategories()) {
                List<ISettingComponent> tempResults = new ArrayList<>();
                for (Setting s : sub.getSettings()) {
                    if (s.getTitle().toLowerCase().contains(query)) {
                        tempResults.add(createRow(s));
                    }
                }

                if (!tempResults.isEmpty() || sub.getName().toLowerCase().contains(query)) {
                    activeRows.add(new HeaderRow(sub.getName()));
                    totalHeight += 20;
                    activeRows.addAll(tempResults);
                    totalHeight += (tempResults.size() * 85);
                }
            }
        }

        this.maxScroll = Math.max(0, totalHeight - this.height);
    }

    private ISettingComponent createRow(Setting s) {
        if (s instanceof ToggleSetting) return new ToggleRow((ToggleSetting) s);
        if (s instanceof IntSliderSetting || s instanceof FloatSliderSetting) return new SliderRow(s);
        if (s instanceof TextSetting) return new TextRow((TextSetting) s);
        return new HeaderRow(s.getTitle()); // Fallback
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == -1) {
            GuiHandler.setGuiToOpen(new MainGui());
        }

        if (button.id >= 0) {
            selectedCategory = button.id;

            searchField.setText("");
            searchField.setFocused(false);

            this.scrollAmount = 0;

            for (GuiButton b : this.buttonList) {
                if (b.id >= 0) b.enabled = true;
            }

            button.enabled = false;

            initGui();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String title = "MeowMayo Settings";
        this.drawDefaultBackground();

        drawCenteredString(this.fontRendererObj, title, this.width / 2, 12, 0xFFFFFF);

        searchField.draw(mouseX, mouseY);

        drawHorizontalLine(10, width - 10, 30, 0xFFAAAAAA);
        drawVerticalLine(100, 30, height-10, 0xFFAAAAAA);

        ScaledResolution scaledRes = new ScaledResolution(mc);

        int scale = scaledRes.getScaleFactor();
        int scissorX = 105 * scale;
        int scissorY = 10 * scale;
        int scissorWidth = (width - 115) * scale;
        int scissorHeight = (height - 45) * scale;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);

        int currY = 40 - (int) scrollAmount;
        for (ISettingComponent row : activeRows) {
            if (currY + row.getHeight() > 30 && currY < height) {
                row.draw(110, currY, width - 120, mouseX, mouseY);
            }
            currY += row.getHeight();
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Mouse.getDWheel();
        if (wheel != 0) {
            scrollAmount -= (wheel / 120f) * 20;
            scrollAmount = Math.max(0, Math.min(scrollAmount, maxScroll));
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        String textBefore = searchField.getText();
        searchField.mouseClicked(mouseX, mouseY, mouseButton);
        if (!searchField.getText().equals(textBefore)) {
            scrollAmount = 0;
            rebuildSettingsList();
        }

        int currY = 40 - (int) scrollAmount;

        for (ISettingComponent row : activeRows) {
            if (mouseY >= currY && mouseY <= currY + row.getHeight()) {
                if (row.mouseClicked(mouseX, mouseY, mouseButton)) {
                    break;
                }
            }
            currY += row.getHeight();
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);

        for (ISettingComponent row : activeRows) {
            row.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        String oldText = searchField.getText();
        searchField.keyTyped(typedChar, keyCode);

        if (!searchField.getText().equals(oldText)) {
            scrollAmount = 0;

            if (!searchField.getText().isEmpty()) {
                for (GuiButton b : this.buttonList) {
                    if (b.id >= 0) b.enabled = true;
                }
            } else {
                if (this.buttonList.size() > selectedCategory + 1) {
                    this.buttonList.get(selectedCategory + 1).enabled = false;
                }
            }

            rebuildSettingsList();
        }

        if (keyCode == 1) GuiHandler.setGuiToOpen(new MainGui());

        for (ISettingComponent row : activeRows) {
            row.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
