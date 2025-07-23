package in.ac.adit;

import java.sql.*;

public class ResultSetMetaDataDemo {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/student_db";
        String username = "root";
        String password = "Kkaushal@07";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(jdbcURL, username, password);

            String query = "SELECT * FROM students";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();

            int columnCount = rsmd.getColumnCount();
            System.out.println("Total Columns: " + columnCount);
            System.out.println();

            for (int i = 1; i <= columnCount; i++) {
                System.out.println("Column " + i + ":");
                System.out.println("  Name        : " + rsmd.getColumnName(i));
                System.out.println("  Label       : " + rsmd.getColumnLabel(i));
                System.out.println("  Type        : " + rsmd.getColumnTypeName(i));
                System.out.println("  Display Size: " + rsmd.getColumnDisplaySize(i));
                System.out.println("  Table Name  : " + rsmd.getTableName(i));
                System.out.println("  Is Nullable : " + (rsmd.isNullable(i) == ResultSetMetaData.columnNullable ? "YES" : "NO"));
                System.out.println("  Is Auto Inc : " + rsmd.isAutoIncrement(i));
                System.out.println();
            }

            rs.close();
            ps.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
