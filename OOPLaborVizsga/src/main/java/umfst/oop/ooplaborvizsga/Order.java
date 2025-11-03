/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package umfst.oop.ooplaborvizsga;

import umfst.oop.ooplaborvizsga.Pojos.Tax;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author edite
 */
/**
 * The main Order entity, extends BaseEntity.
 * It holds collections of other objects (Requirement: Use ArrayList).
 */

public class Order extends BaseEntity {

    private String orderId;
    private Date createdAt;
    private String status;
    private Customer customer;
    private List<OrderItem> items;
    private List<Tax> taxes;
    
    public Order(String orderId, Customer customer, Date createdAt, String status) {
        super();
        this.orderId = orderId;
        this.customer = customer;
        this.createdAt = createdAt;
        this.status = status;
        this.items = new ArrayList<OrderItem>(); // Requirement: Use ArrayList
        this.taxes = new ArrayList<Tax>(); // Requirement: Use ArrayList
        registerEntity(this);
    }
    
    public void addItem(OrderItem item) { this.items.add(item); }
    public void addTax(Tax tax) { this.taxes.add(tax); }
    
    public List<OrderItem> getItems() { return items; }
    public Customer getCustomer() { return customer; }
    public String getStatus() { return status; }
    public Date getCreatedAt() { return createdAt; }

    /**
     * Main business logic method to calculate the grand total for the order.
     * It iterates over the abstract OrderItem list (Requirement 10).
     * @return The grand total for the order.
     */
    public double calculateGrandTotal() {
        double total = 0;
        // Assume first tax applies to all, as per JSON
        Tax tax = (taxes.isEmpty()) ? new Tax("NONE", 0.0) : taxes.get(0); 
        
        // Requirement 10: Use interface/abstract in logic
        for (OrderItem item : items) {
            total += item.calculateLineTotal(this.customer, tax);
        }
        // Could add shipping costs, etc. here
        return total;
    }

    @Override
    public String businessKey() {
        return this.orderId;
    }
    
    @Override
    public String toString() {
        return "Order [ID: " + orderId + ", Customer: " + customer.getName() 
               + ", Status: " + status + ", Items: " + items.size() + "]";
    }
}

