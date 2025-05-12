package kuchen;

import verwaltung.Hersteller;

import java.io.Serializable;
import java.time.Duration;
import java.util.Collection;

public interface Kuchen extends Serializable {
    Hersteller getHersteller();
    Collection<Allergen> getAllergene();
    int getNaehrwert();
    Duration getHaltbarkeit();
}
