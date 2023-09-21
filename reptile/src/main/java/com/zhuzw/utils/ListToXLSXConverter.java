package com.zhuzw.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ListToXLSXConverter {

    public static void convertToXlsx(List<List<String>> data, String outputFilePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        int rowNumber = 0;
        for (List<String> rowData : data) {
            Row currentRow = sheet.createRow(rowNumber++);

            int columnNumber = 0;
            for (String cellValue : rowData) {
                Cell cell = currentRow.createCell(columnNumber++);
                cell.setCellValue(cellValue);
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream(outputFilePath)) {
            workbook.write(outputStream);
        }

        workbook.close();
        System.out.println(outputFilePath + " XLSX file generated successfully!");
    }
}
