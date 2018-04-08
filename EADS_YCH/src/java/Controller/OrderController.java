/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Algorithm.KMeansCluster.CentroidCluster;
import Algorithm.KMeansCluster.KMeans;
import Algorithm.KMeansCluster.LocationWrapper;
import Configuration.Setting;
import DAO.OrderDAO;
import Entity.Order;
import Entity.OrderLine;
import Entity.PickItem;
import Entity.Picker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Boon Keat
 */
public class OrderController {

    private static ArrayList<Picker> humanList = new ArrayList<Picker>();
    private static ArrayList<Picker> forkliftList = new ArrayList<Picker>();

    public static void init() {
        for (int i = 0; i < Setting.numOfPicker; i++) {
            humanList.add(new Picker());
        }

        for (int i = 0; i < Setting.numOfForkLift; i++) {
            forkliftList.add(new Picker());
        }
    }

    public static ArrayList<Picker> getHumanList() {
        return humanList;
    }

    public static ArrayList<Picker> getForkLiftList() {
        return forkliftList;
    }

    public static void assignOrder(Set<String> docNumSet) {

        Set<String> humanLocation = new HashSet<String>();
        Set<String> highLocation = new HashSet<String>();

        HashMap<String, ArrayList<OrderLine>> orderLocationList = OrderDAO.generateOrderLocationList(docNumSet);

        for (String docNum : docNumSet) {
            Order order = OrderDAO.getOrder(docNum);

//            System.out.println(order.getItems());
            ArrayList<OrderLine> humanLevel = order.getHumanLevelItem();

            for (OrderLine orderline : humanLevel) {
                humanLocation.add(orderline.getLocation());
            }

            ArrayList<OrderLine> highLevel = order.getHighLevelItem();

            for (OrderLine orderline : highLevel) {
                highLocation.add(orderline.getLocation());
            }
        }

//        System.out.println(humanLocation.size());
//        System.out.println(highLocation.size());
        if (humanLocation.size() >= Setting.numOfPicker) {
            List<CentroidCluster<LocationWrapper>> resultHumanLevel = KMeans.getCluster(humanLocation, Setting.numOfPicker);

            for (int i = 0; i < resultHumanLevel.size(); i++) {

                ArrayList<PickItem> pickingList = new ArrayList<PickItem>();
                Picker picker = humanList.get(i);

                for (LocationWrapper locationWrapper : resultHumanLevel.get(i).getPoints()) {

                    String location = locationWrapper.getLocation().getObjName();

                    if (orderLocationList.containsKey(location)) {

                        ArrayList<OrderLine> orders = orderLocationList.get(location);
                        for (OrderLine orderItem : orders) {
                            for (int n = 0; n < orderItem.getNumOfCarton(); n++) {
                                pickingList.add(new PickItem(orderItem.getProductSKU(), orderItem.getLocation(), 1));
                            }
//                            pickingList.add(new PickItem(orderItem.getProductSKU(), orderItem.getLocation(), orderItem.getNumOfCarton()));
                        }
                    }
                }

                picker.setPickingList(pickingList);
            }
        } else {

            int i = 0;

            for (String location : humanLocation) {
                ArrayList<PickItem> pickingList = new ArrayList<PickItem>();

                Picker picker = humanList.get(i);

                if (orderLocationList.containsKey(location)) {

                    ArrayList<OrderLine> orders = orderLocationList.get(location);
                    for (OrderLine orderItem : orders) {
                        for (int n = 0; n < orderItem.getNumOfCarton(); n++) {
                            pickingList.add(new PickItem(orderItem.getProductSKU(), orderItem.getLocation(), 1));
                        }
//                        pickingList.add(new PickItem(orderItem.getProductSKU(), orderItem.getLocation(), orderItem.getNumOfCarton()));
                    }
                }

                picker.setPickingList(pickingList);
                i++;
            }
        }

        if (highLocation.size() >= Setting.numOfForkLift) {

            List<CentroidCluster<LocationWrapper>> resultHighLevel = KMeans.getCluster(highLocation, Setting.numOfForkLift);

            for (int i = 0; i < resultHighLevel.size(); i++) {

                ArrayList<PickItem> pickingList = new ArrayList<PickItem>();
                Picker picker = forkliftList.get(i);

                for (LocationWrapper locationWrapper : resultHighLevel.get(i).getPoints()) {

                    String location = locationWrapper.getLocation().getObjName();

                    if (orderLocationList.containsKey(location)) {

                        ArrayList<OrderLine> orders = orderLocationList.get(location);
                        for (OrderLine orderItem : orders) {
                            for (int n = 0; n < orderItem.getNumOfCarton(); n++) {
                                pickingList.add(new PickItem(orderItem.getProductSKU(), orderItem.getLocation(), 1));
                            }
//                            pickingList.add(new PickItem(orderItem.getProductSKU(), orderItem.getLocation(), orderItem.getNumOfCarton()));
                        }
                    }
                }

                picker.setPickingList(pickingList);
            }
        } else {

            int i = 0;

            for (String location : highLocation) {
                ArrayList<PickItem> pickingList = new ArrayList<PickItem>();

                Picker picker = forkliftList.get(i);

                if (orderLocationList.containsKey(location)) {

                    ArrayList<OrderLine> orders = orderLocationList.get(location);
                    for (OrderLine orderItem : orders) {
                        for (int n = 0; n < orderItem.getNumOfCarton(); n++) {
                            pickingList.add(new PickItem(orderItem.getProductSKU(), orderItem.getLocation(), 1));
                        }
//                        pickingList.add(new PickItem(orderItem.getProductSKU(), orderItem.getLocation(), orderItem.getNumOfCarton()));
                    }
                }

                picker.setPickingList(pickingList);
                i++;
            }
        }
    }
}
