package sim3;

import Kontroller.Automat;
import Modell.Hersteller;
import Modell.Kuchen;
import sim1.RandomGenerator;

import java.util.Random;

public class ProducerSim3 implements Runnable {
    private final Automat automat;

    public ProducerSim3(Automat automat) {
        this.automat = automat;
    }

    @Override
    public void run() {
        try {
            while (true) {

                Kuchen zufallKuch = RandomGenerator.createRandomKuchen(addRandomHerst());
                automat.addKuchen(zufallKuch);
                Thread.sleep(new Random().nextInt(1000));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public synchronized Hersteller addRandomHerst() {
        Hersteller newHersteller = RandomGenerator.zufallHersteller();
        automat.getHerstellerList().add(newHersteller);
        return newHersteller;
    }

}
