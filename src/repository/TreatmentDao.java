package repository;

import model.Treatment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TreatmentDao {

    // insert one treatment row into the database
    public int insert(Treatment treatment) {
        String sql = "INSERT INTO treatments(name, administration_mode, cost) VALUES (?, ?, ?)";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, treatment.getName());
            ps.setString(2, treatment.getAdministrationMode());
            ps.setDouble(3, treatment.getCost());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare insert treatment", e);
        }
    }

    // read one treatment by id
    // read one treatment by id
    public String findByIdBasic(int treatmentId) {
        String sql = "SELECT treatment_id, name, administration_mode, cost FROM treatments WHERE treatment_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, treatmentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return rs.getInt("treatment_id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getString("administration_mode") + " | " +
                        rs.getDouble("cost");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findById treatment", e);
        }
    }

    // read all treatments in display format
    // read all treatments in display format
    public List<String> findAllBasic() {
        String sql = "SELECT treatment_id, name, administration_mode, cost FROM treatments ORDER BY treatment_id";
        List<String> rows = new ArrayList<>();

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows.add(
                        rs.getInt("treatment_id") + " | " +
                                rs.getString("name") + " | " +
                                rs.getString("administration_mode") + " | " +
                                rs.getDouble("cost")
                );
            }
            return rows;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findAll treatments", e);
        }
    }

    // update one treatment row by id
    // update one treatment row by id
    public boolean update(int treatmentId, Treatment treatment) {
        String sql = "UPDATE treatments SET name = ?, administration_mode= ?, cost = ? WHERE treatment_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, treatment.getName());
            ps.setString(2, treatment.getAdministrationMode());
            ps.setDouble(3, treatment.getCost());
            ps.setInt(4, treatmentId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare update treatment", e);
        }
    }

    // delete one treatment row by id
    // delete one treatment row by id
    public boolean delete(int treatmentId) {
        String sql = "DELETE FROM treatments WHERE treatment_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, treatmentId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare delete treatment", e);
        }
    }
}
