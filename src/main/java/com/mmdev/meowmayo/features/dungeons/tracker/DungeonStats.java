package com.mmdev.meowmayo.features.dungeons.tracker;

import com.mmdev.meowmayo.utils.ChatUtils;
import com.mmdev.meowmayo.utils.tracker.Stats;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class DungeonStats {
    // Developers Note: This can be converted to use a single static data class, BUT im rushing to get this finished so i will impl that later, for now you get code vomit
    public static class F2Stats extends Stats {
        F2Stats() {
            this.slowestSplits = new double[5];
            this.slowestLagSplits = new double[5];
            this.fastestSplits = new double[5];
            this.fastestLagSplits = new double[5];
            this.compSplitTimes = new double[5];
            this.compLagTimes = new double[5];

            this.bestSplits = new double[5];
            Arrays.fill(bestSplits, Double.MAX_VALUE);
        }

        @Override
        public void addSuccess(
                long startTime,
                List<Long> rt,
                List<Long> lt
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

                if (runSplits[i] < this.bestSplits[i]) {
                    this.bestSplits[i] = runSplits[i]; // new BEST!
                }
            }

            this.totalLag += (totalLagTime / 1000.0);

            if (totalRunTime > this.slowest || this.slowest == -1.0) {
                this.slowest = totalRunTime;
                this.slowestLag = (totalLagTime / 1000.0);
                this.slowestSplits = runSplits.clone();
                this.slowestLagSplits = lagSplits.clone();
            }

            if (totalRunTime < this.fastest || this.fastest == -1.0) {
                this.fastest = totalRunTime;
                this.fastestLag = (totalLagTime / 1000.0);
                this.fastestSplits = runSplits.clone();
                this.fastestLagSplits = lagSplits.clone();
            }
        }
    }

    public static class F3Stats extends Stats {
        F3Stats() {
            this.slowestSplits = new double[6];
            this.slowestLagSplits = new double[6];
            this.fastestSplits = new double[6];
            this.fastestLagSplits = new double[6];
            this.compSplitTimes = new double[6];
            this.compLagTimes = new double[6];

            this.bestSplits = new double[6];
            Arrays.fill(bestSplits, Double.MAX_VALUE);
        }

        @Override
        public void addSuccess(
                long startTime,
                List<Long> rt,
                List<Long> lt
        ) {
            if (rt.size() != 6 || lt.size() != 6) {
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
                    (rt.get(4) - rt.get(3)) / 1000.0,
                    (rt.get(5) - rt.get(4)) / 1000.0
            };

            long totalLagTime = 0;
            double[] lagSplits = new double[6];

            for (int i = 0; i < 6; i++) {
                this.compSplitTimes[i] += runSplits[i];
                this.compLagTimes[i] += (lt.get(i)/1000.0);

                lagSplits[i] = (lt.get(i)/1000.0);
                totalLagTime += lt.get(i);

                if (runSplits[i] < this.bestSplits[i]) {
                    this.bestSplits[i] = runSplits[i]; // new BEST!
                }
            }

            this.totalLag += (totalLagTime / 1000.0);

            if (totalRunTime > this.slowest || this.slowest == -1.0) {
                this.slowest = totalRunTime;
                this.slowestLag = (totalLagTime / 1000.0);
                this.slowestSplits = runSplits.clone();
                this.slowestLagSplits = lagSplits.clone();
            }

            if (totalRunTime < this.fastest || this.fastest == -1.0) {
                this.fastest = totalRunTime;
                this.fastestLag = (totalLagTime / 1000.0);
                this.fastestSplits = runSplits.clone();
                this.fastestLagSplits = lagSplits.clone();
            }
        }
    }

    public static class F5Stats extends Stats {
        F5Stats() {
            this.slowestSplits = new double[4];
            this.slowestLagSplits = new double[4];
            this.fastestSplits = new double[4];
            this.fastestLagSplits = new double[4];
            this.compSplitTimes = new double[4];
            this.compLagTimes = new double[4];

            this.bestSplits = new double[4];
            Arrays.fill(bestSplits, Double.MAX_VALUE);
        }

        @Override
        public void addSuccess(
                long startTime,
                List<Long> rt,
                List<Long> lt
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

            for (int i = 0; i < 6; i++) {
                this.compSplitTimes[i] += runSplits[i];
                this.compLagTimes[i] += (lt.get(i)/1000.0);

                lagSplits[i] = (lt.get(i)/1000.0);
                totalLagTime += lt.get(i);

                if (runSplits[i] < this.bestSplits[i]) {
                    this.bestSplits[i] = runSplits[i]; // new BEST!
                }
            }

            this.totalLag += (totalLagTime / 1000.0);

            if (totalRunTime > this.slowest || this.slowest == -1.0) {
                this.slowest = totalRunTime;
                this.slowestLag = (totalLagTime / 1000.0);
                this.slowestSplits = runSplits.clone();
                this.slowestLagSplits = lagSplits.clone();
            }

            if (totalRunTime < this.fastest || this.fastest == -1.0) {
                this.fastest = totalRunTime;
                this.fastestLag = (totalLagTime / 1000.0);
                this.fastestSplits = runSplits.clone();
                this.fastestLagSplits = lagSplits.clone();
            }
        }
    }

    public static class F6Stats extends Stats {
        F6Stats() {
            this.slowestSplits = new double[6];
            this.slowestLagSplits = new double[6];
            this.fastestSplits = new double[6];
            this.fastestLagSplits = new double[6];
            this.compSplitTimes = new double[6];
            this.compLagTimes = new double[6];

            this.bestSplits = new double[6];
            Arrays.fill(bestSplits, Double.MAX_VALUE);
        }

        @Override
        public void addSuccess(
                long startTime,
                List<Long> rt,
                List<Long> lt
        ) {
            if (rt.size() != 6 || lt.size() != 6) {
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
                    (rt.get(4) - rt.get(3)) / 1000.0,
                    (rt.get(5) - rt.get(4)) / 1000.0
            };

            long totalLagTime = 0;
            double[] lagSplits = new double[6];

            for (int i = 0; i < 6; i++) {
                this.compSplitTimes[i] += runSplits[i];
                this.compLagTimes[i] += (lt.get(i)/1000.0);

                lagSplits[i] = (lt.get(i)/1000.0);
                totalLagTime += lt.get(i);

                if (runSplits[i] < this.bestSplits[i]) {
                    this.bestSplits[i] = runSplits[i]; // new BEST!
                }
            }

            this.totalLag += (totalLagTime / 1000.0);

            if (totalRunTime > this.slowest || this.slowest == -1.0) {
                this.slowest = totalRunTime;
                this.slowestLag = (totalLagTime / 1000.0);
                this.slowestSplits = runSplits.clone();
                this.slowestLagSplits = lagSplits.clone();
            }

            if (totalRunTime < this.fastest || this.fastest == -1.0) {
                this.fastest = totalRunTime;
                this.fastestLag = (totalLagTime / 1000.0);
                this.fastestSplits = runSplits.clone();
                this.fastestLagSplits = lagSplits.clone();
            }
        }
    }

    public static class F7Stats extends Stats {
        F7Stats() {
            this.slowestSplits = new double[8];
            this.slowestLagSplits = new double[8];
            this.fastestSplits = new double[8];
            this.fastestLagSplits = new double[8];
            this.compSplitTimes = new double[8];
            this.compLagTimes = new double[8];

            this.bestSplits = new double[8];
            Arrays.fill(bestSplits, Double.MAX_VALUE);
        }

        @Override
        public void addSuccess(
                long startTime,
                List<Long> rt,
                List<Long> lt
        ) {
            if (rt.size() != 8 || lt.size() != 8) {
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
                    (rt.get(4) - rt.get(3)) / 1000.0,
                    (rt.get(5) - rt.get(4)) / 1000.0,
                    (rt.get(6) - rt.get(5)) / 1000.0,
                    (rt.get(7) - rt.get(6)) / 1000.0,
            };

            long totalLagTime = 0;
            double[] lagSplits = new double[8];

            for (int i = 0; i < 8; i++) {
                this.compSplitTimes[i] += runSplits[i];
                this.compLagTimes[i] += (lt.get(i)/1000.0);

                lagSplits[i] = (lt.get(i)/1000.0);
                totalLagTime += lt.get(i);

                if (runSplits[i] < this.bestSplits[i]) {
                    this.bestSplits[i] = runSplits[i]; // new BEST!
                }
            }

            this.totalLag += (totalLagTime / 1000.0);

            if (totalRunTime > this.slowest || this.slowest == -1.0) {
                this.slowest = totalRunTime;
                this.slowestLag = (totalLagTime / 1000.0);
                this.slowestSplits = runSplits.clone();
                this.slowestLagSplits = lagSplits.clone();
            }

            if (totalRunTime < this.fastest || this.fastest == -1.0) {
                this.fastest = totalRunTime;
                this.fastestLag = (totalLagTime / 1000.0);
                this.fastestSplits = runSplits.clone();
                this.fastestLagSplits = lagSplits.clone();
            }
        }
    }

    public static class M2Stats extends Stats {
        M2Stats() {
            this.slowestSplits = new double[5];
            this.slowestLagSplits = new double[5];
            this.fastestSplits = new double[5];
            this.fastestLagSplits = new double[5];
            this.compSplitTimes = new double[5];
            this.compLagTimes = new double[5];

            this.bestSplits = new double[5];
            Arrays.fill(bestSplits, Double.MAX_VALUE);
        }

        @Override
        public void addSuccess(
                long startTime,
                List<Long> rt,
                List<Long> lt
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

                if (runSplits[i] < this.bestSplits[i]) {
                    this.bestSplits[i] = runSplits[i]; // new BEST!
                }
            }

            this.totalLag += (totalLagTime / 1000.0);

            if (totalRunTime > this.slowest || this.slowest == -1.0) {
                this.slowest = totalRunTime;
                this.slowestLag = (totalLagTime / 1000.0);
                this.slowestSplits = runSplits.clone();
                this.slowestLagSplits = lagSplits.clone();
            }

            if (totalRunTime < this.fastest || this.fastest == -1.0) {
                this.fastest = totalRunTime;
                this.fastestLag = (totalLagTime / 1000.0);
                this.fastestSplits = runSplits.clone();
                this.fastestLagSplits = lagSplits.clone();
            }
        }
    }

    public static class M3Stats extends Stats {
        M3Stats() {
            this.slowestSplits = new double[6];
            this.slowestLagSplits = new double[6];
            this.fastestSplits = new double[6];
            this.fastestLagSplits = new double[6];
            this.compSplitTimes = new double[6];
            this.compLagTimes = new double[6];

            this.bestSplits = new double[6];
            Arrays.fill(bestSplits, Double.MAX_VALUE);
        }

        @Override
        public void addSuccess(
                long startTime,
                List<Long> rt,
                List<Long> lt
        ) {
            if (rt.size() != 6 || lt.size() != 6) {
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
                    (rt.get(4) - rt.get(3)) / 1000.0,
                    (rt.get(5) - rt.get(4)) / 1000.0
            };

            long totalLagTime = 0;
            double[] lagSplits = new double[6];

            for (int i = 0; i < 6; i++) {
                this.compSplitTimes[i] += runSplits[i];
                this.compLagTimes[i] += (lt.get(i)/1000.0);

                lagSplits[i] = (lt.get(i)/1000.0);
                totalLagTime += lt.get(i);

                if (runSplits[i] < this.bestSplits[i]) {
                    this.bestSplits[i] = runSplits[i]; // new BEST!
                }
            }

            this.totalLag += (totalLagTime / 1000.0);

            if (totalRunTime > this.slowest || this.slowest == -1.0) {
                this.slowest = totalRunTime;
                this.slowestLag = (totalLagTime / 1000.0);
                this.slowestSplits = runSplits.clone();
                this.slowestLagSplits = lagSplits.clone();
            }

            if (totalRunTime < this.fastest || this.fastest == -1.0) {
                this.fastest = totalRunTime;
                this.fastestLag = (totalLagTime / 1000.0);
                this.fastestSplits = runSplits.clone();
                this.fastestLagSplits = lagSplits.clone();
            }
        }
    }

    public static class M5Stats extends Stats {
        M5Stats() {
            this.slowestSplits = new double[4];
            this.slowestLagSplits = new double[4];
            this.fastestSplits = new double[4];
            this.fastestLagSplits = new double[4];
            this.compSplitTimes = new double[4];
            this.compLagTimes = new double[4];

            this.bestSplits = new double[4];
            Arrays.fill(bestSplits, Double.MAX_VALUE);
        }

        @Override
        public void addSuccess(
                long startTime,
                List<Long> rt,
                List<Long> lt
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

            for (int i = 0; i < 6; i++) {
                this.compSplitTimes[i] += runSplits[i];
                this.compLagTimes[i] += (lt.get(i)/1000.0);

                lagSplits[i] = (lt.get(i)/1000.0);
                totalLagTime += lt.get(i);

                if (runSplits[i] < this.bestSplits[i]) {
                    this.bestSplits[i] = runSplits[i]; // new BEST!
                }
            }

            this.totalLag += (totalLagTime / 1000.0);

            if (totalRunTime > this.slowest || this.slowest == -1.0) {
                this.slowest = totalRunTime;
                this.slowestLag = (totalLagTime / 1000.0);
                this.slowestSplits = runSplits.clone();
                this.slowestLagSplits = lagSplits.clone();
            }

            if (totalRunTime < this.fastest || this.fastest == -1.0) {
                this.fastest = totalRunTime;
                this.fastestLag = (totalLagTime / 1000.0);
                this.fastestSplits = runSplits.clone();
                this.fastestLagSplits = lagSplits.clone();
            }
        }
    }

    public static class M6Stats extends Stats {
        M6Stats() {
            this.slowestSplits = new double[6];
            this.slowestLagSplits = new double[6];
            this.fastestSplits = new double[6];
            this.fastestLagSplits = new double[6];
            this.compSplitTimes = new double[6];
            this.compLagTimes = new double[6];

            this.bestSplits = new double[6];
            Arrays.fill(bestSplits, Double.MAX_VALUE);
        }

        @Override
        public void addSuccess(
                long startTime,
                List<Long> rt,
                List<Long> lt
        ) {
            if (rt.size() != 6 || lt.size() != 6) {
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
                    (rt.get(4) - rt.get(3)) / 1000.0,
                    (rt.get(5) - rt.get(4)) / 1000.0
            };

            long totalLagTime = 0;
            double[] lagSplits = new double[6];

            for (int i = 0; i < 6; i++) {
                this.compSplitTimes[i] += runSplits[i];
                this.compLagTimes[i] += (lt.get(i)/1000.0);

                lagSplits[i] = (lt.get(i)/1000.0);
                totalLagTime += lt.get(i);

                if (runSplits[i] < this.bestSplits[i]) {
                    this.bestSplits[i] = runSplits[i]; // new BEST!
                }
            }

            this.totalLag += (totalLagTime / 1000.0);

            if (totalRunTime > this.slowest || this.slowest == -1.0) {
                this.slowest = totalRunTime;
                this.slowestLag = (totalLagTime / 1000.0);
                this.slowestSplits = runSplits.clone();
                this.slowestLagSplits = lagSplits.clone();
            }

            if (totalRunTime < this.fastest || this.fastest == -1.0) {
                this.fastest = totalRunTime;
                this.fastestLag = (totalLagTime / 1000.0);
                this.fastestSplits = runSplits.clone();
                this.fastestLagSplits = lagSplits.clone();
            }
        }
    }

    public static class M7Stats extends Stats {
        M7Stats() {
            this.slowestSplits = new double[9];
            this.slowestLagSplits = new double[9];
            this.fastestSplits = new double[9];
            this.fastestLagSplits = new double[9];
            this.compSplitTimes = new double[9];
            this.compLagTimes = new double[9];

            this.bestSplits = new double[9];
            Arrays.fill(bestSplits, Double.MAX_VALUE);
        }

        @Override
        public void addSuccess(
                long startTime,
                List<Long> rt,
                List<Long> lt
        ) {
            if (rt.size() != 9 || lt.size() != 9) {
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
                    (rt.get(4) - rt.get(3)) / 1000.0,
                    (rt.get(5) - rt.get(4)) / 1000.0,
                    (rt.get(6) - rt.get(5)) / 1000.0,
                    (rt.get(7) - rt.get(6)) / 1000.0,
                    (rt.get(8) - rt.get(7)) / 1000.0
            };

            long totalLagTime = 0;
            double[] lagSplits = new double[9];

            for (int i = 0; i < 9; i++) {
                this.compSplitTimes[i] += runSplits[i];
                this.compLagTimes[i] += (lt.get(i)/1000.0);

                lagSplits[i] = (lt.get(i)/1000.0);
                totalLagTime += lt.get(i);

                if (runSplits[i] < this.bestSplits[i]) {
                    this.bestSplits[i] = runSplits[i]; // new BEST!
                }
            }

            this.totalLag += (totalLagTime / 1000.0);

            if (totalRunTime > this.slowest || this.slowest == -1.0) {
                this.slowest = totalRunTime;
                this.slowestLag = (totalLagTime / 1000.0);
                this.slowestSplits = runSplits.clone();
                this.slowestLagSplits = lagSplits.clone();
            }

            if (totalRunTime < this.fastest || this.fastest == -1.0) {
                this.fastest = totalRunTime;
                this.fastestLag = (totalLagTime / 1000.0);
                this.fastestSplits = runSplits.clone();
                this.fastestLagSplits = lagSplits.clone();
            }
        }
    }

    public static double getRemaining(String floor, int phase) {
        switch (floor) {
            case "F2":
                return globalF2Stats.getRemaining(phase);
            case "F3":
                return globalF3Stats.getRemaining(phase);
            case "F5":
                return globalF5Stats.getRemaining(phase);
            case "F6":
                return globalF6Stats.getRemaining(phase);
            case "F7":
                return globalF7Stats.getRemaining(phase);
            case "M2":
                return globalM2Stats.getRemaining(phase);
            case "M3":
                return globalM3Stats.getRemaining(phase);
            case "M5":
                return globalM5Stats.getRemaining(phase);
            case "M6":
                return globalM6Stats.getRemaining(phase);
            case "M7":
                return globalM7Stats.getRemaining(phase);
        }
        return 0;
    }

    public static void saveConfig(String floor) {
        switch (floor) {
            case "F2":
                save(globalF2Stats, "DTSF2Stats.dat");
                break;
            case "F3":
                save(globalF3Stats, "DTSF3Stats.dat");
                break;
            case "F5":
                save(globalF5Stats, "DTSF5Stats.dat");
                break;
            case "F6":
                save(globalF6Stats, "DTSF6Stats.dat");
                break;
            case "F7":
                save(globalF7Stats, "DTSF7Stats.dat");
                break;
            case "M2":
                save(globalM2Stats, "DTSM2Stats.dat");
                break;
            case "M3":
                save(globalM3Stats, "DTSM3Stats.dat");
                break;
            case "M5":
                save(globalM5Stats, "DTSM5Stats.dat");
                break;
            case "M6":
                save(globalM6Stats, "DTSM6Stats.dat");
                break;
            case "M7":
                save(globalM7Stats, "DTSM7Stats.dat");
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

    public static F2Stats sessionF2Stats;
    public static F2Stats globalF2Stats;

    public static F3Stats sessionF3Stats;
    public static F3Stats globalF3Stats;

    public static F5Stats sessionF5Stats;
    public static F5Stats globalF5Stats;

    public static F6Stats sessionF6Stats;
    public static F6Stats globalF6Stats;

    public static F7Stats sessionF7Stats;
    public static F7Stats globalF7Stats;

    public static M2Stats sessionM2Stats;
    public static M2Stats globalM2Stats;

    public static M3Stats sessionM3Stats;
    public static M3Stats globalM3Stats;

    public static M5Stats sessionM5Stats;
    public static M5Stats globalM5Stats;

    public static M6Stats sessionM6Stats;
    public static M6Stats globalM6Stats;

    public static M7Stats sessionM7Stats;
    public static M7Stats globalM7Stats;

    public static void init(File file) {
        configFile = file;

        //F2
        File f2Stats = new File(configFile, "DTSF2Stats.dat");

        sessionF2Stats = new F2Stats();
        globalF2Stats = new F2Stats();

        if (!f2Stats.exists()) {
            globalF2Stats.reset();
            save(globalF2Stats, "DTSF2Stats.dat");
        } else {
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(f2Stats.toPath()))) {
                Object obj = in.readObject();
                if (obj instanceof F2Stats) {
                    globalF2Stats = (F2Stats) obj;
                } else {
                    globalF2Stats.reset();
                    save(globalF2Stats, "DTSF2Stats.dat");
                }
            } catch (Exception e) {
                globalF2Stats.reset();
                save(globalF2Stats, "DTSF2Stats.dat");
            }
        }

        //F3
        File f3Stats = new File(configFile, "DTSF3Stats.dat");

        sessionF3Stats = new F3Stats();
        globalF3Stats = new F3Stats();

        if (!f3Stats.exists()) {
            globalF3Stats.reset();
            save(globalF3Stats, "DTSF3Stats.dat");
        } else {
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(f3Stats.toPath()))) {
                Object obj = in.readObject();
                if (obj instanceof F3Stats) {
                    globalF3Stats = (F3Stats) obj;
                } else {
                    globalF3Stats.reset();
                    save(globalF3Stats, "DTSF3Stats.dat");
                }
            } catch (Exception e) {
                globalF3Stats.reset();
                save(globalF3Stats, "DTSF3Stats.dat");
            }
        }

        //F5
        File f5Stats = new File(configFile, "DTSF5Stats.dat");

        sessionF5Stats = new F5Stats();
        globalF5Stats = new F5Stats();

        if (!f5Stats.exists()) {
            globalF5Stats.reset();
            save(globalF5Stats, "DTSF5Stats.dat");
        } else {
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(f5Stats.toPath()))) {
                Object obj = in.readObject();
                if (obj instanceof F5Stats) {
                    globalF5Stats = (F5Stats) obj;
                } else {
                    globalF5Stats.reset();
                    save(globalF5Stats, "DTSF5Stats.dat");
                }
            } catch (Exception e) {
                globalF5Stats.reset();
                save(globalF5Stats, "DTSF5Stats.dat");
            }
        }

        //F6
        File f6Stats = new File(configFile, "DTSF6Stats.dat");

        sessionF6Stats = new F6Stats();
        globalF6Stats = new F6Stats();

        if (!f6Stats.exists()) {
            globalF6Stats.reset();
            save(globalF6Stats, "DTSF6Stats.dat");
        } else {
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(f6Stats.toPath()))) {
                Object obj = in.readObject();
                if (obj instanceof F6Stats) {
                    globalF6Stats = (F6Stats) obj;
                } else {
                    globalF6Stats.reset();
                    save(globalF6Stats, "DTSF6Stats.dat");
                }
            } catch (Exception e) {
                globalF6Stats.reset();
                save(globalF6Stats, "DTSF6Stats.dat");
            }
        }

        //F7
        File f7Stats = new File(configFile, "DTSF7Stats.dat");

        sessionF7Stats = new F7Stats();
        globalF7Stats = new F7Stats();

        if (!f7Stats.exists()) {
            globalF7Stats.reset();
            save(globalF7Stats, "DTSF7Stats.dat");
        } else {
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(f7Stats.toPath()))) {
                Object obj = in.readObject();
                if (obj instanceof F7Stats) {
                    globalF7Stats = (F7Stats) obj;
                } else {
                    globalF7Stats.reset();
                    save(globalF7Stats, "DTSF7Stats.dat");
                }
            } catch (Exception e) {
                globalF7Stats.reset();
                save(globalF7Stats, "DTSF7Stats.dat");
            }
        }

        // M2
        File m2Stats = new File(configFile, "DTSM2Stats.dat");

        sessionM2Stats = new M2Stats();
        globalM2Stats = new M2Stats();

        if (!m2Stats.exists()) {
            globalM2Stats.reset();
            save(globalM2Stats, "DTSM2Stats.dat");
        } else {
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(m2Stats.toPath()))) {
                Object obj = in.readObject();
                if (obj instanceof M2Stats) {
                    globalM2Stats = (M2Stats) obj;
                } else {
                    globalM2Stats.reset();
                    save(globalM2Stats, "DTSM2Stats.dat");
                }
            } catch (Exception e) {
                globalM2Stats.reset();
                save(globalM2Stats, "DTSM2Stats.dat");
            }
        }

        // M3
        File m3Stats = new File(configFile, "DTSM3Stats.dat");

        sessionM3Stats = new M3Stats();
        globalM3Stats = new M3Stats();

        if (!m3Stats.exists()) {
            globalM3Stats.reset();
            save(globalM3Stats, "DTSM3Stats.dat");
        } else {
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(m3Stats.toPath()))) {
                Object obj = in.readObject();
                if (obj instanceof M3Stats) {
                    globalM3Stats = (M3Stats) obj;
                } else {
                    globalM3Stats.reset();
                    save(globalM3Stats, "DTSM3Stats.dat");
                }
            } catch (Exception e) {
                globalM3Stats.reset();
                save(globalM3Stats, "DTSM3Stats.dat");
            }
        }

        // M5
        File m5Stats = new File(configFile, "DTSM5Stats.dat");

        sessionM5Stats = new M5Stats();
        globalM5Stats = new M5Stats();

        if (!m5Stats.exists()) {
            globalM5Stats.reset();
            save(globalM5Stats, "DTSM5Stats.dat");
        } else {
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(m5Stats.toPath()))) {
                Object obj = in.readObject();
                if (obj instanceof M5Stats) {
                    globalM5Stats = (M5Stats) obj;
                } else {
                    globalM5Stats.reset();
                    save(globalM5Stats, "DTSM5Stats.dat");
                }
            } catch (Exception e) {
                globalM5Stats.reset();
                save(globalM5Stats, "DTSM5Stats.dat");
            }
        }

        // M6
        File m6Stats = new File(configFile, "DTSM6Stats.dat");

        sessionM6Stats = new M6Stats();
        globalM6Stats = new M6Stats();

        if (!m6Stats.exists()) {
            globalM6Stats.reset();
            save(globalM6Stats, "DTSM6Stats.dat");
        } else {
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(m6Stats.toPath()))) {
                Object obj = in.readObject();
                if (obj instanceof M6Stats) {
                    globalM6Stats = (M6Stats) obj;
                } else {
                    globalM6Stats.reset();
                    save(globalM6Stats, "DTSM6Stats.dat");
                }
            } catch (Exception e) {
                globalM6Stats.reset();
                save(globalM6Stats, "DTSM6Stats.dat");
            }
        }

        // M7
        File m7Stats = new File(configFile, "DTSM7Stats.dat");

        sessionM7Stats = new M7Stats();
        globalM7Stats = new M7Stats();

        if (!m7Stats.exists()) {
            globalM7Stats.reset();
            save(globalM7Stats, "DTSM7Stats.dat");
        } else {
            try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(m7Stats.toPath()))) {
                Object obj = in.readObject();
                if (obj instanceof M7Stats) {
                    globalM7Stats = (M7Stats) obj;
                } else {
                    globalM7Stats.reset();
                    save(globalM7Stats, "DTSM7Stats.dat");
                }
            } catch (Exception e) {
                globalM7Stats.reset();
                save(globalM7Stats, "DTSM7Stats.dat");
            }
        }
    }
}
