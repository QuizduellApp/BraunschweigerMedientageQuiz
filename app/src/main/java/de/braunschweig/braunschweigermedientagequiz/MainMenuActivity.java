package de.braunschweig.braunschweigermedientagequiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Hauptbildschirm nach dem Login
 */
public class MainMenuActivity extends Activity {
    MainActivity main = new MainActivity();
    SpielData spielData;
    private static final String TAG_SPIEL_DATA = "spielData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        //Benutzer Daten übernehmen
        Intent i = getIntent();
        spielData = (SpielData) i.getSerializableExtra("spielData");

        setNeuesSpielButtonClickListener();
        setOffeneSpieleButtonClickListener();
        setHighscoreButtonClickListener();
        setPersDatenButtonClickListener();
    }

    private void setNeuesSpielButtonClickListener() {
        Button neuesSpiel = (Button) findViewById(R.id.buttonNeuesSpiel);
        main.buttonpressed(neuesSpiel);
        neuesSpiel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), FriendlistActivity.class);
                //Benutzer Daten an nächste Activity senden
                myIntent.putExtra(TAG_SPIEL_DATA, spielData);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    private void setOffeneSpieleButtonClickListener() {
        Button offeneSpiele = (Button) findViewById(R.id.buttonOffeneSpiele);
        main.buttonpressed(offeneSpiele);
        offeneSpiele.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), OffeneSpieleActivity.class);
                //Benutzer Daten an nächste Activity senden
                myIntent.putExtra(TAG_SPIEL_DATA, spielData);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    private void setHighscoreButtonClickListener() {
        Button highscore = (Button) findViewById(R.id.buttonHighscore);
        main.buttonpressed(highscore);
        highscore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), HighscoreActivity.class);
                //Benutzer Daten an nächste Activity senden
                myIntent.putExtra(TAG_SPIEL_DATA, spielData);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    private void setPersDatenButtonClickListener() {
        Button persDaten = (Button) findViewById(R.id.buttonPersDaten);
        main.buttonpressed(persDaten);
        persDaten.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), PersDatenActivity.class);
                //Benutzer Daten an nächste Activity senden
                myIntent.putExtra(TAG_SPIEL_DATA, spielData);
                startActivityForResult(myIntent, 0);
            }
        });
    }
}
