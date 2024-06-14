package gui;

import data.SpielerContainer;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.AbstractButton;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;

public class Wuerfelflaeche extends JPanel {
    private static JButton wuerfeln = new JButton();
    private static ArrayList<JTextField> wuerfel = new ArrayList<>();
    private static ArrayList<JCheckBox> behalteneWuerfel = new ArrayList<>();
    private int x = 100, y = 100;
    private static String isSelected = "";
    protected Wuerfelflaeche () {
        this.setLayout(new FlowLayout());
        this.setSize(300,300);

        JPanel wurf = new JPanel(new GridLayout(2,10));
        this.neueWurfanzeige(wurf,Hauptfenster.getWurfAnzeige());

        wuerfeln = new JButton("Würfeln");
        wuerfeln.setToolTipText("Klick mich, um zu würfeln!");
        wuerfeln.addActionListener(e -> {
        	int i = OK_OPTION;
            if ((int) behalteneWuerfel.stream().filter(AbstractButton::isSelected).count() == 5) {
                i = JOptionPane.showConfirmDialog(wuerfeln,
                        "Sie wollen würfeln, obwohl sie fünf Würfel behalten wollen!", "Fragwürdige Auswahl der behaltenen Würfel", OK_CANCEL_OPTION);
            }
            if (i == OK_OPTION) {
                Hauptfenster.setZaehler(Hauptfenster.getZaehler() + 1);
                Wuerfelanimation neu = new Wuerfelanimation();
                neu.start();
            }
        });
        
        this.add(wurf);
        this.add(wuerfeln);
    }
    protected static void setAllChEnabledFalse() {
        for (JCheckBox ch: behalteneWuerfel) {
            ch.setEnabled(false);
            ch.setSelected(false);
        }
    }
    private void neueWurfanzeige(JPanel wurf, int[] wurfAnzeige) {
        for (int i = 0; i < 5; i++) {
            //farbige Würfel zentriert und mit Abstand zueinander
            JTextField text = new JTextField("" + wurfAnzeige[i]);
            text.setHorizontalAlignment(SwingConstants.CENTER);
            text.setFont(new Font(Font.SERIF,Font.BOLD, 15));
            text.setEditable(false);
            text.setBackground(new Color(189, 205, 243));
            wuerfel.add(text);
            wurf.add(text);
            wurf.add(new JLabel(""));
        }
        for (int i = 0; i < 5; i++) {
            //CheckBoxen, um zu markieren, ob Würfel behalten werden soll
            JCheckBox ch = new JCheckBox();
            behalteneWuerfel.add(ch);
            ch.setEnabled(false);
            ch.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved (MouseEvent e) {
                    //Falls die Mouse über einer CheckBox hovert, soll ein Text angezeigt werden
                    if (e.getComponent().isEnabled()) {
                        isSelected = (((JCheckBox) e.getComponent()).isSelected()) ? "nicht " : "";
                        x = e.getX();
                        y = e.getY();
                        repaint();
                    }
                }
            });
            ch.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseExited (MouseEvent e) {
                    //Falls die Mouse nicht mehr über einer CheckBox hovert, soll kein Text angezeigt werden
                    if (e.getComponent().isEnabled()) {
                        x = 100;
                        y = 100;
                        //Text ausserhalb von Komponente
                        repaint();
                    }
                }
            });
            wurf.add(ch);
            wurf.add(new JLabel(""));
        }
    }
    protected static void updateWurfanzeige(int[] wurfAnzeige) {
        for (int i = 0; i < 5; i++) {
            wuerfel.get(i).setText("" + wurfAnzeige[i]);
        }
    }
    protected static void setWuerfelButtonEnabled(boolean b) {
        wuerfeln.setEnabled(b);
    }
    protected static ArrayList<JCheckBox> getBehalteneWuerfel() {
        return behalteneWuerfel;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int xVerschiebung = 0;
        switch (Hauptfenster.getSpielerAnzahl()) {
            //Je nach Spieleranzahl verschiebt sich die Wuerfelflaeche nach rechts
            case (1), (2) -> xVerschiebung += 30 * 2;
            case (3) -> xVerschiebung += 40 * 3;
            case (4) -> xVerschiebung += 45 * 4;
            case (5) -> xVerschiebung += 50 * 5;
            case (6) -> xVerschiebung += 57 * 5;
        }
        g.drawString("Klick mich, um den Würfel " + isSelected + "zu behalten!",
                x + xVerschiebung,y + 26);
    }
}