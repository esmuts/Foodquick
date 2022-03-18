import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * The Meal class is used to obtain meal information for each order item. It contains methods for
 * entering meal item information and adding it to the database, assessing whether another
 * meal item is required, obtaining special instructions, and calculating the total cost.
 *
 */

public class Meal {

    // Meal variables
    private String menuItem;
    private int quantity;
    private BigDecimal price;

    // Database variables
    private Statement statement;
    private ResultSet results;

    /**
     *  Constructor method instantiates Meal object with a received Statement instance to submit
     *  SQL statements to the database.
     *
     * @param statement Statement instance received from calling class.
     * @throws SQLException if there is an error initialising the SQL Statement instance.
     */
    public Meal(Statement statement) throws SQLException {
        this.statement = statement;
    }

    /**
     * This method is used to obtain order item details from the user.
     *
     * @param orderID the orderID value associated with the current meal item, to add to the
     *                record as foreign key.
     * @throws SQLException if there is an error adding details to the database.
     */

    public void inputMealItem(int orderID) throws SQLException {

        Scanner input = new Scanner(System.in);

        // Sets negative values to enable while loops.
        price = BigDecimal.valueOf(-1);
        quantity = -1;

        // Obtains order item detail
        System.out.println("\nEnter menu item to add to order: ");
        this.menuItem = input.nextLine();

        // Do-while loop repeats until the user enters a valid price (number).
        do {
            System.out.println("Enter item price: ");
            try {
                // Converts String input to double, then to BigDecimal
                price = BigDecimal.valueOf(Double.valueOf(input.nextLine()));
            }
            // Catch exception prompts user to enter a valid price.
            catch (NumberFormatException error) {
                    System.out.println("Please enter a valid price! (e.g. 25 or 99.99)");
                }
        } while (price.equals(BigDecimal.valueOf(-1)));

        // Do-while loop repeats until the user enters a valid quantity
        // (number).
        do {
            System.out.println(
                    "How many of this item do you want to order?");
            try {
                quantity = Integer.parseInt(input.nextLine());
            }
            // Catch exception prompts user to enter the quantity again.
            catch (NumberFormatException error) {
                System.out.println("Please enter a valid quantity!");
            }
        } while (quantity == -1);

        // Adds meal items to the database. The item_id field is automatically created.
        statement.executeUpdate("INSERT INTO order_item (meal_type, price, quantity, order_id)" +
                "VALUES ('" + menuItem + "', '" + price + "', '" + quantity + "', '" +
                orderID + "')");
    }

    /**
     * This method is used to ask the user if they want to add another menu item to their meal.
     *
     * @return char the user's response to another meal query (y/n)
     */
    public char getAnotherMeal() {

        Scanner input = new Scanner(System.in);

        System.out.println("Would you like to add another item to the meal? (y/n) ");
        return input.nextLine().charAt(0);
    }

    /**
     * This method is used to calculate and return the total cost of all the menu items associated
     * with a particular order ID.
     *
     * @param orderID the order ID for the current order.
     * @return a BigDecimal value of the total price for all items associated with the received
     *         orderID.
     * @throws SQLException if there is an error accessing the database.
     */
    public BigDecimal calculateTotal(int orderID) throws SQLException {

        BigDecimal itemTotal;
        BigDecimal totalPrice = BigDecimal.valueOf(0);


        // Gets all relevant items from the database
        results = statement.executeQuery("SELECT quantity, price FROM order_item " +
                "WHERE order_id = " + orderID);
        // Loops through the result set to calculate the total price.
        if (results.next()) {
            do {
                // Calculates total for current item
                itemTotal = results.getBigDecimal("price").
                        multiply(BigDecimal.valueOf(results.getInt("quantity")));
                // Adds item total to total price
                totalPrice = totalPrice.add(itemTotal);
            } while (results.next());
            return totalPrice;
        } else {
            return BigDecimal.valueOf(0);
        }
    }
}
