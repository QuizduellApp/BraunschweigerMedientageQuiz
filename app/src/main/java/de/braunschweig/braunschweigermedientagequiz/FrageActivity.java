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

import org.w3c.dom.Text;

import java.util.Map;

/**
 * Frage zur ausgewählten Kategorie wird angezeigt.
 */
public class FrageActivity extends Activity{
    // Datenobjekt der Benutzerdetails
    SpielData spielData;
    MainActivity main = new MainActivity();
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

        // Runde laden und einzelne Fragen ermitteln, wird in Kategorie erledigt.

        // Ermitteln, welche Frage angezeigt werden soll

        int frageId;
        switch (spielData.getFrageAktuell()) {
            case 1:
                frageId = spielData.getFrage1Id();
                break;
            case 2:
                frageId = spielData.getFrage2Id();
                break;
            case 3:
                frageId = spielData.getFrage3Id();
                break;
            default:
                frageId = 0;
        }

        // Frage abrufen, aufgrund der aktuellen ID
        frage =  spiel.getFrage(frageId);

        if (!frage.isEmpty()) {
            // Frage schreiben
            TextView tvFrage = (TextView) findViewById(R.id.FrageView1);
            tvFrage.setText(frage.get("frage"));

            // Button Text ändern
            Button Antwort1 = (Button) findViewById(R.id.Antwort1);
            main.buttonpressed(Antwort1);
            Antwort1.setText(frage.get("antwort1"));

            Button Antwort2 = (Button) findViewById(R.id.Antwort2);
            main.buttonpressed(Antwort2);
            Antwort2.setText(frage.get("antwort2"));

            Button Antwort3 = (Button) findViewById(R.id.Antwort3);
            main.buttonpressed(Antwort3);
            Antwort3.setText(frage.get("antwort3"));

            Button Antwort4 = (Button) findViewById(R.id.Antwort4);
            main.buttonpressed(Antwort4);
            Antwort4.setText(frage.get("antwort4"));

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
            frageBeantwortet = true;

            // Frage in die Datenbank schreiben
            //new SaveAnswer(benutzerAntwortNo).execute();

            // Antwort speichern: Zähler der aktuellen Frage 1 hoch zählen und Antwort speichern
            switch (spielData.getFrageAktuell()) {
                case 1:
                    spielData.setAntwortFrage1(benutzerAntwortNo);
                    break;
                case 2:
                    spielData.setAntwortFrage2(benutzerAntwortNo);
                    break;
                case 3:
                    spielData.setAntwortFrage3(benutzerAntwortNo);
                    break;
            }
            // Benutzer Antwort mit richtiger Antwort vergleichen
            if (Integer.parseInt(frage.get("richtige_antwort")) == benutzerAntwortNo) {

                // Buttons einfärben, je nach richtiger oder falscher Antwort
                int resID = getResources().getIdentifier("Antwort"+benutzerAntwortNo, "id", "de.braunschweig.braunschweigermedientagequiz");
                Button button = (Button) findViewById(resID);
                button.setBackground( getResources().getDrawable(R.drawable.button_wronganswer));

                // Zähler richtige Antworten hoch zählen
                spielData.setRichtigeAntworten(spielData.getRichtigeAntworten()+1);

                // Überprüfen, ob das Rundenende erreicht ist
                if (spielData.getFrageAktuell() >= 3) {

                    // Rundenende mit Daten in die Datenbank speichern und bereinigen
                    rundenende();

                    // Dialog Rundenende anzeigen
                    showDialogRundenende();
                } else {
                    // Dialog richtige Antwort anzeigen
                    showDialogRightAnswer();
                }
            } else {
                // Falsche Antwort Button einfärben
                int resID = getResources().getIdentifier("Antwort"+benutzerAntwortNo, "id", "de.braunschweig.braunschweigermedientagequiz");
                Button button = (Button) findViewById(resID);
                button.setBackground( getResources().getDrawable(R.drawable.button_rightanswer));

                // Überprüfen, ob das Rundenende erreicht ist
                if (spielData.getFrageAktuell() >= 3) {

                    // Rundenende mit Daten in die Datenbank speichern und bereinigen
                    rundenende();

                    // Dialog Rundenende anzeigen
                    showDialogRundenende();
                } else {
                    // Dialog falsche Antwort anzeigen
                    showDialogWrongAnswer();
                }
            }
            spielData.setFrageAktuell(spielData.getFrageAktuell()+1);
        }
    }

    // Wenn eine Runde von drei Fragen beendet wurde
    private void rundenende(){
        // Daten der Runde in die Datenbank schreiben
        spiel.setAntworten(spielData.getRundeId(), spielData.getBenutzerId(),
                spielData.getAntwortFrage1(), spielData.getAntwortFrage2(), spielData.getAntwortFrage3());

        Log.d("RUNDE_ENDE: ", "RundeID: " +spielData.getRundeId());
        Log.d("RUNDE_ENDE: ", "BenutzerID: " +spielData.getBenutzerId());
        Log.d("RUNDE_ENDE: ", "Antwort1: " +spielData.getAntwortFrage1());
        Log.d("RUNDE_ENDE: ", "Antwort2: " +spielData.getAntwortFrage2());
        Log.d("RUNDE_ENDE: ", "Antwort3: " +spielData.getAntwortFrage3());

        // NextToPlay auf den Gegner setzen
        spiel.setNextToPlay(spielData.getSpielId(), spielData.getGegnerId());

        // Highscore erhöhen um die erreichten Punkte
        // Je Frage ein Punkt
        spiel.setHighscore(spielData.getBenutzerId(), spielData.getRichtigeAntworten());


        // TODO überprüfen, ob die Runde aus OFFENE SPIELE gestartet wurde, dann neue Kategorie wählen

    }

    private void showDialogRundenende() {
        // Ermitteln, wie viele Fragen richtig beantwortet wurden

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sie haben "+spielData.getRichtigeAntworten()+
                " von 3 Fragen richtig beantwortet!\n\n" +
                "Warten Sie bis Ihr Gegner gespielt hat!")
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

    private void showDialogRightAnswer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sie haben die Frage richtig beantwortet!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent myIntent = new Intent(((Dialog) dialog).getContext(), FrageActivity.class);
                        myIntent.putExtra(TAG_SPIEL_DATA,spielData);
                        startActivity(myIntent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void setRoundOnInterface (Integer Runde) {
        TextView F1 = (TextView) findViewById(R.id.TextFrage1);
        TextView F2 = (TextView) findViewById(R.id.TextFrage2);
        TextView F3 = (TextView) findViewById(R.id.TextFrage3);
        if (Runde == 1) {F1.setTextColor(Color.parseColor("@color/orange")); F2.setTextColor(Color.parseColor("@color/schwarz")); F3.setTextColor(Color.parseColor("@color/schwarz")); }
        else if (Runde == 2) {F2.setTextColor(Color.parseColor("@color/orange")); F1.setTextColor(Color.parseColor("@color/schwarz")); F3.setTextColor(Color.parseColor("@color/schwarz"));    }
        else {F3.setTextColor(Color.parseColor("@color/orange")); F1.setTextColor(Color.parseColor("@color/schwarz")); F2.setTextColor(Color.parseColor("@color/schwarz"));   }

    }


    private void showDialogWrongAnswer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sie haben die Frage leider falsch beantwortet!")
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent myIntent = new Intent(((Dialog) dialog).getContext(), FrageActivity.class);
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
