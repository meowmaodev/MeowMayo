package com.mmdev.meowmayo.commands;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.features.kuudra.tracker.KuudraStats;
import com.mmdev.meowmayo.utils.ChatUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.Arrays;
import java.util.List;

public class KuudraTimeCommand extends CommandBase {
    private ToggleSetting kuudraTrack = (ToggleSetting) ConfigSettings.getSetting("Average Kuudra Run Time Tracker");

    @Override
    public String getCommandName() {
        return "kuudratime";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("kt");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // allow all players to run
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "kuudratime";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (!kuudraTrack.getValue()) {
            ChatUtils.system("You are not currently tracking run data!");
            return;
        }
        if (args.length == 0) {
            ChatUtils.system("Please input a tier to view!");
        } else {
            switch (args[0].toLowerCase()) {
                case "basic":
                    if (args.length == 1) {
                        if (KuudraStats.sessionBasicStats.totalRuns == 0) {
                            ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4Kuudra Basic Tier Session Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + KuudraStats.sessionBasicStats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + KuudraStats.sessionBasicStats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.totalCompTime / KuudraStats.sessionBasicStats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.totalTime - KuudraStats.sessionBasicStats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.totalTime / KuudraStats.sessionBasicStats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.slowest)
                            );
                        }
                        return;
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "help":
                                ChatUtils.system("Kuudra Basic Tier Run Tracker:\n" +
                                        "§2=§r View General stats with /kuudratime basic\n" +
                                        "§2=§r View fastest run splits with /kuudratime basic fastest\n" +
                                        "§2=§r View slowest run splits with /kuudratime basic slowest\n" +
                                        "§2=§r View total run split times with /kuudratime basic phases\n" +
                                        "§2=§r View total times lost to lag with /kuudratime basic lag\n" +
                                        "§2=§r View global tracker times with /kuudratime basic <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalBasicStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Fastest Basic Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.fastest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.fastestLagSplits[3]) + " §rlost to lag)"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionBasicStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Fastest Basic Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.fastest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.fastestLagSplits[3]) + " §rlost to lag)"
                                        );
                                    }
                                }
                                return;
                            case "slowest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalBasicStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Slowest Basic Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.slowest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.slowestLagSplits[3]) + " §rlost to lag)"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionBasicStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Slowest Basic Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.slowest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.slowestLagSplits[3]) + " §rlost to lag)"
                                        );
                                    }
                                }
                                return;
                            case "phases":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalBasicStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Basic Splits Breakdown:\n" +
                                                " §2| §r§fAverage Supplies Spawn: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.compSplitTimes[0] / KuudraStats.globalBasicStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.compLagTimes[0] / KuudraStats.globalBasicStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Supplies: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.compSplitTimes[1] / KuudraStats.globalBasicStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.compLagTimes[1] / KuudraStats.globalBasicStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Build: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.compSplitTimes[2] / KuudraStats.globalBasicStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.compLagTimes[2] / KuudraStats.globalBasicStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage DPS: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.compSplitTimes[3] / KuudraStats.globalBasicStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.compLagTimes[3] / KuudraStats.globalBasicStats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionBasicStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Basic Splits Breakdown:\n" +
                                                " §2| §r§fAverage Supplies Spawn: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.compSplitTimes[0] / KuudraStats.sessionBasicStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.compLagTimes[0] / KuudraStats.sessionBasicStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Supplies: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.compSplitTimes[1] / KuudraStats.sessionBasicStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.compLagTimes[1] / KuudraStats.sessionBasicStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Build: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.compSplitTimes[2] / KuudraStats.sessionBasicStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.compLagTimes[2] / KuudraStats.sessionBasicStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage DPS: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.compSplitTimes[3] / KuudraStats.sessionBasicStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.compLagTimes[3] / KuudraStats.sessionBasicStats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "lag":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalBasicStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Basic Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.totalLag / KuudraStats.globalBasicStats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionBasicStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Basic Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBasicStats.totalLag / KuudraStats.sessionBasicStats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "global":
                                if (KuudraStats.globalBasicStats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4Kuudra Basic Tier Total Run Stats:\n" +
                                            " §2| §r§fRuns Tracked: §a§l" + KuudraStats.globalBasicStats.totalRuns + "\n" +
                                            " §2| §r§fRuns Completed: §a§l" + KuudraStats.globalBasicStats.totalComps + "\n" +
                                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.totalCompTime / KuudraStats.globalBasicStats.totalComps) + "\n" +
                                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.totalCompTime) + "\n" +
                                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.totalLag) + "\n" +
                                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.totalTime - KuudraStats.globalBasicStats.totalCompTime) + "\n" +
                                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.totalTime / KuudraStats.globalBasicStats.totalRuns) + "\n" +
                                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.fastest) + "\n" +
                                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalBasicStats.slowest)
                                    );
                                }
                                return;
                            default:
                                ChatUtils.system("Unrecognized Tag! Do </kuudratime basic help> for more info.");
                                break;
                        }
                    }
                    break;
                case "hot":
                    if (args.length == 1) {
                        if (KuudraStats.sessionHotStats.totalRuns == 0) {
                            ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4Kuudra Hot Tier Session Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + KuudraStats.sessionHotStats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + KuudraStats.sessionHotStats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.totalCompTime / KuudraStats.sessionHotStats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.totalTime - KuudraStats.sessionHotStats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.totalTime / KuudraStats.sessionHotStats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.slowest)
                            );
                        }
                        return;
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "help":
                                ChatUtils.system("Kuudra Hot Tier Run Tracker:\n" +
                                        "§2=§r View General stats with /kuudratime hot\n" +
                                        "§2=§r View fastest run splits with /kuudratime hot fastest\n" +
                                        "§2=§r View slowest run splits with /kuudratime hot slowest\n" +
                                        "§2=§r View total run split times with /kuudratime hot phases\n" +
                                        "§2=§r View total times lost to lag with /kuudratime hot lag\n" +
                                        "§2=§r View global tracker times with /kuudratime hot <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalHotStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Fastest Hot Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.fastest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.fastestLagSplits[3]) + " §rlost to lag)"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionHotStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Fastest Hot Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.fastest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.fastestLagSplits[3]) + " §rlost to lag)"
                                        );
                                    }
                                }
                                return;
                            case "slowest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalHotStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Slowest Hot Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.slowest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.slowestLagSplits[3]) + " §rlost to lag)"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionHotStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Slowest Hot Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.slowest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.slowestLagSplits[3]) + " §rlost to lag)"
                                        );
                                    }
                                }
                                return;
                            case "phases":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalHotStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Hot Splits Breakdown:\n" +
                                                " §2| §r§fAverage Supplies Spawn: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.compSplitTimes[0] / KuudraStats.globalHotStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.compLagTimes[0] / KuudraStats.globalHotStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Supplies: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.compSplitTimes[1] / KuudraStats.globalHotStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.compLagTimes[1] / KuudraStats.globalHotStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Build: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.compSplitTimes[2] / KuudraStats.globalHotStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.compLagTimes[2] / KuudraStats.globalHotStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage DPS: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.compSplitTimes[3] / KuudraStats.globalHotStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.compLagTimes[3] / KuudraStats.globalHotStats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionHotStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Hot Splits Breakdown:\n" +
                                                " §2| §r§fAverage Supplies Spawn: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.compSplitTimes[0] / KuudraStats.sessionHotStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.compLagTimes[0] / KuudraStats.sessionHotStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Supplies: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.compSplitTimes[1] / KuudraStats.sessionHotStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.compLagTimes[1] / KuudraStats.sessionHotStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Build: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.compSplitTimes[2] / KuudraStats.sessionHotStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.compLagTimes[2] / KuudraStats.sessionHotStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage DPS: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.compSplitTimes[3] / KuudraStats.sessionHotStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.compLagTimes[3] / KuudraStats.sessionHotStats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "lag":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalHotStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Hot Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.totalLag / KuudraStats.globalHotStats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionHotStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Hot Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(KuudraStats.sessionHotStats.totalLag / KuudraStats.sessionHotStats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "global":
                                if (KuudraStats.globalHotStats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4Kuudra Hot Tier Total Run Stats:\n" +
                                            " §2| §r§fRuns Tracked: §a§l" + KuudraStats.globalHotStats.totalRuns + "\n" +
                                            " §2| §r§fRuns Completed: §a§l" + KuudraStats.globalHotStats.totalComps + "\n" +
                                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.totalCompTime / KuudraStats.globalHotStats.totalComps) + "\n" +
                                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.totalCompTime) + "\n" +
                                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.totalLag) + "\n" +
                                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.totalTime - KuudraStats.globalHotStats.totalCompTime) + "\n" +
                                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.totalTime / KuudraStats.globalHotStats.totalRuns) + "\n" +
                                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.fastest) + "\n" +
                                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalHotStats.slowest)
                                    );
                                }
                                return;
                            default:
                                ChatUtils.system("Unrecognized Tag! Do </kuudratime hot help> for more info.");
                                break;
                        }
                    }
                    break;
                case "burning":
                    if (args.length == 1) {
                        if (KuudraStats.sessionBurningStats.totalRuns == 0) {
                            ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4Kuudra Burning Tier Session Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + KuudraStats.sessionBurningStats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + KuudraStats.sessionBurningStats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.totalCompTime / KuudraStats.sessionBurningStats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.totalTime - KuudraStats.sessionBurningStats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.totalTime / KuudraStats.sessionBurningStats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.slowest)
                            );
                        }
                        return;
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "help":
                                ChatUtils.system("Kuudra Burning Tier Run Tracker:\n" +
                                        "§2=§r View General stats with /kuudratime burning\n" +
                                        "§2=§r View fastest run splits with /kuudratime burning fastest\n" +
                                        "§2=§r View slowest run splits with /kuudratime burning slowest\n" +
                                        "§2=§r View total run split times with /kuudratime burning phases\n" +
                                        "§2=§r View total times lost to lag with /kuudratime burning lag\n" +
                                        "§2=§r View global tracker times with /kuudratime burning <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalBurningStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Fastest Burning Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.fastest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.fastestLagSplits[3]) + " §rlost to lag)"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionBurningStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Fastest Burning Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.fastest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.fastestLagSplits[3]) + " §rlost to lag)"
                                        );
                                    }
                                }
                                return;
                            case "slowest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalBurningStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Slowest Burning Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.slowest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.slowestLagSplits[3]) + " §rlost to lag)"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionBurningStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Slowest Burning Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.slowest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.slowestLagSplits[3]) + " §rlost to lag)"
                                        );
                                    }
                                }
                                return;
                            case "phases":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalBurningStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Burning Splits Breakdown:\n" +
                                                " §2| §r§fAverage Supplies Spawn: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.compSplitTimes[0] / KuudraStats.globalBurningStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.compLagTimes[0] / KuudraStats.globalBurningStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Supplies: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.compSplitTimes[1] / KuudraStats.globalBurningStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.compLagTimes[1] / KuudraStats.globalBurningStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Build: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.compSplitTimes[2] / KuudraStats.globalBurningStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.compLagTimes[2] / KuudraStats.globalBurningStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage DPS: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.compSplitTimes[3] / KuudraStats.globalBurningStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.compLagTimes[3] / KuudraStats.globalBurningStats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionBurningStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Burning Splits Breakdown:\n" +
                                                " §2| §r§fAverage Supplies Spawn: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.compSplitTimes[0] / KuudraStats.sessionBurningStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.compLagTimes[0] / KuudraStats.sessionBurningStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Supplies: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.compSplitTimes[1] / KuudraStats.sessionBurningStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.compLagTimes[1] / KuudraStats.sessionBurningStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Build: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.compSplitTimes[2] / KuudraStats.sessionBurningStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.compLagTimes[2] / KuudraStats.sessionBurningStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage DPS: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.compSplitTimes[3] / KuudraStats.sessionBurningStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.compLagTimes[3] / KuudraStats.sessionBurningStats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "lag":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalBurningStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Burning Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.totalLag / KuudraStats.globalBurningStats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionBurningStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Burning Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(KuudraStats.sessionBurningStats.totalLag / KuudraStats.sessionBurningStats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "global":
                                if (KuudraStats.globalBurningStats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4Kuudra Burning Tier Total Run Stats:\n" +
                                            " §2| §r§fRuns Tracked: §a§l" + KuudraStats.globalBurningStats.totalRuns + "\n" +
                                            " §2| §r§fRuns Completed: §a§l" + KuudraStats.globalBurningStats.totalComps + "\n" +
                                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.totalCompTime / KuudraStats.globalBurningStats.totalComps) + "\n" +
                                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.totalCompTime) + "\n" +
                                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.totalLag) + "\n" +
                                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.totalTime - KuudraStats.globalBurningStats.totalCompTime) + "\n" +
                                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.totalTime / KuudraStats.globalBurningStats.totalRuns) + "\n" +
                                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.fastest) + "\n" +
                                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalBurningStats.slowest)
                                    );
                                }
                                return;
                            default:
                                ChatUtils.system("Unrecognized Tag! Do </kuudratime burning help> for more info.");
                                break;
                        }
                    }
                    break;
                case "fiery":
                    if (args.length == 1) {
                        if (KuudraStats.sessionFieryStats.totalRuns == 0) {
                            ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4Kuudra Fiery Tier Session Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + KuudraStats.sessionFieryStats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + KuudraStats.sessionFieryStats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.totalCompTime / KuudraStats.sessionFieryStats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.totalTime - KuudraStats.sessionFieryStats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.totalTime / KuudraStats.sessionFieryStats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.slowest)
                            );
                        }
                        return;
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "help":
                                ChatUtils.system("Kuudra Fiery Tier Run Tracker:\n" +
                                        "§2=§r View General stats with /kuudratime fiery\n" +
                                        "§2=§r View fastest run splits with /kuudratime fiery fastest\n" +
                                        "§2=§r View slowest run splits with /kuudratime fiery slowest\n" +
                                        "§2=§r View total run split times with /kuudratime fiery phases\n" +
                                        "§2=§r View total times lost to lag with /kuudratime fiery lag\n" +
                                        "§2=§r View global tracker times with /kuudratime fiery <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalFieryStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Fastest Fiery Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.fastest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.fastestLagSplits[3]) + " §rlost to lag)"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionFieryStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Fastest Fiery Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.fastest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.fastestLagSplits[3]) + " §rlost to lag)"
                                        );
                                    }
                                }
                                return;
                            case "slowest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalFieryStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Slowest Fiery Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.slowest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.slowestLagSplits[3]) + " §rlost to lag)"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionFieryStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Slowest Fiery Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.slowest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.slowestLagSplits[3]) + " §rlost to lag)"
                                        );
                                    }
                                }
                                return;
                            case "phases":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalFieryStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Fiery Splits Breakdown:\n" +
                                                " §2| §r§fAverage Supplies Spawn: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.compSplitTimes[0] / KuudraStats.globalFieryStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.compLagTimes[0] / KuudraStats.globalFieryStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Supplies: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.compSplitTimes[1] / KuudraStats.globalFieryStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.compLagTimes[1] / KuudraStats.globalFieryStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Build: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.compSplitTimes[2] / KuudraStats.globalFieryStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.compLagTimes[2] / KuudraStats.globalFieryStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage DPS: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.compSplitTimes[3] / KuudraStats.globalFieryStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.compLagTimes[3] / KuudraStats.globalFieryStats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionFieryStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Fiery Splits Breakdown:\n" +
                                                " §2| §r§fAverage Supplies Spawn: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.compSplitTimes[0] / KuudraStats.sessionFieryStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.compLagTimes[0] / KuudraStats.sessionFieryStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Supplies: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.compSplitTimes[1] / KuudraStats.sessionFieryStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.compLagTimes[1] / KuudraStats.sessionFieryStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Build: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.compSplitTimes[2] / KuudraStats.sessionFieryStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.compLagTimes[2] / KuudraStats.sessionFieryStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage DPS: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.compSplitTimes[3] / KuudraStats.sessionFieryStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.compLagTimes[3] / KuudraStats.sessionFieryStats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "lag":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalFieryStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Fiery Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.totalLag / KuudraStats.globalFieryStats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionFieryStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Fiery Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(KuudraStats.sessionFieryStats.totalLag / KuudraStats.sessionFieryStats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "global":
                                if (KuudraStats.globalFieryStats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4Kuudra Fiery Tier Total Run Stats:\n" +
                                            " §2| §r§fRuns Tracked: §a§l" + KuudraStats.globalFieryStats.totalRuns + "\n" +
                                            " §2| §r§fRuns Completed: §a§l" + KuudraStats.globalFieryStats.totalComps + "\n" +
                                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.totalCompTime / KuudraStats.globalFieryStats.totalComps) + "\n" +
                                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.totalCompTime) + "\n" +
                                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.totalLag) + "\n" +
                                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.totalTime - KuudraStats.globalFieryStats.totalCompTime) + "\n" +
                                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.totalTime / KuudraStats.globalFieryStats.totalRuns) + "\n" +
                                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.fastest) + "\n" +
                                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalFieryStats.slowest)
                                    );
                                }
                                return;
                            default:
                                ChatUtils.system("Unrecognized Tag! Do </kuudratime fiery help> for more info.");
                                break;
                        }
                    }
                    break;
                case "infernal":
                    if (args.length == 1) {
                        if (KuudraStats.sessionInfernalStats.totalRuns == 0) {
                            ChatUtils.system("You have no session runs tracked!");
                        } else {
                            ChatUtils.system("§4Kuudra Infernal Tier Session Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + KuudraStats.sessionInfernalStats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + KuudraStats.sessionInfernalStats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.totalCompTime / KuudraStats.sessionInfernalStats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.totalTime - KuudraStats.sessionInfernalStats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.totalTime / KuudraStats.sessionInfernalStats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.slowest)
                            );
                        }
                        return;
                    } else {
                        switch (args[1].toLowerCase()) {
                            case "help":
                                ChatUtils.system("Kuudra Infernal Tier Run Tracker:\n" +
                                        "§2=§r View General stats with /kuudratime infernal\n" +
                                        "§2=§r View fastest run splits with /kuudratime infernal fastest\n" +
                                        "§2=§r View slowest run splits with /kuudratime infernal slowest\n" +
                                        "§2=§r View total run split times with /kuudratime infernal phases\n" +
                                        "§2=§r View total times lost to lag with /kuudratime infernal lag\n" +
                                        "§2=§r View global tracker times with /kuudratime infernal <key> global");
                                return;
                            case "fastest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalInfernalStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Fastest Infernal Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.fastest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fFinal Phase Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.fastestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.fastestLagSplits[4]) + " §rlost to lag)"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionInfernalStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Fastest Infernal Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.fastest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.fastestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.fastestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.fastestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.fastestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.fastestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.fastestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.fastestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.fastestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.fastestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fFinal Phase Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.fastestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.fastestLagSplits[4]) + " §rlost to lag)"
                                        );
                                    }
                                }
                                return;
                            case "slowest":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalInfernalStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Slowest Infernal Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.slowest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.slowestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fFinal Phase Took: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.slowestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.slowestLagSplits[4]) + " §rlost to lag)"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionInfernalStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Slowest Infernal Run:\n" +
                                                " §2| §r§fTotal Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.slowest) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.slowestLag) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Spawn Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.slowestSplits[0]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.slowestLagSplits[0]) + " §rlost to lag)\n" +
                                                " §2| §r§fSupplies Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.slowestSplits[1]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.slowestLagSplits[1]) + " §rlost to lag)\n" +
                                                " §2| §r§fBuild Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.slowestSplits[2]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.slowestLagSplits[2]) + " §rlost to lag)\n" +
                                                " §2| §r§fDPS Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.slowestSplits[3]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.slowestLagSplits[3]) + " §rlost to lag)\n" +
                                                " §2| §r§fFinal Phase Took: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.slowestSplits[4]) + " §r(§a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.slowestLagSplits[4]) + " §rlost to lag)"
                                        );
                                    }
                                }
                                return;
                            case "phases":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalInfernalStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Infernal Splits Breakdown:\n" +
                                                " §2| §r§fAverage Supplies Spawn: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.compSplitTimes[0] / KuudraStats.globalInfernalStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.compLagTimes[0] / KuudraStats.globalInfernalStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Supplies: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.compSplitTimes[1] / KuudraStats.globalInfernalStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.compLagTimes[1] / KuudraStats.globalInfernalStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Build: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.compSplitTimes[2] / KuudraStats.globalInfernalStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.compLagTimes[2] / KuudraStats.globalInfernalStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage DPS: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.compSplitTimes[3] / KuudraStats.globalInfernalStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.compLagTimes[3] / KuudraStats.globalInfernalStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Final Phase: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.compSplitTimes[4] / KuudraStats.globalInfernalStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.compLagTimes[4] / KuudraStats.globalInfernalStats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionInfernalStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Infernal Splits Breakdown:\n" +
                                                " §2| §r§fAverage Supplies Spawn: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.compSplitTimes[0] / KuudraStats.sessionInfernalStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.compLagTimes[0] / KuudraStats.sessionInfernalStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Supplies: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.compSplitTimes[1] / KuudraStats.sessionInfernalStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.compLagTimes[1] / KuudraStats.sessionInfernalStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Build: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.compSplitTimes[2] / KuudraStats.sessionInfernalStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.compLagTimes[2] / KuudraStats.sessionInfernalStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage DPS: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.compSplitTimes[3] / KuudraStats.sessionInfernalStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.compLagTimes[3] / KuudraStats.sessionInfernalStats.totalComps) + ")\n" +
                                                " §2| §r§fAverage Final Phase: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.compSplitTimes[4] / KuudraStats.sessionInfernalStats.totalComps) + " §r(Average Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.compLagTimes[4] / KuudraStats.sessionInfernalStats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "lag":
                                if (args.length == 3 && args[2].equalsIgnoreCase("global")) {
                                    if (KuudraStats.globalInfernalStats.totalComps == 0) {
                                        ChatUtils.system("You have no global runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Global Infernal Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.totalLag / KuudraStats.globalInfernalStats.totalComps) + ")"
                                        );
                                    }
                                } else {
                                    if (KuudraStats.sessionInfernalStats.totalComps == 0) {
                                        ChatUtils.system("You have no session runs tracked!");
                                    } else {
                                        ChatUtils.system("§4Session Infernal Lag Breakdown:\n" +
                                                " §2| §r§fTotal Time Lost to Lag: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.totalLag) + " §r(Average Lag Per Run: §a§l" + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.totalLag / KuudraStats.sessionInfernalStats.totalComps) + ")"
                                        );
                                    }
                                }
                                return;
                            case "global":
                                if (KuudraStats.globalInfernalStats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4Kuudra Infernal Tier Total Run Stats:\n" +
                                            " §2| §r§fRuns Tracked: §a§l" + KuudraStats.globalInfernalStats.totalRuns + "\n" +
                                            " §2| §r§fRuns Completed: §a§l" + KuudraStats.globalInfernalStats.totalComps + "\n" +
                                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.totalCompTime / KuudraStats.globalInfernalStats.totalComps) + "\n" +
                                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.totalCompTime) + "\n" +
                                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.totalLag) + "\n" +
                                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.totalTime - KuudraStats.globalInfernalStats.totalCompTime) + "\n" +
                                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.totalTime / KuudraStats.globalInfernalStats.totalRuns) + "\n" +
                                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.fastest) + "\n" +
                                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(KuudraStats.globalInfernalStats.slowest)
                                    );
                                }
                                return;
                            default:
                                ChatUtils.system("Unrecognized Tag! Do </kuudratime infernal help> for more info.");
                                break;
                        }
                    }
                    break;
            }
        }
    }
}
