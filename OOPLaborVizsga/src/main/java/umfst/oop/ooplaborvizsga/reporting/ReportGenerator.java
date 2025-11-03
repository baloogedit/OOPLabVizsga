/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package umfst.oop.ooplaborvizsga.reporting;

import umfst.oop.ooplaborvizsga.Order;
import umfst.oop.ooplaborvizsga.OrderItem;
import umfst.oop.ooplaborvizsga.Shippable;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Requirement 1 & 8: Reads model objects, computes totals,
 * identifies shippable items, applies sorting, and writes report.txt.
 */
public class ReportGenerator {

    private List<Order> orders;
    private StringBuilder consoleSummary;
    private StringBuilder reportFile;

    public ReportGenerator(List<Order> orders) {
        this.orders = orders;
        this.consoleSummary = new StringBuilder();
        this.reportFile = new StringBuilder();
    }

    /**
     * Main method to generate all outputs.
     */
    public void generate() {
        consoleSummary.append("--- CONSOLE SUMMARY ---");
        reportFile.append("--- E-COMMERCE FULL ORDER REPORT ---");

        double grandTotalAllOrders = 0;
        int totalItems = 0;
        int physicalItems = 0;

        for (Order order : orders) {
            reportFile.append("\n\n============================================\n");
            reportFile.append("ORDER ID: " + order.businessKey() + "\n");
            reportFile.append("CUSTOMER: " + order.getCustomer().getName() + " (Tier: " + order.getCustomer().getLoyaltyTier() + ")\n");
            reportFile.append("STATUS: " + order.getStatus() + "\n");
            reportFile.append("--------------------------------------------\n");
            reportFile.append("ITEMS:\n");

            // Requirement 8d: Output items sorted by total line value.
            // We get the items, calculate their totals, then sort.
            double orderTotal = order.calculateGrandTotal(); // This calculates all line totals
            grandTotalAllOrders += orderTotal;
            
            // Java 6: Must use anonymous inner class
            List<OrderItem> sortedItems = new ArrayList<OrderItem>(order.getItems());
            Collections.sort(sortedItems, new Comparator<OrderItem>() {
                public int compare(OrderItem o1, OrderItem o2) {
                    // We need the *final* total (post-tax/discount)
                    // We must re-calculate or store it. Our calculateLineTotal stores it.
                    double total1 = o1.getBaseTotal() - o1.getCalculatedDiscount() + o1.getCalculatedTax();
                    double total2 = o2.getBaseTotal() - o2.getCalculatedDiscount() + o2.getCalculatedTax();
                    // Sort descending
                    return Double.compare(total2, total1);
                }
            });

            // Requirement 8b: Identify digital/service vs physical
            for (OrderItem item : sortedItems) {
                totalItems++;
                reportFile.append(item.toString() + "\n");
                
                double base = item.getBaseTotal();
                double discount = item.getCalculatedDiscount();
                double tax = item.getCalculatedTax();
                double lineTotal = base - discount + tax;

                reportFile.append(String.format("  -> Base: %.2f, Discount: -%.2f, Tax: +%.2f, TOTAL: %.2f\n",
                    base, discount, tax, lineTotal));

                // Requirement 10: Use interface/abstract in logic
                if (item instanceof Shippable) {
                    physicalItems++;
                    Shippable s = (Shippable) item; // Cast to interface
                    reportFile.append("  -> [PHYSICAL] Weight: " + s.getShippingWeightKg() + "kg\n");
                } else {
                    reportFile.append("  -> [NON-PHYSICAL] Shipping not required.\n");
                }
            }
            reportFile.append("--------------------------------------------\n");
            reportFile.append(String.format("ORDER TOTAL: %.2f\n", orderTotal));
        }
        
        reportFile.append("\n\n============================================\n");
        reportFile.append("--- GRAND SUMMARY ---\n");
        reportFile.append(String.format("Total Value All Orders: %.2f\n", grandTotalAllOrders));
        reportFile.append("Total Orders Processed: " + orders.size() + "\n");
        reportFile.append("Total Line Items: " + totalItems + "\n");
        reportFile.append("Shippable (Physical) Items: " + physicalItems + "\n");
        reportFile.append("Non-Shippable (Digital/Service) Items: " + (totalItems - physicalItems) + "\n");
        
        // Build console summary
        consoleSummary.append("\nSuccessfully processed " + orders.size() + " valid orders.");
        consoleSummary.append("\nTotal items: " + totalItems + " (" + physicalItems + " physical).");
        consoleSummary.append(String.format("\nGrand total value: %.2f\n", grandTotalAllOrders));
        consoleSummary.append("Full details written to report.txt\n");
    }

    /**
     * Requirement 1: Write report.txt
     */
    public void writeReportToFile(String filename) {
        // Java 6: FileWriter and PrintWriter
        FileWriter fileWriter = null;
        PrintWriter printWriter = null;
        try {
            fileWriter = new FileWriter(filename);
            printWriter = new PrintWriter(fileWriter);
            printWriter.print(reportFile.toString());
        } catch (Exception e) {
            System.err.println("Failed to write report file: " + e.getMessage());
        } finally {
            try {
                if (printWriter != null) printWriter.close();
                if (fileWriter != null) fileWriter.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }
    
    public String getConsoleSummary() {
        return consoleSummary.toString();
    }
    
    public String getReportContent() {
        return reportFile.toString();
    }
}
