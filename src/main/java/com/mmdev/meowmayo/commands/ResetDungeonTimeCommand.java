package com.mmdev.meowmayo.commands;

import com.mmdev.meowmayo.features.dungeons.tracker.DungeonStats;
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
            ChatUtils.system("Please input which floor to reset!");
        } else {
            switch (args[0].toLowerCase()) {
                case "f2":
                    if (args.length == 1) {
                        DungeonStats.sessionF2Stats.reset();
                        ChatUtils.system("Reset F2 Session Stats!");
                    } else {
                        DungeonStats.globalF2Stats.reset();
                        ChatUtils.system("Reset F2 Global Stats!");
                    }
                    break;
                case "f3":
                    if (args.length == 1) {
                        DungeonStats.sessionF3Stats.reset();
                        ChatUtils.system("Reset F3 Session Stats!");
                    } else {
                        DungeonStats.globalF3Stats.reset();
                        ChatUtils.system("Reset F3 Global Stats!");
                    }
                    break;
                case "f5":
                    if (args.length == 1) {
                        DungeonStats.sessionF5Stats.reset();
                        ChatUtils.system("Reset F5 Session Stats!");
                    } else {
                        DungeonStats.globalF5Stats.reset();
                        ChatUtils.system("Reset F5 Global Stats!");
                    }
                    break;
                case "f6":
                    if (args.length == 1) {
                        DungeonStats.sessionF6Stats.reset();
                        ChatUtils.system("Reset F6 Session Stats!");
                    } else {
                        DungeonStats.globalF6Stats.reset();
                        ChatUtils.system("Reset F6 Global Stats!");
                    }
                    break;
                case "f7":
                    if (args.length == 1) {
                        DungeonStats.sessionF7Stats.reset();
                        ChatUtils.system("Reset F7 Session Stats!");
                    } else {
                        DungeonStats.globalF7Stats.reset();
                        ChatUtils.system("Reset F7 Global Stats!");
                    }
                    break;

                case "m2":
                    if (args.length == 1) {
                        DungeonStats.sessionM2Stats.reset();
                        ChatUtils.system("Reset M2 Session Stats!");
                    } else {
                        DungeonStats.globalM2Stats.reset();
                        ChatUtils.system("Reset M2 Global Stats!");
                    }
                    break;
                case "m3":
                    if (args.length == 1) {
                        DungeonStats.sessionM3Stats.reset();
                        ChatUtils.system("Reset M3 Session Stats!");
                    } else {
                        DungeonStats.globalM3Stats.reset();
                        ChatUtils.system("Reset M3 Global Stats!");
                    }
                    break;
                case "m5":
                    if (args.length == 1) {
                        DungeonStats.sessionM5Stats.reset();
                        ChatUtils.system("Reset M5 Session Stats!");
                    } else {
                        DungeonStats.globalM5Stats.reset();
                        ChatUtils.system("Reset M5 Global Stats!");
                    }
                    break;
                case "m6":
                    if (args.length == 1) {
                        DungeonStats.sessionM6Stats.reset();
                        ChatUtils.system("Reset M6 Session Stats!");
                    } else {
                        DungeonStats.globalM6Stats.reset();
                        ChatUtils.system("Reset M6 Global Stats!");
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
