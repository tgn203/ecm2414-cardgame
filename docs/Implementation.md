# Card Class

The first class created was the Card class. It is declared as final, making card objects immutable, which prevents their values from changing just as a real playing card’s value cannot change during a game. No setter methods are provided, further enforcing immutability. Because of this immutability, Card instances can be safely shared between threads without additional synchronization, making the class inherently thread safe. The constructor validates its input, throwing an IllegalArgumentException if a negative value is provided, which improves the reliability of the program. Additionally, each Card object stores only a single integer, keeping memory usage low and ensuring minimal performance overhead.

# CardGame Class

The second class, CardGame, acts as the central controller for the card game application. It is responsible for managing the entire game lifecycle, including initialization, execution, and termination. The class maintains four primary collections: players, representing all participants in the game; decks, representing the decks of cards used by players; pack, the full set of cards loaded from the input file; and threads, which run one thread per player to enable synchronized gameplay. All these ArrayList fields are declared as final, ensuring that, while their contents can change, the references themselves improve data safety and program stability.

The main method creates a new CardGame instance and calls its start() method. The start() method handles the setup process, starting with scanning user input from the terminal to determine the number of players and the location of the pack file. After receiving valid input, the class then loads and validates the pack, initializes the decks and players, deals the cards, and then creates and starts the player threads. The main thread then waits for all player threads to finish - this occurs when one player declares a win, at which point all other threads are instantly stopped.

# Output

Once the threads have terminated, the class writes the final deck outputs to appropriately named files and announces that the game has finished. Other notable methods include the dealCards() method, which ensures fairness through round-robin card distribution, and the writeDeckOutputs() method, which ensures that output files are correctly named and accurately reflect the final state of each deck.

# Deck Class

The third class, Deck, represents an individual deck of cards used within the card game. It functions as a thread safe collection where players draw cards from the top and discard cards to the bottom. Each deck is assigned a unique ID upon creation using a static volatile counter, ensuring that deck identifiers remain distinct even when multiple decks are created concurrently. The class uses a linked list as its internal data structure, stored within a Queue interface, allowing efficient addition and removal operations from either end.

The class constructor assigns each deck a unique identifier and initializes an empty queue of cards. The key methods, such as addCard() and drawCard(), are both synchronized to ensure safe concurrent access when multiple player threads interact with the same deck simultaneously. This design prevents race conditions and ensures that card operations remain consistent.

# Player Class

The final class, Player, represents everyone in the card game. Each player had a dedicated thread, allowing all players to act concurrently and interact with the decks with no issues. A player will draw cards from the designated left deck and discard to their right deck, continuing until a player gets a winning hand and declares a win. Each player has a unique ID, which is managed through a static volatile counter, ensuring distinct threads are initialized at the same time.

The class maintains several methods: the players ID, references to the left and right decks, a list representing the players hand, a random number generator for card selection, and a log file writer for recording game actions. All references are declared as final, ensuring immutability once the player is initialized. Log files are created within an output directory and document the actions taken by each player.

# The Main Method

The run method defines the main gameplay loop, by calling the drawAndDiscard() method until a winner is declared. The drawAndDiscard() method ensures thread safety by locking both decks in a fixed order based on ID to prevent deadlocks. Each turn the player draws a card from the left deck, selects a discard, preferring any card that does not match their ID, then placing it into the deck on their right.

If a player obtains a winning hand, the declareWin() method announces victory, then stopping all other threads via the winnerId variable. Once a winner is declared, all other players detect the update and exit their loops, logging their final hands.
