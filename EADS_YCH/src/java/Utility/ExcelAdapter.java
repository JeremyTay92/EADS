/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Boon Keat
 */
public class ExcelAdapter {

    private static ArrayList<ArrayList<String>> records;

    public static ArrayList<ArrayList<String>> loadData(String filename, String sheetname) {

        try {

            FileInputStream excelFile = new FileInputStream(new File(filename));

            Workbook workbook = new XSSFWorkbook(excelFile);

            Sheet datatypeSheet = workbook.getSheet(sheetname);
            Iterator<Row> iterator = datatypeSheet.iterator();

            records = new ArrayList<ArrayList<String>>();
            
            Row firstRow = iterator.next();
            
            Iterator<Cell> cellIteratorFirstRow = firstRow.iterator();
            while (cellIteratorFirstRow.hasNext()) {
                cellIteratorFirstRow.next();
            }
            
            int colNum = firstRow.getPhysicalNumberOfCells();

            while (iterator.hasNext()) {

                Row currentRow = iterator.next();
                ArrayList<String> row = new ArrayList<String>();

                for (int i = 0; i < colNum; i++) {

                    Cell currentCell = currentRow.getCell(i);
                    //getCellTypeEnum shown as deprecated for version 3.15
                    //getCellTypeEnum ill be renamed to getCellType starting from version 4.0

                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
                        row.add(currentCell.getStringCellValue() + "");
                    } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        row.add(currentCell.getNumericCellValue() + "");
                    }else if(currentCell.getCellTypeEnum() == CellType.FORMULA){
                        row.add(currentCell.getCellFormula()+ "");
                    }
                }
                records.add(row);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return records;
        }
    }
}
