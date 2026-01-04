package com.mmdev.meowmayo.utils;

import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

import java.util.*;
import java.util.regex.*;

public class PartyUtils {
    private static boolean kuudraChange = true;
    private static boolean dungeonChange = true;

    private static boolean downtime = false;

    private static boolean inParty = false;
    private static String leader = "";
    private static final Set<String> party = new HashSet<>();

    private static boolean leaveFlag = false;

    private static final String playerName = Minecraft.getMinecraft().getSession().getUsername();

    // ===== Getters =====
    public static boolean getKuudraFlag() { return kuudraChange; }
    public static boolean getDungeonFlag() { return dungeonChange; }

    public static void useKuudraFlag() { kuudraChange = false; }
    public static void useDungeonFlag() { dungeonChange = false; }

    public static void requestDowntime() { downtime = true; }
    public static void useDowntimeFlag() { downtime = false; }
    public static boolean getDowntimeFlag() { return downtime; }

    public static boolean isLeader() { return leader.equals(playerName); }
    public static boolean isInParty() { return inParty; }
    public static Set<String> getParty() { return party; }

    // ===== Regex Patterns =====
    private static final Pattern disbandPattern = Pattern.compile("^(.+) has disbanded the party!$");
    private static final Pattern removedPattern = Pattern.compile("^(.+) has been removed from the party\\.$");
    private static final Pattern memberLeftPattern = Pattern.compile("^(.+) has left the party\\.$");
    private static final Pattern transferLeavePattern = Pattern.compile("^The party was transferred to (.+) because (.+) left$");
    private static final Pattern transferByPattern = Pattern.compile("^The party was transferred to (.+) by (.+)$");
    private static final Pattern joinedPattern = Pattern.compile("^(.+) joined the party\\.$");
    private static final Pattern pfinderDungeonPattern = Pattern.compile("^Party Finder > (.+) joined the dungeon group! \\(.+\\)$");
    private static final Pattern pfinderGroupPattern = Pattern.compile("^Party Finder > (.+) joined the group! \\(.+\\)$");
    private static final Pattern youJoinedPattern = Pattern.compile("^You have joined (.+)'s party!$");
    private static final Pattern plistPattern = Pattern.compile("^Party Members \\((.+)\\)$");
    private static final Pattern leaderPattern = Pattern.compile("^Party Leader: (.+) ●$");
    private static final Pattern notInPartyPattern = Pattern.compile("^You are not currently in a party\\.$");
    private static final Pattern membersPattern = Pattern.compile("^Party Members:(.+)● $");
    private static final Pattern moderatorsPattern = Pattern.compile("^Party Moderators:(.+)● $");

    private boolean expectingConnection = false;
    @SubscribeEvent
    public void onServerJoin(ClientConnectedToServerEvent event) {
        expectingConnection = true;
    }

    // makeshift
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        if (!expectingConnection) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.thePlayer == null) return;

        getPartyMembers();
        expectingConnection = false;
    }

    // ===== Chat Listener =====
    @SubscribeEvent
    public void onChatPacket(S02ChatReceivedEvent event) {
        String msg = event.getUnformattedMessage();

        // Disband
        if (disbandPattern.matcher(msg).matches() || msg.equals("The party was disbanded because all invites expired and the party was empty.")) {
            if(!ChatUtils.isSystemMessage(msg)) return;
            resetParty();
            return;
        }

        // Left party
        if (msg.equals("You left the party.")) {
            leaveFlag = true;
            resetParty();
            DelayUtils.scheduleTask(() -> {leaveFlag = false;}, 1000);
            return;
        }

        // Removed / Left member
        Matcher removed = removedPattern.matcher(msg);
        Matcher left = memberLeftPattern.matcher(msg);
        if (removed.matches() || left.matches()) {
            String player = removed.matches() ? removed.group(1) : left.group(1);
            party.remove(ChatUtils.stripRank(player));
            inParty = true;
            kuudraChange = dungeonChange = downtime = true;
            return;
        }

        // Transfer because left
        Matcher tLeave = transferLeavePattern.matcher(msg);
        if (tLeave.matches()) {
            if (!leaveFlag) {
                leader = ChatUtils.stripRank(tLeave.group(1));
                party.remove(ChatUtils.stripRank(tLeave.group(2)));
                inParty = true;
            }
            return;
        }

        // Transfer by
        Matcher tBy = transferByPattern.matcher(msg);
        if (tBy.matches()) {
            leader = ChatUtils.stripRank(tBy.group(1));
            inParty = true;
            return;
        }

        // Joined party
        Matcher joined = joinedPattern.matcher(msg);
        if (joined.matches()) {
            String user = ChatUtils.stripRank(joined.group(1));
            party.add(user);
            if (party.size() == 1 && !inParty) { // this should make you leader if someone joins your party and the party doesnt exist
                party.add(playerName);
                leader = playerName;
            }
            inParty = true;
            kuudraChange = dungeonChange = downtime = true;
            return;
        }

        // Party Finder (dungeon group)
        Matcher pfDungeon = pfinderDungeonPattern.matcher(msg);
        Matcher pfGroup = pfinderGroupPattern.matcher(msg);
        if (pfDungeon.matches() || pfGroup.matches()) {
            String user = ChatUtils.stripRank(pfDungeon.matches() ? pfDungeon.group(1) : pfGroup.group(1));
            if (user.equals(playerName)) {
                getPartyMembers();
                return;
            }
            party.add(user);
            inParty = true;
            kuudraChange = dungeonChange = downtime = true;
            return;
        }

        // Joined someone else’s party
        Matcher youJoined = youJoinedPattern.matcher(msg);
        if (youJoined.matches()) {
            DelayUtils.scheduleTask(PartyUtils::getPartyMembers, 500);
            return;
        }

        // Party list output
        Matcher plist = plistPattern.matcher(msg);
        if (plist.matches()) {
            party.clear();
            inParty = true;
            kuudraChange = dungeonChange = downtime = true;
            return;
        }

        Matcher lead = leaderPattern.matcher(msg);
        if (lead.matches()) {
            leader = ChatUtils.stripRank(lead.group(1));
            party.add(leader);
            inParty = true;
            return;
        }

        if (notInPartyPattern.matcher(msg).matches()) {
            resetParty();
            return;
        }

        Matcher members = membersPattern.matcher(msg);
        Matcher moderators = moderatorsPattern.matcher(msg);
        if (members.matches() || moderators.matches()) {
            String list = (members.matches() ? members.group(1) : moderators.group(1));
            String[] arr = list.split(" ● ");
            for (String m : arr) {
                String clean = ChatUtils.stripRank(m.trim());
                party.add(clean);
            }
        }
    }

    // ===== Helpers =====
    private static void resetParty() {
        inParty = false;
        leader = "";
        party.clear();
        kuudraChange = dungeonChange = downtime = true;
    }

    private static void getPartyMembers() {
        party.clear();
        ChatUtils.command("p list");
    }
}