/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuration;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author nicholastps
 */
public class Database {
    public static HashMap<String, Set> batchOrders;

    public static void setBatchOrders(HashMap<String, Set> batchOrders) {
        Database.batchOrders = batchOrders;
    }

    public static HashMap<String, Set> getBatchOrders() {
        return batchOrders;
    }
    
    
    
}
