<%-- 
    Document   : results
    Created on : Mar 29, 2018, 9:48:48 PM
    Author     : Jeremy
--%>
<%@page import="Entity.PickItem"%>
<%@page import="Utility.Warehouse"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="Configuration.Setting"%>
<%@page import="Algorithm.TSPGenetic.Location"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%
    TreeMap<String, HashMap<String, ArrayList<Location>>> returnData = (TreeMap<String, HashMap<String, ArrayList<Location>>>)request.getAttribute("returnData");
    //out.println(returnData);
    //out.println(Setting.numOfPicker);
    //out.println(Setting.numOfForkLift);
    int totalDistance = 0;
    TreeMap<String, Double> pickerDistance = new TreeMap<>();
    ArrayList<String> batchNumber = new ArrayList<>(returnData.keySet());
    //out.println(returnData);
    //get distance travelled for each picker - each picker is a point on the line graph, mean line in between
    TreeMap<String, Integer> batchDistanceMap = new TreeMap<>();
    Iterator<Map.Entry<String, HashMap<String, ArrayList<Location>>>> it = returnData.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry<String, HashMap<String,ArrayList<Location>>>batch = it.next();
        String batchId = batch.getKey();
        HashMap<String, ArrayList<Location>> batchDetails = batch.getValue();
        int batchDistance = 0;
        for (String key : batchDetails.keySet()) {
            //get currentDistance for current picker
            Double currentPickerDistance = pickerDistance.get(key);
            if (currentPickerDistance == null || currentPickerDistance == 0.0){
                currentPickerDistance = 0.0;
            }
            
            //loop through list of locations per picker
            ArrayList<Location> locations = batchDetails.get(key);
            for (int i = 0; i < locations.size()-1; i++){
                batchDistance += Warehouse.calculateDistance(locations.get(i).getLocation(), locations.get(i+1).getLocation());
                currentPickerDistance += Warehouse.calculateDistance(locations.get(i).getLocation(), locations.get(i+1).getLocation());
            }
            pickerDistance.put(key, currentPickerDistance);
            //out.println(key + " " + currentPickerDistance);
        }
        totalDistance += batchDistance;
        batchDistanceMap.put(batchId, batchDistance);
        //out.println("Batch Id " + batchId + " has batch distance of " + batchDistance + "\n");
    }
    pageContext.setAttribute("totalDistance",totalDistance);
    pageContext.setAttribute("batchDistanceMap",batchDistanceMap);
    pageContext.setAttribute("pickerDistance",pickerDistance);
    //out.println(pickerDistance);
    //get total distance covered for each batch
    //scatter plot to show distance covered per picker for each batch
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set scope="application" var="context" value=""/>
<!DOCTYPE html>

<html>
    <link rel="stylesheet" href="summaryTable.css">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <style>
        body {font-family: Arial;}

        /* Style the tab */
        .tab {
            overflow: hidden;
            border: 1px solid #ccc;
            background-color: #f1f1f1;
        }

        /* Style the buttons inside the tab */
        .tab button {
            background-color: inherit;
            float: left;
            border: none;
            outline: none;
            cursor: pointer;
            padding: 14px 16px;
            transition: 0.3s;
            font-size: 17px;
        }

        /* Change background color of buttons on hover */
        .tab button:hover {
            background-color: #ddd;
        }

        /* Create an active/current tablink class */
        .tab button.active {
            background-color: #ccc;
        }

        /* Style the tab content */
        .tabcontent {
            display: none;
            padding: 6px 12px;
            border: 1px solid #ccc;
            border-top: none;
        }
        </style>
    </head>
    <body>
        <div class="tab">
            <button class="tablinks" onclick="openCity(event, 'Overview')">Overview</button>
            <button class="tablinks" onclick="openCity(event, 'Report')">Report</button>
            <!-- <button class="tablinks" onclick="openCity(event, 'Layout')">Layout</button> -->
            <button id="myButton" class="float-right submit-button" >Form</button>
        </div>
        
        <script>
        function openCity(evt, cityName) {
            var i, tabcontent, tablinks;
            tabcontent = document.getElementsByClassName("table-users");
            for (i = 0; i < tabcontent.length; i++) {
                tabcontent[i].style.display = "none";
            }
            tablinks = document.getElementsByClassName("tablinks");
            for (i = 0; i < tablinks.length; i++) {
                tablinks[i].className = tablinks[i].className.replace(" active", "");
            }
            document.getElementById(cityName).style.display = "block";
            evt.currentTarget.className += " active";
        }
        </script>

        <script type="text/javascript">
            document.getElementById("myButton").onclick = function () {
                location.href = "http://localhost:8084/EADS_YCH/form.jsp";
            };
        </script>
        
        <!-- @Nicholas, change in the table below -->
        <div id="Overview" class="table-users">
           <div class="header">Summary</div>

           <table cellspacing="0">
              <tr>
                 <th class="nav">Variable</th>
                 <th class="nav">Value</th>
              </tr>

              <tr>
                 <td>Total Distance</td>
                 <td>${totalDistance}</td>
              </tr>
              <c:forEach var="entry" items="${pickerDistance}">
                  <tr>
                      <td><c:out value="${entry.key}"/></td>
                      <td padding-left:5em><c:out value="${entry.value}"/></td>
                  </tr>
              </c:forEach>
              <c:forEach var="entry" items="${batchDistanceMap}">
                  <tr>
                      <td><c:out value="${entry.key}"/></td>
                      <td padding-left:5em><c:out value="${entry.value}"/></td>
                  </tr>
              </c:forEach>
              
           </table>
        </div>
    </body>
    
    <div id="Report" class="table-users">
            <%
                for(int i=0; i<batchNumber.size(); i++){
                    String batch = batchNumber.get(i);
            %>
            <header class="header">
                <%
                    out.println(batch);
                %>      
            </header>

            <section>
                <%
                    HashMap<String, ArrayList<Location>> current = returnData.get(batch);
                    ArrayList<String> PickerInfo = new ArrayList<>(current.keySet());
                    for (int j = 0; j < PickerInfo.size(); j++){
                        int count = 1;
                        String picker = PickerInfo.get(j);
                        ArrayList<Location> locationList = current.get(picker);
                        ArrayList<Location> visitedLocation = new ArrayList<>();
                %>
                <table cellspacing="0">
                <header class="nav">
                    <%
                        out.println(picker);
                    %>  
                </header>
                    <tr>
                        <td>
                            Sequence
                        </td>
                        <td>
                            Location
                        </td>
                        <td>
                            Item
                        </td>
                        <td>
                            Quantity (Cartons)
                        </td>
                    </tr>
                    <%
                        for(int k=0; k<locationList.size(); k++){
                            ArrayList<PickItem> pickList = locationList.get(k).getPickingList(); 
                            int compare = k+1;
                            int quantity = 1;
                            if (visitedLocation.size()==0 || !(visitedLocation.get(visitedLocation.size()-1).getLocation().equals(locationList.get(k).getLocation()))){
                    %>        
                    <tr>
                        <td>
                            <%
                                out.println(count++);
                            %>  
                        </td>
                        <td>
                            <%
                                out.println(locationList.get(k).getLocation());
                            %>  
                        </td>
                            <%                     
                                if (pickList.size()!=0){
                            %>
                        <td>
                                <%
                                    out.println(pickList.get(0).getProductSKU());
                                %>  
                        </td>
                                <%
                                    if(locationList.size()!= k){
                                        while (locationList.get(k).getLocation().equals(locationList.get(compare).getLocation())){
                                            quantity++;
                                            compare++;
                                        }
                                    }
                                %>                       
                        <td align="center">
                                <%
                                    out.println(quantity);
                                %>  
                        </td>
                            <%
                                } else {
                            %>
                            <td> - </td>
                            <td> - </td>
                    <%
                                }
                            }                      
                            visitedLocation.add(locationList.get(k));
                        }
                    %>
                        </tr>    
                <%
                    }
                %> 
    
                </table>
            </section>
            <% 
                }
            %>
    </div>
    
    <div id="Layout" class="table-users">
        <!-- @Nicholas, put your stuff here for the diagram -->
    </div>
</html>

