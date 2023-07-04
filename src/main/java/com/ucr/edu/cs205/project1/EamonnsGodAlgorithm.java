package com.ucr.edu.cs205.project1;


import java.util.*;
import java.util.stream.Collectors;

/**
 * The main class implementing the 8 puzzle problem solving algorithm.
 */
public class EamonnsGodAlgorithm {

    /**
     * Enumeration representing the queue function for the search algorithm.
     */
    public enum QueueFunction {UCS, AStarMisplaced, AStarManhattan}

    private static int boardEdge = 0;
    private static int boardSize = 9;
    private boolean isBacktrackingEnabled = false;
    private static final List<Integer> goalState = new ArrayList<>();

    /**
     * Class representing the cost of a node in the priority queue.
     */
    private class NodeCost {
        float cost;
        Node node;

        public NodeCost(float cost, Node node) {
            this.cost = cost;
            this.node = node;
        }
    }

    /**
     * Expands the given node by generating its child nodes based on valid moves.
     *
     * @param node The node to expand.
     * @return The list of child nodes.
     */
    public List<Node> expand(Node node) {
        List<Node> children = new ArrayList<>();
        int positionOfEmptyTile = node.getState().indexOf(0);
        Node child;
        Operators op = new Operators();
        child = op.shiftTileLeft(node, positionOfEmptyTile, boardEdge);
        if (child != null) {
            children.add(child);
        }
        child = op.shiftTileRight(node, positionOfEmptyTile, boardEdge);
        if (child != null) {
            children.add(child);
        }
        child = op.shiftTileUp(node, positionOfEmptyTile, boardEdge);
        if (child != null) {
            children.add(child);
        }
        child = op.shiftTileDown(node, positionOfEmptyTile, boardEdge);
        if (child != null) {
            children.add(child);
        }

        return children;
    }
    /**
     * Performs the ultimate search algorithm given by Dr.Eamonn Keogh to solve the 8 puzzle problem.
     *
     * @param initialState  The initial state of the puzzle.
     * @param queueFunction The queue function for the search algorithm.
     */
    public void ultimateSearch(Node initialState, QueueFunction queueFunction) {
        int noOfExpandedNodes = 0;
        int maxQSize = 0;

        int localExpandedNodes=0;
        int localDepth=0;
        Map<Integer, Integer> localExpandedNodesMap = new HashMap<>();
        if (isGoalState(initialState)) {
            System.out.println("The puzzle is already solved");
            return;
        }
        Comparator<NodeCost> comparator = (o1, o2) -> Float.compare(o1.cost, o2.cost);
        PriorityQueue<NodeCost> nodes = new PriorityQueue<>(100, comparator);
        Map<String, Boolean> visited = new HashMap<>();
        nodes.add(new NodeCost(Float.POSITIVE_INFINITY, initialState));
        while (true) {

            if (nodes.isEmpty()) {
                System.out.println("No Solution found after expanding " + noOfExpandedNodes + " with Queue Size " + maxQSize);
                return;
            }
            maxQSize = Math.max(maxQSize, nodes.size());
            NodeCost nodeCost = nodes.poll();
            if(nodeCost.node.getDepth()!=localDepth)
            {
                localExpandedNodesMap.put(localDepth, localExpandedNodesMap.getOrDefault(localDepth,0)+localExpandedNodes);
                localDepth=nodeCost.node.getDepth();
                localExpandedNodes=0;
            }

            if (isBacktrackingEnabled) {
                if (queueFunction.equals(QueueFunction.UCS)) {
                    System.out.println("The best state to expand with a g(n) = " + nodeCost.node.getDepth() + " and h(n) = " + 0 + " is .... ");
                } else
                    System.out.println("The best state to expand with a g(n) = " + nodeCost.node.getDepth() + " and h(n) = " + nodeCost.cost + " is ....");
                System.out.println("Depth: " + nodeCost.node.getDepth() + " Node Cost: " + nodeCost.cost);
                for (int i = 0; i < boardSize; i++) {
                    if (i != 0 && i % (boardEdge) == 0)
                        System.out.println();
                    System.out.print(nodeCost.node.getState().get(i) + " ");
                }
                System.out.println();
            }

            if (isGoalState(nodeCost.node)) {
                System.out.println();
                if(isBacktrackingEnabled)
                    System.out.println("Depth = No. of nodes expanded at given depth\n"+localExpandedNodesMap);
                System.out.println();
                System.out.println("Solution depth was " + nodeCost.node.getDepth() +
                        "\nNumber of nodes expanded: " + noOfExpandedNodes +
                        "\nMax Queue Size: " + maxQSize);
                System.out.println();

                Node test = nodeCost.node;
                while (test != null && isBacktrackingEnabled) {
                    System.out.println("Backtracking solution from Goal State to Initial State, State at Depth- " + test.getDepth());
                    System.out.println("Depth: " + test.getDepth());
                    for (int i = 0; i < boardSize; i++) {
                        if (i != 0 && i % (boardEdge) == 0)
                            System.out.println();
                        System.out.print(test.getState().get(i) + " ");
                    }
                    System.out.println();
                    test = test.getParentNode();
                }
                return;
            }
            visited.put(getListOfNumbersAsString(nodeCost.node.getState()), true);
            for (Node child : expand(nodeCost.node)) {
                if (!visited.containsKey(getListOfNumbersAsString(child.getState()))) {
                    if (queueFunction.equals(QueueFunction.UCS))
                        nodes.add(new NodeCost(child.getDepth(), child));
                    else if (queueFunction.equals(QueueFunction.AStarMisplaced))
                        nodes.add(new NodeCost(child.getDepth() + calculateMisplacedTilesCount(child.getState()), child));
                    else
                        nodes.add(new NodeCost(child.getDepth() + calculateTotalManhattanDistance(child.getState()), child));
                }
            }
            noOfExpandedNodes += 1;
            localExpandedNodes+=1;
        }


    }

    /**
     * Converts a list of integers to a string representation.
     *
     * @param intList The list of integers.
     * @return The string representation.
     */
    public static String getListOfNumbersAsString(List<Integer> intList) {
        if (intList == null || intList.isEmpty())
            return "";
        return intList.stream().map(String::valueOf).collect(Collectors.joining());
    }

    /**
     * Checks if a given number is a perfect square.
     *
     * @param number The number to check.
     * @return True if the number is a perfect square, false otherwise.
     */
    public static boolean checkPerfectSquare(double number) {
        double sqrt = Math.sqrt(number);
        return ((sqrt - Math.floor(sqrt)) == 0);
    }

    /**
     * Calculates the count of misplaced tiles in the puzzle state.
     *
     * @param gameState The state of the puzzle.
     * @return The count of misplaced tiles.
     */
    public int calculateMisplacedTilesCount(List<Integer> gameState) {
        int misplacedCount = 0;
        for (int i = 0; i < boardSize; i++) {
            if (gameState.get(i) == 0) {
                continue;
            }
            if (!gameState.get(i).equals(goalState.get(i))) {
                misplacedCount++;
            }
        }
        return misplacedCount;
    }

    /**
     * Calculates the total Manhattan distance of tiles in the puzzle state.
     *
     * @param gameState The state of the puzzle.
     * @return The total Manhattan distance.
     */
    public int calculateTotalManhattanDistance(List<Integer> gameState) {
        int totalDistance = 0;
        for (int i = 1; i < boardSize; i++) {
            int currentIndex = gameState.indexOf(i);
            int goalIndex = goalState.indexOf(i);

            if (currentIndex == goalIndex) {
                continue;
            }
            int currentRow = (currentIndex / boardEdge) + 1;
            int currentCol = (currentIndex % boardEdge) + 1;
            int goalRow = (goalIndex / boardEdge) + 1;
            int goalCol = (goalIndex % boardEdge) + 1;

            int tileDistance = Math.abs(currentRow - goalRow) + Math.abs(currentCol - goalCol);
            totalDistance += tileDistance;
        }
        return totalDistance;
    }

    /**
     * Checks if the given state is the goal state.
     *
     * @param currState The current state of the puzzle.
     * @return True if the state is the goal state, false otherwise.
     */
    public Boolean isGoalState(Node currState) {
        return goalState.equals(currState.getState());
    }

    /**
     * Generates the goal state of the puzzle.
     */
    public void generateGoalState() {
        for (int i = 1; i < boardSize; i++)
            goalState.add(i);
        goalState.add(0);
    }

    /**
     * The main method/entrypoint to run the 8 puzzle problem solver.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to an 8-Puzzle Solver. Type '1' to use a default puzzle, or '2' to create your own");
        int choice1 = sc.nextInt();
        int choice2;
        int choice3;
        QueueFunction algoChoice = QueueFunction.AStarManhattan;
        List<Integer> inputPuzzle = new ArrayList<>();
        Map<Integer, List<Integer>> eightPuzzle = new HashMap<>();

        eightPuzzle.put(0, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 0)); // 0 - trivial
        eightPuzzle.put(1, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 0, 8)); // 1 - very easy
        eightPuzzle.put(2, Arrays.asList(1, 2, 0, 4, 5, 3, 7, 8, 6)); // 2 - easy
        eightPuzzle.put(3, Arrays.asList(0, 1, 2, 4, 5, 3, 7, 8, 6)); // 3 - doable
        eightPuzzle.put(4, Arrays.asList(8, 7, 1, 6, 0, 2, 5, 4, 3)); // 4 - oh boy
        eightPuzzle.put(5, Arrays.asList(1, 2, 3, 4, 5, 6, 8, 7, 0)); // 5 - impossible

        if (!checkPerfectSquare(boardSize)) {
            System.out.println("Please intialize a board size that is a perfect square");
            return;
        }
        boardEdge = (int) Math.sqrt(boardSize);
        if (choice1 == 2) {
            System.out.println("Enter the size of the puzzle: ");
            boardSize = sc.nextInt();
            if (!checkPerfectSquare(boardSize)) {
                System.out.println("Please intialize a board size that is a perfect square");
                return;
            }
            boardEdge = (int) Math.sqrt(boardSize);
            System.out.println("Enter the puzzle in the order asked");
            for (int i = 0; i < boardEdge; i++) {
                System.out.println("Enter the values for row " + (i + 1));
                for (int j = 0; j < boardEdge; j++) {
                    int x = sc.nextInt();
                    if (inputPuzzle.contains(x)) {
                        System.out.println("Duplicate value entered, Please try again");
                        return;
                    }
                    if (x < 0 || x > boardSize - 1) {
                        System.out.println("Duplicate value entered, Please try again");
                        return;
                    }
                    inputPuzzle.add(x);
                }

            }

        } else {
            System.out.println("Select difficulty for 8 puzzle problem on the scale of 0-5, 0 being trivial and 5 being impossible");
            choice2 = sc.nextInt();
            inputPuzzle = eightPuzzle.get(choice2);
            if (choice2 < 0 || choice2 > 5) {
                System.out.println("Please enter a right choice, try running once again");
                return;
            }
        }

        System.out.println("Select algorithm. (1) for Uniform Cost Search, (2) for A* with Misplaced Tile Heuristic, or (3) for A* with Manhattan Distance Heuristic");
        switch (sc.nextInt()) {
            case 1: {
                algoChoice = QueueFunction.UCS;
                break;
            }

            case 2: {
                algoChoice = QueueFunction.AStarMisplaced;
                break;
            }
        }

        System.out.println("Do you want to see back tracking? (1) Yes or (2) No");
        choice3 = sc.nextInt();
        EamonnsGodAlgorithm search = new EamonnsGodAlgorithm();

        if (choice3 == 1)
            search.isBacktrackingEnabled = true;

        Node testGame = new Node(inputPuzzle, null);
        search.generateGoalState();
        search.ultimateSearch(testGame, algoChoice);
    }
}
