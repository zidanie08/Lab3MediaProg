import Kontroller.Automat;
import sim1.ConsumerSim1;
import sim1.ProducerSim1;

public class MainSim1 {
    public static void main(String[] args) {
        int capacity = args.length > 0 ? Integer.parseInt(args[0]) : 10;
        Automat automat = new Automat(capacity);
        ProducerSim1 producer = new ProducerSim1(automat);
        Thread producerThread = new Thread(producer);
        producerThread.start();
        ConsumerSim1 consumer = new ConsumerSim1(automat);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
    }

}
