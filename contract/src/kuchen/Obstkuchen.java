package kuchen;

import verwaltung.Verkaufsobjekt;

import java.io.Serializable;

public interface Obstkuchen extends Kuchen, Verkaufsobjekt, Serializable {
    String getObstsorte();
}
