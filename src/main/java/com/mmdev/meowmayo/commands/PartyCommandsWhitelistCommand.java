package com.mmdev.meowmayo.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

import com.mmdev.meowmayo.utils.PartyCommandListUtils;
import com.mmdev.meowmayo.utils.ChatUtils;

public class PartyCommandsWhitelistCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "whitelist";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/whitelist <add|remove> <ign>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 2) {
            ChatUtils.system("§cUsage: /whitelist <add|remove> <ign>");
            return;
        }

        String action = args[0].toLowerCase();
        String player = args[1].toLowerCase();

        if ("add".equals(action)) {
            ChatUtils.system(PartyCommandListUtils.addToWhitelist(player) ? ("Added " + player + " to the whitelist!") : (player + " is already on the whitelist!"));
        }
        else if ("remove".equals(action)) {
            ChatUtils.system(PartyCommandListUtils.removeFromWhitelist(player) ? ("Removed " + player + " from the whitelist!") : (player + " is not on the whitelist!"));
        }
        else {
            ChatUtils.system("You can only add or remove players from the whitelist!");
        }
    }
}

