import Kontroller.Automat;
import Modell.Hersteller;
import Modell.Kremkuchen;
import Modell.Kuchen;
import kuchen.Allergen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import sim1.RandomGenerator;
import sim2.ConsumerSim2;
import sim3.ProducerSim3;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class testSimulation {
    private Automat automat;
    private ProducerSim3 producer;
    private Automat mockAutomat;

    @BeforeEach
    void setUp() {
        this.automat = new Automat(10);
        this.producer = new ProducerSim3(automat);
        mockAutomat = mock(Automat.class);
        mockAutomat = mock(Automat.class);
    }

    @Test
    void testAddZufallHersteller() {
        Hersteller addHerst = producer.addRandomHerst();
        assertTrue(automat.getHerstellerList().contains(addHerst));
    }
    @Test
    void testRunAddKuchen() throws InterruptedException {
        Thread producer = new Thread(this.producer);
        producer.start();

        Thread.sleep(1000);
        producer.interrupt();
        assertFalse(automat.getKuchenList().isEmpty());
    }

    @Test
    void testZufallHerstellerLength() {
        Hersteller zufallHersteller = RandomGenerator.zufallHersteller();
        assertTrue(zufallHersteller.getName().length() >= 1 && zufallHersteller.getName().length() <= 10, "Name Laenge stimmt nicht");
    }

    @RepeatedTest(5)
    void testAddZufallKuchen() {
        Hersteller herst = new Hersteller("lid");
        Kuchen kuch = RandomGenerator.createRandomKuchen(herst);
        assertNotNull(kuch, "Kuchen soll nicht 0");
        assertEquals(herst, kuch.getHersteller(), "Hersteller stimmt nicht");
    }

    @Test
    void testConsumerRemoveKuchen() throws InterruptedException {
        Automat automat = new Automat(10) {
            private List<Kuchen> kuchenList = new ArrayList<>();

            @Override
            public boolean deleteKuchen(int index) {
                if (!kuchenList.isEmpty() && index < kuchenList.size()) {
                    kuchenList.remove(index);
                    return true;
                }
                return false;
            }

            @Override
            public List<Kuchen> getKuchenList() {
                return kuchenList;
            }
        };


        Kuchen testKuchen = new Kremkuchen() {
            @Override
            public Hersteller getHersteller() {
                return  RandomGenerator.zufallHersteller();
            }

            @Override
            public EnumSet<Allergen> getAllergene() {
                return EnumSet.noneOf(Allergen.class);
            }

            @Override
            public int getNaehrwert() {
                return 100;
            }

            @Override
            public Duration getHaltbarkeit() {
                return Duration.ofDays(5);
            }

            @Override
            public BigDecimal getPreis() {
                return BigDecimal.valueOf(2.99);
            }

            @Override
            public Date getInspektionsdatum() {
                return new Date();
            }

        };

        automat.getKuchenList().add(testKuchen);

        ConsumerSim2 consum = new ConsumerSim2(automat);

        new Thread(consum).start();

        Thread.sleep(1000);

        assertEquals(0, automat.getKuchenList().size());
    }
}
