package com.badlogic.D6D;

// Import Statements
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class PlayerCreator implements Screen {

    private final MainGame game;
    private SpriteBatch batch;
    private MonitorScreen monitorScreen;
    private ScreenCamera screenCamera;
    private final Array<UiDisplay> uiElements = new Array<>();
    private int selectedStatIndex = 0;
    private BitmapFont font;

    // Stat Names
    private final String[] statNames = {
            "Intelligence", "Mental", "Constitution", "Swiftness", "Courage", "Charisma"
    };

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
        uiElements
                .add(new UiDisplay(player, "MaleNeutralAway", 1, 1.0f, 70, 520, 120, 120, screenCamera.getViewport()));

        // Stat Frames
        for (int i = 0; i < 6; i++) {
            int y = 580 - i * 80;
            uiElements.add(
                    new UiDisplay(ui, "BTN_Select", i == 0 ? 2 : 1, 1.0f, 230, y, 200, 70, screenCamera.getViewport()));
        }

        // Stat Icons
        String[] iconNames = { "STAT_Intel", "STAT_Mental", "STAT_Constitution", "STAT_Swiftness", "STAT_Courage",
                "STAT_Charisma" };
        for (int i = 0; i < iconNames.length; i++) {
            int y = 590 - i * 80;
            uiElements.add(new UiDisplay(ui, iconNames[i], 1, 1.0f, 240, y, 50, 50, screenCamera.getViewport()));
        }

        // + / - Buttons
        for (int i = 0; i < 6; i++) {
            int y = 590 - (i * 80);
            uiElements.add(new UiDisplay(ui, "BTN_Plus", 1, 1.0f, 330, y, 40, 40, screenCamera.getViewport()));
            uiElements.add(new UiDisplay(ui, "BTN_Minus", 1, 1.0f, 380, y, 40, 40, screenCamera.getViewport()));
        }

        font = new BitmapFont();
        font.getData().setScale(1.80f);
    }

    @Override
    public void render(float delta) {
        // Clear screen with black background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update Animation Times
        monitorScreen.update(delta);
        for (UiDisplay element : uiElements)
            element.update(delta);

        // Draw Call (Screen Elements)
        batch.begin();
        monitorScreen.render(batch);
        for (UiDisplay element : uiElements)
            element.render(batch);

        // Draw stat values
        int[] statYPositions = { 590, 510, 430, 350, 270, 190 };
        Player player = Player.getInstance();

        for (int i = 0; i < statNames.length; i++) {
            if (i == selectedStatIndex) {
                font.setColor(1, 1, 0, 1); // Highlight selected (NOt Connected to Animation)
            } else {
                font.setColor(1, 1, 1, 1); // White
            }
            int value = player.getStat(statNames[i]);
            font.draw(batch, String.valueOf(value), 295, statYPositions[i] + 35);
        }

        batch.end();

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY(); // Flip Y

            Player player = Player.getInstance();

            for (int i = 0; i < statNames.length; i++) {
                int yStart = 590 - (i * 80);
                int yEnd = yStart + 50;

                // Select stat
                if (x >= 240 && x <= 290 && y >= yStart && y <= yEnd) {
                    selectedStatIndex = i;
                    return;
                }

                // Plus Button
                if (x >= 310 && x <= 350 && y >= yStart && y <= yStart + 40) {
                    String stat = statNames[i];
                    if (player.getStat(stat) < 20) {
                        player.adjustStat(stat, 1);
                        selectedStatIndex = i;
                    }
                    return;
                }

                // Minus Button
                if (x >= 360 && x <= 400 && y >= yStart && y <= yStart + 40) {
                    String stat = statNames[i];
                    if (player.getStat(stat) > 1) {
                        player.adjustStat(stat, -1);
                        selectedStatIndex = i; //
                    }
                    return;
                }
            }
        }
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
    @Override
    public void pause() {
    } // Not used but required by Screen interface

    @Override
    public void resume() {
    } // Not used but required by Screen interface
}
