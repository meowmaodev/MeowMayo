package com.mmdev.meowmayo.features.dungeons;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.features.dungeons.tracker.DungeonTracker;
import com.mmdev.meowmayo.utils.*;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import com.mmdev.meowmayo.utils.events.S2AParticleReceivedEvent;
import com.mmdev.meowmayo.utils.events.S32ReceivedEvent;
import com.mmdev.meowmayo.utils.tracker.Events;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class F7BossFeatures {
    private ToggleSetting p3Death = (ToggleSetting) ConfigSettings.getSetting("Phase 3 Death Warning");
    private ToggleSetting dragonSpawnTitle = (ToggleSetting) ConfigSettings.getSetting("Dragon Spawn Title");
    private static ToggleSetting splitDragons = (ToggleSetting) ConfigSettings.getSetting("Split Dragon");
    private ToggleSetting dragonSpawnTimer = (ToggleSetting) ConfigSettings.getSetting("Dragon Spawn Timer");
    private ToggleSetting termBreakdown = (ToggleSetting) ConfigSettings.getSetting("Terminals Breakdown");
    private ToggleSetting pre4Remind = (ToggleSetting) ConfigSettings.getSetting("Pre4 Reminder");

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

    private static int termPhase = 0;
    private static boolean termsDone = false;
    private static boolean gateBoom = false;

    private static boolean pre4Done = false;

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
        if (DungeonTracker.getPhase() == 8 && DungeonTracker.getTier().equals("M7")) {
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
        if (DungeonTracker.getTier().equals("M7") || DungeonTracker.getTier().equals("F7")) {
            if (p3Death.getValue()) {
                if (DungeonTracker.getPhase() == 5) {
                    if (p3TickTimer == 45) {
                        PlayerUtils.makeTextAlert("DAMAGE TICK INCOMING!", "note.pling");
                    }
                    if (p3TickTimer > 45) {
                        Minecraft.getMinecraft().thePlayer.playSound("note.pling", 1.0F, 1.0F);
                    }
                    if (p3TickTimer >= 60) { // 40t cycle
                        PlayerUtils.stopAlert();
                        PlayerUtils.makeTextTitle("DAMAGE TICK PASSED!", 500);
                        p3TickTimer = 0;
                    }
                    p3TickTimer++;
                }
            }

            if (DungeonTracker.getPhase() == 8) {
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
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        PlayerUtils.stopAlert();
    }

    Pattern device = Pattern.compile("^(.{2,16}) completed a device! \\((\\d+)/(\\d+)\\)$");
    Pattern terminal = Pattern.compile("^(.{2,16}) activated a terminal! \\((\\d+)/(\\d+)\\)$");
    Pattern lever = Pattern.compile("^(.{2,16}) activated a lever! \\((\\d+)/(\\d+)\\)$");

    @SubscribeEvent
    public void onChatPacket(S02ChatReceivedEvent event) {
        String msg = event.getUnformattedMessage();

        if (!termBreakdown.getValue()) return;

        // this detects the actual terminal phases
        if ((DungeonTracker.getTier().equals("M7") || DungeonTracker.getTier().equals("F7")) && DungeonTracker.getPhase() == 5) { // make this f7 too when thats added to tracker
            if (msg.equals("The gate has been destroyed!")) {
                gateBoom = true;
            }

            Matcher mDev = device.matcher(msg);
            if (mDev.matches()) {
                int a = Integer.parseInt(mDev.group(2));
                int b = Integer.parseInt(mDev.group(3));

                if (a == b) {
                    termsDone = true;
                }

                EntityPlayer termFinisher = WorldUtils.getPlayerByName(mDev.group(1));

                if (termFinisher != null) {
                    if (termFinisher.posZ < 98 && termFinisher.posZ > 90 && termFinisher.posX > 104) {
                        ChatUtils.system(mDev.group(1) + " has completed Simon Says!");
                    }
                    if (termFinisher.posX > 55 && termFinisher.posX < 64 && termFinisher.posZ > 135) {
                        ChatUtils.system(mDev.group(1) + " has completed Lights Device!");
                    }
                    if (termFinisher.posZ < 82 && termFinisher.posZ > 71 && termFinisher.posX < 6) {
                        ChatUtils.system(mDev.group(1) + " has completed Arrow Align Device!");
                    }
                    if (termFinisher.posX > 60 && termFinisher.posX < 64 && termFinisher.posZ < 38) {
                        ChatUtils.system(mDev.group(1) + " has completed Arrow Shooter Device!");
                        pre4Done = true;
                    }
                }
            }

            Matcher mLev = lever.matcher(msg);
            if (mLev.matches()) {
                int a = Integer.parseInt(mLev.group(2));
                int b = Integer.parseInt(mLev.group(3));

                if (a == b) {
                    termsDone = true;
                }
            }

            Matcher mTerm = terminal.matcher(msg);
            if (mTerm.matches()) {
                int a = Integer.parseInt(mTerm.group(2));
                int b = Integer.parseInt(mTerm.group(3));

                if (a == b) {
                    termsDone = true;
                }
            }

            if (termsDone && gateBoom) {
                termsDone = false;
                gateBoom = false;
                termPhase++;
                ChatUtils.system("Entering Term Phase: " + termPhase);
                if (termPhase == 4 && pre4Remind.getValue()) {
                    if (!pre4Done) {
                        if (playerClass == 'B') {
                            ChatUtils.system("PRE4 IS NOT DONE");
                        }
                    } else {
                        ChatUtils.system("PRE4 IS DONE");
                    }
                }
            }
        }
    }

    public static void signal(Events event) {
        switch (event) {
            case STORM_DEAD:
                pre4Done = false;
                gateBoom = false;
                termsDone = false;
                termPhase = 1;
                break;
            case GOLDOR_DEAD:
                pre4Done = false;
                gateBoom = false;
                termsDone = false;
                termPhase = 0;

                p3TickTimer = 0;
                PlayerUtils.stopAlert();
                break;
            case RELICS_DOWN:
                firstDrag = true;
                playerClass = DungeonUtils.getPlayerClass();
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
