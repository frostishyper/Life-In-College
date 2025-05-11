package com.badlogic.D6D;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.audio.Music;

public class MenuScreen implements Screen {

    private final MainGame game;
    private SpriteBatch batch; // Allows Multiple textures in one draw call
    private TextureRegion background; // Background texture region
    private Animation<TextureRegion> backgroundAnimation; // Animation for the background
    private float animationTime; // Tracks elapsed time for the animation
    private OrthographicCamera camera; // 2D camera (Flat) [Renders World  wihtout any perspective]
    private Viewport viewport; // Mantains aspect ratio and proper scaling
    private Music startupMusic; // Declare startupMusic as a class-level variable

    private static final float WORLD_WIDTH = 1280f; // Virtual Width (To maintain aspect ratio)
    private static final float WORLD_HEIGHT = 720f; // Virtual Height (To maintain aspect ratio)
    // Are Basically used as reference when scaling the screen
    // Initial Screen Size is 1280x720 (From Inside Lwjgl3Launcher)

    // Constructor For MenuScreen

    public MenuScreen(MainGame game) {
        this.game = game;
    }


    // Initiaalizes the screen elements
     @Override
    public void show() {

        // Startup music
        startupMusic = Loader.getStartupMusic();
        if (startupMusic != null) {
            startupMusic.setLooping(false); // Ensure it doesn't loop
            startupMusic.play(); // Play the music
        }

        batch = new SpriteBatch(); // Prep Asset for drawing

        // Setup camera and viewport (2D not perspective distortion)
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();

        // Center the camera on the screen
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();

        // Get Assets from the Loader
        TextureAtlas atlas = Loader.getAtlas("Ui_Assets.atlas");
        
        // Create animation frames (initialize the array)
        Array<TextureRegion> frames = new Array<>();
        // Add frames to the array using for loop
        for (int i = 1; i <= 4; i++) {
            TextureRegion frame = atlas.findRegion("Ui_Screen_Border", i);
            if (frame != null) {
                frames.add(frame);
            }
        }

        // Create the animation (300ms per frame, looping)
        backgroundAnimation = new Animation<>(0.3f, frames, Animation.PlayMode.LOOP);
        animationTime = 0f; // Initialize animation time

        // Set Cursor Handler
        Gdx.input.setInputProcessor(new CursorHandler());
    }
    

    @Override
public void render(float delta) {
    // Clear the screen with black
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    
    // Update the camera when the screen is resized
    camera.update();
    batch.setProjectionMatrix(camera.combined);

    // Update animation time
    animationTime += delta;

    // Get the current frame of the animation
    TextureRegion currentFrame = backgroundAnimation.getKeyFrame(animationTime);

    // Start the draw call for screen elements
    batch.begin();
    if (currentFrame != null) {
        // Draw the current frame of the animation
        batch.draw(currentFrame, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
    }
    batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }


    @Override
    public void hide() {
        // Stop the music when the screen is hidden
        if (startupMusic != null && startupMusic.isPlaying()) {
            startupMusic.stop();
        }
    }

    @Override
    public void dispose() {
        // Dispose of resources specific to the menu screen
        if (startupMusic != null) {
            startupMusic.dispose();
        }
    }

    //Currently unused methods
    @Override public void pause() {}
    @Override public void resume() {}
}
