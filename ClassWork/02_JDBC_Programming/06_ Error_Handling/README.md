# **Error Handling in JDBC:**

- In JDBC, error handling is important to ensure our program doesn't crash when something goes wrong (like wrong SQL query, DB connection failure, etc.).
- Most JDBC operations throw `SQLException`, so we use `try-catch` to handle it.

### **Example:**

```java
try {
    Connection con = DriverManager.getConnection(...);
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM students");
} catch (SQLException e) {
    System.out.println("Error occurred: " + e.getMessage());
}
```

---

## `SQLException`:

- `SQLException` is a checked exception in Java.
- It is thrown when something goes wrong during JDBC operations like:
    - Connection issues
    - Wrong SQL query
    - Table doesn’t exist
    - Incorrect column name
    - Login failure, etc.

### **Common Methods of SQLException:**

- When an error occurs, you can use the following methods from `SQLException` object to understand what went wrong:

| Method               | What It Shows                            |
| -------------------- | ---------------------------------------- |
| `getMessage()`       | The error message (human-readable)       |
| `getErrorCode()`     | Vendor-specific DB error code            |
| `getSQLState()`      | Standard SQL error code (string)         |
| `printStackTrace()`  | Technical details of the error           |
| `getNextException()` | If there are multiple chained exceptions |

---

### 1. `getMessage()`

- Gives a simple description of what went wrong.

#### **Example:**

```java
catch (SQLException e) {
    System.out.println("Error Message: " + e.getMessage());
}
```

##### **Output:**

```
Error Message: Table 'student_db.wrong_table' doesn't exist
```

---

### 2. `getSQLState()`

- Returns a 5-digit error code defined by the SQL standard.
- Helps to know type of error (like table not found, syntax error, etc.)

#### **Example:**

```java
catch (SQLException e) {
    System.out.println("SQL State: " + e.getSQLState());
}
```

##### **Output:**

```
SQL State: 42S02
```
- Here, `42S02` means "Base table or view not found" in MySQL

---

### 3. `getErrorCode()`

- Returns a database-specific error number (each DB like MySQL, Oracle has different codes)

#### **Example:**

```java
catch (SQLException e) {
    System.out.println("Error Code: " + e.getErrorCode());
}
```

##### **Output:**

```
Error Code: 1146
```
- Means in MySQL, 1146 = Table doesn't exist

---

### 4. `printStackTrace()`

- Prints the full error trace (useful for debugging). Shows where the error happened in the code.

#### **Example:**

```java
catch (SQLException e) {
    e.printStackTrace(); // Useful for developers
}
```

##### **Output:**

```
java.sql.SQLSyntaxErrorException: Table 'student_db.wrong_table' doesn't exist
	at com.mysql.cj.jdbc.ClientPreparedStatement.executeQuery ...
	at SQLExceptionDemo.main(SQLExceptionDemo.java:17)
```

---

### 5. `getNextException()`

If there are multiple SQL exceptions, this helps you to go to the next one.

#### **Example:**

```java
catch (SQLException e) {
    while (e != null) {
        System.out.println("Message: " + e.getMessage());
        e = e.getNextException(); // move to next exception if available
    }
}
```
- Mostly used in batch queries, like if multiple inserts fail together.

---

Sure Kaushal! Let’s understand the **`SQLWarning`** class in a very simple way with example.

---

## `SQLWarning`:

- It is a subclass of `SQLException`.
- It represents warnings, not errors.
- Warnings don’t stop the program, but they give important messages from the database.
- Usually occurs when:
  - Query succeeds but something unusual happens
  - Example: Using deprecated feature, data truncation, etc.

| `SQLException`                   | `SQLWarning`                   |
| -------------------------------- | ------------------------------ |
| Represents errors                | Represents warnings            |
| Stops the program (if unhandled) | Doesn’t stop execution         |
| Caught using `catch`             | Accessed using `getWarnings()` |

---

### **Common Methods in `SQLWarning`:**

- Just like `SQLException`:
- `getMessage()` – Description of the warning
- `getSQLState()` – SQL standard code
- `getErrorCode()` – DB-specific code
- `getNextWarning()` – Next warning in chain

---

### **Example:**

```java
import java.sql.*;

public class SQLWarningDemo {
    public static void main(String[] args) {
        try {

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "password");

            Statement stmt = con.createStatement();

            // Suppose this causes a warning (like truncating data)
            stmt.executeUpdate("INSERT INTO demo (name) VALUES ('A very very long name that may be truncated')");

            // Get warning from connection
            SQLWarning warning = stmt.getWarnings();

            while (warning != null) {
                System.out.println("Warning:");
                System.out.println("Message: " + warning.getMessage());
                System.out.println("SQLState: " + warning.getSQLState());
                System.out.println("ErrorCode: " + warning.getErrorCode());
                warning = warning.getNextWarning(); // next warning if any
            }

            stmt.close();
            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

---

#### **Output:**

```
Warning:
Message: Data truncated for column 'name' at row 1
SQLState: 01000
ErrorCode: 1265
```

---