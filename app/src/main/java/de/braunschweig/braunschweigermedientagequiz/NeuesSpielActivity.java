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
    String bid;
    String gegnerID;
    String gegnerName;
    int neuesSpielID;
    Spiel spiel = new Spiel();
    Select select = new Select();
    boolean asyncTaskFinished = false;
    // Progress Dialog
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neues_spiel_kategorie);

        // Übergebene Daten Ändern
        Intent i = getIntent();
        bid = i.getStringExtra(TAG_BID);
        gegnerName = i.getStringExtra("gegnerName");

        Log.d("GEGNER NAME: ", gegnerName);

        // Neues Spiel in die Datenbank schreiben
        new SaveNewGame().execute();

        // Neue Runde anlegen mit zufälliger Kategorie und zufälligen Fragen
        //new SetRound().execute(); zu früh, Benutzer muss Button erst klicken

        // Kategorie Namen laden
        new GetCategories().execute();

        cat1ButtonClickListener();
        cat2ButtonClickListener();
    }

    class SaveNewGame extends AsyncTask<String, String, String> {
        /**
         * Neu gestartetes Spiel abspeichern
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    gegnerID = select.getBenutzerIDFromName(gegnerName);
                    int spieler1 = Integer.parseInt(bid);
                    int spieler2 = Integer.parseInt(gegnerID);

                    if (!bid.isEmpty() && !gegnerID.isEmpty()) {
                        boolean neuesSpiel = spiel.setNeuesSpiel(spieler1, spieler2);
                        // TODO Fehlerbehandlung, wenn neues Spiel nicht angelegt werden konnte

                        if (neuesSpiel) {
                            neuesSpielID = spiel.getSpielId(spieler1, spieler2);
                            Log.d("NEUES SPIEL ID: ", ""+neuesSpielID);
                        }
                        Log.d("APP Spieler Neues Spiel: ", "Status: "+neuesSpiel+" - "+spieler1 + spieler2);
                    } else {
                        // TODO Fehlerbehandlung
                    }
                    asyncTaskFinished = true;
                }
            });

            return null;
        }
    }


    class SetRound extends AsyncTask<String, String, String> {
        /**
         * Neue Runde mit zufälligen Fragen und Kategorie anlegen
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // TODO Hinweis einblenden, dass die Runde gespeichert und geladen wird
                    int i = 0;
                }
            });

            return null;
        }
    }


    class GetCategories extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NeuesSpielActivity.this);
            pDialog.setMessage("Kategorie wird geladen...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Kategorien ermitteln
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    kategorie =  spiel.getKategorie(Integer.parseInt(bid));

                    if (!kategorie.isEmpty()) {
                        String cat1 = kategorie.get("cat1");
                        String cat2 = kategorie.get("cat2");

                        // Button Text ändern
                        Button button = (Button) findViewById(R.id.cat1);
                        button.setText(cat1);

                        Button button2 = (Button) findViewById(R.id.cat2);
                        button2.setText(cat2);

                        Log.d("APP_NEUESSPIEL", cat1 + cat2);
                    } else {
                        // TODO Fehlerbehandlung
                    }
                }
            });

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all data
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
                    new SetRound().execute();
                    Bundle extras = new Bundle();
                    extras.putString("TAG_BID",bid);
                    extras.putString("TAG_GAME","1");
                    extras.putString("TAG_CAT",kategorie.get("cat2_id"));
                    intent.putExtras(extras);
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
                    Bundle extras = new Bundle();
                    extras.putString("TAG_BID",bid);
                    extras.putString("TAG_GAME","1");
                    extras.putString("TAG_CAT",kategorie.get("cat2_id"));
                    intent.putExtras(extras);
                }
                startActivity(intent);
            }
        });
    }
}


