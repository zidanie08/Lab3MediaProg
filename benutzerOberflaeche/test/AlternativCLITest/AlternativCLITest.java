package AlternativCLITest;

import Cli.AlternativCLI;
import Kontroller.Automat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlternativCLITest {
    private final PrintStream printStream = System.out;
    private final InputStream inputStream = System.in;
    private final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    private Automat automat;
    private AlternativCLI alternativCLI;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(byteStream));
        automat = new Automat(10);
        alternativCLI = new AlternativCLI(automat);
    }

    @AfterEach
    void refresh() {
        System.setOut(printStream);
        System.setIn(inputStream);
    }

    @Test
    public void testAddHersteller() {
        String testEingabe = ":c" + System.lineSeparator() +
                "1" + System.lineSeparator() +
                "Lidl" + System.lineSeparator() +
                ":c" + System.lineSeparator() +
                "1" + System.lineSeparator() +
                "Aldi" + System.lineSeparator() +
                ":q" + System.lineSeparator();
        System.setIn(new ByteArrayInputStream(testEingabe.getBytes()));

        alternativCLI.actionAltCLI();
        assertEquals(2, automat.getHerstellerList().size());
        assertTrue(automat.getHerstellerList().stream().anyMatch(h -> h.getName().equals("Lidl")));
        assertTrue(automat.getHerstellerList().stream().anyMatch(h -> h.getName().equals("Aldi")));
    }
}
