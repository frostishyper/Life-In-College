package com.badlogic.D6D;

// Import Statements
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

// Reusable Class For Handling UI Buttons And Their Animations
public class UiButton {

    // Local Variables
    private final Animation<TextureRegion> hoverAnimation;
    private final TextureRegion idleFrame;
    private final Rectangle bounds;
    private final Viewport viewport;
    private float hoverTime = 0f;
    private boolean isHovered = false;
    private boolean hasPlayed = false;

    private Runnable onClick; // Function to call when the button is clicked
    
    // Constructor for UiButton
    // Controls Only (Buttons, Etc)
    // Initializes the button with its animation frames and position
    // Takes a TextureAtlas, region name, frame count, total time for animation, and position/size
    public UiButton(TextureAtlas atlas, String regionName, int frameCount, float totalTime,
                    float x, float y, float width, float height, Viewport viewport) {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i <= frameCount; i++) {
            TextureRegion frame = atlas.findRegion(regionName, i);
            if (frame != null) {
                frames.add(frame);
            }
        }

        idleFrame = atlas.findRegion(regionName, 1);
        hoverAnimation = new Animation<>(totalTime / (frameCount - 1), frames, Animation.PlayMode.NORMAL);
        bounds = new Rectangle(x, y, width, height);
        this.viewport = viewport;
    }

    // Trigger the onClick function when the button is clicked 
    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    // Constructor for UiButton with TextureRegion
    // Initializes the button with a single TextureRegion for idle state (Not Hovered)
    public void update(float delta) {
    Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.input.getY());
    viewport.unproject(mouse);

    if (bounds.contains(mouse)) {
        isHovered = true;
        if (!hasPlayed) hoverTime += delta;
        if (hoverTime >= hoverAnimation.getAnimationDuration()) {
            hoverTime = hoverAnimation.getAnimationDuration(); // Clamp
            hasPlayed = true;
        }

        // Click trigger
        if (Gdx.input.justTouched() && onClick != null) {
            onClick.run();
        }

    } else {
        isHovered = false;
        hoverTime = 0;
        hasPlayed = false;
        }
    }

    // Check if the button is being hovered
    public void render(SpriteBatch batch) {
        if (isHovered && (hoverTime > 0 || hasPlayed)) {
            TextureRegion frame = hoverAnimation.getKeyFrame(hoverTime);
            batch.draw(frame, bounds.x, bounds.y, bounds.width, bounds.height);
        } else {
            batch.draw(idleFrame, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    // README (Use Case Example)
    //
    // In Local Variable
    // private UiButton playButton;
    //
    // In Show()
    // playButton = new UiButton(atlas, "BTN_Play", 7, 1.2f, 550, 200, 160, 60, screenCamera.getViewport());
    //
    // In render()
    // playButton.update(delta);
    // playButton.render(batch); // In Draw Call/Batch
}
