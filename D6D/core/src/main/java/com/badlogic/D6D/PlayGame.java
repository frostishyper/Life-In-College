package com.badlogic.D6D;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
// Import Statements
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class PlayGame implements Screen {

    private final MainGame game;                                              // Reference to the main game class
    private SpriteBatch batch;                                               // Used for drawing 2D elements
    private MonitorScreen monitorScreen;                                    // Handles animated background         (MonitorScreen.java)
    private ScreenCamera screenCamera;                                     // Encapsulates camera + viewport logic (ScreenCamera.java)       
    private PortraitManager portraitManager;                               // Add PortraitManager instance
    private UiDisplay portraitDisplay;                                     // UI element for the portrait
    private final Array<UiButton> uiButtons = new Array<>();              // Array to hold buttons handled by      (UiButton.java)
    private final Array<UiDisplay> uiElements = new Array<>();           // Array to hold elements handled by      (UiDisplay.java)
    private final List<StatLabel> statLabels = new ArrayList<>();       // List to hold stat labels                (StatLabel.java)
    private DayCycle dayCycle;                                         // DayCycle instance to manage time slots   (DayCycle.java)
    private UiDisplay timeIcon;                                       // Icon for time of day                      (UiDisplay.java) 
    private DayCycle.TimeSlot lastTimeSlot;                          // Last time slot to check for changes        (DayCycle.java)                   
    private StatLabel dayLabel;                                     // Label for current day                       (StatLabel.java) 
    private StatLabel timeSlotLabel;                               // Label for current time of day                (StatLabel.java)
    private ScenarioEngine scenarioEngine;                        // Scenario engine to manage scenarios           (ScenarioEngine.java)  
    private ChoiceSystem choiceSystem;                           // System to handle player choices                (ChoiceSystem.java)
    private GlyphLayout titleLayout;                            // Layout for scenario title text                  
    private GlyphLayout descriptionLayout;                     // Layout for scenario description text             
    private GlyphLayout outcomeLayout;                        // Layout for scenario outcome text                 
    private GlyphLayout statChangeLayout;                    // New layout for stat changes 
    private BitmapFont dialogueFont;                        // Font for rendering text
    private String scenarioTitle;                          // Title of the current scenario                         (ScenarioEngine.java)
    private String scenarioDescription;                   // Description of the current scenario                     (ScenarioEngine.java)

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

        // Initialize PortraitManager
        portraitManager = new PortraitManager(player);

        // Initialize Screen Elements

        // Portait With Frame and Name
        uiElements.add(new UiDisplay(ui, "Ui_Portrait_Frame", 1, 1.0f, 50, 540, 120, 120, screenCamera.getViewport()));
        portraitDisplay = new UiDisplay(player, "MaleNeutralAway", 1, 1.0f, 65, 555, 90, 90, screenCamera.getViewport());
        uiElements.add(portraitDisplay);
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

        // Initialize DayCycle
        dayCycle = new DayCycle();
        lastTimeSlot = dayCycle.getCurrentTimeSlot();
        
        // Initialize Scenario Engine
        scenarioEngine = new ScenarioEngine();
        scenarioEngine.setDayCycle(dayCycle);
        
        // Initialize Choice System
        choiceSystem = new ChoiceSystem(ui, screenCamera.getViewport(), scenarioEngine, dayCycle);
        choiceSystem.generateChoiceButtons();
        
        // Initialize Scenario Text
        dialogueFont = new BitmapFont();
        dialogueFont.getData().setScale(1.1f);
        titleLayout = new GlyphLayout();
        descriptionLayout = new GlyphLayout();
        outcomeLayout = new GlyphLayout();
        statChangeLayout = new GlyphLayout(); // Initialize stat change layout
        
        updateScenarioText();

        // Time Icon
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
        // Check if player has died or gone insane
        GameOverScreen gameOverScreen = GameOverScreen.checkPlayerStatus(game);
        if (gameOverScreen != null) {
            game.setScreen(gameOverScreen);
            return; // Exit render method early
        }
        
        // Clear screen with black background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Initialize time and day labels
        timeSlotLabel.setText(lastTimeSlot.name().charAt(0) + lastTimeSlot.name().substring(1).toLowerCase() + " :");
        dayLabel.setText("Day " + dayCycle.getCurrentDay());

        // Update Animation Times
        monitorScreen.update(delta);
        for (UiDisplay element : uiElements) element.update(delta);
        for (UiButton element : uiButtons) element.update(delta);
        choiceSystem.update(delta);
        
        // Update PortraitManager based on player's health and sanity
        Player player = Player.getInstance();
        portraitManager.updatePortrait(player.getCurrentHealth(), player.getCurrentSanity());

        // Update the portrait display with the current portrait
        TextureRegion currentPortrait = portraitManager.getCurrentPortrait();
        portraitDisplay.setRegion(currentPortrait);

        // Time Icon
        // Update Day and Time Slot Labels
        DayCycle.TimeSlot currentSlot = dayCycle.getCurrentTimeSlot();
        if (currentSlot != lastTimeSlot) {
            timeIcon.setRegion(getRegionNameForSlot(currentSlot));  // Update icon
            lastTimeSlot = currentSlot;

            // Update time slot text and day number
            timeSlotLabel.setText(currentSlot.name().charAt(0) + currentSlot.name().substring(1).toLowerCase() + " :");
            dayLabel.setText("Day " + dayCycle.getCurrentDay());
            
            // Find appropriate scenario for new time slot
            // Note: No need to check for END_OF_DAY anymore as it doesn't exist
            // The ScenarioEngine will automatically be notified when the day changes
            scenarioEngine.findNextScenario();
            updateScenarioText();
            choiceSystem.generateChoiceButtons();
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
        choiceSystem.render(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Ensure viewport scales correctly when screen size changes
        screenCamera.resize(width, height);
    }
    
    // Update the scenario text based on current state
    private void updateScenarioText() {
        scenarioTitle = scenarioEngine.getTitle();
        scenarioDescription = scenarioEngine.getDescription();
        
        titleLayout.setText(dialogueFont, scenarioTitle);
        descriptionLayout.setText(dialogueFont, scenarioDescription, Color.WHITE, 660, Align.left, true);
        
        // Set outcome text if showing an outcome
        if (choiceSystem.isShowingOutcome()) {
            String outcomeText = choiceSystem.getOutcomeText();
            outcomeLayout.setText(dialogueFont, outcomeText, Color.GREEN, 660, Align.left, true);
        }
    }

    // Render scenario text
    private void scenarioTextRender(SpriteBatch batch) {
        float dialogueX = 350;
        float dialogueY = 600;

        dialogueFont.draw(batch, titleLayout, dialogueX, dialogueY);
        
        // Draw outcome or description
        if (choiceSystem.isShowingOutcome()) {
            // Get the original outcome text without stat changes
            String outcomeOnly = choiceSystem.getOutcomeText().split("\n")[0];
            outcomeLayout.setText(dialogueFont, outcomeOnly, Color.WHITE, 660, Align.left, true);
            dialogueFont.draw(batch, outcomeLayout, dialogueX, dialogueY - 40);
            
            // Render stat changes with colors
            Map<String, Integer> statChanges = choiceSystem.getStatChanges();
            if (!statChanges.isEmpty()) {
                float statChangeY = dialogueY - 40 - outcomeLayout.height - 20;
                
                for (Map.Entry<String, Integer> entry : statChanges.entrySet()) {
                    String statName = entry.getKey();
                    int change = entry.getValue();
                    
                    Color color = change > 0 ? Color.GREEN : Color.RED;
                    String changeText = (change > 0 ? "+" : "") + change + " " + statName;
                    
                    statChangeLayout.setText(dialogueFont, changeText, color, 660, Align.left, false);
                    dialogueFont.setColor(color);
                    dialogueFont.draw(batch, statChangeLayout, dialogueX, statChangeY);
                    statChangeY -= 25; // Move down for next stat change
                }
                
                // Reset font color to white
                dialogueFont.setColor(Color.WHITE);
            }
        } else {
            dialogueFont.draw(batch, descriptionLayout, dialogueX, dialogueY - 40);
        }
    }

    // Helper Method - Updated to remove END_OF_DAY case
    private String getRegionNameForSlot(DayCycle.TimeSlot slot) {
        switch (slot) {
            case MORNING: return "Ui_Morning";
            case DAY: return "Ui_Day";
            case EVENING: return "Ui_Evening";
            default: return "Ui_Morning"; // fallback/default
        }
    }

    @Override public void hide() {}     // Not used but required by Screen interface
    @Override public void pause() {}    // Not used but required by Screen interface
    @Override public void resume() {}   // Not used but required by Screen interface
    
    @Override
    public void dispose() {
        // Release resources when screen is disposed
        if (batch != null) {
            batch.dispose();
        }
        if (dialogueFont != null) {
            dialogueFont.dispose();
        }
    }
}