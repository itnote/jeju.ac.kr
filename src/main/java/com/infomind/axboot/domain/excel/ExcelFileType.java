package com.infomind.axboot.domain.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExcelFileType {

    public static Workbook getWorkbook(String filePath) {
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(filePath);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        Workbook workbook = null;

        if(filePath.toUpperCase().endsWith(".XLS")) {
            try {
                workbook = new HSSFWorkbook(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        } else if(filePath.toUpperCase().endsWith(".XLSX")) {
            try {
                workbook = new XSSFWorkbook(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return workbook;
    }
}
