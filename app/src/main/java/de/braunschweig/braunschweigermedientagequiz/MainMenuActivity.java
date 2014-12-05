package de.braunschweig.braunschweigermedientagequiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by Christian on 22.11.2014.
 */
public class MainMenuActivity extends Activity {

    String bid;
    private static final String TAG_BID = "benutzerid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        //Benutzer ID übernehmen
        Intent i = getIntent();
        bid = i.getStringExtra(TAG_BID);

        setNeuesSpielButtonClickListener();
        setOffeneSpieleButtonClickListener();
        setHighscoreButtonClickListener();
        setPersDatenButtonClickListener();
    }

    private void setNeuesSpielButtonClickListener() {
        Button neuesSpiel = (Button) findViewById(R.id.buttonNeuesSpiel);
        neuesSpiel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), NeuesSpielActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    private void setOffeneSpieleButtonClickListener() {
        Button offeneSpiele = (Button) findViewById(R.id.buttonOffeneSpiele);
        offeneSpiele.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), OffeneSpieleActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    private void setHighscoreButtonClickListener() {
        Button highscore = (Button) findViewById(R.id.buttonHighscore);
        highscore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), HighscoreActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    private void setPersDatenButtonClickListener() {
        Button persDaten = (Button) findViewById(R.id.buttonPersDaten);
        persDaten.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), PersDatenActivity.class);

                //Benutzer ID an nächste Activity senden
                myIntent.putExtra(TAG_BID, bid);

                startActivityForResult(myIntent, 0);
            }
        });
    }
}
