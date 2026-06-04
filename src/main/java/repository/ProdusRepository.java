package repository;

import config.DatabaseConnection;
import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdusRepository implements GenericRepository<Produs, Integer> {
    private static ProdusRepository instance;

    private ProdusRepository() {}

    public static ProdusRepository getInstance() {
        if (instance == null) {
            instance = new ProdusRepository();
        }
        return instance;
    }

    @Override
    public void create(Produs produs) {
        String sql = "INSERT INTO produse (nume, pret, tip, este_alcoolica) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produs.getNume());
            stmt.setDouble(2, produs.getPret());
            if (produs instanceof Bautura) {
                stmt.setString(3, "BAUTURA");
                stmt.setBoolean(4, ((Bautura) produs).isEsteAlcoolica());
            } else {
                stmt.setString(3, "PREPARAT");
                stmt.setBoolean(4, false);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Produs read(Integer id) {
        String sql = "SELECT * FROM produse WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tip = rs.getString("tip");
                    if ("BAUTURA".equals(tip)) {
                        return new Bautura(rs.getString("nume"), rs.getDouble("pret"), rs.getBoolean("este_alcoolica"));
                    } else {
                        return new PreparatCulinar(rs.getString("nume"), rs.getDouble("pret"), null, new ArrayList<>());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Produs> readAll() {
        List<Produs> list = new ArrayList<>();
        String sql = "SELECT * FROM produse";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String tip = rs.getString("tip");
                if ("BAUTURA".equals(tip)) {
                    list.add(new Bautura(rs.getString("nume"), rs.getDouble("pret"), rs.getBoolean("este_alcoolica")));
                } else {
                    list.add(new PreparatCulinar(rs.getString("nume"), rs.getDouble("pret"), null, new ArrayList<>()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void update(Produs produs) {
        String sql = "UPDATE produse SET pret = ? WHERE nume = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, produs.getPret());
            stmt.setString(2, produs.getNume());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM produse WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}