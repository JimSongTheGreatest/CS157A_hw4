import javax.swing.*;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

/**
 * I just removed System.out.print and added JoptionPane in this GUI version of code
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
        String output = "";
        ResultSet rs = executeQuery("SELECT * FROM " + table + " where id=\'" + id + "\'");
        output += printColumns(table);
        while (rs.next()) {
            //Retrieving the ResultSetMetadata object
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int count = rsMetaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                output += (rs.getString(rsMetaData.getColumnName(i)));
                int length = rs.getString(rsMetaData.getColumnName(i)).length();
                for (int j = length; j < 10; j++)
                    output += " ";
            }
            output += "\n";
        }
        JTextArea textArea = new JTextArea(6, 25);
        textArea.setText(output);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane);
        rs.close();
    }

    /**
     * Print List of Data of specific Table.
     * Helping Function
     */
    public static void printData(String table) throws SQLException {
        String output = "";
        ResultSet rs = executeQuery("SELECT * FROM " + table);
        output += printColumns(table);
        while (rs.next()) {
            //Retrieving the ResultSetMetadata object
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int count = rsMetaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                output += (rs.getString(rsMetaData.getColumnName(i)));
                int length = rs.getString(rsMetaData.getColumnName(i)).length();
                for (int j = length; j < 10; j++)
                    output += " ";
            }
            output += "\n";
        }
        JTextArea textArea = new JTextArea(6, 25);
        textArea.setText(output);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane);
        rs.close();
    }

    /**
     * Print List of Columns of specific Table.
     * Helping Function
     */
    public static String printColumns(String table) throws SQLException {
        String output = "";
        ResultSet rs = executeQuery("SELECT * FROM " + table);
        while (rs.next()) {
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int count = rsMetaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                output += (rsMetaData.getColumnName(i));
                int length = rsMetaData.getColumnName(i).length();
                for (int j = length; j < 10; j++)
                    output += " ";
            }
            output += "\n";
            for (int i = 1; i <= count; i++) {
                int length = rsMetaData.getColumnName(i).length();
                for (int j = 0; j < length; j++)
                    output += ("-");
                for (int j = length; j < 10; j++)
                    output += (" ");
            }
            output += "\n";
            break;
        }
        rs.close();
        return output;
    }

    /**
     * Test database connection.
     */
    public static void testConnection() {
        String output = "";
        output += (":: TEST - CONNECTING TO DATABASE\n");
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url, username, password);
            con.close();
            output += (":: TEST - SUCCESSFULLY CONNECTED TO DATABASE\n");
        } catch (Exception e) {
            output += (":: TEST - FAILED CONNECTED TO DATABASE\n");
            e.printStackTrace();
        }
        JTextArea textArea = new JTextArea(6, 25);
        textArea.setText(output);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane);
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
        String output = "";
        output += (":: CREATE NEW CUSTOMER - RUNNING\n");
        String statement = "INSERT INTO p1.customer(name, gender, age, pin) " +
                "VALUES(\'" + name + "\',\'" + gender + "\',\'" + age + "\',\'" + pin + "\');";
        try {
            if (Integer.valueOf(pin) <= 0) {
                output += (":: CREATE NEW CUSTOMER - ERROR - INVALID PIN\n");
                return;
            }
        } catch (NumberFormatException e) {
            output += (":: CREATE NEW CUSTOMER - ERROR - INVALID PIN\n");
            return;
        }
        executeUpdate(statement);
        output += (":: CREATE NEW CUSTOMER - SUCCESS\n");
        JTextArea textArea = new JTextArea(6, 25);
        textArea.setText(output);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane);
    }

    /**
     * Open a new account.
     *
     * @param id     customer id
     * @param type   type of account
     * @param amount initial deposit amount
     */
    public static void openAccount(String id, String type, String amount) {
        String output = "";
        output += (":: OPEN ACCOUNT - RUNNING\n");
        String statement = "INSERT INTO p1.account(id, balance, type, status) " +
                "VALUES(\'" + id + "\',\'" + amount + "\',\'" + type + "\',\'A\');";
        executeUpdate(statement);
        output += (":: OPEN ACCOUNT - SUCCESS\n");
        JTextArea textArea = new JTextArea(6, 25);
        textArea.setText(output);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane);
    }

    /**
     * Close an account.
     *
     * @param accNum account number
     */
    public static void closeAccount(String accNum) {
        String output = "";
        output += (":: CLOSE ACCOUNT - RUNNING\n");
        String statement = "UPDATE p1.account SET status=\'I\' where number = \'" + accNum + "\';";
        executeUpdate(statement);
        output += (":: CLOSE ACCOUNT - SUCCESS\n");
        JTextArea textArea = new JTextArea(6, 25);
        textArea.setText(output);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane);
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
        String output = "";
        output += (":: DEPOSIT - RUNNING\n");
        int balance = checkBalance(accNum);
        try {
            if (Integer.valueOf(amount) <= 0) {
                output += (":: DEPOSIT - ERROR - INVALID AMOUNT\n");
            }
        } catch (NumberFormatException e) {
            output += (":: DEPOSIT - ERROR - INVALID AMOUNT\n");
        }
        String statement = "UPDATE p1.account SET balance=\'" + (balance + Integer.valueOf(amount)) + "\' where number = \'" + accNum + "\';";
        executeUpdate(statement);
        output += (":: DEPOSIT - SUCCESS\n");
        JTextArea textArea = new JTextArea(6, 25);
        textArea.setText(output);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane);
    }

    /**
     * Withdraw from an account.
     *
     * @param accNum account number
     * @param amount withdraw amount
     */
    public static boolean withdraw(String accNum, String amount) throws SQLException {
        String output = "";
        output += (":: WITHDRAW - RUNNING\n");
        int balance = checkBalance(accNum);
        try {
            if (balance - Integer.valueOf(amount) < 0) {
                output += (":: WITHDRAW - ERROR - NOT ENOUGH FUNDS\n");
                return false;
            }
        } catch (NumberFormatException e) {
            output += (":: WITHDRAW - ERROR - INVALID AMOUNT\n");
            return false;
        }
        String statement = "UPDATE p1.account SET balance=\'" + (balance - Integer.valueOf(amount)) + "\' where number = \'" + accNum + "\';";
        executeUpdate(statement);
        output += (":: WITHDRAW - SUCCESS\n");
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
        String output = "";
        output += (":: TRANSFER - RUNNING\n");
        int balance = checkBalance(srcAccNum);
        try {
            if (balance - Integer.valueOf(amount) < 0) {
                output += (":: TRANSFER - ERROR - NOT ENOUGH FUNDS\n");
                return;
            }
        } catch (NumberFormatException e) {
            output += (":: TRANSFER - ERROR - INVALID AMOUNT\n");
            return;
        }
        String statement = "UPDATE p1.account SET balance=\'" + (balance - Integer.valueOf(amount)) + "\' where number = \'" + srcAccNum + "\';";
        executeUpdate(statement);
        balance = checkBalance(destAccNum);
        statement = "UPDATE p1.account SET balance=\'" + (balance + Integer.valueOf(amount)) + "\' where number = \'" + destAccNum + "\';";
        executeUpdate(statement);
        output += (":: TRANSFER - SUCCESS\n");
    }

    /**
     * Display account summary.
     *
     * @param cusID customer ID
     */
    public static void accountSummary(String cusID) throws SQLException {
        String output = "";
        output += (":: ACCOUNT SUMMARY - RUNNING\n");
        printData("p1.account", cusID);
        output += (":: ACCOUNT SUMMARY - SUCCESS\n");
        JTextArea textArea = new JTextArea(6, 25);
        textArea.setText(output);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane);
    }

    /**
     * Display Report A - Customer Information with Total Balance in Decreasing Order.
     */
    public static void reportA() throws SQLException {
        String output = "";
        output += (":: REPORT A - RUNNING\n");
        ResultSet rs = executeQuery("SELECT C.name, C.gender, C.age , A.balance, A.status FROM p1.customer C JOIN p1.account A ON (A.id = C.id) order by A.balance DESC");
        output += ("Name      Gender    Age       Balance   Status\n");
        output += ("----      ------    ---       -------   ------\n");
        while (rs.next()) {
            //Retrieving the ResultSetMetadata object
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int count = rsMetaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                output += (rs.getString(rsMetaData.getColumnName(i)));
                int length = rs.getString(rsMetaData.getColumnName(i)).length();
                for (int j = length; j < 10; j++)
                    output += (" ");
            }
            output += ("\n");
        }
        rs.close();
        output += (":: REPORT A - SUCCESS\n");
        JTextArea textArea = new JTextArea(6, 25);
        textArea.setText(output);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane);
    }

    /**
     * Display Report B - Customer Information with Total Balance in Decreasing Order.
     *
     * @param min minimum age
     * @param max maximum age
     */
    public static void reportB(String min, String max) throws SQLException {
        String output = "";
        output += (":: REPORT B - RUNNING\n");
        ResultSet rs = executeQuery("SELECT C.name, C.gender, C.age , A.balance, A.status FROM p1.customer C JOIN p1.account A ON (A.id = C.id) where C.age between " + min + " and " + max + " order by A.balance DESC");
        output += printColumns("p1.customer");
        while (rs.next()) {
            //Retrieving the ResultSetMetadata object
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int count = rsMetaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                output += (rs.getString(rsMetaData.getColumnName(i)));
                int length = rs.getString(rsMetaData.getColumnName(i)).length();
                for (int j = length; j < 10; j++)
                    output += (" ");
            }
            output += ("\n");
        }
        rs.close();
        output += (":: REPORT B - SUCCESS\n");
        JTextArea textArea = new JTextArea(6, 25);
        textArea.setText(output);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane);
    }
}
