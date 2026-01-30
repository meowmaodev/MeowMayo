package com.mmdev.meowmayo.keybinds;

import com.mmdev.meowmayo.features.general.farming.PestAlert;
import com.mmdev.meowmayo.utils.ChatUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyInputHandler {
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ModKeybinds.wardrobeKey.isPressed())
            ChatUtils.command("wd");

        if (ModKeybinds.petKey.isPressed())
            ChatUtils.command("pets");

        if (ModKeybinds.equipmentKey.isPressed())
            ChatUtils.command("eq");

        if (ModKeybinds.pestWarpKey.isPressed())
            PestAlert.pestWarp();
    }
}