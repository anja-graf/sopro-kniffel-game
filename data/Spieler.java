package data;

import java.util.Arrays;

public class Spieler implements ISpieler {
    private int[] punkte = new int[20];
    private String name;
    private boolean[] enabledStatus = new boolean[20];
    public Spieler(String name) {
        this.name = name;
        Arrays.fill(punkte, 0);
        
    }
    public int getPunkt(int index){return punkte[index];}
    public String getStrPunkt(int index){return String.valueOf(punkte[index]);}
    public int[] getPunkte(){return punkte;}
    public String getName(){return name;}
    public boolean getEnabledStatus(int index) {return enabledStatus[index];}
    public void setPunkt(int punkt, int index) {
        punkte[index] = punkt;
    }
    public void setName(String name) throws IllegalSpielerNameException {
    	if (!checkName(name)) throw new IllegalSpielerNameException();
    	this.name = name;
    }
    public void setEnabledStatus(boolean bol, int index) {
        enabledStatus[index] = bol;
    }
    public boolean checkName(String name) {
    	if (name.length() <= 15 && name.matches("[a-zA-Z_0-9]+[\s]?[a-zA-Z_0-9]*")) return true;
    	return false;
    }
}
