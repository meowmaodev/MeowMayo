package com.mmdev.meowmayo.features.kuudra.tracker.listener;

import com.mmdev.meowmayo.features.kuudra.tracker.KuudraTracker;
import com.mmdev.meowmayo.utils.tracker.Events;
import com.mmdev.meowmayo.utils.tracker.PhaseListener;
import com.mmdev.meowmayo.utils.tracker.listeners.TickListener;

public class KuudraTickListener extends TickListener {
    KuudraTracker tracker;
    Events event;

    public KuudraTickListener(KuudraTracker tracker, Events event, Runnable onTick) {
        super(event, onTick);
        this.event = event;
        this.tracker = tracker;
    }
}
