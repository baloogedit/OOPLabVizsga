package umfst.oop.ooplaborvizsga.util;

import umfst.oop.ooplaborvizsga.*;
import umfst.oop.ooplaborvizsga.Pojos.*;
import umfst.oop.ooplaborvizsga.exception.DomainValidationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * DataLoader: Reads from a real data.json file and converts into model objects.
 */
public class DataLoader {

    public static List<Order> loadOrders() throws DomainValidationException {
        List<Order> orders = new ArrayList<Order>();
        String jsonData = readFile("data.json");

        if (jsonData == null || jsonData.trim().length() == 0) {
            throw new DomainValidationException("Error: data.json is empty or missing.");
        }

        try {
            JSONObject root = new JSONObject(jsonData);
            JSONArray ordersArray = root.optJSONArray("orders");
            if (ordersArray == null) return orders;

            for (int i = 0; i < ordersArray.length(); i++) {
                JSONObject oObj = ordersArray.getJSONObject(i);
                try {
                    Order order = parseOrder(oObj);
                    orders.add(order);
                } catch (DomainValidationException e) {
                    System.err.println("Validation failed: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Unexpected error parsing order: " + e.getMessage());
                }
            }
        } catch (JSONException e) {
            throw new DomainValidationException("Invalid JSON format: " + e.getMessage());
        }

        return orders;
    }

    private static Order parseOrder(JSONObject oObj) throws Exception {
        String orderId = oObj.optString("orderId", "");
        String status = oObj.optString("status", "UNKNOWN");
        String dateStr = oObj.optString("date", null);

        Date orderDate;
        if (dateStr != null) {
            try {
                orderDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(dateStr);
            } catch (Exception e) {
                orderDate = new Date();
            }
        } else {
            orderDate = new Date();
        }

        JSONObject cObj = oObj.optJSONObject("customer");
        Customer c = parseCustomer(cObj);

        Order order = new Order(orderId, c, orderDate, status);

        // Add tax
        JSONObject taxObj = oObj.optJSONObject("tax");
        if (taxObj != null) {
            Tax tax = new Tax(taxObj.optString("name", "VAT"), taxObj.optDouble("rate", 0.0));
            order.addTax(tax);
        }

        // Parse items
        JSONArray items = oObj.optJSONArray("items");
        if (items != null) {
            for (int j = 0; j < items.length(); j++) {
                JSONObject itemObj = items.getJSONObject(j);
                OrderItem item = parseItem(itemObj);
                order.addItem(validateItem(order, item));
            }
        }

        return order;
    }

    private static Customer parseCustomer(JSONObject cObj) {
        if (cObj == null) return null;
        String id = cObj.optString("customerId", "");
        String email = cObj.optString("email", "");
        String loyalty = cObj.optString("loyaltyLevel", "BRONZE");

        JSONObject nameObj = cObj.optJSONObject("name");
        String first = (nameObj != null) ? nameObj.optString("firstName", "") : "";
        String last = (nameObj != null) ? nameObj.optString("lastName", "") : "";

        Customer c = new Customer(id, new Name(first, last), email, loyalty);

        JSONObject addr = cObj.optJSONObject("address");
        if (addr != null) {
            c.addAddress(addr.optString("type", "billing"),
                    addr.optString("street", ""),
                    addr.optString("city", ""),
                    addr.optString("country", ""),
                    addr.optString("postalCode", ""));
        }

        return c;
    }

    private static OrderItem parseItem(JSONObject itemObj) throws Exception {
        String type = itemObj.optString("type", "PhysicalItem");
        String sku = itemObj.optString("sku", "");
        String name = itemObj.optString("name", "");
        int qty = itemObj.optInt("qty", 1);
        double price = itemObj.optDouble("price", 0.0);

        if ("DigitalItem".equalsIgnoreCase(type)) {
            JSONObject lic = itemObj.optJSONObject("license");
            License license = null;
            if (lic != null) {
                license = new License(lic.optString("key", ""), new Date());
            }
            return new DigitalItem(sku, name, qty, price, license);

        } else if ("ServiceItem".equalsIgnoreCase(type)) {
            JSONObject sd = itemObj.optJSONObject("serviceDetails");
            ServiceDetails details = null;
            if (sd != null) {
                details = new ServiceDetails(sd.optString("name", ""), sd.optInt("durationMonths", 0));
            }
            return new ServiceItem(sku, name, qty, price, details);

        } else { // Default: PhysicalItem
            JSONObject dim = itemObj.optJSONObject("dimensions");
            JSONObject w = itemObj.optJSONObject("weight");

            Dimensions d = (dim != null)
                    ? new Dimensions(dim.optDouble("width", 0), dim.optDouble("height", 0),
                    dim.optDouble("depth", 0), dim.optString("unit", "cm"))
                    : null;

            Weight weight = (w != null)
                    ? new Weight(w.optDouble("value", 0), w.optString("unit", "kg"))
                    : null;

            PhysicalItem p = new PhysicalItem(sku, name, qty, price, d, weight);

            JSONArray discounts = itemObj.optJSONArray("discounts");
            if (discounts != null) {
                for (int i = 0; i < discounts.length(); i++) {
                    JSONObject disc = discounts.getJSONObject(i);
                    p.addDiscount(new Discount(disc.optString("code", ""), disc.optDouble("amount", 0.0)));
                }
            }
            return p;
        }
    }

    private static OrderItem validateItem(Order order, OrderItem item) throws DomainValidationException {
        if (item.businessKey() == null || item.businessKey().trim().isEmpty()) {
            throw new DomainValidationException("Invalid item in order " + order.businessKey() + ": SKU is missing");
        }
        if (item.getQty() <= 0) {
            throw new DomainValidationException("Invalid quantity for SKU " + item.businessKey());
        }
        if (item.getPrice() < 0) {
            throw new DomainValidationException("Negative price for SKU " + item.businessKey());
        }

        BaseEntity.registerEntity(item);
        return item;
    }

    private static String readFile(String path) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return sb.toString();
    }
}
