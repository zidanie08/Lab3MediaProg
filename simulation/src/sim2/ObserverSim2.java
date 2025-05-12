package sim2;

import Kontroller.Automat;
import Modell.Kuchen;

import java.util.Observable;
import java.util.Observer;

public class ObserverSim2 implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Automat && arg instanceof Kuchen) {
            Kuchen kuchen = (Kuchen) arg;
            Automat automat = (Automat) o;

            if (automat.getKuchenList().contains(kuchen)) {
                System.out.println("Beobachter: Kuchen " + kuchen.getFachnummer() + " wurde hinzugefügt.");
            } else {
                System.out.println("Beobachter: Kuchen " + kuchen.getFachnummer() + " wurde gelöscht.");
            }
        }
    }
}
