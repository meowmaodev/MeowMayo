package com.mmdev.meowmayo.features.dungeons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LeapHighlight {
    private String itemNameMatch;

    public LeapHighlight() {
        this.itemNameMatch = "porkchop";
    }

    @SubscribeEvent
    public void onRenderContainer(GuiScreenEvent.DrawScreenEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null) return;
        if (!(mc.thePlayer.openContainer instanceof ContainerChest)) return;

        ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;

//        ChatUtils.system(chest.getLowerChestInventory().getDisplayName().getUnformattedText());

        if (!(mc.currentScreen instanceof GuiContainer)) return;
        GuiContainer container = (GuiContainer) mc.currentScreen;

        int guiLeft = (container.width - 176) / 2;
        int guiTop = (container.height - (114 + (chest.getLowerChestInventory().getSizeInventory() / 9) * 18)) / 2;

        int chestSize = chest.getLowerChestInventory().getSizeInventory();

        for (int i = 0; i < chestSize; i++) {
            Slot slot = chest.getSlot(i);

            if (!slot.getHasStack()) continue;

            ItemStack stack = slot.getStack();

            // Match by item name
            String itemName = stack.getDisplayName();
            if (itemName == null) continue;

            if (itemName.toLowerCase().contains(itemNameMatch)) {

                int x = guiLeft + slot.xDisplayPosition;
                int y = guiTop + slot.yDisplayPosition;

                int width = 16;
                int height = 16;

                net.minecraft.client.gui.Gui.drawRect(x, y, x + width, y + height, 0x80FF0000);
            }
        }
    }
}
