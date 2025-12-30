package com.mmdev.meowmayo.keybinds;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class ModKeybinds {
    public static KeyBinding wardrobeKey;
    public static KeyBinding petKey;
    public static KeyBinding equipmentKey;

    public static void init() {
        wardrobeKey = new KeyBinding("key.meowmayo.wardrobe", Keyboard.KEY_NONE, "key.categories.meowmayo");
        petKey = new KeyBinding("key.meowmayo.pet", Keyboard.KEY_NONE, "key.categories.meowmayo");
        equipmentKey = new KeyBinding("key.meowmayo.equipment", Keyboard.KEY_NONE, "key.categories.meowmayo");

        ClientRegistry.registerKeyBinding(wardrobeKey);
        ClientRegistry.registerKeyBinding(petKey);
        ClientRegistry.registerKeyBinding(equipmentKey);
    }
}
