package com.badlogic.D6D;

// Import Statements
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

// Reusable Class For Handling The Animated Background/Border
public class MonitorScreen {

    // Local Variables
    private Animation<TextureRegion> backgroundAnimation;
    private float animationTime = 0f;

    // Constants for windowed screen dimensions
    public static final float WORLD_WIDTH = 1280f;
    public static final float WORLD_HEIGHT = 720f;

    // Constructor for MonitorScreen
    // Initializes the animated background using a TextureAtlas
    public MonitorScreen(TextureAtlas atlas) {
        Array<TextureRegion> frames = new Array<>();

        // Load the frames for the animation from the atlas
        for (int i = 1; i <= 4; i++) {
            TextureRegion frame = atlas.findRegion("Ui_Screen_Border", i);
            if (frame != null) {
                frames.add(frame);
            }
        }

        backgroundAnimation = new Animation<>(0.3f, frames, Animation.PlayMode.LOOP);
    }

    // Update the animation time based on delta time
    public void update(float delta) {
        animationTime += delta;
    }

    // Render the current frame of the animation
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = backgroundAnimation.getKeyFrame(animationTime);
        if (currentFrame != null) {
            batch.draw(currentFrame, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        }
    }
}
