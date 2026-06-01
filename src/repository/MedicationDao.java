package repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicationDao {

    // insert one medication row into the database
    public int insert(String medName, String dosage, double price, int stock, boolean reqPrescription) {
        String sql = "INSERT INTO medications(med_name, dosage, price, stock, req_prescription) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, medName);
            ps.setString(2, dosage);
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.setBoolean(5, reqPrescription);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare insert medication", e);
        }
    }

    // read one medication by id
    public String findByIdBasic(int medicationId) {
        String sql = "SELECT medication_id, med_name, dosage, price, stock, req_prescription FROM medications WHERE medication_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, medicationId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return rs.getInt("medication_id") + " | " + rs.getString("med_name") +
                        " | Dosage: " + rs.getString("dosage") +
                        " | Price: " + rs.getDouble("price") +
                        " | Stock: " + rs.getInt("stock") +
                        " | Req Prescription: " + rs.getBoolean("req_prescription");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findById medication", e);
        }
    }

    // read all medications in display format
    public List<String> findAllBasic() {
        String sql = "SELECT medication_id, med_name, dosage, price, stock, req_prescription FROM medications ORDER BY medication_id";
        List<String> rows = new ArrayList<>();

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows.add(
                        rs.getInt("medication_id") + " | " + rs.getString("med_name") +
                                " | Dosage: " + rs.getString("dosage") +
                                " | Price: " + rs.getDouble("price") +
                                " | Stock: " + rs.getInt("stock") +
                                " | Req Prescription: " + rs.getBoolean("req_prescription")
                );
            }
            return rows;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findAll medications", e);
        }
    }

    // update medication price and stock by id
    public boolean update(int medicationId, double newPrice, int newStock) {
        String sql = "UPDATE medications SET price = ?, stock = ? WHERE medication_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, newPrice);
            ps.setInt(2, newStock);
            ps.setInt(3, medicationId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare update medication", e);
        }
    }

    // delete one medication row by id
    public boolean delete(int medicationId) {
        String sql = "DELETE FROM medications WHERE medication_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, medicationId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare delete medication", e);
        }
    }

    // return current stock for medication id
    public int getStock(int medicationId) {
        String sql = "SELECT stock FROM medications WHERE medication_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, medicationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("stock");
                else return -1;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare getStock medication", e);
        }
    }

    // atomic operation: decrease stock by amount only if sufficient stock exists
    // uses WHERE clause to check stock >= amount, preventing over-prescription
    // returns true if update succeeded (stock was sufficient), false otherwise
    // the condition (stock >= ?) ensures atomicity: UPDATE and CHECK happen together
    public boolean decreaseStock(int medicationId, int amount) {
        String sql = "UPDATE medications SET stock = stock - ? WHERE medication_id = ? AND stock >= ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, amount);
            ps.setInt(2, medicationId);
            ps.setInt(3, amount);
            int rows = ps.executeUpdate();
            return rows > 0; // if 0 rows updated: either not enough stock or medication not found
        } catch (SQLException e) {
            throw new RuntimeException("Eroare decreaseStock medication", e);
        }
    }
}
