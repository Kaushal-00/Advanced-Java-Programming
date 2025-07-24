# **Transaction:**

- A transaction is a group of SQL operations that are executed together as a single unit.
- They follow the ACID properties:

| Property        | Meaning                                              |
| --------------- | ---------------------------------------------------- |
| A - Atomicity   | All or nothing (either all steps happen, or none do) |
| C - Consistency | DB remains valid before and after the transaction    |
| I - Isolation   | Each transaction runs independently                  |
| D - Durability  | Once committed, changes are permanent                |

---

- Suppose we are transferring ₹500 from Account A to B:

```sql
1. Debit ₹500 from A
2. Credit ₹500 to B
```

- If step 1 succeeds but step 2 fails — money is lost!
- So you wrap both in a transaction — if any step fails, rollback both.

---

## **How JDBC Handles Transactions:**

- By default, JDBC runs in Auto-commit mode, meaning:
    - Every SQL statement is immediately committed (saved permanently).
- To manually manage transactions:

---

### **Steps to Use Transactions in JDBC:**

```java
Connection con = DriverManager.getConnection(...);

// Step 1: Turn OFF auto-commit
con.setAutoCommit(false);

try {
    // Step 2: Perform multiple queries
    Statement stmt = con.createStatement();
    stmt.executeUpdate("UPDATE account SET balance = balance - 500 WHERE id = 1");
    stmt.executeUpdate("UPDATE account SET balance = balance + 500 WHERE id = 2");

    // Step 3: If all succeed, COMMIT
    con.commit();
    System.out.println("Transaction Successful");

} catch (Exception e) {
    // Step 4: If any error, ROLLBACK
    con.rollback();
    System.out.println("Transaction Failed! Changes Rolled Back");
}
```

---

## **Common Transaction Methods:**

| Method                        | Description                                           |
| ----------------------------- | ----------------------------------------------------- |
| `setAutoCommit(boolean)`      | Enable/disable auto-commit mode.                      |
| `commit()`                    | Save all changes made during the current transaction. |
| `rollback()`                  | Undo all changes since the last commit.               |
| `getAutoCommit()`             | Check if auto-commit is currently on or off.          |
| `setSavepoint()` *(optional)* | Create a savepoint (partial rollback).                |
| `rollback(Savepoint)`         | Rollback to a specific savepoint.                     |

- These methods come from the `java.sql.Connection` interface.
- So whenever we write:

```java
Connection con = DriverManager.getConnection(...);
```

- We can directly use the transaction methods on `con` because `Connection` provides them.
- Auto-commit = `true` by default in JDBC.

---

### **Savepoint:**

- A Savepoint is like a bookmark in a transaction.
- It allows us to partially rollback to a specific point in our transaction without undoing the entire transaction.
- Imagine we're transferring ₹1000 from one account to another in three steps:
    1. Withdraw from account A ✅
    2. Deposit into account B ✅
    3. Update transaction log ❌ (fails)
- Now, instead of rolling back everything (including step 1 and 2), you can rollback only to a specific step (like before step 3) using a savepoint.

---

### **Savepoint Methods:**

- These methods are available in the `Connection` interface.

| Method                           | Description                           |
| -------------------------------- | ------------------------------------- |
| `setSavepoint()`                 | Creates an unnamed savepoint          |
| `setSavepoint(String name)`      | Creates a named savepoint             |
| `rollback(Savepoint sp)`         | Rolls back to the specified savepoint |
| `releaseSavepoint(Savepoint sp)` | Removes the savepoint                 |

---

### **Example:**

```java
Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "user", "pass");
con.setAutoCommit(false);  // manual transaction

Statement stmt = con.createStatement();
stmt.executeUpdate("UPDATE account SET balance = balance - 1000 WHERE id = 1");

Savepoint sp1 = con.setSavepoint("AfterDebit");  // create a savepoint

stmt.executeUpdate("UPDATE account SET balance = balance + 1000 WHERE id = 2");

// Suppose this fails:
try {
    stmt.executeUpdate("INSERT INTO log VALUES (null, 'transfer', NOW())");
    con.commit();
} catch (SQLException e) {
    con.rollback(sp1);  // rollback only to the savepoint
    con.commit();       // commit only step 1
}
```

- If we Used just `con.rollback();`
    - Then the entire transaction rolls back — Step 1, 2, and 3 are all undone
    - Nothing will be saved in the database.
- `rollback()` Undo the entire transaction while, `rollback(Savepoint sp)` Undo part of the transaction (till savepoint).

---