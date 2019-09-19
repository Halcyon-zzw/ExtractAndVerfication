package demand.event_classify_2;

import com.csvreader.CsvReader;
import config.ApplicationProperties;
import deal.AbstractDealFileWay;
import deal.DealFileWay;
import deal.DealFileWayConditional;
import deal.DealFileWayConfitionalModify;
import deal.impl.AllClassifyExcelDeal;
import deal.impl.LyricExcelDealConditional;
import demand.event_classify.EventClassifyCsvDealOriginal;
import extract.AbstractExtractCsvValue;
import extract.ExtractCsvValue;
import org.springframework.beans.factory.annotation.Autowired;
import pool.DealFile;
import pool.DealFileModify;
import process.ColumnProcess;
import process.ColumnProcessCsv;
import process.impl.ContentCsvProcess;
import process.impl.TitleCsvProcess;
import util.FileUtil;

import javax.validation.valueextraction.ExtractedValue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提取事件分类数据，从事件补充分类中提取100条数据，其他数据从舆情语料中提取
 *
 * @Author: zhuzw
 * @Date: 2019/9/9 15:28
 * @Version: 1.0
 */
public class EventClassigySupplementCsvDeal implements DealFileWay {

//    @Autowired
    private ApplicationProperties aps = new ApplicationProperties();

    /**
     * 获取的数据量
     */
    private final int DATA_COUNT = aps.getEventClassifyProperties().getDataCount();

    /**
     * 从补充数据中提取的数据量
     */
    private final int DATA_COUNT_FROM_SUMMPLEMENT = 100;

    /**
     * 提取二级事件分类数据
     */
    private ExtractCsvValue extractSecondaryEvent = new ExtractCsvValue(new String[]{"5", "7", "8"});

//    private ColumnProcess titleCsvProcess = new TitleCsvProcess(false);
    private ColumnProcess contentCsvProcess = new ContentCsvProcess();
    private ColumnProcess secondaryEventCsvProcess = new EventClassifyCsvProcess();

    private ColumnProcessCsv titleProcessCsv = new ColumnProcessCsv("7");
    private ColumnProcessCsv contentProcessCsv = new ColumnProcessCsv("8");
    private ColumnProcessCsv secondaryEventProcessCsv = new ColumnProcessCsv("5");




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
                String secondaryEvent = csvReader.get(5);

                if (secondaryHashMap.get(secondaryEvent) != null && secondaryHashMap.get(secondaryEvent).size() >= DATA_COUNT_FROM_SUMMPLEMENT) {
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
//
        String labelString = "[";
        //从map中提取结果
        for (Map.Entry<String, List<String>> secondaryEvent : secondaryHashMap.entrySet()) {
            System.out.println(secondaryEvent.getKey() + ": " + secondaryEvent.getValue().size());
            //处理list,剔除数量为200以下的分类
            if (secondaryEvent.getValue().size() < 200) {
                continue;
            }
            labelString = labelString + "\"" + secondaryEvent.getKey() + "\"" + ",";
            resultList.addAll(secondaryEvent.getValue());
        }

        //输出标签列表
        labelString = labelString + "]";
        System.out.println(labelString);
        System.out.println(resultList.size());
        return resultList;
    }

    private String extractedRowValue(CsvReader csvReader) {

        return extractSecondaryEvent.extracRowValue(csvReader);
    }


    private boolean isDeal(CsvReader csvReader) throws IOException {
        boolean isDeal = titleProcessCsv.isProcess(csvReader)
                && contentProcessCsv.isProcess(csvReader)
                && secondaryEventProcessCsv.isProcess(csvReader);

        String classify = csvReader.get(5);
        //循环判断是否包含分类
        int num = 0;
        for (String temp : classifyList) {
            if (! temp.split("-")[0].equals(classify)) {
                num++;
            } else {
                break;
            }
        }
        if (num == classifyList.size()) {
            //不包含，不处理
            isDeal = false;
        }


        return isDeal;
//        return titleCsvProcess.isProcess(csvReader)
//                && contentCsvProcess.isProcess(csvReader);
//                && secondaryEventCsvProcess.isProcess(csvReader);
    }




    /**
     * 填补数据
     * @param secondaryEventHashMap
     * @return
     */
    private void fillUp(Map<String, List<String>> secondaryEventHashMap) {



        //计算需要补充的数量
        HashMap<String, Integer> supplementMap = calCategoryAndCount(secondaryEventHashMap);


        //从excel中提取数据
        supplementMap.forEach((category, count) -> {
            fillFromExcel(secondaryEventHashMap, category, count);
        });
        //更新数据
        supplementMap = calCategoryAndCount(secondaryEventHashMap);
        //从舆情语料中提取数据
        fillFromFile(secondaryEventHashMap, supplementMap);

    }

    private void fillFromExcel(Map<String, List<String>> secondaryEventHashMap, String fileKey, Integer count) {
        String prefixPath = "E:\\下载\\钉钉文件\\工作资料\\债券舆情语料\\事件分类\\舆情事件分类语料提供-20180910\\";
        String suffix = ".xlsx";
        String fileName = getFileNameMapping(fileKey);
        String filePath = prefixPath + fileName + suffix;

        DealFileWayConditional lyricExcelDealConditional = new LyricExcelDealConditional();
        List<String> secondaryEventList = lyricExcelDealConditional.extractedValue(filePath, fileKey, count);
        if (null != secondaryEventList) {
            secondaryEventHashMap.get(fileKey).addAll(secondaryEventList);
        }
    }

    private HashMap<String, Integer> calCategoryAndCount(Map<String, List<String>> secondaryEventHashMap) {
        HashMap<String, Integer> supplementMap = new HashMap<>();
        for (Map.Entry<String, List<String>> secondaryEvent : secondaryEventHashMap.entrySet()) {

            String eventKey = secondaryEvent.getKey();
            int count = DATA_COUNT - secondaryEvent.getValue().size();
            //数据量不够,提取xx条数据
            supplementMap.put(eventKey, count);
//            fillFromFile(secondaryEventHashMap, eventKey, count);

        }

        //填补map中不存在的分类
        for (String s : classifyList) {
            int num = 0;
            //遍历map
            for (Map.Entry<String, List<String>> secondaryEvent : secondaryEventHashMap.entrySet()) {
                if (s.contains(secondaryEvent.getKey())) {
                    break;
                }
                num++;
            }
            //如果num == map大小，则存在分类未加入map
            if (num == secondaryEventHashMap.size()) {
                String eventKey = s.split("-")[0];
                //键值对初始化
                secondaryEventHashMap.put(eventKey, new ArrayList<>());
//                fillFromFile(secondaryEventHashMap, fileKey, DATA_COUNT);
                supplementMap.put(eventKey, DATA_COUNT);
            }
        }
        return supplementMap;
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
     * 从fileName文件中添加count条数据到事件分类列表中
     * @param secondaryEventHashMap 总的result
     *
     */
    private void fillFromFile(Map<String, List<String>> secondaryEventHashMap, HashMap<String, Integer> supplementMap) {


        String filePath = "E:\\下载\\钉钉文件\\工作资料\\bert\\舆情语料.csv";

        DealFileWayConfitionalModify eventClassifyCsvDealOriginal = new EventClassifyCsvDealOriginal();
        List<String> secondaryEventList;
        HashMap<String, List<String>> eventListMap = eventClassifyCsvDealOriginal.extractedValue(filePath, supplementMap);
        for (Map.Entry<String, List<String>> entry: eventListMap.entrySet()) {
            if (null != entry.getValue()) {
                secondaryEventHashMap.get(entry.getKey()).addAll(entry.getValue());
            }
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
        String classifyExcelPath = "E:\\下载\\钉钉文件\\工作资料\\create\\事件分类（二级）.xlsx";

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
