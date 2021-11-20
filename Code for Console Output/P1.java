import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Scanner;

public class P1 {
    private static Scanner input = new Scanner(System.in);
    private static int Custid = 0;      // To store which customer login, to avoid asking again and again

    /**
     * New Customer which will take input from user and call newCustomer function of BankingSystem class
     *
     * @return
     * @throws SQLException
     */
    private static int newCustomer() throws SQLException {
        System.out.print("Enter Name:");
        String name = input.next();
        System.out.print("Enter Age:");
        String age = input.next();
        System.out.print("Enter Gender:");
        String gender = input.next();
        System.out.print("Enter Pin:");
        String pin = input.next();
        BankingSystem.newCustomer(name, gender, age, pin);
        String statement = "SELECT id from p1.customer where name=\'" + name + "\' AND pin =\'" + pin + "\'";   // SQL Statement
        ResultSet rs = BankingSystem.executeQuery(statement);       // Calling BankingSystem function which will run SQL statement
        while (rs.next()) {                                         // The above function returned a result set, going throughout that result set in this loop
            return Integer.valueOf(rs.getString("id"));
        }
        rs.close();     // Closing result set
        return 0;       // if result set is empty, return 0
    }

    /**
     * CustomerLogin, will take input from user and will do operating accordingly
     *
     * @throws SQLException
     */
    private static void customerLogin() throws SQLException {
        System.out.print("Enter Customer ID:");
        Custid = input.nextInt();
        System.out.print("Enter Customer Pin:");
        int pin = input.nextInt();
        if (Custid == 0 && pin == 0) {
            AdminMainMenu();        // if Customer ID and Pin is 0, system will take you to admin view.
        }
        String statement = "SELECT * from p1.customer where id=\'" + Custid + "\' AND pin =\'" + pin + "\'";
        System.out.println(statement);  // Printing statement for log.
        ResultSet rs = BankingSystem.executeQuery(statement);
        while (rs.next()) {
            customerMainMenu();     // if result set is not empty, go to customerMainMenu
            rs.close();             // closing result set
            return;                 // return from function
        }
        System.out.println("Invalid Login, Try again later");       // if above return; line didn't run, this line will be executed
    }

    /**
     * Printing Main Menu and will ask user to select and option, and will do action accordingly
     *
     * @throws SQLException
     */
    private static void MainMenu() throws SQLException {
        System.out.println("Welcome to the Self Services Banking System!");
        System.out.println("1. New Customer\n" +
                "2. Customer Login\n" +
                "3. Exit\n");
        switch (input.nextInt()) {
            case 1:
                int id = newCustomer();
                if (id != 0) {
                    System.out.println("Successfully, Your ID is " + id);
                    Custid = id;
                } else {
                    System.out.println("An Error occurred");
                }
                break;
            case 2:
                customerLogin();    // Calling above customerLogin function
                break;
            case 3:
                return;
            default:
                System.out.println("Wrong Input, Try Again");
                break;
        }
        MainMenu();
    }

    /**
     * Open Account, and will take input and call Execute Query function of BankingSystem's class
     *
     * @return
     * @throws SQLException
     */
    private static int OpenAccount() throws SQLException {
        System.out.print("Enter type (C or S):");
        String type = input.next();
        type = type.toUpperCase();
        System.out.print("Enter initial deposit (amount >0 ):");
        String amount = input.next();
        BankingSystem.openAccount(String.valueOf(Custid), type, amount);
        String statement = "SELECT number from p1.account where id=\'" + Custid + "\'";
        System.out.println(statement);
        ResultSet rs = BankingSystem.executeQuery(statement);   // Calling function to execute query
        while (rs.next()) {
            return Integer.valueOf(rs.getString("number"));
        }
        rs.close();
        return 0;
    }

    /**
     * To take input for BankingSystems's CloseAccount function
     *
     * @return
     * @throws SQLException
     */
    private static int CloseAccount() throws SQLException {
        System.out.print("Enter your account number:");
        String accNum = input.next();
        int balance = BankingSystem.checkBalance(accNum);
        BankingSystem.withdraw(accNum, String.valueOf(balance));
        BankingSystem.closeAccount(accNum);
        return balance;
    }

    /**
     * To take input and validate amount and pass values to BankingSystems's Deposit function
     *
     * @throws SQLException
     */
    private static void Deposit() throws SQLException {
        System.out.print("Enter your account number:");
        String accNum = input.next();
        System.out.print("Enter amount you want to deposit:");
        int amount = input.nextInt();
        if (amount > 0) {
            System.out.println("Deposit request sent");
            BankingSystem.deposit(accNum, String.valueOf(amount));
        } else {
            System.out.println("Amount should be greater than zero");
        }
    }

    /**
     * To take input from user and passing it to BankingSystems's class
     *
     * @throws SQLException
     */
    private static void Withdraw() throws SQLException {
        System.out.print("Enter your account number:");
        String accNum = input.next();
        System.out.print("Enter amount you want to Withdraw:");
        int amount = input.nextInt();
        if (amount > 0) {
            System.out.println("Withdraw request sent");
            BankingSystem.withdraw(accNum, String.valueOf(amount));
        } else {
            System.out.println("Amount should be greater than zero");
        }
    }

    /**
     * User input for BankingSystems's function 'Transfer'
     *
     * @throws SQLException
     */
    private static void Transfer() throws SQLException {
        System.out.print("Enter amount you want to Transfer:");
        int amount = input.nextInt();
        if (amount > 0) {
            System.out.print("Enter your account number:");
            String accNum = input.next();
            System.out.print("Enter destination account number:");
            String DestaccNum = input.next();
            System.out.println("Transfer request sent");
            BankingSystem.transfer(accNum, DestaccNum, String.valueOf(amount));
        } else {
            System.out.println("Amount should be greater than zero");
        }
    }

    /**
     * Print menu and ask for user option, and doing action accordingly
     * @throws SQLException
     */
    private static void customerMainMenu() throws SQLException {
        System.out.println("Customer Main Menu");
        System.out.println("1. Open Account\n" +
                "2. Close Account\n" +
                "3. Deposit\n" +
                "4. Withdraw\n" +
                "5. Transfer\n" +
                "6. Account Summary\n" +
                "7. Exit\n");
        switch (input.nextInt()) {
            case 1:
                int number = OpenAccount();
                if (number != 0) {
                    System.out.println("Account opened successfully, your account number is " + number);
                } else {
                    System.out.println("An Error occurred");
                }
                break;
            case 2:
                int balance = CloseAccount();
                System.out.println("Account closed successfully, here is your balance $" + balance);
                break;
            case 3:
                Deposit();
                break;
            case 4:
                Withdraw();
                break;
            case 5:
                Transfer();
                break;
            case 6:
                BankingSystem.accountSummary(String.valueOf(Custid));
                break;
            case 7:
                return;
            default:
                System.out.println("Wrong Input, Try Again");
                break;
        }
        customerMainMenu();
    }

    /**
     * Menu of Admin, asking user input, and actions accordingly
     * @throws SQLException
     */
    private static void AdminMainMenu() throws SQLException {
        System.out.println("Administrator Main Menu");
        System.out.println("1. Account Summary for a Customer\n" +
                "2. Report A :: Customer Information with Total Balance in Decreasing Order\n" +
                "3. Report B :: Find the Average Total Balance Between Age Groups\n" +
                "4. Exit\n");
        switch (input.nextInt()) {
            case 1:
                System.out.print("Enter customer id:");
                Custid = input.nextInt();
                BankingSystem.accountSummary(String.valueOf(Custid));
                break;
            case 2:
                BankingSystem.reportA();
                break;
            case 3:
                System.out.print("Enter Min age:");
                int min = input.nextInt();
                System.out.print("Enter Max age:");
                int max = input.nextInt();
                BankingSystem.reportB(String.valueOf(min), String.valueOf(max));
                break;
            case 4:
                return;
            default:
                System.out.println("Wrong Input, Try Again");
                break;
        }
        AdminMainMenu();
    }

    /**
     * Driver function.
     * @param argv
     * @throws SQLException
     */
    public static void main(String argv[]) throws SQLException {
        argv = new String[1];
        argv[0] = "db.properties";
        System.out.println(":: PROGRAM START");

        if (argv.length < 1) {
            System.out.println("Need database properties filename");
        } else {
            BankingSystem.init(argv[0]);
            BankingSystem.testConnection();
            System.out.println();
            MainMenu();
        }
        System.out.println(":: PROGRAM END");

    }
}
