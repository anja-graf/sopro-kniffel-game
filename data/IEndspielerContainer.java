package data;

import java.util.ArrayList;
import store.LoadSaveException;

/**Alle Endspieler werden mit dem Endspieler Container verlinkt und verwaltet*/
public interface IEndspielerContainer {
	/**Lädt das Leaderboard durch Aufruf der loadLeaderboard-Methode von ILeaderboardSpeicherung,
	 *sortiert es nach höchster Punktzahl und gibt die Top-Ten zurück*/
	String[][] loadSortedLeaderboard() throws LoadSaveException;
	/**Ruft die addToLeaderboard-Methode von ILeaderboardSpeicherung auf*/
	void addToLeaderboard(IEndspieler e) throws LoadSaveException;
	/**Gibt alle gelinkten Endspieler zurück*/
	ArrayList<IEndspieler> getAlleEndspieler();
	/**Linkt einen übergebenen Endspieler mit dem Leaderboard*/
	void linkEndspieler(IEndspieler s);
	/**Ruft die close-Methode von ILeaderboardSpeicherung auf*/
	void close();
}
