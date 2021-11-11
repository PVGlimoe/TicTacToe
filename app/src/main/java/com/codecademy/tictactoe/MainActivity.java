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
    private PLAYER_TYPE playerType;
    private enum PLAYER_TYPE {
        PLAYER_1, PLAYER_2;
    }

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
                        playerType = PLAYER_TYPE.PLAYER_1;
                    }
                    else {
                        playerType = PLAYER_TYPE.PLAYER_2;
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

        startActivity(intent);
    }




    public void playGame (View view){
        Map<String, Object> playerData = new HashMap<>();

       if(playerType == PLAYER_TYPE.PLAYER_1){
           EditText p1 = findViewById(R.id.editTextPlayer1);
           Object player1 = p1.getText().toString();

           playerData.put("player1", player1);
       }
       else {

           EditText p2 = findViewById(R.id.editTextPlayer2);
           String player2 = p2.getText().toString();

           playerData.put("player2", player2);
           playerData.put("ready", true);
       }

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





    }
}