package com.badlogic.D6D;

// Import Statements
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.viewport.Viewport;

// Class To Display Player Stats And Info To The Screen
public class StatLabel {
    private final String statName;
    private final BitmapFont font;
    private final float x, y;
    private final GlyphLayout layout = new GlyphLayout();
    private final Viewport viewport;
    private String text = null;

    public StatLabel(String statName, float x, float y, Viewport viewport) {
        this.statName = statName;
        this.x = x;
        this.y = y;
        this.viewport = viewport;
        this.font = new BitmapFont();

        if (statName.equals("PlayerName")) {
            font.getData().setScale(1.5f);
        } else {
            font.getData().setScale(1.2f);
        }
    }

    // Rendering Behavior
    public void render(SpriteBatch batch) {
    String displayText;

    if (text != null) {
        displayText = text;
    } else {
        Player player = Player.getInstance();
        switch (statName) {
            case "PlayerName":
                displayText = player.name;
                break;
            case "Health":
                displayText = player.getCurrentHealth() + " / " + player.getBaseHealth();
                break;
            case "Sanity":
                displayText = player.getCurrentSanity() + " / " + player.getBaseSanity();
                break;
            default:
                int value = player.getStat(statName);
                displayText = String.valueOf(value);
                break;
        }
    }

    layout.setText(font, displayText);
    font.draw(batch, layout, x - layout.width / 2, y + layout.height / 2);
    }

    public void setText(String newText) {
    this.text = newText;
    }
}
