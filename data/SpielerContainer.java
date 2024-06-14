package data;

import store.ISpielspeicherung;
import store.LoadSaveException;
import store.Spielspeicherung;

import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class SpielerContainer implements Iterable<ISpieler>, Serializable, ISpielerContainer {
    private static SpielerContainer unique = null;
    private ArrayList<ISpieler> alleSpieler = new ArrayList<>();
    private int runde = 0;
    private SpielerContainer() {}
    public static SpielerContainer instance() {
        if (unique == null) {
            unique = new SpielerContainer();
        }
        return unique;
    }
    public void linkSpieler(ISpieler s) {
        if (!alleSpieler.contains(s)) {
            alleSpieler.add(s);
        } else {
            throw new IllegalArgumentException("Programmfehler!");
        }
    }
    public ArrayList<ISpieler> getAlleSpieler() {
        return alleSpieler;
    }

    @Override
    public Iterator<ISpieler> iterator() {
        return this.alleSpieler.iterator();
    }
	@Override
	public void saveGame(String path) throws LoadSaveException {
        Spielspeicherung.instance().saveGame(this,path);
    }
	@Override
	public void loadUnfinishedGame(String path) throws LoadSaveException {
		Spielspeicherung.instance().loadUnfinishedGame(this, path);
	}
	@Override
	public void saveUnfinishedGame(String path) throws LoadSaveException {
		Spielspeicherung.instance().saveUnfinishedGame(this,path);
	}
	public int getRunde() {
		return runde;
	}
	public void setRunde(int zaehler) {
		this.runde = zaehler;
	}


}
