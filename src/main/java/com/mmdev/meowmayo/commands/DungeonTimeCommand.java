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
                case "f1":
                    if (args.length == 1) {
                        if (DungeonStats.sessionF1Stats.totalRuns == 0) {
                            ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4F1 Session Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + DungeonStats.sessionF1Stats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + DungeonStats.sessionF1Stats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.totalCompTime / DungeonStats.sessionF1Stats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.totalTime - DungeonStats.sessionF1Stats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.totalTime / DungeonStats.sessionF1Stats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.slowest)
                            );
                        }
                        return;
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "help":
                                ChatUtils.system("F1 Run Tracker:\n" +
                                        "§2=§r View General stats with /dungeontime f1\n" +
                                        "§2=§r View fastest run splits with /dungeontime f1 fastest\n" +
                                        "§2=§r View slowest run splits with /dungeontime f1 slowest\n" +
                                        "§2=§r View total run split times with /dungeontime f1 phases\n" +
                                        "§2=§r View total times lost to lag with /dungeontime f1 lag\n" +
                                        "§2=§r View global tracker times with /dungeontime f1 <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF1Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalF1Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global Fastest F1 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fBonzo Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF1Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionF1Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session Fastest F1 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fBonzo Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                }
                                return;
                            case "slowest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF1Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Slowest F1 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fBonzo Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.slowestLagSplits[3]) + " §rlost to lag)\n"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF1Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Slowest F1 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fBonzo Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.slowestLagSplits[3]) + " §rlost to lag)\n"
                                        );
                                    }
                                }
                                return;
                            case "phases":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF1Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalF1Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global F1 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.compSplitTimes[0] / DungeonStats.globalF1Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.compLagTimes[0] / DungeonStats.globalF1Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.compSplitTimes[1] / DungeonStats.globalF1Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.compLagTimes[1] / DungeonStats.globalF1Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.compSplitTimes[2] / DungeonStats.globalF1Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.compLagTimes[2] / DungeonStats.globalF1Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Bonzo: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.compSplitTimes[3] / DungeonStats.globalF1Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.compLagTimes[3] / DungeonStats.globalF1Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Bonzo: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF1Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionF1Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session F1 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.compSplitTimes[0] / DungeonStats.sessionF1Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.compLagTimes[0] / DungeonStats.sessionF1Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.compSplitTimes[1] / DungeonStats.sessionF1Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.compLagTimes[1] / DungeonStats.sessionF1Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.compSplitTimes[2] / DungeonStats.sessionF1Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.compLagTimes[2] / DungeonStats.sessionF1Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Bonzo: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.compSplitTimes[3] / DungeonStats.sessionF1Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.compLagTimes[3] / DungeonStats.sessionF1Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Bonzo: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                }
                                return;
                            case "lag":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF1Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global F1 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.totalLag / DungeonStats.globalF1Stats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF1Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session F1 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF1Stats.totalLag / DungeonStats.sessionF1Stats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "global":
                                if (DungeonStats.globalF1Stats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4F1 Total Run Stats:\n" +
                                            " §2| §r§fRuns Tracked: §a§l" + DungeonStats.globalF1Stats.totalRuns + "\n" +
                                            " §2| §r§fRuns Completed: §a§l" + DungeonStats.globalF1Stats.totalComps + "\n" +
                                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.totalCompTime / DungeonStats.globalF1Stats.totalComps) + "\n" +
                                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.totalCompTime) + "\n" +
                                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.totalLag) + "\n" +
                                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.totalTime - DungeonStats.globalF1Stats.totalCompTime) + "\n" +
                                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.totalTime / DungeonStats.globalF1Stats.totalRuns) + "\n" +
                                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.fastest) + "\n" +
                                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF1Stats.slowest)
                                    );
                                }
                                return;
                            default:
                                ChatUtils.system("Unrecognized Tag! Do </dungeontime f1 help> for more info.");
                                break;
                        }
                    }
                    break;
                case "f2":
                    if (args.length == 1) {
                        if (DungeonStats.sessionF2Stats.totalRuns == 0) {
                            ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4F2 Session Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + DungeonStats.sessionF2Stats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + DungeonStats.sessionF2Stats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.totalCompTime / DungeonStats.sessionF2Stats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.totalTime - DungeonStats.sessionF2Stats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.totalTime / DungeonStats.sessionF2Stats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.slowest)
                            );
                        }
                        return;
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "help":
                                ChatUtils.system("F2 Run Tracker:\n" +
                                        "§2=§r View General stats with /dungeontime f2\n" +
                                        "§2=§r View fastest run splits with /dungeontime f2 fastest\n" +
                                        "§2=§r View slowest run splits with /dungeontime f2 slowest\n" +
                                        "§2=§r View total run split times with /dungeontime f2 phases\n" +
                                        "§2=§r View total times lost to lag with /dungeontime f2 lag\n" +
                                        "§2=§r View global tracker times with /dungeontime f2 <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF2Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalF2Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global Fastest F2 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fUndeads Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fScarf Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.fastestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.fastestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF2Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionF2Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session Fastest F2 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fUndeads Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fScarf Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.fastestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.fastestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                }
                                return;
                            case "slowest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF2Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Slowest F2 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fUndeads Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.slowestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fScarf Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.slowestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.slowestLagSplits[4]) + " §rlost to lag)\n"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF2Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Slowest F2 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fUndeads Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.slowestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fScarf Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.slowestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.slowestLagSplits[4]) + " §rlost to lag)\n"
                                        );
                                    }
                                }
                                return;
                            case "phases":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF2Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalF2Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global F2 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.compSplitTimes[0] / DungeonStats.globalF2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.compLagTimes[0] / DungeonStats.globalF2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.compSplitTimes[1] / DungeonStats.globalF2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.compLagTimes[1] / DungeonStats.globalF2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.compSplitTimes[2] / DungeonStats.globalF2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.compLagTimes[2] / DungeonStats.globalF2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Undeads: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.compSplitTimes[3] / DungeonStats.globalF2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.compLagTimes[3] / DungeonStats.globalF2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Undeads: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fAverage Scarf: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.compSplitTimes[4] / DungeonStats.globalF2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.compLagTimes[4] / DungeonStats.globalF2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Scarf: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.bestSplits[4]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF2Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionF2Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session F2 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.compSplitTimes[0] / DungeonStats.sessionF2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.compLagTimes[0] / DungeonStats.sessionF2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.compSplitTimes[1] / DungeonStats.sessionF2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.compLagTimes[1] / DungeonStats.sessionF2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.compSplitTimes[2] / DungeonStats.sessionF2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.compLagTimes[2] / DungeonStats.sessionF2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Undeads: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.compSplitTimes[3] / DungeonStats.sessionF2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.compLagTimes[3] / DungeonStats.sessionF2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Undeads: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fAverage Scarf: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.compSplitTimes[4] / DungeonStats.sessionF2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.compLagTimes[4] / DungeonStats.sessionF2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Scarf: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.bestSplits[4]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                }
                                return;
                            case "lag":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF2Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global F2 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.totalLag / DungeonStats.globalF2Stats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF2Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session F2 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF2Stats.totalLag / DungeonStats.sessionF2Stats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "global":
                                if (DungeonStats.globalF2Stats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4F2 Total Run Stats:\n" +
                                            " §2| §r§fRuns Tracked: §a§l" + DungeonStats.globalF2Stats.totalRuns + "\n" +
                                            " §2| §r§fRuns Completed: §a§l" + DungeonStats.globalF2Stats.totalComps + "\n" +
                                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.totalCompTime / DungeonStats.globalF2Stats.totalComps) + "\n" +
                                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.totalCompTime) + "\n" +
                                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.totalLag) + "\n" +
                                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.totalTime - DungeonStats.globalF2Stats.totalCompTime) + "\n" +
                                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.totalTime / DungeonStats.globalF2Stats.totalRuns) + "\n" +
                                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.fastest) + "\n" +
                                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF2Stats.slowest)
                                    );
                                }
                                return;
                            default:
                                ChatUtils.system("Unrecognized Tag! Do </dungeontime f2 help> for more info.");
                                break;
                        }
                    }
                    break;
                case "f3":
                    if (args.length == 1) {
                        if (DungeonStats.sessionF3Stats.totalRuns == 0) {
                            ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4F3 Session Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + DungeonStats.sessionF3Stats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + DungeonStats.sessionF3Stats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.totalCompTime / DungeonStats.sessionF3Stats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.totalTime - DungeonStats.sessionF3Stats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.totalTime / DungeonStats.sessionF3Stats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.slowest)
                            );
                        }
                        return;
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "help":
                                ChatUtils.system("F3 Run Tracker:\n" +
                                        "§2=§r View General stats with /dungeontime f3\n" +
                                        "§2=§r View fastest run splits with /dungeontime f3 fastest\n" +
                                        "§2=§r View slowest run splits with /dungeontime f3 slowest\n" +
                                        "§2=§r View total run split times with /dungeontime f3 phases\n" +
                                        "§2=§r View total times lost to lag with /dungeontime f3 lag\n" +
                                        "§2=§r View global tracker times with /dungeontime f3 <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF3Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalF3Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global Fastest F3 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fGuardians Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fProfessor Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.fastestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.fastestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fSuper Professor Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.fastestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.fastestLagSplits[5]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF3Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionF3Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session Fastest F3 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fGuardians Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fProfessor Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.fastestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.fastestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fSuper Professor Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.fastestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.fastestLagSplits[5]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                }
                                return;
                            case "slowest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF3Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Slowest F3 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fGuardians Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.slowestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fProfessor Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.slowestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.slowestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fSuper Professor Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.slowestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.slowestLagSplits[5]) + " §rlost to lag)\n"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF3Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Slowest F3 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fGuardians Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.slowestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fProfessor Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.slowestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.slowestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fSuper Professor Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.slowestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.slowestLagSplits[5]) + " §rlost to lag)\n"
                                        );
                                    }
                                }
                                return;
                            case "phases":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF3Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalF3Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global F3 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.compSplitTimes[0] / DungeonStats.globalF3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.compLagTimes[0] / DungeonStats.globalF3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.compSplitTimes[1] / DungeonStats.globalF3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.compLagTimes[1] / DungeonStats.globalF3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.compSplitTimes[2] / DungeonStats.globalF3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.compLagTimes[2] / DungeonStats.globalF3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Guardians: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.compSplitTimes[3] / DungeonStats.globalF3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.compLagTimes[3] / DungeonStats.globalF3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Guardians: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fAverage Professor: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.compSplitTimes[4] / DungeonStats.globalF3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.compLagTimes[4] / DungeonStats.globalF3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Professor: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.bestSplits[4]) + "\n" +
                                                " §2| §r§fAverage Super Professor: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.compSplitTimes[5] / DungeonStats.globalF3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.compLagTimes[5] / DungeonStats.globalF3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Super Professor: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.bestSplits[5]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF3Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionF3Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session F3 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.compSplitTimes[0] / DungeonStats.sessionF3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.compLagTimes[0] / DungeonStats.sessionF3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.compSplitTimes[1] / DungeonStats.sessionF3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.compLagTimes[1] / DungeonStats.sessionF3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.compSplitTimes[2] / DungeonStats.sessionF3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.compLagTimes[2] / DungeonStats.sessionF3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Guardians: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.compSplitTimes[3] / DungeonStats.sessionF3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.compLagTimes[3] / DungeonStats.sessionF3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Guardians: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fAverage Professor: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.compSplitTimes[4] / DungeonStats.sessionF3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.compLagTimes[4] / DungeonStats.sessionF3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Professor: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.bestSplits[4]) + "\n" +
                                                " §2| §r§fAverage Super Professor: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.compSplitTimes[5] / DungeonStats.sessionF3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.compLagTimes[5] / DungeonStats.sessionF3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Super Professor: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.bestSplits[5]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                }
                                return;
                            case "lag":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF3Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global F3 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.totalLag / DungeonStats.globalF3Stats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF3Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session F3 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF3Stats.totalLag / DungeonStats.sessionF3Stats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "global":
                                if (DungeonStats.globalF3Stats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4F3 Total Run Stats:\n" +
                                            " §2| §r§fRuns Tracked: §a§l" + DungeonStats.globalF3Stats.totalRuns + "\n" +
                                            " §2| §r§fRuns Completed: §a§l" + DungeonStats.globalF3Stats.totalComps + "\n" +
                                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.totalCompTime / DungeonStats.globalF3Stats.totalComps) + "\n" +
                                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.totalCompTime) + "\n" +
                                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.totalLag) + "\n" +
                                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.totalTime - DungeonStats.globalF3Stats.totalCompTime) + "\n" +
                                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.totalTime / DungeonStats.globalF3Stats.totalRuns) + "\n" +
                                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.fastest) + "\n" +
                                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF3Stats.slowest)
                                    );
                                }
                                return;
                            default:
                                ChatUtils.system("Unrecognized Tag! Do </dungeontime f3 help> for more info.");
                                break;
                        }
                    }
                    break;
                case "f4":
                    if (args.length == 1) {
                        if (DungeonStats.sessionF4Stats.totalRuns == 0) {
                            ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4F4 Session Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + DungeonStats.sessionF4Stats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + DungeonStats.sessionF4Stats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.totalCompTime / DungeonStats.sessionF4Stats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.totalTime - DungeonStats.sessionF4Stats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.totalTime / DungeonStats.sessionF4Stats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.slowest)
                            );
                        }
                        return;
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "help":
                                ChatUtils.system("F4 Run Tracker:\n" +
                                        "§2=§r View General stats with /dungeontime f4\n" +
                                        "§2=§r View fastest run splits with /dungeontime f4 fastest\n" +
                                        "§2=§r View slowest run splits with /dungeontime f4 slowest\n" +
                                        "§2=§r View total run split times with /dungeontime f4 phases\n" +
                                        "§2=§r View total times lost to lag with /dungeontime f4 lag\n" +
                                        "§2=§r View global tracker times with /dungeontime f4 <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF4Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalF4Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global Fastest F4 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fThorn Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF4Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionF4Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session Fastest F4 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fThorn Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                }
                                return;
                            case "slowest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF4Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Slowest F4 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fThorn Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.slowestLagSplits[3]) + " §rlost to lag)\n"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF4Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Slowest F4 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fThorn Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.slowestLagSplits[3]) + " §rlost to lag)\n"
                                        );
                                    }
                                }
                                return;
                            case "phases":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF4Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalF4Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global F4 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.compSplitTimes[0] / DungeonStats.globalF4Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.compLagTimes[0] / DungeonStats.globalF4Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.compSplitTimes[1] / DungeonStats.globalF4Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.compLagTimes[1] / DungeonStats.globalF4Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.compSplitTimes[2] / DungeonStats.globalF4Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.compLagTimes[2] / DungeonStats.globalF4Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Thorn: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.compSplitTimes[3] / DungeonStats.globalF4Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.compLagTimes[3] / DungeonStats.globalF4Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Thorn: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF4Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionF4Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session F4 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.compSplitTimes[0] / DungeonStats.sessionF4Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.compLagTimes[0] / DungeonStats.sessionF4Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.compSplitTimes[1] / DungeonStats.sessionF4Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.compLagTimes[1] / DungeonStats.sessionF4Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.compSplitTimes[2] / DungeonStats.sessionF4Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.compLagTimes[2] / DungeonStats.sessionF4Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Thorn: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.compSplitTimes[3] / DungeonStats.sessionF4Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.compLagTimes[3] / DungeonStats.sessionF4Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Thorn: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                }
                                return;
                            case "lag":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF4Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global F4 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.totalLag / DungeonStats.globalF4Stats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF4Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session F4 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF4Stats.totalLag / DungeonStats.sessionF4Stats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "global":
                                if (DungeonStats.globalF4Stats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4F4 Total Run Stats:\n" +
                                            " §2| §r§fRuns Tracked: §a§l" + DungeonStats.globalF4Stats.totalRuns + "\n" +
                                            " §2| §r§fRuns Completed: §a§l" + DungeonStats.globalF4Stats.totalComps + "\n" +
                                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.totalCompTime / DungeonStats.globalF4Stats.totalComps) + "\n" +
                                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.totalCompTime) + "\n" +
                                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.totalLag) + "\n" +
                                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.totalTime - DungeonStats.globalF4Stats.totalCompTime) + "\n" +
                                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.totalTime / DungeonStats.globalF4Stats.totalRuns) + "\n" +
                                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.fastest) + "\n" +
                                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF4Stats.slowest)
                                    );
                                }
                                return;
                            default:
                                ChatUtils.system("Unrecognized Tag! Do </dungeontime f4 help> for more info.");
                                break;
                        }
                    }
                    break;
                case "f5":
                    if (args.length == 1) {
                        if (DungeonStats.sessionF5Stats.totalRuns == 0) {
                            ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4F5 Session Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + DungeonStats.sessionF5Stats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + DungeonStats.sessionF5Stats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.totalCompTime / DungeonStats.sessionF5Stats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.totalTime - DungeonStats.sessionF5Stats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.totalTime / DungeonStats.sessionF5Stats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.slowest)
                            );
                        }
                        return;
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "help":
                                ChatUtils.system("F5 Run Tracker:\n" +
                                        "§2=§r View General stats with /dungeontime f5\n" +
                                        "§2=§r View fastest run splits with /dungeontime f5 fastest\n" +
                                        "§2=§r View slowest run splits with /dungeontime f5 slowest\n" +
                                        "§2=§r View total run split times with /dungeontime f5 phases\n" +
                                        "§2=§r View total times lost to lag with /dungeontime f5 lag\n" +
                                        "§2=§r View global tracker times with /dungeontime f5 <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF5Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalF5Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global Fastest F5 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fLivid Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF5Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionF5Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session Fastest F5 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fLivid Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                }
                                return;
                            case "slowest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF5Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Slowest F5 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fLivid Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.slowestLagSplits[3]) + " §rlost to lag)\n"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF5Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Slowest F5 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fLivid Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.slowestLagSplits[3]) + " §rlost to lag)\n"
                                        );
                                    }
                                }
                                return;
                            case "phases":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF5Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalF5Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global F5 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.compSplitTimes[0] / DungeonStats.globalF5Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.compLagTimes[0] / DungeonStats.globalF5Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.compSplitTimes[1] / DungeonStats.globalF5Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.compLagTimes[1] / DungeonStats.globalF5Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.compSplitTimes[2] / DungeonStats.globalF5Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.compLagTimes[2] / DungeonStats.globalF5Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Livid: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.compSplitTimes[3] / DungeonStats.globalF5Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.compLagTimes[3] / DungeonStats.globalF5Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Livid: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF5Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionF5Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session F5 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.compSplitTimes[0] / DungeonStats.sessionF5Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.compLagTimes[0] / DungeonStats.sessionF5Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.compSplitTimes[1] / DungeonStats.sessionF5Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.compLagTimes[1] / DungeonStats.sessionF5Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.compSplitTimes[2] / DungeonStats.sessionF5Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.compLagTimes[2] / DungeonStats.sessionF5Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Livid: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.compSplitTimes[3] / DungeonStats.sessionF5Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.compLagTimes[3] / DungeonStats.sessionF5Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Livid: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                }
                                return;
                            case "lag":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF5Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global F5 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.totalLag / DungeonStats.globalF5Stats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF5Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session F5 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF5Stats.totalLag / DungeonStats.sessionF5Stats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "global":
                                if (DungeonStats.globalF5Stats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4F5 Total Run Stats:\n" +
                                            " §2| §r§fRuns Tracked: §a§l" + DungeonStats.globalF5Stats.totalRuns + "\n" +
                                            " §2| §r§fRuns Completed: §a§l" + DungeonStats.globalF5Stats.totalComps + "\n" +
                                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.totalCompTime / DungeonStats.globalF5Stats.totalComps) + "\n" +
                                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.totalCompTime) + "\n" +
                                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.totalLag) + "\n" +
                                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.totalTime - DungeonStats.globalF5Stats.totalCompTime) + "\n" +
                                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.totalTime / DungeonStats.globalF5Stats.totalRuns) + "\n" +
                                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.fastest) + "\n" +
                                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF5Stats.slowest)
                                    );
                                }
                                return;
                            default:
                                ChatUtils.system("Unrecognized Tag! Do </dungeontime f5 help> for more info.");
                                break;
                        }
                    }
                    break;
                case "f6":
                    if (args.length == 1) {
                        if (DungeonStats.sessionF6Stats.totalRuns == 0) {
                            ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4F6 Session Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + DungeonStats.sessionF6Stats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + DungeonStats.sessionF6Stats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.totalCompTime / DungeonStats.sessionF6Stats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.totalTime - DungeonStats.sessionF6Stats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.totalTime / DungeonStats.sessionF6Stats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.slowest)
                            );
                        }
                        return;
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "help":
                                ChatUtils.system("F6 Run Tracker:\n" +
                                        "§2=§r View General stats with /dungeontime f6\n" +
                                        "§2=§r View fastest run splits with /dungeontime f6 fastest\n" +
                                        "§2=§r View slowest run splits with /dungeontime f6 slowest\n" +
                                        "§2=§r View total run split times with /dungeontime f6 phases\n" +
                                        "§2=§r View total times lost to lag with /dungeontime f6 lag\n" +
                                        "§2=§r View global tracker times with /dungeontime f6 <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF6Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalF6Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global Fastest F6 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fTerracottas Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fGiants Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.fastestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.fastestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fSadan Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.fastestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.fastestLagSplits[5]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF6Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionF6Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session Fastest F6 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fTerracottas Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fGiants Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.fastestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.fastestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fSadan Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.fastestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.fastestLagSplits[5]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                }
                                return;
                            case "slowest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF6Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Slowest F6 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fTerracottas Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.slowestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fGiants Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.slowestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.slowestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fSadan Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.slowestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.slowestLagSplits[5]) + " §rlost to lag)\n"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF6Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Slowest F6 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fTerracottas Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.slowestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fGiants Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.slowestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.slowestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fSadan Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.slowestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.slowestLagSplits[5]) + " §rlost to lag)\n"
                                        );
                                    }
                                }
                                return;
                            case "phases":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF6Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalF6Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global F6 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.compSplitTimes[0] / DungeonStats.globalF6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.compLagTimes[0] / DungeonStats.globalF6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.compSplitTimes[1] / DungeonStats.globalF6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.compLagTimes[1] / DungeonStats.globalF6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.compSplitTimes[2] / DungeonStats.globalF6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.compLagTimes[2] / DungeonStats.globalF6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Terracottas: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.compSplitTimes[3] / DungeonStats.globalF6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.compLagTimes[3] / DungeonStats.globalF6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Terracottas: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fAverage Giants: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.compSplitTimes[4] / DungeonStats.globalF6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.compLagTimes[4] / DungeonStats.globalF6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Giants: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.bestSplits[4]) + "\n" +
                                                " §2| §r§fAverage Sadan: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.compSplitTimes[5] / DungeonStats.globalF6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.compLagTimes[5] / DungeonStats.globalF6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Sadan: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.bestSplits[5]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF6Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionF6Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session F6 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.compSplitTimes[0] / DungeonStats.sessionF6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.compLagTimes[0] / DungeonStats.sessionF6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.compSplitTimes[1] / DungeonStats.sessionF6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.compLagTimes[1] / DungeonStats.sessionF6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.compSplitTimes[2] / DungeonStats.sessionF6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.compLagTimes[2] / DungeonStats.sessionF6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Terracottas: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.compSplitTimes[3] / DungeonStats.sessionF6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.compLagTimes[3] / DungeonStats.sessionF6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Terracottas: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fAverage Giants: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.compSplitTimes[4] / DungeonStats.sessionF6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.compLagTimes[4] / DungeonStats.sessionF6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Giants: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.bestSplits[4]) + "\n" +
                                                " §2| §r§fAverage Sadan: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.compSplitTimes[5] / DungeonStats.sessionF6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.compLagTimes[5] / DungeonStats.sessionF6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Sadan: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.bestSplits[5]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                }
                                return;
                            case "lag":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF6Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global F6 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.totalLag / DungeonStats.globalF6Stats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF6Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session F6 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF6Stats.totalLag / DungeonStats.sessionF6Stats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "global":
                                if (DungeonStats.globalF6Stats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4F6 Total Run Stats:\n" +
                                            " §2| §r§fRuns Tracked: §a§l" + DungeonStats.globalF6Stats.totalRuns + "\n" +
                                            " §2| §r§fRuns Completed: §a§l" + DungeonStats.globalF6Stats.totalComps + "\n" +
                                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.totalCompTime / DungeonStats.globalF6Stats.totalComps) + "\n" +
                                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.totalCompTime) + "\n" +
                                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.totalLag) + "\n" +
                                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.totalTime - DungeonStats.globalF6Stats.totalCompTime) + "\n" +
                                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.totalTime / DungeonStats.globalF6Stats.totalRuns) + "\n" +
                                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.fastest) + "\n" +
                                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF6Stats.slowest)
                                    );
                                }
                                return;
                            default:
                                ChatUtils.system("Unrecognized Tag! Do </dungeontime f6 help> for more info.");
                                break;
                        }
                    }
                    break;
                case "f7":
                    if (args.length == 1) {
                        if (DungeonStats.sessionF7Stats.totalRuns == 0) {
                             ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4F7 Session Run Stats:\n" +
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
                                        "§2=§r View total run split times with /dungeontime f7 phases\n" +
                                        "§2=§r View total times lost to lag with /dungeontime f7 lag\n" +
                                        "§2=§r View global tracker times with /dungeontime f7 <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF7Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalF7Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global Fastest F7 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fMaxor Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fStorm Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fTerminals Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastestLagSplits[5]) + " §rlost to lag)\n" +
                                                " §2| §r§fGoldor Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastestSplits[6]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastestLagSplits[6]) + " §rlost to lag)\n" +
                                                " §2| §r§fNecron Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastestSplits[7]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastestLagSplits[7]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF7Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionF7Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session Fastest F7 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fMaxor Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fStorm Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fTerminals Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastestLagSplits[5]) + " §rlost to lag)\n" +
                                                " §2| §r§fGoldor Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastestSplits[6]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastestLagSplits[6]) + " §rlost to lag)\n" +
                                                " §2| §r§fNecron Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastestSplits[7]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastestLagSplits[7]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                }
                                return;
                            case "slowest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF7Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Slowest F7 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fMaxor Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.slowestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fStorm Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.slowestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.slowestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fTerminals Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.slowestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.slowestLagSplits[5]) + " §rlost to lag)\n" +
                                                " §2| §r§fGoldor Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.slowestSplits[6]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.slowestLagSplits[6]) + " §rlost to lag)\n" +
                                                " §2| §r§fNecron Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.slowestSplits[7]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.slowestLagSplits[7]) + " §rlost to lag)\n"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF7Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Slowest F7 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fMaxor Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fStorm Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fTerminals Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowestLagSplits[5]) + " §rlost to lag)\n" +
                                                " §2| §r§fGoldor Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowestSplits[6]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowestLagSplits[6]) + " §rlost to lag)\n" +
                                                " §2| §r§fNecron Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowestSplits[7]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowestLagSplits[7]) + " §rlost to lag)\n"
                                        );
                                    }
                                }
                                return;
                            case "phases":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF7Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalF7Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global F7 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.compSplitTimes[0] / DungeonStats.globalF7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.compLagTimes[0] / DungeonStats.globalF7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.compSplitTimes[1] / DungeonStats.globalF7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.compLagTimes[1] / DungeonStats.globalF7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.compSplitTimes[2] / DungeonStats.globalF7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.compLagTimes[2] / DungeonStats.globalF7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Maxor: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.compSplitTimes[3] / DungeonStats.globalF7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.compLagTimes[3] / DungeonStats.globalF7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Maxor: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fAverage Storm: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.compSplitTimes[4] / DungeonStats.globalF7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.compLagTimes[4] / DungeonStats.globalF7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Storm: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.bestSplits[4]) + "\n" +
                                                " §2| §r§fAverage Terminals: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.compSplitTimes[5] / DungeonStats.globalF7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.compLagTimes[5] / DungeonStats.globalF7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Terminals: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.bestSplits[5]) + "\n" +
                                                " §2| §r§fAverage Goldor: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.compSplitTimes[6] / DungeonStats.globalF7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.compLagTimes[6] / DungeonStats.globalF7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Goldor: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.bestSplits[6]) + "\n" +
                                                " §2| §r§fAverage Necron: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.compSplitTimes[7] / DungeonStats.globalF7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.compLagTimes[7] / DungeonStats.globalF7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Necron: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.bestSplits[7]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF7Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionF7Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session F7 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.compSplitTimes[0] / DungeonStats.sessionF7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.compLagTimes[0] / DungeonStats.sessionF7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.compSplitTimes[1] / DungeonStats.sessionF7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.compLagTimes[1] / DungeonStats.sessionF7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.compSplitTimes[2] / DungeonStats.sessionF7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.compLagTimes[2] / DungeonStats.sessionF7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Maxor: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.compSplitTimes[3] / DungeonStats.sessionF7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.compLagTimes[3] / DungeonStats.sessionF7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Maxor: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fAverage Storm: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.compSplitTimes[4] / DungeonStats.sessionF7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.compLagTimes[4] / DungeonStats.sessionF7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Storm: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.bestSplits[4]) + "\n" +
                                                " §2| §r§fAverage Terminals: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.compSplitTimes[5] / DungeonStats.sessionF7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.compLagTimes[5] / DungeonStats.sessionF7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Terminals: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.bestSplits[5]) + "\n" +
                                                " §2| §r§fAverage Goldor: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.compSplitTimes[6] / DungeonStats.sessionF7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.compLagTimes[6] / DungeonStats.sessionF7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Goldor: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.bestSplits[6]) + "\n" +
                                                " §2| §r§fAverage Necron: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.compSplitTimes[7] / DungeonStats.sessionF7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.compLagTimes[7] / DungeonStats.sessionF7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Necron: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.bestSplits[7]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                }
                                return;
                            case "lag":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalF7Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global F7 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.globalF7Stats.totalLag / DungeonStats.globalF7Stats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionF7Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session F7 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.sessionF7Stats.totalLag / DungeonStats.sessionF7Stats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "global":
                                if (DungeonStats.globalF7Stats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4F7 Total Run Stats:\n" +
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
                case "m1":
                    if (args.length == 1) {
                        if (DungeonStats.sessionM1Stats.totalRuns == 0) {
                            ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4M1 Session Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + DungeonStats.sessionM1Stats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + DungeonStats.sessionM1Stats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.totalCompTime / DungeonStats.sessionM1Stats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.totalTime - DungeonStats.sessionM1Stats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.totalTime / DungeonStats.sessionM1Stats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.slowest)
                            );
                        }
                        return;
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "help":
                                ChatUtils.system("M1 Run Tracker:\n" +
                                        "§2=§r View General stats with /dungeontime m1\n" +
                                        "§2=§r View fastest run splits with /dungeontime m1 fastest\n" +
                                        "§2=§r View slowest run splits with /dungeontime m1 slowest\n" +
                                        "§2=§r View total run split times with /dungeontime m1 phases\n" +
                                        "§2=§r View total times lost to lag with /dungeontime m1 lag\n" +
                                        "§2=§r View global tracker times with /dungeontime m1 <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM1Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalM1Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global Fastest M1 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fBonzo Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM1Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionM1Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session Fastest M1 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fBonzo Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                }
                                return;
                            case "slowest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM1Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Slowest M1 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fBonzo Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.slowestLagSplits[3]) + " §rlost to lag)\n"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM1Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Slowest M1 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fBonzo Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.slowestLagSplits[3]) + " §rlost to lag)\n"
                                        );
                                    }
                                }
                                return;
                            case "phases":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM1Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalM1Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global M1 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.compSplitTimes[0] / DungeonStats.globalM1Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.compLagTimes[0] / DungeonStats.globalM1Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.compSplitTimes[1] / DungeonStats.globalM1Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.compLagTimes[1] / DungeonStats.globalM1Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.compSplitTimes[2] / DungeonStats.globalM1Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.compLagTimes[2] / DungeonStats.globalM1Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Bonzo: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.compSplitTimes[3] / DungeonStats.globalM1Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.compLagTimes[3] / DungeonStats.globalM1Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Bonzo: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM1Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionM1Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session M1 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.compSplitTimes[0] / DungeonStats.sessionM1Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.compLagTimes[0] / DungeonStats.sessionM1Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.compSplitTimes[1] / DungeonStats.sessionM1Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.compLagTimes[1] / DungeonStats.sessionM1Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.compSplitTimes[2] / DungeonStats.sessionM1Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.compLagTimes[2] / DungeonStats.sessionM1Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Bonzo: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.compSplitTimes[3] / DungeonStats.sessionM1Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.compLagTimes[3] / DungeonStats.sessionM1Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Bonzo: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                }
                                return;
                            case "lag":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM1Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global M1 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.totalLag / DungeonStats.globalM1Stats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM1Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session M1 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM1Stats.totalLag / DungeonStats.sessionM1Stats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "global":
                                if (DungeonStats.globalM1Stats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4M1 Total Run Stats:\n" +
                                            " §2| §r§fRuns Tracked: §a§l" + DungeonStats.globalM1Stats.totalRuns + "\n" +
                                            " §2| §r§fRuns Completed: §a§l" + DungeonStats.globalM1Stats.totalComps + "\n" +
                                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.totalCompTime / DungeonStats.globalM1Stats.totalComps) + "\n" +
                                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.totalCompTime) + "\n" +
                                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.totalLag) + "\n" +
                                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.totalTime - DungeonStats.globalM1Stats.totalCompTime) + "\n" +
                                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.totalTime / DungeonStats.globalM1Stats.totalRuns) + "\n" +
                                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.fastest) + "\n" +
                                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM1Stats.slowest)
                                    );
                                }
                                return;
                            default:
                                ChatUtils.system("Unrecognized Tag! Do </dungeontime m1 help> for more info.");
                                break;
                        }
                    }
                    break;
                case "m2":
                    if (args.length == 1) {
                        if (DungeonStats.sessionM2Stats.totalRuns == 0) {
                            ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4M2 Session Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + DungeonStats.sessionM2Stats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + DungeonStats.sessionM2Stats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.totalCompTime / DungeonStats.sessionM2Stats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.totalTime - DungeonStats.sessionM2Stats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.totalTime / DungeonStats.sessionM2Stats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.slowest)
                            );
                        }
                        return;
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "help":
                                ChatUtils.system("M2 Run Tracker:\n" +
                                        "§2=§r View General stats with /dungeontime m2\n" +
                                        "§2=§r View fastest run splits with /dungeontime m2 fastest\n" +
                                        "§2=§r View slowest run splits with /dungeontime m2 slowest\n" +
                                        "§2=§r View total run split times with /dungeontime m2 phases\n" +
                                        "§2=§r View total times lost to lag with /dungeontime m2 lag\n" +
                                        "§2=§r View global tracker times with /dungeontime m2 <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM2Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalM2Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global Fastest M2 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fUndeads Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fScarf Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.fastestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.fastestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM2Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionM2Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session Fastest M2 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fUndeads Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fScarf Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.fastestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.fastestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                }
                                return;
                            case "slowest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM2Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Slowest M2 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fUndeads Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.slowestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fScarf Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.slowestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.slowestLagSplits[4]) + " §rlost to lag)\n"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM2Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Slowest M2 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fUndeads Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.slowestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fScarf Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.slowestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.slowestLagSplits[4]) + " §rlost to lag)\n"
                                        );
                                    }
                                }
                                return;
                            case "phases":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM2Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalM2Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global M2 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.compSplitTimes[0] / DungeonStats.globalM2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.compLagTimes[0] / DungeonStats.globalM2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.compSplitTimes[1] / DungeonStats.globalM2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.compLagTimes[1] / DungeonStats.globalM2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.compSplitTimes[2] / DungeonStats.globalM2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.compLagTimes[2] / DungeonStats.globalM2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Undeads: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.compSplitTimes[3] / DungeonStats.globalM2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.compLagTimes[3] / DungeonStats.globalM2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Undeads: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fAverage Scarf: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.compSplitTimes[4] / DungeonStats.globalM2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.compLagTimes[4] / DungeonStats.globalM2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Scarf: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.bestSplits[4]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM2Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionM2Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session M2 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.compSplitTimes[0] / DungeonStats.sessionM2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.compLagTimes[0] / DungeonStats.sessionM2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.compSplitTimes[1] / DungeonStats.sessionM2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.compLagTimes[1] / DungeonStats.sessionM2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.compSplitTimes[2] / DungeonStats.sessionM2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.compLagTimes[2] / DungeonStats.sessionM2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Undeads: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.compSplitTimes[3] / DungeonStats.sessionM2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.compLagTimes[3] / DungeonStats.sessionM2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Undeads: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fAverage Scarf: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.compSplitTimes[4] / DungeonStats.sessionM2Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.compLagTimes[4] / DungeonStats.sessionM2Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Scarf: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.bestSplits[4]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                }
                                return;
                            case "lag":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM2Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global M2 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.totalLag / DungeonStats.globalM2Stats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM2Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session M2 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM2Stats.totalLag / DungeonStats.sessionM2Stats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "global":
                                if (DungeonStats.globalM2Stats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4M2 Total Run Stats:\n" +
                                            " §2| §r§fRuns Tracked: §a§l" + DungeonStats.globalM2Stats.totalRuns + "\n" +
                                            " §2| §r§fRuns Completed: §a§l" + DungeonStats.globalM2Stats.totalComps + "\n" +
                                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.totalCompTime / DungeonStats.globalM2Stats.totalComps) + "\n" +
                                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.totalCompTime) + "\n" +
                                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.totalLag) + "\n" +
                                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.totalTime - DungeonStats.globalM2Stats.totalCompTime) + "\n" +
                                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.totalTime / DungeonStats.globalM2Stats.totalRuns) + "\n" +
                                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.fastest) + "\n" +
                                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM2Stats.slowest)
                                    );
                                }
                                return;
                            default:
                                ChatUtils.system("Unrecognized Tag! Do </dungeontime m2 help> for more info.");
                                break;
                        }
                    }
                    break;
                case "m3":
                    if (args.length == 1) {
                        if (DungeonStats.sessionM3Stats.totalRuns == 0) {
                            ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4M3 Session Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + DungeonStats.sessionM3Stats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + DungeonStats.sessionM3Stats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.totalCompTime / DungeonStats.sessionM3Stats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.totalTime - DungeonStats.sessionM3Stats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.totalTime / DungeonStats.sessionM3Stats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.slowest)
                            );
                        }
                        return;
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "help":
                                ChatUtils.system("M3 Run Tracker:\n" +
                                        "§2=§r View General stats with /dungeontime m3\n" +
                                        "§2=§r View fastest run splits with /dungeontime m3 fastest\n" +
                                        "§2=§r View slowest run splits with /dungeontime m3 slowest\n" +
                                        "§2=§r View total run split times with /dungeontime m3 phases\n" +
                                        "§2=§r View total times lost to lag with /dungeontime m3 lag\n" +
                                        "§2=§r View global tracker times with /dungeontime m3 <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM3Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalM3Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global Fastest M3 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fGuardians Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fProfessor Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.fastestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.fastestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fSuper Professor Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.fastestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.fastestLagSplits[5]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM3Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionM3Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session Fastest M3 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fGuardians Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fProfessor Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.fastestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.fastestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fSuper Professor Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.fastestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.fastestLagSplits[5]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                }
                                return;
                            case "slowest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM3Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Slowest M3 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fGuardians Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.slowestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fProfessor Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.slowestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.slowestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fSuper Professor Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.slowestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.slowestLagSplits[5]) + " §rlost to lag)\n"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM3Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Slowest M3 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fGuardians Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.slowestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fProfessor Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.slowestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.slowestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fSuper Professor Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.slowestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.slowestLagSplits[5]) + " §rlost to lag)\n"
                                        );
                                    }
                                }
                                return;
                            case "phases":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM3Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalM3Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global M3 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.compSplitTimes[0] / DungeonStats.globalM3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.compLagTimes[0] / DungeonStats.globalM3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.compSplitTimes[1] / DungeonStats.globalM3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.compLagTimes[1] / DungeonStats.globalM3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.compSplitTimes[2] / DungeonStats.globalM3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.compLagTimes[2] / DungeonStats.globalM3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Guardians: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.compSplitTimes[3] / DungeonStats.globalM3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.compLagTimes[3] / DungeonStats.globalM3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Guardians: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fAverage Professor: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.compSplitTimes[4] / DungeonStats.globalM3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.compLagTimes[4] / DungeonStats.globalM3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Professor: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.bestSplits[4]) + "\n" +
                                                " §2| §r§fAverage Super Professor: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.compSplitTimes[5] / DungeonStats.globalM3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.compLagTimes[5] / DungeonStats.globalM3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Super Professor: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.bestSplits[5]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM3Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionM3Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session M3 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.compSplitTimes[0] / DungeonStats.sessionM3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.compLagTimes[0] / DungeonStats.sessionM3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.compSplitTimes[1] / DungeonStats.sessionM3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.compLagTimes[1] / DungeonStats.sessionM3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.compSplitTimes[2] / DungeonStats.sessionM3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.compLagTimes[2] / DungeonStats.sessionM3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Guardians: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.compSplitTimes[3] / DungeonStats.sessionM3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.compLagTimes[3] / DungeonStats.sessionM3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Guardians: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fAverage Professor: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.compSplitTimes[4] / DungeonStats.sessionM3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.compLagTimes[4] / DungeonStats.sessionM3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Professor: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.bestSplits[4]) + "\n" +
                                                " §2| §r§fAverage Super Professor: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.compSplitTimes[5] / DungeonStats.sessionM3Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.compLagTimes[5] / DungeonStats.sessionM3Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Super Professor: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.bestSplits[5]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                }
                                return;
                            case "lag":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM3Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global M3 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.totalLag / DungeonStats.globalM3Stats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM3Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session M3 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM3Stats.totalLag / DungeonStats.sessionM3Stats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "global":
                                if (DungeonStats.globalM3Stats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4M3 Total Run Stats:\n" +
                                            " §2| §r§fRuns Tracked: §a§l" + DungeonStats.globalM3Stats.totalRuns + "\n" +
                                            " §2| §r§fRuns Completed: §a§l" + DungeonStats.globalM3Stats.totalComps + "\n" +
                                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.totalCompTime / DungeonStats.globalM3Stats.totalComps) + "\n" +
                                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.totalCompTime) + "\n" +
                                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.totalLag) + "\n" +
                                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.totalTime - DungeonStats.globalM3Stats.totalCompTime) + "\n" +
                                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.totalTime / DungeonStats.globalM3Stats.totalRuns) + "\n" +
                                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.fastest) + "\n" +
                                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM3Stats.slowest)
                                    );
                                }
                                return;
                            default:
                                ChatUtils.system("Unrecognized Tag! Do </dungeontime m3 help> for more info.");
                                break;
                        }
                    }
                    break;
                case "m4":
                    if (args.length == 1) {
                        if (DungeonStats.sessionM4Stats.totalRuns == 0) {
                            ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4M4 Session Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + DungeonStats.sessionM4Stats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + DungeonStats.sessionM4Stats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.totalCompTime / DungeonStats.sessionM4Stats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.totalTime - DungeonStats.sessionM4Stats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.totalTime / DungeonStats.sessionM4Stats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.slowest)
                            );
                        }
                        return;
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "help":
                                ChatUtils.system("M4 Run Tracker:\n" +
                                        "§2=§r View General stats with /dungeontime m4\n" +
                                        "§2=§r View fastest run splits with /dungeontime m4 fastest\n" +
                                        "§2=§r View slowest run splits with /dungeontime m4 slowest\n" +
                                        "§2=§r View total run split times with /dungeontime m4 phases\n" +
                                        "§2=§r View total times lost to lag with /dungeontime m4 lag\n" +
                                        "§2=§r View global tracker times with /dungeontime m4 <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM4Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalM4Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global Fastest M4 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fThorn Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM4Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionM4Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session Fastest M4 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fThorn Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                }
                                return;
                            case "slowest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM4Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Slowest M4 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fThorn Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.slowestLagSplits[3]) + " §rlost to lag)\n"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM4Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Slowest M4 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fThorn Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.slowestLagSplits[3]) + " §rlost to lag)\n"
                                        );
                                    }
                                }
                                return;
                            case "phases":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM4Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalM4Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global M4 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.compSplitTimes[0] / DungeonStats.globalM4Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.compLagTimes[0] / DungeonStats.globalM4Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.compSplitTimes[1] / DungeonStats.globalM4Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.compLagTimes[1] / DungeonStats.globalM4Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.compSplitTimes[2] / DungeonStats.globalM4Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.compLagTimes[2] / DungeonStats.globalM4Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Thorn: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.compSplitTimes[3] / DungeonStats.globalM4Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.compLagTimes[3] / DungeonStats.globalM4Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Thorn: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM4Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionM4Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session M4 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.compSplitTimes[0] / DungeonStats.sessionM4Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.compLagTimes[0] / DungeonStats.sessionM4Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.compSplitTimes[1] / DungeonStats.sessionM4Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.compLagTimes[1] / DungeonStats.sessionM4Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.compSplitTimes[2] / DungeonStats.sessionM4Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.compLagTimes[2] / DungeonStats.sessionM4Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Thorn: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.compSplitTimes[3] / DungeonStats.sessionM4Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.compLagTimes[3] / DungeonStats.sessionM4Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Thorn: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                }
                                return;
                            case "lag":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM4Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global M4 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.totalLag / DungeonStats.globalM4Stats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM4Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session M4 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM4Stats.totalLag / DungeonStats.sessionM4Stats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "global":
                                if (DungeonStats.globalM4Stats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4M4 Total Run Stats:\n" +
                                            " §2| §r§fRuns Tracked: §a§l" + DungeonStats.globalM4Stats.totalRuns + "\n" +
                                            " §2| §r§fRuns Completed: §a§l" + DungeonStats.globalM4Stats.totalComps + "\n" +
                                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.totalCompTime / DungeonStats.globalM4Stats.totalComps) + "\n" +
                                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.totalCompTime) + "\n" +
                                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.totalLag) + "\n" +
                                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.totalTime - DungeonStats.globalM4Stats.totalCompTime) + "\n" +
                                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.totalTime / DungeonStats.globalM4Stats.totalRuns) + "\n" +
                                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.fastest) + "\n" +
                                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM4Stats.slowest)
                                    );
                                }
                                return;
                            default:
                                ChatUtils.system("Unrecognized Tag! Do </dungeontime m4 help> for more info.");
                                break;
                        }
                    }
                    break;
                case "m5":
                    if (args.length == 1) {
                        if (DungeonStats.sessionM5Stats.totalRuns == 0) {
                            ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4M5 Session Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + DungeonStats.sessionM5Stats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + DungeonStats.sessionM5Stats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.totalCompTime / DungeonStats.sessionM5Stats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.totalTime - DungeonStats.sessionM5Stats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.totalTime / DungeonStats.sessionM5Stats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.slowest)
                            );
                        }
                        return;
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "help":
                                ChatUtils.system("M5 Run Tracker:\n" +
                                        "§2=§r View General stats with /dungeontime m5\n" +
                                        "§2=§r View fastest run splits with /dungeontime m5 fastest\n" +
                                        "§2=§r View slowest run splits with /dungeontime m5 slowest\n" +
                                        "§2=§r View total run split times with /dungeontime m5 phases\n" +
                                        "§2=§r View total times lost to lag with /dungeontime m5 lag\n" +
                                        "§2=§r View global tracker times with /dungeontime m5 <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM5Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalM5Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global Fastest M5 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fLivid Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM5Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionM5Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session Fastest M5 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fLivid Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                }
                                return;
                            case "slowest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM5Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Slowest M5 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fLivid Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.slowestLagSplits[3]) + " §rlost to lag)\n"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM5Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Slowest M5 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fLivid Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.slowestLagSplits[3]) + " §rlost to lag)\n"
                                        );
                                    }
                                }
                                return;
                            case "phases":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM5Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalM5Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global M5 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.compSplitTimes[0] / DungeonStats.globalM5Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.compLagTimes[0] / DungeonStats.globalM5Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.compSplitTimes[1] / DungeonStats.globalM5Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.compLagTimes[1] / DungeonStats.globalM5Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.compSplitTimes[2] / DungeonStats.globalM5Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.compLagTimes[2] / DungeonStats.globalM5Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Livid: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.compSplitTimes[3] / DungeonStats.globalM5Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.compLagTimes[3] / DungeonStats.globalM5Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Livid: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM5Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionM5Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session M5 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.compSplitTimes[0] / DungeonStats.sessionM5Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.compLagTimes[0] / DungeonStats.sessionM5Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.compSplitTimes[1] / DungeonStats.sessionM5Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.compLagTimes[1] / DungeonStats.sessionM5Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.compSplitTimes[2] / DungeonStats.sessionM5Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.compLagTimes[2] / DungeonStats.sessionM5Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Livid: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.compSplitTimes[3] / DungeonStats.sessionM5Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.compLagTimes[3] / DungeonStats.sessionM5Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Livid: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                }
                                return;
                            case "lag":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM5Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global M5 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.totalLag / DungeonStats.globalM5Stats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM5Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session M5 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM5Stats.totalLag / DungeonStats.sessionM5Stats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "global":
                                if (DungeonStats.globalM5Stats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4M5 Total Run Stats:\n" +
                                            " §2| §r§fRuns Tracked: §a§l" + DungeonStats.globalM5Stats.totalRuns + "\n" +
                                            " §2| §r§fRuns Completed: §a§l" + DungeonStats.globalM5Stats.totalComps + "\n" +
                                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.totalCompTime / DungeonStats.globalM5Stats.totalComps) + "\n" +
                                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.totalCompTime) + "\n" +
                                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.totalLag) + "\n" +
                                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.totalTime - DungeonStats.globalM5Stats.totalCompTime) + "\n" +
                                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.totalTime / DungeonStats.globalM5Stats.totalRuns) + "\n" +
                                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.fastest) + "\n" +
                                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM5Stats.slowest)
                                    );
                                }
                                return;
                            default:
                                ChatUtils.system("Unrecognized Tag! Do </dungeontime m5 help> for more info.");
                                break;
                        }
                    }
                    break;
                case "m6":
                    if (args.length == 1) {
                        if (DungeonStats.sessionM6Stats.totalRuns == 0) {
                            ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4M6 Session Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + DungeonStats.sessionM6Stats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + DungeonStats.sessionM6Stats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.totalCompTime / DungeonStats.sessionM6Stats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.totalTime - DungeonStats.sessionM6Stats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.totalTime / DungeonStats.sessionM6Stats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.slowest)
                            );
                        }
                        return;
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "help":
                                ChatUtils.system("M6 Run Tracker:\n" +
                                        "§2=§r View General stats with /dungeontime m6\n" +
                                        "§2=§r View fastest run splits with /dungeontime m6 fastest\n" +
                                        "§2=§r View slowest run splits with /dungeontime m6 slowest\n" +
                                        "§2=§r View total run split times with /dungeontime m6 phases\n" +
                                        "§2=§r View total times lost to lag with /dungeontime m6 lag\n" +
                                        "§2=§r View global tracker times with /dungeontime m6 <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM6Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalM6Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global Fastest M6 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fTerracottas Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fGiants Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.fastestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.fastestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fSadan Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.fastestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.fastestLagSplits[5]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM6Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionM6Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session Fastest M6 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fTerracottas Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fGiants Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.fastestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.fastestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fSadan Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.fastestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.fastestLagSplits[5]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                }
                                return;
                            case "slowest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM6Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Slowest M6 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fTerracottas Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.slowestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fGiants Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.slowestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.slowestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fSadan Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.slowestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.slowestLagSplits[5]) + " §rlost to lag)\n"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM6Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Slowest M6 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fTerracottas Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.slowestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fGiants Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.slowestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.slowestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fSadan Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.slowestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.slowestLagSplits[5]) + " §rlost to lag)\n"
                                        );
                                    }
                                }
                                return;
                            case "phases":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM6Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalM6Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global M6 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.compSplitTimes[0] / DungeonStats.globalM6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.compLagTimes[0] / DungeonStats.globalM6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.compSplitTimes[1] / DungeonStats.globalM6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.compLagTimes[1] / DungeonStats.globalM6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.compSplitTimes[2] / DungeonStats.globalM6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.compLagTimes[2] / DungeonStats.globalM6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Terracottas: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.compSplitTimes[3] / DungeonStats.globalM6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.compLagTimes[3] / DungeonStats.globalM6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Terracottas: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fAverage Giants: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.compSplitTimes[4] / DungeonStats.globalM6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.compLagTimes[4] / DungeonStats.globalM6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Giants: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.bestSplits[4]) + "\n" +
                                                " §2| §r§fAverage Sadan: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.compSplitTimes[5] / DungeonStats.globalM6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.compLagTimes[5] / DungeonStats.globalM6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Sadan: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.bestSplits[5]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM6Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionM6Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session M6 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.compSplitTimes[0] / DungeonStats.sessionM6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.compLagTimes[0] / DungeonStats.sessionM6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.compSplitTimes[1] / DungeonStats.sessionM6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.compLagTimes[1] / DungeonStats.sessionM6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.compSplitTimes[2] / DungeonStats.sessionM6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.compLagTimes[2] / DungeonStats.sessionM6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Terracottas: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.compSplitTimes[3] / DungeonStats.sessionM6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.compLagTimes[3] / DungeonStats.sessionM6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Terracottas: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fAverage Giants: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.compSplitTimes[4] / DungeonStats.sessionM6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.compLagTimes[4] / DungeonStats.sessionM6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Giants: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.bestSplits[4]) + "\n" +
                                                " §2| §r§fAverage Sadan: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.compSplitTimes[5] / DungeonStats.sessionM6Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.compLagTimes[5] / DungeonStats.sessionM6Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Sadan: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.bestSplits[5]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                }
                                return;
                            case "lag":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM6Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global M6 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.totalLag / DungeonStats.globalM6Stats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM6Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session M6 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM6Stats.totalLag / DungeonStats.sessionM6Stats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "global":
                                if (DungeonStats.globalM6Stats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4M6 Total Run Stats:\n" +
                                            " §2| §r§fRuns Tracked: §a§l" + DungeonStats.globalM6Stats.totalRuns + "\n" +
                                            " §2| §r§fRuns Completed: §a§l" + DungeonStats.globalM6Stats.totalComps + "\n" +
                                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.totalCompTime / DungeonStats.globalM6Stats.totalComps) + "\n" +
                                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.totalCompTime) + "\n" +
                                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.totalLag) + "\n" +
                                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.totalTime - DungeonStats.globalM6Stats.totalCompTime) + "\n" +
                                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.totalTime / DungeonStats.globalM6Stats.totalRuns) + "\n" +
                                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.fastest) + "\n" +
                                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM6Stats.slowest)
                                    );
                                }
                                return;
                            default:
                                ChatUtils.system("Unrecognized Tag! Do </dungeontime m6 help> for more info.");
                                break;
                        }
                    }
                    break;
                case "m7":
                    if (args.length == 1) {
                        if (DungeonStats.sessionM7Stats.totalRuns == 0) {
                            ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4M7 Session Run Stats:\n" +
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
                                        "§2=§r View total run split times with /dungeontime m7 phases\n" +
                                        "§2=§r View total times lost to lag with /dungeontime m7 lag\n" +
                                        "§2=§r View global tracker times with /dungeontime m7 <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM7Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalM7Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global Fastest M7 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fMaxor Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fStorm Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fTerminals Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastestLagSplits[5]) + " §rlost to lag)\n" +
                                                " §2| §r§fGoldor Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastestSplits[6]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastestLagSplits[6]) + " §rlost to lag)\n" +
                                                " §2| §r§fNecron Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastestSplits[7]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastestLagSplits[7]) + " §rlost to lag)\n" +
                                                " §2| §r§fDragons Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastestSplits[8]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastestLagSplits[8]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM7Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionM7Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session Fastest M7 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fMaxor Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fStorm Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fTerminals Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastestLagSplits[5]) + " §rlost to lag)\n" +
                                                " §2| §r§fGoldor Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastestSplits[6]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastestLagSplits[6]) + " §rlost to lag)\n" +
                                                " §2| §r§fNecron Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastestSplits[7]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastestLagSplits[7]) + " §rlost to lag)\n" +
                                                " §2| §r§fDragons Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastestSplits[8]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastestLagSplits[8]) + " §rlost to lag)\n" +
                                                " §2| §r§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastest - sumOfBest) + " §rbehind sum of best!"
                                        );
                                    }
                                }
                                return;
                            case "slowest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM7Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Slowest M7 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fMaxor Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fStorm Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fTerminals Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowestLagSplits[5]) + " §rlost to lag)\n" +
                                                " §2| §r§fGoldor Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowestSplits[6]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowestLagSplits[6]) + " §rlost to lag)\n" +
                                                " §2| §r§fNecron Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowestSplits[7]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowestLagSplits[7]) + " §rlost to lag)\n" +
                                                " §2| §r§fDragons Took: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowestSplits[8]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.slowestLagSplits[8]) + " §rlost to lag)"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM7Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Slowest M7 Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowest) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Rush Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fBlood Camp Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBoss Enter Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fMaxor Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fStorm Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowestLagSplits[4]) + " §rlost to lag)\n" +
                                                " §2| §r§fTerminals Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowestSplits[5]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowestLagSplits[5]) + " §rlost to lag)\n" +
                                                " §2| §r§fGoldor Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowestSplits[6]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowestLagSplits[6]) + " §rlost to lag)\n" +
                                                " §2| §r§fNecron Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowestSplits[7]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowestLagSplits[7]) + " §rlost to lag)\n" +
                                                " §2| §r§fDragons Took: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowestSplits[8]) + " §r(§a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowestLagSplits[8]) + " §rlost to lag)"
                                        );
                                    }
                                }
                                return;
                            case "phases":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM7Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.globalM7Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Global M7 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.compSplitTimes[0] / DungeonStats.globalM7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.compLagTimes[0] / DungeonStats.globalM7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.compSplitTimes[1] / DungeonStats.globalM7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.compLagTimes[1] / DungeonStats.globalM7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.compSplitTimes[2] / DungeonStats.globalM7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.compLagTimes[2] / DungeonStats.globalM7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Maxor: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.compSplitTimes[3] / DungeonStats.globalM7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.compLagTimes[3] / DungeonStats.globalM7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Maxor: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fAverage Storm: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.compSplitTimes[4] / DungeonStats.globalM7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.compLagTimes[4] / DungeonStats.globalM7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Storm: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.bestSplits[4]) + "\n" +
                                                " §2| §r§fAverage Terminals: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.compSplitTimes[5] / DungeonStats.globalM7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.compLagTimes[5] / DungeonStats.globalM7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Terminals: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.bestSplits[5]) + "\n" +
                                                " §2| §r§fAverage Goldor: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.compSplitTimes[6] / DungeonStats.globalM7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.compLagTimes[6] / DungeonStats.globalM7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Goldor: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.bestSplits[6]) + "\n" +
                                                " §2| §r§fAverage Necron: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.compSplitTimes[7] / DungeonStats.globalM7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.compLagTimes[7] / DungeonStats.globalM7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Necron: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.bestSplits[7]) + "\n" +
                                                " §2| §r§fAverage Dragons: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.compSplitTimes[8] / DungeonStats.globalM7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.compLagTimes[8] / DungeonStats.globalM7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Dragons: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.bestSplits[8]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM7Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        double sumOfBest = 0.0;
                                        for (double time : DungeonStats.sessionM7Stats.bestSplits) {
                                            sumOfBest += time;
                                        }

                                        ChatUtils.system("§4Session M7 Splits Breakdown:\n" +
                                                " §2| §r§fAverage Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.compSplitTimes[0] / DungeonStats.sessionM7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.compLagTimes[0] / DungeonStats.sessionM7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Rush: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.bestSplits[0]) + "\n" +
                                                " §2| §r§fAverage Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.compSplitTimes[1] / DungeonStats.sessionM7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.compLagTimes[1] / DungeonStats.sessionM7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Blood Camp: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.bestSplits[1]) + "\n" +
                                                " §2| §r§fAverage Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.compSplitTimes[2] / DungeonStats.sessionM7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.compLagTimes[2] / DungeonStats.sessionM7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Boss Enter: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.bestSplits[2]) + "\n" +
                                                " §2| §r§fAverage Maxor: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.compSplitTimes[3] / DungeonStats.sessionM7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.compLagTimes[3] / DungeonStats.sessionM7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Maxor: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.bestSplits[3]) + "\n" +
                                                " §2| §r§fAverage Storm: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.compSplitTimes[4] / DungeonStats.sessionM7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.compLagTimes[4] / DungeonStats.sessionM7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Storm: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.bestSplits[4]) + "\n" +
                                                " §2| §r§fAverage Terminals: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.compSplitTimes[5] / DungeonStats.sessionM7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.compLagTimes[5] / DungeonStats.sessionM7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Terminals: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.bestSplits[5]) + "\n" +
                                                " §2| §r§fAverage Goldor: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.compSplitTimes[6] / DungeonStats.sessionM7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.compLagTimes[6] / DungeonStats.sessionM7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Goldor: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.bestSplits[6]) + "\n" +
                                                " §2| §r§fAverage Necron: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.compSplitTimes[7] / DungeonStats.sessionM7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.compLagTimes[7] / DungeonStats.sessionM7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Necron: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.bestSplits[7]) + "\n" +
                                                " §2| §r§fAverage Dragons: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.compSplitTimes[8] / DungeonStats.sessionM7Stats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.compLagTimes[8] / DungeonStats.sessionM7Stats.totalComps) + ")\n" +
                                                " §2|- §r§fBest Dragons: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.bestSplits[8]) + "\n" +
                                                " §2| §r§fBest Possible Run: §a§l" + ChatUtils.formatTime(sumOfBest)
                                        );
                                    }
                                }
                                return;
                            case "lag":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (DungeonStats.globalM7Stats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global M7 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.globalM7Stats.totalLag / DungeonStats.globalM7Stats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (DungeonStats.sessionM7Stats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session M7 Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(DungeonStats.sessionM7Stats.totalLag / DungeonStats.sessionM7Stats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "global":
                                if (DungeonStats.globalM7Stats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4M7 Total Run Stats:\n" +
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
