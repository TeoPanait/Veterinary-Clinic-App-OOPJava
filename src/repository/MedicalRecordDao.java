package repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordDao {

    // insert one medical record row into the database
    public int insert(int petId, int vetId, String diagnosis, int treatmentId) {
        String sql = "INSERT INTO medical_records(pet_id, vet_id, diagnosis, treatment_id, record_time) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, petId);
            ps.setInt(2, vetId);
            ps.setString(3, diagnosis);
            if (treatmentId > 0) {
                ps.setInt(4, treatmentId);
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare insert medical record", e);
        }
    }

    // read one medical record by id with joined data
    public String findByIdBasic(int recordId) {
        String sql = """
            SELECT mr.record_id, mr.pet_id, mr.vet_id, mr.diagnosis, mr.treatment_id, mr.record_time,
                   p.name_pet, v.name AS vet_name, v.surname AS vet_surname,
                   t.name AS treatment_name, t.cost
            FROM medical_records mr
            JOIN pets p ON p.pet_id = mr.pet_id
            LEFT JOIN veterinarians v ON v.vet_id = mr.vet_id
            LEFT JOIN treatments t ON t.treatment_id = mr.treatment_id
            WHERE mr.record_id = ?
            """;
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, recordId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return rs.getInt("record_id") + " | model.Pet: " + rs.getString("name_pet") +
                        " | Vet: " + rs.getString("vet_name") + " " + rs.getString("vet_surname") +
                        " | Diagnosis: " + rs.getString("diagnosis") +
                        " | model.Treatment: " + rs.getString("treatment_name") +
                        " | Time: " + rs.getTimestamp("record_time");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findById medical record", e);
        }
    }

    // read all medical records with joined data
    public List<String> findAllBasic() {
        String sql = """
            SELECT mr.record_id, mr.pet_id, mr.vet_id, mr.diagnosis, mr.treatment_id, mr.record_time,
                   p.name_pet, v.name AS vet_name, v.surname AS vet_surname,
                   t.name AS treatment_name
            FROM medical_records mr
            JOIN pets p ON p.pet_id = mr.pet_id
            LEFT JOIN veterinarians v ON v.vet_id = mr.vet_id
            LEFT JOIN treatments t ON t.treatment_id = mr.treatment_id
            ORDER BY mr.record_time DESC
            """;
        List<String> rows = new ArrayList<>();

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows.add(
                        rs.getInt("record_id") + " | model.Pet: " + rs.getString("name_pet") +
                                " | Vet: " + rs.getString("vet_name") + " " + rs.getString("vet_surname") +
                                " | Diagnosis: " + rs.getString("diagnosis") +
                                " | model.Treatment: " + rs.getString("treatment_name") +
                                " | Time: " + rs.getTimestamp("record_time")
                );
            }
            return rows;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findAll medical records", e);
        }
    }

    // update diagnosis and treatment for one record
    public boolean update(int recordId, String newDiagnosis, int newTreatmentId) {
        String sql = "UPDATE medical_records SET diagnosis = ?, treatment_id = ? WHERE record_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newDiagnosis);
            if (newTreatmentId > 0) {
                ps.setInt(2, newTreatmentId);
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setInt(3, recordId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare update medical record", e);
        }
    }

    // delete one medical record row by id
    public boolean delete(int recordId) {
        String sql = "DELETE FROM medical_records WHERE record_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, recordId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare delete medical record", e);
        }
    }
}
