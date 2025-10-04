package com.mmdev.meowmayo.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PartyCommandListUtils {

    private static File whitelistFile;
    private static File blacklistFile;

    public static List<String> whitelist = new ArrayList<>();
    public static List<String> blacklist = new ArrayList<>();

    public static void init(File configDir) {
        // we dont check if the dir exists because the previous function does it :pray:
        whitelistFile = new File(configDir, "PartyWhitelist.txt");
        blacklistFile = new File(configDir, "PartyBlacklist.txt");
        loadLists();
    }

    private static void loadLists() {
        whitelist = loadListFromFile(whitelistFile);
        blacklist = loadListFromFile(blacklistFile);
    }

    private static List<String> loadListFromFile(File file) {
        List<String> list = new ArrayList<>();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !list.contains(line)) {
                    list.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static void saveListToFile(File file, List<String> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (String player : list) {
                bw.write(player);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean addToWhitelist(String player) {
        if (!whitelist.contains(player)) {
            whitelist.add(player);
            saveListToFile(whitelistFile, whitelist);
            return true;
        }
        return false;
    }

    public static boolean removeFromWhitelist(String player) {
        if (whitelist.remove(player)) {
            saveListToFile(whitelistFile, whitelist);
            return true;
        }
        return false;
    }

    public static boolean addToBlacklist(String player) {
        if (!blacklist.contains(player)) {
            blacklist.add(player);
            saveListToFile(blacklistFile, blacklist);
            return true;
        }
        return false;
    }

    public static boolean removeFromBlacklist(String player) {
        if (blacklist.remove(player)) {
            saveListToFile(blacklistFile, blacklist);
            return true;
        }
        return false;
    }
}
