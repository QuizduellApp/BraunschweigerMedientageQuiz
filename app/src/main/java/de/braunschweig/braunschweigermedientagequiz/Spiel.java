package de.braunschweig.braunschweigermedientagequiz;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private static final String url_get_frage = hosturl+"get_frage.php";

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
    public boolean setNeuesSpiel(int spieler1, int spieler2){

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("spieler1", ""+spieler1));
        nameValuePairs.add(new BasicNameValuePair("spieler2", ""+spieler2));
        nameValuePairs.add(new BasicNameValuePair("next_to_play", ""+spieler1));

        Log.d("NEUES SPIEL", "Spieler 1: " + spieler1);
        Log.d("NEUES SPIEL", "Spieler 2: " + spieler2);

        InputStream is = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(hosturl+"set_neues_spiel.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch(Exception e){
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }
        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();

            line = sb.append(reader.readLine()).toString();
            is.close();

            if (!line.isEmpty() && line == "true") return true;
        } catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
        }

        return false;
    }

    /**
     * Zeige offene Spiele
     */
    public void getOffeneSpiele(int spielerId){

    }

    /**
     * Ermittelt den Spieler, der am Zug ist
     */
    public int getWerIstDran(int spielId){
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
     * Frage auswählen
     */
    public Map<String, String> getFrage(int kategorieId){
        Map<String, String> frage = new HashMap<>();

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("kategorie_id", ""+kategorieId));

        try {
            // getting category data by making HTTP request
            // Note that user data url will use GET request
            JSONObject json = jsonParser.makeHttpRequest(url_get_frage, "GET", params);

            // json success tag
            int success;
            success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // Frage mit Antworten in der Map speichern
                frage.put("frage_id",json.getString("Frage_ID"));
                frage.put("cat_id",json.getString("Kategorie_ID"));
                frage.put("frage",json.getString("Frage"));
                frage.put("antwort1",json.getString("Antwort_1"));
                frage.put("antwort2",json.getString("Antwort_2"));
                frage.put("antwort3",json.getString("Antwort_3"));
                frage.put("antwort4",json.getString("Antwort_4"));
                frage.put("richtige_antwort",json.getString("Richtige_Antwort"));

            } else {
                // TODO Fehlerbehandlung
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return frage;
    }

    /**
     * Kategorie speichern
     */
    public boolean setKategorie(int spielId, int kategorieId){


        return true;
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
