package com.mmdev.meowmayo.features.kuudra.tracker;

import com.google.common.eventbus.Subscribe;
import com.mmdev.meowmayo.features.kuudra.tracker.listener.KuudraChatListener;
import com.mmdev.meowmayo.features.kuudra.tracker.listener.KuudraRegexListener;
import com.mmdev.meowmayo.features.kuudra.tracker.listener.KuudraRegexListenerCI;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import com.mmdev.meowmayo.utils.tracker.Events;
import com.mmdev.meowmayo.utils.tracker.PhaseListener;
import com.mmdev.meowmayo.utils.tracker.Tiers;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class KuudraTracker {
    private Tiers currentTier;
    private int currentPhase = -1;
    private int totalPhases = -1;

    private boolean tierChanged = false;

    private KuudraTimer kt;
    private KuudraListener kl;

    private long startTime = 0;
    private long endTime = 0;

    private List<Long> rt = new ArrayList<>();
    private List<Long> lt = new ArrayList<>();

    private boolean runPrimed = false;

    private boolean runActive = false;

    KuudraTracker(KuudraTimer kt, KuudraListener kl, Tiers defaultTier) {
        this.kt = kt;
        this.kl = kl;
        setCurrentTier(defaultTier);
    }

    @SubscribeEvent
    public void onChat(S02ChatReceivedEvent event) {
        String msg = event.getUnformattedMessage();
        if (msg.equals(Minecraft.getMinecraft().thePlayer.getName() + " is now ready!")) {
            // check scoreboard
            if (true && !runPrimed) {
                prepRun();
            }
        }
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

        kt.enable();

        startTime = System.currentTimeMillis();

        this.runActive = true;
        this.runPrimed = false;
    }

    public void endRun() {
        runActive = false;
        currentPhase = -1;
        startTime = endTime = 0;
        rt.clear();
        lt.clear();
    }

    public void setCurrentTier(Tiers tier) {
        this.currentTier = tier;
        this.totalPhases = currentTier.getPhases().size();
        tierChanged = true;
    }

    public void signal(Events event) {
        if (!runActive) return;

        switch (currentTier.getTierName()) {
            case "Basic":
            case "Hot":
                switch (event) {
                    case RUN_START:
                        startRun();
                        break;
                    case SUPPLIES_START:
                        kt.split(rt, lt, 0);
                        advancePhase();
                        break;
                    case SUPPLY_GRABBED:
                        break;
                    case BUILD_START:
                        break;
                }
                break;
            case "Burning":
            case "Fiery":
                switch (event) {
                    case RUN_START:
                        startRun();
                        break;
                }
                break;
            case "Infernal":
                switch (event) {
                    case RUN_START:
                        startRun();
                        break;
                    case SUPPLIES_START:
                        kt.split(rt, lt, 0);
                        advancePhase();
                        break;
                    case BUILD_START:
                        kt.split(rt, lt, 1);
                        advancePhase();
                        break;
                    case CANNON_START:
                        kt.split(rt, lt, 2);
                        advancePhase();
                        break;
                    case EATEN_START:
                        if (rt.get(4) != -1) {
                            rt.set(3, rt.get(4));
                            lt.set(3, lt.get(4));
                        } else {
                            kt.split(rt, lt, 3);
                        }
                        break;
                    case STUNNED_START:
                        kt.split(rt, lt, 4);
                        break;
                    case DPS_DONE:
                        kt.split(rt, lt, 5);
                        break;
                    case SKIP_DONE:
                        kt.split(rt, lt, 6);
                        advancePhase();
                        break;
                    case RUN_END_FAILURE:
                        kt.split(rt, lt, 7);

                        currentTier.getPhases().get(currentPhase).exitPhase();
                        break;
                    case RUN_END_SUCCESS:
                        kt.split(rt, lt, 7);

                        currentTier.getPhases().get(currentPhase).exitPhase();
                        break;
                }
                break;
        }
    }

    public void signal(Events event, Matcher matcher) {}

    public void prepRun() { // this should be called on the "user is ready" message and enters phase 0 starting the run start listener
        kt.flush();
        currentTier.getPhases().get(0).enterPhase();
        runPrimed = true;
    }

    public void advancePhase() {
        currentTier.getPhases().get(currentPhase).exitPhase();
        currentPhase++;
        currentTier.getPhases().get(currentPhase).enterPhase();
        kl.setActiveListeners(currentTier.getPhases().get(currentPhase).getTriggers());
    }

    public void prepResults() {

    }
}
