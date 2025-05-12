package Modell;

import DecoImpl.Belaege;
import DecoImpl.Boeden;
import kuchen.Allergen;
import verwaltung.Hersteller;

import java.awt.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;

public class Kremkuchen extends Kuchen implements kuchen.Kremkuchen, Serializable {
    private static final long serialVersionUID = 1L;
    private String kremsorte;

    public Kremkuchen(Hersteller hersteller, Collection<Allergen> allergen, int naehrwert, Duration haltbarkeit,
                      BigDecimal preis, String kremsorte) {
        super(hersteller, allergen, naehrwert, haltbarkeit, preis);
        this.kremsorte = kremsorte;
    }
    public Kremkuchen(){}

    @Override
    public String getKremsorte() {
        return this.kremsorte;
    }

    public void setKremsorte(String kremsorte) {
        this.kremsorte = kremsorte;
    }

    @Override
    public String toString() {
        return "Kremkuchen{" +
                "kremsorte='" + kremsorte + '\'' +
                "} " + super.toString();
    }

}

