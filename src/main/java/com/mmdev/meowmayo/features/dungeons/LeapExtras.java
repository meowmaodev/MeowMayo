package com.mmdev.meowmayo.features.dungeons;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeapExtras {
    private ToggleSetting announceLeap = (ToggleSetting) ConfigSettings.getSetting("Announce Spirit Leap");

    private static String highlightedPlayer = "";

    public static void setHighlightedPlayer(String name) {
        highlightedPlayer = name.toLowerCase();
    }

    public static void clearHighlightedPlayer() {
        highlightedPlayer = "";
    }

    private Pattern spiritLeap = Pattern.compile("You have teleported to (.+)!");

    @SubscribeEvent
    public void onChatPacket(S02ChatReceivedEvent event) {
        String message = event.getUnformattedMessage();


        Matcher matcher = spiritLeap.matcher(message);
        if (matcher.matches()) {
            if (announceLeap.getValue()) {
                ChatUtils.partyChat("Leaped to " + matcher.group(1));
            }

            clearHighlightedPlayer();
        }
    }

    @SubscribeEvent
    public void onRenderContainer(GuiScreenEvent.DrawScreenEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null) return;
        if (highlightedPlayer.isEmpty()) return;
        if (!(mc.thePlayer.openContainer instanceof ContainerChest)) return;

        ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;

        if (!chest.getLowerChestInventory().getDisplayName().getUnformattedText().equalsIgnoreCase("Spirit Leap")) return;

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

            if (itemName.toLowerCase().contains(highlightedPlayer)) {

                int x = guiLeft + slot.xDisplayPosition;
                int y = guiTop + slot.yDisplayPosition;

                int width = 16;
                int height = 16;

                net.minecraft.client.gui.Gui.drawRect(x, y, x + width, y + height, 0x80FF0000);
            }
        }
    }
}
