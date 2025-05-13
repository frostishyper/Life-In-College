package com.badlogic.D6D;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private static Player instance;

    public String name;

    private final Map<String, Integer> stats;

    private Player() {
        name = "Steve";

        stats = new HashMap<>();
        stats.put("Intelligence", 5);
        stats.put("Mental", 5);
        stats.put("Constitution", 5);
        stats.put("Swiftness", 5);
        stats.put("Courage", 5);
        stats.put("Charisma", 5);
    }

    public static Player getInstance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }

    // Adjust stat value by amount (+/-)
    public void adjustStat(String statName, int amount) {
        if (stats.containsKey(statName)) {
            stats.put(statName, stats.get(statName) + amount);
        }
    }

    // Set stat to a specific value
    public void setStat(String statName, int value) {
        if (stats.containsKey(statName)) {
            stats.put(statName, value);
        }
    }

    // Get current stat value
    public int getStat(String statName) {
        return stats.getOrDefault(statName, 0);
    }

    // Read-only copy of all stats
    public Map<String, Integer> getAllStats() {
        return new HashMap<>(stats);
    }
}
