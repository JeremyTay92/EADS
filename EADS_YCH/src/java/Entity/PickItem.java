/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.util.Date;

/**
 *
 * @author Boon Keat
 */
public class PickItem {
    private String productSKU;
    private String location;
    private double noOfCarton;

    public PickItem(String productSKU, String location, double noOfCarton) {
        this.productSKU = productSKU;
        this.location = location;
        this.noOfCarton = noOfCarton;
    }

    public String getProductSKU() {
        return productSKU;
    }

    public void setProductSKU(String productSKU) {
        this.productSKU = productSKU;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getNoOfCarton() {
        return noOfCarton;
    }

    public void setNoOfCarton(double noOfCarton) {
        this.noOfCarton = noOfCarton;
    }
    
    public String toString(){
        return productSKU + "-" + noOfCarton;
    }
}
