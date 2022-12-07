package com.company;


public class Node_Structure {
    String[][] board = new String[3][3];
    String next_player;
    Node_Structure parent;

    int heuristic_function;
    int node_depth;

    public Node_Structure(String[][] board, Node_Structure parent, int heuristic_function, int node_depth, String next_player) {
        this.board = board;
        this.next_player = next_player;
        this.parent = parent;
        this.heuristic_function = heuristic_function;
        this.node_depth = node_depth;
    }

    public Node_Structure() {
    }

    public Node_Structure(String[][] board) {
        this.board = board;
        heuristic_function = 0;
        node_depth = 0;
        next_player = null;
    }

    public String[][] getBoard() {
        return board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }

    public String getNext_player() {
        return next_player;
    }

    public void setNext_player(String next_player) {
        this.next_player = next_player;
    }

    public Node_Structure getParent() {
        return parent;
    }

    public void setParent(Node_Structure parent) {
        this.parent = parent;
    }

    public int getHeuristic_function() {
        return heuristic_function;
    }

    public void setHeuristic_function(int heuristic_function) {
        this.heuristic_function = heuristic_function;
    }

    public int getNode_depth() {
        return node_depth;
    }

    public void setNode_depth(int node_depth) {
        this.node_depth = node_depth;
    }
}
