import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * The Restaurant class is used to manipulate restaurant data in the FoodQuickMS database. It
 * contains methods for finding and retrieving records from the 'restaurant' table.
 */

public class Restaurant {

    // Database access variables
    private Statement statement;
    private ResultSet results;

    Scanner input = new Scanner(System.in);

    /**
     * Constructor method instantiates Restaurant object with a received Statement instance to submit
     * SQL statements to the database.
     *
     * @param statement Statement instance received from calling class.
     * @throws SQLException if there is an error obtaining an SQL Statement instance.
     */
    public Restaurant(Statement statement) throws SQLException {
        this.statement = statement;
    }

    /**
     * This method is used to select a record from the 'restaurant' table in the FoodQuick database.
     *
     * @return a set containing the user's chosen record.
     * @throws SQLException if there is an error retrieving records from the database.
     */
    public ResultSet pickRestaurantRecord() throws SQLException {

        int choice;

        // Prints the records in the 'customer' table
        results = statement.executeQuery("SELECT * FROM restaurant");
        if (results.next()) {
            System.out.println("\n ** Restaurants ** \n");
            do {
                System.out.println(
                        results.getString("restaurant_id") + ", " +
                        results.getString("restaurant_name") + ", " +
                        results.getString("city"));
            } while (results.next());
        }
        // Selects specific record based on user input.
        System.out.println("\nSelect a restaurant by typing in the restaurant number: ");
        choice = Integer.parseInt(input.nextLine());
        results = statement.executeQuery("SELECT * FROM restaurant WHERE restaurant_id = "
                + choice);

        // Returns a record containing the user's chosen restaurant information.
        return results;
    }
}
