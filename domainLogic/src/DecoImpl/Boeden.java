package DecoImpl;

import Deco.Belaege;
import Deco.Belag;
import Deco.KuchenBoede;
import kuchen.Allergen;
import verwaltung.Hersteller;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class Boeden implements Deco.Boeden {
    KuchenBoede namedeco;
    Allergen allerg;
    BigDecimal preisDeco;
    private KuchenBoede kuchenBoede;
    int naehrwertDeco;
    Duration haltbarkeitDeco;


    public Boeden( KuchenBoede kuchenBoede, Allergen allerg, BigDecimal preisDeco, int naehrwertDeco, Duration haltbarkeitDeco) {
        this.kuchenBoede=kuchenBoede;
        this.allerg = allerg;
        this.preisDeco= preisDeco;
        this.naehrwertDeco= naehrwertDeco;
        this.haltbarkeitDeco= haltbarkeitDeco;
    }

    @Override
    public Allergen getAllerg() {
        return allerg;
    }

    @Override
    public KuchenBoede getKuchenBoede() {
        return kuchenBoede;
    }

    @Override
    public BigDecimal getPreisDeco() {
        return preisDeco;
    }

    @Override
    public int getNaehrwertDeco() {
        return naehrwertDeco;
    }

    @Override
    public Duration getHaltbarkeitDeco() {
        return haltbarkeitDeco;
    }


}
