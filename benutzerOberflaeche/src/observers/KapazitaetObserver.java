package observers;

import Kontroller.Automat;

import java.util.Observable;
import java.util.Observer;

public class KapazitaetObserver implements Observer {

    @Override
    public void update(Observable observable, Object object) {
        if (observable instanceof Automat && object instanceof String) {
            if (object.equals("add kuchen")) {
                int kuchenSize = ((Automat) observable).getKuchenList().size();
                if (kuchenSize>= 0.9 * ((Automat) observable).getMaxSize()) {
                    System.out.println("Die Kapazitaet hat 90% ueberschrietet");
                }
            }
        }
    }
    }

