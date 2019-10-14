package config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

/**
 * 系统属性
 *
 * TODO 每个类都new该对象，改进
 *
 * @Author: zhuzw
 * @Date: 2019/8/26 11:07
 * @Version: 1.0
 */
@Data
public class ApplicationProperties {

    private final BaseProperties baseProperties = new BaseProperties();
    private final PrimaryProperties primaryProperties = new EmotionAndGradeProperties();
    private final EventClassifyProperties eventClassifyProperties = new EventClassifyProperties();
    private final EmotionProperties emotionProperties = new EmotionProperties();
    private final EmotionAndGradeProperties emotionAndGradeProperties = new EmotionAndGradeProperties();
    private final CreateFileProporttionProperties createFileProporttionProperties = new CreateFileProporttionProperties();
    private final ColumnProperties columnProperties = new ColumnProperties();
    private final EventExcelProperties eventExcelProperties = new EventExcelProperties();

    /**
     * 待汇总路径
     */
    private final String summaryPath = "E:\\文件\\工作\\AI\\bert\\原始语料\\舆情语料.csv";

    @Data
    public class BaseProperties {
        private String basePath = "E:\\文件\\工作\\AI\\bert\\";

        private String testResultPath = basePath + "测试结果\\";

        /**
         * 提取的训练语料
         */
        private String trainPath = basePath + "训练语料\\";

        /**
         * 原始语料路径
         */
        private String originalPath = basePath + "原始语料\\";
    }



    /**
     * 主要的参数，如果处理方式为最原始，则更改该参数就行
     */
    @Data
    public class PrimaryProperties {

        protected int dataCount = 10000;
        protected int lessCount = 200;
        protected int labelNumber;

        protected String path;

        protected Object label = "";
        protected Object title = "标题";
        protected Object content = "内容";
        protected boolean haveHeader = true;

    }

    /**
     * 事件分类处理 相关属性
     */
    @Data
    public class EventClassifyProperties extends PrimaryProperties{
        public EventClassifyProperties() {

            super.path = baseProperties.getOriginalPath() + "舆情语料.csv";
            super.dataCount = 10000;
            super.labelNumber = 66;
            super.label = "事件二级分类";
//            super.title = 7;
//            super.content = 8;
//            super.haveHeader = false;
        }
    }

    public class EventProperties extends PrimaryProperties {

        public EventProperties() {
            super.dataCount = 3000;
            super.labelNumber = 66;
            super.label = "事件二级分类";
        }
    }

    /**
     * 事间补充语料属性
     */
    public class EventSupplementProperties extends EventProperties{
        public EventSupplementProperties() {
            super.path = baseProperties.getOriginalPath() + "事件\\事件多分类补充语料.csv";
            super.dataCount = 100;
            super.labelNumber = 66;
            super.label = 5;
            super.title = 7;
            super.content = 8;
            super.haveHeader = false;
        }
    }

    @Data
    public class EventExcelProperties {
        private String path = baseProperties.getOriginalPath() + "债券舆情语料\\事件分类\\舆情事件分类语料提供-20180910\\";
        private int dataCount = 3000;
        private int labelNumber = 1;
        private int label = 1;
        private int title = 3;
        private int content = 4;
        private boolean haveHeader = true;
    }


    /**
     * 舆情情感处理 相关属性
     */
    @Data
    public class EmotionProperties extends PrimaryProperties{
        public EmotionProperties() {

            super.path = baseProperties.getBasePath() + "舆情语料.csv";
            super.dataCount = 3000;
            super.labelNumber = 3;
            super.label = "舆情情感";
            super.haveHeader = true;
        }
    }

    /**
     * 情感&等级处理 相关属性
     */
    @Data
    public class EmotionAndGradeProperties extends PrimaryProperties{
        private final int DATA_COUNT = 13000;
        Object label_1;
        Object label_2;
        public EmotionAndGradeProperties() {

            super.path = baseProperties.getOriginalPath() + "舆情语料.csv";
            super.dataCount = 13000;
            super.labelNumber = 7;
            label_1 = "舆情情感";
            label_2 = "舆情情感等级";
            super.content = "内容";

            super.haveHeader = true;
        }
    }


    /**
     *栏目分类相关属性
     */
    @Data
    public class ColumnProperties extends PrimaryProperties{

        public ColumnProperties() {

            super.path = baseProperties.getOriginalPath() + "栏目\\栏目分类数据源_新.csv";
            super.dataCount = 3000;
            super.labelNumber = 27;
            //1-3，存在Id情况（从sql中导出），
            super.label = 1;
            super.title = 2;
            super.content = 3;
            super.haveHeader = false;
        }
        /**
         * 最少的数据量，少于该值剔除
         */
        private final int leastDataCount = 200;
    }


    /**
     * 按比率生成文件相关属性
     */
    @Data
    public class CreateFileProporttionProperties {

        /**
         * 输出文件比率
         */
        private final int[] proportions = {1, 2, 7};

        /**
         * 基础路径
         */
        private String trainBasePath = baseProperties.getTrainPath();
        /**
         * 具体的数据类型
         * TODO： 生成不同的文件需要修改
         */
//        private String type = "舆情情感";
//        private String type = "情感_等级_test";
//        private String type = "舆情情感and舆情等级";
//        private String type = "事件二级分类";
//        private String type = "栏目分类\\new";
//        private String type = "事件二级分类_补充";
//        private String type = "栏目分类_测试";
        private String type = "情感and等级_标题";

        /**
         * 输出文件路径
         */
        private String[] paths = {
                trainBasePath + type + "\\dev.tsv",
                trainBasePath + type + "\\test.tsv",
                trainBasePath + type + "\\train.tsv",
        };
    }
}
