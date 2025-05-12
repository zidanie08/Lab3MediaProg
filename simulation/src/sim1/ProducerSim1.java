package sim1;

import Kontroller.Automat;
import Modell.Hersteller;
import Modell.Kuchen;


public class ProducerSim1 implements Runnable {

    private final Automat automat;

    public ProducerSim1(Automat automat) {
        this.automat = automat;
    }

    @Override
    public void run() {
        while (true) {
            Kuchen zufallKuchen = RandomGenerator.createRandomKuchen(addRandomHersteller());
            boolean isAdd = automat.addKuchen(zufallKuchen);
            if (isAdd) {
                System.out.println("Kuchen " + zufallKuchen.getFachnummer() + " hinzugefuegt.");
            }
        }
    }

    public synchronized Hersteller addRandomHersteller() {
        Hersteller newHersteller = RandomGenerator.zufallHersteller();
        automat.getHerstellerList().add(newHersteller);
        return newHersteller;
    }
}

