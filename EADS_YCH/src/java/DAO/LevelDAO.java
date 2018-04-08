/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Configuration.Setting;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Boon Keat
 */
public class LevelDAO {

    private static Set<Character> humanLevels = new HashSet<Character>();

    public static Set<Character> getHumanLevels() {
        return humanLevels;
    }

    public static void addLevel(Character character) {
        humanLevels.add(character);
    }

    public static boolean isHuman(String location) {

        Character currentLevel = location.charAt(Setting.levelStringPosition);

        for (Character level : humanLevels) {

            if (level.charValue() == currentLevel.charValue()) {
                return true;
            }
        }

        return false;
    }
    
    public static void setHumanLevel(String range){
        String[] letters = range.split(":");
        for(int i = letters[0].charAt(Setting.levelRangePosition); i <= letters[letters.length-1].charAt(Setting.levelRangePosition); i++){
            addLevel((char)i);
        }
    }
}
