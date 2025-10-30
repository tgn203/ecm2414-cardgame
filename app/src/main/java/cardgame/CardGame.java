// CardGame.java

package cardgame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Main executable class for the card game.
 * Handles setup, validation and game lifecycle.
 */
public class CardGame {

    private final List<Player> players = new ArrayList<>();
    private final List<Deck> decks = new ArrayList<>();
    private final List<Card> pack = new ArrayList<>();
    private final List<Thread> threads = new ArrayList<>();

    /**
     * The executed game function.
     * Calls start method defined by the class.
     */
    public static void main(String[] args) {
        CardGame game = new CardGame();
        game.start();
    }

    public void start() {
        // Read from the terminal:
        Scanner sc = new Scanner(System.in);

        // Get the number of players:
        int numPlayers = 0;
        while (numPlayers <= 0)  {  // Repeat until valid input
            System.out.println("Please enter the number of players: ");
            try {
                numPlayers = Integer.parseInt(sc.nextLine());   // Convert to a number
                if (numPlayers <= 0) {
                    System.out.println("Number of players must be positive.");
                }
            } catch (NumberFormatException e) {
                // Catch invalid formats using error from parseInt:
                System.out.println("Invalid format. Please enter a positive integer.");
            }
        }

        // Get the pack file:
        File packFile = null;
        while (packFile == null || !packFile.exists()) {    // Repeat until valid input
            System.out.println("Please enter location of pack to load: ");
            String path = sc.nextLine();
            packFile = new File(path);

            // Ensure the file exists on the system:
            if (!packFile.exists()) {
                System.out.println("File not found, please try again.");
            }
        }

        // Close the scanner to prevent resource leaks:
        sc.close();

        // Read the pack file, validate contents:
        if (!loadPack(packFile, numPlayers)) {
            System.out.println("Invalid pack file: must contain 8 times the number of players of non-negative cards.");
            return;
        }

        // Initialise decks and players:
        initDecks(numPlayers);
        try {
            initPlayers(numPlayers);
        } catch (IOException e) {
            System.err.println("Error creating log files: " + e.getMessage());
            return;
        }

        // Deal cards:
        dealCards(numPlayers);

        // Create and start all player threads:
        for (Player p : players) {
            Thread t = new Thread(p, "Player-" + p.getPlayerId());
            threads.add(t);
            t.start();
        }

        // Wait for all threads to finish:
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                // If the program is stopped manually, stop the threads:
                Thread.currentThread().interrupt();
            }
        }

        // Write final deck states:
        writeDeckOutputs();
        System.out.println("Game finished. Output files generated.");
    }

    /**
     * Reads the pack file and adds new cards to the pack.
     * Validates card contents and quantity.
     *
     * @param file the file to read from.
     * @param numPlayers the number of players in the game (num cards = 8 * num players)
     * @return if the pack is valid.
     */
    private boolean loadPack(File file, int numPlayers) {
        // Attempt to read the file:
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {     // Repeat until the line is empty
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) return false;

                // Check the line is a number:
                int value;
                try {
                    value = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    return false;   // Not an integer
                }

                if (value < 0) return false;    // Must be >=0
                pack.add(new Card(value));
            }
        } catch (IOException e) {
            //  Catch errors from trying to read the file, e.g. access
            System.out.println("Error reading pack file: " + e.getMessage());
            return false;
        }
        return pack.size() == 8 * numPlayers;
    }

    /**
     * Creates n blank decks for the start of the game, num decks = num players.
     *
     * @param n the number of decks to initialise.
     */
    private void initDecks(int n) {
        for (int i = 0; i < n; i++) {
            decks.add(new Deck());
        }
    }

    /**
     * Initialise n blank players for the start of the game, each with a draw (left) and discard (right) deck.
     *
     * @param n the number of players to initialise.
     * @throws IOException
     */
    private void initPlayers(int n) throws IOException {
        for (int i = 0; i < n; n++) {
            Deck left = decks.get(i);
            Deck right = decks.get((i+1) % n);  // Loop back at the end.
            players.add(new Player(left, right));
        }
    }

    /**
     * Deals cards in a round-robin order to players and decks.
     *
     * Players first get 4 cards each, then the remainder are split equally between the decks.
     *
     * @param n the number of players in the game.
     */
    private void dealCards(int n) {
        int index = 0;

        // Deal 4 cards to each player
        for (int i = 0; i < 4 * n; i++) {
            players.get(i % n).addCardToHand(pack.get(index++));    // Loop through players
        }

        // Deal remaining cards to the decks
        for (int i = 0; i < 4 * n; i++) {
            decks.get(i % n).addCard(pack.get(index++));    // Loop through decks
        }
    }

    private void writeDeckOutputs() {
        for (Deck d : decks) {
            try (FileWriter f = new FileWriter("deck" + d.getDeckId() + "_output.txt")) {
                f.write("deck" + d.getDeckId() + " contents: " + d.contentsAsString() + "\n");
            } catch (IOException e) {
                System.err.println("Error writing deck file: " + e.getMessage());
            }
        }
    }
}
