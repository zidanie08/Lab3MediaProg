//import Client.ClientHandler;
import TCP.ServerTCP;
import UDP.ServerUDP;

import java.util.Scanner;



public class MainServer {

        public static void main(String[] args) {

            Scanner sc = new Scanner(System.in);

            System.out.println("gib protocol(TCP oder UDP) und Kapazitaet ein :");
            String eingabe = sc.nextLine();
            String[] parts = eingabe.split(" ");

            if (parts.length < 2) {
                System.out.println("Eingabe ungueltig" );
                return;
            }

            String protocol = parts[0].toUpperCase();
            int kapazitaet;
            try {
                kapazitaet = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                System.out.println("Kapazitaet ungueltig");
                return;
            }

            switch (protocol) {
                case "TCP":
                    runTcpServer(kapazitaet);
                    break;
                case "UDP":
                    runUdpServer(kapazitaet);
                    break;
                default:
                    System.out.println("Protocol unbekannt");
                    break;
            }
        }

    private static void runTcpServer(int kapazitaet) {
        ServerTCP serverTCP = new ServerTCP(kapazitaet);
        System.out.println("Server TCP läuft mit Kapazitaet =  " + kapazitaet);
        new Thread(serverTCP::start).start();
    }

    private static void runUdpServer(int kapazitaet) {
        ServerUDP serverUDP = new ServerUDP(kapazitaet);
        System.out.println("Server UDP läuft mit Kapazitaet =  " + kapazitaet);
        new Thread(serverUDP::start).start();
    }
    }
