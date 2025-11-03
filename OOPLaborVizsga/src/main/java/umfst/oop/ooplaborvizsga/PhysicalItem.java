/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package umfst.oop.ooplaborvizsga;

import umfst.oop.ooplaborvizsga.Pojos.Dimensions;
import umfst.oop.ooplaborvizsga.Pojos.Tax;
import umfst.oop.ooplaborvizsga.Pojos.Weight;


/**
 *
 * @author edite
 */
/**
 * Requirement 6 & 9: Concrete Subclass 1.
 * Implements the Shippable interface (Requirement 2).
 * Contains the special "GOLD" customer logic (Requirement 8c).
 */
public class PhysicalItem extends OrderItem implements Shippable {

    private Dimensions dimensions;
    private Weight weight;

    public PhysicalItem(String sku, String name, int qty, double price, Dimensions dims, Weight weight) {
        super(sku, name, qty, price);
        this.dimensions = (dims != null) ? dims : new Dimensions(0,0,0,"cm");
        this.weight = (weight != null) ? weight : new Weight(0,"kg");
    }

    @Override
    public String getItemType() {
        return "PHYSICAL";
    }

    // --- Shippable Interface Implementation ---
    @Override
    public Dimensions getDimensions() { return this.dimensions; }
    
    @Override
    public Weight getWeight() { return this.weight; }
    
    @Override
    public double getShippingWeightKg() {
        if (weight == null) return 0;
        double singleWeight = ("kg".equalsIgnoreCase(weight.unit)) ? weight.value : weight.value / 1000.0;
        return singleWeight * qty;
    }

    /**
     * Requirement 8a, 8c: Compute totals with taxes and special GOLD discount.
     */
    @Override
    public double calculateLineTotal(Customer customer, Tax tax) {
        double baseTotal = getBaseTotal();
        double totalDiscount = getBaseDiscountAmount();
        
        // Requirement 8c: Apply extra 5% discount to GOLD customer on physical goods
        if (customer != null && "GOLD".equals(customer.getLoyaltyTier())) {
            totalDiscount += (baseTotal * 0.05);
        }
        
        this.lineDiscount = totalDiscount; // Store for reporting
        
        double discountedTotal = baseTotal - totalDiscount;
        if (discountedTotal < 0) discountedTotal = 0;
        
        this.lineTax = discountedTotal * tax.rate; // Store for reporting
        
        return discountedTotal + this.lineTax;
    }
}