package com.mmdev.meowmayo.utils.tracker.listeners;

import com.mmdev.meowmayo.utils.tracker.Events;
import com.mmdev.meowmayo.utils.tracker.PhaseListener;

public class TickListener extends PhaseListener {
    private final Runnable onTick; // Code to run each tick

    protected TickListener(Events event, Runnable onTick) {
        super(event);
        this.onTick = onTick;
    }

    public void tick() {
        if (!isActive()) return;
        if (onTick != null) {
            onTick.run();
        }
    }
}

