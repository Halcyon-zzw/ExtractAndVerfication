package demand.general.process.child;

import demand.general.process.ArticleProcess;
import demand.general.process.ProcessWay;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/11/12 15:32
 * @Version: 1.0
 */
public class EmotionAndGradeLabel7MappingProcess extends ArticleProcess {

    public EmotionAndGradeLabel7MappingProcess(ProcessWay processWay) {
        super(processWay);
    }

    /**
     * 默认直接使用标签；需计算重写该方法
     * @param labels
     * @return
     */
    @Override
    public String getLabel(String... labels) {

        int label = Integer.parseInt(labels[0]);

        //正负性
        int label_1 = (label - 1) / 3 + 1;
        //星级
        int label_2 = (label - 1) % 3 + 1;
        label = label_1 * 100 + label_2;
        if (2 == label_1 / 100) {
            //中性
            label = 200;
        }
        return String.valueOf(label);
    }

    public void info() {
        System.out.println(">>>>>>>处理7分类。");
        super.processWay.info();
    }
}
