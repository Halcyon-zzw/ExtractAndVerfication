package bert.extract;

import bert.deal_file.DealFile2Strings;
import bert.deal_file.GeneralDealAdapter;
import config.ApplicationProperties;
import config.PropertiesFactory;
import deal.DealFileWay;
import demand.emotion_and_grade_improve.EmotionAndGradeDeal;
import demand.emotion_and_grade_improve.EmotionAndGradeExcelDeal;
import demand.emotion_and_grade_improve.EmotionAndGradeLabel7ChinaProcess;
import demand.general.ArticleProcess;
import demand.general.GeneralCsvDeal;
import demand.general.process.KeywordProcess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public List<String[]> extract() throws IOException {
//        String path = aps.getEmotionAndGradeTestProperties().getPath();
//        Object label = aps.getEmotionAndGradeTestProperties().getLabel();
//        Object titleHeader = aps.getEmotionAndGradeTestProperties().getTitle();
//        Object content = -1;
//        DealFileWay dealFileWay = new GeneralCsvDeal(label, titleHeader, content);
//        ((GeneralCsvDeal) dealFileWay).setSeparator('\t');
//        aps.setPrimaryProperties(aps.getEmotionAndGradeTestProperties());
//        ((GeneralCsvDeal) dealFileWay).setAps(aps);
        aps.setPrimaryProperties(PropertiesFactory.getProperties("emotionAndGradeExcel"));
        Object labelHeader_1 = aps.getEmotionAndGradeExcelProperties().getLabel_1();
        Object labelHeader_2 = aps.getEmotionAndGradeExcelProperties().getLabel_2();
        Object[] labelHeaders = {labelHeader_1, labelHeader_2};
        Object titleHeader = aps.getPrimaryProperties().getTitle();
        Object contentHeader = aps.getPrimaryProperties().getContent();

        //TODO change 目前构建Excel对象
//        dealFileWay = new EmotionAndGradeDeal(labelHeaders, titleHeader, contentHeader);
//        ((EmotionAndGradeDeal) dealFileWay).setArticleProcess(articleProcess);
//        ((EmotionAndGradeDeal) dealFileWay).setAps(aps);
        DealFileWay dealFileWay = new EmotionAndGradeExcelDeal(labelHeaders, titleHeader, contentHeader);
        ArticleProcess articleProcess = new EmotionAndGradeLabel7ChinaProcess(new KeywordProcess());
        ((EmotionAndGradeExcelDeal) dealFileWay).setArticleProcess(articleProcess);

        ((EmotionAndGradeExcelDeal) dealFileWay).setAps(aps);

        String path = aps.getPrimaryProperties().getPath();
        DealFile2Strings dealFile2Strings = new GeneralDealAdapter(dealFileWay);
        List<String[]> tempList = dealFile2Strings.dealFile(path);

        return tempList;
    }
}
