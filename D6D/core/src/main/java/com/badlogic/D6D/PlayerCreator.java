package com.badlogic.D6D;

// Import Statements
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class PlayerCreator implements Screen {

    private final MainGame game;                           // Reference to the main game class
    private SpriteBatch batch;                            // Used for drawing 2D elements
    private MonitorScreen monitorScreen;                 // Handles animated background         (MonitorScreen.java)
    private ScreenCamera screenCamera;                  // Encapsulates camera + viewport logic (ScreenCamera.java)
    private final Array<UiDisplay> uiElements = new Array<>(); // Array to hold elements handled by (UiDisplay.java)

    // Constructor for PlayGame screen
    public PlayerCreator(MainGame game) {
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
        TextureAtlas player = Loader.getAtlas("PlayerPortraits.atlas");

        // Initialize Screen Elements

        // Portrait & Frame
        uiElements.add(new UiDisplay(ui, "Ui_Portrait_Frame", 1, 1.0f, 50, 500, 150, 150, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(player, "MaleNeutralAway", 1, 1.0f, 70, 520, 120, 120, screenCamera.getViewport()));

        //Stat Frames
        uiElements.add(new UiDisplay(ui, "BTN_Select", 2, 1.0f, 230, 580, 200, 70, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "BTN_Select", 1, 1.0f, 230, 500, 200, 70, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "BTN_Select", 1, 1.0f, 230, 420, 200, 70, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "BTN_Select", 1, 1.0f, 230, 340, 200, 70, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "BTN_Select", 1, 1.0f, 230, 260, 200, 70, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "BTN_Select", 1, 1.0f, 230, 180, 200, 70, screenCamera.getViewport()));

        // Stat Icons
        uiElements.add(new UiDisplay(ui, "STAT_Intel", 1, 1.0f, 240, 590, 50, 50, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "STAT_Mental", 1, 1.0f, 240, 510, 50, 50, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "STAT_Constitution", 1, 1.0f, 240, 430, 50, 50, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "STAT_Swiftness", 1, 1.0f, 240, 350, 50, 50, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "STAT_Courage", 1, 1.0f, 240, 270, 50, 50, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "STAT_Charisma", 1, 1.0f, 240, 190, 50, 50, screenCamera.getViewport()));

    }

    @Override
    public void render(float delta) {
        // Clear screen with black background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update Animation Times
        monitorScreen.update(delta);
        for (UiDisplay element : uiElements) element.update(delta);

        // Draw Call (Screen Elements)
        batch.begin();
        monitorScreen.render(batch);
        for (UiDisplay element : uiElements) element.render(batch);
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
