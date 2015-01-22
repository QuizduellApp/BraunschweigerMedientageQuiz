package de.braunschweig.braunschweigermedientagequiz;

import android.app.Activity;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Oberklasse für alle Spiel-Interaktionen
 */
public class Spiel extends Activity {
    private static final String hosturl = MyApplication.get().getString(R.string.webserver);
    private static final String url_get_cat = hosturl+"get_cat.php";

    int bid;

    private static final String TAG_SUCCESS = "success";

    // JSON parser class
    JSONParser jsonParser = new JSONParser();



    public void setBid(int bid) {
        this.bid = bid;
    }

    /**
     * Legt ein neues Spiel in der Datenbank
     */
    public void neuesSpiel(int spieler1, int spieler2){

    }

    /**
     * Zeige offene Spiele
     */
    public void offeneSpiele(int spielerId){

    }

    /**
     * Ermittelt den Spieler, der am Zug ist
     */
    public int werIstDran(int spielId){
        int spielerId = 0;

        return spielerId;
    }

    /**
     * Kategorie auswählen
     */
    public Map<String, String> getKategorie(int spielerId){
        Map<String, String> kategorien = new HashMap<>();

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("bid", ""+spielerId));

        try {
            // getting category data by making HTTP request
            // Note that user data url will use GET request
            JSONObject json = jsonParser.makeHttpRequest(url_get_cat, "GET", params);

            // json success tag
            int success;
            success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // Set cat1 und cat2 Liste
                kategorien.put("cat1",json.getString("cat1"));
                kategorien.put("cat2",json.getString("cat2"));

                // IDs der Kategorien
                kategorien.put("cat1_id",json.getString("cat1_id"));
                kategorien.put("cat2_id",json.getString("cat2_id"));

            } else {
                // TODO Fehlerbehandlung
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return kategorien;
    }

    /**
     * Kategorie speichern
     */
    public boolean setKategorie(int spielId, int kategorieId){


        return true;
    }

    /**
     * Frage auswählen
     */
    public int getFrage(int kategorieId){
        int frageId = 0;

        return frageId;
    }

    /**
     * Frage speichern
     */
    public boolean setFrage(int spielId, int frageId){


        return true;
    }

    /**
     * Antwort zu einer Frage speichern
     */
    public boolean setAntwort(int frageId){


        return true;
    }




}
