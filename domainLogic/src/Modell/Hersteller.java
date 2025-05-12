package Modell;

import java.io.Serializable;
import java.util.Objects;

public class Hersteller implements verwaltung.Hersteller, Serializable {
    private static final long serialVersionUID = 1L;
    private String name;

    public Hersteller(String name) {
        this.name = name;
    }
    public Hersteller(){
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hersteller)) return false;
        Hersteller that = (Hersteller) o;
        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
