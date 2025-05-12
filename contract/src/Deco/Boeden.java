package Deco;

import kuchen.Allergen;
import verwaltung.Hersteller;

import java.math.BigDecimal;
import java.time.Duration;

public interface Boeden {
    Allergen getAllerg();
    KuchenBoede getKuchenBoede();
    BigDecimal getPreisDeco();
    int getNaehrwertDeco();
    Duration getHaltbarkeitDeco();

}
