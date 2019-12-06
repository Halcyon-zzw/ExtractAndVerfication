package demand.general.process.child;

import demand.general.process.ArticleProcess;
import demand.general.process.ProcessWay;

/**
 * 情感7分类数据处理
 *
 * @Author: zhuzw
 * @Date: 2019/9/23 16:36
 * @Version: 1.0
 */
public class EmotionAndGradeLabel5Process extends ArticleProcess {

    public EmotionAndGradeLabel5Process(ProcessWay processWay) {
        super(processWay);
    }

    /**
     * 获取五类标签
     * 1、2 -> 1 利空
     * 3、 -> 2 强烈利空
     * 4、5、6 -> 5 中性
     * 7、8 -> 8 利好
     * 9 -> 9 强烈利好
     *
     * @param labels
     * @return
     */
    @Override
    public String getLabel(String... labels) {

        int label_1 = Integer.parseInt(labels[0]);
        int label_2 = Integer.parseInt(labels[1]);
        //计算label
        int label = (label_1 - 1) * 3 + label_2;

        return mappingLabel(label);
    }

    public String mappingLabel(int label) {
        switch (label) {
            case 1:;
            case 2:
                label = 1;
                break;
            case 3:
                label = 2;
                break;
            case 4:;
            case 5:
            case 6:
                label = 5;
                break;
            case 7:;
            case 8:
                label = 8;
                break;
            case 9:
                label = 9;
                break;
        }
        return String.valueOf(label);
    }

    public void info() {
        System.out.println(">>>>>处理5分类。");
        super.processWay.info();
    }
}
