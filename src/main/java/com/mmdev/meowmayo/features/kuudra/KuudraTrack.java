package com.mmdev.meowmayo.features.kuudra;

import com.mmdev.meowmayo.features.kuudra.tracker.KuudraListener;
import com.mmdev.meowmayo.features.kuudra.tracker.KuudraTiers;
import com.mmdev.meowmayo.features.kuudra.tracker.KuudraTimer;
import com.mmdev.meowmayo.features.kuudra.tracker.KuudraTracker;
import net.minecraftforge.common.MinecraftForge;

public class KuudraTrack {
    static KuudraListener listener = new KuudraListener();
    static KuudraTimer timer = new KuudraTimer();

    static KuudraTracker tracker = new KuudraTracker(timer, listener);
    public static void init() {
        KuudraTiers.init(tracker);
        listener.setRunEnd(KuudraTiers.runOver);
        KuudraTiers.runOver.enable();
        tracker.setCurrentTier(KuudraTiers.infernal); // deefalt

        MinecraftForge.EVENT_BUS.register(listener);
        MinecraftForge.EVENT_BUS.register(timer);
        MinecraftForge.EVENT_BUS.register(tracker);
    }
}
