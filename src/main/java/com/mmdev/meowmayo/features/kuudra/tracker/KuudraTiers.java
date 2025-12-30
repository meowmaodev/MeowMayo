package com.mmdev.meowmayo.features.kuudra.tracker;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.FloatSliderSetting;
import com.mmdev.meowmayo.config.settings.TextSetting;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.features.kuudra.tracker.listener.KuudraChatListener;
import com.mmdev.meowmayo.features.kuudra.tracker.listener.KuudraRegexListener;
import com.mmdev.meowmayo.features.kuudra.tracker.listener.KuudraRegexListenerCI;
import com.mmdev.meowmayo.features.kuudra.tracker.listener.KuudraTickListener;
import com.mmdev.meowmayo.utils.KuudraUtils;
import com.mmdev.meowmayo.utils.PlayerUtils;
import com.mmdev.meowmayo.utils.tracker.Events;
import com.mmdev.meowmayo.utils.tracker.Phase;
import com.mmdev.meowmayo.utils.tracker.Tiers;
import net.minecraft.client.Minecraft;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class KuudraTiers {
    public static Tiers infernal = new Tiers("Infernal");
    public static Tiers fiery = new Tiers("Fiery");
    public static Tiers burning = new Tiers("Burning");
    public static Tiers hot = new Tiers("Hot");
    public static Tiers basic = new Tiers("Basic");

    private static ToggleSetting stunPing = (ToggleSetting) ConfigSettings.getSetting("Stun Ping");
    private static FloatSliderSetting stunPingHp = (FloatSliderSetting) ConfigSettings.getSetting("Stun Ping HP");
    private static TextSetting stunMessage = (TextSetting) ConfigSettings.getSetting("Stun Ping Message");

    public static KuudraRegexListener runOver;

    public static void init(KuudraTracker tracker) {
        //^Party > (.+): No (.+)!$
        KuudraChatListener runStart = new KuudraChatListener(tracker, "[NPC] Elle: Okay adventurers, I will go and fish up Kuudra!", Events.RUN_START);
        KuudraChatListener suppliesStart = new KuudraChatListener(tracker, "[NPC] Elle: Not again!", Events.SUPPLIES_START);
        KuudraRegexListenerCI noSupply = new KuudraRegexListenerCI(tracker, "^Party > (.+): No (.+)!$", Events.NO_SUPPLY);
        KuudraRegexListener supplyGrab = new KuudraRegexListener(tracker, "^(.+) recovered one of Elle's supplies.*$", Events.SUPPLY_GRABBED);
        KuudraChatListener buildStart = new KuudraChatListener(tracker, "[NPC] Elle: OMG! Great work collecting my supplies!", Events.BUILD_START);
        KuudraRegexListenerCI fresh = new KuudraRegexListenerCI(tracker, "^Party > (.+): Fresh.*$", Events.FRESH_PROC);
        KuudraChatListener cannonStart = new KuudraChatListener(tracker, "[NPC] Elle: Phew! The Ballista is finally ready! It should be strong enough to tank Kuudra's blows now!", Events.CANNON_START);
        KuudraChatListener eatenStart = new KuudraChatListener(tracker, "^(.{2,16}) has been eaten by Kuudra!$", Events.EATEN_START);
        KuudraChatListener stunnedStart = new KuudraChatListener(tracker, "^(.{2,16}) destroyed one of Kuudra's pods!$", Events.STUNNED_START);
        KuudraTickListener stunPingListener = new KuudraTickListener(tracker, Events.STUN_PING, () -> {
            if (KuudraUtils.getKuudra() == null) {
                return;
            }

            float kuudraHp = KuudraUtils.getKuudra().getHealth();

            if (stunPing.getValue()) {
                if (kuudraHp < (stunPingHp.getValue()*1000)) {
                    PlayerUtils.makeTextAlert(stunMessage.getValue(), "random.anvil_land", 1000);
                }
            }
        });
        KuudraTickListener skipStart = new KuudraTickListener(tracker, Events.DPS_DONE, () -> {
                if (KuudraUtils.getKuudra() == null) {
                    return;
                }

                float kuudraHp = KuudraUtils.getKuudra().getHealth();

                if (kuudraHp <= 25001.0) {
                    tracker.signal(Events.DPS_DONE);
                }
        });
        KuudraTickListener finalStart = new KuudraTickListener(tracker, Events.SKIP_DONE, () -> {
                if (Minecraft.getMinecraft().thePlayer.posY <= 10.0) tracker.signal(Events.SKIP_DONE);
        });

        runOver = new KuudraRegexListener(tracker, "^\\s*DEFEAT$", Events.RUN_END_FAILURE);
        KuudraRegexListener runSuccess = new KuudraRegexListener(tracker, "^\\s*KUUDRA DOWN!$", Events.RUN_END_SUCCESS);

        basic.addPhase(new Phase("Start", new HashSet<>(Arrays.asList(runStart, suppliesStart))));
        basic.addPhase(new Phase("Supplies", new HashSet<>(Arrays.asList(noSupply, supplyGrab, buildStart))));
        basic.addPhase(new Phase("Build", new HashSet<>(Arrays.asList(fresh, cannonStart))));
        basic.addPhase(new Phase("DPS", new HashSet<>(Collections.singletonList(runSuccess))));

        hot.addPhase(new Phase("Start", new HashSet<>(Arrays.asList(runStart, suppliesStart))));
        hot.addPhase(new Phase("Supplies", new HashSet<>(Arrays.asList(noSupply, supplyGrab, buildStart))));
        hot.addPhase(new Phase("Build", new HashSet<>(Arrays.asList(fresh, cannonStart))));
        hot.addPhase(new Phase("DPS", new HashSet<>(Collections.singletonList(runSuccess))));

        burning.addPhase(new Phase("Start", new HashSet<>(Arrays.asList(runStart, suppliesStart))));
        burning.addPhase(new Phase("Supplies", new HashSet<>(Arrays.asList(noSupply, supplyGrab, buildStart))));
        burning.addPhase(new Phase("Build", new HashSet<>(Arrays.asList(fresh, cannonStart))));
        burning.addPhase(new Phase("DPS", new HashSet<>(Arrays.asList(eatenStart, stunnedStart, runSuccess))));

        fiery.addPhase(new Phase("Start", new HashSet<>(Arrays.asList(runStart, suppliesStart))));
        fiery.addPhase(new Phase("Supplies", new HashSet<>(Arrays.asList(noSupply, supplyGrab, buildStart))));
        fiery.addPhase(new Phase("Build", new HashSet<>(Arrays.asList(fresh, cannonStart))));
        fiery.addPhase(new Phase("DPS", new HashSet<>(Arrays.asList(eatenStart, stunnedStart, runSuccess))));

        infernal.addPhase(new Phase("Start", new HashSet<>(Arrays.asList(runStart, suppliesStart))));
        infernal.addPhase(new Phase("Supplies", new HashSet<>(Arrays.asList(noSupply, supplyGrab, buildStart))));
        infernal.addPhase(new Phase("Build", new HashSet<>(Arrays.asList(fresh, cannonStart))));
        infernal.addPhase(new Phase("DPS", new HashSet<>(Arrays.asList(eatenStart, stunnedStart, stunPingListener, skipStart, finalStart))));
        infernal.addPhase(new Phase("Final", new HashSet<>(Collections.singletonList(runSuccess))));
    }
}
