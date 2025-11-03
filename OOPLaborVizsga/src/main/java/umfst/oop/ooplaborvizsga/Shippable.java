/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package umfst.oop.ooplaborvizsga;

import umfst.oop.ooplaborvizsga.Pojos.Dimensions;
import umfst.oop.ooplaborvizsga.Pojos.Weight;

/**
 *
 * @author edite
 */

//items that have physical attributes for shipping
public interface Shippable {
    
    Dimensions getDimensions();
    Weight getWeight();
    double getShippingWeightKg();
}
