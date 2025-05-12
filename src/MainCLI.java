import Cli.AlternativCLI;
import Cli.CliFunktionen;
import Client.Kommunikation;
import Client.TCPClient;
import Client.UDPClient;
import Kontroller.Automat;

import java.util.Scanner;

public class MainCLI {

    public static void main(String[] args) {
        int port = 2401;
         int kapazitaet = 4;
        Scanner scanner = new Scanner(System.in);

        System.out.println("gib 'tcp' / 'udp' fuer net-modus oder ein Nummer fuer die kapazitaet ein:");
        String eingabe = scanner.nextLine().trim();
        Automat automat;
        Kommunikation komm ;

        try {
            kapazitaet = Integer.parseInt(eingabe);
            automat = new Automat(kapazitaet);
            System.out.println("Local Modus mit Kapazitaet = " + kapazitaet);
            AlternativCLI cli = new AlternativCLI(automat);
            cli.actionAltCLI();
        } catch (NumberFormatException e) {
            String serverAddress = "localhost";


            switch (eingabe.toUpperCase()) {
                case "TCP":
                    System.out.println(" TCP Client gestartet");
                    komm = new TCPClient(serverAddress, port);
                    break;
                case "UDP":
                    System.out.println(" UDP Client gestartet");
                    komm = new UDPClient(serverAddress, port);
                    break;
                default:
                    System.out.println("Eingabe nicht korrekt");
                    return;
            }

            if (komm != null) {
                System.out.println("Verbindung läuft");
                String kapzitaetIs = komm.sendCommand("GETCAPACITY");
                String[] antwort = kapzitaetIs.split(" ");
                System.out.println("test " +Integer.parseInt(antwort[0]));
                if (Integer.parseInt(antwort[0])==1) {
                    System.out.println("ok");
                    int serverKapazitaet = Integer.parseInt(antwort[1]);
                    automat = new Automat(serverKapazitaet);
                    CliFunktionen cliNet = new CliFunktionen(automat, komm);
                    cliNet.action();
                } else {
                    System.out.println("Fehler test");
                }
            } else {
                System.out.println("Fehler bei der Verbindung");
            }
        }
    }
    }

