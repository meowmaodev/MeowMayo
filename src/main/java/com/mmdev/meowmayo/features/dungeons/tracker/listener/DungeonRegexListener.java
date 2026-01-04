package com.mmdev.meowmayo.features.dungeons.tracker.listener;

import com.mmdev.meowmayo.features.dungeons.tracker.DungeonTracker;
import com.mmdev.meowmayo.utils.tracker.Events;
import com.mmdev.meowmayo.utils.tracker.listeners.ChatMatchListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DungeonRegexListener extends ChatMatchListener {
    Events event;
    DungeonTracker tracker;
    Pattern pattern;

    public DungeonRegexListener(DungeonTracker tracker, String regex, Events event)  {
        super(regex, event);
        this.event = event;
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
