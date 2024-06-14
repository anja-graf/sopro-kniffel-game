package gui;

import javax.swing.JCheckBox;
import java.util.Random;

public class Wuerfelanimation extends Thread {
    @Override
    public void run() {
        //Während gewürfelt wird, soll kein Würfel behalten werden können
        // oder eine Punktekategorie ausgewählt werden können
    	Wuerfelflaeche.setWuerfelButtonEnabled(false);
    	for (Spielertabelle s : Hauptfenster.getSpielertabellen()) s.deactivateAll();

        //Würfelanimation durch Schlafenlegen des Threads
        for (int j = 0; j < 10; j++) {
            try {
                wuerfeln();
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.getStackTrace();
            }
        }
        //Reaktivieren der Würfelfläche und der CheckBoxen
        Wuerfelflaeche.setWuerfelButtonEnabled(true);
        for (JCheckBox ch : Wuerfelflaeche.getBehalteneWuerfel()) {
            ch.setEnabled(true);
        }

        //Spieler, der an der Reihe ist, wird wieder aktivert
        int spielerAnDerReihe = Hauptfenster.getRunde() % Hauptfenster.getSpielerAnzahl();
        for (Spielertabelle s : Hauptfenster.getSpielertabellen()) {
            //Alle anderen Spieler bleiben deaktiviert
            if (Hauptfenster.getSpielertabellen().indexOf(s) != spielerAnDerReihe) s.deactivateAll();
            else {
                s.activateSpieler();
                Hauptfenster.updateAnweisung(s.getSpielerName() +
                        " kann eine Punktekategorie auswählen oder das " +
                        (Hauptfenster.getZaehler() + 1) + ". Mal würfeln!");
                //Wenn es sein letzter Zug war, darf der Spieler nicht nochmal Würfeln oder Würfel behalten
                if (Hauptfenster.getZaehler() == 3) {
                	Wuerfelflaeche.setWuerfelButtonEnabled(false);
                    Wuerfelflaeche.setAllChEnabledFalse();
                    Hauptfenster.updateAnweisung(s.getSpielerName() +
                            " muss nun eine Punktekategorie auswählen!");
                }
            }
        }
    }
    private void wuerfeln() {
        //Hilfsoperation: würfelt und aktualisiert die WurfAnzeige
    	Random rdn = new Random();
    	for (JCheckBox ch : Wuerfelflaeche.getBehalteneWuerfel()) {
            ch.setEnabled(false);
            if (!ch.isSelected()) {
                Hauptfenster.setWurfAnzeige(rdn.nextInt(1, 7),Wuerfelflaeche.getBehalteneWuerfel().indexOf(ch));
            }
        }
        Wuerfelflaeche.updateWurfanzeige(Hauptfenster.getWurfAnzeige());
    }
}
