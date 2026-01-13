package com.mmdev.meowmayo.config;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.mmdev.meowmayo.config.settings.*;

public class HudManager {
    private static final List<HudElementSetting> expected = new ArrayList<>();
    private static final String[] removed = {};
    private static File configFile;

    // Write scheduling
    private static long lastWriteTime = 0;
    private static ScheduledFuture<?> scheduledWrite;
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final long WRITE_DELAY_MS = 10_000;

    public static void init(File file) {
        configFile = file;
        defaults();
        read();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (scheduledWrite != null && !scheduledWrite.isDone()) {
                scheduledWrite.cancel(false);
            }
            write();
            scheduler.shutdownNow();
        }));
    }

    private static void add(HudElementSetting setting) {
        expected.add(setting);
    }

    private static void defaults() {
        add(new HudElementSetting("Kuudra Time Splits", "Kuudra Splits\nSpawn: 0.0s\nCrates: 0.0s"));
        add(new HudElementSetting("Dungeon Time Splits", "Dungeon Splits\nBlood Rush: 0.0s\nCamp: 0.0s"));
        add(new HudElementSetting("Invincibility Title", "Bonzo's Mask Procced!"));
        add(new HudElementSetting("Damage Tick Warning", "DAMAGE TICK INCOMING!"));
        add(new HudElementSetting("Ragnarock Title", "Gained 1035 strength from Ragnarock"));
    }

    private static boolean isDepricated(String setting) {
        for (String s : removed) {
            if (s.equals(setting)) return true;
        }
        return false;
    }

    private static void read() {
        if (configFile == null) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            int currLine = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("//")) continue; // skip comments

                String[] contents = line.split(":", -1);
                if (contents.length != 4) return; // broken

                if (currLine == expected.size()) return;

                HudElementSetting s = expected.get(currLine);
                if (contents[0].equals(s.getTitle())) {
                    try {
                        int xPos = Integer.parseInt(contents[1]);
                        int yPos = Integer.parseInt(contents[2]);
                        float scale = Float.parseFloat(contents[3]);

                        s.setX(xPos);
                        s.setY(yPos);
                        s.setScale(scale);
                    } catch (Exception ignored) {}
                } else if (!isDepricated(contents[0])) {
                    return;
                }
                currLine++;
            }
        } catch (IOException ignored) {
        }

        scheduleWrite();
    }

    // Schedule a write respecting the 10-second grace period
    private static synchronized void scheduleWrite() {
        long now = System.currentTimeMillis();
        long timeSinceLastWrite = now - lastWriteTime;

        if (timeSinceLastWrite >= WRITE_DELAY_MS) {
            write();
        } else {
            if (scheduledWrite != null && !scheduledWrite.isDone()) {
                scheduledWrite.cancel(false);
            }
            long delay = WRITE_DELAY_MS - timeSinceLastWrite;
            scheduledWrite = scheduler.schedule(ConfigSettings::write, delay, TimeUnit.MILLISECONDS);
        }
    }

    protected static synchronized void write() {
        lastWriteTime = System.currentTimeMillis();
        File tempFile = new File(configFile.getAbsolutePath() + ".tmp");
        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                writer.write("// Hello! This is the MeowMayo hud locations file.\n");
                writer.write("// Do not edit unless you know what you are doing.\n");
                writer.write("// Invalid edits may cause all config settings to be lost.\n");

                for (HudElementSetting setting : expected) {
                    String line = setting.getTitle() + ":" + setting.getX() + ":" + setting.getY() + ":" + setting.getScale();
                    writer.write(line + "\n");
                }
            }

            Files.move(tempFile.toPath(), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Saved config file");
        } catch (IOException e) {
            e.printStackTrace();

            if (tempFile.exists()) tempFile.delete();
        }
    }

    public static List<HudElementSetting> getLocations() {
        return expected;
    }

    // Public method to edit a setting safely
    public static void editLocation(String title, int x, int y) {
        for (HudElementSetting s : expected) {
            if (s.getTitle().equals(title)) {
                s.setX(x);
                s.setY(y);
                scheduleWrite();
                break;
            }
        }
    }

    public static void editScale(String title, float scale) {
        for (HudElementSetting s : expected) {
            if (s.getTitle().equals(title)) {
                s.setScale(scale);
                scheduleWrite();
                break;
            }
        }
    }

    public static HudElementSetting getLocation(String name) {
        for (HudElementSetting s : expected) {
            if (s.getTitle().equals(name)) {
                return s;
            }
        }
        return null;
    }
}