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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HighscoreActivity extends Activity {
    private static final String hosturl = MyApplication.get().getString(R.string.webserver);

    // Datenobjekt der Benutzerdetails
    SpielData spielData;
    MainActivity main = new MainActivity();
    private static final String TAG_SPIEL_DATA = "spielData";

    ArrayAdapter<String> user;
    ArrayAdapter<String> score;
    ListView userlistview;
    ListView scoreListview;

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

        // Highscore Liste zuweisen
        ArrayList<String> userList = new ArrayList<String>();
        user = new ArrayAdapter<String>(this,R.layout.simplerow,userList);

        ArrayList<String> scoreList = new ArrayList<String>();
        score = new ArrayAdapter<String>(this,R.layout.simplerow,scoreList);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Layout setzen
        setContentView(R.layout.activity_highscore);
        userlistview = (ListView) findViewById(R.id.listView_user);
        scoreListview = (ListView) findViewById(R.id.listView_score);

        // Button zurück
        Button abbrechen = (Button) findViewById(R.id.buttonstepback);
        main.buttonpressed(abbrechen);
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
                        JSONObject json = jsonParser.makeHttpRequest(hosturl+"get_highscore.php", "GET", params);

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received user data
                            JSONArray points = json.getJSONArray(TAG_SCORE);

                            int lengthJsonArr = points.length();

                            for(int i=0; i < lengthJsonArr; i++) {
                                // Get Objekt für jedes Spiel
                                JSONObject JSONchild = points.getJSONObject(i);

                                // Benutzer und den Highscore ermitteln
                                String username = JSONchild.getString(TAG_NAME);
                                String punkte = JSONchild.getString(TAG_HIGHSCORE);
                                Log.d("Benutzername: ", username);
                                Log.d("Highscore: ", punkte);

                                user.add(username);
                                score.add(punkte);
                            }
                            userlistview.setAdapter(user);
                            scoreListview.setAdapter(score);
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
