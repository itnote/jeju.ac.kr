package com.infomind.axboot.domain.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelRead {
    public static List<Map<String, String>> read(ExcelReadOption excelReadOption) {
        Workbook workbook = ExcelFileType.getWorkbook(excelReadOption.getFilePath());
        Sheet sheet = workbook.getSheetAt(0);
        int numOfRows = sheet.getPhysicalNumberOfRows();
        int numOfCells = 0;
        Row row = null;
        Cell cell = null;
        String cellName = "";
        Map<String, String> map = null;
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        for(int rowIndex = excelReadOption.getStartRow() - 1; rowIndex < numOfRows; rowIndex++) {
            row = sheet.getRow(rowIndex);
            if(row != null) {
                numOfCells = row.getLastCellNum();
                map = new HashMap<String, String>();
                for(int cellIndex = 0; cellIndex < numOfCells; cellIndex++) {
                    cell = row.getCell(cellIndex);
                    cellName = ExcelCellRef.getName(cell, cellIndex);

                    if (!excelReadOption.getOutputColumns().contains(cellName)) {
                        continue;
                    }
                    map.put(cellName, ExcelCellRef.getValue(cell));
                }
                result.add(map);
            }
        }
        return result;
    }
}
