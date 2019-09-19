package delete;

import com.csvreader.CsvReader;
import extract.AbstractExtractCsvValue;

import java.io.IOException;

/**
 * 3类情感分类数据提取
 *
 * @Author: zhuzw
 * @Date: 2019/8/28 19:09
 * @Version: 1.0
 */
public class EmotionCsvExtract extends AbstractExtractCsvValue {
    public EmotionCsvExtract() {
    }

    public EmotionCsvExtract(CsvReader csvReader) {
        super(csvReader);
    }

    @Override
    public String extractLabel() {
        String result = null;
        try {
            int emotion = Integer.parseInt(csvReader.get("舆情情感"));
            result = String.valueOf(emotion);
        }catch (IOException ioe) {
            System.out.println("获取舆情情感数据错误！当前位置：" + csvReader.getCurrentRecord());
            System.out.println(ioe.getMessage());
        }
        return result;
    }
}

