# **Statement Interface:**

- `Statement` is a JDBC interface that is used to send and execute static SQL queries (like `SELECT`, `INSERT`, `UPDATE`, `DELETE`) to the database.
- It belongs to the package:
```java
java.sql.Statement
```
- Used when your query doesn't need user input or when you build the query using string concatenation (not recommended for user input).

---

#### **Syntax:**

```java
Statement stmt = con.createStatement();
```

---

- `con.createStatement()` is a method of the `Connection` interface that Returns a new `Statement` object from our active database connection (`con`).
- `Connection` (`con`) = Telephone line to the database
- `Statement` (`stmt`) = Your voice (used to send commands over the line)
- Don't Forget:
    - `stmt.close();`
    - `con.close();`

---

### **Common Methods:**

| Method                      | Use Case                                                           |
| --------------------------- | ------------------------------------------------------------------ |
| `executeQuery(String sql)`  | For `SELECT` queries, returns `ResultSet`                          |
| `executeUpdate(String sql)` | For `INSERT`, `UPDATE`, `DELETE` — returns number of rows affected |
| `execute(String sql)`       | For any SQL, returns `true`/`false`                                |
| `close()`                   | To close the statement                                             |

---

### **`executeQuery(String sql)`:**

- Used to run SQL `SELECT` queries (i.e., fetching data)
- It returns `ResultSet` object — a table of data from the database.

#### **Example:**

```java
Statement stmt = con.createStatement();

ResultSet rs = stmt.executeQuery("SELECT * FROM student");

while (rs.next()) {
    System.out.println(rs.getInt("id") + " " + rs.getString("name"));
}
```

---

### **`executeUpdate(String sql)`:**

- Used to run `INSERT`, `UPDATE`, or `DELETE` queries (i.e., modifying data)
- It returns an `int` — the number of rows affected.

#### **Example:**

```java
// Insert
int inserted = stmt.executeUpdate("INSERT INTO student VALUES (4, 'Kaushal')");
System.out.println("Inserted rows: " + inserted);

// Update
int updated = stmt.executeUpdate("UPDATE student SET name='Karan' WHERE id=4");
System.out.println("Updated rows: " + updated);

// Delete
int deleted = stmt.executeUpdate("DELETE FROM student WHERE id=4");
System.out.println("Deleted rows: " + deleted);
```

---

### **`execute(String sql)`:**

- Used to run any type of SQL (SELECT, INSERT, etc.) when you don't know what will be returned.
- It returns a boolean:
    - `true` → if query returns a `ResultSet` (like SELECT)
    - `false` → if query returns an update count (like INSERT)

#### **Example:**

```java
boolean result = stmt.execute("SELECT * FROM student");

if (result) {
    System.out.println("It returned data (ResultSet).");
} else {
    System.out.println("It returned an update count.");
}
```

---

### **`close()`:**

- Used to Closing the `Statement` to free up memory

#### **Example:**

```java
stmt.close();
```

---

## **Example:**

```java
import java.sql.*;

public class StatementExample {
    public static void main(String[] args) {
        try {
            // Load Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to DB
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/college", "root", "password"
            );

            // Create Statement object
            Statement stmt = con.createStatement();

            // Execute SELECT query
            ResultSet rs = stmt.executeQuery("SELECT * FROM student");

            while (rs.next()) {
                System.out.println(rs.getInt("id") + " " + rs.getString("name"));
            }

            // Execute INSERT query
            int rows = stmt.executeUpdate("INSERT INTO student VALUES (3, 'Karan')");
            System.out.println("Inserted rows: " + rows);

            // Close resources
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
```

---

### **Limitations:**

| Issue              | Description                                                |
| ------------------ | ---------------------------------------------------------- |
| SQL Injection Risk | If you build SQL using `+`, hackers can inject SQL         |
| Not Precompiled    | Each time the query is compiled again — slower performance |
| Not Safe for Input | You must manually handle input formatting                  |

---

### **When to Use `Statement`:**

| Use It When...                         | Don't Use When...                                    |
| -------------------------------------- | ---------------------------------------------------- |
| Query is static and hardcoded          | Taking user input in query (use `PreparedStatement`) |
| No input is inserted                   | Inserting variables (use `?` placeholders instead)   |
| You want fast testing or quick results | You care about security/performance                  |

---

## Note: 

- `Statement` is an interface, so how are methods like `executeQuery()` working? Interfaces only have abstract methods (no body).

- When we write:

```java
Connection con = DriverManager.getConnection(...);
Statement stmt = con.createStatement();
```

- We are not creating an object of the interface itself. We are creating an object of a class that implements the `Statement` interface behind the scenes.
- `DriverManager.getConnection()` returns a `Connection` object of some class like `com.mysql.cj.jdbc.ConnectionImpl`
- `con.createStatement()` returns a `Statement` object of a class like `com.mysql.cj.jdbc.StatementImpl`
- These classes implement the `Statement` interface and provide the actual code for:
    - `executeQuery()`
    - `executeUpdate()`
    - `execute()`
- This methods Implemented in MySQL’s internal classes (not our job)
- The `Statement` interface defines the method, but the actual work is done by classes provided in the JDBC `.jar`, like `StatementImpl`, which implement and define how the method behaves.

- This is the power of interfaces and polymorphism in Java.