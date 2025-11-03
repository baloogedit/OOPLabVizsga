/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package umfst.oop.ooplaborvizsga;

import java.util.Date;

/**
 *
 * @author edite
 */

/**
 * A container class for all simple Plain Old Java Objects (POJOs)
 * derived from the JSON structure, to keep the file count manageable.
 */
public class Pojos {

    public static class Name {
        public String first;
        public String last;
        public Name(String first, String last) { this.first = first; this.last = last; }
        @Override public String toString() { return first + " " + last; }
    }
    
    public static class Address {
        public String type, line1, city, country, zip;
        public Address(String t, String l, String c, String co, String z) {
            this.type = t; this.line1 = l; this.city = c; this.country = co; this.zip = z;
        }
        @Override public String toString() { return type + ": " + line1 + ", " + city; }
    }
    
    public static class Dimensions {
        public double w, h, d; public String unit;
        public Dimensions(double w, double h, double d, String unit) {
            this.w = w; this.h = h; this.d = d; this.unit = unit;
        }
        @Override public String toString() { return w + "x" + h + "x" + d + " " + unit; }
    }
    
    public static class Weight {
        public double value; public String unit;
        public Weight(double v, String u) { this.value = v; this.unit = u; }
        @Override public String toString() { return value + " " + unit; }
    }
    
    public static class License {
        public String key; public Date expiresAt;
        public License(String k, Date e) { this.key = k; this.expiresAt = e; }
        @Override public String toString() { return "Key: " + key; }
    }
    
    public static class ServiceDetails {
        public String provider; public int termsMonths;
        public ServiceDetails(String p, int t) { this.provider = p; this.termsMonths = t; }
        @Override public String toString() { return "Provider: " + provider; }
    }
    
    public static class Discount {
        public String code; public double amount;
        public Discount(String c, double a) { this.code = c; this.amount = a; }
        @Override public String toString() { return code + " (-" + String.format("%.2f", amount) + ")"; }
    }

    public static class Tax {
        public String name; public double rate; // rate as decimal, e.g., 0.21
        public Tax(String n, double r) { this.name = n; this.rate = r; }
        @Override public String toString() { return name + " (" + (rate * 100) + "%)"; }
    }
}
