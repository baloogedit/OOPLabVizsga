/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package umfst.oop.ooplaborvizsga;

import umfst.oop.ooplaborvizsga.exception.DomainValidationException;
import umfst.oop.ooplaborvizsga.Customer;
import umfst.oop.ooplaborvizsga.Order;
import umfst.oop.ooplaborvizsga.OrderItem;
import umfst.oop.ooplaborvizsga.Pojos.Tax;
import umfst.oop.ooplaborvizsga.reporting.ReportGenerator;
import umfst.oop.ooplaborvizsga.ui.QuantityEditor;
import umfst.oop.ooplaborvizsga.util.DataLoader;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.util.List;

/**
 *
 * @author edite
 */
/**
 * Main application entry point.
 * Ties together data loading, reporting, and the GUI.
 */
public class OOPLaborVizsga {

    public static void main(String[] args) {
        
        System.out.println("Starting E-Commerce Report Processor...");
        
        try {
            // Requirement 1 & 4: Load data and handle exceptions
            List<Order> orders = DataLoader.loadOrders();
            
            // Requirement 8: Run business logic and reporting
            ReportGenerator generator = new ReportGenerator(orders);
            generator.generate();
            
            // Requirement 13: Print concise summary to console
            System.out.println(generator.getConsoleSummary());
            
            // Requirement 1: Write report.txt
            generator.writeReportToFile("report.txt");

            // Requirement 12: Launch Swing GUI
            // We'll pick the first item from the first order to edit
            if (!orders.isEmpty() && !orders.get(0).getItems().isEmpty()) {
                final Order orderToEdit = orders.get(0);
                final OrderItem itemToEdit = orderToEdit.getItems().get(0); // The "Clean Code" book
                final Customer customer = orderToEdit.getCustomer();
                final Tax tax = new Tax("VAT", 0.21); // Mock tax for GUI

                // Java 6: Must use SwingUtilities.invokeLater
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        createAndShowGui(itemToEdit, customer, tax);
                    }
                });
            }

        } catch (DomainValidationException e) {
            System.err.println("CRITICAL ERROR: Could not process data.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred:");
            e.printStackTrace();
        }
    }
    
    private static void createAndShowGui(OrderItem item, Customer customer, Tax tax) {
        JFrame frame = new JFrame("Item Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create and add the custom panel
        QuantityEditor editorPanel = new QuantityEditor(item, customer, tax);
        frame.getContentPane().add(editorPanel);
        
        frame.pack();
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);
    }
}
