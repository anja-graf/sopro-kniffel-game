package gui;

import data.EndspielerContainer;
import data.SpielerContainer;
import data.Endspieler;
import data.ISpieler;
import store.LoadSaveException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.util.Comparator;
import java.util.List;

public class Spielende extends JFrame {
    protected Spielende() {
        super("Spielende");
        SpielerContainer.instance().getAlleSpieler().forEach(s -> {
        	try {
				EndspielerContainer.instance().addToLeaderboard(new Endspieler(s));
			} catch (LoadSaveException | NullPointerException e) {
                //Da der Fehler beim Verbindungsaufbau während eines impliziten Updates aufgetreten ist,
                // soll der Benutzer erst beim aktiven Aufrufen des Leaderboards informiert werden.
                System.err.println(e.getMessage());
			}
        });
        
        //Schriftzug im Norden
        JLabel ende = new JLabel("Das Spiel ist zu Ende!");
        ende.setFont(new Font(Font.SERIF,Font.BOLD,20));
        ende.setPreferredSize(new Dimension(300,100));
        ende.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(ende, BorderLayout.NORTH);

        //Rangliste des aktuellen Spiels im Center
        JPanel spielLeaderboard = new JPanel(new GridLayout(Hauptfenster.getSpielerAnzahl() + 1,3));
        spielLeaderboard.add(new JLabel("Platz"));
        spielLeaderboard.add(new JLabel("Spieler"));
        spielLeaderboard.add(new JLabel("Punkte"));
        List<ISpieler> sortedList = SpielerContainer.instance().getAlleSpieler().stream()
				.sorted(Comparator.comparingInt(c -> ((ISpieler) c).getPunkt(19)).reversed()).toList();
        for (int i = 0; i < Hauptfenster.getSpielerAnzahl(); i++) {
            spielLeaderboard.add(new JLabel("" + (i + 1)));
            spielLeaderboard.add(new JLabel("" + sortedList.get(i).getName()));
            spielLeaderboard.add(new JLabel("" + sortedList.get(i).getPunkt(19)));
        }
        this.add((new JLabel("   ")), BorderLayout.WEST);
        this.add((new JLabel("   ")), BorderLayout.EAST);
        this.add(spielLeaderboard,BorderLayout.CENTER);

        //verschiedene Buttons im Süden
        JPanel south = new JPanel(new FlowLayout());

        JButton save = new JButton("Spiel speichern");
        save.setToolTipText("speichert das aktuelle Spiel");
        save.addActionListener(e -> {
        	JFileChooser j = new JFileChooser();
        	if (j.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        		try {
					SpielerContainer.instance().saveGame(j.getSelectedFile().getPath());
				} catch (LoadSaveException l) {
					JOptionPane.showMessageDialog(this, "Das Spiel konnte nicht gespeichert werden!",
	                		"Fehler",JOptionPane.ERROR_MESSAGE);
				}
        	}
        });
        south.add(save);
        
        JButton leaderboard = new JButton("Leaderboard");
        leaderboard.setToolTipText("öffnet das Leaderboard");
        leaderboard.addActionListener(e -> {
            if (EndspielerContainer.instance().getIfLeaderboardInstanceIsNull()) {
                JOptionPane.showMessageDialog(this, "Etwas ist beim Verbindungsaufbau schiefgegangen, " +
                                "deswegen kann das Leaderboard nicht angezeigt werden!", "Fehler",JOptionPane.ERROR_MESSAGE);
            } else {
                new Leaderboard(this);
            }
        });
        south.add(leaderboard);

        JButton beenden = new JButton("Spiel beenden");
        beenden.setToolTipText("beendet das aktuelle Spiel");
        beenden.addActionListener(e -> dispose());
        south.add(beenden);

        this.add(south,BorderLayout.SOUTH);
        this.setVisible(true);
        this.pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    protected static boolean checkSpielende() {
        for (int i = 0; i < 20; i++) {
            if (SpielerContainer.instance().getAlleSpieler().get(Hauptfenster.getSpielerAnzahl() - 1)
                    .getEnabledStatus(i)) return false;
        }
        return true;
    }
}
