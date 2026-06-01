package repository;

import model.Veterinarian;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeterinarianDao {
    // insert one veterinarian row into the database
    public int insert(Veterinarian vet){
        String sql= "INSERT INTO veterinarians(name, surname, phone_number, specialization) VaLUES (?, ?, ?, ?)";
        try(Connection con= DbConnection.getConnection();
            PreparedStatement ps= con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, vet.getName());
            ps.setString(2, vet.getSurname());
            ps.setString(3, vet.getPhoneNumber());
            ps.setString(4, vet.getSpecialization());

            ps.executeUpdate();

            try(ResultSet rs= ps.getGeneratedKeys()){
                if(rs.next())  return rs.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException("Eroare insert vet",e);
        }
    }

    // read one veterinarian by id
    // read one veterinarian by id
    public String findByIdBasic(int id){
        String sql="SELECT vet_id, name, surname, phone_number, specialization FROM veterinarians WHERE vet_id = ?";
        try(Connection con= DbConnection.getConnection();
        PreparedStatement ps=con.prepareStatement(sql)){
            ps.setInt(1, id);

            try(ResultSet rs= ps.executeQuery()){
                if(!rs.next()) return null;
                return rs.getInt("vet_id") + " | " +
                        rs.getString("name") + " " + rs.getString("surname") + " | " +
                        rs.getString("phone_number") + " | " +
                        rs.getString("specialization");
            }
        }catch(SQLException e){
            throw new RuntimeException("Eroare findById vet", e);
        }
    }

    // read all veterinarians in display format
    // read all veterinarians in display format
    public List<String> findAllBasic(){
        String sql="SELECT vet_id, name, surname, phone_number, specialization FROM veterinarians ORDER BY vet_id";
        List<String> rows= new ArrayList<>();

        try(Connection con= DbConnection.getConnection();
        PreparedStatement ps= con.prepareStatement(sql);
        ResultSet rs= ps.executeQuery()){
            while(rs.next()){
                rows.add(
                        rs.getInt("vet_id") + " | " +
                                rs.getString("name") + " " + rs.getString("surname") + " | " +
                                rs.getString("phone_number") + " | " +
                                rs.getString("specialization")
                );
            }
            return rows;
        }catch(SQLException e){
            throw new RuntimeException("Eroare findAll vets", e);
        }
    }

    // update one veterinarian row by id
    // update one veterinarian row by id
    public boolean update(int vetId, Veterinarian vet){
        String sql="UPDATE veterinarians SET name=?, surname=?, phone_number=?, specialization=? WHERE vet_id=?";
        try(Connection con= DbConnection.getConnection();
        PreparedStatement ps= con.prepareStatement(sql)){
            ps.setString(1, vet.getName());
            ps.setString(2, vet.getSurname());
            ps.setString(3, vet.getPhoneNumber());
            ps.setString(4, vet.getSpecialization());
            ps.setInt(5, vetId);

            int rowsAffected=ps.executeUpdate();
            return rowsAffected>0;
    }catch(SQLException e){
        throw new RuntimeException("Eroare update vet", e);}
    }

    // delete one veterinarian row by id
    // delete one veterinarian row by id
    public boolean delete(int vetId){
        String sql="DELETE FROM veterinarians WHERE vet_id=?";
        try(Connection con= DbConnection.getConnection();
        PreparedStatement ps= con.prepareStatement(sql)){
            ps.setInt(1, vetId);
            int rowsAffected= ps.executeUpdate();
            return rowsAffected>0;
    }catch (SQLException e){
        throw new RuntimeException("Eroare delete vet", e);
    }
    }

}
