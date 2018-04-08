/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm.OrderCluster;

import Entity.Order;
import Entity.OrderLine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Boon Keat
 */
public class ClusteringOrderList {

    public static  HashMap<String, Set<String>> clusterOrder(ArrayList<Order> orders) {
        HashMap<String, Set<String>> clusteredMap = new HashMap<String, Set<String>>();
        ArrayList<Order> clusteredOrder = new ArrayList<Order>();

//        int counter = 1;
        
        for (Order order : orders) {
//            System.out.println(counter++);
            Iterator<Order> iter = clusteredOrder.iterator();

            Order newOrder = null;

            while (iter.hasNext()) {
                Order tempOrder = iter.next();

                if (order.isSimilar(tempOrder)) {
                    iter.remove();
                    String docNum = order.getDocNum() + "," + tempOrder.getDocNum();
                    ArrayList<OrderLine> newList = new ArrayList<OrderLine>();
                    newList.addAll(order.getItems());
                    newList.addAll(tempOrder.getItems());

                    newOrder = new Order(newList, docNum);
                    break;
                }
            }
            
            if(newOrder != null){
                clusteredOrder.add(newOrder);
            } else {
                clusteredOrder.add(order);
            }
        }
        
        int count = 1;
        
        for(Order order : clusteredOrder){
            String docNums = order.getDocNum();
            
            Set<String> docNumSet = new HashSet<String>();
            
            if(docNums.contains(",")){
                String[] nums = docNums.split(",");
                for(String num: nums){
                    docNumSet.add(num);
                }
            } else {
                docNumSet.add(docNums);
            }
            
            clusteredMap.put("batch"+count, docNumSet);
            
            count++;
        }
        
        return clusteredMap;
    }
}
