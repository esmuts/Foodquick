import java.io.FileWriter;
import java.io.IOException;

/**
 * The Invoice class is used to create and print invoices for FoodQuick orders. It contains a
 * method for writing a String to a text file.
 *
 */

public class Invoice {

    /** This method prints a String value of the order data to a text file.
     *
     * @param invoiceText a String containing the order data.
     */
    public void printToFile(String invoiceText) {

        // Try-catch block attempts to create a new FileWriter object, and
        // append the invoice's String value to the end of the specified file.
        try {
            FileWriter invoiceWriter = new FileWriter("invoice.txt");
            // Writes the invoice String to the end of the specified file.
            invoiceWriter.write(invoiceText);
            // Closes the FileWriter object.
            invoiceWriter.close();

        } catch (IOException error) {
            System.out.println("There was an error writing to the file.");
            error.printStackTrace();
        }
    }
}
