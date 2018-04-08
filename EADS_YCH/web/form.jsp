<%-- 
    Document   : form
    Created on : Mar 29, 2018, 8:14:24 PM
    Author     : Jeremy
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
    <head>
        <link rel='stylesheet' href='template.css'>
    </head>
    <body>
        
        <div class="form-style-5">
            <form id="InputServlet" method="post" action="InputServlet">
        <fieldset>
            
        <legend><span class="number">1</span> Main Input fields </legend>
        <label for="workersNum">Number of workers: </label>
        <input type="number" name="workersNum" placeholder=" ">
        
        <label for="forkliftsNum">Number of fork lifts: </label>
        <input type="number" name="forkliftsNum" placeholder=" ">
        
        <legend><span class="number">2</span> Optional settings </legend>
        <label for="workerSpeed"> Human traveling speed (m/s): </label>
        <input type="number" name="workerSpeed" placeholder=" 1.71 ">
        
        <label for="forkliftSpeed">Forklift traveling speed (m/s): </label>
        <input type="number" name="forkliftSpeed" placeholder=" 6.94 ">
        
        <label for="workerCapacity"> Human carrying volume (cm3): </label>
        <input type="number" name="workerCapacity" placeholder=" 30 ">
        
        <label for="forkliftCapacity">Forklift carrying weight (kg): </label>
        <input type="number" name="forkliftCapacity" placeholder=" 500 ">
        
        <label for="similarityThreshold">Similarity Threshold (decimal %):</label>
        <input type="number" name="similarityThreshold" placeholder=" 0.3 ">
        
        <label for="startingPoint">Starting point: </label>
        <input type="text" name="startingPoint" placeholder=" 05X07 ">
        
        <label for="endingPoint">Ending point:</label>
        <input type="text" name="endingPoint" placeholder=" 18X07 ">
        
        </fieldset>
        <input type="submit" value="Apply"/>
        </form>
        </div>
        
    </body>
</html>

