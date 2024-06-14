package store;

import data.IEndspieler;
import data.IEndspielerContainer;

/**ILeaderboardSpeicherung gibt die Methoden für den Zugriff auf eine Datenbank vor*/
public interface ILeaderboardSpeicherung {

	/** Fügt inkrementell Endspieler zum Leaderboard hinzu*/
    void addToLeaderboard(IEndspieler e);
    
    /**Lädt alle Einträge des Leaderboards aus der Datenbank*/
    IEndspielerContainer loadLeaderboard();
    
    /**Schließt die Speicherressouce*/
    void close();
}
