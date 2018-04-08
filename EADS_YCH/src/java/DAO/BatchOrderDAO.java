/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Boon Keat
 */
public class BatchOrderDAO {
    private static HashMap<String, Set<String>> batchOrders = new HashMap<String, Set<String>>();

    public static HashMap<String, Set<String>> getBatchOrders() {
        return batchOrders;
    }

    public static void setBatchOrders(HashMap<String, Set<String>> batchOrders) {
        BatchOrderDAO.batchOrders = batchOrders;
    }
    
    public static Set<String> getKeySet(){
        return batchOrders.keySet();
    }
    
    public static Set<String> getBatchOrder(String batchId){
        return batchOrders.get(batchId);
    }
}
