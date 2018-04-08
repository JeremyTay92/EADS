/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Configuration.Setting;
import Entity.Product;
import Utility.ExcelAdapter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Boon Keat
 */
public class ProductDAO {

    private static HashMap<String, Product> productList = new HashMap<String, Product>();

    public static boolean isProductExist(String sku) {
        return productList.containsKey(sku);
    }

    public static HashMap<String, Product> getAllProduct() {
        return productList;
    }

    public static Product getProduct(String sku) {
        return productList.get(sku);
    }

    public static void addProduct(Product item) {
        productList.put(item.getSku(), item);
    }

    public static void loadData(String filename) {
        ArrayList<ArrayList<String>> dataTable = ExcelAdapter.loadData(filename, "Product Dimension");
        
        for (ArrayList<String> row : dataTable) {
            String sku = row.get(Setting.productSKUIndex);
            
            if(isProductExist(sku)){
                continue;
            }
            
            double length =  Double.parseDouble(row.get(Setting.productLengthIndex)) / 10.0;
            double width = Double.parseDouble(row.get(Setting.productWidthIndex)) / 10.0;
            double height = Double.parseDouble(row.get(Setting.productHeightIndex)) / 10.0;
            double weight = Double.parseDouble(row.get(Setting.productWeightIndex));
            
            addProduct(new Product(sku, length, width, height, weight));
        }
    }
}
