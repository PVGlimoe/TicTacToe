package com.codecademy.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game extends AppCompatActivity {
    private String player1;
    private String player2;
    private PlayerType playerType;
    private TextView activePlayerTextView;
    private PlayerType activePlayer = PlayerType.PLAYER_1;

    private DocumentReference boardRef = FirebaseFirestore.getInstance().document("tictactoe/board");
    private DocumentReference playerRef = FirebaseFirestore.getInstance().document("tictactoe/players");
    private TicTacToeBoard game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // I Firestore har vi oprettet et document som hedder board, board ref er en reference til dette dokument.
        // Nå vi kalder addSnapshotListener på boardRed subscriber vi på de ændringer som sker i dokumentet
        // altså hver gang der sker en ændring bliver den lambda funktion, som vi giver som argument, kaldt.
        boardRef.addSnapshotListener((value, error) ->  {
            // Dette er inden i lambda funktionen, den bliver kaldt hver gang en spiller er færdig med sin tur.
            // Det er også vigtigt at have med at den bliver kald både på telefonen for den spiller som foretager ændringen og
            // på den anden spillers telefon.
            // 1. vi tjekker om value eksistere, om vi rent faktisk får noget data tilsendt, eller om der er sket en felj.
            if (value.exists()) {
                // 2. Vi trækker 'board' ud af det data(value) som er blevet sendt, hvilket bare en en list af strings.
                ArrayList<String> board = (ArrayList<String>) value.get("board");
                // 3. vi sige til vores game-objekt(Den som holder styr på al tictactoe logikken) at den skal opdatere sig board.
                game.setBoard(board);
                // 4. Tegn boardet, da der er sket en ændring i databasen skal det også vises i frontenden.
                drawBoard();
                // 5. Skift hvilken spillers tur det nu er
                togglePlayerTurn();
                // 6. Tjek om vi har en vinder
                checkForWin();
            }else if (error != null) {
                System.out.println("Hentede data virkede ikke");
            }
        });

        Intent intent = getIntent();
        player1 = intent.getStringExtra("player1");
        player2 = intent.getStringExtra("player2");
        playerType = (PlayerType) intent.getSerializableExtra("playerType");

        activePlayerTextView = findViewById(R.id.textViewActivePlayer);
        togglePlayerTurn();

        game = new TicTacToeBoard(playerType == PlayerType.PLAYER_1 ? TicTacToeBoard.KRYDS : TicTacToeBoard.BOLLE);
        setActivePlayerTextView();
    }

    public void selectTile(View v){
        if(activePlayer != playerType){
            return;
        }
        int buttonId = v.getId();
        for (int i = 1; i < 10; i++) {
            int id = getResources().getIdentifier("buttonPos"+i, "id", getPackageName());
            if (buttonId == id){
                boolean insertSuccess = game.insertCharacter(i - 1);
                if(!insertSuccess){
                    return;
                }
                Button selectedTile = findViewById(id);

                selectedTile.setText("" + game.getPlayerChar());

                break;
            }
        }

        Map<String, Object> boardData = new HashMap<>();
        boardData.put("board", game.getBoard());

        boardRef.update(boardData)
            .addOnSuccessListener(e -> {
                System.out.println("Player posten virkede");
            })
            .addOnFailureListener(e -> {
                    System.out.println("Player posten virker ikke");
                }
            );
        checkForWin();
    }

    public void drawBoard(){
        ArrayList<String> board = game.getBoard();
        for (int i = 1; i < 10; i++) {
            int id = getResources().getIdentifier("buttonPos"+i, "id", getPackageName());
            Button tile = findViewById(id);
            tile.setText(board.get(i-1));
        }
    }

    public void togglePlayerTurn(){
        activePlayer = activePlayer == PlayerType.PLAYER_1 ? PlayerType.PLAYER_2 : PlayerType.PLAYER_1;
        setActivePlayerTextView();
    }

    public void setActivePlayerTextView(){
        if(activePlayer == playerType){
            activePlayerTextView.setText("Your Turn");
        }
        else {
            String otherPlayerName = playerType == PlayerType.PLAYER_1 ? player2 : player1;
            activePlayerTextView.setText(otherPlayerName + "'s Turn");
        }
    }

    public void checkForWin(){
        switch (game.getState()){
            case RUNNING:
                break;
            case WINNER_O:
                showWinner(PlayerType.PLAYER_2);
                break;
            case WINNER_X:
                showWinner(PlayerType.PLAYER_1);
                break;
            case TIE:
                showTie();
                break;
        }
    }

    public void showWinner(PlayerType winner){
        TextView winnerText = findViewById(R.id.textViewWinner);
        if (winner == PlayerType.PLAYER_1){
            winnerText.setText(player1 + " WINS!");
        } else {
            winnerText.setText(player2 + " WINS!");
        }
        disableButtons();

    }

    public void showTie(){
        TextView winnerText = findViewById(R.id.textViewWinner);
        winnerText.setText("It's a Tie!");
        disableButtons();
    }

    public void resetDatabase(){
        Map<String, Object> boardData = new HashMap<>();
        game.resetBoard();

        boardData.put("board", game.getBoard());

        boardRef.update(boardData)
            .addOnSuccessListener(e -> {
                System.out.println("Player posten virkede");
            })
            .addOnFailureListener(e -> {
                System.out.println("Player posten virker ikke");
            }
        );

        Map<String, Object> playerData = new HashMap<>();
        playerData.put("player1", "");
        playerData.put("player2", "");
        playerData.put("ready", false);

        playerRef.update(playerData)
            .addOnSuccessListener(e -> {
                System.out.println("Player posten virkede");
            })
            .addOnFailureListener(e -> {
                System.out.println("Player posten virker ikke");
            }
        );
    }

    public void disableButtons(){
        for (int i = 1; i < 10; i++) {
            int id = getResources().getIdentifier("buttonPos" + i, "id", getPackageName());
            Button button = findViewById(id);
            button.setEnabled(false);
        }
        findViewById(R.id.buttonPlayAgain).setVisibility(View.VISIBLE);
    }

    public void playAgain(View view){
        resetDatabase();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}