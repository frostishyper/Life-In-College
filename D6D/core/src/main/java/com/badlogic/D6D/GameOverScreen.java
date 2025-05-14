package com.badlogic.D6D;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Align;

public class GameOverScreen implements Screen {
    
    private final MainGame game;
    private SpriteBatch batch;
    private BitmapFont titleFont;
    private BitmapFont subtitleFont;
    private UiButton exitButton;
    private String gameOverTitle;
    private String gameOverSubtext;
    private GlyphLayout titleLayout;
    private GlyphLayout subtextLayout;
    private Player player;
    private ScreenCamera screenCamera;
    
    public GameOverScreen(MainGame game, String deathCause) {
        this.game = game;
        this.player = Player.getInstance();
        
        // Set game over text based on what caused the death
        if ("health".equals(deathCause)) {
            gameOverTitle = "You Died";
            gameOverSubtext = "Your health has been depleted...";
        } else if ("sanity".equals(deathCause)) {
            gameOverTitle = "You Went Insane";
            gameOverSubtext = "Your mind could not withstand the horrors...";
        } else {
            // Default case
            gameOverTitle = "Game Over";
            gameOverSubtext = "The darkness has claimed you...";
        }
    }
    
    @Override
    public void show() {
        batch = new SpriteBatch();
        screenCamera = new ScreenCamera();
        
        // Initialize fonts
        titleFont = new BitmapFont();
        titleFont.getData().setScale(2.5f);
        titleFont.setColor(Color.RED);
        
        subtitleFont = new BitmapFont();
        subtitleFont.getData().setScale(1.5f);
        subtitleFont.setColor(Color.WHITE);
        
        // Initialize text layouts
        titleLayout = new GlyphLayout();
        subtextLayout = new GlyphLayout();
        
        titleLayout.setText(titleFont, gameOverTitle, Color.RED, 1280, Align.center, false);
        subtextLayout.setText(subtitleFont, gameOverSubtext, Color.WHITE, 1280, Align.center, false);
        
        // Get UI atlas for button
        TextureAtlas ui = Loader.getAtlas("Ui_Assets.atlas");
        
        // Create exit button (centered on screen, below text)
        float buttonWidth = 200;
        float buttonHeight = 80;
        float buttonX = (1800 - buttonWidth) / 2;
        float buttonY = 500;
        
        exitButton = new UiButton(ui, "BTN_Exit", 6, 1.2f, buttonX, buttonY, buttonWidth, buttonHeight, screenCamera.getViewport());
        
    }
    
   @Override
    public void render(float delta) {
    Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    exitButton.update(delta);
    exitButton.setOnClick(() -> Gdx.app.exit());

    batch.begin();

    float screenWidth = Gdx.graphics.getWidth();
    float screenHeight = Gdx.graphics.getHeight();
    float spacing = 40f;

    // Stack elements vertically centered
    float totalHeight = titleLayout.height + subtextLayout.height + exitButton.getHeight() + spacing * 2;
    float startY = (screenHeight + totalHeight) / 2;

    float titleX = (screenWidth - titleLayout.width) / 2;
    float titleY = startY;

    float subtextX = (screenWidth - subtextLayout.width) / 2;
    float subtextY = titleY - titleLayout.height - spacing;

    float buttonX = (screenWidth - exitButton.getWidth()) / 2;
    float buttonY = subtextY - subtextLayout.height - spacing;

    titleFont.draw(batch, titleLayout, titleX, titleY);
    subtitleFont.draw(batch, subtextLayout, subtextX, subtextY);

    batch.end();
    }

    
    @Override
    public void resize(int width, int height) {
        screenCamera.resize(width, height);
    }
    
    @Override
    public void pause() {}
    
    @Override
    public void resume() {}
    
    @Override
    public void hide() {}
    
    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        if (titleFont != null) {
            titleFont.dispose();
        }
        if (subtitleFont != null) {
            subtitleFont.dispose();
        }
        if (exitButton != null) {
            exitButton.dispose();
        }
    }
    
    // Static method to check if player is dead and return appropriate screen
    public static GameOverScreen checkPlayerStatus(MainGame game) {
        Player player = Player.getInstance();
        
        if (player.getCurrentHealth() <= 0) {
            return new GameOverScreen(game, "health");
        } else if (player.getCurrentSanity() <= 0) {
            return new GameOverScreen(game, "sanity");
        }
        
        return null; // Player is still alive
    }
}