import Client.TCPClient;
import Kontroller.Automat;
import TCP.ServerTCP;
import org.junit.jupiter.api.BeforeEach;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.testng.AssertJUnit.assertEquals;


public class TestNet {
        private Automat automat;

    private TCPClient client;

        @BeforeEach
        void setUp() {
            automat = new Automat(10);
            client = new TCPClient("127.0.0.1", 2001);
        }

    @Test
        void testAddHerstellerCommandMock() {
            ServerTCP serverTCP = new ServerTCP(10);
            ServerTCP.automat = mock(Automat.class);
            String antwort = serverTCP.processCommand("ADDHERSTELLER;TestHersteller");
            assertNotNull(antwort);
            assertTrue(antwort.contains("erfolgreich") || antwort.contains("Fehler"));
        }

        @Test
        void testProcessCommandAddHersteller() {
             automat = new Automat(10);
            ServerTCP.automat = automat;
            String comm = "ADDHERSTELLER;NewHersteller";
            String antwort = ServerTCP.processCommand(comm);
            assertEquals("ADD erfolgreich ", antwort);
        }

        @Test
        void testProcessCommandFehlt() {
            String comm = "INVALIDCOMMAND";
            String antwort = ServerTCP.processCommand(comm);
            assertTrue(antwort.startsWith("ERROR"));
        }


}
