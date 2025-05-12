import Kontroller.Automat;
import sim3.ConsumerSim3;
import sim3.ProducerSim3;
import sim3.WahlKuchen;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainSim3 {
    public static void main(String[] args) {
        int n = 10;
        Automat automat = new Automat(n);
        new Thread(new ProducerSim3(automat)).start();
        new Thread(new ConsumerSim3(automat)).start();
        new Thread(new WahlKuchen(automat)).start();
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }
}
