package process.impl;

import com.csvreader.CsvReader;
import process.ColumnProcess;

import java.io.IOException;

/**
 * csv文件中内容的判断处理
 *
 * @Author: zhuzw
 * @Date: 2019/8/20 9:14
 * @Version: 1.0
 */
public class ContentCsvProcess implements ColumnProcess {
    @Override
    public boolean isProcess(Object object) {
        CsvReader csvReader = (CsvReader)object;
//        String content =
        try {
            //剔除title为空
            if ("".equals(csvReader.get("内容")) || null == csvReader.get("内容")) {
                return false;
            }
        }catch (IOException ioe) {
            System.out.println("读取内容失败！");
            System.out.println(ioe.getMessage());
        }

        return true;
    }
}
