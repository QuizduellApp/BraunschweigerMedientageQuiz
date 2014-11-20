package de.braunschweig.braunschweigermedientagequiz;

import java.net.URI;
import java.net.URISyntaxException;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.os.Bundle;
import android.content.Intent;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by Dennis on 18.11.14.
 */
public class Registrieren extends Activity{
    ProgressDialog dialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrieren);

        Button eintragen = (Button) findViewById(R.id.buttonEintragen);
        eintragen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view1) {
                EditText editUsername = (EditText) findViewById(R.id.editUsername);
                EditText editFirstName = (EditText) findViewById(R.id.editFirstName);
                EditText editLastName = (EditText) findViewById(R.id.editLastName);
                EditText editEmail = (EditText) findViewById(R.id.editEmail);
                EditText editPassword = (EditText) findViewById(R.id.editPassword);
                //	EditText editPWiederholung = (EditText) findViewById(R.id.editPWiederholung);
// BLA

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
}