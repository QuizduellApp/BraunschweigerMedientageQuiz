package de.braunschweig.braunschweigermedientagequiz;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

import connect.Select;

/**
 * Created by Dennis on 11.01.15.
 */
public class FriendlistActivity extends Activity
{
    EditText addfriend;

    ArrayAdapter<String> listadapter;
    ListView friendlistview;


    // SELECT Strings for HTTP Request
    Select select = new Select();
    InputStream is = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


}
