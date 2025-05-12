package sim1;

import Kontroller.Automat;
import Modell.Kuchen;

import java.util.Random;



public class ConsumerSim1 implements Runnable {
    private final Automat automat;

    public ConsumerSim1(Automat automat) {
        this.automat = automat;
    }

    @Override
    public void run() {
        while (true) {
            int fach =deleteRandomKuchen();
            if (fach != -1) {
                System.out.println("Kuchen mit Fach " + fach + " geloescht.");
            }
        }
    }
    public synchronized int deleteRandomKuchen() {
        if (automat.getKuchenList().isEmpty()) {
            return -1;
        }
        Random zufall = new Random();
        int zufallFach = zufall.nextInt(automat.getKuchenList().size());
        Kuchen kuchenToRemove = automat.getKuchenList().get(zufallFach);

        if (kuchenToRemove != null) {
            automat.getKuchenList().set(zufallFach, null);
            System.out.println("Kuchen geloescht " + kuchenToRemove);
            return zufallFach;
        }
        return -1;
    }

}
