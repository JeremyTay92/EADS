/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm.KMeansCluster;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Boon Keat
 */
public class KMeansCluster {

    public static List<CentroidCluster<LocationWrapper>> generateCluster(int clusterSize, int iter, List<DataPoint> dataPoints) {
        List<DataPoint> locations = dataPoints;
        
//        System.out.println(locations);
        
        List<LocationWrapper> clusterInput = new ArrayList<LocationWrapper>(locations.size());
        for (DataPoint location : locations) {
            clusterInput.add(new LocationWrapper(location));
        }

        // initialize a new clustering algorithm. 
        // we use KMeans++ with 10 clusters and 10000 iterations maximum.
        // we did not specify a distance measure; the default (euclidean distance) is used.
        KMeansPlusPlusClusterer<LocationWrapper> clusterer = new KMeansPlusPlusClusterer<LocationWrapper>(clusterSize, iter);
        List<CentroidCluster<LocationWrapper>> clusterResults = clusterer.cluster(clusterInput);

        // output the clusters
        
        return clusterResults;
    }
}
