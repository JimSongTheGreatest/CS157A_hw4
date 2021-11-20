import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Manage connection to database and perform SQL statements.
 */
public class BankingSystem {
    // Connection properties
    private static String driver;
    private static String url;
    private static String username;
    private static String password;
    private static Connection con;

    /**
     * Initialize database connection given properties file.
     *
     * @param filename name of properties file
     */
    public static void init(String filename) {
        try {
            Properties props = new Properties();                      // Create a new Properties object
            FileInputStream input = new FileInputStream(filename);    // Create a new FileInputStream object using our filename parameter
            props.load(input);                                        // Load the file contents into the Properties object
            driver = props.getProperty("jdbc.driver");                // Load the driver
            url = props.getProperty("jdbc.url");                      // Load the url
            username = props.getProperty("jdbc.username");            // Load the username
            password = props.getProperty("jdbc.password");            // Load the password
            Class.forName(driver);                                    //load the driver
            con = DriverManager.getConnection(url, username, password);                  //Create the connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ExecuteQuery and return ResultSet.
     * Helping Function
     */
    public static ResultSet executeQuery(String query) {
        try {
            Statement stmt = con.createStatement();                                                 //Create a statement
            ResultSet rs = stmt.executeQuery(query);                                                //Executing the query and storing the results in a Result Set
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ExecuteQuery and return ResultSet.
     * Helping Function
     */
    public static int executeUpdate(String query) {
        try {
            Statement stmt = con.createStatement();                                                 //Create a statement
            int rs = stmt.executeUpdate(query);                                                //Executing the query and storing the results in a Result Set
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Print List of Data of specific Table.
     * Helping Function
     */
    public static void printData(String table, String id) throws SQLException {
        ResultSet rs = executeQuery("SELECT * FROM " + table + " where id=\'" + id + "\'");
        printColumns(table);
        while (rs.next()) {
            //Retrieving the ResultSetMetadata object
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int count = rsMetaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                System.out.print(rs.getString(rsMetaData.getColumnName(i)));
                int length = rs.getString(rsMetaData.getColumnName(i)).length();
                for (int j = length; j < 10; j++)
                    System.out.print(" ");
            }
            System.out.println();
        }
        rs.close();
    }

    /**
     * Print List of Data of specific Table.
     * Helping Function
     */
    public static void printData(String table) throws SQLException {
        ResultSet rs = executeQuery("SELECT * FROM " + table);
        printColumns(table);
        while (rs.next()) {
            //Retrieving the ResultSetMetadata object
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int count = rsMetaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                System.out.print(rs.getString(rsMetaData.getColumnName(i)));
                int length = rs.getString(rsMetaData.getColumnName(i)).length();
                for (int j = length; j < 10; j++)
                    System.out.print(" ");
            }
            System.out.println();
        }
        rs.close();
    }

    /**
     * Print List of Columns of specific Table.
     * Helping Function
     */
    public static void printColumns(String table) throws SQLException {
        ResultSet rs = executeQuery("SELECT * FROM " + table);
        while (rs.next()) {
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int count = rsMetaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                System.out.print(rsMetaData.getColumnName(i));
                int length = rsMetaData.getColumnName(i).length();
                for (int j = length; j < 10; j++)
                    System.out.print(" ");
            }
            System.out.println();
            for (int i = 1; i <= count; i++) {
                int length = rsMetaData.getColumnName(i).length();
                for (int j = 0; j < length; j++)
                    System.out.print("-");
                for (int j = length; j < 10; j++)
                    System.out.print(" ");
            }
            System.out.println();
            break;
        }
        rs.close();
    }

    /**
     * Test database connection.
     */
    public static void testConnection() {
        System.out.println(":: TEST - CONNECTING TO DATABASE");
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, username, password);
            con.close();
            System.out.println(":: TEST - SUCCESSFULLY CONNECTED TO DATABASE");
        } catch (Exception e) {
            System.out.println(":: TEST - FAILED CONNECTED TO DATABASE");
            e.printStackTrace();
        }
    }

    /**
     * Create a new customer.
     *
     * @param name   customer name
     * @param gender customer gender
     * @param age    customer age
     * @param pin    customer pin
     */
    public static void newCustomer(String name, String gender, String age, String pin) throws SQLException {
        System.out.println(":: CREATE NEW CUSTOMER - RUNNING");
        String statement = "INSERT INTO p1.customer(name, gender, age, pin) " +
                "VALUES(\'" + name + "\',\'" + gender + "\',\'" + age + "\',\'" + pin + "\');";
        try {
            if (Integer.valueOf(pin) <= 0) {
                System.out.println(":: CREATE NEW CUSTOMER - ERROR - INVALID PIN");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println(":: CREATE NEW CUSTOMER - ERROR - INVALID PIN");
            return;
        }
        executeUpdate(statement);
        System.out.println(":: CREATE NEW CUSTOMER - SUCCESS");
    }

    /**
     * Open a new account.
     *
     * @param id     customer id
     * @param type   type of account
     * @param amount initial deposit amount
     */
    public static void openAccount(String id, String type, String amount) {
        System.out.println(":: OPEN ACCOUNT - RUNNING");
        String statement = "INSERT INTO p1.account(id, balance, type, status) " +
                "VALUES(\'" + id + "\',\'" + amount + "\',\'" + type + "\',\'A\');";
        executeUpdate(statement);
        System.out.println(":: OPEN ACCOUNT - SUCCESS");
    }

    /**
     * Close an account.
     *
     * @param accNum account number
     */
    public static void closeAccount(String accNum) {
        System.out.println(":: CLOSE ACCOUNT - RUNNING");
        String statement = "UPDATE p1.account SET status=\'I\' where number = \'" + accNum + "\';";
        executeUpdate(statement);
        System.out.println(":: CLOSE ACCOUNT - SUCCESS");
    }

    /**
     * Check balance of amount
     */
    public static int checkBalance(String accNum) throws SQLException {
        String statement = "select balance from p1.account where number = \'" + accNum + "\';";
        ResultSet rs = executeQuery(statement);
        while (rs.next()) {
            int balance = Integer.valueOf(rs.getString("balance"));
            return balance;
        }
        return 0;
    }

    /**
     * Deposit into an account.
     *
     * @param accNum account number
     * @param amount deposit amount
     */
    public static void deposit(String accNum, String amount) throws SQLException {
        System.out.println(":: DEPOSIT - RUNNING");
        int balance = checkBalance(accNum);
        try {
            if (Integer.valueOf(amount) <= 0) {
                System.out.println(":: DEPOSIT - ERROR - INVALID AMOUNT");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println(":: DEPOSIT - ERROR - INVALID AMOUNT");
            return;
        }
        String statement = "UPDATE p1.account SET balance=\'" + (balance + Integer.valueOf(amount)) + "\' where number = \'" + accNum + "\';";
        executeUpdate(statement);
        System.out.println(":: DEPOSIT - SUCCESS");
    }

    /**
     * Withdraw from an account.
     *
     * @param accNum account number
     * @param amount withdraw amount
     */
    public static boolean withdraw(String accNum, String amount) throws SQLException {
        System.out.println(":: WITHDRAW - RUNNING");
        int balance = checkBalance(accNum);
        try {
            if (balance - Integer.valueOf(amount) < 0) {
                System.out.println(":: WITHDRAW - ERROR - NOT ENOUGH FUNDS");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println(":: WITHDRAW - ERROR - INVALID AMOUNT");
            return false;
        }
        String statement = "UPDATE p1.account SET balance=\'" + (balance - Integer.valueOf(amount)) + "\' where number = \'" + accNum + "\';";
        executeUpdate(statement);
        System.out.println(":: WITHDRAW - SUCCESS");
        return true;
    }

    /**
     * Transfer amount from source account to destination account.
     *
     * @param srcAccNum  source account number
     * @param destAccNum destination account number
     * @param amount     transfer amount
     */
    public static void transfer(String srcAccNum, String destAccNum, String amount) throws SQLException {
        System.out.println(":: TRANSFER - RUNNING");
        int balance = checkBalance(srcAccNum);
        try {
            if (balance - Integer.valueOf(amount) < 0) {
                System.out.println(":: TRANSFER - ERROR - NOT ENOUGH FUNDS");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println(":: TRANSFER - ERROR - INVALID AMOUNT");
            return;
        }
        String statement = "UPDATE p1.account SET balance=\'" + (balance - Integer.valueOf(amount)) + "\' where number = \'" + srcAccNum + "\';";
        executeUpdate(statement);
        balance = checkBalance(destAccNum);
        statement = "UPDATE p1.account SET balance=\'" + (balance + Integer.valueOf(amount)) + "\' where number = \'" + destAccNum + "\';";
        executeUpdate(statement);
        System.out.println(":: TRANSFER - SUCCESS");
    }

    /**
     * Display account summary.
     *
     * @param cusID customer ID
     */
    public static void accountSummary(String cusID) throws SQLException {
        System.out.println(":: ACCOUNT SUMMARY - RUNNING");
        printData("p1.account", cusID);
        System.out.println(":: ACCOUNT SUMMARY - SUCCESS");
    }

    /**
     * Display Report A - Customer Information with Total Balance in Decreasing Order.
     */
    public static void reportA() throws SQLException {
        System.out.println(":: REPORT A - RUNNING");
        ResultSet rs = executeQuery("SELECT C.name, C.gender, C.age , A.balance, A.status FROM p1.customer C JOIN p1.account A ON (A.id = C.id) order by A.balance DESC");
        System.out.println("Name      Gender    Age       Balance   Status");
        System.out.println("----      ------    ---       -------   ------");
        while (rs.next()) {
            //Retrieving the ResultSetMetadata object
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int count = rsMetaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                System.out.print(rs.getString(rsMetaData.getColumnName(i)));
                int length = rs.getString(rsMetaData.getColumnName(i)).length();
                for (int j = length; j < 10; j++)
                    System.out.print(" ");
            }
            System.out.println();
        }
        rs.close();
        System.out.println(":: REPORT A - SUCCESS");
    }

    /**
     * Display Report B - Customer Information with Total Balance in Decreasing Order.
     *
     * @param min minimum age
     * @param max maximum age
     */
    public static void reportB(String min, String max) throws SQLException {
        System.out.println(":: REPORT B - RUNNING");
        ResultSet rs = executeQuery("SELECT C.name, C.gender, C.age , A.balance, A.status FROM p1.customer C JOIN p1.account A ON (A.id = C.id) where C.age between " + min + " and " + max + " order by A.balance DESC");
        printColumns("p1.customer");
        while (rs.next()) {
            //Retrieving the ResultSetMetadata object
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int count = rsMetaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                System.out.print(rs.getString(rsMetaData.getColumnName(i)));
                int length = rs.getString(rsMetaData.getColumnName(i)).length();
                for (int j = length; j < 10; j++)
                    System.out.print(" ");
            }
            System.out.println();
        }
        rs.close();
        System.out.println(":: REPORT B - SUCCESS");
    }
}
