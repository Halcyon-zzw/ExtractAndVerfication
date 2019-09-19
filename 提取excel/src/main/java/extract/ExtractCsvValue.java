package extract;

import com.csvreader.CsvReader;
import util.StringsUtilCustomize;

import java.io.IOException;

/**
 * 提取csv数据
 *
 * @Author: zhuzw
 * @Date: 2019/9/9 16:41
 * @Version: 2.0
 *          由AbstractExtractCsvValue改进得到
 *          经发现，所有提取数据方式均相同，仅仅是提取的位置不同，所以传入要提取的位置
 */
public class ExtractCsvValue {

    private String[] columns;

    public ExtractCsvValue(String[] columns) {
        this.columns = columns;
    }


    /**
     * 处理string格式，解决生成tsv文件自动换行问题（内容不在一个表格中）
     *
     * @param tempString 待处理文本
     * @return 处理后文本
     */
    protected String dealString(String tempString) {

        return tempString
                .replaceAll("\b","")
                .replaceAll("\f","")
                .replaceAll("\n","")
                .replaceAll("\r","")
                .replaceAll("\t","");
    }

    /**
     * 提取标签值
     * @return
     */
    public String extracRowValue(CsvReader csvReader) {
        /**
         * 截取字符的长度
         */
        int textLength = 10000;

        //列名或id
        String columnValue = "";
        String rowValue = "";
        for (int i = 0; i < columns.length; i++) {
            try {
                if (StringsUtilCustomize.isInteger(columns[i])) {
                    //是数字
                    columnValue = csvReader.get(Integer.valueOf(columns[i]));
                } else {
                    columnValue = csvReader.get(columns[i]);
                }
            } catch (IOException ioe) {
                System.out.println("获取列名:" + columns[i] + "失败");
            }
            if (0 == i) {
                //标签
                rowValue = columnValue;
                continue;
            }
            if (2 == i) {
                //内容
                columnValue = dealString(columnValue);
            }
            //拼接标题和内容
             rowValue = rowValue + "\t" + columnValue;
        }
        if (rowValue.length() > textLength) {
            //截取内容
            rowValue = rowValue.substring(0, textLength);
        }
        return  rowValue;

    }
}
