package com.mmdev.meowmayo.features.dungeons.tracker;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.IntSliderSetting;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.features.dungeons.F7BossFeatures;
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

    private static Tiers currentTier = null;
    private static int currentPhase = -1;
    private int totalPhases = -1;

    private boolean tierChanged = false;

    private DungeonTimer dt;
    private DungeonListener dl;

    private long startTime = 0;
    private long endTime = 0;

    private List<Long> rt = new ArrayList<>();
    private List<Long> lt = new ArrayList<>();

    private boolean runPrimed = false;

    private boolean runActive = false;

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
                        case "F7":
                            setCurrentTier(DungeonTiers.F7);
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
        runActive = false;
        runPrimed = false;
        currentPhase = -1;
        startTime = endTime = 0;
        rt.clear();
        lt.clear();
    }

    public void setCurrentTier(Tiers tier) {
        if (currentTier == null) {
            currentTier = tier;
            this.totalPhases = currentTier.getPhases().size();
            tierChanged = true;
        }
        if (!tier.getTierName().equals(currentTier.getTierName())) {
            currentTier = tier;
            this.totalPhases = currentTier.getPhases().size();
            tierChanged = true;
        }
    }

    public void signal(Events event) {
        if (!runActive && !runPrimed) return;

        switch (currentTier.getTierName()) {
            case "F7":
                switch (event) {
                    case DUNGEON_START:
                        startRun();
                        break;
                    case BLOOD_OPEN:
                        advancePhase();
                        ChatUtils.system("Blood Rush Took: " + ChatUtils.formatTime((rt.get(0) - startTime)/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(0))/1000.0) + ")");
                        break;
                    case BLOOD_DONE:
                        advancePhase();
                        ChatUtils.system("Blood Camp Took: " + ChatUtils.formatTime((rt.get(1) - rt.get(0))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(1))/1000.0) + ")");
                        break;
                    case BOSS_ENTER:
                        advancePhase();
                        ChatUtils.system("Boss Enter Took: " + ChatUtils.formatTime((rt.get(2) - rt.get(1))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(2))/1000.0) + ")");
                        break;
                    case CRYSTAL_PLACED:
                        ChatUtils.system("The Beam is Charging Up!");
                        break;
                    case MAXOR_DEAD:
                        advancePhase();
                        ChatUtils.system("Maxor Took: " + ChatUtils.formatTime((rt.get(3) - rt.get(2))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(3))/1000.0) + ")");
                        break;
                    case STORM_LIGHTNING:
                        break;
                    case STORM_DEAD:
                        F7BossFeatures.signal(Events.STORM_DEAD);
                        advancePhase();
                        ChatUtils.system("Storm Took: " + ChatUtils.formatTime((rt.get(4) - rt.get(3))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(4))/1000.0) + ")");
                        break;
                    case CORE_OPEN:
                        advancePhase();
                        ChatUtils.system("Terminals Took: " + ChatUtils.formatTime((rt.get(5) - rt.get(4))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(5))/1000.0) + ")");
                        break;
                    case GOLDOR_DEAD:
                        F7BossFeatures.signal(Events.GOLDOR_DEAD);
                        advancePhase();
                        ChatUtils.system("Goldor Kill Took: " + ChatUtils.formatTime((rt.get(6) - rt.get(5))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(6))/1000.0) + ")");
                        break;
                    case NECRON_DEAD:
                        ChatUtils.system("Necron Took: " + ChatUtils.formatTime((rt.get(7) - rt.get(6))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(7))/1000.0) + ")");
                        break;
                }
                break;
            case "M7":
                switch (event) {
                    case DUNGEON_START:
                        startRun();
                        break;
                    case BLOOD_OPEN:
                        advancePhase();
                        ChatUtils.system("Blood Rush Took: " + ChatUtils.formatTime((rt.get(0) - startTime)/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(0))/1000.0) + ")");
                        break;
                    case BLOOD_DONE:
                        advancePhase();
                        ChatUtils.system("Blood Camp Took: " + ChatUtils.formatTime((rt.get(1) - rt.get(0))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(1))/1000.0) + ")");
                        break;
                    case BOSS_ENTER:
                        advancePhase();
                        ChatUtils.system("Boss Enter Took: " + ChatUtils.formatTime((rt.get(2) - rt.get(1))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(2))/1000.0) + ")");
                        break;
                    case CRYSTAL_PLACED:
                        ChatUtils.system("The Beam is Charging Up!");
                        break;
                    case MAXOR_DEAD:
                        advancePhase();
                        ChatUtils.system("Maxor Took: " + ChatUtils.formatTime((rt.get(3) - rt.get(2))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(3))/1000.0) + ")");
                        break;
                    case STORM_LIGHTNING:
                        break;
                    case STORM_DEAD:
                        F7BossFeatures.signal(Events.STORM_DEAD);
                        advancePhase();
                        ChatUtils.system("Storm Took: " + ChatUtils.formatTime((rt.get(4) - rt.get(3))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(4))/1000.0) + ")");
                        break;
                    case CORE_OPEN:
                        advancePhase();
                        ChatUtils.system("Terminals Took: " + ChatUtils.formatTime((rt.get(5) - rt.get(4))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(5))/1000.0) + ")");
                        break;
                    case GOLDOR_DEAD:
                        F7BossFeatures.signal(Events.GOLDOR_DEAD);
                        advancePhase();
                        ChatUtils.system("Goldor Kill Took: " + ChatUtils.formatTime((rt.get(6) - rt.get(5))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(6))/1000.0) + ")");
                        break;
                    case NECRON_DEAD:
                        advancePhase();
                        ChatUtils.system("Necron Took: " + ChatUtils.formatTime((rt.get(7) - rt.get(6))/1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(7))/1000.0) + ")");
                        break;
                    case RELICS_DOWN:
                        F7BossFeatures.signal(Events.RELICS_DOWN);
                        break;
                }
                break;
        }
    }

    public void signal(Events event, Matcher matcher) {
        if (!runActive && !runPrimed) return;

        switch (currentTier.getTierName()) {
            case "F7":
                switch (event) {
                    case WITHER_DOOR:
                        ChatUtils.system("A Wither Door has been opened!");
                        break;
                    case DUNGEON_END:
                        ChatUtils.system("Run OVER");
                        prepResults();
                        endRun();
                        break;
                }
                break;
            case "M7":
                switch (event) {
                    case WITHER_DOOR:
                        ChatUtils.system("A Wither Door has been opened!");
                        break;
                    case DUNGEON_END: // this can always proc!!
                        ChatUtils.system("Run OVER");
                        prepResults();
                        endRun();

                        if (autoRequeue.getValue() && PartyUtils.isLeader()) {
                            if (PartyUtils.getDowntimeFlag()) {
                                ChatUtils.partyChat("Taking Downtime!");
                                PartyUtils.useDowntimeFlag();
                            } else {
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon master_catacombs_floor_seven"), autoRequeueDelay.getValue() * 1000);
                            }
                        }
                        break;
                }
                break;
        }
    }

    public void prepRun() { // this should be called on the "user is ready" message and enters phase 0 starting the run start listener
        dt.flush();
        currentPhase = 0;
        currentTier.getPhases().get(currentPhase).enterPhase();
        dl.setActiveListeners(currentTier.getPhases().get(currentPhase).getTriggers());
        runPrimed = true;

        PartyUtils.useDowntimeFlag();
    }

    public void advancePhase() {
        dt.split(rt, lt, currentPhase); // tracks end of split and amount of lag

        currentTier.getPhases().get(currentPhase).exitPhase();
        currentPhase++;
        currentTier.getPhases().get(currentPhase).enterPhase();
        dl.setActiveListeners(currentTier.getPhases().get(currentPhase).getTriggers());
    }

    public void prepResults() {

    }
}
