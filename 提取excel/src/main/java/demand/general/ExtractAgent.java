package demand.general;

import config.ApplicationProperties;
import create.CreateFileWay;
import create.adapter.CreateFileWayAdapter;
import create.impl.CreateFileProportion;
import deal.DealFileWay;
import deal.impl.EventKeywordExcelDeal;
import demand.emotion_and_grade_improve.EmotionAndGradeDeal;
import demand.emotion_and_grade_improve.EmotionAndGradeExcelDeal;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import pool.DealFile;
import util.FileUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
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

    public void extractGeneral() {

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


    public void createFile(Collection<String> resultList) throws IOException {
        CreateFileWay createFileWay = new CreateFileProportion(
                aps.getCreateFileProporttionProperties().getProportions(), aps.getCreateFileProporttionProperties().getPaths());

        CreateFileWayAdapter createFileWayAdapter = new CreateFileWayAdapter(createFileWay);
        createFileWayAdapter.createFile(resultList);

//        createFileWay.createFile(resultList);
    }


    /**
     * 从事件excel中提取关键词并汇总到 总文件中
     */
    public void appendKeywordFromEventExcel() throws IOException {
        String path = "E:\\文件\\工作\\AI\\bert\\原始语料\\舆情语料\\事件分类\\语料\\舆情事件分类语料提供-20180910\\";

        DealFileWay dealFileWay = new EventKeywordExcelDeal();
        DealFile dealFile = new DealFile(dealFileWay);
        List<String> appendList = dealFile.dealFile(path);

        List<String> keywordList = FileUtil.readAll(aps.getKeywordsPath());

        keywordList.addAll(appendList);
        List<String> resultList = keywordList.stream()
                .map(s -> s.replace(" ", ""))   //将关键词中的空格替换
                .filter(s -> !StringUtils.isEmpty(s))   //过滤null and ""
                .collect(Collectors.toSet())
                .stream().collect(Collectors.toList());

        FileUtil.createFile(resultList, aps.getKeywordsPath());
    }
}
