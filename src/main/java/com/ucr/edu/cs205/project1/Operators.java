package com.ucr.edu.cs205.project1;

import java.util.ArrayList;
/**
 * Class containing operators for shifting tiles in the 8 puzzle problem.
 */
public class Operators {
    /**
     * Shifts a tile to the left in the given current node's state.
     *
     * @param currNode The current node.
     * @param position The position of the tile to be shifted.
     * @param boardEdge The edge length of the puzzle board.
     * @return The child node after shifting the tile to the left, or null if not possible.
     */
    public Node shiftTileLeft(Node currNode, int position, int boardEdge) {
        // If the position is on the extreme left, return null
        if (position % boardEdge == 0)
            return null;
        else {
            Node childNode = new Node(new ArrayList<>(currNode.getState()), currNode);
            childNode.swapTiles(position, position - 1);
            return childNode;
        }
    }

    /**
     * Shifts a tile to the right in the given current node's state.
     *
     * @param currNode The current node.
     * @param position The position of the tile to be shifted.
     * @param boardEdge The edge length of the puzzle board.
     * @return The child node after shifting the tile to the right, or null if not possible.
     */
    public Node shiftTileRight(Node currNode, int position, int boardEdge) {

        for (int i = boardEdge - 1; i < boardEdge * boardEdge; i = i + boardEdge) {
            if (position == i)
                return null;
        }

        Node childNode = new Node(new ArrayList<>(currNode.getState()), currNode);
        childNode.swapTiles(position, position + 1);
        return childNode;

    }

    /**
     * Shifts a tile upwards in the given current node's state.
     *
     * @param currNode The current node.
     * @param position The position of the tile to be shifted.
     * @param boardEdge The edge length of the puzzle board.
     * @return The child node after shifting the tile upwards, or null if not possible.
     */
    public Node shiftTileUp(Node currNode, int position, int boardEdge) {
        // If the position is on the top edge, return null
        if (position < boardEdge)
            return null;
        else {
            Node childNode = new Node(new ArrayList<>(currNode.getState()), currNode);
            childNode.swapTiles(position, position - boardEdge);
            return childNode;
        }
    }

    /**
     * Shifts a tile downwards in the given current node's state.
     *
     * @param currNode The current node.
     * @param position The position of the tile to be shifted.
     * @param boardEdge The edge length of the puzzle board.
     * @return The child node after shifting the tile downwards, or null if not possible.
     */
    public Node shiftTileDown(Node currNode, int position, int boardEdge) {
        // If the position is on the bottom edge, return null
        if (position >= boardEdge * (boardEdge - 1))
            return null;
        else {
            Node childNode = new Node(new ArrayList<>(currNode.getState()), currNode);
            childNode.swapTiles(position, position + boardEdge);
            return childNode;
        }
    }
}
