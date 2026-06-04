package repository;

import config.DatabaseConnection;
import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RezervareRepository implements GenericRepository<Rezervare, Integer> {
    private static RezervareRepository instance;

    private RezervareRepository() {}

    public static RezervareRepository getInstance() {
        if (instance == null) {
            instance = new RezervareRepository();
        }
        return instance;
    }

    @Override
    public void create(Rezervare rezervare) {
        String sql = "INSERT INTO rezervari (nume_client, telefon, data_ora, numar_masa) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rezervare.getNumeClient());
            stmt.setString(2, rezervare.getTelefon());
            stmt.setString(3, rezervare.getDataOra());

            if (rezervare.getMasaRezervata() != null) {
                stmt.setInt(4, rezervare.getMasaRezervata().getNumar());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Rezervare read(Integer id) {
        String sql = "SELECT * FROM rezervari WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Rezervare(rs.getString("nume_client"), rs.getString("telefon"), rs.getString("data_ora"), null);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Rezervare> readAll() {
        List<Rezervare> list = new ArrayList<>();
        String sql = "SELECT * FROM rezervari";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Rezervare r = new Rezervare(rs.getString("nume_client"), rs.getString("telefon"), rs.getString("data_ora"), null);
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void update(Rezervare rezervare) {
        String sql = "UPDATE rezervari SET telefon = ?, data_ora = ? WHERE nume_client = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rezervare.getTelefon());
            stmt.setString(2, rezervare.getDataOra());
            stmt.setString(3, rezervare.getNumeClient());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM rezervari WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}