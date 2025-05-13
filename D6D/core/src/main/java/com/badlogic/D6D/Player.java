package com.badlogic.D6D;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private static Player instance;

    public String name = "Steve";

    private final Map<String, Integer> stats = new HashMap<>();
    private int baseHealth, currentHealth;
    private int baseSanity, currentSanity;

    private Player() {
        // Init base stats
        for (String stat : new String[] {
                "Intelligence", "Mental", "Constitution", "Swiftness", "Courage", "Charisma"
        }) stats.put(stat, 2);

        recalculateDerivedStats();
    }

    public static Player getInstance() {
        if (instance == null) instance = new Player();
        return instance;
    }

    // Modify stats
    public void setStat(String stat, int value) {
        if (stats.containsKey(stat)) {
            int oldBaseHealth = baseHealth;
            int oldBaseSanity = baseSanity;

            stats.put(stat, value);
            recalculateDerivedStats();

            // Top off added bonus only
            currentHealth += (baseHealth - oldBaseHealth);
            currentSanity += (baseSanity - oldBaseSanity);

            clampVitals();
        }
    }

    public void adjustStat(String stat, int delta) {
        setStat(stat, getStat(stat) + delta);
    }

    public int getStat(String stat) {
        return stats.getOrDefault(stat, 0);
    }

    public Map<String, Integer> getAllStats() {
        return new HashMap<>(stats);
    }

    // Derived stat logic
    private void recalculateDerivedStats() {
        baseHealth = Math.max(3, stats.get("Constitution") / 2);
        baseSanity = Math.max(3, stats.get("Mental") / 2);

        // Set current to base if uninitialized
        if (currentHealth == 0) currentHealth = baseHealth;
        if (currentSanity == 0) currentSanity = baseSanity;

        clampVitals();
    }

    private void clampVitals() {
        currentHealth = Math.min(currentHealth, baseHealth);
        currentSanity = Math.min(currentSanity, baseSanity);
    }

    // Getters
    public int getBaseHealth()    { return baseHealth; }
    public int getCurrentHealth() { return currentHealth; }
    public int getBaseSanity()    { return baseSanity; }
    public int getCurrentSanity() { return currentSanity; }

    // Change current values (used by game events, damage, healing, etc.)
    public void modifyHealth(int delta) {
        currentHealth = Math.max(0, Math.min(currentHealth + delta, baseHealth));
    }

    public void modifySanity(int delta) {
        currentSanity = Math.max(0, Math.min(currentSanity + delta, baseSanity));
    }

    public void setCurrentHealth(int value) {
        currentHealth = Math.max(0, Math.min(value, baseHealth));
    }

    public void setCurrentSanity(int value) {
        currentSanity = Math.max(0, Math.min(value, baseSanity));
    }
}
