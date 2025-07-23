package in.ac.adit;

import java.sql.*;

public class ResultSetDemo {
    public static void main(String[] args) {

        String jdbcURL = "jdbc:mysql://localhost:3306/student_db";
        String username = "root";
        String password = "Kkaushal@07";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");


            Connection con = DriverManager.getConnection(jdbcURL, username, password);

            Statement stmt = con.createStatement(
                    ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            ResultSet rs = stmt.executeQuery("SELECT * FROM students");

            // -------------------- 1. Print All Records --------------------
            System.out.println("All Students:");
            while (rs.next()) {
                System.out.println(
                    rs.getInt("student_id") + " - " +
                    rs.getString("name") + " - " +
                    rs.getInt("age") + " - " +
                    rs.getString("phone")
                );
            }

            // -------------------- 2. Scroll to Last Row --------------------
            if (rs.last()) {
                System.out.println("\nLast Student Record:");
                System.out.println(rs.getInt(1) + " - " + rs.getString(2));
            }

            // -------------------- 3. Scroll to First Row --------------------
            if (rs.first()) {
                System.out.println("\nFirst Student Record:");
                System.out.println(rs.getInt(1) + " - " + rs.getString(2));
            }

            // -------------------- 4. Update a Record --------------------
            rs.absolute(2); // Move to second row
            rs.updateString("name", "UpdatedBob");
            rs.updateRow(); // Commit update
            System.out.println("\nUpdated second row name to 'UpdatedBob'.");

            // -------------------- 5. Insert a New Record --------------------
            rs.moveToInsertRow();
            rs.updateInt("student_id", 4);
            rs.updateString("name", "David");
            rs.updateInt("age", 23);
            rs.updateString("phone", "4567890123");
            rs.insertRow();
            System.out.println("Inserted new student: David");

            // -------------------- 6. Use ResultSetMetaData --------------------
            rs.beforeFirst(); // Move before first to re-read from start
            ResultSetMetaData rsmd = rs.getMetaData();

            System.out.println("\nColumn Info:");
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                System.out.println("Column " + i + ": " + rsmd.getColumnName(i) + " - " + rsmd.getColumnTypeName(i));
            }

            rs.close();
            stmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
