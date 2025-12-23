package com.mmdev.meowmayo.commands;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.features.dungeons.tracker.DungeonStats;
import com.mmdev.meowmayo.utils.ChatUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.Arrays;
import java.util.List;

public class DungeonTimeCommand extends CommandBase {
    private ToggleSetting dungeonTrack = (ToggleSetting) ConfigSettings.getSetting("Average Dungeon Run Time Tracker");

    @Override
    public String getCommandName() {
        return "dungeontime";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("dt");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // allow all players to run
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "doogan";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (!dungeonTrack.getValue()) {
            ChatUtils.system("You are not currently tracking run data!");
            return;
        }
        if (args.length == 0) {
            ChatUtils.system("Please input a floor to view!");
        } else {
            switch (args[0].toLowerCase()) {
                case "f7":
                    if (args.length == 1) {
                        if (DungeonStats.sessionF7Stats.totalRuns == 0) {
                             ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4Dungeon Session Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + DungeonStats.sessionF7Stats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + DungeonStats.sessionF7Stats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.totalCompTime / DungeonStats.sessionF7Stats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.totalTime - DungeonStats.sessionF7Stats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.totalTime / DungeonStats.sessionF7Stats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowest)
                            );
                        }
                        return;
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "help":
                                ChatUtils.system("F7 Run Tracker:\n" +
                                        "§2=§r View General stats with /dungeontime f7\n" +
                                        "§2=§r View fastest run splits with /dungeontime f7 fastest\n" +
                                        "§2=§r View slowest run splits with /dungeontime f7 slowest\n" +
                                        "§2=§r View total run split times with /dungeontime f7 total\n" +
                                        "§2=§r View total times lost to lag with /dungeontime f7 lag\n" +
                                        "§2=§r View global tracker times with /dungeontime f7 <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    ChatUtils.system("§4Fastest Global Run Splits:\nNot Implemented yet!"
                                    );
                                } else {
                                    ChatUtils.system("§4Fastest Session Run Splits:\nNot Implemented yet!"
                                    );
                                }
                                return;
                            case "slowest":
                                return;
                            case "total":
                                return;
                            case "lag":
                                return;
                            case "global":
                                if (DungeonStats.globalF7Stats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4Dungeon Total Run Stats:\n" +
                                            " §2| §r§fRuns Tracked: §a§l" + DungeonStats.globalF7Stats.totalRuns + "\n" +
                                            " §2| §r§fRuns Completed: §a§l" + DungeonStats.globalF7Stats.totalComps + "\n" +
                                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.totalCompTime / DungeonStats.globalF7Stats.totalComps) + "\n" +
                                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.totalCompTime) + "\n" +
                                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.totalLag) + "\n" +
                                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.totalTime - DungeonStats.globalF7Stats.totalCompTime) + "\n" +
                                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.totalTime / DungeonStats.globalF7Stats.totalRuns) + "\n" +
                                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastest) + "\n" +
                                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.slowest)
                                    );
                                }
                                return;
                            default:
                                ChatUtils.system("Unrecognized Tag! Do </dungeontime f7 help> for more info.");
                                break;
                        }
                    }
                    break;
                case "m7":
                    if (args.length == 1) {
                        if (DungeonStats.sessionM7Stats.totalRuns == 0) {
                            ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4Dungeon Session Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + DungeonStats.sessionM7Stats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + DungeonStats.sessionM7Stats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.totalCompTime / DungeonStats.sessionM7Stats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.totalTime - DungeonStats.sessionM7Stats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.totalTime / DungeonStats.sessionM7Stats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowest)
                            );
                        }
                        return;
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "help":
                                ChatUtils.system("M7 Run Tracker:\n" +
                                        "§2=§r View General stats with /dungeontime m7\n" +
                                        "§2=§r View fastest run splits with /dungeontime m7 fastest\n" +
                                        "§2=§r View slowest run splits with /dungeontime m7 slowest\n" +
                                        "§2=§r View total run split times with /dungeontime m7 total\n" +
                                        "§2=§r View total times lost to lag with /dungeontime m7 lag\n" +
                                        "§2=§r View global tracker times with /dungeontime m7 <key> global");
                                return;
                            case "fastest":
                                return;
                            case "slowest":
                                return;
                            case "total":
                                return;
                            case "lag":
                                return;
                            case "global":
                                if (DungeonStats.globalM7Stats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4Dungeon Total Run Stats:\n" +
                                            " §2| §r§fRuns Tracked: §a§l" + DungeonStats.globalM7Stats.totalRuns + "\n" +
                                            " §2| §r§fRuns Completed: §a§l" + DungeonStats.globalM7Stats.totalComps + "\n" +
                                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.totalCompTime / DungeonStats.globalM7Stats.totalComps) + "\n" +
                                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.totalCompTime) + "\n" +
                                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.totalLag) + "\n" +
                                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.totalTime - DungeonStats.globalM7Stats.totalCompTime) + "\n" +
                                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.totalTime / DungeonStats.globalM7Stats.totalRuns) + "\n" +
                                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastest) + "\n" +
                                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowest)
                                    );
                                }
                                return;
                            default:
                                ChatUtils.system("Unrecognized Tag! Do </dungeontime m7 help> for more info.");
                                break;
                        }
                    }
                    break;
            }
        }
    }
}
