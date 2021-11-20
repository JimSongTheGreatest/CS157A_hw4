import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Scanner;


public class Admin implements ActionListener {
    private volatile static Admin uniqueInstance = null;
    private static JFrame frame;
    private JButton Summary_Button, ReportA_Button, ReportB_Button, MainMenu_Button;
    private JLabel jlable;
    private JPanel pButtons;
    private JPanel ptext;

    /**
     * To return only one instance, no duplicate GUI will be created, method is called Singleton Pattern
     * @return
     */
    public static Admin getInstance() {
        if (uniqueInstance == null) { //Single Checked
            synchronized (Admin.class) {
                if (uniqueInstance == null) { //Double Checked
                    uniqueInstance = new Admin();
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
        jlable = new JLabel("ADMIN", SwingConstants.CENTER);
        Summary_Button = new JButton("Summary of a customer");
        Summary_Button.addActionListener(this);
        ReportA_Button = new JButton("Report A");
        ReportA_Button.addActionListener(this);
        ReportB_Button = new JButton("Report B");
        ReportB_Button.addActionListener(this);
        MainMenu_Button = new JButton("Back to Main Menu");
        MainMenu_Button.addActionListener(this);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        pButtons = new JPanel(new GridLayout(3, 2, 10, 10));
        ptext = new JPanel(new GridLayout(2, 2, 10, 10));
        pButtons.add(Summary_Button);
        pButtons.add(ReportA_Button);
        pButtons.add(ReportB_Button);
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
    public Admin() {
        if (frame == null) {                //  Is it is not previously initialized, initializes it
            initGUI();
        }
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
     * Overriding function of ActionListener, this will be called when any action is performed, like button click
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Summary_Button) {      // if button click is Summary button
            String id = JOptionPane.showInputDialog(frame,
                    "Enter customer ID:", null);    // input id
            try {
                BankingSystem.accountSummary(id);       // call bankingSystem accountSummary function and pass the id we took from user
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == ReportA_Button) {
            try {
                BankingSystem.reportA();        // Calling BankingSystems's reportA function
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == ReportB_Button) {
            String min = JOptionPane.showInputDialog(frame,
                    "Enter minimum age:", null);    //input min age from user
            String max = JOptionPane.showInputDialog(frame,
                    "Enter maximum age:", null);    //input max age from user for report
            try {
                BankingSystem.reportB(min, max);            // calling BankingSystems's reportB function with Min and Max age
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == MainMenu_Button) {
            hide();             // if MainMenu button is clicked, hide this GUI
            MainPage.show();    // and Show MainPage gui
        }
    }
}
