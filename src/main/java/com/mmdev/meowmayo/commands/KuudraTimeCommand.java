package com.mmdev.meowmayo.commands;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.features.kuudra.tracker.KuudraPhases;
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
        return "kooter";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (!kuudraTrack.getValue()) {
            ChatUtils.system("You are not currently tracking run data!");
            return;
        }
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("global")) {
                if (KuudraPhases.globalStats.totalRuns == 0) {
                    ChatUtils.system("No global run data is available!");
                    return;
                }

                if (args.length == 2) {
                    switch (args[1].toLowerCase()) {
                        case "help":
                            ChatUtils.system("Kuudra Global Run Tracker:\n" +
                                    "§2=§r View General stats with /kuudratime global\n" +
                                    "§2=§r View fastest run splits with /kuudratime global fastest\n" +
                                    "§2=§r View slowest run splits with /kuudratime global slowest\n" +
                                    "§2=§r View total run split times with /kuudratime global total\n" +
                                    "§2=§r View total times lost to lag with /kuudratime global lag");
                            return;
                        case "fastest":
                            return;
                        case "slowest":
                            return;
                        case "total":
                            return;
                        case "lag":
                            return;
                        default:
                            ChatUtils.system("§4Kuudra Total Run Stats:\n" +
                                    " §2| §r§fRuns Tracked: §a§l" + KuudraPhases.globalStats.totalRuns + "\n" +
                                    " §2| §r§fRuns Completed: §a§l" + KuudraPhases.globalStats.totalComps + "\n" +
                                    " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(KuudraPhases.globalStats.totalCompTime / KuudraPhases.globalStats.totalComps) + "\n" +
                                    " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(KuudraPhases.globalStats.totalCompTime) + "\n" +
                                    " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(KuudraPhases.globalStats.totalLag) + "\n" +
                                    " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(KuudraPhases.globalStats.totalTime - KuudraPhases.globalStats.totalCompTime) + "\n" +
                                    " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(KuudraPhases.globalStats.totalTime / KuudraPhases.globalStats.totalRuns) + "\n" +
                                    " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(KuudraPhases.globalStats.fastest) + "\n" +
                                    " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(KuudraPhases.globalStats.slowest)
                            );
                            return;
                    }
                } else {
                    ChatUtils.system("§4Kuudra Total Run Stats:\n" +
                            " §2| §r§fRuns Tracked: §a§l" + KuudraPhases.globalStats.totalRuns + "\n" +
                            " §2| §r§fRuns Completed: §a§l" + KuudraPhases.globalStats.totalComps + "\n" +
                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(KuudraPhases.globalStats.totalCompTime / KuudraPhases.globalStats.totalComps) + "\n" +
                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(KuudraPhases.globalStats.totalCompTime) + "\n" +
                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(KuudraPhases.globalStats.totalLag) + "\n" +
                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(KuudraPhases.globalStats.totalTime - KuudraPhases.globalStats.totalCompTime) + "\n" +
                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(KuudraPhases.globalStats.totalTime / KuudraPhases.globalStats.totalRuns) + "\n" +
                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(KuudraPhases.globalStats.fastest) + "\n" +
                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(KuudraPhases.globalStats.slowest)
                    );
                    return;
                }
            }

            if (KuudraPhases.stats.totalRuns == 0) {
                ChatUtils.system("No session run data is available!");
                return;
            }
            switch (args[0].toLowerCase()) {
                case "help":
                    ChatUtils.system("Kuudra Run Tracker:\n" +
                            "§2=§r View General stats with /kuudratime\n" +
                            "§2=§r View fastest run splits with /kuudratime fastest\n" +
                            "§2=§r View slowest run splits with /kuudratime slowest\n" +
                            "§2=§r View total run split times with /kuudratime total\n" +
                            "§2=§r View total times lost to lag with /kuudratime lag");
                    return;
                case "fastest": // fill these out whenever ig
                    return;
                case "slowest":
                    return;
                case "total":
                    return;
                case "lag":
                    return;
                default:
                    ChatUtils.system("§4Kuudra Session Run Stats:\n" +
                            " §2| §r§fRuns Tracked: §a§l" + KuudraPhases.stats.totalRuns + "\n" +
                            " §2| §r§fRuns Completed: §a§l" + KuudraPhases.stats.totalComps + "\n" +
                            " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(KuudraPhases.stats.totalCompTime / KuudraPhases.stats.totalComps) + "\n" +
                            " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(KuudraPhases.stats.totalCompTime) + "\n" +
                            " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(KuudraPhases.stats.totalLag) + "\n" +
                            " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(KuudraPhases.stats.totalTime - KuudraPhases.stats.totalCompTime) + "\n" +
                            " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(KuudraPhases.stats.totalTime / KuudraPhases.stats.totalRuns) + "\n" +
                            " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(KuudraPhases.stats.fastest) + "\n" +
                            " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(KuudraPhases.stats.slowest)
                    );
                    return;
            }
        }

        if (KuudraPhases.stats.totalRuns == 0) {
            ChatUtils.system("No session run data is available!");
            return;
        }

        ChatUtils.system("§4Kuudra Session Run Stats:\n" +
                " §2| §r§fRuns Tracked: §a§l" + KuudraPhases.stats.totalRuns + "\n" +
                " §2| §r§fRuns Completed: §a§l" + KuudraPhases.stats.totalComps + "\n" +
                " §2| §r§fAverage Comp Time: §a§l" + ChatUtils.formatTime(KuudraPhases.stats.totalCompTime / KuudraPhases.stats.totalComps) + "\n" +
                " §2| §r§fTotal Comp Time: §a§l" + ChatUtils.formatTime(KuudraPhases.stats.totalCompTime) + "\n" +
                " §2| §r§fTotal Lag Time: §a§l" + ChatUtils.formatTime(KuudraPhases.stats.totalLag) + "\n" +
                " §2| §r§fFailed Run Wasted Time: §a§l" + ChatUtils.formatTime(KuudraPhases.stats.totalTime - KuudraPhases.stats.totalCompTime) + "\n" +
                " §2| §r§fAverage Run Time: §a§l" + ChatUtils.formatTime(KuudraPhases.stats.totalTime / KuudraPhases.stats.totalRuns) + "\n" +
                " §2| §r§fFastest Run Time: §a§l" + ChatUtils.formatTime(KuudraPhases.stats.fastest) + "\n" +
                " §2| §r§fSlowest Run Time: §a§l" + ChatUtils.formatTime(KuudraPhases.stats.slowest)
        );
    }
}
