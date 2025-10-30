// Deck.java

package cardgame;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Thread-safe, FIFO deck of cards.
 * Draw from the top and discard to the bottom.
 */
public class Deck {

    private static int idCounter = 1;
    private final int deckId;
    private final Queue<Card> cards;

    /**
     * Constructs an empty list with an ID (automatically incremented).
     */
    public Deck() {
        synchronized (Deck.class) {
            this.deckId = idCounter++;
        }
        this.cards = new LinkedList<>();
    }

    /**
     * Adds a card to the bottom of the deck.
     *
     * @param card to add to the bottom.
     */
    public synchronized void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Draws (removes) a card from the top of the deck.
     *
     * @return the drawn card, or null if empty.
     */
    public synchronized Card drawCard() {
        return cards.poll();
    }

    /**
     * Gets this deck's ID.
     *
     * @return the ID of this deck.
     */
    public int getDeckId() {
        return deckId;
    }

    /**
     * Get the size of the list.
     *
     * @return the size of the list.
     */
    public synchronized int size() {
        return cards.size();
    }

    /**
     * Shows the deck's contents as a space-separated string.
     *
     * @return the string form of the deck, separated by spaces.
     */
    public synchronized String contentsAsString() {
        StringBuilder sb = new StringBuilder();
        for (Card c : cards) {
            sb.append(c.getValue()).append(" ");
        }
        return sb.toString().trim();
    }

    /**
     * String representation of the deck.
     *
     * @return the string form of the deck.
     */
    @Override
    public synchronized String toString() {
        return "Deck " + deckId + contentsAsString();
    }
}
