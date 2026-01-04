package com.mmdev.meowmayo.config.settings;

import java.util.ArrayList;
import java.util.List;

public class SettingCategory {
    private String name;
    private List<Setting> miscSettings; // Miscellaneous settings always shown first
    private List<SettingSubcategory> subcategories; // Grouped subcategories

    public SettingCategory(String name) {
        this.name = name;
        this.miscSettings = new ArrayList<>();
        this.subcategories = new ArrayList<>();
    }

    public String getName() { return name; }
    public List<Setting> getMiscSettings() { return miscSettings; }
    public List<SettingSubcategory> getSubcategories() { return subcategories; }

    // Add a miscellaneous setting
    public void addSetting(Setting setting) {
        miscSettings.add(setting);
    }

    // Add a subcategory
    public void addSubcategory(SettingSubcategory category) {
        subcategories.add(category);
    }

    // Search for a setting by title (recursive)
    public Setting findSetting(String title) {
        for (Setting s : miscSettings) {
            if (s.getTitle().equalsIgnoreCase(title)) return s;
        }
        for (SettingSubcategory sub : subcategories) {
            Setting found = sub.findSetting(title);
            if (found != null) return found;
        }
        return null;
    }
}
