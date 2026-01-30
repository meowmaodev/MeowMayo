package com.mmdev.meowmayo.features.kuudra;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.FloatSliderSetting;
import com.mmdev.meowmayo.config.settings.TextSetting;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.features.kuudra.tracker.KuudraTracker;
import com.mmdev.meowmayo.utils.KuudraUtils;
import com.mmdev.meowmayo.utils.PlayerUtils;
import com.mmdev.meowmayo.utils.RenderShapeUtils;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import com.mmdev.meowmayo.utils.events.S32ReceivedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StunFeatures {
    private ToggleSetting stunPing = (ToggleSetting) ConfigSettings.getSetting("Stun Ping");
    private FloatSliderSetting stunPingHp = (FloatSliderSetting) ConfigSettings.getSetting("Stun Ping HP");
    private TextSetting stunMessage = (TextSetting) ConfigSettings.getSetting("Stun Ping Message");

    private ToggleSetting stunWaypoint = (ToggleSetting) ConfigSettings.getSetting("Stun Waypoint");

    private boolean pinged = false;

    private boolean cannoning = false;
    private double xRender = 0.0;
    private double yRender = 0.0;
    private double zRender = 0.0;

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        cannoning = false;
        pinged = false;
    }


    @SubscribeEvent
    public void onServerTick(S32ReceivedEvent event) {
        if (KuudraTracker.getPhase() != 3) return;

        if (cannoning) {
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            if (player.posX == -161 && player.posZ == -186) cannoning = false;

            xRender = player.posX - 3.5;
            yRender = player.posY - 8.9;
            zRender = player.posZ + 8.5;
        }

        if (stunPing.getValue() && KuudraTracker.getTier().equals("Infernal")) {
            if (KuudraUtils.getKuudra() == null) {
                return;
            }

            float kuudraHp = KuudraUtils.getKuudra().getHealth();

//            ChatUtils.system("Kuudra HP: " + kuudraHp + " | VS | " + stunPingHp.getValue()*1000);

            if (kuudraHp < (stunPingHp.getValue()*1000) && !pinged) {
                PlayerUtils.makeTextAlert(stunMessage.getValue(), "random.anvil_land", 1000);
                pinged = true;
            }
        }
    }

    @SubscribeEvent
    public void onChatMessage(S02ChatReceivedEvent event) {
        String msg = event.getUnformattedMessage();

        if (msg.equals("You purchased Human Cannonball!")) cannoning = true;
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (cannoning && KuudraTracker.getPhase() == 3 && stunWaypoint.getValue()) {
            RenderShapeUtils.drawFilledBox(
                    xRender, yRender, zRender,
                    0.4, 0.4, 0.4,
                    0f, 1f, 0f, 1f,
                    true,
                    event.partialTicks
            );
        }
    }
}
