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
public class Population {

    Route[] routes;

    // Construct a population
    public Population(int populationSize, boolean initialise) {
        routes = new Route[populationSize];
        // If we need to initialise a population of routes do so
        if (initialise) {
            // Loop and create individuals
            for (int i = 0; i < populationSize(); i++) {
                Route newRoute = new Route();
                newRoute.generateIndividual();
                saveRoute(i, newRoute);
            }
        }
    }

    // Saves a route
    public void saveRoute(int index, Route route) {
        routes[index] = route;
    }

    // Gets a route from population
    public Route getRoute(int index) {
        return routes[index];
    }

    // Gets the best tour in the population
    public Route getFittest() {
        Route fittest = routes[0];
        // Loop through individuals to find fittest
        for (int i = 1; i < populationSize(); i++) {
            if (fittest.getFitness() <= getRoute(i).getFitness()) {
                fittest = getRoute(i);
            }
        }
        
        return fittest;
        
//        ArrayList<Location> result = fittest.getRoute();
//
//        Location start = RouteManager.getStartLocation();
//
//        Location end = RouteManager.getEndLocation();
//        
//        result.add(0, start);
//        result.add(end);
//
//        return new Route(result);
    }

    // Gets population size
    public int populationSize() {
        return routes.length;
    }
}
