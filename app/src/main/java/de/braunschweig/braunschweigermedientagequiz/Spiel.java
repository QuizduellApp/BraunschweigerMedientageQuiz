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
    private static final String url_get_spiel_id = hosturl+"get_spiel_id.php";
    private static final String url_set_neues_spiel = hosturl+"set_neues_spiel.php";
    private static final String url_set_runde_neu = hosturl+"set_runde_neu.php";
    private static final String url_set_antworten = hosturl+"set_antworten.php";
    private static final String url_set_next_to_play = hosturl+"set_next_to_play.php";
    private static final String url_get_runde = hosturl+"get_runde.php";
    private static final String url_get_aktuelle_runde = hosturl+"get_aktuelle_runde.php";

    int bid;

    private static final String TAG_SUCCESS = "success";

    public void setBid(int bid) {
        this.bid = bid;
    }



    /**
     * Legt ein neues Spiel in der Datenbank an
     */
    public boolean setNeuesSpiel(int spieler1, int spieler2){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("spieler1", ""+spieler1));
        nameValuePairs.add(new BasicNameValuePair("spieler2", ""+spieler2));
        nameValuePairs.add(new BasicNameValuePair("next_to_play", ""+spieler1));

        Log.d("NEUES SPIEL", "Spieler 1: " + spieler1);
        Log.d("NEUES SPIEL", "Spieler 2: " + spieler2);

        String returnResult = httpRequest(nameValuePairs,url_set_neues_spiel);

        if (returnResult.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Ändert den Spieler, der an der Reihe ist
     */
    public boolean setNextToPlay(int spielId, int nextToPlay){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("spiel_id", ""+spielId));
        nameValuePairs.add(new BasicNameValuePair("next_to_play", ""+nextToPlay));

        String returnResult = httpRequest(nameValuePairs,url_set_next_to_play);

        Log.d("SpielID ", ""+spielId);
        Log.d("NextToPlay ",""+nextToPlay);

        if (returnResult.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Zeige offene Spiele
     */
    public void getOffeneSpiele(int spielerId){

    }

    /**
     * Ermittelt den Spieler, der am Zug ist
     */
    public int getSpielId(int spieler1, int spieler2){
        int spielId = 0;

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("spieler1", ""+spieler1));
        params.add(new BasicNameValuePair("spieler2", ""+spieler2));

        try {
            // JSON Request um Spiel ID zu ermitteln
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(url_get_spiel_id, "GET", params);

            // json success tag
            int success;
            success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // Spiel ID sichern
                spielId = Integer.parseInt(json.getString("Spiel_ID"));
            } else {
                // TODO Fehlerbehandlung
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return spielId;
    }

    /**
     * Ermittelt den Spieler, der am Zug ist
     */
    public int getWerIstDran(int spielId){
        int spielerId = 0;

        return spielerId;
    }


    /**
     * Kategorie zufällig auswählen
     */
    public Map<String, String> getKategorie(int spielerId){
        Map<String, String> kategorien = new HashMap<>();

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("bid", ""+spielerId));

        try {
            // getting category data by making HTTP request
            // Note that user data url will use GET request
            JSONParser jsonParser = new JSONParser();
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
     * Frage aus der DB abrufen
     */
    public Map<String, String> getFrage(int frageId){
        Map<String, String> frage = new HashMap<>();

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("frage_id", ""+frageId));

        try {
            // JSON Request Objekt
            JSONParser jsonParser2 = new JSONParser();
            JSONObject json2 = jsonParser2.makeHttpRequest(url_get_frage, "GET", params);

            // JSON Success Tag
            int success;
            success = json2.getInt(TAG_SUCCESS);

            Log.d("JSON URL: ", url_get_frage);
            Log.d("JSON PARAMS: ", params.toString());

            Log.d("JSON RESPONSE: ",json2.toString());


            if (success == 1) {
                // Frage mit Antworten in der Map speichern
                frage.put("frage_id",json2.getString("Frage_ID"));
                frage.put("cat_id",json2.getString("Kategorie_ID"));
                frage.put("frage",json2.getString("Frage"));
                frage.put("antwort1",json2.getString("Antwort_1"));
                frage.put("antwort2",json2.getString("Antwort_2"));
                frage.put("antwort3",json2.getString("Antwort_3"));
                frage.put("antwort4",json2.getString("Antwort_4"));
                frage.put("richtige_antwort",json2.getString("Richtige_Antwort"));
            } else {
                // TODO Fehlerbehandlung
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return frage;
    }

    /**
     * Neue Runde mit ausgewählter Kategorie anlegen
     */
    public boolean setRundeNeu(int spielId, int kategorieId, int benutzerId){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("spiel_id", ""+spielId));
        nameValuePairs.add(new BasicNameValuePair("benutzer_id", ""+benutzerId));
        nameValuePairs.add(new BasicNameValuePair("kategorie_id", ""+kategorieId));

        //Log.d("NEUES SPIEL", "Spieler 1: " + spieler1);
        //Log.d("NEUES SPIEL", "Spieler 2: " + spieler2);

        String returnResult = httpRequest(nameValuePairs,url_set_runde_neu);

        if (returnResult.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Antworten (Rundendaten) speichern
     */
    public boolean setAntworten(int rundeId, int benutzerId, int antwort1, int antwort2, int antwort3){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("runde_id", ""+rundeId));
        nameValuePairs.add(new BasicNameValuePair("benutzer_id", ""+benutzerId));
        nameValuePairs.add(new BasicNameValuePair("antwort_1", ""+antwort1));
        nameValuePairs.add(new BasicNameValuePair("antwort_2", ""+antwort2));
        nameValuePairs.add(new BasicNameValuePair("antwort_3", ""+antwort3));

        String returnResult = httpRequest(nameValuePairs,url_set_antworten);

        if (returnResult.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Runde zurück liefern
     */
    public Map<String, String> getRunde(int rundeId){
        Map<String, String> runde = new HashMap<>();

        // Parameter für den JSON Request
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("runde_id", ""+rundeId));

        try {
            // JSON Parser Objekte
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(url_get_runde, "GET", params);

            // JSON Success Tag
            int success;
            success = json.getInt(TAG_SUCCESS);

            Log.d("JSON URL: ", url_get_frage);
            Log.d("JSON PARAMS: ", params.toString());
            Log.d("JSON RESPONSE: ",json.toString());

            if (success == 1) {
                // Runde mit Antworten in der Map speichern
                runde.put("runde_id",json.getString("Runde_ID"));
                runde.put("spiel_id",json.getString("Spiel_ID"));
                runde.put("benutzer_id",json.getString("Benutzer_ID"));
                runde.put("runde",json.getString("Runde"));
                runde.put("kategorie_id",json.getString("Kategorie_ID"));
                runde.put("frage_id_1",json.getString("Frage_ID_1"));
                runde.put("frage_id_2",json.getString("Frage_ID_2"));
                runde.put("frage_id_3",json.getString("Frage_ID_3"));
            } else {
                // TODO Fehlerbehandlung
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return runde;
    }


    /**
     * Aktuelle Runde zurück liefern
     */
    public int getAktuelleRunde(int spielId){
        // Parameter für den JSON Request
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("spiel_id", ""+spielId));

        try {
            // JSON Parser Objekte
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(url_get_aktuelle_runde, "GET", params);

            // JSON Success Tag
            int success;
            success = json.getInt(TAG_SUCCESS);

            // Log Ausgaben
            Log.d("JSON URL: ", url_get_aktuelle_runde);
            Log.d("JSON PARAMS: ", params.toString());
            Log.d("JSON RESPONSE: ",json.toString());

            if (success == 1) {
                // RundeID zurück liefern
                return Integer.parseInt(json.getString("Runde_ID"));
            } else {
                // TODO Fehlerbehandlung
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }


    /*
    Generalisierter httpRequest
     */
    private String httpRequest(ArrayList<NameValuePair> nameValuePairs, String requestUrl) {
        InputStream is = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(requestUrl);
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
        } catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
        }
        return line;
    }

}
