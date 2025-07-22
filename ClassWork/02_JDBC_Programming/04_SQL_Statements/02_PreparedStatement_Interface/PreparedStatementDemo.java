package in.ac.adit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PreparedStatementDemo {
    public static void main(String[] args) {
    	
        String jdbcURL = "jdbc:mysql://localhost:3306/student_db";
        String username = "root";
        String password = "Kkaushal@07";

        try {
        	
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(jdbcURL, username, password);

            // ----------------- SELECT -----------------
            String selectQuery = "SELECT * FROM students";
            PreparedStatement psSelect = con.prepareStatement(selectQuery);
            ResultSet rs = psSelect.executeQuery();

            System.out.println("\nStudent Records:");
            while (rs.next()) {
                int id = rs.getInt("student_id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String phone = rs.getString("phone");
                System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age + ", Phone: " + phone);
            }

            // ----------------- INSERT -----------------
            String insertQuery = "INSERT INTO students (student_id, name, age, phone) VALUES (?, ?, ?, ?)";
            PreparedStatement psInsert = con.prepareStatement(insertQuery);
            psInsert.setInt(1, 6);
            psInsert.setString(2, "Divyesh");
            psInsert.setInt(3, 20);
            psInsert.setString(4, "9997771723");
            psInsert.executeUpdate();
            System.out.println("\nRecord Inserted!");

            // ----------------- UPDATE -----------------
            String updateQuery = "UPDATE students SET age = ? WHERE student_id = ?";
            PreparedStatement psUpdate = con.prepareStatement(updateQuery);
            psUpdate.setInt(1, 51);
            psUpdate.setInt(2, 1);
            psUpdate.executeUpdate();
            System.out.println("\nRecord Updated!");

            // ----------------- DELETE -----------------
            String deleteQuery = "DELETE FROM students WHERE student_id = ?";
            PreparedStatement psDelete = con.prepareStatement(deleteQuery);
            psDelete.setInt(1, 4);
            psDelete.executeUpdate();
            System.out.println("\nRecord Deleted!");

            // Close connections
            rs.close();
            psSelect.close();
            psInsert.close();
            psUpdate.close();
            psDelete.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
