/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package umfst.oop.ooplaborvizsga;

import umfst.oop.ooplaborvizsga.Pojos.Discount;
import umfst.oop.ooplaborvizsga.Pojos.Tax;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author edite
 */
/**
 * Requirement 6 & 9: Abstract parent for the polymorphic item hierarchy.
 * Extends BaseEntity. All items have a SKU as their businessKey.
 *
 * This is a key part of Requirement 10: Used in logic (List<OrderItem>).
 */

public abstract class OrderItem extends BaseEntity {

    protected String sku;
    protected String name;
    protected int qty;
    protected double price; // Price per unit
    protected List<Discount> discounts;
    
    // This field will be set by the Order when calculating totals
    protected double lineTax;
    protected double lineDiscount;

    public OrderItem(String sku, String name, int qty, double price) {
        super();
        this.sku = (sku != null) ? sku : "SKU-UNKNOWN-" + getId();
        this.name = (name != null) ? name : "Unnamed Product";
        this.qty = (qty > 0) ? qty : 1;
        this.price = (price > 0) ? price : 0.0;
        this.discounts = new ArrayList<Discount>(); // Requirement: Use ArrayList
        
        // Don't register here, as the SKU might be invalid.
        // We let the DataLoader/Parser decide when to register.
    }

    // --- Abstract Methods ---
    
    public abstract String getItemType();

    /**
     * Core business logic method.
     * Each subclass must implement this.
     * @param customer The customer placing the order (for loyalty checks)
     * @param tax The tax to apply
     * @return The final total price for this line (qty * price - discounts + tax)
     */
    public abstract double calculateLineTotal(Customer customer, Tax tax);

    // --- Common Methods ---
    
    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = (qty > 0) ? qty : 0; }
    public double getPrice() { return price; }
    public String getName() { return name; }

    public double getBaseTotal() {
        return this.price * this.qty;
    }
    
    public void addDiscount(Discount discount) {
        this.discounts.add(discount);
    }
    
    public double getCalculatedDiscount() { return this.lineDiscount; }
    public double getCalculatedTax() { return this.lineTax; }

    /**
     * Calculates the *base* discount amount from the discount list.
     * Does not include special customer discounts.
     */
    protected double getBaseDiscountAmount() {
        double totalDiscount = 0;
        for (Discount d : discounts) {
            totalDiscount += d.amount;
        }
        return totalDiscount;
    }

    @Override
    public String businessKey() {
        return this.sku;
    }
    
    /**
     * Requirement 11: Override toString().
     */
    @Override
    public String toString() {
        return "  " + getItemType() + " Item [Name: " + name + ", Qty: " + qty 
               + ", Unit Price: " + String.format("%.2f", price) + ", SKU: " + this.businessKey() + "]";
    }
}
