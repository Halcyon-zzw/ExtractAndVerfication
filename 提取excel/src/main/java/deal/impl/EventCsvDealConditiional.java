package deal.impl;

import deal.DealFileWay;
import deal.DealFileWayConditional;
import old.event_classify_2.EventClassifyCsvDeal;
import pool.DealFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 有条件的对事件分类提取（Csv文件）
 *
 * @Author: zhuzw
 * @Date: 2019/9/9 18:41
 * @Version: 1.0
 */
public class EventCsvDealConditiional implements DealFileWayConditional {
    @Override
    public List<String> extractedValue(String filePath, String conditionString, int count) {
        List<String> resultString = new ArrayList<>();

        DealFileWay lyricClassifyCsvDeal = new EventClassifyCsvDeal();

        DealFile dealLyricCsv = new DealFile(lyricClassifyCsvDeal);
        try {
            resultString = dealLyricCsv.dealFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultString;
    }
}
