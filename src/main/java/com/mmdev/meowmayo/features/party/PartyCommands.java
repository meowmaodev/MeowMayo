package com.mmdev.meowmayo.features.party;

import com.mmdev.meowmayo.features.dungeons.tracker.DungeonStats;
import com.mmdev.meowmayo.features.dungeons.tracker.DungeonTracker;
import com.mmdev.meowmayo.features.kuudra.tracker.KuudraStats;
import com.mmdev.meowmayo.features.kuudra.tracker.KuudraTracker;
import com.mmdev.meowmayo.utils.PartyUtils;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.config.settings.TextSetting;
import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.PartyCommandListUtils;

import static com.mmdev.meowmayo.utils.DelayUtils.scheduleTask;

public class PartyCommands {
    // Party Commands
    private ToggleSetting partyCommands = (ToggleSetting) ConfigSettings.getSetting("Party Commands");
    private TextSetting partyCommandsPrefix = (TextSetting) ConfigSettings.getSetting("Party Commands Prefix");
    private ToggleSetting partyHelpCommand = (ToggleSetting) ConfigSettings.getSetting("Party Help Command");

    // General Party Commands
    private ToggleSetting allInviteCommand = (ToggleSetting) ConfigSettings.getSetting("All Invite Command");
    private ToggleSetting warpCommand = (ToggleSetting) ConfigSettings.getSetting("Warp Command");
    private ToggleSetting inviteCommand = (ToggleSetting) ConfigSettings.getSetting("Invite Command");
    private ToggleSetting partyTransferCommand = (ToggleSetting) ConfigSettings.getSetting("Party Transfer Command");
    private ToggleSetting partyTransferWhitelist = (ToggleSetting) ConfigSettings.getSetting("Party Transfer Whitelist");
    private ToggleSetting partyTransferMeCommand = (ToggleSetting) ConfigSettings.getSetting("Party Transfer Me Command");
    private ToggleSetting partyTransferMeWhitelist = (ToggleSetting) ConfigSettings.getSetting("Party Transfer Me Whitelist");
    private ToggleSetting selloutCommand = (ToggleSetting) ConfigSettings.getSetting("Sellout Command");
    private ToggleSetting coordinatesCommand = (ToggleSetting) ConfigSettings.getSetting("Coordinates Command");

    // Catacombs Commands
    private ToggleSetting dungeonTimeStatsCommand = (ToggleSetting) ConfigSettings.getSetting("Dungeon Time Stats Command");
    private ToggleSetting dungeonTrack = (ToggleSetting) ConfigSettings.getSetting("Average Dungeon Run Time Tracker");
    private ToggleSetting catacombsEntranceCommand = (ToggleSetting) ConfigSettings.getSetting("Catacombs Entrance Command");
    private ToggleSetting masterCatacombsEntranceCommand = (ToggleSetting) ConfigSettings.getSetting("Master Catacombs Entrance Command");

    // Kuudra Commands
    private ToggleSetting kuudraEntranceCommand = (ToggleSetting) ConfigSettings.getSetting("Kuudra Entrance Command");
    private ToggleSetting kuudraTimeStatsCommand = (ToggleSetting) ConfigSettings.getSetting("Kuudra Time Stats Command");
    private ToggleSetting kuudraTrack = (ToggleSetting) ConfigSettings.getSetting("Average Kuudra Run Time Tracker");

    // Fun Commands
    private ToggleSetting coinflipCommand = (ToggleSetting) ConfigSettings.getSetting("Coinflip Command");
    private ToggleSetting diceCommand = (ToggleSetting) ConfigSettings.getSetting("Dice Command");

    private boolean delayed = false;

    private Random r = new Random();

    @SubscribeEvent
    public void onChatPacket(S02ChatReceivedEvent event) {
        String message = event.getUnformattedMessage().toLowerCase();

        if (!partyCommands.getValue()) return;

        String prefix = partyCommandsPrefix.getValue().toLowerCase();
        if (!message.startsWith("party >")) {
            return;
        }

        int bracketIndex = message.indexOf(">");
        int colonIndex = message.indexOf(":");
        if (colonIndex == -1) return;
        if (bracketIndex == -1) return;

        String ign = ChatUtils.stripRank(message.substring(bracketIndex + 1, colonIndex).trim());
        String partyMessage = message.substring(colonIndex + 1).trim();

        if (PartyCommandListUtils.blacklist.contains(ign)) return;

        if (!partyMessage.startsWith(prefix)) return;
        partyMessage = partyMessage.substring(prefix.length());

        String[] args = partyMessage.split(" ");

        switch (args[0].toLowerCase()) {
            case "help":
                if (partyHelpCommand.getValue()) {
                    delayed = true;
                    if (args.length == 1) {
                        ChatUtils.partyChat("MeowMayo Party Commands Help Menu - Current Prefix: " + prefix);
                        scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help <general|catacombs|kuudra|fun>"), 1000);
                        scheduleReset(2000);
                    } else {
                        switch (args[1]) {
                            case "general":
                                if (args.length == 2) {
                                    ChatUtils.partyChat("MeowMayo Party Commands | General:");
                                    scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help general (Command) for more info"), 1000);
                                    scheduleTask(() -> ChatUtils.partyChat("Available Commands: AllInvite | Warp | Invite | PT | PTME | Coordinates"), 2000);
                                    scheduleReset(3000);
                                } else {
                                    switch (args[2]) {
                                        case "allinv":
                                        case "allinvite":
                                            ChatUtils.partyChat("AllInvite: Toggles All Invite");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "AllInvite"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: AllInvite | AllInv"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        case "warp":
                                        case "pwarp":
                                        case "partywarp":
                                            ChatUtils.partyChat("Warp: Warps the party");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "Warp"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: Warp | W | PWarp | PartyWarp"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        case "invite":
                                        case "party":
                                            ChatUtils.partyChat("Invite: Invites a player to the party");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "Invite (player)"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: Invite | Party"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        case "ptme":
                                            ChatUtils.partyChat("PTME: Transfers Party to the user");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "PTME"), 1000);
                                            if (partyTransferMeWhitelist.getValue()) {
                                                scheduleTask(() -> ChatUtils.partyChat("Whitelisted Users Only"), 2000);
                                                scheduleTask(() -> ChatUtils.partyChat("Aliases: PTME"), 3000);
                                                scheduleReset(4000);
                                                break;
                                            }
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: PTME"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        case "pt":
                                        case "partytransfer":
                                        case "ptransfer":
                                        case "transfer":
                                            ChatUtils.partyChat("PT: Transfers Party to the specified user");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "PT (player)"), 1000);
                                            if (partyTransferWhitelist.getValue()) {
                                                scheduleTask(() -> ChatUtils.partyChat("Whitelisted Users Only"), 2000);
                                                scheduleTask(() -> ChatUtils.partyChat("Aliases: PT | PartyTransfer | PTransfer | Transfer"), 3000);
                                                scheduleReset(4000);
                                                break;
                                            }
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: PT | PartyTransfer | PTransfer | Transfer"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        case "coords":
                                        case "coordinates":
                                            ChatUtils.partyChat("Coordinates: Sends player Coordinates");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "Coordinates"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: Coordinates | Coords"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        default:
                                            ChatUtils.partyChat("MeowMayo Party Commands | General:");
                                            scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help general (Command) for more info"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Available Commands: AllInvite | Warp | Invite | PT | PTME | Coordinates"), 2000);
                                            scheduleReset(3000);
                                            break;
                                    }
                                }
                                break;
                            case "catacombs":
                                if (args.length == 2) {
                                    ChatUtils.partyChat("MeowMayo Party Commands | Catacombs:");
                                    scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help catacombs (Command) for more info"), 1000);
                                    scheduleTask(() -> ChatUtils.partyChat("Available Commands: Floor | Master"), 2000);
                                    scheduleReset(3000);
                                } else {
                                    switch (args[2]) {
                                        case "floor":
                                        case "f":
                                            ChatUtils.partyChat("Floor: Enters the given normal mode dungeon floor");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "Floor(Catacombs Floor)"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: Floor | F"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        case "master":
                                        case "m":
                                            ChatUtils.partyChat("Master: Enters the given master mode dungeon floor");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "Master(Master Catacombs Floor)"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: Master | M"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        default:
                                            ChatUtils.partyChat("MeowMayo Party Commands | Catacombs:");
                                            scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help catacombs (Command) for more info"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Available Commands: Floor | Master"), 2000);
                                            scheduleReset(3000);
                                            break;
                                    }
                                }
                                break;
                            case "kuudra":
                                if (args.length == 2) {
                                    ChatUtils.partyChat("MeowMayo Party Commands | Kuudra:");
                                    scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help kuudra (Command) for more info"), 1000);
                                    scheduleTask(() -> ChatUtils.partyChat("Available Commands: tier"), 2000);
                                    scheduleReset(3000);
                                } else {
                                    switch (args[2]) {
                                        case "tier":
                                        case "t":
                                            ChatUtils.partyChat("Tier: Enters the given kuudra tier");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "Tier(Kuudra Tier)"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: Tier | T"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        default:
                                            ChatUtils.partyChat("MeowMayo Party Commands | Kuudra:");
                                            scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help kuudra (Command) for more info"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Available Commands: tier"), 2000);
                                            scheduleReset(3000);
                                            break;
                                    }
                                }
                                break;
                            case "fun":
                                if (args.length == 2) {
                                    ChatUtils.partyChat("MeowMayo Party Commands | Fun:");
                                    scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help fun (Command) for more info"), 1000);
                                    scheduleTask(() -> ChatUtils.partyChat("Available Commands: CoinFlip | Dice"), 2000);
                                    scheduleReset(3000);
                                } else {
                                    switch (args[2]) {
                                        case "cf":
                                        case "coinflip":
                                            ChatUtils.partyChat("CoinFlip: Flips a Coin");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "CoinFlip"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: CoinFlip | CF"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        case "dice":
                                        case "d":
                                        case "roll":
                                            ChatUtils.partyChat("Dice: Rolls a Dice");
                                            scheduleTask(() -> ChatUtils.partyChat("Usage: " + prefix + "Dice (Sides)"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Aliases: Dice | D | Roll"), 2000);
                                            scheduleReset(3000);
                                            break;
                                        default:
                                            ChatUtils.partyChat("MeowMayo Party Commands | Fun:");
                                            scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help fun (Command) for more info"), 1000);
                                            scheduleTask(() -> ChatUtils.partyChat("Available Commands: CoinFlip | Dice"), 2000);
                                            scheduleReset(3000);
                                            break;
                                    }
                                }
                                break;
                            default:
                                ChatUtils.partyChat("MeowMayo Party Commands Help Menu - Current Prefix: " + prefix);
                                scheduleTask(() -> ChatUtils.partyChat("Use " + prefix + "help <general|catacombs|kuudra|fun>"), 1000);
                                scheduleReset(2000);
                                break;
                        }
                    }
                }
                break;
            case "allinv":
            case "allinvite":
                if (allInviteCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("p settings allinvite");
                    scheduleReset(1000);
                }
                break;
            case "warp":
            case "w":
            case "pwarp":
            case "partywarp":
                if (warpCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("p warp");
                    scheduleReset(1000);
                }
                break;
            case "invite":
            case "party":
                if (inviteCommand.getValue()) {
                    delayed = true;
                    if (args.length == 1) {
                        ChatUtils.partyChat("Please input a player to invite");
                    } else {
                        ChatUtils.command("p " + args[1]);
                    }
                    scheduleReset(1000);
                }
                break;
            case "dt":
            case "downtime": // enabled by default (assuming party commands is on!)
                PartyUtils.requestDowntime();
                break;
            case "ptme":
                if (partyTransferMeCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    if (partyTransferMeWhitelist.getValue()) {
                        if (PartyCommandListUtils.whitelist.contains(ign)) {
                            ChatUtils.command("p transfer " + ign);
                        }
                    } else {
                        ChatUtils.command("p transfer " + ign);
                    }
                    scheduleReset(1000);
                }
                break;
            case "pt":
            case "partytransfer":
            case "ptransfer":
            case "transfer":
                if (partyTransferCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    if (partyTransferWhitelist.getValue()) {
                        if (PartyCommandListUtils.whitelist.contains(ign)) {
                            if (args.length == 1) {
                                ChatUtils.partyChat("Please input a player to transfer to");
                            }
                            ChatUtils.command("p transfer " + args[1]);
                        }
                    } else {
                        if (args.length == 1) {
                            ChatUtils.partyChat("Please input a player to transfer to");
                        }
                        ChatUtils.command("p transfer " + args[1]);
                    }
                    scheduleReset(1000);
                }
                break;
            case "coords":
            case "coordinates":
                if (coordinatesCommand.getValue()) {
                    delayed = true;
                    EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
                    ChatUtils.partyChat("X: " + ((int) player.posX) + ", Y: " + ((int) player.posY) + ", Z: " + ((int) player.posZ));
                    scheduleReset(1000);
                }
                break;
            case "ent":
            case "entrance":
                if (catacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon catacombs_entrance");
                    scheduleReset(1000);
                }
                break;
            case "floor1":
            case "f1":
                if (catacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon catacombs_floor_one");
                    scheduleReset(1000);
                }
                break;
            case "floor2":
            case "f2":
                if (catacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon catacombs_floor_two");
                    scheduleReset(1000);
                }
                break;
            case "floor3":
            case "f3":
                if (catacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon catacombs_floor_three");
                    scheduleReset(1000);
                }
                break;
            case "floor4":
            case "f4":
                if (catacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon catacombs_floor_four");
                    scheduleReset(1000);
                }
                break;
            case "floor5":
            case "f5":
                if (catacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon catacombs_floor_five");
                    scheduleReset(1000);
                }
                break;
            case "floor6":
            case "f6":
                if (catacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon catacombs_floor_six");
                    scheduleReset(1000);
                }
                break;
            case "floor7":
            case "f7":
                if (catacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon catacombs_floor_seven");
                    scheduleReset(1000);
                }
                break;
            case "floor":
            case "f":
                if (catacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    if (args.length == 2) {
                        switch (args[1]) {
                            case "1":
                                ChatUtils.command("joindungeon catacombs_floor_one");
                                break;
                            case "2":
                                ChatUtils.command("joindungeon catacombs_floor_two");
                                break;
                            case "3":
                                ChatUtils.command("joindungeon catacombs_floor_three");
                                break;
                            case "4":
                                ChatUtils.command("joindungeon catacombs_floor_four");
                                break;
                            case "5":
                                ChatUtils.command("joindungeon catacombs_floor_five");
                                break;
                            case "6":
                                ChatUtils.command("joindungeon catacombs_floor_six");
                                break;
                            case "7":
                                ChatUtils.command("joindungeon catacombs_floor_seven");
                                break;
                        }
                    }
                    scheduleReset(1000);
                }
                break;
            case "master1":
            case "m1":
                if (masterCatacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon master_catacombs_floor_one");
                    scheduleReset(1000);
                }
                break;
            case "master2":
            case "m2":
                if (masterCatacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon master_catacombs_floor_two");
                    scheduleReset(1000);
                }
                break;
            case "master3":
            case "m3":
                if (masterCatacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon master_catacombs_floor_three");
                    scheduleReset(1000);
                }
                break;
            case "master4":
            case "m4":
                if (masterCatacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon master_catacombs_floor_four");
                    scheduleReset(1000);
                }
                break;
            case "master5":
            case "m5":
                if (masterCatacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon master_catacombs_floor_five");
                    scheduleReset(1000);
                }
                break;
            case "master6":
            case "m6":
                if (masterCatacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon master_catacombs_floor_six");
                    scheduleReset(1000);
                }
                break;
            case "master7":
            case "m7":
                if (masterCatacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon master_catacombs_floor_seven");
                    scheduleReset(1000);
                }
                break;
            case "master":
            case "m":
                if (masterCatacombsEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    if (args.length == 2) {
                        switch (args[1]) {
                            case "1":
                                ChatUtils.command("joindungeon master_catacombs_floor_one");
                                break;
                            case "2":
                                ChatUtils.command("joindungeon master_catacombs_floor_two");
                                break;
                            case "3":
                                ChatUtils.command("joindungeon master_catacombs_floor_three");
                                break;
                            case "4":
                                ChatUtils.command("joindungeon master_catacombs_floor_four");
                                break;
                            case "5":
                                ChatUtils.command("joindungeon master_catacombs_floor_five");
                                break;
                            case "6":
                                ChatUtils.command("joindungeon master_catacombs_floor_six");
                                break;
                            case "7":
                                ChatUtils.command("joindungeon master_catacombs_floor_seven");
                                break;
                        }
                    }
                    scheduleReset(1000);
                }
                break;
            case "dungeontimestats":
            case "dts":
            case "dtstats":
                if (dungeonTimeStatsCommand.getValue()) {
                    delayed = true;

                    if (!dungeonTrack.getValue()) {
                        ChatUtils.partyChat("I am currently not tracking run data!");
                        scheduleReset(1000);
                        return;
                    }

                    int totalRuns, totalComps;
                    double totalTime;

                    switch (DungeonTracker.getTier()) {
                        case "F1":
                            totalRuns = DungeonStats.sessionF1Stats.totalRuns;
                            totalComps = DungeonStats.sessionF1Stats.totalComps;
                            totalTime = DungeonStats.sessionF1Stats.totalTime;

                            if (totalRuns == 0) {
                                ChatUtils.partyChat("I have no available run data for this session!");
                                scheduleReset(1000);
                                return;
                            }

                            ChatUtils.partyChat("MeowMayo > F1 Session Stats:");
                            scheduleTask(() -> ChatUtils.partyChat(" | Runs Tracked: " + totalRuns), 1000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Average Run Time: " + ChatUtils.formatTime(Math.round(totalTime / totalComps))), 2000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Total Run Time: " + ChatUtils.formatTime(totalTime)), 3000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Fastest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionF1Stats.fastest)), 4000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Slowest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionF1Stats.slowest)), 5000);
                            scheduleReset(6000);
                            break;
                        case "F2":
                            totalRuns = DungeonStats.sessionF2Stats.totalRuns;
                            totalComps = DungeonStats.sessionF2Stats.totalComps;
                            totalTime = DungeonStats.sessionF2Stats.totalTime;

                            if (totalRuns == 0) {
                                ChatUtils.partyChat("I have no available run data for this session!");
                                scheduleReset(1000);
                                return;
                            }

                            ChatUtils.partyChat("MeowMayo > F2 Session Stats:");
                            scheduleTask(() -> ChatUtils.partyChat(" | Runs Tracked: " + totalRuns), 1000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Average Run Time: " + ChatUtils.formatTime(Math.round(totalTime / totalComps))), 2000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Total Run Time: " + ChatUtils.formatTime(totalTime)), 3000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Fastest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionF2Stats.fastest)), 4000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Slowest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionF2Stats.slowest)), 5000);
                            scheduleReset(6000);
                            break;
                        case "F3":
                            totalRuns = DungeonStats.sessionF3Stats.totalRuns;
                            totalComps = DungeonStats.sessionF3Stats.totalComps;
                            totalTime = DungeonStats.sessionF3Stats.totalTime;

                            if (totalRuns == 0) {
                                ChatUtils.partyChat("I have no available run data for this session!");
                                scheduleReset(1000);
                                return;
                            }

                            ChatUtils.partyChat("MeowMayo > F3 Session Stats:");
                            scheduleTask(() -> ChatUtils.partyChat(" | Runs Tracked: " + totalRuns), 1000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Average Run Time: " + ChatUtils.formatTime(Math.round(totalTime / totalComps))), 2000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Total Run Time: " + ChatUtils.formatTime(totalTime)), 3000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Fastest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionF3Stats.fastest)), 4000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Slowest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionF3Stats.slowest)), 5000);
                            scheduleReset(6000);
                            break;
                        case "F4":
                            totalRuns = DungeonStats.sessionF4Stats.totalRuns;
                            totalComps = DungeonStats.sessionF4Stats.totalComps;
                            totalTime = DungeonStats.sessionF4Stats.totalTime;

                            if (totalRuns == 0) {
                                ChatUtils.partyChat("I have no available run data for this session!");
                                scheduleReset(1000);
                                return;
                            }

                            ChatUtils.partyChat("MeowMayo > F4 Session Stats:");
                            scheduleTask(() -> ChatUtils.partyChat(" | Runs Tracked: " + totalRuns), 1000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Average Run Time: " + ChatUtils.formatTime(Math.round(totalTime / totalComps))), 2000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Total Run Time: " + ChatUtils.formatTime(totalTime)), 3000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Fastest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionF4Stats.fastest)), 4000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Slowest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionF4Stats.slowest)), 5000);
                            scheduleReset(6000);
                            break;
                        case "F5":
                            totalRuns = DungeonStats.sessionF5Stats.totalRuns;
                            totalComps = DungeonStats.sessionF5Stats.totalComps;
                            totalTime = DungeonStats.sessionF5Stats.totalTime;

                            if (totalRuns == 0) {
                                ChatUtils.partyChat("I have no available run data for this session!");
                                scheduleReset(1000);
                                return;
                            }

                            ChatUtils.partyChat("MeowMayo > F5 Session Stats:");
                            scheduleTask(() -> ChatUtils.partyChat(" | Runs Tracked: " + totalRuns), 1000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Average Run Time: " + ChatUtils.formatTime(Math.round(totalTime / totalComps))), 2000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Total Run Time: " + ChatUtils.formatTime(totalTime)), 3000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Fastest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionF5Stats.fastest)), 4000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Slowest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionF5Stats.slowest)), 5000);
                            scheduleReset(6000);
                            break;
                        case "F6":
                            totalRuns = DungeonStats.sessionF6Stats.totalRuns;
                            totalComps = DungeonStats.sessionF6Stats.totalComps;
                            totalTime = DungeonStats.sessionF6Stats.totalTime;

                            if (totalRuns == 0) {
                                ChatUtils.partyChat("I have no available run data for this session!");
                                scheduleReset(1000);
                                return;
                            }

                            ChatUtils.partyChat("MeowMayo > F6 Session Stats:");
                            scheduleTask(() -> ChatUtils.partyChat(" | Runs Tracked: " + totalRuns), 1000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Average Run Time: " + ChatUtils.formatTime(Math.round(totalTime / totalComps))), 2000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Total Run Time: " + ChatUtils.formatTime(totalTime)), 3000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Fastest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionF6Stats.fastest)), 4000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Slowest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionF6Stats.slowest)), 5000);
                            scheduleReset(6000);
                            break;
                        case "F7":
                            totalRuns = DungeonStats.sessionF7Stats.totalRuns;
                            totalComps = DungeonStats.sessionF7Stats.totalComps;
                            totalTime = DungeonStats.sessionF7Stats.totalTime;

                            if (totalRuns == 0) {
                                ChatUtils.partyChat("I have no available run data for this session!");
                                scheduleReset(1000);
                                return;
                            }

                            ChatUtils.partyChat("MeowMayo > F7 Session Stats:");
                            scheduleTask(() -> ChatUtils.partyChat(" | Runs Tracked: " + totalRuns), 1000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Average Run Time: " + ChatUtils.formatTime(Math.round(totalTime / totalComps))), 2000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Total Run Time: " + ChatUtils.formatTime(totalTime)), 3000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Fastest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionF7Stats.fastest)), 4000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Slowest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionF7Stats.slowest)), 5000);
                            scheduleReset(6000);
                            break;
                        case "M1":
                            totalRuns = DungeonStats.sessionM1Stats.totalRuns;
                            totalComps = DungeonStats.sessionM1Stats.totalComps;
                            totalTime = DungeonStats.sessionM1Stats.totalTime;

                            if (totalRuns == 0) {
                                ChatUtils.partyChat("I have no available run data for this session!");
                                scheduleReset(1000);
                                return;
                            }

                            ChatUtils.partyChat("MeowMayo > M1 Session Stats:");
                            scheduleTask(() -> ChatUtils.partyChat(" | Runs Tracked: " + totalRuns), 1000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Average Run Time: " + ChatUtils.formatTime(Math.round(totalTime / totalComps))), 2000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Total Run Time: " + ChatUtils.formatTime(totalTime)), 3000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Fastest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionM1Stats.fastest)), 4000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Slowest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionM1Stats.slowest)), 5000);
                            scheduleReset(6000);
                            break;
                        case "M2":
                            totalRuns = DungeonStats.sessionM2Stats.totalRuns;
                            totalComps = DungeonStats.sessionM2Stats.totalComps;
                            totalTime = DungeonStats.sessionM2Stats.totalTime;

                            if (totalRuns == 0) {
                                ChatUtils.partyChat("I have no available run data for this session!");
                                scheduleReset(1000);
                                return;
                            }

                            ChatUtils.partyChat("MeowMayo > M2 Session Stats:");
                            scheduleTask(() -> ChatUtils.partyChat(" | Runs Tracked: " + totalRuns), 1000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Average Run Time: " + ChatUtils.formatTime(Math.round(totalTime / totalComps))), 2000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Total Run Time: " + ChatUtils.formatTime(totalTime)), 3000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Fastest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionM2Stats.fastest)), 4000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Slowest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionM2Stats.slowest)), 5000);
                            scheduleReset(6000);
                            break;
                        case "M3":
                            totalRuns = DungeonStats.sessionM3Stats.totalRuns;
                            totalComps = DungeonStats.sessionM3Stats.totalComps;
                            totalTime = DungeonStats.sessionM3Stats.totalTime;

                            if (totalRuns == 0) {
                                ChatUtils.partyChat("I have no available run data for this session!");
                                scheduleReset(1000);
                                return;
                            }

                            ChatUtils.partyChat("MeowMayo > M3 Session Stats:");
                            scheduleTask(() -> ChatUtils.partyChat(" | Runs Tracked: " + totalRuns), 1000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Average Run Time: " + ChatUtils.formatTime(Math.round(totalTime / totalComps))), 2000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Total Run Time: " + ChatUtils.formatTime(totalTime)), 3000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Fastest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionM3Stats.fastest)), 4000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Slowest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionM3Stats.slowest)), 5000);
                            scheduleReset(6000);
                            break;
                        case "M4":
                            totalRuns = DungeonStats.sessionM4Stats.totalRuns;
                            totalComps = DungeonStats.sessionM4Stats.totalComps;
                            totalTime = DungeonStats.sessionM4Stats.totalTime;

                            if (totalRuns == 0) {
                                ChatUtils.partyChat("I have no available run data for this session!");
                                scheduleReset(1000);
                                return;
                            }

                            ChatUtils.partyChat("MeowMayo > M4 Session Stats:");
                            scheduleTask(() -> ChatUtils.partyChat(" | Runs Tracked: " + totalRuns), 1000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Average Run Time: " + ChatUtils.formatTime(Math.round(totalTime / totalComps))), 2000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Total Run Time: " + ChatUtils.formatTime(totalTime)), 3000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Fastest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionM4Stats.fastest)), 4000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Slowest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionM4Stats.slowest)), 5000);
                            scheduleReset(6000);
                            break;
                        case "M5":
                            totalRuns = DungeonStats.sessionM5Stats.totalRuns;
                            totalComps = DungeonStats.sessionM5Stats.totalComps;
                            totalTime = DungeonStats.sessionM5Stats.totalTime;

                            if (totalRuns == 0) {
                                ChatUtils.partyChat("I have no available run data for this session!");
                                scheduleReset(1000);
                                return;
                            }

                            ChatUtils.partyChat("MeowMayo > M5 Session Stats:");
                            scheduleTask(() -> ChatUtils.partyChat(" | Runs Tracked: " + totalRuns), 1000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Average Run Time: " + ChatUtils.formatTime(Math.round(totalTime / totalComps))), 2000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Total Run Time: " + ChatUtils.formatTime(totalTime)), 3000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Fastest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionM5Stats.fastest)), 4000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Slowest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionM5Stats.slowest)), 5000);
                            scheduleReset(6000);
                            break;
                        case "M6":
                            totalRuns = DungeonStats.sessionM6Stats.totalRuns;
                            totalComps = DungeonStats.sessionM6Stats.totalComps;
                            totalTime = DungeonStats.sessionM6Stats.totalTime;

                            if (totalRuns == 0) {
                                ChatUtils.partyChat("I have no available run data for this session!");
                                scheduleReset(1000);
                                return;
                            }

                            ChatUtils.partyChat("MeowMayo > M6 Session Stats:");
                            scheduleTask(() -> ChatUtils.partyChat(" | Runs Tracked: " + totalRuns), 1000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Average Run Time: " + ChatUtils.formatTime(Math.round(totalTime / totalComps))), 2000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Total Run Time: " + ChatUtils.formatTime(totalTime)), 3000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Fastest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionM6Stats.fastest)), 4000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Slowest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionM6Stats.slowest)), 5000);
                            scheduleReset(6000);
                            break;
                        case "M7":
                            totalRuns = DungeonStats.sessionM7Stats.totalRuns;
                            totalComps = DungeonStats.sessionM7Stats.totalComps;
                            totalTime = DungeonStats.sessionM7Stats.totalTime;

                            if (totalRuns == 0) {
                                ChatUtils.partyChat("I have no available run data for this session!");
                                scheduleReset(1000);
                                return;
                            }

                            ChatUtils.partyChat("MeowMayo > M7 Session Stats:");
                            scheduleTask(() -> ChatUtils.partyChat(" | Runs Tracked: " + totalRuns), 1000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Average Run Time: " + ChatUtils.formatTime(Math.round(totalTime / totalComps))), 2000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Total Run Time: " + ChatUtils.formatTime(totalTime)), 3000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Fastest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionM7Stats.fastest)), 4000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Slowest Run Time: " + ChatUtils.formatTime(DungeonStats.sessionM7Stats.slowest)), 5000);
                            scheduleReset(6000);
                            break;
                    }
                }
                break;
            case "basic":
            case "t1":
            case "tier1":
                if (kuudraEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon kuudra_basic");
                    scheduleReset(1000);
                }
                break;

            case "hot":
            case "t2":
            case "tier2":
                if (kuudraEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon kuudra_hot");
                    scheduleReset(1000);
                }
                break;

            case "burning":
            case "t3":
            case "tier3":
                if (kuudraEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon kuudra_burning");
                    scheduleReset(1000);
                }
                break;

            case "fiery":
            case "t4":
            case "tier4":
                if (kuudraEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon kuudra_fiery");
                    scheduleReset(1000);
                }
                break;

            case "infernal":
            case "t5":
            case "tier5":
                if (kuudraEntranceCommand.getValue() && PartyUtils.isLeader()) {
                    delayed = true;
                    ChatUtils.command("joindungeon kuudra_infernal");
                    scheduleReset(1000);
                }
                break;

            case "mm":
            case "meowmayo":
                if (selloutCommand.getValue()) {
                    delayed = true;
                    ChatUtils.partyChat("MeowMayo is a Quality of life mod that offers a ton of quality of life kuudra and dungeons features!");
                    scheduleTask(() -> ChatUtils.partyChat("Download MeowMayo here -> discord.gg/TBtp9rVHhM"), 1000);
                    scheduleReset(2000);
                }
                break;
            case "kuudratimestats":
            case "kts":
            case "ktstats":
                if (kuudraTimeStatsCommand.getValue()) {
                    delayed = true;

                    if (!kuudraTrack.getValue()) {
                        ChatUtils.partyChat("I am currently not tracking run data!");
                        scheduleReset(1000);
                        return;
                    }

                    int totalRuns, totalComps;
                    double totalTime;

                    switch (KuudraTracker.getTier()) {
                        case "Basic":
                            totalRuns = KuudraStats.sessionBasicStats.totalRuns;
                            totalComps = KuudraStats.sessionBasicStats.totalComps;
                            totalTime = KuudraStats.sessionBasicStats.totalTime;

                            if (totalRuns == 0) {
                                ChatUtils.partyChat("I have no available run data for this session!");
                                scheduleReset(1000);
                                return;
                            }

                            ChatUtils.partyChat("MeowMayo > Basic Session Stats:");
                            scheduleTask(() -> ChatUtils.partyChat(" | Runs Tracked: " + totalRuns), 1000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Average Run Time: " + ChatUtils.formatTime(Math.round(totalTime / totalComps))), 2000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Total Run Time: " + ChatUtils.formatTime(totalTime)), 3000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Fastest Run Time: " + ChatUtils.formatTime(KuudraStats.sessionBasicStats.fastest)), 4000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Slowest Run Time: " + ChatUtils.formatTime(KuudraStats.sessionBasicStats.slowest)), 5000);
                            scheduleReset(6000);
                            break;
                        case "Hot":
                            totalRuns = KuudraStats.sessionHotStats.totalRuns;
                            totalComps = KuudraStats.sessionHotStats.totalComps;
                            totalTime = KuudraStats.sessionHotStats.totalTime;

                            if (totalRuns == 0) {
                                ChatUtils.partyChat("I have no available run data for this session!");
                                scheduleReset(1000);
                                return;
                            }

                            ChatUtils.partyChat("MeowMayo > Hot Session Stats:");
                            scheduleTask(() -> ChatUtils.partyChat(" | Runs Tracked: " + totalRuns), 1000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Average Run Time: " + ChatUtils.formatTime(Math.round(totalTime / totalComps))), 2000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Total Run Time: " + ChatUtils.formatTime(totalTime)), 3000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Fastest Run Time: " + ChatUtils.formatTime(KuudraStats.sessionHotStats.fastest)), 4000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Slowest Run Time: " + ChatUtils.formatTime(KuudraStats.sessionHotStats.slowest)), 5000);
                            scheduleReset(6000);
                            break;
                        case "Burning":
                            totalRuns = KuudraStats.sessionBurningStats.totalRuns;
                            totalComps = KuudraStats.sessionBurningStats.totalComps;
                            totalTime = KuudraStats.sessionBurningStats.totalTime;

                            if (totalRuns == 0) {
                                ChatUtils.partyChat("I have no available run data for this session!");
                                scheduleReset(1000);
                                return;
                            }

                            ChatUtils.partyChat("MeowMayo > Burning Session Stats:");
                            scheduleTask(() -> ChatUtils.partyChat(" | Runs Tracked: " + totalRuns), 1000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Average Run Time: " + ChatUtils.formatTime(Math.round(totalTime / totalComps))), 2000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Total Run Time: " + ChatUtils.formatTime(totalTime)), 3000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Fastest Run Time: " + ChatUtils.formatTime(KuudraStats.sessionBurningStats.fastest)), 4000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Slowest Run Time: " + ChatUtils.formatTime(KuudraStats.sessionBurningStats.slowest)), 5000);
                            scheduleReset(6000);
                            break;
                        case "Fiery":
                            totalRuns = KuudraStats.sessionFieryStats.totalRuns;
                            totalComps = KuudraStats.sessionFieryStats.totalComps;
                            totalTime = KuudraStats.sessionFieryStats.totalTime;

                            if (totalRuns == 0) {
                                ChatUtils.partyChat("I have no available run data for this session!");
                                scheduleReset(1000);
                                return;
                            }

                            ChatUtils.partyChat("MeowMayo > Fiery Session Stats:");
                            scheduleTask(() -> ChatUtils.partyChat(" | Runs Tracked: " + totalRuns), 1000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Average Run Time: " + ChatUtils.formatTime(Math.round(totalTime / totalComps))), 2000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Total Run Time: " + ChatUtils.formatTime(totalTime)), 3000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Fastest Run Time: " + ChatUtils.formatTime(KuudraStats.sessionFieryStats.fastest)), 4000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Slowest Run Time: " + ChatUtils.formatTime(KuudraStats.sessionFieryStats.slowest)), 5000);
                            scheduleReset(6000);
                            break;
                        case "Infernal":
                            totalRuns = KuudraStats.sessionInfernalStats.totalRuns;
                            totalComps = KuudraStats.sessionInfernalStats.totalComps;
                            totalTime = KuudraStats.sessionInfernalStats.totalTime;

                            if (totalRuns == 0) {
                                ChatUtils.partyChat("I have no available run data for this session!");
                                scheduleReset(1000);
                                return;
                            }

                            ChatUtils.partyChat("MeowMayo > Infernal Session Stats:");
                            scheduleTask(() -> ChatUtils.partyChat(" | Runs Tracked: " + totalRuns), 1000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Average Run Time: " + ChatUtils.formatTime(Math.round(totalTime / totalComps))), 2000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Total Run Time: " + ChatUtils.formatTime(totalTime)), 3000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Fastest Run Time: " + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.fastest)), 4000);
                            scheduleTask(() -> ChatUtils.partyChat(" | Slowest Run Time: " + ChatUtils.formatTime(KuudraStats.sessionInfernalStats.slowest)), 5000);
                            scheduleReset(6000);
                            break;
                    }
                }
                break;
            case "cf":
            case "coinflip":
                if (coinflipCommand.getValue()) {
                    delayed = true;
                    if (r.nextBoolean()) {
                        ChatUtils.partyChat("Heads!");
                    } else {
                        ChatUtils.partyChat("Tails!");
                    }
                    scheduleReset(1000);
                }
                break;
            case "dice":
            case "d":
            case "roll":
                if (diceCommand.getValue()) {
                    delayed = true;
                    if (args.length == 1) {
                        ChatUtils.partyChat("Please input how many sides the dice should have");
                    } else {
                        try {
                            int side = Integer.parseInt(args[1]);
                            ChatUtils.partyChat("You rolled a " + (r.nextInt(side) + 1) + "!");
                        } catch (NumberFormatException e) {
                            ChatUtils.partyChat("Please input a valid number of sides");
                        }
                    }
                    scheduleReset(1000);
                }
                break;
        }
    }

    private void scheduleReset(long delayMs) {
        scheduleTask(() -> delayed = false, delayMs);
    }
}
