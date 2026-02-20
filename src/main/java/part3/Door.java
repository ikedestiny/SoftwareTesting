package part3;

import java.util.ArrayList;
import java.util.List;

class Door {
    private String id;
    private DoorState state;
    private List<Character> attemptedOpeners;

    public Door(String id) {
        this.id = id;
        this.state = DoorState.LOCKED;
        this.attemptedOpeners = new ArrayList<>();
    }

    public void attemptOpen(Character character) {
        attemptedOpeners.add(character);
        if (character.getState() == CharacterState.DESPERATE) {
            // Desperate characters try harder
            if (Math.random() < 0.3) { // 30% chance
                this.state = DoorState.OPEN;
            }
        }
    }

    public void setBeingOpened(boolean beingOpened) {
        if (beingOpened && state == DoorState.LOCKED) {
            this.state = DoorState.BEING_OPENED;
        }
    }

    public DoorState getState() { return state; }
    public String getId() { return id; }
    public List<Character> getAttemptedOpeners() { return attemptedOpeners; }
}