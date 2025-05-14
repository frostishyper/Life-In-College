package com.badlogic.D6D;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class UiDisplay {
    private Animation<TextureRegion> animation;
    private float stateTime = 0;
    private float x, y, width, height;
    private float scaleX = 1.0f, scaleY = 1.0f;
    private String currentRegionName;
    private TextureAtlas atlas;
    private Viewport viewport;
    private TextureRegion region;

    public UiDisplay(TextureAtlas atlas, String regionName, int index, float scale, float x, float y, float width,
            float height, Viewport viewport) {
        this.atlas = atlas;
        this.currentRegionName = regionName;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.viewport = viewport;
        this.region = atlas.findRegion(regionName);
    }

    // create animation from atlas
    private void createAnimation(String regionName, int frameCount, float animationSpeed) {
        if (atlas == null)
            return;

        // If only one frame, use it directly
        if (frameCount <= 1) {
            TextureRegion region = atlas.findRegion(regionName);
            if (region != null) {
                Array<TextureRegion> frames = new Array<>(1);
                frames.add(region);
                animation = new Animation<>(animationSpeed, frames, Animation.PlayMode.LOOP);
            }
        }
        // Otherwise try to find multiple frames from the atlas
        else {
            Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(regionName);
            if (regions != null && regions.size > 0) {
                animation = new Animation<>(animationSpeed, regions, Animation.PlayMode.LOOP);
            }
        }
    }

    // update the animation state time
    public void update(float delta) {
        stateTime += delta;
    }

    // Renders current frame of the animation
    public void render(SpriteBatch batch) {
        if (region != null) {
            batch.draw(region, x, y, width, height);
        } else if (animation != null) {
            TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

            if (currentFrame != null) {
                batch.draw(
                        currentFrame,
                        x, y, // Position
                        width / 2, height / 2, // Origin (center)
                        width, height, // Size
                        scaleX, scaleY, // Scale
                        0 // Rotation
                );
            }
        }
    }

    // texture Region Update
    public void setRegion(TextureRegion newRegion) {
        this.region = newRegion;
    }

    public void setRegion(String regionName) {
        if (regionName.equals(currentRegionName))
            return;

        currentRegionName = regionName;
        createAnimation(regionName, 1, 0.1f);
    }

    // Scale Display
    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    // Getters
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void dispose() {
        // Animation doesn't need explicit disposal as it uses regions from the atlas
    }
}