package com.badlogic.D6D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class UiButton extends UiDisplay {
    private String text = "";
    private BitmapFont font;
    private GlyphLayout layout;
    private Runnable onClick;
    private boolean isPressed = false;
    private float pressedScale = 0.95f;
    private Color textColor = Color.WHITE;
    
    // Animation variables from old version
    private final Animation<TextureRegion> hoverAnimation;
    private final TextureRegion idleFrame;
    private float hoverTime = 0f;
    private boolean hasPlayed = false;
    private boolean isHovered = false;
    
    public UiButton(com.badlogic.gdx.graphics.g2d.TextureAtlas atlas, String regionName, int animationFrames, 
                   float animationSpeed, float x, float y, float width, float height, 
                   com.badlogic.gdx.utils.viewport.Viewport viewport) {
        
        super(atlas, regionName, animationFrames, animationSpeed, x, y, width, height, viewport);
        
        // Initialize font and layout for text rendering
        font = new BitmapFont();
        layout = new GlyphLayout();
        
        // Set base scale for the font
        font.getData().setScale(1.0f);
        
        // Initialize animation frames like the old version
        com.badlogic.gdx.utils.Array<TextureRegion> frames = new com.badlogic.gdx.utils.Array<>();
        for (int i = 1; i <= animationFrames; i++) {
            TextureRegion frame = atlas.findRegion(regionName, i);
            if (frame != null) {
                frames.add(frame);
            }
        }
        
        idleFrame = atlas.findRegion(regionName, 1);
        // Use total animation time instead of per-frame time
        float totalTime = animationSpeed;
        hoverAnimation = new Animation<>(totalTime / (animationFrames - 1), frames, Animation.PlayMode.NORMAL);
    }
    
    @Override
    public void update(float delta) {
        super.update(delta);
        
        // Check for mouse/touch input and handle hover animation
        checkForInputAndHover(delta);
    }
    
    @Override
    public void render(SpriteBatch batch) {
        // Apply pressed scaling if button is pressed
        float originalScaleX = getScaleX();
        float originalScaleY = getScaleY();
        
        if (isPressed) {
            setScale(originalScaleX * pressedScale, originalScaleY * pressedScale);
        }
        
        // Render the button sprite using animation logic from old version
        if (isHovered && (hoverTime > 0 || hasPlayed)) {
            TextureRegion frame = hoverAnimation.getKeyFrame(hoverTime);
            batch.draw(frame, getX(), getY(), getWidth(), getHeight());
        } else {
            batch.draw(idleFrame, getX(), getY(), getWidth(), getHeight());
        }
        
        // Reset scale
        if (isPressed) {
            setScale(originalScaleX, originalScaleY);
        }
        
        // Render button text if we have any
        if (!text.isEmpty()) {
            float textPaddingLeft = 20f; // Pushes text 20 pixels to the right from button's left edge
            layout.setText(font, text, textColor, getWidth() - textPaddingLeft, Align.left, true);
            float textX = getX() + textPaddingLeft;
            float textY = getY() + (getHeight() + layout.height) / 2;
            font.draw(batch, layout, textX, textY);
        }
    }
    
    // Check for input and handle hover animation like the old version
    private void checkForInputAndHover(float delta) {
        Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        getViewport().unproject(mouse);
        
        boolean isTouching = Gdx.input.isTouched();
        boolean isOver = isPointInButton(mouse.x, mouse.y);
        
        // Handle hover animation logic from old version
        if (isOver) {
            isHovered = true;
            if (!hasPlayed) hoverTime += delta;
            if (hoverTime >= hoverAnimation.getAnimationDuration()) {
                hoverTime = hoverAnimation.getAnimationDuration(); // Clamp
                hasPlayed = true;
            }
            
            // Handle click event
            if (Gdx.input.justTouched() && onClick != null) {
                onClick.run();
            }
        } else {
            isHovered = false;
            hoverTime = 0;
            hasPlayed = false;
        }
        
        // Handle press animation
        isPressed = isTouching && isOver;
    }
    
    // Check if within button bounds
    private boolean isPointInButton(float x, float y) {
        return x >= getX() && x <= getX() + getWidth() && 
               y >= getY() && y <= getY() + getHeight();
    }
    
    // Set button text
    public void setText(String text) {
        this.text = text != null ? text : "";
    }
    
    // Set the text color for the button text
    public void setTextColor(Color color) {
        this.textColor = color;
    }
    
    // Set the font for the button text
    public void setTextFont(BitmapFont font) {
        if (font != null) {
            this.font = font;
        }
    }
    
    // Clikc Handler For Button
    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }
    
    // Disposal
    @Override
    public void dispose() {
        super.dispose();
        if (font != null) {
            font.dispose();
        }
    }
}