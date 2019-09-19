package delete;

import com.csvreader.CsvReader;
import process.ColumnProcess;

import java.io.IOException;

/**
 * csv文件中舆情情感的判断处理
 *
 * @Author: zhuzw
 * @Date: 2019/8/20 10:50
 * @Version: 1.0
 */
public class EmotionCsvProcess implements ColumnProcess {
    /**
     * 取的舆情情感数据量
     */
    private final int EMOTION_COUNT = 100000;
    /**
     * 记录舆情情感分类（1-3）数量 (0不用)
     */
    int[] emotionNum = new int[4];
    @Override
    public boolean isProcess(Object object) {
        CsvReader csvReader = (CsvReader)object;

        try {
            String emotion = csvReader.get("舆情情感");
            //剔除emotion为空情况
            if ("".equals(emotion) || null == emotion) {
                return false;
            }

            //剔除1-3以外的数据
            char[] chars = csvReader.get("舆情情感").toCharArray();
            if (chars[0] < '1' || chars[0] > '3') {
                return false;
            }
            int emotionIndex = Integer.parseInt(emotion);
            //判断数据量
            if (emotionNum[emotionIndex] >= EMOTION_COUNT) {
                return false;
            }

            //统计数量
            emotionNum[emotionIndex]++;

        }catch (IOException ioe) {
            System.out.println("读取舆情情感失败！");
            System.out.println(ioe.getMessage());
        }

        return true;
    }
}
