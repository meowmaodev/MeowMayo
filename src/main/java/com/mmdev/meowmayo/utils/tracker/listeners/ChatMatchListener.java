package com.mmdev.meowmayo.utils.tracker.listeners;

import com.mmdev.meowmayo.utils.tracker.Events;
import com.mmdev.meowmayo.utils.tracker.PhaseListener;

public abstract class ChatMatchListener extends PhaseListener {
    final String check;
    Events events;

    public ChatMatchListener(String check, Events event) {
        super(event);
        this.check = check;
    }

    public abstract void onChatMessage(String msg);
}
