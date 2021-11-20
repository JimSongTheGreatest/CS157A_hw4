
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public final class MainPage implements ActionListener {

    private volatile static MainPage uniqueInstance = null;
    private static JFrame frame;
    private JButton Customer_Button, NewCustomer_Button, Exit_Button;
    private JLabel jlable;
    private JPanel pButtons;
    private JPanel ptext;

    /**
     * To return only one instance, no duplicate GUI will be created, method is called Singleton Pattern
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static MainPage getInstance() throws ClassNotFoundException, SQLException {
        if (uniqueInstance == null) { //Single Checked
            synchronized (MainPage.class) {
                if (uniqueInstance == null) { //Double Checked
                    uniqueInstance = new MainPage();
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
        jlable = new JLabel("Welcome to the Self Services Banking System!", SwingConstants.CENTER);
        NewCustomer_Button = new JButton("New Customer");
        NewCustomer_Button.addActionListener(this);
        Customer_Button = new JButton("Customer Login");
        Customer_Button.addActionListener(this);
        Exit_Button = new JButton("Exit");
        Exit_Button.addActionListener(this);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        pButtons = new JPanel(new GridLayout(3, 2, 10, 10));
        ptext = new JPanel(new GridLayout(2, 2, 10, 10));
        pButtons.add(NewCustomer_Button);
        pButtons.add(Customer_Button);
        pButtons.add(Exit_Button);
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
     * to show GUI, set visibility true
     */
    public static void show() {
        if (frame != null)
            frame.setVisible(true);
    }

    /**
     * to hide GUI, set visibility false
     */
    public static void hide() {
        if (frame != null)
            frame.setVisible(false);
    }

    /**
     * Windows closing function, this will be called when we will click on Exit button on main menu, it will exit system, not only hide GUIs
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void windowClosing() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        JOptionPane.showMessageDialog(null, "Good Bye");
        System.exit(0);
    }

    /**
     * Constructor
     */
    private MainPage(){
        initGUI();
    }

    /**
     * Overriding function of ActionListener, this will be called when any action is performed, like button click
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        hide();
        if (e.getSource() == Exit_Button) {                 // If user clicked on Exit button
            try {
                windowClosing();
            } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getSource() == NewCustomer_Button) {
            String name = JOptionPane.showInputDialog(frame,
                    "Enter name:", null);
            String age = JOptionPane.showInputDialog(frame,
                    "Enter age:", null);
            // For option of Male and Female only
            Object[] options = {"M", "F"};
            int n = JOptionPane.showOptionDialog(frame,
                    "Select your gender",
                    "Gender",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
            String gender = options[n].toString();
            String pin = JOptionPane.showInputDialog(frame,
                    "Enter pin:", null);
            try {
                BankingSystem.newCustomer(name, gender, age, pin);
                String statement = "SELECT id from p1.customer where name=\'" + name + "\' AND pin =\'" + pin + "\'";
                ResultSet rs = BankingSystem.executeQuery(statement);
                while (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Your Id is " + rs.getString("id"), "Account Created", JOptionPane.INFORMATION_MESSAGE);
                    MainPage.show();
                    break;
                }
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } else if (e.getSource() == Customer_Button) {
            hide();                                         // If user click on user button, hide this windows
            try {
                CustomerGUI.getInstance();                          // Open USER GUI
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
