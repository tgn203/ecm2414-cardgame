// DeckTest.java

package cardgame_test;

import cardgame.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link cardgame.Deck} class.
 */
public class DeckTest {

    /**
     * Tests that DeckIDs are automatically incremented for each new instance.
     */
    @Test
    public void testAutomaticIdAssignment() {
        Deck deck1 = new Deck();
        Deck deck2 = new Deck();
        assertNotEquals(deck1.getDeckId(), deck2.getDeckId());
    }

    /**
     * Tests that a card can be added and the size updates.
     * Also tests that that card can be drawn and the size updates.
     */
    @Test
    public void testAddAndDrawCard() {
        // Test adding a card.
        Deck deck = new Deck();
        Card c1 = new Card(10);
        deck.addCard(c1);
        assertEquals(1, deck.size());

        // Test drawing a card.
        Card drawn = deck.drawCard();
        assertEquals(10, drawn.getValue());
        assertEquals(0, deck.size());
    }

    /**
     * Tests that cards are drawn in the correct order.
     * Decks follow a first in, first out (FIFO) structure.
     */
    @Test
    public void testFirstInFirstOut() {
        // Add two cards, 1 then 2.
        Deck deck = new Deck();
        Card c1 = new Card(1);
        deck.addCard(c1);
        Card c2 = new Card(2);
        deck.addCard(c2);

        // Remove them in sequence:
        // First drawn, d1, should be the same as c1 (same value).
        Card d1 = deck.drawCard();
        assertEquals(c1.getValue(), d1.getValue());
        // Second drawn, d2, should be the same as c2 (same value).
        Card d2 = deck.drawCard();
        assertEquals(c2.getValue(), d2.getValue());
    }

    /**
     * Tests that the deck's contents can be displayed as a space-separated string.
     */
    @Test
    public void testContentsAsString() {
        Deck deck = new Deck();
        deck.addCard(new Card(5));
        deck.addCard(new Card(10));
        assertEquals("5 10", deck.contentsAsString());
    }
}
