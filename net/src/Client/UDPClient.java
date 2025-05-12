package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient implements Kommunikation {

    private DatagramSocket socket;
    private InetAddress inetAddress;
    private final int portServ;
    private final String addressServ;
    private boolean isConnect = false;
    public UDPClient(String addressServ, int portServ) {
        this.portServ = portServ;
        this.addressServ = addressServ;
        try {
            socket = new DatagramSocket();
            inetAddress = InetAddress.getByName(addressServ);
            isConnect = true;
        } catch (Exception e) {
            isConnect = false;
            throw new RuntimeException("Fehler mit UDP Client", e);
        }
    }
    public boolean testVerbindung() {
        return isConnect;
    }

    @Override
    public String sendCommand(String command) {
        try {
            byte[] bytes = command.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, inetAddress, portServ);
            socket.send(datagramPacket);

            byte[] bytes1 = new byte[1024];
            datagramPacket = new DatagramPacket(bytes1, bytes1.length);
            socket.receive(datagramPacket);
            return new String(datagramPacket.getData(), 0, datagramPacket.getLength());
        } catch (IOException e) {
            throw new RuntimeException("Fehler", e);
        }
    }

    @Override
    public void close() {
        if (socket != null) {
            socket.close();
        }
    }
}
