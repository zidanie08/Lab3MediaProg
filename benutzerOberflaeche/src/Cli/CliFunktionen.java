package Cli;
import Client.Kommunikation;
import Kontroller.Automat;
import Modell.*;
import kuchen.Allergen;
import observers.AllergenObserver;
import observers.KapazitaetObserver;

import java.math.BigDecimal;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;


public class CliFunktionen  {

    static int n = 5;
    public Automat automat;
    private Kommunikation komm;
    Scanner sc = new Scanner(System.in);
    public CliFunktionen(Automat automat) {
        this.automat = automat;
    }
    public CliFunktionen(Automat automat,  Kommunikation komm) {
        this.automat = automat;
        this.komm= komm;
    }
    public void action() {

        String input;
        System.out.println("             ");
        System.out.print("HTW AUTOMAT\n");
        System.out.println("*******");
        do {
            System.out.println("•\t:c insert modus\n" +
                    "•\t:d delete modus\n" +
                    "•\t:r view modus\n" +
                    "•\t:u Inspect modus\n"+
                            "•\t:p Persistens modus\n" + "•\t:q Beenden(Back to Menu)\n");
            input = sc.next().trim();

            switch (input) {
                case ":c":
                    add();
                    System.out.println(automat.Auflisten());
                    break;
                case ":d":
                   delete();
                    System.out.println(automat.Auflisten());

                    break;
                case ":r":
                    read();
                    break;
                case ":u":
                    Aendern();
                    System.out.println(automat.Auflisten());
                    break;
                case":p":
                    persistModus();
                    break;
                case":q":
                    progEnd();
                    break;

                default:
                    System.out.println("falsche Eingabe, versuchen Sie es erneut");
            }

        } while (!":q".equals(input));

    }

    public void progEnd(){
        System.out.println("Programm Speichern ? ja/nein");
        String reponse = sc.nextLine();
        sc.nextLine();

        switch (reponse) {
            case "ja":
                persistModus();
                break;
            case "nein":
                System.out.println("Programm beendet.");
                action();
                break;
            default:
                System.out.println("Befehl ungueltig ");
        }
    }




    public void add() {
        System.out.println("Waehle eine option 1 - Add Hersteller or 2 - Add Kuchen");
        String choice = sc.next();
        sc.nextLine();

        switch (choice) {
            case "1":
                addHersteller();
                break;
            case "2":
                addKuchen();
                break;
            default:
                System.out.println("Ungueltige Auswahl");
                action();
        }
    }

    void addHersteller() {
        System.out.println("Hersteller Name eingeben :");
       // Scanner sc = new Scanner(System.in);
        String nameH = sc.next();
        String command = "ADDHERSTELLER;" + nameH;
        System.out.println(nameH);
        System.out.println(komm.sendCommand(command));


        try {
            automat.addHersteller(nameH);
            System.out.println("Hersteller erfolgreich hinzugefügt");
            System.out.println(automat.getHerstellerList());
        } catch (Exception e) {
            exception(e);
            action();
        }
    }

    private void addKuchen() {
        System.out.println("Informationen eingeben (Hersteller/Naehwerte/Preis/Haltbarkeit/Allergene/Sorte): ");
        System.out.println("Für Allergene Sie können eingeben: 1-Gluten, 2-Erdnuss, 3-Haselnuss, 4-Sesamsamen");
        System.out.println("mehrere Allergene mit (,) trennen");

        String info = sc.nextLine().toLowerCase();
        String[] infoWert = info.split("/");

        if (infoWert.length < 6) {
            System.out.println(" Anzahl von Parametern ungueltig.");
            return;
        }

        String name = infoWert[0];
        Hersteller hersteller = automat.findHersteller(name);

        if (hersteller == null) {
            System.out.println("Hersteller unbekannt. Bitte erst Hersteller einfuegen.");
            return;
        }

        int naehwerte = Integer.parseInt(infoWert[1]);
        BigDecimal preis = new BigDecimal(infoWert[2]);
        String haltbarkeit = infoWert[3];

        try {
            String[] detailsDate = haltbarkeit.split("-");
            LocalDate localDate = LocalDate.now();
            LocalDate expiration = LocalDate.of(Integer.parseInt(detailsDate[0]), Integer.parseInt(detailsDate[1]), Integer.parseInt(detailsDate[2]));
            if (expiration.isBefore(LocalDate.now())) {
                System.out.println("Haltbarkeitsdatum kann nicht in der Vergangenheit liegen.");
                return;
            }
            LocalDateTime addDate = localDate.atStartOfDay();
            LocalDateTime expirationTime = expiration.atStartOfDay();
            Duration haltbar = Duration.between(addDate, expirationTime);

            String allergenInput = infoWert[4];
            String[] allergenNumbers = allergenInput.split(",");
            EnumSet<Allergen> gewaehlteAllerg = EnumSet.noneOf(Allergen.class);

            for (String allergenNumber : allergenNumbers) {
                int num = Integer.parseInt(allergenNumber);
                if (num >= 1 && num <= Allergen.values().length) {
                    gewaehlteAllerg.add(Allergen.values()[num - 1]);
                } else {
                    System.out.println("Allergen-Nummer ungueltig: " + num);
                    return;
                }
            }

            String kuchenArt = infoWert[5];
            handleSorte(hersteller, gewaehlteAllerg, naehwerte, haltbar, preis, kuchenArt);
            String command = String.format("ADDKUCHEN;%s;%d;%s;%s;%s", name, naehwerte, preis, haltbarkeit, "allergenInput");
            System.out.println(komm.sendCommand(command));
        } catch (DateTimeParseException e) {
            System.out.println("Ungueltiges Datum-format. Verwenden Sie das Format 'yyyy-MM-dd'.");
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("Datum ungueltig");
        } finally {
            action();
        }
    }

    private void handleSorte(Hersteller hersteller, EnumSet<Allergen> gewaehlteAllerg, int naehwerte, Duration haltbar, BigDecimal preis, String kuchenArt) {
        // Scanner sc = new Scanner(System.in);
        switch (kuchenArt.toLowerCase()) {
            case "kremkuchen":
                addKremkuchen(hersteller, gewaehlteAllerg, naehwerte, haltbar, preis);
                break;
            case "obstkuchen":
                addObsttkuchen(hersteller, gewaehlteAllerg, naehwerte, haltbar, preis);
                break;
            case "obsttorte":
                addObsttorte(hersteller, gewaehlteAllerg, naehwerte, haltbar, preis);
                break;
            default:
                System.out.println("Ungueltige Auswahl");
                action();
                break;
        }
    }


    void addKremkuchen(Hersteller hersteller, EnumSet<Allergen> gewaehlteAllerg, int naehrwert, Duration haltbar, BigDecimal preis) {
        //Scanner sc = new Scanner(System.in);
        System.out.println("Kreme-Sorte ist : ");
        String kreme = sc.next();
        Kremkuchen kremkuchen = new Kremkuchen(hersteller, gewaehlteAllerg, naehrwert, haltbar, preis, kreme);

        try {
            automat.addKuchen(kremkuchen);
            // System.out.println("Kremkuchen erfolgreich hinzugefügt");
            System.out.println(kremkuchen);
        } catch (Exception e) {
            exception(e);
        }

    }


    private void addObsttkuchen(Hersteller hersteller, EnumSet<Allergen> gewaehlteAllerg, int naehwerte, Duration haltbar, BigDecimal preis) {
        // Scanner sc = new Scanner(System.in);
        System.out.println("Obst sorte ist : ");
        String obst = sc.next();
        Obstkuchen obstkuchen = new Obstkuchen(hersteller, gewaehlteAllerg, naehwerte, haltbar, preis, obst);

        try {
            automat.addKuchen(obstkuchen);
            System.out.println(obstkuchen);
        } catch (Exception e) {
            exception(e);
        }

    }

    private void addObsttorte(Hersteller hersteller, EnumSet<Allergen> gewaehlteAllerg, int naehwerte, Duration haltbar, BigDecimal preis) {

        System.out.println("Kreme-Sorte ist : ");
        String kremeOt = sc.next();
        System.out.println("Obst-Sorte ist : ");
        String obstOt = sc.next();
        Obsttorte obsttorte = new Obsttorte(hersteller, gewaehlteAllerg, naehwerte, haltbar, obstOt, kremeOt, preis);

        try {
            automat.addKuchen(obsttorte);
            System.out.println(obsttorte);
        } catch (Exception e) {
            exception(e);
        }

    }

    private void exception(Exception e) {
        System.err.println("Fehler : " + e.getMessage());
        e.printStackTrace();
    }


    public void read() {

        System.out.println("1 - read Hersteller \n 2 - read Kuchen \n 3 - read Allergen");
        int read = sc.nextInt();
        String command = null;
        if (read == 1) {
            Map<Hersteller, Integer> kuchenProHersteller = automat.anzahlGelagerterKuchen();

            for (Map.Entry<Hersteller, Integer> val : kuchenProHersteller.entrySet()) {
                System.out.println("Hersteller: " + val.getKey().getName() + ", Anzahl Kuchen: " + val.getValue());
            }
            command = "READHERSTELLER";

        } else if (read == 2) {
            System.out.println(automat.Auflisten());
            command = "READKUCHEN";
        }
        if (read == 3) {
            System.out.println("enthalten(i) Allergen in Automat:");
            System.out.println(automat.lagerAllergen());
            System.out.println("\nnicht enthalten(e) Allergen im Automat:");
            System.out.println(automat.noLagerAllergen());
            command = "READALLERGEN";

        } else {
            action();
        } String response = komm.sendCommand(command);
        System.out.println(response);

    }

    public void delete() {
        int fach = 0;
        Scanner sc = new Scanner(System.in);
        System.out.println("1 - Herteller \n 2 -  Kuchen");
        int input = sc.nextInt();
        if (input == 2) {
            System.out.println("Fachnummer ist : ");
             fach = sc.nextInt();
            automat.deleteKuchen(fach);
            System.out.println("Kuchen erfolgreich gelöscht");
        } else if (input == 1) {
            System.out.println("Hersteller Name zum Löschen eingeben: ");
            String herstellerName = sc.next();
            Hersteller herstellerToDelete = automat.findHersteller(herstellerName);
            if (herstellerToDelete != null) {
                automat.deleteHersteller(herstellerToDelete);
                System.out.println("Hersteller erfolgreich gelöscht");
            } else {
                System.out.println("Hersteller nicht gefunden.");
            }
            String command;
            if (input == 2) {
                command = "DELETEKUCHEN;" + fach;
            } else {
                command = "DELETEHERSTELLER;" + herstellerName;
            }
            System.out.println(komm.sendCommand(command));
        } else {
            System.out.println("Ungültige Auswahl");
            action();
        }
        persistModus();
    }

    public void Aendern() {
        System.out.println("Waehlen Sie die Fachnummer einen Kuchen zum inspekt aus:");
        List<Kuchen> kuchens = automat.Auflisten();
        for (int position = 0; position < kuchens.size(); position++) {
            if (kuchens.get(position) != null) {
                System.out.println(position +   "  fuer " + kuchens.get(position).toString());
            }
        }
        int fach = sc.nextInt();
        if (fach >= 0 && fach < kuchens.size() && kuchens.get(fach) != null) {
            Kuchen kuchen = kuchens.get(fach);
            automat.aendern(kuchen);
            System.out.println("Kuchen wurde inspektiert.");
            System.out.println(automat.Auflisten());
            String command = String.format("UPDATE;%d", fach);
            String response = komm.sendCommand(command);
            System.out.println(response);
        } else {
            System.out.println(" Auswahl ungueltig.");
        }
        action();
    }

    public void persistModus() {
        // Scanner sc = new Scanner(System.in);
        System.out.println("Persistenzmodus:");
        System.out.println("1- saveJOS speichert mittels JOS");
        System.out.println("2- loadJOS lädt mittels JOS");
        System.out.println("3- saveJBP speichert mittels JBP");
        System.out.println("4- loadJBP lädt mittels JBP");
        System.out.println("waehlen Sie eine Option:");

        String persis = sc.next();
        switch (persis) {
            case "1":
                saveMitJOS();
                break;
            case "2":
                ladenJOS();
                break;
            case "3":
                saveMitJBP();
                break;
            case "4":
                ladenJBP();
                break;
            default:
                System.out.println("Ungueltige Auswahl");
                break;
        }
        action();
    }

    private void ladenJOS() {
        String fileJOS = "jos.txt";
        try {
            Automat automatL = automat.ladenJOS(fileJOS);
            if (automatL != null) {
                this.automat = automatL;
                System.out.println("Daten erfolgreich aus JOS geladen.");
                String command = "LADEJOS;" + fileJOS;
                System.out.println(komm.sendCommand(command));
            } else {
                System.out.println("Fehler beim Laden der Daten.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ladenJBP() {
        String fileJBP = "jbp.txt";
        try {
            Automat automatL = automat.ladenJBP(fileJBP);
            if (automatL != null && automatL.getKuchenList() != null) {
                this.automat = automatL;
                for (int stelle = 0; stelle < this.automat.getKuchenList().size(); stelle++) {
                    if (this.automat.getKuchenList().get(stelle) == null) {
                        System.out.println("Position" +stelle + "war frei");
                    } else {
                        System.out.println("Position besetzt");
                    }
                }
                String command = "LADEJBP;" + fileJBP;
                System.out.println(komm.sendCommand(command));
            } else {
                System.out.println("Fehler beim Laden");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void saveMitJOS() {
        try {
            String fileName= "jos.txt";
            automat.saveJOS( fileName);
            System.out.println("Zustand erfolgreich mit JOS gespeichert.");
            String command = "SAVEJOS;" + fileName;
            System.out.println(komm.sendCommand(command));
        } catch (Exception e) {
            System.err.println("Fehler beim Save mit JOS: " + e.getMessage());
            e.printStackTrace();
        }action();
    }

    public void saveMitJBP() {
        String fileName= "jbp.txt";
        try {
            automat.saveJBP( fileName);
            System.out.println("Zustand erfolgreich mit JBP gespeichert.");
            String command = "SAVEJBP;" + fileName;
            System.out.println(komm.sendCommand(command));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Fehler beim Speichern  mit JBP.");
        }action();
    }


}

