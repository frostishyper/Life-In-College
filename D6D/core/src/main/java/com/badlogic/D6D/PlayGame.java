package com.badlogic.D6D;

// Import Statements
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class PlayGame implements Screen {

    private final MainGame game;                       // Reference to the main game class
    private SpriteBatch batch;                        // Used for drawing 2D elements
    private MonitorScreen monitorScreen;             // Handles animated background          (MonitorScreen.java)
    private ScreenCamera screenCamera;              // Encapsulates camera + viewport logic (ScreenCamera.java)

    // Constructor for PlayGame screen
    public PlayGame(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        // Initialize camera and animated monitor background
        screenCamera = new ScreenCamera();
        monitorScreen = new MonitorScreen(Loader.getAtlas("Ui_Assets.atlas"));

        // Get Atlas From Loader
        TextureAtlas ui = Loader.getAtlas("Ui_Assets.atlas");
        // Initialize Screen Elements
        
    }

    @Override
    public void render(float delta) {
        // Clear screen with black background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update Animation Times
        monitorScreen.update(delta);

        // Draw Call (Screen Elements)
        batch.begin();
        monitorScreen.render(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Ensure viewport scales correctly when screen size changes
        screenCamera.resize(width, height);
    }

    @Override
    public void hide() {
        // Dispose of resources if needed
    }

    @Override
    public void dispose() {
        // Release resources when screen is disposed
        if (batch != null) {
            batch.dispose();
        }
    }


    // Unused methods
    @Override public void pause() {}     // Not used but required by Screen interface
    @Override public void resume() {}    // Not used but required by Screen interface
}
