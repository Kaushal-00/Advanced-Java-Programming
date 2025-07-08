## **Introduction to JDBC:**

- JDBC = Java Database Connectivity
- It is a Java API (a collection of pre-defined classes and interfaces) that lets your Java program connect and talk to a database, like:
    - MySQL
    - Oracle
    - PostgreSQL
    - SQLite

### **Used For:**

- Connecting Java to database
- Sending SQL queries (like `SELECT`, `INSERT`, `DELETE`)
- Getting results and show them in Java

| Reason               | Explanation                                               |
| -------------------- | --------------------------------------------------------- |
| Connect to Database  | JDBC lets Java apps connect with almost any database      |
| Run SQL Queries      | You can run SQL commands from your Java program           |
| Show Results in Java | You can display or use the result data (like `ResultSet`) |
| Insert/Update/Delete | You can modify data in the database using Java            |
| Secure and Scalable  | JDBC is secure, efficient, and can be used in big apps    |

---

### **Example:**

```java
import java.sql.*;

public class Demo {
    public static void main(String[] args) {
        try {
            // Step 1: Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Step 2: Connect to DB
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/college", "root", "password"
            );

            // Step 3: Run SQL Query
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM student");

            // Step 4: Show result
            while(rs.next()) {
                System.out.println(rs.getInt(1) + " " + rs.getString(2));
            }

            // Step 5: Close connection
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
```

---

### **ODBC:**

- ODBC = Open Database Connectivity
- It is a standard API developed by Microsoft.
- It allows any programming language (like C, C++, VB) to connect to any database (MySQL, Oracle, etc.).
- It's a platform-independent way to interact with databases — but it is mostly used in Windows.

- JDBC = Java Database Connectivity
- It is a Java-specific API provided by Oracle.
- It allows Java programs to connect and interact with databases.
- It is pure Java, and works on all platforms (Windows, Linux, Mac).

---

### **JDBC vs ODBC:**

| Feature                | JDBC                              | ODBC                                       |
| ---------------------- | --------------------------------- | ------------------------------------------ |
| Full Form              | Java Database Connectivity        | Open Database Connectivity                 |
| Developed By           | Oracle (Java-specific)            | Microsoft (general-purpose)                |
| Language Support       | Only Java                         | Multiple languages (C, C++, Python, etc.)  |
| Platform               | Platform-independent (Java)       | Mostly Windows-based                       |
| Installation           | No special driver needed for Java | Requires ODBC driver installation          |
| Security & Performance | Better for Java apps              | Slower with Java (needs bridge)            |
| How Java Uses It       | Direct via JDBC API               | Indirect via JDBC-ODBC bridge (deprecated) |
| Current Use            | Widely used in Java world         | Less used in Java today                    |

---