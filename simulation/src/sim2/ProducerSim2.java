package sim2;

import Kontroller.Automat;
import Modell.Hersteller;
import Modell.Kuchen;
import sim1.RandomGenerator;



public class ProducerSim2 implements Runnable {

    private Automat automat;

    public ProducerSim2(Automat automat) {
        this.automat = automat;
    }

    @Override
    public void run() {
        while (true) {
            Hersteller hersteller = RandomGenerator.zufallHersteller();
            Kuchen kuchen = RandomGenerator.createRandomKuchen(hersteller);
            automat.addHersteller(String.valueOf(hersteller));
            automat.addKuchen(kuchen);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


}