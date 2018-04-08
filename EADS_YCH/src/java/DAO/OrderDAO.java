/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Configuration.Setting;
import Entity.Order;
import Entity.OrderLine;
import Utility.ExcelAdapter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Boon Keat
 */
public class OrderDAO {

    private static HashMap<String, Order> orderList = new HashMap<String, Order>();
    private static HashMap<String, HashMap<String, Order>> orderBasedOnDate = new HashMap<String, HashMap<String, Order>>();

    public static boolean isOrderExist(String docNum) {
        return orderList.containsKey(docNum);
    }
    
    public static HashMap<String, Order> getOrderByDate(String date) {
        return orderBasedOnDate.get(date);
    }

    public static HashMap<String, Order> getAllOrder() {
        return orderList;
    }

    public static Order getOrder(String docNum) {
        return orderList.get(docNum);
    }

    public static void addOrder(Order item) {
        orderList.put(item.getDocNum(), item);
    }

    public static HashMap<String, ArrayList<OrderLine>> generateOrderLocationList(Set<String> docNums) {
        HashMap<String, ArrayList<OrderLine>> orderLocationList = new HashMap<String, ArrayList<OrderLine>>();
        for (String docNum : docNums) {
            Order order = orderList.get(docNum);
            ArrayList<OrderLine> items = order.getItems();
            for (OrderLine item : items) {
                String location = item.getLocation();
                if (orderLocationList.containsKey(location)) {
                    ArrayList<OrderLine> tempList = orderLocationList.get(location);
                    tempList.add(item);
                    orderLocationList.put(location, tempList);
                } else {
                    ArrayList<OrderLine> tempList = new ArrayList<OrderLine>();
                    tempList.add(item);
                    orderLocationList.put(location, tempList);
                }
            }
        }
        return orderLocationList;
    }

    public static void loadData(String filename) {
//        ArrayList<ArrayList<String>> dataTable = ExcelAdapter.loadData(filename, "PickList");

        ArrayList<ArrayList<String>> dataTable = ExcelAdapter.loadData(filename, "Outbound Dec 2017");

        for (ArrayList<String> row : dataTable) {
            try {
                String docNum = row.get(Setting.orderDocNumIndex);

                String productSKU = row.get(Setting.orderProductSKUIndex);
                double quantity = Double.parseDouble(row.get(Setting.orderQuantityIndex));
                double numOfCarton = Double.parseDouble(row.get(Setting.orderNumOfCartonIndex));

//                Date creationDate = getJavaDate(Double.parseDouble(row.get(4)));
                String dateTime = row.get(Setting.orderCreationDateIndex);
                
//                System.out.println(dateTime);
                
                String date = dateTime.substring(0, dateTime.indexOf(" "));

                Date creationDate = new SimpleDateFormat("dd-MMM-yy HH:mm").parse(dateTime);

                String location = row.get(Setting.orderLocationIndex);

                OrderLine tempOrderLine = new OrderLine(docNum, productSKU, quantity, numOfCarton, creationDate, location);

                if (isOrderExist(docNum)) {
                    Order tempOrder = getOrder(docNum);
                    tempOrder.addItem(tempOrderLine);
                    addOrder(tempOrder);
                } else {
                    ArrayList<OrderLine> items = new ArrayList<OrderLine>();
                    items.add(tempOrderLine);
                    Order tempOrder = new Order(items, docNum);
                    addOrder(tempOrder);
                }

                if (orderBasedOnDate.containsKey(date)) {
                    HashMap<String, Order> orderMap = orderBasedOnDate.get(date);
                    if (orderMap.containsKey(docNum)) {
                        Order tempOrder = orderMap.get(docNum);
                        tempOrder.addItem(tempOrderLine);
                        orderMap.put(docNum, tempOrder);
                    } else {
                        ArrayList<OrderLine> items = new ArrayList<OrderLine>();
                        items.add(tempOrderLine);
                        Order tempOrder = new Order(items, docNum);
                        orderMap.put(docNum, tempOrder);
                    }

                    orderBasedOnDate.put(date, orderMap);
                } else {
                    HashMap<String, Order> orderMap = new HashMap<String, Order>();

                    ArrayList<OrderLine> items = new ArrayList<OrderLine>();
                    items.add(tempOrderLine);
                    Order tempOrder = new Order(items, docNum);
                    orderMap.put(docNum, tempOrder);

                    orderBasedOnDate.put(date, orderMap);
                }

            } catch (ParseException ex) {
                Logger.getLogger(OrderDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
