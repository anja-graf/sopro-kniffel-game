package store;

import data.ISpielerContainer;

/**ISpielspeicherung gibt die Methoden zur Speicherung eines kompletten Spiels -nicht nur der Gesamtpunktzahl vor*/
public interface ISpielspeicherung {
    /**Speichert das gesamte Spiel ab */
    void saveGame(ISpielerContainer con, String path) throws LoadSaveException;
    
    /**Falls ein Spiel geschlossen wird, kann es zuvor abgespeichert werden*/
    void saveUnfinishedGame(ISpielerContainer con,String path) throws LoadSaveException;
    
    /**Falls ein Spiel noch nicht beendet war und es vor dem Schließen gespeichert wurde,
     * kann es geladen und weitergespielt werden.*/
    void loadUnfinishedGame(ISpielerContainer con,String path)throws LoadSaveException;
   
}
