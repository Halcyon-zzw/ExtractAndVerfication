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
import demand.emotion_and_grade_improve.EmotionAndGradeDeal;
import demand.event_classify_2.EventClassifyCsvDeal;
import demand.event_classify_2.EventClassigySupplementCsvDeal;
import demand.general.GeneralCsvDeal;
import demand.general.excel.GeneralDealExcel;
import model.ExcelMessage;
import pool.DealFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 提取excel值
 *
 * @Author: zhuzw
 * @Date: 2019/8/6 19:23
 * @Version: 1.0
 */
public class GetExcel {

    static int countFile = 0;

    private static ApplicationProperties aps = new ApplicationProperties();

    private static int[] proportions = aps.getCreateFileProporttionProperties().getProportions();

    private static String[] paths = aps.getCreateFileProporttionProperties().getPaths();


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

        //提取grade and emotion 仅标题
        gradeAndEnotionTitle();
    }

    /**
     * 提取grade and emotion 仅标题
     */
    public static void gradeAndEnotionTitle() throws IOException {
        //待汇总文件路径
        String path = aps.getEmotionAndGradeProperties().getPath();

        Object labelHeader_1 = aps.getEmotionAndGradeProperties().getLabel_1();
        Object labelHeader_2 = aps.getEmotionAndGradeProperties().getLabel_2();
        Object[] labelHeaders = {labelHeader_1, labelHeader_2};
        Object titleHeader = aps.getEmotionAndGradeProperties().getTitle();


        DealFileWay dealFileWay = new EmotionAndGradeDeal(labelHeaders, titleHeader);
        DealFile dealFile = new DealFile(dealFileWay);
        List<String> result = dealFile.dealFile(path);
        //生成文件
//        CreateFileWay createFileWay = new CreateFileProportion(proportions, paths);
//        createFileWay.createFile(result);
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
        String extractedPath = "E:\\下载\\钉钉文件\\工作资料\\bert\\事件多分类补充语料.csv";

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

        String path = "E:\\下载\\钉钉文件\\工作资料\\create\\事件分类.xlsx";

        String dealPath = "E:\\下载\\钉钉文件\\工作资料\\债券舆情语料\\事件分类\\舆情事件分类语料提供-20180910\\";

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
