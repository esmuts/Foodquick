import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.Date;
import java.util.Scanner;

/**
 * This class is used to manage orders in the QuickFood database. It contains methods for creating,
 * modifying, viewing and finalising orders.
 */

public class Order {

    // Order variables
    private int orderID;
    private int customerID;
    private int restaurantID;
    private int driverID;
    private String instructions;
    private BigDecimal total;

    // Database access variables
    private Statement statement;
    private ResultSet results;

    Scanner input = new Scanner(System.in);

    /**
     * Constructor method instantiates Order object with a received Statement instance to submit
     * SQL statements to the database.
     *
     * @throws SQLException if there is an error obtaining an SQL Statement instance.
     */
    public Order(Statement statement) throws SQLException {
        this.statement = statement;
    }

    /**
     * Method obtains order details from various sources and adds it to the FoodQuick database.
     * The database value for 'order_id' is automatically created through Auto_Increment.
     *
     * @return an int value for the number of affected rows.
     * @throws SQLException if there is an error adding details to the database.
     */
    public int createOrder() throws SQLException {

        // Creates new order to auto-create order number, and marks it as incomplete.
        statement.executeUpdate("INSERT INTO quick_order (completed) VALUES (0)");
        // Obtains value of orderID for the highest (and therefore current) order record.
        results = statement.executeQuery("SELECT * FROM quick_order ORDER BY order_id DESC " +
                "LIMIT 1");
        if (results.next()) {
            orderID = results.getInt("order_id");
        }

        // Select a customer for the order by creating a Customer object and calling its methods.
        Customer customer = new Customer(this.statement);
        results = customer.pickCustomerRecord();

        // Adds customer ID parameter to the current order, if found.
        if (results.next()) {
                customerID = results.getInt("customer_id");
        } else {
            // Returns to the main menu without creating an order if invalid customer entered.
            System.out.println("Sorry, no records match that number. The order could not been " +
                    "created. \nHit 'return' to go back to the main menu.");
                input.nextLine();
                return 0;
            }

        // Select a restaurant for the order by creating a Restaurant object and calling its
        // methods.
        Restaurant restaurant = new Restaurant(this.statement);
        results = restaurant.pickRestaurantRecord();

        // Adds customer ID parameter to the current order, if found.
        if (results.next()) {
            restaurantID = results.getInt("restaurant_id");
        // Returns to the main menu without creating an order if invalid restaurant entered.
        } else {
            System.out.println("Sorry, no records match that number. The order could not been " +
                    "created. \nHit 'return' to go back to the main menu.");
            input.nextLine();
            return 0;
        }

        // Get meal items for the order by creating a Meal object and calling its methods.
        Meal meal = new Meal(statement);
        meal.inputMealItem(orderID);
        // While loop asks user if they would like to add another item to the meal until value
        // other than 'y' or 'Y' is entered.
        while (meal.getAnotherMeal() == Character.toLowerCase('y')) {
            meal.inputMealItem(orderID);
        }

        // Get order total
        total = meal.calculateTotal(orderID);

        // Get special instructions for the order.
        System.out.println("Enter special instructions for the meal: ");
        instructions = input.nextLine();

        // Select a driver for the order by creating a Driver object and calling its methods.
        Driver driver = new Driver(statement);
        results = driver.selectDriver(orderID);
        if (results.next()) {
            driverID = results.getInt("driver_id");
        } else {
            System.out.println("\nSorry, no records could be found to match that number. " +
                    "\nHit 'return' to go back to the main menu.");
            input.nextLine();
        }

        // Writes order values to the database
        return statement.executeUpdate("UPDATE quick_order SET " + "customer_id = " + customerID +
                ", restaurant_id = " + restaurantID + ", instructions = '" + instructions +
                "', total = " + total + ", driver_id = " + driverID + " WHERE order_id=" + orderID);
    }

    /**
     * This method retrieves and displays an abbreviated version of all the open (not completed)
     * orders on the FoodQuickMS database. Displayed fields are order number, customer name, total
     * price, and driver name.
     *
     * @throws SQLException if there is an error reading from the database.
     */
    public void viewAllOpenOrders() throws SQLException {

        // Retrieves all open orders from the order table in the database, joining customer
        // and driver tables.
        results = statement.executeQuery("SELECT quick_order.order_id, quick_order.total, " +
                "customer.last_name, driver.driver_id FROM quick_order " +
                "LEFT JOIN customer ON quick_order.customer_id = customer.customer_id " +
                "LEFT JOIN driver ON quick_order.driver_id = driver.driver_id " +
                "WHERE quick_order.completed = 0");

        // Prints the results.
        if (results.next()) {
            System.out.println("\n** Open Orders **\n");
            do {
                System.out.println("Order ID: " + results.getInt("order_id") + ", " +
                        "Customer name: " + results.getString("last_name") + ", " +
                        "Order total: R" + results.getBigDecimal("total") + ", " +
                        "Driver ID: " + results.getInt("driver_id"));
            } while (results.next());
        } else {
            System.out.println("No open orders found. " +
                    "\nHit 'return' to go back to the main menu.");
            input.nextLine();
        }
    }

    /**
     * This method calls a helper method to select an order from the FoodQuick database, and prints
     * the retrieved order record for the user to view.
     *
     * @throws SQLException if the database record cannot be accessed.
     */
    public void viewOrder() throws SQLException {

        // Asks user to choose an order.
        results = this.getOrder();

        // Prints results, if found.
        if (results.next()) {
            System.out.println("\n** Order #" + results.getInt("order_id") + " **" +
                    "\nCustomer code : " + results.getInt("customer_id") +
                    "\nRestaurant code: " + results.getInt("restaurant_id") +
                    "\nSpecial instructions: " + results.getString("instructions") +
                    "\nOrder total: " + results.getBigDecimal("total") +
                    "\nDriver code: " + results.getInt("driver_id") +
                    "\nCompleted: " + results.getInt("completed") +
                    "\nCompletion date: " + results.getDate("completion_date"));

        // Prints error message, if order record could not be found
        } else {
            System.out.println("\nSorry, no records could be found to match that number. " +
                    "\nHit 'return' to go back to the main menu.");
            input.nextLine();
        }
    }

    /**
     * This method is used to finalise orders. Helper methods are called to select an order from
     * the FoodQuick database. An Invoice object is created and its methods called to print the
     * order details to a text file. Finally, the order is marked as completed on the database,
     * and the completion date is entered.
     *
     * @throws SQLException if there is an error accessing the database.
     */
    public void finaliseOrder() throws SQLException {

        // Asks user to choose an order.
        results = this.getOrder();

        // Saves order number from results, if found.
        if (results.next()) {
            int finaliseID = results.getInt("order_id");

            // Calls a method from the Invoice class to print to file, passing a String obtained
            // from the Order object's toString method.
            Invoice invoice = new Invoice();
            invoice.printToFile(this.toString(finaliseID));

            // Converts current date to SQL date type
            Date date = Date.valueOf(LocalDate.now());

            // Sets the order status to completed, and adds a completion date.
            statement.executeUpdate("UPDATE quick_order SET completed = 1, completion_date = '" +
                    date + "' WHERE order_id=" + finaliseID);

        // Print error message if order could not be found.
        } else {
            System.out.println("Sorry, no records could be found to match that number. " +
                    "\nHit 'return' to go back to the main menu.");
            input.nextLine();
        }
    }

    /**
     * This method retrieves and displays an order of the user's choosing. It retrieves all orders
     * matching a search String, and then lets the user choose one from the list.
     *
     * @return a result containing the selected record.
     * @throws SQLException if there is an error reading from the database.
     */
    public ResultSet getOrder() throws SQLException {

        int choice;

        // Obtain an order number to find from the user.
        System.out.println("Type in the order number you are looking for: ");
        choice = Integer.parseInt(input.nextLine());

        // Selects and returns chosen record, if found.
        return statement.executeQuery("SELECT * FROM quick_order WHERE order_id = " + choice);
    }

    /**
     * This method is used to save all the order details to a String.
     *
     * @param finaliseID the order number matching order details to be saved to String
     * @return String containing all the values of the order.
     * @throws SQLException if there is an error passing SQL statements to the FoodQuick database.
     */
    public String toString(int finaliseID) throws SQLException {

        String itemString = "";
        String orderText = "";

        // Get a String description of ordered items, quantities, and prices.
        results = statement.executeQuery("SELECT meal_type, price, quantity FROM order_item " +
                "WHERE order_id=" + finaliseID);
        if (results.next()) {
            do {
                itemString += "\n" + results.getInt("quantity") + " x " +
                        results.getString("meal_type") + " (R" + (results.getBigDecimal("price").
                        multiply(BigDecimal.valueOf(results.getInt("quantity")))) + ")";
            } while (results.next());
        } else itemString += "No menu item information found.";

        // Get a set of results from the database containing customer, restaurant, driver and
        // special instruction information.
        results = statement.executeQuery("SELECT quick_order.order_id, quick_order.instructions, " +
                "quick_order.total, customer.first_name, customer.last_name, customer.contact, " +
                "customer.street, customer.street_no, customer.city, customer.email, " +
                "restaurant.restaurant_name, restaurant.city, restaurant.contact," +
                "driver.driver_name FROM quick_order " +
                "JOIN customer ON quick_order.customer_id = customer.customer_id " +
                "JOIN restaurant ON quick_order.restaurant_id = restaurant.restaurant_id " +
                "JOIN driver ON quick_order.driver_id = driver.driver_id " +
                "WHERE order_id = " + finaliseID);

        // Returns a Sting description of order information.
        if (results.next()) {

            // Adds customer information
            orderText += "Order number: " + results.getInt("quick_order.order_id") +
                    "\nCustomer name: " + results.getString("customer.first_name") + " " +
                    results.getString("customer.last_name") + "\nEmail: " +
                    results.getString("customer.email") + "\nPhone number: " +
                    results.getString("customer.contact") + "\nLocation: " +
                    results.getString("customer.city");

            // Adds restaurant information
            orderText += "\n\nYou have ordered the following from " +
                    results.getString("restaurant.restaurant_name") + " in " +
                    results.getString("restaurant.city") + ": \n";

            // Adds item order information, special instructions and total price
            orderText += itemString + "\n\nSpecial instructions: " +
                    results.getString("quick_order.instructions") + "\n\nTotal: R" +
                    results.getBigDecimal("quick_order.total");

            // Adds driver info and delivery address
            orderText += "\n\n" + results.getString("driver.driver_name") + " is nearest to the " +
                    "restaurant and so they will be delivering your order to you at: \n\n" +
                    results.getInt("customer.street_no") + " " +
                    results.getString("customer.street");

            // Adds restaurant contact number
            orderText += "\n\nIf you need to contact the restaurant, their number is " +
                    results.getString("restaurant.contact") + "\n\n";

            return orderText;

        // Returns fail message if order information could not be found,
        } else {
            return "The order information for order# " + finaliseID + " is unavailable. " +
                    "The invoice could not be printed.";
        }
    }
}
