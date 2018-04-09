/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Algorithm.TSPGenetic.Population;
import Algorithm.TSPGenetic.RouteManager;
import Algorithm.TSPGenetic.TSP_GA;
import Configuration.Setting;
import DAO.LevelDAO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Boon Keat
 */
public class Order {

    private ArrayList<OrderLine> items;
    private String docNum;

    public Order(ArrayList<OrderLine> items, String docNum) {
        this.items = items;
        this.docNum = docNum;
    }

    public ArrayList<OrderLine> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderLine> items) {
        this.items = items;
    }

    public void addItem(OrderLine item) {
        items.add(item);
    }

    public String getDocNum() {
        return docNum;
    }

    public void setDocNum(String docNum) {
        this.docNum = docNum;
    }

    public ArrayList<OrderLine> getHighLevelItem() {
        ArrayList<OrderLine> itemList = new ArrayList<OrderLine>();

        for (OrderLine item : items) {
            String location = item.getLocation();
            if (!LevelDAO.isHuman(location)) {
                itemList.add(item);
            }
        }

        return itemList;
    }

    public ArrayList<OrderLine> getHumanLevelItem() {
        ArrayList<OrderLine> itemList = new ArrayList<OrderLine>();

        for (OrderLine item : items) {
            String location = item.getLocation();
            if (LevelDAO.isHuman(location)) {
                itemList.add(item);
            }
        }

        return itemList;
    }

    public boolean isSimilar(Order anotherOrder) {
//        System.out.println("this order: " + items.size());
//        System.out.println("another order: " + anotherOrder.getItems().size());

        double threshold = 1.0 - Setting.similarityThreshold;

        Picker tempPicker = new Picker();

        ArrayList<OrderLine> humanLevelItem = getHumanLevelItem();

//        Set<String> locations = new HashSet<String>();
        ArrayList<PickItem> pickingList = new ArrayList<PickItem>();

        for (OrderLine item : humanLevelItem) {
//            locations.add(item.getLocation());
//            PickItem tempItem = new PickItem(item.getProductSKU(), item.getLocation(), item.getNumOfCarton());
//            pickingList.add(tempItem);

            for (int n = 0; n < item.getNumOfCarton(); n++) {
                pickingList.add(new PickItem(item.getProductSKU(), item.getLocation(), 1));
            }
        }

        tempPicker.setPickingList(pickingList);

        Population pop = TSP_GA.search(Setting.startPoint, Setting.endPoint, Integer.MAX_VALUE, Integer.MAX_VALUE, tempPicker, Setting.clusterPopulationSize, Setting.clusterGenerationSize);
        double humanLevelDist = 0.0;
        if (pop != null) {
            humanLevelDist = pop.getFittest().getDistance();
        }

        RouteManager.reset();

        ArrayList<OrderLine> otherHumanLevelItem = anotherOrder.getHumanLevelItem();

        for (OrderLine item : otherHumanLevelItem) {
//            locations.add(item.getLocation());
//            PickItem tempItem = new PickItem(item.getProductSKU(), item.getLocation(), item.getNumOfCarton());
//            pickingList.add(tempItem);

            for (int n = 0; n < item.getNumOfCarton(); n++) {
                pickingList.add(new PickItem(item.getProductSKU(), item.getLocation(), 1));
            }
        }

        tempPicker.setPickingList(pickingList);

        pop = TSP_GA.search(Setting.startPoint, Setting.endPoint, Double.MAX_VALUE, Double.MAX_VALUE, tempPicker, Setting.clusterPopulationSize, Setting.clusterGenerationSize);
        double newHumanLevelDist = 0.0;
        if (pop != null) {
            newHumanLevelDist = pop.getFittest().getDistance();
        }

        RouteManager.reset();

        if (humanLevelDist > 0 && humanLevelDist * (1 + threshold) < newHumanLevelDist) {
//            System.out.println("new dist:" +newHumanLevelDist);
//            System.out.println("threshold:" + humanLevelDist * (1 + threshold));
            return false;
        }

        tempPicker.clearPickingList();
//        locations = new HashSet<String>();
        pickingList = new ArrayList<PickItem>();

        ArrayList<OrderLine> forkLevelItem = getHighLevelItem();

        for (OrderLine item : forkLevelItem) {
//            locations.add(item.getLocation());
//            PickItem tempItem = new PickItem(item.getProductSKU(), item.getLocation(), item.getNumOfCarton());
//            pickingList.add(tempItem);

            for (int n = 0; n < item.getNumOfCarton(); n++) {
                pickingList.add(new PickItem(item.getProductSKU(), item.getLocation(), 1));
            }
        }

        tempPicker.setPickingList(pickingList);

        pop = TSP_GA.search(Setting.startPoint, Setting.endPoint, Double.MAX_VALUE, Double.MAX_VALUE, tempPicker, Setting.clusterPopulationSize, Setting.clusterGenerationSize);
        double forkLevelDist = 0.0;
        if (pop != null) {
            forkLevelDist = pop.getFittest().getDistance();
        }

        RouteManager.reset();

        ArrayList<OrderLine> otherForkLevelItem = anotherOrder.getHighLevelItem();

        for (OrderLine item : otherForkLevelItem) {
//            locations.add(item.getLocation());
//            PickItem tempItem = new PickItem(item.getProductSKU(), item.getLocation(), item.getNumOfCarton());
//            pickingList.add(tempItem);

            for (int n = 0; n < item.getNumOfCarton(); n++) {
                pickingList.add(new PickItem(item.getProductSKU(), item.getLocation(), 1));
            }
        }

        tempPicker.setPickingList(pickingList);

        pop = TSP_GA.search(Setting.startPoint, Setting.endPoint, Integer.MAX_VALUE, Integer.MAX_VALUE, tempPicker, Setting.clusterPopulationSize, Setting.clusterGenerationSize);
        double newForkLevelDist = 0.0;
        if (pop != null) {
            newForkLevelDist = pop.getFittest().getDistance();
        }

        RouteManager.reset();

        tempPicker.clearPickingList();
//        locations = new HashSet<String>();
        pickingList = new ArrayList<PickItem>();

        if (forkLevelDist > 0 && forkLevelDist * (1 + threshold) < newForkLevelDist) {
            return false;
        }

        return true;
    }
}
