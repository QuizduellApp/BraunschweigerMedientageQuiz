package de.braunschweig.braunschweigermedientagequiz;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Frage zur ausgewählten Kategorie wird angezeigt.
 */
public class FrageActivity extends Activity{

    String cat_id; // Geerbt von Kategorie_Activity, Kategorien_id
    String User1; // Geerbt von Kategorie_Activity, Benutzer 1
    String User2; // Geerbt von Kategorie_Activity, Benutzer 2


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frage);

        // Ausgewählte Kategorie Speichern
        Intent i = getIntent();
        cat_id = i.getStringExtra("cat_id");

        Log.d("Gewählte Kategorie",cat_id);

        new Get_Question().execute(); //Frage laden
    }

    // Fragen laden Klasse
    class Get_Question  extends AsyncTask<String, String, String> {
        protected String doInBackground(String... params) {
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

    private void Antwort1ButtonClickListener() {
        Button Antwort1 = (Button) findViewById(R.id.Antwort1);
        Antwort1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), FrageActivity.class);
                new Save_Answer().execute();
                startActivityForResult(myIntent, 0);
            }
        });
    }

    private void Antwort2ButtonClickListener() {
        Button Antwort2 = (Button) findViewById(R.id.Antwort1);
        Antwort2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), FrageActivity.class);
                new Save_Answer().execute();
                startActivityForResult(myIntent, 0);
            }
        });
    }

    private void Antwort3ButtonClickListener() {
        Button Antwort3 = (Button) findViewById(R.id.Antwort1);
        Antwort3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), FrageActivity.class);
                new Save_Answer().execute();
                startActivityForResult(myIntent, 0);
            }
        });
    }

    private void Antwort4ButtonClickListener() {
        Button Antwort4 = (Button) findViewById(R.id.Antwort1);
        Antwort4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), FrageActivity.class);
                new Save_Answer().execute();
                startActivityForResult(myIntent, 0);
            }
        });
    }


}
