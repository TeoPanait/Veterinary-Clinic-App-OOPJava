package repository;

import model.Cat;
import model.Dog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetDao {

    // insert a dog using two related tables
    public int insertDog(Dog dog, int ownerId) {
        String insertPetSql = "INSERT INTO pets(owner_id, name_pet, age, weight, pet_type) VALUES (?, ?, ?, ?, ?)";
        String insertDogSql = "INSERT INTO dogs(pet_id, dog_breed, size) VALUES (?, ?, ?)";

        try (Connection con = DbConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                int petId;
                try (PreparedStatement psPet = con.prepareStatement(insertPetSql, Statement.RETURN_GENERATED_KEYS)) {
                    psPet.setInt(1, ownerId);
                    psPet.setString(2, dog.getNamePet());
                    psPet.setInt(3, dog.getAge());
                    psPet.setDouble(4, dog.getWeight());
                    psPet.setString(5, "DOG");
                    psPet.executeUpdate();

                    try (ResultSet rs = psPet.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException("Nu s-a generat pet_id pentru DOG.");
                        }
                        petId = rs.getInt(1);
                    }
                }

                try (PreparedStatement psDog = con.prepareStatement(insertDogSql)) {
                    psDog.setInt(1, petId);
                    psDog.setString(2, dog.getDogBreed());
                    psDog.setString(3, dog.getSize());
                    psDog.executeUpdate();
                }

                con.commit();
                return petId;
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare insert DOG", e);
        }
    }

    public int insertCat(Cat cat, int ownerId) {
        String insertPetSql = "INSERT INTO pets(owner_id, name_pet, age, weight, pet_type) VALUES (?, ?, ?, ?, ?)";
        String insertCatSql = "INSERT INTO cats(pet_id, cat_breed, fur_type, is_indoor) VALUES (?, ?, ?, ?)";

        try (Connection con = DbConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                int petId;
                try (PreparedStatement psPet = con.prepareStatement(insertPetSql, Statement.RETURN_GENERATED_KEYS)) {
                    psPet.setInt(1, ownerId);
                    psPet.setString(2, cat.getNamePet());
                    psPet.setInt(3, cat.getAge());
                    psPet.setDouble(4, cat.getWeight());
                    psPet.setString(5, "CAT");
                    psPet.executeUpdate();

                    try (ResultSet rs = psPet.getGeneratedKeys()) {
                        if (!rs.next()) {
                            throw new SQLException("Nu s-a generat pet_id pentru CAT.");
                        }
                        petId = rs.getInt(1);
                    }
                }

                try (PreparedStatement psCat = con.prepareStatement(insertCatSql)) {
                    psCat.setInt(1, petId);
                    psCat.setString(2, cat.getCatBreed());
                    psCat.setString(3, cat.getFurType());
                    psCat.setBoolean(4, cat.isIndoor());
                    psCat.executeUpdate();
                }

                con.commit();
                return petId;
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare insert CAT", e);
        }
    }

    public String findByIdBasic(int petId) {
        String sql = """
            SELECT p.pet_id,
                   p.name_pet,
                   p.age,
                   p.weight,
                   p.pet_type,
                   c.name AS owner_name,
                   c.surname AS owner_surname,
                   d.dog_breed,
                   d.size,
                   ct.cat_breed,
                   ct.fur_type,
                   ct.is_indoor
            FROM pets p
            JOIN clients c ON c.client_id = p.owner_id
            LEFT JOIN dogs d ON d.pet_id = p.pet_id
            LEFT JOIN cats ct ON ct.pet_id = p.pet_id
            WHERE p.pet_id = ?
            """;

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, petId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                String base = rs.getInt("pet_id") + " | " +
                        rs.getString("name_pet") + " | " +
                        rs.getInt("age") + " ani | " +
                        rs.getDouble("weight") + " kg | " +
                        rs.getString("pet_type") + " | owner: " +
                        rs.getString("owner_name") + " " + rs.getString("owner_surname");

                String type = rs.getString("pet_type");
                if ("DOG".equals(type)) {
                    return base + " | breed: " + rs.getString("dog_breed") + " | size: " + rs.getString("size");
                } else {
                    return base + " | breed: " + rs.getString("cat_breed") +
                            " | fur: " + rs.getString("fur_type") +
                            " | indoor: " + rs.getBoolean("is_indoor");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findById pet", e);
        }
    }

    public List<String> findAllBasic() {
        String sql = """
            SELECT p.pet_id,
                   p.name_pet,
                   p.age,
                   p.weight,
                   p.pet_type,
                   c.name AS owner_name,
                   c.surname AS owner_surname,
                   d.dog_breed,
                   d.size,
                   ct.cat_breed,
                   ct.fur_type,
                   ct.is_indoor
            FROM pets p
            JOIN clients c ON c.client_id = p.owner_id
            LEFT JOIN dogs d ON d.pet_id = p.pet_id
            LEFT JOIN cats ct ON ct.pet_id = p.pet_id
            ORDER BY p.pet_id
            """;

        List<String> rows = new ArrayList<>();

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String base = rs.getInt("pet_id") + " | " +
                        rs.getString("name_pet") + " | " +
                        rs.getInt("age") + " ani | " +
                        rs.getDouble("weight") + " kg | " +
                        rs.getString("pet_type") + " | owner: " +
                        rs.getString("owner_name") + " " + rs.getString("owner_surname");

                String type = rs.getString("pet_type");
                if ("DOG".equals(type)) {
                    rows.add(base + " | breed: " + rs.getString("dog_breed") + " | size: " + rs.getString("size"));
                } else {
                    rows.add(base + " | breed: " + rs.getString("cat_breed") +
                            " | fur: " + rs.getString("fur_type") +
                            " | indoor: " + rs.getBoolean("is_indoor"));
                }
            }
            return rows;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare findAll pets", e);
        }
    }

    public boolean updatePet(int petId, String newName, int newAge, double newWeight){
        String sql= "UPDATE pets SET name_pet=?, age=?, weight=? WHERE pet_id=?";
        try(Connection con= DbConnection.getConnection();
        PreparedStatement ps= con.prepareStatement(sql)){
            ps.setString(1, newName);
            ps.setInt(2, newAge);
            ps.setDouble(3, newWeight);
            ps.setInt(4, petId);

            int rowsAffected= ps.executeUpdate();
            return rowsAffected>0;
    }catch(SQLException e){
        throw new RuntimeException("Eroare update pet", e);
        }
    }

    public boolean updateDog(int petId, String newBreed, String newSize) {
        String sql = "UPDATE dogs SET dog_breed = ?, size = ? WHERE pet_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newBreed);
            ps.setString(2, newSize);
            ps.setInt(3, petId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare update dog", e);
        }
    }

    public boolean updateCat(int petId, String newBreed, String newFurType, boolean newIndoor) {
        String sql = "UPDATE cats SET cat_breed = ?, fur_type = ?, is_indoor = ? WHERE pet_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {


        ps.setString(1, newBreed);
        ps.setString(2, newFurType);
        ps.setBoolean(3, newIndoor);
        ps.setInt(4, petId);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        throw new RuntimeException("Eroare update cat", e);
    }
}

    // delete one pet row
    public boolean deletePet(int petId) {
        String sql = "DELETE FROM pets WHERE pet_id = ?";
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, petId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare delete pet", e);
        }
    }
}
