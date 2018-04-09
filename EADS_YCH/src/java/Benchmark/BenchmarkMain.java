/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Benchmark;

import Algorithm.KOpt.TwoOpt;
import Algorithm.TSPGenetic.Location;
import Algorithm.TSPGenetic.Population;
import Algorithm.TSPGenetic.Route;
import Algorithm.TSPGenetic.TSP_GA;
import CPLEX.TeamOrienteeringProblem;
import Configuration.Setting;
import DAO.BypassDAO;
import DAO.LevelDAO;
import Entity.PickItem;
import Entity.Picker;
import Utility.Warehouse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Jeremy
 */
public class BenchmarkMain {

    public static void main(String[] args) throws Exception {
        ArrayList<PickItem> pickingList = new ArrayList<PickItem>();

        BypassDAO.setBypasses("B29:B30,X29:Z30");
        LevelDAO.setHumanLevel("X:Z");

//        String productSKU, String location, double noOfCarton
//        pickingList.add(new PickItem("212343", "07X15M", 1));
        pickingList.add(new PickItem("212343", "14X12", 1));
        pickingList.add(new PickItem("212343", "12X27", 1));
        pickingList.add(new PickItem("214352", "13X13M", 1));
        pickingList.add(new PickItem("212343", "07X15M", 1));
        pickingList.add(new PickItem("212343", "14X12", 1));
        pickingList.add(new PickItem("212343", "12X27", 1));
        pickingList.add(new PickItem("214352", "13X13M", 1));
        pickingList.add(new PickItem("212343", "07X15M", 1));
//        pickingList.add(new PickItem("212343", "14X12", 1));
//        pickingList.add(new PickItem("212343", "12X27", 1));
        pickingList.add(new PickItem("214352", "13X13M", 1));
        pickingList.add(new PickItem("212343", "07X15M", 1));
//        pickingList.add(new PickItem("212343", "14X12", 1));
        pickingList.add(new PickItem("212343", "12X27", 1));
        pickingList.add(new PickItem("214352", "13X13M", 1));
        pickingList.add(new PickItem("212343", "07X15M", 1));
        pickingList.add(new PickItem("212343", "14X12", 1));
        pickingList.add(new PickItem("212343", "12X27", 1));
        pickingList.add(new PickItem("214352", "13X13M", 1));
        pickingList.add(new PickItem("212343", "07X15M", 1));
        pickingList.add(new PickItem("212343", "14X12", 1));
        pickingList.add(new PickItem("212343", "12X27", 1));
//        pickingList.add(new PickItem("214352", "13X13M", 1));

        System.out.println("Pickinglist size: " + pickingList.size());

        long startTime = System.nanoTime();

        opl(pickingList);

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("OPL Timing: " + totalTime / 1000000000);

        startTime = System.nanoTime();

        ga(pickingList);

        endTime = System.nanoTime();
        totalTime = endTime - startTime;
        System.out.println("GA Timing: " + totalTime / 1000000000);
    }

    public static void opl(ArrayList<PickItem> pickingList) throws Exception {
        ArrayList<ArrayList<PickItem>> result = TeamOrienteeringProblem.solveModel(Setting.startPoint, Setting.endPoint, pickingList, Setting.humanWeightBudget, Setting.humanSizeBudget);
        System.out.println(TeamOrienteeringProblem.convertToLocation(result, Setting.startPoint, Setting.endPoint));
        //System.out.println(calculateDistance(TeamOrienteeringProblem.convertToLocation(result, Setting.startPoint, Setting.endPoint)));
    }

    public static void ga(ArrayList<PickItem> pickingList) {
        Picker picker = new Picker();
        picker.setPickingList(pickingList);

        Population pop = TSP_GA.search(Setting.startPoint, Setting.endPoint, Setting.forkliftWeightBudget, Setting.forkliftSizeBudget, picker, Setting.populationSize, Setting.generationSize);

        if (pop != null) {
            ArrayList<Location> currentSolution = pop.getFittest().getRoute();

            TwoOpt twoOpt = new TwoOpt(Setting.startPoint, Setting.endPoint, currentSolution, Setting.forkliftWeightBudget, Setting.forkliftSizeBudget);
            // get the total distance travelled
            ArrayList<Location> localSolution = twoOpt.getSolution();
            // get the total distance travelled
//                        double totalDistance = twoOpt.evaluationSolution(localSolution);

            System.out.println(TSP_GA.getSolutionRoute(Setting.startPoint, Setting.endPoint, localSolution, picker));
            //System.out.println(calculateDistance(TSP_GA.getSolutionRoute(Setting.startPoint, Setting.endPoint, localSolution, picker)));
        }
    }

    public static double calculateDistance(ArrayList<Location> list) {
        double distance = 0;
            for (int i = 0; i < list.size()-1; i++){
                distance += Warehouse.calculateDistance(list.get(i).getLocation(), list.get(i+1).getLocation());
            }
        return distance;
    }
}
