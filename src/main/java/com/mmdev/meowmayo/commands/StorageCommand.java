package com.mmdev.meowmayo.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;


import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.config.settings.TextSetting;

import java.util.List;
import java.util.Arrays;

public class StorageCommand extends CommandBase {
    private ToggleSetting storageCommand = (ToggleSetting) ConfigSettings.getSetting("Storage Command");
    private TextSetting allowedBackpacks = (TextSetting) ConfigSettings.getSetting("Allowed Backpacks");

    private static String prev = "";
    public static boolean[] BPStatus = new boolean[0];
    public static String[] BPs = new String[0];

    @Override
    public String getCommandName() {
        return "openstorage";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("mmos", "mmstorage");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/OpenStorage - Opens the first backpack with open space";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        String allowedBps = allowedBackpacks.getValue();

        if (!storageCommand.getValue()) return;

        if (!allowedBps.matches("^(\\d+,)*\\d+$")) {
            BPs = new String[0];
            ChatUtils.system("Invalid Backpack list entered");
            return;
        }

        BPs = allowedBps.split(",");

        if (BPs.length != BPStatus.length || !prev.equals(allowedBps)) {
            prev = allowedBps;
            BPStatus = new boolean[BPs.length];
            Arrays.fill(BPStatus, false);
        }

        int status = -1;

        for (int i = 0; i < BPs.length; i++) {
            if (!BPStatus[i]) {
                status = i;
                break;
            }
        }

        if (status == -1) {
            ChatUtils.system("All backpacks are filled");
            return;
        }

        ChatUtils.command("bp " + BPs[status]);
    }
}
