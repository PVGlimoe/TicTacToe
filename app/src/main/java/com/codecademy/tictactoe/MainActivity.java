package com.codecademy.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void playGame (View view)
    {
        Intent intent = new Intent(this, Game.class);

        EditText p1 = findViewById(R.id.editTextPlayer1);
        String player1 = p1.getText().toString();

        EditText p2 = findViewById(R.id.editTextPlayer2);
        String player2 = p2.getText().toString();

        intent.putExtra("player1", player1);
        intent.putExtra("player2", player2);

        startActivity(intent);
    }
}