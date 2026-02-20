package part3;

import java.util.ArrayList;
import java.util.List;

class Rodent {
    private String type;
    private RodentState state;
    private List<Character> hypnotizedVictims;
    private boolean isAirborne;

    public Rodent(String type) {
        this.type = type;
        this.state = RodentState.APPROACHING;
        this.hypnotizedVictims = new ArrayList<>();
        this.isAirborne = true;
    }

    public void advance() {
        if (state == RodentState.APPROACHING) {
            this.state = RodentState.OVERHEAD;
        } else if (state == RodentState.OVERHEAD) {
            this.state = RodentState.HYPNOTIZING;
        }
    }

    public void hypnotize(Character target) {
        if (state == RodentState.HYPNOTIZING) {
            target.becomeHypnotized(this);
            hypnotizedVictims.add(target);
        }
    }

    public RodentState getState() { return state; }
    public String getType() { return type; }
    public List<Character> getHypnotizedVictims() { return hypnotizedVictims; }
}