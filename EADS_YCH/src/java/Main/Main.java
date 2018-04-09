/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Algorithm.KOpt.TwoOpt;
import Algorithm.OrderCluster.ClusteringOrderList;
import Algorithm.TSPGenetic.Location;
import Algorithm.TSPGenetic.Population;
import Algorithm.TSPGenetic.RouteManager;
import Algorithm.TSPGenetic.TSP_GA;
import CPLEX.TeamOrienteeringProblem;
import Configuration.Setting;
import Controller.OrderController;
import DAO.BatchOrderDAO;
import DAO.BypassDAO;
import DAO.LevelDAO;
import DAO.OrderDAO;
import DAO.ProductDAO;
import Entity.Order;
import Entity.PickItem;
import Entity.Picker;
import Entity.Product;
import Utility.Warehouse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Boon Keat
 */
public class Main {

    public static void main(String[] args) throws Exception {
        //load & set data
        ProductDAO.loadData(Setting.fileName);
        OrderDAO.loadData(Setting.fileName);
        BypassDAO.setBypasses("B29:B30,X29:Z30");
        LevelDAO.setHumanLevel("X:Z");
//        
//        System.out.println(Warehouse.calculateDistance("07C45M", "07C46"));
//        System.out.println(Warehouse.calculateDistance("07C46", "07C45M"));
//        
//        System.out.println("================================================");
//        
//        System.out.println(Warehouse.calculateDistance("07C46", "07C05"));
//        System.out.println(Warehouse.calculateDistance("07C05", "07C46"));
//        
//        System.out.println("================================================");
//        
//        System.out.println(Warehouse.calculateDistance("07C46", "08C05"));
//        System.out.println(Warehouse.calculateDistance("08C05", "07C46"));
//        
//        System.out.println("================================================");
//        
//        System.out.println(Warehouse.calculateDistance("07C46", "18C05"));
//        System.out.println(Warehouse.calculateDistance("18C05", "07C46"));

        //init the picker
        OrderController.init();

        //define which orders to do first
        HashMap<String, Order> orders = OrderDAO.getOrderByDate("01-DEC-17");
        System.out.println("Order list size: " + orders.size());
        
        BatchOrderDAO.setBatchOrders(ClusteringOrderList.clusterOrder(new ArrayList<Order>(orders.values())));

//        HashMap<String, Set<String>> batchPickingList = ClusteringOrderList.clusterOrder(new ArrayList<Order>(orders.values()));
        
//        System.out.println(batchPickingList);

        System.out.println("Batch size: " + BatchOrderDAO.getBatchOrders().size());

        String from = "";

//        Location startLocation = new Location(Setting.startPoint, new ArrayList<PickItem>());
//        Location endLocation = new Location(Setting.endPoint, new ArrayList<PickItem>());
        for (String batchId : BatchOrderDAO.getKeySet()) {

            System.out.println("===================" + batchId + "===================");
            
            System.out.println(BatchOrderDAO.getBatchOrder(batchId));

//            System.out.println(batchPickingList.get(batchId));
            //assign out the selected orders
            OrderController.assignOrder(BatchOrderDAO.getBatchOrder(batchId));

            ArrayList<Picker> humanList = OrderController.getHumanList();
            ArrayList<Picker> forkliftList = OrderController.getForkLiftList();

            //generate the route for each human picker
            for (Picker picker : humanList) {

                ArrayList<Location> route = null;

                if (picker.getPickingList().size() == 0) {
//                    System.out.println("EMPTY - " + picker);
                    continue;
                } else if (picker.getPickingList().size() == 1) {
                    from = "";
                    
                    ArrayList<Location> tempRoute = new ArrayList<Location>();
                    tempRoute.add(new Location(Setting.startPoint, new ArrayList<PickItem>()));

                    PickItem item1 = picker.getPickingList().get(0);
                    ArrayList<PickItem> tempList = new ArrayList<PickItem>();
                    tempList.add(item1);

                    Location loc = new Location(item1.getLocation(), tempList);

                    tempRoute.add(loc);

                    tempRoute.add(new Location(Setting.endPoint, new ArrayList<PickItem>()));

                    if (Setting.humanWeightBudget >= loc.getWeight() && Setting.humanSizeBudget >= loc.getSize()) {
                        route = tempRoute;
                    }

//                    if (route == null) {
//
//                        System.out.println(from + "Picker-" + picker.toString() + ": no feasible solution");
//
//                    } else {
//
//                        System.out.println(from + "Picker-" + picker.toString() + ": " + route);
//
//                    }
                } else if (picker.getPickingList().size() <= Setting.idealSize) {

//                    System.out.println(picker.getPickingList());
                    from = "OPL-";

//                OPL picker.getAllLocation()
//                String endLocation, ArrayList<PickItem> pickingList, double weightCapacity, double sizeCapacity
                    ArrayList<ArrayList<PickItem>> oplResult = TeamOrienteeringProblem.solveModel(Setting.startPoint, Setting.endPoint, picker.getPickingList(), Setting.humanWeightBudget, Setting.humanSizeBudget);
//                    System.out.println(oplResult.size());
                    route = TeamOrienteeringProblem.convertToLocation(oplResult, Setting.startPoint, Setting.endPoint);
                } else {

                    //GA
//                    System.out.println(picker.getAllLocation());
                    from = "GA-";

                    Population pop = TSP_GA.search(Setting.startPoint, Setting.endPoint, Setting.humanWeightBudget, Setting.humanSizeBudget, picker, Setting.populationSize, Setting.generationSize);

                    if (pop != null) {
                        ArrayList<Location> currentSolution = pop.getFittest().getRoute();
                        
                        TwoOpt twoOpt = new TwoOpt(Setting.startPoint, Setting.endPoint, currentSolution, Setting.humanWeightBudget, Setting.humanSizeBudget);
                        ArrayList<Location> localSolution = twoOpt.getSolution();
                        // get the total distance travelled
//                        double totalDistance = twoOpt.evaluationSolution(localSolution);
//
//                    System.out.println("Picker" + picker.toString() + ": " + totalDistance);

                        route = TSP_GA.getSolutionRoute(Setting.startPoint, Setting.endPoint, localSolution, picker);
                        // print out the routes
//                        System.out.println("Picker" + picker.toString() + ": " + TSP_GA.getSolutionRoute(Setting.startPoint, Setting.endPoint, localSolution, picker) + " (" + totalDistance + ")");
                    }

                    RouteManager.reset();
                }

                if (route == null) {

                    System.out.println(from + "Picker-" + picker.toString() + ": no feasible solution");

//                    ArrayList<PickItem> list = picker.getPickingList();
//
//                    for (PickItem item : list) {
//                        String SKU = item.getProductSKU();
//                        Product p = ProductDAO.getProduct(SKU);
//                        if (p != null && (p.getWeight() > Setting.humanWeightBudget || (p.getHeight() * p.getLength() * p.getWidth()) > Setting.humanSizeBudget)) {
//                            System.out.println(item);
//                        } else if (p == null && (Setting.unknown_product_weight > Setting.forkliftWeightBudget || (Setting.unknown_product_length * Setting.unknown_product_height * Setting.unknown_product_width) > Setting.forkliftSizeBudget)) {
//                            System.out.println(item);
//                        }
//                    }
//
//                    System.exit(1);

                } else {

//                    for (Location loc : route) {
//                        if (loc.getPickingList().size() > 1) {
//                            System.out.println(loc.getPickingList());
//                            System.exit(1);
//                        }
//                    }

                    System.out.println(from + "Picker-" + picker.toString() + ": " + route);
                }

            }

            //generate the route for each human picker
            for (Picker picker : forkliftList) {

                ArrayList<Location> route = null;

                if (picker.getPickingList().size() == 0) {
//                    System.out.print("EMPTY - ");
                    continue;
                } else if (picker.getPickingList().size() == 1) {
                    ArrayList<Location> tempRoute = new ArrayList<Location>();

                    tempRoute.add(new Location(Setting.startPoint, new ArrayList<PickItem>()));

                    PickItem item1 = picker.getPickingList().get(0);
                    ArrayList<PickItem> tempList = new ArrayList<PickItem>();
                    tempList.add(item1);
                    tempRoute.add(new Location(item1.getLocation(), tempList));

                    tempRoute.add(new Location(Setting.endPoint, new ArrayList<PickItem>()));

                    route = tempRoute;
                } else if (picker.getPickingList().size() <= Setting.idealSize) {
                    //OPL picker.getAllLocation()

                    from = "OPL-";

                    ArrayList<ArrayList<PickItem>> oplResult = TeamOrienteeringProblem.solveModel(Setting.startPoint, Setting.endPoint, picker.getPickingList(), Setting.forkliftWeightBudget, Setting.forkliftSizeBudget);
//                    System.out.println("ForkLift" + picker.toString() + ": ");
                    route = TeamOrienteeringProblem.convertToLocation(oplResult, Setting.startPoint, Setting.endPoint);
//                    System.out.println("");
                } else {

                    //GA
//                    System.out.println(picker.getAllLocation());
                    from = "GA-";

                    Population pop = TSP_GA.search(Setting.startPoint, Setting.endPoint, Setting.forkliftWeightBudget, Setting.forkliftSizeBudget, picker, Setting.populationSize, Setting.generationSize);

                    if (pop != null) {
                        ArrayList<Location> currentSolution = pop.getFittest().getRoute();

                        TwoOpt twoOpt = new TwoOpt(Setting.startPoint, Setting.endPoint, currentSolution, Setting.forkliftWeightBudget, Setting.forkliftSizeBudget);
                        // get the total distance travelled
                        ArrayList<Location> localSolution = twoOpt.getSolution();
                        // get the total distance travelled
//                        double totalDistance = twoOpt.evaluationSolution(localSolution);

                        route = TSP_GA.getSolutionRoute(Setting.startPoint, Setting.endPoint, localSolution, picker);
//
//System.out.println("Picker" + picker.toString() + ": " + totalDistance);

                        // print out the routes
//                        System.out.println("ForkLift" + picker.toString() + ": " + TSP_GA.getSolutionRoute(Setting.startPoint, Setting.endPoint, localSolution, picker) + " (" + totalDistance + ")");
                    }

                    RouteManager.reset();
                }
                if (route == null) {

                    System.out.println(from + "ForkLift-" + picker.toString() + ": no feasible solution");

//                    ArrayList<PickItem> list = picker.getPickingList();
//
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
//                    System.exit(1);

                } else {

                    System.out.println(from + "ForkLift-" + picker.toString() + ": " + route);
                    
//                    for (Location loc : route) {
//                        if (loc.getPickingList().size() > 1) {
//                            System.out.println(loc.getPickingList());
//                            System.exit(1);
//                        }
//                    }

                }
            }

            System.out.println("================================================");
        }
    }
}
