package com.mmdev.meowmayo.features.dungeons.tracker;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.IntSliderSetting;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.features.dungeons.F5BossFeatures;
import com.mmdev.meowmayo.features.dungeons.F7BossFeatures;
import com.mmdev.meowmayo.features.dungeons.LeapExtras;
import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.DelayUtils;
import com.mmdev.meowmayo.utils.PartyUtils;
import com.mmdev.meowmayo.utils.ScoreboardUtils;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import com.mmdev.meowmayo.utils.tracker.Events;
import com.mmdev.meowmayo.utils.tracker.Tiers;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DungeonTracker {
    private ToggleSetting autoRequeue = (ToggleSetting) ConfigSettings.getSetting("Dungeon Auto Requeue");
    private IntSliderSetting autoRequeueDelay = (IntSliderSetting) ConfigSettings.getSetting("Dungeon Requeue Delay");

    private ToggleSetting highlightDoorOpener = (ToggleSetting) ConfigSettings.getSetting("Highlight Door Opener");

    private ToggleSetting dungeonTrack = (ToggleSetting) ConfigSettings.getSetting("Average Dungeon Run Time Tracker");
    private ToggleSetting resetOnParty = (ToggleSetting) ConfigSettings.getSetting("Reset Dungeon Tracker on Party Change");
    private ToggleSetting lagMessage = (ToggleSetting) ConfigSettings.getSetting("Dungeon Run Lag Timing");
    private ToggleSetting btt = (ToggleSetting) ConfigSettings.getSetting("Dungeon B.T.T. Message");

    private static Tiers currentTier = null;
    private static int currentPhase = -1;

    private DungeonTimer dt;
    private DungeonListener dl;

    private long startTime = 0;

    private List<Long> rt = new ArrayList<>();
    private List<Long> lt = new ArrayList<>();

    private boolean runPrimed = false;

    private boolean runActive = false;

    private boolean bossDead = false;

    public DungeonTracker(DungeonTimer dt, DungeonListener dl) {
        this.dt = dt;
        this.dl = dl;
    }

    public static String getTier() {
        return currentTier.getTierName();
    }

    public static int getPhase() {
        return currentPhase;
    }

    Pattern location = Pattern.compile("^⏣ The Catacombs \\((\\w\\d)\\)$");

    @SubscribeEvent
    public void onChat(S02ChatReceivedEvent event) {
        String msg = event.getUnformattedMessage();
        if (msg.equalsIgnoreCase((Minecraft.getMinecraft().thePlayer.getName() + " is now ready!"))) {
            if (!runPrimed) {
                Matcher matcher = location.matcher(ScoreboardUtils.getLocation());

                if (matcher.matches()) {
                    switch (matcher.group(1)) {
                        case "F2":
                            setCurrentTier(DungeonTiers.F2);
                            prepRun();
                            break;
                        case "F3":
                            setCurrentTier(DungeonTiers.F3);
                            prepRun();
                            break;
                        case "F5":
                            setCurrentTier(DungeonTiers.F5);
                            prepRun();
                            break;
                        case "F6":
                            setCurrentTier(DungeonTiers.F6);
                            prepRun();
                            break;
                        case "F7":
                            setCurrentTier(DungeonTiers.F7);
                            prepRun();
                            break;
                        case "M2":
                            setCurrentTier(DungeonTiers.M2);
                            prepRun();
                            break;
                        case "M3":
                            setCurrentTier(DungeonTiers.M3);
                            prepRun();
                            break;
                        case "M5":
                            setCurrentTier(DungeonTiers.M5);
                            prepRun();
                            break;
                        case "M6":
                            setCurrentTier(DungeonTiers.M6);
                            prepRun();
                            break;
                        case "M7":
                            setCurrentTier(DungeonTiers.M7);
                            prepRun();
                            break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        endRun();
    }

    public void startRun() {
        currentPhase = 0;

        int numPhases = currentTier.getTierCount();
        rt.clear();
        lt.clear();

        for (int i = 0; i < numPhases; i++) {
            rt.add(-1L);
            lt.add(0L);
        }

        dt.enable();

        startTime = System.currentTimeMillis();

        this.runActive = true;
        this.runPrimed = false;
    }

    public void endRun() {
        if (runActive || runPrimed) {
            currentTier.getPhases().get(currentPhase).exitPhase();
        }

        runActive = false;
        runPrimed = false;
        currentPhase = -1;
        startTime = 0;
        rt.clear();
        lt.clear();
    }

    public void setCurrentTier(Tiers tier) {
        if (currentTier == null) {
            currentTier = tier;
        }
        if (!tier.getTierName().equals(currentTier.getTierName())) {
            currentTier = tier;
        }
    }

    public void signal(Events event) {
        if (!runActive && !runPrimed) return;

        switch (event) {
            case DUNGEON_START:
                startRun();
                break;
            case BLOOD_OPEN:
                LeapExtras.clearHighlightedPlayer();
                advancePhase();
                if (dungeonTrack.getValue())
                    ChatUtils.system("Blood Rush Took: " + ChatUtils.formatTime((rt.get(0) - startTime) / 1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(0)) / 1000.0) + ") | Best Time: " + ChatUtils.formatTime(((rt.get(0) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                if (btt.getValue())
                    ChatUtils.partyChat("Best Theoretical Time: " + ChatUtils.formatTime(((rt.get(0) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                break;
            case BLOOD_DONE:
                advancePhase();
                if (dungeonTrack.getValue())
                    ChatUtils.system("Blood Camp Took: " + ChatUtils.formatTime((rt.get(1) - rt.get(0))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(1))/1000.0) + ") | Best Time: " + ChatUtils.formatTime(((rt.get(1) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                if (btt.getValue())
                    ChatUtils.partyChat("Best Theoretical Time: " + ChatUtils.formatTime(((rt.get(1) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                break;
            case BOSS_ENTER:
                advancePhase();
                if (dungeonTrack.getValue())
                    ChatUtils.system("Boss Enter Took: " + ChatUtils.formatTime((rt.get(2) - rt.get(1))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(2))/1000.0) + ") | Best Time: " + ChatUtils.formatTime(((rt.get(2) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                if (btt.getValue())
                    ChatUtils.partyChat("Best Theoretical Time: " + ChatUtils.formatTime(((rt.get(2) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                break;
            case SCARF_CRYPTS_DEAD:
                advancePhase();
                if (dungeonTrack.getValue())
                    ChatUtils.system("Scarf Undead Took: " + ChatUtils.formatTime((rt.get(3) - rt.get(2))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(3))/1000.0) + ") | Best Time: " + ChatUtils.formatTime(((rt.get(3) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                if (btt.getValue())
                    ChatUtils.partyChat("Best Theoretical Time: " + ChatUtils.formatTime(((rt.get(3) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                break;
            case GUARDIANS_DEAD:
                advancePhase();
                if (dungeonTrack.getValue())
                    ChatUtils.system("Guardians Took: " + ChatUtils.formatTime((rt.get(3) - rt.get(2))/1000.0) + " (Lag: " + ChatUtils.formatTime(((rt.get(3) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                if (btt.getValue())
                    ChatUtils.partyChat("Best Theoretical Time: " + ChatUtils.formatTime(((rt.get(3) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                break;
            case PROFESSOR_1_DEAD:
                advancePhase();
                if (dungeonTrack.getValue())
                    ChatUtils.system("Human Professor Took: " + ChatUtils.formatTime((rt.get(4) - rt.get(3))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(4))/1000.0) + ") | Best Time: " + ChatUtils.formatTime(((rt.get(4) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                if (btt.getValue())
                    ChatUtils.partyChat("Best Theoretical Time: " + ChatUtils.formatTime(((rt.get(4) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                break;
            case TERRACOTTAS_DEAD:
                advancePhase();
                if (dungeonTrack.getValue())
                    ChatUtils.system("Terracottas Took: " + ChatUtils.formatTime((rt.get(3) - rt.get(2))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(3))/1000.0) + ") | Best Time: " + ChatUtils.formatTime(((rt.get(3) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                if (btt.getValue())
                    ChatUtils.partyChat("Best Theoretical Time: " + ChatUtils.formatTime(((rt.get(3) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                break;
            case GIANTS_DEAD:
                advancePhase();
                if (dungeonTrack.getValue())
                    ChatUtils.system("Giants Took: " + ChatUtils.formatTime((rt.get(4) - rt.get(3))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(4))/1000.0) + ") | Best Time: " + ChatUtils.formatTime(((rt.get(4) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                if (btt.getValue())
                    ChatUtils.partyChat("Best Theoretical Time: " + ChatUtils.formatTime(((rt.get(4) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                break;
            case CRYSTAL_PLACED:
//                ChatUtils.system("The Beam is Charging Up!");
                break;
            case MAXOR_DEAD:
                advancePhase();
                if (dungeonTrack.getValue())
                    ChatUtils.system("Maxor Took: " + ChatUtils.formatTime((rt.get(3) - rt.get(2))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(3))/1000.0) + ") | Best Time: " + ChatUtils.formatTime(((rt.get(3) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                if (btt.getValue())
                    ChatUtils.partyChat("Best Theoretical Time: " + ChatUtils.formatTime(((rt.get(3) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                break;
            case STORM_LIGHTNING:
                break;
            case STORM_DEAD:
                F7BossFeatures.signal(Events.STORM_DEAD);
                advancePhase();
                if (dungeonTrack.getValue())
                    ChatUtils.system("Storm Took: " + ChatUtils.formatTime((rt.get(4) - rt.get(3))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(4))/1000.0) + ") | Best Time: " + ChatUtils.formatTime(((rt.get(4) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                if (btt.getValue())
                    ChatUtils.partyChat("Best Theoretical Time: " + ChatUtils.formatTime(((rt.get(4) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                break;
            case CORE_OPEN:
                F7BossFeatures.signal(Events.CORE_OPEN);
                advancePhase();
                if (dungeonTrack.getValue())
                    ChatUtils.system("Terminals Took: " + ChatUtils.formatTime((rt.get(5) - rt.get(4))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(5))/1000.0) + ") | Best Time: " + ChatUtils.formatTime(((rt.get(5) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                if (btt.getValue())
                    ChatUtils.partyChat("Best Theoretical Time: " + ChatUtils.formatTime(((rt.get(5) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                break;
            case GOLDOR_DEAD:
                F7BossFeatures.signal(Events.GOLDOR_DEAD);
                advancePhase();
                if (dungeonTrack.getValue())
                    ChatUtils.system("Goldor Kill Took: " + ChatUtils.formatTime((rt.get(6) - rt.get(5))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(6))/1000.0) + ") | Best Time: " + ChatUtils.formatTime(((rt.get(6) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                if (btt.getValue())
                    ChatUtils.partyChat("Best Theoretical Time: " + ChatUtils.formatTime(((rt.get(6) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                break;
            case NECRON_DEAD:
                if (currentTier.getTierName().equals("F7")) {
                    bossDead = true;
                } else {
                    advancePhase();
                    if (dungeonTrack.getValue())
                        ChatUtils.system("Necron Took: " + ChatUtils.formatTime((rt.get(7) - rt.get(6)) / 1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(7)) / 1000.0) + ") | Best Time: " + ChatUtils.formatTime(((rt.get(7) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                    if (btt.getValue())
                        ChatUtils.partyChat("Best Theoretical Time: " + ChatUtils.formatTime(((rt.get(7) - startTime) / 1000.0) + (DungeonStats.getRemaining(currentTier.getTierName(), currentPhase))));
                }
                break;
            case RELICS_DOWN:
                F7BossFeatures.signal(Events.RELICS_DOWN);
                break;
            case LIVID_DEAD:
                F5BossFeatures.signalEnd();
            case SCARF_DEAD:
            case PROFESSOR_DEAD:
            case SADAN_DEAD:
            case WITHER_KING_DEAD:
                bossDead = true;
                break;
        }
    }

    public void signal(Events event, Matcher matcher) {
        if (!runActive && !runPrimed) return;

        switch (event) {
            case WITHER_DOOR:
                if (dungeonTrack.getValue())
                    ChatUtils.system("A Wither Door has been opened!");
                if (highlightDoorOpener.getValue()) {
                    LeapExtras.setHighlightedPlayer(matcher.group(1));
                }
                break;
            case DUNGEON_END: // this can always proc!!
                if (bossDead) {
                    dt.split(rt, lt, currentPhase);

                    switch (currentTier.getTierName()) {
                        case "F2":
                            DungeonStats.sessionF2Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.globalF2Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.saveConfig("F2");

                            if (dungeonTrack.getValue()) {
                                long totalLag = 0;
                                for (long lag : lt) {
                                    totalLag += lag;
                                }

                                double runTime = ((System.currentTimeMillis() - startTime) / 1000.0);

                                double sumOfBest = 0.0;

                                for (double time : DungeonStats.globalF2Stats.bestSplits) {
                                    sumOfBest += time;
                                }

                                ChatUtils.system(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(runTime - (totalLag / 1000.0)));
                                ChatUtils.system("§fTotal Run Time: §a§l" + ChatUtils.formatTime(runTime) + " §r| " + ChatUtils.formatTime(runTime - sumOfBest) + " behind sum of best\n" +
                                        "§2||§r§f Blood Rush took §a§l" + ChatUtils.formatTime((rt.get(0) - startTime) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(0) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Blood Camp took §a§l" + ChatUtils.formatTime((rt.get(1) - rt.get(0)) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(1) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Portal took §a§l" + ChatUtils.formatTime((rt.get(2) - rt.get(1)) / 1000.0) + "§r§f to enter\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(2) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Scarf Undeads took §a§l" + ChatUtils.formatTime((rt.get(3) - rt.get(2)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(3) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Scarf took §a§l" + ChatUtils.formatTime((rt.get(4) - rt.get(3)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(4) / 1000.0) + "§r§f to lag\n"
                                );

                                if (lagMessage.getValue()) {
                                    ChatUtils.partyChat(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(((System.currentTimeMillis() - startTime) / 1000.0) - (totalLag / 1000.0)));
                                }
                            }
                            break;
                        case "F3":
                            DungeonStats.sessionF3Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.globalF3Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.saveConfig("F3");

                            if (dungeonTrack.getValue()) {
                                long totalLag = 0;
                                for (long lag : lt) {
                                    totalLag += lag;
                                }

                                double runTime = ((System.currentTimeMillis() - startTime) / 1000.0);

                                double sumOfBest = 0.0;

                                for (double time : DungeonStats.globalF3Stats.bestSplits) {
                                    sumOfBest += time;
                                }

                                ChatUtils.system(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(runTime - (totalLag / 1000.0)));
                                ChatUtils.system("§fTotal Run Time: §a§l" + ChatUtils.formatTime(runTime) + " §r| " + ChatUtils.formatTime(runTime - sumOfBest) + " behind sum of best\n" +
                                        "§2||§r§f Blood Rush took §a§l" + ChatUtils.formatTime((rt.get(0) - startTime) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(0) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Blood Camp took §a§l" + ChatUtils.formatTime((rt.get(1) - rt.get(0)) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(1) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Portal took §a§l" + ChatUtils.formatTime((rt.get(2) - rt.get(1)) / 1000.0) + "§r§f to enter\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(2) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Guardians took §a§l" + ChatUtils.formatTime((rt.get(3) - rt.get(2)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(3) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Human Professor took §a§l" + ChatUtils.formatTime((rt.get(4) - rt.get(3)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(4) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Guardian Professor took §a§l" + ChatUtils.formatTime((rt.get(5) - rt.get(4)) / 1000.0) + "§r§f to kill\n"
                                );

                                if (lagMessage.getValue()) {
                                    ChatUtils.partyChat(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(((System.currentTimeMillis() - startTime) / 1000.0) - (totalLag / 1000.0)));
                                }
                            }
                            break;
                        case "F5":
                            DungeonStats.sessionF5Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.globalF5Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.saveConfig("F5");

                            if (dungeonTrack.getValue()) {
                                long totalLag = 0;
                                for (long lag : lt) {
                                    totalLag += lag;
                                }

                                double runTime = ((System.currentTimeMillis() - startTime) / 1000.0);

                                double sumOfBest = 0.0;

                                for (double time : DungeonStats.globalF5Stats.bestSplits) {
                                    sumOfBest += time;
                                }

                                ChatUtils.system(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(runTime - (totalLag / 1000.0)));
                                ChatUtils.system("§fTotal Run Time: §a§l" + ChatUtils.formatTime(runTime) + " §r| " + ChatUtils.formatTime(runTime - sumOfBest) + " behind sum of best\n" +
                                        "§2||§r§f Blood Rush took §a§l" + ChatUtils.formatTime((rt.get(0) - startTime) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(0) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Blood Camp took §a§l" + ChatUtils.formatTime((rt.get(1) - rt.get(0)) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(1) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Portal took §a§l" + ChatUtils.formatTime((rt.get(2) - rt.get(1)) / 1000.0) + "§r§f to enter\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(2) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Livid took §a§l" + ChatUtils.formatTime((rt.get(3) - rt.get(2)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(3) / 1000.0) + "§r§f to lag\n"
                                );

                                if (lagMessage.getValue()) {
                                    ChatUtils.partyChat(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(((System.currentTimeMillis() - startTime) / 1000.0) - (totalLag / 1000.0)));
                                }
                            }
                            break;
                        case "F6":
                            DungeonStats.sessionF6Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.globalF6Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.saveConfig("F6");

                            if (dungeonTrack.getValue()) {
                                long totalLag = 0;
                                for (long lag : lt) {
                                    totalLag += lag;
                                }

                                double runTime = ((System.currentTimeMillis() - startTime) / 1000.0);

                                double sumOfBest = 0.0;

                                for (double time : DungeonStats.globalF6Stats.bestSplits) {
                                    sumOfBest += time;
                                }

                                ChatUtils.system(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(runTime - (totalLag / 1000.0)));
                                ChatUtils.system("§fTotal Run Time: §a§l" + ChatUtils.formatTime(runTime) + " §r| " + ChatUtils.formatTime(runTime - sumOfBest) + " behind sum of best\n" +
                                        "§2||§r§f Blood Rush took §a§l" + ChatUtils.formatTime((rt.get(0) - startTime) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(0) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Blood Camp took §a§l" + ChatUtils.formatTime((rt.get(1) - rt.get(0)) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(1) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Portal took §a§l" + ChatUtils.formatTime((rt.get(2) - rt.get(1)) / 1000.0) + "§r§f to enter\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(2) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Terracottas took §a§l" + ChatUtils.formatTime((rt.get(3) - rt.get(2)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(3) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Giants took §a§l" + ChatUtils.formatTime((rt.get(4) - rt.get(3)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(4) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Sadan took §a§l" + ChatUtils.formatTime((rt.get(5) - rt.get(4)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(5) / 1000.0) + "§r§f to lag\n"
                                );

                                if (lagMessage.getValue()) {
                                    ChatUtils.partyChat(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(((System.currentTimeMillis() - startTime) / 1000.0) - (totalLag / 1000.0)));
                                }
                            }
                            break;
                        case "F7":
                            DungeonStats.sessionF7Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.globalF7Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.saveConfig("F7");

                            if (dungeonTrack.getValue()) {
                                long totalLag = 0;
                                for (long lag : lt) {
                                    totalLag += lag;
                                }

                                double runTime = ((System.currentTimeMillis() - startTime) / 1000.0);

                                double sumOfBest = 0.0;

                                for (double time : DungeonStats.globalF7Stats.bestSplits) {
                                    sumOfBest += time;
                                }

                                ChatUtils.system(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(runTime - (totalLag / 1000.0)));
                                ChatUtils.system("§fTotal Run Time: §a§l" + ChatUtils.formatTime(runTime) + " §r| " + ChatUtils.formatTime(runTime - sumOfBest) + " behind sum of best\n" +
                                        "§2||§r§f Blood Rush took §a§l" + ChatUtils.formatTime((rt.get(0) - startTime) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(0) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Blood Camp took §a§l" + ChatUtils.formatTime((rt.get(1) - rt.get(0)) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(1) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Portal took §a§l" + ChatUtils.formatTime((rt.get(2) - rt.get(1)) / 1000.0) + "§r§f to enter\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(2) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Maxor took §a§l" + ChatUtils.formatTime((rt.get(3) - rt.get(2)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(3) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Storm took §a§l" + ChatUtils.formatTime((rt.get(4) - rt.get(3)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(4) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Terminals took §a§l" + ChatUtils.formatTime((rt.get(5) - rt.get(4)) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(5) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Goldor took §a§l" + ChatUtils.formatTime((rt.get(6) - rt.get(5)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(6) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Necron took §a§l" + ChatUtils.formatTime((rt.get(7) - rt.get(6)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(7) / 1000.0) + "§r§f to lag\n"
                                );

                                if (lagMessage.getValue()) {
                                    ChatUtils.partyChat(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(((System.currentTimeMillis() - startTime) / 1000.0) - (totalLag / 1000.0)));
                                }
                            }
                            break;
                        case "M2":
                            DungeonStats.sessionM2Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.globalM2Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.saveConfig("M2");

                            if (dungeonTrack.getValue()) {
                                long totalLag = 0;
                                for (long lag : lt) {
                                    totalLag += lag;
                                }

                                double runTime = ((System.currentTimeMillis() - startTime) / 1000.0);

                                double sumOfBest = 0.0;

                                for (double time : DungeonStats.globalM2Stats.bestSplits) {
                                    sumOfBest += time;
                                }

                                ChatUtils.system(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(runTime - (totalLag / 1000.0)));
                                ChatUtils.system("§fTotal Run Time: §a§l" + ChatUtils.formatTime(runTime) + " §r| " + ChatUtils.formatTime(runTime - sumOfBest) + " behind sum of best\n" +
                                        "§2||§r§f Blood Rush took §a§l" + ChatUtils.formatTime((rt.get(0) - startTime) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(0) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Blood Camp took §a§l" + ChatUtils.formatTime((rt.get(1) - rt.get(0)) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(1) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Portal took §a§l" + ChatUtils.formatTime((rt.get(2) - rt.get(1)) / 1000.0) + "§r§f to enter\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(2) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Scarf Undeads took §a§l" + ChatUtils.formatTime((rt.get(3) - rt.get(2)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(3) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Scarf took §a§l" + ChatUtils.formatTime((rt.get(4) - rt.get(3)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(4) / 1000.0) + "§r§f to lag\n"
                                );

                                if (lagMessage.getValue()) {
                                    ChatUtils.partyChat(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(((System.currentTimeMillis() - startTime) / 1000.0) - (totalLag / 1000.0)));
                                }
                            }
                            break;
                        case "M3":
                            DungeonStats.sessionM3Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.globalM3Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.saveConfig("M3");

                            if (dungeonTrack.getValue()) {
                                long totalLag = 0;
                                for (long lag : lt) {
                                    totalLag += lag;
                                }

                                double runTime = ((System.currentTimeMillis() - startTime) / 1000.0);

                                double sumOfBest = 0.0;

                                for (double time : DungeonStats.globalM3Stats.bestSplits) {
                                    sumOfBest += time;
                                }

                                ChatUtils.system(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(runTime - (totalLag / 1000.0)));
                                ChatUtils.system("§fTotal Run Time: §a§l" + ChatUtils.formatTime(runTime) + " §r| " + ChatUtils.formatTime(runTime - sumOfBest) + " behind sum of best\n" +
                                        "§2||§r§f Blood Rush took §a§l" + ChatUtils.formatTime((rt.get(0) - startTime) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(0) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Blood Camp took §a§l" + ChatUtils.formatTime((rt.get(1) - rt.get(0)) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(1) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Portal took §a§l" + ChatUtils.formatTime((rt.get(2) - rt.get(1)) / 1000.0) + "§r§f to enter\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(2) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Guardians took §a§l" + ChatUtils.formatTime((rt.get(3) - rt.get(2)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(3) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Human Professor took §a§l" + ChatUtils.formatTime((rt.get(4) - rt.get(3)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(4) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Guardian Professor took §a§l" + ChatUtils.formatTime((rt.get(5) - rt.get(4)) / 1000.0) + "§r§f to kill\n"
                                );

                                if (lagMessage.getValue()) {
                                    ChatUtils.partyChat(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(((System.currentTimeMillis() - startTime) / 1000.0) - (totalLag / 1000.0)));
                                }
                            }
                            break;
                        case "M5":
                            DungeonStats.sessionM5Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.globalM5Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.saveConfig("M5");

                            if (dungeonTrack.getValue()) {
                                long totalLag = 0;
                                for (long lag : lt) {
                                    totalLag += lag;
                                }

                                double runTime = ((System.currentTimeMillis() - startTime) / 1000.0);

                                double sumOfBest = 0.0;

                                for (double time : DungeonStats.globalM5Stats.bestSplits) {
                                    sumOfBest += time;
                                }

                                ChatUtils.system(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(runTime - (totalLag / 1000.0)));
                                ChatUtils.system("§fTotal Run Time: §a§l" + ChatUtils.formatTime(runTime) + " §r| " + ChatUtils.formatTime(runTime - sumOfBest) + " behind sum of best\n" +
                                        "§2||§r§f Blood Rush took §a§l" + ChatUtils.formatTime((rt.get(0) - startTime) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(0) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Blood Camp took §a§l" + ChatUtils.formatTime((rt.get(1) - rt.get(0)) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(1) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Portal took §a§l" + ChatUtils.formatTime((rt.get(2) - rt.get(1)) / 1000.0) + "§r§f to enter\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(2) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Livid took §a§l" + ChatUtils.formatTime((rt.get(3) - rt.get(2)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(3) / 1000.0) + "§r§f to lag\n"
                                );

                                if (lagMessage.getValue()) {
                                    ChatUtils.partyChat(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(((System.currentTimeMillis() - startTime) / 1000.0) - (totalLag / 1000.0)));
                                }
                            }
                            break;
                        case "M6":
                            DungeonStats.sessionM6Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.globalM6Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.saveConfig("M6");

                            if (dungeonTrack.getValue()) {
                                long totalLag = 0;
                                for (long lag : lt) {
                                    totalLag += lag;
                                }

                                double runTime = ((System.currentTimeMillis() - startTime) / 1000.0);

                                double sumOfBest = 0.0;

                                for (double time : DungeonStats.globalM6Stats.bestSplits) {
                                    sumOfBest += time;
                                }

                                ChatUtils.system(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(runTime - (totalLag / 1000.0)));
                                ChatUtils.system("§fTotal Run Time: §a§l" + ChatUtils.formatTime(runTime) + " §r| " + ChatUtils.formatTime(runTime - sumOfBest) + " behind sum of best\n" +
                                        "§2||§r§f Blood Rush took §a§l" + ChatUtils.formatTime((rt.get(0) - startTime) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(0) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Blood Camp took §a§l" + ChatUtils.formatTime((rt.get(1) - rt.get(0)) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(1) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Portal took §a§l" + ChatUtils.formatTime((rt.get(2) - rt.get(1)) / 1000.0) + "§r§f to enter\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(2) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Terracottas took §a§l" + ChatUtils.formatTime((rt.get(3) - rt.get(2)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(3) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Giants took §a§l" + ChatUtils.formatTime((rt.get(4) - rt.get(3)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(4) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Sadan took §a§l" + ChatUtils.formatTime((rt.get(5) - rt.get(4)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(5) / 1000.0) + "§r§f to lag\n"
                                );

                                if (lagMessage.getValue()) {
                                    ChatUtils.partyChat(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(((System.currentTimeMillis() - startTime) / 1000.0) - (totalLag / 1000.0)));
                                }
                            }
                            break;
                        case "M7":
                            DungeonStats.sessionM7Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.globalM7Stats.addSuccess(startTime, rt, lt);
                            DungeonStats.saveConfig("M7");

                            if (dungeonTrack.getValue()) {
                                long totalLag = 0;
                                for (long lag : lt) {
                                    totalLag += lag;
                                }

                                double runTime = ((System.currentTimeMillis() - startTime) / 1000.0);

                                double sumOfBest = 0.0;

                                for (double time : DungeonStats.globalM7Stats.bestSplits) {
                                    sumOfBest += time;
                                }

                                ChatUtils.system(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(runTime - (totalLag / 1000.0)));
                                ChatUtils.system("§fTotal Run Time: §a§l" + ChatUtils.formatTime(runTime) + " §r| " + ChatUtils.formatTime(runTime - sumOfBest) + " behind sum of best\n" +
                                        "§2||§r§f Blood Rush took §a§l" + ChatUtils.formatTime((rt.get(0) - startTime) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(0) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Blood Camp took §a§l" + ChatUtils.formatTime((rt.get(1) - rt.get(0)) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(1) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Portal took §a§l" + ChatUtils.formatTime((rt.get(2) - rt.get(1)) / 1000.0) + "§r§f to enter\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(2) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Maxor took §a§l" + ChatUtils.formatTime((rt.get(3) - rt.get(2)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(3) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Storm took §a§l" + ChatUtils.formatTime((rt.get(4) - rt.get(3)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(4) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Terminals took §a§l" + ChatUtils.formatTime((rt.get(5) - rt.get(4)) / 1000.0) + "§r§f to complete\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(5) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Goldor took §a§l" + ChatUtils.formatTime((rt.get(6) - rt.get(5)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(6) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Necron took §a§l" + ChatUtils.formatTime((rt.get(7) - rt.get(6)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(7) / 1000.0) + "§r§f to lag\n" +
                                        "§2||§r§f Dragons took §a§l" + ChatUtils.formatTime((rt.get(8) - rt.get(7)) / 1000.0) + "§r§f to kill\n" +
                                        "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(8) / 1000.0) + "§r§f to lag\n"
                                );

                                if (lagMessage.getValue()) {
                                    ChatUtils.partyChat(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(((System.currentTimeMillis() - startTime) / 1000.0) - (totalLag / 1000.0)));
                                }
                            }
                            break;
                    }
                } else {
                    switch (currentTier.getTierName()) {
                        case "F2":
                            DungeonStats.sessionF2Stats.addFailure(startTime);
                            DungeonStats.globalF2Stats.addFailure(startTime);
                            break;
                        case "F3":
                            DungeonStats.sessionF3Stats.addFailure(startTime);
                            DungeonStats.globalF3Stats.addFailure(startTime);
                            break;
                        case "F5":
                            DungeonStats.sessionF5Stats.addFailure(startTime);
                            DungeonStats.globalF5Stats.addFailure(startTime);
                            break;
                        case "F6":
                            DungeonStats.sessionF6Stats.addFailure(startTime);
                            DungeonStats.globalF6Stats.addFailure(startTime);
                            break;
                        case "F7":
                            DungeonStats.sessionF7Stats.addFailure(startTime);
                            DungeonStats.globalF7Stats.addFailure(startTime);
                            break;
                        case "M2":
                            DungeonStats.sessionM2Stats.addFailure(startTime);
                            DungeonStats.globalM2Stats.addFailure(startTime);
                            break;
                        case "M3":
                            DungeonStats.sessionM3Stats.addFailure(startTime);
                            DungeonStats.globalM3Stats.addFailure(startTime);
                            break;
                        case "M5":
                            DungeonStats.sessionM5Stats.addFailure(startTime);
                            DungeonStats.globalM5Stats.addFailure(startTime);
                            break;
                        case "M6":
                            DungeonStats.sessionM6Stats.addFailure(startTime);
                            DungeonStats.globalM6Stats.addFailure(startTime);
                            break;
                        case "M7":
                            DungeonStats.sessionM7Stats.addFailure(startTime);
                            DungeonStats.globalM7Stats.addFailure(startTime);
                            break;
                    }
                }

                endRun();

                if (autoRequeue.getValue() && PartyUtils.isLeader()) {
                    if (PartyUtils.getDowntimeFlag()) {
                        DelayUtils.scheduleTask(() -> ChatUtils.partyChat("Taking Downtime!"), 2500);
                        PartyUtils.useDowntimeFlag();
                    } else {
                        switch (currentTier.getTierName()) {
                            case "F2":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon catacombs_floor_two"), autoRequeueDelay.getValue() * 1000);
                                break;
                            case "F3":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon catacombs_floor_three"), autoRequeueDelay.getValue() * 1000);
                                break;
                            case "F5":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon catacombs_floor_five"), autoRequeueDelay.getValue() * 1000);
                                break;
                            case "F6":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon catacombs_floor_six"), autoRequeueDelay.getValue() * 1000);
                                break;
                            case "F7":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon catacombs_floor_seven"), autoRequeueDelay.getValue() * 1000);
                                break;
                            case "M2":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon master_catacombs_floor_two"), autoRequeueDelay.getValue() * 1000);
                                break;
                            case "M3":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon master_catacombs_floor_three"), autoRequeueDelay.getValue() * 1000);
                                break;
                            case "M5":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon master_catacombs_floor_five"), autoRequeueDelay.getValue() * 1000);
                                break;
                            case "M6":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon master_catacombs_floor_six"), autoRequeueDelay.getValue() * 1000);
                                break;
                            case "M7":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon master_catacombs_floor_seven"), autoRequeueDelay.getValue() * 1000);
                                break;
                        }}
                }
                break;
        }
    }

    public void prepRun() { // this should be called on the "user is ready" message and enters phase 0 starting the run start listener
        bossDead = false;
        dt.flush();
        currentPhase = 0;
        currentTier.getPhases().get(currentPhase).enterPhase();
        dl.setActiveListeners(currentTier.getPhases().get(currentPhase).getTriggers());
        runPrimed = true;

        if (resetOnParty.getValue()) {
            if (PartyUtils.getDungeonFlag()) {
                DungeonStats.sessionF2Stats.reset();
                DungeonStats.sessionF3Stats.reset();
                DungeonStats.sessionF5Stats.reset();
                DungeonStats.sessionF6Stats.reset();
                DungeonStats.sessionF7Stats.reset();
                DungeonStats.sessionM2Stats.reset();
                DungeonStats.sessionM3Stats.reset();
                DungeonStats.sessionM5Stats.reset();
                DungeonStats.sessionM6Stats.reset();
                DungeonStats.sessionM7Stats.reset();
            }
        }

        PartyUtils.useDungeonFlag();
        PartyUtils.useDowntimeFlag();
    }

    public void advancePhase() {
        dt.split(rt, lt, currentPhase); // tracks end of split and amount of lag

        currentTier.getPhases().get(currentPhase).exitPhase();
        currentPhase++;
        currentTier.getPhases().get(currentPhase).enterPhase();
        dl.setActiveListeners(currentTier.getPhases().get(currentPhase).getTriggers());
    }
}
