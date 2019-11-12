package create.impl;

import create.CreateFileWay;
import model.ExcelMessage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 生成excel文件
 *
 * @Author: zhuzw
 * @Date: 2019/8/19 15:23
 * @Version: 1.0
 */
public class CreateExcelFile implements CreateFileWay {

    private String path;

    public CreateExcelFile(String path) {
        this.path = path;
    }

    @Override
    public void createFile(Object object) throws IOException {

        if (null == object) {
//            return;
        }
        List<ExcelMessage> excelMessageList = (List<ExcelMessage>)object;

        File file = new File(path);// 创建excel文件对象
        FileOutputStream fOut = null;
        //汇总工作铺
        Workbook writeWorkbook = new HSSFWorkbook();
        Sheet sheet = writeWorkbook.createSheet();

        //创建表头信息
        createTitle(sheet);

        //创建内容
        createContent(sheet, excelMessageList);


        try {
            //创建输出流
            FileOutputStream outputStream = new FileOutputStream(path);
            writeWorkbook.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成内容
     * @param sheet
     * @param excelMessageList
     */
    private void createContent(Sheet sheet, List<ExcelMessage> excelMessageList) {
        Row row = null;
        for (ExcelMessage excelMessage : excelMessageList) {
            if (sheet.getRow(excelMessage.getRow()) == null) {
                //row为null
                row = sheet.createRow(excelMessage.getRow());
            }

            Cell cell = row.createCell(excelMessage.getColumn());
            cell.setCellValue(excelMessage.getContent());
        }
    }

    /**
     * 生成标题
     * @param sheet
     */
    private void createTitle(Sheet sheet) {
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("事件分类代码");

        cell = row.createCell(1);
        cell.setCellValue("事件分类名称");
    }
}
