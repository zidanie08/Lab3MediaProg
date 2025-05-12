package observers;

import Kontroller.Automat;
import kuchen.Allergen;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class AllergenObserver implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Automat && arg instanceof String) {
            if (arg.equals("Allergen change")) {
                System.out.println("Aenderung der Allergenen");
            }
        }
    }
    }
    

