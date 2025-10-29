// Card.java

package main.java.cardgame;

/**
 * Thread-safe card class.
 */
public final class Card {

    private final int value;

    /**
     * Constructs a card with a specified value.
     *
     * @param value Non-negative integer: the card's denomination.
     * @throws IllegalArgumentException if value is negative.
     */
    public Card(int value) throws IllegalArgumentException {
        if (value < 0) {
            throw new IllegalArgumentException("Card value must be non-negative.");
        }
        this.value = value;
    }

    /**
     * Gets the value (denomination) of the card.
     *
     * @return the card value.
     */
    public int getValue() {
        return value;
    }

    /**
     * String representation of the card.
     *
     * @return the string form of the value.
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
