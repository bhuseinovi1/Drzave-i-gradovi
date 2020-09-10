package ba.unsa.etf.rpr;

import java.io.Serializable;
import java.util.ArrayList;

public class Geografija implements Serializable {
    private ArrayList<Drzava> drzave = new ArrayList<>();
    private ArrayList<Grad> gradovi= new ArrayList<>();

    public Geografija() {
    }

    public Geografija(ArrayList<Drzava> drzave, ArrayList<Grad> gradovi) {
        this.drzave = drzave;
        this.gradovi = gradovi;
    }

    public ArrayList<Drzava> getDrzave() {
        return drzave;
    }

    public void setDrzave(ArrayList<Drzava> drzave) {
        this.drzave = drzave;
    }

    public ArrayList<Grad> getGradovi() {
        return gradovi;
    }

    public void setGradovi(ArrayList<Grad> gradovi) {
        this.gradovi = gradovi;
    }
}
