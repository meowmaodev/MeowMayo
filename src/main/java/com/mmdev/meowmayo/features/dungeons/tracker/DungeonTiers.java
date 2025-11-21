package com.mmdev.meowmayo.features.dungeons.tracker;

import com.mmdev.meowmayo.features.dungeons.tracker.listener.DungeonChatListener;
import com.mmdev.meowmayo.features.dungeons.tracker.listener.DungeonRegexListener;
import com.mmdev.meowmayo.utils.tracker.Events;
import com.mmdev.meowmayo.utils.tracker.Phase;
import com.mmdev.meowmayo.utils.tracker.Tiers;

import java.util.Arrays;
import java.util.HashSet;

public class DungeonTiers {
    public static Tiers M7 = new Tiers("M7");

    public static void init(DungeonTracker tracker) {
        DungeonChatListener runStart = new DungeonChatListener(tracker, "[NPC] Mort: Here, I found this map when I first entered the dungeon.", Events.DUNGEON_START);
        DungeonRegexListener witherDoor = new DungeonRegexListener(tracker, "^(.{2,16}) opened a WITHER door!$", Events.WITHER_DOOR);
        DungeonChatListener bloodOpen = new DungeonChatListener(tracker, "The BLOOD DOOR has been opened!", Events.BLOOD_OPEN);
        DungeonChatListener bloodDone = new DungeonChatListener(tracker, "[BOSS] The Watcher: You have proven yourself. You may pass.", Events.BLOOD_DONE);

        // these are all per boss now!
        DungeonChatListener maxorSpawn = new DungeonChatListener(tracker, "[BOSS] Maxor: WELL! WELL! WELL! LOOK WHO'S HERE!", Events.BOSS_ENTER);
        DungeonRegexListener crystalPlaced = new DungeonRegexListener(tracker, "", Events.CRYSTAL_PLACED);
        DungeonChatListener stormSpawn = new DungeonChatListener(tracker, "[BOSS] Storm: Pathetic Maxor, just like expected.", Events.MAXOR_DEAD);
        DungeonChatListener stormLightning = new DungeonChatListener(tracker, "[BOSS] Storm: THUNDER LET ME BE YOUR CATALYST!", Events.STORM_LIGHTNING);
        DungeonChatListener goldorSpawn = new DungeonChatListener(tracker, "[BOSS] Goldor: Who dares trespass into my domain?", Events.STORM_DEAD);
        DungeonChatListener coreOpen = new DungeonChatListener(tracker, "The Core entrance is opening!", Events.CORE_OPEN);
        DungeonChatListener necronSpawn = new DungeonChatListener(tracker, "[BOSS] Necron: You went further than any human before, congratulations.", Events.GOLDOR_DEAD);
        DungeonChatListener necronDead = new DungeonChatListener(tracker, "[BOSS] Necron: All this, for nothing...", Events.NECRON_DEAD);
        DungeonChatListener witherKingSpawn = new DungeonChatListener(tracker, "[BOSS] Wither King: You... again?", Events.RELICS_DOWN);
        DungeonRegexListener runOver = new DungeonRegexListener(tracker, "^\\w+> EXTRA STATS <$", Events.DUNGEON_END);

        M7.addPhase(
                new Phase("Blood Rush", new HashSet<>(Arrays.asList(
                        runStart, witherDoor, bloodOpen, runOver
                )), () -> {}, () -> {})
        );
        M7.addPhase(
                new Phase("Blood Camp", new HashSet<>(Arrays.asList(
                        bloodDone, maxorSpawn, runOver
                )), () -> {}, () -> {})
        );
        M7.addPhase(
                new Phase("Maxor", new HashSet<>(Arrays.asList(
                        crystalPlaced, stormSpawn, runOver
                )), () -> {}, () -> {})
        );
        M7.addPhase(
                new Phase("Storm", new HashSet<>(Arrays.asList(
                        stormLightning, goldorSpawn, runOver
                )), () -> {}, () -> {})
        );
        M7.addPhase(
                new Phase("Terminals", new HashSet<>(Arrays.asList(
                        coreOpen, runOver
                )), () -> {}, () -> {})
        );
        M7.addPhase(
                new Phase("Core", new HashSet<>(Arrays.asList(
                        necronSpawn, runOver
                )), () -> {}, () -> {})
        );
        M7.addPhase(
                new Phase("Necron", new HashSet<>(Arrays.asList(
                        necronDead, runOver
                )), () -> {}, () -> {})
        );
        M7.addPhase(
                new Phase("Dragons", new HashSet<>(Arrays.asList(
                        witherKingSpawn, runOver // this should be checked to ensure the phase name is dragons or smth. rudementary checks idk. hopefully i can make a better soln but this will do for now to get it made.
                )), () -> {}, () -> {})
        );
    }
}
