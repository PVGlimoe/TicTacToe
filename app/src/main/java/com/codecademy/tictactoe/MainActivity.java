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
            // Denne lambda funktion bliver kald hver gang der sker ændringer i playerdokumentet i firestore.
            if (value.exists()) {
                player1 = value.getString("player1");
                player2 = value.getString("player2");
                ready = value.getBoolean("ready");
                if(playerType == null){
                    if(player1.equals("")){
                        playerType = PlayerType.PLAYER_1;
                    }
                    else {
                        playerType = PlayerType.PLAYER_2;
                    }
                }
                // Når player2 indsætter sig navn i databasen ændres værdien af ready til true,
                // hvilket betyder at spillet går i gang.
                if(ready){
                    startGame();
                }

            }else if (error != null) {
                System.out.println("Hentede data virkede ikke");
            }
        });

    }

    public void startGame(){
        // Vi laver en ny intent
        Intent intent = new Intent(this, Game.class);
        // Vi sender player1 og player2 med i intenten, de indeholder bare deres navne.
        intent.putExtra("player1", player1);
        intent.putExtra("player2", player2);
        // Derudover sender vi også player typen med, den kan have to værdier PLAYER_1, eller PLAYER_2
        // Dette bruges til at holde styr på hvis tur det er.
        intent.putExtra("playerType", playerType);

        startActivity(intent);
    }




    // Bliver kaldt nå man trykker på knappen 'Play'
    public void playGame (View view){
        // Da firestore er strukturer med key:value pairs, sender man data til den i form af hashmaps
        Map<String, Object> playerData = new HashMap<>();
        EditText playerNameEditText = findViewById(R.id.editTextPlayer);
        // vi trækker spillerens navn ud af input feltet
        Object playerName = playerNameEditText.getText().toString();
        // Hvis vi er den første spiller som logger på sætter vi player1 key'en til vore navn i hashmappet
        if(playerType == PlayerType.PLAYER_1){
           playerData.put("player1", playerName);
       }
        // Ellers sætter vi den til player2, og tilføjer ready key'en og sætter den til true,
        // for at meddele at spillet kan gå i gang
       else {
           playerData.put("player2", playerName);
           playerData.put("ready", true);
       }

       // til sidst sender vi hashmappet til databasen.
        playerRef.update(playerData).addOnSuccessListener(e -> {
            System.out.println("Player posten virkede");
        }).addOnFailureListener( e -> {
            System.out.println("Player posten virker ikke");
        });
    }
}