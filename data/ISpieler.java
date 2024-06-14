package data;

/**Je nach gewählter Spieleranzahl gibt es ein bis sechs ISpieler pro Spiel.
 * ISpieler verwaltet Attribute über den jeweiligen Spieler*/
public interface ISpieler {
    /**Gibt den Punkt einer spezifischen Kategorie eines Spielers zurück*/
    int getPunkt(int index);
    /**Gibt den Punkt einer spezifischen Kategorie eines Spielers als String zurück*/
    String getStrPunkt(int index);
    /**Gibt die Gesamtpunktzahl eines Spielers zurück*/
    int[] getPunkte();
    /**Gibt den Namen eines Spielers zurück*/
    String getName();
    /**Gibt an, ob eine spezifische Kategorie eines Spielers bereits geklickt wurde. Bei Gesamtfeldern immer false*/
    boolean getEnabledStatus(int index);
    /**Setzt den Punkt einer spezifischen Kategorie eines Spielers*/
    void setPunkt(int punkt, int index);
    /**Setzt den Namen eines Spielers*/
    void setName(String name) throws IllegalSpielerNameException;
    /**Setzt eine spezifische Kategorie eines Spielers als bereits geklickt oder noch nicht geklickt.
     * Bei Gesamtfeldern sollte nie true übergeben werden*/
    void setEnabledStatus(boolean bol, int index);
}
