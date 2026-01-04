package com.mmdev.meowmayo.commands;

import com.mmdev.meowmayo.features.kuudra.tracker.KuudraStats;
import com.mmdev.meowmayo.utils.ChatUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.Arrays;
import java.util.List;

public class ResetKuudraTimeCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "rskt";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("resetkuudratime");
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
            ChatUtils.system("Please input which tier to reset!");
        } else {
            switch (args[0].toLowerCase()) {
                case "basic":
                    if (args.length == 1) {
                        KuudraStats.sessionBasicStats.reset();
                        ChatUtils.system("Reset Basic Session Stats!");
                    } else {
                        KuudraStats.globalBasicStats.reset();
                        ChatUtils.system("Reset Basic Global Stats!");
                    }
                    break;
                case "hot":
                    if (args.length == 1) {
                        KuudraStats.sessionHotStats.reset();
                        ChatUtils.system("Reset Hot Session Stats!");
                    } else {
                        KuudraStats.globalHotStats.reset();
                        ChatUtils.system("Reset Hot Global Stats!");
                    }
                    break;
                case "burning":
                    if (args.length == 1) {
                        KuudraStats.sessionBurningStats.reset();
                        ChatUtils.system("Reset Burning Session Stats!");
                    } else {
                        KuudraStats.globalBurningStats.reset();
                        ChatUtils.system("Reset Burning Global Stats!");
                    }
                    break;
                case "fiery":
                    if (args.length == 1) {
                        KuudraStats.sessionFieryStats.reset();
                        ChatUtils.system("Reset Fiery Session Stats!");
                    } else {
                        KuudraStats.sessionFieryStats.reset();
                        ChatUtils.system("Reset Fiery Global Stats!");
                    }
                    break;
                case "infernal":
                    if (args.length == 1) {
                        KuudraStats.sessionInfernalStats.reset();
                        ChatUtils.system("Reset Infernal Session Stats!");
                    } else {
                        KuudraStats.globalInfernalStats.reset();
                        ChatUtils.system("Reset Infernal Global Stats!");
                    }
                    break;
            }
        }
    }
}
