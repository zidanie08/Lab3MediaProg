package ObserverTest;

import Kontroller.Automat;
import observers.KapazitaetObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KapazitaetObserverTest {
    private KapazitaetObserver kaPobserver;
    private Automat automatMock;

    @BeforeEach
    void setUp() {
        kaPobserver = new KapazitaetObserver();
        automatMock = mock(Automat.class);
    }

    @Test
    void addMoeglich() {
        when(automatMock.getKuchenList().size()).thenReturn(5);
        when(automatMock.getMaxSize()).thenReturn(10);
        kaPobserver.update(automatMock, "add kuchen");
    }

    @Test
    void kapazitaetFastVoll() {
        when(automatMock.getKuchenList().size()).thenReturn(9);
        when(automatMock.getMaxSize()).thenReturn(10);
        kaPobserver.update(automatMock, "Automat fast Voll");

    }
}
