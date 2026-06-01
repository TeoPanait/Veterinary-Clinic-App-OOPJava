package repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// dao for prescription table: join entity between medical_records and medications
// prescriptions (1) --- medications (N), (1) --- medical_records (N)
// allows independent medication inventory while tracking medication use per record
public class PrescriptionDao {

    // insert prescription: link medical record to medication with quantity
    // returns auto-generated prescription_id
    public int insert(int recordId, int medicationId, int quantity) {
        String sql = "INSERT INTO prescriptions(record_id, medication_id, quantity) VALUES (?, ?, ?)";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, recordId);
            ps.setInt(2, medicationId);
            ps.setInt(3, quantity);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare insert prescription", e);
        }
    }

    // find all prescriptions for a medical record via join with medications table
    // returns formatted strings with: prescription_id, med_id, med_name, quantity, timestamp
    // join ensures medication names are fetched without separate query
    public List<String> findByRecordId(int recordId) {
        String sql = """
            SELECT p.prescription_id, p.medication_id, m.med_name, p.quantity, p.prescribed_at
            FROM prescriptions p
            JOIN medications m ON m.medication_id = p.medication_id
            WHERE p.record_id = ?
            ORDER BY p.prescribed_at
            """;
        List<String> rows = new ArrayList<>();
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, recordId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(
                        rs.getInt("prescription_id") + " | med_id: " + rs.getInt("medication_id") +
                        " | " + rs.getString("med_name") +
                        " | qty: " + rs.getInt("quantity") +
                        " | at: " + rs.getTimestamp("prescribed_at")
                    );
                }
            }
            return rows;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findByRecordId prescriptions", e);
        }
    }
}
