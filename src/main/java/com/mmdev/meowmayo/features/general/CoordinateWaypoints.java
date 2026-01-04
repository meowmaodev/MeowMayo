package com.mmdev.meowmayo.features.general;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.IntSliderSetting;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.utils.DelayUtils;
import com.mmdev.meowmayo.utils.RenderShapeUtils;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoordinateWaypoints {
    private ToggleSetting partyWaypoint = (ToggleSetting) ConfigSettings.getSetting("Party Chat Coordinate Waypoints");
    private IntSliderSetting pWaypTime = (IntSliderSetting) ConfigSettings.getSetting("Party Chat Coordinate Waypoints Duration");

    Pattern coordinate = Pattern.compile("^Party > (.+): X: (-?\\d+), Y: (-?\\d+), Z: (-?\\d+).*$", Pattern.CASE_INSENSITIVE);

    public static class waypoint {
        String label;
        int x, y, z;

        public waypoint(String label, int x, int y, int z) {
            this.label = label;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public Queue<waypoint> waypoints = new LinkedList<waypoint>();

    @SubscribeEvent
    public void onChatMessage(S02ChatReceivedEvent event) {
        String message = event.getUnformattedMessage();

        if (partyWaypoint.getValue()) {
            Matcher matcher = coordinate.matcher(message);

            if (matcher.matches()) {
                String name = matcher.group(1);
                int x = Integer.parseInt(matcher.group(2));
                int y = Integer.parseInt(matcher.group(3));
                int z = Integer.parseInt(matcher.group(4));

                waypoints.add(new waypoint(name, x, y, z));

                DelayUtils.scheduleTask(() -> {
                    waypoints.remove();
                }, pWaypTime.getValue() * 1000);
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!partyWaypoint.getValue()) return;
        for (waypoint w: waypoints) {
            RenderShapeUtils.drawFilledBox(w.x + 0.5, w.y + 0.5, w.z + 0.5, 1, 1, 1, 0F, 1F, 1F, 1F, true, event.partialTicks);
            RenderShapeUtils.drawFloatingText(w.label, w.x + 0.5, w.y + 1.5, w.z + 0.5, 0.05F, event.partialTicks);
        }
    }
}
