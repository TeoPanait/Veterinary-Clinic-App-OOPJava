package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import model.*;
import repository.*;

public class DbTest {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/vet_clinic";
        String user = "postgres";
        String password = "admin";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT current_database()")) {

            if (rs.next()) {
                System.out.println("Conectat la DB: " + rs.getString(1));
            } else {
                System.out.println("Conectat, dar query fara rezultat.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
