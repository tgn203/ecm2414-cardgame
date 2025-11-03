# Continuous Assessment: Card Game
## ECM2414 Software Development

This project implements a multi-threaded card game in Java. It includes production code and a full suite of JUnit tests verifying the correctness and robustness of the system.

In the Git repository, the production package `cardgame` can be found at `app/src/main/java/cardgame/`. The testing suite package `cardgame_test` can be found at `app/src/main/java/cardgame_test/`.

All builds and tests are managed by **Gradle**. The project targets **Eclipse Adoptium Temurin JDK 25.0.1**.

## Requirements
- **Java**: Eclipse Adoptium Temurin JDK 25.0.1 (or later)
- **Gradle**: 8.x (or later) [optional: makes testing simpler]

<details>
<summary>Using Gradle:</summary>
Gradle is an optional Java project and package manager used to simplify the development and testing process. If Gradle is installed on the system (it is recommended to use the provided devcontainer), then use `gradle` throughout.

Otherwise, use the provided Gradle Wrappers depending on your operating system. Replace all instances of the <code>gradle</code> command throughout as follows:
- Windows: <code>.\gradlew.bat [...]</code>
- Linux: <code>.\gradlew [...]</code>
</details>


## Running the Game
To build and run the game:
<details>
<summary>Using Gradle:</summary>
<pre><code class="language-bash">
git clone https://github.com/tgn203/ecm2414-cardgame.git
cd app
gradle build
java -jar build/libs/app.jar
</code></pre>
</details>

<details>
<summary>Using <code>javac</code> and <code>java</code>:</summary>
<pre><code class="language-bash">
javac cardgame\*.java

java cardgame.CardGame
</code></pre>
</details>

You will be prompted to enter a number of players and provide the path to a valid pack file, *e.g.*:
```bash
Please enter the number of players:
4
Please enter location of pack to load:
example_input.txt
```

Once the game has completed, all outputted log files will be placed in the `out/` directory.

## Running Tests
The project uses **JUnit 4.13.2** for automated unit testing.

<details>
<summary>Using Gradle:</summary>
<pre><code class="language-bash">bash
cd app
gradle cleanTest test
</code></pre>

>*This will make use of the automatically imported JUnit 4.13.2 library.*

Test results will be outputted into the terminal (*i.e.* <code>BUILD SUCCESSFUL in 4s</code>), and a detailed report can be found at `app/build/reports/tests/test/index.html`. Open this file in a web browser for information on which tests passed or failed, and why.
</details>

<details>
<summary>Using <code>java</code> and <code>javac</code>:</summary>
<pre><code class="language-bash">
javac -cp ".;lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar" cardgame\*.java cardgame_test\*.java

java -cp ".;lib\junit-4.13.2.jar;lib\hamcrest-core-1.3.jar" org.junit.runner.JUnitCore cardgame_test.CardTest cardgame_test.DeckTest cardgame_test.PlayerTest cardgame_test.CardGameTest
</code></pre>

>*This will make use of the included standalone distribution of JUnit, found under <code>lib/</code>.*

Test results will be outputted into the terminal (*i.e.* <code>OK (18 tests)</code>).
</details>

## Output Files
After running the game or tests, generated files appear in the following structure:
```bash
out/
 ├─ playerX_output.txt
 ├─ deckX_output.txt
 └─ <temporary test logs>
```

## License
This project was developed as part of the University of Exeter ECM2414: Software Development continuous assessment. This project is for academic submission only, and is not licensed for commercial use.

**Thomas Noakes**

**Maximilien Read**

(c) 2025
