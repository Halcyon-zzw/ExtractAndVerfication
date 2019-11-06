package demand.general;

import config.ApplicationProperties;
import config.PropertiesFactory;
import create.CreateFileWay;
import create.impl.CreateFileProportion;
import deal.DealFileWay;
import demand.emotion_and_grade_improve.EmotionAndGradeLabel7Process;
import demand.emotion_and_grade_improve.EmotionAndGradeDeal;
import demand.general.process.KeywordProcess;
import lombok.Setter;
import pool.DealFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * 提取中介类，用于组合DealWay与ArticleProcess之间的组合
 *
 * @Author: zhuzw
 * @Date: 2019/10/30 17:10
 * @Version: 1.0
 */
public class ExtractAgent {

    @Setter
    private ApplicationProperties aps = new ApplicationProperties();
    private int[] proportions = aps.getCreateFileProporttionProperties().getProportions();

    private DealFileWay dealFileWay;

    @Setter
    private ArticleProcess articleProcess;


    public ExtractAgent() {

    }

    public ExtractAgent(ArticleProcess articleProcess, ApplicationProperties aps) {
        this.aps = aps;
        this.articleProcess = articleProcess;
    }

    public void extractGeneral() {

    }

    public void extractEmotionAndGrade() throws IOException {
        //待汇总文件路径
        String path = aps.getEmotionAndGradeProperties().getPath();

        Object labelHeader_1 = aps.getEmotionAndGradeProperties().getLabel_1();
        Object labelHeader_2 = aps.getEmotionAndGradeProperties().getLabel_2();
        Object[] labelHeaders = {labelHeader_1, labelHeader_2};
        Object titleHeader = aps.getEmotionAndGradeProperties().getTitle();
        Object contentHeader = aps.getEmotionAndGradeProperties().getContent();

//        DealFileWay dealFileWay = new EmotionAndGradeDeal(labelHeaders, titleHeader);
        dealFileWay = new EmotionAndGradeDeal(labelHeaders, titleHeader, contentHeader);

//        ((EmotionAndGradeDeal) dealFileWay).setArticleProcess(new EmotionAndGradeLabel7Process(new KeywordProcess()));
//        aps.setPrimaryProperties(PropertiesFactory.getProperties("emotionAndGrade"));
        ((EmotionAndGradeDeal) dealFileWay).setArticleProcess(articleProcess);
        ((EmotionAndGradeDeal) dealFileWay).setAps(aps);

        DealFile dealFile = new DealFile(dealFileWay);
        List<String> result = dealFile.dealFile(path);

        //生成文件
        createFile(result);

    }

    public void createFile(List<String> resultList) throws IOException {
        CreateFileWay createFileWay = new CreateFileProportion(proportions, aps.getCreateFileProporttionProperties().getPaths());
        createFileWay.createFile(resultList);
    }
}
