package com.badlogic.D6D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;


    public class StatLabel {
    private String type;
    private float x, y;
    private BitmapFont font;
    private GlyphLayout layout;
    private Viewport viewport;
    private String customText;
    
    public StatLabel(String type, float x, float y, Viewport viewport) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.viewport = viewport;
        
        // Initialize font and layout
        font = new BitmapFont();
        font.getData().setScale(1.0f);
        font.setColor(Color.WHITE);
        layout = new GlyphLayout();
    }
    
    // Renders Stat Label
    public void render(SpriteBatch batch) {
        String text = getText();
        layout.setText(font, text);
        font.draw(batch, text, x - layout.width / 2, y);
    }
    
    // Custom Text For Label
    public void setText(String text) {
        this.customText = text;
    }
    
    // Sets the type of stat to display
    private String getText() {
        if (customText != null) {
            return customText;
        }
        
        Player player = Player.getInstance();
        
        switch (type) {
            case "PlayerName":
                return player.name;
            case "Health":
                return player.getCurrentHealth() + "/" + player.getBaseHealth();
            case "Sanity":
                return player.getCurrentSanity() + "/" + player.getBaseSanity();
            case "Intelligence":
            case "Mental":
            case "Constitution":
            case "Swiftness":
            case "Courage":
            case "Charisma":
                return String.valueOf(player.getStat(type));
            default:
                return "";
        }
    }
    
    // Disposal
    public void dispose() {
        if (font != null) {
            font.dispose();
        }
    }
}