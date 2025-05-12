import Kontroller.Automat;
import sim2.ConsumerSim2;
import sim2.ObserverSim2;
import sim2.ProducerSim2;

import java.util.Scanner;

public class MainSim2 {

    public static void main(String[] args) {
        int n = 4;
        int nThreads= 2;
        Automat automat = new Automat(n);
        ObserverSim2 observer = new ObserverSim2();
        automat.addObserver(observer);
        for (int i = 0; i < nThreads; i++) {
            new Thread(new ProducerSim2(automat)).start();
            new Thread(new ConsumerSim2(automat)).start();
        }
    }

    }


