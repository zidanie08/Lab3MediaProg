package DecoImpl;

import Deco.Belag;
import Deco.KuchenBoede;
import kuchen.Allergen;
import verwaltung.Hersteller;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;

public class Belaege implements Deco.Belaege {

    Allergen allerg;
    BigDecimal preisDeco;
    Belag nameDekorations;
    int naehrwertDeco;
    Duration haltbarkeitDeco;

    public Belaege(Belag nameDekorations, Allergen allerg, BigDecimal preisDeco, int naehrwertDeco, Duration haltbarkeitDeco ){
        this.nameDekorations= nameDekorations;
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
    public Belag getNameDekorations() {
        return nameDekorations ;
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
