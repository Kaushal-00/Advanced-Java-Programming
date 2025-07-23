# **JDBC Types:**

- JDBC Types are SQL data types defined in Java using constants from the `java.sql.Types` class.
- They help JDBC drivers understand what type of data is being passed between Java and SQL.

---

## **Mapping Between SQL Types and Java Types:**

| **SQL Type**    | **JDBC Type (`java.sql.Types`)** | **Java Type**         | **Example Value**       | **Get Method**         | **Set Method**                |
| --------------- | -------------------------------- | --------------------- | ----------------------- | ---------------------- | ----------------------------- |
| `INT`           | `INTEGER`                        | `int` / `Integer`     | `101`                   | `getInt("col")`        | `setInt(index, value)`        |
| `BIGINT`        | `BIGINT`                         | `long` / `Long`       | `123456789L`            | `getLong("col")`       | `setLong(index, value)`       |
| `SMALLINT`      | `SMALLINT`                       | `short` / `Short`     | `12`                    | `getShort("col")`      | `setShort(index, value)`      |
| `TINYINT`       | `TINYINT`                        | `byte` / `Byte`       | `1`                     | `getByte("col")`       | `setByte(index, value)`       |
| `FLOAT`         | `FLOAT`                          | `float` / `Float`     | `55.5f`                 | `getFloat("col")`      | `setFloat(index, value)`      |
| `DOUBLE`        | `DOUBLE`                         | `double` / `Double`   | `99.99`                 | `getDouble("col")`     | `setDouble(index, value)`     |
| `DECIMAL`       | `DECIMAL`                        | `BigDecimal`          | `1050.75`               | `getBigDecimal("col")` | `setBigDecimal(index, value)` |
| `NUMERIC`       | `NUMERIC`                        | `BigDecimal`          | `9999.99`               | `getBigDecimal("col")` | `setBigDecimal(index, value)` |
| `CHAR`          | `CHAR`                           | `String`              | `"A"`                   | `getString("col")`     | `setString(index, value)`     |
| `VARCHAR`       | `VARCHAR`                        | `String`              | `"Hello"`               | `getString("col")`     | `setString(index, value)`     |
| `LONGVARCHAR`   | `LONGVARCHAR`                    | `String`              | Very long text          | `getString("col")`     | `setString(index, value)`     |
| `BOOLEAN`       | `BOOLEAN`                        | `boolean` / `Boolean` | `true`                  | `getBoolean("col")`    | `setBoolean(index, value)`    |
| `DATE`          | `DATE`                           | `java.sql.Date`       | `2025-07-08`            | `getDate("col")`       | `setDate(index, value)`       |
| `TIME`          | `TIME`                           | `java.sql.Time`       | `14:30:00`              | `getTime("col")`       | `setTime(index, value)`       |
| `TIMESTAMP`     | `TIMESTAMP`                      | `java.sql.Timestamp`  | `2025-07-08 14:30:00.0` | `getTimestamp("col")`  | `setTimestamp(index, value)`  |
| `BLOB`          | `BLOB`                           | `Blob` / `byte[]`     | Image/File              | `getBlob("col")`       | `setBlob(index, value)`       |
| `CLOB`          | `CLOB`                           | `Clob` / `String`     | Very large text         | `getClob("col")`       | `setClob(index, value)`       |
| `BIT`           | `BIT`                            | `boolean` / `Boolean` | `true` / `false`        | `getBoolean("col")`    | `setBoolean(index, value)`    |
| `NULL`          | `NULL`                           | `null`                | `null`                  | `getObject("col")`     | `setNull(index, Types.TYPE)`  |
| `REAL`          | `REAL`                           | `float`               | `45.67f`                | `getFloat("col")`      | `setFloat(index, value)`      |
| `VARBINARY`     | `VARBINARY`                      | `byte[]`              | Binary content          | `getBytes("col")`      | `setBytes(index, value)`      |
| `LONGVARBINARY` | `LONGVARBINARY`                  | `byte[]`              | Large binary content    | `getBytes("col")`      | `setBytes(index, value)`      |

---

## **Example:**

```sql
CREATE TABLE student (
    id INT,
    name VARCHAR(50),
    marks DECIMAL(5,2),
    is_passed BOOLEAN,
    dob DATE
);
```

```java
import java.sql.*;

public class JDBCTypeMapping {
    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "password");

            String query = "INSERT INTO student (id, name, marks, is_passed, dob) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, 101);                            // INTEGER
            ps.setString(2, "Kaushal");                   // VARCHAR
            ps.setBigDecimal(3, new java.math.BigDecimal("88.75")); // DECIMAL
            ps.setBoolean(4, true);                       // BOOLEAN
            ps.setDate(5, java.sql.Date.valueOf("2004-10-22")); // DATE

            ps.executeUpdate();
            System.out.println("Data inserted!");

            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

---

## **Notes:**

- `java.sql.Types` defines constants like `Types.INTEGER`, `Types.VARCHAR`, etc.
- Drivers use these types to match SQL data types with Java data types.
- `PreparedStatement` uses Java types directly, and internally JDBC maps them to SQL types.

---