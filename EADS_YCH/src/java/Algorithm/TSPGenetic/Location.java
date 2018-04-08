/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm.TSPGenetic;

import Configuration.Setting;
import DAO.ProductDAO;
import Entity.PickItem;
import Entity.Picker;
import Entity.Product;
import Utility.Warehouse;
import java.util.ArrayList;

/**
 *
 * @author Boon Keat
 */
public class Location {

    String location;
    double weight;
    double size;

    ArrayList<PickItem> pickingList;

    public Location(String location, ArrayList<PickItem> pickingList) {
        this.location = location;
        this.pickingList = pickingList;

        double valueWeight = 0.0;
        double valueSize = 0.0;

        for (PickItem item : pickingList) {
            String sku = item.getProductSKU();

            Product product = ProductDAO.getProduct(sku);

            if (product != null) {
                valueWeight += product.getWeight() * item.getNoOfCarton();
                valueSize += product.getWidth() * product.getHeight() * product.getLength() * item.getNoOfCarton();
            } else {
                valueWeight += Setting.unknown_product_weight;
                valueSize += Setting.unknown_product_length * Setting.unknown_product_width * Setting.unknown_product_height * item.getNoOfCarton();
            }

        }

        this.weight = valueWeight;
        this.size = valueSize;

//        System.out.println(size);
//        System.out.println(weight);
    }

    public String getLocation() {
        return location;
    }

    public double getWeight() {
        return weight;
    }

    public double getSize() {
        return size;
    }

    public double distanceTo(Location otherPoint) {

//        double distance =  2 + Math.random() * 10000;
        double distance = Warehouse.calculateDistance(location, otherPoint.getLocation());

//        System.out.println(location + " - " + otherPoint + " : " + distance);
        return distance;
    }
    
    public ArrayList<PickItem> getPickingList(){
        return pickingList;
    }

    public String toString() {
        return location + pickingList;

//        return location;
    }
}
