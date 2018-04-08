/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import Algorithm.TSPGenetic.Location;
import Configuration.Setting;
import Entity.PickItem;
import Main.RunAlgo;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nicholastps
 */
@WebServlet(name = "InputServlet", urlPatterns = {"/InputServlet"})
public class InputServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            //System.out.println("Success");
            /* TODO output your page here. You may use following sample code. */
            String workersNum = request.getParameter("workersNum");
            String forkliftsNum = request.getParameter("forkliftsNum");
            String workerSpeed = request.getParameter("workerSpeed");
            String forkliftSpeed = request.getParameter("forkliftSpeed");
            String workerCapacity = request.getParameter("workerCapacity");
            String forkliftCapacity = request.getParameter("forkliftCapacity");
            String similarityThreshold = request.getParameter("similarityThreshold");
            String startingPoint = request.getParameter("startingPoint");
            String endingPoint = request.getParameter("endingPoint");
            
            if (!(workersNum == null || workersNum.equals(""))){
                Setting.setNumOfPicker(Integer.parseInt(workersNum)) ;
            }
            
            if (!(forkliftsNum == null || forkliftsNum.equals(""))){
                Setting.setNumOfForkLift(Integer.parseInt(forkliftsNum)) ;
                //System.out.println("Check if modify:" + Setting.numOfForkLift);
            }

            if (!(workerSpeed != null || workerSpeed.equals(""))){
                Setting.setHumanTravellingSpeed(Integer.parseInt(workerSpeed)) ;
            }
            
            if (!(forkliftSpeed != null || forkliftSpeed.equals(""))){
                Setting.setForkliftTravellingSpeed(Integer.parseInt(forkliftSpeed)) ;
            }
            
            if (!(workerCapacity != null || workerCapacity.equals(""))){
                Setting.setHumanWeightBudget(Integer.parseInt(workerCapacity)) ;
            }
            
            if (!(forkliftCapacity != null || forkliftCapacity.equals(""))){
                Setting.setForkliftWeightBudget(Integer.parseInt(forkliftCapacity)) ;
            }
            
            if (!(similarityThreshold != null || similarityThreshold.equals(""))){
                Setting.setSimilarityThreshold(Integer.parseInt(similarityThreshold)) ;
            }
            
            if (!(startingPoint != null || startingPoint.equals(""))){
                Setting.setStartPoint(startingPoint);
            }
            
            if (!(endingPoint != null || endingPoint.equals(""))){
                Setting.setEndPoint(endingPoint);
            }
            TreeMap<String,HashMap<String, ArrayList<Location>>> returnData = new TreeMap<>();
            
            try {
                returnData = RunAlgo.main(new String[0]);
                out.println("Main completed");
            } catch (Exception e){
                e.printStackTrace();
            }
            
            request.setAttribute("returnData", returnData);
            RequestDispatcher view = request.getRequestDispatcher("results.jsp");
            view.forward(request, response);

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
