/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CPLEX;

import Algorithm.TSPGenetic.Location;
import Configuration.Setting;
import DAO.BypassDAO;
import DAO.LevelDAO;
import DAO.ProductDAO;
import Entity.PickItem;
import Entity.Product;
import Utility.Warehouse;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.util.ArrayList;

/**
 *
 * @author Boon Keat
 */
public class TeamOrienteeringProblem {

    public static void main(String[] args) throws Exception {
        ArrayList<PickItem> pickingList = new ArrayList<PickItem>();

        BypassDAO.setBypasses("B29:B30,X29:Z30");
        LevelDAO.setHumanLevel("X:Z");

//        String productSKU, String location, double noOfCarton
//        pickingList.add(new PickItem("212343", "07X15M", 1));
//        pickingList.add(new PickItem("212343", "14X12", 1));
//        pickingList.add(new PickItem("212343", "12X27", 1));
//        pickingList.add(new PickItem("214352", "13X13M", 1));
//        pickingList.add(new PickItem("212343", "07X15M", 1));
//        pickingList.add(new PickItem("212343", "14X12", 1));
        pickingList.add(new PickItem("212343", "12X27", 1));
        pickingList.add(new PickItem("214352", "13X13M", 1));
        pickingList.add(new PickItem("212343", "07X15M", 1));
        pickingList.add(new PickItem("212343", "14X12", 1));
        pickingList.add(new PickItem("212343", "12X27", 1));
        pickingList.add(new PickItem("214352", "13X13M", 1));
        pickingList.add(new PickItem("212343", "07X15M", 1));
//        pickingList.add(new PickItem("212343", "14X12", 1));
        pickingList.add(new PickItem("212343", "12X27", 1));
//        pickingList.add(new PickItem("214352", "13X13M", 1));
        pickingList.add(new PickItem("212343", "07X15M", 1));
//        pickingList.add(new PickItem("212343", "14X12", 1));
//        pickingList.add(new PickItem("212343", "12X27", 1));
//        pickingList.add(new PickItem("214352", "13X13M", 1));
//        pickingList.add(new PickItem("212343", "07X15M", 1));
//        pickingList.add(new PickItem("212343", "14X12", 1));
//        pickingList.add(new PickItem("212343", "12X27", 1));
//        pickingList.add(new PickItem("214352", "13X13M", 1));

        System.out.println("Pickinglist size: " + pickingList.size());

        ArrayList<ArrayList<PickItem>> result = solveModel(Setting.startPoint, Setting.endPoint, pickingList, Setting.humanWeightBudget, Setting.humanSizeBudget);
        System.out.println(convertToLocation(result, Setting.startPoint, Setting.endPoint));
    }
    
    public static ArrayList<Location> convertToLocation(ArrayList<ArrayList<PickItem>> pickingList, String startLocation, String endLocation) {
 ArrayList<Location> resultList = new ArrayList<Location>();
        int counter = 0;
        String startPoint = startLocation;
        
        if(pickingList == null){
            return null;
        }
        
        for (ArrayList<PickItem> list : pickingList) {

            if (list.isEmpty()) {
                continue;
            }

            if (counter == 0) {
                resultList.add(new Location(startPoint, new ArrayList<PickItem>()));
                counter++;
            }

            for (int i = 0; i < list.size(); i++) {
                    PickItem item1 = list.get(i);
                    ArrayList<PickItem> tempList = new ArrayList<PickItem>();
                    tempList.add(item1);
                    resultList.add(new Location(item1.getLocation(), tempList));
            }

            resultList.add(new Location(endLocation, new ArrayList<PickItem>()));
        }
        
        return resultList;
    }

    public static void displayResult(ArrayList<ArrayList<PickItem>> pickingList, String startLocation, String endLocation) {
        double totalDistance = 0.0;

        int counter = 0;
        String startPoint = startLocation;
        for (ArrayList<PickItem> list : pickingList) {

            if (list.isEmpty()) {
                continue;
            }

            if (counter == 0) {
                System.out.print(startPoint + " ");
                counter++;
            }

            totalDistance += Warehouse.calculateDistance(startPoint, list.get(0).getLocation());

            for (int i = 0; i < list.size(); i++) {
                if (i + 1 < list.size()) {
                    PickItem item1 = list.get(i);
                    PickItem item2 = list.get(i + 1);
                    System.out.print(item1.getLocation() + " ");
                    totalDistance += Warehouse.calculateDistance(item1.getLocation(), item2.getLocation());
                }
            }
            
            int lastIndex = list.size() - 1;
            PickItem lastItem = list.get(lastIndex);

            System.out.print(lastItem.getLocation() + " " + endLocation + " ");

            totalDistance += Warehouse.calculateDistance(endLocation, lastItem.getLocation());
        }

        System.out.println("(" + totalDistance + ")");
    }

    public static ArrayList<ArrayList<PickItem>> solveModel(String startLocation, String endLocation, ArrayList<PickItem> pickingList, double weightCapacity, double sizeCapacity) throws Exception {

        ArrayList<ArrayList<PickItem>> solutionPickingMap = new ArrayList<ArrayList<PickItem>>();

        try {
            // Define an empty model
            IloCplex model = new IloCplex();
            model.setOut(null);

            ArrayList<Double> weightList = new ArrayList<Double>();
            ArrayList<Double> sizeList = new ArrayList<Double>();

            for (PickItem item : pickingList) {
                String sku = item.getProductSKU();

                Product product = ProductDAO.getProduct(sku);

                double valueWeight = 0.0;
                double valueSize = 0.0;

                if (product != null) {
                    valueWeight = product.getWeight() * item.getNoOfCarton();
                    valueSize = product.getWidth() * product.getHeight() * product.getLength() * item.getNoOfCarton();
                } else {
                    valueWeight = Setting.unknown_product_weight;
                    valueSize = Setting.unknown_product_length * Setting.unknown_product_width * Setting.unknown_product_height * item.getNoOfCarton();
                }

                weightList.add(valueWeight);
                sizeList.add(valueSize);
            }

            int numOfPath = pickingList.size();
            int numOfItem = pickingList.size();
            int numOfNonItem = 1;
            int numOfNode = pickingList.size() + numOfNonItem;

            // Define the binary variables
            IloNumVar[][][] x = new IloNumVar[numOfNode][numOfNode][numOfPath];
            for (int i = 0; i < numOfNode; i++) {
                for (int j = 0; j < numOfNode; j++) {
                    for (int p = 0; p < numOfPath; p++) {
                        x[i][j][p] = model.boolVar();
                    }
                }
            }

            IloNumVar[][] y = new IloNumVar[numOfItem][numOfPath];
            for (int i = 0; i < numOfItem; i++) {
                for (int p = 0; p < numOfPath; p++) {
                    y[i][p] = model.boolVar();
                }
            }

            IloNumVar[][] u = new IloNumVar[numOfNode][numOfPath];
            for (int i = 0; i < numOfNode; i++) {
                for (int p = 0; p < numOfPath; p++) {
                    u[i][p] = model.intVar(0, numOfNode);
                }
            }

            // Define the objective function
            IloLinearNumExpr obj = model.linearNumExpr();

            PickItem item1 = null;
            PickItem item2 = null;

            for (int i = 0; i < numOfNode; i++) {
                if (i >= numOfNonItem) {
                    item1 = pickingList.get(i - numOfNonItem);
                }

                for (int j = 0; j < numOfNode; j++) {

                    if (j >= numOfNonItem) {
                        item2 = pickingList.get(j - numOfNonItem);
                    }

                    for (int p = 0; p < numOfPath; p++) {
                        String location1 = "";
                        String location2 = "";

                        if (i == 0) {
                            if (p == 0) {
                                location1 = startLocation;
                            } else {
                                location1 = endLocation;
                            }
                        } else {
                            location1 = item1.getLocation();
                        }

                        if (j == 0) {
                            location2 = endLocation;
                        } else {
                            location2 = item2.getLocation();
                        }

                        obj.addTerm(Warehouse.calculateDistance(location1, location2), x[i][j][p]);
                    }
                }
            }
            model.addMinimize(obj);

            // Define the constraints
            for (int p = 0; p < numOfPath; p++) {
                IloLinearNumExpr constraintEnd = model.linearNumExpr();

                for (int i = numOfNonItem; i < numOfNode; i++) {
                    constraintEnd.addTerm(1, x[i][0][p]);
                }

                IloLinearNumExpr constraintStart = model.linearNumExpr();

                for (int j = numOfNonItem; j < numOfNode; j++) {
                    constraintStart.addTerm(1, x[0][j][p]);
                }

                model.addEq(constraintEnd, constraintStart);

                IloLinearNumExpr constraintTotal = model.linearNumExpr();

                for (int i = numOfNonItem; i < numOfNode; i++) {
                    for (int j = numOfNonItem; j < numOfNode; j++) {
                        constraintTotal.addTerm(1, x[i][j][p]);
                    }
                }

                model.addLe(constraintStart, constraintTotal);
                model.addGe(model.prod(constraintStart, numOfItem * numOfItem), constraintTotal);
            }

            for (int i = 0; i < numOfItem; i++) {
                IloLinearNumExpr constraintVisitAll = model.linearNumExpr();
                for (int p = 0; p < numOfPath; p++) {
                    constraintVisitAll.addTerm(1, y[i][p]);
                }
                model.addEq(constraintVisitAll, 1);
            }

            for (int p = 0; p < numOfPath; p++) {
                for (int j = numOfNonItem; j < numOfNode; j++) {

                    IloLinearNumExpr constraintConnectivityIn = model.linearNumExpr();
                    IloLinearNumExpr constraintConnectivityOut = model.linearNumExpr();
                    IloLinearNumExpr constraintConnectivityVisit = model.linearNumExpr();

                    for (int i = 0; i < numOfNode; i++) {
                        constraintConnectivityIn.addTerm(1, x[j][i][p]);
                    }

                    for (int i = 0; i < numOfNode; i++) {
                        constraintConnectivityOut.addTerm(1, x[i][j][p]);
                    }

                    constraintConnectivityVisit.addTerm(1, y[j - numOfNonItem][p]);

                    IloLinearNumExpr constraintConnectivitySameIn = model.linearNumExpr();

                    constraintConnectivitySameIn.addTerm(1, x[j][j][p]);
                    model.addEq(constraintConnectivitySameIn, 0);

                    model.addEq(constraintConnectivityIn, constraintConnectivityOut);
                    model.addEq(constraintConnectivityIn, constraintConnectivityVisit);
                }
            }

            for (int p = 0; p < numOfPath; p++) {
                IloLinearNumExpr constraintWeight = model.linearNumExpr();
                for (int i = 0; i < numOfNode; i++) {
                    for (int j = numOfNonItem; j < numOfNode; j++) {
                        constraintWeight.addTerm(weightList.get(j - numOfNonItem), x[i][j][p]);
                    }
                }
                model.addLe(constraintWeight, weightCapacity);
            }

            for (int p = 0; p < numOfPath; p++) {
                IloLinearNumExpr constraintSize = model.linearNumExpr();
                for (int i = 0; i < numOfNode; i++) {
                    for (int j = numOfNonItem; j < numOfNode; j++) {
                        constraintSize.addTerm(sizeList.get(j - numOfNonItem), x[i][j][p]);
                    }
                }
                model.addLe(constraintSize, sizeCapacity);
            }

            for (int p = 0; p < numOfPath; p++) {
                IloLinearNumExpr constraintSubtour1 = model.linearNumExpr();

                constraintSubtour1.addTerm(1, u[0][p]);

                IloLinearNumExpr constraintTotal = model.linearNumExpr();

                for (int i = numOfNonItem; i < numOfNode; i++) {
                    constraintTotal.addTerm(1, y[i - numOfNonItem][p]);
                }

                model.addLe(constraintSubtour1, 1);

                model.addLe(constraintSubtour1, constraintTotal);

                model.addGe(model.prod(constraintSubtour1, numOfNode), constraintTotal);
            }

            for (int p = 0; p < numOfPath; p++) {
                for (int i = numOfNonItem; i < numOfNode; i++) {
                    IloLinearNumExpr constraintSubtour1 = model.linearNumExpr();
                    for (int j = 0; j < numOfNode; j++) {
                        constraintSubtour1.addTerm(1, x[j][i][p]);
                    }
                    IloLinearNumExpr constraintSubtour2 = model.linearNumExpr();
                    constraintSubtour2.addTerm(1, u[i][p]);

                    IloLinearNumExpr constraintTotal = model.linearNumExpr();
                    constraintTotal.addTerm(2, y[i - numOfNonItem][p]);

                    model.addLe(constraintSubtour2, model.prod(constraintSubtour1, numOfNode));

                    model.addGe(constraintSubtour2, constraintTotal);
                }

            }

            for (int p = 0; p < numOfPath; p++) {
                for (int i = numOfNonItem; i < numOfNode; i++) {
                    IloLinearNumExpr constraintSubtour3 = model.linearNumExpr();
                    IloLinearNumExpr constraintSubtour3IsVisit = model.linearNumExpr();

                    constraintSubtour3.addTerm(1, u[i][p]);

                    for (int j = 0; j < numOfItem; j++) {
                        constraintSubtour3IsVisit.addTerm(1, y[j][p]);
                    }

                    model.addLe(constraintSubtour3, model.sum(constraintSubtour3IsVisit, 1));
                }

            }

            for (int p = 0; p < numOfPath; p++) {
                for (int i = numOfNonItem; i < numOfNode; i++) {
                    for (int j = numOfNonItem; j < numOfNode; j++) {
                        IloNumExpr lhs = model.sum(model.sum(1, u[i][p]), model.prod(-1, u[j][p]));
                        IloNumExpr rhs = model.prod(numOfItem, model.sum(1, model.prod(-1, x[i][j][p])));

                        model.addLe(lhs, rhs);
                    }
                }
            }

            IloLinearNumExpr constraintStartPath = model.linearNumExpr();
            constraintStartPath.addTerm(1, u[0][0]);

            model.addEq(constraintStartPath, 1);

            boolean isSolved = model.solve();
            if (isSolved) {
//                double objValue = model.getObjValue();
//                System.out.println("obj_val = " + objValue);
//
//                System.out.println("========================================");
//                for (int p = 0; p < numOfPath; p++) {
//                    for (int i = 0; i < numOfItem; i++) {
//                        System.out.print("y[" + (i + 1) + "][" + (p + 1) + "] = " + model.getValue(y[i][p]) + "   ");
//                    }
//                    System.out.println("");
//                }
//                System.out.println("========================================");
//
//                System.out.println("========================================");
//                for (int p = 0; p < numOfPath; p++) {
//                    for (int i = 0; i < numOfNode; i++) {
//                        for (int j = 0; j < numOfNode; j++) {
//                            System.out.print("x[" + (i + 1) + "][" + (j + 1) + "][" + (p + 1) + "] = " + model.getValue(x[i][j][p]) + "   ");
//                        }
//                        System.out.println(" ");
//                    }
//                }
//                System.out.println("========================================");

//                System.out.println("========================================");
                for (int p = 0; p < numOfPath; p++) {
//                    for (int i = 0; i < 1; i++) {

                    int[] position = new int[numOfItem];

                    ArrayList<PickItem> solutionPickingList = new ArrayList<PickItem>();
                    for (int i = numOfNonItem; i < numOfNode; i++) {

                        if (model.getValue(u[i][p]) > 0.5) {
                            
                            position[(int) Math.round(model.getValue(u[i][p])) - 2] = i;

//                            System.out.print("u[" + (i + 1) + "][" + (p + 1) + "] = " + Math.round(model.getValue(u[i][p]) - 2) + "   ");
//                            
                        }
                    }

//                    System.out.println("");
                    for (int j = 0; j < position.length; j++) {
                        if (position[j] != 0) {
//                            System.out.print(position[j] + " ");
                            solutionPickingList.add(pickingList.get(position[j] - numOfNonItem));
                        }
                    }
                    if (!solutionPickingList.isEmpty()) {
                        solutionPickingMap.add(solutionPickingList);
                    }

//                    System.out.println("");
                }

//                System.out.println("========================================");
            } else {
                solutionPickingMap = null;
            }

            model.end();
        } catch (IloException ex) {
            System.out.println(ex);
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            return solutionPickingMap;
        }
    }
}
