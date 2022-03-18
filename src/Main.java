/**
 * FoodQuick Order Processing System 2.0
 *<p>
 * A basic food order and delivery processing application.
 * <p>
 * Customer orders from selected restaurants are matched with available drivers. Data for orders,
 * customers, restaurants and drivers are stored and accessed via a MySQL database. Invoices for
 * orders are printed out as .txt files.
 *
 * @author Eckard Smuts
 * @version 2022-02-16
 */

import java.util.Scanner;

/**
 * The Main class calls the user interface for interacting with the FoodQuick app.
 */

public class Main {

    /**
     * Main method calls the app user interface until the user chooses the exit option ('x').
     *
     * @param args String array that receives command-line input for running the application.
     */
    public static void main (String[] args) {

        // Constructs menu object to call menu methods.
        Menu menu = new Menu();
        // Constructs Scanner object to pass to Menu class.
        Scanner input = new Scanner(System.in);
        char choice;

        System.out.println("Welcome to the FoodQuick Order Delivery System.\n");

        do {
            // Calls menu and obtains user choice.
            choice = menu.printMenu(input);
            // Executes chosen menu option.
            menu.executeOption(choice);
        } while (!(choice == 'x'));

        // Closes scanner object
        input.close();
    }
}
