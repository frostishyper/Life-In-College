package com.badlogic.D6D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class ScenarioEngine {

    private JsonValue scenarios;
    private int currentIndex = 0;

    public ScenarioEngine() {
        JsonReader reader = new JsonReader();
        FileHandle file = Gdx.files.internal("Data/Tester.json");
        scenarios = reader.parse(file);
    }

    public String getTitle() {
        return scenarios.get(currentIndex).getString("title", "Missing Title");
    }

    public String getDescription() {
        return scenarios.get(currentIndex).getString("description", "Missing Description");
    }

    public void next() {
        if (currentIndex + 1 < scenarios.size) {
            currentIndex++;
        }
    }

    public void previous() {
        if (currentIndex > 0) {
            currentIndex--;
        }
    }

    public void jumpTo(int id) {
        for (int i = 0; i < scenarios.size; i++) {
            if (scenarios.get(i).getInt("Day", -1) == id) {
                currentIndex = i;
                break;
            }
        }
    }
}
