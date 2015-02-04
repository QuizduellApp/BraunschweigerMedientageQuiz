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
import android.widget.Toast;

/**
 * Persönliche Daten des Benutzers anzeigen.
 */
public class PersDatenActivity extends Activity {

    EditText editBenutzername;
    EditText editEmail;
    EditText editPassword;
    EditText editPasswortWdh;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private static final String hosturl = MyApplication.get().getString(R.string.webserver);

    // URL um Benutzerdaten zu laden
    private static final String url_get_persdaten = hosturl+"get_persdaten.php";
    private static final String url_update_persdaten = hosturl+"update_persdaten.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_USER = "benutzer";
    private static final String TAG_BID = "benutzerid";
    private static final String TAG_NAME = "benutzername";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PASSWORD = "passwort";

    SpielData spielData;
    private static final String TAG_SPIEL_DATA = "spielData";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pers_daten);

        Log.i("APP", url_get_persdaten);

        //Benutzer Daten übernehmen
        Intent i = getIntent();
        spielData = (SpielData) i.getSerializableExtra("spielData");

        // Getting complete product details in background thread
        new GetBenutzerDetails().execute();

        setDatenAendernButtonClickListener();

        // Button zurück
        Button abbrechen = (Button) findViewById(R.id.buttonbackpers);
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




    private void setDatenAendernButtonClickListener() {
        Button datenAendern = (Button) findViewById(R.id.buttonDatenAendern);
        datenAendern.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editBenutzername = (EditText) findViewById(R.id.editTextBenutzername);
                editEmail = (EditText) findViewById(R.id.editTextEmail);
                editPassword = (EditText) findViewById(R.id.editTextNeuesPasswort);
                editPasswortWdh = (EditText) findViewById(R.id.editTextNeuesPasswortWdh);

                // Felder überprüfen
                if (editBenutzername.getText().length() == 0 || editEmail.getText().length() == 0) {
                    Toast.makeText(PersDatenActivity.this, "Benutzername und E-Mail sind Pflichtfelder!", Toast.LENGTH_LONG).show();
                } else if (editPassword.getText().length() > 0 && !editPassword.getText().toString().equals(editPasswortWdh.getText().toString())) {
                    // Passwort Wiederholung falsch
                    Toast.makeText(PersDatenActivity.this, "Das Passwort wurde nicht richtig wiederholt!", Toast.LENGTH_LONG).show();
                } else {
                    pDialog = new ProgressDialog(PersDatenActivity.this);
                    pDialog.setMessage("Daten ändern...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();

                    boolean status = saveBenutzerDetails();

                    pDialog.dismiss();

                    if (status) {
                        // Dialog anzeigen, dass die Daten erfolgreich geändert werden konnten
                        Toast.makeText(PersDatenActivity.this, "Daten erfolgreich geändert!", Toast.LENGTH_LONG).show();

                        Intent myIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
                        //Benutzer Daten an die nächste Activity übermitteln
                        myIntent.putExtra(TAG_SPIEL_DATA, spielData);
                        startActivityForResult(myIntent, 0);

                    } else {
                        // Dialog anzeigen, dass die Daten nicht geändert werden konnten
                        Toast.makeText(PersDatenActivity.this, "Fehler beim Ändern der Daten!", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    private boolean saveBenutzerDetails(){
        // getting updated data from EditTexts
        String benutzername = editBenutzername.getText().toString();
        String email = editEmail.getText().toString();
        String passwort = editPassword.getText().toString();

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("bid", ""+spielData.getBenutzerId()));
        params.add(new BasicNameValuePair("benutzername", benutzername));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("passwort", passwort));

        // sending modified data through http request
        // Notice that update product url accepts POST method
        JSONObject json = jsonParser.makeHttpRequest(url_update_persdaten, "POST", params);

        // check json success tag
        try {
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // Erfolgreich geändert

                // Session updaten
                SessionManager sesMan = new SessionManager(getApplicationContext());

                // SpielData aktualisieren
                if (!benutzername.isEmpty()) {
                    spielData.setBenutzername(benutzername);
                    sesMan.setBenutzername(benutzername);
                }
                if(passwort != null) {
                    spielData.setPasswort(passwort);
                    sesMan.setPasswort(passwort);
                }

                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * background Async Task to get user data
     * */
    class GetBenutzerDetails extends AsyncTask<String, String, String> {

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
                        JSONObject json = jsonParser.makeHttpRequest(url_get_persdaten, "GET", params);

                        // check your log for json response
                        Log.d("User Data BID:", ""+spielData.getBenutzerId());
                        Log.d("User Data", json.toString());

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received user data
                            JSONArray userObj = json.getJSONArray(TAG_USER); // JSON Array

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
