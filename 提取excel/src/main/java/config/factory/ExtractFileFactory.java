package config.factory;

import demand.general.record.CsvRowRecordExtract;
import demand.general.record.ExcelRowRecordExtract;
import demand.general.record.RowRecordExtract;

import java.nio.charset.Charset;

/**
 * 提取文件类型
 *
 * @Author: zhuzw
 * @Date: 2019/11/18 10:04
 * @Version: 1.0
 */
public class ExtractFileFactory {
    public static RowRecordExtract getRowRecordExtract(String type) {
        if ("excel".equalsIgnoreCase(type) || "xml".equalsIgnoreCase(type) || "xlsx".equalsIgnoreCase(type)) {
            //excel表格文件处理
            return new ExcelRowRecordExtract();
        }else if("csv".equalsIgnoreCase(type)) {
            return new CsvRowRecordExtract();
        }else if ("csv&change".equalsIgnoreCase(type)) {
            //csv文件编码格式改变
            return new CsvRowRecordExtract(Charset.forName("gbk"));
        } else if ("tsv".equalsIgnoreCase(type)) {
            return new CsvRowRecordExtract('\t');
        }else if ("tsv&change".equalsIgnoreCase(type)) {
            //csv文件编码格式改变
            return new CsvRowRecordExtract('\t', Charset.forName("gbk"));
        }
        return null;
    }
}
