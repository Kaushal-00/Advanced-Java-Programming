# **ResultSetMetaData Interface:**

- `ResultSetMetaData` is an interface that provides information about the columns in a `ResultSet`.
- This is helpful when:
    - We don’t know the exact structure of the table
    - We want to display table info dynamically (e.g., building a table UI)

---

## **Common Uses:**

| We can find out…                 | Example                    |
| -------------------------------- | -------------------------- |
| Number of columns                | 3                          |
| Name of each column              | `id`, `name`, `marks`      |
| Data type of each column         | `INT`, `VARCHAR`, `DOUBLE` |
| Column label (alias)             | `Student ID`, etc.         |
| Whether column is auto-increment | `true` / `false`           |

---

- We get it from a `ResultSet`:

```java
ResultSet rs = stmt.executeQuery("SELECT * FROM student");
ResultSetMetaData rsmd = rs.getMetaData();
```

- Here `rs` holds the result rows and also remembers the column info.
- So when we call `.getMetaData()`, it returns an object of `ResultSetMetaData`, which gives info about the columns.
- When we call `.getMetaData()`, Internally, it returns something like:

```java
return new com.mysql.cj.jdbc.result.ResultSetMetaData();
```

- This class implements all the methods defined in ResultSetMetaData Interrface.

---

## **Example:**

- Let’s say we have this table:

```sql
student(id INT, name VARCHAR(50), marks DOUBLE)
```

```java
Statement stmt = con.createStatement();
ResultSet rs = stmt.executeQuery("SELECT * FROM student");

ResultSetMetaData rsmd = rs.getMetaData();

int columnCount = rsmd.getColumnCount();

System.out.println("Total Columns: " + columnCount);

for (int i = 1; i <= columnCount; i++) {
    System.out.println("Column " + i + ":");
    System.out.println("  Name: " + rsmd.getColumnName(i));
    System.out.println("  Type: " + rsmd.getColumnTypeName(i));
}
```

---

## **Common Methods:**

| Method                         | Description                                  |
| ------------------------------ | -------------------------------------------- |
| `getColumnCount()`             | Returns number of columns in the ResultSet   |
| `getColumnName(int index)`     | Returns the column name at given index       |
| `getColumnTypeName(int index)` | Returns SQL data type name (e.g., `VARCHAR`) |
| `isAutoIncrement(int index)`   | Checks if column is auto-increment           |
| `getColumnLabel(int index)`    | Returns alias name (if used)                 |
| `isNullable(int index)`        | Returns if the column can be `NULL`          |

---

### 1. `getColumnCount()`

- Returns the total number of columns in the result set.

```java
int count = rsmd.getColumnCount();
System.out.println("Columns: " + count);
```

---

### 2. `getColumnName(int column)`

- Returns the name of the column (based on index).

```java
System.out.println(rsmd.getColumnName(1));  // e.g., "id"
```

---

### 3. `getColumnLabel(int column)`

- Returns the column alias (if used), otherwise same as `getColumnName()`.

```java
// Query: SELECT id AS student_id FROM student
System.out.println(rsmd.getColumnLabel(1));  // "student_id"
```

---

### 4. `getColumnType(int column)`

- Returns the SQL type as an `int` (like `Types.INTEGER`, `Types.VARCHAR`).

```java
int type = rsmd.getColumnType(1);  // Output: 4 (if INTEGER)
```

---

### 5. `getColumnTypeName(int column)`

- Returns the SQL type **name** as a string (like `INT`, `VARCHAR`).

```java
System.out.println(rsmd.getColumnTypeName(1));  // Output: "INT"
```

---

### 6. `isNullable(int column)`

- Returns whether the column can contain `NULL`.
    - `0` → `columnNoNulls`
    - `1` → `columnNullable`
    - `2` → `columnNullableUnknown`

```java
int nullable = rsmd.isNullable(2);
System.out.println("Nullable: " + nullable);
```

---

### 7. `isAutoIncrement(int column)`

- Returns `true` if the column is auto-incremented.

```java
System.out.println(rsmd.isAutoIncrement(1));  // true or false
```

---

### 8. `getPrecision(int column)`

- Returns total number of digits for numeric columns.

```java
System.out.println(rsmd.getPrecision(3));  // e.g., 5
```

---

### 9. `getScale(int column)`

- Returns the number of digits after the decimal point.

```java
System.out.println(rsmd.getScale(3));  // e.g., 2 (for 75.25)
```

---

### 10. `isReadOnly(int column)`

- Checks if the column is read-only (can’t be updated).

```java
System.out.println(rsmd.isReadOnly(1));
```

---

### 11. `isWritable(int column)` / `isDefinitelyWritable(int column)`

- Returns `true` if the column is writable (or definitely writable).

```java
System.out.println(rsmd.isWritable(2));            // Possibly writable
System.out.println(rsmd.isDefinitelyWritable(2));  // Must be writable
```

---