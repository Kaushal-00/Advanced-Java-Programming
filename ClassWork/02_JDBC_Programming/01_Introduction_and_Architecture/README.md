# **Introduction to JDBC:**

- JDBC = Java Database Connectivity
- It is a Java API (a collection of pre-defined classes and interfaces) that lets your Java program connect and talk to a database, like:
    - MySQL
    - Oracle
    - PostgreSQL
    - SQLite

## **Used For:**

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

## **Example:**

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

## **ODBC:**

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

## **JDBC Architectures:**

- JDBC Architecture explains how a Java program communicates with a database using the JDBC API.
- Components Involved:

| Component               | Role                                                   |
| ----------------------- | ------------------------------------------------------ |
| Java Application        | The program written by you in Java                     |
| JDBC API                | Java classes and interfaces to connect to the database |
| JDBC Driver Manager     | Manages the JDBC drivers and creates connections       |
| JDBC Driver             | Converts Java calls to database-specific calls         |
| Database                | Stores your actual data (like MySQL, Oracle, etc.)     |

- Java Application:
    - You write code to:
    - Connect to DB
    - Run queries
    - Display results

- JDBC API:
    - You use Java classes like:
    - `DriverManager`
    - `Connection`
    - `Statement`
    - `ResultSet`

- JDBC DriverManager:
    - It chooses the right JDBC Driver
    - Manages connections to different types of databases

- JDBC Driver:
    - Converts JDBC function calls into native calls for the database
    - Example: `MySQLDriver`, `OracleDriver`, etc.

- Database:
    - Executes SQL queries (like `SELECT * FROM student`)
    - Sends data/results back to Java program

- There are two common architectures:
    - 2-tier architecture
    - 3-tier architecture

---

### **2-Tier Architecture:**

- Your Java Application directly connects to the Database using JDBC.
- Fast and simple
- Not suitable for large, distributed applications

---

#### **Structure:**

```
+--------------------+
|  Java Application  |
+--------------------+
         |
         | JDBC API
         ↓
+--------------------+
|   Database Server   |
+--------------------+
```

---

#### **Example:**

- We create a Java program to manage student records.
- Java → Connects to MySQL database directly
- Java sends: `SELECT * FROM student`
- MySQL sends result back → Java prints it
- Used in:
    - Small desktop applications
    - College assignments
    - Local projects

---

### **3-Tier Architecture:**

- Your Java Application talks to a middle layer (like a server), and that server talks to the database.
- More secure
- Scalable for big applications
- Business logic is separate
- More setup and complex

---

#### **Structure:**

```
+--------------------+
|  Java Application  |  ← UI or Frontend
+--------------------+
         |
         | HTTP Request (e.g., via Servlet or JSP)
         ↓
+--------------------+
|   Application Server (Tomcat, Spring) |
|   (Business Logic & JDBC here)        |
+--------------------+
         |
         | JDBC API
         ↓
+--------------------+
|   Database Server   |
+--------------------+
```

---

#### **Example:**

- Java App (frontend) sends request: "Add student"
- Server (backend using Servlet/JSP/Spring) receives request
- Server runs JDBC code: `INSERT INTO student VALUES (...)`
- Data is added to database
- Server sends back a success message
- Used in:
    - Web applications
    - Enterprise systems
    - Real-world professional apps

---

## **Main Components of JDBC API:**

| Component           | Purpose                                                          |
| ------------------- | ---------------------------------------------------------------- |
| `DriverManager`     | Connects your Java program to the correct JDBC driver            |
| `Connection`        | Represents the connection to the database                        |
| `Statement`         | Used to execute SQL queries (like SELECT, INSERT)                |
| `PreparedStatement` | Like `Statement`, but used for dynamic queries (with parameters) |
| `CallableStatement` | Used to call stored procedures in the database                   |
| `ResultSet`         | Stores output from SELECT queries                                |
| `SQLException`      | Handles errors during JDBC operations                            |
| `ResultSetMetaData` | Gives info about the columns in a ResultSet                      |
| `SQLWarning`        | Handles non-critical warnings                                    |
| `Driver`            | Interface that every JDBC driver must implement                  |

---