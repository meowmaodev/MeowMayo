package com.mmdev.meowmayo.features.dungeons;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.utils.*;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import com.mmdev.meowmayo.utils.events.S2AParticleReceivedEvent;
import com.mmdev.meowmayo.utils.events.S32ReceivedEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class F7BossFeatures {
    private ToggleSetting p3Death = (ToggleSetting) ConfigSettings.getSetting("Phase 3 Death Warning");
    private ToggleSetting dragonSpawnTitle = (ToggleSetting) ConfigSettings.getSetting("Dragon Spawn Title");
    private ToggleSetting splitDragons = (ToggleSetting) ConfigSettings.getSetting("Split Dragon");
    private ToggleSetting dragonSpawnTimer = (ToggleSetting) ConfigSettings.getSetting("Dragon Spawn Timer");

    public static int bossPhase = 0;
    // 1 = maxor, 2 = storm, 3 = terms, 4 = tunnel, 5 = necron, 6 = relics, 7 = dragons

    private static HashMap<String, Integer> prio;
    private static HashMap<String, Integer> noSplitPrio; // who isnt splitting in the big 2025?

    static {
        prio = new HashMap<>();
        prio.put("Purple", 1);
        prio.put("Blue", 2);
        prio.put("Red", 3);
        prio.put("Green", 4);
        prio.put("Orange", 5);

        noSplitPrio = new HashMap<>();
        noSplitPrio.put("Red", 1);
        noSplitPrio.put("Orange", 2);
        noSplitPrio.put("Blue", 3);
        noSplitPrio.put("Purple", 4);
        noSplitPrio.put("Green", 5);
    }

    public static class DragonTimer {
        int time;
        String color;

        DragonTimer(String color) {
            this.time = 5000;
            this.color = color;
        }

        DragonTimer(String color, int time) {
            this.time = time;
            this.color = color;
        }
    }

    private static ArrayList<DragonTimer> spawningDragons = new ArrayList<>();
    private static ArrayList<DragonTimer> recentlySpawned = new ArrayList<>();

    private static boolean firstDrag = false;

    private static int p3TickTimer = 0;

    private static Character playerClass = null;

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (dragonSpawnTimer.getValue()) {
            for (DragonTimer dragonTimer : spawningDragons) {
                RenderShapeUtils.drawFloatingText(dragonTimer.time + "ms", getDragTimerX(dragonTimer.color), 16, getDragTimerZ(dragonTimer.color), 0.3f, event.partialTicks);
            }
        }
    }

    @SubscribeEvent
    public void onParticleSpawn(S2AParticleReceivedEvent event) {
        if (bossPhase == 7) {
            if (splitDragons.getValue()) {
                if (event.getParticleName().equalsIgnoreCase("ENCHANTMENTTABLE")) {
                    String spawning = DungeonUtils.getDragonStatue(event.getParticleX(), event.getParticleY(), event.getParticleZ());
                    if (spawning == null) return;
                    if (spawningDragons.stream().noneMatch(dt -> dt.color.equals(spawning)) && recentlySpawned.stream().noneMatch(dt -> dt.color.equals(spawning))) {
                        spawningDragons.add(new DragonTimer(spawning));
                    } else {
                        return;
                    }

                    if (dragonSpawnTitle.getValue()) {
                        if (firstDrag) {
                            if (spawningDragons.size() == 2) { // split
                                String d1 = spawningDragons.get(0).color;
                                String d2 = spawningDragons.get(1).color;
                                if (splitDragons.getValue()) {
                                    int d1Prio = prio.get(d1);
                                    int d2Prio = prio.get(d2);

                                    if ((playerClass == 'A' || playerClass == 'T')) {
                                        if (d1Prio < d2Prio) {
                                            PlayerUtils.makeTextTitle(DungeonUtils.getDragonPrefix(d1) + d1 + " Dragon Spawning", 2000);
                                            ChatUtils.system(DungeonUtils.getDragonPrefix(d1) + d1 + " Dragon Spawning");
                                        } else {
                                            PlayerUtils.makeTextTitle(DungeonUtils.getDragonPrefix(d2) + d2 + " Dragon Spawning", 2000);
                                            ChatUtils.system(DungeonUtils.getDragonPrefix(d2) + d2 + " Dragon Spawning");
                                        }
                                    } else {
                                        if (d1Prio > d2Prio) {
                                            PlayerUtils.makeTextTitle(DungeonUtils.getDragonPrefix(d1) + d1 + " Dragon Spawning", 2000);
                                            ChatUtils.system(DungeonUtils.getDragonPrefix(d1) + d1 + " Dragon Spawning");
                                        } else {
                                            PlayerUtils.makeTextTitle(DungeonUtils.getDragonPrefix(d2) + d2 + " Dragon Spawning", 2000);
                                            ChatUtils.system(DungeonUtils.getDragonPrefix(d2) + d2 + " Dragon Spawning");
                                        }
                                    }
                                } else { // no split prio
                                    int d1Prio = noSplitPrio.get(d1);
                                    int d2Prio = noSplitPrio.get(d2);

                                    if (d1Prio < d2Prio) {
                                        PlayerUtils.makeTextTitle(DungeonUtils.getDragonPrefix(d1) + d1 + " Dragon Spawning", 2000);
                                        ChatUtils.system(DungeonUtils.getDragonPrefix(d1) + d1 + " Dragon Spawning");
                                    } else {
                                        PlayerUtils.makeTextTitle(DungeonUtils.getDragonPrefix(d2) + d2 + " Dragon Spawning", 2000);
                                        ChatUtils.system(DungeonUtils.getDragonPrefix(d2) + d2 + " Dragon Spawning");
                                    }
                                }

                                firstDrag = false;
                            }
                        } else {
                            if (spawningDragons.size() == 1) { // skip
                                PlayerUtils.makeTextTitle(DungeonUtils.getDragonPrefix(spawning) + spawning + " Dragon Spawning", 2000);
                                ChatUtils.system(DungeonUtils.getDragonPrefix(spawning) + spawning + " Dragon Spawning");
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onServerTick(S32ReceivedEvent event) {
        if (p3Death.getValue()) {
            if (bossPhase == 3) {
                if (p3TickTimer == 45) {
                    PlayerUtils.makeTextAlert("DAMAGE TICK INCOMING!", "note.pling");
                }
                if (p3TickTimer > 45) {
                    Minecraft.getMinecraft().thePlayer.playSound("note.pling", 1.0F, 1.0F);
                }
                if (p3TickTimer >= 60) {
                    PlayerUtils.stopAlert();
                    PlayerUtils.makeTextTitle("DAMAGE TICK PASSED!", 500);
                    p3TickTimer = 0;
                }
                p3TickTimer++;
            }
        }

        if (bossPhase == 7) {
            for (int i = spawningDragons.size() - 1; i >= 0; i--) {
                spawningDragons.get(i).time -= 50;

                if (spawningDragons.get(i).time <= 0) {
                    recentlySpawned.add(new DragonTimer(spawningDragons.get(i).color, 2000));
                    spawningDragons.remove(i);
                }
            }

            for (int i = recentlySpawned.size() - 1; i >= 0; i--) {
                recentlySpawned.get(i).time -= 50;

                if (recentlySpawned.get(i).time <= 0) {
                    recentlySpawned.remove(i);
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        bossPhase = 0;
        PlayerUtils.stopAlert();
    }

    @SubscribeEvent
    public void onChatPacket(S02ChatReceivedEvent event) {
        String msg = event.getUnformattedMessage();

        if (msg.equals("[BOSS] Maxor: WELL! WELL! WELL! LOOK WHO'S HERE!")) {
            bossPhase = 1;
        }

        if (msg.equals("[BOSS] Storm: Pathetic Maxor, just like expected.")) {
            bossPhase = 2;
        }

        if (msg.equals("[BOSS] Goldor: Who dares trespass into my domain?")) {
            bossPhase = 3;
            p3TickTimer = 0;
        }

        if (msg.equals("The Core entrance is opening!")) {
            bossPhase = 4;

            p3TickTimer = 0;
            PlayerUtils.stopAlert();
        }

        if (msg.equals("[BOSS] Necron: You went further than any human before, congratulations.")) {
            bossPhase = 5;
        }

        if (msg.equals("[BOSS] Necron: All this, for nothing...")) {
            bossPhase = 6;
        }

        if (msg.equals("[BOSS] Wither King: You... again?")) {
            bossPhase = 7;

            firstDrag = true;

            if (splitDragons.getValue()) {
                playerClass = DungeonUtils.getPlayerClass();
            }
        }
    }

    private int getDragTimerX(String drag) {
        switch (drag) {
            case "Red":
            case "Green":
                return 26;
            case "Blue":
                return 85;
            case "Orange":
                return 86;
            case "Purple":
                return 56;
            default:
                return 0;
        }
    }

    private int getDragTimerZ(String drag) {
        switch (drag) {
            case "Blue":
            case "Green":
                return 94;
            case "Red":
                return 59;
            case "Orange":
                return 56;
            case "Purple":
                return 126;
            default:
                return 0;
        }
    }
}
