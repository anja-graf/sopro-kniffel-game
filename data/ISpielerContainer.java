package data;

import store.LoadSaveException;

/**Alle Spieler werden im Spieler Container gespeichert und verwaltet*/
public interface ISpielerContainer extends Iterable<ISpieler> {
    /**Ruft saveUnfinishedGame von ISpielspeicherung auf*/
    void saveUnfinishedGame(String path) throws LoadSaveException;
    /**Ruft loadUnfinishedGame von ISpielspeicherung auf*/
    void loadUnfinishedGame(String path) throws LoadSaveException;
    /**Ruft saveGame von ISpielspeicherung auf*/
    void saveGame(String path) throws LoadSaveException;
    /**Gibt die Runde zurück*/
    int getRunde();
    /**Setzt die Runde auf den übergebenen Parameter*/
	void setRunde(int zaehler);
}
