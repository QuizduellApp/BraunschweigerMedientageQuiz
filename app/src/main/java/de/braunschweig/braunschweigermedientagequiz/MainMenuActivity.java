package de.braunschweig.braunschweigermedientagequiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Hauptbildschirm nach dem Login
 */
public class MainMenuActivity extends Activity {
    SpielData spielData;
    private static final String TAG_SPIEL_DATA = "spielData";
    // Internet detector
    ConnectionDetector cd;

    @Override
    // Verhindern, dass der Benutzer das Spiel beendet oder zurück zur vorherigen Activity springt
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Wollen Sie sich abmelden?")
                .setCancelable(true)
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Benutzer Daten löschen
                        spielData.resetSpielData();

                        // Login Session zurück setzen, ruft Main Activity auf
                        new SessionManager(getApplicationContext()).logoutUser();
                    }
                })
                .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Angemeldeten Benutzer anzeigen
        TextView tvEingeloggterBenutzer = (TextView) findViewById(R.id.textViewEingeloggterBenutzer);
        tvEingeloggterBenutzer.setText(getString(R.string.text_benutzer)+" "+spielData.getBenutzername());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        // Internet überprüfen
        cd = new ConnectionDetector(getApplicationContext());

        // Alert dialog manager
        AlertDialogManager alert = new AlertDialogManager();

        // Auf Internetverbindung überprüfen
        if (!cd.isConnectingToInternet()) {
            // Keine Internetverbindung vorhanden
            alert.showAlertDialog(this,
                    "Internet Fehler",
                    "Bitte verbinden Sie das Telefon mit dem Internet", false);
            // Anwendung wird hier gestoppt
            return;
        }

        //Benutzer Daten übernehmen
        Intent i = getIntent();
        spielData = (SpielData) i.getSerializableExtra("spielData");

        // Angemeldeten Benutzer anzeigen
        TextView tvEingeloggterBenutzer = (TextView) findViewById(R.id.textViewEingeloggterBenutzer);
        tvEingeloggterBenutzer.setText(getString(R.string.text_benutzer)+" "+spielData.getBenutzername());

        setAbmeldenButtonClickListener();
        setNeuesSpielButtonClickListener();
        setOffeneSpieleButtonClickListener();
        setHighscoreButtonClickListener();
        setPersDatenButtonClickListener();
    }

    private void setAbmeldenButtonClickListener(){
        Button abmelden = (Button) findViewById(R.id.buttonAbmelden);
        abmelden.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Benutzer Daten löschen
                spielData.resetSpielData();

                // Login Session zurück setzen, ruft Main Activity auf
                new SessionManager(getApplicationContext()).logoutUser();
            }
        });
    }

    private void setNeuesSpielButtonClickListener() {
        Button neuesSpiel = (Button) findViewById(R.id.buttonNeuesSpiel);
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
