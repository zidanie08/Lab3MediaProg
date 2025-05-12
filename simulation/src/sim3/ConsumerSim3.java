package sim3;

import Kontroller.Automat;
import Modell.Kuchen;

import java.util.Date;
import java.util.List;

public class ConsumerSim3 implements Runnable{
    private final Automat automat;

    public ConsumerSim3(Automat automat) {
        this.automat = automat;
    }

    @Override
    public void run() {
        try {
            while (true) {
                deleteAltKuch();
                Thread.sleep(1000); 
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    private void deleteAltKuch() {
        List<Kuchen> kuchenList = automat.getKuchenList();
        Kuchen aeltereKuch = null;
        Date altDate = new Date();
        int altFach = -1;

        for (int i = 0; i < kuchenList.size(); i++) {
            Kuchen kuchen = kuchenList.get(i);
            if (kuchen != null && (aeltereKuch == null || kuchen.getInspektionsdatum().before(altDate))) {
                aeltereKuch = kuchen;
                altDate = kuchen.getInspektionsdatum();
                altFach = i;
            }
        }
        if (aeltereKuch != null) {
            kuchenList.set(altFach, null);
            System.out.println("Aeltere Kuchen ist geloescht " + aeltereKuch);
        }
    }
}
