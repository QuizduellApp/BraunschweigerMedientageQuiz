package de.braunschweig.braunschweigermedientagequiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
    // Datenobjekt der Benutzerdetails
    SpielData spielData;
    private static final String TAG_SPIEL_DATA = "spielData";

    Map<String, String> frage;
    Spiel spiel = new Spiel();
    boolean frageBeantwortet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frage);

        // Ausgewählte Kategorie Speichern
        Intent i = getIntent();
        spielData = (SpielData) i.getSerializableExtra("spielData");

        // Logging
        Log.d("Gewählte Kategorie",""+spielData.getKategorieId());

        // Frage zufällig aufgrund der Kategorie ID auswählen
        frage =  spiel.getFrage(spielData.getKategorieId());

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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Die Frage konnte leider nicht geladen werden!")
                    .setCancelable(true)
                    .setPositiveButton("Wiederholen", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(((Dialog) dialog).getContext(), FrageActivity.class);
                            intent.putExtra(TAG_SPIEL_DATA,spielData);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(((Dialog) dialog).getContext(), MainMenuActivity.class);
                            intent.putExtra(TAG_SPIEL_DATA,spielData);
                            startActivity(intent);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        Antwort1ButtonClickListener();
        Antwort2ButtonClickListener();
        Antwort3ButtonClickListener();
        Antwort4ButtonClickListener();
    }

    // Antwort in Datenbank schreiben
    class SaveAnswer extends AsyncTask<String, String, String> {
        int benutzerAntwortNo;
        public SaveAnswer(int benutzerAntwort){
            super();
            benutzerAntwortNo = benutzerAntwort;
        }
        protected String doInBackground(String... params) {

            // Speichern der Benutzerantwort
            //benutzerAntwortNo;

            return null; // Evtl. richtige Antwort returnen?
        }
    }

    private void evaluateAnswer(int benutzerAntwortNo){
        if (!frage.isEmpty() && !frageBeantwortet) {
            // Frage in die Datenbank schreiben
            new SaveAnswer(benutzerAntwortNo).execute();

            // Benutzer Antwort mit richtiger Antwort vergleichen
            if (Integer.parseInt(frage.get("richtige_antwort")) == benutzerAntwortNo) {
                frageBeantwortet = true;

                // Buttons einfärben, je nach richtiger oder falscher Antwort
                int resID = getResources().getIdentifier("Antwort"+benutzerAntwortNo, "id", "de.braunschweig.braunschweigermedientagequiz");
                Button button = (Button) findViewById(resID);
                button.setBackground( getResources().getDrawable(R.drawable.button_wronganswer));

                showDialogRightAnswer();

            } else {
                frageBeantwortet = true;
                // Falsche Antwort Button einfärben
                int resID = getResources().getIdentifier("Antwort"+benutzerAntwortNo, "id", "de.braunschweig.braunschweigermedientagequiz");
                Button button = (Button) findViewById(resID);
                button.setBackground( getResources().getDrawable(R.drawable.button_rightanswer));

                showDialogWrongAnswer();
            }
        }
    }

    private void showDialogRightAnswer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sie haben die Frage richtig beantwortet!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent myIntent = new Intent(((Dialog) dialog).getContext(), MainMenuActivity.class);
                        myIntent.putExtra(TAG_SPIEL_DATA,spielData);
                        startActivity(myIntent);

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
    private void showDialogWrongAnswer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sie haben die Frage leider falsch beantwortet!")
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent myIntent = new Intent(((Dialog) dialog).getContext(), MainMenuActivity.class);
                        myIntent.putExtra(TAG_SPIEL_DATA,spielData);
                        startActivity(myIntent);

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void Antwort1ButtonClickListener() {
        Button Antwort1 = (Button) findViewById(R.id.Antwort1);
        Antwort1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                evaluateAnswer(1);
            }
        });
    }

    private void Antwort2ButtonClickListener() {
        Button Antwort2 = (Button) findViewById(R.id.Antwort2);
        Antwort2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                evaluateAnswer(2);
            }
        });
    }

    private void Antwort3ButtonClickListener() {
        Button Antwort3 = (Button) findViewById(R.id.Antwort3);
        Antwort3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                evaluateAnswer(3);
            }
        });
    }

    private void Antwort4ButtonClickListener() {
        Button Antwort4 = (Button) findViewById(R.id.Antwort4);
        Antwort4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                evaluateAnswer(4);
            }
        });
    }


}
