package part3;

public class Character {
    private String name;
    private CharacterState state;
    private Spaceship currentSpaceship;  // Link back to spaceship

    public Character(String name) {
        this.name = name;
        this.state = CharacterState.NORMAL;
    }

    public Character() {
        this.state = CharacterState.NORMAL;
    }

    public void board(Spaceship spaceship) {
        if (this.getCurrentSpaceship()!= null){
            this.leaveSpaceship();
        }
        if (spaceship.addCharacter(this)) {  // Add character to spaceship
            this.currentSpaceship = spaceship;  // Set reference to spaceship
            System.out.println(this.name + " boarded the spaceship");
        }
    }

    public void leaveSpaceship() {
        if (currentSpaceship != null) {
            currentSpaceship.removeCharacter(this);
            System.out.println(this.name + " left the spaceship");
            this.currentSpaceship = null;
        }
    }

    public Spaceship getCurrentSpaceship() {
        return currentSpaceship;
    }

    public void shout (){
        System.out.println(this.name + " is shouting");
        // Character can interact with spaceship based on state
        if (currentSpaceship != null && state == CharacterState.TERRIFIED) {
            System.out.println("The terrified shout echoes through the ship!");
        }
    }

    public void shoot(){
        System.out.println(this.name + " shoots piu piu");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CharacterState getState() {
        return state;
    }

    public void becomeTerrified() {
        this.state = CharacterState.TERRIFIED;
        System.out.println(this.name + " is terrified");

        // Terrified character affects the spaceship
        if (currentSpaceship != null) {
            currentSpaceship.handleTerrifiedCharacter(this);
        }
    }

    public void becomeDesperate() {
        this.state = CharacterState.DESPERATE;
        System.out.println(name + " became desperate");

        // Desperate character affects the spaceship
        if (currentSpaceship != null) {
            currentSpaceship.handleDesperateCharacter(this);
        }
    }
}