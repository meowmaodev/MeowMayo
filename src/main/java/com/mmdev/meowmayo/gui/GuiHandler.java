package com.mmdev.meowmayo.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GuiHandler {
    private static GuiScreen guiToOpen = null;

    public static void setGuiToOpen(GuiScreen gui) {
        guiToOpen = gui;
    }

    @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent event) {
        if (guiToOpen != null) {
            Minecraft.getMinecraft().displayGuiScreen(guiToOpen);
        }
        guiToOpen = null;
    }
}
