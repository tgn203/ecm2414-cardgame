// PlayerTest.java

package cardgame_test;

import cardgame.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

/**
 * Unit tests for the {@link cardgame.Player} class.
 */
public class PlayerTest {

    /**
     * Tests that PlayerIDs are automatically incremented for each new instance.
     *
     * @throws IOException
     */
    @Test
    public void testAutomaticIdAssignment() throws IOException {
        // Create left and right decks for each player.
        Deck p1_left = new Deck();
        Deck p1_right = new Deck();
        Deck p2_left = new Deck();
        Deck p2_right = new Deck();

        // Create two different players.
        Player p1 = new Player(p1_left, p1_right);
        Player p2 = new Player(p2_left, p2_right);

        assertNotEquals(p1.getPlayerId(), p2.getPlayerId());
    }

    /**
     * Tests that cards can be added to the hand.
     *
     * @throws IOException
     */
    @Test
    public void testAddingToHand() throws IOException {
        // Create decks and the player object.
        Deck left = new Deck();
        Deck right = new Deck();
        Player p = new Player(left, right);

        // Add cards.
        p.addCardToHand(new Card(1));
        p.addCardToHand(new Card(2));
        p.addCardToHand(new Card(3));
        p.addCardToHand(new Card(4));

        assertEquals(4, p.getHand().size());
    }

    /**
     * Test the winning hand detection (four identical cards).
     *
     * Uses reflection to create an accessible copy of the static method.
     *
     * @throws IOException
     */
    @Test
    public void testHasWinningHand() throws IOException {
        // Create decks and the player object.
        Deck left = new Deck();
        Deck right = new Deck();
        Player p = new Player(left, right);

        // Add four identical cards to the hand, instantly winning.
        for (int i = 0; i < 4; i++) {
            p.addCardToHand(new Card(7));
        }

        // Use reflection to test private method.
        try {
            var m = Player.class.getDeclaredMethod("hasWinningHand");
            m.setAccessible(true);
            boolean result = (boolean) m.invoke(p);
            assertTrue(result);
        } catch (Exception e) {
            fail("Reflection test failed: " + e.getMessage());
        }
    }
}
