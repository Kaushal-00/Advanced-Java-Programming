# **PreparedStatement Interface:**

- `PreparedStatement` is a JDBC interface used to execute parameterized SQL queries safely and efficiently.
- Unlike `Statement`, it allows you to write SQL queries with variables using `?` placeholders.
- It is:
    - Safe (prevents SQL injection)
    - Faster (query is compiled once)
    - Clean & readable

---

## **Syntax:**

```java
PreparedStatement pstmt = con.prepareStatement("INSERT INTO student VALUES (?, ?)");
```

- Here, `?` are placeholders for actual values.
- Then you set values like this:

```java
pstmt.setInt(1, 101);           // sets 1st ? to 101
pstmt.setString(2, "Kaushal");  // sets 2nd ? to "Kaushal"
```

---

## **Example:**

```java
import java.sql.*;

public class PreparedStatementExample {
    public static void main(String[] args) {
        try {
            // Load Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to DB
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/college", "root", "password"
            );

            // Prepare SQL with ?
            String query = "INSERT INTO student VALUES (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);

            // Set values
            pstmt.setInt(1, 1);
            pstmt.setString(2, "Kaushal");

            // Execute
            pstmt.executeUpdate();
            System.out.println("Data Inserted!");

            con.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
```

---

## **Example – SELECT Query:**

```java
String query = "SELECT * FROM student WHERE id = ?";
PreparedStatement pstmt = con.prepareStatement(query);
pstmt.setInt(1, 1);  // search for ID 1

ResultSet rs = pstmt.executeQuery();

while (rs.next()) {
    System.out.println(rs.getInt("id") + " " + rs.getString("name"));
}
```

---

## **Common Methods:**

- `PreparedStatement` extends the `Statement` interface, but adds extra methods to safely set values (`?`) in the SQL query.
---

### `setInt(int parameterIndex, int value)`
- Used to set an integer value in the query.

```java
pstmt.setInt(1, 101);  // sets 1st ? to 101
```

---

### `setString(int parameterIndex, String value)`

- Used to set a string value.

```java
pstmt.setString(2, "Kaushal");  // sets 2nd ? to "Kaushal"
```

---

### `setDouble(int parameterIndex, double value)`

- Used to set a decimal value.

```java
pstmt.setDouble(3, 75.5);
```

---

### `setBoolean(int parameterIndex, boolean value)`

- Used for `true` or `false` values.

```java
pstmt.setBoolean(4, true);
```

---

### `executeUpdate()`

- Executes the query if it's an:
    - `INSERT`
    - `UPDATE`
    - `DELETE`

```java
int rows = pstmt.executeUpdate();
System.out.println("Rows affected: " + rows);
```

---

### `executeQuery()`

- Used to fetch data (i.e., run a `SELECT` query).

```java
ResultSet rs = pstmt.executeQuery();
```
---

### `execute()`

- Executes any SQL query (SELECT/INSERT/UPDATE/DELETE); returns `true` if result is a `ResultSet`, otherwise `false`.

```java
boolean result = pstmt.execute();
```

---

### `clearParameters()`

- Clears all the values you’ve set with `setInt`, `setString`, etc. Useful if you're reusing the same `PreparedStatement`.

```java
pstmt.clearParameters();
```

---

## **Statement vs PreparedStatement:**

### **`PreparedStatement` is Safer Than `Statement`because:**

- When we use `Statement`, we build SQL queries using string concatenation:

```java
String name = "Kaushal";
String query = "SELECT * FROM student WHERE name = '" + name + "'";
```

- If the user enters this instead of a normal name:

```java
name = "' OR '1'='1";
```

- The query becomes:

```sql
SELECT * FROM student WHERE name = '' OR '1'='1';
```

- `'1'='1'` is always true, so the whole condition becomes:
- false OR true → true
- So, This will return all records, even without knowing a valid name.
- This is called an SQL Injection Attack.

---

### **Example:**

```java
String name = "' OR '1'='1";  // attacker input
String query = "SELECT * FROM users WHERE username = '" + name + "'";
Statement stmt = con.createStatement();
ResultSet rs = stmt.executeQuery(query);
```

#### **Output:**

- Returns all users — attacker bypassed security!
- If the query was a `DELETE` instead, it could delete your entire database.

---

- `PreparedStatement` uses `?` placeholders for input and treats input as data, not SQL code.

### **Example:**

```java
String name = "' OR '1'='1";  // attacker input
String query = "SELECT * FROM users WHERE username = ?";
PreparedStatement pstmt = con.prepareStatement(query);
pstmt.setString(1, name);  // input is handled safely

ResultSet rs = pstmt.executeQuery();
```

#### **Output:**

- Returns only users whose actual name is `' OR '1'='1` — which is none
- The query remains safe.

---

- `Statement`: combines your input directly into the SQL string
- `PreparedStatement`: precompiles the SQL first, then passes input separately

---

### **Summary Table:**

| Feature             | `Statement`                | `PreparedStatement`          |
| ------------------- | -------------------------- | ---------------------------- |
| SQL Injection Safe? | No                         | Yes                          |
| Query Built With    | String Concatenation       | Precompiled Query with `?`   |
| Input Handling      | Mixed into SQL as raw text | Treated as separate data     |
| Code Readability    | Messy with long strings    | Clean & clear                |

---

### **`PreparedStatement` is Faster Than `Statement`because:**

- When we send an SQL query to the database, it does 3 things:

1. Parse the SQL (check for errors)
2. Compile it into a database execution plan
3. Execute the query

- This process takes time and CPU.

- When we use `Statement`, every time we send a query — even if it's the same structure — it goes through the full process again.

### **Example:**

```java
Statement stmt = con.createStatement();

stmt.executeUpdate("INSERT INTO student VALUES (1, 'Kaushal')");
stmt.executeUpdate("INSERT INTO student VALUES (2, 'Karan')");
stmt.executeUpdate("INSERT INTO student VALUES (3, 'Henil')");
```

- Even though the structure of the query is same, the DB parses + compiles it again and again because it sees them as separate queries.

---

- When we use `PreparedStatement`, the query is compiled only once and then reused with different values.

### **Example:**

```java
String query = "INSERT INTO student VALUES (?, ?)";
PreparedStatement pstmt = con.prepareStatement(query);

pstmt.setInt(1, 1);
pstmt.setString(2, "Kaushal");
pstmt.executeUpdate();

pstmt.setInt(1, 2);
pstmt.setString(2, "Karan");
pstmt.executeUpdate();

pstmt.setInt(1, 3);
pstmt.setString(2, "Henil");
pstmt.executeUpdate();
```

- If we insert 1000 records:
    - With `Statement`: 1000 times parsing + compiling = slow
    - With `PreparedStatement`: 1 time parsing + 1000 executions = fast

---

### **Note:**

- Java internally creates an object of a class that implements `PreparedStatement` — like `com.mysql.cj.jdbc.PreparedStatementImpl`.
- That class provides the actual code for methods like `setInt()`, `executeQuery()`, etc.

---