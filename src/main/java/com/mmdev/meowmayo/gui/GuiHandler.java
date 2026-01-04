package com.mmdev.meowmayo.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GuiHandler {
    private static GuiScreen guiToOpen = null;

    /**
     * Call this from anywhere to queue a GUI for opening.
     */
    public static void setGuiToOpen(GuiScreen gui) {
        guiToOpen = gui;
    }

    /**
     * This runs every render tick and opens the GUI if one is queued.
     */
    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent event) {
        if (guiToOpen != null) {
            Minecraft.getMinecraft().displayGuiScreen(guiToOpen);
        }
        guiToOpen = null;
    }
}
