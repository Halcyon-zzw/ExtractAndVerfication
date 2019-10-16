package config;

import bert.extract.EmotionAndGradeExtract;
import lombok.Data;
import org.springframework.context.annotation.Primary;

/**
 * TODO
 *
 * 统一文件格式：类别_数据量    如：情感and等级_26000(9_13000)
 *
 * @Author: zhuzw
 * @Date: 2019/10/9 15:54
 * @Version: 1.0
 */
@Data
public class FileProperties {
    private ApplicationProperties aps = new ApplicationProperties();

    private PrimaryProperties primaryProperties = new EmotionProperties();
    private EmotionProperties emotionProperties = new EmotionProperties();

    private String basePath = "E:\\文件\\工作\\AI\\bert\\";
    private String testPath = "E:\\文件\\工作\\AI\\bert\\原始语料\\栏目\\测试\\栏目分类数据_验证.csv";
    private String createPath = "E:\\文件\\工作\\AI\\bert\\测试结果\\情感等级_内容\\result.csv";
    private String statisticsPath = "E:\\文件\\工作\\AI\\bert\\测试结果\\情感等级_内容\\统计情况.txt";
//    private String createPath = "E:\\文件\\工作\\AI\\bert\\测试结果\\情感栏目_标题\\result.csv";

//    private String errorPath = aps.getBaseProperties().getTestResultPath() + "情感and等级_标题\\错误数据集.tsv";
    private String errorPath = aps.getBaseProperties().getTestResultPath() + "情感and等级标题\\错误数据集.tsv";

    @Data
    public class PrimaryProperties{
        private String[] categories = {"1", "2", "3", "6", "7", "8", "9"};
        private String basePath = "E:\\文件\\工作\\AI\\bert\\";
        private String testPath = "E:\\文件\\工作\\AI\\bert\\原始语料\\栏目\\测试\\栏目分类数据_验证.csv";
        private String createPath = "E:\\文件\\工作\\AI\\bert\\测试结果\\情感等级_内容\\result.csv";
        private String statisticsPath = "E:\\文件\\工作\\AI\\bert\\测试结果\\情感等级_内容\\统计情况.txt";
        private String errorPath = aps.getBaseProperties().getTestResultPath() + "情感and等级标题\\错误数据集.tsv";
    }

    /**
     * 情感and等级_26000(9_13000)
     */
    @Data
    public class EmotionProperties extends PrimaryProperties {
        public EmotionProperties() {
            super.testPath = super.basePath + "训练数据\\情感and等级_26000(9_13000)\\test.tsv";
            super.createPath = super.basePath + "测试结果\\情感and等级_26000(9_13000)\\result.csv";
            super.statisticsPath = super.basePath + "测试结果\\情感and等级_26000(9_13000)\\统计情况.txt";
            super.errorPath = super.basePath + "测试结果\\情感and等级_26000(9_13000)\\错误数据集.tsv";
        }
    }

}
