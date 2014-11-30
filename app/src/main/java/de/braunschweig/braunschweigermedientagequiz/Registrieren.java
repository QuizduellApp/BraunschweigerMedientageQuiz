package de.braunschweig.braunschweigermedientagequiz;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by Dennis on 18.11.14.
 */
public class Registrieren extends Activity
{
    private ProgressDialog pDialog;
    private TextView statusField,roleField;
    private Context context;
    private int byGetOrPost = 0;
    //flag 0 means get and 1 means post.(By default it is get.)
    EditText editUsername;
    EditText editFirstName;
    EditText editLastName;
    EditText editEmail;
    EditText editPassword;
    InputStream is = null;
    // SELECT Strings for HTTP Request

    boolean result;
    String name;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_registrieren);


        Button eintragen = (Button) findViewById(R.id.buttonEintragen);
        eintragen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view1) {
                editUsername = (EditText) findViewById(R.id.editUsername);
                editFirstName = (EditText) findViewById(R.id.editFirstName);
                editLastName = (EditText) findViewById(R.id.editLastName);
                editEmail = (EditText) findViewById(R.id.editEmail);
                editPassword = (EditText) findViewById(R.id.editPassword);
                //	EditText editPWiederholung = (EditText) findViewById(R.id.editPWiederholung);

                /**
                 * Fehlermeldung falls nicht alle Felder ausgefuellt sind!
                 **/
                if ( editUsername.length() == 0
                        || editFirstName.length() == 0
                        || editLastName.length() == 0
                        || editEmail.length() == 0
                        || editPassword.length() == 0
                    //	 || editPWiederholung.length() == 0
                        ) {
                    Toast.makeText(Registrieren.this, "Bitte fuellen Sie alle Felder aus!", Toast.LENGTH_LONG).show();
                } else if

                        (select(editUsername.getText().toString())==true)
                {String msg = "Benutzer existiert bereits";
                   Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(view1.getContext(),
                            Registrieren.class);
                    startActivityForResult(myIntent, 0);
                }


                else {
                    String benutzer_id = "0";
                    String benutzername = ""+editUsername.getText().toString();
                    String vorname = ""+editFirstName.getText().toString();
                    String nachname = ""+editLastName.getText().toString();
                    String passwort = ""+editPassword.getText().toString();
                    String email = ""+editEmail.getText().toString();
                    String highscore = "0";

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("benutzer_id", benutzer_id));
                    nameValuePairs.add(new BasicNameValuePair("benutzername", benutzername));
                    nameValuePairs.add(new BasicNameValuePair("vorname", vorname));
                    nameValuePairs.add(new BasicNameValuePair("nachname", nachname));
                    nameValuePairs.add(new BasicNameValuePair("passwort", passwort));
                    nameValuePairs.add(new BasicNameValuePair("email", email));
                    nameValuePairs.add(new BasicNameValuePair("highscore", highscore));

                    try {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost =  new HttpPost("http://braunschweigermedientage.comyr.com/create_user.php");
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse response = httpClient.execute(httpPost);

                        HttpEntity entity = response.getEntity();

                        is = entity.getContent();


                        String msg = "Benutzer erfolgreich registriert";
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(view1.getContext(),
                                MainActivity.class);
                        startActivityForResult(myIntent, 0);

                    }
                    catch(ClientProtocolException e)
                    {
                        Log.e("ClientProtocol", "Log_tag");
                        e.printStackTrace();
                    }catch (IOException e)
                    {
                        Log.e("Log_tag","IOException");
                        e.printStackTrace();
                    }

                }

            }

        });


          Button abbrechen = (Button) findViewById(R.id.buttonAbbrechen);
         abbrechen.setOnClickListener(new View.OnClickListener() {
         public void onClick(View view) {
         Intent myIntent = new Intent(view.getContext(),
         MainActivity.class);
         startActivityForResult(myIntent, 0);
         }
         });



    }


    public boolean select(String user)
    {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("user",user));
        try
        {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost =  new HttpPost("http://braunschweigermedientage.comyr.com/compare_userdata.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();

            is = entity.getContent();


        }
        catch(Exception e)
        {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();

            String line = sb.append(reader.readLine()).toString();
            int lol = line.length();



            if (lol  == 5) {
                result = false; //Benutzer nicht vorhanden


            } else {
                result = true; // Benutzer existiert


            }
            is.close();


        }catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());

        }
        return result;
    }






}