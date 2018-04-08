/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm.AStar;

import Entity.PickItem;
import Utility.Warehouse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *
 * @author Boon Keat
 */
public class AstarSearchAlgo {

    //h scores is the stright-line distance from the current city to Bucharest
    public static void main(String[] args) {

        Set<String> locations = new HashSet<String>();
        locations.add("16Y53");
        locations.add("18E38");
        locations.add("15Y51");
        locations.add("14Y45");
        locations.add("10X35");

        HashMap<String, HashMap<String, Double>> adjacencyMatrics = generateAdjacencyMatrix("18D08", "15X17", locations);
        
        System.out.println(adjacencyMatrics);

        List<Node> path = search("18D08", "15X17", locations, adjacencyMatrics);

        System.out.println("Path: " + path);

    }

    //h scores is the stright-line distance from the current city to Bucharest
    public static List<Node> search(String startLocation, String goalLocation, Set<String> locations, HashMap<String, HashMap<String, Double>> adjacencyMatrics) {

        //initialize the graph base on the Romania map
        Node start = new Node(startLocation, 0);
        Node goal = new Node(goalLocation, 374);
        
        HashMap<String, Node> nodeMap = new HashMap<String, Node>();
        for (String location : locations) {
            Node tempNode = new Node(location, 0);
            nodeMap.put(location, tempNode);
        }
        
        Set<String> otherLocationsFromStart = adjacencyMatrics.get(startLocation).keySet();

        Edge[] edgesStart = new Edge[otherLocationsFromStart.size()];
        int i1 = 0;
        for (String otherLocation : otherLocationsFromStart) {
            Node otherNode = nodeMap.get(otherLocation);
            edgesStart[i1] = new Edge(otherNode, adjacencyMatrics.get(startLocation).getOrDefault(otherLocation, Double.NaN));
            i1++;
        }
        start.adjacencies = edgesStart;

        Set<String> otherLocationsToGoal = adjacencyMatrics.get(goalLocation).keySet();

        Edge[] edgesEnd = new Edge[otherLocationsToGoal.size()];
        int i2 = 0;
        for (String otherLocation : otherLocationsToGoal) {
            Node otherNode = nodeMap.get(otherLocation);
            edgesEnd[i2] = new Edge(otherNode, adjacencyMatrics.get(goalLocation).getOrDefault(otherLocation, Double.NaN));
            i2++;
        }
        goal.adjacencies = edgesEnd;
        
        //initialize the edges
        for (String location : locations) {
            Node node = nodeMap.get(location);

            if (!adjacencyMatrics.containsKey(location)) {
                continue;
            }

            Set<String> otherLocations = adjacencyMatrics.get(location).keySet();

            Edge[] edges = new Edge[otherLocations.size()];
            int i = 0;
            for (String otherLocation : otherLocations) {
                Node otherNode = nodeMap.get(otherLocation);
                edges[i] = new Edge(otherNode, adjacencyMatrics.get(location).getOrDefault(otherLocation, Double.NaN));
                i++;
            }
            node.adjacencies = edges;
        }

        AstarSearch(start, goal);

        List<Node> path = getPath(goal);

        path.add(goal);
        path.add(0, start);
        
        return path;

    }

    public static List<Node> getPath(Node target) {
        List<Node> path = new ArrayList<Node>();

        for (Node node = target; node != null; node = node.parent) {
            path.add(node);
        }

        Collections.reverse(path);

        return path;
    }

    public static void AstarSearch(Node source, Node goal) {

        Set<Node> explored = new HashSet<Node>();

        PriorityQueue<Node> queue = new PriorityQueue<Node>(20,
                new Comparator<Node>() {
            //override compare method
            public int compare(Node i, Node j) {
                if (i.f_scores > j.f_scores) {
                    return 1;
                } else if (i.f_scores < j.f_scores) {
                    return -1;
                } else {
                    return 0;
                }
            }

        });

        //cost from start
        source.g_scores = 0;

        queue.add(source);

        boolean found = false;

        while ((!queue.isEmpty()) && (!found)) {

            //the node in having the lowest f_score value
            Node current = queue.poll();

            explored.add(current);

            //goal found
            if (current.value.equals(goal.value)) {
                found = true;
            }

            System.out.println("===============" + current + "================");
            for(Edge e : current.adjacencies){
                System.out.println(e.target + " : " + e.cost);
            }
            System.out.println("===============================================");

            //check every child of current node
            for (Edge e : current.adjacencies) {
                Node child = e.target;
                double cost = e.cost;
                double temp_g_scores = current.g_scores + cost;
                double temp_f_scores = temp_g_scores + child.h_scores;


                /*if child node has been evaluated and 
                                the newer f_score is higher, skip*/
                if ((explored.contains(child))
                        && (temp_f_scores >= child.f_scores)) {
                    continue;
                } /*else if child node is not in queue or 
                                newer f_score is lower*/ else if ((!queue.contains(child))
                        || (temp_f_scores < child.f_scores)) {

                    child.parent = current;
                    child.g_scores = temp_g_scores;
                    child.f_scores = temp_f_scores;

                    if (queue.contains(child)) {
                        queue.remove(child);
                    }

                    queue.add(child);

                }

            }

        }

    }

    public static HashMap<String, HashMap<String, Double>> generateAdjacencyMatrix(String startLocation, String dropOffLocation, Set<String> locations) {

        HashMap<String, HashMap<String, Double>> matrices = new HashMap<String, HashMap<String, Double>>();

        HashMap<String, Double> startMatrixMap = new HashMap<String, Double>();

        for (String location : locations) {
            startMatrixMap.put(location, randomValue());
        }

        matrices.put(startLocation, startMatrixMap);

        HashMap<String, Double> dropOffMatrixMap = new HashMap<String, Double>();

        for (String location : locations) {
            dropOffMatrixMap.put(location, randomValue());
        }

        matrices.put(dropOffLocation, dropOffMatrixMap);

        for (String location : locations) {
            HashMap<String, Double> matrix = new HashMap<String, Double>();

            for (String otherLocation : locations) {
                if (!location.equals(otherLocation)) {

                    matrix.put(otherLocation, randomValue());
                }
            }

            matrices.put(location, matrix);
        }

        return matrices;
    }

    public static double randomValue() {
        return 2 + Math.random() * 10;
    }
}
