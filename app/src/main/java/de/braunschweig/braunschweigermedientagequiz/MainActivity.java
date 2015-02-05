package de.braunschweig.braunschweigermedientagequiz;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AppEventsLogger;

import java.util.HashMap;

public class MainActivity extends FragmentActivity {
    EditText editUsername;
    EditText editPassword;
    Select select = new Select();
    SpielData spielData;
    private static final String TAG_SPIEL_DATA = "spielData";

    // Internet detector
    ConnectionDetector cd;

    //private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Überprüfen, ob Spielerdaten in Shared Preferences bereits vorhanden
        // Wenn vorhanden, wird das Hauptmenü gestartet
        SessionManager sesMan = new SessionManager(getApplicationContext());
        if (sesMan.isLoggedIn()){
            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);

            // spielData setzen
            Log.d("APP Benutzer ID: ",""+sesMan.getBenutzerId());
            Log.d("APP Benutzername: ",""+sesMan.getBenutzername());
            Log.d("APP Passwort: ",""+sesMan.getPasswort()); // könnte null sein
            spielData = new SpielData(sesMan.getBenutzerId()); // Benutzer ID in neuem SpielData Objekt abpseichern
            spielData.setBenutzername(sesMan.getBenutzername()); // Benutzername abspeichern
            spielData.setPasswort(sesMan.getPasswort()); // Passwort abspeichern

            //Benutzer Daten Objekt an die nächste Activity übermitteln
            intent.putExtra(TAG_SPIEL_DATA, spielData);

            startActivityForResult(intent, 0);
        }

        setContentView(R.layout.activity_main);

        // Internet überprüfen
        cd = new ConnectionDetector(getApplicationContext());

        // Alert dialog manager
        AlertDialogManager alert = new AlertDialogManager();

        // Auf Internetverbindung überprüfen
        if (!cd.isConnectingToInternet()) {
            // Keine Internetverbindung vorhanden
            alert.showAlertDialog(this,
                    "Internet Fehler",
                    "Bitte verbinden Sie das Telefon mit dem Internet", false);
            // Anwendung wird hier gestoppt
            return;
        }

        /** TEST FOR FACEBOOK FUNCTION **/
        /**Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        Settings.addLoggingBehavior(LoggingBehavior.REQUESTS);

        Request request = Request.newGraphPathRequest(null, "/4", new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                if (response.getError() != null) {
                    Log.i("MainActivity", String.format("Error making request: %s", response.getError()));
                } else {
                    GraphUser user = response.getGraphObjectAs(GraphUser.class);
                    Log.i("MainActivity", String.format("Name: %s", user.getName()));
                }
            }
        });
        request.executeAsync();
        **/



        /** Registration Button*/
        final Button registrieren = (Button) findViewById(R.id.buttonNeuerBenutzer);
        registrieren.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(),
                        Registrieren.class);
                startActivityForResult(myIntent, 0);
            }

        });

        /** Login Button*/
        Button login = (Button) findViewById(R.id.button_Login);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                editUsername = (EditText) findViewById(R.id.editTextEmail);
                editPassword = (EditText) findViewById(R.id.editTextPasswort);

                Log.i("APP","Username: "+editUsername.getText().toString());
                Log.i("APP","Passwort: "+editPassword.getText().toString());

                if( editUsername.length() == 0 || editPassword.length() == 0 ) {
                    Toast.makeText(MainActivity.this, "Bitte fuellen Sie alle Felder aus!", Toast.LENGTH_LONG).show();

                } else if(select.select_login(editUsername.getText().toString(),editPassword.getText().toString())==false) {
                    String msg = "Benutzerdaten sind inkorrekt";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    editUsername.setText("");
                    editPassword.setText("");

                } else if(select.select_login(editUsername.getText().toString(),editPassword.getText().toString())==true) {

                    //ID des angemeldeten Benutzers ermitteln
                    String bid = select.getBenutzerID(editUsername.getText().toString(),editPassword.getText().toString()); // TODO kann in select_login integriert werden

                    // BID überprüfen
                    if (!bid.isEmpty()) {
                        // Neue Spielerdaten abspeichern
                        spielData = new SpielData(Integer.parseInt(bid)); // BID abspeichern
                        spielData.setBenutzername(editUsername.getText().toString()); // Benutzername abspeichern
                        spielData.setPasswort(editPassword.getText().toString()); // Passwort abspeichern

                        // Daten permanent in Shared Preferences speichern
                        new SessionManager(getApplicationContext()).createLoginSession(
                                spielData.getBenutzername(),
                                spielData.getBenutzerId(),
                                spielData.getPasswort()
                        );

                        Intent intent = new Intent(view.getContext(),
                                MainMenuActivity.class);
                        //Benutzer Daten Objekt an die nächste Activity übermitteln
                        intent.putExtra(TAG_SPIEL_DATA, spielData);

                        startActivityForResult(intent, 0);
                    } else {
                        // Keine BID zurück erhalten
                        Toast.makeText(getApplicationContext(), "Konnte BenutzerID nicht abrufen!", Toast.LENGTH_LONG).show();
                    }
                }
            }

        });

        /** Facebook Login
        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            mainFragment = new MainFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, mainFragment)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            mainFragment = (MainFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }**/
    }


    // Facebook Tracking for App Installs
    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    /**
     * To accurately track the time people spend in your app, you should also log a deactivate event
     * in the onPause() method of each activity where you added the activateApp() method above:
     */
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

}
