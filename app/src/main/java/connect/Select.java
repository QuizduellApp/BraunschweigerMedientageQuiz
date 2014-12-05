package connect;

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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Dennis on 03.12.14.
 */
public class Select extends Activity{
    InputStream is = null;
    boolean result;


    public boolean select_user(String user)
    {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("user",user));
        try
        {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost =  new HttpPost("http://braunschweigermedientage.comyr.com/compare_user.php");
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
                System.out.println(lol);


            } else {
                result = true; // Benutzer existiert


            }
            is.close();


        }catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());

        }
        return result;
    }

    public boolean select_mail(String email)
    {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("email",email));
        try
        {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost =  new HttpPost("http://braunschweigermedientage.comyr.com/compare_mail.php");
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


    public boolean select_login(String user, String passwort)
    {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("user",user));
        nameValuePairs.add(new BasicNameValuePair("passwort",passwort));
        try
        {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost =  new HttpPost("http://braunschweigermedientage.comyr.com/login.php");
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
                result = false; //Eingabe falsch


            } else {
                result = true; // Login erfolgreich


            }
            is.close();


        }catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());

        }
        return result;
    }


    public String getBenutzerID(String user, String passwort) {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("user", user));
        nameValuePairs.add(new BasicNameValuePair("passwort", passwort));
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://braunschweigermedientage.comyr.com/get_benutzerid.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();

            is = entity.getContent();


        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();

            line = sb.append(reader.readLine()).toString();
            is.close();

        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());

        }
        return line;
    }
}
