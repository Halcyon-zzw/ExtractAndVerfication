package demand.event_classify_2;
import com.csvreader.CsvReader;
import extract.AbstractExtractCsvValue;

import java.io.IOException;

/**
 * 提取二级事件分类数据
 *
 * @Author: zhuzw
 * @Date: 2019/8/20 10:09
 * @Version: 1.0
 */
public class EventClassifyCsvExtract extends AbstractExtractCsvValue {

    public EventClassifyCsvExtract() {
    }

    public EventClassifyCsvExtract(CsvReader csvReader) {
        super(csvReader);
    }

    @Override
    public String extractLabel() {
        String result = null;
        try {
            result = csvReader.get("事件二级分类");
        }catch (IOException ioe) {
            System.out.println("获取事件二级分类错误！当前位置：" + csvReader.getCurrentRecord());
            System.out.println(ioe.getMessage());
        }
        return result;
    }
}
