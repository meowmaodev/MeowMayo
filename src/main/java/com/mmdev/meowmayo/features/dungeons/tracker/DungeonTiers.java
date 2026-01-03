package com.mmdev.meowmayo.features.dungeons.tracker;

import com.mmdev.meowmayo.features.dungeons.tracker.listener.DungeonChatListener;
import com.mmdev.meowmayo.features.dungeons.tracker.listener.DungeonRegexListener;
import com.mmdev.meowmayo.utils.tracker.Events;
import com.mmdev.meowmayo.utils.tracker.Phase;
import com.mmdev.meowmayo.utils.tracker.Tiers;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class DungeonTiers {
    public static Tiers F1 = new Tiers("F1");
    public static Tiers F2 = new Tiers("F2");
    public static Tiers F3 = new Tiers("F3");
    public static Tiers F4 = new Tiers("F4");
    public static Tiers F5 = new Tiers("F5");
    public static Tiers F6 = new Tiers("F6");
    public static Tiers F7 = new Tiers("F7");

    public static Tiers M1 = new Tiers("M1");
    public static Tiers M2 = new Tiers("M2");
    public static Tiers M3 = new Tiers("M3");
    public static Tiers M4 = new Tiers("M4");
    public static Tiers M5 = new Tiers("M5");
    public static Tiers M6 = new Tiers("M6");
    public static Tiers M7 = new Tiers("M7");

    public static DungeonRegexListener runOver;

    public static void init(DungeonTracker tracker) {
        // globals
        DungeonChatListener runStart = new DungeonChatListener(tracker, "[NPC] Mort: Here, I found this map when I first entered the dungeon.", Events.DUNGEON_START);
        DungeonRegexListener witherDoor = new DungeonRegexListener(tracker, "^(.{2,16}) opened a WITHER door!$", Events.WITHER_DOOR);
        DungeonChatListener bloodOpen = new DungeonChatListener(tracker, "The BLOOD DOOR has been opened!", Events.BLOOD_OPEN);
        DungeonChatListener bloodDone = new DungeonChatListener(tracker, "[BOSS] The Watcher: You have proven yourself. You may pass.", Events.BLOOD_DONE);

        DungeonRegexListener bossDead = new DungeonRegexListener(tracker, "^\\s*☠ Defeated (.+) in (.+)s$", Events.DUNGEON_BOSS_DEAD);

        // f/m1
        DungeonChatListener bonzoSpawn = new DungeonChatListener(tracker, "[BOSS] Bonzo: Gratz for making it this far, but I’m basically unbeatable.", Events.BOSS_ENTER);

        // f/m2
        DungeonChatListener scarfSpawn = new DungeonChatListener(tracker, "[BOSS] Scarf: This is where the journey ends for you, Adventurers.", Events.BOSS_ENTER);
        DungeonChatListener scarfCryptsDead = new DungeonChatListener(tracker, "[BOSS] Scarf: Those toys are not strong enough I see.", Events.SCARF_CRYPTS_DEAD);

        // f/m3
        DungeonChatListener professorSpawn = new DungeonChatListener(tracker, "[BOSS] The Professor: I was burdened with terrible news recently...", Events.BOSS_ENTER);
        DungeonChatListener guardiansDead = new DungeonChatListener(tracker, "[BOSS] The Professor: Oh? You found my Guardians' one weakness?", Events.GUARDIANS_DEAD);
        DungeonChatListener profDead = new DungeonChatListener(tracker, "[BOSS] The Professor: I see. You have forced me to use my ultimate technique.", Events.PROFESSOR_1_DEAD);

        // f/m4
        DungeonChatListener thornSpawn = new DungeonChatListener(tracker, "[BOSS] Thorn: Welcome Adventurers! I am Thorn, the Spirit! And host of the Vegan Trials!", Events.BOSS_ENTER);

        // f/m5
        DungeonChatListener lividSpawn = new DungeonChatListener(tracker, "[BOSS] Livid: Welcome, you've arrived right on time. I am Livid, the Master of Shadows.", Events.BOSS_ENTER);

        // f/m6
        DungeonChatListener sadanSpawn = new DungeonChatListener(tracker, "[BOSS] Sadan: So you made it all the way here... Now you wish to defy me? Sadan?!", Events.BOSS_ENTER);
        DungeonChatListener terracottasDead = new DungeonChatListener(tracker, "[BOSS] Sadan: ENOUGH!", Events.TERRACOTTAS_DEAD);
        DungeonChatListener giantsDead = new DungeonChatListener(tracker, "[BOSS] Sadan: You did it. I understand now, you have earned my respect.", Events.GIANTS_DEAD);

        // f/m7
        DungeonChatListener maxorSpawn = new DungeonChatListener(tracker, "[BOSS] Maxor: WELL! WELL! WELL! LOOK WHO'S HERE!", Events.BOSS_ENTER);
        DungeonChatListener crystalPlaced = new DungeonChatListener(tracker, "2/2 Energy Crystals are now active!", Events.CRYSTAL_PLACED);
        DungeonChatListener stormSpawn = new DungeonChatListener(tracker, "[BOSS] Storm: Pathetic Maxor, just like expected.", Events.MAXOR_DEAD);
        DungeonChatListener stormLightning = new DungeonChatListener(tracker, "[BOSS] Storm: THUNDER LET ME BE YOUR CATALYST!", Events.STORM_LIGHTNING);
        DungeonChatListener goldorSpawn = new DungeonChatListener(tracker, "[BOSS] Goldor: Who dares trespass into my domain?", Events.STORM_DEAD);
        DungeonChatListener coreOpen = new DungeonChatListener(tracker, "The Core entrance is opening!", Events.CORE_OPEN);
        DungeonChatListener necronSpawn = new DungeonChatListener(tracker, "[BOSS] Necron: You went further than any human before, congratulations.", Events.GOLDOR_DEAD);
        DungeonChatListener necronDead = new DungeonChatListener(tracker, "[BOSS] Necron: All this, for nothing...", Events.NECRON_DEAD);
        DungeonChatListener witherKingSpawn = new DungeonChatListener(tracker, "[BOSS] Wither King: You... again?", Events.RELICS_DOWN);

        runOver = new DungeonRegexListener(tracker, "^\\s+> EXTRA STATS <$", Events.DUNGEON_END);

        F1.addPhase(new Phase("Blood Rush", new HashSet<>(Arrays.asList(runStart, witherDoor, bloodOpen))));
        F1.addPhase(new Phase("Blood Camp", new HashSet<>(Collections.singletonList(bloodDone))));
        F1.addPhase(new Phase("Boss Enter", new HashSet<>(Collections.singletonList(bonzoSpawn))));
        F1.addPhase(new Phase("Bonzo", new HashSet<>(Collections.singletonList(bossDead))));

        F2.addPhase(new Phase("Blood Rush", new HashSet<>(Arrays.asList(runStart, witherDoor, bloodOpen))));
        F2.addPhase(new Phase("Blood Camp", new HashSet<>(Collections.singletonList(bloodDone))));
        F2.addPhase(new Phase("Boss Enter", new HashSet<>(Collections.singletonList(scarfSpawn))));
        F2.addPhase(new Phase("Minions", new HashSet<>(Collections.singletonList(scarfCryptsDead))));
        F2.addPhase(new Phase("Scarf", new HashSet<>(Collections.singletonList(bossDead))));

        F3.addPhase(new Phase("Blood Rush", new HashSet<>(Arrays.asList(runStart, witherDoor, bloodOpen))));
        F3.addPhase(new Phase("Blood Camp", new HashSet<>(Collections.singletonList(bloodDone))));
        F3.addPhase(new Phase("Boss Enter", new HashSet<>(Collections.singletonList(professorSpawn))));
        F3.addPhase(new Phase("Guardians", new HashSet<>(Collections.singletonList(guardiansDead))));
        F3.addPhase(new Phase("Professor", new HashSet<>(Collections.singletonList(profDead))));
        F3.addPhase(new Phase("Super Professor", new HashSet<>(Collections.singletonList(bossDead))));

        F4.addPhase(new Phase("Blood Rush", new HashSet<>(Arrays.asList(runStart, witherDoor, bloodOpen))));
        F4.addPhase(new Phase("Blood Camp", new HashSet<>(Collections.singletonList(bloodDone))));
        F4.addPhase(new Phase("Boss Enter", new HashSet<>(Collections.singletonList(thornSpawn))));
        F4.addPhase(new Phase("Thorn", new HashSet<>(Collections.singletonList(bossDead))));

        F5.addPhase(new Phase("Blood Rush", new HashSet<>(Arrays.asList(runStart, witherDoor, bloodOpen))));
        F5.addPhase(new Phase("Blood Camp", new HashSet<>(Collections.singletonList(bloodDone))));
        F5.addPhase(new Phase("Boss Enter", new HashSet<>(Collections.singletonList(lividSpawn))));
        F5.addPhase(new Phase("Livid", new HashSet<>(Collections.singletonList(bossDead))));

        F6.addPhase(new Phase("Blood Rush", new HashSet<>(Arrays.asList(runStart, witherDoor, bloodOpen))));
        F6.addPhase(new Phase("Blood Camp", new HashSet<>(Collections.singletonList(bloodDone))));
        F6.addPhase(new Phase("Boss Enter", new HashSet<>(Collections.singletonList(sadanSpawn))));
        F6.addPhase(new Phase("Terracottas", new HashSet<>(Collections.singletonList(terracottasDead))));
        F6.addPhase(new Phase("Giants", new HashSet<>(Collections.singletonList(giantsDead))));
        F6.addPhase(new Phase("Sadan", new HashSet<>(Collections.singletonList(bossDead))));

        F7.addPhase(new Phase("Blood Rush", new HashSet<>(Arrays.asList(runStart, witherDoor, bloodOpen))));
        F7.addPhase(new Phase("Blood Camp", new HashSet<>(Collections.singletonList(bloodDone))));
        F7.addPhase(new Phase("Boss Enter", new HashSet<>(Collections.singletonList(maxorSpawn))));
        F7.addPhase(new Phase("Maxor", new HashSet<>(Arrays.asList(crystalPlaced, stormSpawn))));
        F7.addPhase(new Phase("Storm", new HashSet<>(Arrays.asList(stormLightning, goldorSpawn))));
        F7.addPhase(new Phase("Terminals", new HashSet<>(Collections.singletonList(coreOpen))));
        F7.addPhase(new Phase("Core", new HashSet<>(Collections.singletonList(necronSpawn))));
        F7.addPhase(new Phase("Necron", new HashSet<>(Collections.singletonList(bossDead))));

        M1.addPhase(new Phase("Blood Rush", new HashSet<>(Arrays.asList(runStart, witherDoor, bloodOpen))));
        M1.addPhase(new Phase("Blood Camp", new HashSet<>(Collections.singletonList(bloodDone))));
        M1.addPhase(new Phase("Boss Enter", new HashSet<>(Collections.singletonList(bonzoSpawn))));
        M1.addPhase(new Phase("Bonzo", new HashSet<>(Collections.singletonList(bossDead))));

        M2.addPhase(new Phase("Blood Rush", new HashSet<>(Arrays.asList(runStart, witherDoor, bloodOpen))));
        M2.addPhase(new Phase("Blood Camp", new HashSet<>(Collections.singletonList(bloodDone))));
        M2.addPhase(new Phase("Boss Enter", new HashSet<>(Collections.singletonList(scarfSpawn))));
        M2.addPhase(new Phase("Minions", new HashSet<>(Collections.singletonList(scarfCryptsDead))));
        M2.addPhase(new Phase("Scarf", new HashSet<>(Collections.singletonList(bossDead))));

        M3.addPhase(new Phase("Blood Rush", new HashSet<>(Arrays.asList(runStart, witherDoor, bloodOpen))));
        M3.addPhase(new Phase("Blood Camp", new HashSet<>(Collections.singletonList(bloodDone))));
        M3.addPhase(new Phase("Boss Enter", new HashSet<>(Collections.singletonList(professorSpawn))));
        M3.addPhase(new Phase("Guardians", new HashSet<>(Collections.singletonList(guardiansDead))));
        M3.addPhase(new Phase("Professor", new HashSet<>(Collections.singletonList(profDead))));
        M3.addPhase(new Phase("Super Professor", new HashSet<>(Collections.singletonList(bossDead))));

        M4.addPhase(new Phase("Blood Rush", new HashSet<>(Arrays.asList(runStart, witherDoor, bloodOpen))));
        M4.addPhase(new Phase("Blood Camp", new HashSet<>(Collections.singletonList(bloodDone))));
        M4.addPhase(new Phase("Boss Enter", new HashSet<>(Collections.singletonList(thornSpawn))));
        M4.addPhase(new Phase("Thorn", new HashSet<>(Collections.singletonList(bossDead))));

        M5.addPhase(new Phase("Blood Rush", new HashSet<>(Arrays.asList(runStart, witherDoor, bloodOpen))));
        M5.addPhase(new Phase("Blood Camp", new HashSet<>(Collections.singletonList(bloodDone))));
        M5.addPhase(new Phase("Boss Enter", new HashSet<>(Collections.singletonList(lividSpawn))));
        M5.addPhase(new Phase("Livid", new HashSet<>(Collections.singletonList(bossDead))));

        M6.addPhase(new Phase("Blood Rush", new HashSet<>(Arrays.asList(runStart, witherDoor, bloodOpen))));
        M6.addPhase(new Phase("Blood Camp", new HashSet<>(Collections.singletonList(bloodDone))));
        M6.addPhase(new Phase("Boss Enter", new HashSet<>(Collections.singletonList(sadanSpawn))));
        M6.addPhase(new Phase("Terracottas", new HashSet<>(Collections.singletonList(terracottasDead))));
        M6.addPhase(new Phase("Giants", new HashSet<>(Collections.singletonList(giantsDead))));
        M6.addPhase(new Phase("Sadan", new HashSet<>(Collections.singletonList(bossDead))));

        M7.addPhase(new Phase("Blood Rush", new HashSet<>(Arrays.asList(runStart, witherDoor, bloodOpen))));
        M7.addPhase(new Phase("Blood Camp", new HashSet<>(Collections.singletonList(bloodDone))));
        M7.addPhase(new Phase("Boss Enter", new HashSet<>(Collections.singletonList(maxorSpawn))));
        M7.addPhase(new Phase("Maxor", new HashSet<>(Arrays.asList(crystalPlaced, stormSpawn))));
        M7.addPhase(new Phase("Storm", new HashSet<>(Arrays.asList(stormLightning, goldorSpawn))));
        M7.addPhase(new Phase("Terminals", new HashSet<>(Collections.singletonList(coreOpen))));
        M7.addPhase(new Phase("Core", new HashSet<>(Collections.singletonList(necronSpawn))));
        M7.addPhase(new Phase("Necron", new HashSet<>(Collections.singletonList(necronDead))));
        M7.addPhase(new Phase("Dragons", new HashSet<>(Arrays.asList(witherKingSpawn, bossDead))));

    }
}
