package delete.column;


import config.ApplicationProperties;
import extract.AbstractExtractCsvValue;

import java.io.IOException;

/**
 * 从csv文件中提取栏目分类数据
 *
 * @Author: zhuzw
 * @Date: 2019/8/29 8:38
 * @Version: 1.0
 */
public class ColumnCsvExtract extends AbstractExtractCsvValue {
    private final ApplicationProperties aps = new ApplicationProperties();
    private final int COLUMN_INDEX = (int) aps.getColumnProperties().getLabel();
    private final int TITLE_INDEX = (int) aps.getColumnProperties().getTitle();
    private final int CONTENT_INDEX = (int) aps.getColumnProperties().getContent();

    @Override
    public String extractLabel() {
        String result = null;
        try {
            //没有标题，使用下标获取
            int column = Integer.parseInt(csvReader.get(COLUMN_INDEX));

            result = String.valueOf(column);
        } catch (IOException ioe) {
            System.out.println("获取栏目数据错误！当前位置：" + csvReader.getCurrentRecord());
            System.out.println(ioe.getMessage());
        }
        return result;
    }

    /**
     * 提取文章数据， title + content
     *
     * @return
     */
    @Override
    public String extractArticle() {

        /**
         * 截取字符的长度
         */
        int textLength = 10000;
        String result = null;
        try {
            //处理内容中特殊字符问题,解决写入tsv中换行问题
            result = csvReader.get(TITLE_INDEX) + " " + super.dealString(csvReader.get(CONTENT_INDEX));
            if (result.length() > textLength) {
                //截取内容
                result = result.substring(0, textLength);
            }
        } catch (IOException ioe) {
            System.out.println("获取标题或内容错误！当前位置：" + csvReader.getCurrentRecord());
            System.out.println(ioe.getMessage());
        }
        return result;
    }
}
