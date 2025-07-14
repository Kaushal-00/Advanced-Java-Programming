# **ResultSet:**

- `ResultSet` is a table-like object(interface) in Java that holds the data returned from a SQL `SELECT` query.
- We can use it to read each row and column of the result, one by one.

---

- We can get `ResultSet` from either `Statement`, `PreparedStatement`, or `CallableStatement`:

```java
ResultSet rs = pstmt.executeQuery();
```

- Package:

```java
java.sql.ResultSet
```

- Just like `Statement` and `PreparedStatement`, the actual implementation of `ResultSet` is done by the JDBC driver, such as:

```java
com.mysql.cj.jdbc.result.ResultSetImpl
```

---

## **Common Methods:**

### 1. `next()`

- Moves the pointer to the next row.
- Returns `true` if a row exists, `false` if end reached.

```java
while (rs.next()) {
    // process current row
}
```

---

### 2. `getInt("columnName")` or `getInt(columnIndex)`

- Reads an integer value from the current row.

```java
int id = rs.getInt("id");
```

---

### 3. `getString("columnName")`

- Reads a string value from the current row.

```java
String name = rs.getString("name");
```

---

### 4. `getDouble("columnName")`

- Reads a decimal/double value.

```java
double marks = rs.getDouble("marks");
```

---

### 5. `getBoolean("columnName")`

- Reads a `true`/`false` value.

```java
boolean status = rs.getBoolean("is_active");
```

---

### 6. `getDate("columnName")`

- Reads a `java.sql.Date` object.

```java
Date dob = rs.getDate("birth_date");
```

---

### 7. `wasNull()`

- Checks if the last read column was `NULL`.

```java
int score = rs.getInt("score");
if (rs.wasNull()) {
    System.out.println("Score was NULL");
}
```

---

### 8. `close()`

- Closes the `ResultSet` to free memory.

```java
rs.close();
```

---

## **Example:**

- Suppose this is our `student` table:

| id | name    | marks |
| -- | ------- | ----- |
| 1  | Kaushal | 88.5  |
| 2  | Karan   | 91.0  |

```java
String query = "SELECT * FROM student";
PreparedStatement pstmt = con.prepareStatement(query);

ResultSet rs = pstmt.executeQuery();

while (rs.next()) {
    int id = rs.getInt("id");
    String name = rs.getString("name");
    double marks = rs.getDouble("marks");

    System.out.println(id + " " + name + " " + marks);
}
```

---

- `ResultSet` starts before the first row
- Each time you call `next()`, it moves to the next row
- Returns `false` when there are no more rows

---

- When reading values from `ResultSet`, we can use:

1. Column Name (e.g., `"id"`, `"name"`)
2. Column Index (e.g., `1`, `2`, `3` — based on position in query)

---

### **Example:**

- Table: `student`

| Column Index | Column Name | Example Value |
| ------------ | ----------- | ------------- |
| 1            | `id`        | `101`         |
| 2            | `name`      | `"Kaushal"`   |
| 3            | `marks`     | `85.5`        |

---

```java
int id = rs.getInt("id");
String name = rs.getString("name");
double marks = rs.getDouble("marks");
```

- Clear and readable
- Slightly slower internally

---

```java
int id = rs.getInt(1);         // 1st column
String name = rs.getString(2); // 2nd column
double marks = rs.getDouble(3);// 3rd column
```

- Slightly faster
- Confusing if column order changes

---

## **ResultSet Types:**

- There are 3 main types of `ResultSet`:
    - Forward-Only ResultSet
    - Scrollable ResultSet
    - Updatable ResultSet

---

### **Forward-Only ResultSet (`TYPE_FORWARD_ONLY`):**

- A Forward-Only ResultSet allows us to move only in one direction — from top to bottom, one row at a time.
- We can use `next()` to move forward.
- We cannot go back, jump to a row, or scroll.

---

- This type is default, but we can write it explicitly too:

```java
Statement stmt = con.createStatement(
    ResultSet.TYPE_FORWARD_ONLY,
    ResultSet.CONCUR_READ_ONLY
);
```

---

- Very fast and memory-efficient
- Ideal when just reading data once from top to bottom
- Cannot go back or skip to a specific row
- Cannot update the data (unless made updatable separately)
- Cannot see real-time DB changes after query runs

---

### **Scrollable ResultSet:**

- A Scrollable ResultSet allows us to move both forward and backward through the rows, and even jump to a specific row.
- This is helpful when:
    - We want to revisit earlier rows    
    - We want to go to a specific row (e.g., 5th)    
    - We want to read rows in reverse order

---

#### **Methods:**

| Method          | What it does                              |
| --------------- | ----------------------------------------- |
| `next()`        | Move to next row                          |
| `previous()`    | Move to previous row                      |
| `first()`       | Move to first row                         |
| `last()`        | Move to last row                          |
| `absolute(n)`   | Jump to row number `n`                    |
| `relative(n)`   | Jump relative to current position         |
| `beforeFirst()` | Move before the first row (used to reset) |
| `afterLast()`   | Move after the last row                   |

---

- We must create a `Statement` or `PreparedStatement` with scrollable type:

```java
Statement stmt = con.createStatement(
    ResultSet.TYPE_SCROLL_INSENSITIVE,   // or TYPE_SCROLL_SENSITIVE
    ResultSet.CONCUR_READ_ONLY
);
```

---

#### **Scrollable Types:**

| Type                      | Description                                   |
| ------------------------- | --------------------------------------------- |
| `TYPE_SCROLL_INSENSITIVE` | Allows scroll, doesn’t reflect DB changes     |
| `TYPE_SCROLL_SENSITIVE`   | Allows scroll, does reflect DB changes        |

---

- Easy to navigate to any row
- Can go forward, backward, jump
- Useful for building UI apps (pagination, table view)
- Slightly slower than forward-only
- Takes more memory

---

### **Updatable ResultSet:**

- An Updatable ResultSet allows us to modify database rows directly from your Java code using methods like `updateInt()`, `updateString()`, and `updateRow()` — without writing extra SQL.

---

```java
Statement stmt = con.createStatement(
    ResultSet.TYPE_SCROLL_SENSITIVE,     // For moving around
    ResultSet.CONCUR_UPDATABLE           // For allowing updates
);
```

- We must use `TYPE_SCROLL_*` to move freely
- Use `CONCUR_UPDATABLE` to make it editable

---

### **Example:**

```java
ResultSet rs = stmt.executeQuery("SELECT * FROM student");

while (rs.next()) {
    int id = rs.getInt("id");

    if (id == 1) {
        rs.updateString("name", "Kaushal Updated");
        rs.updateDouble("marks", 95.5);
        rs.updateRow();  // Pushes changes to DB
    }
}
```

---

### **Example: Insert New Row using ResultSet:**

```java
rs.moveToInsertRow();  // Moves to empty row

rs.updateInt("id", 4);
rs.updateString("name", "New Student");
rs.updateDouble("marks", 82.0);

rs.insertRow();  // Inserts row into DB
```

---

### **Example: Delete Row using ResultSet:**

```java
rs.absolute(2);   // Move to 2nd row
rs.deleteRow();   // Deletes that row from DB
```

---

### **Common Methods:**

| Method                     | Description                        |
| -------------------------- | ---------------------------------- |
| `updateString("col", val)` | Set a new value to a string column |
| `updateInt("col", val)`    | Set new integer value              |
| `updateRow()`              | Saves changes to the current row   |
| `moveToInsertRow()`        | Moves to a special insert-only row |
| `insertRow()`              | Inserts a new row                  |
| `deleteRow()`              | Deletes the current row            |

---

### **Requirements:**

- The table must have a primary key.
- The SQL query should not use JOINs or complex views.
- Not all databases or drivers support this — MySQL does.

---