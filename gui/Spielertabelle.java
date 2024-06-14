package gui;

import data.ISpieler;
import data.IllegalSpielerNameException;
import data.KniffelPunkte;
import data.Spieler;
import data.SpielerContainer;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JFrame;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Spielertabelle extends JPanel implements ActionListener {
    private SpielerContainer container = SpielerContainer.instance();
    private ArrayList<JButton> alleJButtons = new ArrayList<>();
    private JTextField namensfeld;
    private JFrame hauptfenster;
    private ISpieler spieler;
    protected Spielertabelle(int j, JFrame hauptfenster, boolean load) {
        super();
        this.setLayout(new GridLayout(21,1));
        this.hauptfenster = hauptfenster;
        //Je nachdem, ob ein gespeichertes Spiel geladen wurde, werden die Attribute der Spieler wiederhergestellt
        if (!load) {
		    this.spieler = new Spieler("Spieler " + (j + 1));
		    container.linkSpieler(spieler); 
        } else {
        	this.spieler = container.getAlleSpieler().get(j);
        }
        namensfeld = new JTextField(spieler.getName());
        namensfeld.setToolTipText("Ändere meinen Namen!");
        this.add(namensfeld);
        
	    for (int i = 0; i < 20; i++) {
	        JButton b = new JButton();
	        b.addActionListener(this);
	        b.setBackground(new Color(255, 255, 255));
	        b.setEnabled(false);
            //Alle Felder bis auf die Gesamtpunkte wurden noch nicht geklickt;
	        if (!load) spieler.setEnabledStatus((i < 6 || i > 9) && (i < 17),i);
	        this.add(b);
	        alleJButtons.add(b);
	    }
    }

    protected String getSpielerName() {
    	try {
			spieler.setName(namensfeld.getText());
		} catch (IllegalSpielerNameException e1) {
			namensfeld.setText(spieler.getName());
			System.out.println("Falsches Format, Name wurde zurückgesetzt");
		}
        return spieler.getName();
    }
    
    protected void deactivateAll() {
        for (JButton b : alleJButtons) {
            int row = alleJButtons.indexOf(b);
            b.setText(spieler.getStrPunkt(row));
            if (row == 9) b.setText("");
            b.setEnabled(false);
            b.setBackground(new Color(255, 255, 255));
            if (spieler.getEnabledStatus(row) && spieler.getPunkt(row) == 0) b.setText("");
        }
    }

    protected void activateSpieler() {
        for (JButton b : alleJButtons) {
            int row = alleJButtons.indexOf(b);
            if (spieler.getEnabledStatus(row)) {
                //Die Punkte-Kategorie wurde noch nicht gewählt
                b.setEnabled(true);
                b.setText(String.valueOf(KniffelPunkte.berechnen(Hauptfenster.getWurfAnzeige())[row]));
                b.setBackground(new Color(189, 205, 243));
            } else {
                //Die Punkte-Kategorie wurde bereits gewählt oder ist ein Gesamtpunktefeld
                b.setEnabled(false);
                b.setText(spieler.getStrPunkt(row));
                if (row == 9) b.setText("");
            }
            //Der Spieler hat bereits ein Kniffel gewürfelt (Joker)
            if (KniffelPunkte.berechnen(Hauptfenster.getWurfAnzeige())[15] == 50
                    && spieler.getPunkt(15) != 0) {
                b.setText(String.valueOf(KniffelPunkte.mehrmalsKniffel()[row]));
                if (!spieler.getEnabledStatus(row)) b.setText(spieler.getStrPunkt(row));
                if (row == 15) b.setText(String.valueOf(spieler.getPunkt(15) + 50));
                if (row == 9) b.setText("");
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //Zug des Spielers ist beendet, da er eine Punktekategorie ausgewählt hat
        Hauptfenster.updateAnweisung(Hauptfenster.getSpielertabelle((Hauptfenster.getRunde() + 1)
                        % Hauptfenster.getSpielerAnzahl()).getSpielerName() + " darf als nächstes würfeln!");
        Wuerfelflaeche.setAllChEnabledFalse();

        //das ausgewählte Punktefeld wird gespeichert
        int row = alleJButtons.indexOf(e.getSource());
        spieler.setEnabledStatus(false,row);
        if (spieler.getPunkt(15) == 0) {
            spieler.setPunkt(KniffelPunkte.berechnen(Hauptfenster.getWurfAnzeige())[row],row);
        } else {
            spieler.setPunkt(KniffelPunkte.mehrmalsKniffel()[row],row);
            spieler.setPunkt(spieler.getPunkt(15) + 50, 15);
        }
        KniffelPunkte.updateGesamtFelder(spieler.getPunkte());

        //Vorbereitung auf nächsten Spieler
        Hauptfenster.incrementRunde();
        Hauptfenster.setZaehler(0);
        deactivateAll();
        Wuerfelflaeche.setWuerfelButtonEnabled(true);

        //Die Würfel werden zurückgesetzt
        Hauptfenster.setWurfAnzeige(new int[]{0,0,0,0,0});
        Wuerfelflaeche.updateWurfanzeige(new int[]{0,0,0,0,0});

        //Der Spielername wird aus dem Textfeld übernommen
        try {
			this.spieler.setName(getSpielerName());
		} catch (IllegalSpielerNameException ill) {
            //Da Name bereits in getSpielerName überprüft wird, kann hier keine Exception mehr vorkommen
            System.err.println(ill.getMessage());
		}

        //Es wird geprüft, ob alle Spieler alle Züge gemacht haben
        if (Spielende.checkSpielende()) {
            new Spielende();
            hauptfenster.dispose();
        }
    }
}
