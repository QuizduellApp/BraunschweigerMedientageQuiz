package de.braunschweig.braunschweigermedientagequiz;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AppEventsLogger;

public class MainActivity extends FragmentActivity {
    EditText editUsername;
    EditText editPassword;
    Select select = new Select();
    private static final String TAG_BID = "benutzerid";

    //private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        Log.i("APP","TESTEN");
        String hosturl = this.getString(R.string.webserver);
        Log.i("APP", "HOSTURL: "+hosturl);

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
        Button registrieren = (Button) findViewById(R.id.buttonNeuerBenutzer);
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

                } else if(select.select_login(getApplicationContext(), editUsername.getText().toString(),editPassword.getText().toString())==false) {
                    String msg = "Benutzerdaten sind inkorrekt";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    editUsername.setText("");
                    editPassword.setText("");



                } else if(select.select_login(getApplicationContext(),editUsername.getText().toString(),editPassword.getText().toString())==true) {

                    //ID des angemeldeten Benutzers ermitteln
                    String bid = select.getBenutzerID(editUsername.getText().toString(),editPassword.getText().toString());

                    Intent myIntent = new Intent(view.getContext(),
                            MainMenuActivity.class);

                    //Benutzer ID an die nächste Activity übermitteln
                    myIntent.putExtra(TAG_BID, bid);

                    startActivityForResult(myIntent, 0);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
