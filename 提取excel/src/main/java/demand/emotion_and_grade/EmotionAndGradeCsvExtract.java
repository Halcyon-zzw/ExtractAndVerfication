package demand.emotion_and_grade;


import extract.AbstractExtractCsvValue;

import java.io.IOException;

/**
 * 提取舆情情感 + 舆情等级（9类） 数据
 *
 * @Author: zhuzw
 * @Date: 2019/8/22 13:16
 * @Version: 1.0
 */
public class EmotionAndGradeCsvExtract extends AbstractExtractCsvValue {
    @Override
    public String extractLabel() {
        String result = null;
        try {
            int emotion = Integer.parseInt(csvReader.get("舆情情感"));
            int grade = Integer.parseInt(csvReader.get("舆情情感等级"));
            //计算的分类（1-9）
            int emotionAndGrade = (emotion - 1) * 3 + grade;
            result = String.valueOf(emotionAndGrade);
        }catch (IOException ioe) {
            System.out.println("获取舆情情感或舆情等级数据错误！当前位置：" + csvReader.getCurrentRecord());
            System.out.println(ioe.getMessage());
        }
        return result;
    }
}
