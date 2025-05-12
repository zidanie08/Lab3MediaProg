package sim1;

import Modell.*;
import kuchen.Allergen;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Random;

public class RandomGenerator implements Serializable {
    public static Hersteller zufallHersteller() {
        Random zufall = new Random();
        int size = zufall.nextInt(7) + 1;
        StringBuilder makeString = new StringBuilder(size);

        String zeichen = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        for (int i = 0; i < size; i++) {
             int zufallFach = zufall.nextInt(zeichen.length());
            char zufallChar = zeichen.charAt(zufallFach);
            makeString.append(zufallChar);
        }

        return new Hersteller(makeString.toString());

    }
    public static Kuchen createRandomKuchen(Modell.Hersteller hersteller) {
        Collection<Allergen> allergen = null;
        int naehrwert = 56;
        Duration haltbarkeit = Duration.ofDays(3);
        BigDecimal preis = BigDecimal.valueOf(12);


        Kuchen[] kuchensorte = {
                new Kremkuchen(hersteller, allergen, naehrwert, haltbarkeit, preis, "KremSorte"),
                new Obstkuchen(hersteller, allergen, naehrwert, haltbarkeit, preis, "ObstSorte"),
                new Obsttorte(hersteller, allergen, naehrwert, haltbarkeit, "ObstSorte", "KremSorte", preis)
        };

        Random zufall = new Random();
        int fach = zufall.nextInt(kuchensorte.length);
        return kuchensorte[fach];

    }




}

