/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm.TSPGenetic;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Boon Keat
 */
public class Route {

    // Holds our tour of cities
    private ArrayList<Location> route = new ArrayList<Location>();
    // Cache
    private double fitness = 0;
    private int distance = 0;

    // Constructs a blank route
    
    public Route() {
        for (int i = 0; i < RouteManager.numberOfLocations(); i++) {
            route.add(null);
        }
    }

    public Route(ArrayList<Location> route) {
        this.route = route;
    }

    // Creates a random individual
    public void generateIndividual() {
        // Loop through all our destination location and add them to our route
        for (int locationIndex = 0; locationIndex < RouteManager.numberOfLocations(); locationIndex++) {
            setLocation(locationIndex, RouteManager.getLocation(locationIndex));
        }
        // Randomly reorder the tour
        Collections.shuffle(route);
    }

    // Gets a location from the route
    public Location getLocation(int routePosition) {
        return (Location) route.get(routePosition);
    }

    // Sets a location in a certain position within a route
    public void setLocation(int routePosition, Location location) {
        
//        System.out.println(location.size);
        
        route.set(routePosition, location);
        // If the route been altered we need to reset the fitness and distance
        fitness = 0;
        distance = 0;
    }

    // Gets the route fitness
    public double getFitness() {
        if (fitness == 0) {
            fitness = 1 / (double) getDistance();
        }
        return fitness;
    }

    // Gets the total distance of the route
    public int getDistance() {
        if (distance == 0) {
            int routeDistance = 0;
            double routeWeight = 0;
            double routeSize = 0;

            Location start = RouteManager.getStartLocation();
            Location end = RouteManager.getEndLocation();
            
            // Loop through our tour's cities
            for (int locationIndex = 0; locationIndex < routeSize(); locationIndex++) {
                // Get city we're travelling from
                Location fromLocation = getLocation(locationIndex);
                // City we're travelling to
                Location destinationLocation;
                // Check we're not on our tour's last city, if we are set our 
                // tour's final destination city to our starting city
                if (locationIndex + 1 < routeSize()) {
                    destinationLocation = getLocation(locationIndex + 1);
                } else {
                    destinationLocation = getLocation(0);
                }
                // Get the distance between the two cities

//                System.out.println(destinationLocation.size);
                
                if (RouteManager.getWeightCapacity() <= routeWeight || RouteManager.getSizeCapacity() <= routeSize) {
                    routeDistance += end.distanceTo(fromLocation);
                    routeDistance += end.distanceTo(destinationLocation);
                    routeSize = 0;
                    routeWeight = 0;
                } else {
                    routeDistance += fromLocation.distanceTo(destinationLocation);
                    routeSize += destinationLocation.size;
                    routeWeight += destinationLocation.weight;
                }
            }

            routeDistance += start.distanceTo(getLocation(0));

            routeDistance += end.distanceTo(getLocation(routeSize() - 1));

            distance = routeDistance;
        }
        return distance;
    }

    public int routeSize() {
        return route.size();
    }

    public boolean containsLocation(Location location) {
        return route.contains(location);
    }

    public ArrayList<Location> getRoute() {
        return route;
    }

    @Override
    public String toString() {
        String geneString = "|";
        for (int i = 0; i < routeSize(); i++) {
            geneString += getLocation(i) + "|";
        }
        return geneString;
    }
}
