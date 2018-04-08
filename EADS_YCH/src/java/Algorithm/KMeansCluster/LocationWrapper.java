/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm.KMeansCluster;

/**
 *
 * @author Boon Keat
 */
public class LocationWrapper implements Clusterable {
    private double[] points;
    private DataPoint location;
    
     public LocationWrapper(DataPoint location) {
        this.location = location;
        this.points = new double[] { location.getX(), location.getY() };
    }

    public DataPoint getLocation() {
        return location;
    }

    public double[] getPoint() {
        return points;
    }
}
