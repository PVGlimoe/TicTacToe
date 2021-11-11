package com.codecademy.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game extends AppCompatActivity {

    String player1;
    String player2;
    TextView activePlayer;

    private DocumentReference boardRef = FirebaseFirestore.getInstance().document("tictactoe/board");
    private DocumentReference playerRef = FirebaseFirestore.getInstance().document("tictactoe/players");
    private TicTacToeBoard board = new TicTacToeBoard();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        player1 = intent.getStringExtra("player1");
        player2 = intent.getStringExtra("player2");

        activePlayer = findViewById(R.id.textViewActivePlayer);
        activePlayer.setText(player1 + "'s Turn");




    }

    public void selectTile(View v){


        playerRef.update(playerData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("Player posten virkede");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Player posten virker ikke");
            }
        });

        int buttonId = v.getId();
        for (int i = 1; i < 10; i++) {
            int id = getResources().getIdentifier("buttonPos"+i, "id", getPackageName());
            if (buttonId == id){
                Button selectedTile = findViewById(id);
                if (!selectedTile.getText().toString().equals("")){
                    break;
                }
                String activePlayerSting = activePlayer.getText().toString();
                if (activePlayerSting.equals(player1 + "'s Turn")){
                    selectedTile.setText("X");
                    activePlayer.setText(player2 + "'s Turn");
                } else {
                    selectedTile.setText("O");
                    activePlayer.setText(player1 + "'s Turn");
                }
                break;

            }

        }
        checkForWin();
    }

    public void checkForWin(){

        TextView winnerText = findViewById(R.id.textViewWinner);
        List<String> positionValues = new ArrayList<>();

        for (int i = 1; i <10 ; i++) {
            int id = getResources().getIdentifier("buttonPos"+i, "id", getPackageName());
            Button button = findViewById(id);
            String pos = button.getText().toString();

            positionValues.add(pos);

        }

    }

    public void showWinner(String symbol){
        TextView winnerText = findViewById(R.id.textViewWinner);
        if (symbol.equals("X")){
            winnerText.setText(player1 + " WINS!");
        } else {
            winnerText.setText(player2 + " WINS!");
        }
        disableButtons();
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}