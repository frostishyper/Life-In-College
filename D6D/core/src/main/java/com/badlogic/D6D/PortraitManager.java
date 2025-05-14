package com.badlogic.D6D;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PortraitManager {
    private TextureAtlas playerPortraits;
    private TextureRegion currentPortrait;

    // Constructor to initialize with the playerPortraits atlas
    public PortraitManager(TextureAtlas playerPortraits) {
        this.playerPortraits = playerPortraits;
        this.currentPortrait = getDefaultPortrait(); // Set default portrait initially
    }

    // Getter for playerPortraits (optional)
    public TextureAtlas getPlayerPortraits() {
        return playerPortraits;
    }

    // Method to get the default portrait
    public TextureRegion getDefaultPortrait() {
        return playerPortraits.findRegion("MaleNeutralAway");
    }

    // Method to get the stressed portrait
    public TextureRegion getStressedPortrait() {
        return playerPortraits.findRegion("MaleStressed");
    }

    // Method to update the portrait based on player's health and sanity
    public void updatePortrait(int currentHealth, int currentSanity) {
        if (currentHealth == 1 && currentSanity == 1) {
            currentPortrait = getStressedPortrait(); // Switch to stressed portrait
        } else {
            currentPortrait = getDefaultPortrait(); // Switch back to default portrait
        }
    }

    // Method to get the current portrait
    public TextureRegion getCurrentPortrait() {
        return currentPortrait;
    }
}
