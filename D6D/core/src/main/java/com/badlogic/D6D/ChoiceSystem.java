package com.badlogic.D6D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChoiceSystem {
    private final Array<UiButton> choiceButtons = new Array<>();
    private final Array<UiDisplay> statIcons = new Array<>();
    private UiButton continueButton;
    private final TextureAtlas ui;
    private final Viewport viewport;
    private final Random random = new Random();
    private final ScenarioEngine scenarioEngine;
    private final DayCycle dayCycle;
    private String outcomeText = "";
    private boolean showingOutcome = false;
    private BitmapFont buttonFont;
    private Map<String, Integer> statChanges = new HashMap<>(); // Track stat changes
    
    public ChoiceSystem(TextureAtlas uiAtlas, Viewport viewport, ScenarioEngine scenarioEngine, DayCycle dayCycle) {
        this.ui = uiAtlas;
        this.viewport = viewport;
        this.scenarioEngine = scenarioEngine;
        this.dayCycle = dayCycle;
        
        // Initialize font for button text
        buttonFont = new BitmapFont();
        buttonFont.getData().setScale(0.9f);
    }
    
    // Generate Choice buttons based on the current scenario
    public void generateChoiceButtons() {
        clearButtons();
        statChanges.clear(); // Clear previous stat changes
        
        if (showingOutcome) {
            createContinueButton();
            return;
        }
        
        JsonValue currentScenario = scenarioEngine.getCurrentScenario();
        JsonValue choices = currentScenario.get("choices");
        
        if (choices == null || choices.size == 0) {
            return;
        }
        
        int choiceCount = choices.size;
        int buttonWidth = 320;
        int buttonHeight = 60;
        int baseX = 500; // Center of dialogue box
        int baseY = 110; // Below dialogue box
        
        // Calculate starting positions
        // For 2x2 grid layout, adjust spacing as needed
        for (int i = 0; i < choiceCount; i++) {
            JsonValue choice = choices.get(i);
            String choiceText = choice.getString("text", "Empty Choice");
            
            // Position buttons in a 2x2 grid
            int row = i / 2;
            int col = i % 2;
            int x = baseX + (col * (buttonWidth + 40)) - buttonWidth/2; // Position adjusted for 2 columns
            int y = baseY + (row * (buttonHeight + 20)); // Position adjusted for 2 rows
            
            UiButton button = new UiButton(ui, "BTN_Select", 1, 1.0f, x, y, buttonWidth, buttonHeight, viewport);
            button.setText(choiceText);
            button.setTextFont(buttonFont);
            
            final int choiceIndex = i;
            button.setOnClick(() -> handleChoice(choiceIndex));
            choiceButtons.add(button);
            
            // Check if this choice has a stat check
            if (choice.has("check")) {
                JsonValue check = choice.get("check");
                String statName = check.getString("stat", "");
                int requiredValue = check.getInt("value", 1);
                int statValue = Player.getInstance().getStat(statName);
                
                // Calculate success chance
                int successChance = Math.min(100, Math.max(0, (statValue * 10) / requiredValue));
                
                // Add stat icon beside the button
                UiDisplay statIcon = new UiDisplay(ui, "STAT_" + statName, 1, 1.0f, x + buttonWidth - 30, y + buttonHeight/2 - 20, 32, 32, viewport);
                statIcons.add(statIcon);
                
                // Update button text to include chance of success
                button.setText(choiceText + " (" + successChance + "%)");
            }
        }
    }
    
    // Choice Selection Handler
    public void handleChoice(int choiceIndex) {
        JsonValue currentScenario = scenarioEngine.getCurrentScenario();
        JsonValue choices = currentScenario.get("choices");
        
        if (choices == null || choiceIndex >= choices.size) {
            return;
        }
        
        JsonValue choice = choices.get(choiceIndex);
        Player player = Player.getInstance();
        JsonValue effectToApply = null;
        String newBranch = "0";
        
        // Check if it's a stat check choice
        if (choice.has("check")) {
            JsonValue check = choice.get("check");
            String statName = check.getString("stat", "");
            int requiredValue = check.getInt("value", 1);
            int statValue = player.getStat(statName);
            
            // Calculate success chance and roll for success
            int successChance = Math.min(100, Math.max(0, (statValue * 10) / requiredValue));
            boolean success = random.nextInt(100) < successChance;
            
            // Apply results based on pass/fail
            effectToApply = success ? check.get("onPass") : check.get("onFail");
            if (effectToApply != null) {
                newBranch = effectToApply.getString("branch", "0");
            }
        } 
        // Handle normal effect with branching
        else if (choice.has("effect")) {
            JsonValue effect = choice.get("effect");
            effectToApply = effect;
            
            // Set the branch state in the scenario engine
            if (effect.has("branch")) {
                newBranch = effect.getString("branch", "0");
            }
            
            // If there's a specific branch effect and we're already on a non-zero branch
            if (!scenarioEngine.getCurrentBranch().equals("0") && choice.has("branchEffect")) {
                effectToApply = choice.get("branchEffect");
                if (effectToApply.has("branch")) {
                    newBranch = effectToApply.getString("branch", "0");
                }
            }
        }
        
        // Apply the effects if we have any
        if (effectToApply != null) {
            applyEffects(player, effectToApply);
            scenarioEngine.setBranch(newBranch);
        }
        
        // Show outcome and create continue button
        showingOutcome = true;
        clearButtons();
        createContinueButton();
    }
    
    // Applies the effects of a choice
    private void applyEffects(Player player, JsonValue effect) {
        // Store outcome text
        outcomeText = effect.getString("outcome", "No outcome specified");
        
        // Apply stat changes if any
        if (effect.has("stats")) {
            JsonValue stats = effect.get("stats");
            for (JsonValue stat : stats) { // Use iterator to loop through child elements
                String statName = stat.name(); // Get the key (stat name)
                int value = stat.asInt();     // Get the value
                
                // Store the stat change for display
                statChanges.put(statName, value);
                
                if (statName.equals("Health")) {
                    player.modifyHealth(value);
                } else if (statName.equals("Sanity")) {
                    player.modifySanity(value);
                } else {
                    player.adjustStat(statName, value);
                }
            }
        }
    }
    
    // Create Continue
    private void createContinueButton() {
        int buttonWidth = 200;
        int buttonHeight = 60;
        int x = 600; // Center below dialogue box
        int y = 150;
        
        continueButton = new UiButton(ui, "BTN_Select", 1, 1.0f, x - buttonWidth/2, y, buttonWidth, buttonHeight, viewport);
        continueButton.setText("Continue");
        continueButton.setTextFont(buttonFont);
        
        continueButton.setOnClick(() -> {
            // Progress the day cycle and move to next scenario
            dayCycle.progressTick();
            showingOutcome = false;
            scenarioEngine.setBranch("0"); // Reset branch when continuing
            
            // If we've reached the end of the current time slot, find next scenario
            // Otherwise just continue to the next scenario
            if (dayCycle.getCurrentTick() == 0) {
                scenarioEngine.findNextScenario();
            } else {
                scenarioEngine.next();
            }
            
            // Generate new choice buttons for the next scenario
            generateChoiceButtons();
        });
    }
    
    // Clears all choice buttons and stat icons
    public void clearButtons() {
        choiceButtons.clear();
        statIcons.clear();
        continueButton = null;
    }
    
    // Renders the choice buttons and stat icons
    public void render(SpriteBatch batch) {
        
        // Render choice buttons
        for (UiButton button : choiceButtons) {
            button.render(batch);
        }

        // Render stat icons
        for (UiDisplay icon : statIcons) {
            icon.render(batch);
        }
        
        // Render continue button if showing outcome
        if (continueButton != null) {
            continueButton.render(batch);
        }
    }
    
    public void update(float delta) {
        // Update all choice buttons
        for (UiButton button : choiceButtons) {
            button.update(delta);
        }
        
        if (continueButton != null) {
            continueButton.update(delta);
        }
        
        for (UiDisplay icon : statIcons) {
            icon.update(delta);
        }
    }
    
    // Get the full outcome text with stat changes
    public String getOutcomeText() {
        if (statChanges.isEmpty()) {
            return outcomeText;
        }
        
        StringBuilder fullOutcome = new StringBuilder(outcomeText);
        
        // Add stat changes to the outcome text
        for (Map.Entry<String, Integer> entry : statChanges.entrySet()) {
            String statName = entry.getKey();
            int change = entry.getValue();
            
            fullOutcome.append("\n");
            if (change > 0) {
                fullOutcome.append("+" + change + " " + statName);
            } else {
                fullOutcome.append(change + " " + statName);
            }
        }
        
        return fullOutcome.toString();
    }
    
    // Get stat changes for colored rendering
    public Map<String, Integer> getStatChanges() {
        return statChanges;
    }
    
    public boolean isShowingOutcome() {
        return showingOutcome;
    }
    
    // Cleanup
    public void dispose() {
        if (buttonFont != null) {
            buttonFont.dispose();
        }
    }
}