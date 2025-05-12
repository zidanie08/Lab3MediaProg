package kuchen;

import verwaltung.Verkaufsobjekt;

import java.io.Serializable;

public interface Kremkuchen extends Kuchen, Verkaufsobjekt, Serializable {
    String getKremsorte();
}
