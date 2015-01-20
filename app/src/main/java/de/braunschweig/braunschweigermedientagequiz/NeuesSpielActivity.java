package de.braunschweig.braunschweigermedientagequiz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class NeuesSpielActivity extends Activity{

    private static final String hosturl = MyApplication.get().getString(R.string.webserver);
    EditText editcat1;
    EditText editcat2;
    String bid;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_NAME = "Name";

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private static final String url_get_cat = hosturl+"get_cat.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newgame_categorie);
        new Get_Categories().execute();
    }

    class Get_Categories extends AsyncTask<String, String, String> {
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
                        JSONObject json = jsonParser.makeHttpRequest(url_get_cat, "GET", params);

                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received user data
                            JSONArray userObj = json.getJSONArray(TAG_NAME); // JSON Array

                            // get first user object from JSON Array
                            JSONObject user = userObj.getJSONObject(0);

                            Log.d(TAG_NAME,TAG_NAME);

                            // display user data in EditText
                            editcat1.setText(user.getString(TAG_NAME));
                            editcat2.setText(user.getString(TAG_NAME));

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
    }
}


