package DecoImpl;

import Deco.Dekoration;
import Deco.KuchenBoede;
import kuchen.Allergen;
import Modell.Hersteller;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class Dekorator implements Dekoration {

    private Collection<Belaege> belaege;
    private Boeden boeden;
    private Hersteller herstellerDec;
    private Date inspektionsdatumDeco;
    private Date adddatumDeco;
    private int fachnummerDeco;
    public Dekorator(Collection<Belaege> belaege, Boeden boeden, Hersteller herstellerDec, Date inspektionsdatumDeco, Date adddatumDeco, int fachnummerDeco) {
        this.belaege = belaege;
        this.boeden = boeden;
        this.herstellerDec = herstellerDec;
        this.inspektionsdatumDeco = inspektionsdatumDeco;
        this.adddatumDeco = adddatumDeco;
        this.fachnummerDeco = fachnummerDeco;
    }

    @Override
    public Hersteller getHerstellerDec() {
        return herstellerDec;
    }

    @Override
    public Date getInspektionsdatumDeco() {
        return inspektionsdatumDeco;
    }

    @Override
    public Date getAdddatumDeco() {
        return adddatumDeco;
    }

    @Override
    public int getFachnummerDeco() {
        return fachnummerDeco;
    }

    @Override
    public KuchenBoede getNamedeco() {
        return null;
    }
    public String getName() {
        String nameBelag = belaege.stream()
                .map(belag -> belag.getNameDekorations().name())
                .collect(Collectors.joining(" "));
        return boeden.getKuchenBoede().name() +  " " + nameBelag;
    }

    @Override
    public Collection<Allergen> getAllerg() {
        HashSet<Allergen> allergene = new HashSet<>();
        for (Belaege belag : belaege) {
            allergene.add(belag.getAllerg());
        }
        allergene.add(boeden.getAllerg());
        return allergene;
    }



    @Override
    public BigDecimal getPreisDeco() {
        BigDecimal totalPreis = boeden.getPreisDeco();
        for (Belaege belag : belaege) {
            totalPreis = totalPreis.add(belag.getPreisDeco());
        }
        return totalPreis;
    }

    @Override
    public int getNaehrwertDeco() {
        int totalNaehrwert = boeden.getNaehrwertDeco();
        for (Belaege belag : belaege) {
            totalNaehrwert += belag.getNaehrwertDeco();
        }
        return totalNaehrwert;
    }

    @Override
    public Duration getHaltbarkeitDeco() {
        Duration minHaltbarkeit = Duration.ofDays(Long.MAX_VALUE);
        for (Belaege belag : belaege) {
            if (belag.getHaltbarkeitDeco().compareTo(minHaltbarkeit) < 0) {
                minHaltbarkeit = belag.getHaltbarkeitDeco();
            }
        }
        return boeden.getHaltbarkeitDeco().compareTo(minHaltbarkeit) < 0 ? boeden.getHaltbarkeitDeco() : minHaltbarkeit;
    }

    public String getKuchenInfo() {
        String nameBelag = belaege.stream()
                .map(belag -> belag.getNameDekorations().name())
                .collect(Collectors.joining(" "));
        return boeden.getKuchenBoede().name() + " " + herstellerDec.getName() + " " + nameBelag;
    }

}
