package gui;

import data.SpielerContainer;
import store.LoadSaveException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class Hauptfenster extends JFrame {
    private static int[] wurfAnzeige = {0,0,0,0,0};
    private static int zaehler, spielerAnzahl, runde = 0;
    private static ArrayList<Spielertabelle> alleSpielertabellen = new ArrayList<>();
    private static JLabel anweisung;
    protected Hauptfenster (int spielerAnzahl,boolean load) {
        super("Kniffel");
        
        SpielerContainer container = SpielerContainer.instance();
        Hauptfenster.runde = container.getRunde();
        Hauptfenster.spielerAnzahl = spielerAnzahl;

        //Spielanweisung zentriert im NORDEN
        JPanel panelNorth = new JPanel(new FlowLayout());
        anweisung = new JLabel("Drücke auf \"Würfeln\", um das Spiel zu starten!");
        if (load) anweisung.setText("Drücke auf \"Würfeln\", um das Spiel weiterzuspielen!");
        anweisung.setFont(new Font("Arial",Font.PLAIN,13));
        panelNorth.add(anweisung);
        this.add(panelNorth, BorderLayout.NORTH);

        //Tabelle mit Beschriftung im CENTER
        JPanel panelCenter = new JPanel(new GridLayout(1,2 + spielerAnzahl));
        new Beschriftung(panelCenter);

        //Die Spieler sollen rechts stehen
        for (int i = 0; i < spielerAnzahl; i++) {
            Spielertabelle s = new Spielertabelle(i,this,load);
            panelCenter.add(s);
            alleSpielertabellen.add(s);
        }
        this.add(panelCenter,BorderLayout.CENTER);

        //Würfelfäche im SÜDEN
        JPanel panelSouth = new Wuerfelflaeche();
        this.add(panelSouth, BorderLayout.SOUTH);
        this.add((new JLabel("   ")), BorderLayout.WEST);
        this.add((new JLabel("   ")), BorderLayout.EAST);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int i = JOptionPane.showOptionDialog(e.getWindow(),
                        "Wollen Sie wirklich das laufende Spiel beenden?", "Spiel beenden",
                       JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null,
                       new String[] {"Ja","Nein","Davor Abspeichern"},"Ja");
                if (i == 0) {
                   dispose();
                } else if (i == 2) {
                    if (zaehler != 0) {
                        JOptionPane.showMessageDialog(e.getWindow(), "Das Spiel kann nicht abgespeichert werden,"
                            + " weil der Zug von " + alleSpielertabellen.get(runde % spielerAnzahl).getSpielerName()
                            + " noch nicht beendet ist!", "Fehler",JOptionPane.ERROR_MESSAGE);
                    } else {
                        JFileChooser f = new JFileChooser();
                        if (f.showSaveDialog(e.getWindow()) == JFileChooser.APPROVE_OPTION) {
                            try {
                                container.setRunde(runde);
                                container.saveUnfinishedGame(f.getSelectedFile().getPath());
                                dispose();
                            } catch (LoadSaveException l) {
                                JOptionPane.showMessageDialog(e.getWindow(),
                                        "Das Spiel konnte nicht abgespeichert werden!",
                                        "Fehler",JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            }
        });
        this.pack();
        if (load) alleSpielertabellen.forEach(s -> s.deactivateAll());
        //Size von 2 Spielern - sonst wird Anweisungstext nicht ganz angezeigt
        if (spielerAnzahl == 1) this.setSize(478,541);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
    }

    //getter-Methoden
    protected static ArrayList<Spielertabelle> getSpielertabellen() {return alleSpielertabellen;}
    protected static Spielertabelle getSpielertabelle(int index) {return alleSpielertabellen.get(index);}
    protected static int getSpielerAnzahl() {
        return spielerAnzahl;
    }
    protected static int[] getWurfAnzeige() {
        return wurfAnzeige;
    }
    protected static int getRunde() {
        return runde;
    }
    protected static int getZaehler() {
        return zaehler;
    }

    //setter-Methoden
    protected static void updateAnweisung(String neuerText) {
        Hauptfenster.anweisung.setText(neuerText);
    }
    protected static void incrementRunde() {
        runde++;
    }
    protected static void setZaehler(int neuerWert) {
        zaehler =  neuerWert;
    }
    protected static void setWurfAnzeige(int[] wurf) {
       wurfAnzeige = wurf;
    }
    protected static void setWurfAnzeige(int augenzahl, int index) {
        wurfAnzeige[index] = augenzahl;
    }
}
