package repository;

import model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDao {

    // insert one client into the database
    public int insert(Client client) {
        String sql = "INSERT INTO clients(name, surname, phone_number, email, address) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, client.getName());
            ps.setString(2, client.getSurname());
            ps.setString(3, client.getPhoneNumber());
            ps.setString(4, client.getEmail());
            ps.setString(5, client.getAddress());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare insert client", e);
        }
    }

    // read all clients in a simple display format
    public List<String> findAllBasic() {
        String sql = "SELECT client_id, name, surname, phone_number, email, address FROM clients ORDER BY client_id";
        List<String> rows = new ArrayList<>();

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows.add(
                        rs.getInt("client_id") + " | " +
                                rs.getString("name") + " " + rs.getString("surname") + " | " +
                                rs.getString("phone_number") + " | " +
                                rs.getString("email") + " | " +
                                rs.getString("address")
                );
            }
            return rows;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findAll clients", e);
        }
    }

    // read one client by id
    public String findByIdBasic(int id) {
        String sql = "SELECT client_id, name, surname, phone_number, email, address FROM clients WHERE client_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return rs.getInt("client_id") + " | " +
                        rs.getString("name") + " " + rs.getString("surname") + " | " +
                        rs.getString("phone_number") + " | " +
                        rs.getString("email") + " | " +
                        rs.getString("address");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findById client", e);
        }
    }

    // update one client row by id
    public boolean update(int clientId, Client client) {
        String sql = "UPDATE clients SET name = ?, surname = ?, phone_number = ?, email = ?, address = ? WHERE client_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, client.getName());
            ps.setString(2, client.getSurname());
            ps.setString(3, client.getPhoneNumber());
            ps.setString(4, client.getEmail());
            ps.setString(5, client.getAddress());
            ps.setInt(6, clientId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare update client", e);
        }
    }

    // delete one client row by id
    public boolean delete(int clientId) {
        String sql = "DELETE FROM clients WHERE client_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, clientId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare delete client", e);
        }
    }

}
