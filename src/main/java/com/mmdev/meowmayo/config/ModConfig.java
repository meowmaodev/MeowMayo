package com.mmdev.meowmayo.config;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;

import com.mmdev.meowmayo.config.settings.*;

public class ModConfig {

    private static File configFile;

    private static List<SettingCategory> settings = new ArrayList<>();

    /**
     * Initialize the configuration. Should be called from the main mod class preInit.
     * @param mcConfigDir The Minecraft config directory (passed from FMLPreInitializationEvent)
     */
    public static void init(File mcConfigDir) {
        // Ensure the config directory exists
        if (!mcConfigDir.exists()) mcConfigDir.mkdirs();

        // Use a file inside the config folder
        configFile = new File(mcConfigDir, "meowmayo.meow");

        if (!configFile.exists()) { // ensure the user has a config file to read
            try {
                configFile.createNewFile();
            } catch (IOException ignored) {

            }
        }

        load();
    }

    /** Load the configuration from the file */
    public static void load() {
        ConfigSettings.init(configFile);
        settings = ConfigSettings.sortSettings();
    }

    public static List<SettingCategory> getSettings() {
        return settings;
    }

    /** Save the configuration to the file */
    public static void edit(String setting, Object value) {
        ConfigSettings.edit(setting, value); // i love objects
    }
}
