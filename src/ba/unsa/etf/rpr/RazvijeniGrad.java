package ba.unsa.etf.rpr;

import java.util.ArrayList;

public class RazvijeniGrad extends Grad{
    public RazvijeniGrad(int id, String naziv, int brojStanovnika, Drzava drzava) {
        super(id, naziv, brojStanovnika, drzava);
    }

    public RazvijeniGrad(int id, String naziv, int brojStanovnika, Drzava drzava, int nadmorskaVisina) {
        super(id, naziv, brojStanovnika, drzava, nadmorskaVisina);
    }

    public RazvijeniGrad(int id, String naziv, int brojStanovnika, Drzava drzava, int nadmorskaVisina, int zagadjenost) {
        super(id, naziv, brojStanovnika, drzava, nadmorskaVisina, zagadjenost);
    }

    public RazvijeniGrad() {
    }

    public RazvijeniGrad(int id, String naziv, int brojStanovnika, Drzava drzava, int nadmorskaVisina, int zagadjenost, ArrayList<Grad> pobratimi) {
        super(id, naziv, brojStanovnika, drzava, nadmorskaVisina, zagadjenost, pobratimi);
    }

    public RazvijeniGrad(int id, String naziv, int brojStanovnika, Drzava drzava, int nadmorskaVisina, int zagadjenost, ArrayList<Grad> pobratimi, String slika) {
        super(id, naziv, brojStanovnika, drzava, nadmorskaVisina, zagadjenost, pobratimi, slika);
    }

    public int brojBolnica() {
        return (int)Math.ceil(super.getBrojStanovnika()/10000.0);
    }
}
