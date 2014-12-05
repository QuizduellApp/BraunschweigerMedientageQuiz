package de.braunschweig.braunschweigermedientagequiz;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Christian on 30.11.2014.
 */
public class PersDatenActivity extends Activity {

    EditText editBenutzername;
    EditText editEmail;
    EditText editPasswort;
    EditText editPasswortWdh;
    String bid;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // URL um Benutzerdaten zu laden
    private static final String url_get_persdaten = "http://braunschweigermedientage.comyr.com/get_persdaten.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USER = "benutzer";
    private static final String TAG_BID = "benutzerid";
    private static final String TAG_NAME = "benutzername";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PASSWORD = "passwort";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pers_daten);

        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        bid = i.getStringExtra(TAG_BID);

        // Getting complete product details in background thread
        new GetProductDetails().execute();

        setDatenAendernButtonClickListener();
    }


    private void setDatenAendernButtonClickListener() {
        Button datenAendern = (Button) findViewById(R.id.buttonDatenAendern);
        datenAendern.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // starting background task to update product
                //new SaveProductDetails().execute();
            }
        });
    }

    /**
     * Background Async Task to get user data
     * */
    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PersDatenActivity.this);
            pDialog.setMessage("Benutzerdaten werden geladen...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Getting user data in background thread
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
                        JSONObject json = jsonParser.makeHttpRequest(url_get_persdaten, "GET", params);

                        // check your log for json response
                        Log.d("User Data", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received user data
                            JSONArray userObj = json
                                    .getJSONArray(TAG_USER); // JSON Array

                            // get first user object from JSON Array
                            JSONObject user = userObj.getJSONObject(0);

                            // user with this bid found
                            // Edit Text
                            editBenutzername = (EditText) findViewById(R.id.editTextBenutzername);
                            editEmail = (EditText) findViewById(R.id.editTextEmail);

                            // display user data in EditText
                            editBenutzername.setText(user.getString(TAG_NAME));
                            editEmail.setText(user.getString(TAG_EMAIL));

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
