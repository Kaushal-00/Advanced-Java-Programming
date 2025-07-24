# **Batch Processing:**

- In JDBC, Batch Processing is used to execute multiple SQL statements (like `INSERT`, `UPDATE`, `DELETE`) in a single go, rather than executing them one by one.
- This:
    - Reduces communication with the database
    - Increases performance
    - Saves time when executing similar operations repeatedly

---

## **Example:**

- We want to insert 3 records into the `student` table:

```sql
CREATE TABLE student (
    id INT PRIMARY KEY,
    name VARCHAR(50)
);
```

---

#### **Batch Using `Statement`:**

```java
import java.sql.*;

public class BatchExample {
    public static void main(String[] args) {
        try (
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "password");
            Statement stmt = con.createStatement();
        ) {
            con.setAutoCommit(false); // optional, to control rollback if any error

            // Add queries to batch
            stmt.addBatch("INSERT INTO student VALUES (1, 'Kaushal')");
            stmt.addBatch("INSERT INTO student VALUES (2, 'Karan')");
            stmt.addBatch("INSERT INTO student VALUES (3, 'Henil')");

            // Execute all at once
            int[] result = stmt.executeBatch();

            con.commit(); // commit all
            System.out.println("Inserted " + result.length + " records.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

#### **Batch Using `PreparedStatement`:**

```java
import java.sql.*;

public class PreparedBatchExample {
    public static void main(String[] args) {
        try (
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "password");
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO student VALUES (?, ?)");
        ) {
            con.setAutoCommit(false);

            pstmt.setInt(1, 4);
            pstmt.setString(2, "Raj");
            pstmt.addBatch();

            pstmt.setInt(1, 5);
            pstmt.setString(2, "Mehul");
            pstmt.addBatch();

            pstmt.setInt(1, 6);
            pstmt.setString(2, "Nidhi");
            pstmt.addBatch();

            int[] result = pstmt.executeBatch();
            con.commit();

            System.out.println("Inserted " + result.length + " rows using PreparedStatement.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

- `stmt.executeBatch()` is used to run all the queries we added using `addBatch()`.
- It returns an array of integers.
- That array tells you the result of each query.
- We added 3 SQL statements to the batch:

```java
stmt.addBatch("INSERT INTO student VALUES (1, 'A')");
stmt.addBatch("INSERT INTO student VALUES (2, 'B')");
stmt.addBatch("INSERT INTO student VALUES (3, 'C')");

int[] result = stmt.executeBatch();
```

- Now `result` is like:

```java
result[0] → how many rows affected by 1st query  
result[1] → how many rows affected by 2nd query  
result[2] → how many rows affected by 3rd query
```

- Usually, each value will be `1` (if one row was inserted), or `0` if nothing happened.

- Internally, JDBC creates a queue/list to hold all the SQL statements when you use `addBatch()`.
- JDBC sends all added queries together to the database, and runs them one by one internally.
- Internally, it works like this:
    1. JDBC collects all SQL statements into a list (batch).
    2. When you call `executeBatch()`:
        - JDBC opens 1 connection to DB.
        - Sends all collected queries at once.
        - Database runs them sequentially, like a loop.
    3. JDBC receives the result of each query.
    4. Returns all results in an `int[]` array.

---

### **Common Methods:**

- `Statement` interface provides methods for batch processing. `java.sql.Statement`
- `PreparedStatement` supports batch processing as it extends `Statement` interface. `java.sql.PreparedStatement`
- `Connection` interface provides methods to manage transactions. `java.sql.Connection`

| Method                 | Description                                    |
| ---------------------- | ---------------------------------------------- |
| `addBatch(sql)`        | Adds a SQL statement to the batch              |
| `executeBatch()`       | Executes all added statements in one go        |
| `clearBatch()`         | Clears all statements from the batch           |
| `setAutoCommit(false)` | Helps in manual transaction control (optional) |
| `commit()`             | Commits all successful batch commands          |
| `rollback()`           | Rolls back the transaction if an error occurs  |

---

### 1. `addBatch(String sql)`
- Used to add a SQL query to the batch list.

```java
stmt.addBatch("INSERT INTO student VALUES (1, 'Kaushal')");
stmt.addBatch("INSERT INTO student VALUES (2, 'Henil')");
stmt.addBatch("INSERT INTO student VALUES (3, 'Karan')");
```

- These queries are stored in an internal list, not executed yet.

---

### 2. `executeBatch()`
- Used to run all queries added with `addBatch()`.

```java
int[] result = stmt.executeBatch();
```

- This runs all the added queries, one by one, in the same order.
- Returns an array of integers showing the number of rows affected by each query.

---

### 3. `clearBatch()`
- Used to clear all queries from the batch list (before `executeBatch()` is called).

```java
stmt.clearBatch();
```
- Use this if you added wrong queries or want to cancel the batch.

---

### 4. `Connection.setAutoCommit(false)`

- Used to manually control transactions.
- So if one query fails, you can roll back all.

```java
conn.setAutoCommit(false);
```

---

### 5. `conn.commit()` and `conn.rollback()`

- These are used to finalize or cancel the entire batch.

```java
conn.commit();    // Save changes permanently
conn.rollback();  // Cancel all changes (if error)
```

---