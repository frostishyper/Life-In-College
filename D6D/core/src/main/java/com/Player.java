package com.tom.textrpg;

public class Player {
    private int wisdom;
    private int mental;
    private int constitution;
    private int reflex;
    private int courage;
    private int charisma;
    private int health;
    private int sanity;

    public Player() {
        // Default stats
        this.wisdom = 0;
        this.mental = 0;
        this.constitution = 0;
        this.reflex = 0;
        this.courage = 0;
        this.charisma = 0;
        this.health = 0;
        this.sanity = 0;
    }

    // Getters
    public int getWisdom() { return wisdom; }
    public int getMental() { return mental; }
    public int getConstitution() { return constitution; }
    public int getReflex() { return reflex; }
    public int getCourage() { return courage; }
    public int getCharisma() { return charisma; }
    public int getHealth() { return health; }
    public int getSanity() { return sanity; }

    // Setters
    public void setWisdom(int value) { wisdom = value; }
    public void setMental(int value) { mental = value; }
    public void setConstitution(int value) { constitution = value; }
    public void setReflex(int value) { reflex = value; }
    public void setCourage(int value) { courage = value; }
    public void setCharisma(int value) { charisma = value; }
    public void setHealth(int value) { health = value; }
    public void setSanity(int value) { sanity = value; }

    // Increment methods (optional for game use)
    public void increaseWisdom(int amount) { wisdom += amount; }
    public void increaseMental(int amount) { mental += amount; }
    public void increaseConstitution(int amount) { constitution += amount; }
    public void increaseReflex(int amount) { reflex += amount; }
    public void increaseCourage(int amount) { courage += amount; }
    public void increaseCharisma(int amount) { charisma += amount; }
    public void increaseHealth(int amount) { health += amount; }
    public void increaseSanity(int amount) { sanity += amount; }

    // Debug print (optional)
    public void printStats() {
        System.out.println("Player Stats:");
        System.out.println("Wisdom: " + wisdom);
        System.out.println("Mental: " + mental);
        System.out.println("Constitution: " + constitution);
        System.out.println("Reflex: " + reflex);
        System.out.println("Courage: " + courage);
        System.out.println("Charisma: " + charisma);
        System.out.println("Health: " + health);
        System.out.println("Sanity: " + sanity);
    }
}
