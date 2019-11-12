package deal.impl;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class EventKeywordExcelDealTest {

    @Test
    public void testExtractKeyword() throws IOException {
        EventKeywordExcelDeal eventKeywordExcelDeal = new EventKeywordExcelDeal();
        List<String> list = eventKeywordExcelDeal.extractedValue("E:\\文件\\工作\\AI\\bert\\原始语料\\舆情语料\\事件分类\\语料\\舆情事件分类语料提供-20180910\\102001-事件分类-资质风险.xlsx");
        System.out.println(list.size());
        for (String str : list) {
            System.out.println(str);
        }
    }

}