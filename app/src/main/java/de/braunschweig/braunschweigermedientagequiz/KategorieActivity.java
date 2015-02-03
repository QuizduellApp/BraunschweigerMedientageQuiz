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
 * Kategorien anzeigen zur Auswahl für den Benutzer
 */
public class KategorieActivity extends Activity{
    Map<String, String> kategorie;

    // Datenobjekt der Benutzerdetails
    SpielData spielData;
    private static final String TAG_SPIEL_DATA = "spielData";

    Spiel spiel = new Spiel();
    boolean asyncTaskFinished = false;
    private ProgressDialog pDialog; // Dialoganzeige

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neues_spiel_kategorie);

        // Übergebene Daten
        Intent i = getIntent();
        spielData = (SpielData) i.getSerializableExtra(TAG_SPIEL_DATA);

        // Kategorien laden
        //kategorienLaden(); // direktes laden der Kategorien
        new GetCategories().execute(); // Kategorien im Hintergrund laden

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


    /*
     * Kategorien laden und anzeigen
     */
    class GetCategories extends AsyncTask<String, String, String> {
        //Before starting background thread Show Progress Dialog
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(KategorieActivity.this);
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
                    kategorienLaden();
                    asyncTaskFinished = true;
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

                    // Rundendaten, der zuvor erstellten Runde, laden
                    int aktuelleRunde = spiel.getAktuelleRunde(spielData.getSpielId());
                    Map<String, String> runde = spiel.getRunde(aktuelleRunde);

                    spielData.setFrageAktuell(1);
                    spielData.setFrage1Id(Integer.parseInt(runde.get("frage_id_1")));
                    spielData.setFrage2Id(Integer.parseInt(runde.get("frage_id_2")));
                    spielData.setFrage3Id(Integer.parseInt(runde.get("frage_id_3")));
                    spielData.setRundeId(Integer.parseInt(runde.get("runde_id")));
                    spielData.setRundeCount(Integer.parseInt(runde.get("runde")));

                    //Benutzer Daten an die nächste Activity übermitteln
                    intent.putExtra(TAG_SPIEL_DATA, spielData);
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

                    // Rundendaten, der zuvor erstellten Runde, laden
                    int aktuelleRunde = spiel.getAktuelleRunde(spielData.getSpielId());
                    Map<String, String> runde = spiel.getRunde(aktuelleRunde);

                    spielData.setFrage1Id(Integer.parseInt(runde.get("frage_id_1")));
                    spielData.setFrage2Id(Integer.parseInt(runde.get("frage_id_2")));
                    spielData.setFrage3Id(Integer.parseInt(runde.get("frage_id_3")));
                    spielData.setRundeId(Integer.parseInt(runde.get("runde_id")));
                    spielData.setRundeCount(Integer.parseInt(runde.get("runde")));

                    //Benutzer Daten an die nächste Activity übermitteln
                    intent.putExtra(TAG_SPIEL_DATA, spielData);
                }
                startActivity(intent);
            }
        });
    }
}


