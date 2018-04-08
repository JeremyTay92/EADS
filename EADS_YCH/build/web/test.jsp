<%-- 
    Document   : test
    Created on : Apr 8, 2018, 11:32:12 PM
    Author     : Jeremy
--%>

<%@page import="Utility.Warehouse"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            out.println("TEST 1: " + Warehouse.calculateDistance("18C46", "07C46"));
            
            out.println("TEST 1.5: " + Warehouse.calculateDistance("07C46", "18C46"));
            
            out.println("TEST 2: " + Warehouse.calculateDistance("17C44", "18C46"));
            
            out.println("TEST 3: " + Warehouse.calculateDistance("18C46", "17C44"));
        %>
    </body>
</html>
