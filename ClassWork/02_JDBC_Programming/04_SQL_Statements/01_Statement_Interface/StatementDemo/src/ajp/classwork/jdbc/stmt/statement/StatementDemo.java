package ajp.classwork.jdbc.stmt.statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class StatementDemo {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/student_db";
        String username = "root";
        String password = "Kkaushal@07";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(jdbcURL, username, password);
            Statement stmt = con.createStatement();

            // ----------------- SELECT -----------------
            String selectQuery = "SELECT * FROM students";
            ResultSet rs = stmt.executeQuery(selectQuery);
            
            System.out.println("\nStudent Records:");
            while (rs.next()) {
            	int id = rs.getInt("student_id");
            	String name = rs.getString("name");
            	int age = rs.getInt("age");
            	String phone = rs.getString("phone");
            	System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age + ", Phone: " + phone);
            }
            
            // ----------------- INSERT -----------------
            String insertQuery = "INSERT INTO students (student_id, name, age, phone) VALUES (5, 'Kaushal', 20, '9998881723')";
            stmt.executeUpdate(insertQuery);
            System.out.println("\nRecord Inserted!");


            // ----------------- UPDATE -----------------
            String updateQuery = "UPDATE students SET age = 21 WHERE student_id = 1";
            stmt.executeUpdate(updateQuery);
            System.out.println("\nRecord Updated!");

            // ----------------- DELETE -----------------
            String deleteQuery = "DELETE FROM students WHERE student_id = 3";
            stmt.executeUpdate(deleteQuery);
            System.out.println("\nRecord Deleted!");

            stmt.close();
            con.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
