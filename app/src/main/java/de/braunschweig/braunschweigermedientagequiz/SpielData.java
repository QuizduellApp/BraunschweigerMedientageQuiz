package de.braunschweig.braunschweigermedientagequiz;

import java.io.Serializable;

/**
 * Daten zum Spiel werden hier gespeichert
 */
@SuppressWarnings("serial") // Kompiler Warnung wird unterdrückt
public class SpielData implements Serializable {
    private int benutzerId;
    private String benutzername;
    private String passwort;
    private int spielId;
    private int gegnerId;
    private int kategorieId;
    private int rundeId;
    private int rundeCount;
    private int frageAktuell;
    private int frage1Id;
    private int frage2Id;
    private int frage3Id;
    private int antwortFrage1;
    private int antwortFrage2;
    private int antwortFrage3;
    private int richtigeAntworten = 0;

    public void resetSpiel(){
        spielId = 0;
        gegnerId = 0;
        kategorieId = 0;
        rundeId = 0;
        rundeCount = 0;
        frageAktuell = 0;
        frage1Id = 0;
        frage2Id = 0;
        frage3Id = 0;
        antwortFrage1 = 0;
        antwortFrage2 = 0;
        antwortFrage3 = 0;
        richtigeAntworten = 0;
    }

    // Alle Daten zurück setzen
    public void resetSpielData(){
        benutzerId = 0;
        benutzername = "";
        passwort = "";
        spielId = 0;
        gegnerId = 0;
        kategorieId = 0;
        rundeId = 0;
        rundeCount = 0;
        frageAktuell = 0;
        frage1Id = 0;
        frage2Id = 0;
        frage3Id = 0;
        antwortFrage1 = 0;
        antwortFrage2 = 0;
        antwortFrage3 = 0;
        richtigeAntworten = 0;
    }

    public SpielData(int benutzerId){
        this.benutzerId = benutzerId;
    }

    public int getBenutzerId() {
        return benutzerId;
    }

    public void setBenutzerId(int benutzerId) {
        this.benutzerId = benutzerId;
    }

    public String getBenutzername() {
        return benutzername;
    }

    public void setBenutzername(String benutzername) {
        this.benutzername = benutzername;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public int getSpielId() {
        return spielId;
    }

    public void setSpielId(int spielId) {
        this.spielId = spielId;
    }

    public int getGegnerId() {
        return gegnerId;
    }

    public void setGegnerId(int gegnerId) {
        this.gegnerId = gegnerId;
    }

    public int getKategorieId() {
        return kategorieId;
    }

    public void setKategorieId(int kategorieId) {
        this.kategorieId = kategorieId;
    }

    public int getRundeId() {
        return rundeId;
    }

    public void setRundeId(int rundeId) {
        this.rundeId = rundeId;
    }

    public int getRundeCount() {
        return rundeCount;
    }

    public void setRundeCount(int rundeCount) {
        this.rundeCount = rundeCount;
    }

    public int getFrageAktuell() {
        return frageAktuell;
    }

    public void setFrageAktuell(int frageAktuell) {
        this.frageAktuell = frageAktuell;
    }

    public int getFrage1Id() {
        return frage1Id;
    }

    public void setFrage1Id(int frage1Id) {
        this.frage1Id = frage1Id;
    }

    public int getFrage2Id() {
        return frage2Id;
    }

    public void setFrage2Id(int frage2Id) {
        this.frage2Id = frage2Id;
    }

    public int getFrage3Id() {
        return frage3Id;
    }

    public void setFrage3Id(int frage3Id) {
        this.frage3Id = frage3Id;
    }

    public int getAntwortFrage1() {
        return antwortFrage1;
    }

    public void setAntwortFrage1(int antwortFrage1) {
        this.antwortFrage1 = antwortFrage1;
    }

    public int getAntwortFrage2() {
        return antwortFrage2;
    }

    public void setAntwortFrage2(int antwortFrage2) {
        this.antwortFrage2 = antwortFrage2;
    }

    public int getAntwortFrage3() {
        return antwortFrage3;
    }

    public void setAntwortFrage3(int antwortFrage3) {
        this.antwortFrage3 = antwortFrage3;
    }

    public int getRichtigeAntworten() {
        return richtigeAntworten;
    }

    public void setRichtigeAntworten(int richtigeAntworten) {
        this.richtigeAntworten = richtigeAntworten;
    }
}