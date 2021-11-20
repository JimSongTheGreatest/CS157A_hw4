import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

public class P2 {

    /**
     * Driver function
     * @param argv
     * @throws IOException
     */
    public static void main(String[] argv) {
        argv = new String[1];
        argv[0] = "db.properties";

        if (argv.length < 1) {
            JOptionPane.showMessageDialog(null, "Need database properties filename", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            BankingSystem.init(argv[0]);
            BankingSystem.testConnection();
            try {
                MainPage.getInstance();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
