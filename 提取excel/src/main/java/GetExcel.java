import config.ApplicationProperties;
import config.ExcelProperties;
import config.PropertiesFactory;
import create.CreateFileWay;
import create.impl.CreateExcelFile;
import create.impl.CreateFileProportion;
import deal.AbstractDealFileWay;
import deal.DealFileWay;
import deal.impl.FileNameDeal;
import delete.LyricExcelDeal;
import delete.column.ColumnCsvDeal;
import delete.column.ColumnCsvExtract;
import demand.emotion_and_grade.EmotionAndGradeCsvDeal;
import demand.emotion_and_grade_improve.*;
import old.event_classify_2.EventClassifyCsvDeal;
import old.event_classify_2.EventClassigySupplementCsvDeal;
import demand.general.ArticleProcess;
import demand.general.ExtractAgent;
import demand.general.GeneralCsvDeal;
import demand.general.excel.GeneralDealExcel;
import demand.general.process.KeywordProcess;
import model.ExcelMessage;
import pool.DealFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 提取excel值
 *
 * @Author: zhuzw
 * @Date: 2019/8/6 19:23
 * @Version: 1.0
 *
 * TODO
 * 优化数据处理方式
 * 将设计模式引入项目，
 * 学习spring框架，将spring框架引入项目
 * 将日志引入项目
 * 将项目改成web项目，属性通过web可选实现
 *
 */
public class GetExcel {

    static int countFile = 0;

    private static String basePath = "E:\\文件\\工作\\AI\\bert\\";

    private static ApplicationProperties aps = new ApplicationProperties();

    private static int[] proportions = aps.getCreateFileProporttionProperties().getProportions();

    private static String[] paths = aps.getCreateFileProporttionProperties().getPaths();

    private static ExtractAgent extractAgent = new ExtractAgent();

    public static void main(String[] args) throws IOException {
        //提取舆情情感+舆情等级（9类）                                                                                                                                                                                                                                                                                                  数据
//        dealEmotionAndGradeCsvDemo();
        //提取舆情情感（3类）数据
//        dealGeneralDemo();
//        dealLyricExcelDemo();
        //提取分类数据
//        extractClassify();
        //文件名生成文件
//        createFileByFileName();
        //提取栏目分类数据
//        dealColumnCsvDemo();
        //提取补充事件分类数据
//        extractClassifySupplement();
//        generalExcelDemo();
//        mergeTitleAndContent();
        long startTime = System.currentTimeMillis();
        //处理8分类 TODO change
        ArticleProcess articleProcess = new EmotionAndGradeLabel7ChinaProcess(new KeywordProcess());
        //处理7分类
//        ArticleProcess articleProcess = new EmotionAndGradeLabel7Process(new KeywordProcess());
        //处理5分类
//        ArticleProcess articleProcess = new EmotionAndGradeLabel5Process(new KeywordProcess());
        //原始处理，
//        ArticleProcess articleProcess = new EmotionAndGradeLabel7Process(new NoneProcess());

        //分类数量修改分类处理方式  TODO change
        aps.setPrimaryProperties(PropertiesFactory.getProperties("emotionAndGradeExcel"));
        String type = getCreateType(aps, articleProcess);
        aps.getCreateFileProporttionProperties().setType(type);
        //输出配置
        printProperties(aps);
        //输出分类信息
        articleProcess.info();
        //提取grade and emotion
        extractAgent.setArticleProcess(articleProcess);
        extractAgent.setAps(aps);
        extractAgent.extractEmotionAndGrade();
        System.out.println("耗时：" + (System.currentTimeMillis() - startTime));

    }

    /**
     * 获取生成文件的类型
     * @param aps
     * @param articleProcess
     * @return
     */
    public static String getCreateType(ApplicationProperties aps, ArticleProcess articleProcess) {
        //当前日期，格式：1106
        String date = LocalDate.now().getMonthValue() + ""
                //小于10在前面补充0
                + (LocalDate.now().getDayOfMonth() < 10 ? "0" + LocalDate.now().getDayOfMonth() : LocalDate.now().getDayOfMonth());

        //数据量为-1时，显示为 all，否则显示原本数量
        String dataCountString = aps.getPrimaryProperties().getDataCount()  == -1 ? "all" : aps.getPrimaryProperties().getDataCount()  + "";
        String type = aps.getPrimaryProperties().getType() + "\\"   //文件夹
                + aps.getPrimaryProperties().getType()     //类型
                + "_" + aps.getPrimaryProperties().getLabelNumber()     //label数量,
                + "_" + articleProcess.getProcessWay().getType()        //处理方式
                + "_" + dataCountString       //数据量（每类）
                + "_" + aps.getCreateFileProporttionProperties().getCreateProportionString() //输出文件的比率
                + "_" + date;       //当前日期
        return type;
    }

    public static void printProperties(ApplicationProperties aps) {
        System.out.println("开始处理...");
        System.out.println("处理参数：");
        System.out.println("===================");
        System.out.println(aps.getPrimaryProperties().info());
        System.out.println("数据长度：" + aps.getArticleLength());
        System.out.println("输出路径:" + aps.getCreateFileProporttionProperties().getTrainDir());
    }


    /**
     * 处理tsv文件的配置
     */
    private static void mergeTitleAndContent() throws IOException {
        //待汇总文件路径
        String path = aps.getEmotionAndGradeTsvProperties().getPath();

        Object labelHeader = aps.getEmotionAndGradeTsvProperties().getLabel();
        Object titleHeader = aps.getEmotionAndGradeTsvProperties().getTitle();
        Object contentHeader = aps.getEmotionAndGradeTsvProperties().getContent();

//        DealFileWay dealFileWay = new EmotionAndGradeDeal(labelHeaders, titleHeader);
        DealFileWay dealFileWay = new GeneralCsvDeal(labelHeader, titleHeader, contentHeader);
        ((GeneralCsvDeal) dealFileWay).setSeparator('\t');
        aps.setPrimaryProperties(PropertiesFactory.getProperties("emotionAndGradeTsv"));
        ((GeneralCsvDeal) dealFileWay).setAps(aps);
        DealFile dealFile = new DealFile(dealFileWay);
        List<String> result = dealFile.dealFile(path);
        //生成文件
        CreateFileWay createFileWay = new CreateFileProportion(proportions, paths);
        createFileWay.createFile(result);

    }



    /**
     * 常规提取excel
     *
     * @throws IOException
     */
    private static void generalExcelDemo() throws IOException {
        //待汇总文件路径
        String path = aps.getEventExcelProperties().getPath();

        int labelHeader = aps.getEventExcelProperties().getLabel();
        int titleHeader = aps.getEventExcelProperties().getTitle();
        int contentHeader = aps.getEventExcelProperties().getContent();

        DealFileWay dealFileWay = new GeneralDealExcel(labelHeader, titleHeader, contentHeader);
        DealFile dealFile = new DealFile(dealFileWay);

        List<String> result = dealFile.dealFile(path);
    }


    /**
     * 从补充数据中提取数据
     *
     * @throws IOException
     */
    public static void extractClassifySupplement() throws IOException {

        //待提取文件路径
        String extractedPath = basePath + "原始语料\\事件\\事件多分类补充语料.csv";

        DealFileWay lyricClassifyCsvDeal = new EventClassigySupplementCsvDeal();
        CreateFileWay createFileProportion = new CreateFileProportion(proportions, paths);
        DealFile dealLyricCsv = new DealFile(lyricClassifyCsvDeal, createFileProportion);
        dealLyricCsv.dealAndCreateFile(extractedPath);

    }

    private static void dealColumnCsvDemo() throws IOException {
        //待处理文件路径
        String pendingPath = aps.getColumnProperties().getPath();

        AbstractDealFileWay abstractDealFileWay = new ColumnCsvDeal(new ColumnCsvExtract());
        abstractDealFileWay.extractedValue(pendingPath);
//        CreateFileWay createFileProportion = new CreateFileProportion(proportions, paths);
//        DealFileModify dealLyricCsv = new DealFileModify(abstractDealFileWay, createFileProportion);
//        dealLyricCsv.dealAndCreateFile(pendingPath);
    }

    /**
     * 演示提取舆情情感（3类）数据
     */
    private static void dealGeneralDemo() throws IOException {
        //待汇总文件路径
        ApplicationProperties.PrimaryProperties properties = PropertiesFactory.getProperties("column");


        generalDeal(properties);

//        CreateFileWay createFileProportion = new CreateFileProportion(proportions, paths);
//        createFileProportion.createFile(result);
    }

    private static void generalDeal(ApplicationProperties.PrimaryProperties properties) throws IOException {
        Object labelHeader = properties.getLabel();
        Object titleHeader = properties.getTitle();
        Object contentHeader = properties.getContent();
        String path = properties.getPath();
        DealFileWay dealCsvWay = new GeneralCsvDeal(labelHeader, titleHeader, contentHeader);
        List<String> result = dealCsvWay.extractedValue(path);
    }

    /**
     * 演示提取舆情情感+舆情等级（9类）数据
     *
     * @throws IOException
     */
    public static void dealEmotionAndGradeCsvDemo() throws IOException {

        //待汇总文件路径
        String summariedPath = aps.getSummaryPath();

        DealFileWay dealCsvWay = new EmotionAndGradeCsvDeal();
        CreateFileWay createFileProportion = new CreateFileProportion(proportions, paths);
        DealFile dealLyricCsv = new DealFile(dealCsvWay, createFileProportion);
        dealLyricCsv.dealAndCreateFile(summariedPath);

    }

    /**
     * 处理舆情excel数据
     *
     * @throws IOException
     */
    public static void dealLyricExcelDemo() throws IOException {
        //待汇总文件路径
        String summariedPath = ExcelProperties.summariedPath + "102001-事件分类-资质风险.xlsx";
        String[] paths = {
                ExcelProperties.devTsvPath,
                ExcelProperties.testTsvPath,
                ExcelProperties.trainTsvPath
        };

        DealFileWay dealFileWay = new LyricExcelDeal();
        CreateFileWay createFileWay = new CreateFileProportion(proportions, paths);
        DealFile dealFile = new DealFile(dealFileWay, createFileWay);
        dealFile.dealAndCreateFile(summariedPath);
    }


    /**
     * 提取分类数据
     */
    public static void extractClassify() throws IOException {

        //待提取文件路径
        String extractedPath = aps.getSummaryPath();

        DealFileWay lyricClassifyCsvDeal = new EventClassifyCsvDeal();
        CreateFileWay createFileProportion = new CreateFileProportion(proportions, paths);
        DealFile dealLyricCsv = new DealFile(lyricClassifyCsvDeal, createFileProportion);
        dealLyricCsv.dealAndCreateFile(extractedPath);
    }


    /**
     * 提取目录下的文件名，并将其生成文件
     *
     * @throws IOException
     */
    public static void createFileByFileName() throws IOException {

        String path = basePath + "训练语料\\事件分类.xlsx";

        String dealPath = basePath + "原始语料\\债券舆情语料\\事件分类\\舆情事件分类语料提供-20180910\\";

        DealFileWay dealFileWay = new FileNameDeal();
        DealFile fileNameToFile = new DealFile(dealFileWay);
        List<String> strings = fileNameToFile.dealFile(dealPath);
        //将strings转为ExcelMessage
        List<ExcelMessage> excelMessages = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            String[] tempStrings = strings.get(i).split("\t");


            //设置列
            for (int j = 0; j < tempStrings.length; j++) {
                ExcelMessage excelMessage = new ExcelMessage();
                excelMessage.setRow(i + 1);
                excelMessage.setColumn(j);
                excelMessage.setContent(tempStrings[j]);
                excelMessages.add(excelMessage);
            }
        }


        CreateFileWay createFileWay = new CreateExcelFile(path);
        createFileWay.createFile(excelMessages);
    }
}
