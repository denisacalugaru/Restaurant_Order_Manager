package repository;

import config.DatabaseConnection;
import model.Angajat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AngajatRepository implements GenericRepository<Angajat, Integer> {

    private static AngajatRepository instance;

    private AngajatRepository() {}

    public static AngajatRepository getInstance() {
        if (instance == null) {
            instance = new AngajatRepository();
        }
        return instance;
    }

    @Override
    public void create(Angajat angajat) {
        String sql = "INSERT INTO angajati (nume, rol, salariu) VALUES (?, ?, ?) ON CONFLICT (nume) DO NOTHING";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) { //transmite structura textului SQL la server pentru a fi pre-compilata și securizata impotriva atacurilor de tip sql injection
            stmt.setString(1, angajat.getNume());
            stmt.setString(2, angajat.getRol());
            stmt.setDouble(3, angajat.getSalariu());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Angajat read(Integer id) {
        String sql = "SELECT * FROM angajati WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Angajat(
                            rs.getString("nume"),
                            rs.getString("rol"),
                            rs.getDouble("salariu")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Angajat> readAll() {
        List<Angajat> list = new ArrayList<>();
        String sql = "SELECT * FROM angajati ORDER BY id ASC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Angajat(
                        rs.getString("nume"),
                        rs.getString("rol"),
                        rs.getDouble("salariu")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void update(Angajat angajat) {
        String sql = "UPDATE angajati SET rol = ?, salariu = ? WHERE nume = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, angajat.getRol());
            stmt.setDouble(2, angajat.getSalariu());
            stmt.setString(3, angajat.getNume());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM angajati WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}