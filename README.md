# FoodQuick

Foodquick is a basic order processing and delivery system, built as a capstone project for a Java course. The program lets the user place an order for a customer at a restaurant, selects a driver to fulfil the order, and prints an invoice for the order to a text file.

## Contents

- Running FoodQuick
- Usage
- Credits

## Running FoodQuick

To run FoodQuick, download the source files and resources onto your local system. Then create a new project in your preferred Java IDE, and import the files into the project. Here is a simple explanation for how to do so in Eclipse:

[https://stackoverflow.com/questions/20170470/importing-class-java-files-in-eclipse]()

In order to run successfully, the user will have to set up a local database and populate it with appropriate tables and values. The login details for the database can be set in 'DBConnection.java'.

Simply run 'Main.java' to start the program.

## Usage

The main method automatically runs all the facets of the program. Simply follow on-screen prompts to enter order data. A driver for the order will automatically be chosen, and an invoice for the order will be printed to a text file.

The path for the invoice text file can be changed in 'Invoice.java'. The default path is set to 'resources/invoice.txt'.

## Credits

This program was written as a capstone project for a full-stack web development course at [https://www.hyperiondev.com](). Please feel free to let me know if you have any comments or suggestions.
