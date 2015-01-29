package de.braunschweig.braunschweigermedientagequiz;

import java.io.Serializable;

/**
 * Daten zum Spiel werden hier gespeichert
 */
@SuppressWarnings("serial") // Kompiler Warnung wird unterdrückt
public class SpielData implements Serializable {

    private int kategorieId;
    private int spielId;
    private int frage1Id;
    private int frage2Id;
    private int frage3Id;
    private int antwortFrage1;
    private int antwortFrage2;
    private int antwortFrage3;

    public SpielData(int spielId, int kategorieId, String kategorie1, String kategorie2){
        this.spielId = spielId;
        this.kategorieId = kategorieId;

    }

}