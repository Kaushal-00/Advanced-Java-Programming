# **CallableStatement Interface:**

- `CallableStatement` is a JDBC interface used to call stored procedures in a database.

### **Stored Procedure:**

- A stored procedure is a set of SQL statements saved in the database with a name.
- We can call it from Java using `CallableStatement`.

---

- Use `CallableStatement` when:
    - We want to reuse logic written in the database
    - We want better performance
    - We want to do complex operations like loops, conditions, multiple queries

---

## **Syntax:**

```java
CallableStatement cs = con.prepareCall("{call procedure_name(?, ?, ...)}");
```

---

## **Example:**

- Let’s say our MySQL DB has this stored procedure:

```sql
DELIMITER //
CREATE PROCEDURE getStudent(IN stu_id INT)
BEGIN
    SELECT * FROM student WHERE id = stu_id;
END //
DELIMITER ;
```

- Now, call it in Java:

```java
CallableStatement cs = con.prepareCall("{call getStudent(?)}");
cs.setInt(1, 1);  // pass ID = 1

ResultSet rs = cs.executeQuery();

while (rs.next()) {
    System.out.println(rs.getInt("id") + " " + rs.getString("name"));
}
```

---

## **Types of Parameters:**

| Type    | Description                    | Java Method                         |
| ------- | ------------------------------ | ----------------------------------- |
| `IN`    | Input parameter                | `setInt()`, etc.                    |
| `OUT`   | Output from DB to Java         | `registerOutParameter()`            |
| `INOUT` | Works as both input and output | `setX()` + `registerOutParameter()` |

---

## **Example:**

- Assume this procedure returns a count:

```sql
CREATE PROCEDURE getCount(OUT total INT)
BEGIN
    SELECT COUNT(*) INTO total FROM student;
END;
```

Java code:

```java
CallableStatement cs = con.prepareCall("{call getCount(?)}");
cs.registerOutParameter(1, java.sql.Types.INTEGER);
cs.execute();

int total = cs.getInt(1);
System.out.println("Total Students: " + total);
```

---

## **Summary Table:**

| Method                              | Description                            |
| ----------------------------------- | -------------------------------------- |
| `prepareCall(sql)`                  | Creates `CallableStatement`            |
| `setInt(index, value)`              | Sets input parameter                   |
| `registerOutParameter(index, type)` | Registers output parameter             |
| `executeQuery()`                    | Executes if procedure returns `SELECT` |
| `executeUpdate()`                   | Executes if it updates data            |
| `getInt(index)`                     | Gets value of OUT parameter            |

---