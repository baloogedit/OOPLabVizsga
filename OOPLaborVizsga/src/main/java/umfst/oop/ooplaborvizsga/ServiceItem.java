/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package umfst.oop.ooplaborvizsga;

import umfst.oop.ooplaborvizsga.Pojos.ServiceDetails;
import umfst.oop.ooplaborvizsga.Pojos.Tax;

/**
 *
 * @author edite
 */
/**
 * Requirement 6 & 9: Concrete Subclass 3.
 * Not Shippable.
 */

public class ServiceItem extends OrderItem {

    private ServiceDetails service;

    public ServiceItem(String sku, String name, int qty, double price, ServiceDetails service) {
        super(sku, name, qty, price);
        this.service = service;
    }

    @Override
    public String getItemType() {
        return "SERVICE";
    }

    /**
     * Requirement 8a: Compute totals. No special GOLD discount applies.
     */
    @Override
    public double calculateLineTotal(Customer customer, Tax tax) {
        double baseTotal = getBaseTotal();
        double totalDiscount = getBaseDiscountAmount();
        
        this.lineDiscount = totalDiscount; // Store for reporting
        
        double discountedTotal = baseTotal - totalDiscount;
        if (discountedTotal < 0) discountedTotal = 0;
        
        this.lineTax = discountedTotal * tax.rate; // Store for reporting
        
        return discountedTotal + this.lineTax;
    }
}
