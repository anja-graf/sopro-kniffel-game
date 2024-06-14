package store;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import data.Endspieler;
import data.EndspielerContainer;
import data.IEndspieler;
import data.IEndspielerContainer;
import java.sql.SQLException;

public class LeaderboardSpeicherung implements ILeaderboardSpeicherung {
	private Connection c = null;
	private static LeaderboardSpeicherung unique = null;
	
	private LeaderboardSpeicherung() throws LoadSaveException {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			c = DriverManager.getConnection("jdbc:derby:derbyDB;create=true","","");
		} catch (SQLException | ClassNotFoundException e) {
			throw new LoadSaveException("Etwas ist beim Verbindungsaufbau schiefgegangen, "
					+ "deswegen kann das Leaderboard nicht angezeigt werden!");
		}
	}
	public static LeaderboardSpeicherung instance() throws LoadSaveException {
		if (unique == null) {
            unique = new LeaderboardSpeicherung();
        }
        return unique;
	}
	
   @Override
    public void addToLeaderboard(IEndspieler e) {
    	String befehl = "INSERT INTO Leaderboard VALUES (DEFAULT,?,?,?)";
    	try (PreparedStatement p = c.prepareStatement(befehl)){
			p.setDate(1, Date.valueOf(e.getDatum()));
			p.setString(2, e.getName());
			p.setInt(3, e.getGesamtPunkte());
			p.executeUpdate();
			System.out.println("Das Hinzufuegen des Spielers " + e.getName() + " war erfolgreich");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
    }
    @Override
    public IEndspielerContainer loadLeaderboard() {
    	EndspielerContainer end = EndspielerContainer.instance();
		try (Statement st = c.createStatement()) {
			String befehl = "SELECT * FROM Leaderboard";
			ResultSet r = st.executeQuery(befehl);
			while(r.next()) {
				Endspieler e = new Endspieler(r.getDate("Date").toLocalDate(),r.getString("Name"),r.getInt("Points"));
				end.linkEndspieler(e);
			}
			System.out.println("Das Laden des Leaderboards aus der Datenbank war erfolgreich");
		} catch (SQLException e) {
			e.getStackTrace();
		}
		return end;
    }
    
	@Override
	public void close() {
		try {
			if (c != null) {
				c.close();
				System.out.println("Das Schließen der Datenbank war erfolgreich");
			}
		} catch (SQLException e) {
			System.err.println(e.getStackTrace());
		}
	}
	
	
	/*Die SQL Datenbank besteht aus folgender Tabelle:
	 * "CREATE TABLE Leaderboard(
	 * 		ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	 * 		Date DATE NOT NULL,
	 * 		Name VARCHAR(30) NOT NULL,
	 * 		Points INTEGER NOT NULL,
	 * 		PRIMARY KEY (ID)
	 * )";
	 */

}
