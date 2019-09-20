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

    private final PrimaryProperties primaryProperties = new EventProperties();
    private final EventClassifyProperties eventClassifyProperties = new EventClassifyProperties();
    private final EmotionProperties emotionProperties = new EmotionProperties();
    private final EmotionAndGradeProperties emotionAndGradeProperties = new EmotionAndGradeProperties();
    private final CreateFileProporttionProperties createFileProporttionProperties = new CreateFileProporttionProperties();
    private final ColumnProperties columnProperties = new ColumnProperties();
    private final EventExcelProperties eventExcelProperties = new EventExcelProperties();
    /**
     * 待汇总路径
     */
    private final String summaryPath = "E:\\下载\\钉钉文件\\工作资料\\bert\\舆情语料.csv";








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

            super.path = "E:\\下载\\钉钉文件\\工作资料\\bert\\舆情语料.csv";
            super.dataCount = 10000;
            super.labelNumber = 66;
            super.label = 5;
            super.title = 7;
            super.content = 8;
            super.haveHeader = false;
        }
    }

    /**
     * 舆情情感处理 相关属性
     */
    @Data
    public class EmotionProperties extends PrimaryProperties{
        public EmotionProperties() {

            super.path = "E:\\文件\\工作\\AI\\bert\\舆情语料.csv";
            super.dataCount = 300;
            super.labelNumber = 3;
            super.label = "舆情情感";
            super.haveHeader = true;
        }


    }

    public class EventProperties extends PrimaryProperties{

        public EventProperties() {
            super.path = "E:\\下载\\钉钉文件\\工作资料\\bert\\舆情语料.csv";
            super.dataCount = 3000;
            super.labelNumber = 66;
            super.label = "事件二级分类";
        }
        /**
         * 事间补充语料属性
         */
        public class EventSupplementProperties extends EventProperties{
            public EventSupplementProperties() {
                super.path = "E:\\下载\\钉钉文件\\工作资料\\bert\\事件多分类补充语料.csv";
                super.dataCount = 100;
                super.labelNumber = 66;
                super.label = 5;
                super.title = 7;
                super.content = 8;
                super.haveHeader = false;
            }
        }
    }

    @Data
    public class EventExcelProperties {
        private String path = "E:\\下载\\钉钉文件\\工作资料\\债券舆情语料\\事件分类\\舆情事件分类语料提供-20180910\\102001-事件分类-资质风险.xlsx";
        private int dataCount = 3000;
        private int labelNumber = 1;
        private int label = 1;
        private int title = 2;
        private int content = 3;
        private boolean haveHeader = true;
    }

    /**
     * 事件分类相关属性
     */
    @Data
    public class ColumnProperties extends PrimaryProperties{

        public ColumnProperties() {

            super.path = "E:\\下载\\钉钉文件\\工作资料\\bert\\栏目分类数据源_新.csv";
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
     * 情感&等级处理 相关属性
     */
    @Data
    public static class EmotionAndGradeProperties {
        private final int DATA_COUNT = 13000;
    }


    @Data
    public static class CreateFileProperties {

    }

    /**
     * 按比率生成文件相关属性
     */
    @Data
    public static class CreateFileProporttionProperties {

        /**
         * 输出文件比率
         */
        private final int[] proportions = {1, 2, 7};

        /**
         * 基础路径
         */
        private String basePath = "E:\\下载\\钉钉文件\\工作资料\\create\\";
        /**
         * 具体的数据类型
         */
//        private String type = "舆情情感";
//        private String type = "情感_等级_test";
//        private String type = "舆情情感and舆情等级";
//        private String type = "事件二级分类";
//        private String type = "栏目分类\\new";
//        private String type = "事件二级分类_补充";
        private String type = "栏目分类_测试";


        /**
         * 输出文件路径
         */
        private String[] paths = {
                basePath + type + "\\dev.tsv",
                basePath + type + "\\test.tsv",
                basePath + type + "\\train.tsv",
        };

    }


}
