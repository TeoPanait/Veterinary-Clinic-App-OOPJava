package repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDao {

    // insert one appointment row into the database
    public int insert(int petId, int vetId, LocalDateTime appointmentTime, String reason, String status) {
        String sql = "INSERT INTO appointments(pet_id, vet_id, appointment_time, reason, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, petId);
            ps.setInt(2, vetId);
            ps.setTimestamp(3, Timestamp.valueOf(appointmentTime));
            ps.setString(4, reason);
            ps.setString(5, status);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare insert appointment", e);
        }
    }

    // read one appointment by id with joined pet and vet data
    public String findByIdBasic(int appointmentId) {
        String sql = """
            SELECT a.appointment_id, a.pet_id, a.vet_id, a.appointment_time, a.reason, a.status,
                   p.name_pet, v.name AS vet_name, v.surname AS vet_surname
            FROM appointments a
            JOIN pets p ON p.pet_id = a.pet_id
            JOIN veterinarians v ON v.vet_id = a.vet_id
            WHERE a.appointment_id = ?
            """;
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return rs.getInt("appointment_id") + " | model.Pet: " + rs.getString("name_pet") +
                        " | Vet: " + rs.getString("vet_name") + " " + rs.getString("vet_surname") +
                        " | Time: " + rs.getTimestamp("appointment_time") +
                        " | Reason: " + rs.getString("reason") +
                        " | Status: " + rs.getString("status");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findById appointment", e);
        }
    }

    // read all appointments with joined details
    public List<String> findAllBasic() {
        String sql = """
            SELECT a.appointment_id, a.pet_id, a.vet_id, a.appointment_time, a.reason, a.status,
                   p.name_pet, v.name AS vet_name, v.surname AS vet_surname
            FROM appointments a
            JOIN pets p ON p.pet_id = a.pet_id
            JOIN veterinarians v ON v.vet_id = a.vet_id
            ORDER BY a.appointment_time
            """;
        List<String> rows = new ArrayList<>();

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows.add(
                        rs.getInt("appointment_id") + " | model.Pet: " + rs.getString("name_pet") +
                                " | Vet: " + rs.getString("vet_name") + " " + rs.getString("vet_surname") +
                                " | Time: " + rs.getTimestamp("appointment_time") +
                                " | Reason: " + rs.getString("reason") +
                                " | Status: " + rs.getString("status")
                );
            }
            return rows;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findAll appointments", e);
        }
    }

    // update one appointment row by id
    public boolean update(int appointmentId, LocalDateTime newTime, String newReason, String newStatus) {
        String sql = "UPDATE appointments SET appointment_time = ?, reason = ?, status = ? WHERE appointment_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(newTime));
            ps.setString(2, newReason);
            ps.setString(3, newStatus);
            ps.setInt(4, appointmentId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare update appointment", e);
        }
    }

    // delete one appointment row by id
    public boolean delete(int appointmentId) {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare delete appointment", e);
        }
    }
}
