package de.braunschweig.braunschweigermedientagequiz;
        
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
        
public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "BSMediaQuiz";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // Variablen, die auf dem Gerät gespeichert werden sollen. Public um diese von Außen lesbar zu machen
    public static final String KEY_NAME = "benutzer_name";
    public static final String KEY_NAME_ID = "benutzer_id";
    public static final String KEY_PASSWORT = "passwort";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    
     /**
     * Login Session anlegen
     * */
     public void createLoginSession(String benutzerName, int benutzerId, String passwort){
        // Login Value auf True setzen
        editor.putBoolean(IS_LOGIN, true);

        // Daten speichern
        editor.putString(KEY_NAME, benutzerName);
        editor.putInt(KEY_NAME_ID, benutzerId);
        editor.putString(KEY_PASSWORT, passwort);

         // Änderungen abspeichern
        editor.commit();
     }

    public int getBenutzerId() {
        return pref.getInt(KEY_NAME_ID,0);
    }

    public void setBenutzerId(int benutzerId) {
        // Daten speichern
        editor.putInt(KEY_NAME_ID, benutzerId);

        // Änderungen abspeichern
        editor.commit();
    }

    public String getBenutzername() {
        return pref.getString(KEY_NAME, "");
    }

    public void setBenutzername(String benutzerName) {
        // Daten speichern
        editor.putString(KEY_NAME, benutzerName);

        // Änderungen abspeichern
        editor.commit();
    }

    public String getPasswort() {
        return pref.getString(KEY_PASSWORT, "");
    }

    public void setPasswort(String passwort) {
        // Daten speichern
        editor.putString(KEY_PASSWORT, passwort);

        // Änderungen abspeichern
        editor.commit();
    }

     /**
     * Abgepseicherte Session Daten zurück geben
     * */
     public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // Daten schreiben
        user.put(KEY_NAME, pref.getString(KEY_NAME, ""));
        user.put(KEY_NAME_ID, ""+pref.getInt(KEY_NAME_ID, 0));
        user.put(KEY_PASSWORT, pref.getString(KEY_PASSWORT, ""));

         // Benutzer zurück liefern
        return user;
     }
    
     /**
     * Session löschen
     * */
     public void logoutUser(){
        // Alle Daten von Shared Preferences löschen
        editor.clear();
        editor.commit();
        
        // Nach dem Logout den Benutzer auf die StartActivity umleiten
        Intent i = new Intent(_context, MainActivity.class);
        // Andere Activities schließen
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        
        // Neue Flags setzen
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        
        // Starte MainActivity
        _context.startActivity(i);
     }
    
     /**
     * Login überprüfen
     * **/
     public boolean isLoggedIn(){
         String bid = ""+ getBenutzerId();
         if (!bid.isEmpty() && !getBenutzername().isEmpty() && !getPasswort().isEmpty()) return true;
         return false;
         //return pref.getBoolean(IS_LOGIN, false);
     }
}
