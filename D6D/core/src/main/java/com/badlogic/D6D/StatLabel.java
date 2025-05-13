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
        String text;

        Player player = Player.getInstance();

        switch (statName) {
            case "PlayerName":
                text = player.name;
                break;
            case "Health":
                text = player.getCurrentHealth() + " / " + player.getBaseHealth();
                break;
            case "Sanity":
                text = player.getCurrentSanity() + " / " + player.getBaseSanity();
                break;
            default:
                int value = player.getStat(statName);
                text = String.valueOf(value);
                break;
        }

        layout.setText(font, text);
        font.draw(batch, layout, x - layout.width / 2, y + layout.height / 2);
    }
}
