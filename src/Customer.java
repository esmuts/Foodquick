import java.util.Scanner;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Customer class is used to manipulate customer data in the FoodQuickMS database. It contains
 * methods for adding and updating customers in the FoodQuick database, as well as helper methods
 * for finding and retrieve records from the 'customer' table.
 */

public class Customer {

    // Customer variables
    private String firstName;
    private String lastName;
    private String contact;
    private String streetName;
    private int streetNumber;
    private String city;
    private String email;

    // Database access variables
    private Statement statement;
    private ResultSet results;

    Scanner input = new Scanner(System.in);

    /**
     * Constructor method instantiates Customer object with a received Statement instance to submit
     * SQL statements to the database.
     *
     * @param statement Statement instance received from calling class.
     * @throws SQLException if there is an error initialising the SQL Statement instance.
     */
    public Customer(Statement statement) throws SQLException {
        this.statement = statement;
    }

    /**
     * Method obtains customer details from user input and adds it to the FoodQuick database.
     * The database value for 'customer_id' is automatically created through Auto_Increment.
     *
     * @throws SQLException if there is an error adding details to the database.
     */
    public void addCustomer() throws SQLException {

        // User input
        System.out.println("Enter customer's first name: ");
        this.firstName = input.nextLine();
        System.out.println("Enter customer's last name: ");
        this.lastName = input.nextLine();
        System.out.println("Enter customer's contact number: ");
        this.contact = input.nextLine();
        System.out.println("Enter customer's street name: ");
        this.streetName = input.nextLine();
        System.out.println("Enter customer's street number: ");
        this.streetNumber = Integer.parseInt(input.nextLine());
        System.out.println("Enter customer's city: ");
        this.city = input.nextLine();
        System.out.println("Enter customer's email address: ");
        this.email = input.nextLine();

        // Adds info to the database. The customer_id field is automatically created
        statement.executeUpdate("INSERT INTO customer (first_name, last_name, contact, street," +
                "street_no, city, email) VALUES ('" + this.firstName + "', '" + this.lastName +
                "', '" + this.contact + "', '" + this.streetName + "', '" + this.streetNumber +
                "', '" + this.city + "', '" + this.email + "');");
    }

    /**
     * This method updates customer information. It calls helper methods to retrieve a particular
     * record based on user input, and then alters a user-specified field with a new value
     * entered by the user.
     *
     * @return int containing the value of the number of records affected.
     * @throws SQLException if there is an error reading from or writing to the database.
     */

    public int updateCustomer() throws SQLException {

        String newValue;
        String updateField = "";
        int fieldChoice;

        // Selects record from a set of records matching user query.
        results = (this.pickCustomerRecord());

        // Prints the selected record.
        if (results.next()) {
            System.out.println(
                    "Customer ID: " + results.getInt("customer_id") +
                    "\n[1] First name: " + results.getString("first_name") +
                    "\n[2] Last name: " + results.getString("last_name") +
                    "\n[3] Contact: " + results.getString("contact") +
                    "\n[4] Street: " + results.getString("street") +
                    "\n[5] Street number: " + results.getInt("street_no") +
                    "\n[6] City: " + results.getString("city") +
                    "\n[7] Email: " + results.getString("email"));

            // Obtains field to modify from the user.
            System.out.println("\nChoose field to update (1-7): ");
            fieldChoice = input.nextLine().charAt(0);

            // Switch expression assigns value to update field based on user's choice.
            switch (fieldChoice) {
                case '1':
                    updateField = "first_name";
                    break;
                case '2':
                    updateField = "last_name";
                    break;
                case '3':
                    updateField = "contact";
                    break;
                case '4':
                    updateField = "street";
                    break;
                case '5':
                    updateField = "street_no";
                    break;
                case '6':
                    updateField = "city";
                    break;
                case '7':
                    updateField = "email";
                    break;
                // Prints error message and returns zero if invalid field choice entered.
                default:
                    System.out.println("Sorry, that is not a valid option. " +
                            "\nHit 'return' to go back to the main menu.");
                    input.nextLine();
                    return 0;
            }

            // Obtains new value for the field from the user.
            System.out.println("Enter a new value for the field: ");
            newValue = input.nextLine();

            // Modifies chosen field in the database.
            return statement.executeUpdate("UPDATE customer SET " + updateField + " = '" +
                    newValue + "' WHERE customer_ID = " + results.getInt("customer_id"));

        // Prints message if no matching record could be found, and returns 0.
        } else {
            System.out.println("Sorry, no records could be found to match that number. " +
                    "\nHit 'return' to go back to the main menu.");
            input.nextLine();
            return 0;
        }
    }

    /**
     * This method is used to select a record from the 'customer' table in the FoodQuick database.
     *
     * @return a set containing the user's chosen record.
     * @throws SQLException if there is an error retrieving records from the database.
     */
    public ResultSet pickCustomerRecord() throws SQLException {

        int choice;

        // Prints the records in the 'customer' table
        results = statement.executeQuery("SELECT * FROM customer");
        if (results.next()) {
            System.out.println("\n ** Customers ** \n");
            do {
                System.out.println(
                        results.getString("customer_id") + ", " +
                        results.getString("last_name") + ", " +
                        results.getString("contact") + ", " +
                        results.getString("city"));
            } while (results.next());
        }
        // Selects specific record based on user input.
        System.out.println("\nSelect a customer by typing in the customer number: ");
        choice = Integer.parseInt(input.nextLine());
        results = statement.executeQuery("SELECT * FROM customer WHERE customer_id = "
                    + choice);

        // Returns a record containing the user's chosen customer information.
        return results;
    }
}
