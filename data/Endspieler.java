package data;

import java.time.LocalDate;

public class Endspieler implements IEndspieler {
	private LocalDate datum = LocalDate.now();
	private String name;
	private int gesamtPunkte;
	
	public Endspieler (ISpieler s) {
		this.name = s.getName();
		this.gesamtPunkte = s.getPunkt(19);
	}
	public Endspieler (LocalDate date, String name, int gesamtPunkte) {
		this.datum = date;
		this.name = name;
		this.gesamtPunkte = gesamtPunkte;
	}
	
	public int getGesamtPunkte() {
		return gesamtPunkte;
	}
	public String getName() {
		return name;
	}
	public LocalDate getDatum() {
		return datum;
	}
}
