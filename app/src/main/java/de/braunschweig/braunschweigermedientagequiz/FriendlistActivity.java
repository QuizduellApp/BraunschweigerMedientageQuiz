package de.braunschweig.braunschweigermedientagequiz;

import android.app.Activity;
<<<<<<< HEAD
import android.app.ListActivity;
import android.app.ProgressDialog;
=======
>>>>>>> FETCH_HEAD
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dennis on 11.01.15.
 */
public class FriendlistActivity extends Activity
{
    EditText addfriend;
    EditText freund;
    String bid;

    ArrayAdapter<String> listadapter;
    ListView friendlistview;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_BID = "benutzerid";
    private static final String TAG_USER = "benutzer";
    private static final String TAG_NAME = "benutzername";


    // SELECT Strings for HTTP Request
    Select select = new Select();
    InputStream is = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getting product details from intent
        Intent i = getIntent();

        // getting product id (pid) from intent
        bid = i.getStringExtra(TAG_BID);

        // Getting complete product details in background thread
        new GetFriends().execute();

        ArrayList<String> friendlist = new ArrayList<String>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //* Layout setzen */
        setContentView(R.layout.activity_friendlist);
        friendlistview = (ListView) findViewById(R.id.friendListView);


        /** Benutzer der Freundesliste hinzufügen */
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
                                                 else if(select.self(addfriend.getText().toString()).equals(addfriend.getText().toString()))
                                                 {
                                                     /** Wenn Benutzer existiert, dann in die Liste schreiben */
                                                     String msg = "Das bist du";
                                                     Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                                 }

                                             /** Prüfen ob Benutzer schon in Liste vorhanden */
                                             else if(listadapter.getCount()==0)
                                             {
                                                 /** Wenn Benutzer existiert, dann in die Liste schreiben */
                                                 String benutzername = ""+addfriend.getText().toString();
                                                 listadapter.add(benutzername);
                                                 addfriend.setText("");
                                                 friendlistview.setAdapter(listadapter);
                                                 String msg = "Freund hinzugefügt";
                                                 Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                             }
                                             else if (listadapter.getCount()>0)
                                             {   // Prüfen ob Nutzer bereits in der Friendlist ist
                                                 for (int i = 0; i <listadapter.getCount(); i++)
                                                     if (addfriend.getText().toString().equals(listadapter.getItem(i)))
                                                     {
                                                         String msg = "Freund bereits hinzugefügt";
                                                         Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                                         i=listadapter.getCount();
                                                     }
                                                 else if (i==listadapter.getCount()-1)
                                                         {
                                                             String benutzername = ""+addfriend.getText().toString();
                                                             listadapter.add(benutzername);
                                                             addfriend.setText("");
                                                             friendlistview.setAdapter(listadapter);
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
                        JSONObject json = jsonParser.makeHttpRequest("http://braunschweigermedientage.comyr.com/get_friends.php", "GET", params);

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received user data
                            JSONArray userObj = json.getJSONArray(TAG_USER);  // JSON Array
                            JSONObject user = userObj.getJSONObject(0);
                            String friend = json.getString("benutzer");
                            int l = 0;  // Länge des Benutzernamens
                            int r = 2;  // Kommt nach dem Namen
                            int a = 27;   // Anfang des Strings
                            for (int i =0; i<userObj.length(); i++) {
                                int end = friend.indexOf("\"",a+l);
                                String freund = friend.substring(a+l,end);
                                int k = (freund.length());
                                l = l+ k;
                                Log.d(String.valueOf(l),String.valueOf(a));
                                a= a+27+r;

                                listadapter.add(freund);

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
