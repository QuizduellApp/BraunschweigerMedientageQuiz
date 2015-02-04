package de.braunschweig.braunschweigermedientagequiz;

import android.app.Activity;

import android.app.ProgressDialog;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Freundesliste anzeigen und Möglichkeit Freunde hinzu zu fügen.
 */
public class FriendlistActivity extends Activity
{
    EditText addfriend;
    private static final String hosturl = MyApplication.get().getString(R.string.webserver);

    ArrayAdapter<String> listadapter;
    ListView friendlistview;
    MainActivity main = new MainActivity();
    Spiel spiel = new Spiel();

    // Datenobjekt der Benutzerdetails
    SpielData spielData;
    private static final String TAG_SPIEL_DATA = "spielData";

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USER = "benutzer";
    private static final String TAG_NAME = "benutzername";

    // SELECT Strings for HTTP Request
    Select select = new Select();

    private int neuesSpielSpeichern(String gegnerName){
        // Evtl. vorhandene SpielDaten löschen
        spielData.resetSpiel();

        spielData.setGegnerId(Integer.parseInt(select.getBenutzerIDFromName(gegnerName)));
        int spieler1 = spielData.getBenutzerId();
        int spieler2 = spielData.getGegnerId();

        Log.d("GEGNER NAME: ", gegnerName + " - ID: "+spielData.getGegnerId());

        // Überprüfen, ob ein offenes Spiel mit dem gewünschten Gegner schon existiert
        int testSpielExistiert = spiel.getSpielId(spieler1, spieler2);
        if (testSpielExistiert > 0) {
            return 1; // Fehler Spiel schon vorhanden
        }

        // Neues Spiel abspeichern
        boolean neuesSpiel = spiel.setNeuesSpiel(spieler1, spieler2);

        if (neuesSpiel) {
            spielData.setSpielId(spiel.getSpielId(spieler1, spieler2));
            Log.d("NEUES SPIEL ID: ", ""+spielData.getSpielId());
            Log.d("APP Spieler Neues Spiel: ", "Status: "+neuesSpiel+" - "+spieler1 + spieler2);
            return 0; // Alles okay mit dem Anlegen des Spiels
        }

        return 2; // Fehler mit dem Anlegen des neuen Spiels
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Benutzerdaten laden
        Intent i = getIntent();
        spielData = (SpielData)i.getSerializableExtra(TAG_SPIEL_DATA);

        // Freunde im Hintergrund laden
        new GetFriends().execute();

        ArrayList<String> friendlist = new ArrayList<String>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Layout setzen
        setContentView(R.layout.activity_friendlist);
        friendlistview = (ListView) findViewById(R.id.friendListView);

        // Neues Spiel starten nach Klick auf einen Spieler in der Freundesliste
        friendlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(),
                        KategorieActivity.class);

                // Spiel mit dem ausgewählten Freund abspeichern
                int neuesSpielStatus = neuesSpielSpeichern(listadapter.getItem(position));

                switch (neuesSpielStatus) {
                    case 1:
                        // Fehler: Spiel schon angelegt
                        Toast.makeText(getApplicationContext(), "Sie haben bereits ein offenes Spiel mit diesem Freund!", Toast.LENGTH_LONG).show();
                        return;
                    case 2:
                        // Fehler mit dem Anlegen des neuen Spiels
                        Toast.makeText(getApplicationContext(), "Leider konnte das Spiel nicht angelegt werden!", Toast.LENGTH_LONG).show();
                        return;
                }

                intent.putExtra(TAG_SPIEL_DATA, spielData);

                startActivityForResult(intent, 0);
            }
        });


        // Benutzer der Freundesliste hinzufügen
        Button eintragen = (Button) findViewById(R.id.buttonaddfriend);
        listadapter = new ArrayAdapter<String>(this,R.layout.simplerow,friendlist);
        eintragen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view1) {
                addfriend = (EditText) findViewById(R.id.addfriend);

                /** Benutzer existiert nicht in der Datenbank  */
                if (select.select_user(addfriend.getText().toString()) == false) {
                    String msg = "Benutzer unbekannt";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
                /** Selbst adden verboten */
                else if(select.self(""+spielData.getBenutzerId()).equals(addfriend.getText().toString()))
                {
                    /** Wenn Benutzer existiert, dann in die Liste schreiben */
                    String msg = "Das bist du";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }

                /** Prüfen ob Benutzer schon in der Liste vorhanden */
                else if(listadapter.getCount()==0)
                {
                    /** Wenn Benutzer existiert, dann in die Liste schreiben */

                    String benutzername = ""+addfriend.getText().toString();
                    listadapter.add(benutzername);
                    addfriend.setText("");
                    friendlistview.setAdapter(listadapter);
                    select.insert_friend(benutzername,""+spielData.getBenutzerId());
                    String msg = "Freund hinzugefügt";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
                else if (listadapter.getCount()>0)
                {   // Prüfen ob Nutzer bereits in der Friendlist ist
                    for (int i = 0; i <listadapter.getCount(); i++)
                        if (addfriend.getText().toString().equals(listadapter.getItem(i))){
                            String msg = "Freund bereits hinzugefügt";
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            i=listadapter.getCount();
                        } else if (i==listadapter.getCount()-1){
                            String benutzername = ""+addfriend.getText().toString();
                            listadapter.add(benutzername);
                            addfriend.setText("");
                            friendlistview.setAdapter(listadapter);
                            select.insert_friend(benutzername,""+spielData.getBenutzerId());
                            String msg = "Freund hinzugefügt";
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            i=listadapter.getCount();
                        }
                }
            }
        });

        /** Step back */
        Button abbrechen = (Button) findViewById(R.id.buttonstepback);
        abbrechen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(),
                        MainMenuActivity.class);

                //Benutzer Daten an die nächste Activity übermitteln
                myIntent.putExtra(TAG_SPIEL_DATA, spielData);

                startActivityForResult(myIntent, 0);
            }
        });
    }
    /** Vorhandene Freunde laden */
    class GetFriends extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FriendlistActivity.this);
            pDialog.setMessage("Freunde werden geladen...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Freunde im Hintergrund laden
         * */
        protected String doInBackground(String... params) {

            // updating UI from background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("bid", ""+spielData.getBenutzerId()));

                        // getting user data by making HTTP request
                        // Note that user data url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(hosturl+"get_friends.php", "GET", params);

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received user data
                            JSONArray userObj = json.getJSONArray(TAG_USER);  // JSON Array
                            Log.d("APP_Friendlist",userObj.toString());

                            int lengthJsonArr = userObj.length();

                            for(int i=0; i < lengthJsonArr; i++) {
                                // Get Objekt für jeden Benutzer
                                JSONObject jsonChildNode = userObj.getJSONObject(i);

                                // Name des Freundes ermitteln
                                String freund = jsonChildNode.optString(TAG_NAME);
                                Log.d("Freund: ", freund);

                                // Überprüfen, ob Benutzer (Freund) schon in der Liste
                                boolean friendExists = false;
                                for (int j = 0; j < listadapter.getCount(); j++){
                                    if (freund.equals(listadapter.getItem(j))) {
                                        friendExists = true;
                                    }
                                }
                                if (!friendExists) {
                                    listadapter.add(freund);
                                }
                            }
                            friendlistview.setAdapter(listadapter);
                        }else{
                            // user with bid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
}
