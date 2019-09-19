package extract;

import com.csvreader.CsvReader;
import config.ApplicationProperties;
import lombok.Data;

import java.io.IOException;

/**
 * 提取csv文件的一行数据的抽象方法
 * 已经定义好标题及内容的提取方法，子类自需要实现提取标签的方法
 *
 * @Author: zhuzw
 * @Date: 2019/8/20 9:59
 * @Version: 1.0
 */
@Data
public abstract class AbstractExtractCsvValue {
    private ApplicationProperties aps = new ApplicationProperties();

    protected CsvReader csvReader;

    public AbstractExtractCsvValue() {
    }

    public AbstractExtractCsvValue(CsvReader csvReader) {
        this.csvReader = csvReader;
    }

    /**
     * 提取文章数据， title + content
     *
     * @return
     */
     public String extractArticle() {

        /**
         * 截取字符的长度
         */
        int textLength = 10000;
        String result = null;
        try {
            //处理内容中特殊字符问题,解决写入tsv中换行问题
            result = csvReader.get((String)aps.getPrimaryProperties().getTitle()) + " " + dealString(csvReader.get((String)aps.getPrimaryProperties().getContent()));
            if (result.length() > textLength) {
                //截取内容
                result = result.substring(0, textLength);
            }
        }catch (IOException ioe) {
            System.out.println("获取标题或内容错误！当前位置：" + csvReader.getCurrentRecord());
            System.out.println(ioe.getMessage());
        }
        return result;
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
    public abstract String extractLabel();

    /**
     * 提取一行值
     *
     * @return 标签值 + \t + 文章值
     */
    public String extractRowValue() {
        return extractLabel() + "\t" + extractArticle();
    }
}
