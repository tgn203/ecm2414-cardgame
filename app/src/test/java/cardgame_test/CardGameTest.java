// CardGameTest.java

package cardgame_test;

import cardgame.*;
import java.io.*;
import java.util.*;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link cardgame.CardGame} class.
 */
public class CardGameTest {

    private CardGame game;

    /**
     * Set up the game before each test.
     */
    @Before
    public void setup() {
        game = new CardGame();

        // Clean any old output.
        File out = new File("out");
        if (out.exists()) {
            // If the folder exists, remove the contents.
            for (File f : Objects.requireNonNull(out.listFiles())) {
                f.delete();
            }
        } else {
            // If the folder doesn't exist, make it.
            out.mkdirs();
        }
    }

    /**
     * Utility function to run a private method in a public way.
     *
     * @param name the name of the method of {@link cardgame.CardGame}.
     * @param params the parameter types of the arguments (if any) of the method.
     * @param args the arguments (if any) of the method.
     * @return an object that reflectively runs the private methods.
     * @throws Exception catches exceptions generically.
     */
    private Object callPrivate(String name, Class<?>[] params, Object... args) throws Exception {
        Method m = CardGame.class.getDeclaredMethod(name, params);
        m.setAccessible(true);
        return m.invoke(game, args);
    }

    /**
     * Utility function to create a valid pack.
     *
     * @param n the number of players.
     * @return the valid pack file.
     * @throws IOException
     */
    private File makeTempValidPack(int n) throws IOException {
        File file = File.createTempFile("valid", ".txt");
        try (PrintWriter w = new PrintWriter(file)) {
            // Add values to the file.
            for (int i = 0; i < 8 * n; i++) {
                w.println(i % 5);
            }
        }
        return file;
    }

    /**
     * Tests if a valid pack is accepted.
     *
     * @throws Exception if there was a problem with running the private method.
     */
    @Test
    public void testValidPackFile() throws Exception {
        // Use a temporary valid pack.
        File pack = makeTempValidPack(4);

        // Test the pack.
        boolean result = (boolean) callPrivate("loadPack", new Class[]{File.class, int.class}, pack, 4);
        assertTrue(result);
    }

    /**
     * Tests if a pack with too few cards is rejected.
     *
     * @throws Exception if there was a problem with running the private method.
     */
    @Test
    public void testLoadPackInvalidCount() throws Exception {
        // Create a temporary invalid pack file.
        File tempFile = File.createTempFile("invalid_pack", ".txt");
        try (PrintWriter writer = new PrintWriter(tempFile)) {
            // Too few cards.
            writer.println("1");
        }

        // Test the pack.
        boolean result = (boolean) callPrivate("loadPack", new Class[]{File.class, int.class}, tempFile, 4);
        assertFalse(result);
    }

    /**
     * Tests if a pack with a blank line is rejected.
     *
     * @throws Exception if there was a problem with running the private method.
     */
    @Test
    public void testEmptyLineInPack() throws Exception {
        // Create a temp valid pack file
        File tempFile = File.createTempFile("empty_line_invalid", ".txt");
        try (PrintWriter writer = new PrintWriter(tempFile)) {
            // Empty line:
            writer.println("1");
            writer.println("");
            writer.println("2");
        }

        // Test the pack.
        boolean result = (boolean) callPrivate("loadPack", new Class[]{File.class, int.class}, tempFile, 4);
        assertFalse(result);
    }

    /**
     * Tests if the correct number of decks are created and initialised.
     *
     * @throws Exception if there was a problem with running the private method.
     */
    @Test
    public void testInitDecks() throws Exception {
        callPrivate("initDecks", new Class[]{int.class}, 4);

        // Get the private field 'decks'.
        Field decksField = CardGame.class.getDeclaredField("decks");
        decksField.setAccessible(true);

        // Test the list of decks.
        @SuppressWarnings("unchecked")
        List<Deck> decks = (List<Deck>) decksField.get(game);
        assertEquals(4, decks.size());
    }

    /**
     * Tests if the correct number of players are created and initialised.
     *
     * @throws Exception if there was a problem with running the private method.
     */
    @Test
    public void testInitPlayers() throws Exception {
        callPrivate("initDecks", new Class[]{int.class}, 3);
        callPrivate("initPlayers", new Class[]{int.class}, 3);

        // Get the private field 'players'.
        Field playersField = CardGame.class.getDeclaredField("players");
        playersField.setAccessible(true);

        // Test the list of players.
        @SuppressWarnings("unchecked")
        List<Player> players = (List<Player>) playersField.get(game);
        assertEquals(3, players.size());
    }

    /**
     * Tests that all players receive exactly 4 cards to their hand.
     *
     * @throws Exception if there was a problem with running the private method.
     */
    @Test
    public void testDealCards() throws Exception {
        callPrivate("initDecks", new Class[]{int.class}, 2);
        callPrivate("initPlayers", new Class[]{int.class}, 2);

        // Get the private field 'pack'.
        Field packField = CardGame.class.getDeclaredField("pack");
        packField.setAccessible(true);

        // Fill the pack with cards [0..16].
        @SuppressWarnings("unchecked")
        List<Card> pack = (List<Card>) packField.get(game);
        for (int i = 0; i < 16; i++) {
            pack.add(new Card(i));
        }

        callPrivate("dealCards", new Class[]{int.class}, 2);

        // Get the private field 'players'.
        Field playersField = CardGame.class.getDeclaredField("players");
        playersField.setAccessible(true);

        // Test the list of players: each should have 4 cards.
        @SuppressWarnings("unchecked")
        List<Player> players = (List<Player>) playersField.get(game);
        for (Player p : players) {
            assertEquals(4, p.getHand().size());
        }
    }

    /**
     * Tests that deck contents are correctly written to file.
     *
     * @throws Exception if there was a problem with running the private method.
     */
    @Test
    public void testWriteDeckOutputs() throws Exception {
        callPrivate("initDecks", new Class[]{int.class}, 2);

        // Get the private field 'decks'.
        Field decksField = CardGame.class.getDeclaredField("decks");
        decksField.setAccessible(true);

        // Put some cards into each deck.
        @SuppressWarnings("unchecked")
        List<Deck> decks = (List<Deck>) decksField.get(game);
        for (Deck d : decks) {
            d.addCard(new Card(5));
            d.addCard(new Card(5));
        }

        callPrivate("writeDeckOutputs", new Class[]{}, new Object[]{});

        // Check that the output files exist and contents are correct.
        for (Deck d : decks) {
            // Check file exists.
            File f = new File("out/deck" + d.getDeckId() + "_output.txt");
            assertTrue(f.exists());

            // Check contents are correct.
            String content = Files.readString(f.toPath()).trim();
            assertTrue(content.contains("deck" + d.getDeckId()));
        }
    }

    /**
     * Clean up after tests: remove output files.
     */
    @After
    public void tearDown() {
        // Clean up output files directory.
        File out = new File("out");
        if (out.exists()) {
            for (File f : Objects.requireNonNull(out.listFiles())) {
                f.delete();
            }
            out.delete();
        }
    }
}
