package com.badlogic.D6D;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
// Import Statements
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class PlayGame implements Screen {

    private final MainGame game;                                              // Reference to the main game class
    private SpriteBatch batch;                                               // Used for drawing 2D elements
    private MonitorScreen monitorScreen;                                    // Handles animated background         (MonitorScreen.java)
    private ScreenCamera screenCamera;                                     // Encapsulates camera + viewport logic (ScreenCamera.java)       
    private final Array<UiButton> uiButtons = new Array<>();              // Array to hold buttons handled by      (UiButton.java)
    private final Array<UiDisplay> uiElements = new Array<>();           // Array to hold elements handled by      (UiDisplay.java)
    private final List<StatLabel> statLabels = new ArrayList<>();       // List to hold stat labels                (StatLabel.java)
    private DayCycle dayCycle;                                         // DayCycle instance to manage time slots   (DayCycle.java)
    private UiDisplay timeIcon;                                       // Icon for time of day                      (UiDisplay.java) 
    private DayCycle.TimeSlot lastTimeSlot;                          // Last time slot to check for changes        (DayCycle.java)                   
    private StatLabel dayLabel;                                     // Label for current day                       (StatLabel.java) 
    private StatLabel timeSlotLabel;                               // Label for current time of day                (StatLabel.java)
    private ScenarioEngine scenarioEngine;
    private GlyphLayout titleLayout;
    private GlyphLayout descriptionLayout;
    private BitmapFont dialogueFont;
    private String scenarioTitle;
    private String scenarioDescription;

    

    // Constructor for PlayGame screen
    public PlayGame(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        // Initialize camera and animated monitor background
        screenCamera = new ScreenCamera();
        monitorScreen = new MonitorScreen(Loader.getAtlas("Ui_Assets.atlas"));

        // Get Atlas From Loader
        TextureAtlas ui = Loader.getAtlas("Ui_Assets.atlas");
        TextureAtlas player = Loader.getAtlas("PlayerPortraits.atlas");

        // Initialize Screen Elements

        // Portait With Frame and Name
        uiElements.add(new UiDisplay(ui, "Ui_Portrait_Frame", 1, 1.0f, 50, 540, 120, 120, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(player, "MaleNeutralAway", 1, 1.0f, 65, 555, 90, 90, screenCamera.getViewport()));
        statLabels.add(new StatLabel("PlayerName", 110, 530, screenCamera.getViewport()));

        // Stat Frames
        uiElements.add(new UiDisplay(ui, "BTN_Select", 1, 1.0f, 50, 460, 120, 60, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "BTN_Select", 1, 1.0f, 50, 400, 120, 60, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "BTN_Select", 1, 1.0f, 50, 340, 120, 60, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "BTN_Select", 1, 1.0f, 50, 280, 120, 60, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "BTN_Select", 1, 1.0f, 50, 220, 120, 60, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "BTN_Select", 1, 1.0f, 50, 160, 120, 60, screenCamera.getViewport()));

        // Stat Icons
        uiElements.add(new UiDisplay(ui, "STAT_Intel", 1, 1.0f, 60, 470, 40, 40, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "STAT_Mental", 1, 1.0f, 60, 410, 40, 40, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "STAT_Constitution", 1, 1.0f, 60, 350, 40, 40, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "STAT_Swiftness", 1, 1.0f, 60, 290, 40, 40, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "STAT_Courage", 1, 1.0f, 60, 230, 40, 40, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "STAT_Charisma", 1, 1.0f, 60, 170, 40, 40, screenCamera.getViewport()));

        // Life And Sanity Icons
        uiElements.add(new UiDisplay(ui, "STAT_Health", 1, 1.0f, 180, 600, 50, 50, screenCamera.getViewport()));
        uiElements.add(new UiDisplay(ui, "STAT_Sanity", 1, 1.0f, 180, 540, 50, 50, screenCamera.getViewport()));

        // Dialogue Box
        uiElements.add(new UiDisplay(ui, "Ui_Window_Horizontal", 1, 1.0f, 320, 300, 700, 350, screenCamera.getViewport()));

        // Intialize Scenario Text
        scenarioEngine = new ScenarioEngine();
        dialogueFont = new BitmapFont();
        dialogueFont.getData().setScale(1.1f);
        titleLayout = new GlyphLayout();
        descriptionLayout = new GlyphLayout();
        scenarioTitle = scenarioEngine.getTitle();
        scenarioDescription = scenarioEngine.getDescription();
        updateScenarioLayout();

        // Time Icon
        dayCycle = new DayCycle();
        lastTimeSlot = dayCycle.getCurrentTimeSlot();
        timeIcon = new UiDisplay(ui, getRegionNameForSlot(lastTimeSlot), 1, 1.0f, 1160, 570, 60, 60, screenCamera.getViewport());
        uiElements.add(timeIcon);

        // Time And Day Labels
        dayLabel = new StatLabel("DayLabel", 1190, 540, screenCamera.getViewport());
        timeSlotLabel = new StatLabel("TimeSlotLabel", 1120, 600, screenCamera.getViewport());
        statLabels.add(timeSlotLabel);
        statLabels.add(dayLabel);

        // Exit Button
        uiButtons.add(new UiButton(ui, "BTN_Exit", 6, 1.0f, 50, 70, 150, 60, screenCamera.getViewport()));

        // Stat value labels
        String[] statOrder = {
            "Intelligence", "Mental", "Constitution", 
            "Swiftness", "Courage", "Charisma"
        };

        int startY = 490;
        for (String stat : statOrder) {
            statLabels.add(new StatLabel(stat, 120, startY, screenCamera.getViewport()));
            startY -= 60;
        }

        // Health and Sanity value labels
        statLabels.add(new StatLabel("Health", 260, 625, screenCamera.getViewport()));
        statLabels.add(new StatLabel("Sanity", 260, 565, screenCamera.getViewport()));


    }

    @Override
    public void render(float delta) {
        // Clear screen with black background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Initialize time aand day labels
        timeSlotLabel.setText(lastTimeSlot.name().charAt(0) + lastTimeSlot.name().substring(1).toLowerCase() + " :");
        dayLabel.setText("Day " + dayCycle.getCurrentDay());

        // Update Animation Times
        monitorScreen.update(delta);
        for (UiDisplay element : uiElements) element.update(delta);
        for (UiButton element : uiButtons) element.update(delta);

        
        // Time Icon
        // Update Day and Time Slot Labels
        DayCycle.TimeSlot currentSlot = dayCycle.getCurrentTimeSlot();
        if (currentSlot != lastTimeSlot) {
        timeIcon.setRegion(getRegionNameForSlot(currentSlot));  // Update icon
        lastTimeSlot = currentSlot;

        // Update time slot text and day number
        timeSlotLabel.setText(currentSlot.name().charAt(0) + currentSlot.name().substring(1).toLowerCase() + " :");
        dayLabel.setText("Day " + dayCycle.getCurrentDay());
}

        // Exit Button
        uiButtons.get(0).setOnClick(() -> {
            game.setScreen(new MenuScreen(game));
        });

        // Draw Call (Screen Elements)
        batch.begin();
        monitorScreen.render(batch);
        for (UiDisplay element : uiElements) element.render(batch);
        for (UiButton element : uiButtons) element.render(batch);
        for (StatLabel label : statLabels) label.render(batch);
        scenarioTextRender(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Ensure viewport scales correctly when screen size changes
        screenCamera.resize(width, height);
    }

    @Override
    public void hide() {
        // Dispose of resources if needed
    }

    @Override
    public void dispose() {
        // Release resources when screen is disposed
        if (batch != null) {
            batch.dispose();
        }
    }

    // Helper Method
        private String getRegionNameForSlot(DayCycle.TimeSlot slot) {
        switch (slot) {
            case MORNING: return "Ui_Morning";
            case DAY: return "Ui_Day";
            case EVENING: return "Ui_Evening";
            default: return "Ui_Morning"; // fallback/default
        }
    }

    // Helper Method For Scenario Text
    private void updateScenarioLayout() {
    titleLayout.setText(dialogueFont, scenarioTitle);
    descriptionLayout.setText(dialogueFont, scenarioDescription, Color.WHITE, 660, Align.left, true);
    }

    private void scenarioTextRender(SpriteBatch batch) {
    float dialogueX = 350;
    float dialogueY = 600;

    dialogueFont.draw(batch, titleLayout, dialogueX, dialogueY);
    dialogueFont.draw(batch, descriptionLayout, dialogueX, dialogueY - 40);
    }


    // Unused methods
    @Override public void pause() {}     // Not used but required by Screen interface
    @Override public void resume() {}    // Not used but required by Screen interface
}
