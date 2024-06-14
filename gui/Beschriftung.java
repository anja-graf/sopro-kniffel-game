package gui;

import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.Color;

public class Beschriftung {
    protected Beschriftung (JPanel panelCenter) {
        //Die Punkte Kategorien sollen links stehen
        JPanel links = new JPanel(new GridLayout(21,1));
        String[] labelsKategorien = {" ","1er","2er","3er","4er","5er","6er","Gesamt",
                "Bonus bei mind. 63","Gesamt oberer Teil","","Dreierpasch",
                "Viererpasch","Full House","Kleine Straße","Große Straße",
                "Kniffel","Chance","Gesamt unterer Teil","Gesamt oberer Teil",
                "Endsumme"};

        //Die Punkteverteilung soll in der Mitte stehen
        JPanel mitte = new JPanel(new GridLayout(21,1));
        String[] labelsPunkte = {" ","nur Einser zählen","nur Zweier zählen","nur Dreier zählen",
                "nur Vierer zählen","nur Fünfer zählen","nur Sechser zählen","--->","plus 35",
                "--->","","alle Augen zählen","alle Augen zählen","25 Punkte","30 Punkte",
                "40 Punkte","50 Punkte","alle Augen zählen","--->","--->","--->"};

        //Beides wird uneditierbar zum panelCenter hinzugefügt
        for (int i = 0; i < 21; i++) {
            JTextField text1 = new JTextField(labelsKategorien[i]);
            JTextField text2 = new JTextField(labelsPunkte[i]);
            text1.setEditable(false);
            text2.setEditable(false);
            if (i != 0) {
                text1.setBackground(new Color(255, 255, 255));
                text2.setBackground(new Color(255, 255, 255));
            }
            links.add(text1);
            mitte.add(text2);
        }
        panelCenter.add(links);
        panelCenter.add(mitte);
    }
}
