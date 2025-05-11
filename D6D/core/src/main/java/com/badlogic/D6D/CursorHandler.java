package com.badlogic.D6D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.audio.Sound;

public class CursorHandler extends InputAdapter{
    
    private final Sound clickSound;

    public CursorHandler() {
        // Load the click sound from the Loader
        this.clickSound = Loader.getClickSound();

        // Load the custom cursor from the atlas
        TextureAtlas atlas = Loader.getAtlas("Ui_Assets.atlas");
        TextureRegion cursorRegion = atlas.findRegion("Ui_Cursor");

        if (cursorRegion != null) {
            // Create a Pixmap for the custom cursor
            Texture cursorTexture = cursorRegion.getTexture();
            Pixmap pixmap = new Pixmap(cursorRegion.getRegionWidth(), cursorRegion.getRegionHeight(), Pixmap.Format.RGBA8888);
            cursorTexture.getTextureData().prepare();
            pixmap.drawPixmap(cursorTexture.getTextureData().consumePixmap(),
                    cursorRegion.getRegionX(), cursorRegion.getRegionY(),
                    cursorRegion.getRegionWidth(), cursorRegion.getRegionHeight(),
                    0, 0, cursorRegion.getRegionWidth(), cursorRegion.getRegionHeight());

            // Set the custom cursor with the top-left pixel as the click point
            Gdx.graphics.setCursor(Gdx.graphics.newCursor(pixmap, 0, 0));
            pixmap.dispose(); // Dispose of the Pixmap after setting the cursor
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) { // Left mouse button
            if (clickSound != null) {
                clickSound.play(); // Play the click sound
            }
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }
}
