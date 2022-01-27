package hu.petrik.etlap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtlapDb {
    Connection conn;

    public EtlapDb() throws SQLException {
        this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/etlapdb", "root", "");
    }

    public List<Etlap> getEtlap() throws SQLException {
        List<Etlap> etlapok = new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql = "SELECT * FROM etlap";
        ResultSet result = stmt.executeQuery(sql);
        while (result.next()) {
            int id = result.getInt("id");
            String nev = result.getString("nev");
            String leiras = result.getString("leiras");
            int ar = result.getInt("ar");
            String kategoria = result.getString("kategoria");
            Etlap etlap = new Etlap(id, nev, leiras, ar, kategoria);
            etlapok.add(etlap);
        }
        return etlapok;
    }

    public int etlapFelvetele(String nev, String leiras, int ar, String kategoria) throws SQLException {
        String sql = "INSERT INTO etlap(nev, leiras, ar, kategoria) VALUES (?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nev);
        stmt.setString(2, leiras);
        stmt.setInt(3, ar);
        stmt.setString(4, kategoria);
        return stmt.executeUpdate();
    }

    public boolean etlapTorlese(int id) throws SQLException {
        String sql = "DELETE FROM etlap WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        int torlensoSor = stmt.executeUpdate();
        return torlensoSor == 1;
    }
}
