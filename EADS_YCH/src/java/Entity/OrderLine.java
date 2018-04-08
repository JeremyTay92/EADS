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
public class OrderLine {
    private String docNum;
    private String productSKU;
    private double quantity;
    private double numOfCarton;
    private Date creationDate;
    private String location;

    public OrderLine(String docNum, String productSKU, double quantity, double numOfCarton, Date creationDate, String location) {
        this.docNum = docNum;
        this.productSKU = productSKU;
        this.quantity = quantity;
        this.numOfCarton = numOfCarton;
        this.creationDate = creationDate;
        this.location = location;
    }

    public String getDocNum() {
        return docNum;
    }

    public void setDocNum(String docNum) {
        this.docNum = docNum;
    }

    public String getProductSKU() {
        return productSKU;
    }

    public void setProductSKU(String productSKU) {
        this.productSKU = productSKU;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getNumOfCarton() {
        return numOfCarton;
    }

    public void setNumOfCarton(double numOfCarton) {
        this.numOfCarton = numOfCarton;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
    public String toString(){
        return productSKU + "-" + quantity + "-" + location;
    }
}
