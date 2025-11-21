package com.mmdev.meowmayo.utils.tracker;

import com.mmdev.meowmayo.utils.ChatUtils;
import scala.actors.threadpool.Arrays;

import java.util.List;
import java.util.Set;

public class Phase {
    private final String name;
    private final Set<PhaseListener> triggers;

    private final Runnable onEnter;      // Code to run when phase starts
    private final Runnable onExit;       // Optional cleanup when phase ends

    public Phase(String name, Set<PhaseListener> triggers, Runnable onEnter, Runnable onExit) {
        this.name = name;
        this.triggers = triggers;
        this.onEnter = onEnter;
        this.onExit = onExit;
    }

    public String getName() {
        return name;
    }

    public Set<PhaseListener> getTriggers() {
        return triggers;
    }

    public void enterPhase() {
        for (PhaseListener listener : triggers) {
            listener.enable();
        }
        if (onEnter != null) onEnter.run();
    }

    public void exitPhase() {
        for (PhaseListener listener : triggers) {
            listener.disable();
        }
        if (onExit != null) onExit.run();
    }
}
