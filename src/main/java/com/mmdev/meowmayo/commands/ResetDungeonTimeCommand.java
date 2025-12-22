package com.mmdev.meowmayo.commands;

import com.mmdev.meowmayo.features.dungeons.tracker.DungeonStats;
import com.mmdev.meowmayo.features.kuudra.tracker.KuudraPhases;
import com.mmdev.meowmayo.utils.ChatUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.Arrays;
import java.util.List;

public class ResetDungeonTimeCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "rsdt";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("resetdungeontime");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "rsdt";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // allow all players to run
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            KuudraPhases.rsKT();
            ChatUtils.system("Please input which floor to reset!");
        } else {
            switch (args[0].toLowerCase()) {
                case "f7":
                    if (args.length == 1) {
                        DungeonStats.sessionF7Stats.reset();
                        ChatUtils.system("Reset F7 Session Stats!");
                    } else {
                        DungeonStats.globalF7Stats.reset();
                        ChatUtils.system("Reset F7 Global Stats!");
                    }
                    break;

                case "m7":
                    if (args.length == 1) {
                        DungeonStats.sessionM7Stats.reset();
                        ChatUtils.system("Reset M7 Session Stats!");
                    } else {
                        DungeonStats.globalM7Stats.reset();
                        ChatUtils.system("Reset M7 Global Stats!");
                    }
                    break;
            }
        }
    }
}
