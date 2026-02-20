package part3;

import java.util.ArrayList;
import java.util.List;

class Character {
    private String name;
    private CharacterState state;
    private boolean isBeingPulled;
    private Character pulledBy;
    private List<String> actionLog;

    public Character(String name) {
        this.name = name;
        this.state = CharacterState.NORMAL;
        this.isBeingPulled = false;
        this.pulledBy = null;
        this.actionLog = new ArrayList<>();
    }

    public void grabHand(Character other) {
        actionLog.add(name + " grabbed " + other.getName() + "'s hand");
        other.setBeingPulled(true, this);
    }

    public void pullTowards(Door door) {
        if (isBeingPulled) {
            actionLog.add(name + " is being pulled towards " + door.getId());
            door.setBeingOpened(true);
        }
    }

    public void becomeHypnotized(Rodent rodent) {
        if (rodent.getState() == RodentState.HYPNOTIZING) {
            this.state = CharacterState.HYPNOTIZED;
            actionLog.add(name + " became hypnotized by " + rodent.getType());
        }
    }

    public void becomeTerrified() {
        this.state = CharacterState.TERRIFIED;
        actionLog.add(name + " became terrified");
    }

    public void becomeDesperate() {
        this.state = CharacterState.DESPERATE;
        actionLog.add(name + " became desperate");
    }

    public void setBeingPulled(boolean beingPulled, Character puller) {
        this.isBeingPulled = beingPulled;
        this.pulledBy = puller;
        if (beingPulled) {
            actionLog.add(name + " is now being pulled by " + puller.getName());
        }
    }

    public CharacterState getState() { return state; }
    public String getName() { return name; }
    public List<String> getActionLog() { return actionLog; }
}