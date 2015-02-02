package de.braunschweig.braunschweigermedientagequiz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Map;

/**
 *
 */
public class NeuesSpielActivity extends Activity{
    private static final String TAG_BID = "benutzerid";
    Map<String, String> kategorie;

    // Datenobjekt der Benutzerdetails
    SpielData spielData;
    private static final String TAG_SPIEL_DATA = "spielData";

    String gegnerID;
    String gegnerName;
    //int neuesSpielID;
    Spiel spiel = new Spiel();
    Select select = new Select();
    boolean asyncTaskFinished = false;
    private ProgressDialog pDialog; // Dialoganzeige

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neues_spiel_kategorie);

        // Übergebene Daten
        Intent i = getIntent();
        spielData = (SpielData) i.getSerializableExtra(TAG_SPIEL_DATA);
        gegnerName = i.getStringExtra("gegnerName");

        Log.d("GEGNER NAME: ", gegnerName);

        // Neues Spiel in die Datenbank schreiben
        new SaveNewGame().execute();

        // Kategorie Namen laden
        //new GetCategories().execute();
        kategorienLaden();


        // Klick auf den Button.
        // Hier wird auch die Runde angelegt und mit zufälligen Fragen zur gewählten Kategorie befüllt.
        cat1ButtonClickListener();
        cat2ButtonClickListener();
    }

    // Kategorien laden
    private boolean kategorienLaden() {
        kategorie =  spiel.getKategorie(spielData.getBenutzerId());

        if (!kategorie.isEmpty()) {
            String cat1 = kategorie.get("cat1");
            String cat2 = kategorie.get("cat2");

            // Button Text ändern
            Button button = (Button) findViewById(R.id.cat1);
            button.setText(cat1);

            Button button2 = (Button) findViewById(R.id.cat2);
            button2.setText(cat2);

            Log.d("APP_NEUESSPIEL", cat1 + cat2);
            return true;
        } else {
            // TODO Fehlerbehandlung
        }
        return false;
    }

    /**
     * Neu gestartetes Spiel abspeichern
     */
    class SaveNewGame extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    gegnerID = select.getBenutzerIDFromName(gegnerName);
                    int spieler1 = spielData.getBenutzerId();
                    int spieler2 = Integer.parseInt(gegnerID);

                    boolean neuesSpiel = spiel.setNeuesSpiel(spieler1, spieler2);
                    // TODO Fehlerbehandlung, wenn neues Spiel nicht angelegt werden konnte

                    if (neuesSpiel) {
                        spielData.setSpielId(spiel.getSpielId(spieler1, spieler2));
                        Log.d("NEUES SPIEL ID: ", ""+spielData.getSpielId());
                    }
                    Log.d("APP Spieler Neues Spiel: ", "Status: "+neuesSpiel+" - "+spieler1 + spieler2);

                    asyncTaskFinished = true;
                }
            });

            return null;
        }
    }


    /**
     * Neue Runde mit zufälligen Fragen und Kategorie anlegen
     * */
    class SetRound extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // TODO Hinweis einblenden, dass die Runde gespeichert und geladen wird
                }
            });
            return null;
        }
    }


    /*
     * Kategorien laden und anzeigen
     */
    class GetCategories extends AsyncTask<String, String, String> {
        //Before starting background thread Show Progress Dialog
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NeuesSpielActivity.this);
            pDialog.setMessage("Kategorie wird geladen...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        //Kategorien ermitteln
        protected String doInBackground(String... params) {
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                }
            });
            return null;
        }

        // Nach Ende des Backgroundthreads den Vortschrittsdialog schließen
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }
    }


    private void cat1ButtonClickListener() {
        Button cat1Button = (Button) findViewById(R.id.cat1);
        cat1Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!asyncTaskFinished) return;
                Intent intent = new Intent(v.getContext(), FrageActivity.class);

                if (!kategorie.isEmpty()) {
                    // Runde abspeichern
                    spiel.setRunde(spielData.getSpielId(),Integer.parseInt(kategorie.get("cat1_id")),spielData.getBenutzerId());
                    spielData.setKategorieId(Integer.parseInt(kategorie.get("cat1_id")));

                    //Benutzer Daten an die nächste Activity übermitteln
                    intent.putExtra(TAG_SPIEL_DATA, spielData);

                    //myIntent.putExtra("cat_id", kategorie.get("cat1_id"));
                    //myIntent.putExtra("cat_text", kategorie.get("cat1"));
                }
                startActivity(intent);
            }
        });
    }

    private void cat2ButtonClickListener() {
        Button cat2Button = (Button) findViewById(R.id.cat2);
        cat2Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!asyncTaskFinished) return;
                Intent intent = new Intent(v.getContext(), FrageActivity.class);

                if (!kategorie.isEmpty()) {
                    // Runde abspeichern
                    spiel.setRunde(spielData.getSpielId(),Integer.parseInt(kategorie.get("cat2_id")),spielData.getBenutzerId());
                    spielData.setKategorieId(Integer.parseInt(kategorie.get("cat2_id")));

                    //Benutzer Daten an die nächste Activity übermitteln
                    intent.putExtra(TAG_SPIEL_DATA, spielData);

                    //myIntent.putExtra("cat_id", kategorie.get("cat2_id"));
                    //myIntent.putExtra("cat_text", kategorie.get("cat2"));
                }
                startActivity(intent);
            }
        });
    }
}


