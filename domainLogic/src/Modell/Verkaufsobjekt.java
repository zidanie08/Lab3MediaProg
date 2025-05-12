package Modell;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Verkaufsobjekt implements verwaltung.Verkaufsobjekt, Serializable {
    private static final long serialVersionUID = 1L;
    public Verkaufsobjekt() {
    }

    private BigDecimal preis;
    private Date inspektionsdatum;
    private int fachnummer;

    @Override
    public BigDecimal getPreis() {
        return this.preis;
    }

    @Override
    public Date getInspektionsdatum() {
        return this.inspektionsdatum;
    }

    @Override
    public int getFachnummer() {
        return this.fachnummer;
    }

    public void setInspektionsdatum(Date inspektionsdatum) {
        this.inspektionsdatum = inspektionsdatum;
    }

    public void setFachnummer(int fachnummer) {
        this.fachnummer = fachnummer;
    }

    public void setPreis(BigDecimal preis) {
        this.preis = preis;
    }

}
