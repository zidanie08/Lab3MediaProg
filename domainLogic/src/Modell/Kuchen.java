package Modell;

import kuchen.Allergen;
import verwaltung.Hersteller;
import verwaltung.Verkaufsobjekt;

import java.io.Serializable;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.math.BigDecimal;

public abstract class Kuchen implements kuchen.Kuchen, Verkaufsobjekt, Serializable {
    private static final long serialVersionUID = 1L;
    private BigDecimal preis;
    private Date inspektionsdatum;
    private int fachnummer;
    private Hersteller hersteller;
    private Collection<Allergen> allergen;
    private int naehrwert;
    private Duration haltbarkeit;

    private Date einfuegeDatum;

    public Kuchen(Hersteller hersteller, Collection<Allergen> allergen, int naehrwert, Duration haltbarkeit,
                  BigDecimal preis) {
        this.hersteller = hersteller;
        this.allergen = allergen;
        this.naehrwert = naehrwert;
        this.haltbarkeit = haltbarkeit;
        this.preis = preis;
    }

    public Kuchen() {

    }

    @Override
    public Hersteller getHersteller() {
        return this.hersteller;
    }

    @Override
    public Collection<Allergen> getAllergene() {
        return this.allergen;
    }

    @Override
    public int getNaehrwert() {
        return this.naehrwert;
    }

    @Override
    public Duration getHaltbarkeit() {
        return this.haltbarkeit;
    }

    public void setHersteller(Hersteller hersteller) {
        this.hersteller = hersteller;
    }

    public void setAllergen(Collection<Allergen> allergen) {
        this.allergen = allergen;
    }

    public void setNaehrwert(int naehrwert) {
        this.naehrwert = naehrwert;
    }

    public void setHaltbarkeit(Duration haltbarkeit) {
        this.haltbarkeit = haltbarkeit;
    }

    public Collection<Allergen> getAllergen() {
        return allergen;
    }

   /* public Date getEinfuegeDatum() {
        return einfuegeDatum;
    }*/

    public void setEinfuegeDatum(Date einfuegeDatum) {
        this.einfuegeDatum = einfuegeDatum;
    }

    @Override
    public int getFachnummer() {
        return this.fachnummer;
    }

    public void setFachnummer(int fachnummer) {
        this.fachnummer = fachnummer;
    }

    @Override
    public Date getInspektionsdatum() {
        return this.inspektionsdatum;
    }

    public void setInspektionsdatum(Date inspektionsdatum) {
        this.inspektionsdatum = inspektionsdatum;
    }

    @Override
    public BigDecimal getPreis() {
        return this.preis;
    }

    public void setPreis(BigDecimal preis) {
        this.preis = preis;
    }

    @Override
    public  String toString() {
        return "Kuchen{" +
                "preis=" + preis +
                ", fachnummer=" + fachnummer +
                ", hersteller=" + hersteller +
                ", allergen=" + allergen +
                ", naehrwert=" + naehrwert +
                ", haltbarkeit=" + haltbarkeit +
                '}';
    }
}
