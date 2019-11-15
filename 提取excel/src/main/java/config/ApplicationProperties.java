package config;

import lombok.Data;

import java.time.LocalDate;

/**
 * 系统属性
 * <p>
 * TODO 每个类都new该对象，改进
 *
 * @Author: zhuzw
 * @Date: 2019/8/26 11:07
 * @Version: 1.0
 */
@Data
public class ApplicationProperties {

    //    private final int beginLength = 140;
//    private final int endLength = 58;
//    private final int articleLength = beginLength + endLength;
    private final int beginLength = 200;
    private final int endLength = 150;
    private final int articleLength = beginLength + endLength;

    private final int lessDataCount = -1;


    private final BaseProperties baseProperties = new BaseProperties();
    private final EventClassifyProperties eventClassifyProperties = new EventClassifyProperties();
    private final EmotionProperties emotionProperties = new EmotionProperties();
    private final EmotionAndGradeProperties emotionAndGradeProperties = new EmotionAndGradeProperties();
    private final EmotionAndGradeProperties5 emotionAndGradeProperties5 = new EmotionAndGradeProperties5();
    private final EmotionAndGradeExcelProperties emotionAndGradeExcelProperties = new EmotionAndGradeExcelProperties();
    private final CreateFileProporttionProperties createFileProporttionProperties = new CreateFileProporttionProperties();
    private final ColumnProperties columnProperties = new ColumnProperties();
    private final EventExcelProperties eventExcelProperties = new EventExcelProperties();
    private final EmotionAndGradeTsvProperties emotionAndGradeTsvProperties = new EmotionAndGradeTsvProperties();
    private final EmotionAndGradeTestProperties emotionAndGradeTestProperties = new EmotionAndGradeTestProperties();
    private final String keywordsPath = baseProperties.basePath + "情感关键词_所有.txt";
    private final String sentenceSeparator = "[。？?！!；;]";
    private final CreateFile createFile = new CreateFile();
    /**
     * 待汇总路径
     */
    private final String summaryPath = "E:\\文件\\工作\\AI\\bert\\原始语料\\舆情语料.csv";
    private PrimaryProperties primaryProperties = new EmotionAndGradeProperties5();

    private final String temporaryPath = "E:\\文件\\工作\\AI\\bert\\临时文件.txt";

    public class Other {

    }

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
        protected Object[] labels = {};
        protected Object title = "标题";
        protected Object content = "内容";
        protected boolean haveHeader = true;

        protected boolean createFile = true;

        protected String type = "";

        public String info() {
            return "处理类型：" + type + "\n"
                    + "路径：" + path + "\n"
                    + "数据量：" + dataCount + "\n"
                    + "过滤数据量：" + lessCount + "\n"
                    + "标签数量：" + labelNumber;
        }
    }

    /**
     * 事件分类处理 相关属性
     */
    @Data
    public class EventClassifyProperties extends PrimaryProperties {
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
    public class EventSupplementProperties extends EventProperties {
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
    public class EmotionProperties extends PrimaryProperties {
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
    public class EmotionAndGradeProperties extends PrimaryProperties {
        protected Object label_1;
        protected Object label_2;
        private int DATA_COUNT = -1;

        public EmotionAndGradeProperties() {

            super.path = baseProperties.getOriginalPath() + "舆情语料.csv";
            super.dataCount = 14000;
            super.labelNumber = 7;
            label_1 = "舆情情感";
            label_2 = "舆情情感等级";
            Object[] labelss = {label_1, label_2};
            super.labels = labelss;
            super.content = "内容";

            super.haveHeader = true;

            super.type = "情感and等级";
        }
    }

    /**
     * 情感&等级处理 相关属性
     */
    @Data
    public class EmotionAndGradeExcelProperties extends EmotionAndGradeProperties {

        public EmotionAndGradeExcelProperties() {
            super.path = baseProperties.getOriginalPath() + "\\舆情语料\\舆情情感\\语料\\舆情情感语料.xlsx";
            super.dataCount = -1;
            super.lessCount = 10000;
            super.labelNumber = 10;
            label_1 = 3;
            label_2 = 4;
            Object[] labelss = {label_1, label_2};
            super.labels = labelss;

            super.title = 2;
            super.content = 5;
        }
    }

    public class EmotionAndGradeProperties5 extends EmotionAndGradeProperties {
        public EmotionAndGradeProperties5() {
            super();
            super.labelNumber = 5;
        }
    }

    public class PrimaryTestProperties {

    }


    public class EmotionAndGradeTestProperties extends PrimaryProperties {
        public EmotionAndGradeTestProperties() {
//            super.path = baseProperties.getTrainPath() + "";
            super.path = baseProperties.getTestResultPath() + "\\情感7类测试集.csv";
//            super.path = baseProperties.getBasePath() + "\\情感识别\\未来标准集合";
            super.dataCount = -1;
            super.labelNumber = 7;
            super.label = 2;
            Object[] labelss = {2, 3};
            super.title = 7;
            super.content = 8;
//            Object[] labelss = {1, 2};
//            super.title = 3;
//            super.content = 4;

            super.labels = labelss;
            super.haveHeader = false;

            super.type = "情感and等级_test";
        }
    }


    public class EmotionAndGradeTsvProperties extends PrimaryProperties {
        public EmotionAndGradeTsvProperties() {
            super.path = baseProperties.getTrainPath() + "\\情感and等级_test\\情感and等级_test_7_none_all_118_1113\\all.tsv";
            super.dataCount = -1;
            super.labelNumber = 7;
            super.label = 0;
            super.title = -1;
            super.content = 1;
            super.haveHeader = false;
            super.createFile = false;

            super.type = "tsv";


        }

        public String toString() {
            return "路径：" + path + "\n"
                    + "数据量：" + dataCount + "\n"
                    + "过滤数据量：" + lessCount + "\n"
                    + "标签数量：" + labelNumber + "\n";
        }
    }


    /**
     * 栏目分类相关属性
     */
    @Data
    public class ColumnProperties extends PrimaryProperties {

        /**
         * 最少的数据量，少于该值剔除
         */
        private final int leastDataCount = 200;

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
    }


    /**
     * 按比率生成文件相关属性
     */
    @Data
    public class CreateFileProporttionProperties {

        /**
         * 输出文件比率
         */
//        private final int[] proportions = {1, 2, 7};
        private final int[] proportions = {1, 1, 8};
        private final String createProportionString = "118";
        /**
         * 当前日期，格式：1106
         */
        String data = LocalDate.now().getMonthValue() + ""
                //小于10在前面补充0
                + (LocalDate.now().getDayOfMonth() < 10 ? "0" + LocalDate.now().getDayOfMonth() : LocalDate.now().getDayOfMonth());
        /**
         * 基础路径
         */
        private String trainBasePath = baseProperties.getTrainPath();
        /**
         * 具体的数据类型
         * 命名：类型_处理方式_数据量_生成文件方式_日期
         * TODO： 生成不同的文件需要修改
         */
//        private String type = "舆情情感";
//        private String type = "情感_等级_test";
//        private String type = "舆情情感and舆情等级";
//        private String type = "事件二级分类";
//        private String type = "栏目分类\\new";
//        private String type = "事件二级分类_补充";
//        private String type = "栏目分类_测试";
//        private String type = "情感and等级_标题_test";
//        private String type = "情感and等级_含无效";
//        private String type = "情感and等级_删无效";
//        private String type = "情感and等级_26000";
//        private String type = "情感and等级_50000";
//        private String type = "情感and等级_summary_All_118";
//        private String type = "情感and等级_keyword_All_118_1101";
//        private String type = "情感and等级_keyword_26000_118_1102";
//        private String type = "情感and等级_keyword_All_118_1102";
//        private String type = "情感and等级_keyword_delete_All_118_1102";
        //new:  类型_分类数量_处理方式_数据量_输出文件方式_日期
//        private String type = primaryProperties.getType() + "_" + primaryProperties.getLabelNumber()
//                + "_keyword_delete_" + primaryProperties.getDataCount() + "_" + createProportionString + "_" + data;
        private String type;

        private String trainDir;
        /**
         * 输出文件路径
         */
        private String path;

        private String[] paths;

        public String getTrainDir() {
            return trainBasePath + type;
        }

        public String getPath() {
            return trainBasePath + type + "\\all.tsv";
        }

        public String[] getPaths() {
            paths = new String[]{
                    trainBasePath + type + "\\dev.tsv",
                    trainBasePath + type + "\\test.tsv",
                    trainBasePath + type + "\\train.tsv",
            };
            return paths;
        }
<<<<<<< HEAD
    }

    @Data
    public class CreateFile {
        private String deletePath = baseProperties.getBasePath() + "delete\\result.tsv";
=======
>>>>>>> f3a5f76a346ab516592180b911483953d6b69ff1
    }
}
