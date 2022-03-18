import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The Driver class is used to manipulate driver data in the FoodQuickMS database. It contains a
 * method for matching a driver to an order.
 *
 */

public class Driver {

    private Statement statement;
    private ResultSet results;

    String city;

    /**
     *  Constructor method instantiates Driver object with a received Statement instance to submit
     *  SQL statements to the database.
     *
     * @param statement Statement instance received from calling class.
     * @throws SQLException if there is an error initialising the SQL Statement instance.
     */
    public Driver(Statement statement) throws SQLException {
        this.statement = statement;
    }

    /**
     * This method is used to select a driver for a FoodQuick order. It determines which drivers
     * are in the right city, and selects the one with the minimum load.
     *
     * @return ResultSet the record of the driver selected for the order.
     * @throws SQLException if there is an error accessing the database.
     */
    public ResultSet selectDriver(int orderID) throws SQLException {

        // Gets the city in which the restaurant is located.
        results = statement.executeQuery("SELECT restaurant.city FROM quick_order " +
                "JOIN restaurant ON quick_order.restaurant_id = restaurant.restaurant_id " +
                        "WHERE order_id = " + orderID);
        // Saves city to String
        if (results.next()) {
            city = results.getString("city");
        }
        // Selects and returns the driver with the minimum load in the city.
        return statement.executeQuery("SELECT * FROM driver ORDER BY driver_load ASC LIMIT 1");
    }
}
