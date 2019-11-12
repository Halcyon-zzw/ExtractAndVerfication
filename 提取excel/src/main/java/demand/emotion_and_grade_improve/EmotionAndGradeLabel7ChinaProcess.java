package demand.emotion_and_grade_improve;

import demand.general.ArticleProcess;
import demand.general.process.ProcessWay;

/**
 * 情感7分类数据处理，通过label_1,label_2映射得到
 * 负面一星：101
 * 负面二星：102
 * 负面三星：103
 * 中性无：200
 * 正面一星：301
 * 正面二星：302
 * 正面三星：303
 *
 * @Author: zhuzw
 * @Date: 2019/9/23 16:36
 * @Version: 1.0
 */
public class EmotionAndGradeLabel7ChinaProcess extends ArticleProcess {
    EmotionAndGradeLabel8Process emotionAndGradeLabel8Process;

    public EmotionAndGradeLabel7ChinaProcess(ProcessWay processWay) {
        super(processWay);
        emotionAndGradeLabel8Process = new EmotionAndGradeLabel8Process(processWay);
    }

    /**
     * 默认直接使用标签；需计算重写该方法
     * @param labels
     * @return
     */
    @Override
    public String getLabel(String... labels) {
        String labelString = emotionAndGradeLabel8Process.getLabel(labels);
        Integer label = Integer.valueOf(labelString);
        if (2 == label / 100) {
            label = 200;
        }
        return label.toString();
    }

    public void info() {
        System.out.println(">>>>>>>处理中文7分类。");
        super.processWay.info();
    }
}
