/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuration;

/**
 *
 * @author Boon Keat
 */
public class Setting {
    public static String directory = "C:\\Users\\Jeremy\\Downloads\\EADS (IS421)\\Proj\\Project\\EADS_YCH";
    public static String fileName = directory + "\\test\\SMU SIS EADS Data v3.xlsx";
    public static String startPoint = "05X07";
    public static String endPoint = "18X07";
    
    //OPL & GA
    public static int idealSize = 10;
    
    //missing data
    public static double unknown_product_weight = 5.3;
    public static double unknown_product_length = 40.0;
    public static double unknown_product_width = 38.0;
    public static double unknown_product_height = 20.0;
    
    //Order Cluster
    public static double similarityThreshold = 0.3;
    public static int clusterGenerationSize = 10;
    public static int clusterPopulationSize = 2;
    
    //Knapsack
    public static int knapsackGenerationSize = 1000;
    public static int knapsackPopulationSize = 10;
    public static double knapsack_crossover = 0.5;
    public static double knapsack_mutation = 0.03;
    
    //TSP
    public static int generationSize = 1000;
    public static int populationSize = 10;
    
    //K-Means Cluster
    public static int iterSize = 1000;
    public static int seedingSize = 10;
    
    //metre per second
    public static double humanTravellingSpeed = 1.71;
    public static double forkliftTravellingSpeed = (25) * 1000 / 3600;
    
    //second
    public static double pickSpeed = 60;
    
    //cm^3
    public static double humanSizeBudget = 6_062_400.0;
    //kg
    public static double forkliftSizeBudget = 12_124_800;
    
    public static double humanWeightBudget = 288.6;
    public static double forkliftWeightBudget = 577.2;
    
    //metre
    public static double rackUnit = 0.9;
    public static double laneUnit = 1.15;
    public static double spaceBetweenLane = 3.4;
    
    public static String startingRack = "07";
    public static String endingRack = "56";
    
    public static int numOfPicker = 4;
    public static int numOfForkLift = 2;
    
    public static int laneStringPostion = 0;
    public static int laneStringLength = 2;
    public static int levelStringPosition = laneStringPostion + laneStringLength;
    public static int levelStringLength = 1;
    public static int rackStringPosition = levelStringPosition + levelStringLength;
    public static int rackNumStringLength = 2;
    
    public static int levelRangePosition = 0;
    public static int levelRangeLength = 1;
    public static int rackRangePosition = levelRangePosition + levelRangeLength;
    public static int rackNumRangeLength = 2;
    
    public static int productSKUIndex = 0;
    public static int productLengthIndex = 1;
    public static int productWidthIndex = 2;
    public static int productHeightIndex = 3;
    public static int productWeightIndex = 4;
    
    public static int orderDocNumIndex = 0;
    public static int orderProductSKUIndex = 1;
    public static int orderQuantityIndex = 2;
    public static int orderNumOfCartonIndex = 3;
    public static int orderCreationDateIndex = 4;
    public static int orderLocationIndex = 5;
    
    public static void setStartPoint(String startPoint) {
        Setting.startPoint = startPoint;
    }

    public static void setEndPoint(String endPoint) {
        Setting.endPoint = endPoint;
    }

    public static void setSimilarityThreshold(double similarityThreshold) {
        Setting.similarityThreshold = similarityThreshold;
    }

    public static void setHumanTravellingSpeed(double humanTravellingSpeed) {
        Setting.humanTravellingSpeed = humanTravellingSpeed;
    }

    public static void setForkliftTravellingSpeed(double forkliftTravellingSpeed) {
        Setting.forkliftTravellingSpeed = forkliftTravellingSpeed;
    }

    public static void setNumOfPicker(int numOfPicker) {
        Setting.numOfPicker = numOfPicker;
    }

    public static void setNumOfForkLift(int numOfForkLift) {
        Setting.numOfForkLift = numOfForkLift;
    }

    public static void setHumanWeightBudget(double humanWeightBudget) {
        Setting.humanWeightBudget = humanWeightBudget;
    }

    public static void setForkliftWeightBudget(double forkliftWeightBudget) {
        Setting.forkliftWeightBudget = forkliftWeightBudget;
    }
}
