# **Database Connection:**

- A Database Connection is a bridge between your Java program and your database (like MySQL), so they can talk to each other.
- Java logs into MySQL using:
    - JDBC URL
    - Username
    - Password

---

### **Syntax to Connect:**

```java
Connection con = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/college",  // DB URL
    "root",                                 // Username
    "password"                              // Password
);

```

---

## **DriverManager:**

- `DriverManager` is a built-in Java class that manages all JDBC drivers and helps our program connect to a database.
- We don’t need to create an object of `DriverManager` — just use its static methods like: `DriverManager.getConnection(...);`
- It returns a `Connection` object — Java uses this object to send SQL queries.

- It acts like a "Driver Selector".
- Once the driver is loaded (`Class.forName(...)`), `DriverManager` knows:
    - This Java app wants to connect to MySQL. Let me give the right driver for it.
    - It then returns a `Connection` object.

| Description                                            | Code Example                       |
| ------------------------------------------------------ | ---------------------------------- |
| Load the JDBC Driver class (from .jar file)            | `Class.forName(...)`               |
| Connect to the database using URL, username & password | `DriverManager.getConnection(...)` |
| Get a `Connection` object to run queries               | Stored in variable `con`           |

---

## **JDBC URL Format:**

```
jdbc:<database-type>://<host>:<port>/<database-name>
```

### **Example:**

```
jdbc:mysql://localhost:3306/college
```

| Part        | Meaning                                |
| ----------- | -------------------------------------- |
| `jdbc`      | Using Java Database Connectivity       |
| `mysql`     | The database type (MySQL)              |
| `localhost` | The server address (your own computer) |
| `3306`      | Port number where MySQL runs (default) |
| `college`   | Name of our database                   |

---

## **Username & Password:**

- `"root"` = MySQL username
- `"password"` = MySQL password

- These must match our MySQL Workbench login.

---

## **Connection Object:**

- In JDBC, a `Connection` object is like a bridge between your Java program and the database.
- When we connect to the database using `DriverManager.getConnection(...)`, it returns a `Connection` object.
- used to:
    - Create SQL queries
    - Send them to the database
    - Manage transactions (commit/rollback)
    - Close the connection when done

| Task                               | Code Example                                           |
| ---------------------------------- | ------------------------------------------------------ |
| Create a SQL Statement             | `Statement stmt = con.createStatement();`              |
| Prepare a SQL query                | `PreparedStatement pstmt = con.prepareStatement(...);` |
| Call a stored procedure            | `CallableStatement cs = con.prepareCall(...);`         |
| Commit or rollback a transaction   | `con.commit();` / `con.rollback();`                    |
| Turn off auto-commit mode          | `con.setAutoCommit(false)`                             |
| Close the connection               | `con.close();`                                         |
| Checks if the connection is closed | `con.isClosed();`                                      |

- Always close the `Connection` when we are done:
```java
con.close();
```
- It frees up resources, avoids memory leaks, and keeps the database server healthy.

---

## **Example:**

```java
import java.sql.*;

public class ConnectDB {
    public static void main(String[] args) {
        try {
            // Step 1: Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Step 2: Connect to MySQL database
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/college",
                "root",
                "your_password"
            );

            // Step 3: Show success message
            System.out.println("Connected to MySQL Database!");

            // Step 4: Close connection
            con.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
```

---

## **Exmaple:**

```java
import java.sql.*;

public class ConnectionExample {
    public static void main(String[] args) {
        try {
            // Load driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Get connection object
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/college", "root", "password"
            );

            // Use connection to create statement and run a query
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM student");

            while (rs.next()) {
                System.out.println(rs.getInt("id") + " " + rs.getString("name"));
            }

            // Close the connection
            con.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
```

---

### **Exception Handling in JDBC:**

- In JDBC, almost all database-related errors are caught using the class:
```java
java.sql.SQLException
```
- It is a checked exception, so we must handle it.

---

#### **Basic Structure:**

```java
try {
    // JDBC code
} catch (SQLException e) {
    // Handle SQL errors
} catch (ClassNotFoundException e) {
    // Handle driver loading error
} catch (Exception e) {
    // Handle any other error
}
```

---

#### **Example:**

```java
import java.sql.*;

public class ExceptionHandlingExample {
    public static void main(String[] args) {
        try {
            // Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to MySQL
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/college", "root", "password"
            );

            // Create and execute query
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM student");

            while (rs.next()) {
                System.out.println(rs.getInt("id") + " " + rs.getString("name"));
            }

            // Close connection
            con.close();

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");

        } catch (Exception e) {
            System.out.println("Some other error: " + e);
        }
    }
}
```

| Method           | Description                          |
| ---------------- | ------------------------------------ |
| `getMessage()`   | Returns error message                |
| `getSQLState()`  | Returns SQL state code               |
| `getErrorCode()` | Returns database-specific error code |

- If the `.jar` file is missing or driver is not found, it throws:
```
ClassNotFoundException: com.mysql.cj.jdbc.Driver
```

---