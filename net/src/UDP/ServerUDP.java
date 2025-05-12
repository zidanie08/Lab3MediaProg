package UDP;

import Kontroller.Automat;
import Modell.*;
import kuchen.Allergen;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.EnumSet;
import java.util.List;

public class ServerUDP {
    private static final int PORT = 2401;
    private Automat automat;

    public ServerUDP(int capacity) {
        this.automat = new Automat(capacity);
    }

    public void start() {
        try (DatagramSocket datagramSocket = new DatagramSocket(PORT)) {
            System.out.println("UDP Server startet auf port " + PORT + " mit Kapazitaet " + automat.getMaxSize());

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(datagramPacket);
                String receive = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                String antwortStr = processCommand(receive);
                byte[] antwortByt = antwortStr.getBytes();
                DatagramPacket antwort = new DatagramPacket(antwortByt, antwortByt.length, datagramPacket.getAddress(), datagramPacket.getPort());
                datagramSocket.send(antwort);
            }
        } catch (SocketException e) {
            System.err.println("SocketException: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }
    private String processCommand(String command) {
        String[] comm = command.split(";");
        String action = comm[0];

        switch (action) {
            case "ADDHERSTELLER":
                if (comm.length < 2) return "FEHLER Hersteller Name fehlt";
                String name = comm[1];
                Hersteller hersteller = automat.addHersteller(name);
                return hersteller != null ? "ERFOLG Hersteller hinzugefuegt" : "FEHLER Hersteller hinzufuegen fehlt";

            case "ADDKUCHEN":
                if (comm.length < 6) return "FEHLER Daten nicht genuegend";

                String herstellerName = comm[1];
                int naehwerte = Integer.parseInt(comm[2]);
                BigDecimal preis = new BigDecimal(comm[3]);
                String haltbarkeitInput = comm[4];
                LocalDate haltbarkeitDate = LocalDate.parse(haltbarkeitInput);
                Duration haltbarkeit = Duration.between(LocalDateTime.now(), haltbarkeitDate.atStartOfDay());
                String allergeneInput = comm[5];
                EnumSet<Allergen> allergene = EnumSet.noneOf(Allergen.class);

                for (String allergen : allergeneInput.split(",")) {
                    try {
                        allergene.add(Allergen.valueOf(allergen.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        return "FEHLER  allergen ungueltig: " + allergen;
                    }
                }

                String kuchenSorte = comm[6];
                Kuchen kuchen;
                hersteller = automat.findHersteller(herstellerName);

                if (hersteller == null) return "FEHLER Hersteller unbekannt";

                switch (kuchenSorte.toLowerCase()) {
                    case "kremkuchen":
                        kuchen = new Kremkuchen(hersteller, allergene, naehwerte, haltbarkeit, preis, "Kreme");
                        break;
                    case "obstkuchen":
                        kuchen = new Obstkuchen(hersteller, allergene, naehwerte, haltbarkeit, preis, "Obst");
                        break;
                    case "obsttorte":
                        kuchen = new Obsttorte(hersteller, allergene, naehwerte, haltbarkeit, "Obst", "Kreme", preis);
                        break;
                    default:
                        return "FEHLER";
                }

                boolean isAdd = automat.addKuchen(kuchen);
                return isAdd ? "SUCCESS Kuchen hinzugefuegt" : "FEHLER Problem beim Add";


            case "READHERSTELLER":
                List<Hersteller> herstellerListe = automat.getHerstellerList();
                StringBuilder herstellerListBuilder = new StringBuilder("ERFOLG Herstellerliste:");
                for (Hersteller h : herstellerListe) {
                    herstellerListBuilder.append(h.getName()).append(",");
                }
                return herstellerListBuilder.toString();


            case "READKUCHEN":
                List<Kuchen> kuchenListe = automat.Auflisten();
                StringBuilder kuchenBuilder = new StringBuilder("ERFOLG Kuchenliste:");
                for (Kuchen kuchen1 : kuchenListe) {
                    if (kuchen1 != null) {
                        kuchenBuilder.append(kuchen1.toString()).append(",");
                    }
                }
                return kuchenBuilder.toString();

            case "DELETEHERSTELLER":
                if (comm.length < 2) return "FEHLER Hersteller Name fehlt";
                String nameToDelete = comm[1];
                Hersteller herstellerToDelete = automat.findHersteller(nameToDelete);
                if (herstellerToDelete != null) {
                    automat.deleteHersteller(herstellerToDelete);
                    return "SUCCESS Hersteller geloescht";
                } else {
                    return "ERROR Hersteller unbekannt";
                }
            case "DELETEKUCHEN":
                if (comm.length < 2) return "FEHLER Fachnummer fehlt";
                try {
                    int fachnummer = Integer.parseInt(comm[1]);
                    boolean deleted = automat.deleteKuchen(fachnummer);
                    return deleted ? "SUCCESS Kuchen geloescht" : "ERROR Problem beim Loeschen";
                } catch (NumberFormatException e) {
                    return "FEHLER";
                }

            case "LADEJOS":
                if (comm.length < 2) return "FEHLER: Dateiname fehlt";
                String fileJOS = comm[1];
                try {
                    Automat automatJOS = automat.ladenJOS(fileJOS);
                    return automatJOS != null ? "ERFOLG" : "FEHLER! Zustand konnte nicht aus JOS geladen werden";
                } catch (Exception e) {
                    return "FEHLER: " + e.getMessage();
                }

            case "SAVEJOS":
                if (comm.length < 2) return "FEHLER: Dateiname fehlt";
                fileJOS = comm[1];
                try {
                    automat.saveJOS(fileJOS);
                    return "ERFOLG: Zustand in JOS gespeichert";
                } catch (Exception e) {
                    return "FEHLER: " + e.getMessage();
                }

            case "LADEJBP":
                if (comm.length < 2) return "FEHLER Dateiname fehlt";
                String fileJBP = comm[1];
                try {
                    Automat automatJBP = automat.ladenJBP(fileJBP);
                    return automatJBP != null ? "ERFOLG Zustand aus JBP geladen" : "FEHLER Zustand konnte nicht aus JBP geladen werden";
                } catch (Exception e) {
                    return "FEHLER;Ausnahme während JBP Laden: " + e.getMessage();
                }

            case "SAVEJBP":
                if (comm.length < 2) return "FEHLER Dateiname fehlt";
                fileJBP = comm[1];
                try {
                    automat.saveJBP(fileJBP);
                    return "ERFOLG Zustand in JBP gespeichert";
                } catch (Exception e) {
                    return "FEHLER Ausnahme während JBP Speichern: " + e.getMessage();
                }

            case "UPDATE":
                if (comm.length < 2) return "FEHLER Anzahl von Daten ungenuegend";
                int fach = Integer.parseInt(comm[1]);
                Kuchen kuchenToInspekt = automat.getKuchenByFach(fach);
                if (kuchenToInspekt != null) {
                    return "ERFOLG Inspektionsdatum aktualisiert";
                } else {
                    return "FEHLER Kuchen nicht gefunden";
                }

            case "GETCAPACITY":
                return "1 " + automat.getMaxSize();

            default:
                return "ERROR Befehl ungueltig";
        }
    }
}
