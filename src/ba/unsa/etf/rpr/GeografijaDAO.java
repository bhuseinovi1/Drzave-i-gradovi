package ba.unsa.etf.rpr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GeografijaDAO {
    private static GeografijaDAO instance;
    private Connection conn;

    public Connection getConn() {
        return conn;
    }

    private PreparedStatement glavniGradUpit, dajDrzavuUpit, obrisiDrzavuUpit, obrisiGradoveZaDrzavuUpit, nadjiDrzavuUpit,
            dajGradoveUpit, dodajGradUpit, odrediIdGradaUpit, dodajDrzavuUpit, odrediIdDrzaveUpit, promijeniGradUpit, dajGradUpit,
            nadjiGradUpit, obrisiGradUpit, dajDrzaveUpit;

    private PreparedStatement dodajPobratimaUpit, obrisiPobratimaUpit, dajPobratime;

    public static GeografijaDAO getInstance() {
        if (instance == null) instance = new GeografijaDAO();
        return instance;
    }
    private GeografijaDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            glavniGradUpit = conn.prepareStatement("SELECT grad.id, grad.naziv, grad.broj_stanovnika, grad.drzava , grad.nadmorska_visina , grad.zagadjenost, grad.slika FROM grad, drzava WHERE grad.drzava=drzava.id AND drzava.naziv=?");
        } catch (SQLException e) {
            regenerisiBazu();
            try {
                glavniGradUpit = conn.prepareStatement("SELECT grad.id, grad.naziv, grad.broj_stanovnika, grad.drzava , grad.nadmorska_visina, grad.zagadjenost, grad.slika FROM grad, drzava WHERE grad.drzava=drzava.id AND drzava.naziv=?");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        try {
            dajDrzavuUpit = conn.prepareStatement("SELECT * FROM drzava WHERE id=?");
            dajGradUpit = conn.prepareStatement("SELECT * FROM grad WHERE id=?");
            obrisiGradoveZaDrzavuUpit = conn.prepareStatement("DELETE FROM grad WHERE drzava=?");
            obrisiDrzavuUpit = conn.prepareStatement("DELETE FROM drzava WHERE id=?");
            obrisiGradUpit = conn.prepareStatement("DELETE FROM grad WHERE id=?");
            nadjiDrzavuUpit = conn.prepareStatement("SELECT * FROM drzava WHERE naziv=?");
            nadjiGradUpit = conn.prepareStatement("SELECT * FROM grad WHERE naziv=?");
            dajGradoveUpit = conn.prepareStatement("SELECT * FROM grad ORDER BY broj_stanovnika DESC");
            dajDrzaveUpit = conn.prepareStatement("SELECT * FROM drzava ORDER BY naziv");

            dodajGradUpit = conn.prepareStatement("INSERT INTO grad VALUES(?,?,?,?,?,?,?,?)");
            odrediIdGradaUpit = conn.prepareStatement("SELECT MAX(id)+1 FROM grad");
            dodajDrzavuUpit = conn.prepareStatement("INSERT INTO drzava VALUES(?,?,?,?)");
            odrediIdDrzaveUpit = conn.prepareStatement("SELECT MAX(id)+1 FROM drzava");

            promijeniGradUpit = conn.prepareStatement("UPDATE grad SET naziv=?, broj_stanovnika=?, drzava=? , nadmorska_visina=?, zagadjenost=?, tip=?, slika=? WHERE id=?");

            dodajPobratimaUpit = conn.prepareStatement("INSERT INTO pobratimi VALUES(?,?)");
            obrisiPobratimaUpit = conn.prepareStatement("DELETE FROM pobratimi WHERE g1=? OR g2=?");
            dajPobratime = conn.prepareStatement("SELECT * FROM pobratimi");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void removeInstance() {
        if (instance == null) return;
        instance.close();
        instance = null;
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void regenerisiBazu() {
        Scanner ulaz = null;
        try {
            ulaz = new Scanner(new FileInputStream("baza.db.sql"));
            String sqlUpit = "";
            while (ulaz.hasNext()) {
                sqlUpit += ulaz.nextLine();
                if ( sqlUpit.charAt( sqlUpit.length()-1 ) == ';') {
                    try {
                        Statement stmt = conn.createStatement();
                        stmt.execute(sqlUpit);
                        sqlUpit = "";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            ulaz.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Grad glavniGrad(String drzava) {
        try {
            Drzava d = nadjiDrzavu(drzava);
            glavniGradUpit.setString(1, drzava);
            ResultSet rs = glavniGradUpit.executeQuery();
            if (!rs.next()) return null;
            return dajGradIzResultSeta(rs, d, true);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Grad dajGradIzResultSeta(ResultSet rs, Drzava d, boolean pobratim) throws SQLException {
        // potrebno pretrazivanje pobratimi tabele
        if(!pobratim) {
            if (rs.getInt(7) == 1)
                return new RazvijeniGrad(rs.getInt(1), rs.getString(2), rs.getInt(3), d, rs.getInt(5), rs.getInt(6),new ArrayList<Grad>(),rs.getString(8));
            else if (rs.getInt(7) == 2)
                return new SrednjeRazvijeniGrad(rs.getInt(1), rs.getString(2), rs.getInt(3), d, rs.getInt(5), rs.getInt(6),new ArrayList<Grad>(),rs.getString(8));
            else return new NerazvijeniGrad(rs.getInt(1), rs.getString(2), rs.getInt(3), d, rs.getInt(5), rs.getInt(6),new ArrayList<Grad>(),rs.getString(8));
        }
        else {
            ArrayList<Grad> pobratimi = dajPobratimeGradaSaId(rs.getInt(1));
            if (rs.getInt(7) == 1)
                return new RazvijeniGrad(rs.getInt(1), rs.getString(2), rs.getInt(3), d, rs.getInt(5), rs.getInt(6),pobratimi,rs.getString(8));
            else if (rs.getInt(7) == 2)
                return new SrednjeRazvijeniGrad(rs.getInt(1), rs.getString(2), rs.getInt(3), d, rs.getInt(5), rs.getInt(6),pobratimi,rs.getString(8));
            else return new NerazvijeniGrad(rs.getInt(1), rs.getString(2), rs.getInt(3), d, rs.getInt(5), rs.getInt(6),pobratimi,rs.getString(8));
        }
        //return new Grad(rs.getInt(1), rs.getString(2), rs.getInt(3), d, rs.getInt(5), rs.getInt(6));
    }

    private ArrayList<Grad> dajPobratimeGradaSaId(int id) {
        // losa ideja, kreirati sve pobratime, ali bez posljednjeg konstruktora
        ArrayList<Grad> pobratimi = new ArrayList<>();
        try {
            ResultSet rs = dajPobratime.executeQuery();
            while(rs.next()) {
                if(rs.getInt(1)==id) dajGradUpit.setInt(1,rs.getInt(2));
                else if(rs.getInt(2)==id) dajGradUpit.setInt(1,rs.getInt(1));
                ResultSet resultSet = dajGradUpit.executeQuery();
                while (resultSet.next()) {
                    //Drzava d = dajDrzavu(resultSet.getInt(4)); ovo pravi problem
                    Grad grad = dajGradIzResultSeta(resultSet, null, false); // ovdje nek vraca grad sa null kao pobratimima
                    if(!pobratimi.contains(grad) && grad.getId()!=id) pobratimi.add(grad);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return pobratimi;
    }

    private Drzava dajDrzavu(int id) {
        try {
            dajDrzavuUpit.setInt(1, id);
            ResultSet rs = dajDrzavuUpit.executeQuery();
            if (!rs.next()) return null;
            return dajDrzavuIzResultSeta(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Grad dajGrad(int id, Drzava d) {
        try {
            dajGradUpit.setInt(1, id);
            ResultSet rs = dajGradUpit.executeQuery();
            if (!rs.next()) return null;
            return dajGradIzResultSeta(rs, d, true);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    private Drzava dajDrzavuIzResultSeta(ResultSet rs) throws SQLException {
        Drzava d = new Drzava(rs.getInt(1), rs.getString(2), null);
        d.setGlavniGrad( dajGrad(rs.getInt(3), d ));
        d.setNajveciGrad( dajGrad(rs.getInt(4),d ));
        return d;
    }

    public void obrisiDrzavu(String nazivDrzave) {
        try {
            nadjiDrzavuUpit.setString(1, nazivDrzave);
            ResultSet rs = nadjiDrzavuUpit.executeQuery();
            if (!rs.next()) return;
            Drzava drzava = dajDrzavuIzResultSeta(rs);

            obrisiGradoveZaDrzavuUpit.setInt(1, drzava.getId());
            obrisiGradoveZaDrzavuUpit.executeUpdate();

            obrisiDrzavuUpit.setInt(1, drzava.getId());
            obrisiDrzavuUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Grad> gradovi() {
        ArrayList<Grad> rezultat = new ArrayList();
        try {
            ResultSet rs = dajGradoveUpit.executeQuery();
            while (rs.next()) {
                Drzava d = dajDrzavu(rs.getInt(4));
                Grad grad = dajGradIzResultSeta(rs, d, true);
                rezultat.add(grad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rezultat;
    }

    public ArrayList<Drzava> drzave() {
        ArrayList<Drzava> rezultat = new ArrayList();
        try {
            ResultSet rs = dajDrzaveUpit.executeQuery();
            while (rs.next()) {
                Drzava drzava = dajDrzavuIzResultSeta(rs);
                rezultat.add(drzava);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rezultat;
    }

    public void dodajGrad(Grad grad) {
        try {
            ResultSet rs = odrediIdGradaUpit.executeQuery();
            int id = 1;
            if (rs.next()) {
                id = rs.getInt(1);
            }

            dodajGradUpit.setInt(1, id);
            dodajGradUpit.setString(2, grad.getNaziv());
            dodajGradUpit.setInt(3, grad.getBrojStanovnika());
            dodajGradUpit.setInt(4, grad.getDrzava().getId());
            dodajGradUpit.setInt(5, grad.getNadmorskaVisina());
            dodajGradUpit.setInt(6, grad.getZagadjenost());

            //dodano
            if(grad instanceof RazvijeniGrad)
                dodajGradUpit.setInt(7, 1);
            else if(grad instanceof SrednjeRazvijeniGrad)
                dodajGradUpit.setInt(7,2);
            else dodajGradUpit.setInt(7,3);

            dodajGradUpit.setString(8,grad.getSlika());

            dodajGradUpit.executeUpdate();

            for(Grad pobratim : grad.getPobratimi()) {
                dodajPobratimaUpit.setInt(1, grad.getId());
                dodajPobratimaUpit.setInt(2, pobratim.getId());
                dodajPobratimaUpit.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dodajDrzavu(Drzava drzava) {
        try {
            ResultSet rs = odrediIdDrzaveUpit.executeQuery();
            int id = 1;
            if (rs.next()) {
                id = rs.getInt(1);
            }

            dodajDrzavuUpit.setInt(1, id);
            dodajDrzavuUpit.setString(2, drzava.getNaziv());
            dodajDrzavuUpit.setInt(3, drzava.getGlavniGrad().getId());
            dodajDrzavuUpit.setInt(4, drzava.getNajveciGrad().getId());
            dodajDrzavuUpit.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void izmijeniGrad(Grad grad) {
        try {
            promijeniGradUpit.setString(1, grad.getNaziv());
            promijeniGradUpit.setInt(2, grad.getBrojStanovnika());
            promijeniGradUpit.setInt(3, grad.getDrzava().getId());
            promijeniGradUpit.setInt(4, grad.getNadmorskaVisina());
            promijeniGradUpit.setInt(5, grad.getZagadjenost());

            //dodano
            if(grad instanceof RazvijeniGrad)
                promijeniGradUpit.setInt(6, 1);
            else if(grad instanceof SrednjeRazvijeniGrad)
                promijeniGradUpit.setInt(6,2);
            else promijeniGradUpit.setInt(6,3);

            promijeniGradUpit.setString(7, grad.getSlika());
            promijeniGradUpit.setInt(8, grad.getId());
            promijeniGradUpit.executeUpdate();


            for(Grad pobratim : grad.getPobratimi()) {
                dodajPobratimaUpit.setInt(1, grad.getId());
                dodajPobratimaUpit.setInt(2, pobratim.getId());
                dodajPobratimaUpit.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Drzava nadjiDrzavu(String nazivDrzave) {
        try {
            nadjiDrzavuUpit.setString(1, nazivDrzave);
            ResultSet rs = nadjiDrzavuUpit.executeQuery();
            if (!rs.next()) return null;
            return dajDrzavuIzResultSeta(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Grad nadjiGrad(String nazivGrada) {
        try {
            nadjiGradUpit.setString(1, nazivGrada);
            ResultSet rs = nadjiGradUpit.executeQuery();
            if (!rs.next()) return null;
            Drzava d = dajDrzavu(rs.getInt(4));
            return dajGradIzResultSeta(rs, d, true);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void obrisiGrad(Grad grad) {
        try {
            obrisiGradUpit.setInt(1, grad.getId());
            obrisiGradUpit.executeUpdate();

            // brisanje pobratima
            obrisiPobratimaUpit.setInt(1,grad.getId());
            obrisiPobratimaUpit.setInt(2,grad.getId());
            obrisiPobratimaUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
