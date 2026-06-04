package repository;

import config.DatabaseConnection;
import model.Masa;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MasaRepository implements GenericRepository<Masa, Integer> {
    private static MasaRepository instance;

    private MasaRepository() {}

    public static MasaRepository getInstance() {
        if (instance == null) {
            instance = new MasaRepository();
        }
        return instance;
    }

    @Override
    public void create(Masa masa) {
        String sql = "INSERT INTO mese (numar, capacitate, status) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, masa.getNumar());
            stmt.setInt(2, masa.getCapacitate());
            stmt.setString(3, masa.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Masa read(Integer numar) {
        String sql = "SELECT * FROM mese WHERE numar = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, numar);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Masa masa = new Masa(rs.getInt("numar"), rs.getInt("capacitate"));
                    masa.setStatus(rs.getString("status"));
                    return masa;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Masa> readAll() {
        List<Masa> list = new ArrayList<>();
        // Am adaugat ORDER BY pentru ca mesele sa ramana mereu sortate crescator (1, 2, 3... 10)
        String sql = "SELECT * FROM mese ORDER BY numar ASC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Masa masa = new Masa(rs.getInt("numar"), rs.getInt("capacitate"));
                masa.setStatus(rs.getString("status"));
                list.add(masa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void update(Masa masa) {
        String sql = "UPDATE mese SET capacitate = ?, status = ? WHERE numar = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, masa.getCapacitate());
            stmt.setString(2, masa.getStatus());
            stmt.setInt(3, masa.getNumar());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer numar) {
        String sql = "DELETE FROM mese WHERE numar = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, numar);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}