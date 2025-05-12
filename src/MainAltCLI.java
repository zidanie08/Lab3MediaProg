import Cli.AlternativCLI;
import Kontroller.Automat;
import observers.AllergenObserver;

import java.io.PrintWriter;
import java.util.Scanner;

public class MainAltCLI { // Loeschen von Herstellern und auflisten der Allergene deaktiviert
    public static void main(String[] args) {
        Automat automat = new Automat(3);
        AllergenObserver allergenObserver = new AllergenObserver();
        automat.addObserverAllergenes(allergenObserver);
        AlternativCLI cli = new AlternativCLI(automat);
        cli.actionAltCLI();

    }
}
