package process;

import com.csvreader.CsvReader;
import org.apache.commons.lang3.StringUtils;
import util.StringsUtilCustomize;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * 列是否处理(Csv文件)
 *
 * @Author: zhuzw
 * @Date: 2019/9/9 16:14
 * @Version: 1.0
 */
public class ColumnProcessCsv implements ColumnProcess{

    /**
     * 列名或列下标
     */
    private String column;

    public ColumnProcessCsv(String column) {
        this.column = column;
    }

    @Override
    public boolean isProcess(Object object) {
        CsvReader csvReader = (CsvReader)object;
        String columnValue;

        try {
            if (StringsUtilCustomize.isInteger(column)) {
                columnValue = csvReader.get(Integer.valueOf(column));
            } else {
                columnValue = csvReader.get(column);
            }
            //剔除title为空
            if (StringUtils.isEmpty(columnValue)) {
                return false;
            }
        }catch (IOException ioe) {
            System.out.println("读取内容失败！");
            System.out.println(ioe.getMessage());
        }

        return true;
    }


}

