package com.badlogic.D6D;

// Import Statements
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.audio.Music;

// This is Screen in game
// This class is the MAIN MENU
public class MenuScreen implements Screen {

    // Local Variables
    private final MainGame game;                      // Reference to the main game class
    private SpriteBatch batch;                       // Used for drawing 2D elements
    private MonitorScreen monitorScreen;            // Handles animated background          (MonitorScreen.java)
    private ScreenCamera screenCamera;             // Encapsulates camera + viewport logic (ScreenCamera.java)
    private UiDisplay mainTitle;                  // Title with animation                 (UiDisplay.java)
    private UiButton playButton;                 // Play button with hover animation     (UiButton.java)
    private UiButton exitButton;                // Exit button with hover animation     (UiButton.java)
    private Music startupMusic;                // Intro music that plays once

    // Constructor for MenuScreen
    // Initializes the screen with a reference to the main game class
    public MenuScreen(MainGame game) {
        this.game = game;
    }
    

    
    @Override
    public void show() {
        // Load and play intro music (Once)
        startupMusic = Loader.getStartupMusic();
        if (startupMusic != null) {
            startupMusic.setLooping(false);
            startupMusic.play();
        }

        batch = new SpriteBatch();

        // Initialize camera and animated monitor background
        screenCamera = new ScreenCamera();
        monitorScreen = new MonitorScreen(Loader.getAtlas("Ui_Assets.atlas"));

        // Get Atlas From Loader
        TextureAtlas ui = Loader.getAtlas("Ui_Assets.atlas");
        // Intialize Screen Elements
        mainTitle = new UiDisplay(ui,"Ui_MainTitle",4,1.0f,220, 300, 800, 300,screenCamera.getViewport());
        playButton = new UiButton(ui, "BTN_Play", 7, 1.2f, 550, 220, 160, 60, screenCamera.getViewport());
        exitButton = new UiButton(ui, "BTN_Exit", 6, 1.0f, 550, 120, 160, 60, screenCamera.getViewport());

        //Exit Button
        exitButton.setOnClick(() -> Gdx.app.exit());

        // Set up custom cursor input handling (CursorHandler.java)
        Gdx.input.setInputProcessor(new CursorHandler()); 
    }

    @Override
    public void render(float delta) {
        // Clear screen with black background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update camera and input-based animations
        screenCamera.update();
        batch.setProjectionMatrix(screenCamera.getCamera().combined);

        // Update Animation Times
        monitorScreen.update(delta);
        mainTitle.update(delta);
        playButton.update(delta);
        exitButton.update(delta);

        // Clicks
        playButton.setOnClick(() -> {game.setScreen(new PlayGame(game));});
        exitButton.setOnClick(() -> Gdx.app.exit());

        // Draw Call (Screen Elements)
        batch.begin();
        monitorScreen.render(batch);
        mainTitle.render(batch);
        playButton.render(batch);
        exitButton.render(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Ensure viewport scales correctly when screen size changes
        screenCamera.resize(width, height);
    }

    @Override
    public void hide() {
        // Stop music if screen is hidden or changed
        if (startupMusic != null && startupMusic.isPlaying()) {
            startupMusic.stop();
        }
    }

    @Override
    public void dispose() {
        // Release music resources when the screen is disposed
        if (startupMusic != null) {
            batch.dispose();
            startupMusic.dispose();
        }
    }

    // Unused methods 
    @Override public void pause() {}     
    @Override public void resume() {}    
}
