package com.company;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

public class TicTacToe {

    Vector<Node_Structure> possibleNextMoveNode = new Vector<>();

    public void gameStarts() {
        Node_Structure root_node = new Node_Structure();
        this.output_board(root_node.getBoard());
        int player = this.get_player();

        if (player == 1) {
            root_node.setNext_player("O");
            this.opponent_move(root_node);
        } else {
            root_node.setNext_player("X");
            this.program_move(root_node);
        }
    }

    private void program_move(Node_Structure current_node) {
        Node_Structure new_node = new Node_Structure();
        new_node.setBoard(current_node.getBoard());
        new_node.next_player = "X";

        new_node = this.nextStep(new_node);
        this.output_board(new_node.getBoard());

        if (this.Win(new_node) == true) System.out.println("Computer won! ");
        else if (this.isLeafNode(new_node) == true) System.out.println("The game is draw");
        else this.opponent_move(new_node);

    }

    private Node_Structure nextStep(Node_Structure new_node) {
        this.MinMaxAlphaBetaPruning(new_node, this.initializeAlpha(new_node), this.initializeBeta(new_node));
        Node_Structure another_node = this.MaxNodeInList(possibleNextMoveNode);
        possibleNextMoveNode.removeAllElements();
        return another_node;
    }

    private Node_Structure MaxNodeInList(Vector<Node_Structure> possibleNextMoveNode) {
        Node_Structure max_node = possibleNextMoveNode.get(0);
        int list_size = possibleNextMoveNode.size();
        for (int i = 0; i < list_size; i++)
            if (max_node.getHeuristic_function() < possibleNextMoveNode.get(i).getHeuristic_function())
                max_node = possibleNextMoveNode.get(i);
        return max_node;
    }

    //Main function where algorithm is implemented
    public int MinMaxAlphaBetaPruning(Node_Structure current_node, int Alpha, int Beta) {
        int current_node_alpha = Alpha;
        int current_node_beta = Beta;

        if (this.isLeafNode(current_node) == true) return this.MinMaxLeafNode(current_node);
        else if (current_node.getNext_player() == "O")
            return this.MiniMaxAlpha_MaxNode(current_node, current_node_alpha, current_node_beta);
        else return this.MiniMaxBeta_MinNode(current_node, current_node_alpha, current_node_beta);
    }

    public int MiniMaxBeta_MinNode(Node_Structure current_node, int current_node_alpha, int current_node_beta) {
        Vector<Node_Structure> all_successors = this.getAllSuccessors(current_node);
        for (int i = 0; i < all_successors.size(); i++) {
            Node_Structure successor = all_successors.get(i);
            int current_max = this.MinMaxAlphaBetaPruning(successor, current_node_alpha, current_node_beta);
            current_node_alpha = this.MaxTwoIntegers(current_node_alpha, current_max);
            current_node.setHeuristic_function(this.MaxTwoIntegers(current_node.getHeuristic_function(), current_node_alpha));
            if (current_node_alpha >= current_node_beta) break;
        }
        if (this.possibleNextMove(current_node) != null) possibleNextMoveNode.add(current_node);
        return current_node_alpha;
    }

    public int MiniMaxAlpha_MaxNode(Node_Structure current_node, int current_node_alpha, int current_node_beta) {
        Vector<Node_Structure> all_successors = this.getAllSuccessors(current_node);
        for (int i = 0; i < all_successors.size(); i++) {
            Node_Structure successor = all_successors.get(i);
            int current_min = this.MinMaxAlphaBetaPruning(successor, current_node_alpha, current_node_beta);
            current_node_beta = this.MinTwoIntegers(current_node_beta, current_min);
            current_node.setHeuristic_function(this.MinTwoIntegers(current_node.getHeuristic_function(), current_node_beta));
            if (current_node_alpha >= current_node_beta) break;
        }
        if (this.possibleNextMove(current_node) != null) possibleNextMoveNode.add(current_node);
        return current_node_beta;
    }

    public int MinTwoIntegers(int number, int number01) {
        if (number > number01) return number01;
        else return number;
    }

    public int MaxTwoIntegers(int number, int number01) {
        if (number < number01) return number01;
        else return number;
    }

    public Vector<Node_Structure> getAllSuccessors(Node_Structure current_node) {
        Vector<Node_Structure> all_successors = new Vector<>();
        ArrayList<int[]> allEmptySquares = this.getAllEmptySquares(current_node);
        int numberOfEmptySquares = allEmptySquares.size();
        for (int i = 0; i < numberOfEmptySquares; i++) {
            all_successors.add(this.getSuccesor(current_node, allEmptySquares.get(i)));
        }
        return all_successors;
    }

    public ArrayList<int[]> getAllEmptySquares(Node_Structure current_node) {
        int board_size = current_node.getBoard().length;
        ArrayList<int[]> list = new ArrayList<>();
        for (int r = 0; r < board_size; r++) {
            for (int c = 0; c < board_size; c++) {
                if (current_node.board[r][c] == null) list.add(this.addValueToArray(r, c));
            }
        }
        return list;
    }

    public int MinMaxLeafNode(Node_Structure current_node) {
        if (this.possibleNextMove(current_node) != null) possibleNextMoveNode.add(current_node);
        return this.evaluateHeuristicValue(current_node);
    }

    public Node_Structure possibleNextMove(Node_Structure current_node) {
        if (current_node.getNode_depth() == 1) return current_node;
        else return null;
    }

    public int initializeBeta(Node_Structure new_node) {
        if (this.isLeafNode(new_node) == true) return this.evaluateHeuristicValue(new_node);
        else return 1000;
    }

    public int initializeAlpha(Node_Structure new_node) {
        if (this.isLeafNode(new_node) == true) return this.evaluateHeuristicValue(new_node);
        else return -1000;
    }

    public void opponent_move(Node_Structure current_node) {
        Node_Structure new_node;
        int[] opponent_input = this.getInputFromOpponentMove(current_node);
        new_node = this.getSuccesor(current_node, opponent_input);
        this.output_board(new_node.getBoard());
        if (this.Win(new_node) == true) System.out.println(" You won! Did not expect this!");
        else if (this.isLeafNode(new_node) == true) System.out.println(" It is draw.");

        else this.program_move(new_node);
    }

    public Node_Structure getSuccesor(Node_Structure current_node, int[] opponent_input) {
        if (this.isLeafNode(current_node) == true) return null;
        else {
            if (current_node.getNext_player() == "X")
                return new Node_Structure(this.updateBoard(current_node, opponent_input), current_node, this.evaluateHeuristicValue(current_node), current_node.getNode_depth() + 1, "O");
            else
                return new Node_Structure(this.updateBoard(current_node, opponent_input), current_node, this.evaluateHeuristicValue(current_node), current_node.getNode_depth() + 1, "X");
        }
    }

    public int evaluateHeuristicValue(Node_Structure current_node) {
        if (current_node.getNext_player() == "X" && this.Win(current_node) == true) return -1;
        if (current_node.getNext_player() == "O" && this.Win(current_node) == true) return 1;
        return 0;
    }

    public String[][] updateBoard(Node_Structure current_node, int[] opponent_input) {
        String[][] new_board = this.copyBoard(current_node.getBoard());
        new_board[opponent_input[0]][opponent_input[1]] = current_node.getNext_player();
        return new_board;
    }

    public String[][] copyBoard(String[][] board) {
        int board_size = board.length;
        String[][] new_board = new String[board_size][board_size];
        for (int r = 0; r < board_size; r++) {    // r = row
            for (int c = 0; c < board_size; c++)  // c = column
                new_board[r][c] = board[r][c];
        }
        return new_board;
    }

    public boolean isLeafNode(Node_Structure current_node) {
        return this.Win(current_node) || (this.EmptySquareOnBoard(current_node) == null);
    }


    //Checking if final state (win) has reached
    public boolean Win(Node_Structure current_node) {
        return (this.winOnRow(current_node)
                || this.winOnColumn(current_node)
                || this.winOnDiagonal(current_node));
    }

    public boolean winOnDiagonal(Node_Structure current_node) {
        String[][] board = current_node.getBoard();
        if (board[1][1] != null) {
            if (board[0][0] != null && board[2][2] != null) {
                String[][] board01 = current_node.getBoard();
                return (board01[1][1].contentEquals(board01[0][0]) && board01[1][1].contentEquals(board01[2][2]));
            } else if (board[0][2] != null && board[2][0] != null) {
                String[][] board02 = current_node.getBoard();
                return (board02[1][1].contentEquals(board02[0][2]) && board02[1][1].contentEquals(board02[2][0]));
            } else return false;
        } else return false;
    }

    public boolean winOnColumn(Node_Structure current_node) {
        for (int c = 0; c < current_node.getBoard().length; c++) {
            int times_repeated = 0;
            String scan_for_element = current_node.getBoard()[0][c];
            if (scan_for_element == null) break;
            for (int r = 1; r < current_node.getBoard().length; r++) {
                String next_string = current_node.board[r][c];
                if (next_string == null) break;
                else if (scan_for_element.contentEquals(next_string) == false) break;
                else times_repeated++;
            }
            if (times_repeated == 2) return true;
        }
        return false;
    }

    public boolean winOnRow(Node_Structure current_node) {
        for (int r = 0; r < current_node.getBoard().length; r++) {
            int times_of_node_repeated = 0;
            String scan_for_element = current_node.getBoard()[r][0];
            if (scan_for_element == null) break;
            for (int c = 1; c < current_node.getBoard().length; c++) {
                String next_string = current_node.getBoard()[r][c];
                if (next_string == null) break;
                else if (scan_for_element.contentEquals(next_string) == false) break;
                else times_of_node_repeated++;
            }
            if (times_of_node_repeated == 2) return true;
        }
        return false;
    }

    private int[] EmptySquareOnBoard(Node_Structure current_node) {
        int boardSize = current_node.getBoard().length;
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (current_node.board[r][c] == null) return this.addValueToArray(r, c);
            }
        }
        return null;
    }

    private int[] addValueToArray(int r, int c) {
        int[] array = new int[2];
        array[0] = r;
        array[1] = c;
        return array;
    }


    private int[] getInputFromOpponentMove(Node_Structure current_node) {
        int[] opponent_input = this.readOpponentInput();

        while (opponent_input[0] >= 3 || opponent_input[0] < 0
                || opponent_input[1] >= 3 || opponent_input[1] < 0
                || this.emptySquareOpponentInput(current_node, opponent_input) == false) {
            System.out.println("Incorrect move");
            opponent_input = this.readOpponentInput();
        }
        return opponent_input;
    }

    private boolean emptySquareOpponentInput(Node_Structure current_node, int[] opponent_input) {
        String[][] Board = current_node.getBoard();
        return Board[opponent_input[0]][opponent_input[1]] == null;
    }

    private int[] readOpponentInput() {
        int[] opponent_input = new int[2];
        Scanner r = new Scanner(System.in);
        Scanner c = new Scanner(System.in);

        System.out.print("Row you want to check: ");
        opponent_input[0] = r.nextInt();
        System.out.print("Column you want to check: ");
        opponent_input[1] = c.nextInt();

        opponent_input[0]--;
        opponent_input[1]--;

        return opponent_input;
    }


    private int get_player() {
        Scanner p = new Scanner(System.in);
        System.out.println("Play First? Yes (1) or No (0) : ");
        return p.nextInt(); // passes int value, if not it throws error
    }

    public void output_board(String[][] board) {
        int board_size = board.length;

        System.out.println(" Current standing: ");
        System.out.println();
        for (int r = 0; r < board_size; r++) {         //r stands for row
            System.out.print("|");

            for (int c = 0; c < board_size; c++) {   // c stands for column
                if (board[r][c] == null) System.out.print("  " + " |");
                else System.out.print(" " + board[r][c] + " |");
            }

            System.out.println();
            System.out.println("_____________");
            System.out.println();
        }
    }
}
