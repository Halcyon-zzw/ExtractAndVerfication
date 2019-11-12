package old.event_classify_2;

import com.csvreader.CsvReader;
import config.ApplicationProperties;
import deal.DealFileWay;
import deal.DealFileWayConditional;
import deal.impl.AllClassifyExcelDeal;
import deal.impl.LyricExcelDealConditional;
import old.AbstractExtractCsvValue;
import process.ColumnProcess;
import process.impl.ContentCsvProcess;
import process.impl.TitleCsvProcess;
import util.FileUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提取csv文件，事件分类数据
 *
 * @Author: zhuzw
 * @Date: 2019/8/19 10:01
 * @Version: 1.0
 */
public class EventClassifyCsvDeal implements DealFileWay {

    private ApplicationProperties aps = new ApplicationProperties();
    /**
     * 获取的数据量
     */
    private final int DATA_COUNT = aps.getEventClassifyProperties().getDataCount();

    /**
     * 提取二级事件分类数据
     */
    private AbstractExtractCsvValue extractSecondaryEvent = new EventClassifyCsvExtract();

    private ColumnProcess titleCsvProcess = new TitleCsvProcess();
    private ColumnProcess contentCsvProcess = new ContentCsvProcess();
    private ColumnProcess secondaryEventCsvProcess = new EventClassifyCsvProcess();

    /**
     * 事件分类列表
     */
    private List<String> classifyList = getClassify();

    /**
     * 从总数据中提取数据，如果不够，则从好多列表中提取数据，
     * @param path
     * @return
     * @throws IOException
     */
    @Override
    public List<String> extractedValue(String path) throws IOException {
        List<String> resultList = new ArrayList<>();
        Map<String, List<String>> secondaryHashMap = new HashMap<>();

        CsvReader csvReader = FileUtil.getCsvReader(path);

        // 逐条读取记录，直至读完
        try {
            csvReader.readHeaders();
            while (csvReader.readRecord()) {

                if (! isDeal(csvReader)){
                    continue;
                }

                //获取二级事件分类
                String secondaryEvent = csvReader.get("事件二级分类");

                if (secondaryHashMap.get(secondaryEvent) != null && secondaryHashMap.get(secondaryEvent).size() >= DATA_COUNT) {
                    //数据量够
                    continue;
                }
                if (secondaryHashMap.get(secondaryEvent) == null) {
                    //没有数据,先初始化
                    secondaryHashMap.put(secondaryEvent, new ArrayList<>());
                }
                //提取一行内容
                String rowValue = extractedRowValue(csvReader);
                //加入hashMap中
                secondaryHashMap.get(secondaryEvent).add(rowValue);
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != csvReader) {
                csvReader.close();
            }
        }

        //填补数据
        fillUp(secondaryHashMap);

        //从map中提取结果
        for (Map.Entry<String, List<String>> secondaryEvent : secondaryHashMap.entrySet()) {
            System.out.println(secondaryEvent.getKey() + ": " + secondaryEvent.getValue().size());
            //处理list,剔除数量为200以下的分类
            if (secondaryEvent.getValue().size() < 200) {
                //输出
                System.out.println(secondaryEvent.getKey());
                continue;
            }
            resultList.addAll(secondaryEvent.getValue());
        }
        System.out.println(resultList.size());
        return resultList;
    }

    private String extractedRowValue(CsvReader csvReader) {
        extractSecondaryEvent.setCsvReader(csvReader);

        return extractSecondaryEvent.extractRowValue();
    }


    /**
     * 处理string格式，解决生成tsv文件自动换行问题（内容不在一个表格中）
     *
     * @param tempString 待处理文本
     * @return 处理后文本
     */
    private String dealString(String tempString) {

        return tempString
                .replaceAll("\b","")
                .replaceAll("\f","")
                .replaceAll("\n","")
                .replaceAll("\r","")
                .replaceAll("\t","");
    }

    private boolean isDeal(CsvReader csvReader) throws IOException {
        return titleCsvProcess.isProcess(csvReader)
                && contentCsvProcess.isProcess(csvReader)
                && secondaryEventCsvProcess.isProcess(csvReader);
    }




    /**
     * 填补数据
     * @param secondaryEventHashMap
     * @return
     */
    private void fillUp(Map<String, List<String>> secondaryEventHashMap) {
        //填补不够的
        for (Map.Entry<String, List<String>> secondaryEvent : secondaryEventHashMap.entrySet()) {
            if (secondaryEvent.getValue().size() < DATA_COUNT) {
                String fileKey = secondaryEvent.getKey();
                int count = DATA_COUNT - secondaryEvent.getValue().size();
                //数据量不够,从xxx文件中提取xx条数据
                fillFromFile(secondaryEventHashMap, fileKey, count);
            }
        }

        //填补大文件中不存在的
        for (String s : classifyList) {
            int num = 0;
            for (Map.Entry<String, List<String>> secondaryEvent : secondaryEventHashMap.entrySet()) {
                if (s.contains(secondaryEvent.getKey())) {
                    break;
                }
                num++;
            }
            if (num == secondaryEventHashMap.size()) {
                String fileKey = s.split("-")[0];
                //键值对初始化
                secondaryEventHashMap.put(fileKey, new ArrayList<>());
                fillFromFile(secondaryEventHashMap, fileKey, DATA_COUNT);
            }
        }

    }

    /**a
     * 通过关键子获取映射文件名
     * @param key
     * @return
     */
    private String getFileNameMapping(String key) {
        for (String s : classifyList) {
            if (s.contains(key)) {
                return s;
            }
        }
        return null;
    }

    /**
     * 从fileName(excel)文件中添加count条数据到事件分类列表中
     * @param secondaryEventHashMap
     * @param fileKey 文件名映射键(key)
     * @param count 数据量
     */
    private void fillFromFile(Map<String, List<String>> secondaryEventHashMap, String fileKey, int count) {
        String prefixPath = aps.getBaseProperties().getOriginalPath() + "债券舆情语料\\事件分类\\舆情事件分类语料提供-20180910\\";
        String suffix = ".xlsx";
        String fileName = getFileNameMapping(fileKey);
        String filePath = prefixPath + fileName + suffix;

        DealFileWayConditional lyricExcelDealConditional = new LyricExcelDealConditional();
        List<String> secondaryEventList = lyricExcelDealConditional.extractedValue(filePath, fileKey, count);
        if (null != secondaryEventList) {
            secondaryEventHashMap.get(fileKey).addAll(secondaryEventList);
        }
    }

    /**
     * 获取事件分类列表
     * @return
     * @throws IOException
     */
    private List<String> getClassify() {
        /**
         * 事件分类文件路径
         */
        String classifyExcelPath = aps.getBaseProperties().getTrainPath() + "事件二级分类.xlsx";

        List<String> eventClassifyList = new ArrayList<>();
        DealFileWay allClassifyExcelDeal = new AllClassifyExcelDeal();
        try {
            eventClassifyList = allClassifyExcelDeal.extractedValue(classifyExcelPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return eventClassifyList;

    }
}
