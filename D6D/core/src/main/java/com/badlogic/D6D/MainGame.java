package com.badlogic.D6D;

//Import Statements
import com.badlogic.gdx.Game;  

//Root Game Class
public class MainGame extends Game {
    
    @Override
    public void create() {
        Loader.init(); // Calls the init method in the Loader class to load assets
        while (!Loader.update()) {
            // Wait for assets to load
        }

        // Goes to main menu screen after assets are loaded
        setScreen(new MenuScreen(this)); 
    }

    @Override
    public void dispose() {
        Loader.dispose(); // Disposes of Loader class to free up memory
        super.dispose(); 
    }

}
