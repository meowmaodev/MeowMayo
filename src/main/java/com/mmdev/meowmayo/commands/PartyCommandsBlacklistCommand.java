package com.mmdev.meowmayo.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import com.mmdev.meowmayo.utils.PartyCommandListUtils;
import com.mmdev.meowmayo.utils.ChatUtils;

public class PartyCommandsBlacklistCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "blacklist";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/blacklist <add|remove> <ign>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 2) {
            ChatUtils.system("§cUsage: /blacklist <add|remove> <ign>");
            return;
        }

        String action = args[0].toLowerCase();
        String player = args[1].toLowerCase();

        if ("add".equals(action)) {
            ChatUtils.system(PartyCommandListUtils.addToBlacklist(player) ? ("Added " + player + " to the blacklist!") : (player + " is already on the blacklist!"));
        }
        else if ("remove".equals(action)) {
            ChatUtils.system(PartyCommandListUtils.removeFromBlacklist(player) ? ("Removed " + player + " from the blacklist!") : (player + " is not on the blacklist!"));
        }
        else {
            ChatUtils.system("You can only add or remove players from the blacklist!");
        }
    }
}

