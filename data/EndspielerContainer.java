package data;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import store.ILeaderboardSpeicherung;
import store.LeaderboardSpeicherung;
import store.LoadSaveException;

public class EndspielerContainer implements IEndspielerContainer {
    private static EndspielerContainer unique = null;
    private ArrayList<IEndspieler> alleEndspieler = new ArrayList<>();
    private String[][] leaderboardArray = null;
    private static ILeaderboardSpeicherung instance = null;
    private EndspielerContainer() {}

    public static EndspielerContainer instance() {
        if (unique == null) {
            unique = new EndspielerContainer();
            try {
				instance = LeaderboardSpeicherung.instance();
			} catch (LoadSaveException e) {
				System.out.println(e.getMessage());
			}
        }
        return unique;
    }
    public void linkEndspieler(IEndspieler s) {
        if (!alleEndspieler.contains(s)) {
            alleEndspieler.add(s);
        } else {
            System.err.println("Es wurde versucht einen Endspieler zum Container hinzuzufügen, " +
                    "der bereits darin enthalten ist!");
        }
    }
    @Override
    public ArrayList<IEndspieler> getAlleEndspieler() {
        return alleEndspieler;
    }
    public boolean getIfLeaderboardInstanceIsNull() {
        return instance == null;
    }
    @Override
    public String[][] loadSortedLeaderboard() throws LoadSaveException {
        //falls bereits das Leaderboard geladen wurde, wird das zwischengespeicherte leaderboardArray zurückgegeben
    	if (this.leaderboardArray != null) return this.leaderboardArray;

		String[][] inhalt = new String[11][4];
		List<IEndspieler> sortList = instance.loadLeaderboard().getAlleEndspieler().stream()
				.sorted(Comparator.comparingInt(IEndspieler::getGesamtPunkte).reversed())
				.limit(10).toList();
        //Spaltennamen
		inhalt[0] = new String[]{"Rang","Datum","Name","Gesamtpunkte"};
        //Tabelleninhalt
		for (IEndspieler e : sortList) {
			int i = sortList.indexOf(e) + 1;
			inhalt[i][0] = "" + i;
			inhalt[i][1] = e.getDatum().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
			inhalt[i][2] = "" + e.getName();
			inhalt[i][3] = "" + e.getGesamtPunkte();
		}
		this.leaderboardArray = inhalt;
		return inhalt;
	}
    @Override
    public void addToLeaderboard(IEndspieler e) throws LoadSaveException {
        instance.addToLeaderboard(e);
    }
    @Override
    public void close() {
        instance.close();
    }
}


