/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Algorithm.KnapsackGenetic.KnapsackProblem;
import Algorithm.TSPGenetic.TSP_GA;
import Configuration.Setting;
import DAO.LevelDAO;
import DAO.ProductDAO;
import Utility.Warehouse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Boon Keat
 */
public class Picker {

    private ArrayList<PickItem> pickingList = new ArrayList<PickItem>();
    private static int counter = 0;
    private String id;
    
    public Picker(){
        this.id = ++counter + "";
    }

    public ArrayList<PickItem> getPickingList() {
        return pickingList;
    }

    public void clearPickingList() {
        this.pickingList = new ArrayList<PickItem>();
    }

    public void setPickingList(ArrayList<PickItem> pickingList) {
        this.pickingList = pickingList;
    }

    public Set<String> getAllLocation() {
        Set<String> locations = new HashSet<String>();

        for (PickItem item : pickingList) {
            locations.add(item.getLocation());
        }

        return locations;
    }

    public HashMap<String, HashMap<String, Double>> generateAdjacencyMatrix(String startLocation, String dropOffLocation) {

        HashMap<String, HashMap<String, Double>> matrices = new HashMap<String, HashMap<String, Double>>();

        HashMap<String, Double> startMatrixMap = new HashMap<String, Double>();

        for (int x = 0; x < pickingList.size(); x++) {
            PickItem pickingItem = pickingList.get(x);

            startMatrixMap.put(pickingItem.getLocation(), Warehouse.calculateDistance(startLocation, pickingItem.getLocation()));
        }

        matrices.put(startLocation, startMatrixMap);

        HashMap<String, Double> dropOffMatrixMap = new HashMap<String, Double>();

        for (int x = 0; x < pickingList.size(); x++) {
            PickItem pickingItem = pickingList.get(x);

            dropOffMatrixMap.put(pickingItem.getLocation(), Warehouse.calculateDistance(dropOffLocation, pickingItem.getLocation()));
        }

        matrices.put(dropOffLocation, dropOffMatrixMap);

        for (int i = 0; i < pickingList.size(); i++) {
            HashMap<String, Double> matrix = new HashMap<String, Double>();
            PickItem firstItem = pickingList.get(i);

            for (int x = 0; x < pickingList.size(); x++) {
                if (i != x) {

                    PickItem secondItem = pickingList.get(x);

                    matrix.put(secondItem.getLocation(), Warehouse.calculateDistance(firstItem.getLocation(), secondItem.getLocation()));
                }
            }

            matrices.put(firstItem.getLocation(), matrix);
        }

        return matrices;
    }

    public HashMap<String, ArrayList<PickItem>> knapsackItem(String location) {

        double weightBudget = Setting.humanWeightBudget;
        double sizeBudget = Setting.humanSizeBudget;

        if (!LevelDAO.isHuman(location)) {
            weightBudget = Setting.forkliftWeightBudget;
            sizeBudget = Setting.forkliftSizeBudget;
        }

        HashMap<String, ArrayList<PickItem>> returnMap = new HashMap<String, ArrayList<PickItem>>();

        ArrayList<PickItem> tempList = new ArrayList<PickItem>();

        for (PickItem item : pickingList) {
            String itemLocation = item.getLocation();

            if (itemLocation.equals(location)) {
                tempList.add(item);
            }
        }

        int counter = 1;
        
        while (!tempList.isEmpty()) {
//            System.out.println(tempList.size());

            ArrayList<PickItem> tempResultList = new ArrayList<PickItem>();
            KnapsackProblem kp = new KnapsackProblem(tempList, sizeBudget, weightBudget);
            ArrayList<Integer> solution = kp.generateSolution();

//            System.out.println(solution);
//            System.out.println(tempList.size() == solution.size());
            for (int i = 0; i < solution.size(); i++) {
                int isPick = solution.get(i);
                if (isPick == 1) {
                    tempResultList.add(tempList.get(i));
                }
            }

            int removeCounter = 0;
            for (int i = 0; i < solution.size(); i++) {
                int isPick = solution.get(i);
                if (isPick == 1) {
                    tempList.remove(i - removeCounter);
                    removeCounter++;
                }
            }

            if (!tempResultList.isEmpty()) {

                returnMap.put("knapsack" + counter, tempResultList);

                counter++;
            }
        }
        
//        System.out.println(returnMap);

        return returnMap;
    }
    
    public String toString(){
        return "Picker " + id;
    }

//    public double getLocationWeight(String location) {
//        double value = 0.0;
//
//        for (PickItem item : pickingList) {
//            String itemLocation = item.getLocation();
//
//            if (itemLocation.equals(location)) {
//
//                String sku = item.getProductSKU();
//                Product product = ProductDAO.getProduct(sku);
//
//                if (product != null) {
////                    System.out.println("Weight:" + product.getWeight());
//                    value += product.getWeight() * item.getNoOfCarton();
//                } else {
//                    value += Setting.unknown_product_weight * item.getNoOfCarton();
//                }
//            }
//        }
//
////        value += TSP_GA.randomValue();
////        if(value <= 0)
////        System.out.println(location + "-Weight : " + value);
//        return value;
//    }
//
//    public double getLocationSize(String location) {
//        double value = 0.0;
//
//        for (PickItem item : pickingList) {
//            String itemLocation = item.getLocation();
//
//            if (itemLocation.equals(location)) {
//
//                String sku = item.getProductSKU();
//                Product product = ProductDAO.getProduct(sku);
//                if (product != null) {
////                    System.out.println("Size:" + product.getWidth() * product.getHeight()* product.getLength());
//                    value += product.getWidth() * product.getHeight() * product.getLength() * item.getNoOfCarton();
//                } else {
//                    value += Setting.unknown_product_length * Setting.unknown_product_width * Setting.unknown_product_height * item.getNoOfCarton();
//                }
//            }
//        }
//
////        value += TSP_GA.randomValue();
////        if(value <= 0)
////        System.out.println(location + "-Size : " + value);
//        return value;
//    }

    public String getId() {
        return id;
    }
}
