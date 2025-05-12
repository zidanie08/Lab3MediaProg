package ObserverTest;

import observers.AllergenObserver;
import org.junit.jupiter.api.Test;

import java.util.Observable;

import static org.mockito.Mockito.mock;

public class AllergenObserverTest {
    @Test
    void updateMitValidParam() {
        Observable observable = mock(Observable.class);
        AllergenObserver observer = new AllergenObserver();
        observer.update(observable, "Allergen change");
    }

    @Test
    void updateMitInvalidParam() {
        Observable observable = new Observable();
        AllergenObserver observer = new AllergenObserver();
        observer.update(observable, "Falsche Message");

    }
}
