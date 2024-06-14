package gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import data.EndspielerContainer;
import store.LoadSaveException;

public class Leaderboard extends JDialog {
	
	protected Leaderboard(Spielende s) {
		super(s,"Leaderboard - TOP TEN",true);
		try {
			JTable spielerListe = new JTable(EndspielerContainer.instance().loadSortedLeaderboard(),
					new String[]{"Rang", "Datum", "Name", "Gesamtpunkte"});

			spielerListe.setEnabled(false);
			this.add(spielerListe, BorderLayout.CENTER);
		} catch (LoadSaveException l) {
			JOptionPane.showMessageDialog(this, l.getMessage(),"Fehler",JOptionPane.ERROR_MESSAGE);
		}
		this.setSize(450,213);
	
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				EndspielerContainer.instance().close();
				dispose();
			}
		});
		this.setVisible(true);
	}
}
