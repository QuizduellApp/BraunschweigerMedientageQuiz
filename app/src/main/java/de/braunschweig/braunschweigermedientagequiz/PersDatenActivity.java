package de.braunschweig.braunschweigermedientagequiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Christian on 30.11.2014.
 */
public class PersDatenActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pers_daten);

        setDatenAendernButtonClickListener();
    }


    private void setDatenAendernButtonClickListener() {
        Button datenAendern = (Button) findViewById(R.id.buttonDatenAendern);
        datenAendern.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }
}
