package com.mmdev.meowmayo.commands;

import com.mmdev.meowmayo.features.kuudra.KuudraPhases;
import com.mmdev.meowmayo.utils.ChatUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class ResetKuudraTimeCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "rskt";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "rskt";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // allow all players to run
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            KuudraPhases.rsKT();
            ChatUtils.system("Reset Session Stats!");
        } else {
            KuudraPhases.rsGlobalKT();
            ChatUtils.system("Reset Global Stats!");
        }
    }
}
