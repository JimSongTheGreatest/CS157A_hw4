import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CustomerGUI implements ActionListener {
    private static int Custid;
    public static String name;
    private volatile static CustomerGUI uniqueInstance = null;
    private static JFrame frame;
    private JButton Open_Button, Close_Button, Deposit_Button, Withdraw_Button, Transfer_Button, AccountSummary_Button, MainMenu_Button;
    private JLabel jlable;
    private JPanel pButtons;
    private JPanel ptext;

    /**
     * To return only one instance, no duplicate GUI will be created, method is called Singleton Pattern
     *
     * @return
     * @throws SQLException
     */
    public static CustomerGUI getInstance() throws SQLException {
        if (uniqueInstance == null) { //Single Checked
            synchronized (CustomerGUI.class) {
                if (uniqueInstance == null) { //Double Checked
                    uniqueInstance = new CustomerGUI();
                }
            }
        }
        show();
        return uniqueInstance;
    }

    /**
     * Initialize GUI function
     */
    public void initGUI() {
        frame = new JFrame("Self Services Banking System!");
        jlable = new JLabel("Customer", SwingConstants.CENTER);
        Open_Button = new JButton("Open Account");
        Open_Button.addActionListener(this);
        Close_Button = new JButton("Close Account");
        Close_Button.addActionListener(this);
        Deposit_Button = new JButton("Deposit");
        Deposit_Button.addActionListener(this);
        Withdraw_Button = new JButton("Withdraw");
        Withdraw_Button.addActionListener(this);
        Transfer_Button = new JButton("Transfer");
        Transfer_Button.addActionListener(this);
        AccountSummary_Button = new JButton("Account Summary");
        AccountSummary_Button.addActionListener(this);
        MainMenu_Button = new JButton("Back to Main Menu");
        MainMenu_Button.addActionListener(this);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        pButtons = new JPanel(new GridLayout(3, 2, 10, 10));
        ptext = new JPanel(new GridLayout(2, 2, 10, 10));
        pButtons.add(Open_Button);
        pButtons.add(Close_Button);
        pButtons.add(Deposit_Button);
        pButtons.add(Withdraw_Button);
        pButtons.add(Transfer_Button);
        pButtons.add(AccountSummary_Button);
        pButtons.add(MainMenu_Button);
        ptext.add(jlable);

        Container con = frame.getContentPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        con.add(ptext, BorderLayout.NORTH);
        con.add(pButtons, BorderLayout.CENTER);
        con.setVisible(true);
        frame.setSize(700, 200);
        frame.setLocationRelativeTo(null);
    }

    /**
     * Constructor
     */
    public CustomerGUI() {
        if (frame == null) {                //  Is it is not previously initialized, initializes it
            initGUI();
        }
    }

    /**
     * to show GUI, set visibility true, at the time we are setting GUI visible we are doing more things in it too.
     * @throws SQLException
     */
    public static void show() throws SQLException {
        if (frame != null) {
            int pin = -1;
            try {
                Custid = Integer.valueOf(JOptionPane.showInputDialog(frame,
                        "Enter Customer ID:", null));
                pin = Integer.valueOf(JOptionPane.showInputDialog(frame,
                        "Enter PIN:", null));
            } catch (NumberFormatException e) {
                hide();                                                 //  IF BAck to main menu button clicked, hide current windows
                JOptionPane.showMessageDialog(frame, "Try again", "Try again", JOptionPane.ERROR_MESSAGE);
                MainPage.show();                                        //  Show Main Menu Page
            }
            if (Custid == 0 && pin == 0) {
                JOptionPane.showMessageDialog(null, "Welcome Admin", "Welcome", JOptionPane.INFORMATION_MESSAGE);
                hide();
                Admin.getInstance();
            } else if (pin != -1) {
                String statement = "SELECT name from p1.customer where id=\'" + Custid + "\' AND pin =\'" + pin + "\'";
                ResultSet rs = BankingSystem.executeQuery(statement);
                while (rs.next()) {
                    frame.setVisible(true);
                    JOptionPane.showMessageDialog(null, "Welcome " + rs.getString("name"), "Welcome", JOptionPane.INFORMATION_MESSAGE);
                    rs.close();
                    return;
                }
                hide();
                JOptionPane.showMessageDialog(null, "Invalid Login, Try again", "Error", JOptionPane.ERROR_MESSAGE);
                MainPage.show();                                        //  Show Main Menu Page
            }
        }
    }

    /**
     * to hide GUI, set visibility false
     */
    public static void hide() {
        if (frame != null)
            frame.setVisible(false);
    }

    /**
     * Overriding function of ActionListener, this will be called when any action is performed, like button click
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String output = "";
        if (e.getSource() == Open_Button) {
            Object[] options = {"C", "S"};
            int n = JOptionPane.showOptionDialog(frame,
                    "Select Type of account ",
                    "Type",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            String type = options[n].toString();
            String amount = JOptionPane.showInputDialog(frame,
                    "Enter amount:", null);
            BankingSystem.openAccount(String.valueOf(Custid), type, amount);
            String statement = "SELECT number from p1.account where id=\'" + Custid + "\'";
            ResultSet rs = BankingSystem.executeQuery(statement);
            try {
                while (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Your Account number is " + rs.getString("number"));
                    rs.close();
                    break;
                }
            } catch (SQLException error) {
                JOptionPane.showMessageDialog(null, "Error", "Error creating account", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == Close_Button) {
            String accNum = JOptionPane.showInputDialog(frame,
                    "Enter Account Number:", null);
            BankingSystem.closeAccount(accNum);
        } else if (e.getSource() == Deposit_Button) {
            String accNum = JOptionPane.showInputDialog(frame,
                    "Enter Account Number:", null);
            String amount = JOptionPane.showInputDialog(frame,
                    "Enter amount:", null);
            try {
                BankingSystem.deposit(accNum, amount);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == Withdraw_Button) {
            String accNum = JOptionPane.showInputDialog(frame,
                    "Enter Account Number:", null);
            String amount = JOptionPane.showInputDialog(frame,
                    "Enter amount:", null);
            try {
                BankingSystem.withdraw(accNum, amount);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == Transfer_Button) {
            String accNum = JOptionPane.showInputDialog(frame,
                    "Enter your Account Number:", null);
            String desAccNum = JOptionPane.showInputDialog(frame,
                    "Enter destination Account Number:", null);
            String amount = JOptionPane.showInputDialog(frame,
                    "Enter amount:", null);
            try {
                BankingSystem.transfer(accNum, desAccNum, amount);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == AccountSummary_Button) {
            try {
                BankingSystem.accountSummary(String.valueOf(Custid));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == MainMenu_Button) {
            hide();                                                 //  IF BAck to main menu button clicked, hide current windows
            MainPage.show();                                        //  Show Main Menu Page
        }
    }
}
