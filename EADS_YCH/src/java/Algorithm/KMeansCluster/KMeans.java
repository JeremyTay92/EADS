/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm.KMeansCluster;

import Configuration.Data;
import Configuration.Setting;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Boon Keat
 */
public class KMeans {

    public static void main(String[] args) {
        Set<String> locations = Data.getLocationSet();

        ArrayList<DataPoint> lowLevelPoints = new ArrayList<DataPoint>();
        for (String location : locations) {
            int x = Integer.parseInt(location.substring(Setting.laneStringPostion, Setting.laneStringPostion + Setting.laneStringLength));
            int y = Integer.parseInt(location.substring(Setting.rackStringPosition, Setting.rackStringPosition + Setting.rackNumStringLength));
            lowLevelPoints.add(new DataPoint(x, y, location));
        }

        List<CentroidCluster<LocationWrapper>> clusterResults = KMeansCluster.generateCluster(Setting.numOfPicker, Setting.iterSize, lowLevelPoints);

        System.out.println("Total Locations: " + locations.size());

        for (int i = 0; i < clusterResults.size(); i++) {

            System.out.println("Cluster " + i);
//            int count = 1;
//            for (LocationWrapper locationWrapper : clusterResults.get(i).getPoints()) {
//                System.out.println(count++ + ": " + locationWrapper.getLocation().getObjName());
//            }
            System.out.println("Total Points within the cluster: " + clusterResults.get(i).getPoints().size());
        }
    }

    public static List<CentroidCluster<LocationWrapper>> getCluster(Set<String> Locations, int clusterSize) {
        
        ArrayList<DataPoint> points = new ArrayList<DataPoint>();
        
//        System.out.println(Locations);
        
        for (String location : Locations) {
            int x = Integer.parseInt(location.substring(Setting.laneStringPostion, Setting.laneStringPostion + Setting.laneStringLength));
            int y = Integer.parseInt(location.substring(Setting.rackStringPosition, Setting.rackStringPosition + Setting.rackNumStringLength));
            points.add(new DataPoint(x, y, location));
        }

        return KMeansCluster.generateCluster(clusterSize, Setting.iterSize, points);
    }
}
