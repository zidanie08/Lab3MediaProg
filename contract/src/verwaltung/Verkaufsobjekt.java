package verwaltung;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public interface Verkaufsobjekt  extends Serializable {
    BigDecimal getPreis();
    Date getInspektionsdatum();
    int getFachnummer();
}
