package com.badlogic.D6D;

public class DayCycle {
    public enum TimeSlot {
        MORNING,
        DAY,
        EVENING,
        END_OF_DAY
    }

    private TimeSlot currentSlot;
    private int tick;
    private int maxTick;
    private int currentDay;

    public DayCycle() {
        currentDay = 1;
        currentSlot = TimeSlot.MORNING;
        tick = 0;
        maxTick = getMaxTickForSlot(currentSlot);
    }

    private int getMaxTickForSlot(TimeSlot slot) {
        switch (slot) {
            case MORNING: return 1;
            case DAY: return 2;
            case EVENING: return 1;
            default: return 0;
        }
    }

    public TimeSlot getCurrentTimeSlot() {
        return currentSlot;
    }

    public int getCurrentTick() {
        return tick;
    }

    public int getRemainingTicks() {
        return maxTick - tick;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void progressTick() {
        tick++;

        if (tick >= maxTick) {
            moveToNextSlot();
        }
    }

    private void moveToNextSlot() {
        switch (currentSlot) {
            case MORNING:
                currentSlot = TimeSlot.DAY;
                break;
            case DAY:
                currentSlot = TimeSlot.EVENING;
                break;
            case EVENING:
                currentSlot = TimeSlot.END_OF_DAY;
                break;
            case END_OF_DAY:
                startNewDay(); // move to next day
                break;
        }

        tick = 0;
        maxTick = getMaxTickForSlot(currentSlot);
    }

    private void startNewDay() {
        currentDay++;
        currentSlot = TimeSlot.MORNING;
        tick = 0;
        maxTick = getMaxTickForSlot(currentSlot);
    }

    public boolean isDayOver() {
        return currentSlot == TimeSlot.END_OF_DAY;
    }
}

