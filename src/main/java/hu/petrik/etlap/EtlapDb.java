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
        String sql = "SELECT * FROM etlap";
        ResultSet result = stmt.executeQuery(sql);
        while (result.next()) {
            int id = result.getInt("id");
            String nev = result.getString("nev");
            String leiras = result.getString("leiras");
            int ar = result.getInt("ar");
            String kategoria = result.getString("kategoria");
            Etlap etlap = new Etlap(id, nev, leiras, ar, kategoria);
            listEtlap.add(etlap);
        }
        return listEtlap;
    }

    public int newEtlap(String nev, String leiras, int ar, String kategoria) throws SQLException {
        String sql = "INSERT INTO etlap(nev, leiras, ar, kategoria) VALUES (?,?,?,?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, nev);
        stmt.setString(2, leiras);
        stmt.setInt(3, ar);
        stmt.setString(4, kategoria);
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
}
