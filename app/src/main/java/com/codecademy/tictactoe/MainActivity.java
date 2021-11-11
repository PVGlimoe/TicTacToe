package com.codecademy.tictactoe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private DocumentReference playerRef = FirebaseFirestore.getInstance().document("tictactoe/players");

    private String player1;
    private String player2;
    private boolean ready;
    private PlayerType playerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerRef.addSnapshotListener((value, error) ->  {
            if (value.exists()) {
                player1 = value.getString("player1");
                player2 = value.getString("player2");
                ready = value.getBoolean("ready");
                if(playerType == null){
                    if(player1.length() == 0){
                        playerType = PlayerType.PLAYER_1;
                    }
                    else {
                        playerType = PlayerType.PLAYER_2;
                    }
                }
                if(ready){
                    startGame();
                }

            }else if (error != null) {
                System.out.println("Hentede data virkede ikke");
            }
        });

    }

    public void startGame(){
        Intent intent = new Intent(this, Game.class);
        intent.putExtra("player1", player1);
        intent.putExtra("player2", player2);
        intent.putExtra("playerType", playerType);

        startActivity(intent);
    }




    public void playGame (View view){
        Map<String, Object> playerData = new HashMap<>();
        EditText playerNameEditText = findViewById(R.id.editTextPlayer);
        Object playerName = playerNameEditText.getText().toString();
        if(playerType == PlayerType.PLAYER_1){
           playerData.put("player1", playerName);
       }
       else {
           playerData.put("player2", playerName);
           playerData.put("ready", true);
       }

        playerRef.update(playerData).addOnSuccessListener(e -> {
            System.out.println("Player posten virkede");
        }).addOnFailureListener( e -> {
            System.out.println("Player posten virker ikke");
        });
    }
}