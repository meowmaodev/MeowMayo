package com.mmdev.meowmayo.features.general;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mmdev.meowmayo.commands.StorageCommand;

public class BackpackTracker {
    private boolean inInv = false;
    private int viewing = -1;
    private Container container = null;

    private static final Pattern bpRegex = Pattern.compile(".*Backpack.*\\(Slot #(\\d+)\\)");

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui == null) {
            stopTracking();
            return;
        }

        if (!(event.gui instanceof GuiContainer)) {
            stopTracking();
            return;
        }

        GuiContainer gui = (GuiContainer) event.gui;
        String name = gui.inventorySlots.getSlot(0).inventory.getName();

        Matcher matcher = bpRegex.matcher(name);
        if (!matcher.matches()) {
            stopTracking();
            return;
        }

        int slot = Integer.parseInt(matcher.group(1));

        viewing = findBackpackIndex(slot);
        if (viewing == -1) {
            stopTracking();
            return;
        }

        inInv = true;
        container = gui.inventorySlots;
    }

    private void stopTracking() {
        inInv = false;
        viewing = -1;
        container = null;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) { // this could be made better but i dont wanna, this is how the ct does it too :fire:
        if (!inInv || viewing == -1 || container == null) return;
        if (!(Minecraft.getMinecraft().currentScreen instanceof GuiContainer)) return;

        int items = 0;
        int size = container.inventorySlots.size();

        // Only check backpack slots (exclude player inv = last 36 slots)
        for (int i = 0; i < size - 36; i++) {
            Slot slot = container.getSlot(i);
            ItemStack stack = slot.getStack();
            if (stack != null) {
                items++;
            }
        }

        StorageCommand.BPStatus[viewing] = (items == size - 36);
    }

    private int findBackpackIndex(int slot) {
        if (StorageCommand.BPs == null) return -1;
        for (int i = 0; i < StorageCommand.BPs.length; i++) {
            if (StorageCommand.BPs[i].equals(String.valueOf(slot))) {
                return i;
            }
        }
        return -1;
    }
}