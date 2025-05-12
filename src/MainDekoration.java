import Deco.Belag;
import Deco.KuchenBoede;
import DecoImpl.Belaege;
import DecoImpl.Boeden;
import DecoImpl.Dekorator;
import Modell.Hersteller;
import kuchen.Allergen;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainDekoration {

    public static void main(String[] args) {
        List<Belaege> belaegeDeco = Arrays.asList(
                new Belaege(Belag.Apfel, Allergen.Gluten, new BigDecimal("12"), 34, Duration.ofDays(4)),
                new Belaege(Belag.Sahne, Allergen.Erdnuss, new BigDecimal("5"), 20, Duration.ofDays(3))
        );
        Boeden boeden = new Boeden(KuchenBoede.Hefeteig, Allergen.Erdnuss, new BigDecimal("21"), 64, Duration.ofDays(7));
        Hersteller hersteller = new Hersteller("lidl");
        Date inspektionsdatum = new Date();
        Date adddatum = new Date();
        int fachnummer = 1;

        Dekorator kuchen = new Dekorator(belaegeDeco, boeden, hersteller, inspektionsdatum, adddatum, fachnummer);
        System.out.println("Gesamtpreis: " + kuchen.getPreisDeco());
        System.out.println("Name: " + kuchen.getName());
        System.out.println("KuchenInfo: " + kuchen.getKuchenInfo());
    }
}
