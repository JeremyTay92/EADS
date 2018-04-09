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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Boon Keat
 */
public class TSP_GA {

//    public static void main(String[] args) {
//        Set<String> locationSet = new HashSet<String>();
//        
//        ArrayList<String> locations = Data.getLocationList();
//        
//        while(locationSet.size() < 3){
//            int index = new Random().nextInt(locations.size());
//            locationSet.add(locations.get(index));
//        }
//        
//        Picker picker = new Picker();
//
//        String start = "18D08";
//        String end = "15X17";
//
//        Population pop = search(start, end, locationSet, 40, 40, picker, Setting.populationSize, Setting.generationSize);
//
//        // Print final results
//        System.out.println("Finished");
//        System.out.println("Final distance: " + pop.getFittest().getDistance());
//        System.out.println("Solution:");
//
//        System.out.println(getSolutionRoute(start, end, pop, picker));
//    }
    public static ArrayList<Location> getSolutionRoute(String start, String goal, ArrayList<Location> localSolution, Picker picker) {

        Location startLocation = new Location(start, new ArrayList<PickItem>());
        Location endLocation = new Location(goal, new ArrayList<PickItem>());

        ArrayList<Location> currentSolution = localSolution;

        ArrayList<Location> solution = new ArrayList<Location>();

        double routeWeight = 0;
        double routeSize = 0;

        for (Location point : currentSolution) {

            routeSize += point.size;
            routeWeight += point.weight;

//            System.out.println(point.size);
//            System.out.println(point.weight);
//            System.out.println(point.location + "-" + point.size);
//            System.out.println(RouteManager.getWeightCapacity());
//            System.out.println(RouteManager.getWeightCapacity() <= routeWeight);
//            System.out.println(RouteManager.getSizeCapacity() <= routeSize);
//            System.out.println("weight" + routeWeight);
//            System.out.println("size" + routeSize);
            if (RouteManager.getWeightCapacity() <= routeWeight || RouteManager.getSizeCapacity() <= routeSize) {
                routeSize = 0;
                routeWeight = 0;

                solution.add(endLocation);

                if (RouteManager.getWeightCapacity() < point.weight || RouteManager.getSizeCapacity() < point.size) {

//                    ArrayList<PickItem> list = point.getPickingList();
//                    for (PickItem item : list) {
//                        String SKU = item.getProductSKU();
//                        Product p = ProductDAO.getProduct(SKU);
//                        if (p != null && (p.getWeight() > Setting.forkliftWeightBudget || (p.getHeight() * p.getLength() * p.getWidth()) > Setting.forkliftSizeBudget)) {
//                            System.out.println(item);
//                        } else if (p == null && (Setting.unknown_product_weight > Setting.forkliftWeightBudget || (Setting.unknown_product_length * Setting.unknown_product_height * Setting.unknown_product_width) > Setting.forkliftSizeBudget)) {
//                            System.out.println(item);
//                        }
//                    }
//
//                    System.out.println("pw " + point.weight);
//                    System.out.println("cw " + RouteManager.getWeightCapacity());
//
//                    System.out.println("ps " + point.size);
//                    System.out.println("cs " + RouteManager.getSizeCapacity());
                    return null;
                }

                solution.add(point);
            } else {

                solution.add(point);
            }

//            solution.add(point);
        }

        solution.add(0, startLocation);
        solution.add(endLocation);

        return solution;
    }

    public static Population search(String start, String goal, double weightCapacity, double sizeCapacity, Picker picker, int populationSize, int generationSize) {

//        HashMap<String, Object> result = new HashMap<String, Object>();
//        if (locations.isEmpty()) {
//            return null;
//        }

        // Create and add our cities
//        RouteManager rm = new RouteManager();
        RouteManager.setStartLocation(new Location(start, new ArrayList<PickItem>()));
        RouteManager.setEndLocation(new Location(goal, new ArrayList<PickItem>()));
        RouteManager.setSizeCapacity(sizeCapacity);
        RouteManager.setWeightCapacity(weightCapacity);

        ArrayList<PickItem> list = picker.getPickingList();
        
        if(list.isEmpty()){
            return null;
        }

        for (PickItem item : list) {
            ArrayList<PickItem> tempList = new ArrayList<PickItem>();
            tempList.add(item);
            Location point = new Location(item.getLocation(), tempList);
            RouteManager.addLocation(point);
        }

//        for (String location : locations) {
//            HashMap<String, ArrayList<PickItem>> map = picker.knapsackItem(location);
//
//            for (String key : map.keySet()) {
//
//                Location point = new Location(location, map.get(key));
//
////            System.out.println(point.size);
//                RouteManager.addLocation(point);
//            }
//        }
        // Initialize population
        Population pop = new Population(populationSize, true);
//        System.out.println("Initial distance: " + pop.getFittest().getDistance());

        // Evolve population for 1000 generations
        pop = GA.evolvePopulation(pop);
        for (int i = 0; i < generationSize; i++) {
            pop = GA.evolvePopulation(pop);
        }

        return pop;
    }

    public static double randomValue() {
        return 5 + Math.random() * 20;
    }
}
