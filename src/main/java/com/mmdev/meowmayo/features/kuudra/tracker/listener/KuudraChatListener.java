package com.mmdev.meowmayo.features.kuudra.tracker.listener;

import com.mmdev.meowmayo.features.kuudra.tracker.KuudraTracker;
import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.tracker.Events;
import com.mmdev.meowmayo.utils.tracker.listeners.ChatMatchListener;

public class KuudraChatListener extends ChatMatchListener {
    String check;
    Events event;
    KuudraTracker tracker;

    public KuudraChatListener(KuudraTracker tracker, String msg, Events event) {
        super(msg, event);
        this.check = msg;
        this.event = event;
        this.tracker = tracker;
    }

    @Override
    public void onChatMessage(String msg) {
        if (isActive()) {
            if (msg.equals(check)) {
                tracker.signal(event);
            }
        }
    }
}
