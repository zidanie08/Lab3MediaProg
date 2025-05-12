package Kontroller;
import Modell.Hersteller;
import Modell.Kuchen;
import kuchen.Allergen;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;

public class  Automat extends Observable implements Serializable  {
    static final long serialVersionUID = 1L;
     public  int maxSize;
   private  List<Hersteller> herstellerList = new LinkedList<>();
    private  List<Kuchen> kuchenList ;
    public Automat(){
    }

    public  Automat(int maxSize) {
        this.maxSize = maxSize;
        kuchenList = new ArrayList<>(maxSize);
        for(int i = 0; i< maxSize; i++){
            kuchenList.add(null);
        }
    }

    public synchronized List<Hersteller> getHerstellerList() {
        return herstellerList;
    }

    public synchronized int getMaxSize() {
        return maxSize;
    }

    public synchronized void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public synchronized List<Kuchen> getKuchenList() {
        return kuchenList;
    }
    public synchronized List<Kuchen> Auflisten() {
        return new ArrayList<>(this.kuchenList);
    }
     Map<Hersteller, Integer> kuchenProHersteller = new HashMap<>();

    public synchronized  Map<Hersteller, Integer> anzahlGelagerterKuchen() {
        return new HashMap<>(kuchenProHersteller);

    }

    public synchronized void updateKuchenProHersteller() {
        kuchenProHersteller.clear();
        for (Hersteller hersteller : herstellerList) {
            kuchenProHersteller.put(hersteller, 0);
        }

        for (Kuchen kuchen : kuchenList) {
            if (kuchen == null) continue;

            Hersteller hersteller = (Hersteller) kuchen.getHersteller();
            kuchenProHersteller.put(hersteller, kuchenProHersteller.getOrDefault(hersteller, 0) + 1);
        }
    }

    public synchronized boolean addKuchen(Kuchen kuchen) {
        if (isAutomatFull()) return false;

        validateHersteller(kuchen);

        int fach = getFreieStelle();
        insertKuchen(kuchen, fach);

        return true;
    }

    private boolean isAutomatFull() {
        if (nullPosition() == 0) {
            System.out.println("Automat Voll!");
            setChanged();
            notifyObservers("Automat voll");
            return true;
        }
        return false;
    }

    private void validateHersteller(Kuchen kuchen) {
        Hersteller hersteller = (Hersteller) Objects.requireNonNull(kuchen.getHersteller(), "Hersteller null");
        if (!herstellerList.contains(hersteller)) {
            throw new IllegalArgumentException("Hersteller unbekannt");
        }
    }

    private int getFreieStelle() {
        final List<Integer> stelle = findFreePosition(kuchenList);
        if (stelle.isEmpty()) throw new IllegalStateException("Kein freier Platz im Automat");
        return stelle.get(0);
    }

    private void insertKuchen(Kuchen kuchen, int fach) {
        kuchen.setFachnummer(fach);
        Date now = new Date();
        kuchen.setEinfuegeDatum(now);
        kuchen.setInspektionsdatum(now);
        kuchenList.set(fach, kuchen);
        setChanged();
        notifyObservers("Kuchen wurde hinzugefuegt: " + kuchen);
        updateKuchenProHersteller();
    }


    /*public synchronized boolean addKuchen(Kuchen kuchen) {
        if (this.nullPosition() == 0) {
            System.out.println("Automat Voll! Einfuegen nicht mehr möglich !!!!");
            notifyObservers("Automat voll");
            setChanged();
            return false;
        }

        Hersteller hersteller = (Hersteller) Objects.requireNonNull(kuchen.getHersteller(), " Hersteller null");
        if (!herstellerList.contains(hersteller)) {
            throw new IllegalArgumentException("Hersteller unbekannt: ");
        }

        List<Integer> stelle = findFreePosition(kuchenList);
        if (stelle.isEmpty()) {
            return false;
        }

        int fach = stelle.get(0);
        kuchen.setFachnummer(fach);
        kuchen.setInspektionsdatum(new Date());
        kuchen.setEinfuegeDatum(new Date());
        kuchenList.set(fach, kuchen);
        setChanged();
        notifyObservers("Kuchen wurde hinzugefuegt: " + kuchen);
        updateKuchenProHersteller();
        return true;

    }*/


    public int nullPosition() {
           int nullStelle = 0;

            for (final Kuchen kuchen : kuchenList) {
                if (kuchen == null) {
                    nullStelle++;
                }
            }

            return nullStelle;
        }


    public  synchronized  Hersteller addHersteller(String name) {
        for (Hersteller hersteller : herstellerList) {
            if (hersteller.getName().equals(name))
                throw new IllegalArgumentException("Name existiert schon");
        }
        Hersteller hersteller = new Hersteller(name);
        herstellerList.add(hersteller);
        updateKuchenProHersteller();
        return hersteller;
    }

    public synchronized boolean deleteKuchen(int fach) {
        for (Kuchen kuchen : kuchenList) {
            if (kuchen != null && kuchen.getFachnummer() == fach) {
                kuchenList.set(kuchenList.indexOf(kuchen), null);
                setChanged();
                notifyObservers("Aenderung auf Allergen");
                updateKuchenProHersteller();
                return true;
            }
        }
        return false;
    }


    public  synchronized static void aendern(Kuchen kuchen) {
           Date inspektDate = new Date();
           kuchen.setInspektionsdatum(inspektDate);

    }
    public synchronized Hersteller findHersteller(String name) {
        for (Hersteller hersteller : herstellerList) {
            if (hersteller.getName().equals(name)) {
                return hersteller;
            }
        }
        return null;
    }

    public synchronized void deleteHersteller(Hersteller hersteller) {
        herstellerList.remove(hersteller);
        for (int fach = 0; fach < kuchenList.size(); fach++) {
            Kuchen kuchen = kuchenList.get(fach);
            if (kuchen != null && kuchen.getHersteller().equals(hersteller)) {
                kuchenList.set(fach, null); // 
            }
        }
        updateKuchenProHersteller();
    }

    public synchronized static List<Integer> findFreePosition(List<Kuchen> kuchenList) {
        final List<Integer> freePositions = new ArrayList<>();

        for (int fach = 0; fach < kuchenList.size(); fach++) {
            if (kuchenList.get(fach) == null) {
                freePositions.add(fach);
            }
        }

        if (freePositions.isEmpty()) {
            throw new IllegalStateException("keine freie position in Automat");
        }

        return freePositions;
    }

    public synchronized Kuchen getKuchenByFach(int fachnummer) {
        for (Kuchen kuchen : kuchenList) {
            if (kuchen.getFachnummer() == fachnummer) {
                return kuchen;
            }
        }
        return null;
    }
    

    public synchronized void addObserverCapacity(Observer observer) {
        addObserver(observer);
    }

    public synchronized void addObserverAllergenes(Observer observer) {
        addObserver(observer);
    }
    
    public synchronized List<Allergen> lagerAllergen() {
        List<Allergen> allergens = new ArrayList<>();
        for(Kuchen kuchen : kuchenList) {
            if (kuchen != null && kuchen.getAllergene() != null) {
                allergens.addAll(kuchen.getAllergene());
            }
        }
        return allergens;
    }
    public synchronized List<Allergen> noLagerAllergen() {
        List<Allergen> allergenI = lagerAllergen();
        List<Allergen> allergenE = new ArrayList<>(Arrays.asList(Allergen.values())); // Alle moegliche Allergen
        allergenE.removeAll(allergenI);

        return allergenE;
    }

    public synchronized void saveJOS(String fileSJOS) {
        try (ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(fileSJOS))) {
            outStream.writeObject(this);
            System.out.println("Automat erfolgreich mit JOS gespeichert.");
        } catch (FileNotFoundException e) {
            System.out.println("File nicht gefunden.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Fehler");
            e.printStackTrace();
        }
    }

    public synchronized Automat  ladenJOS(String fileLJOS) {
        try (ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(fileLJOS))) {
            return (Automat) inStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("file nicht gefunden.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Fehler");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Fehler");
            e.printStackTrace();
        }
        return null;
    }


    public synchronized void saveJBP(String fileSJBP) {
        try (XMLEncoder encoderXml = new XMLEncoder(new FileOutputStream(fileSJBP))) {
            encoderXml.writeObject(this);
            System.out.println("Automat erfolgreich mit JBP espeichert.");
        } catch (FileNotFoundException e) {
            System.out.println("file nicht gefunden.");
            e.printStackTrace();
        }
    }

    public synchronized Automat  ladenJBP(String fileLJBP) {
        try (XMLDecoder decoderXml = new XMLDecoder(new FileInputStream(fileLJBP))) {
            return (Automat) decoderXml.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("file nicht gefunden.");
            e.printStackTrace();
        }
        return null;
    }

   /* public void setHerstellerList(List<Hersteller> herstellerList) {
        this.herstellerList = herstellerList;
    }

    public void setKuchenList(List<Kuchen> kuchenList) {
        this.kuchenList = kuchenList;
    }
    public Map<Hersteller, Integer> getKuchenProHersteller() {
        return kuchenProHersteller;
    }
    public void setKuchenProHersteller(Map<Hersteller, Integer> kuchenProHersteller) {
        this.kuchenProHersteller = kuchenProHersteller;
    }*/


}

