package demand.general.process.child;

import demand.general.process.ArticleProcess;
import demand.general.process.ProcessWay;

/**
 * 情感7分类数据处理，初始名称为数字
 *
 * @Author: zhuzw
 * @Date: 2019/9/23 16:36
 * @Version: 1.0
 *
 * @Version: 2.0
 *          7分类标签名由 1-9 改成101、102......
 */
public class EmotionAndGradeLabel7Process extends ArticleProcess {

    public EmotionAndGradeLabel7Process(ProcessWay processWay) {
        super(processWay);
    }

    /**
     * 默认直接使用标签；需计算重写该方法
     * @param labels
     * @return
     */
    @Override
    public String getLabel(String... labels) {

        int label_1 = Integer.parseInt(labels[0]);
        int label_2 = Integer.parseInt(labels[1]);
        //计算label
//        int label = (label_1 - 1) * 3 + label_2;
        int label = label_1 * 100 + label_2;
        //中性4、5、6 统一成 200
        if (2 == label / 100) {
            label = 200;
//            label = 5;
        }

        return String.valueOf(label);
    }

    public void info() {
        System.out.println(">>>>>>>处理7分类。");
        super.processWay.info();
    }
}
