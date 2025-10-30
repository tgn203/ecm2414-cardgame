// CardTest.java

package cardgame_test;

import cardgame.Card;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link cardgame.Card} class.
 */
public class CardTest {

    /**
     * Tests that a card can be created and its value can be retrieved.
     */
    @Test
    public void testValidCardCreation() {
        Card c = new Card(5);
        assertEquals(5, c.getValue());
    }

    /**
     * Tests that a card with an invalid value throws an error.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCardCreation() {
        new Card(-1);
    }

    /**
     * Tests that a card's value can be converted to a string.
     */
    @Test
    public void testToString() {
        Card c = new Card(3);
        assertEquals("3", c.toString());
    }
}
