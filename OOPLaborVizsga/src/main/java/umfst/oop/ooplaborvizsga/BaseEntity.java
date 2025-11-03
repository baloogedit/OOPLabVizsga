/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package umfst.oop.ooplaborvizsga;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author edite
 */
public abstract class BaseEntity implements Identifiable{
    


/**
 * Requirement 5: Abstract base class BaseEntity.
 * Implements Identifiable.
 * Contains static members for ID generation and a registry.
 * Implements equals() and hashCode() based on businessKey().
 */

    protected long id;

    // Static member as a counter
    private static long idCounter = 0;

    // Static member as a registry (now using ArrayList)
    private static List<BaseEntity> businessKeyRegistry = new ArrayList<BaseEntity>();

    public BaseEntity() {
        this.id = getNextId();
    }

    private static synchronized long getNextId() {
        return ++idCounter;
    }

    public static void registerEntity(BaseEntity entity) {
        if (entity == null || entity.businessKey() == null || entity.businessKey().length() == 0) {
            return; // Don't register invalid entities
        }

        // Remove any existing entity with the same business key (to maintain uniqueness)
        for (int i = 0; i < businessKeyRegistry.size(); i++) {
            BaseEntity existing = businessKeyRegistry.get(i);
            if (entity.businessKey().equals(existing.businessKey())) {
                businessKeyRegistry.set(i, entity);
                return;
            }
        }

        // Otherwise, add it to the list
        businessKeyRegistry.add(entity);
    }

    public static BaseEntity findByBusinessKey(String key) {
        if (key == null) return null;

        // Linear search since we're using an ArrayList
        for (BaseEntity entity : businessKeyRegistry) {
            if (key.equals(entity.businessKey())) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public long getId() {
        return id;
    }

    /**
     * Abstract method for a unique business key (e.g., SKU, Order ID).
     */
    public abstract String businessKey();

    // --- equals() and hashCode() (Requirement 7) ---

    /**
     * Requirement 7: Override equals() using the business key.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        BaseEntity that = (BaseEntity) obj;
        String thisKey = this.businessKey();
        String thatKey = that.businessKey();
        
        // Java 6: No Objects.equals
        return (thisKey != null) ? thisKey.equals(thatKey) : (thatKey == null);
    }

    /**
     * Requirement 7: Override hashCode() using the business key.
     * @return 
     */
    @Override
    public int hashCode() {
        String thisKey = this.businessKey();
        return (thisKey != null ? thisKey.hashCode() : 0);
    }

    /**
     * Requirement 7: Override toString().
     * @return 
     */
    @Override
    public String toString() {
        return "ID: " + this.id + ", BusinessKey: " + this.businessKey();
    }

    
}
