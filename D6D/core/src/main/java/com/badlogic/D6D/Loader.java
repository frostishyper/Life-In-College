package com.badlogic.D6D;

//Import Statements
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

// Static loader utility class
// This Class is for loading assets into memory
// Load all other future assets using this class
public class Loader {

    // Static To Allow Global Access without instantiation
    // AssetManager is a built in LibGDX class for asset management and loading
    // Refer to documentation for more information
    public static AssetManager assetManager;

    // Call On Game Start
    // Loads all assets into memory
    public static void init() {
        assetManager = new AssetManager();

        // Point to atlas files to load
        assetManager.load("Buttons.atlas", TextureAtlas.class);
        assetManager.load("General.atlas", TextureAtlas.class);
        assetManager.load("Stats.atlas", TextureAtlas.class);
    }

    // Call This repeatedly
     public static boolean update() {
        // Returns true when all assets are done loading
        return assetManager.update(); 
    }

    // Access Loaded Assets
   public static TextureAtlas getAtlas(String name) {
        return assetManager.get(name, TextureAtlas.class);
    }

    // Call This when the game is closed
    // This will dispose of all assets and free up memory
    public static void dispose() {
        assetManager.dispose();
    }


}
