package TCP;


import Kontroller.Automat;
import Modell.*;
import kuchen.Allergen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.EnumSet;
import java.util.List;


import static java.lang.System.out;
public class ServerTCP {
    public static Automat automat;
    int capacity = 4;
    private static final int PORT = 2401;

    public ServerTCP( int capacity) {

        this.automat = new Automat(this.capacity);
    }

    public void start() {
        try (ServerSocket socketServ = new ServerSocket(PORT)) {
            out.println("Server startet auf port " + PORT);

            while (true) {

                try (Socket socketCli = socketServ.accept();
                     PrintWriter printWriter = new PrintWriter(socketCli.getOutputStream(), true);
                     BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socketCli.getInputStream()))) {
                    String eingabe;
                    while ((eingabe = bufferedReader.readLine()) != null) {
                        String message = processCommand(eingabe);
                        printWriter.println(message);
                    }
                } catch (IOException e) {
                    out.println("Fehler");
                    out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            out.println("Fehler");
            out.println(e.getMessage());
        }
    }

    public static String processCommand(String command) {
        String[] comm = command.split(";");
        String action = comm[0];

        switch (action) {
            case "ADDHERSTELLER":
                if (comm.length < 2)
                    return "Fehler Hersteller Name fehlt";
                String nameH = comm[1];
                Hersteller hersteller = automat.addHersteller(nameH);
                return hersteller != null ? "ADD erfolgreich " : "Fehler Hersteller fehlt";

            case "ADDKUCHEN":
                if (comm.length < 6)
                    return "FEHLER Parametern ungenuegend";

                String herstellerName = comm[1];
                int naehwerte = Integer.parseInt(comm[2]);
                BigDecimal preis = new BigDecimal(comm[3]);
                String haltbarkeitStr = comm[4];
                LocalDate haltbarkeitDate = LocalDate.parse(haltbarkeitStr);
                Duration haltbarkeit = Duration.between(LocalDateTime.now(), haltbarkeitDate.atStartOfDay());
                String valAllergene = comm[5];
                EnumSet<Allergen> allergene = EnumSet.noneOf(Allergen.class);

                for (String allergen : valAllergene.split(",")) {
                    try {
                        allergene.add(Allergen.valueOf(allergen.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        return "FEHLER Allegen unbekannt: " + allergen;
                    }
                }

                String sorte = comm[6];
                Kuchen kuchen;
                 hersteller = automat.findHersteller(herstellerName);

                if (hersteller == null) return "FEHLER Hersteller unbekannt";

                switch (sorte.toLowerCase()) {
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
                        return "ERROR Kuchen type unbekannt";
                }

                boolean isAdd = automat.addKuchen(kuchen);
                return isAdd ? "SUCCESS Kuchen hinzugefuegt" : "FEHLER Problem beim Add";


            case "READHERSTELLER":
                List<Hersteller> herstellers = automat.getHerstellerList();
                StringBuilder herstellerBuilder = new StringBuilder();
                for (Hersteller hersteller1 : herstellers) {
                    herstellerBuilder.append(hersteller1.getName()).append("\n");
                }
                return herstellerBuilder.toString();

            case "READKUCHEN":
                List<Kuchen> kuchens = automat.Auflisten();
                StringBuilder kuchenBuilder = new StringBuilder();
                for (Kuchen kuchen1 : kuchens) {
                    if(kuchen1 != null) {
                        kuchenBuilder.append(kuchen1.toString()).append("\n");
                    }
                }
                return kuchenBuilder.toString();

            case "READALLERGEN":
                List<Allergen> lagerAllergene = automat.lagerAllergen();
                List<Allergen> noLagerAllergene = automat.noLagerAllergen();

                StringBuilder allergenIst = new StringBuilder("SUCCESS LagerAllergene:" );
                for (Allergen allergen : lagerAllergene) {
                    allergenIst.append(allergen.name()).append(",");
                }
                allergenIst.append(" NoLagerAllergene:");
                for (Allergen allergen : noLagerAllergene) {
                    allergenIst.append(allergen.name()).append(",");
                }
                return allergenIst.toString();

            case "DELETEHERSTELLER":
                if (comm.length < 2) return "FEHLER Name unbekannt";
                String nameToDelete = comm[1];
                Hersteller herstellerToDelete = automat.findHersteller(nameToDelete);
                if (herstellerToDelete != null) {
                    automat.deleteHersteller(herstellerToDelete);
                    return "SUCCESS Hersteller wurde geloescht";
                } else {
                    return "Fehler Hersteller unbekannt";
                }
            case "DELETEKUCHEN":
                if (comm.length < 2) return "FEHLER Fach fehlt";
                try {
                    int fach = Integer.parseInt(comm[1]);
                    boolean isDelete = automat.deleteKuchen(fach);
                    return isDelete ? "SUCCESS Kuchen geloescht" : "ERROR Problem beim Loeschen";
                } catch (NumberFormatException e) {
                    return "ERROR Problem beim DELETE";
                }

            case "SAVEJBP":
                if (comm.length < 2) return "FEHLER Datei fehlt";
                String fileJBP = comm[1];
                try {
                    automat.saveJBP(fileJBP);
                    return  "SUCCESS Zustand gespeichert";
                } catch (Exception e) {
                    return  "ERROR Problem beim Save  " + e.getMessage();
                }


            case "LADEJBP":
                if (comm.length < 2) return "Fehler Datei fehlt";
                fileJBP = comm[1];
                try {
                    Automat automatJBP = automat.ladenJBP(fileJBP);
                    if (automatJBP != null) {
                        return  "SUCCESS Zustand geladen mit JBP";
                    } else {
                        return  "Fehler Problem";
                    }
                } catch (Exception e) {
                    return  "Fehler Problem beim LADEN " + e.getMessage();
                }

            case "SAVEJOS":
                if (comm.length < 2) return "FEHLER Datei fehlt";
                String fileJOS = comm[1];
                try {
                    automat.saveJOS(fileJOS);
                    return "SUCCESS Zustand gespeichert";
                } catch (Exception e) {
                    return "FEHLER Problem beim Save " + e.getMessage();
                }

            case "LADEJOS":
                if (comm.length < 2) return "FEHLER Datei fehlt";
                fileJOS = comm[1];
                try {
                    Automat automatJOS = automat.ladenJOS(fileJOS);
                    if (automatJOS != null) {
                        return "SUCCESS Zustand geladen";
                    } else {
                        return "FEHLER Problem beim Laden";
                    }
                } catch (Exception e) {
                    return "Fehler Problem beim Laden: " + e.getMessage();
                }


            case "UPDATE":
                if (comm.length < 2) return "FEHLER Parametrn ungenuegend";
                int fach = Integer.parseInt(comm[1]);
                Kuchen kuchenToInspekt = automat.getKuchenByFach(fach);
                if (kuchenToInspekt != null) {
                    automat.aendern(kuchenToInspekt);
                    return "SUCCESS kuchen wurde inspektiert";
                } else {
                    return "Fehler Kuchen unbekannt";
                }

            case "GETCAPACITY":
                return "1 " + automat.getMaxSize();

            default:
                return "ERROR Befehl ungueltig";
        }
    }
}
