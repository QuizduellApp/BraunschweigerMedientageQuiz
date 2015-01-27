package de.braunschweig.braunschweigermedientagequiz;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;

/**
 * Frage zur ausgewählten Kategorie wird angezeigt.
 */
public class FrageActivity extends Activity{
    String cat_id; // Geerbt von Kategorie_Activity, Kategorien_id
    private static final String TAG_BID = "benutzerid";
    Map<String, String> frage;
    String bid;
    Spiel spiel = new Spiel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frage);

        // Ausgewählte Kategorie Speichern
        Intent i = getIntent();
        cat_id = i.getStringExtra("cat_id");
        bid = i.getStringExtra(TAG_BID);

        Log.d("Gewählte Kategorie",cat_id);

        frage =  spiel.getFrage(Integer.parseInt(cat_id));

        if (!frage.isEmpty()) {
            // Frage schreiben
            TextView tvFrage = (TextView) findViewById(R.id.FrageView1);
            tvFrage.setText(frage.get("frage"));

            //if (tvFrage.getText().length()>30) tvFrage.setTextSize(8); // Break nach char 30

            // Button Text ändern
            Button Antwort1 = (Button) findViewById(R.id.Antwort1);
            Antwort1.setText(frage.get("antwort1"));


            Button Antwort2 = (Button) findViewById(R.id.Antwort2);
            Antwort2.setText(frage.get("antwort2"));


            Button Antwort3 = (Button) findViewById(R.id.Antwort3);
            Antwort3.setText(frage.get("antwort3"));


            Button Antwort4 = (Button) findViewById(R.id.Antwort4);
            Antwort4.setText(frage.get("antwort4"));



            //Log.d("APP_NEUESSPIEL", cat1 + cat2);
        } else {
            // TODO Fehlerbehandlung
        }

        //new Get_Question().execute(); //Frage laden

        Antwort1ButtonClickListener();
    }

    // Fragen laden Klasse
    class Get_Question  extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {

            frage =  spiel.getFrage(Integer.parseInt(cat_id));

            if (!frage.isEmpty()) {
                // Frage schreiben
                TextView tvFrage = (TextView) findViewById(R.id.FrageView1);
                tvFrage.setText(frage.get("frage"));

                // Button Text ändern
                Button Antwort1 = (Button) findViewById(R.id.Antwort1);
                Antwort1.setText(frage.get("antwort1"));

                Button Antwort2 = (Button) findViewById(R.id.Antwort2);
                Antwort2.setText(frage.get("antwort2"));

                Button Antwort3 = (Button) findViewById(R.id.Antwort3);
                Antwort3.setText(frage.get("antwort3"));

                Button Antwort4 = (Button) findViewById(R.id.Antwort4);
                Antwort4.setText(frage.get("antwort4"));


                //Log.d("APP_NEUESSPIEL", cat1 + cat2);
            } else {
                // TODO Fehlerbehandlung
            }
            return null; // Evtl. richtige Antwort returnen? Und dann mit Nutzer Antwort vergleichen
        }
    }

    // Antwort in Datenbank schreiben
    class Save_Answer extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {
            return null; // Evtl. richtige Antwort returnen?
        }
    }


    // Wenn auf ein Button geklickt wird, dann die richtige Antwort Grün färben, 4 Sekunden warten, dann nächste Frage holen?!

    /*private void Antwort1ButtonClickListener() {
        Button Antwort1 = (Button) findViewById(R.id.Antwort1);
        Antwort1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), FrageActivity.class);
                new Save_Answer().execute();
                startActivityForResult(myIntent, 0);
            }
        });
    }*/

    private void Antwort1ButtonClickListener() {

        Button Antwort1 = (Button) findViewById(R.id.Antwort1);
        Antwort1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), FrageActivity.class);
                if (!frage.isEmpty()) {
                    myIntent.putExtra("cat_id", frage.get("frage"));
                }
                startActivityForResult(myIntent, 0);
            }
        });
    }

    private void Antwort2ButtonClickListener() {
        Button Antwort2 = (Button) findViewById(R.id.Antwort2);
        Antwort2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), FrageActivity.class);
                new Save_Answer().execute();
                startActivityForResult(myIntent, 0);
            }
        });
    }

    private void Antwort3ButtonClickListener() {
        Button Antwort3 = (Button) findViewById(R.id.Antwort3);
        Antwort3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), FrageActivity.class);
                new Save_Answer().execute();
                startActivityForResult(myIntent, 0);
            }
        });
    }

    private void Antwort4ButtonClickListener() {
        Button Antwort4 = (Button) findViewById(R.id.Antwort4);
        Antwort4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), FrageActivity.class);
                new Save_Answer().execute();
                startActivityForResult(myIntent, 0);
            }
        });
    }


}
