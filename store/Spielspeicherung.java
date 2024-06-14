package store;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import store.ISpielspeicherung;
import data.ISpieler;
import data.ISpielerContainer;
import data.Spieler;
import data.SpielerContainer;

import javax.swing.*;

public class Spielspeicherung implements ISpielspeicherung {


	private static Spielspeicherung unique = null;
	private String filename;
	
	public Spielspeicherung() {
	}

	public static Spielspeicherung instance() {
		if (unique == null) {
			unique = new Spielspeicherung();
		}
		return unique;
	}
	@Override
	public void saveGame(ISpielerContainer con, String path) throws LoadSaveException {
		try (PrintWriter w = new PrintWriter(new File(path))) {
			String[] labelsKategorien = {"1er\t\t\t","2er\t\t\t","3er\t\t\t","4er\t\t\t","5er\t\t\t",
					"6er\t\t\t","Gesamt\t\t\t","Bonus bei mind. 63\t","Gesamt oberer Teil\t","",
					"Dreierpasch\t\t","Viererpasch\t\t","Full House\t\t","Kleine Straße\t\t","Große Straße\t\t",
					"Kniffel\t\t\t","Chance\t\t\t","Gesamt unterer Teil\t","Gesamt oberer Teil\t","Endsumme\t\t"};
			String[] labelsPunkte = {"nur Einser zählen\t\t","nur Zweier zählen\t\t","nur Dreier zählen\t\t",
					"nur Vierer zählen\t\t","nur Fünfer zählen\t\t","nur Sechser zählen\t\t","------->\t\t\t",
					"plus 35 Punkte\t\t\t","------->\t\t\t","","alle Augen zählen\t\t","alle Augen zählen\t\t",
					"25 Punkte\t\t\t","30 Punkte\t\t\t","40 Punkte\t\t\t","50 Punkte\t\t\t","alle Augen zählen\t\t",
					"------->\t\t\t","------->\t\t\t","------->\t\t\t"};
			w.println("Kniffelspiel vom " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
			w.print("\t\t\t\t\t\t\t");
			con.forEach(e -> w.print(e.getName() + "\t\t"));
			w.println();
			for (int i = 0; i < 20; i++) {
				w.print(labelsKategorien[i] + labelsPunkte[i]);
				for (ISpieler s: con) {
					if (i == 9) w.print("");
					else w.print(s.getPunkt(i) + "\t\t");
				}
				w.println();
			}

		} catch (FileNotFoundException f) {
			throw new LoadSaveException(f.getMessage());	
		}
	}

	@Override
	public void saveUnfinishedGame(ISpielerContainer con, String path) throws LoadSaveException {
		File f = new File(path);
		try (PrintWriter w = new PrintWriter(f)) {
			w.println("startOfKniffelGame");
			for (ISpieler s : con) {
				w.println("Spieler");
				w.println(s.getName());
				for (int i = 0; i < 20; i++) {
					w.println(s.getPunkt(i));
					w.println(s.getEnabledStatus(i));
				}
			}
			w.println("end");
			w.println(con.getRunde());
			f.setReadOnly();
		} catch (FileNotFoundException ex) {
			throw new LoadSaveException(ex.getMessage());
		}
	}

	@Override
	public void loadUnfinishedGame(ISpielerContainer con,String path)throws LoadSaveException {
		try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))))) {
			String line;
			if (!r.readLine().equals("startOfKniffelGame")) throw new LoadSaveException("Falsches Format!");
			while(!(line = r.readLine()).equals("end")) {
				if (!line.equals("Spieler")) throw new LoadSaveException("Falsches Format!");
				ISpieler temp = new Spieler(r.readLine());
				for (int i = 0; i < 20; i++) {
					temp.setPunkt(Integer.parseInt(r.readLine()),i);
					temp.setEnabledStatus(Boolean.parseBoolean(r.readLine()), i);
				}
				SpielerContainer.instance().linkSpieler(temp);
			}
			SpielerContainer.instance().setRunde(Integer.parseInt(r.readLine()));
		} catch (IOException | NumberFormatException e) {
			throw new LoadSaveException(e.getMessage());
		}
	}

}
