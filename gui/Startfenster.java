package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import data.SpielerContainer;
import store.LoadSaveException;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Dimension;


public class Startfenster extends JFrame {
    protected Startfenster () {
        super("Willkommen!");
        //Kniffel Text zentriert im Norden
        JLabel kniffel = new JLabel("Kniffel");
        kniffel.setFont(new Font(Font.SERIF,Font.BOLD,50));
        kniffel.setPreferredSize(new Dimension(300,100));
        kniffel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(kniffel, BorderLayout.NORTH);

        //Spieleranzahl im Süden
        JPanel panelSouth = new JPanel(new GridLayout(2,1));
        JPanel anzahl = new JPanel(new FlowLayout());
        anzahl.add(new JLabel("Spieleranzahl: "));
        JTextField spieler = new JTextField(5);
        spieler.setToolTipText("Spieleranzahl hier eingeben");
        anzahl.add(spieler);

        //verschieden Buttons im Süden
        JPanel buttons = new JPanel(new FlowLayout());
        JButton newGame = new JButton("Neues Spiel starten");
        newGame.addActionListener(e -> {
            try {
                new Hauptfenster(checkSpieleranzahl(spieler.getText()),false);
                this.dispose();
            } catch (IllegalNumberOfPlayersException i) {
                JOptionPane.showMessageDialog(this, "Bitte Spieleranzahl zwischen 1 und 6 eingeben!",
                		"Ungültige Eingabe",JOptionPane.ERROR_MESSAGE);
                spieler.setText("");
            }
        });

        JButton loadGame = new JButton("Angefangenes Spiel laden");
        loadGame.addActionListener(e -> {
        	JFileChooser f = new JFileChooser();
        	if (f.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        		try {
        			SpielerContainer s = SpielerContainer.instance();
					s.loadUnfinishedGame(f.getSelectedFile().getPath());
					new Hauptfenster(s.getAlleSpieler().size(),true);
					this.dispose();
				} catch (LoadSaveException l) {
					JOptionPane.showMessageDialog(this, "Das Spiel konnte nicht geladen werden!",
	                		"Fehler",JOptionPane.ERROR_MESSAGE);
				}
        	}
        });
        buttons.add(loadGame);
        buttons.add(newGame);
        
        panelSouth.add(anzahl);
        panelSouth.add(buttons);

        this.add(panelSouth, BorderLayout.SOUTH);
        this.pack();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    private int checkSpieleranzahl(String eingabe) throws IllegalNumberOfPlayersException {
        //Hilfsmethode zur Überprüfung, ob Spieleranzahl zwischen 1 und 6
        try {
			int anzahl = Integer.parseInt(eingabe);
		    if (anzahl < 1 || anzahl > 6) {
		        throw new IllegalNumberOfPlayersException();
		    }
		    return anzahl;
        } catch (NumberFormatException n) {
        	throw new IllegalNumberOfPlayersException();
        }
    }

    public static void main(String[] args) {
        new Startfenster();
    }
}
