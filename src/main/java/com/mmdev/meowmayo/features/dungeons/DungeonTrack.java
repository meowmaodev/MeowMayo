package com.mmdev.meowmayo.features.dungeons;

import com.mmdev.meowmayo.features.dungeons.tracker.DungeonListener;
import com.mmdev.meowmayo.features.dungeons.tracker.DungeonTiers;
import com.mmdev.meowmayo.features.dungeons.tracker.DungeonTimer;
import com.mmdev.meowmayo.features.dungeons.tracker.DungeonTracker;
import net.minecraftforge.common.MinecraftForge;

public class DungeonTrack {
    static DungeonListener listener = new DungeonListener();
    static DungeonTimer timer = new DungeonTimer();

    static DungeonTracker tracker = new DungeonTracker(timer, listener);
    public static void init() {
        DungeonTiers.init(tracker);
        listener.setRunEnd(DungeonTiers.runOver);
        DungeonTiers.runOver.enable();
        tracker.setCurrentTier(DungeonTiers.M7); // deefalt

        MinecraftForge.EVENT_BUS.register(listener);
        MinecraftForge.EVENT_BUS.register(timer);
        MinecraftForge.EVENT_BUS.register(tracker);
    }
}
