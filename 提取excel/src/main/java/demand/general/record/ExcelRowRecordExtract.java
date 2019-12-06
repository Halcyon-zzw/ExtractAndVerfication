package demand.general.record;

import lombok.Setter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/11/17 12:48
 * @Version: 1.0
 */
public class ExcelRowRecordExtract implements RowRecordExtract {

    private String path;
    private static Sheet sheet;
    /**
     * 最后一行的index，注意是index，不是行数
     */
    private int lastRowIndex;
    private static int firstRowIndex = 0;
    @Setter
    private int sheetIndex = 0;


    /**
     * 文件处理方式初始化，解决多文件抽取问题
     * @param path
     */
    @Override
    public void init(String path) {
        this.path = path;
        sheet = FileUtil.getSheet(path, sheetIndex);
        firstRowIndex = 0;
        lastRowIndex = sheet.getLastRowNum();
    }

    @Override
    public List<String[]> getRawRecords() throws IOException {
        return getRawRecords(1000);
    }

    @Override
    public List<String[]> getRawRecords(int count) throws IOException {

        List<String[]> resultList = null;
        int extractCount = 0;
        //连续为null的数量
        int contimuousNullCount = 0;
        /**
         * 存在lastRowIndex很大，但实际有用的数据很少的情况，
         * 解决方法：1、遇到row == null 直接退出；   存在问题，可能就中间某行存在（一些特殊表格可能存在）
         *           2、连续 5 次为null时退出；
         */
        for (int rowIndex = firstRowIndex; rowIndex <= lastRowIndex; rowIndex++) {
            if (extractCount >= count) {
                break;
            }
            //连续5次为null
            if (5 == contimuousNullCount) {
                break;
            }
            Row row = sheet.getRow(rowIndex);
            if (null == row) {
                contimuousNullCount++;
                continue;
            }

            contimuousNullCount= 0;
            String[] rawRecord = new String[row.getLastCellNum() + 1];

            int index = row.getFirstCellNum();
            if (!(index >= 0)) {
                //存在空行数据，但row不为null，且firstIndex 为 -1
                System.out.println(path + " index为负！");
                break;
            }
            for (; index <= row.getLastCellNum(); index++) {

                if (null != row.getCell(index)) {
                    rawRecord[index] = row.getCell(index).toString();
                }
            }
            if (null == resultList) {
                resultList = new ArrayList<>();
            }
            resultList.add(rawRecord);
            extractCount++;
            firstRowIndex++;
        }
        return resultList;
    }
}
