package demand.general;

import com.csvreader.CsvReader;
import config.ApplicationProperties;
import deal.DealFileWay;
import util.FileUtil;
import util.StringsUtilCustomize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 常规数据处理,
 * 使用于直接提取数据，label不需要计算,
 * 仅提取三列label、title、content；
 * 需扩展重写extract方法
 *
 * @Author: zhuzw
 * @Date: 2019/8/28 19:08
 * @Version: 1.0
 * @Version 2.0
 * 添加构造函数，外部设置提取的目标，解决多次调用问题。运行时决定调用对象
 */
public class GeneralCsvDeal implements DealFileWay {

    /**
     * 变量
     */
    private final ApplicationProperties aps = new ApplicationProperties();

    /**
     * 行数据处理
     */
    private RowValueProcess rowValueProcess = new RowValueProcess();

    /**
     * 数量控制
     */
    private CountControl countControl = new CountControl();

    /**
     * 处理的数量
     */
    private int remindCount = 0;

    private Object labelHeader;
    private Object titleHeader;
    private Object contentHeader;

    public GeneralCsvDeal(Object labelHeader, Object titleHeader, Object contentHeader) {
        this.labelHeader = labelHeader;
        this.titleHeader = titleHeader;
        this.contentHeader = contentHeader;
    }

    /**
     * 从文件中提取数据
     *
     * @param csvPath 文件路径
     * @return 舆情情感\t标题\t内容
     */
    @Override
    public List<String> extractedValue(String csvPath) {
        List<String> resultList = new ArrayList<>();

        HashMap<String, List<String>> tempResultMap = new HashMap<>();

        //获取csvRead
        CsvReader csvReader = FileUtil.getCsvReader(csvPath);
        System.out.println("开始处理...");
        // 逐条读取记录，直至读完
        try {
            if (haveHeader()) {
                csvReader.readHeaders();
            }
            while (csvReader.readRecord()) {

                //从文件中获取值
                String label = "";
                String title = "";
                String content = "";
                if (haveHeader()) {
                    label = csvReader.get((String)labelHeader);
                    title = csvReader.get((String) titleHeader);
                    content = csvReader.get((String) contentHeader);
                } else {
                    label = csvReader.get((Integer) labelHeader);
                    title = csvReader.get((Integer) titleHeader);
                    content = csvReader.get((Integer) contentHeader);
                }


                //是否为空
                if (StringsUtilCustomize.isEnpty(label, title, content)) {
                    continue;
                }

                //数量控制
                int operationStatus = countControl.operationStatus(label, aps.getPrimaryProperties().getDataCount(), aps.getPrimaryProperties().getLabelNumber());
                if (-1 == operationStatus) {
                    break;
                } else if (0 == operationStatus) {
                    continue;
                }

                //提取一行数据
                String rowValue = rowValueProcess.extractRowValue(new String[]{label}, title, content);

                if (null == tempResultMap.get(label)) {
                    tempResultMap.put(label, new ArrayList<String>());
                }
                tempResultMap.get(label).add(rowValue);

                //提醒
//                remind(resultList, 10000);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != csvReader) {
                csvReader.close();
            }
        }
        System.out.println("处理完毕!");
        //提取情况,输出提取数据的情况。label: number
        List<String> extractSituation = countControl.getExtract();
        System.out.println("----------数据量情况------------");
        extractSituation.forEach(System.out::println);
        System.out.println("----------过滤数据量少的情况------------");
        extractSituation = countControl.filterLess(tempResultMap, 400);
        extractSituation.forEach(System.out::println);

//        List<String> resultList = tempResultMap.entrySet().stream()
//                .map(a -> {return a.getValue();})
//                .collect(Collectors.toList());
        //TODO 改用流操作
        for (Map.Entry<String, List<String>> temp : tempResultMap.entrySet()) {
            resultList.addAll(temp.getValue());
        }

        System.out.println("----数据量：" + resultList.size());
        return resultList;
    }

    /**
     * 处理提醒
     *
     * @param resultList
     * @param count      提醒步长
     */
    private void remind(List<String> resultList, int count) {
        if (0 == resultList.size() % count) {
            System.out.println("已处理：" + (remindCount += count));
        }
    }

    /**
     * 是否有标题
     *
     * @return
     */
    private boolean haveHeader() {

        return aps.getPrimaryProperties().isHaveHeader();
    }

}
