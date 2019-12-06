package bert.extract;

import config.ApplicationProperties;
import config.factory.PropertiesFactory;
import demand.general.process.child.EmotionAndGradeLabel7ChinaProcess;
import demand.general.process.ArticleProcess;
import demand.agent.ExtractAgent;
import demand.general.process.process_way.InvalidProcess;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 情感 and 等级 测试数据提取
 *
 * @Author: zhuzw
 * @Date: 2019/10/10 17:10
 * @Version: 1.0
 */
public class EmotionAndGradeExtract {
    private ApplicationProperties aps = new ApplicationProperties();

//    public List<String[]> extract() throws IOException {
//        String path = aps.getEmotionAndGradeProperties().getPath();
//        Object labelHeader_1 = aps.getEmotionAndGradeProperties().getLabel_1();
//        Object labelHeader_2 = aps.getEmotionAndGradeProperties().getLabel_2();
//        Object[] labelHeaders = {labelHeader_1, labelHeader_2};
//        Object titleHeader = aps.getEmotionAndGradeProperties().getTitle();
//
//        DealFileWay dealFileWay = new EmotionAndGradeDeal(labelHeaders, titleHeader);
//        DealFile2Strings dealFile2Strings = new GeneralDealAdapter(dealFileWay);
//        List<String[]> tempList = dealFile2Strings.dealFile(path);
//
//        List<String[]> resultList = new ArrayList<>();
//
//        //截取未参加训练的数据
//        int num = 0;
//        for (int i = 0; i < 7; i++) {
//            resultList.addAll(tempList.subList((i + 1) * 13000, (i + 1) * 13000 + 1300));
//        }
//
//        return resultList;
//    }

    /**
     * ============================EmotionAndGradeExcel=========================
     */
    private List<String> extractEmotionAndGradeExcel() {
        ArticleProcess articleProcess = new EmotionAndGradeLabel7ChinaProcess(new InvalidProcess());
        aps.setPrimaryProperties(PropertiesFactory.getProperties("emotionAndGradeExcel"));
        return extractEmotionAndGradeExcel(aps, articleProcess);
    }

    private List<String> extractEmotionAndGradeExcel(ApplicationProperties aps, ArticleProcess articleProcess) {
        ExtractAgent extractAgent = new ExtractAgent();
        //提取excel中grade and emotion
        extractAgent.setAps(aps);
        extractAgent.setArticleProcess(articleProcess);
        try {
            return extractAgent.extractEmotionAndGradeExcel(aps, articleProcess);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String[]> extract() throws IOException {

        //从tsv获取数据
//        aps.setPrimaryProperties(PropertiesFactory.getProperties("emotionAndGradeTsv"));
//        Object label = aps.getPrimaryProperties().getLabel();
//        Object titleHeader = aps.getPrimaryProperties().getTitle();
//        Object content = aps.getPrimaryProperties().getContent();
//        DealFileWay dealFileWay = new GeneralCsvDeal(label, titleHeader, content);
//        ArticleProcess articleProcess = new EmotionAndGradeLabel7MappingProcess(new KeywordProcess());
//        ((GeneralCsvDeal) dealFileWay).setSeparator('\t');
//        ((GeneralCsvDeal) dealFileWay).setArticleProcess(articleProcess);
//        ((GeneralCsvDeal) dealFileWay).setAps(aps);


//        aps.setPrimaryProperties(PropertiesFactory.getProperties("emotionAndGradeExcel"));
//        Object[] labelHeaders = aps.getPrimaryProperties().getLabels();
//        Object titleHeader = aps.getPrimaryProperties().getTitle();
//        Object contentHeader = aps.getPrimaryProperties().getContent();
//
//        ArticleProcess articleProcess = new EmotionAndGradeLabel7ChinaProcess(new InvalidProcess());
//        //TODO change 目前构建Excel对象
////        DealFileWay dealFileWay = new EmotionAndGradeDeal(labelHeaders, titleHeader, contentHeader);
////        ((EmotionAndGradeDeal) dealFileWay).setArticleProcess(articleProcess);
////        ((EmotionAndGradeDeal) dealFileWay).setAps(aps);
//
//        DealFileWay dealFileWay = new EmotionAndGradeExcelDeal(labelHeaders, titleHeader, contentHeader);
//        ((EmotionAndGradeExcelDeal) dealFileWay).setArticleProcess(articleProcess);
//        ((EmotionAndGradeExcelDeal) dealFileWay).setAps(aps);
//
//        String path = aps.getPrimaryProperties().getPath();
//        DealFile2Strings dealFile2Strings = new GeneralDealAdapter(dealFileWay);
//        List<String[]> tempList = dealFile2Strings.dealFile(path);
        List<String> result = extractEmotionAndGradeExcel();
        return result.stream().map(s -> s.split("\t")).collect(Collectors.toList());
    }
}
