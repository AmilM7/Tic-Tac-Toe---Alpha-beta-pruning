package com.company;

public class Main_Tic_Tac_Toe {

    public static void main(String[] args) {
        try {
            TicTacToe ticTacToe = new TicTacToe();
            ticTacToe.gameStarts();
        } catch (Exception e) {
            System.out.println("Error occured. Program stopped");
            System.out.println("Error: " + e);
        }
    }
}
