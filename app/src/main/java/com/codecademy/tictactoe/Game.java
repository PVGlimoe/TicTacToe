package com.codecademy.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Game extends AppCompatActivity {

    String player1;
    String player2;
    TextView activePlayer;

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

        //oppe Hen
        if (positionValues.get(0).equals(positionValues.get(1)) && positionValues.get(0).equals(positionValues.get(2)) && !positionValues.get(0).isEmpty()){
            showWinner(positionValues.get(0));

        }

        //midt Hen
        if (positionValues.get(3).equals(positionValues.get(4)) && positionValues.get(3).equals(positionValues.get(5)) && !positionValues.get(3).isEmpty()){
            showWinner(positionValues.get(3));
        }

        //nede Hen
        if (positionValues.get(6).equals(positionValues.get(7)) && positionValues.get(6).equals(positionValues.get(8)) && !positionValues.get(6).isEmpty()){
            showWinner(positionValues.get(6));
        }

        //Venstre ned
        if (positionValues.get(0).equals(positionValues.get(3)) && positionValues.get(0).equals(positionValues.get(6)) && !positionValues.get(0).isEmpty()){
            showWinner(positionValues.get(0));
        }

        //Midt ned
        if (positionValues.get(1).equals(positionValues.get(4)) && positionValues.get(1).equals(positionValues.get(7)) && !positionValues.get(1).isEmpty()){
            showWinner(positionValues.get(1));
        }

        //Højre ned
        if (positionValues.get(2).equals(positionValues.get(5)) && positionValues.get(2).equals(positionValues.get(8)) && !positionValues.get(2).isEmpty()){
            showWinner(positionValues.get(2));
        }

        //Skrå ned
        if (positionValues.get(0).equals(positionValues.get(4)) && positionValues.get(0).equals(positionValues.get(8)) && !positionValues.get(0).isEmpty()){
            showWinner(positionValues.get(0));
        }

        //skrå op
        if (positionValues.get(2).equals(positionValues.get(4)) && positionValues.get(2).equals(positionValues.get(6)) && !positionValues.get(2).isEmpty()){
            showWinner(positionValues.get(2));
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