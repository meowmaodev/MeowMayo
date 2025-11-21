package com.mmdev.meowmayo.features.general;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.DelayUtils;
import com.mmdev.meowmayo.utils.PlayerUtils;
import com.mmdev.meowmayo.utils.events.S02ActionBarReceivedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuffItems {
    private ToggleSetting ragMessage = (ToggleSetting) ConfigSettings.getSetting("Ragnarok Buff Message");
    private ToggleSetting ragTitle = (ToggleSetting) ConfigSettings.getSetting("Ragnarok Buff Title");

    private static final Pattern digitPattern = Pattern.compile("(\\d+)");

    private static boolean ragCast = false;

    @SubscribeEvent
    public void onChatPacket(S02ActionBarReceivedEvent event) {
        String msg = event.getMessage();
        if (!ragCast && (ragMessage.getValue() || ragTitle.getValue())) {
            if (msg != null && msg.length() >= 7 && msg.regionMatches(true, msg.length() - 7, "casting", 0, 7)) {

                Minecraft mc = Minecraft.getMinecraft();

                if (mc == null) return;

                ItemStack held = mc.thePlayer.getHeldItem();

                if (held == null || !held.hasTagCompound()) return;

                String itemName = held.getDisplayName().toLowerCase();

                if (itemName.toLowerCase().contains("ragnarock")) {
                    NBTTagCompound display = held.getTagCompound().getCompoundTag("display");
                    if (!display.hasKey("Lore", 9)) return;
                    NBTTagList lore = display.getTagList("Lore", 8);

                    int strength = 0;

                    for (int i = 0; i < lore.tagCount(); i++) {
                        String line = lore.getStringTagAt(i);
                        if (line.toLowerCase().contains("strength")) {
                            Matcher num = digitPattern.matcher(line.replaceAll("§.", ""));
                            if (num.find()) {
                                strength = (int) (Integer.parseInt(num.group(1)) * 1.5);
                                ragCast = true;
                                DelayUtils.scheduleTask(() -> ragCast = false, 3500);
                                break;
                            }
                        }
                    }
                    if (ragTitle.getValue()) {
                        PlayerUtils.makeTextTitle("Gained " + strength + " Strength", 1500);
                    }
                    if (ragMessage.getValue()) {
                        ChatUtils.partyChat("Gained " + strength + " strength from Ragnarock");
                    }
                }
            }
        }
    }
}
