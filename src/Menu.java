import java.sql.SQLException;
import java.util.Scanner;

/**
 * The Menu class manages user interaction with the FoodQuick app. It has methods to obtain
 * the user's choice of menu option, and to execute the option.
 */

public class Menu {

    /**
     * Method prints a menu to obtain user input.
     *
     * @param input receives a Scanner object to read user input.
     * @return user's choice of menu option.
     */
    public char printMenu(Scanner input) {

        System.out.println("\nPlease select a menu option:\n" +
                "\n[1] Add new customer" +
                "\n[2] Update existing customer" +
                "\n[3] Create new order" +
                "\n[4] View existing order" +
                "\n[5] View all open orders" +
                "\n[6] Finalise order & print invoice" +
                "\n[x] Exit program");
        // Returns first typed character as user choice
        return Character.toLowerCase(input.next().charAt(0));
    }

    /**
     * Method employs a switch statement to call an operation that matches the user's chosen
     * menu option. A connection object is created, and a statement passed from the connection
     * object to each operation that requires one.
     *
     * @param choice receives user's choice of menu option.
     */
    public void executeOption(char choice) {

        int rowsAffected;

        try {

            // Creates an object to connect to the FoodQuickMS database.
            DBConnection connection = new DBConnection();

            switch (choice) {
                // Add customer
                case '1': {
                    Customer customer = new Customer(connection.getStatement());
                    customer.addCustomer();
                    break;
                }
                // Update customer
                case '2': {
                    Customer customer = new Customer(connection.getStatement());
                    rowsAffected = customer.updateCustomer();
                    System.out.println(rowsAffected + " customers were added.");
                    break;
                }
                // Create new order
                case '3': {
                    Order order = new Order(connection.getStatement());
                    rowsAffected = order.createOrder();
                    System.out.println(rowsAffected + " orders were created.");
                    break;
                }
                // View existing order
                case '4': {
                    Order order = new Order(connection.getStatement());
                    order.viewOrder();
                    break;
                }
                // View all open orders
                case '5': {
                    Order order = new Order(connection.getStatement());
                    order.viewAllOpenOrders();
                    break;
                }
                // Finalise order
                case '6': {
                    Order order = new Order(connection.getStatement());
                    order.finaliseOrder();
                    break;
                }
                // Exit program
                case 'x':
                    System.out.println("Bye!");
                    break;
                // Invalid option
                default:
                    System.out.println("Not a valid choice. Please try again");
                    break;
            }
            // Closes database statement and connection.
            connection.closeConnection();

            // Catches SQL exceptions thrown by method calls in the switch statement.
        } catch (SQLException error) {
            error.printStackTrace();
            System.out.println("There was an error connecting to the database." +
                    "\nPlease check your configuration and try running the program again.");
        }
    }
}
