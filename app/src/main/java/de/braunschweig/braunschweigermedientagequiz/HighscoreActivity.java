package de.braunschweig.braunschweigermedientagequiz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HighscoreActivity extends Activity {
    private static final String hosturl = MyApplication.get().getString(R.string.webserver);

    // Datenobjekt der Benutzerdetails
    SpielData spielData;
    private static final String TAG_SPIEL_DATA = "spielData";

    ListView highscoreListView;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NAME = "Benutzername";
    private static final String TAG_SCORE = "score";
    private static final String TAG_HIGHSCORE = "Highscore";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Benutzer Daten übernehmen
        Intent i = getIntent();
        spielData = (SpielData) i.getSerializableExtra("spielData");

        // Highscore Liste im Hintergrund ermitteln
        new GetHighscores().execute();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Layout setzen
        setContentView(R.layout.activity_highscore);
        highscoreListView = (ListView) findViewById(R.id.listViewHighscore);

        // Button zurück
        Button abbrechen = (Button) findViewById(R.id.buttonstepback);
        abbrechen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),
                        MainMenuActivity.class);
                //Benutzer Daten an nächste Activity senden
                intent.putExtra(TAG_SPIEL_DATA, spielData);
                startActivityForResult(intent, 0);
            }
        });

    }

    /** Highscores laden */
    class GetHighscores extends AsyncTask<String, String, String> {

        // Fortschrittsdialog einblenden
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HighscoreActivity.this);
            pDialog.setMessage("Highscore wird geladen...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        // Highscore im Hintergrund laden
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
                        JSONObject json = jsonParser.makeHttpRequest(hosturl+"get_highscore.php", "GET", params);

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received user data
                            JSONArray points = json.getJSONArray(TAG_SCORE);

                            int lengthJsonArr = points.length();

                            ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                            for(int i=0; i < lengthJsonArr; i++) {
                                // Get Objekt für jedes Spiel
                                JSONObject JSONchild = points.getJSONObject(i);

                                // Benutzer und den Highscore ermitteln
                                String username = JSONchild.getString(TAG_NAME);
                                String punkte = JSONchild.getString(TAG_HIGHSCORE);
                                Log.d("Benutzername: ", username);
                                Log.d("Highscore: ", punkte);

                                // Liste befüllen
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("name", username);
                                map.put("highscore", ""+punkte);
                                mylist.add(map);
                            }
                            SimpleAdapter listHighscore = new SimpleAdapter(getApplicationContext(), mylist, R.layout.two_column_row,
                                    new String[] {"name", "highscore"}, new int[] {R.id.listName, R.id.listHighscore});
                            highscoreListView.setAdapter(listHighscore);
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

        // Dialog schließen nach Beendigung des Hintergrundtasks
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all data
            pDialog.dismiss();
        }
    }

}
