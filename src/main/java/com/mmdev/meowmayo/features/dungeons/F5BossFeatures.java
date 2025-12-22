package com.mmdev.meowmayo.features.dungeons;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.features.dungeons.tracker.DungeonTracker;
import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.RenderShapeUtils;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import com.mmdev.meowmayo.utils.events.S32ReceivedEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

public class F5BossFeatures {
    private ToggleSetting highlightLivid = (ToggleSetting) ConfigSettings.getSetting("Livid Solver");

    private static boolean active = false;

    private EntityArmorStand livid = null;
    // this should be changed to be off the player models themsevles ill do that later ig


    private static HashMap<Integer, String> names = new HashMap<>();

    static {
        names.put(0, "§f");
        names.put(2, "§d");
        names.put(4, "§e");
        names.put(5, "§a");
        names.put(8, "§7");
        names.put(10, "§5");
        names.put(11, "§9");
        names.put(13, "§2");
        names.put(14, "§c");
    }

    private static BlockPos blockPos = new BlockPos(5, 108, 59);
    private static int blockState = -1;
    // 4 - yellow - &e
    // 11 - blue - &9
    // 13 - green - &2
    // 5 - lime - &a
    // 14 - red - &c
    // 10 - purple - &5
    // 2 - magenta - &d
    // 8 - gray - &7
    // 0 - white - &f

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        active = false;
        blockState = -1;
    }

    @SubscribeEvent
    public void onChatPacket(S02ChatReceivedEvent event) {
        String msg = event.getUnformattedMessage();

        if (!(DungeonTracker.getTier().equals("M5") || DungeonTracker.getTier().equals("F5"))) return;

        if (DungeonTracker.getPhase() == 3) {
            if (msg.equalsIgnoreCase("[BOSS] Livid: I respect you for making it to here, but I'll be your undoing.")) {
                ChatUtils.system("Searching for Livid!");
                active = true;
            }
        }
    }

    @SubscribeEvent
    public void onServerTick(S32ReceivedEvent event) {
        if (active) {
            WorldClient world = Minecraft.getMinecraft().theWorld;

            Block block = world.getBlockState(blockPos).getBlock();

            int nbs = block.getMetaFromState(Minecraft.getMinecraft().theWorld.getBlockState(blockPos));
            if (nbs != blockState) {
                blockState = nbs;

                ChatUtils.system("Found " + world.getLoadedEntityList().size() + " entities");

                for (Entity e : world.getLoadedEntityList()) {
                    if (e instanceof EntityArmorStand) {
                        EntityArmorStand entity = (EntityArmorStand) e;
                        String eName = entity.getName();
                        if (eName == null) continue;
                        if (eName.startsWith(names.get(blockState)) && eName.toLowerCase().contains("livid")) {
                            livid = entity;
                            ChatUtils.system("Found livid");
                            return;
                        }
                    }
                }

                livid = null;
            }

            if (livid == null) {
                for (Entity e : world.getLoadedEntityList()) {
                    if (e instanceof EntityArmorStand) {
                        EntityArmorStand entity = (EntityArmorStand) e;
                        String eName = entity.getName();
                        if (eName == null) continue;
                        if (eName.startsWith(names.get(blockState)) && eName.toLowerCase().contains("livid")) {
                            livid = entity;
                            ChatUtils.system("Found livid");
                            return;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!highlightLivid.getValue()) return;
        if (!active) return;
        if (livid == null) return;
        RenderShapeUtils.drawBox(livid.posX, livid.posY, livid.posZ, 0.8, 2.0, 0.8, 0F, 1F, 1F, 1F, false, event.partialTicks);
    }


    public static void signalEnd() {
        active = false;
    }
}
