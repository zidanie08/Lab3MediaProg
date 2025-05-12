package sim3;

import Kontroller.Automat;
import Modell.Kuchen;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class WahlKuchen implements Runnable {
    private final Automat automat;

    public WahlKuchen(Automat automat) {
        this.automat = automat;
    }

    @Override
    public void run() {
        try {
            while (true) {
                inspectRandomKuchen();
                Thread.sleep(1500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    private void inspectRandomKuchen() {
        List<Kuchen> kuchenList = automat.getKuchenList();
        List<Kuchen> validKuchen = kuchenList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (!validKuchen.isEmpty()) {
            Random zufall = new Random();
            int fach = zufall.nextInt(validKuchen.size());
            Kuchen randomKuchen = validKuchen.get(fach);

            randomKuchen.setInspektionsdatum(new Date());
            System.out.println("zufall inspektion von Kuchen " + randomKuchen);
        }
    }

}
