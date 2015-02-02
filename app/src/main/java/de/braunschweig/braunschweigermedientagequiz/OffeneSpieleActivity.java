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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class OffeneSpieleActivity extends Activity{
    private static final String hosturl = MyApplication.get().getString(R.string.webserver);

    ArrayAdapter<String> benutzer;
    ArrayAdapter<String> spiele;
    ListView friendlistview;

    // Datenobjekt der Benutzerdetails
    SpielData spielData;
    private static final String TAG_SPIEL_DATA = "spielData";

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_GAME = "Spiel_ID";
    private static final String TAG_USER = "benutzer";
    private static final String TAG_NAME = "Benutzername";

    // SELECT Strings for HTTP Request
    Select select = new Select();
    InputStream is = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ausgewählte Kategorie Speichern
        Intent i = getIntent();
        spielData = (SpielData) i.getSerializableExtra("spielData");

        // Offene Spiele abrufen
        new GetOpenGames().execute();

        // Spiele Liste zuweisen
        ArrayList<String> gamesList = new ArrayList<String>();
        benutzer = new ArrayAdapter<String>(this,R.layout.simplerow,gamesList);
        ArrayList<String> gamesidList = new ArrayList<String>();
        spiele = new ArrayAdapter<String>(this,R.layout.simplerow,gamesidList);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //* Layout setzen */
        setContentView(R.layout.activity_opengames);
        friendlistview = (ListView) findViewById(R.id.gamesListView);

        // Liste anklickbar machen
        friendlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(),
                        FrageActivity.class);

                // TODO Kategorie Auswahl ermöglichen
                spielData.setSpielId(Integer.parseInt(spiele.getItem(position)));

                //Benutzer Daten an die nächste Activity übermitteln
                intent.putExtra(TAG_SPIEL_DATA, spielData);

                startActivity(intent);
            }
        });

        /** Zurück */
        Button abbrechen = (Button) findViewById(R.id.buttonstepback);
        abbrechen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),
                        MainMenuActivity.class);

                //Benutzer Daten an die nächste Activity übermitteln
                intent.putExtra(TAG_SPIEL_DATA, spielData);

                startActivityForResult(intent, 0);
            }
        });

    }

    /** Vorhandene Spiele ggn Freunde laden */
    class GetOpenGames extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(OffeneSpieleActivity.this);
            pDialog.setMessage("Offene Spiele werden geladen...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Offene Spiele laden
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread
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
                        JSONObject json = jsonParser.makeHttpRequest(hosturl+"get_games.php", "GET", params);
                        Log.d("User Data", json.toString());
                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received user data
                            JSONArray friends = json.getJSONArray(TAG_USER);
                            Log.d("APP_Games", friends.toString());
                            Log.d("Freunde", String.valueOf(friends.length()));
                            int lengthJsonArr = friends.length();

                            for(int i=0; i < lengthJsonArr; i++) {
                                // Get Objekt für jedes Spiel
                                JSONObject spiel_ID_child = friends.getJSONObject(i);

                                // Name des Freundes und der Spiel_ID ermitteln
                                String game_id = spiel_ID_child.optString(TAG_GAME);
                                String freund = spiel_ID_child.optString(TAG_NAME);
                                Log.d("Spiel gegen: ", freund);
                                Log.d("Spiel_ID: ", game_id);
                                boolean friendExists = false;
                                for (int j = 0; j < benutzer.getCount(); j++){
                                    if (freund.equals(benutzer.getItem(j))) {
                                        friendExists = true;
                                    }
                                }
                                if (!friendExists) {
                                    benutzer.add(freund);
                                    spiele.add(game_id);
                                }

                            }
                            friendlistview.setAdapter(benutzer);
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
        // Dialog schließen nach Beendigung des Hintergrund Tasks
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all data
            pDialog.dismiss();
        }
    }


}
