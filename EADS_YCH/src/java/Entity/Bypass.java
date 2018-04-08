/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

/**
 *
 * @author Boon Keat
 */
public class Bypass implements Comparable<Bypass>{
    private String rack;
    private char level;

    public Bypass(String rack, char level) {
        this.rack = rack;
        this.level = level;
    }

    public String getRack() {
        return rack;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    public char getLevel() {
        return level;
    }

    public void setLevel(char level) {
        this.level = level;
    }
    
    public String toString(){
        return level+rack;
    }

    @Override
    public int compareTo(Bypass o) {
        return toString().compareTo(o.toString());
    }
}
