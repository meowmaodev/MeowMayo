package com.mmdev.meowmayo.features.kuudra.tracker;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.HudManager;
import com.mmdev.meowmayo.config.settings.HudElementSetting;
import com.mmdev.meowmayo.config.settings.IntSliderSetting;
import com.mmdev.meowmayo.config.settings.ToggleSetting;
import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.DelayUtils;
import com.mmdev.meowmayo.utils.PartyUtils;
import com.mmdev.meowmayo.utils.ScoreboardUtils;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import com.mmdev.meowmayo.utils.tracker.Events;
import com.mmdev.meowmayo.utils.tracker.Tiers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KuudraTracker {
    private ToggleSetting autoRequeue = (ToggleSetting) ConfigSettings.getSetting("Kuudra Auto Requeue");
    private IntSliderSetting autoRequeueDelay = (IntSliderSetting) ConfigSettings.getSetting("Kuudra Requeue Delay");

    private ToggleSetting kuudraTrack = (ToggleSetting) ConfigSettings.getSetting("Average Kuudra Run Time Tracker");
    private ToggleSetting resetOnParty = (ToggleSetting) ConfigSettings.getSetting("Reset Tracker on Party Change");
    private IntSliderSetting preLeeway = (IntSliderSetting) ConfigSettings.getSetting("Pre Tracker Leeway");
    private ToggleSetting missedPre = (ToggleSetting) ConfigSettings.getSetting("Pre Tracker");
    private ToggleSetting supplyInfoMessage = (ToggleSetting) ConfigSettings.getSetting("Supplies Overview");
    private ToggleSetting freshInfoMessage = (ToggleSetting) ConfigSettings.getSetting("Fresh Overview");
    private ToggleSetting lagMessage = (ToggleSetting) ConfigSettings.getSetting("Lag Timing");

    private ToggleSetting splits = (ToggleSetting) ConfigSettings.getSetting("Kuudra Splits");

    private HudElementSetting splitLocation = HudManager.getLocation("Kuudra Time Splits");

    private final List<String> cachedSplits = new ArrayList<>();
    private String activeSplitLine = null;

    private static Tiers currentTier;
    private static int currentPhase = -1;

    private static KuudraTimer kt;
    private KuudraListener kl;

    private long startTime = 0;

    private List<Long> rt = new ArrayList<>();
    private List<Long> lt = new ArrayList<>();

    private List<String[]> supplies = new ArrayList<>();
    private static Set<String> grabbed = new HashSet<>();

    private static List<String[]> freshes = new ArrayList<>();

    private boolean runPrimed = false;

    private boolean runActive = false;

    public KuudraTracker(KuudraTimer kt, KuudraListener kl) {
        KuudraTracker.kt = kt;
        this.kl = kl;
    }

    public static String getTier() {
        return currentTier.getTierName();
    }

    public static int getPhase() {
        return currentPhase;
    }

    public static String getFormattedSplitTime() {
        return ChatUtils.formatTime(kt.getCurrentSplit() / 1000.0);
    }

    public static double getUnformattedLagSplitTime() {
        return (kt.getCurrentLaglessSplit() / 1000.0);
    }

    Pattern location = Pattern.compile("^⏣ Kuudra's Hollow \\((T\\d)\\)$");


    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !runActive || !splits.getValue()) return;

        activeSplitLine = currentTier.getPhases().get(currentPhase).getName() + ": " +
                ChatUtils.formatTime(kt.getCurrentSplit()/1000.0) + " (" +
                ChatUtils.formatTime(kt.getCurrentLaglessSplit()/1000.0) + ")";
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        if (!runActive || splitLocation == null || activeSplitLine == null) return;

        Minecraft mc = Minecraft.getMinecraft();

        GlStateManager.pushMatrix();
        GlStateManager.translate(splitLocation.getX(), splitLocation.getY(), 0);
        GlStateManager.scale(splitLocation.getScale(), splitLocation.getScale(), 1.0f);

        int yOffset = 0;
        int fontHeight = mc.fontRendererObj.FONT_HEIGHT;

        mc.fontRendererObj.drawStringWithShadow(getTier() + " Run Splits:", 0, yOffset, 0xFFFFFF);
        yOffset += fontHeight;

        for (String line : cachedSplits) {
            mc.fontRendererObj.drawStringWithShadow(line, 0, yOffset, 0xFFFFFF);
            yOffset += fontHeight;
        }

        mc.fontRendererObj.drawStringWithShadow(activeSplitLine, 0, yOffset, 0xFFFFFF);

        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void onChat(S02ChatReceivedEvent event) {
        String msg = event.getUnformattedMessage();
        if (msg.equals(Minecraft.getMinecraft().thePlayer.getName() + " is now ready!")) {
            if (!runPrimed) {
                Matcher matcher = location.matcher(ScoreboardUtils.getLocation());

                if (matcher.matches()) {
                    switch (matcher.group(1)) {
                        case "T1":
                            setCurrentTier(KuudraTiers.basic);
                            prepRun();
                            break;
                        case "T2":
                            setCurrentTier(KuudraTiers.hot);
                            prepRun();
                            break;
                        case "T3":
                            setCurrentTier(KuudraTiers.burning);
                            prepRun();
                            break;
                        case "T4":
                            setCurrentTier(KuudraTiers.fiery);
                            prepRun();
                            break;
                        case "T5":
                            setCurrentTier(KuudraTiers.infernal);
                            prepRun();
                            break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        endRun();
    }

    public void startRun() {
        currentPhase = 0;

        int numPhases = currentTier.getTierCount();
        rt.clear();
        lt.clear();

        for (int i = 0; i < numPhases; i++) {
            rt.add(-1L);
            lt.add(0L);
        }

        kt.enable();

        startTime = System.currentTimeMillis();

        this.runActive = true;
        this.runPrimed = false;
    }

    public void endRun() {
        if (runActive || runPrimed) {
        currentTier.getPhases().get(currentPhase).exitPhase();
        }

        freshes.clear();
        supplies.clear();

        runActive = false;
        runPrimed = false;
        currentPhase = -1;
        startTime = 0;
        rt.clear();
        lt.clear();
    }

    public void setCurrentTier(Tiers tier) {
        if (currentTier == null) {
            currentTier = tier;
        }
        if (!tier.getTierName().equals(currentTier.getTierName())) {
            currentTier = tier;
        }
    }

    public void signal(Events event) {
        if (!runActive && !runPrimed) return;

        switch (event) {
            case RUN_START:
                startRun();
                break;
            case SUPPLIES_START:
                advancePhase();
                if (kuudraTrack.getValue())
                    ChatUtils.system("Supply Spawn Took: " + ChatUtils.formatTime((rt.get(0) - startTime) / 1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(0)) / 1000.0) + ")");
                break;
            case BUILD_START:
                advancePhase();

                Set<String> temp = PartyUtils.getParty();

                if (grabbed.size() < temp.size()) {
                    temp.removeAll(grabbed);
                }

                if (kuudraTrack.getValue())
                    ChatUtils.system("Supplies Phase Took: " + ChatUtils.formatTime((rt.get(1) - rt.get(0)) / 1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(1)) / 1000.0) + ")");

                if (missedPre.getValue()) {
                    String preMessage = "§fMissed Pres:";
                    for (String pm : grabbed) {
                        preMessage += ("\n§b§l" + pm + " §r§a§lgrabbed §rtheir pre");
                    }
                    for (String pm : temp) {
                        preMessage += ("\n§b§l" + pm + " §r§c§lmissed §rtheir pre");
                    }
                    ChatUtils.system(preMessage);
                }

                if (supplyInfoMessage.getValue()) {
                    String supplyMessage = "§fSupplies Overview:";
                    for (String[] ar : supplies) {
                        supplyMessage += ("\n§b§l" + ar[0] + " §rrecovered a supply at §a§l" + ar[1]);
                    }
                    ChatUtils.system(supplyMessage);
                }
                break;
            case CANNON_START:
                advancePhase();

                if (kuudraTrack.getValue())
                    ChatUtils.system("Build Phase Took: " + ChatUtils.formatTime((rt.get(2) - rt.get(1)) / 1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(2)) / 1000.0) + ")");

                if (freshInfoMessage.getValue()) {
                    String freshMessage = ("§a§l" + freshes.size() + " §r§ffreshes detected");
                    for (String[] fresh : freshes) {
                        freshMessage += ("\n§b§l" + fresh[0] + " §rFreshed at §a§l" + fresh[1]);
                    }
                    ChatUtils.system(freshMessage);
                }
                break;
            case SKIP_DONE:
                advancePhase();

                if (kuudraTrack.getValue())
                    ChatUtils.system("DPS Phase Took: " + ChatUtils.formatTime((rt.get(3) - rt.get(2)) / 1000.0) + " (Lag: " + ChatUtils.formatTime((lt.get(3)) / 1000.0) + ")");
                break;

        }
    }

    public void signal(Events event, Matcher matcher) {
        if (!runActive && !runPrimed) return;

        switch (event) {
            case NO_SUPPLY:
                String supply = matcher.group(2).toLowerCase();
                String player = ChatUtils.stripRank(matcher.group(1));

                if (supply.equals("x") || supply.equals("triangle") || supply.equals("slash") || supply.equals("equals")) {
                    grabbed.add(player);
                }
                break;
            case SUPPLY_GRABBED:
                String playerName = ChatUtils.stripRank(matcher.group(1));
                supplies.add(new String[]{playerName, ChatUtils.formatTime(kt.getCurrentSplit() / 1000.0)});
                if ((kt.getCurrentLaglessSplit() / 1000.0) < (8.0 + preLeeway.getValue())) {
                    grabbed.add(playerName);
                }
                break;
            case FRESH_PROC:
                String fresher = ChatUtils.stripRank(matcher.group(1));
                freshes.add(new String[]{fresher, ChatUtils.formatTime(kt.getCurrentSplit() / 1000.0)});
                break;
            case RUN_END_SUCCESS:
                kt.split(rt, lt, currentPhase);

                switch (currentTier.getTierName()) {
                    case "Basic":
                        KuudraStats.sessionBasicStats.addSuccess(startTime, rt, lt, freshes, supplies);
                        KuudraStats.globalBasicStats.addSuccess(startTime, rt, lt, freshes, supplies);
                        KuudraStats.saveConfig("Basic");

                        if (kuudraTrack.getValue()) {
                            long totalLag = 0;
                            for (long lag : lt) {
                                totalLag += lag;
                            }

                            double runTime = ((System.currentTimeMillis() - startTime) / 1000.0);

                            ChatUtils.system(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(runTime - (totalLag / 1000.0)));
                            ChatUtils.system("§fTotal Run Time: §a§l" + ChatUtils.formatTime(runTime) + "\n" +
                                    "§2||§r§f Crates took §a§l" + ChatUtils.formatTime((rt.get(0) - startTime) / 1000.0) + "§r§f to spawn\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(0) / 1000.0) + "§r§f to lag\n" +
                                    "§2||§r§f Supplies took §a§l" + ChatUtils.formatTime((rt.get(1) - rt.get(0)) / 1000.0) + "§r§f to fish up\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(1) / 1000.0) + "§r§f to lag\n" +
                                    "§2||§r§f Ballista took §a§l" + ChatUtils.formatTime((rt.get(2) - rt.get(1)) / 1000.0) + "§r§f to build\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(2) / 1000.0) + "§r§f to lag\n" +
                                    "§2||§r§f DPS took §a§l" + ChatUtils.formatTime((rt.get(3) - rt.get(2)) / 1000.0) + "§r§f to complete\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(3) / 1000.0) + "§r§f to lag\n"
                            );

                            if (lagMessage.getValue()) {
                                ChatUtils.partyChat(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(((System.currentTimeMillis() - startTime) / 1000.0) - (totalLag / 1000.0)));
                            }
                        }
                        break;
                    case "Hot":
                        KuudraStats.sessionHotStats.addSuccess(startTime, rt, lt, freshes, supplies);
                        KuudraStats.globalHotStats.addSuccess(startTime, rt, lt, freshes, supplies);
                        KuudraStats.saveConfig("Hot");

                        if (kuudraTrack.getValue()) {
                            long totalLag = 0;
                            for (long lag : lt) {
                                totalLag += lag;
                            }

                            double runTime = ((System.currentTimeMillis() - startTime) / 1000.0);

                            ChatUtils.system(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(runTime - (totalLag / 1000.0)));
                            ChatUtils.system("§fTotal Run Time: §a§l" + ChatUtils.formatTime(runTime) + "\n" +
                                    "§2||§r§f Crates took §a§l" + ChatUtils.formatTime((rt.get(0) - startTime) / 1000.0) + "§r§f to spawn\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(0) / 1000.0) + "§r§f to lag\n" +
                                    "§2||§r§f Supplies took §a§l" + ChatUtils.formatTime((rt.get(1) - rt.get(0)) / 1000.0) + "§r§f to fish up\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(1) / 1000.0) + "§r§f to lag\n" +
                                    "§2||§r§f Ballista took §a§l" + ChatUtils.formatTime((rt.get(2) - rt.get(1)) / 1000.0) + "§r§f to build\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(2) / 1000.0) + "§r§f to lag\n" +
                                    "§2||§r§f DPS took §a§l" + ChatUtils.formatTime((rt.get(3) - rt.get(2)) / 1000.0) + "§r§f to complete\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(3) / 1000.0) + "§r§f to lag\n"
                            );

                            if (lagMessage.getValue()) {
                                ChatUtils.partyChat(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(((System.currentTimeMillis() - startTime) / 1000.0) - (totalLag / 1000.0)));
                            }
                        }
                        break;
                    case "Burning":
                        KuudraStats.sessionBurningStats.addSuccess(startTime, rt, lt, freshes, supplies);
                        KuudraStats.globalBurningStats.addSuccess(startTime, rt, lt, freshes, supplies);
                        KuudraStats.saveConfig("Burning");

                        if (kuudraTrack.getValue()) {
                            long totalLag = 0;
                            for (long lag : lt) {
                                totalLag += lag;
                            }

                            double runTime = ((System.currentTimeMillis() - startTime) / 1000.0);

                            ChatUtils.system(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(runTime - (totalLag / 1000.0)));
                            ChatUtils.system("§fTotal Run Time: §a§l" + ChatUtils.formatTime(runTime) + "\n" +
                                    "§2||§r§f Crates took §a§l" + ChatUtils.formatTime((rt.get(0) - startTime) / 1000.0) + "§r§f to spawn\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(0) / 1000.0) + "§r§f to lag\n" +
                                    "§2||§r§f Supplies took §a§l" + ChatUtils.formatTime((rt.get(1) - rt.get(0)) / 1000.0) + "§r§f to fish up\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(1) / 1000.0) + "§r§f to lag\n" +
                                    "§2||§r§f Ballista took §a§l" + ChatUtils.formatTime((rt.get(2) - rt.get(1)) / 1000.0) + "§r§f to build\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(2) / 1000.0) + "§r§f to lag\n" +
                                    "§2||§r§f DPS took §a§l" + ChatUtils.formatTime((rt.get(3) - rt.get(2)) / 1000.0) + "§r§f to complete\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(3) / 1000.0) + "§r§f to lag\n"
                            );

                            if (lagMessage.getValue()) {
                                ChatUtils.partyChat(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(((System.currentTimeMillis() - startTime) / 1000.0) - (totalLag / 1000.0)));
                            }
                        }
                        break;
                    case "Fiery":
                        KuudraStats.sessionFieryStats.addSuccess(startTime, rt, lt, freshes, supplies);
                        KuudraStats.globalFieryStats.addSuccess(startTime, rt, lt, freshes, supplies);
                        KuudraStats.saveConfig("Fiery");

                        if (kuudraTrack.getValue()) {
                            long totalLag = 0;
                            for (long lag : lt) {
                                totalLag += lag;
                            }

                            double runTime = ((System.currentTimeMillis() - startTime) / 1000.0);

                            ChatUtils.system(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(runTime - (totalLag / 1000.0)));
                            ChatUtils.system("§fTotal Run Time: §a§l" + ChatUtils.formatTime(runTime) + "\n" +
                                    "§2||§r§f Crates took §a§l" + ChatUtils.formatTime((rt.get(0) - startTime) / 1000.0) + "§r§f to spawn\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(0) / 1000.0) + "§r§f to lag\n" +
                                    "§2||§r§f Supplies took §a§l" + ChatUtils.formatTime((rt.get(1) - rt.get(0)) / 1000.0) + "§r§f to fish up\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(1) / 1000.0) + "§r§f to lag\n" +
                                    "§2||§r§f Ballista took §a§l" + ChatUtils.formatTime((rt.get(2) - rt.get(1)) / 1000.0) + "§r§f to build\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(2) / 1000.0) + "§r§f to lag\n" +
                                    "§2||§r§f DPS took §a§l" + ChatUtils.formatTime((rt.get(3) - rt.get(2)) / 1000.0) + "§r§f to complete\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(3) / 1000.0) + "§r§f to lag\n"
                            );

                            if (lagMessage.getValue()) {
                                ChatUtils.partyChat(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(((System.currentTimeMillis() - startTime) / 1000.0) - (totalLag / 1000.0)));
                            }
                        }
                        break;
                    case "Infernal":
                        KuudraStats.sessionInfernalStats.addSuccess(startTime, rt, lt, freshes, supplies);
                        KuudraStats.globalInfernalStats.addSuccess(startTime, rt, lt, freshes, supplies);
                        KuudraStats.saveConfig("Infernal");

                        if (kuudraTrack.getValue()) {
                            long totalLag = 0;
                            for (long lag : lt) {
                                totalLag += lag;
                            }

                            double runTime = ((System.currentTimeMillis() - startTime) / 1000.0);

                            ChatUtils.system(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(runTime - (totalLag / 1000.0)));
                            ChatUtils.system("§fTotal Run Time: §a§l" + ChatUtils.formatTime(runTime) + "\n" +
                                    "§2||§r§f Crates took §a§l" + ChatUtils.formatTime((rt.get(0) - startTime) / 1000.0) + "§r§f to spawn\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(0) / 1000.0) + "§r§f to lag\n" +
                                    "§2||§r§f Supplies took §a§l" + ChatUtils.formatTime((rt.get(1) - rt.get(0)) / 1000.0) + "§r§f to fish up\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(1) / 1000.0) + "§r§f to lag\n" +
                                    "§2||§r§f Ballista took §a§l" + ChatUtils.formatTime((rt.get(2) - rt.get(1)) / 1000.0) + "§r§f to build\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(2) / 1000.0) + "§r§f to lag\n" +
                                    "§2||§r§f DPS took §a§l" + ChatUtils.formatTime((rt.get(3) - rt.get(2)) / 1000.0) + "§r§f to complete\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(3) / 1000.0) + "§r§f to lag\n" +
                                    "§2||§r§f Final Phase took §a§l" + ChatUtils.formatTime((rt.get(4) - rt.get(3)) / 1000.0) + "§r§f to kill\n" +
                                    "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lt.get(4) / 1000.0) + "§r§f to lag\n"
                            );

                            if (lagMessage.getValue()) {
                                ChatUtils.partyChat(ChatUtils.formatTime(totalLag/1000.0) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(((System.currentTimeMillis() - startTime) / 1000.0) - (totalLag / 1000.0)));
                            }
                        }
                        break;
                }

                if (autoRequeue.getValue() && PartyUtils.isLeader()) {
                    if (PartyUtils.getDowntimeFlag()) {
                        DelayUtils.scheduleTask(() -> ChatUtils.partyChat("Taking Downtime!"), 2500);
                        PartyUtils.useDowntimeFlag();
                    } else {
                        switch (currentTier.getTierName()) {
                            case "Basic":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon kuudra_basic"), autoRequeueDelay.getValue() * 1000);
                                break;
                            case "Hot":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon kuudra_hot"), autoRequeueDelay.getValue() * 1000);
                                break;
                            case "Burning":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon kuudra_burning"), autoRequeueDelay.getValue() * 1000);
                                break;
                            case "Fiery":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon kuudra_fiery"), autoRequeueDelay.getValue() * 1000);
                                break;
                            case "Infernal":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon kuudra_infernal"), autoRequeueDelay.getValue() * 1000);
                                break;
                        }
                    }
                }
                break;
            case RUN_END_FAILURE:
                switch (currentTier.getTierName()) {
                    case "Basic":
                        KuudraStats.sessionBasicStats.addFailure(startTime);
                        KuudraStats.globalBasicStats.addFailure(startTime);
                        break;
                    case "Hot":
                        KuudraStats.sessionHotStats.addFailure(startTime);
                        KuudraStats.globalHotStats.addFailure(startTime);
                        break;
                    case "Burning":
                        KuudraStats.sessionBurningStats.addFailure(startTime);
                        KuudraStats.globalBurningStats.addFailure(startTime);
                        break;
                    case "Fiery":
                        KuudraStats.sessionFieryStats.addFailure(startTime);
                        KuudraStats.globalFieryStats.addFailure(startTime);
                        break;
                    case "Infernal":
                        KuudraStats.sessionInfernalStats.addFailure(startTime);
                        KuudraStats.globalInfernalStats.addFailure(startTime);
                        break;
                }

                if (autoRequeue.getValue() && PartyUtils.isLeader()) {
                    if (PartyUtils.getDowntimeFlag()) {
                        DelayUtils.scheduleTask(() -> ChatUtils.partyChat("Taking Downtime!"), 2500);
                        PartyUtils.useDowntimeFlag();
                    } else {
                        switch (currentTier.getTierName()) {
                            case "Basic":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon kuudra_basic"), autoRequeueDelay.getValue() * 1000);
                                break;
                            case "Hot":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon kuudra_hot"), autoRequeueDelay.getValue() * 1000);
                                break;
                            case "Burning":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon kuudra_burning"), autoRequeueDelay.getValue() * 1000);
                                break;
                            case "Fiery":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon kuudra_fiery"), autoRequeueDelay.getValue() * 1000);
                                break;
                            case "Infernal":
                                DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon kuudra_infernal"), autoRequeueDelay.getValue() * 1000);
                                break;
                        }
                    }
                }
                break;
        }
    }

    public void prepRun() {
        kt.flush();
        currentPhase = 0;
        currentTier.getPhases().get(currentPhase).enterPhase();

        kl.setActiveListeners(currentTier.getPhases().get(currentPhase).getTriggers());
        runPrimed = true;

        if (resetOnParty.getValue()) {
            if (PartyUtils.getKuudraFlag()) {
                KuudraStats.sessionBasicStats.reset();
                KuudraStats.sessionHotStats.reset();
                KuudraStats.sessionBurningStats.reset();
                KuudraStats.sessionFieryStats.reset();
                KuudraStats.sessionInfernalStats.reset();
            }
        }

        PartyUtils.useKuudraFlag();
        PartyUtils.useDowntimeFlag();
    }

    public void advancePhase() {
        kt.split(rt, lt, currentPhase);

        if (currentPhase <= 0) {
            cachedSplits.add(currentTier.getPhases().get(0).getName() + ": " + ChatUtils.formatTime((rt.get(0) - startTime) / 1000.0) + " (" + ChatUtils.formatTime(((rt.get(0) - startTime) / 1000.0) - ((lt.get(0)) / 1000.0)) + ")");
        } else {
            cachedSplits.add(currentTier.getPhases().get(currentPhase).getName() + ": " + ChatUtils.formatTime((rt.get(currentPhase) - rt.get(currentPhase-1)) / 1000.0) + " (" + ChatUtils.formatTime(((rt.get(currentPhase) - rt.get(currentPhase-1)) / 1000.0) - ((lt.get(currentPhase)) / 1000.0)) + ")");
        }

        currentTier.getPhases().get(currentPhase).exitPhase();
        currentPhase++;
        currentTier.getPhases().get(currentPhase).enterPhase();
        kl.setActiveListeners(currentTier.getPhases().get(currentPhase).getTriggers());
    }
}
