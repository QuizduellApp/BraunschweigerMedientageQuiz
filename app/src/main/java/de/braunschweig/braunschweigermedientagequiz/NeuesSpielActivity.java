package de.braunschweig.braunschweigermedientagequiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
    Spiel spiel = new Spiel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neues_spiel_kategorie);

        Intent i = getIntent();
        bid = i.getStringExtra(TAG_BID);

        new Get_Categories().execute();

        cat1ButtonClickListener();
        cat2ButtonClickListener();

    }

    class Get_Categories extends AsyncTask<String, String, String> {
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
    }


    private void cat1ButtonClickListener() {

        Button cat1Button = (Button) findViewById(R.id.cat1);
        cat1Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), FrageActivity.class);
                if (!kategorie.isEmpty()) {
                    myIntent.putExtra("cat_id", kategorie.get("cat1_id"));
                    myIntent.putExtra("cat_text", kategorie.get("cat1"));
                    myIntent.putExtra(TAG_BID, bid);
                }
                startActivityForResult(myIntent, 0);
            }
        });
    }

    private void cat2ButtonClickListener() {

        Button cat2Button = (Button) findViewById(R.id.cat2);
        cat2Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), FrageActivity.class);
                if (!kategorie.isEmpty()) {
                    myIntent.putExtra("cat_id", kategorie.get("cat2_id"));
                    myIntent.putExtra("cat_text", kategorie.get("cat2"));
                    myIntent.putExtra(TAG_BID, bid);
                }
                startActivityForResult(myIntent, 0);
            }
        });
    }

}


