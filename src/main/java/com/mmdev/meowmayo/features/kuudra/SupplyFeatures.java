package com.mmdev.meowmayo.features.kuudra;

import com.mmdev.meowmayo.config.settings.IntSliderSetting;
import com.mmdev.meowmayo.features.kuudra.tracker.KuudraTracker;
import com.mmdev.meowmayo.utils.SupplyUtils;
import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.config.settings.FloatSliderSetting;
import com.mmdev.meowmayo.utils.MathUtils;
import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.PlayerUtils;
import com.mmdev.meowmayo.utils.RenderShapeUtils;

import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import com.mmdev.meowmayo.utils.events.S32ReceivedEvent;
import com.mmdev.meowmayo.utils.events.S45ReceivedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class SupplyFeatures {

    private ToggleSetting waypoint1 = (ToggleSetting) ConfigSettings.getSetting("Pearl Waypoint");
    private ToggleSetting waypoint2 = (ToggleSetting) ConfigSettings.getSetting("Second Pearl Waypoint");
    private FloatSliderSetting waypointSize = (FloatSliderSetting) ConfigSettings.getSetting("Pearl Waypoint Size");
    private ToggleSetting noSupply = (ToggleSetting) ConfigSettings.getSetting("No Supply");
    private ToggleSetting pearlTimer = (ToggleSetting) ConfigSettings.getSetting("Pearl Timer");
    private IntSliderSetting talisman = (IntSliderSetting) ConfigSettings.getSetting("Kuudra Talisman Tier");

    private static SupplyUtils.CrateLocation preLocation;

    private static boolean first = false;
    private static boolean second = false;

    private static boolean pickingUp = false;
    private static boolean isPre = false;

    private static String missing = "";

    public static double[] firstAt = new double[]{0.0, 0.0, 0.0};
    public static double[] secondAt = new double[]{0.0, 0.0, 0.0};

    public static double[] firstUpTarget = new double[]{0.0, 0.0, 0.0};
    public static int firstUpTiming = 0;
    public static boolean firstUpFound = false;

    public static double[] firstFlatTarget = new double[]{0.0, 0.0, 0.0};
    public static int firstFlatTiming = 0;
    public static boolean firstFlatFound = false;

    public static double[] secondUpTarget = new double[]{0.0, 0.0, 0.0};
    public static int secondUpTiming = 0;
    public static boolean secondUpFound = false;

    public static double[] secondFlatTarget = new double[]{0.0, 0.0, 0.0};
    public static int secondFlatTiming = 0;
    public static boolean secondFlatFound = false;

    public static int time = 0;
    private static int ticks = 0;

    private static double[] playerPos = new double[]{0.0, 0.0, 0.0};

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) return;

        if (pickingUp && KuudraTracker.getPhase() == 1) {

            double X = Minecraft.getMinecraft().thePlayer.posX;
            double Y = Minecraft.getMinecraft().thePlayer.posY;
            double Z = Minecraft.getMinecraft().thePlayer.posZ;

            double dX = X - playerPos[0];
            double dY = Y - playerPos[1];
            double dZ = Z - playerPos[2];

            if (dX != 0 || dY != 0 || dZ != 0) {
                playerPos[0] = X;
                playerPos[1] = Y;
                playerPos[2] = Z;

                // the isPre flag is backwards in the ct version so its backwards here too ig...
                if (waypoint1.getValue()) {
                    if (firstAt[0] == 0.0) {
                        ChatUtils.system("An error has occurred determining your Supply Location.");
                        return;
                    }

                    SupplyUtils.PearlResult res = SupplyUtils.calculatePearl(firstAt);

                    double[] posUp = res.getUpLocation();
                    int timeUp = res.getUpTiming();

                    if (posUp[0] == 0.0) {
                        ChatUtils.system("An error has occurred calculating Upwards Pearl Trajectory. | Error: " + posUp[1]);
                    } else {
                        firstUpTarget = posUp;
                        firstUpTiming = timeUp;

                        firstUpFound = true;
                    }

                    double[] posFlat = res.getFlatLocation();
                    int timeFlat = res.getFlatTiming();

                    if (posFlat[0] == 0.0) {
                        ChatUtils.system("An error has occurred calculating Upwards Pearl Trajectory. | Error: " + posFlat[1]);
                    } else {
                        firstFlatTarget = posFlat;
                        firstFlatTiming = timeFlat;

                        firstFlatFound = true;
                    }
                }
                if (waypoint2.getValue() && isPre) {
                    if (secondAt[0] == 0.0) {
                        ChatUtils.system("An error has occured determining your Second Location.");
                        return;
                    }

                    SupplyUtils.PearlResult res = SupplyUtils.calculatePearl(secondAt);

                    double[] posUp = res.getUpLocation();
                    int timeUp = res.getUpTiming();

                    if (posUp[0] == 0.0) {
                        ChatUtils.system("An error has occurred calculating Upwards Pearl Trajectory. | Error: " + posUp[1]);
                    } else {
                        secondUpTarget = posUp;
                        secondUpTiming = timeUp;

                        secondUpFound = true;
                    }

                    double[] posFlat = res.getFlatLocation();
                    int timeFlat = res.getFlatTiming();

                    if (posFlat[0] == 0.0) {
                        ChatUtils.system("An error has occurred calculating Upwards Pearl Trajectory. | Error: " + posFlat[1]);
                    } else {
                        secondFlatTarget = posFlat;
                        secondFlatTiming = timeFlat;

                        secondFlatFound = true;
                    }
                }
            }
        }
    }

    private Pattern pattern = Pattern.compile("Party > .*: No (.+)!");
    @SubscribeEvent
    public void onChatPacket(S02ChatReceivedEvent event) {
        String msg = event.getUnformattedMessage();

        if (Minecraft.getMinecraft().thePlayer == null) return;

        if (msg.equals("[NPC] Elle: Head over to the main platform, I will join you when I get a bite!")) {
            preLocation = null;
            first = false;
            second = false;

            // Example coordinates for each pre-spot
            double[][] preSpots = {
                    {-67.5, 77, -122.5}, // tri
                    {-142.5, 77, -151}, // x
                    {-65.5, 76, -87.0}, // =
                    {-113.5, 77, -68.5} // slash
            };

            double px = Minecraft.getMinecraft().thePlayer.posX;
            double py = Minecraft.getMinecraft().thePlayer.posY;
            double pz = Minecraft.getMinecraft().thePlayer.posZ;

            int closestIndex = 0;
            double closestDistSq = Double.MAX_VALUE;

            for (int i = 0; i < preSpots.length; i++) {
                double dx = px - preSpots[i][0];
                double dy = py - preSpots[i][1];
                double dz = pz - preSpots[i][2];

                double distSq = dx*dx + dy*dy + dz*dz; // squared distance is enough for comparison

                if (distSq < closestDistSq) {
                    closestDistSq = distSq;
                    closestIndex = i;
                }
            }

            switch (closestIndex) {
                case 0:
                    preLocation = SupplyUtils.CrateLocation.TRIANGLE;
                    break;
                case 1:
                    preLocation = SupplyUtils.CrateLocation.X;
                    break;
                case 2:
                    preLocation = SupplyUtils.CrateLocation.EQUALS;
                    break;
                case 3:
                    preLocation = SupplyUtils.CrateLocation.SLASH;
                    break;
            }

        }

        if (msg.equals("[NPC] Elle: Not again!")) {
            if (preLocation == null) return;

            List<double[]> crates = new ArrayList<>();
            for (Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) { // i probably took this recognition from someone!!!
                if (entity instanceof EntityGiantZombie) {
                    EntityGiantZombie giant = (EntityGiantZombie) entity;

                    if (giant.getHeldItem() != null && giant.getHeldItem().toString().equals("1xitem.skull@3")) {
                        float yaw = giant.rotationYaw;
                        double x = giant.posX + (3.7 * Math.cos(Math.toRadians(yaw + 130)));
                        double z = giant.posZ + (3.7 * Math.sin(Math.toRadians(yaw + 130)));

                        crates.add(new double[]{x, 75.0, z});
                    }
                }
            }

//            ChatUtils.system("Detected " + crates.size() + " crates!");

            for (double[] crate : crates) {
                if (MathUtils.get3dDistance(preLocation.getLocation(), crate) < 18.0) {
                    first = true;
                }
                switch (preLocation) {
                    case TRIANGLE:
                        if (MathUtils.get3dDistance(SupplyUtils.CrateLocation.TRIANGLE.getLocation(), crate) < 18) second = true;
                        break;
                    case X:
                        if (MathUtils.get3dDistance(SupplyUtils.CrateLocation.X.getLocation(), crate) < 18) second = true;
                        break;
                    case EQUALS:
                        if (MathUtils.get3dDistance(SupplyUtils.CrateLocation.EQUALS.getLocation(), crate) < 18) second = true;
                        break;
                    case SLASH:
                        if (MathUtils.get3dDistance(SupplyUtils.CrateLocation.SLASH.getLocation(), crate) < 18) second = true;
                        break;
                }
                if (first && second) break;
            }

            if ((!first) && noSupply.getValue()) {
                ChatUtils.partyChat("No " + preLocation.getName() + "!");
            } else if ((!second) && noSupply.getValue()) {
                switch (preLocation) {
                    case TRIANGLE:
                        ChatUtils.partyChat("No Shop!");
                        break;
                    case X:
                        ChatUtils.partyChat("No xCannon!");
                        break;
                    case SLASH:
                        ChatUtils.partyChat("No Square!");
                        break;
                }
            }
        }

        Matcher matcher = pattern.matcher(msg);
        if (matcher.matches()) {
            missing = matcher.group(1).toLowerCase();
            if (!noSupply.getValue()) return;
            PlayerUtils.makeTitle("Missing: " + missing);
        }

        if (msg.equals("You moved and the Chest slipped out of your hands!")) endGrab();
        if (msg.equals("[NPC] Elle: OMG! Great work collecting my supplies!")) {
            missing = "";
            endGrab();
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (pickingUp && KuudraTracker.getPhase() == 1 && waypoint1.getValue()) {
            double size = ((double) waypointSize.getValue()) / 100;
            if (firstUpFound) {
                RenderShapeUtils.drawBox(
                        firstUpTarget[0], firstUpTarget[1], firstUpTarget[2],
                        size, size, size,
                        (time > firstUpTiming ? 1f : 0f), (time > firstUpTiming ? 0f : 1f), 0f, 1f,
                        true,
                        event.partialTicks
                );
                if (pearlTimer.getValue()) {
                    RenderShapeUtils.drawFloatingText(
                            (time > firstUpTiming) ? "" + (firstUpTiming - time) : "THROW NOW!",
                            firstUpTarget[0], firstUpTarget[1] + 0.85, firstUpTarget[2],
                            0.05f, event.partialTicks
                            );
                }
            }
            if (firstFlatFound) {
                RenderShapeUtils.drawBox(
                        firstFlatTarget[0], firstFlatTarget[1], firstFlatTarget[2],
                        size, size, size,
                        (time > firstFlatTiming ? 1f : 0f), (time > firstFlatTiming ? 0f : 1f), 0f, 1f,
                        true,
                        event.partialTicks
                );
                if (pearlTimer.getValue()) {
                    RenderShapeUtils.drawFloatingText(
                            (time > firstFlatTiming) ? "" + (firstFlatTiming - time) : "THROW NOW!",
                            firstFlatTarget[0], firstFlatTarget[1] + 0.85, firstFlatTarget[2],
                            0.05f, event.partialTicks
                    );
                }
            }
            if (waypoint2.getValue()) {
                if (secondUpFound) {
                    RenderShapeUtils.drawBox(
                            secondUpTarget[0], secondUpTarget[1], secondUpTarget[2],
                            size, size, size,
                            (time > secondUpTiming - 6 ? 1f : 0f), 0f, 1f, 1f,
                            true,
                            event.partialTicks
                    );
                    if (pearlTimer.getValue()) {
                        RenderShapeUtils.drawFloatingText(
                                (time > secondUpTiming) ? "" + (secondUpTiming - time) : "THROW NOW!",
                                secondUpTarget[0], secondUpTarget[1] + 0.85, secondUpTarget[2],
                                0.05f, event.partialTicks
                        );
                    }
                }
                if (secondFlatFound) {
                    RenderShapeUtils.drawBox(
                            secondFlatTarget[0], secondFlatTarget[1], secondFlatTarget[2],
                            size, size, size,
                            (time > secondFlatTiming - 6 ? 1f : 0f), 0f, 1f, 1f,
                            true,
                            event.partialTicks
                    );
                    if (pearlTimer.getValue()) {
                        RenderShapeUtils.drawFloatingText(
                                (time > secondFlatTiming) ? "" + (secondFlatTiming - time) : "THROW NOW!",
                                secondFlatTarget[0], secondFlatTarget[1] + 0.85, secondFlatTarget[2],
                                0.05f, event.partialTicks
                        );
                    }
                }
            }
        }
    }

    Pattern progressBar = Pattern.compile("\\[\\|+]\\s?(\\d{1,3})%");
    @SubscribeEvent
    public void onPacketReceived(S45ReceivedEvent event) {
        if (!(waypoint1.getValue() || pearlTimer.getValue()) || KuudraTracker.getPhase() != 1) return;
        String title = event.getUnformattedTitle();

        Matcher matcher = progressBar.matcher(title);

        if (matcher.find()) {
            String numberStr = matcher.group(1);
            int percentage = Integer.parseInt(numberStr);

            if (percentage == 0 && !pickingUp) {
                isPre = false;
                if (waypoint1.getValue()) {
                    SupplyUtils.PreLocation pl = SupplyUtils.getPreLocation(missing);
                    firstAt = pl == null ? new double[]{0.0, 0.0, 0.0} : pl.getLocation();
                    if (waypoint2.getValue() && KuudraTracker.getUnformattedLagSplitTime() < 7.0) {
                        isPre = true;
                        SupplyUtils.SecondLocation sl = SupplyUtils.getSecondLocation();
                        secondAt = sl == null ? new double[]{0.0, 0.0, 0.0} : sl.getLocation();
                    }
                }
                pickingUp = true;
            }
            if (percentage == 100 && pickingUp) {
                endGrab();
            }
        }
    }

    @SubscribeEvent
    public void onServerTick(S32ReceivedEvent event) {
        if (!pickingUp) return;

        ticks++;

        time = SupplyUtils.getSupplyTime(talisman.getValue(), KuudraTracker.getTier()) - ticks;

        if (ticks > SupplyUtils.getSupplyTime(talisman.getValue(), KuudraTracker.getTier()) + 5) {
            endGrab();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        missing = "";
        endGrab();
    }

    private static void endGrab() {
        isPre = false;

        playerPos[0] = 0.0;
        playerPos[1] = 0.0;
        playerPos[2] = 0.0;

        firstUpTarget = new double[]{0.0, 0.0, 0.0};
        firstUpTiming = 0;

        secondUpTarget = new double[]{0.0, 0.0, 0.0};
        secondUpTiming = 0;

        firstAt = new double[]{0.0, 0.0, 0.0};
        secondAt = new double[]{0.0, 0.0, 0.0};

        firstUpFound = false; // apparently this fixed something in the other version?
        firstFlatFound = false;
        secondUpFound = false;
        secondFlatFound = false;

        endTimer();
    }

    private static void endTimer() {
        pickingUp = false;
        ticks = 0;
    }
}