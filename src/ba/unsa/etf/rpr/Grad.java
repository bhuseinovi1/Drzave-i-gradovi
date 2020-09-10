package ba.unsa.etf.rpr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Grad implements Serializable {
    private int id;
    private String naziv;
    private int brojStanovnika;
    private Drzava drzava;
    private int nadmorskaVisina;
    private int zagadjenost;
    private ArrayList<Grad> pobratimi = new ArrayList<>();
    private String slika;

    public Grad(int id, String naziv, int brojStanovnika, Drzava drzava) {
        this.id = id;
        this.naziv = naziv;
        this.brojStanovnika = brojStanovnika;
        this.drzava = drzava;
    }

    public Grad(int id, String naziv, int brojStanovnika, Drzava drzava, int nadmorskaVisina) {
        this.id = id;
        this.naziv = naziv;
        this.brojStanovnika = brojStanovnika;
        this.drzava = drzava;
        this.nadmorskaVisina = nadmorskaVisina;
    }

    public Grad(int id, String naziv, int brojStanovnika, Drzava drzava, int nadmorskaVisina, int zagadjenost) {
        this.id = id;
        this.naziv = naziv;
        this.brojStanovnika = brojStanovnika;
        this.drzava = drzava;
        this.nadmorskaVisina = nadmorskaVisina;
        this.zagadjenost = zagadjenost;
    }

    public Grad(int id, String naziv, int brojStanovnika, Drzava drzava, int nadmorskaVisina, int zagadjenost, ArrayList<Grad> pobratimi) {
        this.id = id;
        this.naziv = naziv;
        this.brojStanovnika = brojStanovnika;
        this.drzava = drzava;
        this.nadmorskaVisina = nadmorskaVisina;
        this.zagadjenost = zagadjenost;
        this.pobratimi = pobratimi;
    }

    public Grad(int id, String naziv, int brojStanovnika, Drzava drzava, int nadmorskaVisina, int zagadjenost, ArrayList<Grad> pobratimi, String slika) {
        this.id = id;
        this.naziv = naziv;
        this.brojStanovnika = brojStanovnika;
        this.drzava = drzava;
        this.nadmorskaVisina = nadmorskaVisina;
        this.zagadjenost = zagadjenost;
        this.pobratimi = pobratimi;
        this.slika = slika;
    }

    public Grad() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(int brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
    }

    public Drzava getDrzava() {
        return drzava;
    }

    public void setDrzava(Drzava drzava) {
        this.drzava = drzava;
    }

    public int getNadmorskaVisina() {
        return nadmorskaVisina;
    }

    public void setNadmorskaVisina(int nadmorskaVisina) {
        this.nadmorskaVisina = nadmorskaVisina;
    }

    public int getZagadjenost() {
        return zagadjenost;
    }

    public void setZagadjenost(int zagadjenost) {
        if(zagadjenost<1 || zagadjenost>10) throw new IllegalArgumentException("Zagadjenost mora biti u intervalu od 1 do 10");
        this.zagadjenost = zagadjenost;
    }

    public ArrayList<Grad> getPobratimi() {
        return pobratimi;
    }

    public void setPobratimi(ArrayList<Grad> pobratimi) {
        this.pobratimi = pobratimi;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    @Override
    public String toString() { return naziv; }

    public abstract int brojBolnica();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Grad grad = (Grad) o;
        return id == grad.id &&
                Objects.equals(naziv, grad.naziv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, naziv);
    }
}
