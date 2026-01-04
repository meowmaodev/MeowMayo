package com.mmdev.meowmayo.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;

import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import java.util.Arrays;

public class GetPearlsCommand extends CommandBase {
    private ToggleSetting compactPearls = (ToggleSetting) ConfigSettings.getSetting("Compact Pearls");
    private static boolean waiting = false;

    @Override
    public String getCommandName() {
        return "getpearls";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("gfspearls");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/GetPearls - Gets a full stack of pearls";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (waiting) return;
        waiting = true;

        final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            waiting = false;
            return;
        }

        int pearls = 0;
        int hBar = 0;

        ItemStack[] inv = player.inventory.mainInventory;

        for (int i = 0; i < inv.length; i++) {
            ItemStack stack = inv[i];
            if (stack == null) continue;

            if (stack.getItem() == Items.ender_pearl) {
                pearls += stack.stackSize;
                if (i < 9) {
                    hBar += stack.stackSize;
                }
            }
        }

        if ((pearls > 16 || pearls != hBar) && compactPearls.getValue()) {
            ChatUtils.command("gfs ender pearl " + (20 - pearls));
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    ChatUtils.command("gfs ender pearl 16");
                    waiting = false;
                }
            }, 2000);
        } else if (pearls == 16) {
            waiting = false;
        } else {
            ChatUtils.command("gfs ender pearl " + (16 - pearls));
            waiting = false;
        }
    }
}
