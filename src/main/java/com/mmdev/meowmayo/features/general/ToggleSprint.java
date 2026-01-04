package com.mmdev.meowmayo.features.general;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;

public class ToggleSprint {
    private final ToggleSetting toggleSprintSetting = (ToggleSetting) ConfigSettings.getSetting("Toggle Sprint");

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) return;

        KeyBinding sprintKey = Minecraft.getMinecraft().gameSettings.keyBindSprint;
        if (toggleSprintSetting.getValue()) {
            KeyBinding.setKeyBindState(sprintKey.getKeyCode(), true);
        } else {
            KeyBinding.setKeyBindState(sprintKey.getKeyCode(), Keyboard.isKeyDown(sprintKey.getKeyCode()));
        }
    }
}
