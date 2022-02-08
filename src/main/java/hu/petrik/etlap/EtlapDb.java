package hu.petrik.etlap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtlapDb {
    Connection connection;

    public EtlapDb() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/etlapdb", "root", "");
    }

    public List<Etlap> getEtlap() throws SQLException {
        List<Etlap> listEtlap = new ArrayList<>();
        Statement stmt = connection.createStatement();
        String sql = "SELECT * FROM etlap INNER JOIN kategoria ON (etlap.kategoria_id = kategoria.id)";
        ResultSet result = stmt.executeQuery(sql);
        while (result.next()) {
            int id = result.getInt("id");
            String nev = result.getString("nev");
            String leiras = result.getString("leiras");
            int ar = result.getInt("ar");
            String kategoria = result.getString("kategoria.nev");
            Etlap etlap = new Etlap(id, nev, leiras, ar, kategoria);
            listEtlap.add(etlap);
        }
        return listEtlap;
    }

    public int newEtlap(String nev, String leiras, int ar, int kategoria_id) throws SQLException {
        String sql = "INSERT INTO etlap(nev, leiras, ar, kategoria_id) VALUES (?,?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, nev);
        stmt.setString(2, leiras);
        stmt.setInt(3, ar);
        stmt.setInt(4, kategoria_id);
        return stmt.executeUpdate();
    }

    public boolean deleteEtlap(int id) throws SQLException {
        String sql = "DELETE FROM etlap WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        int torlensoSor = stmt.executeUpdate();
        return torlensoSor == 1;
    }

    public boolean szazalekEmelesEtlapOsszes(int emeles) throws SQLException {
        String sql = "UPDATE etlap SET ar = ar * (1 + ? / 100)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, emeles);
        int valtoztatottSorok = stmt.executeUpdate();
        return valtoztatottSorok == 1;
    }

    public boolean szazalekEmelesEtlapKivalasztott(int id, int emeles) throws SQLException {
        String sql = "UPDATE etlap SET ar = ar * (1 + ? / 100) WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, emeles);
        stmt.setInt(2, id);
        int valtoztatottSorok = stmt.executeUpdate();
        return valtoztatottSorok == 1;
    }

    public boolean ftEmelesEtlapOsszes(int emeles) throws SQLException {
        String sql = "UPDATE etlap SET ar = ar + ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, emeles);
        int valtoztatottSorok = stmt.executeUpdate();
        return valtoztatottSorok == 1;
    }

    public boolean ftEmelesEtlapKivalasztott(int id, int emeles) throws SQLException {
        String sql = "UPDATE etlap SET ar = ar + ? WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, emeles);
        stmt.setInt(2, id);
        int valtoztatottSorok = stmt.executeUpdate();
        return valtoztatottSorok == 1;
    }

    public List<Kategoria> getKategoria() throws SQLException {
        List<Kategoria> kategoriaList = new ArrayList<>();
        Statement stmt = connection.createStatement();
        String sql = "SELECT * FROM kategoria ORDER BY id";
        ResultSet resultSet = stmt.executeQuery(sql);
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String nev = resultSet.getString("nev");
            Kategoria kategoria = new Kategoria(id, nev);
            kategoriaList.add(kategoria);
        }
        return kategoriaList;
    }

    public boolean deleteKategoria(int id) throws SQLException {
        String sql = "DELETE FROM kategoria WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, id);
        int valtoztatott = stmt.executeUpdate();
        return valtoztatott == 1;
    }

    public int newKategoria(String nev) throws SQLException {
        String sql="INSERT INTO kategoria (nev) VALUES (?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, nev);
        return stmt.executeUpdate();
    }
}
