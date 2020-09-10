package ba.unsa.etf.rpr;

import java.util.ArrayList;

public class SrednjeRazvijeniGrad extends Grad{
    public SrednjeRazvijeniGrad(int id, String naziv, int brojStanovnika, Drzava drzava) {
        super(id, naziv, brojStanovnika, drzava);
    }

    public SrednjeRazvijeniGrad(int id, String naziv, int brojStanovnika, Drzava drzava, int nadmorskaVisina) {
        super(id, naziv, brojStanovnika, drzava, nadmorskaVisina);
    }

    public SrednjeRazvijeniGrad(int id, String naziv, int brojStanovnika, Drzava drzava, int nadmorskaVisina, int zagadjenost) {
        super(id, naziv, brojStanovnika, drzava, nadmorskaVisina, zagadjenost);
    }

    public SrednjeRazvijeniGrad() {
    }

    public SrednjeRazvijeniGrad(int id, String naziv, int brojStanovnika, Drzava drzava, int nadmorskaVisina, int zagadjenost, ArrayList<Grad> pobratimi) {
        super(id, naziv, brojStanovnika, drzava, nadmorskaVisina, zagadjenost, pobratimi);
    }

    public SrednjeRazvijeniGrad(int id, String naziv, int brojStanovnika, Drzava drzava, int nadmorskaVisina, int zagadjenost, ArrayList<Grad> pobratimi, String slika) {
        super(id, naziv, brojStanovnika, drzava, nadmorskaVisina, zagadjenost, pobratimi, slika);
    }

    public int brojBolnica() {
        return (int)Math.ceil(super.getBrojStanovnika()/25000.0);
    }
}
