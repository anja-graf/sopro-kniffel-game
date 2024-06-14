package data;

import java.util.Arrays;
/**KniffelPunkte ist für die Berechnung der Punkte zuständig*/
public interface KniffelPunkte {

    /**Berechnet für alle Kategorien des Kniffelspiels die jeweilige Punktevergabe und gibt diese als Array zurück*/
    static int[] berechnen(int[] wurf) {
        int[] punkte = new int[20];
        Arrays.fill(punkte,0);
        for (int i = 0; i < 6; i++) {
            punkte[i] = SummeEinerAugenzahl(wurf, i + 1);
        }
        punkte[16] = Chance(wurf);
        UnGleicheAugenzahlen(wurf,punkte);
        return punkte;
    }
    /**Berechnet die Gesamtfelder, also Felder, bei denen die Punkte aus anderen Tabellen zusammenaddiert werden,
     * und updatet diese im übergebenen Array */

    static void updateGesamtFelder(int[] spielerPunkte) {
    	//Gesamter oberer Teil
        spielerPunkte[6] = Arrays.stream(spielerPunkte,0,6).sum();
        //Bonus
        spielerPunkte[7] = (spielerPunkte[6] >= 63) ? 35 : 0;
        //Gesamt oberer Teil mit Bonus
        spielerPunkte[8] = spielerPunkte[6] + spielerPunkte[7];
        //Gesamt unterer Teil
        spielerPunkte[17] = Arrays.stream(spielerPunkte,10,17).sum();
        spielerPunkte[18] = spielerPunkte[8];
        //Endsumme
        spielerPunkte[19] = spielerPunkte[8] + spielerPunkte [17];
    }
    private static int SummeEinerAugenzahl(int[] wurf, int augenzahl) {
        return Arrays.stream(wurf).filter(i -> i == augenzahl).sum();
    }
    private static int Chance(int[] wurf) {
        return Arrays.stream(wurf).sum();
    }

    private static void UnGleicheAugenzahlen(int[] wurf, int[] punkte) {
        int[] augenzahlen = Arrays.stream(wurf).distinct().toArray();
        Arrays.sort(augenzahlen);
        int variations = augenzahlen.length;
        int[] copy = Arrays.copyOf(wurf,5);
        Arrays.sort(copy);

        if (variations >= 4) {
            String s = Arrays.toString(augenzahlen);
            //Große Straße
            if (s.contains("1, 2, 3, 4, 5") || s.contains("2, 3, 4, 5, 6")) punkte[14] = 40;

            //Kleine Straße
            if (s.contains("1, 2, 3, 4") || s.contains("2, 3, 4, 5") || s.contains("3, 4, 5, 6")) punkte[13] = 30;
        } else {
            if (variations == 1) {
                //erstes Kniffel
                punkte[15] = 50;
                punkte[10] = copy[0] * 5;
                punkte[11] = copy[0] * 5;
                //ist laut Regelwerk kein FullHouse
            } else if (variations == 2) {
                int x = augenzahlen[0];
                int y = augenzahlen[1];
                String s = Arrays.toString(copy);
                if (s.contains(x + ", " + x + ", " + x + ", " + x)) {
                    //Viererpasch und Dreierpasch
                    punkte[11] = x * 4 + y;
                    punkte[10] = x * 4 + y;
                } else if (s.contains(y + ", " + y + ", " + y + ", " + y)){
                    punkte[11] = y * 4 + x;
                    punkte[10] = y * 4 + x;
                } else {
                    //FullHouse
                    if (copy[0] == copy[2]) punkte[10] = copy[0] * 3 + copy[3] * 2;
                    else punkte[10] = copy[3] * 3 + copy[0] * 2;
                    punkte[12] = 25;
                }
            } else {
                //Dreierpasch
                if (Arrays.stream(wurf).filter(i -> i == augenzahlen[0]).count() == 3)
                    punkte[10] = augenzahlen[0] * 3 + augenzahlen[1] + augenzahlen[2];
                if (Arrays.stream(wurf).filter(i -> i == augenzahlen[1]).count() == 3)
                    punkte[10] = augenzahlen[1] * 3 + augenzahlen[0] + augenzahlen[2];
                if (Arrays.stream(wurf).filter(i -> i == augenzahlen[2]).count() == 3)
                    punkte[10] = augenzahlen[2] * 3 + augenzahlen[0] + augenzahlen[1];
            }
        }
    }
    static int[] mehrmalsKniffel() {
        //Kniffel als Joker
        int[] punkte = new int[20];
        for (int i = 1; i < 7; i++) {
            punkte[i - 1] = SummeEinerAugenzahl(new int[]{i,i,i,i,i}, i);
        }
        punkte[15] += 50;
        punkte[10] = 6 * 5;
        punkte[11] = 6 * 5;
        punkte[12] = 25;
        punkte[13] = 30;
        punkte[14] = 40;
        punkte[16] = Chance(new int[] {6,6,6,6,6});
        return punkte;
    }
}
