/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package umfst.oop.ooplaborvizsga.ui;

import umfst.oop.ooplaborvizsga.Customer;
import umfst.oop.ooplaborvizsga.OrderItem;
import umfst.oop.ooplaborvizsga.Pojos.Tax;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.NumberFormat;

/**
 * Requirement 12: Swing GUI panel to edit item quantity.
 * This panel takes an OrderItem (the abstract class) and a Customer,
 * demonstrating polymorphism in the GUI.
 */
public class QuantityEditor extends JPanel {

    // Requirement 10: Use abstract class directly in logic
    private OrderItem item; 
    private Customer customer;
    private Tax tax;

    private JSpinner quantitySpinner;
    private JLabel itemNameLabel;
    private JLabel basePriceLabel;
    private JLabel discountLabel;
    private JLabel taxLabel;
    private JLabel finalPriceLabel;

    // Java 6: NumberFormat for currency
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    public QuantityEditor(OrderItem item, Customer customer, Tax tax) {
        // Requirement 10: Logic uses the abstract class
        this.item = item; 
        this.customer = customer;
        this.tax = tax;

        // --- Setup GUI ---
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Live Quantity Editor"));

        itemNameLabel = new JLabel(item.getName() + " (" + item.businessKey() + ")");
        add(itemNameLabel, BorderLayout.NORTH);

        // Spinner for quantity
        // SpinnerNumberModel(initialValue, min, max, step)
        SpinnerModel spinnerModel = new SpinnerNumberModel(item.getQty(), 1, 99, 1);
        quantitySpinner = new JSpinner(spinnerModel);
        
        // Price details panel
        JPanel detailsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        basePriceLabel = new JLabel();
        discountLabel = new JLabel();
        taxLabel = new JLabel();
        finalPriceLabel = new JLabel();
        
        detailsPanel.add(new JLabel("Base Price:"));
        detailsPanel.add(basePriceLabel);
        detailsPanel.add(new JLabel("Discount:"));
        detailsPanel.add(discountLabel);
        detailsPanel.add(new JLabel("Tax:"));
        detailsPanel.add(taxLabel);
        detailsPanel.add(new JLabel("FINAL LINE PRICE:"));
        detailsPanel.add(finalPriceLabel);
        
        add(new JLabel("Quantity:"), BorderLayout.WEST);
        add(quantitySpinner, BorderLayout.CENTER);
        add(detailsPanel, BorderLayout.SOUTH);

        // --- Event Listener ---
        quantitySpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                // On spinner change, update the item and recalculate
                int newQty = (Integer) quantitySpinner.getValue();
                updatePrice(newQty);
            }
        });
        
        // Initial calculation
        updatePrice(item.getQty());
    }

    /**
     * Requirement 12: Live recompute
     * This method is called every time the spinner changes.
     */
    private void updatePrice(int newQty) {
        // 1. Set the new quantity on the model object
        item.setQty(newQty);
        
        // 2. Recalculate the total
        // The polymorphic calculateLineTotal() method is called.
        // The GUI doesn't know or care if it's a PhysicalItem or DigitalItem.
        double finalTotal = item.calculateLineTotal(customer, tax);
        
        // 3. Update all labels
        basePriceLabel.setText(currencyFormat.format(item.getBaseTotal()));
        
        // Requirement 12: Per-line discount visibility
        discountLabel.setText(currencyFormat.format(item.getCalculatedDiscount() * -1)); 
        taxLabel.setText(currencyFormat.format(item.getCalculatedTax()));
        finalPriceLabel.setText(currencyFormat.format(finalTotal));
        
        // Also update the main item label
        itemNameLabel.setText(item.getName() + " (Qty: " + newQty + ")");
    }
}

