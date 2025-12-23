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
                                        "§2=§r View total run split times with /kuudratime basic total\n" +
                                        "§2=§r View total times lost to lag with /kuudratime basic lag\n" +
                                        "§2=§r View global tracker times with /kuudratime basic <key> global");
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
                                        "§2=§r View total run split times with /kuudratime hot total\n" +
                                        "§2=§r View total times lost to lag with /kuudratime hot lag\n" +
                                        "§2=§r View global tracker times with /kuudratime hot <key> global");
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
                                        "§2=§r View total run split times with /kuudratime burning total\n" +
                                        "§2=§r View total times lost to lag with /kuudratime burning lag\n" +
                                        "§2=§r View global tracker times with /kuudratime burning <key> global");
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
                                if (KuudraStats.globalBurningStats.totalRuns == 0) {
                                    ChatUtils.system("You have no global runs tracked!");
                                } else {
                                    ChatUtils.system("§4Kuudra Basic Tier Total Run Stats:\n" +
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
                                        "§2=§r View total run split times with /kuudratime fiery total\n" +
                                        "§2=§r View total times lost to lag with /kuudratime fiery lag\n" +
                                        "§2=§r View global tracker times with /kuudratime fiery <key> global");
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
                                        "§2=§r View total run split times with /kuudratime infernal total\n" +
                                        "§2=§r View total times lost to lag with /kuudratime infernal lag\n" +
                                        "§2=§r View global tracker times with /kuudratime infernal <key> global");
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
