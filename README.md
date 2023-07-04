# CS205 Project
8-Puzzle Problem using Uniform Cost Search, A* search (with mutliple heuristics- Misplaced Tiles and Manhattan Distance)
## Prerequisites
- Java 8 or higher
- Apache Maven 3.9.0 or higher (Optional, as I have already bundled the jar. Use it if you want to build the project)

## How to Execute
1. Ensure that the script file has execute permissions. If not, run the following command:
   ```
   chmod +x script.sh
   ```
2. Run the shell script using the following command:
   ```
   sh ./start.sh
   ```

## Description
The shell script is designed to build and execute the CS205 project. It checks if the `CS205Project-1.0-SNAPSHOT.jar` file exists. If not, it uses Maven to build the project by running `mvn clean install`. After a delay of 5 seconds, it clears the terminal screen and executes the project by running `java -jar CS205Project-1.0-SNAPSHOT.jar`.

Please make sure that you have Java 8 or higher installed on your system and Apache Maven 3.9.0 or higher installed for the successful execution of the script.