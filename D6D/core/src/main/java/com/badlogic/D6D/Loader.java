package com.badlogic.D6D;

import com.badlogic.gdx.Gdx;
//Import Statements
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

// Static loader utility class
// This Class is for loading assets into memory
// Load all other future assets using this class
public class Loader {

    // Static To Allow Global Access without instantiation
    // AssetManager is a built in LibGDX class for asset management and loading
    // Refer to documentation for more information
    public static AssetManager assetManager;
    public static Sound clickSound; 
    public static Music startupMusic; 

    // Call On Game Start
    // Loads assets into memory
    public static void init() {
        assetManager = new AssetManager();

        // Point to assets to load (Atlases and SFX files)
        assetManager.load("Ui_Assets.atlas", TextureAtlas.class);
        assetManager.load("PlayerPortraits.atlas", TextureAtlas.class);
        assetManager.load("Click.wav", Sound.class);
        assetManager.load("Startup.ogg", Music.class);
    }

    // Call This repeatedly
     public static boolean update() {
        // Returns true when all assets are done loading
        return assetManager.update(); 
    }

    public static void prepareSFXAssets() {
        clickSound = assetManager.get("Click.wav", Sound.class);
        startupMusic = assetManager.get("Startup.ogg", Music.class);
    }

    // Access Loaded Assets
   public static TextureAtlas getAtlas(String name) {
        return assetManager.get(name, TextureAtlas.class);
    }

    // Access the click sound
    public static Sound getClickSound() {
        return clickSound;
    }

    // Access the startup music
    public static Music getStartupMusic() {
        return startupMusic;
    }

    // Call This when the game is closed
    // This will dispose of all assets and free up memory
    public static void dispose() {
        assetManager.dispose();
         if (startupMusic != null) {
            startupMusic.dispose(); 
        }
        if (clickSound != null) {
            clickSound.dispose(); 
        }
    }


}
