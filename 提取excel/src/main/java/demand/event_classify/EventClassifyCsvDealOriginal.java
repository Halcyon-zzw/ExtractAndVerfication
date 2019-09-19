package demand.event_classify;

import com.csvreader.CsvReader;
import deal.DealFileWay;
import deal.DealFileWayConditional;
import deal.DealFileWayConfitionalModify;
import extract.ExtractCsvValue;
import process.ColumnProcessCsv;
import util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 原始的提取事件分类数据
 *
 * @Author: zhuzw
 * @Date: 2019/9/9 19:12
 * @Version: 1.0
 */
public class EventClassifyCsvDealOriginal implements DealFileWayConfitionalModify {
    private final ColumnProcessCsv eventProcessCsv = new ColumnProcessCsv("事件二级分类");
    private final ColumnProcessCsv titleProcessCsv = new ColumnProcessCsv("标题");
    private final ColumnProcessCsv contentProcessCsv = new ColumnProcessCsv("内容");

    private final ExtractCsvValue extractCsvValue = new ExtractCsvValue(new String[]{"事件二级分类", "标题", "内容"});


    /**
     * 提取一行数据的值
     *
     * @param csvReader 行数据
     * @return
     */
    private String extractedRowValue(CsvReader csvReader) {
        return extractCsvValue.extracRowValue(csvReader);
    }

    /**
     * 判断是否处理该行
     *
     * @param csvReader
     */
    private boolean isDeal(CsvReader csvReader) throws IOException {
        return eventProcessCsv.isProcess(csvReader)
                && titleProcessCsv.isProcess(csvReader)
                && contentProcessCsv.isProcess(csvReader);
    }

    @Override
    public HashMap<String, List<String>> extractedValue(String csvPath, HashMap<String, Integer> conditionMap) {
        HashMap<String, List<String>> resultHashMap = new HashMap<>();
        //初始化
        for(Map.Entry<String, Integer> entry : conditionMap.entrySet()) {
            resultHashMap.put(entry.getKey(), new ArrayList<String>());
        }
        List<String> resultList = new ArrayList<>();

        //获取csvRead
        CsvReader csvReader = FileUtil.getCsvReader(csvPath);
        // 逐条读取记录，直至读完
        try {
            csvReader.readHeaders();

            while (csvReader.readRecord()) {
                //TODO 待实现，判断数量是否够
//                if (num >= count) {
//                    break;
//                }
                //是否处理
                if (!isDeal(csvReader)) {
                    continue;
                }
                String key = csvReader.get("事件二级分类");
                //TODO 考虑放在那个里面
                if(!resultHashMap.keySet().contains(key)) {
                    continue;
                }
                //数量是否够
                if (resultHashMap.get(key).size() >= conditionMap.get(key)) {
                    continue;
                }

                //提取一行数据
                String rowValue = extractedRowValue(csvReader);
                resultHashMap.get(key).add(rowValue);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != csvReader) {
                csvReader.close();
            }
        }
        return resultHashMap;
    }
}
