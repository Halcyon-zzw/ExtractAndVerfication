package demand.mapping;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MappingControllerTest {
    MappingController mappingController = new MappingController();

    long startTime;
    @Before
    public void startTime() {
        startTime = System.currentTimeMillis();
    }

    @Test
    public void testClassifyByCustomize() {
        String articlePath = "E:\\文件\\工作\\AI\\资讯分类\\情感分类\\错误分类\\2019舆情稽核语料（7000）.xlsx";
        String rulePath = "E:\\文件\\工作\\AI\\资讯分类\\情感分类\\rule\\result.tsv";
        System.out.println(mappingController.classifyByCustomize(articlePath, rulePath));
    }


    @Test
    public void testMultiClassifyByRuleWithFile() {
        String articlePath = "E:\\文件\\工作\\AI\\资讯分类\\情感分类\\错误分类\\2019舆情稽核语料（7000）.xlsx";
        System.out.println(mappingController.multiClassifyByRuleWithFile(articlePath));

    }

    @After
    public void endTime() {
        System.out.println(System.currentTimeMillis() - startTime);
    }
}