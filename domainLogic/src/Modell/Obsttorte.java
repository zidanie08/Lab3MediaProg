package Modell;

import kuchen.Allergen;
import verwaltung.Hersteller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;

public class Obsttorte extends Kuchen implements kuchen.Obsttorte, Serializable {
    private static final long serialVersionUID = 1L;
    private String kremsorte;
    private String obstsorte;

    public Obsttorte(Hersteller hersteller, Collection<Allergen> allergen, int naehrwert, Duration haltbarkeit,
                     String obstsorte, String kremsorte, BigDecimal preis) {
        super(hersteller, allergen, naehrwert, haltbarkeit, preis);
        this.obstsorte = obstsorte;
        this.kremsorte = kremsorte;
    }
    public Obsttorte(){}

    public void setKremsorte(String kremsorte) {
        this.kremsorte = kremsorte;
    }

    public void setObstsorte(String obstsorte) {
        this.obstsorte = obstsorte;
    }

    @Override
    public String getKremsorte() {
        return this.kremsorte;
    }

    @Override
    public String getObstsorte() {
        return this.obstsorte;
    }

    @Override
    public String toString() {
        return "Obsttorte{" +
                "kremsorte='" + kremsorte + '\'' +
                ", obstsorte='" + obstsorte + '\'' +
                "} " + super.toString();
    }
}
