package com.mmdev.meowmayo.config;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.mmdev.meowmayo.config.settings.*;

public class ConfigSettings {
    private static final List<Setting> expected = new ArrayList<>(); // stored settings for validation
    private static final String[] removed = {}; // removed settings
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

    private static void add(Setting setting) {
        expected.add(setting);
    }

    private static void defaults() {
        // new settings must go at the end! removed settings go in `removed` array
        add(new ToggleSetting("Storage Command", "Allows the usage of a command to open storage", "General", "Storage", false));
        add(new TextSetting("Allowed Backpacks", "Backpacks to be opened with command | Separate in order with a comma separated list i.e. \"13,9,18\" - opens in order 13, 9, 18", "General", "Storage", ""));
        add(new ToggleSetting("Bonzo's Mask Announcement", "Announces in party chat when Bonzo's Mask is used", "General", "Invincibility Items", false));
        add(new ToggleSetting("Bonzo's Mask Title", "Shows a title on screen when Bonzo's Mask is used", "General", "Invincibility Items", false));
        add(new ToggleSetting("Phoenix Pet Announcement", "Announces in party chat when Phoenix Pet is used", "General", "Invincibility Items", false));
        add(new ToggleSetting("Phoenix Pet Title", "Shows a title on screen when Phoenix Pet is used", "General", "Invincibility Items", false));
        add(new ToggleSetting("Spirit Mask Announcement", "Announces in party chat when Spirit Mask is used", "General", "Invincibility Items", false));
        add(new ToggleSetting("Spirit Mask Title", "Shows a title on screen when Spirit Mask is used", "General", "Invincibility Items", false));
        add(new ToggleSetting("Announce Mana", "Annouces mana used from Endstone Sword Ability", "Kuudra", "", false));
        add(new ToggleSetting("Announce Fresh", "Annouces when user procs the Fresh Tools ability", "Kuudra", "", false));
        add(new ToggleSetting("Average Kuudra Run Time Tracker", "Tracks average kuudra run time per session | /kuudratime to view | Refreshes on game restart - /resetkuudratime to force reset", "Kuudra", "Run Tracker", false));
        add(new ToggleSetting("Supplies Info", "Tracks who get supplies and when", "Kuudra", "Run Tracker", false));
        add(new ToggleSetting("Supplies Overview", "Sends a detailed message after supplies phase containing supplies overview", "Kuudra", "Run Tracker", false));
        add(new ToggleSetting("Fresh Info", "Tracks who freshes and when", "Kuudra", "Run Tracker", false));
        add(new ToggleSetting("Fresh Overview", "Sends a detailed message after build phase containing fresh overview", "Kuudra", "Run Tracker", false));
        add(new ToggleSetting("Lag Timing", "Sends message in party chat about how much lag occurred in run", "Kuudra", "Run Tracker", false));
        add(new ToggleSetting("Reset Tracker on Party Change", "Resets session tracker when party changes", "Kuudra", "Run Tracker", false));
        add(new ToggleSetting("Pre Tracker", "Tracks who misses their pre", "Kuudra", "Run Tracker", false));
        add(new IntSliderSetting("Pre Tracker Leeway", "The amount of extra time given for pre tracking. Default is 8 seconds.", "Kuudra", "Run Tracker", 0, 0, 3));
        add(new ToggleSetting("No Supply", "Annouces when your pre is missing", "Kuudra", "Supplies", false));
        add(new ToggleSetting("Pearl Timer", "Adds a timer to pickup that tells when to throw pearl", "Kuudra", "Supplies", false));
        add(new ToggleSetting("Pearl Waypoint", "Draws a box on where to aim to throw pearls | Red -> Green", "Kuudra", "Supplies", false));
        add(new FloatSliderSetting("Pearl Waypoint Size", "Pearl Waypoint Size", "Kuudra", "Supplies", 8.0f, 1.0f, 20.0f));
        add(new ToggleSetting("Second Pearl Waypoint", "Shows where to throw second pearl to land at second crate", "Kuudra", "Supplies", false));
        add(new ToggleSetting("Fresh Alert", "Displays a title when Fresh Tools Activates", "Kuudra", "", false));
        add(new ToggleSetting("Danger Block Warning", "Warns you when standing on a dangerous block in final phase", "Kuudra", "", false));
        add(new ToggleSetting("Last Alive", "Warns you and party when a player is last standing", "Kuudra", "", false));
        add(new ToggleSetting("Stun Ping", "Alerts you when you when to throw pearl for stun", "Kuudra", "Stun", false));
        add(new FloatSliderSetting("Stun Ping HP", "What HP To alert on", "Kuudra", "Stun", 30.0f, 30.0f, 80.0f));
        add(new TextSetting("Stun Ping Message", "Message to display on stun ping", "Kuudra", "Stun", "Throw Pearl!"));
        add(new ToggleSetting("Rend Pull Damage", "Shows how much rend damage is dealt", "Kuudra", "Rend", false));
        add(new ToggleSetting("Rend Bone Info", "Shows when various Bonemerang events happen", "Kuudra", "Rend", false));
        add(new ToggleSetting("Rend Alert", "Shows title when back bone hits", "Kuudra", "Rend", false));
        add(new ToggleSetting("Compact Pearls", "Automatically gets enough pearls to compact and put in hotbar (requires enchanted pearls in personal compactor)", "General", "Pearls", false));
        add(new ToggleSetting("Toggle Sprint", "Makes player always sprint", "General", "Toggle Sprint", false));
        add(new ToggleSetting("Party Commands", "Enables Party Commands", "Party Commands", "", false));
        add(new TextSetting("Party Commands Prefix", "Prefix used for party commands | Highly recommended to not keep blank", "Party Commands", "", "!"));
        add(new ToggleSetting("Party Help Command", "Enables Help Party Command", "Party Commands", "", false));
        add(new ToggleSetting("All Invite Command", "Enables All Invite Party Command", "Party Commands", "General", false));
        add(new ToggleSetting("Warp Command", "Enables Warp Party Command", "Party Commands", "General", false));
        add(new ToggleSetting("Invite Command", "Enables Invite Party Command", "Party Commands", "General", false));
        add(new ToggleSetting("Party Transfer Command", "Enables Party Transfer Party Command", "Party Commands", "General", false));
        add(new ToggleSetting("Party Transfer Whitelist", "Makes Party Transfer Whitelist Only", "Party Commands", "General", false));
        add(new ToggleSetting("Party Transfer Me Command", "Enables Party Transfer Me Party Command", "Party Commands", "General", false));
        add(new ToggleSetting("Party Transfer Me Whitelist", "Makes Party Transfer Me Whitelist Only", "Party Commands", "General", false));
        add(new ToggleSetting("Sellout Command", "Enables Sellout Party Command", "Party Commands", "General", false));
        add(new ToggleSetting("Catacombs Entrance Command", "Enables Catacombs Entrance Party Command", "Party Commands", "Catacombs", false));
        add(new ToggleSetting("Master Catacombs Entrance Command", "Enables Master Catacombs Entrance Party Command", "Party Commands", "Catacombs", false));
        add(new ToggleSetting("Kuudra Entrance Command", "Enables Kuudra Entrance Party Command", "Party Commands", "Kuudra", false));
        add(new ToggleSetting("Kuudra Time Stats Command", "Enables Kuudra Time Stats Party Command", "Party Commands", "Kuudra", false));
        add(new ToggleSetting("Coinflip Command", "Enables Coinflip Party Command", "Party Commands", "Fun", false));
        add(new ToggleSetting("Dice Command", "Enables Dice Party Command", "Party Commands", "Fun", false));
    }

    private static int readInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            return 0;
        }
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
                if (contents.length != 3) return; // broken

                if (currLine == expected.size()) return;

                Setting s = expected.get(currLine);
                if (contents[0].equals(s.getTitle())) {
                    switch (readInt(contents[1])) {
                        case 1:
                            if (!(s instanceof ToggleSetting)) return;
                            s.setValue(Boolean.parseBoolean(contents[2]));
                            break;
                        case 2:
                            if (!(s instanceof TextSetting)) return;
                            s.setValue(contents[2]);
                            break;
                        case 3:
                            if (!(s instanceof IntSliderSetting)) return;
                            s.setValue(Integer.parseInt(contents[2]));
                            break;
                        case 4:
                            if (!(s instanceof FloatSliderSetting)) return;
                            s.setValue(Float.parseFloat(contents[2]));
                            break;
                        default:
                            return;
                    }
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
                writer.write("// Hello! This is the MeowMayo config file.\n");
                writer.write("// Do not edit unless you know what you are doing.\n");
                writer.write("// Invalid edits may cause all config settings to be lost.\n");

                for (Setting setting : expected) {
                    String line = null;
                    if (setting instanceof ToggleSetting) line = setting.getTitle() + ":1:" + setting.getValue();
                    else if (setting instanceof TextSetting) line = setting.getTitle() + ":2:" + setting.getValue();
                    else if (setting instanceof IntSliderSetting) line = setting.getTitle() + ":3:" + setting.getValue();
                    else if (setting instanceof FloatSliderSetting) line = setting.getTitle() + ":4:" + setting.getValue();

                    if (line != null) writer.write(line + "\n");
                }
            }

            Files.move(tempFile.toPath(), configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Saved config file");
        } catch (IOException e) {
            e.printStackTrace();

            if (tempFile.exists()) tempFile.delete();
        }
    }

    // Public method to edit a setting safely
    public static void edit(String title, Object value) {
        for (Setting s : expected) {
            if (s.getTitle().equals(title)) {
                s.setValue(value);
                scheduleWrite();
                break;
            }
        }
    }

    public static List<SettingCategory> sortSettings() {
        Map<String, SettingCategory> categoryMap = new HashMap<>();

        // Build categories and subcategories
        for (Setting s : expected) {
            String categoryName = s.getCategory();
            String subcategoryName = s.getSubcategory();

            // Get or create category
            SettingCategory category = categoryMap.computeIfAbsent(categoryName, SettingCategory::new);

            if (subcategoryName == null || subcategoryName.isEmpty()) {
                // Miscellaneous setting
                category.addSetting(s);
            } else {
                // Get or create subcategory
                SettingSubcategory subcategory = null;
                for (SettingSubcategory sc : category.getSubcategories()) {
                    if (sc.getName().equalsIgnoreCase(subcategoryName)) {
                        subcategory = sc;
                        break;
                    }
                }
                if (subcategory == null) {
                    subcategory = new SettingSubcategory(subcategoryName);
                    category.addSubcategory(subcategory);
                }

                subcategory.addSetting(s);
            }
        }

        // Sort everything alphabetically
        for (SettingCategory cat : categoryMap.values()) {
            cat.getMiscSettings().sort(Comparator.comparing(Setting::getTitle, String.CASE_INSENSITIVE_ORDER));
            cat.getSubcategories().sort(Comparator.comparing(SettingSubcategory::getName, String.CASE_INSENSITIVE_ORDER));
            for (SettingSubcategory sc : cat.getSubcategories()) {
                sc.getSettings().sort(Comparator.comparing(Setting::getTitle, String.CASE_INSENSITIVE_ORDER));
            }
        }

        // Return sorted categories
        List<SettingCategory> sortedCategories = new ArrayList<>(categoryMap.values());
        sortedCategories.sort(Comparator.comparing(SettingCategory::getName, String.CASE_INSENSITIVE_ORDER));

        return sortedCategories;
    }

    public static Setting getSetting(String name) {
        for (Setting s : expected) {
            if (s.getTitle().equals(name)) {
                return s;
            }
        }
        return null;
    }
}