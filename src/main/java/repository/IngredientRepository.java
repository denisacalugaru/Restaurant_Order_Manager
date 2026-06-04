package repository;

import config.DatabaseConnection;
import model.Ingredient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientRepository implements GenericRepository<Ingredient, Integer> {

    private static IngredientRepository instance;

    private IngredientRepository() {}

    public static IngredientRepository getInstance() {
        if (instance == null) {
            instance = new IngredientRepository();
        }
        return instance;
    }

    @Override
    public void create(Ingredient ingredient) {
        String sql = "INSERT INTO ingrediente (nume, stoc, este_vegan) VALUES (?, ?, ?) ON CONFLICT (nume) DO NOTHING";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ingredient.getNume());
            stmt.setDouble(2, ingredient.getStoc());
            stmt.setBoolean(3, ingredient.isEsteVegan());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Ingredient read(Integer id) {
        String sql = "SELECT * FROM ingrediente WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Ingredient(
                            rs.getString("nume"),
                            rs.getDouble("stoc"),
                            rs.getBoolean("este_vegan")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Ingredient> readAll() {
        List<Ingredient> list = new ArrayList<>();
        String sql = "SELECT * FROM ingrediente ORDER BY id ASC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Ingredient(
                        rs.getString("nume"),
                        rs.getDouble("stoc"),
                        rs.getBoolean("este_vegan")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void update(Ingredient ingredient) {
        String sql = "UPDATE ingrediente SET stoc = ?, este_vegan = ? WHERE nume = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, ingredient.getStoc());
            stmt.setBoolean(2, ingredient.isEsteVegan());
            stmt.setString(3, ingredient.getNume());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM ingrediente WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}