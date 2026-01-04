package com.mmdev.meowmayo.features.kuudra.tracker;

import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.tracker.Stats;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class KuudraStats {
    public static class t14Stats extends Stats {
        // this should still be condensed but i can at least share 1 static thing for now
        // hacky and i will fix it eventually

        public List<String[]> fastestSupplies;
        public int fastestFreshCount;
        public List<String[]> fastestFresh;

        public List<String[]> slowestSupplies;
        public int slowestFreshCount;
        public List<String[]> slowestFresh;

        t14Stats() {
            this.slowestSplits = new double[4];
            this.slowestLagSplits = new double[4];
            this.fastestSplits = new double[4];
            this.fastestLagSplits = new double[4];
            this.compSplitTimes = new double[4];
            this.compLagTimes = new double[4];

            this.fastestSupplies = new ArrayList<>();
            this.fastestFresh = new ArrayList<>();
            this.fastestFreshCount = -1;
            this.slowestSupplies = new ArrayList<>();
            this.slowestFresh = new ArrayList<>();
            this.slowestFreshCount = -1;

            this.bestSplits = new double[0];
        }

        public void addSuccess(
                long startTime,
                List<Long> rt,
                List<Long> lt,
                List<String[]> fresh,
                List<String[]> sup
        ) {
            if (rt.size() != 4 || lt.size() != 4) {
                ChatUtils.system("Error Adding Run Time. Misformatted Splits!");
            }

            double totalRunTime = (System.currentTimeMillis() - startTime) / 1000.0;

            this.totalRuns++;
            this.totalComps++;

            this.totalTime += totalRunTime;
            this.totalCompTime += totalRunTime;

            double[] runSplits = new double[] {
                    (rt.get(0) - startTime) / 1000.0,
                    (rt.get(1) - rt.get(0)) / 1000.0,
                    (rt.get(2) - rt.get(1)) / 1000.0,
                    (rt.get(3) - rt.get(2)) / 1000.0
            };

            long totalLagTime = 0;
            double[] lagSplits = new double[4];

            for (int i = 0; i < 4; i++) {
                this.compSplitTimes[i] += runSplits[i];
                this.compLagTimes[i] += (lt.get(i)/1000.0);

                lagSplits[i] = (lt.get(i)/1000.0);
                totalLagTime += lt.get(i);
            }

            this.totalLag += (totalLagTime / 1000.0);

            if (totalRunTime > this.slowest || this.slowest == -1.0) {
                this.slowest = totalRunTime;
                this.slowestLag = (totalLagTime / 1000.0);
                this.slowestSplits = runSplits.clone();
                this.slowestLagSplits = lagSplits.clone();
                this.slowestFresh = fresh;
                this.slowestFreshCount = fresh.size();
                this.slowestSupplies = sup;
            }

            if (totalRunTime < this.fastest || this.fastest == -1.0) {
                this.fastest = totalRunTime;
                this.fastestLag = (totalLagTime / 1000.0);
                this.fastestSplits = runSplits.clone();
                this.fastestLagSplits = lagSplits.clone();
                this.fastestFresh = fresh;
                this.fastestFreshCount = fresh.size();
                this.fastestSupplies = sup;
            }
        }

        @Override
        public void addSuccess(
                long startTime,
                List<Long> rt,
                List<Long> lt
        ) {
            // not used!
        }
    }

    public static class t5Stats extends Stats {
        public List<String[]> fastestSupplies;
        public int fastestFreshCount;
        public List<String[]> fastestFresh;

        public List<String[]> slowestSupplies;
        public int slowestFreshCount;
        public List<String[]> slowestFresh;

        t5Stats() {
            this.slowestSplits = new double[5];
            this.slowestLagSplits = new double[5];
            this.fastestSplits = new double[5];
            this.fastestLagSplits = new double[5];
            this.compSplitTimes = new double[5];
            this.compLagTimes = new double[5];

            this.fastestSupplies = new ArrayList<>();
            this.fastestFresh = new ArrayList<>();
            this.fastestFreshCount = -1;
            this.slowestSupplies = new ArrayList<>();
            this.slowestFresh = new ArrayList<>();
            this.slowestFreshCount = -1;

            this.bestSplits = new double[0];
        }

        public void addSuccess(
                long startTime,
                List<Long> rt,
                List<Long> lt,
                List<String[]> fresh,
                List<String[]> sup
        ) {
            if (rt.size() != 5 || lt.size() != 5) {
                ChatUtils.system("Error Adding Run Time. Misformatted Splits!");
            }

            double totalRunTime = (System.currentTimeMillis() - startTime) / 1000.0;

            this.totalRuns++;
            this.totalComps++;

            this.totalTime += totalRunTime;
            this.totalCompTime += totalRunTime;

            double[] runSplits = new double[] {
                    (rt.get(0) - startTime) / 1000.0,
                    (rt.get(1) - rt.get(0)) / 1000.0,
                    (rt.get(2) - rt.get(1)) / 1000.0,
                    (rt.get(3) - rt.get(2)) / 1000.0,
                    (rt.get(4) - rt.get(3)) / 1000.0
            };

            long totalLagTime = 0;
            double[] lagSplits = new double[5];

            for (int i = 0; i < 5; i++) {
                this.compSplitTimes[i] += runSplits[i];
                this.compLagTimes[i] += (lt.get(i)/1000.0);

                lagSplits[i] = (lt.get(i)/1000.0);
                totalLagTime += lt.get(i);
            }

            this.totalLag += (totalLagTime / 1000.0);


            if (totalRunTime > this.slowest || this.slowest == -1.0) {
                this.slowest = totalRunTime;
                this.slowestLag = (totalLagTime / 1000.0);
                this.slowestSplits = runSplits.clone();
                this.slowestLagSplits = lagSplits.clone();
                this.slowestFresh = fresh;
                this.slowestFreshCount = fresh.size();
                this.slowestSupplies = sup;
            }

            if (totalRunTime < this.fastest || this.fastest == -1.0) {
                this.fastest = totalRunTime;
                this.fastestLag = (totalLagTime / 1000.0);
                this.fastestSplits = runSplits.clone();
                this.fastestLagSplits = lagSplits.clone();
                this.fastestFresh = fresh;
                this.fastestFreshCount = fresh.size();
                this.fastestSupplies = sup;
            }
        }

        @Override
        public void addSuccess(
                long startTime,
                List<Long> rt,
                List<Long> lt
        ) {
            // not used!
            // this structure probably needs to be rewritten but im just tryna release man :sob:
        }
    }

    public static void saveConfig(String tier) {
        switch (tier) {
            case "Basic":
                save(globalBasicStats, "KTSBasicStats.dat");
                break;
            case "Hot":
                save(globalHotStats, "KTSHotStats.dat");
                break;
            case "Burning":
                save(globalBurningStats, "KTSBurningStats.dat");
                break;
            case "Fiery":
                save(globalFieryStats, "KTSFieryStats.dat");
                break;
            case "Infernal":
                save(globalInfernalStats, "KTSInfernalStats.dat");
                break;
        }
    }

    private static void save(Stats stat, String name) {
        try {
            File file = new File(configFile, name);
            if (!file.exists()) {
                file.createNewFile();
            }
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file.toPath()))) {
                out.writeObject(stat);
            } catch (IOException ignored) {
            }
        } catch (IOException ignored) {
        }
    }

    public static File configFile;

    public static t14Stats sessionBasicStats;
    public static t14Stats globalBasicStats;

    public static t14Stats sessionHotStats;
    public static t14Stats globalHotStats;

    public static t14Stats sessionBurningStats;
    public static t14Stats globalBurningStats;

    public static t14Stats sessionFieryStats;
    public static t14Stats globalFieryStats;

    public static t5Stats sessionInfernalStats;
    public static t5Stats globalInfernalStats;


    public static void init(File file) {
        configFile = file;

        // basic
        File basicStats = new File(configFile, "KTSBasicStats.dat");

        sessionBasicStats = new t14Stats();
        globalBasicStats = new t14Stats();

        if (!basicStats.exists()) {
            globalBasicStats.reset();
            save(globalBasicStats, "KTSBasicStats.dat");
        } else {
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(basicStats.toPath()))) {
                Object obj = in.readObject();
                if (obj instanceof t14Stats) {
                    globalBasicStats = (t14Stats) obj;
                } else {
                    globalBasicStats.reset();
                    save(globalBasicStats, "KTSBasicStats.dat");
                }
            } catch (Exception e) {
                globalBasicStats.reset();
                save(globalBasicStats, "KTSBasicStats.dat");
            }
        }

        // hot
        File hotStats = new File(configFile, "KTSHotStats.dat");

        sessionHotStats = new t14Stats();
        globalHotStats = new t14Stats();

        if (!hotStats.exists()) {
            globalHotStats.reset();
            save(globalHotStats, "KTSHotStats.dat");
        } else {
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(basicStats.toPath()))) {
                Object obj = in.readObject();
                if (obj instanceof t14Stats) {
                    globalHotStats = (t14Stats) obj;
                } else {
                    globalHotStats.reset();
                    save(globalHotStats, "KTSHotStats.dat");
                }
            } catch (Exception e) {
                globalHotStats.reset();
                save(globalHotStats, "KTSHotStats.dat");
            }
        }

        // burning
        File burningStats = new File(configFile, "KTSBurningStats.dat");

        sessionBurningStats = new t14Stats();
        globalBurningStats = new t14Stats();

        if (!burningStats.exists()) {
            globalBurningStats.reset();
            save(globalBurningStats, "KTSBurningStats.dat");
        } else {
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(basicStats.toPath()))) {
                Object obj = in.readObject();
                if (obj instanceof t14Stats) {
                    globalBurningStats = (t14Stats) obj;
                } else {
                    globalBurningStats.reset();
                    save(globalBurningStats, "KTSBurningStats.dat");
                }
            } catch (Exception e) {
                globalBurningStats.reset();
                save(globalBurningStats, "KTSBurningStats.dat");
            }
        }

        // fiery
        File fieryStats = new File(configFile, "KTSFieryStats.dat");

        sessionFieryStats = new t14Stats();
        globalFieryStats = new t14Stats();

        if (!fieryStats.exists()) {
            globalFieryStats.reset();
            save(globalFieryStats, "KTSFieryStats.dat");
        } else {
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(basicStats.toPath()))) {
                Object obj = in.readObject();
                if (obj instanceof t14Stats) {
                    globalFieryStats = (t14Stats) obj;
                } else {
                    globalFieryStats.reset();
                    save(globalFieryStats, "KTSFieryStats.dat");
                }
            } catch (Exception e) {
                globalFieryStats.reset();
                save(globalFieryStats, "KTSFieryStats.dat");
            }
        }

        // infernal
        File infernalStats = new File(configFile, "KTSInfernalStats.dat");

        sessionInfernalStats = new t5Stats();
        globalInfernalStats = new t5Stats();

        if (!infernalStats.exists()) {
            globalInfernalStats.reset();
            save(globalInfernalStats, "KTSInfernalStats.dat");
        } else {
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(basicStats.toPath()))) {
                Object obj = in.readObject();
                if (obj instanceof t5Stats) {
                    globalInfernalStats = (t5Stats) obj;
                } else {
                    globalInfernalStats.reset();
                    save(globalInfernalStats, "KTSInfernalStats.dat");
                }
            } catch (Exception e) {
                globalInfernalStats.reset();
                save(globalInfernalStats, "KTSInfernalStats.dat");
            }
        }
    }
}
