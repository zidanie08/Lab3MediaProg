package sim2;

import Kontroller.Automat;
import Modell.Kuchen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConsumerSim2 implements Runnable {

    private Automat automat;

    public ConsumerSim2(Automat automat) {
        this.automat = automat;
    }

    @Override
    public void run() {
        while (true) {
            if (!automat.getKuchenList().isEmpty()) {
                int index = (int) (Math.random() * automat.getKuchenList().size());
                automat.deleteKuchen(index);
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}