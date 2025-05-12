package Deco;

import kuchen.Allergen;
import kuchen.Kuchen;
import verwaltung.Hersteller;
import verwaltung.Verkaufsobjekt;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Set;

public interface Belaege  {
    Allergen getAllerg();
    Belag getNameDekorations();
    BigDecimal getPreisDeco();
    int getNaehrwertDeco();
    Duration getHaltbarkeitDeco();
}
