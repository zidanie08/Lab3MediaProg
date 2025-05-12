package Modell;

import kuchen.Allergen;
import verwaltung.Hersteller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;

public class Obstkuchen extends Kuchen implements kuchen.Obstkuchen, Serializable {
    private static final long serialVersionUID = 1L;
    private String obstsorte;

    public Obstkuchen(Hersteller hersteller, Collection<Allergen> allergen, int naehrwert, Duration haltbarkeit,
                      BigDecimal preis, String obstorte) {
        super(hersteller, allergen, naehrwert, haltbarkeit, preis);
        this.obstsorte = obstorte;
    }
    public Obstkuchen(){
        super();
    }

   /* public void setObstsorte(String obstsorte) {
        this.obstsorte = obstsorte;
    }*/

    @Override
    public String getObstsorte() {
        return this.obstsorte;
    }

    @Override
    public String toString() {
        return "Obstkuchen{" +
                "obstsorte='" + obstsorte + '\'' +
                "} " + super.toString();
    }
}
