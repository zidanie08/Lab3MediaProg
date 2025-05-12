package Deco;

import kuchen.Allergen;
import verwaltung.Hersteller;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;

public interface Dekoration {
    Hersteller getHerstellerDec();
    Date getInspektionsdatumDeco();
    Date getAdddatumDeco();
    int getFachnummerDeco();
    KuchenBoede getNamedeco();
    Collection<Allergen> getAllerg();
    BigDecimal getPreisDeco();
    int getNaehrwertDeco();
    Duration getHaltbarkeitDeco();
}
