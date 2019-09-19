package demand.emotion_and_grade;

import com.csvreader.CsvReader;
import process.ColumnProcess;

import java.io.IOException;

/**
 * csv文件中舆情等级的判断处理
 *
 * @Author: zhuzw
 * @Date: 2019/8/20 10:47
 * @Version: 1.0
 */
public class GradeCsvProcess implements ColumnProcess {

    @Override
    public boolean isProcess(Object object) {
        CsvReader csvReader = (CsvReader)object;

        try {
            //剔除grade为空情况
            if ("".equals(csvReader.get("舆情情感等级")) || null == csvReader.get("舆情情感等级")) {
                return false;
            }

        }catch (IOException ioe) {
            System.out.println("舆情情感等级！");
            System.out.println(ioe.getMessage());
        }

        return true;
    }
}
