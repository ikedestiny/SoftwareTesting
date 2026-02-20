package part3;
import java.util.ArrayList;
import java.util.List;

class Scene {
    private List<Character> characters;
    private List<Door> doors;
    private List<Rodent> rodents;
    private List<String> sceneEvents;

    public Scene() {
        characters = new ArrayList<>();
        doors = new ArrayList<>();
        rodents = new ArrayList<>();
        sceneEvents = new ArrayList<>();
    }

    public void addCharacter(Character c) { characters.add(c); }
    public void addDoor(Door d) { doors.add(d); }
    public void addRodent(Rodent r) { rodents.add(r); }

    public void executeNarrative() {
        Character trillian = characters.stream()
                .filter(c -> c.getName().equals("Trillian"))
                .findFirst().orElse(null);

        Character arthur = characters.stream()
                .filter(c -> c.getName().equals("Arthur"))
                .findFirst().orElse(null);

        Character ford = characters.stream()
                .filter(c -> c.getName().equals("Ford"))
                .findFirst().orElse(null);

        Character zaphod = characters.stream()
                .filter(c -> c.getName().equals("Zaphod"))
                .findFirst().orElse(null);

        Door mainDoor = doors.get(0);
        Rodent approachingRodent = rodents.get(0);

        if (trillian != null) {
            trillian.becomeDesperate();
            trillian.grabHand(arthur);

            if (arthur != null) {
                arthur.becomeTerrified();
            }

            trillian.pullTowards(mainDoor);
        }

        if (ford != null && zaphod != null) {
            mainDoor.attemptOpen(ford);
            mainDoor.attemptOpen(zaphod);
        }

        if (approachingRodent != null && arthur != null) {
            approachingRodent.advance();
            approachingRodent.advance();
            approachingRodent.hypnotize(arthur);
        }
    }

    public List<String> getSceneEvents() { return sceneEvents; }
}
