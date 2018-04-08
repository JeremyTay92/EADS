/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Configuration.Setting;
import Entity.Bypass;
import Utility.Warehouse;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Boon Keat
 */
public class BypassDAO {

    private static ArrayList<Bypass> bypasses = new ArrayList<Bypass>();

    public static ArrayList<Bypass> getBypasses() {
        return bypasses;
    }

    public static void addBypass(Bypass bypass) {
        bypasses.add(bypass);
    }

    public static boolean isBypass(String location) {
        for (Bypass bypass : bypasses) {
            char level = location.charAt(Setting.levelStringPosition);
            String rack = location.substring(Setting.rackStringPosition);

            if (level == bypass.getLevel() && rack.equals(bypass.getRack())) {
                return true;
            }
        }

        return false;
    }

    public static String getStartBypass() {
        double smallestValue = Double.MAX_VALUE;
        String position = "";
        
//        System.out.println(bypasses);
        
        for (Bypass bypass : bypasses) {
            String rack = bypass.getRack();

            double value = Double.parseDouble(rack.substring(0, Setting.rackNumStringLength));
            
            if (rack.contains("M")) {
                value += 0.5;
            }

            if (value <= smallestValue) {
                
//                System.out.println(rack + ": " + value);
                
                smallestValue = value;
                position = Warehouse.getRackName((int)Math.floor(value));

                if (rack.contains("M")) {
                    position += "M";
                }
            }
        }

//        System.out.println(position);
        return position;
    }

    public static String getEndBypass() {
        double largestValue = Double.MIN_VALUE;
        String position = "";
        
//        System.out.println(bypasses);
        
        for (Bypass bypass : bypasses) {
            String rack = bypass.getRack();

            double value = Integer.parseInt(rack.substring(0, Setting.rackNumStringLength));
            
            if (rack.contains("M")) {
                value += 0.5;
            }

            if (value >= largestValue) {
                largestValue = value;
                position = Warehouse.getRackName((int)Math.floor(value));

                if (rack.contains("M")) {
                    position += "M";
                }

            }
        }

        return position;
    }

    public static void setBypasses(String range) {
        String[] rangeArr = range.split(",");
        for (int index = 0; index < rangeArr.length; index++) {
            String[] letters = rangeArr[index].split(":");
            String starting = letters[0];

            char startingLevel = starting.charAt(Setting.levelRangePosition);

//            System.out.println(startingLevel);
            String startingRack = starting.substring(Setting.rackRangePosition);

//            System.out.println(startingRack);
            String ending = letters[letters.length - 1];

            char endingLevel = ending.charAt(Setting.levelRangePosition);
            String endingRack = ending.substring(Setting.rackRangePosition);

            for (int i = startingLevel; i <= endingLevel; i++) {

//                System.out.println(startingRack);
                int startRackNum = Integer.parseInt(startingRack.substring(0, Setting.rackNumRangeLength));
                int endRackNum = Integer.parseInt(endingRack.substring(0, Setting.rackNumRangeLength));

                if (startingRack.contains("M")) {
                    addBypass(new Bypass(startingRack, (char) i));
                    startRackNum += 1;
                }

                if (endingRack.contains("M")) {
                    addBypass(new Bypass(endingRack, (char) i));
                }

                for (int x = startRackNum; x <= endRackNum; x++) {

                    String tempRack = Warehouse.getRackName(x);

                    addBypass(new Bypass(tempRack, (char) i));

                    if (x % 2 == 1) {
                        addBypass(new Bypass(tempRack + "M", (char) i));
                    }
                }
            }
        }

        Collections.sort(bypasses);

//        System.out.println(bypasses);
    }
}
