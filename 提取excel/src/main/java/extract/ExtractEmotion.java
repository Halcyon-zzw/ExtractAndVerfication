package extract;

import com.csvreader.CsvReader;
import java.io.IOException;

/**
 * 提取舆情情感数据
 *
 * @Author: zhuzw
 * @Date: 2019/8/20 11:06
 * @Version: 1.0
 */
public class ExtractEmotion extends AbstractExtractCsvValue {

    public ExtractEmotion() {
    }

    public ExtractEmotion(CsvReader csvReader) {
        super(csvReader);
    }

    @Override
    public String extractLabel() {
        String result = null;
        try {
            result = csvReader.get("舆情情感");
        }catch (IOException ioe) {
            System.out.println("获取舆情情感错误！当前位置：" + csvReader.getCurrentRecord());
            System.out.println(ioe.getMessage());
        }
        return result;
    }
}
