// Step 1: MySQL Stored Procedure

// Create this procedure in your MySQL database:
/*
 	DELIMITER //

	CREATE PROCEDURE getAllStudents()
	BEGIN
    	SELECT * FROM students;
	END //

	DELIMITER ;
*/

package in.ac.adit;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class CallableStatementDemo {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/student_db";
        String username = "root";
        String password = "Kkaushal@07";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(jdbcURL, username, password);

            // ----------------- CALL STORED PROCEDURE -----------------
            CallableStatement cstmt = con.prepareCall("{call getAllStudents()}");
            ResultSet rs = cstmt.executeQuery();

            System.out.println("\nStudent Records from Stored Procedure:");
            while (rs.next()) {
                int id = rs.getInt("student_id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String phone = rs.getString("phone");

                System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age + ", Phone: " + phone);
            }

            rs.close();
            cstmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
