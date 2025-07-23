package in.ac.adit;

import java.io.*;
import java.sql.*;

public class ExcelToMySQL {
	
	static final String JDBC_URL = "jdbc:mysql://localhost:3306/student_db";
	static final String USERNAME = "root";
	static final String PASSWORD = "Kkaushal@07";
	
    public static void main(String[] args) {
    	
    	String filePath = "resources/Students_Data.csv";

        FileReader fr = null;
        BufferedReader br = null;

        try {
        	
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);

            System.out.println("Printing raw lines from file:\n-----------------------------");
            String rawLine;
            
            while ((rawLine = br.readLine()) != null) {
                System.out.println(rawLine);
            }

            // Re-initialize after printing (BufferedReader doesn't reset automatically)
            fr = new FileReader(filePath);
            br = new BufferedReader(fr);

            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            
            String sql = "INSERT INTO students (student_id, name, age, phone) VALUES (?, ?, ?, ?)";
            
            PreparedStatement pstm = connection.prepareStatement(sql);

            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {
            	
                if (skipHeader) {
                    skipHeader = false; // skip first header row
                    continue;
                }

                String[] data = line.split(",");

                int id = Integer.parseInt(data[0]);
                String name = data[1];
                int age = Integer.parseInt(data[2]);
                long phone = Long.parseLong(data[3]);

                pstm.setInt(1, id);
                pstm.setString(2, name);
                pstm.setInt(3, age);
                pstm.setLong(4, phone);

                pstm.executeUpdate();
            }

            System.out.println("\n Data inserted successfully!");

            connection.close();
            br.close();
            fr.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
