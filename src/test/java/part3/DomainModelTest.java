package part3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class DomainModelTest {
    private Scene scene;
    private Character trillian;
    private Character arthur;
    private Character ford;
    private Character zaphod;
    private Door mainDoor;
    private Rodent rodent;

    @BeforeEach
    public void setUp() {
        scene = new Scene();

        trillian = new Character("Trillian");
        arthur = new Character("Arthur");
        ford = new Character("Ford");
        zaphod = new Character("Zaphod");
        mainDoor = new Door("Main Exit");
        rodent = new Rodent("Airborne Gopher");

        scene.addCharacter(trillian);
        scene.addCharacter(arthur);
        scene.addCharacter(ford);
        scene.addCharacter(zaphod);
        scene.addDoor(mainDoor);
        scene.addRodent(rodent);
    }

    @Test
    @DisplayName("Test 1: Initial character states")
    public void testInitialStates() {
        assertEquals(CharacterState.NORMAL, trillian.getState());
        assertEquals(CharacterState.NORMAL, arthur.getState());
        assertEquals(CharacterState.NORMAL, ford.getState());
        assertEquals(CharacterState.NORMAL, zaphod.getState());
    }

    @Test
    @DisplayName("Test 2: Door initial state")
    public void testDoorInitialState() {
        assertEquals(DoorState.LOCKED, mainDoor.getState());
    }

    @Test
    @DisplayName("Test 3: Rodent initial state")
    public void testRodentInitialState() {
        assertEquals(RodentState.APPROACHING, rodent.getState());
    }

    @Test
    @DisplayName("Test 4: Trillian becomes desperate")
    public void testTrillianDesperation() {
        trillian.becomeDesperate();
        assertEquals(CharacterState.DESPERATE, trillian.getState());
        assertTrue(trillian.getActionLog().contains("Trillian became desperate"));
    }

    @Test
    @DisplayName("Test 5: Arthur becomes terrified")
    public void testArthurTerror() {
        arthur.becomeTerrified();
        assertEquals(CharacterState.TERRIFIED, arthur.getState());
    }

    @Test
    @DisplayName("Test 6: Hand grabbing interaction")
    public void testHandGrabbing() {
        trillian.grabHand(arthur);
        assertTrue(trillian.getActionLog().contains("Trillian grabbed Arthur's hand"));
        assertTrue(arthur.getActionLog().contains("Arthur is now being pulled by Trillian"));
    }

    @Test
    @DisplayName("Test 7: Door opening attempts")
    public void testDoorOpeningAttempts() {
        mainDoor.attemptOpen(ford);
        mainDoor.attemptOpen(zaphod);

        assertEquals(2, mainDoor.getAttemptedOpeners().size());
        assertTrue(mainDoor.getAttemptedOpeners().contains(ford));
        assertTrue(mainDoor.getAttemptedOpeners().contains(zaphod));
    }

    @Test
    @DisplayName("Test 8: Rodent advancement")
    public void testRodentAdvancement() {
        assertEquals(RodentState.APPROACHING, rodent.getState());
        rodent.advance();
        assertEquals(RodentState.OVERHEAD, rodent.getState());
        rodent.advance();
        assertEquals(RodentState.HYPNOTIZING, rodent.getState());
    }

    @Test
    @DisplayName("Test 9: Hypnotism effect")
    public void testHypnotism() {
        rodent.advance();
        rodent.advance();
        rodent.hypnotize(arthur);

        assertEquals(CharacterState.HYPNOTIZED, arthur.getState());
        assertTrue(rodent.getHypnotizedVictims().contains(arthur));
        assertTrue(arthur.getActionLog().contains("Arthur became hypnotized by Airborne Gopher"));
    }

    @Test
    @DisplayName("Test 10: Complete narrative execution")
    public void testNarrativeExecution() {
        scene.executeNarrative();

        // Verify narrative outcomes
        assertEquals(CharacterState.DESPERATE, trillian.getState());
        assertEquals(CharacterState.HYPNOTIZED, arthur.getState());

        // Door should be in process of being opened
        assertTrue(mainDoor.getState() == DoorState.BEING_OPENED ||
                mainDoor.getState() == DoorState.LOCKED);

        // Rodent should be hypnotizing
        assertEquals(RodentState.HYPNOTIZING, rodent.getState());
    }

    @ParameterizedTest
    @CsvSource({
            "Trillian, DESPERATE, true",
            "Arthur, HYPNOTIZED, true",
            "Ford, NORMAL, false",
            "Zaphod, NORMAL, false"
    })
    @DisplayName("Test 11: Parameterized character state tests")
    public void testCharacterStates(String name, CharacterState expectedState,
                                    boolean isAffectedByRodent) {
        Character character = null;
        switch(name) {
            case "Trillian": character = trillian; break;
            case "Arthur": character = arthur; break;
            case "Ford": character = ford; break;
            case "Zaphod": character = zaphod; break;
        }

        if (isAffectedByRodent && name.equals("Arthur")) {
            rodent.advance();
            rodent.advance();
            rodent.hypnotize(arthur);
        } else if (name.equals("Trillian")) {
            character.becomeDesperate();
        }

        assertEquals(expectedState, character.getState());
    }
}