package demand.mapping;

import demand.mapping.model.TermSimplify;
import org.junit.Before;
import org.junit.Test;
import util.FileUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class StatisticsControllerTest {
    private StatisticsController statisticsController = new StatisticsController();
    String text = "";

    @Before
    public void init() {
        text = FileUtil.readAll("E:\\文件\\工作\\AI\\资讯分类\\test\\testText.txt").get(0);
    }

    @Test
    public void testStatisticsWordFrequencyWithFileByFilter() {
        String path = "E:\\文件\\工作\\AI\\资讯分类\\test\\testText.txt";
//        String path = "E:\\文件\\工作\\AI\\资讯分类\\情感分类\\错误分类\\2019舆情稽核语料（7000）.xlsx";

        statisticsController.statisticsWordFrequencyWithFileByFilter(path);

//        String[] natureArr = {"a", "n"};
//        statisticsController.statisticsWordFrequencyWithFile(path, natureArr, 10);
    }

}