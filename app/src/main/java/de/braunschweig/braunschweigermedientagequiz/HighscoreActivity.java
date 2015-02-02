package de.braunschweig.braunschweigermedientagequiz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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


public class HighscoreActivity extends Activity {
    String bid;
    private static final String hosturl = MyApplication.get().getString(R.string.webserver);

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
    private static final String TAG_BID = "benutzerid";
    private static final String TAG_NAME = "Benutzername";
    private static final String TAG_SCORE = "Highscore";

    // SELECT Strings for HTTP Request
    Select select = new Select();
    InputStream is = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getting user details from intent
        Intent i = getIntent();
        new GetHighscores().execute();

        // Spiele Liste zuweisen
        ArrayList<String> userList = new ArrayList<String>();
        user = new ArrayAdapter<String>(this,R.layout.simplerow,userList);

        ArrayList<String> scoreList = new ArrayList<String>();
        score = new ArrayAdapter<String>(this,R.layout.simplerow,scoreList);

        // getting user id (pid) from intent
        bid = i.getStringExtra(TAG_BID);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //* Layout setzen */
        setContentView(R.layout.activity_highscore);
        userlistview = (ListView) findViewById(R.id.listView_user);
        scoreListview = (ListView) findViewById(R.id.listView_score);

        /** Step back */
        Button abbrechen = (Button) findViewById(R.id.buttonstepback);
        abbrechen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(),
                        MainMenuActivity.class);
                //Benutzer ID an die nächste Activity übermitteln
                myIntent.putExtra(TAG_BID, bid);
                startActivityForResult(myIntent, 0);
            }
        });

    }

    /** Highscores laden */
    class GetHighscores extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HighscoreActivity.this);
            pDialog.setMessage("Highscore wird geladen...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Highscore laden
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
                        params.add(new BasicNameValuePair("bid", bid));

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
                                String punkte = JSONchild.getString(TAG_SCORE);
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
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all data
            pDialog.dismiss();
        }
    }

}
