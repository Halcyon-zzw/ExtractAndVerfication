package demand.agent;

import config.ApplicationProperties;
import create.CreateFileWay;
import create.adapter.CreateFileWayAdapter;
import create.impl.CreateFileDirect;
import deal.DealFileWay;
import demand.general.process.ArticleProcess;
import lombok.Setter;
import old.GeneralCsvDeal;
import old.emotion_grade.EmotionAndGradeDeal;
import old.emotion_grade.EmotionAndGradeExcelDeal;
import pool.DealFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 提取中介类，用于组合DealWay与ArticleProcess之间的组合
 *
 * @Author: zhuzw
 * @Date: 2019/10/30 17:10
 * @Version: 1.0
 */
public class ExtractAgent {

    @Setter
    private ApplicationProperties aps;

    private DealFileWay dealFileWay;

    @Setter
    private ArticleProcess articleProcess;


    public ExtractAgent() {

    }

    public ExtractAgent(ArticleProcess articleProcess, ApplicationProperties aps) {
        this.aps = aps;
        this.articleProcess = articleProcess;
    }

    /**
     * 输出相关属性，设置文件类型（路径）
     *
     * @param aps
     * @param articleProcess
     */
    private static void printInfo(ApplicationProperties aps, ArticleProcess articleProcess) {
        String type = getCreateType(aps, articleProcess);
        aps.getCreateFileProporttionProperties().setType(type);
        //输出配置
        printProperties(aps);
        //输出分类信息
        articleProcess.info();
    }

    /**
     * 获取生成文件的类型
     *
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
        String dataCountString = aps.getPrimaryProperties().getDataCount() == -1 ? "all" : aps.getPrimaryProperties().getDataCount() + "";
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

    public void extractEmotionAndGrade() throws IOException {


        //TODO change  目前获取Excel属性
//        Object labelHeader_1 = aps.getEmotionAndGradeExcelProperties().getLabel_1();
//        Object labelHeader_2 = aps.getEmotionAndGradeExcelProperties().getLabel_2();
        Object[] labelHeaders = aps.getPrimaryProperties().getLabels();
        Object titleHeader = aps.getPrimaryProperties().getTitle();
        Object contentHeader = aps.getPrimaryProperties().getContent();

        //TODO change 目前构建Excel对象
//        dealFileWay = new EmotionAndGradeDeal(labelHeaders, titleHeader, contentHeader);
//        ((EmotionAndGradeDeal) dealFileWay).setArticleProcess(articleProcess);
//        ((EmotionAndGradeDeal) dealFileWay).setAps(aps);

        //TODO change
        dealFileWay = new EmotionAndGradeDeal(labelHeaders, titleHeader, contentHeader);
        ((EmotionAndGradeDeal) dealFileWay).setArticleProcess(articleProcess);
        ((EmotionAndGradeDeal) dealFileWay).setAps(aps);

        DealFile dealFile = new DealFile(dealFileWay);
        List<String> result = dealFile.dealFile(aps.getPrimaryProperties().getPath());

        System.out.println("去重前长度：" + result.size());
        Set<String> resultSet = result.stream().map(s -> s).collect(Collectors.toSet());
        System.out.println("去重后长度：" + resultSet.size());

        //生成文件
        createFile(result);
    }

    public List<String> extractEmotionAndGradeExcel(ApplicationProperties aps, ArticleProcess articleProcess) throws IOException {
        //获取Excel属性
        Object[] labelHeaders = aps.getPrimaryProperties().getLabels();
        Object titleHeader = aps.getPrimaryProperties().getTitle();
        Object contentHeader = aps.getPrimaryProperties().getContent();

        //TODO change 目前构建Excel对象
        dealFileWay = new EmotionAndGradeExcelDeal(labelHeaders, titleHeader, contentHeader);
        ((EmotionAndGradeExcelDeal) dealFileWay).setArticleProcess(articleProcess);
        ((EmotionAndGradeExcelDeal) dealFileWay).setAps(aps);


        DealFile dealFile = new DealFile(dealFileWay);
        List<String> result = dealFile.dealFile(aps.getPrimaryProperties().getPath());

//        System.out.println("去重前长度：" + result.size());
////        Set<String> resultSet = result.stream().map(s -> s).collect(Collectors.toSet());
//        Set<String> resultSet = new HashSet<>();
//        for (String s : result) {
//            if (resultSet.contains(s)) {
//                System.out.println(s);
//            }
//            resultSet.add(s);
//        }
//        System.out.println("去重后长度：" + resultSet.size());

        //生成文件
        createFile(result);

        return result;
    }

    public void createFile(Collection<String> resultList) throws IOException {
        //TODO change
//        CreateFileWay createFileWay = new CreateFileProportion(
//                aps.getCreateFileProporttionProperties().getProportions(), aps.getCreateFileProporttionProperties().getPaths());

        CreateFileWay createFileWay = new CreateFileDirect(aps.getCreateFileProporttionProperties().getPath());

        CreateFileWayAdapter createFileWayAdapter = new CreateFileWayAdapter(createFileWay);
        createFileWayAdapter.createFile(resultList);

//        createFileWay.createFile(resultList);
    }


    /**
     * 通用处理的配置
     *
     * @throws IOException
     */
    public List<String> extractGeneral(ApplicationProperties aps, ArticleProcess articleProcess, char separator) throws IOException {
        printInfo(aps, articleProcess);
        Object labelHeader = aps.getPrimaryProperties().getLabel();
        Object titleHeader = aps.getPrimaryProperties().getTitle();
        Object contentHeader = aps.getPrimaryProperties().getContent();
        DealFileWay dealFileWay = new GeneralCsvDeal(labelHeader, titleHeader, contentHeader);
        ((GeneralCsvDeal) dealFileWay).setArticleProcess(articleProcess);
        ((GeneralCsvDeal) dealFileWay).setSeparator(separator);
        ((GeneralCsvDeal) dealFileWay).setAps(aps);
        DealFile dealFile = new DealFile(dealFileWay);
        List<String> result = dealFile.dealFile(aps.getPrimaryProperties().getPath());

        //TODO 直接生成文件
        if (aps.getPrimaryProperties().isCreateFile()) {
            CreateFileWay createFileWay = new CreateFileDirect(aps.getCreateFileProporttionProperties().getPath());
            createFileWay.createFile(result);
        }

        return result;
    }

}
