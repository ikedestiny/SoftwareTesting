package part3;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class SpaceshipCharacterTest {

    private Spaceship ship;
    private Character alice;
    private Character bob;
    private Character charlie;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        // Redirect System.out to capture output for testing
        System.setOut(new PrintStream(outContent));

        // Create a new spaceship with 2 spaces
        ship = new Spaceship(2);

        // Create characters
        alice = new part3.Character("Alice");
        bob = new part3.Character("Bob");
        charlie = new part3.Character("Charlie");
    }

    @Test
    @DisplayName("Test Spaceship Initialization")
    void testSpaceshipInitialization() {
        assertNotNull(ship);
        assertEquals(2, ship.getSpaces());
        assertEquals(ShipState.OFF, ship.getState());
        assertNotNull(ship.getEngine());
        assertFalse(ship.getEngine().isStarted());
        assertTrue(ship.getCharacters().isEmpty());
        assertEquals(2, ship.getAvailableSpaces());
    }

    @Test
    @DisplayName("Test Character Initialization")
    void testCharacterInitialization() {
        assertNotNull(alice);
        assertEquals("Alice", alice.getName());
        Assertions.assertEquals(CharacterState.NORMAL, alice.getState());
        assertNull(alice.getCurrentSpaceship());
    }

    @Test
    @DisplayName("Test Character Boarding Spaceship")
    void testCharacterBoarding() {
        // Alice boards the ship
        alice.board(ship);

        // Verify Alice is on the ship
        assertTrue(ship.getCharacters().contains(alice));
        assertEquals(ship, alice.getCurrentSpaceship());
        assertEquals(1, ship.getCharacters().size());
        assertEquals(1, ship.getAvailableSpaces());

        // Bob boards the ship
        bob.board(ship);

        // Verify both are on the ship
        assertTrue(ship.getCharacters().contains(bob));
        assertEquals(2, ship.getCharacters().size());
        assertEquals(0, ship.getAvailableSpaces());

        // Charlie tries to board but no space
        charlie.board(ship);

        // Verify Charlie didn't board
        assertFalse(ship.getCharacters().contains(charlie));
        assertNull(charlie.getCurrentSpaceship());
        assertEquals(2, ship.getCharacters().size());

        // Verify output messages
        String output = outContent.toString();
        assertTrue(output.contains("Alice boarded the spaceship"));
        assertTrue(output.contains("Bob boarded the spaceship"));
        assertTrue(output.contains("No more space for characters!"));
    }

    @Test
    @DisplayName("Test Character Leaving Spaceship")
    void testCharacterLeaving() {
        // Add characters
        alice.board(ship);
        bob.board(ship);

        assertEquals(2, ship.getCharacters().size());

        // Alice leaves
        alice.leaveSpaceship();

        // Verify Alice left
        assertFalse(ship.getCharacters().contains(alice));
        assertNull(alice.getCurrentSpaceship());
        assertEquals(1, ship.getCharacters().size());
        assertEquals(1, ship.getAvailableSpaces());
        assertTrue(ship.getCharacters().contains(bob));

        // Verify output
        String output = outContent.toString();
        assertTrue(output.contains("Alice left the spaceship"));
    }

    @Test
    @DisplayName("Test Character Boarding When Already on Another Ship")
    void testBoardingWhenAlreadyOnShip() {
        Spaceship ship2 = new Spaceship(3);

        // Alice boards first ship
        alice.board(ship);
        assertEquals(ship, alice.getCurrentSpaceship());
        assertTrue(ship.getCharacters().contains(alice));

        // Try to board second ship
        alice.board(ship2);

  }

    @Test
    @DisplayName("Test Ship Turn On")
    void testShipTurnOn() {
        ship.turnOn();

        assertTrue(ship.getEngine().isStarted());
        assertEquals(ShipState.STARTED, ship.getState());

        // Add a character and verify they're notified
        alice.board(ship);
        outContent.reset();

        ship.turnOn(); // Turn on again

        String output = outContent.toString();
        assertTrue(output.contains("Alice hears: The ship is turning on!"));
    }

    @Test
    @DisplayName("Test Ship Launch")
    void testShipLaunch() {
        // Add characters
        alice.board(ship);
        bob.board(ship);

        // Try to launch without turning on
        outContent.reset();
        ship.launch();

        assertEquals(ShipState.OFF, ship.getState());
        String output = outContent.toString();
        assertFalse(output.contains("launching"));

        // Turn on and launch
        ship.turnOn();
        outContent.reset();
        ship.launch();

        assertEquals(ShipState.LAUNCHED, ship.getState());
        output = outContent.toString();
        assertTrue(output.contains("Ship launching with 2 characters!"));
        assertTrue(output.contains("Alice is excited about the launch!"));
        assertTrue(output.contains("Bob is excited about the launch!"));
    }

    @Test
    @DisplayName("Test Character State Changes and Ship Reactions")
    void testCharacterStateChanges() {
        alice.board(ship);
        bob.board(ship);

        // Alice becomes terrified
        outContent.reset();
        alice.becomeTerrified();

        assertEquals(CharacterState.TERRIFIED, alice.getState());
        String output = outContent.toString();
        assertTrue(output.contains("Alice is terrified"));
        assertTrue(output.contains("The ship shakes from Alice's terror!"));

        // Bob becomes desperate
        outContent.reset();
        bob.becomeDesperate();

        assertEquals(CharacterState.DESPERATE, bob.getState());
        output = outContent.toString();
        assertTrue(output.contains("Bob became desperate"));
        assertTrue(output.contains("Bob's desperation affects the ship's systems!"));
    }

    @Test
    @DisplayName("Test Character Actions")
    void testCharacterActions() {
        alice.board(ship);

        outContent.reset();
        alice.shout();

        String output = outContent.toString();
        assertTrue(output.contains("Alice is shouting"));

        // When terrified, shout affects ship
        outContent.reset();
        alice.becomeTerrified();
        alice.shout();

        output = outContent.toString();
        assertTrue(output.contains("The terrified shout echoes through the ship!"));

        outContent.reset();
        alice.shoot();
        output = outContent.toString();
        assertTrue(output.contains("Alice shoots piu piu"));
    }

    @Test
    @DisplayName("Test Multiple Characters and Edge Cases")
    void testMultipleCharactersAndEdgeCases() {
        // Add null character
        assertFalse(ship.addCharacter(null));

        // Add max characters
        alice.board(ship);
        bob.board(ship);

        assertEquals(2, ship.getCharacters().size());

        // Try to add beyond capacity
        assertFalse(ship.addCharacter(charlie));

        // Remove non-existent character
        ship.removeCharacter(charlie); // Should not throw exception

        // Remove existing character
        ship.removeCharacter(alice);
        assertFalse(ship.getCharacters().contains(alice));
        assertEquals(1, ship.getCharacters().size());

        // Try to leave ship when not on one
        charlie.leaveSpaceship(); // Should not throw exception
    }

    @Test
    @DisplayName("Test Engine Functionality")
    void testEngineFunctionality() {
        Engine engine = ship.getEngine();

        assertFalse(engine.isStarted());

        engine.setStarted(true);
        assertTrue(engine.isStarted());

        // Ship turn on should start engine
        ship.turnOn();
        assertTrue(engine.isStarted());
    }

    @Test
    @DisplayName("Test Getter and Setter Methods")
    void testGettersAndSetters() {
        // Test Spaceship setters/getters
        ship.setSpaces(10);
        assertEquals(10, ship.getSpaces());

        Engine newEngine = new Engine();
        ship.setEngine(newEngine);
        assertEquals(newEngine, ship.getEngine());

        ship.setState(ShipState.IN_SPACE);
        assertEquals(ShipState.IN_SPACE, ship.getState());

        // Test Character setters/getters
        alice.setName("Alice Cooper");
        assertEquals("Alice Cooper", alice.getName());

        // Character state should only change through become methods
        alice.becomeTerrified();
        assertEquals(CharacterState.TERRIFIED, alice.getState());
    }

    @Test
    @DisplayName("Test Ship State Transitions")
    void testShipStateTransitions() {
        assertEquals(ShipState.OFF, ship.getState());

        ship.turnOn();
        assertEquals(ShipState.STARTED, ship.getState());

        ship.launch();
        assertEquals(ShipState.LAUNCHED, ship.getState());

        // Test other states if methods exist
        ship.setState(ShipState.HUMMING);
        assertEquals(ShipState.HUMMING, ship.getState());

        ship.setState(ShipState.IN_SPACE);
        assertEquals(ShipState.IN_SPACE, ship.getState());
    }

    @Test
    @DisplayName("Test Character Default Constructor")
    void testCharacterDefaultConstructor() {
        part3.Character anonymous = new part3.Character();
        assertNotNull(anonymous);
        assertNull(anonymous.getName());
        assertEquals(CharacterState.NORMAL, anonymous.getState());

        anonymous.setName("Anonymous");
        assertEquals("Anonymous", anonymous.getName());
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }
}