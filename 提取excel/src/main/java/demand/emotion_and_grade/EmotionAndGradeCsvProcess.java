package demand.emotion_and_grade;

import com.csvreader.CsvReader;
import config.ApplicationProperties;
import process.ColumnProcess;

import java.io.IOException;

/**
 * csv文件中舆情等级 + 舆情等级的处理判断
 *
 * 前面判断程序中，放置&&最后，先剔除空元素后才进行下述操作
 * @Author: zhuzw
 * @Date: 2019/8/20 10:47
 * @Version: 1.0
 */
public class EmotionAndGradeCsvProcess implements ColumnProcess {
    private ApplicationProperties aps = new ApplicationProperties();

    /**
     * 每类（舆情情感+舆情等级）取的数据量
     */
    private final int EMOTION_AND_GRADE_COUNT = aps.getEmotionAndGradeProperties().getDATA_COUNT();
    /**
     * 记录1-9分类中的数量  (0不用)
     */
    private int[] emotionAndGradeNum = new int[10];

    @Override
    public boolean isProcess(Object object) {
        CsvReader csvReader = (CsvReader)object;

        try {
            int emotion = Integer.parseInt(csvReader.get("舆情情感"));
            int grade = Integer.parseInt(csvReader.get("舆情情感等级"));
            //计算的分类（1-9）
            int emotionAndGrade = (emotion - 1) * 3 + grade;

            /**
             * 剔除分类为4、5的数据，数据量太少；
             * 信息是从输出结果分析判断的
             */
            if (4 == emotionAndGrade || 5 == emotionAndGrade) {
                return false;
            }

            //每个分类（9类）取DATA_COUNT条数据
            if (emotionAndGradeNum[emotionAndGrade] >= EMOTION_AND_GRADE_COUNT) {
                if (-1 == EMOTION_AND_GRADE_COUNT) {
                    //提取所有
                    return true;
                }
                return false;
            }
            //程序后续返回true，添加成功，故对应分类数量++ ,
            emotionAndGradeNum[emotionAndGrade]++;


        }catch (IOException ioe) {
            System.out.println("舆情情感或舆情等级读取失败！");
            System.out.println(ioe.getMessage());
        }

        return true;
    }
}
