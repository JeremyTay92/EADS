/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm.KMeansCluster;

/**
 *
 * @author Boon Keat
 */

public class DataPoint {
    private double mX,mY;
    private String mObjName;;

    public DataPoint(double x, double y, String name) {
        mX = x;
        mY = y;
        mObjName = name;
    }

    public double getX() {
        return mX;
    }

    public double getY() {
        return mY;
    }

    public String getObjName() {
        return mObjName;
    }

    public String toString(){
        return mY + "," + mX;
    }
}
