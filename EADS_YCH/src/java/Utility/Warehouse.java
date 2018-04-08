/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import Configuration.Setting;
import DAO.BypassDAO;

/**
 *
 * @author Boon Keat
 */
public class Warehouse {

    private static int roundUp(double d) {
        return (d > (int) d) ? (int) d + 1 : (int) d;
    }

    public static double calculateDistance(String firstLocation, String secondLocation) {
        String firstLane = firstLocation.substring(Setting.laneStringPostion, Setting.laneStringPostion + Setting.laneStringLength);
        String firstRack = firstLocation.substring(Setting.rackStringPosition);

        String secondLane = secondLocation.substring(Setting.laneStringPostion, Setting.laneStringPostion + Setting.laneStringLength);
        String secondRack = secondLocation.substring(Setting.rackStringPosition);

        int distanceRackUnit = 0;
        int distanceLaneUnit = 0;
        int distanceLaneSpaceUnit = 0;

//        System.out.println(firstRack);
//        System.out.println(secondRack);
        if (firstLane.equals(secondLane)) {
            distanceRackUnit = calculateRackUnit(firstRack, secondRack);
        } else if (isLaneOpposite(firstLocation, secondLocation)) {
            distanceLaneSpaceUnit++;
            distanceRackUnit = calculateRackUnit(firstRack, secondRack);
        } else {
            String transitPositionRack = getPassby(firstLocation, secondLocation).substring(Setting.rackStringPosition);
            
//            System.out.println(transitPositionRack);
            
            distanceRackUnit += calculateRackUnit(firstRack, transitPositionRack);
            
            distanceRackUnit += calculateRackUnit(secondRack, transitPositionRack);

            int firstLaneNum = Integer.parseInt(firstLane);
            int secondLaneNum = Integer.parseInt(secondLane);

            distanceLaneUnit = Math.abs(secondLaneNum - firstLaneNum);
            distanceLaneSpaceUnit = roundUp(Math.abs(secondLaneNum - firstLaneNum) / 2.0);
        }

        return distanceRackUnit * Setting.rackUnit + distanceLaneUnit * Setting.laneUnit + distanceLaneSpaceUnit * Setting.spaceBetweenLane;
    }

    public static int calculateRackUnit(String firstRack, String secondRack) {
        int distanceRackUnit = 0;

        int firstRackNum = 0;
        if (firstRack.length() > 2) {
            firstRackNum = Integer.parseInt(firstRack.substring(0, Setting.rackNumRangeLength));
        } else {
            firstRackNum = Integer.parseInt(firstRack);
        }

        int secondRackNum = 0;
        if (secondRack.length() > 2) {
            secondRackNum = Integer.parseInt(secondRack.substring(0, Setting.rackNumRangeLength));
        } else {
            secondRackNum = Integer.parseInt(secondRack);
        }

        distanceRackUnit = Math.abs(secondRackNum - firstRackNum);

        distanceRackUnit += roundUp(distanceRackUnit / 2.0);

        if (!firstRack.contains("M") || !secondRack.contains("M")) {

            if (firstRack.contains("M") && firstRackNum < secondRackNum) {
                distanceRackUnit--;
            } else if (firstRack.contains("M") && firstRackNum > secondRackNum) {
                if (secondRackNum % 2 == 1) {
                    distanceRackUnit++;
                }
            } else if (secondRack.contains("M") && firstRackNum < secondRackNum) {
                if (secondRackNum % 2 == 1) {
                    distanceRackUnit++;
                }
            } else if (secondRack.contains("M") && firstRackNum > secondRackNum) {
                distanceRackUnit--;
            }
        }

        return distanceRackUnit;
    }

    public static String getPassby(String start, String end) {

        String firstRack = start.substring(Setting.rackStringPosition);
        String secondRack = end.substring(Setting.rackStringPosition);

        int first = Integer.parseInt(firstRack.substring(0, Setting.rackNumRangeLength));
        int second = Integer.parseInt(secondRack.substring(0, Setting.rackNumRangeLength));

        if (first > second) {
            int temp = first;
            first = second;
            second = temp;
        }

        String passby = "";

        if (firstRack.equals(secondRack)) {
            passby = getNearestPassby(start);
        } else if (passPassby(first, second)) {
            
            String laneWithLevel = start.substring(Setting.laneStringPostion, Setting.laneStringPostion + Setting.laneStringLength) + "X";
            
            passby = laneWithLevel + getPassbyAlong(Integer.parseInt(firstRack.substring(0, Setting.rackNumRangeLength)), Integer.parseInt(secondRack.substring(0, Setting.rackNumRangeLength)));
        } else {
            passby = getNearestPassby(start);
        }

        return passby;
    }

    public static String getPassbyAlong(int firstRack, int secondRack) {
        String endBypassRack = BypassDAO.getEndBypass();

        String startBypassRack = BypassDAO.getStartBypass();

        if (firstRack > secondRack) {
            return endBypassRack;
        } else {
            return startBypassRack;
        }
    }

    public static boolean passPassby(int firstRack, int secondRack) {
        String endBypassRack = BypassDAO.getEndBypass();
        int end = Integer.parseInt(endBypassRack.substring(0, Setting.rackNumRangeLength));

        String startBypassRack = BypassDAO.getStartBypass();
        int start = Integer.parseInt(startBypassRack.substring(0, Setting.rackNumRangeLength));

        for (int i = firstRack; i <= secondRack; i++) {
            if (i == start) {
                return true;
            }

            if (i == end) {
                return true;
            }
        }

        return false;
    }

    public static String getNearestPassby(String location) {

        String laneWithLevel = location.substring(Setting.laneStringPostion, Setting.laneStringPostion + Setting.laneStringLength) + "X";

        String currentRack = location.substring(Setting.rackStringPosition);

        String startBypassRack = BypassDAO.getStartBypass();

//        System.out.println(laneWithLevel + currentRack);
//        System.out.println(laneWithLevel + startBypassRack);
        double distanceToStartBypass = calculateDistance(laneWithLevel + currentRack, laneWithLevel + startBypassRack);

        String nearestPoint = laneWithLevel + startBypassRack;
        double nearestDistance = distanceToStartBypass;

        String endBypassRack = BypassDAO.getEndBypass();
        double distanceToEndBypass = calculateDistance(laneWithLevel + currentRack, laneWithLevel + endBypassRack);

        if (distanceToEndBypass < nearestDistance) {
            nearestPoint = laneWithLevel + endBypassRack;
            nearestDistance = distanceToEndBypass;
        }

        String startingRack = Setting.startingRack;
        double distanceToStart = calculateDistance(laneWithLevel + currentRack, laneWithLevel + startingRack);

        if (distanceToStart < nearestDistance) {
            nearestPoint = laneWithLevel + startingRack;
            nearestDistance = distanceToStart;
        }

        String endingRack = Setting.endingRack;
        double distanceToEnd = calculateDistance(laneWithLevel + currentRack, laneWithLevel + endingRack);

        if (distanceToEnd < nearestDistance) {
            nearestPoint = laneWithLevel + endingRack;
            nearestDistance = distanceToEnd;
        }

        return nearestPoint;
    }

    public static boolean isLaneOpposite(String firstLocation, String secondLocation) {
        int firstLane = Integer.parseInt(firstLocation.substring(Setting.laneStringPostion, Setting.laneStringPostion + Setting.laneStringLength));
        int secondLane = Integer.parseInt(secondLocation.substring(Setting.laneStringPostion, Setting.laneStringPostion + Setting.laneStringLength));
        int smallerNum = firstLane;
        int biggerNum = secondLane;

        if (smallerNum > biggerNum) {
            int tempNum = biggerNum;
            biggerNum = smallerNum;
            smallerNum = tempNum;
        }

        return smallerNum % 2 == 1 && smallerNum + 1 == biggerNum;
    }

    public static boolean isRackBeside(String firstRack, String secondRack) {
        if (firstRack.contains("M")) {
            try {
                int firstNumber = Integer.parseInt(firstRack.substring(Setting.rackNumRangeLength));
                int secondNumber = Integer.parseInt(secondRack);

                if (firstNumber == secondNumber || firstNumber == secondNumber - 1) {
                    return true;
                } else {
                    return false;
                }
            } catch (NumberFormatException ex) {
                return false;
            }
        } else {
            int firstNumber = Integer.parseInt(firstRack);
            try {
                int secondNumber = Integer.parseInt(secondRack);

                if (firstNumber % 2 == 0 && (firstNumber == secondNumber - 1)) {
                    return true;
                }

                if (firstNumber % 2 == 1 && (firstNumber == secondNumber + 1 || firstNumber == secondNumber)) {
                    return true;
                }

                return false;
            } catch (NumberFormatException ex) {
                int secondNumber = Integer.parseInt(secondRack.substring(Setting.rackNumStringLength));

                if (firstNumber == secondNumber + 1) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    public static String getRackName(int number) {
        return String.format("%02d", number);
    }
}
