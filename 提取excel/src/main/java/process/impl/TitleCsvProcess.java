package process.impl;

import com.csvreader.CsvReader;
import org.apache.commons.lang3.StringUtils;
import process.ColumnProcess;

import java.io.IOException;

/**
 * csv文件中标题的判断处理
 * <p>
 * TODO 添加通过下标获取标题
 *
 * @Author: zhuzw
 * @Date: 2019/8/20 9:10
 * @Version: 1.0
 */
public class TitleCsvProcess implements ColumnProcess {

//    private boolean haveTitle;
//
//    public TitleCsvProcess(boolean haveTitle) {
//        this.haveTitle = haveTitle;
//    }

    @Override
    public boolean isProcess(Object object) {
        CsvReader csvReader = (CsvReader) object;
        String title;
        try {
//            if (haveTitle) {
                title = csvReader.get("标题");
//            } else {
//                title = csvReader.get(7);
//            }

            //剔除title为空
            if (StringUtils.isEmpty(title)) {
                return false;
            }

            //剔除出错数据
            if (title.contains("中国人保申购")) {
                return false;
            }
        } catch (IOException ioe) {
            System.out.println("读取标题失败！");
            System.out.println(ioe.getMessage());
        }

        return true;
    }
}
