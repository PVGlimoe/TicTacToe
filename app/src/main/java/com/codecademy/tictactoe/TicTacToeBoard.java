package com.codecademy.tictactoe;

public class TicTacToeBoard {

    char[] board = {
            ' ', ' ', ' ',
            ' ', ' ', ' ',
            ' ', ' ', ' '
    };
    private static final char KRYDS = 'X';
    private static final char BOLLE = 'O';
    private static final int[][] WINNER_COMBINATIONS = {
            {0,1,2}, {3,4,5}, {6,7,8},
            {0,3,6}, {1,4,7}, {2,5,8},
            {0,4,8}, {2,4,6}
    };

    public enum STATE {
        WINNER_X, WINNER_O, RUNNING, TIE;
    }

    public boolean insertCharacter(int index,char character) {
            if(board[index] == ' '){
                board[index] = character;
                return true;
            }else {
                return false;
            }
    }

    public STATE checkState() {
        int p1, p2, p3;
        for(int[] combination : WINNER_COMBINATIONS){
            p1 = combination[0];
            p2 = combination[1];
            p3 = combination[2];
            if(board[p1] == BOLLE && board[p2] == BOLLE && board[p3] == BOLLE){
                return STATE.WINNER_O;
            }else if (board[p1] == KRYDS && board[p2] == KRYDS && board[p3] == KRYDS){
                return STATE.WINNER_X;
            }
        }
        for(char character : board){
            if(character == ' '){
                return STATE.RUNNING;
            }
        }

        return STATE.TIE;
    }

}
