package Client;

public interface Kommunikation {
    String sendCommand(String command);
    void close();

    boolean testVerbindung();
}
