/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package umfst.oop.ooplaborvizsga;

import umfst.oop.ooplaborvizsga.Pojos.Address;
import umfst.oop.ooplaborvizsga.Pojos.Name;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author edite
 */
/**
 * Customer entity, extends BaseEntity.
 * Holds the `loyaltyTier`, which is crucial for business logic.
 */

public class Customer extends BaseEntity{
    private String customerId;
    private Name name;
    private String email;
    private String loyaltyTier;
    private List<Address> addresses;

    /**
     * Requirement 11: Overloaded constructor.
     * Primary constructor with all details.
     */
    public Customer(String customerId, Name name, String email, String loyaltyTier) {
        super();
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.loyaltyTier = (loyaltyTier != null) ? loyaltyTier : "BRONZE"; // Default
        this.addresses = new ArrayList<Address>(); // Requirement: Use ArrayList
        registerEntity(this);
    }
    
    /**
     * Requirement 11: Overloaded constructor.
     * Simpler constructor for a new customer.
     */
    public Customer(String customerId, Name name, String email) {
        // Calls the primary constructor with a default loyalty tier
        this(customerId, name, email, "BRONZE");
    }

    public String getLoyaltyTier() {
        return loyaltyTier;
    }
    
    public Name getName() {
        return name;
    }

    /**
     * Requirement 11: Overloaded method.
     */
    public void addAddress(Address address) {
        this.addresses.add(address);
    }
    
    /**
     * Requirement 11: Overloaded method.
     */
    public void addAddress(String type, String line1, String city, String country, String zip) {
        this.addAddress(new Address(type, line1, city, country, zip));
    }

    @Override
    public String businessKey() {
        return this.customerId;
    }
    
    @Override
    public String toString() {
        return "Customer [Name: " + name + ", Tier: " + loyaltyTier + ", " + super.toString() + "]";
    }
    
}
