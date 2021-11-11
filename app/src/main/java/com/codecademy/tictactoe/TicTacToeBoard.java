package com.codecademy.tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TicTacToeBoard {
    ArrayList<String> board;
    public static final String KRYDS = "X";
    public static final String BOLLE = "O";
    private static final int[][] WINNER_COMBINATIONS = {
            {0,1,2}, {3,4,5}, {6,7,8},
            {0,3,6}, {1,4,7}, {2,5,8},
            {0,4,8}, {2,4,6}
    };

    private String playerChar;

    public enum STATE {
        WINNER_X, WINNER_O, RUNNING, TIE;
    }

    public TicTacToeBoard(String playerChar){
        this.playerChar = playerChar;
        this.resetBoard();
    }

    public String getPlayerChar(){
        return playerChar;
    }

    public ArrayList<String> getBoard(){
        return board;
    }

    public void setBoard(ArrayList<String> board){
        this.board = board;
    }

    public boolean insertCharacter(int index) {
        if(board.get(index).equals("")){
            board.set(index, playerChar);
            return true;
        }
        else {
            return false;
        }
    }

    public void resetBoard(){
        board = new ArrayList<>(Collections.nCopies(9, ""));
    }

    public STATE getState() {
        int p1, p2, p3;
        for(int[] combination : WINNER_COMBINATIONS){
            p1 = combination[0];
            p2 = combination[1];
            p3 = combination[2];
            if(board.get(p1).equals(BOLLE) && board.get(p2).equals(BOLLE) && board.get(p3).equals(BOLLE)){
                return STATE.WINNER_O;
            }else if (board.get(p1).equals(KRYDS) && board.get(p2).equals(KRYDS) && board.get(p3).equals(KRYDS)){
                return STATE.WINNER_X;
            }
        }
        for(String character : board){
            if(character.equals("")){
                return STATE.RUNNING;
            }
        }
        return STATE.TIE;
    }
}
