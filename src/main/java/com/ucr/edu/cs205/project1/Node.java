package com.ucr.edu.cs205.project1;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node in the search tree for the 8 puzzle problem.
 */
public class Node {
    private final List<Integer> state;
    private final Node parentNode;
    private final int depth;

    /**
     * Constructs a new Node with the given state and parent node.
     *
     * @param state The state of the puzzle board.
     * @param parentNode The parent node of the current node.
     */
    public Node(List<Integer> state, Node parentNode) {
        this.state = new ArrayList<>(state);
        if (parentNode == null) {
            this.parentNode = null;
            this.depth = 0;
        } else {
            this.parentNode = parentNode;
            this.depth = this.parentNode.depth + 1;
        }
    }

    /**
     * Returns the parent node of the current node.
     *
     * @return The parent node.
     */
    public Node getParentNode() {
        return parentNode;
    }

    /**
     * Returns the depth of the current node in the search tree.
     *
     * @return The depth of the node.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Returns the state of the puzzle board associated with the node.
     *
     * @return The state of the puzzle board.
     */
    public List<Integer> getState() {
        return this.state;
    }

    /**
     * Swaps the tiles at the given positions in the puzzle board state.
     *
     * @param pos1 The position of the first tile.
     * @param pos2 The position of the second tile.
     */
    public void swapTiles(int pos1, int pos2) {
        int temp = this.state.get(pos1);
        this.state.set(pos1, this.state.get(pos2));
        this.state.set(pos2, temp);
    }

}
