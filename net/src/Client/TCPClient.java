package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient implements Kommunikation {
    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private final String addressServ;
    private final int portServ;
    private boolean isConnect = false;
    public TCPClient(String addressServ, int portServ) {
        this.addressServ = addressServ;
        this.portServ = portServ;
        connect();
    }

    private void connect() {
        try {
            socket = new Socket(addressServ, portServ);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            isConnect = true;
        } catch (IOException e) {
            isConnect = false;
            throw new RuntimeException("Verbindung mit dem Server unmöglich", e);
        }
    }
    public boolean testVerbindung() {
        return isConnect;
    }

    @Override
    public String sendCommand(String command) {
        printWriter.println(command);
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Fehler", e);
        }
    }

    @Override
    public void close() {
        try {
            if (bufferedReader != null) bufferedReader.close();
            if (printWriter != null) printWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
