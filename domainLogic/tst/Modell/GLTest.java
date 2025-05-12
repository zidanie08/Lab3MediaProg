package Modell;

import Kontroller.Automat;
import kuchen.Allergen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.io.File;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GLTest {
    @Spy
    Automat automat = new Automat(10);
    private final String testFile = "GLtest.jos";
    File datei = new File(testFile);
    private Hersteller hersteller;
    private Kuchen kuchenSpy;
    Kuchen kuchen = mock(Kuchen.class);
    Observer observMock = mock(Observer.class);


    @BeforeEach
    void setUp() {
        automat = new Automat(10);
        hersteller = new Hersteller("TestHersteller");
        kuchenSpy = spy(new Kremkuchen());

    }

    @Test
    void saveAndLoadJOS() {
        automat.saveJOS(testFile);
         datei = new File(testFile);
        assertTrue(datei.exists());

        Automat automatL = automat.ladenJOS(testFile);
        assertNotNull(automatL);
        assertEquals(automat.getMaxSize(), automatL.getMaxSize());
    }

    @Test
    void saveAndLoadJBP() {
        automat.saveJBP(testFile);
        datei = new File(testFile);
        assertTrue(datei.exists());

        Automat automatL = automat.ladenJBP(testFile);
        assertNotNull(automatL);
        assertEquals(automat.getMaxSize(), automatL.getMaxSize());
    }

    @Test
    void addValidHersteller() {
        String herstellerName = "Herst1";
        Hersteller hersteller = automat.addHersteller(herstellerName);
        assertTrue(automat.getHerstellerList().contains(hersteller));
    }

    @Test
    void addInvalidHersteller() {
        String herstellerName = "Herst2";
        automat.addHersteller(herstellerName);
        assertThrows(IllegalArgumentException.class, () -> automat.addHersteller(herstellerName));
    }
    @Test
    void addValidKuchen() {
        Hersteller hersteller = automat.addHersteller("Herst3");
        Kuchen kuchen = mock(Kuchen.class);
        when(kuchen.getHersteller()).thenReturn(hersteller);

        assertTrue(automat.addKuchen(kuchen));
    }

    @Test
    void addKuchenAutomatVoll() {
        Automat automatVoll = new Automat(0);
        Kuchen kuchen = mock(Kuchen.class);

        assertFalse(automatVoll.addKuchen(kuchen));
    }
    @Test
    void kuchenSucessDelete() {
        Hersteller hersteller = automat.addHersteller("Herst4");
         kuchen = mock(Kuchen.class);
        when(kuchen.getHersteller()).thenReturn(hersteller);
        when(kuchen.getFachnummer()).thenReturn(0);

        assertTrue(automat.addKuchen(kuchen));
        assertTrue(automat.deleteKuchen(0));
    }

    @Test
    void deleteMitFascheFach() {
        assertFalse(automat.deleteKuchen(-1));
    }

    @Test
    void anzahlGelagerterKuchen() {
        Hersteller herst1 = automat.addHersteller("Herst5");
         kuchen = mock(Kuchen.class);
        when(kuchen.getHersteller()).thenReturn(herst1);
        automat.addKuchen(kuchen);

        Hersteller herst2 = automat.addHersteller("Herst6");
        Kuchen kuch2 = mock(Kuchen.class);
        when(kuch2.getHersteller()).thenReturn(herst2);
        automat.addKuchen(kuch2);

        Map<Hersteller, Integer> anzahl = automat.anzahlGelagerterKuchen();
        assertEquals(1, anzahl.get(herst1).intValue());
        assertEquals(1, anzahl.get(herst2).intValue());
    }

    @Test
    void addKuchenNotifObserver() {
        Automat automat = new Automat(10);
        automat.addHersteller("TestHersteller");
        Hersteller hersteller = automat.getHerstellerList().get(0);

         observMock = mock(Observer.class);
        automat.addObserver(observMock);

        Kuchen kuchen = new Kremkuchen(hersteller, EnumSet.of(Allergen.Gluten), 50,  Duration.ofHours(54), BigDecimal.valueOf(8.50),"or");
        automat.addKuchen(kuchen);

        verify(observMock, times(1)).update(eq(automat), any());
    }



    @Test
    void deleteKuchenNotifObserver() {
         automat = new Automat(10);
         observMock = mock(Observer.class);
        automat.addObserver(observMock);

        kuchen = mock(Kuchen.class);
        Hersteller hersteller = new Hersteller("HerstellerTest");
        automat.addHersteller(hersteller.getName());
        when(kuchen.getHersteller()).thenReturn(hersteller);
        when(kuchen.getFachnummer()).thenReturn(0);

        automat.addKuchen(kuchen);
        automat.deleteKuchen(0);
        verify(observMock, atLeast(2)).update(any(Automat.class), any());
    }

   @Test
   void notifyObserversOnAction() {
        automat = new Automat(10);
        observMock = mock(Observer.class);
        automat.addObserver(observMock);
        hersteller = new Hersteller("TestHersteller");
       kuchen = mock(Kuchen.class);
       when(kuchen.getHersteller()).thenReturn(hersteller);
       when(kuchen.getAllergene()).thenReturn(Collections.emptySet());
       automat.addHersteller(hersteller.getName());
       boolean result = automat.addKuchen(kuchen);
       assertTrue(result, "success");
       verify(observMock, times(1)).update(eq(automat), any());
   }


    @Test
    void addHerstellerUndRefreshListSpy() {
        Automat automatSpy = spy(new Automat(10));
        String herstellerName = "NeueHerst";
        automatSpy.addHersteller(herstellerName);
        verify(automatSpy).updateKuchenProHersteller();
    }

    @Test
    void addKuchenMitAllergen() {
         automat = new Automat(10);
         observMock = mock(Observer.class);
        automat.addObserver(observMock);
        Hersteller hersteller = new Hersteller("Herst");
        automat.addHersteller(hersteller.getName());
         kuchen = new Kremkuchen(hersteller, EnumSet.of(Allergen.Gluten), 250,  Duration.ofHours(48), BigDecimal.valueOf(3.50),"or");

        automat.addKuchen(kuchen);
        verify(observMock, times(1)).update(eq(automat), any());
    }


    @Test
    void automatVoll() {
         automat = spy(new Automat(1));
        Hersteller hersteller = new Hersteller("VollHerst");
        Kuchen kuchen1 = mock(Kuchen.class);
        Kuchen kuchen2 = mock(Kuchen.class);

        automat.addHersteller(hersteller.getName());
        when(kuchen1.getHersteller()).thenReturn(hersteller);
        when(kuchen2.getHersteller()).thenReturn(hersteller);
        automat.addKuchen(kuchen1);
        automat.addKuchen(kuchen2);

        verify(automat, times(1)).notifyObservers("Automat voll");
    }

    @Test
    void testKapselungUndObserverNotif() {
         automat = Mockito.spy(new Automat(10));
         observMock = mock(Observer.class);
        automat.addObserver(observMock);

         hersteller = new Hersteller("HerstellerTest");
         kuchen = mock(Kuchen.class);
        when(kuchen.getHersteller()).thenReturn(hersteller);

        automat.addHersteller(hersteller.getName());
        automat.addKuchen(kuchen);
        verify(observMock, times(1)).update(eq(automat), any());

        automat.deleteObserver(observMock);
    }
    @Test
    void testUpdateKuchenProHersteller() {
        Hersteller hersteller1 = automat.addHersteller("Herst1");
        Hersteller hersteller2 = automat.addHersteller("Herst2");
        Kuchen kuch = new Kremkuchen(hersteller1, Collections.emptySet(), 20, Duration.ofDays(1), new BigDecimal("2.00"), "Vanille");
        Kuchen kuchen = new Kremkuchen(hersteller2, Collections.emptySet(), 60, Duration.ofDays(2), new BigDecimal("3.00"), "koko");

        automat.addKuchen(kuch);
        automat.addKuchen(kuchen);

        Map<Hersteller, Integer> kuchenCount = automat.anzahlGelagerterKuchen();
        assertEquals(1, kuchenCount.getOrDefault(hersteller1, 0));
        assertEquals(1, kuchenCount.getOrDefault(hersteller2, 0));
    }

    @Test
    void refreshListNachAddHersteller() {
        String herstellerName = "Hersteller";
        int size = automat.getHerstellerList().size();
        Hersteller herst = automat.addHersteller(herstellerName);
        int sizeAdd = automat.getHerstellerList().size();
        assertTrue(sizeAdd == size + 1, "Probleme mit size ListHersteller");
        assertTrue(automat.getHerstellerList().contains(herst), "neu Hersteller fehlt");
    }

    @Test
    void testLagerAllergen() {
        Hersteller hersteller = automat.addHersteller("Herst2");
        Set<Allergen> allergenes = new HashSet<>(Arrays.asList(Allergen.Gluten, Allergen.Haselnuss));
         kuchen = new Kremkuchen(hersteller, allergenes, 300, Duration.ofDays(3), new BigDecimal("4.00"), "Nuss");
        automat.addKuchen(kuchen);

        List<Allergen> lagerAllergen = automat.lagerAllergen();
        assertTrue(lagerAllergen.containsAll(allergenes));
    }

    @Test
    void testNoLagerAllergen() {
        Hersteller hersteller = automat.addHersteller("Herst1");
        Set<Allergen> allergenes = new HashSet<>(Arrays.asList(Allergen.Gluten, Allergen.Haselnuss));
         kuchen = new Kremkuchen(hersteller, allergenes, 40, Duration.ofDays(4), new BigDecimal("5.00"), "Nuss");
        automat.addKuchen(kuchen);

        List<Allergen> noLagerAllergen = automat.noLagerAllergen();
        assertFalse(noLagerAllergen.contains(Allergen.Gluten));
        assertFalse(noLagerAllergen.contains(Allergen.Haselnuss));
    }


    @Test
    void testObsttorteAdd() {
        Obsttorte obsttorte = new Obsttorte(hersteller, Collections.singleton(Allergen.Haselnuss), 20, Duration.ofDays(3), "banan", "Sahne", new BigDecimal("5.00"));
        assertEquals("banan", obsttorte.getObstsorte());
        assertEquals("Sahne", obsttorte.getKremsorte());
    }



    @Test
    void testObstkuchenAdd() {
        Obstkuchen obstkuchen = new Obstkuchen(hersteller, Collections.singleton(Allergen.Gluten), 60, Duration.ofDays(2), new BigDecimal("3.00"), "Apfel");
        assertEquals("Apfel", obstkuchen.getObstsorte());
    }

    @Test
    void testObsttorteToString() {
        Obsttorte obsttorte = new Obsttorte(hersteller, Collections.singleton(Allergen.Haselnuss), 20, Duration.ofDays(3), "banan", "Sahne", new BigDecimal("5.50"));
        String toStringOutput = obsttorte.toString();
        assertTrue(toStringOutput.contains("Obsttorte{kremsorte='Sahne', obstsorte='banan'}"));
        assertTrue(toStringOutput.contains("preis=5.50"));
    }

    @Test
    void testObstkuchenToString() {
        Obstkuchen obstkuchen = new Obstkuchen(hersteller, Collections.singleton(Allergen.Haselnuss), 40, Duration.ofDays(4), new BigDecimal("3.23"), "obs");
        String toStringOutput = obstkuchen.toString();
        assertTrue(toStringOutput.contains("Obstkuchen{obstsorte='obs'}"));
        assertTrue(toStringOutput.contains("preis=3.23"));
    }

    @Test
    public void testSetAndGetPreis() {
        Verkaufsobjekt verkaufsobjekt = new Verkaufsobjekt();
        BigDecimal preis = new BigDecimal("59");
        verkaufsobjekt.setPreis(preis);
        assertEquals(preis, verkaufsobjekt.getPreis());
    }

    @Test
    public void testSetAndGetInspektionsdatum() {
        Verkaufsobjekt objekt = new Verkaufsobjekt();
        Date inspektDate = new Date();
        objekt.setInspektionsdatum(inspektDate);
        assertEquals(inspektDate, objekt.getInspektionsdatum());
    }

    @Test
    public void testSetAndGetFachnummer() {
        Verkaufsobjekt verkaufsobjekt = new Verkaufsobjekt();
        int fachnummer = 3;
        verkaufsobjekt.setFachnummer(fachnummer);
        assertEquals(fachnummer, verkaufsobjekt.getFachnummer());
    }
    @Test
    public void testLeereConstructor() {
        Obsttorte obsttorte = new Obsttorte();
        assertNotNull(obsttorte);
        assertNull(obsttorte.getKremsorte());
        assertNull(obsttorte.getObstsorte());
    }
    @Test
    public void testSetKremsorte() {
        Obsttorte obsttorte = new Obsttorte();
        String creme = "krem";
        obsttorte.setKremsorte(creme);
        assertEquals(creme, obsttorte.getKremsorte());
    }

    @Test
    public void testSetObstsorte() {
        Obsttorte obsttorte = new Obsttorte();
        String obstsorte = "banana";
        obsttorte.setObstsorte(obstsorte);
        assertEquals(obstsorte, obsttorte.getObstsorte());
    }
    @Test
    public void testObstkuchenValid() {
         hersteller = new Hersteller("HerstellerTest");
        Collection<Allergen> allergen = Arrays.asList(Allergen.Gluten, Allergen.Haselnuss);
        int naehrwert = 400;
        Duration haltbarkeit = Duration.ofDays(3);
        BigDecimal preis = new BigDecimal("8");
        String obstsorte = "vanilla";
        Obstkuchen obstkuch = new Obstkuchen(hersteller, allergen, naehrwert, haltbarkeit, preis, obstsorte);

        assertEquals(hersteller, obstkuch.getHersteller());
        assertEquals(naehrwert, obstkuch.getNaehrwert());
        assertEquals(haltbarkeit, obstkuch.getHaltbarkeit());
        assertEquals(preis, obstkuch.getPreis());
        assertEquals(obstsorte, obstkuch.getObstsorte());
    }
    @Test
    public void testObstkuchenLeereConstructor() {
        Obstkuchen obstkuch = new Obstkuchen();
        assertNotNull(obstkuch);
        assertNull(obstkuch.getObstsorte());
    }
    @Test
    void testSetAndGetNaehrwert() {
        int naehrwert = 800;
        kuchenSpy.setNaehrwert(naehrwert);
        assertEquals(naehrwert, kuchenSpy.getNaehrwert());
    }

    @Test
    void testSetAndGetHaltbarkeit() {
        Duration haltbar = Duration.ofDays(3);
        kuchenSpy.setHaltbarkeit(haltbar);
        assertEquals(haltbar, kuchenSpy.getHaltbarkeit());
    }

    @Test
    void testSetAndGetAllergen() {
        Collection<Allergen> allergen = Arrays.asList(Allergen.Gluten, Allergen.Erdnuss);
        kuchenSpy.setAllergen(allergen);
        assertTrue(kuchenSpy.getAllergen().containsAll(allergen));
    }
  @Test
  void testNameSetter() {
       hersteller = new Hersteller();
      hersteller.setName("Lidl");
      assertEquals("Lidl", hersteller.getName(), "soll Lidl sein");
  }

    @Test
    void testGetKremsorte() {
         kuchen = new Kremkuchen();
        ((Kremkuchen) kuchen).setKremsorte("Vanilla");
        assertEquals("Vanilla", ((Kremkuchen) kuchen).getKremsorte(), "Kreme sorte stimmt nicht");
    }

    @Test
    void testSetHersteller() {
        Hersteller herstellerSoll = new Hersteller("NewHersteller");
        kuchenSpy.setHersteller(herstellerSoll);
        assertEquals(herstellerSoll, kuchenSpy.getHersteller(), "Hersteller stimmt nicht");
    }

    @Test
    void testGetFachnummer() {
         kuchen = mock(Kuchen.class);
        when(kuchen.getFachnummer()).thenReturn(4);

        assertEquals(4, kuchen.getFachnummer(), "Fach soll 4 sein");
    }

    @Test
    void testGetInspektionsdatum() {
         kuchen = mock(Kuchen.class);
        Date dateSoll = new Date();
        when(kuchen.getInspektionsdatum()).thenReturn(dateSoll);

        assertEquals(dateSoll, kuchen.getInspektionsdatum(), "Ungueltige Datum");
    }

    @Test
    void testSetPreis() {
         kuchen = spy(Kuchen.class);
        doNothing().when(kuchen).setPreis(any(BigDecimal.class));

        BigDecimal preis = new BigDecimal("15.50");
        kuchen.setPreis(preis);

        verify(kuchen).setPreis(preis);
    }

}