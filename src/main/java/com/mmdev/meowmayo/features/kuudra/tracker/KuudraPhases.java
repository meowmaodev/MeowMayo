package com.mmdev.meowmayo.features.kuudra.tracker;

import com.mmdev.meowmayo.config.settings.FloatSliderSetting;
import com.mmdev.meowmayo.config.settings.IntSliderSetting;
import com.mmdev.meowmayo.config.settings.TextSetting;
import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.DelayUtils;
import com.mmdev.meowmayo.utils.PartyUtils;
import com.mmdev.meowmayo.utils.PlayerUtils;
import com.mmdev.meowmayo.utils.events.S02ChatReceivedEvent;
import com.mmdev.meowmayo.utils.events.S32ReceivedEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.lang.Math;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mmdev.meowmayo.config.ConfigSettings;
import com.mmdev.meowmayo.config.settings.ToggleSetting;

public class KuudraPhases {
    // Config Data
    private ToggleSetting kuudraTrack = (ToggleSetting) ConfigSettings.getSetting("Average Kuudra Run Time Tracker");
    private ToggleSetting resetOnParty = (ToggleSetting) ConfigSettings.getSetting("Reset Tracker on Party Change");
    private ToggleSetting supplyInfo = (ToggleSetting) ConfigSettings.getSetting("Supplies Info");
    private IntSliderSetting preLeeway = (IntSliderSetting) ConfigSettings.getSetting("Pre Tracker Leeway");
    private ToggleSetting missedPre = (ToggleSetting) ConfigSettings.getSetting("Pre Tracker");
    private ToggleSetting supplyInfoMessage = (ToggleSetting) ConfigSettings.getSetting("Supplies Overview");
    private ToggleSetting freshInfo = (ToggleSetting) ConfigSettings.getSetting("Fresh Info");
    private ToggleSetting freshInfoMessage = (ToggleSetting) ConfigSettings.getSetting("Fresh Overview");
    private ToggleSetting stunPing = (ToggleSetting) ConfigSettings.getSetting("Stun Ping");
    private FloatSliderSetting stunPingHp = (FloatSliderSetting) ConfigSettings.getSetting("Stun Ping HP");
    private TextSetting stunMessage = (TextSetting) ConfigSettings.getSetting("Stun Ping Message");
    private ToggleSetting lagMessage = (ToggleSetting) ConfigSettings.getSetting("Lag Timing");
    private ToggleSetting autoRequeue = (ToggleSetting) ConfigSettings.getSetting("Kuudra Auto Requeue");
    private IntSliderSetting autoRequeueDelay = (IntSliderSetting) ConfigSettings.getSetting("Kuudra Requeue Delay");

    private static File configFile;

    public static KTStats stats;
    public static KTStats globalStats;

    public static void init(File file) {
        configFile = file;
        stats = new KTStats();
        globalStats = new KTStats();
        loadStats();
    }

    // Class Data
    private static boolean inKuudra = false;

    private static int currPhase = 0;

    private long[] splits = new long[8];
    private long[] lag = new long[8];

    private static long clientMs = 0;
    private static long serverMs = 0;

    private Set<String> currParty = new HashSet<>();

    private Map<String, int[]> preConsistency = new HashMap<>();

    public static boolean getInKuudra() { return inKuudra; }

    public static int getCurrPhase() { return currPhase; }

    public static String getFormattedSplitTime() {
        return ChatUtils.formatTime(Math.max(Math.round((clientMs / 1000.0) * 100.0) / 100.0, 0.0));
    }

    public static double getUnformattedLagSplitTime() { return Math.max(Math.round((serverMs / 1000.0) * 100.0) / 100.0, 0.0); }

    private static Pattern noSup = Pattern.compile("^Party > (.+): No (.+)!$");
    private static Pattern recovered = Pattern.compile("^(.+) recovered one of Elle's supplies.*$");

    private static List<String[]> supplies = new ArrayList<>();
    private static Set<String> grabbed = new HashSet<>();

    private static Pattern freshed = Pattern.compile("^Party > (.+): Fresh.*$", Pattern.CASE_INSENSITIVE);

    private static int freshCount = 0;
    private static List<String[]> freshes = new ArrayList<>();

    private static boolean p6L = false;
    private static boolean p7L = false;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (inKuudra) {
            if (event.phase != TickEvent.Phase.END) return;
            clientMs += 50;
        }
    }
    @SubscribeEvent
    public void onServerTick(S32ReceivedEvent event) {
        if (inKuudra) {
            serverMs += 50;

            if (p6L) {
                if (getKuudra() == null) {
                    return;
                }

                float kuudraHp = getKuudra().getHealth();

                if (stunPing.getValue()) {
                    if (kuudraHp < (stunPingHp.getValue()*1000)) {
                        PlayerUtils.makeTextAlert(stunMessage.getValue(), "random.anvil_land", 1000);
                    }
                }

                if (kuudraHp > 25001.0) return;

                currPhase = 6;
                lagSplit(5);

                p7L = true;

                if (kuudraTrack.getValue()) {
                    splits[6] = System.currentTimeMillis();
                }

                p6L = false;
            }

            if (p7L) {
                if (Minecraft.getMinecraft().thePlayer.posY > 10.0) return;
                currPhase = 7;

                lagSplit(6);

                if (kuudraTrack.getValue()) {
                    splits[7] = System.currentTimeMillis();
                }

                p7L = false;
            }
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        endRun();
    }

    @SubscribeEvent
    public void onChatPacket(S02ChatReceivedEvent event) {
        String msg = event.getUnformattedMessage();

        if (Minecraft.getMinecraft().thePlayer == null) return;

        if (msg.equals("[NPC] Elle: Okay adventurers, I will go and fish up Kuudra!")) { // run start
            endRun();
            inKuudra = true;

            if (!kuudraTrack.getValue()) return;
            if (PartyUtils.getKuudraFlag()) {
                Set<String> newParty = PartyUtils.getParty();

                if (!newParty.equals(currParty)) {
                    if (resetOnParty.getValue()) {
                        stats.reset();
                    }
                    currParty = newParty;
                    if (supplyInfo.getValue()) {
                        preConsistency.clear();
                        for (String party : currParty) {
                            preConsistency.put(party, new int[]{0, 0, 0});
                        }
                    }
                }
            }

            PartyUtils.useDowntimeFlag();

            splits[0] = System.currentTimeMillis();
        }

        if (msg.equals("[NPC] Elle: Not again!")) {
            currPhase = 1;
            lagSplit(0);

            if (!(kuudraTrack.getValue() && inKuudra)) return;
            splits[1] = System.currentTimeMillis();
        }

        if (supplyInfo.getValue() && kuudraTrack.getValue()) {
            Matcher noSupMatch = noSup.matcher(msg);
            if (noSupMatch.matches()) {
                String playerName = ChatUtils.stripRank(noSupMatch.group(1));
                String noSupply = noSupMatch.group(2).toLowerCase();

                if (noSupply.equals("x") || noSupply.equals("triangle") || noSupply.equals("slash") || noSupply.equals("equals")) {
                    grabbed.add(playerName);
                    preConsistency.get(playerName)[1]++;
                }
                return;
            }

            if (currPhase == 1) {
                Matcher recoveredMatch = recovered.matcher(msg);
                if (recoveredMatch.matches()) {
                    String playerName = ChatUtils.stripRank(recoveredMatch.group(1));
                    supplies.add(new String[]{playerName, getFormattedSplitTime()});
                    if (getUnformattedLagSplitTime() < (8.0 + preLeeway.getValue())) {
                        grabbed.add(playerName);
                        preConsistency.get(playerName)[0]++;
                    }
                }
            }
        }

        if (msg.equals("[NPC] Elle: OMG! Great work collecting my supplies!")) {
            if (currPhase != 1) return;
            currPhase = 2;
            lagSplit(1);

            if (!(kuudraTrack.getValue() && inKuudra)) return;

            if (supplyInfo.getValue()) {
                Set<String> temp = new HashSet<>(currParty);

                if (grabbed.size() < currParty.size()) {
                    temp.removeAll(grabbed);

                    for (String party : temp) {
                        preConsistency.get(party)[2]++;
                    }
                }

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
            }

            splits[2] = System.currentTimeMillis();
        }

        if (kuudraTrack.getValue() && freshInfo.getValue()) {
            Matcher freshMatch = freshed.matcher(msg);
            if (freshMatch.matches()) {
                String playerName = ChatUtils.stripRank(freshMatch.group(1));
                freshCount++;
                freshes.add(new String[]{playerName, getFormattedSplitTime()});
            }
        }

        if (msg.equals("[NPC] Elle: Phew! The Ballista is finally ready! It should be strong enough to tank Kuudra's blows now!")) {
            if (currPhase != 2) return;

            currPhase = 3;
            lagSplit(2);

            if (!(kuudraTrack.getValue() && inKuudra)) return;
            if (freshInfo.getValue() && freshInfoMessage.getValue()) {
                String freshMessage = ("§a§l" + freshCount + " §r§ffreshes detected");
                for (String[] fresh : freshes) {
                    freshMessage += ("\n§b§l" + fresh[0] + " §rFreshed at §a§l" + fresh[1]);
                }
                ChatUtils.system(freshMessage);
            }

            splits[3] = System.currentTimeMillis();
        }

        if (msg.endsWith("has been eaten by Kuudra!") && !msg.startsWith("Elle") && msg.length() <= 44) {
            if (currPhase != 3) return;

            currPhase = 4;
            lagSplit(3);

            if (!(kuudraTrack.getValue() && inKuudra)) return;
            splits[4] = System.currentTimeMillis();
        }

        if (msg.endsWith("destroyed one of Kuudra's pods!") && msg.length() <= 50) {
            if (!(currPhase == 3 || currPhase == 4)) return;

            currPhase = 5;

            p6L = true;

            if (splits[4] == -1) {
                lagSplit(3);
            } else {
                lagSplit(4);
            }
            if (!(kuudraTrack.getValue() && inKuudra)) return;
            if (splits[4] == -1) {
                splits[4] = System.currentTimeMillis();
            }
            splits[5] = System.currentTimeMillis();
        }

        if (msg.trim().equals("DEFEAT")) {
            if (kuudraTrack.getValue() && inKuudra) {
                stats.totalRuns++;
                globalStats.totalRuns++;
                stats.totalTime += (System.currentTimeMillis() / 1000.0) - ((double) splits[0]);
                globalStats.totalTime += (System.currentTimeMillis() / 1000.0) - ((double) splits[0]);
                saveStats();
            }

            if (autoRequeue.getValue() && PartyUtils.isLeader()) {
                if (PartyUtils.getDowntimeFlag()) {
                    ChatUtils.partyChat("Taking Downtime!");
                    PartyUtils.useDowntimeFlag();
                } else {
                    DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon kuudra_infernal"), autoRequeueDelay.getValue() * 1000);
                }
            }
        }

        if (msg.trim().equals("KUUDRA DOWN!")) {
            lagSplit(7);

            if (kuudraTrack.getValue() && inKuudra) {
                splits[8] = System.currentTimeMillis();

                double runTime = ((splits[8] - splits[0]) / 1000.0);

                boolean valid = true;
                for (long time : splits) {
                    if (time == -1) {
                        valid = false;
                        break;
                    }
                }

                if (valid) {
                    stats.totalRuns++;
                    globalStats.totalRuns++;
                    stats.totalComps++;
                    globalStats.totalComps++;
                    stats.totalTime += runTime;
                    globalStats.totalTime += runTime;
                    stats.totalCompTime += runTime;
                    globalStats.totalCompTime += runTime;

                    double[] runSplits = new double[]{
                            (splits[1] - splits[0]) / 1000.0,
                            (splits[2] - splits[1]) / 1000.0,
                            (splits[3] - splits[2]) / 1000.0,
                            (splits[4] - splits[3]) / 1000.0,
                            (splits[5] - splits[4]) / 1000.0,
                            (splits[6] - splits[5]) / 1000.0,
                            (splits[7] - splits[6]) / 1000.0,
                            (splits[8] - splits[7]) / 1000.0
                    };

                    double runLag = 0.0;

                    double[] lagSplits = new double[8];

                    for (int i = 0; i < 8; i++) {
                        stats.compSplitTimes[i] += runSplits[i];
                        globalStats.compSplitTimes[i] += runSplits[i];
                        stats.compLagTimes[i] += lag[i] / 1000.0;
                        globalStats.compLagTimes[i] += lag[i] / 1000.0;
                        lagSplits[i] = lag[i] / 1000.0;
                        runLag += lag[i] / 1000.0;
                    }

                    runLag = Math.round(runLag * 1000.0) / 1000.0;

                    if (runTime > stats.slowest || stats.slowest == -1.0) {
                        stats.slowest = runTime;
                        stats.slowestLag = runLag;
                        stats.slowestSplits = runSplits.clone();
                        stats.slowestLagSplits = lagSplits.clone();

                        stats.slowestSupplies = supplies;

                        stats.slowestFreshCount = freshCount;
                        stats.slowestFresh = freshes;
                    }

                    if (runTime > globalStats.slowest || globalStats.slowest == -1.0) {
                        globalStats.slowest = runTime;
                        globalStats.slowestLag = runLag;
                        globalStats.slowestSplits = runSplits.clone();
                        globalStats.slowestLagSplits = lagSplits.clone();

                        globalStats.slowestSupplies = supplies;

                        globalStats.slowestFreshCount = freshCount;
                        globalStats.slowestFresh = freshes;
                    }

                    if (runTime > stats.fastest || stats.fastest == -1.0) {
                        stats.fastest = runTime;
                        stats.fastestLag = runLag;
                        stats.fastestSplits = runSplits.clone();
                        stats.fastestLagSplits = lagSplits.clone();

                        stats.fastestSupplies = supplies;

                        stats.fastestFreshCount = freshCount;
                        stats.fastestFresh = freshes;
                    }

                    if (runTime > globalStats.fastest || globalStats.fastest == -1.0) {
                        globalStats.fastest = runTime;
                        globalStats.fastestLag = runLag;
                        globalStats.fastestSplits = runSplits.clone();
                        globalStats.fastestLagSplits = lagSplits.clone();

                        globalStats.fastestSupplies = supplies;

                        globalStats.fastestFreshCount = freshCount;
                        globalStats.fastestFresh = freshes;
                    }

                    stats.totalLag += runLag;
                    globalStats.totalLag += runLag;

                    if (lagMessage.getValue()) {
                        ChatUtils.partyChat(ChatUtils.formatTime(runLag) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(runTime - runLag));
                    }

                    ChatUtils.system(ChatUtils.formatTime(runLag) + " Lost to server lag | No lag time: " + ChatUtils.formatTime(runTime - runLag));
                    ChatUtils.system("§fTotal Run Time: §a§l" + ChatUtils.formatTime(runTime) + "\n" +
                            "§2||§r§f Crates took §a§l" + ChatUtils.formatTime(runSplits[0]) + "§r§f to spawn\n" +
                            "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lagSplits[0]) + "§r§f to lag\n" +
                            "§2||§r§f Supplies took §a§l" + ChatUtils.formatTime(runSplits[1]) + "§r§f to finish\n" +
                            "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lagSplits[1]) + "§r§f to lag\n" +
                            "§2||§r§f Build took §a§l" + ChatUtils.formatTime(runSplits[2]) + "§r§f to finish\n" +
                            "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lagSplits[2]) + "§r§f to lag\n" +
                            "§2||§r§f Cannon took §a§l" + ChatUtils.formatTime(runSplits[3]) + "§r§f to launch\n" +
                            "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lagSplits[3]) + "§r§f to lag\n" +
                            "§2||§r§f Kuudra took §a§l" + ChatUtils.formatTime(runSplits[4]) + "§r§f to stun\n" +
                            "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lagSplits[4]) + "§r§f to lag\n" +
                            "§2||§r§f DPS took §a§l" + ChatUtils.formatTime(runSplits[5]) + "§r§f to complete\n" +
                            "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lagSplits[5]) + "§r§f to lag\n" +
                            "§2||§r§f Skip took §a§l" + ChatUtils.formatTime(runSplits[6]) + "\n" +
                            "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lagSplits[6]) + "§r§f to lag\n" +
                            "§2||§r§f Final Phase took §a§l" + ChatUtils.formatTime(runSplits[7]) + "\n" +
                            "§2||==§r§f Lost §c§l" + ChatUtils.formatTime(lagSplits[7]) + "§r§f to lag\n"
                    );

                    saveStats();
                }
            }

            endRun();

            if (autoRequeue.getValue() && PartyUtils.isLeader()) {
                if (PartyUtils.getDowntimeFlag()) {
                    ChatUtils.partyChat("Taking Downtime!");
                    PartyUtils.useDowntimeFlag();
                } else {
                    DelayUtils.scheduleTask(() -> ChatUtils.command("joindungeon kuudra_infernal"), autoRequeueDelay.getValue() * 1000);
                }
            }
        }
    }

    public static class KTStats implements Serializable {
        private static final long serialVersionUID = 1L;

        public int totalRuns = 0;
        public int totalComps = 0;
        public double totalTime = 0.0;
        public double totalCompTime = 0.0;

        public double fastest = -1.0;
        public double fastestLag = -1.0;
        public double[] fastestSplits = new double[8];
        public List<String[]> fastestSupplies = new ArrayList<>();
        public int fastestFreshCount = -1;
        public List<String[]> fastestFresh = new ArrayList<>();
        public double[] fastestLagSplits = new double[8];

        public double slowest = -1.0;
        public double slowestLag = -1.0;
        public double[] slowestSplits = new double[8];
        public List<String[]> slowestSupplies = new ArrayList<>();
        public int slowestFreshCount = -1;
        public List<String[]> slowestFresh = new ArrayList<>();
        public double[] slowestLagSplits = new double[8];

        public double[] compSplitTimes = new double[8];
        public double totalLag = 0.0;
        public double[] compLagTimes = new double[8];

        public void reset() {
            totalRuns = 0;
            totalComps = 0;
            totalTime = 0.0;
            totalCompTime = 0.0;

            fastest = -1.0;
            fastestLag = -1.0;
            Arrays.fill(fastestSplits, 0);
            fastestSupplies.clear();
            fastestFreshCount = -1;
            fastestFresh.clear();
            Arrays.fill(fastestLagSplits, 0);

            slowest = -1.0;
            slowestLag = -1.0;
            Arrays.fill(slowestSplits, 0);
            slowestSupplies.clear();
            slowestFreshCount = -1;
            slowestFresh.clear();
            Arrays.fill(slowestLagSplits, 0);

            Arrays.fill(compSplitTimes, 0);
            totalLag = 0;
            Arrays.fill(compLagTimes, 0);
        }

        public void validate() {
            if (totalRuns < 0) totalRuns = 0;
            if (totalComps < 0) totalComps = 0;
            if (fastest < 0) fastest = -1.0;
            if (slowest < 0) slowest = -1.0;

            if (fastestSplits == null || fastestSplits.length != 8) fastestSplits = new double[8];
            if (slowestSplits == null || slowestSplits.length != 8) slowestSplits = new double[8];
            if (fastestLagSplits == null || fastestLagSplits.length != 8) fastestLagSplits = new double[8];
            if (slowestLagSplits == null || slowestLagSplits.length != 8) slowestLagSplits = new double[8];
            if (compSplitTimes == null || compSplitTimes.length != 8) compSplitTimes = new double[8];
            if (compLagTimes == null || compLagTimes.length != 8) compLagTimes = new double[8];

            if (fastestSupplies == null) fastestSupplies = new ArrayList<>();
            if (fastestFresh == null) fastestFresh = new ArrayList<>();
            if (slowestSupplies == null) slowestSupplies = new ArrayList<>();
            if (slowestFresh == null) slowestFresh = new ArrayList<>();
        }
    }

    public static void saveStats() {
        try {
            File file = new File(configFile, "KuudraTimeStats.dat");
            if (!file.exists()) {
                file.createNewFile();
            }
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file.toPath()))) {
                out.writeObject(globalStats);
            } catch (IOException ignored) {
            }
        } catch (IOException ignored) {
        }
    }

    // ----- Load stats -----
    public static void loadStats() {
        File file = new File(configFile, "KuudraTimeStats.dat");
        if (!file.exists()) {
            globalStats.reset();
            saveStats();
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file.toPath()))) {
            Object obj = in.readObject();
            if (obj instanceof KTStats) {
                globalStats = (KTStats) obj;
                globalStats.validate();
            } else {
                globalStats.reset();
                saveStats();
            }
        } catch (Exception e) {
            globalStats.reset();
            saveStats();
        }
    }

    public static void rsKT() {
        stats.reset();
    }

    public static void rsGlobalKT() {
        globalStats.reset();
        saveStats();
    }

    public static EntityMagmaCube getKuudra() {
        for (Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) { // i probably took this recognition from someone!!!
            if (entity instanceof EntityMagmaCube) {
                EntityMagmaCube cube = (EntityMagmaCube) entity;

                if (cube.getSlimeSize() >= 15 ) return cube;
            }
        }
        return null;
    }

    private void lagSplit(int index) {
        lag[index] = Math.max(0, clientMs - serverMs);
        clientMs = 0;
        serverMs = 0;
    }

    private void endRun() {
        inKuudra = false;
        currPhase = 0;

        p7L = false;
        p6L = false;

        supplies = new ArrayList<String[]>();
        grabbed = new HashSet<String>();

        freshes = new ArrayList<String[]>();
        freshCount = 0;

        Arrays.fill(splits, -1);

        Arrays.fill(lag, 0);

        clientMs = 0;
        serverMs = 0;
    }
}
