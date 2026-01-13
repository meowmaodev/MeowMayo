package com.mmdev.meowmayo.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class MainGui extends GuiScreen {
    @Override
    public void initGui() {
        super.initGui();

        this.buttonList.clear();
        this.buttonList.add(new GuiButton(-1, width - 25, 5, 20, 20, "X"));


        this.buttonList.add(new GuiButton(1, width/2 - 100, height/2, 200, 20, "Edit Config"));
        this.buttonList.add(new GuiButton(2, width/2 - 100, height/2 + 40, 200, 20, "Edit Gui"));

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

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

