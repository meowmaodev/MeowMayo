package com.mmdev.meowmayo.config.settings;

import java.util.ArrayList;
import java.util.List;

public class SettingSubcategory {
    private String name;
    private List<Setting> settings;

    public SettingSubcategory(String name) {
        this.name = name;
        this.settings = new ArrayList<>();
    }

    public String getName() { return name; }

    public List<Setting> getSettings() { return settings; }

    // Add a miscellaneous setting
    public void addSetting(Setting setting) {
        settings.add(setting);
    }

    // Search for a setting by title (recursive)
    public Setting findSetting(String title) {
        for (Setting s : settings) {
            if (s.getTitle().equalsIgnoreCase(title)) return s;
        }
        return null;
    }
}
