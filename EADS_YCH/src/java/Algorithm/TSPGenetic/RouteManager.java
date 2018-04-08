/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm.TSPGenetic;

import java.util.ArrayList;

/**
 *
 * @author Boon Keat
 */
public class RouteManager {
    // Holds our location
    private static ArrayList<Location> destinationLocations = new ArrayList<Location>();
    
    private static Location start;
    private static Location end;
    
    private static double weightCapacity;
    private static double sizeCapacity;
    
    public static void setStartLocation(Location point){
        start = point;
    }
    
    public static Location getStartLocation(){
        return start;
    }
    
    public static void setEndLocation(Location point){
        end = point;
    }
    
    public static Location getEndLocation(){
        return end;
    }

    public static ArrayList<Location> getDestinationLocations() {
        return destinationLocations;
    }

    public static void setDestinationLocations(ArrayList<Location> destinationLocations) {
        RouteManager.destinationLocations = destinationLocations;
    }

    public static Location getStart() {
        return start;
    }

    public static void setStart(Location start) {
        RouteManager.start = start;
    }

    public static Location getEnd() {
        return end;
    }

    public static void setEnd(Location end) {
        RouteManager.end = end;
    }

    public static double getWeightCapacity() {
        return weightCapacity;
    }

    public static void setWeightCapacity(double weightCapacity) {
        RouteManager.weightCapacity = weightCapacity;
    }

    public static double getSizeCapacity() {
        return sizeCapacity;
    }

    public static void setSizeCapacity(double sizeCapacity) {
        RouteManager.sizeCapacity = sizeCapacity;
    }

    // Adds a destination city
    public static void addLocation(Location point) {
        
        destinationLocations.add(point);
    }
    
    // Get a city
    public static Location getLocation(int index){
        return (Location)destinationLocations.get(index);
    }
    
    // Get the number of destination cities
    public static int numberOfLocations(){
        return destinationLocations.size();
    }
    
    public static void reset(){
        destinationLocations = new ArrayList<Location>();
        start = null;
        end = null;
        weightCapacity = 0.0;
        sizeCapacity = 0.0;
    }
}
