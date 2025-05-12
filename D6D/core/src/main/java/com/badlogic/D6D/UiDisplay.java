package com.badlogic.D6D;

// Import Statements
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class UiDisplay {
    
    //Local Variables
    private final Rectangle bounds;
    private final Viewport viewport;
    private final Animation<TextureRegion> animation;
    private final TextureRegion staticFrame;
    private final boolean isAnimated;

    private float stateTime = 0f;

    // Constructor: Handles both static and animated displays
    public UiDisplay(TextureAtlas atlas, String regionName, int frameCount, float totalTime,
                     float x, float y, float width, float height, Viewport viewport) {

        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i <= frameCount; i++) {
            TextureRegion frame = atlas.findRegion(regionName, i);
            if (frame != null) {
                frames.add(frame);
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

    public void update(float delta) {
        if (isAnimated) {
            stateTime += delta;
        }
    }

    public void render(SpriteBatch batch) {
        if (isAnimated && animation != null) {
            TextureRegion frame = animation.getKeyFrame(stateTime);
            batch.draw(frame, bounds.x, bounds.y, bounds.width, bounds.height);
        } else if (staticFrame != null) {
            batch.draw(staticFrame, bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }
}


