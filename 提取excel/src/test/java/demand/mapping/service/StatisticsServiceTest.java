package demand.mapping.service;

import demand.mapping.model.TermSimplify;
import org.junit.Before;
import org.junit.Test;
import util.FileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StatisticsServiceTest {

    StatisticsService statisticsService = new StatisticsService();
    String text = "";

    @Before
    public void init() {
        text = FileUtil.readAll("E:\\文件\\工作\\AI\\资讯分类\\test\\testText.txt").get(0);
    }

    @Test
    public void testWordFrequencyStatistics() {
        String text = FileUtil.readAll("E:\\文件\\工作\\AI\\资讯分类\\test\\testText.txt").get(0);

        Map<TermSimplify, Integer> wordFrequencyMap = statisticsService.statisticsAllWordFrequencyByFilter(text);
        for (Map.Entry<TermSimplify, Integer> termSimplifyIntegerEntry : wordFrequencyMap.entrySet()) {
            System.out.println(termSimplifyIntegerEntry.getKey().getWord()
                    + "/" + termSimplifyIntegerEntry.getKey().getNature()
                    + " -> 词频：" + termSimplifyIntegerEntry.getValue());
        }
    }


    @Test
    public void testStatisticsWordFrequencyByNatures() {
        String[] natureArr = {"n", "adj", "v"};
        List<String> natures = new ArrayList<>(Arrays.asList(natureArr));

        Map<TermSimplify, Integer> wordFrequencyMap = statisticsService.statisticsWordFrequency(text);
        for (Map.Entry<TermSimplify, Integer> termSimplifyIntegerEntry : wordFrequencyMap.entrySet()) {
            System.out.println(termSimplifyIntegerEntry.getKey().getWord()
                    + "/" + termSimplifyIntegerEntry.getKey().getNature()
                    + " -> 词频：" + termSimplifyIntegerEntry.getValue());
        }
    }

    @Test
    public void testStatisticsWordFrequencyWithFile() {
//        String text = FileUtil.readAll("E:\\文件\\工作\\AI\\资讯分类\\test\\testText.txt").get(0);

        String path = "E:\\文件\\工作\\AI\\资讯分类\\test\\all.csv";
//        String path = "E:\\文件\\工作\\AI\\资讯分类\\test\\testText.txt";
//        String path = "E:\\文件\\工作\\AI\\资讯分类\\test\\2019舆情稽核语料（7000）.xlsx";

        Map<TermSimplify, Integer> wordFrequencyMap = statisticsService.statisticsWordFrequencyWithFileByFilter(path);
        for (Map.Entry<TermSimplify, Integer> termSimplifyIntegerEntry : wordFrequencyMap.entrySet()) {
            System.out.println(termSimplifyIntegerEntry.getKey().getWord()
                    + "/" + termSimplifyIntegerEntry.getKey().getNature()
                    + " -> 词频：" + termSimplifyIntegerEntry.getValue());
        }
    }

}