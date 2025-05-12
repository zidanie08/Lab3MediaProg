/*package Client;

import TCP.ServerTCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler  implements Runnable {


    private Socket socketClient;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public ClientHandler(Socket socketClient) {
        this.socketClient = socketClient;
    }

    @Override
    public void run() {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            printWriter = new PrintWriter(socketClient.getOutputStream(), true);
            String eingabe;

            while ((eingabe = bufferedReader.readLine()) != null) {
                String antwort = ServerTCP.processCommand(eingabe);
                printWriter.println(antwort);
            }
        } catch (IOException e) {
            System.err.println("Fehler Bei der Verbindung mit Port");
            System.err.println(e.getMessage());
        } finally {
            try {
                bufferedReader.close();
                printWriter.close();
                socketClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
*/