package com.mmdev.meowmayo.features.general.farming;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.DelayUtils;
import com.mmdev.meowmayo.utils.PlayerUtils;
import com.mmdev.meowmayo.utils.RenderShapeUtils;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PestAlert {
    private ToggleSetting pestAlert = (ToggleSetting) ConfigSettings.getSetting("Pest Alert");
    private ToggleSetting armorSwap = (ToggleSetting) ConfigSettings.getSetting("Pest Swap Alert");

    private static ToggleSetting pestWaypoint = (ToggleSetting) ConfigSettings.getSetting("Pest Warp Waypoint");

    private static String pestPlot = "";

    private static boolean cooldown = false;

    private static boolean drawing = false;
    private static double posX = 0.0, posY = 0.0, posZ = 0.0;

    Pattern pestSpawn = Pattern.compile("^YUCK! (\\d) ൠ Pest have spawned in Plot - (.+)!$");

    Pattern pestCooldownMS = Pattern.compile("Cooldown: (\\d+)m\\s*(\\d+)s");
    Pattern pestCooldownS = Pattern.compile("Cooldown: (\\d)+s");

    @SubscribeEvent
    public void onChatPacket(S02ChatReceivedEvent event) {
        String msg = event.getUnformattedMessage();

        Matcher psm = pestSpawn.matcher(msg);

        if (psm.matches()) {
            pestPlot = psm.group(2);

            if (pestAlert.getValue()) {
                PlayerUtils.makeTextAlert(psm.group(1) + " Pests Spawned!", "random.anvil_land", 1000);

                if (armorSwap.getValue()) {
                    DelayUtils.scheduleTask(() -> {
                        int cd = getCooldown();

                        if (cd > 0) {
                            DelayUtils.scheduleTask(() -> {
                                PlayerUtils.makeTextAlert("Swap Armor!", "random.anvil_use", 1000);
                            }, (cd - 3) * 1000L);
                        }
                    }, 3500);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!drawing) return;
        RenderShapeUtils.drawFilledBox(posX + 0.5, posY + 0.5, posZ + 0.5, 1, 1, 1, 1F, 0F, 1F, 1F, true, event.partialTicks);
    }

    public static void pestWarp() {
        if (cooldown) return;
        if (pestPlot.isEmpty()) return;

        cooldown = true;

        if (pestWaypoint.getValue()) {
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

            if (player != null) {
                posX = player.posX;
                posY = player.posY;
                posZ = player.posZ;

                drawing = true;

                DelayUtils.scheduleTask(() -> {
                    drawing = false;
                }, 30000);
            }
        }

        ChatUtils.command("plottp " + pestPlot);

        pestPlot = "";

        DelayUtils.scheduleTask(() -> {
            cooldown = false;
        }, 5000);
    }



    private int getCooldown() {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc == null || mc.thePlayer.sendQueue == null) return -1;

        Collection<NetworkPlayerInfo> tabList = mc.thePlayer.sendQueue.getPlayerInfoMap();

        for (NetworkPlayerInfo networkPlayerInfo : tabList) {
            if (networkPlayerInfo.getDisplayName() != null) {
                String line = networkPlayerInfo.getDisplayName().getUnformattedText().trim();
                if (line.startsWith("Cooldown:")) {
                    Matcher msM = pestCooldownMS.matcher(line);
                    if (msM.matches()) {
                        return (Integer.parseInt(msM.group(1)) * 60) + Integer.parseInt(msM.group(2));
                    }

                    Matcher sM = pestCooldownS.matcher(line);
                    if (sM.matches()) {
                        return Integer.parseInt(sM.group(1));
                    }
                }
            }
        }
        return -1;
    }
}
