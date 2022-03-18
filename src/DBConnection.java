import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * This class controls the application's connection to the FoodQuickMS database.
 * <p>
 * The class level String parameters (connectionURL, connectionLogin, connectionPassword) are
 * valid for local MySQL access only, and need to be modified by each user according to their own
 * database configuration requirements.
 */

public class DBConnection {

    // User needs to modify these String values for local database configuration
    private final String connectionURL = "jdbc:mysql://localhost:3306/foodquickms";
    private final String connectionUser = "localuser";
    private final String connectionPassword = "password123";

    private Connection connection;
    private Statement statement;

    /**
     * Constructor initialises DBConnection object with a connection to the FoodQuickMS database.
     */
    public DBConnection() {
        // Creates a database connection
        try {
            this.connection = DriverManager.getConnection(
                    connectionURL, connectionUser, connectionPassword);
            this.statement = connection.createStatement();
            // Catches SQL exceptions
        } catch (SQLException error){
            error.printStackTrace();
            System.out.println("There is a problem connecting with the FoodQuick database." +
                    "\nPlease check your configuration and try running the program again.");
        }
    }

    /**
     * Accessor method for SQL Statement instance.
     *
     * @return statement instance for the current database connection.
     * @throws SQLException
     */
    public Statement getStatement() throws SQLException {
        return statement;
    }

    /**
     * Accessor method used to close the current database Connection instance.
     *
     * @throws SQLException
     */
    public void closeConnection () throws SQLException{
        connection.close();
    }
}
