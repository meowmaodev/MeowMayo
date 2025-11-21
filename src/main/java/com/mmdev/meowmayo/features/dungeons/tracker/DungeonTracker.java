package com.mmdev.meowmayo.features.dungeons.tracker;

import com.mmdev.meowmayo.utils.ChatUtils;
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
    private Tiers currentTier = null;
    private int currentPhase = -1;
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

    Pattern location = Pattern.compile("^⏣ The Catacombs \\((\\w\\d)\\)$");

    @SubscribeEvent
    public void onChat(S02ChatReceivedEvent event) {
        String msg = event.getUnformattedMessage();
        if (msg.equalsIgnoreCase((Minecraft.getMinecraft().thePlayer.getName() + " is now ready!"))) {
            if (!runPrimed) {
                Matcher matcher = location.matcher(ScoreboardUtils.getLocation());

                if (matcher.matches()) {
                    switch (matcher.group(1)) {
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
        this.currentPhase = 0;

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
            this.currentTier = tier;
            this.totalPhases = currentTier.getPhases().size();
            tierChanged = true;
        }
        if (!tier.getTierName().equals(currentTier.getTierName())) {
            this.currentTier = tier;
            this.totalPhases = currentTier.getPhases().size();
            tierChanged = true;
        }
    }

    public void signal(Events event) {
        if (!runActive && !runPrimed) return;

        switch (currentTier.getTierName()) {
            case "M7":
                switch (event) {
                    case DUNGEON_START:
                        ChatUtils.system("Run Started");
                        startRun();
                        break;
                    case WITHER_DOOR:
                        break;
                    case BLOOD_OPEN:
                        ChatUtils.system("Blood Open");
                        advancePhase();
                        break;
                    case BLOOD_DONE:
                        ChatUtils.system("Watched Done");
                        break;
                    case BOSS_ENTER:
                        ChatUtils.system("Entered Boss");
                        advancePhase();
                        break;
                    case CRYSTAL_PLACED:
                    case MAXOR_DEAD:
                        ChatUtils.system("Maxor Dead");
                        advancePhase();
                    case STORM_LIGHTNING:
                        break;
                    case STORM_DEAD:
                        ChatUtils.system("Storm Dead");
                        advancePhase();
                        break;
                    case CORE_OPEN:
                        advancePhase();
                        break;
                    case GOLDOR_DEAD:
                        ChatUtils.system("Goldor Dead");
                        advancePhase();
                        break;
                    case NECRON_DEAD:
                        ChatUtils.system("Necron Dead");
                        advancePhase();
                        break;
                    case RELICS_DOWN:
                        break;
                    case DUNGEON_END:
                        ChatUtils.system("Run OVER");
                        break;
                }
                break;
        }
    }

    public void signal(Events event, Matcher matcher) {}

    public void prepRun() { // this should be called on the "user is ready" message and enters phase 0 starting the run start listener
        dt.flush();
        currentPhase = 0;
        currentTier.getPhases().get(currentPhase).enterPhase();
        dl.setActiveListeners(currentTier.getPhases().get(currentPhase).getTriggers());
        runPrimed = true;
    }

    public void advancePhase() {
        currentTier.getPhases().get(currentPhase).exitPhase();
        currentPhase++;
        currentTier.getPhases().get(currentPhase).enterPhase();
        dl.setActiveListeners(currentTier.getPhases().get(currentPhase).getTriggers());
    }

    public void prepResults() {

    }
}
