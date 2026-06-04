package repository;

import config.DatabaseConnection;
import model.Categorie;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieRepository implements GenericRepository<Categorie, Integer> {

    private static CategorieRepository instance;

    private CategorieRepository() {}

    public static CategorieRepository getInstance() {
        if (instance == null) {
            instance = new CategorieRepository();
        }
        return instance;
    }

    @Override
    public void create(Categorie categorie) {
        String sql = "INSERT INTO categorii (denumire) VALUES (?) ON CONFLICT (denumire) DO NOTHING";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categorie.getDenumire());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Categorie read(Integer id) {
        String sql = "SELECT * FROM categorii WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Categorie(rs.getInt("id"), rs.getString("denumire"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Categorie> readAll() {
        List<Categorie> list = new ArrayList<>();
        String sql = "SELECT * FROM categorii ORDER BY id ASC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Categorie(rs.getInt("id"), rs.getString("denumire")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void update(Categorie categorie) {
        String sql = "UPDATE categorii SET denumire = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categorie.getDenumire());
            stmt.setInt(2, categorie.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM categorii WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}