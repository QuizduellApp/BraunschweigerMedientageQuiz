package de.braunschweig.braunschweigermedientagequiz;


import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import connect.Select;

public class MainActivity extends Activity {
    EditText editUsername;
    EditText editPassword;
    Select select = new Select();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

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

                if ( editUsername.length() == 0
                        || editPassword.length() == 0 )
                {
                    Toast.makeText(MainActivity.this, "Bitte fuellen Sie alle Felder aus!", Toast.LENGTH_LONG).show();

                } else if

                        (select.select_login(editUsername.getText().toString(),editPassword.getText().toString())==false)


                {String msg = "Benutzerdaten sind inkorrekt";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(view.getContext(),
                            MainActivity.class);   //TODO MainMenü eintragen zum weiterleiten
                    startActivityForResult(myIntent, 0);
                }
               else if
                        (select.select_login(editUsername.getText().toString(),editPassword.getText().toString())==true)
               {


               Intent myIntent = new Intent(view.getContext(),
                        MainMenuActivity.class);
                startActivityForResult(myIntent, 0);
                }
            }

        });

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
}
