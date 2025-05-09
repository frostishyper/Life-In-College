package com.tom.textrpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PortraitManager {
    private Texture currentPortrait;

    // All portraits as fields
    private Texture maleAfraid;
    private Texture maleHurt;
    private Texture maleStressed;
    private Texture neutralStaring;
    private Texture neutralAway;
    private Texture laughingClosed;
    private Texture laughingStaring;
    private Texture laughingCrying;
    private Texture smilingStaring;
    private Texture speakingStaring;
    private Texture staringBlank;
    private Texture staringConcerned;
    private Texture staringEmotionless;
    private Texture staringEmotionlessAnnoyed;
    private Texture staringSad;
    private final Runnable[] portraitFunctions;
    private int currentIndex = 0;

    public PortraitManager() {
        // Load all portraits
        maleAfraid = load("MaleAfraid.png");
        maleHurt = load("MaleHurt.png");
        maleStressed = load("MaleStressed.png");
        neutralStaring = load("NeutralStaring.png");
        neutralAway = load("NeutralAway.png");
        laughingClosed = load("LaughingClosed.png");
        laughingStaring = load("LaughingStaring.png");
        laughingCrying = load("LaughingCryingpng.png"); 
        smilingStaring = load("SmilingStaring.png");
        speakingStaring = load("SpeakingStaring.png");
        staringBlank = load("StaringBlank.png");
        staringConcerned = load("StaringConcerned.png");
        staringEmotionless = load("StaringEmotionless.png");
        staringEmotionlessAnnoyed = load("StaringEmotionlessAnnoyed.png");
        staringSad = load("StaringSad.png");

        currentPortrait = maleStressed; // Default

        
    {
        // Initialize array with all show functions
        portraitFunctions = new Runnable[]{
            this::showMaleAfraid,
            this::showMaleHurt,
            this::showMaleStressed,
            this::showNeutralStaring,
            this::showNeutralAway,
            this::showLaughingClosed,
            this::showLaughingStaring,
            this::showLaughingCrying,
            this::showSmilingStaring,
            this::showSpeakingStaring,
            this::showStaringBlank,
            this::showStaringConcerned,
            this::showStaringEmotionless,
            this::showStaringEmotionlessAnnoyed,
            this::showStaringSad
        };
    }

    }

    public void nextPortrait() {
        currentIndex = (currentIndex + 1) % portraitFunctions.length;
        portraitFunctions[currentIndex].run();
    }

    private Texture load(String name) {
        Texture tex = new Texture(Gdx.files.internal("portraits/" + name));
        tex.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        return tex;
    }

    // === Individual functions ===
    public void showMaleAfraid() { currentPortrait = maleAfraid; }
    public void showMaleHurt() { currentPortrait = maleHurt; }
    public void showMaleStressed() { currentPortrait = maleStressed; }
    public void showNeutralStaring() { currentPortrait = neutralStaring; }
    public void showNeutralAway() { currentPortrait = neutralAway; }
    public void showLaughingClosed() { currentPortrait = laughingClosed; }
    public void showLaughingStaring() { currentPortrait = laughingStaring; }
    public void showLaughingCrying() { currentPortrait = laughingCrying; }
    public void showSmilingStaring() { currentPortrait = smilingStaring; }
    public void showSpeakingStaring() { currentPortrait = speakingStaring; }
    public void showStaringBlank() { currentPortrait = staringBlank; }
    public void showStaringConcerned() { currentPortrait = staringConcerned; }
    public void showStaringEmotionless() { currentPortrait = staringEmotionless; }
    public void showStaringEmotionlessAnnoyed() { currentPortrait = staringEmotionlessAnnoyed; }
    public void showStaringSad() { currentPortrait = staringSad; }

    public void draw(SpriteBatch batch, float x, float y) {
        if (currentPortrait != null) {
            batch.draw(currentPortrait, x, y);
        }
    }

    public void dispose() {
        maleAfraid.dispose();
        maleHurt.dispose();
        maleStressed.dispose();
        neutralStaring.dispose();
        neutralAway.dispose();
        laughingClosed.dispose();
        laughingStaring.dispose();
        laughingCrying.dispose();
        smilingStaring.dispose();
        speakingStaring.dispose();
        staringBlank.dispose();
        staringConcerned.dispose();
        staringEmotionless.dispose();
        staringEmotionlessAnnoyed.dispose();
        staringSad.dispose();
    }

    
}

