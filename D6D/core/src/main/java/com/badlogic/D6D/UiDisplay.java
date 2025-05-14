package com.badlogic.D6D;

// Import Statements
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class UiDisplay {

    // Local Variables
    private final Rectangle bounds;
    private final Viewport viewport;
    private final Animation<TextureRegion> animation;
    private TextureRegion staticFrame; // Mutable for setRegion() changes
    private final boolean isAnimated;

    private float stateTime = 0f;

    // Constructor: Handles both static and animated displays
    // Display Elements Only (No Clicks)
    public UiDisplay(TextureAtlas atlas, String regionName, int frameCount, float totalTime,
                     float x, float y, float width, float height, Viewport viewport) {

        Array<TextureRegion> frames = new Array<>();
        if (frameCount == 1) {
            TextureRegion frame = atlas.findRegion(regionName); // No index for single frame
            if (frame != null) {
                frames.add(frame);
            }
        } else {
            for (int i = 1; i <= frameCount; i++) {
                TextureRegion frame = atlas.findRegion(regionName, i);
                if (frame != null) {
                    frames.add(frame);
                }
            }
        }

        this.bounds = new Rectangle(x, y, width, height);
        this.viewport = viewport;

        if (frames.size > 1) {
            this.isAnimated = true;
            this.animation = new Animation<>(totalTime / (frameCount - 1), frames, Animation.PlayMode.LOOP);
            this.staticFrame = null;
        } else {
            this.isAnimated = false;
            this.animation = null;
            this.staticFrame = frames.size > 0 ? frames.get(0) : null;
        }
    }

    // Update Animation Frame (Only if Animated)
    public void update(float delta) {
        if (isAnimated) {
            stateTime += delta;
        }
    }

    // Render Call — Draws Static or Animated Frame
    public void render(SpriteBatch batch) {
        if (isAnimated && animation != null) {
            TextureRegion frame = animation.getKeyFrame(stateTime);
            batch.draw(frame, bounds.x, bounds.y, bounds.width, bounds.height);
        } else if (staticFrame != null) {
            batch.draw(staticFrame, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    // Allows changing the visual region (Only for Static Displays)
    public void setRegion(String regionName) {
        if (!isAnimated) {
            TextureAtlas atlas = Loader.getAtlas("Ui_Assets.atlas");
            TextureRegion newRegion = atlas.findRegion(regionName);
            if (newRegion != null) {
                this.staticFrame = newRegion;
            }
        }
    }

    // README (Use Case Example)
    //
    // In Local Variable
    // private UiDisplay mainTitle;
    //
    // In show()
    // mainTitle = new UiDisplay(atlas, "Ui_MainTitle", 4, 1.0f, 220, 300, 800,
    //                            300, screenCamera.getViewport());
    //
    // In render()
    // mainTitle.update(delta);
    // mainTitle.render(batch); // In Draw Call/Batch
}
