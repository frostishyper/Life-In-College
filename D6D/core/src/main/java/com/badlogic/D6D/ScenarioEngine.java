package com.badlogic.D6D;

// Import Statements
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

// This class is responsible for reading scenarios from a JSON file
public class ScenarioEngine {

    private JsonValue scenarios;
    private int currentIndex = 0;
    private DayCycle dayCycle;
    private String currentBranch = "0";


    public ScenarioEngine() {
        JsonReader reader = new JsonReader();
        FileHandle file = Gdx.files.internal("Data/Tester.json");
        scenarios = reader.parse(file);
    }

    public void setDayCycle(DayCycle dayCycle) {
        this.dayCycle = dayCycle;
    }

    public String getTitle() {
        return getCurrentScenario().getString("title", "Missing Title");
    }

    public String getDescription() {
        return getCurrentScenario().getString("description", "Missing Description");
    }

    public JsonValue getCurrentScenario() {
        return scenarios.get(currentIndex);
    }

    public void next() {
        if (currentIndex + 1 < scenarios.size) {
            currentIndex++;
        } else {
            // Game over or loop back to first scenario
            currentIndex = 0;
        }
    }

    // Find The Right Scenario
    public void findNextScenario() {
        if (dayCycle == null) return;
        
        int day = dayCycle.getCurrentDay();
        DayCycle.TimeSlot slot = dayCycle.getCurrentTimeSlot();
        
        for (int i = 0; i < scenarios.size; i++) {
            JsonValue scenario = scenarios.get(i);
            
            // Check if this scenario matches the current day and time slot
            if (scenario.getInt("Day", 0) == day && 
                scenario.getString("timeSlot", "").equalsIgnoreCase(slot.name())) {
                currentIndex = i;
                return;
            }
        }
    }

    public void setBranch(String branch) {
        this.currentBranch = branch;
    }

    public String getCurrentBranch() {
        return currentBranch;
    }

    public void previous() {
        if (currentIndex > 0) {
            currentIndex--;
        }
    }

    public void jumpTo(int day) {
        for (int i = 0; i < scenarios.size; i++) {
            if (scenarios.get(i).getInt("Day", -1) == day) {
                currentIndex = i;
                break;
            }
        }
    }
    

    
}