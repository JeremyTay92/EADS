/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm.KOpt;

import Algorithm.TSPGenetic.Location;
import Entity.PickItem;
import Entity.Picker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Boon Keat
 */
public class TwoOpt {

    private Location startLocation;
    private Location endLocation;
    private ArrayList<Location> bestSolution;
    private double weightBudget;
    private double sizeBudget;

    public TwoOpt(String startPoint, String endPoint, ArrayList<Location> currentSolution, double weightBudget, double sizeBudget) {
        startLocation = new Location(startPoint, new ArrayList<PickItem>());
        endLocation = new Location(endPoint, new ArrayList<PickItem>());

        this.weightBudget = weightBudget;
        this.sizeBudget = sizeBudget;

        bestSolution = new ArrayList<Location>(currentSolution);
    }
    
    public ArrayList<Location> getSolution(){
        int iterNum = bestSolution.size()^2;
        for(int i = 0; i < iterNum; i++){
            swap();
        }
        
        return bestSolution;
    }
    
    public void swap(){
         ArrayList<Location> tempSolution = new ArrayList<Location>(bestSolution);
         double bestDist = evaluationSolution(bestSolution);
         Random r = new Random();
         
         int min = 0;
         int max = tempSolution.size();
         
         int i = r.nextInt(max - min) + min;
         int j = r.nextInt(max - min) + min;
         
         if(i == j){
             return;
         }
         
         Collections.swap(tempSolution, i, j);
         
         double newDist = evaluationSolution(tempSolution);
         
         if(newDist < bestDist){
             bestSolution = tempSolution;
         }
    }

    public double evaluationSolution(ArrayList<Location> tempSolution) {

        int routeDistance = 0;
        double routeWeight = 0;
        double routeSize = 0;

        for (int locationIndex = 0; locationIndex < tempSolution.size(); locationIndex++) {
            // Get city we're travelling from
            Location fromLocation = tempSolution.get(locationIndex);
            // City we're travelling to
            Location destinationLocation;
            // Check we're not on our tour's last city, if we are set our 
            // tour's final destination city to our starting city
            if (locationIndex + 1 < tempSolution.size()) {
                destinationLocation = tempSolution.get(locationIndex + 1);
            } else {
                destinationLocation = tempSolution.get(0);
            }
            // Get the distance between the two cities

//                System.out.println(destinationLocation.size);
            if (weightBudget <= routeWeight || sizeBudget <= routeSize) {
                routeDistance += endLocation.distanceTo(fromLocation);
                routeDistance += endLocation.distanceTo(destinationLocation);
                routeSize = 0;
                routeWeight = 0;
            } else {
                routeDistance += fromLocation.distanceTo(destinationLocation);
                routeSize += destinationLocation.getSize();
                routeWeight += destinationLocation.getWeight();
            }
        }

        routeDistance += startLocation.distanceTo(tempSolution.get(0));

        routeDistance += endLocation.distanceTo(tempSolution.get(tempSolution.size() - 1));

        return routeDistance;
    }

}
