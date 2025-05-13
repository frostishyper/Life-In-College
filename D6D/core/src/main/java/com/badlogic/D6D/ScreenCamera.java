package com.badlogic.D6D;

// Import Statements
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

// Reusable Class For Handling Camera and Viewport
public class ScreenCamera {

    // Local Variables (Windowed Screen Size)
    public static final float WORLD_WIDTH = 1280f;
    public static final float WORLD_HEIGHT = 720f;

    // Camera and Viewport
    private final OrthographicCamera camera;
    private final Viewport viewport;

    // Constructor for ScreenCamera
    // Initializes the camera and viewport
    // Handles screen resizing behavior and camera updates
    public ScreenCamera() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();
        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
        camera.update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    // Returns the viewport
    public Viewport getViewport() {
        return viewport;
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void update() {
        camera.update();
    }
}
