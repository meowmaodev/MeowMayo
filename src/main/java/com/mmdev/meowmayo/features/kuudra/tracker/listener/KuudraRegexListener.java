package com.mmdev.meowmayo.features.kuudra.tracker.listener;

import com.mmdev.meowmayo.features.kuudra.tracker.KuudraTracker;
import com.mmdev.meowmayo.utils.tracker.Events;
import com.mmdev.meowmayo.utils.tracker.listeners.ChatMatchListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KuudraRegexListener extends ChatMatchListener {
    Events event;
    KuudraTracker tracker;
    Pattern pattern;

    public KuudraRegexListener(KuudraTracker tracker, String regex, Events event)  {
        super(regex, event);
        this.tracker = tracker;
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public void onChatMessage(String msg) {
        if (isActive()) {
            Matcher matcher = pattern.matcher(msg);
            if (matcher.matches()) {
                tracker.signal(event, matcher);
            }
        }
    }
}
