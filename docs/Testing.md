# Testing Overview

The testing for this project was done with JUnit 4, specifically version junit:4.13.2. This was selected for its lightweight imports and ease of use with the project manager Gradle. We used Gradle to simplify installation of JUnit on a Linux virtual development environment, and it also makes running tests easier. The project is organised into two separate directories, each in a different Java package. The production code is in app/src/main/java/cardgame, and the testing scripts are in app/src/main/java/cardgame_test. This separation made it simpler to manage importing the classes of the production code for testing.

Each test class mirrors the structure of the corresponding production code class. This means the test scripts are easy to navigate, and tracing logic between implementation and validation is simpler. Each individual method of each class is tested, along with some additional functionality that covers more cases. The testing approach follows a bottom-up strategy, focusing first on validating the correctness of low-level components before moving on to their integration. For example, since the Card class was programmed before the Deck class, and the Deck class implementation relies on the Card class, the testing script for the Card class was developed and is run before that of the Deck class.

Tests can be run using the Gradle build tool, via gradle test. The outcome of all tests will determine whether the testing build is success, as printed on screen. In addition, a breakdown of which tests may have passed or failed, broken down by class, can be found at app/build/reports/tests/test/index.html.

# Testing Strategy

We used unit testing for the smaller, deterministic classes (Card and Deck). These tests verify basic data integrity that is most commonly encountered when developing further additions to the program. The tests ensure the expected input types are valid and that the resulting objects can be used as part of the implementation.

The Player and CardGame classes are more complicated as they are built more around scripting. This means we used integration testing strategies to confirm validity, as these classes involve concurrent behaviours and user-dependent I/O. The main aim was to ensure that the threading aspect functioned as intended, and that the input of game configuration files and the output of logs are handled safely.

Finally, we used validation testing to ensure that all user inputs are handled correctly. This includes negative card values, unwritable log directories and more. This ensures that the program is safe and robust to use when given to a real user, who may potentially have differing system setups to the ones that the tests were written and run on.

Testing was primarily designed around black-box principles: we chose to verify observable behaviours without relying on the actual implementation whenever possible. This means the tests were written with the expected result or outcome in mind, and the methods made available by the class were used to reach that point.

However, due to the private functions found in the CardGameTest class, some white-box reflection tests were used. We created some helper functions to correctly invoke the loadPack and initDecks methods, for example, as these were not accessible through public interfaces but were crucial to verify the program runs correctly in isolation. This is justified as the testing utility functions (for example callPrivate) allow systemic validation without having to modify the existing production code.

# Test Design by Class

## Card

Tests for the Card class verify correct creation, immutability, and input validation.

- testValidCardCreation confirms that a card can be created with a non-negative integer value, and return that same value via getValue().
- testInvalidCardCreation ensures that the constructor method throws an IllegalArgumentException for negative values.
- testToString validates that toString() correctly reflects the card's numerical value.

These tests are deterministic and complete as the class does not contain any side effects or external dependencies.

## Deck

Tests for the Deck class focus on collection integrity and synchronisation.

- testAddAndDrawCard ensures new decks can be created, and that existing cards can be both added to and drawn from the deck.
- testFirstInFirstOut validates the first in, first out (FIFO) queue structure of the deck.
- testContentsAsString ensures the deck can produce a human-readable report of what cards it contains, which is useful for creating reports at the end of each game.
- testThreadSafety ensures no problems can occur when two or more threads are accessing the list of cards at the same time. This eliminates race conditions or data corruption from multiple processes concurrently changing data.

## Player

Tests for the Player class verify that each player can correctly interact with the decks and cards made available to it in a concurrent manner.

- testAddingToHand ensures that the player can interact with the deck interfaces it is provided with, even though it is synchronised with other players' processes.
- testHasWinningHand validates that a player can recognise when it has a hand that wins the game, and it can inform other players about this.

## CardGame

Tests for the CardGame class validate higher-level system behaviour whilst still isolating dependencies.

- testValidPackFile ensures that the system can correctly load a valid pack file, as generated by the makeTempValidPack() helper function.
- testLoadPackInvalidCount validates that the system will reject pack files with an invalid number of card values, i.e. eight times the number of players. This invalid pack file is artificially generated within the function.
- testEmptyLineInPack validates that the system will reject pack files with an empty line instead of a card. This invalid pack file is artificially generated within the function.
- testInitDecks and testInitPlayers verify that the correct number of objects are created for a given number of players for decks and players respectively.
- testDealCards ensures that each player gets four cards at the beginning of the game.
- testWriteDeckOutputs ensures that, at the end of the game, each deck creates a log file at the output directory, with the correct naming and content format.

In addition, there is a setup function to create a common CardGame object that is called before each test with the @Before annotation, and a tearDown function to delete temporary output files called after all tests with @After annotation. Testing these private, helper functions using reflection shows that it is possible to verify deterministic correctness without having to simulate the entire game or use user input.

# Testing Outcomes

Using Gradle's default test runner, all tests passed successfully. Functional cover includes all constructors and public methods, all expected exceptions and errors, file generation and clean-up logic for output files across all classes. Branch coverage was not measured automatically; however, all control flow paths (e.g. valid and invalid input branches) have at least one corresponding test.

Concurrency behaviour was tested manually via the DeckTest class and manually in the code review. Final deck states were observed when using multiple different runs across test cases and production runs. When production code was tested manually, multiple runs were executed with the same input to account for random behaviour in the discarding functionality. No race conditions or deadlocks were observed.

# Reflection and Future Improvements

The most difficult class to test was the CardGame class, due to its large number of methods marked as private. It also depended most on I/O and user terminal input to successfully execute. To mitigate this, helper functions were used in the start() function rather than relying solely on one large script. In a larger project, it may be helpful to instead set up an interface to artificially inject the data from these operations to automate the testing process fully.

File output verification is currently only based on the existence of the output files and whether the content matches an expected pattern. Instead, mock file systems could be created to further test the output capabilities, and output from various files could be checked against one another.

Concurrency should also be tested more rigorously using stress tests that spawn large numbers of simulated players. This will further verify thread-safe synchronised deck access. However, this may prove more difficult on underpowered hardware.

Overall, the testing design ensures we can be confident in the correctness and robustness of the system. We have complete coverage of both valid and invalid behaviours across all classes, and clear traceability between production classes and their associated test suites. The complete test suite provides strong confidence that the application behaves as intended across a range of normal and erroneous scenarios.
