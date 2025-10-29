// Player.java

package main.java.cardgame;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A thread-safe player class.
 * Each player draws from the 'left' deck and discards to the 'right' deck.
 * Stop when a player wins the game (all four cards the same).
 */
public class Player implements Runnable {

    private static int idCounter = 1;
    private static volatile Integer winnerId = null;    // Shared between players

    private final int playerId;
    private final Deck leftDeck;
    private final Deck rightDeck;
    private final List<Card> hand;
    private final Random random;
    private final PrintWriter log;

    /**
     * Constructs an empty list with an ID (automatically incremented).
     *
     * @param leftDeck to draw from.
     * @param rightDeck to discard to.
     * @throws IOException if the log file can't be written to.
     */
    public Player(Deck leftDeck, Deck rightDeck) throws IOException {
        synchronized (Player.class) {
            this.playerId = idCounter++;
        }
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.hand = new ArrayList<>(4);
        this.random = new Random();
        this.log = new PrintWriter(new FileWriter("player" + playerId + "_output.txt"), true);
    }

    /**
     * Gets this player's ID.
     *
     * @return the ID.
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Return the hand as an array list.
     *
     * @return the hand array list.
     */
    public synchronized List<Card> getHand() {
        return new ArrayList<>(hand);
    }

    /**
     * Adds a given card to the player's hand
     *
     * @param card to add to their hand.
     */
    public synchronized void addCardToHand(Card card) {
        hand.add(card);
    }

    /**
     * Choose a card in the hand to discard.
     * Picked based on the preferred denomination to the player's number (ID)
     * Randomly discards another card if no preferred ones are present.
     *
     * @return the card to be discarded.
     */
    private synchronized Card chooseDiscard() {
        List<Card> notPreferred = new ArrayList<>();
        for (Card c : hand) {
            if (c.getValue() != playerId) {
                notPreferred.add(c);
            }
        }
        if (notPreferred.isEmpty()) {
            return hand.get(random.nextInt(hand.size()));
        }
        return notPreferred.get(random.nextInt(notPreferred.size()));
    }


    /**
     * Checks if this player has won (four of the same card).
     *
     * @return true if the player has won.
     */
    private synchronized boolean hasWinningHand() {
        if (hand.isEmpty()) return false;
        int value = hand.get(0).getValue(); // get the value of the first card
        for (Card c : hand) { // check if all cards have the same value
            if (c.getValue() != value) { // if any card is different return false
                return false;
            }
        }
        return true;
    }

    /**
     * Broadcast that the player has won.
     */
    private void declareWin() {
        // Only the first player can declare themselves as the winner
        synchronized (Player.class) {
            if (winnerId != null) return;   // Someone else won
            winnerId = playerId;
        }

        System.out.println("player " + playerId + " wins");
        log.println("player " + playerId + " wins");
        log.println("player " + playerId + " exits");
        log.println("player " + playerId + " final hand: " + handToString());
        log.close();
    }

    /**
     * Executes one atomic turn (a draw and a discard).
     */
    private void drawAndDiscard() {
        // Use synchronised decks as locks
        synchronized (leftDeck) {
            synchronized (rightDeck) {
                // If any player has won, stop playing:
                if (winnerId != null) return;

                // Draw a new card:
                Card drawn = leftDeck.drawCard();
                if (drawn == null) return;

                // Add the drawn card to hand:
                hand.add(drawn);
                log.println("player " + playerId + " draws a " + drawn.getValue() + " from deck " + leftDeck.getDeckId());

                // Choose a card to discard and discard it:
                Card discarded = chooseDiscard();
                hand.remove(discarded);

                // Add it to the next pile:
                rightDeck.addCard(discarded);
                log.println("player " + playerId + " discards a " + discarded.getValue() + " to deck " + rightDeck.getDeckId());
                log.println("player " + playerId + " current hand is " + handToString());

                // Check if this player has won:
                if (hasWinningHand()) {
                    declareWin();
                }
            }
        }
    }

    /**
     * Main thread for logic for the player.
     */
    @Override
    public void run() {
        try {
            log.println("player " + playerId + " initial hand " + handToString());

            // Check if the player has instantly won:
            if (hasWinningHand()) {
                declareWin();
                return;
            }

            // Main gameplay loop:
            while (winnerId == null) drawAndDiscard();

            // Game over - final log:
            log.println("player " + winnerId + " has informed player " + playerId + " that player " + winnerId + " has won");
            log.println("player " + playerId + " exits");
            log.println("player " + playerId + " hand: " + handToString());
            log.close();

        } catch (Exception e) {
            // Generically get errors
            log.println("Error: " +  e.getMessage());
        }
    }

    /**
     * Shows the hand contents as a space-separated string.
     *
     * @return the string form of the hand, separated by spaces.
     */
    private synchronized String handToString() {
        StringBuilder sb = new StringBuilder();
        for (Card c : hand) {
            sb.append(c.getValue()).append(" ");
        }
        return sb.toString().trim();
    }
}
