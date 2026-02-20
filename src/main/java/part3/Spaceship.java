package part3;

import java.util.ArrayList;
import java.util.List;

public class Spaceship {
    private int spaces;
    private Engine engine;
    private ShipState state;
    private List<Character> characters;

    public Spaceship(int spaces) {
        this.spaces = spaces;
        this.engine = new Engine();
        this.state = ShipState.OFF;
        this.characters = new ArrayList<Character>();
    }

    public void turnOn(){
        this.engine.setStarted(true);
        this.state = ShipState.STARTED;
        notifyAllCharacters("The ship is turning on!");
    }

    public boolean addCharacter(Character character) {  // Changed to return boolean
        if (characters.size() < spaces && character != null) {
            this.characters.add(character);
            return true;
        }
        System.out.println("No more space for characters!");
        return false;
    }

    public void removeCharacter(Character character) {
        if (this.characters.remove(character)) {
            System.out.println(character.getName() + " removed from ship");
        }
    }

    public void handleTerrifiedCharacter(Character character) {
        System.out.println("The ship shakes from " + character.getName() + "'s terror!");
    }

    public void handleDesperateCharacter(Character character) {
        System.out.println(character.getName() + "'s desperation affects the ship's systems!");
    }

    private void notifyAllCharacters(String message) {
        for (Character c : characters) {
            System.out.println(c.getName() + " hears: " + message);
        }
    }

    public void launch() {
        if (state == ShipState.STARTED && engine.isStarted()) {
            this.state = ShipState.LAUNCHED;
            System.out.println("Ship launching with " + characters.size() + " characters!");

            // All characters react to launch
            for (Character c : characters) {
                if (c.getState() == CharacterState.NORMAL) {
                    System.out.println(c.getName() + " is excited about the launch!");
                }
            }
        }
    }

    // Getters and setters remain the same
    public int getSpaces() { return spaces; }
    public void setSpaces(int spaces) { this.spaces = spaces; }
    public Engine getEngine() { return engine; }
    public void setEngine(Engine engine) { this.engine = engine; }
    public ShipState getState() { return state; }
    public void setState(ShipState state) { this.state = state; }
    public List<Character> getCharacters() { return characters; }
    public int getAvailableSpaces() { return spaces - characters.size(); }
}