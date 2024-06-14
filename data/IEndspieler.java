package data;

import java.time.LocalDate;
/**Endspieler sind Spieler nach einem beendeten Spiel, die alle Punkte-Kategorien einmal ausgewählt haben*/
public interface IEndspieler {
	/**Gibt die Gesamtpunktzahl eines Endspielers zurück*/
	int getGesamtPunkte();
	/**Gibt den Namen eines Endspielers zurück*/
	String getName();
	/**Gibt das Datum, an dem das Spiel stattgefunden hat, zurück*/
	LocalDate getDatum();
}
